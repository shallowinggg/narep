package com.shallowinggg.narep.core.generators.common;

import com.shallowinggg.narep.core.annotation.Generator;
import com.shallowinggg.narep.core.common.CodeGeneratorHelper;
import com.shallowinggg.narep.core.common.ConfigInfos;
import com.shallowinggg.narep.core.generators.ClassCodeGenerator;
import com.shallowinggg.narep.core.lang.FieldInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.shallowinggg.narep.core.lang.Modifier.PRIVATE_STATIC_FINAL;
import static com.shallowinggg.narep.core.lang.Modifier.PUBLIC_STATIC_FINAL;


/**
 * @author shallowinggg
 */
@Generator
public class RemotingHelperCodeGenerator extends ClassCodeGenerator {
    private static final String CLASS_NAME = "RemotingHelper";
    private static final String SUB_PACKAGE = "common";

    public RemotingHelperCodeGenerator() {
        super(CLASS_NAME, null, SUB_PACKAGE);
        List<String> dependencyNames = Arrays.asList("RemotingConnectException",
                "RemotingSendRequestException", "RemotingTimeoutException", "RemotingCommand");
        setDependencyNames(dependencyNames);

        String loggerName = "\"" + ConfigInfos.getInstance().loggerName() + "\"";
        List<FieldInfo> fields = new ArrayList<>(3);
        fields.add(new FieldInfo(PUBLIC_STATIC_FINAL, "String", "REMOTING_LOGGER_NAME", loggerName));
        fields.add(new FieldInfo(PUBLIC_STATIC_FINAL, "String", "DEFAULT_CHARSET", "\"UTF-8\""));
        fields.add(new FieldInfo(PRIVATE_STATIC_FINAL, "Logger", "log", CodeGeneratorHelper.buildLoggerField(CLASS_NAME)));
        setFields(fields);
    }

    @Override
    public String buildImports() {
        StringBuilder imports = new StringBuilder(550);
        CodeGeneratorHelper.buildDependencyImports(imports, getDependencies());
        imports.append("import io.netty.channel.Channel;\n" +
                "import org.apache.logging.log4j.LogManager;\n" +
                "import org.apache.logging.log4j.Logger;\n" +
                "\n" +
                "import java.io.IOException;\n" +
                "import java.net.InetSocketAddress;\n" +
                "import java.net.SocketAddress;\n" +
                "import java.nio.ByteBuffer;\n" +
                "import java.nio.channels.SocketChannel;\n\n");
        return imports.toString();
    }

    @Override
    public String buildMethods() {
        return exceptionSimpleDesc() +
                string2SocketAddress() +
                invokeSync() +
                parseChannelRemoteAddr() +
                parseSocketAddressAddr();
    }


    // methods

    private String exceptionSimpleDesc() {
        return "    public static String exceptionSimpleDesc(final Throwable e) {\n" +
                "        StringBuilder sb = new StringBuilder();\n" +
                "        if (e != null) {\n" +
                "            sb.append(e.toString());\n" +
                "\n" +
                "            StackTraceElement[] stackTrace = e.getStackTrace();\n" +
                "            if (stackTrace != null && stackTrace.length > 0) {\n" +
                "                StackTraceElement element = stackTrace[0];\n" +
                "                sb.append(\", \");\n" +
                "                sb.append(element.toString());\n" +
                "            }\n" +
                "        }\n" +
                "\n" +
                "        return sb.toString();\n" +
                "    }\n\n";
    }

    private String string2SocketAddress() {
        return "    public static SocketAddress string2SocketAddress(final String addr) {\n" +
                "        String[] s = addr.split(\":\");\n" +
                "        return new InetSocketAddress(s[0], Integer.parseInt(s[1]));\n" +
                "    }\n\n";
    }

