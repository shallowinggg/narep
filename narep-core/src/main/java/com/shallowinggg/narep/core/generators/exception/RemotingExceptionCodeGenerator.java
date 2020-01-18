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
public class RemotingExceptionCodeGenerator extends ClassCodeGenerator {
    private static final String CLASS_NAME = "RemotingException";
    private static final String PARENT_CLASS = "Exception";
    private List<FieldMetaData> fields = new ArrayList<>(1);

    public RemotingExceptionCodeGenerator() {
        super(CLASS_NAME, PARENT_CLASS);
        fields.add(new FieldMetaData(PRIVATE_STATIC_FINAL, "long", "serialVersionUID", "-5690687334570505110L"));
    }

    @Override
    public String buildFields() {
        return CodeGeneratorHelper.buildFieldsByMetaData(fields);
    }

    @Override
    public String buildMethods() {
        return "    public RemotingException(String message) {\n" +
                "        super(message);\n" +
                "    }\n" +
                "\n" +
                "    public RemotingException(String message, Throwable cause) {\n" +
                "        super(message, cause);\n" +
                "    }\n";
    }
}
