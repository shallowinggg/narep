package com.shallowinggg.narep.core.lang;

import java.io.File;

/**
 * 这个类提供了Java语言规范定义的语法，同时在此基础上
 * 定义了一些方便使用的字符常量以方便生成代码。
 *
 * @author shallowinggg
 */
public class JLSConstants {
    public static final String PACKAGE = "package";
    public static final String IMPORT = "import";
    public static final String CLASS = "class";
    public static final String INTERFACE = "interface";
    public static final String ENUM = "enum";
    public static final String ANNOTATION = "@interface";
    public static final String EXTENDS = "extends";
    public static final String IMPLEMENTS = "implements";
    public static final String STATIC = "static";

    public static final String PACKAGE_DELIMITER = ".";
    public static final String END_OF_STATEMENT = ";";
    public static final String INNER_CLASS_SEPARATOR = "$";
    public static final String LINE_SEPARATOR = System.lineSeparator();
    public static final String DOUBLE_LINE_SEPARATOR = System.lineSeparator() + System.lineSeparator();
    public static final char FILE_SEPARATOR = File.separatorChar;

    public static final String INTERFACE_DECL = INTERFACE + " ";
    public static final String CLASS_DECL = CLASS + " ";
    public static final String ENUM_DECL = ENUM + " ";
    public static final String ANNOTATION_DECL = ANNOTATION + " ";
    public static final String EXTENDS_DECL = " " + EXTENDS + " ";
    public static final String IMPLEMENTS_DECL = " " + IMPLEMENTS + " ";
    public static final String BLOCK_START = " {" + LINE_SEPARATOR;
    public static final String IMPORT_STATIC = IMPORT + " " + STATIC + " ";
}
