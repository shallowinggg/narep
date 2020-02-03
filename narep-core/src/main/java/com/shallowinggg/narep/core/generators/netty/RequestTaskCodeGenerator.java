package com.shallowinggg.narep.core.generators.netty;

import com.shallowinggg.narep.core.annotation.Generator;
import com.shallowinggg.narep.core.common.CodeGeneratorHelper;
import com.shallowinggg.narep.core.generators.ClassCodeGenerator;
import com.shallowinggg.narep.core.lang.FieldInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.shallowinggg.narep.core.lang.Modifier.PRIVATE;
import static com.shallowinggg.narep.core.lang.Modifier.PRIVATE_FINAL;


/**
 * @author shallowinggg
 */
@Generator
public class RequestTaskCodeGenerator extends ClassCodeGenerator {
    private static final String CLASS_NAME = "RequestTask";
    private static final String[] INTERFACES = new String[]{"Runnable"};
    private static final String SUB_PACKAGE = "netty";
    private static final List<String> DEPENDENCY_NAMES = Collections.singletonList("RemotingCommand");

    public RequestTaskCodeGenerator() {
        super(CLASS_NAME, null, SUB_PACKAGE, INTERFACES);
        setDependencyNames(DEPENDENCY_NAMES);

        List<FieldInfo> fields = new ArrayList<>(5);
        fields.add(new FieldInfo(PRIVATE_FINAL, "Runnable", "runnable"));
        fields.add(new FieldInfo(PRIVATE_FINAL, "long", "createTimestamp", "System.currentTimeMillis()"));
        fields.add(new FieldInfo(PRIVATE_FINAL, "Channel", "channel"));
        fields.add(new FieldInfo(PRIVATE_FINAL, "RemotingCommand", "request"));
        fields.add(new FieldInfo(PRIVATE, "boolean", "stopRun", "false"));
        setFields(fields);
    }

    @Override
    public String buildImports() {
        StringBuilder builder = new StringBuilder(100);
        CodeGeneratorHelper.buildDependencyImports(builder, getDependencies());
        builder.append("import io.netty.channel.Channel;\n\n");
        return builder.toString();
    }

    @Override
    public String buildMethods() {
        StringBuilder builder = new StringBuilder();
        builder.append("    public RequestTask(final Runnable runnable, final Channel channel, final RemotingCommand request) {\n" +
                "        this.runnable = runnable;\n" +
                "        this.channel = channel;\n" +
                "        this.request = request;\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public int hashCode() {\n" +
                "        int result = runnable != null ? runnable.hashCode() : 0;\n" +
                "        result = 31 * result + (int) (getCreateTimestamp() ^ (getCreateTimestamp() >>> 32));\n" +
                "        result = 31 * result + (channel != null ? channel.hashCode() : 0);\n" +
                "        result = 31 * result + (request != null ? request.hashCode() : 0);\n" +
                "        result = 31 * result + (isStopRun() ? 1 : 0);\n" +
                "        return result;\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public boolean equals(final Object o) {\n" +
                "        if (this == o)\n" +
                "            return true;\n" +
                "        if (!(o instanceof RequestTask))\n" +
                "            return false;\n" +
                "\n" +
                "        final RequestTask that = (RequestTask) o;\n" +
                "\n" +
                "        if (getCreateTimestamp() != that.getCreateTimestamp())\n" +
                "            return false;\n" +
                "        if (isStopRun() != that.isStopRun())\n" +
                "            return false;\n" +
                "        if (channel != null ? !channel.equals(that.channel) : that.channel != null)\n" +
                "            return false;\n" +
                "        return request != null ? request.getOpaque() == that.request.getOpaque() : that.request == null;\n" +
                "    }\n\n");
        CodeGeneratorHelper.buildGetterMethod(builder, getFields().get(1));
        CodeGeneratorHelper.buildGetterAndSetterMethods(builder, getFields().subList(4, 5));
        builder.append("    @Override\n" +
                "    public void run() {\n" +
                "        if (!this.stopRun) {\n" +
                "            this.runnable.run();\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    public void returnResponse(int code, String remark) {\n" +
                "        final RemotingCommand response = RemotingCommand.createResponseCommand(code, remark);\n" +
                "        response.setOpaque(request.getOpaque());\n" +
                "        this.channel.writeAndFlush(response);\n" +
                "    }\n\n");
        return builder.toString();
    }
}
