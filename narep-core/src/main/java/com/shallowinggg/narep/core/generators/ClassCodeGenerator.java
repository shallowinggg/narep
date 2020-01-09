package com.shallowinggg.narep.core.generators;

import com.shallowinggg.narep.core.common.CodeGeneratorHelper;
import com.shallowinggg.narep.core.util.StringTinyUtils;

/**
 * 类文件代码生成器
 *
 * @author shallowinggg
 */
public class ClassCodeGenerator extends AbstractJavaCodeGenerator {

    public ClassCodeGenerator(String name) {
        super(name);
    }

    public ClassCodeGenerator(String name, String parentName) {
        super(name, parentName);
    }

    public ClassCodeGenerator(String name, String parentName, String subPackageName) {
        super(name, parentName, subPackageName);
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
