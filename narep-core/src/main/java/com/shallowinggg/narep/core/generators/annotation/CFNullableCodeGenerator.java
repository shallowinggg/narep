package com.shallowinggg.narep.core.generators.annotation;

import com.shallowinggg.narep.core.annotation.Generator;
import com.shallowinggg.narep.core.annotation.Profile;
import com.shallowinggg.narep.core.generators.AnnotationCodeGenerator;

/**
 * @author shallowinggg
 */
@Generator
@Profile("default")
public class CFNullableCodeGenerator extends AnnotationCodeGenerator {
    private static final String CLASS_NAME = "CFNullable";
    private static final String SUB_PACKAGE = "annotation";

    public CFNullableCodeGenerator() {
        super(CLASS_NAME, SUB_PACKAGE);
    }

    @Override
    public String buildImports() {
        return "import java.lang.annotation.*;\n\n";
    }

    @Override
    public String buildClassComment() {
        return "@Documented\n" +
                "@Retention(RetentionPolicy.RUNTIME)\n" +
                "@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE})\n";
    }
}
