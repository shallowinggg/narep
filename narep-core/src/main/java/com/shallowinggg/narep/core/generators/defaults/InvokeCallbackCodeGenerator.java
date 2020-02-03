package com.shallowinggg.narep.core.generators.defaults;

import com.shallowinggg.narep.core.annotation.Generator;
import com.shallowinggg.narep.core.common.CodeGeneratorHelper;
import com.shallowinggg.narep.core.generators.InterfaceCodeGenerator;

import java.util.Collections;
import java.util.List;

/**
 * @author shallowinggg
 */
@Generator
public class InvokeCallbackCodeGenerator extends InterfaceCodeGenerator {
    private static final String INTERFACE_NAME = "InvokeCallback";
    private static final List<String> DEPENDENCY_NAMES = Collections.singletonList("ResponseFuture");

    public InvokeCallbackCodeGenerator() {
        super(INTERFACE_NAME, DEPENDENCY_NAMES);
    }

    @Override
    public String buildImports() {
        StringBuilder builder = new StringBuilder(60);
        CodeGeneratorHelper.buildDependencyImports(builder, getDependencies());
        builder.append("\n");
        return builder.toString();
    }

    @Override
    public String buildMethods() {
        return "    void operationComplete(final ResponseFuture responseFuture);\n\n";
    }
}
