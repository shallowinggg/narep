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
public class RemotingConnectExceptionCodeGenerator extends ClassCodeGenerator {

    private static final String CLASS_NAME = "RemotingConnectException";
    private static final String PARENT_CLASS = "RemotingException";
    private static final String SUB_PACKAGE = "exception";

    public RemotingConnectExceptionCodeGenerator() {
        super(CLASS_NAME, PARENT_CLASS, SUB_PACKAGE);
        setFields(Collections.singletonList(new FieldInfo(PRIVATE_STATIC_FINAL, "long",
                "serialVersionUID", "-5565366231695911316L")));
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
