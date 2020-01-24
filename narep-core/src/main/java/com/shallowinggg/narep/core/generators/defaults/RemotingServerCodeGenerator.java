package com.shallowinggg.narep.core.generators.defaults;

import com.shallowinggg.narep.core.annotation.Generator;
import com.shallowinggg.narep.core.common.CodeGeneratorHelper;
import com.shallowinggg.narep.core.generators.InterfaceCodeGenerator;

import java.util.Arrays;
import java.util.List;

/**
 * @author shallowinggg
 */
@Generator
public class RemotingServerCodeGenerator extends InterfaceCodeGenerator {
    private static final String INTERFACE_NAME = "RemotingServer";
    private static final String PARENT_INTERFACE = "RemotingService";
    private static final List<String> DEPENDENCIES = Arrays.asList("Pair.java",
            "RemotingSendRequestException.java", "RemotingTimeoutException.java",
            "RemotingTooMuchRequestException.java", "NettyRequestProcessor.java",
            "RemotingCommand.java");

    public RemotingServerCodeGenerator() {
        super(INTERFACE_NAME, PARENT_INTERFACE);
        setDependenciesName(DEPENDENCIES);
    }

    @Override
    public String buildImports() {
        StringBuilder builder = new StringBuilder(500);
        CodeGeneratorHelper.buildDependencyImports(builder, getDependencies());
        builder.append("import io.netty.channel.Channel;\n" +
                "import java.util.concurrent.ExecutorService;\n\n");
        return builder.toString();
    }

    @Override
    public String buildMethods() {
        return "    void registerProcessor(final int requestCode, final NettyRequestProcessor processor,\n" +
                "                           final ExecutorService executor);\n" +
                "\n" +
                "    void registerDefaultProcessor(final NettyRequestProcessor processor, final ExecutorService executor);\n" +
                "\n" +
                "    int localListenPort();\n" +
                "\n" +
                "    Pair<NettyRequestProcessor, ExecutorService> getProcessorPair(final int requestCode);\n" +
                "\n" +
                "    RemotingCommand invokeSync(final Channel channel, final RemotingCommand request,\n" +
                "                               final long timeoutMillis) throws InterruptedException, RemotingSendRequestException,\n" +
                "            RemotingTimeoutException;\n" +
                "\n" +
                "    void invokeAsync(final Channel channel, final RemotingCommand request, final long timeoutMillis,\n" +
                "                     final InvokeCallback invokeCallback) throws InterruptedException,\n" +
                "            RemotingTooMuchRequestException, RemotingTimeoutException, RemotingSendRequestException;\n" +
                "\n" +
                "    void invokeOneway(final Channel channel, final RemotingCommand request, final long timeoutMillis)\n" +
                "            throws InterruptedException, RemotingTooMuchRequestException, RemotingTimeoutException,\n" +
                "            RemotingSendRequestException;\n\n";
    }
}
