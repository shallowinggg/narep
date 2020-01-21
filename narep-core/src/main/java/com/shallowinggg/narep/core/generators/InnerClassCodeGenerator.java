package com.shallowinggg.narep.core.generators;

import com.shallowinggg.narep.core.DependencyResolver;
import com.shallowinggg.narep.core.JavaCodeGenerator;
import com.shallowinggg.narep.core.common.CodeGeneratorHelper;

import java.io.IOException;

import static com.shallowinggg.narep.core.util.StringTinyUtils.EMPTY_STRING;

/**
 * @author shallowinggg
 */
public class InnerClassCodeGenerator implements JavaCodeGenerator {

    /**
     * 外部类
     */
    private JavaCodeGenerator outer;

    /**
     * 内部类
     */
    private AbstractJavaCodeGenerator concrete;

    public InnerClassCodeGenerator(JavaCodeGenerator outer, AbstractJavaCodeGenerator concrete) {
        this.outer = outer;
        this.concrete = concrete;
    }

    @Override
    public String fullQualifiedName() {
        return CodeGeneratorHelper.buildInnerClassFullQualifiedName(outer.fullQualifiedName(),
                concrete.getName());
    }

    @Override
    public String openSourceLicense() {
        return EMPTY_STRING;
    }

    @Override
    public String buildPackage() {
        return EMPTY_STRING;
    }

    @Override
    public String buildImports() {
        return EMPTY_STRING;
    }

    @Override
    public String buildClassComment() {
        return concrete.buildClassComment();
    }

    @Override
    public String buildDeclaration() {
        return concrete.buildDeclaration();
    }

    @Override
    public String buildFields() {
        return concrete.buildFields();
    }

    @Override
    public String buildMethods() {
        return concrete.buildMethods();
    }

    @Override
    public String buildInnerClass() {
        return concrete.buildInnerClass();
    }

    @Override
    public boolean resolveDependencies(DependencyResolver resolver) {
        return true;
    }

    @Override
    public String fileName() {
        return EMPTY_STRING;
    }

    @Override
    public void write() throws IOException {
        throw new UnsupportedOperationException();
    }

}
