package com.shallowinggg.narep.core.generators.exception;

import com.shallowinggg.narep.core.CodeGenerator;
import com.shallowinggg.narep.core.common.CodeGeneratorHelper;
import com.shallowinggg.narep.core.common.GeneratorConfig;

/**
 * @author shallowinggg
 */
public class RemotingExceptionCodeGenerator implements CodeGenerator {
    private static final String CLASS_NAME = "RemotingException";
    private static final String PARENT_CLASS = "Exception";

    private GeneratorConfig generatorConfig;

    public RemotingExceptionCodeGenerator(GeneratorConfig generatorConfig) {
        this.generatorConfig = generatorConfig;
    }

    @Override
    public String fileName() {
        return CodeGeneratorHelper.buildFileName(generatorConfig.getBasePackage(),
                GeneratorConfig.PACKAGE_EXCEPTION, CLASS_NAME);
    }

    @Override
    public String buildPackage() {
        return CodeGeneratorHelper.buildExceptionPackage(generatorConfig.getBasePackage());
    }

    @Override
    public String buildImports() {
        return System.lineSeparator();
    }

    @Override
    public String buildName() {
        return CodeGeneratorHelper.buildClassDeclaration(CLASS_NAME, PARENT_CLASS);
    }

    @Override
    public String buildFields() {
        return "private static final long serialVersionUID = -5690687334570505110L;\n" + System.lineSeparator();
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
