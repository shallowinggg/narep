package com.shallowinggg.narep.core.lang;

import java.util.Objects;

/**
 * 这个类用来表示字段信息，包括访问限定符，字段类型，
 * 字段名称，初始值以及注释信息。其中前三个为必需信息，
 * 后两个为可选信息。
 *
 * 通常你可以通过此类提供的三个构造方法
 * {@link FieldInfo#FieldInfo(Modifier, String, String)},
 * {@link FieldInfo#FieldInfo(Modifier, String, String, String)}以及
 * {@link FieldInfo#FieldInfo(Modifier, String, String, String, String)}
 * 来构造字段信息。当然，它们不能满足所有的构造需求，
 * 因此当你遇到此种情况时，你还可以使用其提供的
 * {@link Builder}类进行构造，它提供了流式语法以方便
 * 你的使用。
 *
 * 例如：
 * <pre><code>
 *     FieldInfo name = new FieldInfo(Modifier.PRIVATE, "String", "name", "test");
 * </code></pre>
 *
 * 上述代码将生成如下语句：
 * <pre>
 *     private String name = "test";
 * </pre>
 *
 * 注意：此类只用于生成代码，因此不会检查给定的类型
 * 是否存在以及给定的初始值是否与类型匹配。
 *
 * @see Modifier
 * @author shallowinggg
 */
public class FieldInfo {
    private final Modifier modifier;
    private final String type;
    private final String name;
    private final String initValue;
    private final String comment;

    public FieldInfo(Modifier modifier, String type, String name) {
        this(modifier, type, name, null, null);
    }

    public FieldInfo(Modifier modifier, String type, String name, String initValue) {
        this(modifier, type, name, initValue, null);
    }

    public FieldInfo(Modifier modifier, String type, String name, String initValue, String comment) {
        Objects.requireNonNull(modifier, "FieldInfo#modifier must not be null");
        Objects.requireNonNull(type, "FieldInfo#type must not be null");
        Objects.requireNonNull(name, "FieldInfo#name must not be null");

        this.modifier = modifier;
        this.type = type;
        this.name = name;
        this.initValue = initValue;
        this.comment = comment;
    }

    public static class Builder {
        private Modifier modifier;
        private String type;
        private String name;
        private String initValue;
        private String comment;

        public Builder modifier(Modifier modifier) {
            this.modifier = modifier;
            return this;
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder initValue(String initValue) {
            this.initValue = initValue;
            return this;
        }

        public Builder comment(String comment) {
            this.comment = comment;
            return this;
        }

        public FieldInfo build() {
            return new FieldInfo(modifier, type, name, initValue, comment);
        }
    }


    public Modifier getModifier() {
        return modifier;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getInitValue() {
        return initValue;
    }

    public String getComment() {
        return comment;
    }
}
