package com.shallowinggg.narep.core.generators.protocol;

import com.shallowinggg.narep.core.annotation.Generator;
import com.shallowinggg.narep.core.annotation.Profile;
import com.shallowinggg.narep.core.common.CodeGeneratorHelper;
import com.shallowinggg.narep.core.generators.ClassCodeGenerator;
import com.shallowinggg.narep.core.lang.FieldInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.shallowinggg.narep.core.lang.Modifier.*;

/**
 * @author shallowinggg
 */
@Generator
@Profile("default")
public class DefaultRemotingCommandCodeGenerator extends ClassCodeGenerator {
    private static final String CLASS_NAME = "RemotingCommand";
    private static final String SUB_PACKAGE = "protocol";
    private static final List<String> DEPENDENCY_NAMES = Arrays.asList("CommandCustomHeader",
            "CFNotNull", "RemotingHelper", "RemotingCommandException",
            "RemotingCommandType", "RemotingSerializable", "RemotingSysResponseCode",
            "SerializeType");

    public DefaultRemotingCommandCodeGenerator() {
        super(CLASS_NAME, null, SUB_PACKAGE);
        setDependencyNames(DEPENDENCY_NAMES);

        List<FieldInfo> fields = new ArrayList<>(31);
        fields.add(new FieldInfo(PRIVATE_STATIC_FINAL, "String", "SERIALIZE_TYPE_PROPERTY", "\"remoting.serialize.type\""));
        fields.add(new FieldInfo(PRIVATE_STATIC_FINAL, "String", "SERIALIZE_TYPE_ENV", "\"REMOTING_SERIALIZE_TYPE\""));
        fields.add(new FieldInfo(PRIVATE_STATIC_FINAL, "String", "REMOTING_VERSION_KEY", "\"remoting.remoting.version\""));
        fields.add(new FieldInfo(PRIVATE_STATIC_FINAL, "Logger", "log", CodeGeneratorHelper.buildLoggerField(CLASS_NAME)));
        fields.add(new FieldInfo(PRIVATE_STATIC_FINAL, "int", "RPC_TYPE", "0"));
        fields.add(new FieldInfo(PRIVATE_STATIC_FINAL, "int", "RPC_ONEWAY", "1"));
        fields.add(new FieldInfo(PRIVATE_STATIC_FINAL, "Map<Class<? extends CommandCustomHeader>, Field[]>",
                "CLASS_HASH_MAP", "new HashMap<>()"));
        fields.add(new FieldInfo(PRIVATE_STATIC_FINAL, "Map<Class<?>, String>", "CANONICAL_NAME_CACHE", "new HashMap<>()"));
        fields.add(new FieldInfo(PRIVATE_STATIC_FINAL, "Map<Field, Boolean>", "NULLABLE_FIELD_CACHE", "new HashMap<>()"));
        fields.add(new FieldInfo(PRIVATE_STATIC_FINAL, "String", "STRING_CANONICAL_NAME", "String.class.getCanonicalName()"));
        fields.add(new FieldInfo(PRIVATE_STATIC_FINAL, "String", "DOUBLE_CANONICAL_NAME_1", "Double.class.getCanonicalName()"));
        fields.add(new FieldInfo(PRIVATE_STATIC_FINAL, "String", "DOUBLE_CANONICAL_NAME_2", "double.class.getCanonicalName()"));
        fields.add(new FieldInfo(PRIVATE_STATIC_FINAL, "String", "INTEGER_CANONICAL_NAME_1", "Integer.class.getCanonicalName()"));
        fields.add(new FieldInfo(PRIVATE_STATIC_FINAL, "String", "INTEGER_CANONICAL_NAME_2", "int.class.getCanonicalName()"));
        fields.add(new FieldInfo(PRIVATE_STATIC_FINAL, "String", "LONG_CANONICAL_NAME_1", "Long.class.getCanonicalName()"));
        fields.add(new FieldInfo(PRIVATE_STATIC_FINAL, "String", "LONG_CANONICAL_NAME_2", "long.class.getCanonicalName()"));
        fields.add(new FieldInfo(PRIVATE_STATIC_FINAL, "String", "BOOLEAN_CANONICAL_NAME_1", "Boolean.class.getCanonicalName()"));
        fields.add(new FieldInfo(PRIVATE_STATIC_FINAL, "String", "BOOLEAN_CANONICAL_NAME_2", "boolean.class.getCanonicalName()"));

        fields.add(new FieldInfo(PRIVATE_STATIC_VOLATILE, "int", "configVersion", "-1"));
        fields.add(new FieldInfo(PRIVATE_STATIC, "AtomicInteger", "requestId", "new AtomicInteger(0)"));
        fields.add(new FieldInfo(PRIVATE_STATIC, "SerializeType", "serializeTypeConfigInThisServer", "SerializeType.JSON"));

        fields.add(new FieldInfo(PRIVATE, "int", "code"));
        fields.add(new FieldInfo(PRIVATE, "LanguageCode", "language", "LanguageCode.JAVA"));
        fields.add(new FieldInfo(PRIVATE, "int", "version", "0"));
        fields.add(new FieldInfo(PRIVATE, "int", "opaque", "requestId.getAndIncrement()"));
        fields.add(new FieldInfo(PRIVATE, "int", "flag", "0"));
        fields.add(new FieldInfo(PRIVATE, "String", "remark"));
        fields.add(new FieldInfo(PRIVATE, "HashMap<String, String>", "extFields"));
        fields.add(new FieldInfo(PRIVATE_TRANSIENT, "CommandCustomHeader", "customHeader"));
        fields.add(new FieldInfo(PRIVATE, "SerializeType", "serializeTypeCurrentRPC", "serializeTypeConfigInThisServer"));
        fields.add(new FieldInfo(PRIVATE_TRANSIENT, "byte[]", "body"));
        setFields(fields);
    }

