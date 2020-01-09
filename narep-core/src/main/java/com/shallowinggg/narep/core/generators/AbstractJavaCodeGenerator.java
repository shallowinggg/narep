package com.shallowinggg.narep.core.generators;

import com.shallowinggg.narep.core.JavaCodeGenerator;
import com.shallowinggg.narep.core.common.CodeGeneratorHelper;
import com.shallowinggg.narep.core.common.ConfigManager;
import com.shallowinggg.narep.core.common.GeneratorConfig;
import com.shallowinggg.narep.core.util.FileUtils;
import com.shallowinggg.narep.core.util.StringTinyUtils;

import java.io.IOException;

import static com.shallowinggg.narep.core.common.GeneratorConfig.FILE_SEPARATOR;
import static com.shallowinggg.narep.core.common.GeneratorConfig.LINE_SEPARATOR;

/**
 * @author shallowinggg
 */
public abstract class AbstractJavaCodeGenerator implements JavaCodeGenerator {
    private String name;
    private String parentName;
    private String subPackageName;

    private GeneratorConfig config = (GeneratorConfig) ConfigManager.getInstance().getConfig(GeneratorConfig.CONFIG_NAME);

    public AbstractJavaCodeGenerator(String name) {
        this(name, null);
    }

    public AbstractJavaCodeGenerator(String name, String parentName) {
        this(name, parentName, null);
    }

    public AbstractJavaCodeGenerator(String name, String parentName, String subPackageName) {
        this.name = name;
        this.parentName = parentName;
        this.subPackageName = subPackageName;
    }

    @Override
    public String fileName() {
        return fullQualifiedName().replace('.', FILE_SEPARATOR) + JavaCodeGenerator.EXTENSION;
    }

    @Override
    public String fullQualifiedName() {
        if(StringTinyUtils.isEmpty(subPackageName)) {
            return CodeGeneratorHelper.buildFullQualifiedName(config.getBasePackage(), name);
        } else {
            return CodeGeneratorHelper.buildFullQualifiedName(config.getBasePackage(), subPackageName, name);
        }
    }

    @Override
    public String openSourceLicense() {
        return "/*\n" +
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
                " */\n";
    }

    @Override
    public String buildPackage() {
        if(StringTinyUtils.isEmpty(subPackageName)) {
            return CodeGeneratorHelper.buildDefaultPackage(config.getBasePackage());
        } else {
            return CodeGeneratorHelper.buildSubPackage(config.getBasePackage(), subPackageName);
        }
    }

    @Override
    public String buildImports() {
        return LINE_SEPARATOR;
    }

    @Override
    public String buildClassComment() {
        return "/**\n" +
                " * @author auto generate\n" +
                " */\n";
    }

    @Override
    public String buildFields() {
        return LINE_SEPARATOR;
    }

    @Override
    public String buildMethods() {
        return LINE_SEPARATOR;
    }

    @Override
    public void write() throws IOException {
        String storePath = config.getStoreLocation() + FILE_SEPARATOR + fileName();
        String content = openSourceLicense() +
                buildPackage() +
                buildImports() +
                buildClassComment() +
                buildName() +
                buildFields() +
                buildMethods() +
                JavaCodeGenerator.END_OF_CLASS;
        FileUtils.writeFile(storePath, content, JavaCodeGenerator.DEFAULT_CHARSET);
    }

    public String getName() {
        return name;
    }

    public String getParentName() {
        return parentName;
    }
}
