package com.shallowinggg.narep.core;

/**
 * Store config infos for generating files.
 *
 * The user can implement {@link #init()} method
 * to execute some required init operations.
 *
 * Implementations will be registered to
 * {@link com.shallowinggg.narep.core.common.ConfigManager} and provide
 * infos for {@link com.shallowinggg.narep.core.common.ConfigInfos}.
 *
 * @author shallowinggg
 * @see com.shallowinggg.narep.core.common.ConfigManager#register(String, Config)
 * @see com.shallowinggg.narep.core.common.ConfigInfos
 */
@FunctionalInterface
public interface Config {

    /**
     * init config
     */
    void init();
}