    private String invokeSync() {
        return "    public static RemotingCommand invokeSync(final String addr, final RemotingCommand request,\n" +
                "                                             final long timeoutMillis) throws InterruptedException, RemotingConnectException,\n" +
                "            RemotingSendRequestException, RemotingTimeoutException {\n" +
                "        long beginTime = System.currentTimeMillis();\n" +
                "        SocketAddress socketAddress = RemotingUtil.string2SocketAddress(addr);\n" +
                "        SocketChannel socketChannel = RemotingUtil.connect(socketAddress);\n" +
                "        if (socketChannel != null) {\n" +
                "            boolean sendRequestOK = false;\n" +
                "\n" +
                "            try {\n" +
                "\n" +
                "                socketChannel.configureBlocking(true);\n" +
                "\n" +
                "                //bugfix  http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4614802\n" +
                "                socketChannel.socket().setSoTimeout((int) timeoutMillis);\n" +
                "\n" +
                "                ByteBuffer byteBufferRequest = request.encode();\n" +
                "                while (byteBufferRequest.hasRemaining()) {\n" +
                "                    int length = socketChannel.write(byteBufferRequest);\n" +
                "                    if (length > 0) {\n" +
                "                        if (byteBufferRequest.hasRemaining()) {\n" +
                "                            if ((System.currentTimeMillis() - beginTime) > timeoutMillis) {\n" +
                "\n" +
                "                                throw new RemotingSendRequestException(addr);\n" +
                "                            }\n" +
                "                        }\n" +
                "                    } else {\n" +
                "                        throw new RemotingSendRequestException(addr);\n" +
                "                    }\n" +
                "\n" +
                "                    Thread.sleep(1);\n" +
                "                }\n" +
                "\n" +
                "                sendRequestOK = true;\n" +
                "\n" +
                "                ByteBuffer byteBufferSize = ByteBuffer.allocate(4);\n" +
                "                while (byteBufferSize.hasRemaining()) {\n" +
                "                    int length = socketChannel.read(byteBufferSize);\n" +
                "                    if (length > 0) {\n" +
                "                        if (byteBufferSize.hasRemaining()) {\n" +
                "                            if ((System.currentTimeMillis() - beginTime) > timeoutMillis) {\n" +
                "                                throw new RemotingTimeoutException(addr, timeoutMillis);\n" +
                "                            }\n" +
                "                        }\n" +
                "                    } else {\n" +
                "                        throw new RemotingTimeoutException(addr, timeoutMillis);\n" +
                "                    }\n" +
                "\n" +
                "                    Thread.sleep(1);\n" +
                "                }\n" +
                "\n" +
                "                int size = byteBufferSize.getInt(0);\n" +
                "                ByteBuffer byteBufferBody = ByteBuffer.allocate(size);\n" +
                "                while (byteBufferBody.hasRemaining()) {\n" +
                "                    int length = socketChannel.read(byteBufferBody);\n" +
                "                    if (length > 0) {\n" +
                "                        if (byteBufferBody.hasRemaining()) {\n" +
                "                            if ((System.currentTimeMillis() - beginTime) > timeoutMillis) {\n" +
                "                                throw new RemotingTimeoutException(addr, timeoutMillis);\n" +
                "                            }\n" +
                "                        }\n" +
                "                    } else {\n" +
                "                        throw new RemotingTimeoutException(addr, timeoutMillis);\n" +
                "                    }\n" +
                "\n" +
                "                    Thread.sleep(1);\n" +
                "                }\n" +
                "\n" +
                "                byteBufferBody.flip();\n" +
                "                return RemotingCommand.decode(byteBufferBody);\n" +
                "            } catch (IOException e) {\n" +
                "                log.error(\"invokeSync failure\", e);\n" +
                "\n" +
                "                if (sendRequestOK) {\n" +
                "                    throw new RemotingTimeoutException(addr, timeoutMillis);\n" +
                "                } else {\n" +
                "                    throw new RemotingSendRequestException(addr);\n" +
                "                }\n" +
                "            } finally {\n" +
                "                try {\n" +
                "                    socketChannel.close();\n" +
                "                } catch (IOException e) {\n" +
                "                    e.printStackTrace();\n" +
                "                }\n" +
                "            }\n" +
                "        } else {\n" +
                "            throw new RemotingConnectException(addr);\n" +
                "        }\n" +
                "    }\n\n";
    }

    private String parseChannelRemoteAddr() {
        return "    public static String parseChannelRemoteAddr(final Channel channel) {\n" +
                "        if (null == channel) {\n" +
                "            return \"\";\n" +
                "        }\n" +
                "        SocketAddress remote = channel.remoteAddress();\n" +
                "        final String addr = remote != null ? remote.toString() : \"\";\n" +
                "\n" +
                "        if (addr.length() > 0) {\n" +
                "            int index = addr.lastIndexOf(\"/\");\n" +
                "            if (index >= 0) {\n" +
                "                return addr.substring(index + 1);\n" +
                "            }\n" +
                "\n" +
                "            return addr;\n" +
                "        }\n" +
                "\n" +
                "        return \"\";\n" +
                "    }\n\n";
    }

    private String parseSocketAddressAddr() {
        return "    public static String parseSocketAddressAddr(SocketAddress socketAddress) {\n" +
                "        if (socketAddress != null) {\n" +
                "            final String addr = socketAddress.toString();\n" +
                "\n" +
                "            if (addr.length() > 0) {\n" +
                "                return addr.substring(1);\n" +
                "            }\n" +
                "        }\n" +
                "        return \"\";\n" +
                "    }\n\n";
    }
}
