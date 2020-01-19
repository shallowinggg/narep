package com.shallowinggg.narep.core.generators.netty;

import com.shallowinggg.narep.core.common.CodeGeneratorHelper;
import com.shallowinggg.narep.core.generators.ClassCodeGenerator;

/**
 * @author shallowinggg
 */
public class NettyEventTypeCodeGenerator extends ClassCodeGenerator {
    private static final String ENUM_NAME = "NettyEventType";
    private static final String SUB_PACKAGE = "netty";

    public NettyEventTypeCodeGenerator() {
        super(ENUM_NAME, null, SUB_PACKAGE);
    }

    @Override
    public String buildName() {
        return CodeGeneratorHelper.buildEnumDeclaration(ENUM_NAME);
    }

    @Override
    public String buildFields() {
        return "    CONNECT,\n" +
                "    CLOSE,\n" +
                "    IDLE,\n" +
                "    EXCEPTION\n";
    }
}
