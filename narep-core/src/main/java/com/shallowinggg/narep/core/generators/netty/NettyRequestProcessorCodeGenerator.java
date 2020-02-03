package com.shallowinggg.narep.core.generators.netty;

import com.shallowinggg.narep.core.annotation.Generator;
import com.shallowinggg.narep.core.common.CodeGeneratorHelper;
import com.shallowinggg.narep.core.generators.InterfaceCodeGenerator;

import java.util.Collections;
import java.util.List;

/**
 * @author shallowinggg
 */
@Generator
public class NettyRequestProcessorCodeGenerator extends InterfaceCodeGenerator {
    private static final String CLASS_NAME = "NettyRequestProcessor";
    private static final String SUB_PACKAGE = "netty";
    private static final List<String> DEPENDENCY_NAMES = Collections.singletonList("RemotingCommand");

    public NettyRequestProcessorCodeGenerator() {
        super(CLASS_NAME, null, SUB_PACKAGE);
        setDependencyNames(DEPENDENCY_NAMES);
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
    public String buildImports() {
        StringBuilder builder = new StringBuilder(120);
        CodeGeneratorHelper.buildDependencyImports(builder, getDependencies());
        builder.append("import io.netty.channel.ChannelHandlerContext;\n\n");
        return builder.toString();
    }

    @Override
    public String buildMethods() {
        return "    RemotingCommand processRequest(ChannelHandlerContext ctx, RemotingCommand request)\n" +
                "            throws Exception;\n" +
                "\n" +
                "    boolean rejectRequest();\n";
    }
}
