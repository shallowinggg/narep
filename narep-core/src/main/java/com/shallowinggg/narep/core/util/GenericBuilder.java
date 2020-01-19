package com.shallowinggg.narep.core.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 通用的Builder模式
 *
 * @author shallowinggg
 */
public class GenericBuilder<T> {
    private final Supplier<T> instantiate;
    private List<Consumer<T>> instantiateModifiers = new ArrayList<>();

    public GenericBuilder(Supplier<T> instantiate) {
        this.instantiate = instantiate;
    }

    public static <T> GenericBuilder<T> of(Supplier<T> instantiate) {
        return new GenericBuilder<>(instantiate);
    }

    public <U> GenericBuilder<T> with(BiConsumer<T, U> consumer, U value) {
        Consumer<T> c = instance -> consumer.accept(instance, value);
        instantiateModifiers.add(c);
        return this;
    }

    public T build() {
        T value = instantiate.get();
        instantiateModifiers.forEach(modifier -> modifier.accept(value));
        instantiateModifiers.clear();
        return value;
    }
}
