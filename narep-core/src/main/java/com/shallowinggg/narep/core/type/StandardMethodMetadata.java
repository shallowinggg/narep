package com.shallowinggg.narep.core.type;

import com.shallowinggg.narep.core.annotation.AnnotatedElementUtils;
import com.shallowinggg.narep.core.annotation.MergedAnnotations;
import com.shallowinggg.narep.core.annotation.MergedAnnotations.SearchStrategy;
import com.shallowinggg.narep.core.annotation.RepeatableContainers;
import com.shallowinggg.narep.core.util.Conditions;
import com.shallowinggg.narep.core.util.MultiValueMap;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;

/**
 * {@link MethodMetadata} implementation that uses standard reflection
 * to introspect a given {@code Method}.
 *
 * @author Juergen Hoeller
 * @author Mark Pollack
 * @author Chris Beams
 * @author Phillip Webb
 * @since 3.0
 */
public class StandardMethodMetadata implements MethodMetadata {

    private final Method introspectedMethod;

    private final boolean nestedAnnotationsAsMap;

    private final MergedAnnotations mergedAnnotations;


    /**
     * Create a new StandardMethodMetadata wrapper for the given Method.
     * @param introspectedMethod the Method to introspect
     * @deprecated since 5.2 in favor of obtaining instances via {@link AnnotationMetadata}
     */
    @Deprecated
    public StandardMethodMetadata(Method introspectedMethod) {
        this(introspectedMethod, false);
    }

    @Deprecated
    public StandardMethodMetadata(Method introspectedMethod, boolean nestedAnnotationsAsMap) {
        Conditions.notNull(introspectedMethod, "Method must not be null");
        this.introspectedMethod = introspectedMethod;
        this.nestedAnnotationsAsMap = nestedAnnotationsAsMap;
        this.mergedAnnotations = MergedAnnotations.from(
                introspectedMethod, SearchStrategy.DIRECT, RepeatableContainers.none());
    }

    @Override
    public MergedAnnotations getAnnotations() {
        return this.mergedAnnotations;
    }

    /**
     * Return the underlying Method.
     */
    public final Method getIntrospectedMethod() {
        return this.introspectedMethod;
    }

    @Override
    public String getMethodName() {
        return this.introspectedMethod.getName();
    }

    @Override
    public String getDeclaringClassName() {
        return this.introspectedMethod.getDeclaringClass().getName();
    }

    @Override
    public String getReturnTypeName() {
        return this.introspectedMethod.getReturnType().getName();
    }

    @Override
    public boolean isAbstract() {
        return Modifier.isAbstract(this.introspectedMethod.getModifiers());
    }

    @Override
    public boolean isStatic() {
        return Modifier.isStatic(this.introspectedMethod.getModifiers());
    }

    @Override
    public boolean isFinal() {
        return Modifier.isFinal(this.introspectedMethod.getModifiers());
    }

    @Override
    public boolean isOverridable() {
        return !isStatic() && !isFinal() && !isPrivate();
    }

    private boolean isPrivate() {
        return Modifier.isPrivate(this.introspectedMethod.getModifiers());
    }

    @Override
    public Map<String, Object> getAnnotationAttributes(String annotationName, boolean classValuesAsString) {
        if (this.nestedAnnotationsAsMap) {
            return MethodMetadata.super.getAnnotationAttributes(annotationName, classValuesAsString);
        }
        return AnnotatedElementUtils.getMergedAnnotationAttributes(this.introspectedMethod,
                annotationName, classValuesAsString, false);
    }

    @Override
    public MultiValueMap<String, Object> getAllAnnotationAttributes(String annotationName, boolean classValuesAsString) {
        if (this.nestedAnnotationsAsMap) {
            return MethodMetadata.super.getAllAnnotationAttributes(annotationName, classValuesAsString);
        }
        return AnnotatedElementUtils.getAllAnnotationAttributes(this.introspectedMethod,
                annotationName, classValuesAsString, false);
    }

}
