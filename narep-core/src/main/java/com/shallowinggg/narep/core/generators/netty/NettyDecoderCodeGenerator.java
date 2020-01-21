package com.shallowinggg.narep.core.generators.netty;

import com.shallowinggg.narep.core.common.CodeGeneratorHelper;
import com.shallowinggg.narep.core.common.ConfigInfos;
import com.shallowinggg.narep.core.generators.ClassCodeGenerator;
import com.shallowinggg.narep.core.lang.FieldInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.shallowinggg.narep.core.lang.Modifier.PRIVATE_STATIC_FINAL;

/**
 * @author shallowinggg
 */
public class NettyDecoderCodeGenerator extends ClassCodeGenerator {
    private static final String CLASS_NAME = "NettyDecoder";
    private static final String PARENT_NAME = "LengthFieldBasedFrameDecoder";
    private static final String SUB_PACKAGE = "netty";
    private static final List<String> DEPENDENCIES = Arrays.asList("RemotingHelper.java",
            "RemotingUtil.java", "RemotingCommand.java");

    public NettyDecoderCodeGenerator() {
        super(CLASS_NAME, PARENT_NAME, SUB_PACKAGE);
        setDependenciesName(DEPENDENCIES);

        String key = ConfigInfos.getInstance().basePackage() + ".frameMaxLength";
        List<FieldInfo> fields = new ArrayList<>(2);
        fields.add(new FieldInfo(PRIVATE_STATIC_FINAL, "Logger", "log",
                CodeGeneratorHelper.buildLoggerField(CLASS_NAME)));
        fields.add(new FieldInfo(PRIVATE_STATIC_FINAL, "int", "FRAME_MAX_LENGTH",
                "Integer.parseInt(System.getProperty(\"" + key + "\", \"16777216\"))"));
        setFields(fields);
    }

    @Override
    public String buildImports() {
        StringBuilder builder = new StringBuilder(500);
        CodeGeneratorHelper.buildDependencyImports(builder, getDependencies());
        builder.append("import io.netty.buffer.ByteBuf;\n" +
                "import io.netty.channel.ChannelHandlerContext;\n" +
                "import io.netty.handler.codec.LengthFieldBasedFrameDecoder;\n" +
                "import org.apache.logging.log4j.LogManager;\n" +
                "import org.apache.logging.log4j.Logger;\n" +
                "\n" +
                "import java.nio.ByteBuffer;\n\n");
        return builder.toString();
    }

    @Override
    public String buildMethods() {
        return "    public NettyDecoder() {\n" +
                "        super(FRAME_MAX_LENGTH, 0, 4, 0, 4);\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {\n" +
                "        ByteBuf frame = null;\n" +
                "        try {\n" +
                "            frame = (ByteBuf) super.decode(ctx, in);\n" +
                "            if (null == frame) {\n" +
                "                return null;\n" +
                "            }\n" +
                "\n" +
                "            ByteBuffer byteBuffer = frame.nioBuffer();\n" +
                "\n" +
                "            return RemotingCommand.decode(byteBuffer);\n" +
                "        } catch (Exception e) {\n" +
                "            log.error(\"decode exception, \" + RemotingHelper.parseChannelRemoteAddr(ctx.channel()), e);\n" +
                "            RemotingUtil.closeChannel(ctx.channel());\n" +
                "        } finally {\n" +
                "            if (null != frame) {\n" +
                "                frame.release();\n" +
                "            }\n" +
                "        }\n" +
                "\n" +
                "        return null;\n" +
                "    }\n";
    }
}
