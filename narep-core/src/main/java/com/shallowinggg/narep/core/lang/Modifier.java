package com.shallowinggg.narep.core.lang;

/**
 * 访问限定符枚举，类、字段以及方法限定符统一在这个类中。
 * <p>
 * 注意：此枚举类重写了其{@link Modifier#toString()}方法，
 * 并且不符合规范语义，只用于方便生成代码使用。
 *
 * @author shallowinggg
 */
public enum Modifier {

    /**
     * private
     */
    PRIVATE("private "),
    /**
     * protected
     */
    PROTECTED("protected "),
    /**
     * 包默认级别
     */
    PACKAGE(""),
    /**
     * public
     */
    PUBLIC("public "),

    PUBLIC_STATIC_FINAL("public static final "),
    PUBLIC_STATIC("public static "),
    PRIVATE_STATIC_FINAL("private static final "),
    PRIVATE_STATIC("private static "),
    PRIVATE_FINAL("private final "),
    PRIVATE_VOLATILE("private volatile "),
    PROTECTED_FINAL("protected final "),
    PROTECTED_VOLATILE("protected volatile ");

    private String value;

    Modifier(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