    @Override
    public String buildImports() {
        StringBuilder builder = new StringBuilder(1000);
        CodeGeneratorHelper.buildDependencyImports(builder, getDependencies());
        builder.append("import com.alibaba.fastjson.annotation.JSONField;\n" +
                "import org.apache.logging.log4j.LogManager;\n" +
                "import org.apache.logging.log4j.Logger;\n" +
                "\n" +
                "import java.lang.annotation.Annotation;\n" +
                "import java.lang.reflect.Field;\n" +
                "import java.lang.reflect.Modifier;\n" +
                "import java.nio.ByteBuffer;\n" +
                "import java.util.HashMap;\n" +
                "import java.util.Map;\n" +
                "import java.util.concurrent.atomic.AtomicInteger;\n\n");
        return builder.toString();
    }


    @Override
    public String buildMethods() {
        StringBuilder builder = new StringBuilder(15000);
        builder.append(staticInit())
                .append(staticFactoryMethods())
                .append(decode())
                .append(headerDecode())
                .append(helper())
                .append(decodeCommandCustomHeader())
                .append(decodeCommandCustomHeaderHelper())
                .append(encode())
                .append(headerEncode())
                .append(makeCustomHeaderToNet())
                .append(encodeHeader())
                .append(specialGettersAndSetters());
        CodeGeneratorHelper.buildGetterAndSetterMethods(builder, getFields().subList(21, 31));
        builder.append("    @Override\n" +
                "    public String toString() {\n" +
                "        return \"RemotingCommand [code=\" + code + \", language=\" + language + \", version=\" + version + \", opaque=\" + opaque + \", flag(B)=\"\n" +
                "                + Integer.toBinaryString(flag) + \", remark=\" + remark + \", extFields=\" + extFields + \", serializeTypeCurrentRPC=\"\n" +
                "                + serializeTypeCurrentRPC + \"]\";\n" +
                "    }\n\n");
        return builder.toString();
    }

    private String staticInit() {
        return "    static {\n" +
                "        final String protocol = System.getProperty(SERIALIZE_TYPE_PROPERTY, System.getenv(SERIALIZE_TYPE_ENV));\n" +
                "        if (!isBlank(protocol)) {\n" +
                "            try {\n" +
                "                serializeTypeConfigInThisServer = SerializeType.valueOf(protocol);\n" +
                "            } catch (IllegalArgumentException e) {\n" +
                "                throw new RuntimeException(\"parser specified protocol error. protocol=\" + protocol, e);\n" +
                "            }\n" +
                "        }\n" +
                "    }\n\n";
    }

