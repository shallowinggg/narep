package com.shallowinggg.narep.core.common;

import com.shallowinggg.narep.core.lang.Modifier;

import java.util.List;

/**
 * This interface is a strategy that builds class declaration
 * statement.
 * <p>
 * There are various declarations in Java, e.g. class, interface,
 * enum, annotation. Besides, {@literal extends} and
 * {@literal implements} are used often too. For each case, it
 * should create a strategy.
 *
 * @author shallowinggg
 * @see com.shallowinggg.narep.core.common.ClassDeclarations.PlainClass
 * @see com.shallowinggg.narep.core.common.ClassDeclarations.SubClass
 * @see com.shallowinggg.narep.core.common.ClassDeclarations.ImplementorClass
 * @see com.shallowinggg.narep.core.common.ClassDeclarations.CompleteClass
 * @see com.shallowinggg.narep.core.common.ClassDeclarations.PlainInterface
 * @see com.shallowinggg.narep.core.common.ClassDeclarations.SubInterface
 * @see com.shallowinggg.narep.core.common.ClassDeclarations.EnumClass
 * @see com.shallowinggg.narep.core.common.ClassDeclarations.AnnotationClass
 * @see com.shallowinggg.narep.core.common.ClassDeclarations.GenericClass
 */
public interface ClassDeclarationBuildStrategy {
    /**
     * Build class declaration statement with given params.
     * e.g.
     * <blockquote><pre>
     *         public class Bus extends Car {
     * </pre></blockquote>
     *
     * @param modifier       access modifier
     * @param className      class name
     * @param parentName     parent class name, maybe null
     * @param interfaceNames interface names that implement, maybe null
     * @param generics       generic names, maybe null
     * @return class declaration statement
     */
    String buildDeclaration(Modifier modifier, String className, String parentName,
                            String[] interfaceNames, List<String> generics);
}
