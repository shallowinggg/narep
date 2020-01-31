package com.shallowinggg.narep.admin.xml;

import org.w3c.dom.Document;

/**
 * SPI for parsing an XML document that contains Spring bean definitions.
 * Used by {@link XmlProtocolDefinitionReader} for actually parsing a DOM document.
 *
 * <p>Instantiated per document to parse: implementations can hold
 * state in instance variables during the execution of the
 * {@code registerBeanDefinitions} method &mdash; for example, global
 * settings that are defined for all bean definitions in the document.
 *
 * @author Juergen Hoeller
 * @author Rob Harrop
 * @see XmlProtocolDefinitionReader#setDocumentReaderClass
 */
public interface ProtocolDefinitionDocumentReader {

    /**
     * Read bean definitions from the given DOM document and
     * register them with the registry in the given reader context.
     * @param doc the DOM document
     * (includes the target registry and the resource being parsed)
     * @return parse result
     * @throws ProtocolDefinitionStoreException in case of parsing errors
     */
    ProtocolDefinition parseProtocolDefinition(Document doc) throws ProtocolDefinitionStoreException;

}

