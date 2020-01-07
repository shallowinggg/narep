package com.shallowinggg.narep.core.common;

import com.shallowinggg.narep.core.util.FileUtils;

import java.io.File;

/**
 * @author shallowinggg
 */
public class GeneratorConfig {
    public static final String PACKAGE = "package";
    public static final String PACKAGE_DELIMITER = ".";
    public static final String PACKAGE_COMMON = "common";
    public static final String PACKAGE_EXCEPTION = "exception";
    public static final String PACKAGE_NETTY = "netty";
    public static final String PACKAGE_PROTOCOL = "protocol";
    public static final String EOS_DELIMITER = ";";
    public static final String IMPORT = "import";

    private static final String DEFAULT_BASE_PACKAGE = "com.example.remoting";
    private static final String DEFAULT_STORE_LOCATION = System.getProperty("user.home") + "/generator";

    private static final GeneratorConfig INSTANCE = new GeneratorConfig();

    private String basePackage = DEFAULT_BASE_PACKAGE;
    private String storeLocation = DEFAULT_STORE_LOCATION;

    private GeneratorConfig() {
        FileUtils.ensureDirOk(new File(DEFAULT_BASE_PACKAGE));
    }

    public static GeneratorConfig getInstance() {
        return INSTANCE;
    }

    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public String getStoreLocation() {
        return storeLocation;
    }

    public void setStoreLocation(String storeLocation) {
        FileUtils.ensureDirOk(new File(storeLocation));
        this.storeLocation = storeLocation;
    }
}
