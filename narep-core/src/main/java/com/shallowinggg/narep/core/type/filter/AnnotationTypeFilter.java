package com.shallowinggg.narep.core.type.filter;

import com.shallowinggg.narep.core.type.AnnotationMetadata;
import com.shallowinggg.narep.core.type.ClassMetadata;
import com.shallowinggg.narep.core.type.classreading.MetadataReader;
import com.shallowinggg.narep.core.type.classreading.MetadataReaderFactory;

import java.io.IOException;
import java.lang.annotation.Annotation;

/**
 * @author shallowinggg
 */
public class AnnotationTypeFilter implements TypeFilter {
    private final Class<? extends Annotation> annotationType;

    private final boolean considerMetaAnnotations;

    public AnnotationTypeFilter(Class<? extends Annotation> annotationType) {
        this(annotationType, true);
    }

    public AnnotationTypeFilter(Class<? extends Annotation> annotationType, boolean considerMetaAnnotations) {
        this.annotationType = annotationType;
        this.considerMetaAnnotations = considerMetaAnnotations;
    }

    @Override
    public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
        // This method optimizes avoiding unnecessary creation of ClassReaders
        // as well as visiting over those readers.
        if (matchSelf(metadataReader)) {
            return true;
        }
        ClassMetadata metadata = metadataReader.getClassMetadata();
        return matchClassName(metadata.getClassName());
    }

    protected boolean matchSelf(MetadataReader metadataReader) {
        AnnotationMetadata metadata = metadataReader.getAnnotationMetadata();
        return metadata.hasAnnotation(this.annotationType.getName()) ||
                (this.considerMetaAnnotations && metadata.hasMetaAnnotation(this.annotationType.getName()));
    }

    protected boolean matchClassName(String className) {
        return false;
    }
}
