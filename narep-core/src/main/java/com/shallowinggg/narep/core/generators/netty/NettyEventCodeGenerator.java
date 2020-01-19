package com.shallowinggg.narep.core.generators.netty;

import com.shallowinggg.narep.core.common.CodeGeneratorHelper;
import com.shallowinggg.narep.core.common.FieldMetaData;
import com.shallowinggg.narep.core.generators.ClassCodeGenerator;

import java.util.ArrayList;
import java.util.List;

import static com.shallowinggg.narep.core.common.FieldMetaData.Modifier.PRIVATE_FINAL;

/**
 * @author shallowinggg
 */
public class NettyEventCodeGenerator extends ClassCodeGenerator {
    private static final String CLASS_NAME = "NettyEvent";
    private static final String SUB_PACKAGE = "netty";
    private List<FieldMetaData> fields = new ArrayList<>(3);

    public NettyEventCodeGenerator() {
        super(CLASS_NAME, null, SUB_PACKAGE);

        fields.add(new FieldMetaData(PRIVATE_FINAL, "NettyEventType", "type"));
        fields.add(new FieldMetaData(PRIVATE_FINAL, "String", "remoteAddr"));
        fields.add(new FieldMetaData(PRIVATE_FINAL, "Channel", "channel"));
    }

    @Override
    public String buildImports() {
        return "import io.netty.channel.Channel;\n\n";
    }

    @Override
    public String buildFields() {
        return CodeGeneratorHelper.buildFieldsByMetaData(fields);
    }

    @Override
    public String buildMethods() {
        StringBuilder builder = new StringBuilder(600);
        builder.append("    public NettyEvent(NettyEventType type, String remoteAddr, Channel channel) {\n" +
                "        this.type = type;\n" +
                "        this.remoteAddr = remoteAddr;\n" +
                "        this.channel = channel;\n" +
                "    }\n\n");
        CodeGeneratorHelper.buildGetterMethods(builder, fields);
        builder.append(CodeGeneratorHelper.buildToStringMethod(CLASS_NAME, fields));
        return builder.toString();
    }
}
