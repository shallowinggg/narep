package com.shallowinggg.narep.core.generators.netty;

import com.shallowinggg.narep.core.annotation.Generator;
import com.shallowinggg.narep.core.common.CodeGeneratorHelper;
import com.shallowinggg.narep.core.generators.ClassCodeGenerator;
import com.shallowinggg.narep.core.lang.FieldInfo;

import java.util.ArrayList;
import java.util.List;

import static com.shallowinggg.narep.core.lang.Modifier.PRIVATE;

/**
 * @author shallowinggg
 */
@Generator
public class NettyClientConfigCodeGenerator extends ClassCodeGenerator {
    private static final String CLASS_NAME = "NettyClientConfig";
    private static final String SUB_PACKAGE = "netty";

    public NettyClientConfigCodeGenerator() {
        super(CLASS_NAME, null, SUB_PACKAGE);

        List<FieldInfo> fields = new ArrayList<>(12);
        fields.add(new FieldInfo.Builder().modifier(PRIVATE)
                .type("int").name("clientWorkerThreads").initValue("4")
                .comment("    /**\n" +
                        "     * Worker thread number\n" +
                        "     */").build());
        fields.add(new FieldInfo(PRIVATE, "int", "clientCallbackExecutorThreads", "Runtime.getRuntime().availableProcessors()"));
        fields.add(new FieldInfo(PRIVATE, "int", "clientOnewaySemaphoreValue", "NettySystemConfig.CLIENT_ONEWAY_SEMAPHORE_VALUE"));
        fields.add(new FieldInfo(PRIVATE, "int", "clientAsyncSemaphoreValue", "NettySystemConfig.CLIENT_ASYNC_SEMAPHORE_VALUE"));
        fields.add(new FieldInfo(PRIVATE, "int", "connectTimeoutMillis", "3000"));
        fields.add(new FieldInfo(PRIVATE, "long", "channelNotActiveInterval", "1000 * 60"));
        fields.add(new FieldInfo.Builder().modifier(PRIVATE)
                .type("int").name("clientChannelMaxIdleTimeSeconds").initValue("120")
                .comment("    /**\n" +
                        "     * IdleStateEvent will be triggered when neither read nor write was performed for\n" +
                        "     * the specified period of this time. Specify {@code 0} to disable\n" +
                        "     */").build());
        fields.add(new FieldInfo(PRIVATE, "int", "clientSocketSndBufSize", "NettySystemConfig.socketSndbufSize"));
        fields.add(new FieldInfo(PRIVATE, "int", "clientSocketRcvBufSize", "NettySystemConfig.socketRcvbufSize"));
        fields.add(new FieldInfo(PRIVATE, "boolean", "clientPooledByteBufAllocatorEnable"));
        fields.add(new FieldInfo(PRIVATE, "boolean", "clientCloseSocketIfTimeout"));
        fields.add(new FieldInfo(PRIVATE, "boolean", "useTLS"));
        setFields(fields);
    }

    @Override
    public String buildMethods() {
        return CodeGeneratorHelper.buildGetterAndSetterMethods(getFields());
    }
}
