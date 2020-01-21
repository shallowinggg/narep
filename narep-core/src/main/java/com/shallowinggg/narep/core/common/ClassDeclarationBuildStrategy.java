package com.shallowinggg.narep.core.common;

/**
 * 这个接口负责类、接口、注释以及注解的声明构造。
 * 因为存在了多种形式的声明，因此使用策略模式负责
 * 每一种形式的构造。
 *
 * @author shallowinggg
 */
public interface ClassDeclarationBuildStrategy {
    /**
     * 通过是否存在父类（接口），是否实现接口等信息生成
     * 接口类声明。
     * 例如：
     * <blockquote><pre>
     *         public class Bus extends Car {
     * </pre></blockquote>
     *
     * @return 声明
     */
    String buildDeclaration();
}
