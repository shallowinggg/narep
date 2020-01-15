package com.shallowinggg.narep.core.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author shallowinggg
 */
public class Utils {
    private static Map<String, Class<?>> classMap = new HashMap<>(8);
    private static Map<String, Integer> primitiveTypeLens = new HashMap<>(8);

    static {
        classMap.put("boolean", boolean.class);
        classMap.put("byte", byte.class);
        classMap.put("short", short.class);
        classMap.put("int", int.class);
        classMap.put("long", long.class);
        classMap.put("string", String.class);
        classMap.put("map", HashMap.class);

        primitiveTypeLens.put("byte", 1);
        primitiveTypeLens.put("short", 2);
        primitiveTypeLens.put("int", 4);
        primitiveTypeLens.put("long", 4);
    }

    public static Class<?> resolveString2Class(String className) {
        return classMap.get(className);
    }

    public static int calcPrimitiveTypeLen(String primitiveType) {
        return primitiveTypeLens.getOrDefault(primitiveType, -1);
    }

    private Utils() {
    }
}
