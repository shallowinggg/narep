package com.shallowinggg.narep.core.common;

/**
 * 字段基本元信息，只提供给生成器使用。
 *
 * @author shallowinggg
 */
public class FieldMetaData {
    private Modifier modifier;
    private String clazz;
    private String name;
    private String defaultValue;
    private String comment;

    public FieldMetaData(Modifier modifier, String clazz, String name) {
        this.modifier = modifier;
        this.clazz = clazz;
        this.name = name;
    }

    public FieldMetaData(Modifier modifier, String clazz, String name, String defaultValue) {
        this.modifier = modifier;
        this.clazz = clazz;
        this.name = name;
        this.defaultValue = defaultValue;
    }

    public FieldMetaData() {
    }

    /**
     * 访问限定符
     * <p>
     * 注意：此枚举类重写了{@link Modifier#toString()}方法，
     * 并且不符合规范语义，只用于生成代码使用。
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

    public Modifier getModifier() {
        return modifier;
    }

    public void setModifier(Modifier modifier) {
        this.modifier = modifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
