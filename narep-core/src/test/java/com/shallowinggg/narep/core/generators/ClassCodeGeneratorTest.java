package com.shallowinggg.narep.core.generators;

import com.shallowinggg.narep.core.GeneratorController;
import com.shallowinggg.narep.core.JavaCodeGenerator;
import com.shallowinggg.narep.core.common.CodeGeneratorHelper;
import com.shallowinggg.narep.core.generators.common.TlsModeCodeGenerator;
import com.shallowinggg.narep.core.lang.FieldInfo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.shallowinggg.narep.core.lang.Modifier.*;

public class ClassCodeGeneratorTest {

    @Before
    public void before() {
        GeneratorController controller = new GeneratorController();
        controller.init();
    }

    @Test
    public void testUse() {
        JavaCodeGenerator app = new ClassCodeGenerator("App") {
            {
                List<FieldInfo> fields = new ArrayList<>(2);
                fields.add(new FieldInfo(PRIVATE, "String", "name"));
                fields.add(new FieldInfo(PRIVATE_FINAL, "String", "constant", "\"\""));
                setFields(fields);
            }

            @Override
            public String buildMethods() {
                return CodeGeneratorHelper.buildGetterAndSetterMethods(getFields().subList(0, 1))
                        + CodeGeneratorHelper.buildGetterMethod(getFields().get(1));
            }
        };

        String val = app.openSourceLicense() +
                app.buildPackage() +
                app.buildImports() +
                app.buildClassComment() +
                app.buildDeclaration() +
                app.buildFields() +
                app.buildMethods() +
                app.buildInnerClass() +
                JavaCodeGenerator.END_OF_CLASS;

        Assert.assertEquals("/*\n" +
                " * Licensed to the Apache Software Foundation (ASF) under one or more\n" +
                " * contributor license agreements.  See the NOTICE file distributed with\n" +
                " * this work for additional information regarding copyright ownership.\n" +
                " * The ASF licenses this file to You under the Apache License, Version 2.0\n" +
                " * (the \"License\"); you may not use this file except in compliance with\n" +
                " * the License.  You may obtain a copy of the License at\n" +
                " *\n" +
                " *     http://www.apache.org/licenses/LICENSE-2.0\n" +
                " *\n" +
                " * Unless required by applicable law or agreed to in writing, software\n" +
                " * distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                " * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                " * See the License for the specific language governing permissions and\n" +
                " * limitations under the License.\n" +
                " */\n" +
                "package com.example.remoting;\n" +
                "\n" +
                "\n" +
                "/**\n" +
                " * @author auto generate\n" +
                " */\n" +
                "public class App {\n" +
                "    private String name;\n" +
                "\n" +
                "    private final String constant = \"\";\n" +
                "\n" +
                "    public void setName(String name) {\n" +
                "        this.name = name;\n" +
                "    }\n" +
                "\n" +
                "    public String getName() {\n" +
                "        return name;\n" +
                "    }\n" +
                "\n" +
                "    public String getConstant() {\n" +
                "        return constant;\n" +
                "    }\n" +
                "\n" +
                "}", val);
    }

    @Test
    public void testInterfaceCodeGenerator() {
        JavaCodeGenerator config = new InterfaceCodeGenerator("Config", null, "util") {
            @Override
            public String buildMethods() {
                return "    void test();\n";
            }
        };

        String val = config.openSourceLicense() +
                config.buildPackage() +
                config.buildImports() +
                config.buildClassComment() +
                config.buildDeclaration() +
                config.buildFields() +
                config.buildMethods() +
                config.buildInnerClass() +
                JavaCodeGenerator.END_OF_CLASS;

        Assert.assertEquals("/*\n" +
                " * Licensed to the Apache Software Foundation (ASF) under one or more\n" +
                " * contributor license agreements.  See the NOTICE file distributed with\n" +
                " * this work for additional information regarding copyright ownership.\n" +
                " * The ASF licenses this file to You under the Apache License, Version 2.0\n" +
                " * (the \"License\"); you may not use this file except in compliance with\n" +
                " * the License.  You may obtain a copy of the License at\n" +
                " *\n" +
                " *     http://www.apache.org/licenses/LICENSE-2.0\n" +
                " *\n" +
                " * Unless required by applicable law or agreed to in writing, software\n" +
                " * distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                " * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                " * See the License for the specific language governing permissions and\n" +
                " * limitations under the License.\n" +
                " */\n" +
                "package com.example.remoting.util;\n" +
                "\n" +
                "\n" +
                "/**\n" +
                " * @author auto generate\n" +
                " */\n" +
                "public interface Config {\n" +
                "\n" +
                "    void test();\n" +
                "}", val);

    }

