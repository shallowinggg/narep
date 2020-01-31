package com.shallowinggg.narep.core.env;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.AccessControlException;
import java.util.Map;

public class StandardEnvironment implements Environment {
    private static Logger logger = LoggerFactory.getLogger(StandardEnvironment.class);

    /** System environment property source name: {@value}. */
    public static final String SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME = "systemEnvironment";

    /** JVM system properties property source name: {@value}. */
    public static final String SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME = "systemProperties";

    private final MutablePropertySources propertySources = new MutablePropertySources();

    private final PropertyResolver propertyResolver =
            new PropertySourcesPropertyResolver(this.propertySources);

    public StandardEnvironment() {
        customizePropertySources(this.propertySources);
    }

    /**
     * Customize the set of property sources with those appropriate for any standard
     * Java environment:
     * <ul>
     * <li>{@value #SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME}
     * <li>{@value #SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME}
     * </ul>
     * <p>Properties present in {@value #SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME} will
     * take precedence over those in {@value #SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME}.
     */
    protected void customizePropertySources(MutablePropertySources propertySources) {
        propertySources.addLast(
                new PropertiesPropertySource(SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME, getSystemProperties()));
        propertySources.addLast(
                new SystemEnvironmentPropertySource(SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME, getSystemEnvironment()));
    }


    @SuppressWarnings({"rawtypes", "unchecked"})
    public Map<String, Object> getSystemProperties() {
        try {
            return (Map) System.getProperties();
        }
        catch (AccessControlException ex) {
            return (Map) new ReadOnlySystemAttributesMap() {
                @Override
                @Nullable
                protected String getSystemAttribute(String attributeName) {
                    try {
                        return System.getProperty(attributeName);
                    }
                    catch (AccessControlException ex) {
                        if (logger.isInfoEnabled()) {
                            logger.info("Caught AccessControlException when accessing system property '" +
                                    attributeName + "'; its value will be returned [null]. Reason: " + ex.getMessage());
                        }
                        return null;
                    }
                }
            };
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public Map<String, Object> getSystemEnvironment() {
        try {
            return (Map) System.getenv();
        }
        catch (AccessControlException ex) {
            return (Map) new ReadOnlySystemAttributesMap() {
                @Override
                @Nullable
                protected String getSystemAttribute(String attributeName) {
                    try {
                        return System.getenv(attributeName);
                    }
                    catch (AccessControlException ex) {
                        if (logger.isInfoEnabled()) {
                            logger.info("Caught AccessControlException when accessing system environment variable '" +
                                    attributeName + "'; its value will be returned [null]. Reason: " + ex.getMessage());
                        }
                        return null;
                    }
                }
            };
        }
    }


    //---------------------------------------------------------------------
    // Implementation of PropertyResolver interface
    //---------------------------------------------------------------------

    @Override
    public boolean containsProperty(String key) {
        return this.propertyResolver.containsProperty(key);
    }

    @Override
    @Nullable
    public String getProperty(String key) {
        return this.propertyResolver.getProperty(key);
    }

    @Override
    public String getProperty(String key, String defaultValue) {
        return this.propertyResolver.getProperty(key, defaultValue);
    }

    @Override
    @Nullable
    public <T> T getProperty(String key, Class<T> targetType) {
        return this.propertyResolver.getProperty(key, targetType);
    }

    @Override
    public <T> T getProperty(String key, Class<T> targetType, T defaultValue) {
        return this.propertyResolver.getProperty(key, targetType, defaultValue);
    }

    @Override
    public String getRequiredProperty(String key) throws IllegalStateException {
        return this.propertyResolver.getRequiredProperty(key);
    }

    @Override
    public <T> T getRequiredProperty(String key, Class<T> targetType) throws IllegalStateException {
        return this.propertyResolver.getRequiredProperty(key, targetType);
    }

    @Override
    public String resolvePlaceholders(String text) {
        return this.propertyResolver.resolvePlaceholders(text);
    }

    @Override
    public String resolveRequiredPlaceholders(String text) throws IllegalArgumentException {
        return this.propertyResolver.resolveRequiredPlaceholders(text);
    }

}
