package com.shallowinggg.narep.core.common;

import com.shallowinggg.narep.core.CodeGenerator;
import com.shallowinggg.narep.core.util.FileUtils;

import java.io.File;
import java.util.List;

import static com.shallowinggg.narep.core.common.GeneratorConfig.*;

/**
 * @author shallowinggg
 */
public class CodeGeneratorHelper {
    private static final String MODULE_NAME = "remoting";
    private static final String JAVA_FOLDER = "src" + FILE_SEPARATOR + "main" + FILE_SEPARATOR + "java";

    public static String buildFullQualifiedName(String basePackageName, String className) {
        return basePackageName + PACKAGE_DELIMITER + className;
    }

    public static String buildFullQualifiedName(String basePackageName, String subPackageName,  String className) {
        return basePackageName + PACKAGE_DELIMITER + subPackageName +
                PACKAGE_DELIMITER + className;
    }

    public static void buildDependencyImports(StringBuilder builder, List<CodeGenerator> dependencies) {
        for (CodeGenerator codeGenerator : dependencies) {
            builder.append(GeneratorConfig.IMPORT).append(" ").append(codeGenerator.fileName())
                    .append(LINE_SEPARATOR);
        }
    }

    public static String buildDefaultPackage(String basePackageName) {
        return PACKAGE + " " + basePackageName + EOS_DELIMITER + DOUBLE_LINE_SEPARATOR;
    }

    public static String buildSubPackage(String basePackageName, String subPackageName) {
        return PACKAGE + " " + basePackageName + PACKAGE_DELIMITER +
                subPackageName + EOS_DELIMITER + DOUBLE_LINE_SEPARATOR;
    }

    public static String buildProtocolPackage(String basePackageName) {
        return PACKAGE + " " + basePackageName + PACKAGE_DELIMITER +
                GeneratorConfig.PACKAGE_PROTOCOL + EOS_DELIMITER +
                System.lineSeparator() + System.lineSeparator();
    }

    public static String buildCommonPackage(String basePackageName) {
        return PACKAGE + " " + basePackageName + PACKAGE_DELIMITER +
                GeneratorConfig.PACKAGE_COMMON + EOS_DELIMITER +
                System.lineSeparator() + System.lineSeparator();
    }

    public static String buildNettyPackage(String basePackageName) {
        return PACKAGE + " " + basePackageName + PACKAGE_DELIMITER +
                GeneratorConfig.PACKAGE_NETTY + EOS_DELIMITER +
                System.lineSeparator() + System.lineSeparator();
    }

    public static String buildExceptionPackage(String basePackageName) {
        return PACKAGE + " " + basePackageName + PACKAGE_DELIMITER +
                GeneratorConfig.PACKAGE_EXCEPTION + EOS_DELIMITER +
                System.lineSeparator() + System.lineSeparator();
    }

    public static String buildInterfaceDeclaration(String interfaceName) {
        return "public interface " + interfaceName + " {" + System.lineSeparator();
    }

    public static String buildInterfaceDeclaration(String interfaceName, String parent) {
        return "public interface " + interfaceName + " extend " + parent + " {" + System.lineSeparator();
    }

    public static String buildClassDeclaration(String className) {
        return "public class " + className + " {" + System.lineSeparator();
    }

    public static String buildClassDeclaration(String className, String parent) {
        return "public class " + className + " extend " + parent + " {" + System.lineSeparator();
    }

    public static String buildNecessaryFolders(String basePath) {
        // src/main/java
        String source = basePath + FILE_SEPARATOR + MODULE_NAME + FILE_SEPARATOR + JAVA_FOLDER;
        FileUtils.ensureDirOk(new File(source));
        return source;
    }

    private CodeGeneratorHelper() {}
}
