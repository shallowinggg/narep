package com.shallowinggg.narep.core.generators;

import com.shallowinggg.narep.core.common.ClassDeclarations;
import com.shallowinggg.narep.core.lang.Modifier;

import java.util.List;

/**
 * @author shallowinggg
 */
public class EnumCodeGenerator extends AbstractJavaCodeGenerator {
    public EnumCodeGenerator(String name) {
        super(name);
    }

    public EnumCodeGenerator(Modifier modifier, String name) {
        super(modifier, name);
    }

    public EnumCodeGenerator(String name, List<String> dependenciesName) {
        super(name, dependenciesName);
    }

    public EnumCodeGenerator(String name, String subPackageName) {
        super(name, null, subPackageName);
    }

    @Override
    public String buildDeclaration() {
        return ClassDeclarations.buildStrategy(getModifier(), getName(), null, null,
                null, false, true, false, false).buildDeclaration();
    }
}
