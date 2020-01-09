package com.shallowinggg.narep.core.common;

import com.shallowinggg.narep.core.Config;
import com.shallowinggg.narep.core.util.Conditions;
import com.shallowinggg.narep.core.util.StringTinyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 配置管理器，所有必需的{@link com.shallowinggg.narep.core.Config}都
 * 要注册到此类中。
 *
 * @author shallowinggg
 */
public class ConfigManager {
    private static final Logger LOG = LoggerFactory.getLogger(ConfigManager.class);
    private static final ConfigManager INSTANCE = new ConfigManager();

    private Map<String, Config> configMap = new HashMap<>(8);

    public static ConfigManager getInstance() {
        return INSTANCE;
    }

    public void register(String name, Config config) {
        Conditions.checkArgument(StringTinyUtils.isNotBlank(name), "config name must not be blank");
        Conditions.checkArgument(config != null, "config must not be null");
        configMap.put(name, config);
        if(LOG.isDebugEnabled()) {
            LOG.debug("register config: <{}>", name);
        }
    }

    public Config getConfig(String name) {
        return configMap.get(name);
    }
}
