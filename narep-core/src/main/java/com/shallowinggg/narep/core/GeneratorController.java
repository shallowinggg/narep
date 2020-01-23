package com.shallowinggg.narep.core;

import com.shallowinggg.narep.core.common.*;
import com.shallowinggg.narep.core.generators.CodeGeneratorManager;
import com.shallowinggg.narep.core.lang.ProtocolField;
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
    private volatile boolean init;

    public void init() {
        if(!init) {
            if (configManager.getConfig(GeneratorConfig.CONFIG_NAME) == null) {
                GeneratorConfig config = new GeneratorConfig();
                config.init();

                LOG.info("No user specified <{}> config found, use default config", GeneratorConfig.CONFIG_NAME);
                registerConfig(GeneratorConfig.CONFIG_NAME, config);
            }

            if (configManager.getConfig(ProtocolConfig.CONFIG_NAME) == null) {
                LOG.info("No user specified <{}> config found, use default config", ProtocolConfig.CONFIG_NAME);
                registerConfig(ProtocolConfig.CONFIG_NAME, new ProtocolConfig());
            }

            if (configManager.getConfig(LogConfig.CONFIG_NAME) == null) {
                LOG.info("No user specified <{}> config found, use default config", LogConfig.CONFIG_NAME);
                registerConfig(LogConfig.CONFIG_NAME, new LogConfig());
            }

            configInfos.init();
            init = true;
            if (LOG.isDebugEnabled()) {
                LOG.debug("GeneratorController init success");
            }
        }
    }

    public void start() {
        codeGeneratorManager.registerExceptionCodeGenerators();

    }

    public void registerConfig(String name, Config config) {
        configManager.register(name, config);
    }

    public void registerProtocolField(ProtocolField field) {
        if(!init) {
            throw new IllegalStateException("GeneratorController has not initialized yet");
        }
        ((ProtocolConfig) configManager.getConfig(ProtocolConfig.CONFIG_NAME)).addProtocolField(field);
    }
}
