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
public class RemotingTooMuchRequestExceptionCodeGenerator extends ClassCodeGenerator {
    private static final String CLASS_NAME = "RemotingTooMuchRequestException";
    private static final String PARENT_CLASS = "RemotingException";
    private static final String SUB_PACKAGE = "exception";

    public RemotingTooMuchRequestExceptionCodeGenerator() {
        super(CLASS_NAME, PARENT_CLASS, SUB_PACKAGE);
        setFields(Collections.singletonList(new FieldInfo(PRIVATE_STATIC_FINAL, "long",
                "serialVersionUID", "4326919581254519654L")));
    }

    @Override
    public String buildMethods() {
        return "    public RemotingTooMuchRequestException(String message) {\n" +
                "        super(message);\n" +
                "    }\n";
    }
}
