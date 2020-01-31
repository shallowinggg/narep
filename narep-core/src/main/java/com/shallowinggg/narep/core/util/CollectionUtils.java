package com.shallowinggg.narep.core.util;

import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;

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
     *
     * @param array 被检查的数组
     */
    public static boolean isEmpty(Object[] array) {
        return (array == null || array.length == 0);
    }

    /**
     * 判断给定的是否不为空
     *
     * @param array 被检查的数组
     * @see CollectionUtils#isEmpty(Object[])
     */
    public static boolean isNotEmpty(Object[] array) {
        return !isEmpty(array);
    }

    @SuppressWarnings("unchecked")
    public static <K, V> void mergePropertiesIntoMap(@Nullable Properties props, Map<K, V> map) {
        if (props != null) {
            for (Enumeration<?> en = props.propertyNames(); en.hasMoreElements();) {
                String key = (String) en.nextElement();
                Object value = props.get(key);
                if (value == null) {
                    // Allow for defaults fallback or potentially overridden accessor...
                    value = props.getProperty(key);
                }
                map.put((K) key, (V) value);
            }
        }
    }

    private CollectionUtils() {
    }
}
