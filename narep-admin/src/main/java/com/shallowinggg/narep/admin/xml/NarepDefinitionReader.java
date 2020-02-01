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
public interface NarepDefinitionReader {

    /**
     * Return the resource loader to use for resource locations.
     * <p>A {@code null} return value suggests that absolute resource loading
     * is not available for this bean definition reader.
     *
     * @see #loadNarepDefinition(String)
     */
    ResourceLoader getResourceLoader();

    /**
     * Load narep definitions from the specified resource.
     *
     * @param resource the resource descriptor
     * @return read result
     * @throws NarepDefinitionStoreException in case of loading or parsing errors
     */
    NarepDefinition loadNarepDefinition(Resource resource) throws NarepDefinitionStoreException;

    /**
     * Load narep definitions from the specified resource location.
     *
     * @param location the resource location, to be loaded with the ResourceLoader
     *                 of this narep definition reader
     * @return read result
     * @throws NarepDefinitionStoreException in case of loading or parsing errors
     */
    NarepDefinition loadNarepDefinition(String location) throws NarepDefinitionStoreException;
}
