package com.shallowinggg.narep.core.generators.netty;

import com.shallowinggg.narep.core.annotation.Generator;
import com.shallowinggg.narep.core.common.CodeGeneratorHelper;
import com.shallowinggg.narep.core.common.ConfigInfos;
import com.shallowinggg.narep.core.generators.ClassCodeGenerator;
import com.shallowinggg.narep.core.lang.FieldInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.shallowinggg.narep.core.lang.Modifier.PUBLIC_STATIC;
import static com.shallowinggg.narep.core.lang.Modifier.PUBLIC_STATIC_FINAL;


/**
 * @author shallowinggg
 */
@Generator
public class NettySystemConfigCodeGenerator extends ClassCodeGenerator {
    private static final String CLASS_NAME = "NettySystemConfig";
    private static final String SUB_PACKAGE = "netty";
    private static final List<String> DEPENDENCIES = Collections.singletonList("TlsMode.java");

    public NettySystemConfigCodeGenerator() {
        super(CLASS_NAME, null, SUB_PACKAGE);
        setDependenciesName(DEPENDENCIES);

        String basePackage = ConfigInfos.getInstance().basePackage();
        String prefix = basePackage.toUpperCase().replace('.', '_');
        List<String> name = new ArrayList<>();
        List<String> value = new ArrayList<>();

        name.add(prefix + "_NETTY_POOLED_BYTE_BUF_ALLOCATOR_ENABLE");
        value.add("\"" + basePackage + ".nettyPooledByteBufAllocatorEnable\"");
        name.add(prefix + "_CLIENT_ASYNC_SEMAPHORE_VALUE");
        value.add("\"" + basePackage + ".clientAsyncSemaphoreValue\"");
        name.add(prefix + "_CLIENT_ONEWAY_SEMAPHORE_VALUE");
        value.add("\"" + basePackage + ".clientOnewaySemaphoreValue\"");
        name.add(prefix + "_SOCKET_SNDBUF_SIZE");
        value.add("\"" + basePackage + ".socket.sndbuf.size\"");
        name.add(prefix + "_SOCKET_RCVBUF_SIZE");
        value.add("\"" + basePackage + ".socket.rcvbuf.size\"");

        List<FieldInfo> fields = new ArrayList<>(10);
        fields.add(new FieldInfo(PUBLIC_STATIC_FINAL, "String", name.get(0), value.get(0)));
        fields.add(new FieldInfo(PUBLIC_STATIC_FINAL, "String", name.get(1), value.get(1)));
        fields.add(new FieldInfo(PUBLIC_STATIC_FINAL, "String", name.get(2), value.get(2)));
        fields.add(new FieldInfo(PUBLIC_STATIC_FINAL, "String", name.get(3), value.get(3)));
        fields.add(new FieldInfo(PUBLIC_STATIC_FINAL, "String", name.get(4), value.get(4)));

        fields.add(new FieldInfo(PUBLIC_STATIC_FINAL, "boolean", "NETTY_POOLED_BYTE_BUF_ALLOCATOR_ENABLE",
                "\n            Boolean.parseBoolean(System.getProperty(" + name.get(0) + ", \"false\"));"));
        fields.add(new FieldInfo(PUBLIC_STATIC_FINAL, "int", "CLIENT_ASYNC_SEMAPHORE_VALUE",
                "\n            Integer.parseInt(System.getProperty(" + name.get(1) + ", \"65535\"));"));
        fields.add(new FieldInfo(PUBLIC_STATIC_FINAL, "int", "CLIENT_ONEWAY_SEMAPHORE_VALUE",
                "\n            Integer.parseInt(System.getProperty(" + name.get(2) + ", \"65535\"));"));
        fields.add(new FieldInfo(PUBLIC_STATIC, "int", "socketSndbufSize",
                "\n            Integer.parseInt(System.getProperty(" + name.get(3) + ", \"65535\"));"));
        fields.add(new FieldInfo(PUBLIC_STATIC, "int", "socketRcvbufSize",
                "\n            Integer.parseInt(System.getProperty(" + name.get(4) + ", \"65535\"));"));
        setFields(fields);
    }

    @Override
    public String buildImports() {
        StringBuilder builder = new StringBuilder(100);
        CodeGeneratorHelper.buildDependencyImports(builder, getDependencies());
        builder.append("import io.netty.handler.ssl.SslContext;\n\n");
        return builder.toString();
    }
}
