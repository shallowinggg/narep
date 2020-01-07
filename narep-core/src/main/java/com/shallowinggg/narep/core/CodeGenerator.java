package com.shallowinggg.narep.core;

/**
 * 生成Java代码文件的基础接口。
 *
 * 子类实现此接口以构建一个完整的java文件的各个部分，最终组成一个完整的文件。
 *
 * @author shallowinggg
 */
public interface CodeGenerator {
    /**
     * 类结尾符号
     */
    String END_OF_CLASS = "}";

    /**
     * 生成文件的全限定名称
     *
     * @return 文件名
     */
    String fileName();

    /**
     * 生成package语句
     * eg. package com.shallowinggg.narep.core;
     *
     * @return 包含package语句的字符串
     */
    String buildPackage();

    /**
     * 生成import语句
     * eg. import java.util.ArrayList;
     *
     * @return 包含import语句的字符串
     */
    String buildImports();

    /**
     * 生成类声明语句
     * eg. public interface CodeGenerator {
     *
     * @return 包含类声明语句的字符串
     */
    String buildName();

    /**
     * 生成字段
     * eg. private int name;
     *
     * @return 包含字段的字符串
     */
    String buildFields();

    /**
     * 生成方法
     * eg. public int getName();
     *
     * @return 包含方法的字符串
     */
    String buildMethods();
}
