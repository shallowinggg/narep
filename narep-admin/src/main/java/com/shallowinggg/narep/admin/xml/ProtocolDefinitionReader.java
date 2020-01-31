package com.shallowinggg.narep.admin.xml;

import com.shallowinggg.narep.core.io.Resource;
import com.shallowinggg.narep.core.io.ResourceLoader;

/**
 * 读取通信协议定义的简单接口。
 * 指定了使用{@link com.shallowinggg.narep.core.io.Resource}以及{@link String}
 * 类型的位置参数的加载方法。
 *
 * @author shallowinggg
 */
public interface ProtocolDefinitionReader {

    /**
     * Return the resource loader to use for resource locations.
     * <p>A {@code null} return value suggests that absolute resource loading
     * is not available for this bean definition reader.
     * <p>This is mainly meant to be used for importing further resources
     * from within a bean definition resource, for example via the "import"
     * tag in XML bean definitions. It is recommended, however, to apply
     * such imports relative to the defining resource; only explicit full
     * resource locations will trigger absolute resource loading.
     * <p>There is also a {@code loadBeanDefinitions(String)} method available,
     * for loading bean definitions from a resource location (or location pattern).
     * This is a convenience to avoid explicit ResourceLoader handling.
     *
     * @see #loadProtocolDefinition(String)
     */
    ResourceLoader getResourceLoader();
    /**
     * Load protocol definitions from the specified resource.
     *
     * @param resource the resource descriptor
     * @return read result
     * @throws ProtocolDefinitionStoreException in case of loading or parsing errors
     */
    ProtocolDefinition loadProtocolDefinition(Resource resource) throws ProtocolDefinitionStoreException;

    /**
     * Load protocol definitions from the specified resource location.
     *
     * @param location the resource location, to be loaded with the ResourceLoader
     *                 of this bean definition reader
     * @return read result
     * @throws ProtocolDefinitionStoreException in case of loading or parsing errors
     */
    ProtocolDefinition loadProtocolDefinition(String location) throws ProtocolDefinitionStoreException;
}