    private String staticFactoryMethods() {
        return "    protected RemotingCommand() {\n" +
                "    }\n" +
                "\n" +
                "    public static RemotingCommand createRequestCommand(int code, CommandCustomHeader customHeader) {\n" +
                "        RemotingCommand cmd = new RemotingCommand();\n" +
                "        cmd.setCode(code);\n" +
                "        cmd.customHeader = customHeader;\n" +
                "        setCmdVersion(cmd);\n" +
                "        return cmd;\n" +
                "    }\n" +
                "\n" +
                "    private static void setCmdVersion(RemotingCommand cmd) {\n" +
                "        if (configVersion >= 0) {\n" +
                "            cmd.setVersion(configVersion);\n" +
                "        } else {\n" +
                "            String v = System.getProperty(REMOTING_VERSION_KEY);\n" +
                "            if (v != null) {\n" +
                "                int value = Integer.parseInt(v);\n" +
                "                cmd.setVersion(value);\n" +
                "                configVersion = value;\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    public static RemotingCommand createResponseCommand(Class<? extends CommandCustomHeader> classHeader) {\n" +
                "        return createResponseCommand(RemotingSysResponseCode.SYSTEM_ERROR, \"not set any response code\", classHeader);\n" +
                "    }\n" +
                "\n" +
                "    public static RemotingCommand createResponseCommand(int code, String remark,\n" +
                "                                                        Class<? extends CommandCustomHeader> classHeader) {\n" +
                "        RemotingCommand cmd = new RemotingCommand();\n" +
                "        cmd.markResponseType();\n" +
                "        cmd.setCode(code);\n" +
                "        cmd.setRemark(remark);\n" +
                "        setCmdVersion(cmd);\n" +
                "\n" +
                "        if (classHeader != null) {\n" +
                "            try {\n" +
                "                cmd.customHeader = classHeader.newInstance();\n" +
                "            } catch (InstantiationException | IllegalAccessException e) {\n" +
                "                return null;\n" +
                "            }\n" +
                "        }\n" +
                "\n" +
                "        return cmd;\n" +
                "    }\n" +
                "\n" +
                "    public static RemotingCommand createResponseCommand(int code, String remark) {\n" +
                "        return createResponseCommand(code, remark, null);\n" +
                "    }\n\n";
    }

    private String decode() {
        return "    public static RemotingCommand decode(final byte[] array) {\n" +
                "        ByteBuffer byteBuffer = ByteBuffer.wrap(array);\n" +
                "        return decode(byteBuffer);\n" +
                "    }\n" +
                "\n" +
                "    public static RemotingCommand decode(final ByteBuffer byteBuffer) {\n" +
                "        int length = byteBuffer.limit();\n" +
                "        int oriHeaderLen = byteBuffer.getInt();\n" +
                "        int headerLength = getHeaderLength(oriHeaderLen);\n" +
                "\n" +
                "        byte[] headerData = new byte[headerLength];\n" +
                "        byteBuffer.get(headerData);\n" +
                "\n" +
                "        RemotingCommand cmd = headerDecode(headerData, getProtocolType(oriHeaderLen));\n" +
                "\n" +
                "        int bodyLength = length - 4 - headerLength;\n" +
                "        byte[] bodyData = null;\n" +
                "        if (bodyLength > 0) {\n" +
                "            bodyData = new byte[bodyLength];\n" +
                "            byteBuffer.get(bodyData);\n" +
                "        }\n" +
                "        cmd.body = bodyData;\n" +
                "\n" +
                "        return cmd;\n" +
                "    }\n\n";
    }

    private String headerDecode() {
        return "    private static RemotingCommand headerDecode(byte[] headerData, SerializeType type) {\n" +
                "        switch (type) {\n" +
                "            case JSON:\n" +
                "                RemotingCommand resultJson = RemotingSerializable.decode(headerData, RemotingCommand.class);\n" +
                "                resultJson.setSerializeTypeCurrentRPC(type);\n" +
                "                return resultJson;\n" +
                "            case NAREP:\n" +
                "                RemotingCommand resultRMQ = NarepSerializable.narepProtocolDecode(headerData);\n" +
                "                resultRMQ.setSerializeTypeCurrentRPC(type);\n" +
                "                return resultRMQ;\n" +
                "            default:\n" +
                "                break;\n" +
                "        }\n" +
                "\n" +
                "        return null;\n" +
                "    }\n\n";
    }

