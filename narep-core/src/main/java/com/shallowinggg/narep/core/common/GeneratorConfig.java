package com.shallowinggg.narep.core.common;

import com.shallowinggg.narep.core.Config;
import com.shallowinggg.narep.core.util.FileUtils;

import java.io.File;

import static com.shallowinggg.narep.core.common.JLSConstants.FILE_SEPARATOR;

/**
 * @author shallowinggg
 */
public class GeneratorConfig implements Config {
    public static final String CONFIG_NAME = "generator";

    private static final String DEFAULT_BASE_PACKAGE = "com.example.remoting";
    private static final String DEFAULT_STORE_LOCATION = System.getProperty("user.home") + FILE_SEPARATOR + "generator";

    private String basePackage = DEFAULT_BASE_PACKAGE;
    private String storeLocation = DEFAULT_STORE_LOCATION;


    public void init() {
        this.storeLocation = CodeGeneratorHelper.buildNecessaryFolders(this.storeLocation);
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
