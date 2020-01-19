package com.shallowinggg.narep.core.generators.netty;

import com.shallowinggg.narep.core.common.CodeGeneratorHelper;
import com.shallowinggg.narep.core.generators.ClassCodeGenerator;
import com.shallowinggg.narep.core.lang.FieldInfo;

import java.util.ArrayList;
import java.util.List;

import static com.shallowinggg.narep.core.lang.Modifier.PRIVATE;


/**
 * @author shallowinggg
 */
public class NettyServerConfigCodeGenerator extends ClassCodeGenerator {
    private static final String CLASS_NAME = "NettyServerConfig";
    private static final String[] INTERFACES = new String[]{"Cloneable"};
    private static final String SUB_PACKAGE = "netty";

    public NettyServerConfigCodeGenerator() {
        super(CLASS_NAME, null, SUB_PACKAGE, INTERFACES);

        List<FieldInfo> fields = new ArrayList<>(11);
        fields.add(new FieldInfo(PRIVATE, "int", "listenPort", "8888"));
        fields.add(new FieldInfo(PRIVATE, "int", "serverWorkerThreads", "8"));
        fields.add(new FieldInfo(PRIVATE, "int", "serverCallbackExecutorThreads", "0"));
        fields.add(new FieldInfo(PRIVATE, "int", "serverSelectorThreads", "3"));
        fields.add(new FieldInfo(PRIVATE, "int", "serverOnewaySemaphoreValue", "256"));
        fields.add(new FieldInfo(PRIVATE, "int", "serverAsyncSemaphoreValue", "64"));
        fields.add(new FieldInfo(PRIVATE, "int", "serverChannelMaxIdleTimeSeconds", "120"));
        fields.add(new FieldInfo(PRIVATE, "int", "serverSocketSndBufSize", "NettySystemConfig.socketSndbufSize"));
        fields.add(new FieldInfo(PRIVATE, "int", "serverSocketRcvBufSize", "NettySystemConfig.socketRcvbufSize"));
        fields.add(new FieldInfo(PRIVATE, "boolean", "serverPooledByteBufAllocatorEnable", "true"));
        fields.add(new FieldInfo.Builder().modifier(PRIVATE)
                .type("boolean").name("useEpollNativeSelector").initValue("false")
                .comment("    /**\n" +
                        "     * make make install\n" +
                        "     *\n" +
                        "     *\n" +
                        "     * ../glibc-2.10.1/configure \\ --prefix=/usr \\ --with-headers=/usr/include \\\n" +
                        "     * --host=x86_64-linux-gnu \\ --build=x86_64-pc-linux-gnu \\ --without-gd\n" +
                        "     */").build());
    }

    @Override
    public String buildMethods() {
        return CodeGeneratorHelper.buildGetterAndSetterMethods(getFields()) +
                "    @Override\n" +
                "    public Object clone() throws CloneNotSupportedException {\n" +
                "        return (NettyServerConfig) super.clone();\n" +
                "    }\n";
    }
}
