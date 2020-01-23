package com.shallowinggg.narep.core.common;

import com.shallowinggg.narep.core.lang.FieldInfo;
import com.shallowinggg.narep.core.lang.Modifier;
import com.shallowinggg.narep.core.lang.ProtocolField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * @author shallowinggg
 */
public class ConfigInfos {
    private static final Logger LOG = LoggerFactory.getLogger(ConfigInfos.class);

    private static final ConfigInfos INSTANCE = new ConfigInfos();
    private static GeneratorConfig generatorConfig;
    private static ProtocolConfig protocolConfig;
    private static LogConfig logConfig;

    private List<FieldInfo> commandFields;
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
            if (LOG.isDebugEnabled()) {
                LOG.debug("ConfigInfos init success");
            }
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

    public List<FieldInfo> commandFields() {
        if(commandFields == null) {
            if (protocolConfig.getProtocolFields().size() > ProtocolConfig.DEFAULT_FIELDS_SIZE) {
                commandFields = convertConfig2Fields(protocolConfig.getProtocolFields());
            } else {
                commandFields = Collections.emptyList();
            }
        }
        return commandFields;
    }

    public List<ProtocolField> protocolFields() {
        return protocolConfig.getProtocolFields();
    }

    private static List<FieldInfo> convertConfig2Fields(List<ProtocolField> protocolFields) {
        List<FieldInfo> fields = new ArrayList<>(protocolFields.size());
        for (ProtocolField protocolField : protocolFields) {
            fields.add(new FieldInfo(Modifier.PRIVATE, protocolField.getClazz().getSimpleName(), protocolField.getName()));
        }
        return fields;
    }
}
