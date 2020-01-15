package com.shallowinggg.narep.core.generators.netty;

import com.shallowinggg.narep.core.generators.InterfaceCodeGenerator;

/**
 * @author shallowinggg
 */
public class NettyRequestProcessorCodeGenerator extends InterfaceCodeGenerator {
    private static final String CLASS_NAME = "NettyRequestProcessor";
    private static final String SUB_PACKAGE = "netty";

    public NettyRequestProcessorCodeGenerator() {
        super(CLASS_NAME, null, SUB_PACKAGE);
    }

    @Override
    public String buildClassComment() {
        return "/**\n" +
                " * Common remoting command processor\n" +
                " *\n" +
                " * @author auto generate\n" +
                " */\n";
    }

    @Override
    public String buildMethods() {
        return "    RemotingCommand processRequest(ChannelHandlerContext ctx, RemotingCommand request)\n" +
                "            throws Exception;\n" +
                "\n" +
                "    boolean rejectRequest();\n";
    }
}
