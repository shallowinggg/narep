package com.shallowinggg.narep.core.generators;

import com.shallowinggg.narep.core.CodeGenerator;
import com.shallowinggg.narep.core.common.CodeGeneratorHelper;
import com.shallowinggg.narep.core.common.GeneratorConfig;

/**
 * @author shallowinggg
 */
public class RemotingClientCodeGenerator implements CodeGenerator {
    private static final String CLASS_NAME = "RemotingClient";
    private static final String PARENT_INTERFACE = "RemotingService";

    private GeneratorConfig generatorConfig;

    public RemotingClientCodeGenerator(GeneratorConfig generatorConfig) {
        this.generatorConfig = generatorConfig;
    }

    @Override
    public String fileName() {
        return CodeGeneratorHelper.buildFileName(generatorConfig.getBasePackage(), CLASS_NAME);
    }

    @Override
    public String buildPackage() {
        return CodeGeneratorHelper.buildDefaultPackage(generatorConfig.getBasePackage());
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
