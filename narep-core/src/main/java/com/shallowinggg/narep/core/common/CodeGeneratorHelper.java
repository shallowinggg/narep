package com.shallowinggg.narep.core.common;

import com.shallowinggg.narep.core.JavaCodeGenerator;
import com.shallowinggg.narep.core.lang.FieldInfo;
import com.shallowinggg.narep.core.util.Conditions;
import com.shallowinggg.narep.core.util.FileUtils;
import com.shallowinggg.narep.core.util.StringTinyUtils;

import java.io.File;
import java.util.List;

import static com.shallowinggg.narep.core.lang.JLSConstants.*;

/**
 * @author shallowinggg
 */
public class CodeGeneratorHelper {
    private static final String MODULE_NAME = "remoting";
    private static final String JAVA_FOLDER = "src" + FILE_SEPARATOR + "main" + FILE_SEPARATOR + "java";
    private static final int ASSUMED_FIELD_LEN = 50;
    private static final int ASSUMED_METHOD_LEN = 300;

    /**
     * Return full qualified java class name.
     *
     * @param basePackageName package name
     * @param className       class name
     * @return full qualified class name
     * @see #buildFullQualifiedName(String, String, String)
     */
    public static String buildFullQualifiedName(String basePackageName, String className) {
        Conditions.hasText(basePackageName, "basePackageName must has text");
        Conditions.hasText(className, "className must has text");
        return basePackageName + PACKAGE_DELIMITER + className;
    }

    /**
     * Return full qualified java class name.
     *
     * @param basePackageName base package name
     * @param subPackageName  sub package name
     * @param className       class name
     * @return full qualified class name
     */
    public static String buildFullQualifiedName(String basePackageName, String subPackageName, String className) {
        Conditions.hasText(basePackageName, "basePackageName must has text");
        Conditions.hasText(subPackageName, "subPackageName must has text");
        Conditions.hasText(className, "className must has text");
        return basePackageName + PACKAGE_DELIMITER + subPackageName +
                PACKAGE_DELIMITER + className;
    }

    /**
     * Return full qualified java inner class name.
     *
     * @param outer outer class name
     * @param name  inner class name
     * @return full qualified java inner class name
     */
    public static String buildInnerClassFullQualifiedName(String outer, String name) {
        Conditions.hasText(outer, "outer must has text");
        Conditions.hasText(name, "name must has text");
        return outer + INNER_CLASS_SEPARATOR + name;
    }

    /**
     * Concat import statements for inner dependencies with StringBuilder.
     * <p>
     * As the base package name is dynamic, inner dependency classes's full
     * qualified name is dynamic too.
     *
     * @param builder      StringBuilder to concat
     * @param dependencies inner dependencies
     */
    public static void buildDependencyImports(StringBuilder builder, List<JavaCodeGenerator> dependencies) {
        Conditions.notNull(builder, "builder must not be null");
        Conditions.notEmpty(dependencies, "dependencies must not be empty");
        for (JavaCodeGenerator codeGenerator : dependencies) {
            builder.append(IMPORT).append(" ").append(codeGenerator.fullQualifiedName()).append(";")
                    .append(LINE_SEPARATOR);
        }
    }

    /**
     * Concat static import statements for inner dependencies with StringBuilder.
     *
     * @param builder      StringBuilder to concat
     * @param dependencies inner dependencies
     */
    public static void buildStaticImports(StringBuilder builder, List<JavaCodeGenerator> dependencies) {
        Conditions.notNull(builder, "builder must not be null");
        Conditions.notEmpty(dependencies, "dependencies must not be empty");
        for (JavaCodeGenerator codeGenerator : dependencies) {
            builder.append(IMPORT_STATIC).append(codeGenerator.fullQualifiedName()).append(".*;")
                    .append(LINE_SEPARATOR);
        }
    }

    /**
     * Return package statement for class.
     * e.g. <code>package com.example.remoting;</code>
     *
     * @param packageName package to build
     * @return package statement
     * @see #buildPackageStatement(String, String)
     */
    public static String buildPackageStatement(String packageName) {
        Conditions.hasText(packageName, "packageName must has text");
        return PACKAGE + " " + packageName + END_OF_STATEMENT + DOUBLE_LINE_SEPARATOR;
    }

    /**
     * Return package statement for class.
     * <p>
     * Different from {@link #buildPackageStatement(String)}, this
     * method combines sub package(base package is dynamic).
     * e.g. <code>package com.example.remoting.netty;</code>
     *
     * @param basePackageName base package
     * @return package statement
     */
    public static String buildPackageStatement(String basePackageName, String subPackageName) {
        Conditions.hasText(basePackageName, "basePackageName must has text");
        Conditions.hasText(subPackageName, "subPackageName must has text");
        return PACKAGE + " " + basePackageName + PACKAGE_DELIMITER +
                subPackageName + END_OF_STATEMENT + DOUBLE_LINE_SEPARATOR;
    }

    /**
     * Create module directory and basic java source directory,
     * and return its path.
     * <p>
     * The param "basePath" should be consistent with the value
     * method {@link GeneratorConfig#getStoreLocation()} returns.
     *
     * @param basePath parent path
     * @return java source directory path
     */
    public static String buildNecessaryFolders(String basePath) {
        Conditions.hasText(basePath, "basePath must has text");
        // src/main/java
        String source = basePath + FILE_SEPARATOR + MODULE_NAME + FILE_SEPARATOR + JAVA_FOLDER;
        FileUtils.ensureDirOk(new File(source));
        return source;
    }

