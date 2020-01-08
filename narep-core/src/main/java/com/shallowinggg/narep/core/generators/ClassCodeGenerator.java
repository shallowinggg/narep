package com.shallowinggg.narep.core.generators;

import com.shallowinggg.narep.core.common.CodeGeneratorHelper;
import com.shallowinggg.narep.core.common.GeneratorConfig;
import com.shallowinggg.narep.core.util.StringTinyUtils;

/**
 * 类文件代码生成器
 *
 * @author shallowinggg
 */
public class ClassCodeGenerator extends AbstractJavaCodeGenerator {

    public ClassCodeGenerator(String name, GeneratorConfig config) {
        super(name, config);
    }

    public ClassCodeGenerator(String name, GeneratorConfig config, String parentName) {
        super(name, config, parentName);
    }

    public ClassCodeGenerator(String name, GeneratorConfig config, String parentName, String subPackageName) {
        super(name, config, parentName, subPackageName);
    }

    @Override
    public String buildName() {
        final String parentName = getParentName();
        if(StringTinyUtils.isEmpty(parentName)) {
            return CodeGeneratorHelper.buildClassDeclaration(getName());
        } else {
            return CodeGeneratorHelper.buildClassDeclaration(getName(), parentName);
        }
    }
}
