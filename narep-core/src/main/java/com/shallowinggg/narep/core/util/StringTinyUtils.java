package com.shallowinggg.narep.core.util;

/**
 * @author shallowinggg
 */
public class StringTinyUtils {
    public static final String EMPTY_STRING = "";

    /**
     * 检查给定的<tt>CharSequence</tt>是否为{@code null}或者
     * 没有内容。
     *
     * <blockquote><pre>
     *     String s = ...;
     *     if(StringTinyUtils.isEmpty(s)) {
     *         ...
     *     }
     * </pre></blockquote>
     *
     * @param s 接受检查的<tt>CharSequence</tt>
     * @return true 如果s为null或者长度为0；否则返回false
     */
    public static boolean isEmpty(CharSequence s) {
        return s == null || s.length() == 0;
    }

    public static boolean isNotEmpty(CharSequence s) {
        return !isEmpty(s);
    }

    /**
     * 检查给定的<tt>CharSequence</tt>是否为{@code null}或者
     * 没有内容，或者全是空白字符。
     *
     * <blockquote><pre>
     *     String s = ...;
     *     if(StringTinyUtils.isBlank(s)) {
     *         ...
     *     }
     * </pre></blockquote>
     *
     * @param s 接受检查的<tt>CharSequence</tt>
     * @return true 如果s为null或者长度为0或者全是空白字符；否则返回false
     */
    public static boolean isBlank(CharSequence s) {
        if (isNotEmpty(s)) {
            int len = s.length();
            for (int i = 0; i < len; ++i) {
                if (!Character.isWhitespace(s.charAt(i))) {
                    return false;
                }
            }
        }

        return true;
    }

    public static boolean isNotBlank(CharSequence s) {
        return !isBlank(s);
    }

    public static String firstCharToUpperCase(String origin) {
        assert origin.length() > 0;
        char[] ch = origin.toCharArray();
        if (ch[0] >= 'a' && ch[0] <= 'z') {
            ch[0] = (char) (ch[0] - 32);
        }
        return new String(ch);
    }


    private StringTinyUtils() {
    }
}
