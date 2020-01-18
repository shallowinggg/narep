package com.shallowinggg.narep.core.common;

import com.shallowinggg.narep.core.GeneratorController;
import com.shallowinggg.narep.core.JavaCodeGenerator;
import com.shallowinggg.narep.core.generators.exception.RemotingExceptionCodeGenerator;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

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
        Assert.assertEquals(val, "package com.example.common;" + DOUBLE_LINE_SEPARATOR);
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

    @Test
    public void testBuildLoggerField() {
        ConfigManager manager = ConfigManager.getInstance();
        LogConfig config = new LogConfig();
        manager.register(LogConfig.CONFIG_NAME, config);
        ConfigInfos configInfos = ConfigInfos.getInstance();
        configInfos.init();

        String val = CodeGeneratorHelper.buildLoggerField("App");
        Assert.assertEquals("LogManager.getLogger(App.class)", val);
    }

    @Test
    public void testBuildCustomLoggerField() {
        ConfigManager manager = ConfigManager.getInstance();
        LogConfig config = new LogConfig();
        config.setUseCustomLoggerName(true);
        manager.register(LogConfig.CONFIG_NAME, config);
        ConfigInfos configInfos = ConfigInfos.getInstance();
        configInfos.init();

        String val = CodeGeneratorHelper.buildLoggerField("App");
        Assert.assertEquals("LogManager.getLogger(RemotingHelper.REMOTING_LOGGER_NAME)", val);
    }

    @Test
    public void testBuildFieldsByMetaData() {
        FieldMetaData data = new FieldMetaData(FieldMetaData.Modifier.PRIVATE, "int", "name");
        String val = CodeGeneratorHelper.buildFieldsByMetaData(Collections.singletonList(data));
        Assert.assertEquals("    private int name;\n", val);
    }

    @Test
    public void testBuildGetterAndSetterMethods() {
        FieldMetaData data = new FieldMetaData(FieldMetaData.Modifier.PRIVATE, "int", "name");
        String val = CodeGeneratorHelper.buildGetterAndSetterMethods(Collections.singletonList(data));
        Assert.assertEquals("    public void setName(int name) {\n"
                + "        this.name = name;\n"
                + "    }\n\n"
                + "    public int getName() {\n"
                + "        return name;\n"
                + "    }\n\n", val);

        FieldMetaData booleanData = new FieldMetaData(FieldMetaData.Modifier.PRIVATE, "boolean", "compress");
        val = CodeGeneratorHelper.buildGetterAndSetterMethods(Collections.singletonList(booleanData));
        Assert.assertEquals("    public void setCompress(boolean compress) {\n"
                + "        this.compress = compress;\n"
                + "    }\n\n"
                + "    public boolean isCompress() {\n"
                + "        return compress;\n"
                + "    }\n\n", val);
    }

    @Test
    public void testBuildSetterMethods() {
        FieldMetaData name = new FieldMetaData(FieldMetaData.Modifier.PRIVATE, "int", "name");
        FieldMetaData compress = new FieldMetaData(FieldMetaData.Modifier.PRIVATE, "boolean", "compress");
        StringBuilder builder = new StringBuilder();
        CodeGeneratorHelper.buildSetterMethods(builder, Arrays.asList(name, compress));
        Assert.assertEquals("    public void setName(int name) {\n" +
                "        this.name = name;\n" +
                "    }\n\n" +
                "    public void setCompress(boolean compress) {\n" +
                "        this.compress = compress;\n" +
                "    }\n\n", builder.toString());
    }

    @Test
    public void testBuildGetterMethods() {
        FieldMetaData name = new FieldMetaData(FieldMetaData.Modifier.PRIVATE, "int", "name");
        FieldMetaData compress = new FieldMetaData(FieldMetaData.Modifier.PRIVATE, "boolean", "compress");
        StringBuilder builder = new StringBuilder();
        CodeGeneratorHelper.buildGetterMethods(builder, Arrays.asList(name, compress));
        Assert.assertEquals("    public int getName() {\n" +
                "        return name;\n" +
                "    }\n\n" +
                "    public boolean isCompress() {\n" +
                "        return compress;\n" +
                "    }\n\n", builder.toString());
    }

    @Test
    public void testBuildSetterMethod() {
        FieldMetaData name = new FieldMetaData(FieldMetaData.Modifier.PRIVATE, "int", "name");
        String val = CodeGeneratorHelper.buildSetterMethod(name);
        Assert.assertEquals("    public void setName(int name) {\n" +
                "        this.name = name;\n" +
                "    }\n\n", val);
    }

    @Test
    public void testBuildGetterMethod() {
        FieldMetaData name = new FieldMetaData(FieldMetaData.Modifier.PRIVATE, "int", "name");
        String val = CodeGeneratorHelper.buildGetterMethod(name);
        Assert.assertEquals("    public int getName() {\n" +
                "        return name;\n" +
                "    }\n\n", val);
    }

    @Test
    public void testBuildToStringMethod() {
        FieldMetaData name = new FieldMetaData(FieldMetaData.Modifier.PRIVATE, "int", "name");
        FieldMetaData compress = new FieldMetaData(FieldMetaData.Modifier.PRIVATE, "boolean", "compress");
        String val = CodeGeneratorHelper.buildToStringMethod("App", Arrays.asList(name, compress));
        Assert.assertEquals("    @Override\n" +
                "    public String toString() {\n" +
                "        return \"App [name=\" + name\n" +
                "                + \", compress=\" + compress\n" +
                "                + \"]\";\n" +
                "    }\n\n", val);
    }

    @Test
    public void testBuildDependencyImports() {
        GeneratorController controller = new GeneratorController();
        controller.init();
        JavaCodeGenerator generator = new RemotingExceptionCodeGenerator();
        StringBuilder builder = new StringBuilder();
        CodeGeneratorHelper.buildDependencyImports(builder, Collections.singletonList(generator));
        Assert.assertEquals("import com.example.remoting.exception.RemotingException;\n", builder.toString());
    }

    @Test
    public void testBuildStaticImports() {
        GeneratorController controller = new GeneratorController();
        controller.init();
        JavaCodeGenerator generator = new RemotingExceptionCodeGenerator();
        StringBuilder builder = new StringBuilder();
        CodeGeneratorHelper.buildStaticImports(builder, Collections.singletonList(generator));
        Assert.assertEquals("import static com.example.remoting.exception.RemotingException.*;\n", builder.toString());
    }

}
