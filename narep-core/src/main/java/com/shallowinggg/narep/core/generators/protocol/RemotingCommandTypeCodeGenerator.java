package com.shallowinggg.narep.core.generators.protocol;

import com.shallowinggg.narep.core.annotation.Generator;
import com.shallowinggg.narep.core.generators.EnumCodeGenerator;

/**
 * @author shallowinggg
 */
@Generator
public class RemotingCommandTypeCodeGenerator extends EnumCodeGenerator {
    private static final String ENUM_NAME = "RemotingCommandType";
    private static final String SUB_PACKAGE = "protocol";

    public RemotingCommandTypeCodeGenerator() {
        super(ENUM_NAME, SUB_PACKAGE);
    }

    @Override
    public String buildFields() {
        return "    REQUEST_COMMAND,\n" +
                "    RESPONSE_COMMAND\n";
    }
}
