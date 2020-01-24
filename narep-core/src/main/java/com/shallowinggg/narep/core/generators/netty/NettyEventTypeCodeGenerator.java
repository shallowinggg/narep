package com.shallowinggg.narep.core.generators.netty;

import com.shallowinggg.narep.core.annotation.Generator;
import com.shallowinggg.narep.core.generators.EnumCodeGenerator;

/**
 * @author shallowinggg
 */
@Generator
public class NettyEventTypeCodeGenerator extends EnumCodeGenerator {
    private static final String ENUM_NAME = "NettyEventType";
    private static final String SUB_PACKAGE = "netty";

    public NettyEventTypeCodeGenerator() {
        super(ENUM_NAME, SUB_PACKAGE);
    }

    @Override
    public String buildFields() {
        return "    CONNECT,\n" +
                "    CLOSE,\n" +
                "    IDLE,\n" +
                "    EXCEPTION\n";
    }
}
