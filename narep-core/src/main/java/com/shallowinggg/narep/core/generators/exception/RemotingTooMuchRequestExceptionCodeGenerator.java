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
public class RemotingTooMuchRequestExceptionCodeGenerator extends ClassCodeGenerator {
    private static final String CLASS_NAME = "RemotingTooMuchRequestException";
    private static final String PARENT_CLASS = "RemotingException";
    private List<FieldMetaData> fields = new ArrayList<>(1);

    public RemotingTooMuchRequestExceptionCodeGenerator() {
        super(CLASS_NAME, PARENT_CLASS);
        fields.add(new FieldMetaData(PRIVATE_STATIC_FINAL, "long", "serialVersionUID", "4326919581254519654L"));
    }

    @Override
    public String buildFields() {
        return CodeGeneratorHelper.buildFieldsByMetaData(fields);
    }

    @Override
    public String buildMethods() {
        return "    public RemotingTooMuchRequestException(String message) {\n" +
                "        super(message);\n" +
                "    }\n";
    }
}
