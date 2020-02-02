package com.shallowinggg.narep.core.exception;

import com.shallowinggg.narep.core.CodeGenerator;

/**
 * Exception thrown when CodeGenerator write file fail.
 *
 * @author shallowinggg
 * @see CodeGenerator#write()
 */
public class FileGenerateException extends RuntimeException {
    public FileGenerateException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
