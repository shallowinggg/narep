package com.shallowinggg.narep.core.generators.exception;

import com.shallowinggg.narep.core.generators.ClassCodeGenerator;

/**
 * @author shallowinggg
 */
public class RemotingTooMuchRequestExceptionCodeGenerator extends ClassCodeGenerator {
    private static final String CLASS_NAME = "RemotingTooMuchRequestException";
    private static final String PARENT_CLASS = "RemotingException";

    public RemotingTooMuchRequestExceptionCodeGenerator() {
        super(CLASS_NAME, PARENT_CLASS);
    }

    @Override
    public String buildFields() {
        return "    private static final long serialVersionUID = 4326919581254519654L;\n\n";
    }

    @Override
    public String buildMethods() {
        return "    public RemotingTooMuchRequestException(String message) {\n" +
                "        super(message);\n" +
                "    }\n";
    }
}
