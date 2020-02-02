package com.shallowinggg.narep.core.generators.protocol;

import com.shallowinggg.narep.core.annotation.Generator;
import com.shallowinggg.narep.core.annotation.Profile;
import com.shallowinggg.narep.core.common.CodeGeneratorHelper;
import com.shallowinggg.narep.core.common.ConfigInfos;
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
@Profile("custom")
public class CustomRemotingCommandCodeGenerator extends ClassCodeGenerator {
    private static final String CLASS_NAME = "RemotingCommand";
    private static final String SUB_PACKAGE = "protocol";
    private static final List<String> DEPENDENCY_NAMES = Arrays.asList("RemotingHelper",
            "RemotingCommandException", "RemotingCommandType",
            "RemotingSerializable", "RemotingSysResponseCode", "SerializeType");

    public CustomRemotingCommandCodeGenerator() {
        super(CLASS_NAME, null, SUB_PACKAGE);
        setDependencyNames(DEPENDENCY_NAMES);

        List<FieldInfo> commandFields = ConfigInfos.getInstance().commandFields();
        List<FieldInfo> fields = new ArrayList<>(10 + commandFields.size());
        fields.add(new FieldInfo(PRIVATE_STATIC_FINAL, "String", "SERIALIZE_TYPE_PROPERTY", "\"remoting.serialize.type\""));
        fields.add(new FieldInfo(PRIVATE_STATIC_FINAL, "String", "SERIALIZE_TYPE_ENV", "\"REMOTING_SERIALIZE_TYPE\""));
        fields.add(new FieldInfo(PRIVATE_STATIC_FINAL, "String", "REMOTING_VERSION_KEY", "\"remoting.remoting.version\""));
        fields.add(new FieldInfo(PRIVATE_STATIC_FINAL, "Logger", "log", CodeGeneratorHelper.buildLoggerField(CLASS_NAME)));
        fields.add(new FieldInfo(PRIVATE_STATIC_FINAL, "int", "RPC_TYPE", "0"));
        fields.add(new FieldInfo(PRIVATE_STATIC_FINAL, "int", "RPC_ONEWAY", "1"));
        fields.add(new FieldInfo(PRIVATE_STATIC, "AtomicInteger", "requestId", "new AtomicInteger(0)"));
        fields.add(new FieldInfo(PRIVATE_STATIC, "SerializeType", "serializeTypeConfigInThisServer", "SerializeType.JSON"));
        fields.addAll(commandFields);
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
                .append(encode())
                .append(headerEncode())
                .append(encodeHeader())
                .append(specialGettersAndSetters());
        int end = getFields().size();
        int start = end - (ConfigInfos.getInstance().commandFields().size() + 2);
        CodeGeneratorHelper.buildGetterAndSetterMethods(builder, getFields().subList(start, end));
        builder.append(buildToStringMethod());
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
                "    public static RemotingCommand createResponseCommand(int code, String remark) {\n" +
                "        RemotingCommand cmd = new RemotingCommand();\n" +
                "        cmd.markResponseType();\n" +
                "        cmd.setCode(code);\n" +
                "        cmd.setRemark(remark);\n" +
                "        return cmd;\n" +
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
                "\n";
    }

    private String buildToStringMethod() {
        StringBuilder builder = new StringBuilder(400);
        builder.append("    @Override\n" +
                "    public String toString() {\n" +
                "        return \"RemotingCommand [code=\" + code\n" +
                "                + \", flag(B)=\" + Integer.toBinaryString(flag)\n" +
                "                + \", opaque=\" + opaque\n");

        // code
        // flag
        // opaque
        // custom fields             ---- start
        // ...
        // serializeTypeCurrentRPC
        //                           ---- end
        int end = getFields().size() - 1;
        int start = end - 1 - (ConfigInfos.getInstance().commandFields().size() - 3);
        List<FieldInfo> others = getFields().subList(start, end);
        for (FieldInfo field : others) {
            String name = field.getName();
            builder.append("                + \", ").append(name).append("=\" + ").append(name).append("\n");
        }
        builder.setLength(builder.length() - 1);
        builder.append(" + \"]\";\n")
                .append("    }\n\n");

        return builder.toString();
    }


}