    private String helper() {
        return "    public static int getHeaderLength(int length) {\n" +
                "        return length & 0xFFFFFF;\n" +
                "    }\n" +
                "\n" +
                "    public static SerializeType getProtocolType(int source) {\n" +
                "        return SerializeType.valueOf((byte) ((source >> 24) & 0xFF));\n" +
                "    }\n" +
                "\n" +
                "    public static int createNewRequestId() {\n" +
                "        return requestId.incrementAndGet();\n" +
                "    }\n" +
                "\n" +
                "    private static boolean isBlank(String str) {\n" +
                "        int strLen;\n" +
                "        if (str == null || (strLen = str.length()) == 0) {\n" +
                "            return true;\n" +
                "        }\n" +
                "        for (int i = 0; i < strLen; i++) {\n" +
                "            if (!Character.isWhitespace(str.charAt(i))) {\n" +
                "                return false;\n" +
                "            }\n" +
                "        }\n" +
                "        return true;\n" +
                "    }\n" +
                "\n" +
                "    public static byte[] markProtocolType(int source, SerializeType type) {\n" +
                "        byte[] result = new byte[4];\n" +
                "\n" +
                "        result[0] = type.getCode();\n" +
                "        result[1] = (byte) ((source >> 16) & 0xFF);\n" +
                "        result[2] = (byte) ((source >> 8) & 0xFF);\n" +
                "        result[3] = (byte) (source & 0xFF);\n" +
                "        return result;\n" +
                "    }\n" +
                "\n" +
                "    public void markResponseType() {\n" +
                "        int bits = 1 << RPC_TYPE;\n" +
                "        this.flag |= bits;\n" +
                "    }\n\n";
    }

    private String decodeCommandCustomHeader() {
        return "    public CommandCustomHeader decodeCommandCustomHeader(\n" +
                "            Class<? extends CommandCustomHeader> classHeader) throws RemotingCommandException {\n" +
                "        CommandCustomHeader objectHeader;\n" +
                "        try {\n" +
                "            objectHeader = classHeader.newInstance();\n" +
                "        } catch (InstantiationException | IllegalAccessException e) {\n" +
                "            return null;\n" +
                "        }\n" +
                "\n" +
                "        if (this.extFields != null) {\n" +
                "\n" +
                "            Field[] fields = getClazzFields(classHeader);\n" +
                "            for (Field field : fields) {\n" +
                "                if (!Modifier.isStatic(field.getModifiers())) {\n" +
                "                    String fieldName = field.getName();\n" +
                "                    if (!fieldName.startsWith(\"this\")) {\n" +
                "                        try {\n" +
                "                            String value = this.extFields.get(fieldName);\n" +
                "                            if (null == value) {\n" +
                "                                if (!isFieldNullable(field)) {\n" +
                "                                    throw new RemotingCommandException(\"the custom field <\" + fieldName + \"> is null\");\n" +
                "                                }\n" +
                "                                continue;\n" +
                "                            }\n" +
                "\n" +
                "                            field.setAccessible(true);\n" +
                "                            String type = getCanonicalName(field.getType());\n" +
                "                            Object valueParsed;\n" +
                "\n" +
                "                            if (type.equals(STRING_CANONICAL_NAME)) {\n" +
                "                                valueParsed = value;\n" +
                "                            } else if (type.equals(INTEGER_CANONICAL_NAME_1) || type.equals(INTEGER_CANONICAL_NAME_2)) {\n" +
                "                                valueParsed = Integer.parseInt(value);\n" +
                "                            } else if (type.equals(LONG_CANONICAL_NAME_1) || type.equals(LONG_CANONICAL_NAME_2)) {\n" +
                "                                valueParsed = Long.parseLong(value);\n" +
                "                            } else if (type.equals(BOOLEAN_CANONICAL_NAME_1) || type.equals(BOOLEAN_CANONICAL_NAME_2)) {\n" +
                "                                valueParsed = Boolean.parseBoolean(value);\n" +
                "                            } else if (type.equals(DOUBLE_CANONICAL_NAME_1) || type.equals(DOUBLE_CANONICAL_NAME_2)) {\n" +
                "                                valueParsed = Double.parseDouble(value);\n" +
                "                            } else {\n" +
                "                                throw new RemotingCommandException(\"the custom field <\" + fieldName + \"> type is not supported\");\n" +
                "                            }\n" +
                "\n" +
                "                            field.set(objectHeader, valueParsed);\n" +
                "\n" +
                "                        } catch (Throwable e) {\n" +
                "                            log.error(\"Failed field [{}] decoding\", fieldName, e);\n" +
                "                        }\n" +
                "                    }\n" +
                "                }\n" +
                "            }\n" +
                "\n" +
                "            objectHeader.checkFields();\n" +
                "        }\n" +
                "\n" +
                "        return objectHeader;\n" +
                "    }\n" +
                "\n";
    }

