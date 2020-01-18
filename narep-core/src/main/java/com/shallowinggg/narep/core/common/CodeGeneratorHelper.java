package com.shallowinggg.narep.core.common;

import com.shallowinggg.narep.core.JavaCodeGenerator;
import com.shallowinggg.narep.core.util.CollectionUtils;
import com.shallowinggg.narep.core.util.Conditions;
import com.shallowinggg.narep.core.util.FileUtils;
import com.shallowinggg.narep.core.util.StringTinyUtils;

import java.io.File;
import java.util.List;

import static com.shallowinggg.narep.core.common.JLSConstants.*;

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

    public static void buildDependencyImports(StringBuilder builder, List<JavaCodeGenerator> dependencies) {
        Conditions.checkArgument(CollectionUtils.isNotEmpty(dependencies), "dependencies must not be null or empty");
        for (JavaCodeGenerator codeGenerator : dependencies) {
            builder.append(IMPORT).append(" ").append(codeGenerator.fullQualifiedName())
                    .append(LINE_SEPARATOR);
        }
    }

    public static void buildStaticImports(StringBuilder builder, List<JavaCodeGenerator> dependencies) {
        Conditions.checkArgument(CollectionUtils.isNotEmpty(dependencies), "dependencies must not be null or empty");
        for (JavaCodeGenerator codeGenerator : dependencies) {
            builder.append(IMPORT_STATIC).append(codeGenerator.fullQualifiedName()).append(".*")
                    .append(LINE_SEPARATOR);
        }
    }

    public static String buildDefaultPackage(String basePackageName) {
        return PACKAGE + " " + basePackageName + END_OF_STATEMENT + DOUBLE_LINE_SEPARATOR;
    }

    public static String buildSubPackage(String basePackageName, String subPackageName) {
        return PACKAGE + " " + basePackageName + PACKAGE_DELIMITER +
                subPackageName + END_OF_STATEMENT + DOUBLE_LINE_SEPARATOR;
    }

    public static String buildInterfaceDeclaration(String interfaceName) {
        return INTERFACE_DECL + interfaceName + DECL_END;
    }

    public static String buildInterfaceDeclaration(String interfaceName, String parent) {
        return INTERFACE_DECL + interfaceName + EXTENDS_DECL + parent + DECL_END;
    }

    public static String buildClassDeclaration(String className) {
        return CLASS_DECL + className + DECL_END;
    }

    public static String buildClassDeclaration(String className, String parent) {
        return CLASS_DECL + className + EXTENDS_DECL + parent + DECL_END;
    }

    public static String buildClassDeclaration(String className, String... interfaceNames) {
        Conditions.checkArgument(CollectionUtils.isNotEmpty(interfaceNames),
                "At least one interface should be specified");
        StringBuilder builder = new StringBuilder(50);
        builder.append(CLASS_DECL).append(className).append(IMPLEMENTS_DECL);
        for (String interfaceName : interfaceNames) {
            builder.append(interfaceName).append(", ");
        }
        builder.setLength(builder.length() - 2);
        builder.append(DECL_END);
        return builder.toString();
    }

    public static String buildClassDeclaration(String className, String parent, String... interfaceNames) {
        Conditions.checkArgument(CollectionUtils.isNotEmpty(interfaceNames),
                "At least one interface should be specified");
        StringBuilder builder = new StringBuilder(50);
        builder.append(CLASS_DECL).append(className).append(EXTENDS_DECL).append(parent)
                .append(IMPLEMENTS_DECL);
        for (String interfaceName : interfaceNames) {
            builder.append(interfaceName).append(", ");
        }
        builder.setLength(builder.length() - 2);
        builder.append(DECL_END);
        return builder.toString();
    }

    public static String buildEnumDeclaration(String enumName) {
        return ENUM_DECL + enumName + DECL_END;
    }

    public static String buildGenericClassDeclaration(String className, List<String> generics) {
        Conditions.checkArgument(CollectionUtils.isNotEmpty(generics), "generics must not be null");
        StringBuilder declaration = new StringBuilder(30);
        declaration.append(CLASS_DECL).append(className).append("<");
        for (String generic : generics) {
            declaration.append(generic).append(", ");
        }
        declaration.setLength(declaration.length() - 2);
        declaration.append(">").append(DECL_END);
        return declaration.toString();
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
                (useCustomLoggerName ? "RemotingHelper.REMOTING_LOGGER_NAME" : className + ".class")
                + ");\n";
    }

    public static String buildFieldsByMetaData(List<FieldMetaData> fields) {
        Conditions.checkArgument(CollectionUtils.isNotEmpty(fields), "fields must not be null");
        StringBuilder builder = new StringBuilder(fields.size() * ASSUMED_FIELD_LEN);
        for (FieldMetaData field : fields) {
            if (field.getComment() != null) {
                builder.append(field.getComment()).append("\n");
            }
            builder.append("    ").append(field.getModifier()).append(field.getClazz()).append(" ")
                    .append(field.getName());
            if (field.getDefaultValue() != null) {
                builder.append(" = ").append(field.getDefaultValue());
            }
            builder.append(";\n");
        }
        return builder.toString();
    }

    public static String buildGetterAndSetterMethods(List<FieldMetaData> fields) {
        Conditions.checkArgument(CollectionUtils.isNotEmpty(fields), "fields must not be null");
        StringBuilder builder = new StringBuilder(fields.size() * ASSUMED_METHOD_LEN);
        for (FieldMetaData field : fields) {
            String name = field.getName();
            String type = field.getClazz();
            String methodName = StringTinyUtils.firstCharToUpperCase(field.getName());
            String getterName = "boolean".equals(type) ? " is" : " get";
            builder.append("    public void set").append(methodName).append("(").append(type)
                    .append(" ").append(name).append(") {\n")
                    .append("        this.").append(name).append(" = ").append(name).append(";\n")
                    .append("    }\n")
                    .append("\n")
                    .append("    public ").append(type).append(getterName).append(methodName).append("() {\n")
                    .append("        return ").append(name).append(";\n")
                    .append("    }\n\n");
        }
        return builder.toString();
    }


    private CodeGeneratorHelper() {
    }
}
