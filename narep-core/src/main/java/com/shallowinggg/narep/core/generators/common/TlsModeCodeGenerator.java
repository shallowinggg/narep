package com.shallowinggg.narep.core.generators.common;

import com.shallowinggg.narep.core.common.CodeGeneratorHelper;
import com.shallowinggg.narep.core.generators.ClassCodeGenerator;

public class TlsModeCodeGenerator extends ClassCodeGenerator {
    private static final String ENUM_NAME = "TlsMode";
    private static final String SUB_PACKAGE = "common";

    public TlsModeCodeGenerator() {
        super(ENUM_NAME, SUB_PACKAGE);
    }

    @Override
    public String buildClassComment() {
        return "/**\n" +
                " * For server, three SSL modes are supported: disabled, permissive and enforcing.\n" +
                " * <ol>\n" +
                " *     <li><strong>disabled:</strong> SSL is not supported; any incoming SSL handshake will be rejected, causing connection closed.</li>\n" +
                " *     <li><strong>permissive:</strong> SSL is optional, aka, server in this mode can serve client connections with or without SSL;</li>\n" +
                " *     <li><strong>enforcing:</strong> SSL is required, aka, non SSL connection will be rejected.</li>\n" +
                " * </ol>\n" +
                " */\n";
    }

    @Override
    public String buildName() {
        return CodeGeneratorHelper.buildEnumDeclaration(ENUM_NAME);
    }

    @Override
    public String buildFields() {
        return "    DISABLED(\"disabled\"),\n" +
                "    PERMISSIVE(\"permissive\"),\n" +
                "    ENFORCING(\"enforcing\");\n" +
                "\n" +
                "    private String name;\n\n";
    }

    @Override
    public String buildMethods() {
        return "    TlsMode(String name) {\n" +
                "        this.name = name;\n" +
                "    }\n" +
                "\n" +
                "    public static TlsMode parse(String mode) {\n" +
                "        for (TlsMode tlsMode : TlsMode.values()) {\n" +
                "            if (tlsMode.name.equals(mode)) {\n" +
                "                return tlsMode;\n" +
                "            }\n" +
                "        }\n" +
                "\n" +
                "        return PERMISSIVE;\n" +
                "    }\n" +
                "\n" +
                "    public String getName() {\n" +
                "        return name;\n" +
                "    }\n";
    }
}
