package com.shallowinggg.narep.core.util;

import com.sun.istack.internal.Nullable;

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
     * 检查给定的条件是否为{@literal true}。这个方法主要是用来进行构造方法和
     * 普通方法的参数验证，如果参数不符合条件那么将抛出一个
     * {@link IllegalArgumentException}异常。
     *
     * <blockquote><pre>
     *      public add(Object o) {
     *          Conditions.checkArgument(o != null);
     *      }
     * </pre></blockquote>
     *
     * @param expr 参数检验表达式
     */
    public static void checkArgument(boolean expr) {
        if (!expr) {
            throw new IllegalArgumentException();
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
     * <pre class="code">Assert.hasText(name, "'name' must not be empty");</pre>
     * @param text the String to check
     * @param message the exception message to use if the assertion fails
     * @throws IllegalArgumentException if the text does not contain valid text content
     * @see StringTinyUtils#isNotBlank(CharSequence)
     */
    public static void hasText(@Nullable String text, String message) {
        if (!StringTinyUtils.isNotBlank(text)) {
            throw new IllegalArgumentException(message);
        }
    }

    private Conditions() {
    }
}
