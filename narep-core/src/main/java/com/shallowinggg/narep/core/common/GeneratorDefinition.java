package com.shallowinggg.narep.core.common;

/**
 * @author shallowinggg
 */
public interface GeneratorDefinition {

    String getClassName();

    void setClassName(String className);

    Class<?> getClazz();

    void setClazz(Class<?> clazz);

    String getProfile();

    void setProfile(String profile);
}
