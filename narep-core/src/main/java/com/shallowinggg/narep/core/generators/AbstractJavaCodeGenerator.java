package com.shallowinggg.narep.core.generators;

import com.shallowinggg.narep.core.DependencyResolver;
import com.shallowinggg.narep.core.JavaCodeGenerator;
import com.shallowinggg.narep.core.common.CodeGeneratorHelper;
import com.shallowinggg.narep.core.common.CodeGeneratorManager;
import com.shallowinggg.narep.core.common.ConfigInfos;
import com.shallowinggg.narep.core.common.GeneratorConfig;
import com.shallowinggg.narep.core.lang.Modifier;
import com.shallowinggg.narep.core.util.CollectionUtils;
import com.shallowinggg.narep.core.util.FileUtils;
import com.shallowinggg.narep.core.util.StringTinyUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static com.shallowinggg.narep.core.lang.JLSConstants.FILE_SEPARATOR;
import static com.shallowinggg.narep.core.lang.JLSConstants.LINE_SEPARATOR;

/**
 * java代码文件生成器的通用骨架。
 *
 * 此类提取了类，接口，枚举以及注解的共同特性，并
 * 提供了通用的代码生成方式，子类可以继承此类以定制
 * 自己额外的特性。
 *
 * @author shallowinggg
 */
public abstract class AbstractJavaCodeGenerator implements JavaCodeGenerator {
    private static final String NON_PARENT = "";
    private static final String NON_SUB_PACKAGE = "";
    private static final List<String> NON_DEPENDENCIES = Collections.emptyList();

    /**
     * 类限定符，默认为public。
     *
     * 你可以使用此类提供的构造方法
     * {@link this#AbstractJavaCodeGenerator(Modifier, String)}
     * 或者使用{@link this#setModifier(Modifier)}
     * 来设置你想要的限定符。
     */
    private Modifier modifier = Modifier.PUBLIC;

    /**
     * 类(接口、枚举、注解)名称
     */
    private String name;

    /**
     * 父类(接口)名称
     *
     * 注意：注解以及枚举没有父类。
     */
    private String parentName;

    /**
     * 子包名称
     *
     * 通过{@link ConfigInfos#basePackage()} + subPackageName
     * 可以构造出此类所在包的完成名称。
     * 你可以使用{@link GeneratorConfig#setBasePackage(String)}方法
     * 来设置{@link ConfigInfos#basePackage()}的值。
     *
     * @see this#buildPackage()
     */
    private String subPackageName;

    /**
     * 依赖类的名称（只限于项目内的类，不包括jdk或者其他jar包中的类）
     *
     * 此字段的作用是构造import语句，因为{@link ConfigInfos#basePackage()}
     * 的值是动态的，无法静态生成。同时，此字段还可以检查所依赖的类
     * 是否存在（项目内的类需要通过生成器生成，检查对应的生成器是否
     * 被注册到{@link CodeGeneratorManager}）。
     *
     * @see this#dependencies
     */
    private List<String> dependenciesName;

    /**
     * 依赖类对应的代码生成器
     *
     * 构造生成器集合后，生成代码之前进行依赖解析，
     * 将{@link this#dependenciesName}中的值解析为
     * 对应的代码生成器，同时此步可以检查依赖类的代码
     * 生成器是否被注册。
     *
     * @see this#dependenciesName
     * @see DependencyResolver
     * @see CodeGeneratorManager
     */
    private List<JavaCodeGenerator> dependencies;

    /**
     * 内部类集合
     *
     * @see InnerClassCodeGenerator
     */
    private List<InnerClassCodeGenerator> innerClass;

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
                                     List<String> dependenciesName, List<InnerClassCodeGenerator> innerClass) {
        this.name = name;
        this.parentName = parentName;
        this.subPackageName = subPackageName;
        this.dependenciesName = dependenciesName;
        this.innerClass = innerClass;
    }

    public AbstractJavaCodeGenerator(Modifier modifier, String name, String parentName, String subPackageName,
                                     List<String> dependenciesName, List<InnerClassCodeGenerator> innerClass) {
        this.modifier = modifier;
        this.name = name;
        this.parentName = parentName;
        this.subPackageName = subPackageName;
        this.dependenciesName = dependenciesName;
        this.innerClass = innerClass;
    }

    /**
     * 生成文件的名称，默认都是[类名.java]形式。
     *
     * @return 文件名称
     */
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

    /**
     * 这个远程模块出自apache rocketMQ的remoting模块，
     * 它使用了apache的开源协议，因此生成的代码中也包含了
     * 此开源协议信息。
     */
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

    /**
     * 默认假设此类不引用任何外部类，只返回一个换行符代替。
     */
    @Override
    public String buildImports() {
        return LINE_SEPARATOR;
    }

    /**
     * 默认类注释只生成一个作者
     */
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
        if (CollectionUtils.isNotEmpty(innerClass)) {
            StringBuilder builder = new StringBuilder(1000 * innerClass.size());
            for (InnerClassCodeGenerator inner : innerClass) {
                // TODO: 作为可配置选项
                String indent = "    ";
                String content = inner.buildClassComment() +
                        inner.buildDeclaration() +
                        inner.buildFields() +
                        inner.buildMethods() +
                        inner.buildInnerClass() +
                        JavaCodeGenerator.END_OF_CLASS;
                Stream.of(content.split("\n")).forEach(s -> builder.append(indent).append(s).append("\n"));
            }
            return builder.toString();
        }
        return "";
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
                buildInnerClass() +
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

    public void setInnerClass(List<InnerClassCodeGenerator> innerClass) {
        this.innerClass = innerClass;
    }

    public void setModifier(Modifier modifier) {
        this.modifier = modifier;
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
