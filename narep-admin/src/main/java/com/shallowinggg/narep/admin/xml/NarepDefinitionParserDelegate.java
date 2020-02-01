package com.shallowinggg.narep.admin.xml;

import com.shallowinggg.narep.core.lang.ProtocolField;
import com.shallowinggg.narep.core.util.StringTinyUtils;
import com.shallowinggg.narep.core.util.Utils;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;

/**
 * @author shallowinggg
 */
public class NarepDefinitionParserDelegate {
    private static final Logger LOG = LoggerFactory.getLogger(NarepDefinitionParserDelegate.class);

    public static final String NAREP_NAMESPACE_URI = "http://www.shallowinggg.com/schema/narep";

    public static final String PACKAGE_ELEMENT = "package";

    public static final String LOCATION_ELEMENT = "location";

    public static final String LOG_ELEMENT = "log";

    public static final String PROTOCOL_ELEMENT = "protocol";

    public static final String FIELD_ELEMENT = "field";

    public static final String NAME_ATTRIBUTE = "name";

    public static final String VALUE_ATTRIBUTE = "value";

    public static final String CUSTOM_ATTRIBUTE = "custom";

    public static final String TYPE_ATTRIBUTE = "type";

    public static final String LEN_ATTRIBUTE = "len";

    public static final String TRUE_VALUE = "true";

    /**
     * help parse {@code <narep>} sub elements, avoid checking element one more times
     */
    private BitSet elements;

    /**
     * Parse the supplied {@code <narep>} element and return corresponding
     * definition.
     *
     * @param element narep declaration element
     * @return parse result
     */
    public NarepDefinition parseNarepDefinitionElement(Element element) {
        NarepDefinition definition = new NarepDefinition();
        this.elements = new BitSet(element.getChildNodes().getLength());

        parsePackageElement(element, definition);
        parseLocationElement(element, definition);
        parseLogElement(element, definition);
        parseProtocolElement(element, definition);
        return definition;
    }

