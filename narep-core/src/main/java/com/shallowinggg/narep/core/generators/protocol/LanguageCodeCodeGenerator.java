package com.shallowinggg.narep.core.generators.protocol;

import com.shallowinggg.narep.core.annotation.Generator;
import com.shallowinggg.narep.core.annotation.Profile;
import com.shallowinggg.narep.core.generators.EnumCodeGenerator;

/**
 * @author shallowinggg
 */
@Generator
@Profile("default")
public class LanguageCodeCodeGenerator extends EnumCodeGenerator {
    private static final String ENUM_NAME = "LanguageCode";
    private static final String SUB_PACKAGE = "protocol";

    public LanguageCodeCodeGenerator() {
        super(ENUM_NAME, SUB_PACKAGE);
    }

    @Override
    public String buildFields() {
        return "    JAVA((byte) 0),\n" +
                "    CPP((byte) 1),\n" +
                "    DOTNET((byte) 2),\n" +
                "    PYTHON((byte) 3),\n" +
                "    DELPHI((byte) 4),\n" +
                "    ERLANG((byte) 5),\n" +
                "    RUBY((byte) 6),\n" +
                "    OTHER((byte) 7),\n" +
                "    HTTP((byte) 8),\n" +
                "    GO((byte) 9),\n" +
                "    PHP((byte) 10),\n" +
                "    OMS((byte) 11);\n" +
                "\n" +
                "    private byte code;\n\n";
    }

    @Override
    public String buildMethods() {
        return "    LanguageCode(byte code) {\n" +
                "        this.code = code;\n" +
                "    }\n" +
                "\n" +
                "    public static LanguageCode valueOf(byte code) {\n" +
                "        for (LanguageCode languageCode : LanguageCode.values()) {\n" +
                "            if (languageCode.getCode() == code) {\n" +
                "                return languageCode;\n" +
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