    private String decodeCommandCustomHeaderHelper() {
        return "    private Field[] getClazzFields(Class<? extends CommandCustomHeader> classHeader) {\n" +
                "        Field[] field = CLASS_HASH_MAP.get(classHeader);\n" +
                "\n" +
                "        if (field == null) {\n" +
                "            field = classHeader.getDeclaredFields();\n" +
                "            synchronized (CLASS_HASH_MAP) {\n" +
                "                CLASS_HASH_MAP.put(classHeader, field);\n" +
                "            }\n" +
                "        }\n" +
                "        return field;\n" +
                "    }\n" +
                "\n" +
                "    private boolean isFieldNullable(Field field) {\n" +
                "        if (!NULLABLE_FIELD_CACHE.containsKey(field)) {\n" +
                "            Annotation annotation = field.getAnnotation(CFNotNull.class);\n" +
                "            synchronized (NULLABLE_FIELD_CACHE) {\n" +
                "                NULLABLE_FIELD_CACHE.put(field, annotation == null);\n" +
                "            }\n" +
                "        }\n" +
                "        return NULLABLE_FIELD_CACHE.get(field);\n" +
                "    }\n" +
                "\n" +
                "    private String getCanonicalName(Class<?> clazz) {\n" +
                "        String name = CANONICAL_NAME_CACHE.get(clazz);\n" +
                "\n" +
                "        if (name == null) {\n" +
                "            name = clazz.getCanonicalName();\n" +
                "            synchronized (CANONICAL_NAME_CACHE) {\n" +
                "                CANONICAL_NAME_CACHE.put(clazz, name);\n" +
                "            }\n" +
                "        }\n" +
                "        return name;\n" +
                "    }\n\n";
    }

    private String encode() {
        return "    public ByteBuffer encode() {\n" +
                "        // 1> header length size\n" +
                "        int length = 4;\n" +
                "\n" +
                "        // 2> header data length\n" +
                "        byte[] headerData = this.headerEncode();\n" +
                "        length += headerData.length;\n" +
                "\n" +
                "        // 3> body data length\n" +
                "        if (this.body != null) {\n" +
                "            length += body.length;\n" +
                "        }\n" +
                "\n" +
                "        ByteBuffer result = ByteBuffer.allocate(4 + length);\n" +
                "\n" +
                "        // length\n" +
                "        result.putInt(length);\n" +
                "\n" +
                "        // header length\n" +
                "        result.put(markProtocolType(headerData.length, serializeTypeCurrentRPC));\n" +
                "\n" +
                "        // header data\n" +
                "        result.put(headerData);\n" +
                "\n" +
                "        // body data;\n" +
                "        if (this.body != null) {\n" +
                "            result.put(this.body);\n" +
                "        }\n" +
                "\n" +
                "        result.flip();\n" +
                "\n" +
                "        return result;\n" +
                "    }\n\n";
    }

