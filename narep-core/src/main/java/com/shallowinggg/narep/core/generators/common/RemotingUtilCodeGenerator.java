package com.shallowinggg.narep.core.generators.common;

import com.shallowinggg.narep.core.common.CodeGeneratorHelper;
import com.shallowinggg.narep.core.common.FieldMetaData;
import com.shallowinggg.narep.core.generators.ClassCodeGenerator;

import java.util.ArrayList;
import java.util.List;

import static com.shallowinggg.narep.core.common.FieldMetaData.Modifier.*;

/**
 * @author shallowinggg
 */
public class RemotingUtilCodeGenerator extends ClassCodeGenerator {
    private static final String CLASS_NAME = "RemotingUtil";
    private static final String SUB_PACKAGE = "common";
    private List<FieldMetaData> fields = new ArrayList<>(4);

    public RemotingUtilCodeGenerator() {
        super(CLASS_NAME, null, SUB_PACKAGE);

        fields.add(new FieldMetaData(PUBLIC_STATIC_FINAL, "String", "OS_NAME", "System.getProperty(\"os.name\")"));
        fields.add(new FieldMetaData(PRIVATE_STATIC_FINAL, "Logger", "log", CodeGeneratorHelper.buildLoggerField(CLASS_NAME)));
        fields.add(new FieldMetaData(PRIVATE_STATIC, "boolean", "isLinuxPlatform", "false"));
        fields.add(new FieldMetaData(PRIVATE_STATIC, "boolean", "isWindowsPlatform", "false"));
    }

    @Override
    public String buildImports() {
        return "import io.netty.channel.Channel;\n" +
                "import io.netty.channel.ChannelFuture;\n" +
                "import io.netty.channel.ChannelFutureListener;\n" +
                "import org.apache.logging.log4j.LogManager;\n" +
                "import org.apache.logging.log4j.Logger;\n" +
                "\n" +
                "import java.io.IOException;\n" +
                "import java.lang.reflect.Method;\n" +
                "import java.net.*;\n" +
                "import java.nio.channels.Selector;\n" +
                "import java.nio.channels.SocketChannel;\n" +
                "import java.nio.channels.spi.SelectorProvider;\n" +
                "import java.util.ArrayList;\n" +
                "import java.util.Enumeration;\n\n";
    }

    @Override
    public String buildFields() {
        return CodeGeneratorHelper.buildFieldsByMetaData(fields);
    }

    @Override
    public String buildMethods() {
        return staticConstructor() +
                platform() +
                openSelector() +
                getLocalAddress() +
                normalizeHostAddress() +
                string2SocketAddress() +
                socketAddress2String() +
                connect() +
                closeChannel();
    }

    private String staticConstructor() {
        return "    static {\n" +
                "        if (OS_NAME != null && OS_NAME.toLowerCase().contains(\"linux\")) {\n" +
                "            isLinuxPlatform = true;\n" +
                "        }\n" +
                "\n" +
                "        if (OS_NAME != null && OS_NAME.toLowerCase().contains(\"windows\")) {\n" +
                "            isWindowsPlatform = true;\n" +
                "        }\n" +
                "    }\n\n";
    }

    private String platform() {
        return "    public static boolean isWindowsPlatform() {\n" +
                "        return isWindowsPlatform;\n" +
                "    }\n" +
                "\n" +
                "   public static boolean isLinuxPlatform() {\n" +
                "        return isLinuxPlatform;\n" +
                "    }\n\n";
    }

    private String openSelector() {
        return "    public static Selector openSelector() throws IOException {\n" +
                "        Selector result = null;\n" +
                "\n" +
                "        if (isLinuxPlatform()) {\n" +
                "            try {\n" +
                "                final Class<?> providerClazz = Class.forName(\"sun.nio.ch.EPollSelectorProvider\");\n" +
                "                if (providerClazz != null) {\n" +
                "                    try {\n" +
                "                        final Method method = providerClazz.getMethod(\"provider\");\n" +
                "                        if (method != null) {\n" +
                "                            final SelectorProvider selectorProvider = (SelectorProvider) method.invoke(null);\n" +
                "                            if (selectorProvider != null) {\n" +
                "                                result = selectorProvider.openSelector();\n" +
                "                            }\n" +
                "                        }\n" +
                "                    } catch (final Exception e) {\n" +
                "                        log.warn(\"Open ePoll Selector for linux platform exception\", e);\n" +
                "                    }\n" +
                "                }\n" +
                "            } catch (final Exception e) {\n" +
                "                // ignore\n" +
                "            }\n" +
                "        }\n" +
                "\n" +
                "        if (result == null) {\n" +
                "            result = Selector.open();\n" +
                "        }\n" +
                "\n" +
                "        return result;\n" +
                "    }\n\n";
    }

