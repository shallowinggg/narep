package com.shallowinggg.narep.core.generators.netty;

import com.shallowinggg.narep.core.annotation.Generator;
import com.shallowinggg.narep.core.common.CodeGeneratorHelper;
import com.shallowinggg.narep.core.generators.ClassCodeGenerator;
import com.shallowinggg.narep.core.lang.FieldInfo;

import java.util.ArrayList;
import java.util.List;

import static com.shallowinggg.narep.core.lang.Modifier.PRIVATE_FINAL;


/**
 * @author shallowinggg
 */
@Generator
public class NettyEventCodeGenerator extends ClassCodeGenerator {
    private static final String CLASS_NAME = "NettyEvent";
    private static final String SUB_PACKAGE = "netty";

    public NettyEventCodeGenerator() {
        super(CLASS_NAME, null, SUB_PACKAGE);

        List<FieldInfo> fields = new ArrayList<>(3);
        fields.add(new FieldInfo(PRIVATE_FINAL, "NettyEventType", "type"));
        fields.add(new FieldInfo(PRIVATE_FINAL, "String", "remoteAddr"));
        fields.add(new FieldInfo(PRIVATE_FINAL, "Channel", "channel"));
        setFields(fields);
    }

    @Override
    public String buildImports() {
        return "import io.netty.channel.Channel;\n\n";
    }

    @Override
    public String buildMethods() {
        StringBuilder builder = new StringBuilder(600);
        builder.append("    public NettyEvent(NettyEventType type, String remoteAddr, Channel channel) {\n" +
                "        this.type = type;\n" +
                "        this.remoteAddr = remoteAddr;\n" +
                "        this.channel = channel;\n" +
                "    }\n\n");
        CodeGeneratorHelper.buildGetterMethods(builder, getFields());
        builder.append(CodeGeneratorHelper.buildToStringMethod(CLASS_NAME, getFields()));
        return builder.toString();
    }
}
