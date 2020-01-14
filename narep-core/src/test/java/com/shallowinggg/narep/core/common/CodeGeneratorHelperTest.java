package com.shallowinggg.narep.core.common;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

import static com.shallowinggg.narep.core.common.JLSConstants.DOUBLE_LINE_SEPARATOR;
import static com.shallowinggg.narep.core.common.JLSConstants.LINE_SEPARATOR;

public class CodeGeneratorHelperTest {

    @Test
    public void testBuildFullQualifiedName() {
        String val = CodeGeneratorHelper.buildFullQualifiedName("com.example", "App");
        Assert.assertEquals(val, "com.example.App");

        val = CodeGeneratorHelper.buildFullQualifiedName("com.example",
                "common", "App");
        Assert.assertEquals(val, "com.example.common.App");
    }

    @Test
    public void testBuildPackage() {
        String val = CodeGeneratorHelper.buildDefaultPackage("com.example");
        Assert.assertEquals(val, "package com.example;" + DOUBLE_LINE_SEPARATOR);

        val = CodeGeneratorHelper.buildSubPackage("com.example", "common");
        Assert.assertEquals(val, "package com.example.common;"  + DOUBLE_LINE_SEPARATOR);
    }

    @Test
    public void testBuildInterfaceDeclaration() {
        String val = CodeGeneratorHelper.buildInterfaceDeclaration("CodeGenerator");
        Assert.assertEquals(val, "public interface CodeGenerator {" + LINE_SEPARATOR);

        val = CodeGeneratorHelper.buildInterfaceDeclaration("JavaCodeGenerator", "CodeGenerator");
        Assert.assertEquals(val, "public interface JavaCodeGenerator extends CodeGenerator {" + LINE_SEPARATOR);
    }

    @Test
    public void testBuildClassDeclaration() {
        String val = CodeGeneratorHelper.buildClassDeclaration("ClassCodeGenerator");
        Assert.assertEquals(val, "public class ClassCodeGenerator {" + LINE_SEPARATOR);

        val = CodeGeneratorHelper.buildClassDeclaration("PairCodeGenerator", "ClassCodeGenerator");
        Assert.assertEquals(val, "public class PairCodeGenerator extends ClassCodeGenerator {" + LINE_SEPARATOR);
    }

    @Test
    public void testBuildEnumDeclaration() {
        String val = CodeGeneratorHelper.buildEnumDeclaration("TlsMode");
        Assert.assertEquals(val, "public enum TlsMode {" + LINE_SEPARATOR);
    }

    @Test
    public void testBuildGenericClassDeclaration() {
        String val = CodeGeneratorHelper.buildGenericClassDeclaration("Pair", Arrays.asList("T1", "T2"));
        Assert.assertEquals(val, "public class Pair<T1, T2> {" + LINE_SEPARATOR);
    }
}
