package com.shallowinggg.narep.core.common;

/**
 * @author shallowinggg
 */
public interface GeneratorNameGenerator {

    /**
     * Generate a generator name for the given generator definition.
     * @param definition the generator definition to generate a name for
     * @param registry the generator definition registry that the given definition
     * is supposed to be registered with
     * @return the generated bean name
     */
    String generateGeneratorName(GeneratorDefinition definition, CodeGeneratorManager registry);
}
