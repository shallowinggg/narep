package com.shallowinggg.narep.core;

import com.shallowinggg.narep.core.common.*;
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
    private ConfigInfos configInfos = ConfigInfos.getInstance();

    public void init() {
        if(configManager.getConfig(GeneratorConfig.CONFIG_NAME) == null) {
            GeneratorConfig config = new GeneratorConfig();
            config.init();
            registerConfig(GeneratorConfig.CONFIG_NAME, config);
            if(LOG.isInfoEnabled()) {
                LOG.info("No user specified <{}> config found, use default config", GeneratorConfig.CONFIG_NAME);
            }
        }

        if(configManager.getConfig(ProtocolConfig.CONFIG_NAME) == null) {
            registerConfig(ProtocolConfig.CONFIG_NAME, new ProtocolConfig());
            if(LOG.isInfoEnabled()) {
                LOG.info("No user specified <{}> config found, use default config", ProtocolConfig.CONFIG_NAME);
            }
        }

        if(configManager.getConfig(LogConfig.CONFIG_NAME) == null) {
            registerConfig(LogConfig.CONFIG_NAME, new LogConfig());if(LOG.isInfoEnabled()) {
                LOG.info("No user specified <{}> config found, use default config", LogConfig.CONFIG_NAME);
            }
        }

        configInfos.init();
    }

    public void start() {
        codeGeneratorManager.registerExceptionCodeGenerators();

    }

    public void registerConfig(String name, Config config) {
        configManager.register(name, config);
    }
}
