package com.shallowinggg.narep.core.generators.common;

import com.shallowinggg.narep.core.annotation.Generator;
import com.shallowinggg.narep.core.common.CodeGeneratorHelper;
import com.shallowinggg.narep.core.generators.GenericClassCodeGenerator;
import com.shallowinggg.narep.core.lang.FieldInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.shallowinggg.narep.core.lang.Modifier.PRIVATE;


/**
 * @author shallowinggg
 */
@Generator
public class PairCodeGenerator extends GenericClassCodeGenerator {
    private static final String CLASS_NAME = "Pair";
    private static final String SUB_PACKAGE = "common";
    private static final List<String> GENERICS = Arrays.asList("T1", "T2");

    public PairCodeGenerator() {
        super(CLASS_NAME, SUB_PACKAGE, GENERICS);

        List<FieldInfo> fields = new ArrayList<>(2);
        fields.add(new FieldInfo(PRIVATE, "T1", "object1"));
        fields.add(new FieldInfo(PRIVATE, "T2", "object2"));
        setFields(fields);
    }

    @Override
    public String buildMethods() {
        return "    public Pair(T1 object1, T2 object2) {\n" +
                "        this.object1 = object1;\n" +
                "        this.object2 = object2;\n" +
                "    }\n" +
                CodeGeneratorHelper.buildGetterAndSetterMethods(getFields());
    }
}
