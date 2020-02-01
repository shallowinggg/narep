package com.shallowinggg.narep.admin.xml;

import com.shallowinggg.narep.core.io.EncodedResource;
import com.shallowinggg.narep.core.io.Resource;
import com.shallowinggg.narep.core.io.ResourceLoader;
import com.shallowinggg.narep.core.util.ClassUtils;
import com.shallowinggg.narep.core.util.Conditions;
import com.shallowinggg.narep.core.util.StringTinyUtils;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Document;
import org.xml.sax.*;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author shallowinggg
 */
public class XmlNarepDefinitionReader extends AbstractNarepDefinitionReader {
    /**
     * Indicates that the validation should be disabled.
     */
    public static final int VALIDATION_NONE = XmlValidationModeDetector.VALIDATION_NONE;

    /**
     * Indicates that the validation mode should be detected automatically.
     */
    public static final int VALIDATION_AUTO = XmlValidationModeDetector.VALIDATION_AUTO;

    /**
     * Indicates that DTD validation should be used.
     */
    public static final int VALIDATION_DTD = XmlValidationModeDetector.VALIDATION_DTD;

    /**
     * Indicates that XSD validation should be used.
     */
    public static final int VALIDATION_XSD = XmlValidationModeDetector.VALIDATION_XSD;

    private int validationMode = VALIDATION_AUTO;

    private boolean namespaceAware = false;

    private DocumentLoader documentLoader = new DefaultDocumentLoader();

    private EntityResolver entityResolver;

    private ErrorHandler errorHandler = new SimpleSaxErrorHandler(logger);

    private final XmlValidationModeDetector validationModeDetector = new XmlValidationModeDetector();

    private Class<? extends NarepDefinitionDocumentReader> documentReaderClass =
            DefaultNarepDefinitionDocumentReader.class;

    /**
     * Create a default XmlNarepDefinitionReader
     */
    public XmlNarepDefinitionReader() {
        super();
    }

    @Override
    public NarepDefinition loadNarepDefinition(Resource resource) throws NarepDefinitionStoreException {
        return loadNarepDefinition(new EncodedResource(resource));
    }

    public NarepDefinition loadNarepDefinition(EncodedResource encodedResource) throws NarepDefinitionStoreException {
        Conditions.notNull(encodedResource, "encodedResource must not be null");
        if (logger.isTraceEnabled()) {
            logger.trace("Loading XML protocol definition from {}", encodedResource);
        }

        try {
            try (InputStream inputStream = encodedResource.getInputStream()) {
                InputSource inputSource = new InputSource(inputStream);
                if (encodedResource.getEncoding() != null) {
                    inputSource.setEncoding(encodedResource.getEncoding());
                }
                NarepDefinition definition = doLoadNarepDefinition(inputSource, encodedResource.getResource());
                String location = definition.getLocation();
                if(StringTinyUtils.isNotEmpty(location)) {
                    location = getEnvironment().resolvePlaceholders(location);
                    definition.setLocation(location);
                }
                return definition;
            }
        } catch (IOException e) {
            throw new NarepDefinitionStoreException(
                    "IOException paring XML Document from " + encodedResource.getResource(), e);
        }
    }

    private NarepDefinition doLoadNarepDefinition(InputSource inputSource, Resource resource)
            throws NarepDefinitionStoreException {
        try {
            Document document = doLoadDocument(inputSource, resource);
            return parseDocument(document, resource);
        } catch (SAXParseException ex) {
            throw new NarepDefinitionStoreException(
                    "Line " + ex.getLineNumber() + " in XML document from " + resource + " is invalid", ex);
        } catch (SAXException ex) {
            throw new NarepDefinitionStoreException(
                    "XML document from " + resource + " is invalid", ex);
        } catch (ParserConfigurationException ex) {
            throw new NarepDefinitionStoreException(
                    "Parser configuration exception parsing XML from " + resource, ex);
        } catch (IOException ex) {
            throw new NarepDefinitionStoreException(
                    "IOException parsing XML document from " + resource, ex);
        } catch (Throwable ex) {
            throw new NarepDefinitionStoreException(
                    "Unexpected exception parsing XML document from " + resource, ex);
        }
    }

