package com.shallowinggg.narep.core.common;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

import static com.shallowinggg.narep.core.lang.Modifier.PUBLIC;

public class ClassDeclarationBuildStrategyTest {

    @Test
    public void testPlainInterface() {
        String val = ClassDeclarations.buildStrategy(PUBLIC, "Inter", null, null,
                null, true, false, false, false).buildDeclaration();
        Assert.assertEquals("public interface Inter {\n", val);
    }

    @Test
    public void testSubInterface() {
        String val = ClassDeclarations.buildStrategy(PUBLIC, "Sub", "Parent", null,
                null, true, false, false, false).buildDeclaration();
        Assert.assertEquals("public interface Sub extends Parent {\n", val);
    }

    @Test
    public void testPlainClass() {
        String val = ClassDeclarations.buildStrategy(PUBLIC, "Inter", null, null,
                null, false, false, false, false).buildDeclaration();
        Assert.assertEquals("public class Inter {\n", val);
    }

    @Test
    public void testSubClass() {
        String val = ClassDeclarations.buildStrategy(PUBLIC, "Sub", "Parent", null,
                null, false, false, false, false).buildDeclaration();
        Assert.assertEquals("public class Sub extends Parent {\n", val);
    }

    @Test
    public void testImplementorClass() {
        String val = ClassDeclarations.buildStrategy(PUBLIC, "ProtocolConfig", null,
                new String[] {"Config"}, null, false,
                false, false, false).buildDeclaration();
        Assert.assertEquals("public class ProtocolConfig implements Config {\n", val);
    }

    @Test
    public void testCompleteClass() {
        String val = ClassDeclarations.buildStrategy(PUBLIC, "Bus", "Car",
                new String[]{"Big"}, null, false,
                false, false, false).buildDeclaration();
        Assert.assertEquals("public class Bus extends Car implements Big {\n", val);
    }

    @Test
    public void testEnumClass() {
        String val = ClassDeclarations.buildStrategy(PUBLIC, "Bus", null,
                null, null, false,
                true, false, false).buildDeclaration();
        Assert.assertEquals("public enum Bus {\n", val);
    }

    @Test
    public void testAnnotationClass() {
        String val = ClassDeclarations.buildStrategy(PUBLIC, "Bus", null,
                null, null, false,
                false, true, false).buildDeclaration();
        Assert.assertEquals("public @interface Bus {\n", val);
    }

    @Test
    public void testPlainGenericClass() {
        String val = ClassDeclarations.buildStrategy(PUBLIC, "Pair", null,
                null, Arrays.asList("T1", "T2"), false,
                false, false, true).buildDeclaration();
        Assert.assertEquals("public class Pair<T1, T2> {\n", val);
    }

    @Test
    public void testErrorGenericClass() {
        try {
            ClassDeclarations.buildStrategy(PUBLIC, "Pair", null,
                    null, Arrays.asList("T1", "T2"), false,
                    true, false, true).buildDeclaration();
        } catch (Exception e) {
            Assert.assertEquals("enum or annotation type can't use generic", e.getMessage());
        }
    }

}
