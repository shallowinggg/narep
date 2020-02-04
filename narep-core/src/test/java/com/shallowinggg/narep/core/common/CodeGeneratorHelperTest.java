package com.shallowinggg.narep.core.common;

import com.shallowinggg.narep.core.GeneratorController;
import com.shallowinggg.narep.core.JavaCodeGenerator;
import com.shallowinggg.narep.core.generators.exception.RemotingExceptionCodeGenerator;
import com.shallowinggg.narep.core.lang.FieldInfo;
import com.shallowinggg.narep.core.lang.Modifier;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static com.shallowinggg.narep.core.lang.JLSConstants.DOUBLE_LINE_SEPARATOR;

public class CodeGeneratorHelperTest {

    @Test
    public void testBuildFullQualifiedName() {
        String val = CodeGeneratorHelper.buildFullQualifiedName("com.example", "App");
        Assert.assertEquals("com.example.App", val);

        val = CodeGeneratorHelper.buildFullQualifiedName("com.example",
                "common", "App");
        Assert.assertEquals("com.example.common.App", val);
    }

    @Test
    public void testBuildInnerClassFullQualifiedName() {
        Assert.assertEquals("com.example.Outer$Inner",
                CodeGeneratorHelper.buildInnerClassFullQualifiedName("com.example.Outer", "Inner"));
    }

    @Test
    public void testBuildPackage() {
        String val = CodeGeneratorHelper.buildDefaultPackage("com.example");
        Assert.assertEquals("package com.example;" + DOUBLE_LINE_SEPARATOR, val);

        val = CodeGeneratorHelper.buildSubPackage("com.example", "common");
        Assert.assertEquals("package com.example.common;" + DOUBLE_LINE_SEPARATOR, val);
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
        FieldInfo data = new FieldInfo(Modifier.PRIVATE, "int", "name");
        String val = CodeGeneratorHelper.buildFieldsByFieldInfos(Collections.singletonList(data));
        Assert.assertEquals("    private int name;\n\n", val);
    }

    @Test
    public void testBuildGetterAndSetterMethods() {
        FieldInfo data = new FieldInfo(Modifier.PRIVATE, "int", "name");
        String val = CodeGeneratorHelper.buildGetterAndSetterMethods(Collections.singletonList(data));
        Assert.assertEquals("    public void setName(int name) {\n"
                + "        this.name = name;\n"
                + "    }\n\n"
                + "    public int getName() {\n"
                + "        return name;\n"
                + "    }\n\n", val);

        FieldInfo booleanData = new FieldInfo(Modifier.PRIVATE, "boolean", "compress");
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
        FieldInfo name = new FieldInfo(Modifier.PRIVATE, "int", "name");
        FieldInfo compress = new FieldInfo(Modifier.PRIVATE, "boolean", "compress");
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
        FieldInfo name = new FieldInfo(Modifier.PRIVATE, "int", "name");
        FieldInfo compress = new FieldInfo(Modifier.PRIVATE, "boolean", "compress");
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
        FieldInfo name = new FieldInfo(Modifier.PRIVATE, "int", "name");
        String val = CodeGeneratorHelper.buildSetterMethod(name);
        Assert.assertEquals("    public void setName(int name) {\n" +
                "        this.name = name;\n" +
                "    }\n\n", val);
    }

    @Test
    public void testBuildGetterMethod() {
        FieldInfo name = new FieldInfo(Modifier.PRIVATE, "int", "name");
        String val = CodeGeneratorHelper.buildGetterMethod(name);
        Assert.assertEquals("    public int getName() {\n" +
                "        return name;\n" +
                "    }\n\n", val);
    }

    @Test
    public void testBuildToStringMethod() {
        FieldInfo name = new FieldInfo(Modifier.PRIVATE, "int", "name");
        FieldInfo compress = new FieldInfo(Modifier.PRIVATE, "boolean", "compress");
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
