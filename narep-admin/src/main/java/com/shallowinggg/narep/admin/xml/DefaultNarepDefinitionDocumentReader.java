package com.shallowinggg.narep.admin.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author shallowinggg
 */
public class DefaultNarepDefinitionDocumentReader implements NarepDefinitionDocumentReader {

    private NarepDefinitionParserDelegate delegate = new NarepDefinitionParserDelegate();


    @Override
    public NarepDefinition parseNarepDefinition(Document doc) throws NarepDefinitionStoreException {
        Element root = doc.getDocumentElement();

        if(this.delegate.isDefaultNamespace(root)) {
            return this.delegate.parseNarepDefinitionElement(root);
        }
        return null;
    }

}
