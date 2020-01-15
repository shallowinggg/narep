package com.shallowinggg.narep.core.generators.netty;

import com.shallowinggg.narep.core.common.CodeGeneratorHelper;
import com.shallowinggg.narep.core.common.FieldMetaData;
import com.shallowinggg.narep.core.common.GenericBuilder;
import com.shallowinggg.narep.core.generators.ClassCodeGenerator;

import java.util.ArrayList;
import java.util.List;

import static com.shallowinggg.narep.core.common.FieldMetaData.Modifier.PRIVATE;

/**
 * @author shallowinggg
 */
public class NettyClientConfigCodeGenerator extends ClassCodeGenerator {
    private static final String CLASS_NAME = "NettyClientConfig";
    private static final String SUB_PACKAGE = "netty";

    private List<FieldMetaData> fields = new ArrayList<>(12);

    public NettyClientConfigCodeGenerator() {
        super(CLASS_NAME, SUB_PACKAGE);
        fields.add(GenericBuilder.of(FieldMetaData::new)
                .with(FieldMetaData::setModifier, PRIVATE)
                .with(FieldMetaData::setClazz, "int")
                .with(FieldMetaData::setName, "clientWorkerThreads")
                .with(FieldMetaData::setDefaultValue, "4")
                .with(FieldMetaData::setComment, "    /**\n" +
                        "     * Worker thread number\n" +
                        "     */").build());
        fields.add(new FieldMetaData(PRIVATE, "int", "clientCallbackExecutorThreads", "Runtime.getRuntime().availableProcessors()"));
        fields.add(new FieldMetaData(PRIVATE, "int", "clientOnewaySemaphoreValue", "NettySystemConfig.CLIENT_ONEWAY_SEMAPHORE_VALUE"));
        fields.add(new FieldMetaData(PRIVATE, "int", "clientAsyncSemaphoreValue", "NettySystemConfig.CLIENT_ASYNC_SEMAPHORE_VALUE"));
        fields.add(new FieldMetaData(PRIVATE, "int", "connectTimeoutMillis", "3000"));
        fields.add(new FieldMetaData(PRIVATE, "long", "channelNotActiveInterval", "1000 * 60"));
        fields.add(GenericBuilder.of(FieldMetaData::new)
                .with(FieldMetaData::setModifier, PRIVATE)
                .with(FieldMetaData::setClazz, "int")
                .with(FieldMetaData::setName, "clientChannelMaxIdleTimeSeconds")
                .with(FieldMetaData::setDefaultValue, "120")
                .with(FieldMetaData::setComment, "    /**\n" +
                        "     * IdleStateEvent will be triggered when neither read nor write was performed for\n" +
                        "     * the specified period of this time. Specify {@code 0} to disable\n" +
                        "     */").build());
        fields.add(new FieldMetaData(PRIVATE, "int", "clientSocketSndBufSize", "NettySystemConfig.socketSndbufSize"));
        fields.add(new FieldMetaData(PRIVATE, "int", "clientSocketRcvBufSize", "NettySystemConfig.socketRcvbufSize"));
        fields.add(new FieldMetaData(PRIVATE, "boolean", "clientPooledByteBufAllocatorEnable"));
        fields.add(new FieldMetaData(PRIVATE, "boolean", "clientCloseSocketIfTimeout"));
        fields.add(new FieldMetaData(PRIVATE, "boolean", "useTLS"));
    }

    @Override
    public String buildFields() {
        return CodeGeneratorHelper.buildFieldsByMetaData(fields);
    }

    @Override
    public String buildMethods() {
        return CodeGeneratorHelper.buildGetterAndSetterMethods(fields);
    }
}
