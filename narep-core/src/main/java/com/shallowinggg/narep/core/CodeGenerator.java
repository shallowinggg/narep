package com.shallowinggg.narep.core;

import java.io.IOException;

/**
 * 生成文件的基础接口。
 *
 * 这个接口的子接口定义自己的文件结构，并组成文件各部分内容并调用
 * {@link CodeGenerator#write()}方法生成一个完整的文件。
 * 子类实现此接口以构建一个完整的java文件的各个部分，最终组成一个完整的文件。
 *
 * @author shallowinggg
 */
public interface CodeGenerator {
    String DEFAULT_CHARSET = "utf-8";

    /**
     * 生成的文件名称
     *
     * <blockquote><pre>
     *     List.java
     * </pre></blockquote>
     *
     * @return 文件名
     */
    String fileName();

    /**
     * 写入文件
     *
     * @throws IOException 当写入文件失败时
     */
    void write() throws IOException;
}
