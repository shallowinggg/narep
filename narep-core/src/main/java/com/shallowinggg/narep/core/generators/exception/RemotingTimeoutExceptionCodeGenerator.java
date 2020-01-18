package com.shallowinggg.narep.core.generators.exception;

import com.shallowinggg.narep.core.common.CodeGeneratorHelper;
import com.shallowinggg.narep.core.common.FieldMetaData;
import com.shallowinggg.narep.core.generators.ClassCodeGenerator;

import java.util.ArrayList;
import java.util.List;

import static com.shallowinggg.narep.core.common.FieldMetaData.Modifier.PRIVATE_STATIC_FINAL;

/**
 * @author shallowinggg
 */
public class RemotingTimeoutExceptionCodeGenerator extends ClassCodeGenerator {
    private static final String CLASS_NAME = "RemotingTimeoutException";
    private static final String PARENT_CLASS = "RemotingException";
    private List<FieldMetaData> fields = new ArrayList<>(1);

    public RemotingTimeoutExceptionCodeGenerator() {
        super(CLASS_NAME, PARENT_CLASS);
        fields.add(new FieldMetaData(PRIVATE_STATIC_FINAL, "long", "serialVersionUID", "4106899185095245979L"));
    }

    @Override
    public String buildFields() {
        return CodeGeneratorHelper.buildFieldsByMetaData(fields);
    }

    @Override
    public String buildMethods() {
        return "    public RemotingTimeoutException(String message) {\n" +
                "        super(message);\n" +
                "    }\n" +
                "\n" +
                "    public RemotingTimeoutException(String addr, long timeoutMillis) {\n" +
                "        this(addr, timeoutMillis, null);\n" +
                "    }\n" +
                "\n" +
                "    public RemotingTimeoutException(String addr, long timeoutMillis, Throwable cause) {\n" +
                "        super(\"wait response on the channel <\" + addr + \"> timeout, \" + timeoutMillis + \"(ms)\", cause);\n" +
                "    }\n";
    }
}
