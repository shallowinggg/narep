package com.shallowinggg.narep.core.generators.defaults;

import com.shallowinggg.narep.core.annotation.Generator;
import com.shallowinggg.narep.core.annotation.Profiler;
import com.shallowinggg.narep.core.common.CodeGeneratorHelper;
import com.shallowinggg.narep.core.generators.InterfaceCodeGenerator;

import java.util.Collections;
import java.util.List;

/**
 * @author shallowinggg
 */
@Generator
@Profiler("default")
public class CommandCustomHeaderCodeGenerator extends InterfaceCodeGenerator {
    private static final String INTERFACE_NAME = "CommandCustomHeader";
    private static final List<String> DEPENDENCY_NAMES = Collections.singletonList("RemotingCommandException");


    public CommandCustomHeaderCodeGenerator() {
        super(INTERFACE_NAME, DEPENDENCY_NAMES);
    }

    @Override
    public String buildImports() {
        StringBuilder builder = new StringBuilder(70);
        CodeGeneratorHelper.buildDependencyImports(builder, getDependencies());
        return builder.toString();
    }

    @Override
    public String buildMethods() {
        return "    void checkFields() throws RemotingCommandException;\n";
    }
}
