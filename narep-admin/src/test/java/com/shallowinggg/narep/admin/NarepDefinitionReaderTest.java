package com.shallowinggg.narep.admin;

import com.shallowinggg.narep.admin.xml.NarepDefinition;
import com.shallowinggg.narep.admin.xml.NarepDefinitionReader;
import com.shallowinggg.narep.admin.xml.XmlNarepDefinitionReader;
import org.junit.Assert;
import org.junit.Test;

public class NarepDefinitionReaderTest {

    @Test
    public void test() {
        NarepDefinitionReader reader = new XmlNarepDefinitionReader();
        NarepDefinition result = reader.loadNarepDefinition("classpath:test.xml");
        String expected = "NarepDefinition{packageName='com.example.remoting', location='";
        String userHome = System.getProperty("user.home");
        expected += userHome + "/generator', useCustomLogger=true, loggerName='Remoting', protocolFields=null}";
        Assert.assertEquals(expected, result.toString());
    }

}
