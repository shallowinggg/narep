package com.shallowinggg.narep.core.generators.exception;

import com.shallowinggg.narep.core.annotation.Generator;
import com.shallowinggg.narep.core.generators.ClassCodeGenerator;
import com.shallowinggg.narep.core.lang.FieldInfo;

import java.util.Collections;

import static com.shallowinggg.narep.core.lang.Modifier.PRIVATE_STATIC_FINAL;


/**
 * @author shallowinggg
 */
@Generator
public class RemotingCommandExceptionCodeGenerator extends ClassCodeGenerator {
    private static final String CLASS_NAME = "RemotingCommandException";
    private static final String PARENT_CLASS = "RemotingException";
    private static final String SUB_PACKAGE = "exception";

    public RemotingCommandExceptionCodeGenerator() {
        super(CLASS_NAME, PARENT_CLASS, SUB_PACKAGE);
        setFields(Collections.singletonList(new FieldInfo(PRIVATE_STATIC_FINAL, "long",
                "serialVersionUID", "-6061365915274953096L")));
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
