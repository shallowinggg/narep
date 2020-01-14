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

    /**
     * 判断给定的数组是否为空：
     * 例如： {@code null} 或者长度为0。
     * @param array 被检查的数组
     */
    public static boolean isEmpty(Object[] array) {
        return (array == null || array.length == 0);
    }

    /**
     * 判断给定的是否不为空
     * @param array 被检查的数组
     * @see CollectionUtils#isEmpty(Object[])
     */
    public static boolean isNotEmpty(Object[] array) {
        return !isEmpty(array);
    }

    private CollectionUtils() {}
}
