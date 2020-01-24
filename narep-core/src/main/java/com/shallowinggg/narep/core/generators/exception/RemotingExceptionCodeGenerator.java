package com.shallowinggg.narep.core.generators.exception;

import com.shallowinggg.narep.core.annotation.Generator;
import com.shallowinggg.narep.core.generators.ClassCodeGenerator;
import com.shallowinggg.narep.core.lang.FieldInfo;
import com.shallowinggg.narep.core.lang.Modifier;

import java.util.Collections;

/**
 * @author shallowinggg
 */
@Generator
public class RemotingExceptionCodeGenerator extends ClassCodeGenerator {
    private static final String CLASS_NAME = "RemotingException";
    private static final String PARENT_CLASS = "Exception";
    private static final String SUB_PACKAGE = "exception";

    public RemotingExceptionCodeGenerator() {
        super(CLASS_NAME, PARENT_CLASS, SUB_PACKAGE);
        setFields(Collections.singletonList(new FieldInfo(Modifier.PRIVATE_STATIC_FINAL, "long",
                "serialVersionUID", "-5690687334570505110L")));
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
