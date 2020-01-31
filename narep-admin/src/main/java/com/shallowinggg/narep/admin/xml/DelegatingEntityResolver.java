package com.shallowinggg.narep.admin.xml;

import com.shallowinggg.narep.core.util.Conditions;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;

/**
 * {@link EntityResolver} implementation that delegates to a {@link BeansDtdResolver}
 * and a {@link PluggableSchemaResolver} for DTDs and XML schemas, respectively.
 *
 * @author Rob Harrop
 * @author Juergen Hoeller
 * @author Rick Evans
 * @since 2.0
 * @see BeansDtdResolver
 * @see PluggableSchemaResolver
 */
public class DelegatingEntityResolver implements EntityResolver {

    /**
     * Suffix for DTD files.
     */
    public static final String DTD_SUFFIX = ".dtd";

    /**
     * Suffix for schema definition files.
     */
    public static final String XSD_SUFFIX = ".xsd";


    private final EntityResolver dtdResolver;

    private final EntityResolver schemaResolver;

    /**
     * Create a new DelegatingEntityResolver that use default
     * dtd resolver {@link BeansDtdResolver} and default
     * schema resolver {@link PluggableSchemaResolver} with
     * supplied classloader.
     *
     * @param classLoader the classloader use for PluggableSchemaResolver.
     *                    if it is null, use default classloader
     */
    public DelegatingEntityResolver(ClassLoader classLoader) {
        this.dtdResolver = new BeansDtdResolver();
        this.schemaResolver = new PluggableSchemaResolver(classLoader);
    }

    /**
     * Create a new DelegatingEntityResolver that delegate to
     * the given {@link EntityResolver EntityResolvers}.
     *
     * @param dtdResolver    the EntityResolver to resolve dtd
     * @param schemaResolver the EntityResolver to resolver xsd
     */
    public DelegatingEntityResolver(EntityResolver dtdResolver, EntityResolver schemaResolver) {
        Conditions.notNull(dtdResolver, "dtdResolver must not be null");
        Conditions.notNull(schemaResolver, "schemaResolver must not be null");
        this.dtdResolver = dtdResolver;
        this.schemaResolver = schemaResolver;
    }

    @Override
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        if (systemId != null) {
            if (systemId.endsWith(DTD_SUFFIX)) {
                return dtdResolver.resolveEntity(publicId, systemId);
            } else if (systemId.endsWith(XSD_SUFFIX)) {
                return schemaResolver.resolveEntity(publicId, systemId);
            }
        }
        return null;
    }
}
