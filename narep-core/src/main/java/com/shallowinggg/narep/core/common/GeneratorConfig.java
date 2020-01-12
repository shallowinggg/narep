package com.shallowinggg.narep.core.common;

import com.shallowinggg.narep.core.Config;
import com.shallowinggg.narep.core.util.FileUtils;

import java.io.File;

/**
 * @author shallowinggg
 */
public class GeneratorConfig implements Config {
    public static final String CONFIG_NAME = "generator";

    public static final String PACKAGE = "package";
    public static final String IMPORT = "import";
    public static final String PACKAGE_DELIMITER = ".";
    public static final String EOS_DELIMITER = ";";
    public static final String LINE_SEPARATOR = System.lineSeparator();
    public static final String DOUBLE_LINE_SEPARATOR = System.lineSeparator() + System.lineSeparator();
    public static final char FILE_SEPARATOR = File.separatorChar;

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
