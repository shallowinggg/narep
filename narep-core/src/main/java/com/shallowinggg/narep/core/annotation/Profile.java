package com.shallowinggg.narep.core.annotation;

import java.lang.annotation.*;

/**
 * Indicates that a generator is eligible for registration when a
 * {@linkplain #value specified profile} are active.
 *
 * <p>The {@code @Profile} annotation may be used in the following way:
 * <ul>
 * <li>as a type-level annotation on any class directly annotated with
 * {@code @Generator}</li>
 * <p>
 * The {@code @Profile} annotation only works when it is annotated with
 * classes in package "com.shallowinggg.core.narep.generators".
 *
 * @author shallowinggg
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Profile {
    String value();
}
