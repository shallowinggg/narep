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
public class NettyServerConfigCodeGenerator extends ClassCodeGenerator {
    private static final String CLASS_NAME = "NettyServerConfig";
    private static final String[] INTERFACES = new String[]{"Cloneable"};
    private static final String SUB_PACKAGE = "netty";
    private List<FieldMetaData> fields = new ArrayList<>(11);

    public NettyServerConfigCodeGenerator() {
        super(CLASS_NAME, null, SUB_PACKAGE, INTERFACES);
        fields.add(new FieldMetaData(PRIVATE, "int", "listenPort", "8888"));
        fields.add(new FieldMetaData(PRIVATE, "int", "serverWorkerThreads", "8"));
        fields.add(new FieldMetaData(PRIVATE, "int", "serverCallbackExecutorThreads", "0"));
        fields.add(new FieldMetaData(PRIVATE, "int", "serverSelectorThreads", "3"));
        fields.add(new FieldMetaData(PRIVATE, "int", "serverOnewaySemaphoreValue", "256"));
        fields.add(new FieldMetaData(PRIVATE, "int", "serverAsyncSemaphoreValue", "64"));
        fields.add(new FieldMetaData(PRIVATE, "int", "serverChannelMaxIdleTimeSeconds", "120"));
        fields.add(new FieldMetaData(PRIVATE, "int", "serverSocketSndBufSize", "NettySystemConfig.socketSndbufSize"));
        fields.add(new FieldMetaData(PRIVATE, "int", "serverSocketRcvBufSize", "NettySystemConfig.socketRcvbufSize"));
        fields.add(new FieldMetaData(PRIVATE, "boolean", "serverPooledByteBufAllocatorEnable", "true"));
        fields.add(GenericBuilder.of(FieldMetaData::new)
                .with(FieldMetaData::setModifier, PRIVATE)
                .with(FieldMetaData::setClazz, "boolean")
                .with(FieldMetaData::setName, "useEpollNativeSelector")
                .with(FieldMetaData::setDefaultValue, "false")
                .with(FieldMetaData::setComment, "    /**\n" +
                        "     * make make install\n" +
                        "     *\n" +
                        "     *\n" +
                        "     * ../glibc-2.10.1/configure \\ --prefix=/usr \\ --with-headers=/usr/include \\\n" +
                        "     * --host=x86_64-linux-gnu \\ --build=x86_64-pc-linux-gnu \\ --without-gd\n" +
                        "     */").build());
    }

    @Override
    public String buildFields() {
        return CodeGeneratorHelper.buildFieldsByMetaData(fields);
    }

    @Override
    public String buildMethods() {
        return CodeGeneratorHelper.buildGetterAndSetterMethods(fields) +
                "    @Override\n" +
                "    public Object clone() throws CloneNotSupportedException {\n" +
                "        return (NettyServerConfig) super.clone();\n" +
                "    }\n";
    }
}
