package com.shallowinggg.narep.core.util;

import java.util.Objects;

/**
 * @author shallowinggg
 */
public class Pair<A, B> {
    public final A first;
    public final B second;

    public Pair(A first, B second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public String toString() {
        return "Pair[" + this.first + "," + this.second + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(first, pair.first) &&
                Objects.equals(second, pair.second);
    }

    @Override
    public int hashCode() {
        if (this.first == null) {
            return this.second == null ? 0 : this.second.hashCode() + 1;
        } else {
            return this.second == null ? this.first.hashCode() + 2 : this.first.hashCode() * 17 + this.second.hashCode();
        }
    }

    public static <A, B> Pair<A, B> of(A var0, B var1) {
        return new Pair<>(var0, var1);
    }
}
