package com.shallowinggg.narep.admin.xml;

import org.jetbrains.annotations.Nullable;

/**
 * Used by the {@link DefaultProtocolDefinitionDocumentReader} to
 * locate a {@link NamespaceHandler} implementation for a particular namespace URI.
 *
 * @author Rob Harrop
 * @since 2.0
 * @see NamespaceHandler
 */
@FunctionalInterface
public interface NamespaceHandlerResolver {

    /**
     * Resolve the namespace URI and return the located {@link NamespaceHandler}
     * implementation.
     * @param namespaceUri the relevant namespace URI
     * @return the located {@link NamespaceHandler} (may be {@code null})
     */
    @Nullable
    NamespaceHandler resolve(String namespaceUri);

}
