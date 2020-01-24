package com.shallowinggg.narep.core.generators.defaults;

import com.shallowinggg.narep.core.annotation.Generator;
import com.shallowinggg.narep.core.generators.InterfaceCodeGenerator;

/**
 * @author shallowinggg
 */
@Generator
public class ChannelEventListenerCodeGenerator extends InterfaceCodeGenerator {
    private static final String INTERFACE_NAME = "ChannelEventListener";

    public ChannelEventListenerCodeGenerator() {
        super(INTERFACE_NAME);
    }

    @Override
    public String buildImports() {
        return "import io.netty.channel.Channel;\n\n";
    }

    @Override
    public String buildMethods() {
        return "    void onChannelConnect(final String remoteAddr, final Channel channel);\n" +
                "\n" +
                "    void onChannelClose(final String remoteAddr, final Channel channel);\n" +
                "\n" +
                "    void onChannelException(final String remoteAddr, final Channel channel);\n" +
                "\n" +
                "    void onChannelIdle(final String remoteAddr, final Channel channel);\n\n";
    }
}
