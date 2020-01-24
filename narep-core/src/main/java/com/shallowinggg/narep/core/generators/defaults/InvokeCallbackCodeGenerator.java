package com.shallowinggg.narep.core.generators.defaults;

import com.shallowinggg.narep.core.annotation.Generator;
import com.shallowinggg.narep.core.generators.InterfaceCodeGenerator;

import java.util.Collections;
import java.util.List;

/**
 * @author shallowinggg
 */
@Generator
public class InvokeCallbackCodeGenerator extends InterfaceCodeGenerator {
    private static final String INTERFACE_NAME = "InvokeCallback";
    private static final List<String> DEPENDENCIES = Collections.singletonList("ResponseFuture.java");

    public InvokeCallbackCodeGenerator() {
        super(INTERFACE_NAME, DEPENDENCIES);
    }

    @Override
    public String buildMethods() {
        return "    void operationComplete(final ResponseFuture responseFuture);\n\n";
    }
}
