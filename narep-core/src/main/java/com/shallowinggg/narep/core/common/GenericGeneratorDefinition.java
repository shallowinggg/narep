package com.shallowinggg.narep.core.common;

import com.shallowinggg.narep.core.io.Resource;

/**
 * @author shallowinggg
 */
public class GenericGeneratorDefinition implements GeneratorDefinition {
    private Class<?> clazz;
    private String className;
    private Resource resource;
    private String profiler;

    public GenericGeneratorDefinition() {
    }

    @Override
    public Class<?> getClazz() {
        return clazz;
    }

    @Override
    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    public String getClassName() {
        return className;
    }

    @Override
    public void setClassName(String className) {
        this.className = className;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public String getProfiler() {
        return profiler;
    }

    @Override
    public void setProfiler(String profiler) {
        this.profiler = profiler;
    }
}