    /**
     * Actually load specified document using configured DocumentLoader.
     *
     * @param inputSource the SAX InputSource to read from
     * @param resource    the resource descriptor for the xml file
     * @return the DOM Document
     * @throws Exception when thrown from DocumentLoader
     * @see DocumentLoader#loadDocument(InputSource, EntityResolver, ErrorHandler, int, boolean)
     */
    private Document doLoadDocument(InputSource inputSource, Resource resource) throws Exception {
        return this.documentLoader.loadDocument(inputSource, getEntityResolver(),
                getErrorHandler(), getValidationModeForResource(resource), namespaceAware);
    }

    private int getValidationModeForResource(Resource resource) {
        int validationModeToUse = getValidationMode();
        if (validationModeToUse != VALIDATION_AUTO) {
            return validationModeToUse;
        }
        int detectedMode = detectValidationMode(resource);
        if (detectedMode != VALIDATION_AUTO) {
            return detectedMode;
        }
        // Hmm, we didn't get a clear indication... Let's assume XSD,
        // since apparently no DTD declaration has been found up until
        // detection stopped (before finding the document's root tag).
        return VALIDATION_XSD;
    }

    private int detectValidationMode(Resource resource) {
        if (resource.isOpen()) {
            throw new NarepDefinitionStoreException(
                    "Passed-in Resource [" + resource + "] contains an open stream: " +
                            "cannot determine validation mode automatically. Either pass in a Resource " +
                            "that is able to create fresh streams, or explicitly specify the validationMode " +
                            "on your XmlNarepDefinitionReader instance.");
        }

        InputStream inputStream;
        try {
            inputStream = resource.getInputStream();
        } catch (IOException ex) {
            throw new NarepDefinitionStoreException(
                    "Unable to determine validation mode for [" + resource + "]: cannot open InputStream. " +
                            "Did you attempt to load directly from a SAX InputSource without specifying the " +
                            "validationMode on your XmlNarepDefinitionReader instance?", ex);
        }

        try {
            return this.validationModeDetector.detectValidationMode(inputStream);
        } catch (IOException ex) {
            throw new NarepDefinitionStoreException("Unable to determine validation mode for [" +
                    resource + "]: an error occurred whilst reading from the InputStream.", ex);
        }
    }

    public NarepDefinition parseDocument(Document document, Resource resource) throws NarepDefinitionStoreException {
        NarepDefinitionDocumentReader documentReader = createProtocolDefinitionDocumentReader();
        return documentReader.parseNarepDefinition(document);
    }

    protected NarepDefinitionDocumentReader createProtocolDefinitionDocumentReader() {
        return ClassUtils.instantiateClass(this.documentReaderClass);
    }

    public void setEntityResolver(@Nullable EntityResolver entityResolver) {
        this.entityResolver = entityResolver;
    }

    public EntityResolver getEntityResolver() {
        if (this.entityResolver == null) {
            ResourceLoader resourceLoader = getResourceLoader();
            if (resourceLoader != null) {
                this.entityResolver = new ResourceEntityResolver(resourceLoader);
            } else {
                this.entityResolver = new DelegatingEntityResolver(null);
            }
        }
        return this.entityResolver;
    }

    public void setErrorHandler(ErrorHandler errorHandler) {
        Conditions.notNull(errorHandler, "errorHandler must not be null");
        this.errorHandler = errorHandler;
    }

    public ErrorHandler getErrorHandler() {
        return errorHandler;
    }

    public void setDocumentReaderClass(Class<? extends NarepDefinitionDocumentReader> documentReaderClass) {
        this.documentReaderClass = documentReaderClass;
    }

    public int getValidationMode() {
        return this.validationMode;
    }

    public void setValidationMode(int validationMode) {
        this.validationMode = validationMode;
    }

    public void setNamespaceAware(boolean namespaceAware) {
        this.namespaceAware = namespaceAware;
    }
}
