package com.shallowinggg.narep.core.generators;

import com.shallowinggg.narep.core.CodeGenerator;
import com.shallowinggg.narep.core.common.CodeGeneratorHelper;
import com.shallowinggg.narep.core.common.GeneratorConfig;

/**
 * @author shallowinggg
 */
public class RemotingServiceCodeGenerator implements CodeGenerator {
    private static final String CLASS_NAME = "RemotingService";
    private GeneratorConfig generatorConfig;

    public RemotingServiceCodeGenerator(GeneratorConfig generatorConfig) {
        this.generatorConfig = generatorConfig;
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
        return System.lineSeparator();
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
                "     * 启动远程服务\n" +
                "     */\n" +
                "    void start();\n" +
                "\n" +
                "    /**\n" +
                "     * 关闭远程服务\n" +
                "     */\n" +
                "    void shutdown();\n" +
                "\n" +
                "    /**\n" +
                "     * 注册{@link RPCHook}，发送请求以及收到回复时执行相应的\n" +
                "     * 预设逻辑。\n" +
                "     *\n" +
                "     * @param rpcHook RPCHook\n" +
                "     */\n" +
                "    void registerRPCHook(RPCHook rpcHook);\n";
    }
}
