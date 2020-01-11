package com.shallowinggg.narep.core.generators.common;

import com.shallowinggg.narep.core.common.CodeGeneratorHelper;
import com.shallowinggg.narep.core.generators.ClassCodeGenerator;

import java.util.Arrays;
import java.util.List;

/**
 * @author shallowinggg
 */
public class PairCodeGenerator extends ClassCodeGenerator {
    private static final String CLASS_NAME = "Pair";
    private static final List<String> GENERICS = Arrays.asList("T1", "T2");

    public PairCodeGenerator() {
        super(CLASS_NAME);
    }

    @Override
    public String buildName() {
        return CodeGeneratorHelper.buildGenericClassDeclaration(CLASS_NAME, GENERICS);
    }

    @Override
    public String buildFields() {
        return "    private T1 object1;\n" +
                "    private T2 object2;\n\n";
    }

    @Override
    public String buildMethods() {
        return "    public Pair(T1 object1, T2 object2) {\n" +
                "        this.object1 = object1;\n" +
                "        this.object2 = object2;\n" +
                "    }\n" +
                "\n" +
                "    public T1 getObject1() {\n" +
                "        return object1;\n" +
                "    }\n" +
                "\n" +
                "    public void setObject1(T1 object1) {\n" +
                "        this.object1 = object1;\n" +
                "    }\n" +
                "\n" +
                "    public T2 getObject2() {\n" +
                "        return object2;\n" +
                "    }\n" +
                "\n" +
                "    public void setObject2(T2 object2) {\n" +
                "        this.object2 = object2;\n" +
                "    }\n";
    }
}
