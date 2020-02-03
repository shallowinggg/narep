package com.shallowinggg.narep.core.generators;

import com.shallowinggg.narep.core.common.ClassDeclarations;

import java.util.List;

/**
 * @author shallowinggg
 */
public class GenericClassCodeGenerator extends ClassCodeGenerator {
    private List<String> generics;

    public GenericClassCodeGenerator(String name, String subPackageName, List<String> generics) {
        super(name, null, subPackageName);
        this.generics = generics;
    }

    public GenericClassCodeGenerator(String name, String parentName, String subPackageName,
                                     String[] interfaceNames, List<String> generics) {
        super(name, parentName, subPackageName, interfaceNames);
        this.generics = generics;
    }

    @Override
    public String buildDeclaration() {
        return ClassDeclarations.buildStrategy(getModifier(), getName(), getParentName(), getInterfaceNames(),
                generics,false, false, false, true).buildDeclaration();
    }
}
