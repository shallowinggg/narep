package com.shallowinggg.narep.core.exception;

/**
 * @author Sam Brannen
 */
public class AnnotationConfigurationException extends RuntimeException {

    /**
     * Construct a new {@code AnnotationConfigurationException} with the
     * supplied message.
     * @param message the detail message
     */
    public AnnotationConfigurationException(String message) {
        super(message);
    }

    /**
     * Construct a new {@code AnnotationConfigurationException} with the
     * supplied message and cause.
     * @param message the detail message
     * @param cause the root cause
     */
    public AnnotationConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

}
