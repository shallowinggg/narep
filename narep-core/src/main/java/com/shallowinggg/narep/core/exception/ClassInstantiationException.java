package com.shallowinggg.narep.core.exception;

/**
 * @author shallowinggg
 */
public class ClassInstantiationException extends RuntimeException {

    public ClassInstantiationException(String msg) {
        super(msg);
    }

    public ClassInstantiationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