    private String getLocalAddress() {
        return "    public static String getLocalAddress() {\n" +
                "        try {\n" +
                "            // Traversal Network interface to get the first non-loopback and non-private address\n" +
                "            Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();\n" +
                "            ArrayList<String> ipv4Result = new ArrayList<String>();\n" +
                "            ArrayList<String> ipv6Result = new ArrayList<String>();\n" +
                "            while (enumeration.hasMoreElements()) {\n" +
                "                final NetworkInterface networkInterface = enumeration.nextElement();\n" +
                "                final Enumeration<InetAddress> en = networkInterface.getInetAddresses();\n" +
                "                while (en.hasMoreElements()) {\n" +
                "                    final InetAddress address = en.nextElement();\n" +
                "                    if (!address.isLoopbackAddress()) {\n" +
                "                        if (address instanceof Inet6Address) {\n" +
                "                            ipv6Result.add(normalizeHostAddress(address));\n" +
                "                        } else {\n" +
                "                            ipv4Result.add(normalizeHostAddress(address));\n" +
                "                        }\n" +
                "                    }\n" +
                "                }\n" +
                "            }\n" +
                "\n" +
                "            // prefer ipv4\n" +
                "            if (!ipv4Result.isEmpty()) {\n" +
                "                for (String ip : ipv4Result) {\n" +
                "                    if (ip.startsWith(\"127.0\") || ip.startsWith(\"192.168\")) {\n" +
                "                        continue;\n" +
                "                    }\n" +
                "\n" +
                "                    return ip;\n" +
                "                }\n" +
                "\n" +
                "                return ipv4Result.get(ipv4Result.size() - 1);\n" +
                "            } else if (!ipv6Result.isEmpty()) {\n" +
                "                return ipv6Result.get(0);\n" +
                "            }\n" +
                "            //If failed to find,fall back to localhost\n" +
                "            final InetAddress localHost = InetAddress.getLocalHost();\n" +
                "            return normalizeHostAddress(localHost);\n" +
                "        } catch (Exception e) {\n" +
                "            log.error(\"Failed to obtain local address\", e);\n" +
                "        }\n" +
                "\n" +
                "        return null;\n" +
                "    }\n\n";
    }

    private String normalizeHostAddress() {
        return "    public static String normalizeHostAddress(final InetAddress localHost) {\n" +
                "        if (localHost instanceof Inet6Address) {\n" +
                "            return \"[\" + localHost.getHostAddress() + \"]\";\n" +
                "        } else {\n" +
                "            return localHost.getHostAddress();\n" +
                "        }\n" +
                "    }\n\n";
    }

    private String string2SocketAddress() {
        return "    public static SocketAddress string2SocketAddress(final String addr) {\n" +
                "        String[] s = addr.split(\":\");\n" +
                "        InetSocketAddress isa = new InetSocketAddress(s[0], Integer.parseInt(s[1]));\n" +
                "        return isa;\n" +
                "    }\n\n";
    }

    private String socketAddress2String() {
        return "    public static String socketAddress2String(final SocketAddress addr) {\n" +
                "        StringBuilder sb = new StringBuilder();\n" +
                "        InetSocketAddress inetSocketAddress = (InetSocketAddress) addr;\n" +
                "        sb.append(inetSocketAddress.getAddress().getHostAddress());\n" +
                "        sb.append(\":\");\n" +
                "        sb.append(inetSocketAddress.getPort());\n" +
                "        return sb.toString();\n" +
                "    }\n\n";
    }

    private String connect() {
        return "    public static SocketChannel connect(SocketAddress remote) {\n" +
                "        return connect(remote, 1000 * 5);\n" +
                "    }\n" +
                "\n" +
                "    public static SocketChannel connect(SocketAddress remote, final int timeoutMillis) {\n" +
                "        SocketChannel sc = null;\n" +
                "        try {\n" +
                "            sc = SocketChannel.open();\n" +
                "            sc.configureBlocking(true);\n" +
                "            sc.socket().setSoLinger(false, -1);\n" +
                "            sc.socket().setTcpNoDelay(true);\n" +
                "            sc.socket().setReceiveBufferSize(1024 * 64);\n" +
                "            sc.socket().setSendBufferSize(1024 * 64);\n" +
                "            sc.socket().connect(remote, timeoutMillis);\n" +
                "            sc.configureBlocking(false);\n" +
                "            return sc;\n" +
                "        } catch (Exception e) {\n" +
                "            if (sc != null) {\n" +
                "                try {\n" +
                "                    sc.close();\n" +
                "                } catch (IOException e1) {\n" +
                "                    e1.printStackTrace();\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "\n" +
                "        return null;\n" +
                "    }\n\n";
    }

    private String closeChannel() {
        return "    public static void closeChannel(Channel channel) {\n" +
                "        final String addrRemote = RemotingHelper.parseChannelRemoteAddr(channel);\n" +
                "        channel.close().addListener(new ChannelFutureListener() {\n" +
                "            @Override\n" +
                "            public void operationComplete(ChannelFuture future) throws Exception {\n" +
                "                log.info(\"closeChannel: close the connection to remote address[{}] result: {}\", addrRemote,\n" +
                "                        future.isSuccess());\n" +
                "            }\n" +
                "        });\n" +
                "    }\n\n";
    }
}
