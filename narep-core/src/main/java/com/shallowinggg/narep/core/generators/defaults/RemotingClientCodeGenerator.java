package com.shallowinggg.narep.core.generators.defaults;

import com.shallowinggg.narep.core.common.GeneratorConfig;
import com.shallowinggg.narep.core.generators.InterfaceCodeGenerator;

/**
 * @author shallowinggg
 */
public class RemotingClientCodeGenerator extends InterfaceCodeGenerator {
    private static final String INTERFACE_NAME = "RemotingClient";
    private static final String PARENT_INTERFACE = "RemotingService";

    public RemotingClientCodeGenerator(GeneratorConfig generatorConfig) {
        super(INTERFACE_NAME, generatorConfig, PARENT_INTERFACE);
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
