package com.shallowinggg.narep.core.generators.netty;

import com.shallowinggg.narep.core.annotation.Generator;
import com.shallowinggg.narep.core.common.CodeGeneratorHelper;
import com.shallowinggg.narep.core.generators.ClassCodeGenerator;
import com.shallowinggg.narep.core.lang.FieldInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.shallowinggg.narep.core.lang.Modifier.PRIVATE_FINAL;
import static com.shallowinggg.narep.core.lang.Modifier.PRIVATE_VOLATILE;


/**
 * @author shallowinggg
 */
@Generator
public class ResponseFutureCodeGenerator extends ClassCodeGenerator {
    private static final String CLASS_NAME = "ResponseFuture";
    private static final String SUB_PACKAGE = "netty";
    private static final List<String> DEPENDENCY_NAMES = Arrays.asList("InvokeCallback",
            "SemaphoreReleaseOnlyOnce", "RemotingCommand");

    public ResponseFutureCodeGenerator() {
        super(CLASS_NAME, null, SUB_PACKAGE);
        setDependencyNames(DEPENDENCY_NAMES);

        List<FieldInfo> fields = new ArrayList<>(11);
        fields.add(new FieldInfo(PRIVATE_FINAL, "int", "opaque"));
        fields.add(new FieldInfo(PRIVATE_FINAL, "Channel", "processChannel"));
        fields.add(new FieldInfo(PRIVATE_FINAL, "long", "timeoutMillis"));
        fields.add(new FieldInfo(PRIVATE_FINAL, "InvokeCallback", "invokeCallback"));
        fields.add(new FieldInfo(PRIVATE_FINAL, "long", "beginTimestamp", "System.currentTimeMillis()"));
        fields.add(new FieldInfo(PRIVATE_FINAL, "CountDownLatch", "countDownLatch", "new CountDownLatch(1)"));
        fields.add(new FieldInfo(PRIVATE_FINAL, "SemaphoreReleaseOnlyOnce", "once"));
        fields.add(new FieldInfo(PRIVATE_FINAL, "AtomicBoolean", "executeCallbackOnlyOnce", "new AtomicBoolean(false)"));
        fields.add(new FieldInfo(PRIVATE_VOLATILE, "RemotingCommand", "responseCommand"));
        fields.add(new FieldInfo(PRIVATE_VOLATILE, "boolean", "sendRequestOK", "true"));
        fields.add(new FieldInfo(PRIVATE_VOLATILE, "Throwable", "cause"));
        setFields(fields);
    }

    @Override
    public String buildImports() {
        StringBuilder builder = new StringBuilder(400);
        CodeGeneratorHelper.buildDependencyImports(builder, getDependencies());
        builder.append("import io.netty.channel.Channel;\n" +
                "\n" +
                "import java.util.concurrent.CountDownLatch;\n" +
                "import java.util.concurrent.TimeUnit;\n" +
                "import java.util.concurrent.atomic.AtomicBoolean;\n\n");
        return builder.toString();
    }


    @Override
    public String buildMethods() {
        StringBuilder builder = new StringBuilder(3000);

        builder.append("    public ResponseFuture(Channel channel, int opaque, long timeoutMillis, InvokeCallback invokeCallback,\n" +
                "                          SemaphoreReleaseOnlyOnce once) {\n" +
                "        this.opaque = opaque;\n" +
                "        this.processChannel = channel;\n" +
                "        this.timeoutMillis = timeoutMillis;\n" +
                "        this.invokeCallback = invokeCallback;\n" +
                "        this.once = once;\n" +
                "    }\n" +
                "\n" +
                "    public void executeInvokeCallback() {\n" +
                "        if (invokeCallback != null) {\n" +
                "            if (this.executeCallbackOnlyOnce.compareAndSet(false, true)) {\n" +
                "                invokeCallback.operationComplete(this);\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    public void release() {\n" +
                "        if (this.once != null) {\n" +
                "            this.once.release();\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    public boolean isTimeout() {\n" +
                "        long diff = System.currentTimeMillis() - this.beginTimestamp;\n" +
                "        return diff > this.timeoutMillis;\n" +
                "    }\n" +
                "\n" +
                "    public RemotingCommand waitResponse(final long timeoutMillis) throws InterruptedException {\n" +
                "        this.countDownLatch.await(timeoutMillis, TimeUnit.MILLISECONDS);\n" +
                "        return this.responseCommand;\n" +
                "    }\n" +
                "\n" +
                "    public void putResponse(final RemotingCommand responseCommand) {\n" +
                "        this.responseCommand = responseCommand;\n" +
                "        this.countDownLatch.countDown();\n" +
                "    }\n\n");
        CodeGeneratorHelper.buildGetterMethods(builder, getFields().subList(0, 5));
        CodeGeneratorHelper.buildGetterAndSetterMethods(builder, getFields().subList(8, 11));
        builder.append(CodeGeneratorHelper.buildToStringMethod(CLASS_NAME, getFields()));
        return builder.toString();
    }
}
