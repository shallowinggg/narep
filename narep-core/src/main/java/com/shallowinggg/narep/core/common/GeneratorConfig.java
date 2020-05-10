package com.shallowinggg.narep.core.common;

import com.shallowinggg.narep.core.Config;
import com.shallowinggg.narep.core.util.FileUtils;
import org.jetbrains.annotations.Nullable;

import java.io.File;

import static com.shallowinggg.narep.core.lang.JLSConstants.FILE_SEPARATOR;

/**
 * {@link Config} Implementation that stores common infos used for
 * building the whole module, including file store location, package
 * name etc.
 *
 * @author shallowinggg
 */
public class GeneratorConfig implements Config {
    public static final String CONFIG_NAME = "generator";

    private static final String DEFAULT_BASE_PACKAGE = "com.example.remoting";
    private static final String DEFAULT_STORE_LOCATION = System.getProperty("user.home") + FILE_SEPARATOR + "generator";
    private static final String DEFAULT_TLS_CONFIG_LOCATION = "/etc/narep/tls.properties";

    /**
     * Specify the name of base java package, all generated classes
     * will under this package.
     * default value is "com.example.remoting".
     */
    private String basePackage;

    /**
     * Specify where the generated files store, it is root directory.
     * Here is how file structure looks like:
     * <pre>
     * storeLocation
     *   - remoting [ModuleName]
     *     - src
     *       - main
     *         - java
     *           - basePackage
     * </pre>
     * default value is "${user.home}/generator".
     */
    private String storeLocation;

    /**
     * Specify the default location where file tls.properties stores,
     * this file will be used to store tls configs.
     * default value is "/etc/narep/tls.properties".
     *
     * @see com.shallowinggg.narep.core.generators.netty.TlsSystemConfigCodeGenerator
     */
    private String tlsConfigLocation;

    @Override
    public void init() {
        FileUtils.ensureDirOk(new File(getStoreLocation()));
        this.storeLocation = CodeGeneratorHelper.buildNecessaryFolders(this.storeLocation);
    }

    public String getBasePackage() {
        if (this.basePackage == null) {
            this.basePackage = DEFAULT_BASE_PACKAGE;
        }
        return this.basePackage;
    }

    public void setBasePackage(@Nullable String basePackage) {
        this.basePackage = basePackage;
    }

    public String getStoreLocation() {
        if (this.storeLocation == null) {
            this.storeLocation = DEFAULT_STORE_LOCATION;
        }
        return this.storeLocation;
    }

    public void setStoreLocation(@Nullable String storeLocation) {
        this.storeLocation = storeLocation;
    }

    public String getTlsConfigLocation() {
        if (this.tlsConfigLocation == null) {
            this.tlsConfigLocation = DEFAULT_TLS_CONFIG_LOCATION;
        }
        return this.tlsConfigLocation;
    }

    public void setTlsConfigLocation(@Nullable String tlsConfigLocation) {
        this.tlsConfigLocation = tlsConfigLocation;
    }

    @Override
    public String toString() {
        return "GeneratorConfig{" +
                "basePackage='" + basePackage + '\'' +
                ", storeLocation='" + storeLocation + '\'' +
                ", tlsConfigLocation='" + tlsConfigLocation + '\'' +
                '}';
    }
}
