package com.shallowinggg.narep.admin.xml;

import com.shallowinggg.narep.core.lang.ProtocolField;

import java.util.List;

/**
 * @author shallowinggg
 */
public class NarepDefinition {

    private String packageName;

    private String location;

    private boolean useCustomLogger;

    private String loggerName;

    private List<ProtocolField> protocolFields;


    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isUseCustomLogger() {
        return useCustomLogger;
    }

    public void setUseCustomLogger(boolean useCustomLogger) {
        this.useCustomLogger = useCustomLogger;
    }

    public String getLoggerName() {
        return loggerName;
    }

    public void setLoggerName(String loggerName) {
        this.loggerName = loggerName;
    }

    public List<ProtocolField> getProtocolFields() {
        return protocolFields;
    }

    public void setProtocolFields(List<ProtocolField> protocolFields) {
        this.protocolFields = protocolFields;
    }

    @Override
    public String toString() {
        return "NarepDefinition{" +
                "packageName='" + packageName + '\'' +
                ", location='" + location + '\'' +
                ", useCustomLogger=" + useCustomLogger +
                ", loggerName='" + loggerName + '\'' +
                ", protocolFields=" + protocolFields +
                '}';
    }
}
