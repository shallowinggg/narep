package com.shallowinggg.narep.core.generators.netty;

import com.shallowinggg.narep.core.annotation.Generator;
import com.shallowinggg.narep.core.generators.ClassCodeGenerator;

/**
 * @author shallowinggg
 */
@Generator
public class FileRegionEncoderCodeGenerator extends ClassCodeGenerator {
    private static final String CLASS_NAME = "FileRegionEncoder";
    private static final String PARENT_NAME = "MessageToByteEncoder<FileRegion>";
    private static final String SUB_PACKAGE = "netty";

    public FileRegionEncoderCodeGenerator() {
        super(CLASS_NAME, PARENT_NAME, SUB_PACKAGE);
    }

    @Override
    public String buildImports() {
        return "import io.netty.buffer.ByteBuf;\n" +
                "import io.netty.channel.ChannelHandlerContext;\n" +
                "import io.netty.channel.FileRegion;\n" +
                "import io.netty.handler.codec.MessageToByteEncoder;\n" +
                "import io.netty.handler.ssl.SslHandler;\n" +
                "\n" +
                "import java.io.IOException;\n" +
                "import java.nio.ByteBuffer;\n" +
                "import java.nio.channels.WritableByteChannel;\n\n";
    }

    @Override
    public String buildClassComment() {
        return "/**\n" +
                " * <p>\n" +
                " * By default, file region are directly transferred to socket channel which is known as zero copy. In case we need\n" +
                " * to encrypt transmission, data being sent should go through the {@link SslHandler}. This encoder ensures this\n" +
                " * process.\n" +
                " * </p>\n" +
                " */\n";
    }

    @Override
    public String buildMethods() {
        return "    /**\n" +
                "     * Encode a message into a {@link io.netty.buffer.ByteBuf}. This method will be called for each written message that\n" +
                "     * can be handled by this encoder.\n" +
                "     *\n" +
                "     * @param ctx the {@link io.netty.channel.ChannelHandlerContext} which this {@link\n" +
                "     *            io.netty.handler.codec.MessageToByteEncoder} belongs to\n" +
                "     * @param msg the message to encode\n" +
                "     * @param out the {@link io.netty.buffer.ByteBuf} into which the encoded message will be written\n" +
                "     * @throws Exception is thrown if an error occurs\n" +
                "     */\n" +
                "    @Override\n" +
                "    protected void encode(ChannelHandlerContext ctx, FileRegion msg, final ByteBuf out) throws Exception {\n" +
                "        WritableByteChannel writableByteChannel = new WritableByteChannel() {\n" +
                "            @Override\n" +
                "            public int write(ByteBuffer src) throws IOException {\n" +
                "                out.writeBytes(src);\n" +
                "                return out.capacity();\n" +
                "            }\n" +
                "\n" +
                "            @Override\n" +
                "            public boolean isOpen() {\n" +
                "                return true;\n" +
                "            }\n" +
                "\n" +
                "            @Override\n" +
                "            public void close() throws IOException {\n" +
                "            }\n" +
                "        };\n" +
                "\n" +
                "        long toTransfer = msg.count();\n" +
                "\n" +
                "        while (true) {\n" +
                "            long transferred = msg.transfered();\n" +
                "            if (toTransfer - transferred <= 0) {\n" +
                "                break;\n" +
                "            }\n" +
                "            msg.transferTo(writableByteChannel, transferred);\n" +
                "        }\n" +
                "    }\n\n";
    }
}
