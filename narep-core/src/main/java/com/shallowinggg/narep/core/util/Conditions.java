package com.shallowinggg.narep.core.util;

/**
 * @author shallowinggg
 */
public class Conditions {

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
        if(!expr) {
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
     * @param expr 参数检验表达式
     * @param formatMsg 错误信息
     * @param objects 错误信息相关参数值
     */
    public static void checkArgument(boolean expr, String formatMsg, Object... objects) {
        if(!expr) {
            throw new IllegalArgumentException(String.format(formatMsg, objects));
        }
    }

    private Conditions() {}
}
