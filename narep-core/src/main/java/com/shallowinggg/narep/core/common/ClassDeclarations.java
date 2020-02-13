package com.shallowinggg.narep.core.common;

import com.shallowinggg.narep.core.JavaCodeGenerator;
import com.shallowinggg.narep.core.lang.Modifier;
import com.shallowinggg.narep.core.util.CollectionUtils;
import com.shallowinggg.narep.core.util.StringTinyUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Utility class used to build class declaration statement.
 * <p>
 * All public methods provided in this class are anti-pattern,
 * they don't follow the principle of strategy pattern. Because
 * all usages of them are gathered in high layer of
 * {@link com.shallowinggg.narep.core.generators.AbstractJavaCodeGenerator},
 * e.g. {@link com.shallowinggg.narep.core.generators.ClassCodeGenerator}.
 * All sub class are not need to implement method {@link
 * JavaCodeGenerator#buildDeclaration()} respectively.
 *
 * <p>
 * <pre>
 *     - 接口
 *       - 常规接口     public interface Inter {
 *       - 子接口      public interface Sub extends Parent {
 *     - 枚举
 *       public enum Test {
 *     - 注解
 *       public @interface Test {
 *     - 类
 *       - 常规类      public class Car {
 *       - 子类        public class Bus extends Car {
 *       - 实现接口的类 public class ProtocolConfig implements Config {
 *       - 完整声明的类 public class Bus extends Car implements Big {
 *
 *     - 范型类
 *       - 接口
 *         - 常规接口     public interface Inter<T> {
 *         - 子接口      public interface Sub<T> extends Parent {
 *         - 类
 *           - 常规类      public class Car<T> {
 *           - 子类        public class Bus<T> extends Car {
 *           - 实现接口的类 public class ProtocolConfig<T> implements Config {
 *           - 完整声明的类 public class Bus<T> extends Car implements Big {
 * </pre>
 * <p>
 *
 * @author shallowinggg
 */
public class ClassDeclarations {
    /**
     * Cache for use, this classes are all stateless.
     */
    private static final PlainInterface PLAIN_INTERFACE = new PlainInterface();
    private static final SubInterface SUB_INTERFACE = new SubInterface();
    private static final EnumClass ENUM_CLASS = new EnumClass();
    private static final AnnotationClass ANNOTATION_CLASS = new AnnotationClass();
    private static final PlainClass PLAIN_CLASS = new PlainClass();
    private static final SubClass SUB_CLASS = new SubClass();
    private static final ImplementorClass IMPLEMENTOR_CLASS = new ImplementorClass();
    private static final CompleteClass COMPLETE_CLASS = new CompleteClass();

    public static String buildInterfaceDecl(Modifier modifier, String interfaceName, @Nullable String parentName) {
        return determineInterfaceStrategy(parentName)
                .buildDeclaration(modifier, interfaceName, parentName, null, null);
    }

    public static String buildEnumDecl(Modifier modifier, String enumName) {
        return ENUM_CLASS.buildDeclaration(modifier, enumName, null, null, null);
    }

    public static String buildAnnotationDecl(Modifier modifier, String annotationName) {
        return ANNOTATION_CLASS.buildDeclaration(modifier, annotationName, null, null, null);
    }

    public static String buildClassDecl(Modifier modifier, String className, @Nullable String parentName,
                                        @Nullable String[] interfaceNames) {
        return determineClassStrategy(parentName, interfaceNames)
                .buildDeclaration(modifier, className, parentName, interfaceNames, null);
    }

    public static String buildGenericClassDecl(Modifier modifier, String className, @Nullable String parentName,
                                               @Nullable String[] interfaceNames, List<String> generics,
                                               boolean isClass, boolean isInterface) {
        if (isClass) {
            return new GenericClass(determineClassStrategy(parentName, interfaceNames))
                    .buildDeclaration(modifier, className, parentName, interfaceNames, generics);
        } else if (isInterface) {
            return new GenericClass(determineInterfaceStrategy(parentName))
                    .buildDeclaration(modifier, className, parentName, interfaceNames, generics);
        } else {
            throw new IllegalArgumentException("generic must be class or interface");
        }
    }

    private static ClassDeclarationBuildStrategy determineClassStrategy(@Nullable String parentName,
                                                                        @Nullable String[] interfaceNames) {
        if (StringTinyUtils.isNotEmpty(parentName) && CollectionUtils.isNotEmpty(interfaceNames)) {
            return COMPLETE_CLASS;
        } else if (StringTinyUtils.isNotEmpty(parentName)) {
            return SUB_CLASS;
        } else if (CollectionUtils.isNotEmpty(interfaceNames)) {
            return IMPLEMENTOR_CLASS;
        } else {
            return PLAIN_CLASS;
        }
    }

    private static ClassDeclarationBuildStrategy determineInterfaceStrategy(@Nullable String parentName) {
        if (StringTinyUtils.isNotEmpty(parentName)) {
            return SUB_INTERFACE;
        } else {
            return PLAIN_INTERFACE;
        }
    }

    // generic class

    static class GenericClass implements ClassDeclarationBuildStrategy {
        /**
         * "class ".length()
         */
        private static final int MAX_SEARCH_START_POS = 6;

        private final ClassDeclarationBuildStrategy strategy;

        public GenericClass(ClassDeclarationBuildStrategy strategy) {
            this.strategy = strategy;
        }

        @Override
        public String buildDeclaration(Modifier modifier, String className, String parentName,
                                       String[] interfaceNames, List<String> generics) {
            String declaration = strategy.buildDeclaration(modifier, className, parentName, interfaceNames, null);
            int pos = declaration.indexOf(className, MAX_SEARCH_START_POS);
            int insertPos = pos + className.length();
            StringBuilder builder = new StringBuilder(declaration);
            builder.insert(insertPos, ClassDeclarationHelper.buildGeneric(generics));
            return builder.toString();
        }
    }

    // Interface

    static class PlainInterface implements ClassDeclarationBuildStrategy {

        @Override
        public String buildDeclaration(Modifier modifier, String className, String parentName,
                                       @Nullable String[] interfaceNames, @Nullable List<String> generics) {
            return ClassDeclarationHelper.buildInterfaceDeclaration(modifier, className);
        }

    }

    static class SubInterface implements ClassDeclarationBuildStrategy {

        @Override
        public String buildDeclaration(Modifier modifier, String className, String parentName,
                                       @Nullable String[] interfaceNames, @Nullable List<String> generics) {
            return ClassDeclarationHelper.buildInterfaceDeclaration(modifier, className, parentName);
        }
    }

    // Class

    static class PlainClass implements ClassDeclarationBuildStrategy {

        @Override
        public String buildDeclaration(Modifier modifier, String className, @Nullable String parentName,
                                       @Nullable String[] interfaceNames, @Nullable List<String> generics) {
            return ClassDeclarationHelper.buildClassDeclaration(modifier, className);
        }
    }

    static class SubClass implements ClassDeclarationBuildStrategy {

        @Override
        public String buildDeclaration(Modifier modifier, String className, String parentName,
                                       @Nullable String[] interfaceNames, @Nullable List<String> generics) {
            return ClassDeclarationHelper.buildClassDeclaration(modifier, className, parentName);
        }
    }

    static class ImplementorClass implements ClassDeclarationBuildStrategy {

        @Override
        public String buildDeclaration(Modifier modifier, String className, @Nullable String parentName,
                                       String[] interfaceNames, @Nullable List<String> generics) {
            return ClassDeclarationHelper.buildClassDeclaration(modifier, className, interfaceNames);
        }
    }

    static class CompleteClass implements ClassDeclarationBuildStrategy {

        @Override
        public String buildDeclaration(Modifier modifier, String className, String parentName,
                                       String[] interfaceNames, @Nullable List<String> generics) {
            return ClassDeclarationHelper.buildClassDeclaration(modifier, className, parentName, interfaceNames);
        }

    }

    // Enum

    static class EnumClass implements ClassDeclarationBuildStrategy {

        @Override
        public String buildDeclaration(Modifier modifier, String className, @Nullable String parentName,
                                       @Nullable String[] interfaceNames, @Nullable List<String> generics) {
            return ClassDeclarationHelper.buildEnumDeclaration(modifier, className);
        }
    }

    // Annotation

    static class AnnotationClass implements ClassDeclarationBuildStrategy {

        @Override
        public String buildDeclaration(Modifier modifier, String className, @Nullable String parentName,
                                       @Nullable String[] interfaceNames, @Nullable List<String> generics) {
            return ClassDeclarationHelper.buildAnnotationDeclaration(modifier, className);
        }
    }


    private ClassDeclarations() {
    }
}
