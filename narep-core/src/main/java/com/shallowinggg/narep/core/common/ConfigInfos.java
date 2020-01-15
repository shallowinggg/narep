package com.shallowinggg.narep.core.common;

/**
 * @author shallowinggg
 */
public class ConfigInfos {
    private static final ConfigInfos INSTANCE = new ConfigInfos();
    private static GeneratorConfig generatorConfig;
    private static ProtocolConfig protocolConfig;
    private static LogConfig logConfig;

    private volatile boolean init;

    private ConfigInfos() {
    }

    public static ConfigInfos getInstance() {
        return INSTANCE;
    }

    public void init() {
        if (!init) {
            generatorConfig = (GeneratorConfig) ConfigManager.getInstance().getConfig(GeneratorConfig.CONFIG_NAME);
            protocolConfig = (ProtocolConfig) ConfigManager.getInstance().getConfig(ProtocolConfig.CONFIG_NAME);
            logConfig = (LogConfig) ConfigManager.getInstance().getConfig(LogConfig.CONFIG_NAME);
            init = true;
        }
    }

    public String basePackage() {
        return generatorConfig.getBasePackage();
    }

    public String storeLocation() {
        return generatorConfig.getStoreLocation();
    }

    public boolean useCustomLoggerName() {
        return logConfig.isUseCustomLoggerName();
    }

    public String loggerName() {
        return logConfig.getLoggerName();
    }
}
