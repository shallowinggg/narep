package com.shallowinggg.narep.core.common;

import com.shallowinggg.narep.core.type.AnnotationMetadata;
import com.shallowinggg.narep.core.type.MethodMetadata;
import org.jetbrains.annotations.Nullable;

/**
 * @author shallowinggg
 */
public interface AnnotatedGeneratorDefinition extends GeneratorDefinition {

    /**
     * Obtain the annotation metadata (as well as basic class metadata)
     * for this bean definition's bean class.
     * @return the annotation metadata object (never {@code null})
     */
    AnnotationMetadata getMetadata();

    /**
     * Obtain metadata for this bean definition's factory method, if any.
     * @return the factory method metadata, or {@code null} if none
     * @since 4.1.1
     */
    @Nullable
    MethodMetadata getFactoryMethodMetadata();
}
