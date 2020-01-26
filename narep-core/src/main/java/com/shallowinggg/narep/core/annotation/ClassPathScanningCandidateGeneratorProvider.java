package com.shallowinggg.narep.core.annotation;

import com.shallowinggg.narep.core.common.AnnotatedGeneratorDefinition;
import com.shallowinggg.narep.core.common.GeneratorDefinition;
import com.shallowinggg.narep.core.common.ScannedGenericGeneratorDefinition;
import com.shallowinggg.narep.core.exception.GeneratorDefinitionStoreException;
import com.shallowinggg.narep.core.io.Resource;
import com.shallowinggg.narep.core.io.support.PathMatchingResourcePatternResolver;
import com.shallowinggg.narep.core.io.support.ResourcePatternResolver;
import com.shallowinggg.narep.core.type.AnnotationMetadata;
import com.shallowinggg.narep.core.type.classreading.CachingMetadataReaderFactory;
import com.shallowinggg.narep.core.type.classreading.MetadataReader;
import com.shallowinggg.narep.core.type.classreading.MetadataReaderFactory;
import com.shallowinggg.narep.core.type.filter.AnnotationTypeFilter;
import com.shallowinggg.narep.core.type.filter.TypeFilter;
import com.shallowinggg.narep.core.util.ClassUtils;
import com.shallowinggg.narep.core.util.Conditions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author shallowinggg
 */
public class ClassPathScanningCandidateGeneratorProvider {

    static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";

    protected final Logger logger = LogManager.getLogger(getClass());

    private String resourcePattern = DEFAULT_RESOURCE_PATTERN;

    private final List<TypeFilter> includeFilters = new LinkedList<>();

    private MetadataReaderFactory metadataReaderFactory;

    private ResourcePatternResolver resourcePatternResolver;

    /**
     * Protected constructor for flexible subclass initialization.
     */
    protected ClassPathScanningCandidateGeneratorProvider() {
    }

    /**
     * Create a ClassPathScanningCandidateComponentProvider.
     * @param useDefaultFilters whether to register the default filters for the
     * {@link Generator @Generator}  stereotype annotations
     * @see #registerDefaultFilters()
     */
    public ClassPathScanningCandidateGeneratorProvider(boolean useDefaultFilters) {
        if (useDefaultFilters) {
            registerDefaultFilters();
        }
        setResourceLoader();
    }

    /**
     * Register the default filter for {@link Generator @Generator}.
     * <p>This will implicitly register all annotations that have the
     * {@link Generator @Generator} meta-annotation.
     */
    protected void registerDefaultFilters() {
        this.includeFilters.add(new AnnotationTypeFilter(Generator.class));
    }

    protected void setResourceLoader() {
        this.resourcePatternResolver = new PathMatchingResourcePatternResolver();
        this.metadataReaderFactory = new CachingMetadataReaderFactory();
    }

    /**
     * Add an include type filter to the <i>end</i> of the inclusion list.
     */
    public void addIncludeFilter(TypeFilter includeFilter) {
        this.includeFilters.add(includeFilter);
    }

    /**
     * Return the MetadataReaderFactory used by this component provider.
     */
    public final MetadataReaderFactory getMetadataReaderFactory() {
        if (this.metadataReaderFactory == null) {
            this.metadataReaderFactory = new CachingMetadataReaderFactory();
        }
        return this.metadataReaderFactory;
    }


    private ResourcePatternResolver getResourcePatternResolver() {
        if (this.resourcePatternResolver == null) {
            this.resourcePatternResolver = new PathMatchingResourcePatternResolver();
        }
        return this.resourcePatternResolver;
    }

    public void setResourcePattern(String resourcePattern) {
        Conditions.notNull(resourcePattern, "'resourcePattern' must not be null");
        this.resourcePattern = resourcePattern;
    }

    /**
     * Scan the class path for candidate components.
     *
     * @param basePackage the package to check for annotated classes
     * @return a corresponding Set of autodetected bean definitions
     */
    public Set<GeneratorDefinition> findCandidateComponents(String basePackage) {
        return scanCandidateComponents(basePackage);
    }

    private Set<GeneratorDefinition> scanCandidateComponents(String basePackage) {
        Set<GeneratorDefinition> candidates = new LinkedHashSet<>();
        try {
            String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                    resolveBasePackage(basePackage) + '/' + this.resourcePattern;
            Resource[] resources = getResourcePatternResolver().getResources(packageSearchPath);
            boolean traceEnabled = logger.isTraceEnabled();
            boolean debugEnabled = logger.isDebugEnabled();
            for (Resource resource : resources) {
                if (traceEnabled) {
                    logger.trace("Scanning " + resource);
                }
                if (resource.isReadable()) {
                    try {
                        MetadataReader metadataReader = getMetadataReaderFactory().getMetadataReader(resource);
                        if (isCandidateComponent(metadataReader)) {
                            ScannedGenericGeneratorDefinition sbd = new ScannedGenericGeneratorDefinition(metadataReader);
                            sbd.setResource(resource);
                            if (isCandidateComponent(sbd)) {
                                if (debugEnabled) {
                                    logger.debug("Identified candidate component class: " + resource);
                                }
                                candidates.add(sbd);
                            } else {
                                if (debugEnabled) {
                                    logger.debug("Ignored because not a concrete top-level class: " + resource);
                                }
                            }
                        } else {
                            if (traceEnabled) {
                                logger.trace("Ignored because not matching any filter: " + resource);
                            }
                        }
                    } catch (Throwable ex) {
                        throw new GeneratorDefinitionStoreException(
                                "Failed to read candidate component class: " + resource, ex);
                    }
                } else {
                    if (traceEnabled) {
                        logger.trace("Ignored because not readable: " + resource);
                    }
                }
            }
        } catch (IOException ex) {
            throw new GeneratorDefinitionStoreException("I/O failure during classpath scanning", ex);
        }
        return candidates;
    }

    /**
     * Resolve the specified base package into a pattern specification for
     * the package search path.
     * <p>The default implementation resolves placeholders against system properties,
     * and converts a "."-based package path to a "/"-based resource path.
     *
     * @param basePackage the base package as specified by the user
     * @return the pattern specification to be used for package searching
     */
    protected String resolveBasePackage(String basePackage) {
        return ClassUtils.convertClassNameToResourcePath(basePackage);
    }

    /**
     * Determine whether the given class does not match any exclude filter
     * and does match at least one include filter.
     * @param metadataReader the ASM ClassReader for the class
     * @return whether the class qualifies as a candidate component
     */
    protected boolean isCandidateComponent(MetadataReader metadataReader) throws IOException {
        for (TypeFilter tf : this.includeFilters) {
            if (tf.match(metadataReader, getMetadataReaderFactory())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determine whether the given bean definition qualifies as candidate.
     * <p>The default implementation checks whether the class is not an interface
     * and not dependent on an enclosing class.
     * <p>Can be overridden in subclasses.
     * @param beanDefinition the bean definition to check
     * @return whether the bean definition qualifies as a candidate component
     */
    protected boolean isCandidateComponent(AnnotatedGeneratorDefinition beanDefinition) {
        AnnotationMetadata metadata = beanDefinition.getMetadata();
        return (metadata.isIndependent() && metadata.isConcrete());
    }
}

