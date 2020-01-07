package com.shallowinggg.narep.core.common;

import com.shallowinggg.narep.core.CodeGenerator;

import java.util.List;

/**
 * @author shallowinggg
 */
public class CodeGeneratorHelper {

    public static String buildFileName(String basePackageName, String className) {
        return basePackageName + GeneratorConfig.PACKAGE_DELIMITER + className;
    }

    public static String buildFileName(String basePackageName, String midPackage,  String className) {
        return basePackageName + GeneratorConfig.PACKAGE_DELIMITER + midPackage +
                GeneratorConfig.PACKAGE_DELIMITER + className;
    }

    public static void buildDependencyImports(StringBuilder builder, List<CodeGenerator> dependencies) {
        for (CodeGenerator codeGenerator : dependencies) {
            builder.append(GeneratorConfig.IMPORT).append(" ").append(codeGenerator.fileName())
                    .append(System.lineSeparator());
        }
    }

    public static String buildDefaultPackage(String basePackageName) {
        return GeneratorConfig.PACKAGE + " " + basePackageName +
                GeneratorConfig.EOS_DELIMITER + System.lineSeparator() + System.lineSeparator();
    }

    public static String buildProtocolPackage(String basePackageName) {
        return GeneratorConfig.PACKAGE + " " + basePackageName + GeneratorConfig.PACKAGE_DELIMITER +
                GeneratorConfig.PACKAGE_PROTOCOL + GeneratorConfig.EOS_DELIMITER +
                System.lineSeparator() + System.lineSeparator();
    }

    public static String buildCommonPackage(String basePackageName) {
        return GeneratorConfig.PACKAGE + " " + basePackageName + GeneratorConfig.PACKAGE_DELIMITER +
                GeneratorConfig.PACKAGE_COMMON + GeneratorConfig.EOS_DELIMITER +
                System.lineSeparator() + System.lineSeparator();
    }

    public static String buildNettyPackage(String basePackageName) {
        return GeneratorConfig.PACKAGE + " " + basePackageName + GeneratorConfig.PACKAGE_DELIMITER +
                GeneratorConfig.PACKAGE_NETTY + GeneratorConfig.EOS_DELIMITER +
                System.lineSeparator() + System.lineSeparator();
    }

    public static String buildExceptionPackage(String basePackageName) {
        return GeneratorConfig.PACKAGE + " " + basePackageName + GeneratorConfig.PACKAGE_DELIMITER +
                GeneratorConfig.PACKAGE_EXCEPTION + GeneratorConfig.EOS_DELIMITER +
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

    private CodeGeneratorHelper() {}
}