    /**
     * Parse the package element underneath the given element and apply
     * to the given definition, if any.
     *
     * @param element    narep declaration element
     * @param definition narep definition
     */
    private void parsePackageElement(Element element, NarepDefinition definition) {
        NodeList nodeList = element.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); ++i) {
            Node node = nodeList.item(i);
            if (hasNotParsed(i) && isCandidateElement(node) && nodeNameEquals(node, PACKAGE_ELEMENT)) {
                Element packageElement = (Element) node;
                String name = packageElement.getAttribute(NAME_ATTRIBUTE);
                definition.setPackageName(name);
                recordParsed(i);
            }
        }
    }

    /**
     * Parse the location element underneath the given element and apply
     * to the given definition, if any.
     *
     * @param element    narep declaration element
     * @param definition narep definition
     */
    private void parseLocationElement(Element element, NarepDefinition definition) {
        NodeList nodeList = element.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); ++i) {
            Node node = nodeList.item(i);
            if (hasNotParsed(i) && isCandidateElement(node) && nodeNameEquals(node, LOCATION_ELEMENT)) {
                Element packageElement = (Element) node;
                String location = packageElement.getAttribute(VALUE_ATTRIBUTE);
                definition.setLocation(location);
                recordParsed(i);
            }
        }
    }

    /**
     * Parse the log element underneath the given element and apply
     * to the given definition, if any.
     *
     * @param element    narep declaration element
     * @param definition narep definition
     */
    private void parseLogElement(Element element, NarepDefinition definition) {
        NodeList nodeList = element.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); ++i) {
            Node node = nodeList.item(i);
            if (hasNotParsed(i) && isCandidateElement(node) && nodeNameEquals(node, LOG_ELEMENT)) {
                Element logElement = (Element) node;
                String loggerName = logElement.getAttribute(NAME_ATTRIBUTE);
                definition.setLoggerName(loggerName);
                String useCustomLogger = logElement.getAttribute(CUSTOM_ATTRIBUTE);
                definition.setUseCustomLogger(TRUE_VALUE.equals(useCustomLogger));
                recordParsed(i);
            }
        }
    }

    /**
     * Parse the protocol element underneath the given element and apply
     * to the given definition, if any.
     *
     * @param element    narep declaration element
     * @param definition narep definition
     */
    private void parseProtocolElement(Element element, NarepDefinition definition) {
        NodeList nodeList = element.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); ++i) {
            Node node = nodeList.item(i);
            if (hasNotParsed(i) && isCandidateElement(node) && nodeNameEquals(node, PROTOCOL_ELEMENT)) {
                Element protocolElement = (Element) node;
                NodeList fieldList = protocolElement.getChildNodes();
                int length = fieldList.getLength();
                List<ProtocolField> protocolFields = length == 0 ? Collections.emptyList() : new ArrayList<>(length);
                for (int j = 0; j < length; ++j) {
                    Node field = fieldList.item(j);
                    if (isCandidateElement(field) && nodeNameEquals(field, FIELD_ELEMENT)) {
                        Element fieldElement = (Element) field;
                        parseSubFieldElement(fieldElement, protocolFields);
                    }
                }
                definition.setProtocolFields(protocolFields);
                recordParsed(i);
            }
        }
    }

    /**
     * Parse the field element underneath the given element and apply
     * to the given definition, if any.
     *
     * @param element        protocol declaration element
     * @param protocolFields protocol fields defined in narep definition
     */
    private void parseSubFieldElement(Element element, List<ProtocolField> protocolFields) {
        String name = element.getAttribute(NAME_ATTRIBUTE);
        String type = element.getAttribute(TYPE_ATTRIBUTE);
        String len;
        Class<?> clazz = Utils.resolveString2Class(type);

        if (element.hasAttribute(LEN_ATTRIBUTE)) {
            len = element.getAttribute(LEN_ATTRIBUTE);
            int length = Integer.parseInt(len);
            length = checkLength(type, length, name);
            protocolFields.add(new ProtocolField(name, clazz, length));
        } else {
            int length = Utils.calcPrimitiveTypeLen(type);
            protocolFields.add(new ProtocolField(name, clazz, length));
        }
    }

    public boolean nodeNameEquals(Node node, String desiredName) {
        return desiredName.equals(node.getNodeName()) || desiredName.equals(getLocalName(node));
    }

    public String getLocalName(Node node) {
        return node.getLocalName();
    }

    /**
     * Get the namespace URI for the supplied node.
     * <p>The default implementation uses {@link Node#getNamespaceURI}.
     * Subclasses may override the default implementation to provide a
     * different namespace identification mechanism.
     *
     * @param node the node
     */
    @Nullable
    public String getNamespaceUri(Node node) {
        return node.getNamespaceURI();
    }

    /**
     * Determine whether the given URI indicates the default namespace.
     */
    public boolean isDefaultNamespace(@Nullable String namespaceUri) {
        return (StringTinyUtils.isEmpty(namespaceUri) || NAREP_NAMESPACE_URI.equals(namespaceUri));
    }

    public boolean isDefaultNamespace(Node node) {
        return isDefaultNamespace(getNamespaceUri(node));
    }

    private boolean isCandidateElement(Node node) {
        return (node instanceof Element && isDefaultNamespace(node));
    }

    private boolean hasNotParsed(int pos) {
        return !this.elements.get(pos);
    }

    private void recordParsed(int pos) {
        this.elements.set(pos);
    }

    /**
     * check custom protocol field length, if it is less than or equal
     * to default length of the type, or larger than default length of
     * the type, use default length instead.
     *
     * @param type      protocol field type
     * @param customLen field custom length
     * @param name      protocol field name
     * @return ultimate length
     */
    private int checkLength(String type, int customLen, String name) {
        int maxLen = Utils.calcPrimitiveTypeLen(type);
        int minLen = maxLen >> 1;
        if (customLen <= minLen) {
            if (LOG.isWarnEnabled()) {
                LOG.warn("protocol field {} custom length {} too small, use default length {}, please use appropriate type",
                        name, customLen, maxLen);
            }
            return maxLen;
        } else if (customLen > maxLen) {
            if (LOG.isWarnEnabled()) {
                LOG.warn("protocol field {} custom length {} too big, use default length {}, please use appropriate type",
                        name, customLen, maxLen);
            }
            return maxLen;
        } else {
            return customLen;
        }
    }

}
