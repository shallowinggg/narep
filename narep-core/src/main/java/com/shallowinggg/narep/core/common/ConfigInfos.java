package com.shallowinggg.narep.core.common;

import com.shallowinggg.narep.core.lang.FieldInfo;
import com.shallowinggg.narep.core.lang.Modifier;
import com.shallowinggg.narep.core.lang.ProtocolField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


/**
 *
 *
 * @author shallowinggg
 */
public class ConfigInfos {
    private static final Logger LOG = LoggerFactory.getLogger(ConfigInfos.class);

    public static final String GENERATOR_PACKAGE = "com.shallowinggg.narep.core.generators";
    public static final String CUSTOM_PROFILE = "custom";

    private static final ConfigInfos INSTANCE = new ConfigInfos();
    private GeneratorConfig generatorConfig;
    private ProtocolConfig protocolConfig;
    private LogConfig logConfig;

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
        if(this.commandFields == null) {
            if (this.protocolConfig.getProtocolFields().size() > ProtocolConfig.DEFAULT_FIELDS_SIZE) {
                this.commandFields = convertConfig2Fields(this.protocolConfig.getProtocolFields());
            } else {
                this.commandFields = Collections.emptyList();
            }
        }
        return this.commandFields;
    }

    public List<ProtocolField> protocolFields() {
        return protocolConfig.getProtocolFields();
    }

    public boolean isUseCustomProtocol() {
        return this.protocolConfig.getProtocolFields().size() > ProtocolConfig.DEFAULT_FIELDS_SIZE;
    }

    private static List<FieldInfo> convertConfig2Fields(List<ProtocolField> protocolFields) {
        List<FieldInfo> fields = new ArrayList<>(protocolFields.size());
        for (ProtocolField protocolField : protocolFields) {
            Class<?> clazz = protocolField.getClazz();
            if(HashMap.class.equals(clazz)) {
                String type = "HashMap<String, String>";
                fields.add(new FieldInfo(Modifier.PRIVATE, type, protocolField.getName()));
            } else {
                fields.add(new FieldInfo(Modifier.PRIVATE, clazz.getSimpleName(), protocolField.getName()));
            }
        }
        return fields;
    }

    public String tlsConfigLocation() {
        return this.generatorConfig.getTlsConfigLocation();
    }
}
