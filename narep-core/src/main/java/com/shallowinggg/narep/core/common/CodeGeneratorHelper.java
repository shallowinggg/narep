package com.shallowinggg.narep.core.common;

import com.shallowinggg.narep.core.JavaCodeGenerator;
import com.shallowinggg.narep.core.util.CollectionUtils;
import com.shallowinggg.narep.core.util.Conditions;
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

    public static void buildDependencyImports(StringBuilder builder, List<JavaCodeGenerator> dependencies) {
        Conditions.checkArgument(CollectionUtils.isNotEmpty(dependencies), "dependencies must not be null or empty");
        for (JavaCodeGenerator codeGenerator : dependencies) {
            builder.append(GeneratorConfig.IMPORT).append(" ").append(codeGenerator.fullQualifiedName())
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

    public static String buildInterfaceDeclaration(String interfaceName) {
        return "public interface " + interfaceName + " {" + LINE_SEPARATOR;
    }

    public static String buildInterfaceDeclaration(String interfaceName, String parent) {
        return "public interface " + interfaceName + " extend " + parent + " {" + LINE_SEPARATOR;
    }

    public static String buildClassDeclaration(String className) {
        return "public class " + className + " {" + LINE_SEPARATOR;
    }

    public static String buildClassDeclaration(String className, String parent) {
        return "public class " + className + " extend " + parent + " {" + LINE_SEPARATOR;
    }

    public static String buildEnumDeclaration(String enumName) {
        return "public enum " + enumName + " {" + LINE_SEPARATOR;
    }

    public static String buildGenericClassDeclaration(String className, List<String> generics) {
        Conditions.checkArgument(CollectionUtils.isNotEmpty(generics), "generics must not be null");
        StringBuilder declaration = new StringBuilder(30);
        declaration.append("public class ").append(className).append("<");
        for(String generic : generics) {
            declaration.append(generic).append(", ");
        }
        declaration.setLength(declaration.length() - 2);
        declaration.append("> {").append(LINE_SEPARATOR);
        return declaration.toString();
    }

    public static String buildNecessaryFolders(String basePath) {
        // src/main/java
        String source = basePath + FILE_SEPARATOR + MODULE_NAME + FILE_SEPARATOR + JAVA_FOLDER;
        FileUtils.ensureDirOk(new File(source));
        return source;
    }

    private CodeGeneratorHelper() {}
}