    public static String buildLoggerField(String className) {
        Conditions.hasText(className, "className must has text");
        boolean useCustomLoggerName = ConfigInfos.getInstance().useCustomLoggerName();
        return "LogManager.getLogger(" +
                (useCustomLoggerName ? "RemotingHelper.REMOTING_LOGGER_NAME" : className + ".class") + ")";
    }

    public static String buildFieldsByFieldInfos(List<FieldInfo> fields) {
        Conditions.notEmpty(fields, "fields must not be empty");
        StringBuilder builder = new StringBuilder(fields.size() * ASSUMED_FIELD_LEN);
        for (FieldInfo field : fields) {
            if (field.getComment() != null) {
                builder.append(field.getComment()).append("\n");
            }
            builder.append("    ").append(field.getModifier()).append(field.getType()).append(" ")
                    .append(field.getName());
            if (field.getInitValue() != null) {
                builder.append(" = ").append(field.getInitValue());
            }
            builder.append(";\n\n");
        }
        return builder.toString();
    }

    public static String buildGetterAndSetterMethods(List<FieldInfo> fields) {
        Conditions.notEmpty(fields, "fields must not be empty");
        StringBuilder builder = new StringBuilder(fields.size() * ASSUMED_METHOD_LEN);
        for (FieldInfo field : fields) {
            String name = field.getName();
            String type = field.getType();
            builder.append(setterMethodDeclaration(type, name)).append(" {\n")
                    .append("        this.").append(name).append(" = ").append(name).append(";\n")
                    .append("    }\n")
                    .append("\n")
                    .append(getterMethodDeclaration(type, name)).append(" {\n")
                    .append("        return ").append(name).append(";\n")
                    .append("    }\n\n");
        }
        return builder.toString();
    }

    public static void buildGetterAndSetterMethods(StringBuilder builder, List<FieldInfo> fields) {
        Conditions.notNull(builder, "builder must not be null");
        Conditions.notEmpty(fields, "fields must not be empty");
        for (FieldInfo field : fields) {
            String name = field.getName();
            String type = field.getType();
            builder.append(setterMethodDeclaration(type, name)).append(" {\n")
                    .append("        this.").append(name).append(" = ").append(name).append(";\n")
                    .append("    }\n")
                    .append("\n")
                    .append(getterMethodDeclaration(type, name)).append(" {\n")
                    .append("        return ").append(name).append(";\n")
                    .append("    }\n\n");
        }
    }

    public static String buildGetterMethod(FieldInfo field) {
        Conditions.notNull(field, "field must not be null");
        String name = field.getName();
        String type = field.getType();

        return getterMethodDeclaration(type, name) + " {\n" +
                "        return " + name + ";\n" +
                "    }\n\n";
    }

    public static String buildSetterMethod(FieldInfo field) {
        Conditions.notNull(field, "field must not be null");
        String name = field.getName();
        String type = field.getType();

        return setterMethodDeclaration(type, name) + " {\n"
                + "        this." + name + " = " + name + ";\n"
                + "    }\n\n";
    }

    public static void buildGetterMethod(StringBuilder builder, FieldInfo field) {
        Conditions.notNull(builder, "builder must not be null");
        Conditions.notNull(field, "field must not be null");

        String name = field.getName();
        String type = field.getType();
        builder.append(getterMethodDeclaration(type, name)).append(" {\n")
                .append("        return ").append(name).append(";\n")
                .append("    }\n\n");
    }

    public static void buildSetterMethod(StringBuilder builder, FieldInfo field) {
        Conditions.notNull(builder, "builder must not be null");
        Conditions.notNull(field, "field must not be null");

        String name = field.getName();
        String type = field.getType();
        builder.append(setterMethodDeclaration(type, name)).append(" {\n")
                .append("        this.").append(name).append(" = ").append(name).append(";\n")
                .append("    }\n\n");
    }

    public static void buildGetterMethods(StringBuilder builder, List<FieldInfo> fields) {
        Conditions.notNull(builder, "builder must not be null");
        Conditions.notEmpty(fields, "fields must be null or empty");
        for (FieldInfo field : fields) {
            buildGetterMethod(builder, field);
        }
    }

    public static void buildSetterMethods(StringBuilder builder, List<FieldInfo> fields) {
        Conditions.notNull(builder, "builder must not be null");
        Conditions.notEmpty(fields, "fields must be null or empty");
        for (FieldInfo field : fields) {
            buildSetterMethod(builder, field);
        }
    }

    private static String setterMethodDeclaration(String type, String name) {
        String setterName = "set" + StringTinyUtils.firstCharToUpperCase(name);
        return "    public void " + setterName + "(" + type + " " + name + ")";
    }

    private static String getterMethodDeclaration(String type, String name) {
        String getterName = ("boolean".equals(type) ? " is" : " get") + StringTinyUtils.firstCharToUpperCase(name);
        return "    public " + type + getterName + "()";
    }

    public static String buildToStringMethod(String className, List<FieldInfo> fields) {
        Conditions.notEmpty(className, "className must not be empty");
        Conditions.notEmpty(fields, "fields must not be null or empty");
        StringBuilder builder = new StringBuilder(fields.size() * ASSUMED_FIELD_LEN);
        builder.append("    @Override\n" +
                "    public String toString() {\n" +
                "        return \"").append(className).append(" [");
        for (FieldInfo field : fields) {
            String name = field.getName();
            builder.append(name).append("=\" + ").append(name)
                    .append("\n                + \", ");
        }
        builder.setLength(builder.length() - 2);
        builder.append("]\";\n")
                .append("    }\n\n");
        return builder.toString();
    }

    private CodeGeneratorHelper() {
    }
}
