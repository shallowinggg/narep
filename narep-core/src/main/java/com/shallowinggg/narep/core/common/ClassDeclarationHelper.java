package com.shallowinggg.narep.core.common;

import com.shallowinggg.narep.core.lang.Modifier;
import com.shallowinggg.narep.core.util.Conditions;

import java.util.List;

import static com.shallowinggg.narep.core.lang.JLSConstants.*;

/**
 * @author shallowinggg
 */
public class ClassDeclarationHelper {

    public static String buildGeneric(List<String> generics) {
        Conditions.notEmpty(generics, "generics must not be null or emprty");
        StringBuilder builder = new StringBuilder(2 + generics.size() * 2);
        builder.append('<');
        for(String generic : generics) {
            builder.append(generic).append(", ");
        }
        builder.setLength(builder.length() - 2);
        builder.append('>');
        return builder.toString();
    }

    public static String buildInterfaceDeclaration(Modifier modifier, String interfaceName) {
        return modifier + INTERFACE_DECL + interfaceName + BLOCK_START;
    }

    public static String buildInterfaceDeclaration(Modifier modifier, String interfaceName, String parent) {
        return modifier + INTERFACE_DECL + interfaceName + EXTENDS_DECL + parent + BLOCK_START;
    }

    public static String buildClassDeclaration(Modifier modifier, String className) {
        return modifier + CLASS_DECL + className + BLOCK_START;
    }

    public static String buildClassDeclaration(Modifier modifier, String className, String parent) {
        return modifier + CLASS_DECL + className + EXTENDS_DECL + parent + BLOCK_START;
    }

    public static String buildClassDeclaration(Modifier modifier, String className, String... interfaceNames) {
        Conditions.notEmpty(interfaceNames, "At least one interface should be specified");
        StringBuilder builder = new StringBuilder(50);
        builder.append(modifier).append(CLASS_DECL).append(className).append(IMPLEMENTS_DECL);
        for (String interfaceName : interfaceNames) {
            builder.append(interfaceName).append(", ");
        }
        builder.setLength(builder.length() - 2);
        builder.append(BLOCK_START);
        return builder.toString();
    }

    public static String buildClassDeclaration(Modifier modifier, String className, String parent, String... interfaceNames) {
        Conditions.notEmpty(interfaceNames, "At least one interface should be specified");
        StringBuilder builder = new StringBuilder(50);
        builder.append(modifier).append(CLASS_DECL).append(className).append(EXTENDS_DECL).append(parent)
                .append(IMPLEMENTS_DECL);
        for (String interfaceName : interfaceNames) {
            builder.append(interfaceName).append(", ");
        }
        builder.setLength(builder.length() - 2);
        builder.append(BLOCK_START);
        return builder.toString();
    }

    public static String buildEnumDeclaration(Modifier modifier, String enumName) {
        return modifier + ENUM_DECL + enumName + BLOCK_START;
    }

    public static String buildAnnotationDeclaration(Modifier modifier, String annotationName) {
        return modifier + ANNOTATION_DECL + annotationName + BLOCK_START;
    }

    private ClassDeclarationHelper() {
    }
}
