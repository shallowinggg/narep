package com.shallowinggg.narep.core.generators;

import com.shallowinggg.narep.core.common.ClassDeclarations;
import com.shallowinggg.narep.core.lang.Modifier;

import java.util.List;

/**
 * @author shallowinggg
 */
public class AnnotationCodeGenerator extends AbstractJavaCodeGenerator {
    public AnnotationCodeGenerator(String name) {
        super(name);
    }

    public AnnotationCodeGenerator(Modifier modifier, String name) {
        super(modifier, name);
    }

    public AnnotationCodeGenerator(String name, List<String> dependenciesName) {
        super(name, dependenciesName);
    }

    public AnnotationCodeGenerator(String name, String subPackageName) {
        super(name, null, subPackageName);
    }

    @Override
    public String buildDeclaration() {
        return ClassDeclarations.buildStrategy(getModifier(), getName(), null, null,
                null, false, false, true, false).buildDeclaration();
    }
}
