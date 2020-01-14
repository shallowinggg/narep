package com.shallowinggg.narep.core.generators;

import com.shallowinggg.narep.core.common.ClassDeclarations;

import java.util.List;

/**
 * 接口文件代码生成器
 *
 * @author shallowinggg
 */
public class InterfaceCodeGenerator extends AbstractJavaCodeGenerator {

    public InterfaceCodeGenerator(String name) {
        super(name);
    }

    public InterfaceCodeGenerator(String name, List<String> dependenciesName) {
        super(name, dependenciesName);
    }

    public InterfaceCodeGenerator(String name, String parentName) {
        super(name, parentName);
    }

    public InterfaceCodeGenerator(String name, String parentName, String subPackageName) {
        super(name, parentName, subPackageName);
    }

    @Override
    public String buildName() {
        return ClassDeclarations.buildStrategy(getName(), getParentName(), null, null,
                true, false).buildDeclaration();
    }
}
