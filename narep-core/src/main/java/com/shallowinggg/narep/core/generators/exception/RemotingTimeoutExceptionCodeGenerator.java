package com.shallowinggg.narep.core.generators.exception;

import com.shallowinggg.narep.core.common.GeneratorConfig;
import com.shallowinggg.narep.core.generators.ClassCodeGenerator;

/**
 * @author shallowinggg
 */
public class RemotingTimeoutExceptionCodeGenerator extends ClassCodeGenerator {
    private static final String CLASS_NAME = "RemotingTimeoutException";
    private static final String PARENT_CLASS = "RemotingException";

    public RemotingTimeoutExceptionCodeGenerator(GeneratorConfig generatorConfig) {
        super(CLASS_NAME, generatorConfig, PARENT_CLASS);
    }

    @Override
    public String buildFields() {
        return "    private static final long serialVersionUID = 4106899185095245979L;\n\n";
    }

    @Override
    public String buildMethods() {
        return "    public RemotingTimeoutException(String message) {\n" +
                "        super(message);\n" +
                "    }\n" +
                "\n" +
                "    public RemotingTimeoutException(String addr, long timeoutMillis) {\n" +
                "        this(addr, timeoutMillis, null);\n" +
                "    }\n" +
                "\n" +
                "    public RemotingTimeoutException(String addr, long timeoutMillis, Throwable cause) {\n" +
                "        super(\"wait response on the channel <\" + addr + \"> timeout, \" + timeoutMillis + \"(ms)\", cause);\n" +
                "    }\n";
    }
}
