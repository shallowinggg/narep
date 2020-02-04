package com.shallowinggg.narep.core.common;

import com.shallowinggg.narep.core.GeneratorController;
import com.shallowinggg.narep.core.lang.ProtocolField;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SerializableHelperTest {

    @BeforeClass
    public static void before() {
        GeneratorController controller = new GeneratorController();
        controller.init();
        controller.registerProtocolField(new ProtocolField("ext", HashMap.class, -1));
    }

    @Test
    public void testPrimitiveFields() {
        List<ProtocolField> val = SerializableHelper.primitiveFields(ConfigInfos.getInstance().protocolFields());
        Assert.assertEquals("[ProtocolField[name='code', clazz=int, len=2], ProtocolField[name='flag', clazz=int, len=1], " +
                "ProtocolField[name='opaque', clazz=int, len=4]]", val.toString());

    }

    @Test
    public void testCompositeFields() {
        List<ProtocolField> val = SerializableHelper.compositeFields(ConfigInfos.getInstance().protocolFields());
        Assert.assertEquals("[ProtocolField[name='remark', clazz=class java.lang.String, len=-1], " +
                "ProtocolField[name='ext', clazz=class java.util.HashMap, len=-1]]", val.toString());

        List<ProtocolField> origin = new ArrayList<>(3);
        origin.add(new ProtocolField("a", int.class, 4));
        origin.add(new ProtocolField("b", int.class, 4));
        origin.add(new ProtocolField("c", int.class, 4));
        Assert.assertEquals(0, SerializableHelper.compositeFields(origin).size());

        origin = new ArrayList<>(3);
        origin.add(new ProtocolField("a", String.class, -1));
        origin.add(new ProtocolField("b", String.class, -1));
        origin.add(new ProtocolField("c", String.class, -1));
        Assert.assertEquals(3, SerializableHelper.compositeFields(origin).size());
    }

    @Test
    public void testBuildCompositeField() {
        List<ProtocolField> compositeFields = SerializableHelper.compositeFields(ConfigInfos.getInstance().protocolFields());
        StringBuilder builder = new StringBuilder(600);
        for(ProtocolField field : compositeFields) {
            builder.append(SerializableHelper.buildCompositeFieldEncodeData(field));
        }
        Assert.assertEquals("        // String remark\n" +
                "        byte[] remarkBytes = null;\n" +
                "        int remarkLen = 0;\n" +
                "        if (cmd.getRemark() != null && cmd.getRemark().length() > 0) {\n" +
                "            remarkBytes = cmd.getRemark().getBytes(CHARSET_UTF8);\n" +
                "            remarkLen = remarkBytes.length;\n" +
                "        }\n" +
                "\n" +
                "        // HashMap ext\n" +
                "        byte[] extBytes = null;\n" +
                "        int extLen = 0;\n" +
                "        if (cmd.getExt() != null && !cmd.getExt().isEmpty()) {\n" +
                "            extBytes = mapSerialize(cmd.getExt());\n" +
                "            extLen = extBytes.length;\n" +
                "        }\n\n", builder.toString());
    }

    @Test
    public void testBuildPutCompositeField() {
        List<ProtocolField> compositeFields = SerializableHelper.compositeFields(ConfigInfos.getInstance().protocolFields());
        StringBuilder builder = new StringBuilder(500);
        for(ProtocolField field : compositeFields) {
            builder.append(SerializableHelper.buildPutCompositeField(field));
        }
        Assert.assertEquals("        // String remark\n" +
                "        if (remarkBytes != null) {\n" +
                "            headerBuffer.putInt(remarkBytes.length);\n" +
                "            headerBuffer.put(remarkBytes);\n" +
                "        } else {\n" +
                "            headerBuffer.putInt(0);\n" +
                "        }\n" +
                "        // HashMap ext\n" +
                "        if (extBytes != null) {\n" +
                "            headerBuffer.putInt(extBytes.length);\n" +
                "            headerBuffer.put(extBytes);\n" +
                "        } else {\n" +
                "            headerBuffer.putInt(0);\n" +
                "        }\n", builder.toString());
    }

    @Test
    public void testBuildPutPrimitiveField() {
        List<ProtocolField> primitiveFields = SerializableHelper.primitiveFields(ConfigInfos.getInstance().protocolFields());
        StringBuilder builder = new StringBuilder(300);
        for(ProtocolField field : primitiveFields) {
            builder.append(SerializableHelper.buildPutPrimitiveField(field));
        }
        Assert.assertEquals("        // int code [2 bytes]\n" +
                "        headerBuffer.putShort((short) cmd.getCode());\n" +
                "        // int flag [1 bytes]\n" +
                "        headerBuffer.put((byte) cmd.getFlag());\n" +
                "        // int opaque [4 bytes]\n" +
                "        headerBuffer.putInt(cmd.getOpaque());\n", builder.toString());
    }

    @Test
    public void testBuildGetPrimitiveField() {
        List<ProtocolField> primitiveFields = SerializableHelper.primitiveFields(ConfigInfos.getInstance().protocolFields());
        StringBuilder builder = new StringBuilder(200);
        for(ProtocolField field : primitiveFields) {
            builder.append(SerializableHelper.buildGetPrimitiveField(field));
        }
        Assert.assertEquals("        // int code [2 bytes]\n" +
                "        cmd.setCode(headerBuffer.getShort());\n" +
                "        // int flag [1 bytes]\n" +
                "        cmd.setFlag(headerBuffer.get());\n" +
                "        // int opaque [4 bytes]\n" +
                "        cmd.setOpaque(headerBuffer.getInt());\n", builder.toString());
    }

    @Test
    public void testBuildGetCompositeField() {
        List<ProtocolField> compositeFields = SerializableHelper.compositeFields(ConfigInfos.getInstance().protocolFields());
        StringBuilder builder = new StringBuilder(600);
        for(ProtocolField field : compositeFields) {
            builder.append(SerializableHelper.buildGetCompositeField(field));
        }
        Assert.assertEquals("        // String remark\n" +
                "        int remarkLength = headerBuffer.getInt();\n" +
                "        if (remarkLength > 0) {\n" +
                "            byte[] remarkContent = new byte[remarkLength];\n" +
                "            headerBuffer.get(remarkContent);\n" +
                "            cmd.setRemark(new String(remarkContent, CHARSET_UTF8));\n" +
                "        }\n" +
                "        // HashMap ext\n" +
                "        int extLength = headerBuffer.getInt();\n" +
                "        if (extLength > 0) {\n" +
                "            byte[] extContent = new byte[extLength];\n" +
                "            headerBuffer.get(extContent);\n" +
                "            cmd.setExt(mapDeserialize(extContent));\n" +
                "        }\n", builder.toString());
    }

    @Test
    public void testBuildCalcTotalLen() {
        List<ProtocolField> fields = ConfigInfos.getInstance().protocolFields();
        Assert.assertEquals("    private static int calTotalLen(int[] composites) {\n" +
                "        return 2 // int code\n" +
                "                + 1 // int flag\n" +
                "                + 4 // int opaque\n" +
                "                + 4 + composites[0] // String remark\n" +
                "                + 4 + composites[1] // HashMap ext\n" +
                "                ;\n" +
                "    }\n\n", SerializableHelper.buildCalcTotalLen(fields));
    }
}
