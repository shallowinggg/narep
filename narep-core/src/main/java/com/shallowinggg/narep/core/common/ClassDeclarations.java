package com.shallowinggg.narep.core.common;

import com.shallowinggg.narep.core.lang.Modifier;
import com.shallowinggg.narep.core.util.CollectionUtils;
import com.shallowinggg.narep.core.util.StringTinyUtils;

import java.util.List;

/**
 * 生成类声明语句的工具类，其中定义了所有可能的声明形式。
 *
 * @author shallowinggg
 */
public class ClassDeclarations {

    /**
     * 构造类声明策略。
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
     * <p>
     * 注意：此方法不会判定给定的参数是否符合规范，因此只会
     * 忽略多余的参数，不要依赖此方法进行参数检查。
     *
     * @param modifier       访问限定符
     * @param className      类名称
     * @param parentName     父类名称
     * @param interfaceNames 接口列表
     * @param generics       范型列表
     * @param isInterface    是否为接口
     * @param isEnum         是否为枚举
     * @param isAnnotation   是否为注解
     * @param isGeneric      是否为范型
     * @return 类声明策略
     */
    public static ClassDeclarationBuildStrategy buildStrategy(Modifier modifier, String className, String parentName,
                                                              String[] interfaceNames, List<String> generics,
                                                              boolean isInterface, boolean isEnum,
                                                              boolean isAnnotation, boolean isGeneric) {
        if (isGeneric) {
            if (isEnum || isAnnotation) {
                throw new IllegalArgumentException("enum or annotation type can't use generic");
            }
            ClassDeclarationBuildStrategy strategy = buildStrategy(modifier, className, parentName, interfaceNames,
                    generics, isInterface, false, false, false);
            return new GenericDeclarationStrategyWrapper(strategy, className, generics);
        }
        if (isInterface) {
            if (StringTinyUtils.isNotEmpty(parentName)) {
                return new SubInterface(modifier, className, parentName);
            } else {
                return new PlainInterface(modifier, className);
            }
        } else if (isEnum) {
            return new EnumClass(modifier, className);
        } else if (isAnnotation) {
            return new AnnotationClass(modifier, className);
        } else {
            if (StringTinyUtils.isNotEmpty(parentName) && CollectionUtils.isNotEmpty(interfaceNames)) {
                return new CompleteClass(modifier, className, parentName, interfaceNames);
            } else if (StringTinyUtils.isNotEmpty(parentName)) {
                return new SubClass(modifier, className, parentName);
            } else if (CollectionUtils.isNotEmpty(interfaceNames)) {
                return new ImplementorClass(modifier, className, interfaceNames);
            } else {
                return new PlainClass(modifier, className);
            }
        }
    }

    static class DeclarationStrategyWrapper implements ClassDeclarationBuildStrategy {
        ClassDeclarationBuildStrategy strategy;

        DeclarationStrategyWrapper(ClassDeclarationBuildStrategy strategy) {
            this.strategy = strategy;
        }

        @Override
        public String buildDeclaration() {
            return strategy.buildDeclaration();
        }

    }

    static class GenericDeclarationStrategyWrapper extends DeclarationStrategyWrapper {
        /**
         * "class ".length()
         */
        private static final int MAX_SEARCH_START_POS = 6;
        private String name;
        private List<String> generics;

        GenericDeclarationStrategyWrapper(ClassDeclarationBuildStrategy strategy,
                                          String name, List<String> generics) {
            super(strategy);
            this.name = name;
            this.generics = generics;
        }

        @Override
        public String buildDeclaration() {
            String declaration = super.buildDeclaration();
            int pos = declaration.indexOf(name, MAX_SEARCH_START_POS);
            int insertPos = pos + name.length();
            StringBuilder builder = new StringBuilder(declaration);
            builder.insert(insertPos, ClassDeclarationHelper.buildGeneric(generics));
            return builder.toString();
        }
    }

    static abstract class AbstractDeclarationBuildStrategy implements ClassDeclarationBuildStrategy {
        protected Modifier modifier;
        protected String name;
        protected String parentName;

        AbstractDeclarationBuildStrategy(Modifier modifier, String name, String parentName) {
            this.modifier = modifier;
            this.name = name;
            this.parentName = parentName;
        }

    }

    // 接口

    static abstract class InterfaceDeclaration extends AbstractDeclarationBuildStrategy {
        InterfaceDeclaration(Modifier modifier, String name, String parentName) {
            super(modifier, name, parentName);
        }
    }

    static class PlainInterface extends InterfaceDeclaration {
        PlainInterface(Modifier modifier, String name) {
            super(modifier, name, null);
        }

        @Override
        public String buildDeclaration() {
            return ClassDeclarationHelper.buildInterfaceDeclaration(modifier, name);
        }
    }

    static class SubInterface extends InterfaceDeclaration {
        SubInterface(Modifier modifier, String name, String parentName) {
            super(modifier, name, parentName);
        }

        @Override
        public String buildDeclaration() {
            return ClassDeclarationHelper.buildInterfaceDeclaration(modifier, name, parentName);
        }
    }

    // 类

    static abstract class AbstractClassDeclarationBuildStrategy extends AbstractDeclarationBuildStrategy {
        protected String[] interfaceNames;

        AbstractClassDeclarationBuildStrategy(Modifier modifier, String name, String parentName,
                                              String[] interfaceNames) {
            super(modifier, name, parentName);
            this.interfaceNames = interfaceNames;
        }
    }

    static class PlainClass extends AbstractClassDeclarationBuildStrategy {
        PlainClass(Modifier modifier, String name) {
            super(modifier, name, null, null);
        }

        @Override
        public String buildDeclaration() {
            return ClassDeclarationHelper.buildClassDeclaration(modifier, name);
        }
    }

    static class SubClass extends AbstractClassDeclarationBuildStrategy {
        SubClass(Modifier modifier, String name, String parentName) {
            super(modifier, name, parentName, null);
        }

        @Override
        public String buildDeclaration() {
            return ClassDeclarationHelper.buildClassDeclaration(modifier, name, parentName);
        }
    }

    static class ImplementorClass extends AbstractClassDeclarationBuildStrategy {
        ImplementorClass(Modifier modifier, String name, String[] interfaceNames) {
            super(modifier, name, null, interfaceNames);
        }

        @Override
        public String buildDeclaration() {
            return ClassDeclarationHelper.buildClassDeclaration(modifier, name, interfaceNames);
        }
    }

    static class CompleteClass extends AbstractClassDeclarationBuildStrategy {
        CompleteClass(Modifier modifier, String name, String parentName, String[] interfaceNames) {
            super(modifier, name, parentName, interfaceNames);
        }

        @Override
        public String buildDeclaration() {
            return ClassDeclarationHelper.buildClassDeclaration(modifier, name, parentName, interfaceNames);
        }
    }

    // 枚举

    static class EnumClass extends AbstractDeclarationBuildStrategy {
        EnumClass(Modifier modifier, String name) {
            super(modifier, name, null);
        }

        @Override
        public String buildDeclaration() {
            return ClassDeclarationHelper.buildEnumDeclaration(modifier, name);
        }

    }

    // 注解

    static class AnnotationClass extends AbstractDeclarationBuildStrategy {
        AnnotationClass(Modifier modifier, String name) {
            super(modifier, name, null);
        }

        @Override
        public String buildDeclaration() {
            return ClassDeclarationHelper.buildAnnotationDeclaration(modifier, name);
        }

    }


    private ClassDeclarations() {
    }
}
