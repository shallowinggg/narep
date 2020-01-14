package com.shallowinggg.narep.core.common;

import com.shallowinggg.narep.core.util.CollectionUtils;
import com.shallowinggg.narep.core.util.StringTinyUtils;

import java.util.List;

/**
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
     *     - 范型类
     *       - 常规范型类    public class Generic<T> {
     *     - 类
     *       - 常规类      public class Car{
     *       - 子类        public class Bus extends Car {
     *       - 实现接口的类 public class ProtocolConfig implements Config {
     *       - 完整声明的类 public class Bus extends Car implements Big {
     * </pre>
     *
     * 注意：范型类暂不实现完整声明的策略，并且枚举类暂不提供实现。
     *
     * @param className      类名称
     * @param parentName     父类名称
     * @param interfaceNames 接口列表
     * @param generics       范型列表
     * @param isInterface    是否为接口
     * @param isGeneric      是否为范型
     * @return 类声明策略
     */
    public static ClassDeclarationBuildStrategy buildStrategy(String className, String parentName,
                                                              String[] interfaceNames, List<String> generics,
                                                              boolean isInterface, boolean isGeneric) {
        if (isInterface) {
            if (StringTinyUtils.isNotEmpty(parentName)) {
                return new SubInterface(className, parentName);
            } else {
                return new PlainInterface(className, null);
            }
        } else if (isGeneric) {
            // 当前只支持常规范型声明
            return new PlainGenericClass(className, null, null, generics);
        } else {
            if (StringTinyUtils.isNotEmpty(parentName) && CollectionUtils.isNotEmpty(interfaceNames)) {
                return new ComplexClass(className, parentName, interfaceNames);
            } else if (StringTinyUtils.isNotEmpty(parentName)) {
                return new SubClass(className, parentName, null);
            } else if (CollectionUtils.isNotEmpty(interfaceNames)) {
                return new ImplementClass(className, null, interfaceNames);
            } else {
                return new PlainClass(className, null, null);
            }
        }
    }

    static abstract class AbstractDeclarationBuildStrategy implements ClassDeclarationBuildStrategy {
        protected String name;
        protected String parentName;

        AbstractDeclarationBuildStrategy(String name, String parentName) {
            this.name = name;
            this.parentName = parentName;
        }
    }

    // 接口

    static class PlainInterface extends AbstractDeclarationBuildStrategy {
        PlainInterface(String name, String parentName) {
            super(name, parentName);
        }

        @Override
        public String buildDeclaration() {
            return CodeGeneratorHelper.buildInterfaceDeclaration(name);
        }
    }

    static class SubInterface extends AbstractDeclarationBuildStrategy {
        SubInterface(String name, String parentName) {
            super(name, parentName);
        }

        @Override
        public String buildDeclaration() {
            return CodeGeneratorHelper.buildInterfaceDeclaration(name, parentName);
        }
    }

    // 类

    static abstract class AbstractClassDeclarationBuildStrategy extends AbstractDeclarationBuildStrategy {
        protected String[] interfaceNames;

        AbstractClassDeclarationBuildStrategy(String name, String parentName, String[] interfaceNames) {
            super(name, parentName);
            this.interfaceNames = interfaceNames;
        }
    }

    static class PlainClass extends AbstractClassDeclarationBuildStrategy {
        PlainClass(String name, String parentName, String[] interfaceNames) {
            super(name, parentName, interfaceNames);
        }

        @Override
        public String buildDeclaration() {
            return CodeGeneratorHelper.buildClassDeclaration(name);
        }
    }

    static class SubClass extends AbstractClassDeclarationBuildStrategy {
        SubClass(String name, String parentName, String[] interfaceNames) {
            super(name, parentName, interfaceNames);
        }

        @Override
        public String buildDeclaration() {
            return CodeGeneratorHelper.buildClassDeclaration(name, parentName);
        }
    }

    static class ImplementClass extends AbstractClassDeclarationBuildStrategy {
        ImplementClass(String name, String parentName, String[] interfaceNames) {
            super(name, parentName, interfaceNames);
        }

        @Override
        public String buildDeclaration() {
            return CodeGeneratorHelper.buildClassDeclaration(name, interfaceNames);
        }
    }

    static class ComplexClass extends AbstractClassDeclarationBuildStrategy {
        ComplexClass(String name, String parentName, String[] interfaceNames) {
            super(name, parentName, interfaceNames);
        }

        @Override
        public String buildDeclaration() {
            return CodeGeneratorHelper.buildClassDeclaration(name, parentName, interfaceNames);
        }
    }

    // 范型类

    static abstract class AbstractGenericClassDeclarationBuildStrategy extends AbstractClassDeclarationBuildStrategy {
        protected List<String> generics;

        AbstractGenericClassDeclarationBuildStrategy(String name, String parentName,
                                                     String[] interfaceNames, List<String> generics) {
            super(name, parentName, interfaceNames);
            this.generics = generics;
        }
    }

    /**
     * 粗略实现，暂时不对其进行详细拓展
     */
    static class PlainGenericClass extends AbstractGenericClassDeclarationBuildStrategy {
        PlainGenericClass(String name, String parentName, String[] interfaceNames, List<String> generics) {
            super(name, parentName, interfaceNames, generics);
        }

        @Override
        public String buildDeclaration() {
            return CodeGeneratorHelper.buildGenericClassDeclaration(name, generics);
        }
    }


    private ClassDeclarations() {}
}
