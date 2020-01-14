package com.shallowinggg.narep.core.common;

import java.io.File;

/**
 * Java语言规范定义的常量
 *
 * @author shallowinggg
 */
public class JLSConstants {
    public static final String PACKAGE = "package";
    public static final String IMPORT = "import";
    public static final String PUBLIC = "public";
    public static final String CLASS = "class";
    public static final String INTERFACE = "interface";
    public static final String ENUM = "enum";
    public static final String EXTENDS = "extends";
    public static final String IMPLEMENTS = "implements";

    public static final String PACKAGE_DELIMITER = ".";
    public static final String END_OF_STATEMENT = ";";
    public static final String LINE_SEPARATOR = System.lineSeparator();
    public static final String DOUBLE_LINE_SEPARATOR = System.lineSeparator() + System.lineSeparator();
    public static final char FILE_SEPARATOR = File.separatorChar;

    public static final String INTERFACE_DECL = PUBLIC + " " + INTERFACE + " ";
    public static final String CLASS_DECL = PUBLIC + " " + CLASS + " ";
    public static final String ENUM_DECL = PUBLIC + " " + ENUM + " ";
    public static final String EXTENDS_DECL = " " + EXTENDS + " ";
    public static final String IMPLEMENTS_DECL = " " + IMPLEMENTS + " ";
    public static final String DECL_END = " {" + LINE_SEPARATOR;
}
