package com.shallowinggg.narep.core.generators.netty;

import com.shallowinggg.narep.core.annotation.Generator;
import com.shallowinggg.narep.core.common.CodeGeneratorHelper;
import com.shallowinggg.narep.core.generators.ClassCodeGenerator;
import com.shallowinggg.narep.core.lang.FieldInfo;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.shallowinggg.narep.core.lang.Modifier.PRIVATE_STATIC_FINAL;

/**
 * @author shallowinggg
 */
@Generator
public class NettyEncoderCodeGenerator extends ClassCodeGenerator {
    private static final String CLASS_NAME = "NettyEncoder";
    private static final String PARENT_NAME = "MessageToByteEncoder<RemotingCommand>";
    private static final String SUB_PACKAGE = "netty";
    private static final List<String> DEPENDENCIES = Arrays.asList("RemotingHelper.java",
            "RemotingUtil.java", "RemotingCommand.java");

    public NettyEncoderCodeGenerator() {
        super(CLASS_NAME, PARENT_NAME, SUB_PACKAGE);
        setDependenciesName(DEPENDENCIES);

        setFields(Collections.singletonList(new FieldInfo(PRIVATE_STATIC_FINAL, "Logger", "log",
                CodeGeneratorHelper.buildLoggerField(CLASS_NAME))));
    }

    @Override
    public String buildImports() {
        StringBuilder builder = new StringBuilder(500);
        CodeGeneratorHelper.buildDependencyImports(builder, getDependencies());
        builder.append("import io.netty.buffer.ByteBuf;\n" +
                "import io.netty.channel.ChannelHandler;\n" +
                "import io.netty.channel.ChannelHandlerContext;\n" +
                "import io.netty.handler.codec.MessageToByteEncoder;\n" +
                "import org.apache.logging.log4j.LogManager;\n" +
                "import org.apache.logging.log4j.Logger;\n" +
                "\n" +
                "import java.nio.ByteBuffer;\n\n");
        return builder.toString();
    }

    @Override
    public String buildMethods() {
        return "    @Override\n" +
                "    public void encode(ChannelHandlerContext ctx, RemotingCommand remotingCommand, ByteBuf out)\n" +
                "            throws Exception {\n" +
                "        try {\n" +
                "            ByteBuffer header = remotingCommand.encodeHeader();\n" +
                "            out.writeBytes(header);\n" +
                "            byte[] body = remotingCommand.getBody();\n" +
                "            if (body != null) {\n" +
                "                out.writeBytes(body);\n" +
                "            }\n" +
                "        } catch (Exception e) {\n" +
                "            log.error(\"encode exception, \" + RemotingHelper.parseChannelRemoteAddr(ctx.channel()), e);\n" +
                "            if (remotingCommand != null) {\n" +
                "                log.error(remotingCommand.toString());\n" +
                "            }\n" +
                "            RemotingUtil.closeChannel(ctx.channel());\n" +
                "        }\n" +
                "    }\n";
    }
}
