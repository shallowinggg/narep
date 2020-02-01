package com.shallowinggg.narep.core.common;

public class AnnotationGeneratorNameGenerator implements GeneratorNameGenerator {
    private static final String SUFFIX = "CodeGenerator";
    private static final int SUFFIX_LENGTH = SUFFIX.length();

    private static final AnnotationGeneratorNameGenerator INSTANCE = new AnnotationGeneratorNameGenerator();

    private AnnotationGeneratorNameGenerator() {
    }

    public static AnnotationGeneratorNameGenerator getInstance() {
        return INSTANCE;
    }

    @Override
    public String generateGeneratorName(GeneratorDefinition definition, CodeGeneratorManager registry) {
        String name = definition.getClazz().getSimpleName();
        return name.substring(0, name.length() - SUFFIX_LENGTH);
    }
}
