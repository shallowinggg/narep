package com.shallowinggg.narep.core;

import com.shallowinggg.narep.core.common.ConfigManager;
import com.shallowinggg.narep.core.common.GeneratorConfig;
import com.shallowinggg.narep.core.common.ProtocolConfig;
import com.shallowinggg.narep.core.generators.CodeGeneratorManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author shallowinggg
 */
public class GeneratorController {
    private static final Logger LOG = LoggerFactory.getLogger(GeneratorController.class);

    private ConfigManager configManager = ConfigManager.getInstance();
    private CodeGeneratorManager codeGeneratorManager = CodeGeneratorManager.getInstance();

    public void init() {
        if(configManager.getConfig(GeneratorConfig.CONFIG_NAME) == null) {
            configManager.register(GeneratorConfig.CONFIG_NAME, new GeneratorConfig());
            if(LOG.isInfoEnabled()) {
                LOG.info("No user specified <{}> config found, use default config", GeneratorConfig.CONFIG_NAME);
            }
        }

        if(configManager.getConfig(ProtocolConfig.CONFIG_NAME) == null) {
            configManager.register(ProtocolConfig.CONFIG_NAME, new ProtocolConfig());
            if(LOG.isInfoEnabled()) {
                LOG.info("No user specified <{}> config found, use default config", ProtocolConfig.CONFIG_NAME);
            }
        }
    }

    public void start() {
        codeGeneratorManager.registerExceptionCodeGenerators();

    }

    public void registerConfig(String name, Config config) {
        configManager.register(name, config);
    }
}
