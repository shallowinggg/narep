package com.shallowinggg.narep.core.generators.defaults;

import com.shallowinggg.narep.core.common.CodeGeneratorHelper;
import com.shallowinggg.narep.core.generators.InterfaceCodeGenerator;

import java.util.Arrays;
import java.util.List;

/**
 * @author shallowinggg
 */
public class RemotingClientCodeGenerator extends InterfaceCodeGenerator {
    private static final String INTERFACE_NAME = "RemotingClient";
    private static final String PARENT_INTERFACE = "RemotingService";
    private static final List<String> DEPENDENCIES = Arrays.asList("RemotingConnectException.java",
            "RemotingSendRequestException.java", "RemotingTimeoutException.java",
            "RemotingTooMuchRequestException.java", "NettyRequestProcessor.java",
            "RemotingCommand.java");

    public RemotingClientCodeGenerator() {
        super(INTERFACE_NAME, PARENT_INTERFACE);
        setDependenciesName(DEPENDENCIES);
    }

    @Override
    public String buildImports() {
        StringBuilder builder = new StringBuilder(500);
        CodeGeneratorHelper.buildDependencyImports(builder, getDependencies());
        builder.append("import java.util.List;\n" +
                "import java.util.concurrent.ExecutorService;\n\n");
        return builder.toString();
    }

    @Override
    public String buildMethods() {
        return "    void updateNameServerAddressList(final List<String> addrs);\n" +
                "\n" +
                "    List<String> getNameServerAddressList();\n" +
                "\n" +
                "    RemotingCommand invokeSync(final String addr, final RemotingCommand request,\n" +
                "                               final long timeoutMillis) throws InterruptedException, RemotingConnectException,\n" +
                "            RemotingSendRequestException, RemotingTimeoutException;\n" +
                "\n" +
                "    void invokeAsync(final String addr, final RemotingCommand request, final long timeoutMillis,\n" +
                "                     final InvokeCallback invokeCallback) throws InterruptedException, RemotingConnectException,\n" +
                "            RemotingTooMuchRequestException, RemotingTimeoutException, RemotingSendRequestException;\n" +
                "\n" +
                "    void invokeOneway(final String addr, final RemotingCommand request, final long timeoutMillis)\n" +
                "            throws InterruptedException, RemotingConnectException, RemotingTooMuchRequestException,\n" +
                "            RemotingTimeoutException, RemotingSendRequestException;\n" +
                "\n" +
                "    void registerProcessor(final int requestCode, final NettyRequestProcessor processor,\n" +
                "                           final ExecutorService executor);\n" +
                "\n" +
                "    void setCallbackExecutor(final ExecutorService callbackExecutor);\n" +
                "\n" +
                "    ExecutorService getCallbackExecutor();\n" +
                "\n" +
                "    boolean isChannelWritable(final String addr);\n\n";
    }
}
