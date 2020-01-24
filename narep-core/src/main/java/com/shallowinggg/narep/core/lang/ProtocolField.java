package com.shallowinggg.narep.core.lang;

import java.util.Objects;

/**
 * * 通信协议字段配置信息
 * <p>
 * TODO: 支持位字段
 *
 * @author shallowinggg
 */
public class ProtocolField implements Comparable<ProtocolField> {
    /**
     * 字段名称
     */
    private String name;

    /**
     * 字段类型
     * <p>
     * 只支持{@code byte, short, int, long}四种基本类型以及
     * {@link String}和{@link java.util.HashMap}两种组合类型。
     */
    private Class<?> clazz;

    /**
     * 字段长度
     * <p>
     * 使用基本类型时可以指定其具体长度，并不强制指定为其默认长度。
     * 例如： int 3，  表示此字段在协议中只占据三个字节，而非
     * int类型本身的四个字节。使用组合类型时无需指定字节长度，
     * 默认为-1，具体长度实时计算。
     */
    private int len;

    public ProtocolField(String name, Class<?> clazz, int len) {
        this.name = name;
        this.clazz = clazz;
        this.len = len;
    }

    @Override
    public int compareTo(ProtocolField o) {
        return len - o.getLen();
    }

    public String getName() {
        return name;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public int getLen() {
        return len;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProtocolField that = (ProtocolField) o;
        return len == that.len &&
                name.equals(that.name) &&
                clazz.equals(that.clazz);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, clazz, len);
    }

    @Override
    public String toString() {
        return "ProtocolField[" +
                "name='" + name + '\'' +
                ", clazz=" + clazz +
                ", len=" + len +
                ']';
    }
}
