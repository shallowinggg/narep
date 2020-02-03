package com.shallowinggg.narep.core.annotation;

import com.shallowinggg.narep.core.CodeGenerator;
import com.shallowinggg.narep.core.common.*;
import com.shallowinggg.narep.core.type.AnnotatedTypeMetadata;
import com.shallowinggg.narep.core.util.*;

import java.util.Set;

/**
 * @author shallowinggg
 */
public class ClassPathGeneratorScanner extends ClassPathScanningCandidateGeneratorProvider {
    private static final String DEFAULT_PROFILER = "default";

    private String profiler = DEFAULT_PROFILER;
    private CodeGeneratorManager registry = CodeGeneratorManager.getInstance();
    private GeneratorNameGenerator generatorNameGenerator = AnnotationGeneratorNameGenerator.getInstance();


    public ClassPathGeneratorScanner() {
        super(true);
    }

    public ClassPathGeneratorScanner(String profiler) {
        this(profiler, true);
    }

    public ClassPathGeneratorScanner(String profiler, boolean useDefaultFilters) {
        Conditions.hasText(profiler, "profiler must not be null");
        this.profiler = profiler;

        if (useDefaultFilters) {
            registerDefaultFilters();
        }
        setResourceLoader();
    }


    /**
     * Perform a scan within the specified base packages,
     * returning the registered bean definitions.
     * <p>This method does <i>not</i> register an annotation config processor
     * but rather leaves this up to the caller.
     *
     * @param basePackages the packages to check for annotated classes
     */
    public void doScan(String... basePackages) {
        Conditions.notEmpty(basePackages, "At least one base package must be specified");
        for (String basePackage : basePackages) {
            Set<GeneratorDefinition> candidates = findCandidateComponents(basePackage);
            for (GeneratorDefinition candidate : candidates) {
                if (candidate instanceof AnnotatedGeneratorDefinition) {
                    processCommonDefinitionAnnotations((AnnotatedGeneratorDefinition) candidate);
                }
                if (checkCandidate(candidate)) {
                    Class<?> generatorClass = candidate.getClazz();
                    CodeGenerator codeGenerator = (CodeGenerator) ClassUtils.instantiateClass(generatorClass);
                    String name = generatorNameGenerator.generateGeneratorName(candidate, codeGenerator);
                    registry.register(name, codeGenerator);
                }
            }
        }
    }

    private boolean checkCandidate(GeneratorDefinition gd) {
        if (StringTinyUtils.isNotBlank(gd.getProfile())) {
            return profiler.equals(gd.getProfile());
        }
        return true;
    }

    public static void processCommonDefinitionAnnotations(AnnotatedGeneratorDefinition abd) {
        processCommonDefinitionAnnotations(abd, abd.getMetadata());
    }

    private static void processCommonDefinitionAnnotations(AnnotatedGeneratorDefinition abd, AnnotatedTypeMetadata metadata) {
        AnnotationAttributes profile = attributesFor(metadata, Profile.class);
        if (profile != null) {
            abd.setProfile(profile.getString("value"));
        }
    }

    private static AnnotationAttributes attributesFor(AnnotatedTypeMetadata metadata, Class<?> annotationClass) {
        return attributesFor(metadata, annotationClass.getName());
    }

    static AnnotationAttributes attributesFor(AnnotatedTypeMetadata metadata, String annotationClassName) {
        return AnnotationAttributes.fromMap(metadata.getAnnotationAttributes(annotationClassName, false));
    }
}
