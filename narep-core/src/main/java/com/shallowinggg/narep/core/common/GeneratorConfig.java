package com.shallowinggg.narep.core.common;

import com.shallowinggg.narep.core.Config;
import com.shallowinggg.narep.core.util.FileUtils;

import java.io.File;

import static com.shallowinggg.narep.core.lang.JLSConstants.FILE_SEPARATOR;

/**
 * @author shallowinggg
 */
public class GeneratorConfig implements Config {
    public static final String CONFIG_NAME = "generator";

    private static final String DEFAULT_BASE_PACKAGE = "com.example.remoting";
    private static final String DEFAULT_STORE_LOCATION = System.getProperty("user.home") + FILE_SEPARATOR + "generator";
    private static final String DEFAULT_TLS_CONFIG_LOCATION = "/etc/narep/tls.properties";

    private String basePackage;
    private String storeLocation;
    private String tlsConfigLocation;

    @Override
    public void init() {
        FileUtils.ensureDirOk(new File(getStoreLocation()));
        this.storeLocation = CodeGeneratorHelper.buildNecessaryFolders(this.storeLocation);
    }

    public String getBasePackage() {
        if(this.basePackage == null) {
            this.basePackage = DEFAULT_BASE_PACKAGE;
        }
        return this.basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public String getStoreLocation() {
        if (this.storeLocation == null) {
            this.storeLocation = DEFAULT_STORE_LOCATION;
        }
        return this.storeLocation;
    }

    public void setStoreLocation(String storeLocation) {
        this.storeLocation = storeLocation;
    }

    public String getTlsConfigLocation() {
        if(this.tlsConfigLocation == null) {
            this.tlsConfigLocation = DEFAULT_TLS_CONFIG_LOCATION;
        }
        return this.tlsConfigLocation;
    }

    public void setTlsConfigLocation(String tlsConfigLocation) {
        this.tlsConfigLocation = tlsConfigLocation;
    }
}
