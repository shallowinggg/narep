package com.shallowinggg.narep.core.common;

import com.shallowinggg.narep.core.type.AnnotationMetadata;
import com.shallowinggg.narep.core.type.MethodMetadata;
import com.shallowinggg.narep.core.type.classreading.MetadataReader;
import com.shallowinggg.narep.core.util.ClassUtils;
import com.shallowinggg.narep.core.util.Conditions;
import org.jetbrains.annotations.Nullable;

/**
 * @author shallowinggg
 */
public class ScannedGenericGeneratorDefinition extends GenericGeneratorDefinition implements AnnotatedGeneratorDefinition {

    private final AnnotationMetadata metadata;

    /**
     * Create a new ScannedGenericBeanDefinition for the class that the
     * given MetadataReader describes.
     * @param metadataReader the MetadataReader for the scanned target class
     */
    public ScannedGenericGeneratorDefinition(MetadataReader metadataReader) {
        Conditions.notNull(metadataReader, "MetadataReader must not be null");
        this.metadata = metadataReader.getAnnotationMetadata();
        setClassName(this.metadata.getClassName());
        setClazz(ClassUtils.resolveClassName(getClassName(), null));
    }


    @Override
    public final AnnotationMetadata getMetadata() {
        return this.metadata;
    }

    @Override
    @Nullable
    public MethodMetadata getFactoryMethodMetadata() {
        return null;
    }


}
