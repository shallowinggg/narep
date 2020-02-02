package com.shallowinggg.narep.core.common;

import com.shallowinggg.narep.core.Config;

/**
 * @author shallowinggg
 */
public class LogConfig implements Config {
    public static final String CONFIG_NAME = "log";
    private static final String DEFAULT_LOGGER_NAME = "Remoting";

    private String loggerName = DEFAULT_LOGGER_NAME;
    private boolean useCustomLoggerName;
    // TODO: 加入日志类控制

    public LogConfig() {
    }

    public LogConfig(String loggerName, boolean useCustomLoggerName) {
        this.loggerName = loggerName;
        this.useCustomLoggerName = useCustomLoggerName;
    }

    @Override
    public void init() {

    }

    public boolean isUseCustomLoggerName() {
        return useCustomLoggerName;
    }

    public void setUseCustomLoggerName(boolean useCustomLoggerName) {
        this.useCustomLoggerName = useCustomLoggerName;
    }

    public String getLoggerName() {
        return loggerName;
    }

    public void setLoggerName(String loggerName) {
        this.loggerName = loggerName;
    }
}
