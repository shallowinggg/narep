package com.shallowinggg.narep.core.generators.defaults;

import com.shallowinggg.narep.core.common.CodeGeneratorHelper;
import com.shallowinggg.narep.core.generators.InterfaceCodeGenerator;

import java.util.Collections;

import static com.shallowinggg.narep.core.common.JLSConstants.DOUBLE_LINE_SEPARATOR;

/**
 * @author shallowinggg
 */
public class RPCHookCodeGenerator extends InterfaceCodeGenerator {
    private static final String INTERFACE_NAME = "RPCHook";

    public RPCHookCodeGenerator() {
        super(INTERFACE_NAME, Collections.singletonList("RemotingCommandCodeGenerator.java"));
    }

    @Override
    public String buildImports() {
        StringBuilder imports = new StringBuilder(50);
        CodeGeneratorHelper.buildDependencyImports(imports, getDependencies());
        imports.append(DOUBLE_LINE_SEPARATOR);
        return imports.toString();
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
