package com.shallowinggg.narep.core.generators;

import com.shallowinggg.narep.core.CodeGenerator;
import com.shallowinggg.narep.core.common.CodeGeneratorHelper;
import com.shallowinggg.narep.core.common.GeneratorConfig;

import java.util.List;

/**
 * @author shallowinggg
 */
public class RPCHookCodeGenerator implements CodeGenerator {
    private static final String CLASS_NAME = "RPCHook";

    private GeneratorConfig generatorConfig;
    private List<CodeGenerator> dependencies;

    public RPCHookCodeGenerator(GeneratorConfig generatorConfig, List<CodeGenerator> dependencies) {
        this.generatorConfig = generatorConfig;
        this.dependencies = dependencies;
    }

    @Override
    public String fileName() {
        return CodeGeneratorHelper.buildFileName(generatorConfig.getBasePackage(), CLASS_NAME);
    }

    @Override
    public String buildPackage() {
        return CodeGeneratorHelper.buildDefaultPackage(generatorConfig.getBasePackage());
    }

    @Override
    public String buildImports() {
        StringBuilder imports = new StringBuilder(50);
        CodeGeneratorHelper.buildDependencyImports(imports, dependencies);
        imports.append(System.lineSeparator()).append(System.lineSeparator());
        return imports.toString();
    }

    @Override
    public String buildName() {
        return CodeGeneratorHelper.buildInterfaceDeclaration(CLASS_NAME);
    }

    @Override
    public String buildFields() {
        return System.lineSeparator();
    }

    @Override
    public String buildMethods() {
        return "    /**\n" +
                "     * 在发送请求之前执行\n" +
                "     *\n" +
                "     * @param remoteAddr 接收方地址\n" +
                "     * @param request    请求内容\n" +
                "     */\n" +
                "    void doBeforeRequest(final String remoteAddr, final RemotingCommand request);\n" +
                "\n" +
                "    /**\n" +
                "     * 收到接收方发送的回复后执行\n" +
                "     *\n" +
                "     * @param remoteAddr 接收方地址\n" +
                "     * @param request    请求内容\n" +
                "     * @param response   回复内容\n" +
                "     */\n" +
                "    void doAfterResponse(final String remoteAddr, final RemotingCommand request,\n" +
                "                         final RemotingCommand response);\n";
    }
}
