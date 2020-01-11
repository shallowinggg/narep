package com.shallowinggg.narep.core;

/**
 * java文件代码生成器。
 *
 * 文件结构分为如下：
 *
 * - 开源许可证
 * - package声明
 * - import语句
 * - 类注释
 * - 类/接口声明
 * - 字段
 * - 方法
 *
 * 最后拼接类/接口定义结束符{@link JavaCodeGenerator#END_OF_CLASS }生成一个完整文件。
 *
 * @author shallowinggg
 */
public interface JavaCodeGenerator extends CodeGenerator {

    String EXTENSION = ".java";

    /**
     * 类结尾符号
     */
    String END_OF_CLASS = "}";

    /**
     * 类的全限定名称
     *
     * <blockquote><pre>
     *     java.util.List
     * </pre></blockquote>
     *
     * @return 全限定名
     */
    String fullQualifiedName();

    /**
     * 开源代码许可
     *
     * @return 开源代码许可
     */
    String openSourceLicense();

    /**
     * 生成package语句
     *
     * <blockquote><pre>
     *     package com.shallowinggg.narep.core;
     * </pre></blockquote>
     *
     * @return 包含package语句的字符串
     */
    String buildPackage();

    /**
     * 生成import语句
     *
     * <blockquote><pre>
     *     import java.util.ArrayList;
     * </pre></blockquote>
     *
     * @return 包含import语句的字符串
     */
    String buildImports();

    /**
     * 类注释信息
     *
     * @return 类注释信息
     */
    String buildClassComment();

    /**
     * 生成类声明语句
     *
     * <blockquote><pre>
     *     public interface CodeGenerator {
     * </pre></blockquote>
     *
     * @return 包含类声明语句的字符串
     */
    String buildName();

    /**
     * 生成字段
     *
     * <blockquote><pre>
     *     private int name;
     * </pre></blockquote>
     *
     * @return 包含字段的字符串
     */
    String buildFields();

    /**
     * 生成方法
     *
     * <blockquote><pre>
     *     public int getName() { ... }
     * </pre></blockquote>
     *
     * @return 包含方法的字符串
     */
    String buildMethods();

    /**
     * 检查自身依赖类的生成器是否存在
     *
     * @param resolver 解析器
     * @return true 如果全部存在；否则返回false
     */
    boolean resolveDependencies(DependencyResolver resolver);
}
