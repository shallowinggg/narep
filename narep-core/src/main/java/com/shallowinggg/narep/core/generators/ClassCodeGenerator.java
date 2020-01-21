package com.shallowinggg.narep.core.generators;

import com.shallowinggg.narep.core.common.ClassDeclarations;
import com.shallowinggg.narep.core.common.CodeGeneratorHelper;
import com.shallowinggg.narep.core.lang.FieldInfo;
import com.shallowinggg.narep.core.lang.Modifier;
import com.shallowinggg.narep.core.util.CollectionUtils;

import java.util.List;

/**
 * 类文件代码生成器
 *
 * @author shallowinggg
 */
public class ClassCodeGenerator extends AbstractJavaCodeGenerator {
    private String[] interfaceNames;
    private List<FieldInfo> fields;

    public ClassCodeGenerator(String name) {
        super(name);
    }

    public ClassCodeGenerator(Modifier modifier, String name) {
        super(modifier, name);
    }

    public ClassCodeGenerator(String name, String parentName) {
        super(name, parentName);
    }

    public ClassCodeGenerator(String name, String parentName, String[] interfaceNames) {
        super(name, parentName);
        this.interfaceNames = interfaceNames;
    }

    public ClassCodeGenerator(String name, List<String> dependenciesName) {
        super(name, dependenciesName);
    }

    public ClassCodeGenerator(String name, String parentName, String subPackageName) {
        super(name, parentName, subPackageName);
    }

    public ClassCodeGenerator(String name, String parentName, String subPackageName, String[] interfaceNames) {
        super(name, parentName, subPackageName);
        this.interfaceNames = interfaceNames;
    }

    @Override
    public String buildDeclaration() {
        return ClassDeclarations.buildStrategy(getModifier(), getName(), getParentName(), interfaceNames, null,
                false, false, false, false).buildDeclaration();
    }

    @Override
    public String buildFields() {
        if(CollectionUtils.isNotEmpty(fields)) {
            return CodeGeneratorHelper.buildFieldsByFieldInfos(fields);
        }
        return super.buildFields();
    }

    public void setInterfaceNames(String[] interfaceNames) {
        this.interfaceNames = interfaceNames;
    }

    public String[] getInterfaceNames() {
        return interfaceNames;
    }

    public List<FieldInfo> getFields() {
        return fields;
    }

    public void setFields(List<FieldInfo> fields) {
        this.fields = fields;
    }
}
