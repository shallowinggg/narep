package com.shallowinggg.narep.core.generators.protocol;

import com.shallowinggg.narep.core.common.GeneratorConfig;
import com.shallowinggg.narep.core.generators.ClassCodeGenerator;

/**
 * @author shallowinggg
 */
public class RemotingCommandCodeGenerator extends ClassCodeGenerator {
    private static final String CLASS_NAME = "RemotingCommand";

    private GeneratorConfig generatorConfig;

    public RemotingCommandCodeGenerator(GeneratorConfig generatorConfig) {
        super(CLASS_NAME, generatorConfig);
    }

    @Override
    public String buildImports() {
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
