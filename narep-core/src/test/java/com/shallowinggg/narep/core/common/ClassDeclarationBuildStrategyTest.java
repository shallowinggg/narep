package com.shallowinggg.narep.core.common;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

import static com.shallowinggg.narep.core.lang.Modifier.PUBLIC;

public class ClassDeclarationBuildStrategyTest {

    @Test
    public void testPlainInterface() {
        String val = ClassDeclarations.buildInterfaceDecl(PUBLIC, "Inter", null);
        Assert.assertEquals("public interface Inter {\n", val);
    }

    @Test
    public void testSubInterface() {
        String val = ClassDeclarations.buildInterfaceDecl(PUBLIC, "Sub", "Parent");
        Assert.assertEquals("public interface Sub extends Parent {\n", val);
    }

    @Test
    public void testPlainClass() {
        String val = ClassDeclarations.buildClassDecl(PUBLIC, "Inter", null, null);
        Assert.assertEquals("public class Inter {\n", val);
    }

    @Test
    public void testSubClass() {
        String val = ClassDeclarations.buildClassDecl(PUBLIC, "Sub", "Parent", null);
        Assert.assertEquals("public class Sub extends Parent {\n", val);
    }

    @Test
    public void testImplementorClass() {
        String val = ClassDeclarations.buildClassDecl(PUBLIC, "ProtocolConfig", null, new String[]{"Config"});
        Assert.assertEquals("public class ProtocolConfig implements Config {\n", val);
    }

    @Test
    public void testCompleteClass() {
        String val = ClassDeclarations.buildClassDecl(PUBLIC, "Bus", "Car", new String[]{"Big"});
        Assert.assertEquals("public class Bus extends Car implements Big {\n", val);
    }

    @Test
    public void testEnumClass() {
        String val = ClassDeclarations.buildEnumDecl(PUBLIC, "Bus");
        Assert.assertEquals("public enum Bus {\n", val);
    }

    @Test
    public void testAnnotationClass() {
        String val = ClassDeclarations.buildAnnotationDecl(PUBLIC, "Bus");
        Assert.assertEquals("public @interface Bus {\n", val);
    }

    @Test
    public void testPlainGenericClass() {
        String val = ClassDeclarations.buildGenericClassDecl(PUBLIC, "Pair", null,
                null, Arrays.asList("T1", "T2"), true, false);
        Assert.assertEquals("public class Pair<T1, T2> {\n", val);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testErrorGenericClass() {
        ClassDeclarations.buildGenericClassDecl(PUBLIC, "Pair", null,
                null, Arrays.asList("T1", "T2"), false, false);

    }

}
