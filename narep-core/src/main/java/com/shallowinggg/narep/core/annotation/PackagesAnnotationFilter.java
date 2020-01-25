package com.shallowinggg.narep.core.annotation;

import com.shallowinggg.narep.core.util.Conditions;
import com.shallowinggg.narep.core.util.StringTinyUtils;
import com.sun.istack.internal.Nullable;

import java.util.Arrays;
import java.util.Objects;

/**
 * {@link AnnotationFilter} implementation used for
 * {@link AnnotationFilter#packages(String...)}.
 *
 * @author Phillip Webb
 * @since 5.2
 */
final class PackagesAnnotationFilter implements AnnotationFilter {

    private final String[] prefixes;

    private final int hashCode;


    PackagesAnnotationFilter(String... packages) {
        Objects.requireNonNull(packages, "Packages array must not be null");
        this.prefixes = new String[packages.length];
        for (int i = 0; i < packages.length; i++) {
            String pkg = packages[i];
            Conditions.hasText(pkg, "Packages array must not have empty elements");
            this.prefixes[i] = pkg + ".";
        }
        Arrays.sort(this.prefixes);
        this.hashCode = Arrays.hashCode(this.prefixes);
    }


    @Override
    public boolean matches(String annotationType) {
        for (String prefix : this.prefixes) {
            if (annotationType.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        return Arrays.equals(this.prefixes, ((PackagesAnnotationFilter) other).prefixes);
    }

    @Override
    public int hashCode() {
        return this.hashCode;
    }

    @Override
    public String toString() {
        return "Packages annotation filter: " +
                StringTinyUtils.arrayToCommaDelimitedString(this.prefixes);
    }

}

