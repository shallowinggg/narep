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
public class RemotingSendRequestExceptionCodeGenerator extends ClassCodeGenerator {
    private static final String CLASS_NAME = "RemotingSendRequestException";
    private static final String PARENT_CLASS = "RemotingException";
    private static final String SUB_PACKAGE = "exception";

    public RemotingSendRequestExceptionCodeGenerator() {
        super(CLASS_NAME, PARENT_CLASS, SUB_PACKAGE);
        setFields(Collections.singletonList(new FieldInfo(Modifier.PRIVATE_STATIC_FINAL, "long",
                "serialVersionUID", "5391285827332471674L")));
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
