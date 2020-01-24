package com.shallowinggg.narep.core.generators.protocol;

import com.shallowinggg.narep.core.common.ConfigInfos;
import com.shallowinggg.narep.core.common.SerializableHelper;
import com.shallowinggg.narep.core.generators.ClassCodeGenerator;
import com.shallowinggg.narep.core.lang.FieldInfo;
import com.shallowinggg.narep.core.lang.Modifier;
import com.shallowinggg.narep.core.lang.ProtocolField;

import java.util.Collections;
import java.util.List;

/**
 * @author shallowinggg
 */
public class NarepSerializableCodeGenerator extends ClassCodeGenerator {
    private static final String CLASS_NAME = "NarepSerializable";
    private static final String SUB_PACKAGE = "protocol";
    private List<ProtocolField> protocolFields;
    private List<ProtocolField> primitiveFields;
    private List<ProtocolField> compositeFields;

    public NarepSerializableCodeGenerator() {
        super(CLASS_NAME, null, SUB_PACKAGE);
        setFields(Collections.singletonList(new FieldInfo(Modifier.PRIVATE_STATIC_FINAL, "CHARSET_UTF8",
                "StandardCharsets.UTF_8")));

        protocolFields = ConfigInfos.getInstance().protocolFields();
        primitiveFields = SerializableHelper.primitiveFields(protocolFields);
        compositeFields = SerializableHelper.compositeFields(protocolFields);
    }

    @Override
    public String buildMethods() {
        return narepProtocolEncode() +
                mapSerialize() +
                calcTotalLen() +
                narepProtocolDecode() +
                mapDeserialize();
    }

    private String narepProtocolEncode() {
        StringBuilder builder = new StringBuilder(3000);
        builder.append("    public static byte[] narepProtocolEncode(RemotingCommand cmd) {\n");
        for (ProtocolField compositeField : compositeFields) {
            builder.append(SerializableHelper.buildCompositeField(compositeField));
        }
        List<String> lens = SerializableHelper.buildLocalVarLenNames(compositeFields);
        String values = lens.toString();
        values = values.substring(1, values.length() - 1);
        builder.append("        int[] composites = new int[]{").append(values).append("};\n");
        builder.append("        int totalLen = calTotalLen(composites);\n")
                .append("\n")
                .append("        ByteBuffer headerBuffer = ByteBuffer.allocate(totalLen);\n");
        for (ProtocolField primitiveField : primitiveFields) {
            builder.append(SerializableHelper.buildPutPrimitiveField(primitiveField));
        }
        for (ProtocolField compositeField : compositeFields) {
            builder.append(SerializableHelper.buildPutCompositeField(compositeField));
        }
        builder.append("\n")
                .append("        return headerBuffer.array();\n")
                .append("    }\n\n");

        return builder.toString();
    }

    private String mapSerialize() {
        return "    public static byte[] mapSerialize(HashMap<String, String> map) {\n" +
                "        // keySize+key+valSize+val\n" +
                "        if (null == map || map.isEmpty()) {\n" +
                "            return null;\n" +
                "        }\n" +
                "\n" +
                "        int totalLength = 0;\n" +
                "        int kvLength;\n" +
                "        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();\n" +
                "        while (it.hasNext()) {\n" +
                "            Map.Entry<String, String> entry = it.next();\n" +
                "            if (entry.getKey() != null && entry.getValue() != null) {\n" +
                "                kvLength =\n" +
                "                        // keySize + Key\n" +
                "                        2 + entry.getKey().getBytes(CHARSET_UTF8).length +\n" +
                "                                // valSize + val\n" +
                "                                4 + entry.getValue().getBytes(CHARSET_UTF8).length;\n" +
                "                totalLength += kvLength;\n" +
                "            }\n" +
                "        }\n" +
                "\n" +
                "        ByteBuffer content = ByteBuffer.allocate(totalLength);\n" +
                "        byte[] key;\n" +
                "        byte[] val;\n" +
                "        it = map.entrySet().iterator();\n" +
                "        while (it.hasNext()) {\n" +
                "            Map.Entry<String, String> entry = it.next();\n" +
                "            if (entry.getKey() != null && entry.getValue() != null) {\n" +
                "                key = entry.getKey().getBytes(CHARSET_UTF8);\n" +
                "                val = entry.getValue().getBytes(CHARSET_UTF8);\n" +
                "\n" +
                "                content.putShort((short) key.length);\n" +
                "                content.put(key);\n" +
                "\n" +
                "                content.putInt(val.length);\n" +
                "                content.put(val);\n" +
                "            }\n" +
                "        }\n" +
                "\n" +
                "        return content.array();\n" +
                "    }\n\n";
    }

    private String calcTotalLen() {
        return SerializableHelper.buildCalcTotalLen(protocolFields);
    }

    private String narepProtocolDecode() {
        StringBuilder builder = new StringBuilder(3000);
        builder.append("    public static RemotingCommand narepProtocolDecode(final byte[] headerArray) {\n")
                .append("        RemotingCommand cmd = new RemotingCommand();\n")
                .append("        ByteBuffer headerBuffer = ByteBuffer.wrap(headerArray);\n");
        for (ProtocolField primitiveField : primitiveFields) {
            builder.append(SerializableHelper.buildGetPrimitiveField(primitiveField));
        }
        for (ProtocolField compositeField : compositeFields) {
            builder.append(SerializableHelper.buildGetCompositeField(compositeField));
        }
        builder.append("        return cmd;\n")
                .append("    }\n\n");
        return builder.toString();
    }

    private String mapDeserialize() {
        return "    public static HashMap<String, String> mapDeserialize(byte[] bytes) {\n" +
                "        if (bytes == null || bytes.length <= 0) {\n" +
                "            return null;\n" +
                "        }\n" +
                "\n" +
                "        HashMap<String, String> map = new HashMap<String, String>();\n" +
                "        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);\n" +
                "\n" +
                "        short keySize;\n" +
                "        byte[] keyContent;\n" +
                "        int valSize;\n" +
                "        byte[] valContent;\n" +
                "        while (byteBuffer.hasRemaining()) {\n" +
                "            keySize = byteBuffer.getShort();\n" +
                "            keyContent = new byte[keySize];\n" +
                "            byteBuffer.get(keyContent);\n" +
                "\n" +
                "            valSize = byteBuffer.getInt();\n" +
                "            valContent = new byte[valSize];\n" +
                "            byteBuffer.get(valContent);\n" +
                "\n" +
                "            map.put(new String(keyContent, CHARSET_UTF8), new String(valContent, CHARSET_UTF8));\n" +
                "        }\n" +
                "        return map;\n" +
                "    }\n\n";
    }

}
