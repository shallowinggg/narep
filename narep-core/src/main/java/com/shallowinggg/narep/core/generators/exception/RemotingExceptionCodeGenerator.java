package com.shallowinggg.narep.core.generators.exception;

import com.shallowinggg.narep.core.generators.ClassCodeGenerator;

/**
 * @author shallowinggg
 */
public class RemotingExceptionCodeGenerator extends ClassCodeGenerator {
    private static final String CLASS_NAME = "RemotingException";
    private static final String PARENT_CLASS = "Exception";

    public RemotingExceptionCodeGenerator() {
        super(CLASS_NAME, PARENT_CLASS);
    }

    @Override
    public String buildFields() {
        return "    private static final long serialVersionUID = -5690687334570505110L;\n\n";
    }

    @Override
    public String buildMethods() {
        return "    public RemotingException(String message) {\n" +
                "        super(message);\n" +
                "    }\n" +
                "\n" +
                "    public RemotingException(String message, Throwable cause) {\n" +
                "        super(message, cause);\n" +
                "    }\n";
    }
}
