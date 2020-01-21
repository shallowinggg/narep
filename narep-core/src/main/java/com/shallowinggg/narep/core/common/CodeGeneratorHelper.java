package com.shallowinggg.narep.core.common;

import com.shallowinggg.narep.core.JavaCodeGenerator;
import com.shallowinggg.narep.core.lang.FieldInfo;
import com.shallowinggg.narep.core.util.CollectionUtils;
import com.shallowinggg.narep.core.util.Conditions;
import com.shallowinggg.narep.core.util.FileUtils;
import com.shallowinggg.narep.core.util.StringTinyUtils;

import java.io.File;
import java.util.List;
import java.util.Objects;

import static com.shallowinggg.narep.core.lang.JLSConstants.*;

/**
 * @author shallowinggg
 */
public class CodeGeneratorHelper {
    private static final String MODULE_NAME = "remoting";
    private static final String JAVA_FOLDER = "src" + FILE_SEPARATOR + "main" + FILE_SEPARATOR + "java";
    private static final int ASSUMED_FIELD_LEN = 50;
    private static final int ASSUMED_METHOD_LEN = 300;

    public static String buildFullQualifiedName(String basePackageName, String className) {
        return basePackageName + PACKAGE_DELIMITER + className;
    }

    public static String buildFullQualifiedName(String basePackageName, String subPackageName, String className) {
        return basePackageName + PACKAGE_DELIMITER + subPackageName +
                PACKAGE_DELIMITER + className;
    }

    public static String buildInnerClassFullQualifiedName(String outer, String name) {
        return outer + INNER_CLASS_SEPARATOR + name;
    }

    public static void buildDependencyImports(StringBuilder builder, List<JavaCodeGenerator> dependencies) {
        Conditions.checkArgument(CollectionUtils.isNotEmpty(dependencies), "dependencies must not be null or empty");
        for (JavaCodeGenerator codeGenerator : dependencies) {
            builder.append(IMPORT).append(" ").append(codeGenerator.fullQualifiedName()).append(";")
                    .append(LINE_SEPARATOR);
        }
    }

    public static void buildStaticImports(StringBuilder builder, List<JavaCodeGenerator> dependencies) {
        Conditions.checkArgument(CollectionUtils.isNotEmpty(dependencies), "dependencies must not be null or empty");
        for (JavaCodeGenerator codeGenerator : dependencies) {
            builder.append(IMPORT_STATIC).append(codeGenerator.fullQualifiedName()).append(".*;")
                    .append(LINE_SEPARATOR);
        }
    }

    public static String buildDefaultPackage(String basePackageName) {
        return PACKAGE + " " + basePackageName + END_OF_STATEMENT + LINE_SEPARATOR;
    }

    public static String buildSubPackage(String basePackageName, String subPackageName) {
        return PACKAGE + " " + basePackageName + PACKAGE_DELIMITER +
                subPackageName + END_OF_STATEMENT + LINE_SEPARATOR;
    }

    public static String buildNecessaryFolders(String basePath) {
        // src/main/java
        String source = basePath + FILE_SEPARATOR + MODULE_NAME + FILE_SEPARATOR + JAVA_FOLDER;
        FileUtils.ensureDirOk(new File(source));
        return source;
    }

    public static String buildLoggerField(String className) {
        boolean useCustomLoggerName = ConfigInfos.getInstance().useCustomLoggerName();
        return "LogManager.getLogger(" +
                (useCustomLoggerName ? "RemotingHelper.REMOTING_LOGGER_NAME" : className + ".class") + ")";
    }

    public static String buildFieldsByFieldInfos(List<FieldInfo> fields) {
        Conditions.checkArgument(CollectionUtils.isNotEmpty(fields), "fields must not be null");
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
            builder.append(";\n");
        }
        builder.append("\n");
        return builder.toString();
    }

    public static String buildGetterAndSetterMethods(List<FieldInfo> fields) {
        Conditions.checkArgument(CollectionUtils.isNotEmpty(fields), "fields must not be null");
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
        Conditions.checkArgument(builder != null, "builder must not be null");
        Conditions.checkArgument(CollectionUtils.isNotEmpty(fields), "fields must not be null or empty");
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
        Objects.requireNonNull(field, "field must not be null");
        String name = field.getName();
        String type = field.getType();

        return getterMethodDeclaration(type, name) + " {\n" +
                "        return " + name + ";\n" +
                "    }\n\n";
    }

    public static String buildSetterMethod(FieldInfo field) {
        Objects.requireNonNull(field, "field must not be null");
        String name = field.getName();
        String type = field.getType();

        return setterMethodDeclaration(type, name) + " {\n"
                + "        this." + name + " = " + name + ";\n"
                + "    }\n\n";
    }

    public static void buildGetterMethod(StringBuilder builder, FieldInfo field) {
        Objects.requireNonNull(builder, "builder must not be null");
        Objects.requireNonNull(field, "field must not be null");

        String name = field.getName();
        String type = field.getType();
        builder.append(getterMethodDeclaration(type, name)).append(" {\n")
                .append("        return ").append(name).append(";\n")
                .append("    }\n\n");
    }

    public static void buildSetterMethod(StringBuilder builder, FieldInfo field) {
        Objects.requireNonNull(builder, "builder must not be null");
        Objects.requireNonNull(field, "field must not be null");

        String name = field.getName();
        String type = field.getType();
        builder.append(setterMethodDeclaration(type, name)).append(" {\n")
                .append("        this.").append(name).append(" = ").append(name).append(";\n")
                .append("    }\n\n");
    }

    public static void buildGetterMethods(StringBuilder builder, List<FieldInfo> fields) {
        Objects.requireNonNull(builder, "builder must not be null");
        Conditions.checkArgument(CollectionUtils.isNotEmpty(fields), "fields must be null or empty");
        for (FieldInfo field : fields) {
            buildGetterMethod(builder, field);
        }
    }

    public static void buildSetterMethods(StringBuilder builder, List<FieldInfo> fields) {
        Objects.requireNonNull(builder, "builder must not be null");
        Conditions.checkArgument(CollectionUtils.isNotEmpty(fields), "fields must be null or empty");
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
        Conditions.checkArgument(StringTinyUtils.isNotEmpty(className), "className must not be empty");
        Conditions.checkArgument(CollectionUtils.isNotEmpty(fields), "fields must not be null or empty");
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
