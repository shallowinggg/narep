package com.shallowinggg.narep.core;

import com.shallowinggg.narep.core.annotation.ClassPathGeneratorScanner;
import com.shallowinggg.narep.core.common.*;
import com.shallowinggg.narep.core.common.CodeGeneratorManager;
import com.shallowinggg.narep.core.lang.ProtocolField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author shallowinggg
 */
public class GeneratorController {
    private static final Logger LOG = LoggerFactory.getLogger(GeneratorController.class);

    private final ConfigManager configManager = ConfigManager.getInstance();
    private final CodeGeneratorManager codeGeneratorManager = CodeGeneratorManager.getInstance();
    private final ConfigInfos configInfos = ConfigInfos.getInstance();
    private volatile boolean init;

    public void init() {
        if(!init) {
            // register required configs
            if (configManager.getConfig(GeneratorConfig.CONFIG_NAME) == null) {
                LOG.info("No user specified <{}> config found, use default config", GeneratorConfig.CONFIG_NAME);
                registerConfig(GeneratorConfig.CONFIG_NAME, new GeneratorConfig());
            }

            if (configManager.getConfig(ProtocolConfig.CONFIG_NAME) == null) {
                LOG.info("No user specified <{}> config found, use default config", ProtocolConfig.CONFIG_NAME);
                registerConfig(ProtocolConfig.CONFIG_NAME, new ProtocolConfig());
            }

            if (configManager.getConfig(LogConfig.CONFIG_NAME) == null) {
                LOG.info("No user specified <{}> config found, use default config", LogConfig.CONFIG_NAME);
                registerConfig(LogConfig.CONFIG_NAME, new LogConfig());
            }

            configManager.init();
            configInfos.init();
            init = true;
            if (LOG.isDebugEnabled()) {
                LOG.debug("GeneratorController init success");
            }
        }
    }

    public void start() {
        ClassPathGeneratorScanner scanner;
        if(configInfos.isUseCustomProtocol()) {
            scanner = new ClassPathGeneratorScanner(ConfigInfos.CUSTOM_PROFILE);
        } else {
            scanner = new ClassPathGeneratorScanner();
        }
        scanner.doScan(ConfigInfos.GENERATOR_PACKAGE);
        if(!codeGeneratorManager.resolve()) {
            return;
        }

        codeGeneratorManager.generate();
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
