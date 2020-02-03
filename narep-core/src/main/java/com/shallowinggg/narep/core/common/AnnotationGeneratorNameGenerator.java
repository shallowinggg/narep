package com.shallowinggg.narep.core.common;

import com.shallowinggg.narep.core.CodeGenerator;

public class AnnotationGeneratorNameGenerator implements GeneratorNameGenerator {
    private static final String SUFFIX = ".java";
    private static final int SUFFIX_LENGTH = SUFFIX.length();

    private static final AnnotationGeneratorNameGenerator INSTANCE = new AnnotationGeneratorNameGenerator();

    private AnnotationGeneratorNameGenerator() {
    }

    public static AnnotationGeneratorNameGenerator getInstance() {
        return INSTANCE;
    }

    @Override
    public String generateGeneratorName(GeneratorDefinition definition, CodeGenerator generator) {
        String fileName = generator.fileName();
        if(fileName.endsWith(SUFFIX)) {
            return fileName.substring(0, fileName.length() - SUFFIX_LENGTH);
        }

        return fileName;
    }
}
