package com.shallowinggg.narep.core.generators.defaults;

import com.shallowinggg.narep.core.common.GeneratorConfig;
import com.shallowinggg.narep.core.generators.InterfaceCodeGenerator;

/**
 * @author shallowinggg
 */
public class RemotingServiceCodeGenerator extends InterfaceCodeGenerator {
    private static final String INTERFACE_NAME = "RemotingService";

    public RemotingServiceCodeGenerator(GeneratorConfig generatorConfig) {
        super(INTERFACE_NAME, generatorConfig);
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
