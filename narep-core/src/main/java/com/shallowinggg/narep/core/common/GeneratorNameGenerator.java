package com.shallowinggg.narep.core.common;

import com.shallowinggg.narep.core.CodeGenerator;

/**
 * @author shallowinggg
 */
public interface GeneratorNameGenerator {

    /**
     * Generate a generator name for the given generator definition.
     * @param definition the generator definition to generate a name for
     * is supposed to be registered with
     * @return the generated code generator name
     */
    String generateGeneratorName(GeneratorDefinition definition, CodeGenerator generator);
}
