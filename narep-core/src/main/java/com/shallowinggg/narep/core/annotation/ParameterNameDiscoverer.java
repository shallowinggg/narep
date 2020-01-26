package com.shallowinggg.narep.core.annotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public interface ParameterNameDiscoverer {

    String[] getParameterNames(Method method);

    String[] getParameterNames(Constructor<?> ctor);
}
