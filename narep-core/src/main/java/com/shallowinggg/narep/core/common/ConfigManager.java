package com.shallowinggg.narep.core.common;

import com.shallowinggg.narep.core.Config;
import com.shallowinggg.narep.core.util.Conditions;
import com.shallowinggg.narep.core.util.StringTinyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is provided to store configs, it is an unique repository,
 * all implementations of interface {@link Config} should be registered
 * to it.
 *
 * Before get config from this repository, you should invoke {@link #init()}
 * method to init all configs. Otherwise, you may get unexpected config infos.
 * Of course, {@link Config} can only be used to store some data and have no
 * init operations. So here is no mandatory to invoke {@link #init()} method
 * before invoking {@link #getConfig(String)} method.
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
        if (LOG.isDebugEnabled()) {
            LOG.debug("register config: <{}>", name);
        }
    }

    public void init() {
        for(Config config : configMap.values()) {
            config.init();
        }
    }

    public Config getConfig(String name) {
        return configMap.get(name);
    }
}
