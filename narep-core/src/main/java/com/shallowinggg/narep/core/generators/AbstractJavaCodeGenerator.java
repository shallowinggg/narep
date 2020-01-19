package com.shallowinggg.narep.core.generators;

import com.shallowinggg.narep.core.DependencyResolver;
import com.shallowinggg.narep.core.JavaCodeGenerator;
import com.shallowinggg.narep.core.common.CodeGeneratorHelper;
import com.shallowinggg.narep.core.common.ConfigInfos;
import com.shallowinggg.narep.core.lang.Modifier;
import com.shallowinggg.narep.core.util.CollectionUtils;
import com.shallowinggg.narep.core.util.FileUtils;
import com.shallowinggg.narep.core.util.StringTinyUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static com.shallowinggg.narep.core.lang.JLSConstants.FILE_SEPARATOR;
import static com.shallowinggg.narep.core.lang.JLSConstants.LINE_SEPARATOR;

/**
 * @author shallowinggg
 */
public abstract class AbstractJavaCodeGenerator implements JavaCodeGenerator {
    private static final String NON_PARENT = "";
    private static final String NON_SUB_PACKAGE = "";
    private static final List<String> NON_DEPENDENCIES = Collections.emptyList();

    private Modifier modifier = Modifier.PUBLIC;
    private String name;
    private String parentName;

    private String subPackageName;
    private List<String> dependenciesName;
    private List<JavaCodeGenerator> dependencies;
    private List<JavaCodeGenerator> innerClass;

    public AbstractJavaCodeGenerator(String name) {
        this(name, NON_PARENT);
    }

    public AbstractJavaCodeGenerator(Modifier modifier, String name) {
        this.modifier = modifier;
        this.name = name;
    }

    public AbstractJavaCodeGenerator(String name, List<String> dependenciesName) {
        this.name = name;
        this.dependenciesName = dependenciesName;
    }

    public AbstractJavaCodeGenerator(String name, String parentName) {
        this(name, parentName, NON_SUB_PACKAGE);
    }

    public AbstractJavaCodeGenerator(String name, String parentName, String subPackageName) {
        this(name, parentName, subPackageName, NON_DEPENDENCIES);
    }

    public AbstractJavaCodeGenerator(String name, String parentName, String subPackageName,
                                     List<String> dependenciesName) {
        this(name, parentName, subPackageName, dependenciesName, null);
    }

    public AbstractJavaCodeGenerator(String name, String parentName, String subPackageName,
                                     List<String> dependenciesName, List<JavaCodeGenerator> innerClass) {
        this.name = name;
        this.parentName = parentName;
        this.subPackageName = subPackageName;
        this.dependenciesName = dependenciesName;
        this.innerClass = innerClass;
    }

    public AbstractJavaCodeGenerator(Modifier modifier, String name, String parentName, String subPackageName,
                                     List<String> dependenciesName, List<JavaCodeGenerator> innerClass) {
        this.modifier = modifier;
        this.name = name;
        this.parentName = parentName;
        this.subPackageName = subPackageName;
        this.dependenciesName = dependenciesName;
        this.innerClass = innerClass;
    }

    @Override
    public String fileName() {
        return name + JavaCodeGenerator.EXTENSION;
    }

    @Override
    public String fullQualifiedName() {
        String basePackage = ConfigInfos.getInstance().basePackage();
        if (StringTinyUtils.isEmpty(subPackageName)) {
            return CodeGeneratorHelper.buildFullQualifiedName(basePackage, name);
        } else {
            return CodeGeneratorHelper.buildFullQualifiedName(basePackage, subPackageName, name);
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
        String basePackage = ConfigInfos.getInstance().basePackage();
        if (StringTinyUtils.isEmpty(subPackageName)) {
            return CodeGeneratorHelper.buildDefaultPackage(basePackage);
        } else {
            return CodeGeneratorHelper.buildSubPackage(basePackage, subPackageName);
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
    public String buildInnerClass() {
        return null;
    }

    @Override
    public void write() throws IOException {
        String storePath = ConfigInfos.getInstance().storeLocation() + FILE_SEPARATOR + relativeFilePath();
        String content = openSourceLicense() +
                buildPackage() +
                buildImports() +
                buildClassComment() +
                buildDeclaration() +
                buildFields() +
                buildMethods() +
                JavaCodeGenerator.END_OF_CLASS;
        FileUtils.writeFile(storePath, content, JavaCodeGenerator.DEFAULT_CHARSET);
    }

    @Override
    public boolean resolveDependencies(DependencyResolver resolver) {
        List<JavaCodeGenerator> result = resolver.resolve(dependenciesName);
        if (CollectionUtils.isNotEmpty(result)) {
            this.dependencies = result;
            return true;
        }
        return false;
    }

    private String relativeFilePath() {
        return fullQualifiedName().replace('.', FILE_SEPARATOR) + JavaCodeGenerator.EXTENSION;
    }

    public void setDependenciesName(List<String> dependenciesName) {
        this.dependenciesName = dependenciesName;
    }

    public void setInnerClass(List<JavaCodeGenerator> innerClass) {
        this.innerClass = innerClass;
    }

    public Modifier getModifier() {
        return modifier;
    }

    public String getName() {
        return name;
    }

    public String getParentName() {
        return parentName;
    }

    public List<JavaCodeGenerator> getDependencies() {
        return dependencies;
    }
}
