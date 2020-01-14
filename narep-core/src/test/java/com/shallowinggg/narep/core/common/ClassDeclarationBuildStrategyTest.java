package com.shallowinggg.narep.core.common;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class ClassDeclarationBuildStrategyTest {

    @Test
    public void testPlainInterface() {
        String val = ClassDeclarations.buildStrategy("Inter", null, null,
                null, true, false).buildDeclaration();
        Assert.assertEquals("public interface Inter {\n", val);
    }

    @Test
    public void testSubInterface() {
        String val = ClassDeclarations.buildStrategy("Sub", "Parent", null,
                null, true, false).buildDeclaration();
        Assert.assertEquals("public interface Sub extends Parent {\n", val);
    }

    @Test
    public void testPlainClass() {
        String val = ClassDeclarations.buildStrategy("Inter", null, null,
                null, false, false).buildDeclaration();
        Assert.assertEquals("public class Inter {\n", val);
    }

    @Test
    public void testSubClass() {
        String val = ClassDeclarations.buildStrategy("Sub", "Parent", null,
                null, false, false).buildDeclaration();
        Assert.assertEquals("public class Sub extends Parent {\n", val);
    }

    @Test
    public void testImplementClass() {
        String val = ClassDeclarations.buildStrategy("ProtocolConfig", null,
                new String[] {"Config"}, null, false, false).buildDeclaration();
        Assert.assertEquals("public class ProtocolConfig implements Config {\n", val);
    }

    @Test
    public void testComplexClass() {
        String val = ClassDeclarations.buildStrategy("Bus", "Car",
                new String[]{"Big"}, null, false, false).buildDeclaration();
        Assert.assertEquals("public class Bus extends Car implements Big {\n", val);
    }

    @Test
    public void testPlainGenericClass() {
        String val = ClassDeclarations.buildStrategy("Pair", null,
                null, Arrays.asList("T1", "T2"), false, true).buildDeclaration();
        Assert.assertEquals("public class Pair<T1, T2> {\n", val);
    }

}
