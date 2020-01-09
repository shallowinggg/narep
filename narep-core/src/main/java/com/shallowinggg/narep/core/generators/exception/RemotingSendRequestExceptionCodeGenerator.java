package com.shallowinggg.narep.core.generators.exception;

import com.shallowinggg.narep.core.generators.ClassCodeGenerator;

/**
 * @author shallowinggg
 */
public class RemotingSendRequestExceptionCodeGenerator extends ClassCodeGenerator {
    private static final String CLASS_NAME = "RemotingSendRequestException";
    private static final String PARENT_CLASS = "RemotingException";

    public RemotingSendRequestExceptionCodeGenerator() {
        super(CLASS_NAME, PARENT_CLASS);
    }

    @Override
    public String buildFields() {
        return "    private static final long serialVersionUID = 5391285827332471674L;\n\n";
    }

    @Override
    public String buildMethods() {
        return "    public RemotingSendRequestException(String addr) {\n" +
                "        this(addr, null);\n" +
                "    }\n" +
                "\n" +
                "    public RemotingSendRequestException(String addr, Throwable cause) {\n" +
                "        super(\"send request to <\" + addr + \"> failed\", cause);\n" +
                "    }\n";
    }
}
