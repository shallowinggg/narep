package com.shallowinggg.narep.core.generators.common;

import com.shallowinggg.narep.core.common.CodeGeneratorHelper;
import com.shallowinggg.narep.core.common.FieldMetaData;
import com.shallowinggg.narep.core.generators.GenericClassCodeGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.shallowinggg.narep.core.common.FieldMetaData.Modifier.PRIVATE;

/**
 * @author shallowinggg
 */
public class PairCodeGenerator extends GenericClassCodeGenerator {
    private static final String CLASS_NAME = "Pair";
    private static final String SUB_PACKAGE = "common";
    private static final List<String> GENERICS = Arrays.asList("T1", "T2");
    private List<FieldMetaData> fields = new ArrayList<>(2);

    public PairCodeGenerator() {
        super(CLASS_NAME, SUB_PACKAGE, GENERICS);

        fields.add(new FieldMetaData(PRIVATE, "T1", "object1"));
        fields.add(new FieldMetaData(PRIVATE, "T2", "object2"));
    }

    @Override
    public String buildFields() {
        return CodeGeneratorHelper.buildFieldsByMetaData(fields);
    }

    @Override
    public String buildMethods() {
        return "    public Pair(T1 object1, T2 object2) {\n" +
                "        this.object1 = object1;\n" +
                "        this.object2 = object2;\n" +
                "    }\n" +
                CodeGeneratorHelper.buildGetterAndSetterMethods(fields);
    }
}
