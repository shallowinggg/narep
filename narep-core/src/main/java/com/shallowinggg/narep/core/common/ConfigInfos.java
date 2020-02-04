package com.shallowinggg.narep.core.common;

import com.shallowinggg.narep.core.lang.FieldInfo;
import com.shallowinggg.narep.core.lang.Modifier;
import com.shallowinggg.narep.core.lang.ProtocolField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


/**
 *
 *
 * @author shallowinggg
 */
public class ConfigInfos {
    private static final Logger LOG = LoggerFactory.getLogger(ConfigInfos.class);

    public static final String GENERATOR_PACKAGE = "com.shallowinggg.narep.core.generators";
    public static final String CUSTOM_PROFILE = "custom";

    /**
     * default sort algorithm for {@link ProtocolField}, its main function is to
     * keep the relative order unchanged. In other words, all primitive fields
     * and composite fields will not be sorted, but all composite fields need to
     * be sorted after all primitive fields.
     */
    public static final Comparator<ProtocolField> DEFAULT_PROTOCOL_SORT = (f1, f2) -> {
        if(f1.getLen() == -1 && f2.getLen() == -1) {
            return 0;
        } else if (f1.getLen() != -1 && f2.getLen() != -1) {
            return 0;
        } else if(f1.getLen() != -1) {
            return -1;
        } else {
            return 1;
        }
    };

    private static final ConfigInfos INSTANCE = new ConfigInfos();
    private GeneratorConfig generatorConfig;
    private ProtocolConfig protocolConfig;
    private LogConfig logConfig;

    private List<FieldInfo> commandFields;
    private List<ProtocolField> protocolFields;
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

            this.protocolFields = protocolConfig.getProtocolFields();
            protocolFields.sort(DEFAULT_PROTOCOL_SORT);
            if(protocolFields.size() > ProtocolConfig.DEFAULT_FIELDS_SIZE) {
                this.commandFields = convertConfig2Fields(this.protocolConfig.getProtocolFields());
            } else {
                this.commandFields = Collections.emptyList();
            }

            init = true;
            if (LOG.isDebugEnabled()) {
                LOG.debug("ConfigInfos init success");
            }
        }
    }

    public String basePackage() {
        assert init;
        return generatorConfig.getBasePackage();
    }

    public String storeLocation() {
        assert init;
        return generatorConfig.getStoreLocation();
    }

    public boolean useCustomLoggerName() {
        assert init;
        return logConfig.isUseCustomLoggerName();
    }

    public String loggerName() {
        assert init;
        return logConfig.getLoggerName();
    }

    public List<FieldInfo> commandFields() {
        assert init;
        return this.commandFields;
    }

    public List<ProtocolField> protocolFields() {
        assert init;
        return this.protocolFields;
    }

    public boolean isUseCustomProtocol() {
        assert init;
        return this.protocolFields.size() > ProtocolConfig.DEFAULT_FIELDS_SIZE;
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
        assert init;
        return this.generatorConfig.getTlsConfigLocation();
    }
}
