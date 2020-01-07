package com.shallowinggg.narep.core.generators.protocol;

import com.shallowinggg.narep.core.CodeGenerator;
import com.shallowinggg.narep.core.common.CodeGeneratorHelper;
import com.shallowinggg.narep.core.common.GeneratorConfig;

/**
 * @author shallowinggg
 */
public class RemotingCommandCodeGenerator implements CodeGenerator {
    private static final String CLASS_NAME = "RemotingCommand";

    private GeneratorConfig generatorConfig;

    public RemotingCommandCodeGenerator(GeneratorConfig generatorConfig) {
        this.generatorConfig = generatorConfig;
    }

    @Override
    public String fileName() {
        return CodeGeneratorHelper.buildFileName(generatorConfig.getBasePackage(),
                GeneratorConfig.PACKAGE_PROTOCOL, CLASS_NAME);
    }

    @Override
    public String buildPackage() {
        return CodeGeneratorHelper.buildProtocolPackage(generatorConfig.getBasePackage());
    }

    @Override
    public String buildImports() {
        return null;
    }

    @Override
    public String buildName() {
        return null;
    }

    @Override
    public String buildFields() {
        return null;
    }

    @Override
    public String buildMethods() {
        return null;
    }
}
