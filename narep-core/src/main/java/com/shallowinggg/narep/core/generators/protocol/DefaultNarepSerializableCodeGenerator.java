package com.shallowinggg.narep.core.generators.protocol;

import com.shallowinggg.narep.core.annotation.Generator;
import com.shallowinggg.narep.core.annotation.Profile;
import com.shallowinggg.narep.core.generators.ClassCodeGenerator;
import com.shallowinggg.narep.core.lang.FieldInfo;
import com.shallowinggg.narep.core.lang.Modifier;

import java.util.Collections;

/**
 * @author shallowinggg
 */
@Generator
@Profile("default")
public class DefaultNarepSerializableCodeGenerator extends ClassCodeGenerator {
    private static final String CLASS_NAME = "NarepSerializable";
    private static final String SUB_PACKAGE = "protocol";

    public DefaultNarepSerializableCodeGenerator() {
        super(CLASS_NAME, null, SUB_PACKAGE);
        setFields(Collections.singletonList(new FieldInfo(Modifier.PRIVATE_STATIC_FINAL, "Charset",
                "CHARSET_UTF8", "StandardCharsets.UTF_8")));
    }

    @Override
    public String buildImports() {
        return "import java.nio.ByteBuffer;\n" +
                "import java.nio.charset.Charset;\n" +
                "import java.nio.charset.StandardCharsets;\n" +
                "import java.util.HashMap;\n" +
                "import java.util.Iterator;\n" +
                "import java.util.Map;\n\n";
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
        return "    public static byte[] narepProtocolEncode(RemotingCommand cmd) {\n" +
                "        // String remark\n" +
                "        byte[] remarkBytes = null;\n" +
                "        int remarkLen = 0;\n" +
                "        if (cmd.getRemark() != null && cmd.getRemark().length() > 0) {\n" +
                "            remarkBytes = cmd.getRemark().getBytes(CHARSET_UTF8);\n" +
                "            remarkLen = remarkBytes.length;\n" +
                "        }\n" +
                "\n" +
                "        // HashMap<String, String> extFields\n" +
                "        byte[] extFieldsBytes = null;\n" +
                "        int extLen = 0;\n" +
                "        if (cmd.getExtFields() != null && !cmd.getExtFields().isEmpty()) {\n" +
                "            extFieldsBytes = mapSerialize(cmd.getExtFields());\n" +
                "            extLen = extFieldsBytes.length;\n" +
                "        }\n" +
                "\n" +
                "        int totalLen = calTotalLen(remarkLen, extLen);\n" +
                "\n" +
                "        ByteBuffer headerBuffer = ByteBuffer.allocate(totalLen);\n" +
                "        // int code(~32767)\n" +
                "        headerBuffer.putShort((short) cmd.getCode());\n" +
                "        // LanguageCode language\n" +
                "        headerBuffer.put(cmd.getLanguage().getCode());\n" +
                "        // int version(~32767)\n" +
                "        headerBuffer.putShort((short) cmd.getVersion());\n" +
                "        // int opaque\n" +
                "        headerBuffer.putInt(cmd.getOpaque());\n" +
                "        // int flag\n" +
                "        headerBuffer.putInt(cmd.getFlag());\n" +
                "        // String remark\n" +
                "        if (remarkBytes != null) {\n" +
                "            headerBuffer.putInt(remarkBytes.length);\n" +
                "            headerBuffer.put(remarkBytes);\n" +
                "        } else {\n" +
                "            headerBuffer.putInt(0);\n" +
                "        }\n" +
                "        // HashMap<String, String> extFields;\n" +
                "        if (extFieldsBytes != null) {\n" +
                "            headerBuffer.putInt(extFieldsBytes.length);\n" +
                "            headerBuffer.put(extFieldsBytes);\n" +
                "        } else {\n" +
                "            headerBuffer.putInt(0);\n" +
                "        }\n" +
                "\n" +
                "        return headerBuffer.array();\n" +
                "    }\n\n";
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
        return "    private static int calTotalLen(int remark, int ext) {\n" +
                "        // int code(~32767)\n" +
                "        int length = 2\n" +
                "            // LanguageCode language\n" +
                "            + 1\n" +
                "            // int version(~32767)\n" +
                "            + 2\n" +
                "            // int opaque\n" +
                "            + 4\n" +
                "            // int flag\n" +
                "            + 4\n" +
                "            // String remark\n" +
                "            + 4 + remark\n" +
                "            // HashMap<String, String> extFields\n" +
                "            + 4 + ext;\n" +
                "\n" +
                "        return length;\n" +
                "    }\n\n";
    }

    private String narepProtocolDecode() {
        return "    public static RemotingCommand narepProtocolDecode(final byte[] headerArray) {\n" +
                "        RemotingCommand cmd = new RemotingCommand();\n" +
                "        ByteBuffer headerBuffer = ByteBuffer.wrap(headerArray);\n" +
                "        // int code(~32767)\n" +
                "        cmd.setCode(headerBuffer.getShort());\n" +
                "        // LanguageCode language\n" +
                "        cmd.setLanguage(LanguageCode.valueOf(headerBuffer.get()));\n" +
                "        // int version(~32767)\n" +
                "        cmd.setVersion(headerBuffer.getShort());\n" +
                "        // int opaque\n" +
                "        cmd.setOpaque(headerBuffer.getInt());\n" +
                "        // int flag\n" +
                "        cmd.setFlag(headerBuffer.getInt());\n" +
                "        // String remark\n" +
                "        int remarkLength = headerBuffer.getInt();\n" +
                "        if (remarkLength > 0) {\n" +
                "            byte[] remarkContent = new byte[remarkLength];\n" +
                "            headerBuffer.get(remarkContent);\n" +
                "            cmd.setRemark(new String(remarkContent, CHARSET_UTF8));\n" +
                "        }\n" +
                "\n" +
                "        // HashMap<String, String> extFields\n" +
                "        int extFieldsLength = headerBuffer.getInt();\n" +
                "        if (extFieldsLength > 0) {\n" +
                "            byte[] extFieldsBytes = new byte[extFieldsLength];\n" +
                "            headerBuffer.get(extFieldsBytes);\n" +
                "            cmd.setExtFields(mapDeserialize(extFieldsBytes));\n" +
                "        }\n" +
                "        return cmd;\n" +
                "    }\n\n";
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
