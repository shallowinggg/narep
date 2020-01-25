package com.shallowinggg.narep.core.common;

public class AnnotationGeneratorNameGenerator implements GeneratorNameGenerator {

    private static final AnnotationGeneratorNameGenerator INSTANCE = new AnnotationGeneratorNameGenerator();

    private AnnotationGeneratorNameGenerator() {
    }

    public static AnnotationGeneratorNameGenerator getInstance() {
        return INSTANCE;
    }

    @Override
    public String generateGeneratorName(GeneratorDefinition definition, CodeGeneratorManager registry) {
        return null;
    }
}
