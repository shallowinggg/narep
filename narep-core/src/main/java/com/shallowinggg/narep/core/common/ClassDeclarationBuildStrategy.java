package com.shallowinggg.narep.core.common;

/**
 * 类以及接口声明策略
 *
 * @author shallowinggg
 */
public interface ClassDeclarationBuildStrategy {
    /**
     * 通过是否存在父类（接口），是否实现接口等信息生成
     * 接口类（接口）声明。
     * 例如：
     * <blockquote><pre>
     *         public class Bus extends Car {
     * </pre></blockquote>
     *
     * @return 声明
     */
    String buildDeclaration();
}
