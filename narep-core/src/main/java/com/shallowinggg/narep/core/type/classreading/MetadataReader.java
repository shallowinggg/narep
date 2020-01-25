package com.shallowinggg.narep.core.type.classreading;

import com.shallowinggg.narep.core.io.Resource;
import com.shallowinggg.narep.core.type.AnnotationMetadata;
import com.shallowinggg.narep.core.type.ClassMetadata;

/**
 * Simple facade for accessing class metadata,
 * as read by an ASM.
 *
 * @author Juergen Hoeller
 */
public interface MetadataReader {

    /**
     * Return the resource reference for the class file.
     */
    Resource getResource();

    /**
     * Read basic class metadata for the underlying class.
     */
    ClassMetadata getClassMetadata();

    /**
     * Read full annotation metadata for the underlying class,
     * including metadata for annotated methods.
     */
    AnnotationMetadata getAnnotationMetadata();

}
