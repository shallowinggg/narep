package com.shallowinggg.narep.core.generators.protocol;

import com.shallowinggg.narep.core.annotation.Generator;
import com.shallowinggg.narep.core.generators.ClassCodeGenerator;
import com.shallowinggg.narep.core.lang.FieldInfo;

import java.util.Collections;

import static com.shallowinggg.narep.core.lang.Modifier.PRIVATE_STATIC_FINAL;
import static com.shallowinggg.narep.core.lang.Modifier.PUBLIC_ABSTRACT;

/**
 * @author shallowinggg
 */
@Generator
public class RemotingSerializableCodeGenerator extends ClassCodeGenerator {
    private static final String CLASS_NAME = "RemotingSerializable";
    private static final String SUB_PACKAGE = "protocol";

    public RemotingSerializableCodeGenerator() {
        super(CLASS_NAME, null, SUB_PACKAGE);
        setModifier(PUBLIC_ABSTRACT);
        setFields(Collections.singletonList(new FieldInfo(PRIVATE_STATIC_FINAL, "Charset",
                "CHARSET_UTF8", "StandardCharsets.UTF_8")));
    }

    @Override
    public String buildImports() {
        return "import com.alibaba.fastjson.JSON;\n" +
                "\n" +
                "import java.nio.charset.Charset;\n" +
                "import java.nio.charset.StandardCharsets;\n\n";
    }

    @Override
    public String buildMethods() {
        return "    public static byte[] encode(final Object obj) {\n" +
                "        final String json = toJson(obj, false);\n" +
                "        if (json != null) {\n" +
                "            return json.getBytes(CHARSET_UTF8);\n" +
                "        }\n" +
                "        return null;\n" +
                "    }\n" +
                "\n" +
                "    public static String toJson(final Object obj, boolean prettyFormat) {\n" +
                "        return JSON.toJSONString(obj, prettyFormat);\n" +
                "    }\n" +
                "\n" +
                "    public static <T> T decode(final byte[] data, Class<T> classOfT) {\n" +
                "        final String json = new String(data, CHARSET_UTF8);\n" +
                "        return fromJson(json, classOfT);\n" +
                "    }\n" +
                "\n" +
                "    public static <T> T fromJson(String json, Class<T> classOfT) {\n" +
                "        return JSON.parseObject(json, classOfT);\n" +
                "    }\n" +
                "\n" +
                "    public byte[] encode() {\n" +
                "        final String json = this.toJson();\n" +
                "        if (json != null) {\n" +
                "            return json.getBytes(CHARSET_UTF8);\n" +
                "        }\n" +
                "        return null;\n" +
                "    }\n" +
                "\n" +
                "    public String toJson() {\n" +
                "        return toJson(false);\n" +
                "    }\n" +
                "\n" +
                "    public String toJson(final boolean prettyFormat) {\n" +
                "        return toJson(this, prettyFormat);\n" +
                "    }\n";
    }
}
