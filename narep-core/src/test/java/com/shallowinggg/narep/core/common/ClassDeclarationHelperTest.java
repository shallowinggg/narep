package com.shallowinggg.narep.core.common;

import com.shallowinggg.narep.core.lang.Modifier;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

import static com.shallowinggg.narep.core.lang.JLSConstants.LINE_SEPARATOR;

public class ClassDeclarationHelperTest {

    @Test
    public void testBuildInterfaceDeclaration() {
        String val = ClassDeclarationHelper.buildInterfaceDeclaration(Modifier.PUBLIC, "CodeGenerator");
        Assert.assertEquals(val, "public interface CodeGenerator {" + LINE_SEPARATOR);

        val = ClassDeclarationHelper.buildInterfaceDeclaration(Modifier.PUBLIC, "JavaCodeGenerator", "CodeGenerator");
        Assert.assertEquals(val, "public interface JavaCodeGenerator extends CodeGenerator {" + LINE_SEPARATOR);
    }

    @Test
    public void testBuildClassDeclaration() {
        String val = ClassDeclarationHelper.buildClassDeclaration(Modifier.PUBLIC, "ClassCodeGenerator");
        Assert.assertEquals(val, "public class ClassCodeGenerator {" + LINE_SEPARATOR);

        val = ClassDeclarationHelper.buildClassDeclaration(Modifier.PUBLIC, "PairCodeGenerator", "ClassCodeGenerator");
        Assert.assertEquals(val, "public class PairCodeGenerator extends ClassCodeGenerator {" + LINE_SEPARATOR);
    }

    @Test
    public void testBuildEnumDeclaration() {
        String val = ClassDeclarationHelper.buildEnumDeclaration(Modifier.PUBLIC, "TlsMode");
        Assert.assertEquals(val, "public enum TlsMode {" + LINE_SEPARATOR);
    }

    @Test
    public void testBuildGenericClassDeclaration() {
        String val = ClassDeclarationHelper.buildGeneric(Arrays.asList("T1", "T2"));
        Assert.assertEquals(val, "<T1, T2>");
    }
}
