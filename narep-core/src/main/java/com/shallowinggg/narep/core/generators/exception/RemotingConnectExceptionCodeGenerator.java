package com.shallowinggg.narep.core.generators.exception;

import com.shallowinggg.narep.core.generators.ClassCodeGenerator;

/**
 * @author shallowinggg
 */
public class RemotingConnectExceptionCodeGenerator extends ClassCodeGenerator {

    private static final String CLASS_NAME = "RemotingConnectException";
    private static final String PARENT_CLASS = "RemotingException";

    public RemotingConnectExceptionCodeGenerator() {
        super(CLASS_NAME, PARENT_CLASS);
    }

    @Override
    public String buildFields() {
        return "    private static final long serialVersionUID = -5565366231695911316L;\n\n";
    }

    @Override
    public String buildMethods() {
        return "    public RemotingConnectException(String addr) {\n" +
                "        this(addr, null);\n" +
                "    }\n" +
                "\n" +
                "    public RemotingConnectException(String addr, Throwable cause) {\n" +
                "        super(\"connect to <\" + addr + \"> failed\", cause);\n" +
                "    }\n";
    }
}
