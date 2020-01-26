package com.shallowinggg.narep.core.annotation;

import com.shallowinggg.narep.core.util.ClassUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.annotation.Annotation;

public abstract class KotlinDetector {

    private static final Logger logger = LogManager.getLogger(KotlinDetector.class);

    private static final Class<? extends Annotation> kotlinMetadata;

    private static final boolean kotlinReflectPresent;

    static {
        Class<?> metadata;
        ClassLoader classLoader = KotlinDetector.class.getClassLoader();
        try {
            metadata = ClassUtils.forName("kotlin.Metadata", classLoader);
        }
        catch (ClassNotFoundException ex) {
            // Kotlin API not available - no Kotlin support
            metadata = null;
        }
        kotlinMetadata = (Class<? extends Annotation>) metadata;
        kotlinReflectPresent = ClassUtils.isPresent("kotlin.reflect.full.KClasses", classLoader);
        if (kotlinMetadata != null && !kotlinReflectPresent) {
            logger.info("Kotlin reflection implementation not found at runtime, related features won't be available.");
        }
    }


    /**
     * Determine whether Kotlin is present in general.
     */
    public static boolean isKotlinPresent() {
        return (kotlinMetadata != null);
    }

    /**
     * Determine whether Kotlin reflection is present.
     * @since 5.1
     */
    public static boolean isKotlinReflectPresent() {
        return kotlinReflectPresent;
    }

    /**
     * Determine whether the given {@code Class} is a Kotlin type
     * (with Kotlin metadata present on it).
     */
    public static boolean isKotlinType(Class<?> clazz) {
        return (kotlinMetadata != null && clazz.getDeclaredAnnotation(kotlinMetadata) != null);
    }

}
