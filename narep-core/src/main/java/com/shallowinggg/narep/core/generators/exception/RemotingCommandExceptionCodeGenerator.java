package com.shallowinggg.narep.core.generators.exception;

import com.shallowinggg.narep.core.common.GeneratorConfig;
import com.shallowinggg.narep.core.generators.ClassCodeGenerator;

/**
 * @author shallowinggg
 */
public class RemotingCommandExceptionCodeGenerator extends ClassCodeGenerator {
    private static final String CLASS_NAME = "RemotingCommandException";
    private static final String PARENT_CLASS = "RemotingException";

    public RemotingCommandExceptionCodeGenerator(GeneratorConfig generatorConfig) {
        super(CLASS_NAME, generatorConfig, PARENT_CLASS);
    }

    @Override
    public String buildFields() {
        return "    private static final long serialVersionUID = -6061365915274953096L;\n\n";
    }

    @Override
    public String buildMethods() {
        return "    public RemotingCommandException(String message) {\n" +
                "        super(message, null);\n" +
                "    }\n" +
                "\n" +
                "    public RemotingCommandException(String message, Throwable cause) {\n" +
                "        super(message, cause);\n" +
                "    }\n";
    }
}
