package com.shallowinggg.narep.admin.xml;

import com.shallowinggg.narep.core.env.Environment;
import com.shallowinggg.narep.core.env.EnvironmentCapable;
import com.shallowinggg.narep.core.env.StandardEnvironment;
import com.shallowinggg.narep.core.io.Resource;
import com.shallowinggg.narep.core.io.ResourceLoader;
import com.shallowinggg.narep.core.io.support.PathMatchingResourcePatternResolver;
import com.shallowinggg.narep.core.io.support.ResourcePatternResolver;
import com.shallowinggg.narep.core.util.Conditions;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * Abstract base class for bean definition readers which implement
 * the {@link ProtocolDefinitionReader} interface.
 *
 * @author shallowinggg
 */
public abstract class AbstractProtocolDefinitionReader implements ProtocolDefinitionReader, EnvironmentCapable {
    protected static Logger logger = LoggerFactory.getLogger(AbstractProtocolDefinitionReader.class);

    @Nullable
    private ResourceLoader resourceLoader;

    private Environment environment;

    protected AbstractProtocolDefinitionReader() {
        this.resourceLoader = new PathMatchingResourcePatternResolver();
        this.environment = new StandardEnvironment();
    }

    /**
     * Set the ResourceLoader to use for resource locations.
     * If specifying a ResourcePatternResolver, the bean definition reader
     * will be capable of resolving resource patterns to Resource arrays.
     * <p>Default is PathMatchingResourcePatternResolver, also capable of
     * resource pattern resolving through the ResourcePatternResolver interface.
     * <p>Setting this to {@code null} suggests that absolute resource loading
     * is not available for this bean definition reader.
     *
     * @see ResourcePatternResolver
     * @see PathMatchingResourcePatternResolver
     */
    public void setResourceLoader(@Nullable ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    @Nullable
    public ResourceLoader getResourceLoader() {
        return this.resourceLoader;
    }

    /**
     * Set the Environment to use when reading bean definitions. Most often used
     * for evaluating profile information to determine which bean definitions
     * should be read and which should be omitted.
     */
    public void setEnvironment(Environment environment) {
        Conditions.notNull(environment, "Environment must not be null");
        this.environment = environment;
    }

    @Override
    public Environment getEnvironment() {
        return this.environment;
    }

    @Override
    public ProtocolDefinition loadProtocolDefinition(String location) throws ProtocolDefinitionStoreException {
        return loadProtocolDefinition(location, null);
    }

    public ProtocolDefinition loadProtocolDefinition(String location, @Nullable Set<Resource> actualResources)
            throws ProtocolDefinitionStoreException {
        ResourceLoader resourceLoader = getResourceLoader();
        if (resourceLoader == null) {
            throw new ProtocolDefinitionStoreException(
                    "Cannot load bean definitions from location [" + location + "]: no ResourceLoader available");
        }

        // Can only load single resources by absolute URL.
        Resource resource = resourceLoader.getResource(location);
        ProtocolDefinition protocolDefinition = loadProtocolDefinition(resource);
        if (actualResources != null) {
            actualResources.add(resource);
        }
        if (logger.isTraceEnabled()) {
            logger.trace("Loaded protocol definitions from location [" + location + "]");
        }
        return protocolDefinition;

    }
}
