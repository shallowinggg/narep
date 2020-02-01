package com.shallowinggg.narep.admin.xml;

import org.w3c.dom.Document;

/**
 * SPI for parsing an XML document that contains Spring bean definitions.
 * Used by {@link XmlNarepDefinitionReader} for actually parsing a DOM document.
 *
 * <p>Instantiated per document to parse: implementations can hold
 * state in instance variables during the execution of the
 * {@code registerBeanDefinitions} method &mdash; for example, global
 * settings that are defined for all bean definitions in the document.
 *
 * @author Juergen Hoeller
 * @author Rob Harrop
 * @see XmlNarepDefinitionReader#setDocumentReaderClass
 */
public interface NarepDefinitionDocumentReader {

    /**
     * Read bean definitions from the given DOM document and
     * register them with the registry in the given reader context.
     * @param doc the DOM document
     * (includes the target registry and the resource being parsed)
     * @return parse result
     * @throws NarepDefinitionStoreException in case of parsing errors
     */
    NarepDefinition parseNarepDefinition(Document doc) throws NarepDefinitionStoreException;

}

