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
    private static final int LONG_LEN = 8;
    private static final int LONG_7 = 7;
    private static final int LONG_6 = 6;
    private static final int LONG_5 = 5;
    private static final int INT_LEN = 4;
    private static final int INT_3 = 3;
    private static final int INT_2 = 2;


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
        String getterMethodName = "cmd.get" + StringTinyUtils.firstCharToUpperCase(name) + "()";
        builder.append("        // ").append(field.getClazz().getSimpleName()).append(" ").append(name).append("\n");
        builder.append(put(field, getterMethodName));
        return builder.toString();
    }

    public static String buildGetPrimitiveField(ProtocolField field) {
        Conditions.checkArgument(field != null && field.getLen() != -1, "field must be nonnull and primitive");
        StringBuilder builder = new StringBuilder(150);
        String name = field.getName();
        String setterMethodName = "cmd.set" + StringTinyUtils.firstCharToUpperCase(name) + "(";
        builder.append("        // ").append(field.getClazz().getSimpleName()).append(" ").append(name).append("\n");
        builder.append(get(field, setterMethodName));
        return builder.toString();
    }

    public static String buildGetCompositeField(ProtocolField field) {
        Conditions.checkArgument(field != null && field.getLen() == -1, "field must be nonnull and composite");
        StringBuilder builder = new StringBuilder(150);
        String name = field.getName();
        String lenName = name + "Length";
        String bytesName = name + "Content";
        String setterMethodName = "cmd.set" + StringTinyUtils.firstCharToUpperCase(name);
        String setterVal = String.class.equals(field.getClazz()) ? "new String(" + bytesName + ", CHARSET_UTF8)"
                : "mapDeserialize(" + bytesName + ")";
        builder.append("        // ").append(field.getClazz().getSimpleName()).append(" ").append(name).append("\n");
        builder.append("        int ").append(lenName).append(" = headerBuffer.getInt();\n");
        builder.append("        if (").append(lenName).append(" > 0) {\n");
        builder.append("            byte[] ").append(bytesName).append(" = new byte[").append(lenName).append("];\n");
        builder.append("            headerBuffer.get(").append(bytesName).append(");\n");
        builder.append("            ").append(setterMethodName).append("(").append(setterVal).append(");\n");
        builder.append("        }\n");
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

    private static String put(ProtocolField field, String getterName) {
        if (byte.class.equals(field.getClazz())) {
            return putByte(getterName);
        } else if (short.class.equals(field.getClazz())) {
            return putShort(getterName);
        } else if ((int.class.equals(field.getClazz()))) {
            if (field.getLen() == INT_LEN) {
                return putInt(getterName);
            } else if (field.getLen() == INT_3) {
                String name = field.getName();
                String upperName = StringTinyUtils.firstCharToUpperCase(name);
                String highName = "high" + upperName;
                String lowName = "low" + upperName;
                String val = "        int " + name + " = " + getterName + ";\n" +
                        "        byte " + highName + " = (byte) (" + name + " >> 16);\n" +
                        "        short " + lowName + " = (short) " + name + ";\n";
                return val + putByte(highName) + putShort(lowName);
            } else if (field.getLen() == INT_2) {
                return putShort("(short) " + getterName);
            } else {
                return putByte("(byte) " + getterName);
            }
        } else {
            if (field.getLen() == LONG_LEN) {
                return putLong(getterName);
            } else if (field.getLen() == LONG_7) {
                String name = field.getName();
                String upperName = StringTinyUtils.firstCharToUpperCase(name);
                String highName1 = "high" + upperName + "1";
                String highName2 = "high" + upperName + "2";
                String lowName = "low" + upperName;
                String val = "        long " + name + " = " + getterName + ";\n" +
                        "        byte " + highName1 + " = (byte) (" + name + " >> 48);\n" +
                        "        short " + highName2 + " = (short) (" + name + " >> 32;\n" +
                        "        int " + lowName + " = (int) " + name + ";\n";
                return val + putByte(highName1) + putShort(highName2) + putInt(lowName);
            } else if (field.getLen() == LONG_6) {
                String name = field.getName();
                String upperName = StringTinyUtils.firstCharToUpperCase(name);
                String highName = "high" + upperName;
                String lowName = "low" + upperName;
                String val = "        long " + name + " = " + getterName + ";\n" +
                        "        short " + highName + " = (short) (" + name + " >> 32;\n" +
                        "        int " + lowName + " = (int) " + name + ";\n";
                return val + putShort(highName) + putInt(lowName);
            } else {
                String name = field.getName();
                String upperName = StringTinyUtils.firstCharToUpperCase(name);
                String highName = "high" + upperName;
                String lowName = "low" + upperName;
                String val = "        long " + name + " = " + getterName + ";\n" +
                        "        byte " + highName + " = (byte) (" + name + " >> 32);\n" +
                        "        int " + lowName + " = (int) " + name + ";\n";
                return val + putShort(highName) + putInt(lowName);
            }
        }
    }

    private static String putByte(String val) {
        return "        headerBuffer.put(" + val + ");\n";
    }

    private static String putShort(String val) {
        return "        headerBuffer.putShort(" + val + ");\n";
    }

    private static String putInt(String val) {
        return "        headerBuffer.putInt(" + val + ");\n";
    }

    private static String putLong(String val) {
        return "        headerBuffer.putLong(" + val + ");\n";
    }

    private static String get(ProtocolField field, String setterName) {
        if (byte.class.equals(field.getClazz())) {
            return getByte(setterName);
        } else if (short.class.equals(field.getClazz())) {
            return getShort(setterName);
        } else if ((int.class.equals(field.getClazz()))) {
            if (field.getLen() == INT_LEN) {
                return getInt(setterName);
            } else if (field.getLen() == INT_3) {
                String name = field.getName();
                String upperName = StringTinyUtils.firstCharToUpperCase(name);
                String highName = "high" + upperName;
                String lowName = "low" + upperName;
                String val = "        byte " + highName + " = headerBuffer.get();\n" +
                        "        short " + lowName + " = headerBuffer.getShort();\n" +
                        "        int " + name + " = (" + highName + " << 16) + " + lowName + ";\n";
                return val + setter(setterName, name);
            } else if (field.getLen() == INT_2) {
                return getShort(setterName);
            } else {
                return getByte(setterName);
            }
        } else {
            if (field.getLen() == LONG_LEN) {
                return getLong(setterName);
            } else if (field.getLen() == LONG_7) {
                String name = field.getName();
                String upperName = StringTinyUtils.firstCharToUpperCase(name);
                String highName1 = "high" + upperName + "1";
                String highName2 = "high" + upperName + "2";
                String lowName = "low" + upperName;
                String val = "        byte " + highName1 + " = headerBuffer.get();\n" +
                        "        short " + highName2 + " = headerBuffer.getShort();\n" +
                        "        int " + lowName + " = headerBuffer.getInt();\n" +
                        "        long " + name + " = ((long) ((" + highName1 + " << 16) + " + highName2 + ") << 32) + " + lowName + ";\n";
                return val + setter(setterName, name);
            } else if (field.getLen() == LONG_6) {
                String name = field.getName();
                String upperName = StringTinyUtils.firstCharToUpperCase(name);
                String highName = "high" + upperName;
                String lowName = "low" + upperName;
                String val = "        short " + highName + " = headerBuffer.getShort();\n" +
                        "        int " + lowName + " = headerBuffer.getInt();\n" +
                        "        long " + name + " = ((long) " + highName + " << 32) + " + lowName + ";\n";
                return val + setter(setterName, name);
            } else {
                String name = field.getName();
                String upperName = StringTinyUtils.firstCharToUpperCase(name);
                String highName = "high" + upperName;
                String lowName = "low" + upperName;
                String val = "        byte " + highName + " = headerBuffer.get();\n" +
                        "        int " + lowName + " = headerBuffer.getInt();\n" +
                        "        long " + name + " = ((long) " + highName + " << 48) + " + lowName + ";\n";
                return val + setter(setterName, name);
            }
        }
    }

    private static String getByte(String setter) {
        return "        " + setter + "(headBuffer.get());\n";
    }

    private static String getShort(String setter) {
        return "        " + setter + "(headBuffer.getShort());\n";
    }

    private static String getInt(String setter) {
        return "        " + setter + "(headBuffer.getInt());\n";
    }

    private static String getLong(String setter) {
        return "        " + setter + "(headBuffer.getLong());\n";
    }

    private static String setter(String setter, String val) {
        return "        " + setter + "(" + val + ");\n";
    }

    private SerializableHelper() {
    }
}
