package com.shallowinggg.narep.core.generators.protocol;

import com.shallowinggg.narep.core.annotation.Generator;
import com.shallowinggg.narep.core.generators.EnumCodeGenerator;

/**
 * @author shallowinggg
 */
@Generator
public class SerializeTypeCodeGenerator extends EnumCodeGenerator {
    private static final String ENUM_NAME = "SerializeType";
    private static final String SUB_PACKAGE = "protocol";

    public SerializeTypeCodeGenerator() {
        super(ENUM_NAME, SUB_PACKAGE);
    }

    @Override
    public String buildFields() {
        return "    JSON((byte) 0),\n" +
                "    NAREP((byte) 1);\n" +
                "\n" +
                "    private byte code;\n";
    }

    @Override
    public String buildMethods() {
        return "    SerializeType(byte code) {\n" +
                "        this.code = code;\n" +
                "    }\n" +
                "\n" +
                "    public static SerializeType valueOf(byte code) {\n" +
                "        for (SerializeType serializeType : SerializeType.values()) {\n" +
                "            if (serializeType.getCode() == code) {\n" +
                "                return serializeType;\n" +
                "            }\n" +
                "        }\n" +
                "        return null;\n" +
                "    }\n" +
                "\n" +
                "    public byte getCode() {\n" +
                "        return code;\n" +
                "    }\n";
    }
}
