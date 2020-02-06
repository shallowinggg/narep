package com.shallowinggg.narep.core.type;

import com.shallowinggg.narep.core.util.StringTinyUtils;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Modifier;
import java.util.LinkedHashSet;
import java.util.Objects;

/**
 * {@link ClassMetadata} implementation that uses standard reflection
 * to introspect a given {@code Class}.
 *
 * @author Juergen Hoeller
 */
public class StandardClassMetadata implements ClassMetadata {

    private final Class<?> introspectedClass;

    /**
     * Create a new StandardClassMetadata wrapper for the given Class.
     * @param introspectedClass the Class to introspect
     * @deprecated since 5.2 in favor of {@link StandardAnnotationMetadata}
     */
    @Deprecated
    public StandardClassMetadata(Class<?> introspectedClass) {
        Objects.requireNonNull(introspectedClass, "Class must not be null");
        this.introspectedClass = introspectedClass;
    }

    /**
     * Return the underlying Class.
     */
    public final Class<?> getIntrospectedClass() {
        return this.introspectedClass;
    }


    @Override
    public String getClassName() {
        return this.introspectedClass.getName();
    }

    @Override
    public boolean isInterface() {
        return this.introspectedClass.isInterface();
    }

    @Override
    public boolean isAnnotation() {
        return this.introspectedClass.isAnnotation();
    }

    @Override
    public boolean isAbstract() {
        return Modifier.isAbstract(this.introspectedClass.getModifiers());
    }

    @Override
    public boolean isFinal() {
        return Modifier.isFinal(this.introspectedClass.getModifiers());
    }

    @Override
    public boolean isIndependent() {
        return (!hasEnclosingClass() ||
                (this.introspectedClass.getDeclaringClass() != null &&
                        Modifier.isStatic(this.introspectedClass.getModifiers())));
    }

    @Override
    @Nullable
    public String getEnclosingClassName() {
        Class<?> enclosingClass = this.introspectedClass.getEnclosingClass();
        return (enclosingClass != null ? enclosingClass.getName() : null);
    }

    @Override
    @Nullable
    public String getSuperClassName() {
        Class<?> superClass = this.introspectedClass.getSuperclass();
        return (superClass != null ? superClass.getName() : null);
    }

    @Override
    public String[] getInterfaceNames() {
        Class<?>[] interfaces = this.introspectedClass.getInterfaces();
        String[] ifcNames = new String[interfaces.length];
        for (int i = 0; i < interfaces.length; i++) {
            ifcNames[i] = interfaces[i].getName();
        }
        return ifcNames;
    }

    @Override
    public String[] getMemberClassNames() {
        LinkedHashSet<String> memberClassNames = new LinkedHashSet<>(4);
        for (Class<?> nestedClass : this.introspectedClass.getDeclaredClasses()) {
            memberClassNames.add(nestedClass.getName());
        }
        return StringTinyUtils.toStringArray(memberClassNames);
    }

}
