package com.shallowinggg.narep.core.util;

import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.function.Supplier;

/**
 * @author shallowinggg
 */
public class Conditions {

    /**
     * Assert that an object is not {@code null}.
     * <pre class="code">Assert.notNull(clazz, "The class must not be null");</pre>
     * @param object the object to check
     * @param message the exception message to use if the assertion fails
     * @throws IllegalArgumentException if the object is {@code null}
     */
    public static void notNull(@Nullable Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Assert that an object is not {@code null}.
     * <pre class="code">
     * Assert.notNull(clazz, () -&gt; "The class '" + clazz.getName() + "' must not be null");
     * </pre>
     * @param object the object to check
     * @param messageSupplier a supplier for the exception message to use if the
     * assertion fails
     * @throws IllegalArgumentException if the object is {@code null}
     * @since 5.0
     */
    public static void notNull(@Nullable Object object, Supplier<String> messageSupplier) {
        if (object == null) {
            throw new IllegalArgumentException(nullSafeGet(messageSupplier));
        }
    }

    /**
     * Assert that a CharSequence is not empty.
     *
     * <blockquote><pre>
     *     String s = ...;
     *     Conditions.notEmpty(s, "s must not be empty");
     * </pre></blockquote>
     *
     * @param s the CharSequence to check
     * @param msg the exception message to use if the assertion fails
     * @see StringTinyUtils#isEmpty(CharSequence)
     */
    public static void notEmpty(CharSequence s, String msg) {
        if(StringTinyUtils.isEmpty(s)) {
            throw new IllegalArgumentException(msg);
        }
    }

    /**
     * Assert that a collection is not empty.
     *
     * <blockqupte><pre>
     *     List<Object> l = ...;
     *     Conditions.notEmpty(l, "l must not be empty");
     * </pre></blockqupte>
     *
     * @param collection the collection to check
     * @param msg the exception message to use if the assertion fails
     * @param <T> the collection element type
     */
    public static <T> void notEmpty(Collection<T> collection, String msg) {
        if(CollectionUtils.isEmpty(collection)) {
            throw new IllegalArgumentException(msg);
        }
    }

    /**
     * Assert that an array is not empty.
     *
     * <blockqupte><pre>
     *     int[] l = ...;
     *     Conditions.notEmpty(l, "l must not be empty");
     * </pre></blockqupte>
     *
     * @param array the array to check
     * @param msg the exception message to use if the assertion fails
     */
    public static void notEmpty(Object[] array, String msg) {
        if(CollectionUtils.isEmpty(array)) {
            throw new IllegalArgumentException(msg);
        }
    }

    /**
     * 检查给定的条件是否为{@literal true}。这个方法主要是用来进行构造方法和
     * 普通方法的参数验证，如果参数不符合条件那么将抛出一个定制化的
     * {@link IllegalArgumentException}异常。
     *
     * <blockquote><pre>
     *     public add(Object o) {
     *         Conditions.checkArgument(o != null, "o must not be null");
     *     }
     * </pre></blockquote>
     *
     * @param expr      参数检验表达式
     * @param formatMsg 错误信息
     * @param objects   错误信息相关参数值
     */
    public static void checkArgument(boolean expr, String formatMsg, Object... objects) {
        if (!expr) {
            throw new IllegalArgumentException(String.format(formatMsg, objects));
        }
    }

    public static void checkState(boolean expr, String formatMsg, Object... objects) {
        if (!expr) {
            throw new IllegalStateException(String.format(formatMsg, objects));
        }
    }

    /**
     * Assert that the given String contains valid text content; that is, it must not
     * be {@code null} and must contain at least one non-whitespace character.
     * <pre class="code">Conditions.hasText(name, "'name' must not be empty");</pre>
     * @param text the String to check
     * @param message the exception message to use if the assertion fails
     * @throws IllegalArgumentException if the text does not contain valid text content
     * @see StringTinyUtils#isNotBlank(CharSequence)
     */
    public static void hasText(@Nullable String text, String message) {
        if (StringTinyUtils.isBlank(text)) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Assert that the provided object is an instance of the provided class.
     * <pre class="code">Assert.instanceOf(Foo.class, foo);</pre>
     * @param type the type to check against
     * @param obj the object to check
     * @throws IllegalArgumentException if the object is not an instance of type
     */
    public static void isInstanceOf(Class<?> type, @Nullable Object obj) {
        isInstanceOf(type, obj, "");
    }

    /**
     * Assert that the provided object is an instance of the provided class.
     * <pre class="code">Assert.instanceOf(Foo.class, foo, "Foo expected");</pre>
     * @param type the type to check against
     * @param obj the object to check
     * @param message a message which will be prepended to provide further context.
     * If it is empty or ends in ":" or ";" or "," or ".", a full exception message
     * will be appended. If it ends in a space, the name of the offending object's
     * type will be appended. In any other case, a ":" with a space and the name
     * of the offending object's type will be appended.
     * @throws IllegalArgumentException if the object is not an instance of type
     */
    public static void isInstanceOf(Class<?> type, @Nullable Object obj, String message) {
        notNull(type, "Type to check against must not be null");
        if (!type.isInstance(obj)) {
            instanceCheckFailed(type, obj, message);
        }
    }

    private static void instanceCheckFailed(Class<?> type, @Nullable Object obj, @Nullable String msg) {
        String className = (obj != null ? obj.getClass().getName() : "null");
        String result = "";
        boolean defaultMessage = true;
        if (StringTinyUtils.isNotEmpty(msg)) {
            if (endsWithSeparator(msg)) {
                result = msg + " ";
            }
            else {
                result = messageWithTypeName(msg, className);
                defaultMessage = false;
            }
        }
        if (defaultMessage) {
            result = result + ("Object of class [" + className + "] must be an instance of " + type);
        }
        throw new IllegalArgumentException(result);
    }

    public static void isAssignable(Class<?> superType, @Nullable Class<?> subType, Supplier<String> messageSupplier) {
        notNull(superType, "Super type to check against must not be null");
        if (subType == null || !superType.isAssignableFrom(subType)) {
            assignableCheckFailed(superType, subType, nullSafeGet(messageSupplier));
        }
    }

    private static void assignableCheckFailed(Class<?> superType, @Nullable Class<?> subType, @Nullable String msg) {
        String result = "";
        boolean defaultMessage = true;
        if (StringTinyUtils.isNotEmpty(msg)) {
            if (endsWithSeparator(msg)) {
                result = msg + " ";
            }
            else {
                result = messageWithTypeName(msg, subType);
                defaultMessage = false;
            }
        }
        if (defaultMessage) {
            result = result + (subType + " is not assignable to " + superType);
        }
        throw new IllegalArgumentException(result);
    }

    public static void isTrue(boolean expression, Supplier<String> messageSupplier) {
        if (!expression) {
            throw new IllegalArgumentException(nullSafeGet(messageSupplier));
        }
    }

    private static boolean endsWithSeparator(String msg) {
        return (msg.endsWith(":") || msg.endsWith(";") || msg.endsWith(",") || msg.endsWith("."));
    }

    private static String messageWithTypeName(String msg, @Nullable Object typeName) {
        return msg + (msg.endsWith(" ") ? "" : ": ") + typeName;
    }

    private static String nullSafeGet(@Nullable Supplier<String> messageSupplier) {
        return (messageSupplier != null ? messageSupplier.get() : null);
    }

    private Conditions() {
    }
}
