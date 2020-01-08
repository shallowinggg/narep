package com.shallowinggg.narep.core.generators;

import com.shallowinggg.narep.core.common.CodeGeneratorHelper;
import com.shallowinggg.narep.core.common.GeneratorConfig;
import com.shallowinggg.narep.core.util.StringTinyUtils;

/**
 * 接口文件代码生成器
 *
 * @author shallowinggg
 */
public class InterfaceCodeGenerator extends AbstractJavaCodeGenerator {

    public InterfaceCodeGenerator(String name, GeneratorConfig config) {
        super(name, config);
    }

    public InterfaceCodeGenerator(String name, GeneratorConfig config, String parentName) {
        super(name, config, parentName);
    }

    public InterfaceCodeGenerator(String name, GeneratorConfig config, String parentName, String subPackageName) {
        super(name, config, parentName, subPackageName);
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
