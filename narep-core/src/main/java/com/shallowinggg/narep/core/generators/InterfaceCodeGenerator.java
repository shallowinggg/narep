package com.shallowinggg.narep.core.generators;

import com.shallowinggg.narep.core.common.CodeGeneratorHelper;
import com.shallowinggg.narep.core.util.StringTinyUtils;

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
        final String parentName = getParentName();
        if(StringTinyUtils.isEmpty(parentName)) {
            return CodeGeneratorHelper.buildInterfaceDeclaration(getName());
        } else {
            return CodeGeneratorHelper.buildInterfaceDeclaration(getName(), parentName);
        }
    }
}