    @Test
    public void testEnumCodeGenerator() {
        JavaCodeGenerator tlsMode = new TlsModeCodeGenerator();
        String val = tlsMode.buildPackage() +
                tlsMode.buildImports() +
                tlsMode.buildClassComment() +
                tlsMode.buildDeclaration() +
                tlsMode.buildFields() +
                tlsMode.buildMethods() +
                tlsMode.buildInnerClass() +
                JavaCodeGenerator.END_OF_CLASS;
        Assert.assertEquals("package com.example.remoting.common;\n" +
                "\n" +
                "\n" +
                "/**\n" +
                " * For server, three SSL modes are supported: disabled, permissive and enforcing.\n" +
                " * <ol>\n" +
                " *     <li><strong>disabled:</strong> SSL is not supported; any incoming SSL handshake will be rejected, causing connection closed.</li>\n" +
                " *     <li><strong>permissive:</strong> SSL is optional, aka, server in this mode can serve client connections with or without SSL;</li>\n" +
                " *     <li><strong>enforcing:</strong> SSL is required, aka, non SSL connection will be rejected.</li>\n" +
                " * </ol>\n" +
                " */\n" +
                "public enum TlsMode {\n" +
                "    DISABLED(\"disabled\"),\n" +
                "    PERMISSIVE(\"permissive\"),\n" +
                "    ENFORCING(\"enforcing\");\n" +
                "\n" +
                "    private String name;\n" +
                "\n" +
                "    TlsMode(String name) {\n" +
                "        this.name = name;\n" +
                "    }\n" +
                "\n" +
                "    public static TlsMode parse(String mode) {\n" +
                "        for (TlsMode tlsMode : TlsMode.values()) {\n" +
                "            if (tlsMode.name.equals(mode)) {\n" +
                "                return tlsMode;\n" +
                "            }\n" +
                "        }\n" +
                "\n" +
                "        return PERMISSIVE;\n" +
                "    }\n" +
                "\n" +
                "    public String getName() {\n" +
                "        return name;\n" +
                "    }\n" +
                "}", val);
    }

    @Test
    public void testInnerClass() {
        ClassCodeGenerator outer = new ClassCodeGenerator("Outer") {
            @Override
            public String buildMethods() {
                return "    public void test() {\n" +
                        "    }\n\n";
            }
        };

        ClassCodeGenerator inner = new ClassCodeGenerator(PRIVATE_STATIC, "Inner") {
            @Override
            public String buildMethods() {
                return "    public void test2() {\n" +
                        "    }\n\n";
            }
        };

        InnerClassCodeGenerator innerClass = new InnerClassCodeGenerator(outer, inner);
        outer.setInnerClass(Collections.singletonList(innerClass));

        ClassCodeGenerator inner2 = new ClassCodeGenerator(PRIVATE_STATIC, "Inner2") {
        };

        InnerClassCodeGenerator innerClass2 = new InnerClassCodeGenerator(inner, inner2);
        inner.setInnerClass(Collections.singletonList(innerClass2));

        String val = outer.buildPackage() +
                outer.buildImports() +
                outer.buildClassComment() +
                outer.buildDeclaration() +
                outer.buildFields() +
                outer.buildMethods() +
                outer.buildInnerClass() +
                JavaCodeGenerator.END_OF_CLASS;

        Assert.assertEquals("package com.example.remoting;\n" +
                "\n" +
                "\n" +
                "/**\n" +
                " * @author auto generate\n" +
                " */\n" +
                "public class Outer {\n" +
                "\n" +
                "    public void test() {\n" +
                "    }\n" +
                "\n" +
                "    /**\n" +
                "     * @author auto generate\n" +
                "     */\n" +
                "    private static class Inner {\n" +
                "    \n" +
                "        public void test2() {\n" +
                "        }\n" +
                "    \n" +
                "        /**\n" +
                "         * @author auto generate\n" +
                "         */\n" +
                "        private static class Inner2 {\n" +
                "        \n" +
                "        \n" +
                "        }\n" +
                "    }\n" +
                "}", val);
    }
}
