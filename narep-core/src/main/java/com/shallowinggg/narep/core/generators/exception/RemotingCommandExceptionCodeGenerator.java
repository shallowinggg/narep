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
public class RemotingCommandExceptionCodeGenerator extends ClassCodeGenerator {
    private static final String CLASS_NAME = "RemotingCommandException";
    private static final String PARENT_CLASS = "RemotingException";
    private List<FieldMetaData> fields = new ArrayList<>(1);

    public RemotingCommandExceptionCodeGenerator() {
        super(CLASS_NAME, PARENT_CLASS);
        fields.add(new FieldMetaData(PRIVATE_STATIC_FINAL, "long", "serialVersionUID", "-6061365915274953096L"));
    }

    @Override
    public String buildFields() {
        return CodeGeneratorHelper.buildFieldsByMetaData(fields);
    }

    @Override
    public String buildMethods() {
        return "    public RemotingCommandException(String message) {\n" +
                "        super(message, null);\n" +
                "    }\n" +
                "\n" +
                "    public RemotingCommandException(String message, Throwable cause) {\n" +
                "        super(message, cause);\n" +
                "    }\n";
    }
}
