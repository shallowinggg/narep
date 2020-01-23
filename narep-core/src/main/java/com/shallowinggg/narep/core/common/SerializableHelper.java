package com.shallowinggg.narep.core.common;

import com.shallowinggg.narep.core.lang.ProtocolField;
import com.shallowinggg.narep.core.util.CollectionUtils;
import com.shallowinggg.narep.core.util.Conditions;
import com.shallowinggg.narep.core.util.StringTinyUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author shallowinggg
 */
public class SerializableHelper {

    public static List<ProtocolField> primitiveFields(List<ProtocolField> allFields) {
        List<ProtocolField> compositeFields = compositeFields(allFields);
        return allFields.subList(0, allFields.size() - compositeFields.size());
    }

    public static List<ProtocolField> compositeFields(List<ProtocolField> allFields) {
        Conditions.checkArgument(CollectionUtils.isNotEmpty(allFields), "allFields must not be empty");
        int start = 0;
        int end = allFields.size();
        int middle = start + (end - start) >> 1;
        int pos = 0;
        while (start < end) {
            if (allFields.get(middle).getLen() == -1) {
                end = middle;
                middle = start + (end - start) >> 1;
            } else {
                if (middle + 1 < end && allFields.get(middle + 1).getLen() == -1) {
                    pos = middle + 1;
                    break;
                } else {
                    start = middle;
                    middle = start + (end - start) >> 1;
                }
            }
        }

        if (pos != 0) {
            return allFields.subList(pos, allFields.size());
        }
        return Collections.emptyList();
    }

    public static List<String> buildLocalVarLenNames(List<ProtocolField> compositeFields) {
        Conditions.checkArgument(CollectionUtils.isNotEmpty(compositeFields), "compositeFields must not be empty");
        List<String> names = new ArrayList<>(compositeFields.size());
        for (ProtocolField field : compositeFields) {
            String name = field.getName();
            names.add(name + "Len");
        }
        return names;
    }

    public static String buildCompositeField(ProtocolField field) {
        Conditions.checkArgument(field != null && field.getLen() == -1, "field must be nonnull and composite");
        StringBuilder builder = new StringBuilder(500);
        String name = field.getName();
        String bytesName = name + "Bytes";
        String lenName = name + "Len";
        String getterMethodName = "cmd.get" + StringTinyUtils.firstCharToUpperCase(name) + "()";
        String existMethodName = String.class.equals(field.getClazz()) ? getterMethodName + ".length() > 0"
                : "!" + getterMethodName + ".isEmpty()";
        String bytesVal = String.class.equals(field.getClazz()) ? getterMethodName + ".getBytes(CHARSET_UTF8)"
                : "mapSerialize(" + getterMethodName + ")";
        builder.append("        // ").append(field.getClazz().getSimpleName()).append(" ").append(name).append("\n");
        builder.append("        byte[] ").append(bytesName).append(" = null;\n");
        builder.append("        int ").append(lenName).append(" = 0;\n");
        builder.append("        if (").append(getterMethodName).append(" != null && ").append(existMethodName).append(") {\n");
        builder.append("            ").append(bytesName).append(" = ").append(bytesVal).append(";\n");
        builder.append("            ").append(lenName).append(" = ").append(bytesName).append(".length;\n");
        builder.append("        }\n\n");
        return builder.toString();
    }

    public static String buildPutCompositeField(ProtocolField field) {
        Conditions.checkArgument(field != null && field.getLen() == -1, "field must be nonnull and composite");
        StringBuilder builder = new StringBuilder(300);
        String name = field.getName();
        String bytesName = name + "Bytes";
        builder.append("        // ").append(field.getClazz().getSimpleName()).append(" ").append(name).append("\n");
        builder.append("        if (").append(bytesName).append(" != null ) {\n");
        builder.append("            headerBuffer.putInt(").append(bytesName).append(".length);\n");
        builder.append("            headerBuffer.put(").append(bytesName).append(");\n");
        builder.append("        } else {\n");
        builder.append("            headerBuffer.putInt(0);\n");
        builder.append("        }\n");
        return builder.toString();
    }

    public static String buildPutPrimitiveField(ProtocolField field) {
        Conditions.checkArgument(field != null && field.getLen() != -1, "field must be nonnull and primitive");
        StringBuilder builder = new StringBuilder(150);
        String name = field.getName();
        builder.append("        // ").append(field.getClazz().getSimpleName()).append(" ").append(name).append("\n");


        return builder.toString();
    }

    public static String buildCalcTotalLen(List<ProtocolField> fields) {
        Conditions.checkArgument(CollectionUtils.isNotEmpty(fields), "fields must not be empty");
        StringBuilder builder = new StringBuilder(1000);
        builder.append("    private static int calTotalLen(int[] composites) {\n")
                .append("        return ");
        int len = fields.get(0).getLen();
        String type = fields.get(0).getClazz().getSimpleName();
        String name = fields.get(0).getName();
        builder.append(len).append(" // ").append(type).append(" ").append(name).append("\n");

        Iterator<ProtocolField> itr = fields.iterator();
        itr.next();

        int i = 0;
        while (itr.hasNext()) {
            ProtocolField field = itr.next();
            len = field.getLen();
            type = field.getClazz().getSimpleName();
            name = field.getName();

            if (len != -1) {
                builder.append("                + ").append(len)
                        .append(" // ").append(type).append(" ").append(name).append("\n");
            } else {
                builder.append("                + 4 + composite[").append(i).append("]")
                        .append(" // ").append(type).append(" ").append(name).append("\n");
                i++;
            }
        }
        builder.append("                ;\n")
                .append("    }\n\n");
        return builder.toString();
    }
}
