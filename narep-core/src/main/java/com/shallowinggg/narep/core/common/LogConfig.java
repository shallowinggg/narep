package com.shallowinggg.narep.core.common;

import com.shallowinggg.narep.core.Config;
import org.jetbrains.annotations.Nullable;

/**
 * {@link Config} Implementation that stores infos used for
 * controlling log.
 * <p>
 * This class is only used to control log use in java class,
 * e.g. <code>private Logger log = LogManager.getLogger(...);</code>
 * <p>
 * Default log operation class used for generated classes is log4j2.
 *
 * @author shallowinggg
 */
public class LogConfig implements Config {
    public static final String CONFIG_NAME = "log";
    private static final String DEFAULT_LOGGER_NAME = "Remoting";

    /**
     * Specify the name used for LogManager#getLogger(String).
     * if {@link #useCustomLoggerName} is {@literal false}, this
     * value will be ignored.
     * default value is "Remoting".
     */
    private String loggerName;

    /**
     * Specify whether to use LogManager#getLogger(String) or not.
     * If its value is {@literal false}, java class will use
     * LogManager#getLogger(Class clazz) for logging.
     */
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
        if(loggerName == null) {
            loggerName = DEFAULT_LOGGER_NAME;
        }
        return loggerName;
    }

    public void setLoggerName(@Nullable String loggerName) {
        this.loggerName = loggerName;
    }

    @Override
    public String toString() {
        return "LogConfig{" +
                "loggerName='" + loggerName + '\'' +
                ", useCustomLoggerName=" + useCustomLoggerName +
                '}';
    }
}
