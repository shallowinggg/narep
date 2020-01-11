package com.shallowinggg.narep.core.util;

import java.util.Collection;

/**
 * @author shallowinggg
 */
public class CollectionUtils {

    public static <T> boolean isEmpty(Collection<T> collection) {
        return collection == null || collection.isEmpty();
    }

    public static <T> boolean isNotEmpty(Collection<T> collection) {
        return collection != null && !collection.isEmpty();
    }

    private CollectionUtils() {}
}
