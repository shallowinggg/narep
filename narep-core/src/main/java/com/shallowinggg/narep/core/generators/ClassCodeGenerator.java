package com.shallowinggg.narep.core.generators;

import com.shallowinggg.narep.core.common.ClassDeclarations;

import java.util.List;

/**
 * 类文件代码生成器
 *
 * @author shallowinggg
 */
public class ClassCodeGenerator extends AbstractJavaCodeGenerator {
    private String[] interfaceNames;

    public ClassCodeGenerator(String name) {
        super(name);
    }

    public ClassCodeGenerator(String name, String parentName) {
        super(name, parentName);
    }

    public ClassCodeGenerator(String name, String parentName, String[] interfaceNames) {
        super(name, parentName);
        this.interfaceNames = interfaceNames;
    }

    public ClassCodeGenerator(String name, List<String> dependenciesName) {
        super(name, dependenciesName);
    }

    public ClassCodeGenerator(String name, String parentName, String subPackageName) {
        super(name, parentName, subPackageName);
    }

    public ClassCodeGenerator(String name, String parentName, String subPackageName, String[] interfaceNames) {
        super(name, parentName, subPackageName);
        this.interfaceNames = interfaceNames;
    }

    @Override
    public String buildName() {
        return ClassDeclarations.buildStrategy(getName(), getParentName(), interfaceNames, null,
                false, false).buildDeclaration();
    }

    public void setInterfaceNames(String[] interfaceNames) {
        this.interfaceNames = interfaceNames;
    }

    public String[] getInterfaceNames() {
        return interfaceNames;
    }
}