    private String headerEncode() {
        return "    private byte[] headerEncode() {\n" +
                "        this.makeCustomHeaderToNet();\n" +
                "        if (SerializeType.NAREP == serializeTypeCurrentRPC) {\n" +
                "            return NarepSerializable.narepProtocolEncode(this);\n" +
                "        } else {\n" +
                "            return RemotingSerializable.encode(this);\n" +
                "        }\n" +
                "    }\n\n";
    }

    private String makeCustomHeaderToNet() {
        return "    public void makeCustomHeaderToNet() {\n" +
                "        if (this.customHeader != null) {\n" +
                "            Field[] fields = getClazzFields(customHeader.getClass());\n" +
                "            if (null == this.extFields) {\n" +
                "                this.extFields = new HashMap<>();\n" +
                "            }\n" +
                "\n" +
                "            for (Field field : fields) {\n" +
                "                if (!Modifier.isStatic(field.getModifiers())) {\n" +
                "                    String name = field.getName();\n" +
                "                    if (!name.startsWith(\"this\")) {\n" +
                "                        Object value = null;\n" +
                "                        try {\n" +
                "                            field.setAccessible(true);\n" +
                "                            value = field.get(this.customHeader);\n" +
                "                        } catch (Exception e) {\n" +
                "                            log.error(\"Failed to access field [{}]\", name, e);\n" +
                "                        }\n" +
                "\n" +
                "                        if (value != null) {\n" +
                "                            this.extFields.put(name, value.toString());\n" +
                "                        }\n" +
                "                    }\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    }\n\n";
    }

    private String encodeHeader() {
        return "    public ByteBuffer encodeHeader() {\n" +
                "        return encodeHeader(this.body != null ? this.body.length : 0);\n" +
                "    }\n" +
                "\n" +
                "    public ByteBuffer encodeHeader(final int bodyLength) {\n" +
                "        // 1> header length size\n" +
                "        int length = 4;\n" +
                "\n" +
                "        // 2> header data length\n" +
                "        byte[] headerData;\n" +
                "        headerData = this.headerEncode();\n" +
                "\n" +
                "        length += headerData.length;\n" +
                "\n" +
                "        // 3> body data length\n" +
                "        length += bodyLength;\n" +
                "\n" +
                "        ByteBuffer result = ByteBuffer.allocate(4 + length - bodyLength);\n" +
                "\n" +
                "        // length\n" +
                "        result.putInt(length);\n" +
                "\n" +
                "        // header length\n" +
                "        result.put(markProtocolType(headerData.length, serializeTypeCurrentRPC));\n" +
                "\n" +
                "        // header data\n" +
                "        result.put(headerData);\n" +
                "\n" +
                "        result.flip();\n" +
                "\n" +
                "        return result;\n" +
                "    }\n\n";
    }

    private String specialGettersAndSetters() {
        return "    public void markOnewayRPC() {\n" +
                "        int bits = 1 << RPC_ONEWAY;\n" +
                "        this.flag |= bits;\n" +
                "    }\n" +
                "\n" +
                "    @JSONField(serialize = false)\n" +
                "    public boolean isOnewayRPC() {\n" +
                "        int bits = 1 << RPC_ONEWAY;\n" +
                "        return (this.flag & bits) == bits;\n" +
                "    }\n" +
                "    \n" +
                "    @JSONField(serialize = false)\n" +
                "    public RemotingCommandType getType() {\n" +
                "        if (this.isResponseType()) {\n" +
                "            return RemotingCommandType.RESPONSE_COMMAND;\n" +
                "        }\n" +
                "\n" +
                "        return RemotingCommandType.REQUEST_COMMAND;\n" +
                "    }\n" +
                "\n" +
                "    @JSONField(serialize = false)\n" +
                "    public boolean isResponseType() {\n" +
                "        int bits = 1 << RPC_TYPE;\n" +
                "        return (this.flag & bits) == bits;\n" +
                "    }\n" +
                "\n" +
                "    public static SerializeType getSerializeTypeConfigInThisServer() {\n" +
                "        return serializeTypeConfigInThisServer;\n" +
                "    }\n" +
                "\n" +
                "    public void addExtField(String key, String value) {\n" +
                "        if (null == extFields) {\n" +
                "            extFields = new HashMap<>();\n" +
                "        }\n" +
                "        extFields.put(key, value);\n" +
                "    }\n\n";
    }

}
