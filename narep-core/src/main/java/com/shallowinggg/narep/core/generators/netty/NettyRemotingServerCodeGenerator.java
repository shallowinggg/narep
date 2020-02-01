package com.shallowinggg.narep.core.generators.netty;

import com.shallowinggg.narep.core.annotation.Generator;
import com.shallowinggg.narep.core.common.CodeGeneratorHelper;
import com.shallowinggg.narep.core.generators.ClassCodeGenerator;
import com.shallowinggg.narep.core.generators.InnerClassCodeGenerator;
import com.shallowinggg.narep.core.lang.FieldInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.shallowinggg.narep.core.lang.Modifier.*;

/**
 * @author shallowinggg
 */
@Generator
public class NettyRemotingServerCodeGenerator extends ClassCodeGenerator {
    private static final String CLASS_NAME = "NettyRemotingServer";
    private static final String PARENT_NAME = "AbstractNettyRemoting";
    private static final String[] INTERFACE_NAMES = new String[]{"RemotingServer"};
    private static final String SUB_PACKAGE = "netty";
    private static final List<String> DEPENDENCY_NAMES = Arrays.asList("ChannelEventListener",
            "InvokeCallback", "RPCHook", "RemotingServer", "Pair",
            "RemotingHelper", "RemotingUtil", "TlsMode", "RemotingCommand",
            "RemotingSendRequestException", "RemotingTimeoutException",
            "RemotingTooMuchRequestException");

    public NettyRemotingServerCodeGenerator() {
        super(CLASS_NAME, PARENT_NAME, SUB_PACKAGE, INTERFACE_NAMES);
        setDependencyNames(DEPENDENCY_NAMES);

        List<FieldInfo> fields = new ArrayList<>(17);
        fields.add(new FieldInfo(PRIVATE_STATIC_FINAL, "Logger", "log", CodeGeneratorHelper.buildLoggerField(CLASS_NAME)));
        fields.add(new FieldInfo(PRIVATE_FINAL, "ServerBootstrap", "serverBootstrap"));
        fields.add(new FieldInfo(PRIVATE_FINAL, "EventLoopGroup", "eventLoopGroupSelector"));
        fields.add(new FieldInfo(PRIVATE_FINAL, "EventLoopGroup", "eventLoopGroupBoss"));
        fields.add(new FieldInfo(PRIVATE_FINAL, "NettyServerConfig", "nettyServerConfig"));
        fields.add(new FieldInfo(PRIVATE_FINAL, "ExecutorService", "publicExecutor"));
        fields.add(new FieldInfo(PRIVATE_FINAL, "ChannelEventListener", "channelEventListener"));
        fields.add(new FieldInfo(PRIVATE_FINAL, "ScheduledExecutorService", "timer",
                "Executors.newSingleThreadScheduledExecutor(r -> {\n" +
                        "        Thread t = new Thread(r, \"ServerHouseKeepingService\");\n" +
                        "        t.setDaemon(true);\n" +
                        "        return t;\n" +
                        "    })"));
        fields.add(new FieldInfo(PRIVATE, "DefaultEventExecutorGroup", "defaultEventExecutorGroup"));
        fields.add(new FieldInfo(PRIVATE, "int", "port", "0"));
        fields.add(new FieldInfo(PRIVATE_STATIC_FINAL, "String", "HANDSHAKE_HANDLER_NAME", "\"handshakeHandler\""));
        fields.add(new FieldInfo(PRIVATE_STATIC_FINAL, "String", "TLS_HANDLER_NAME", "\"sslHandler\""));
        fields.add(new FieldInfo(PRIVATE_STATIC_FINAL, "String", "FILE_REGION_ENCODER_NAME", "\"fileRegionEncoder\""));

        fields.add(new FieldInfo(PRIVATE, "HandshakeHandler", "handshakeHandler"));
        fields.add(new FieldInfo(PRIVATE, "NettyEncoder", "encoder"));
        fields.add(new FieldInfo(PRIVATE, "NettyConnectManageHandler", "connectionManageHandler"));
        fields.add(new FieldInfo(PRIVATE, "NettyServerHandler", "serverHandler"));
        setFields(fields);
    }

    @Override
    public String buildImports() {
        StringBuilder builder = new StringBuilder();
        CodeGeneratorHelper.buildDependencyImports(builder, getDependencies());
        builder.append("import io.netty.bootstrap.ServerBootstrap;\n" +
                "import io.netty.buffer.ByteBuf;\n" +
                "import io.netty.buffer.PooledByteBufAllocator;\n" +
                "import io.netty.channel.*;\n" +
                "import io.netty.channel.epoll.Epoll;\n" +
                "import io.netty.channel.epoll.EpollEventLoopGroup;\n" +
                "import io.netty.channel.epoll.EpollServerSocketChannel;\n" +
                "import io.netty.channel.nio.NioEventLoopGroup;\n" +
                "import io.netty.channel.socket.SocketChannel;\n" +
                "import io.netty.channel.socket.nio.NioServerSocketChannel;\n" +
                "import io.netty.handler.timeout.IdleState;\n" +
                "import io.netty.handler.timeout.IdleStateEvent;\n" +
                "import io.netty.handler.timeout.IdleStateHandler;\n" +
                "import io.netty.util.concurrent.DefaultEventExecutorGroup;\n" +
                "import org.apache.logging.log4j.LogManager;\n" +
                "import org.apache.logging.log4j.Logger;\n" +
                "\n" +
                "import java.io.IOException;\n" +
                "import java.net.InetSocketAddress;\n" +
                "import java.security.cert.CertificateException;\n" +
                "import java.util.NoSuchElementException;\n" +
                "import java.util.concurrent.*;\n" +
                "import java.util.concurrent.atomic.AtomicInteger;\n\n");
        return builder.toString();
    }

    @Override
    public String buildMethods() {
        return constructors() +
                loadSslContext() +
                useEpoll() +
                start() +
                shutdown() +
                registerRPCHook() +
                registerProcessor() +
                registerDefaultProcessor() +
                localListenPort() +
                getProcessorPair() +
                invoke() +
                get() +
                prepareSharableHandlers() +
                "\n";
    }

    @Override
    public String buildInnerClass() {
        InnerClassCodeGenerator handshakeHandler = new InnerClassCodeGenerator(this,
                new HandshakeHandlerCodeGenerator());
        InnerClassCodeGenerator nettyServerHandler = new InnerClassCodeGenerator(this,
                new NettyServerHandlerCodeGenerator());
        InnerClassCodeGenerator nettyConnectManageHandler = new InnerClassCodeGenerator(this,
                new NettyConnectManageHandlerCodeGenerator());
        setInnerClass(Arrays.asList(handshakeHandler, nettyServerHandler, nettyConnectManageHandler));
        return super.buildInnerClass();
    }

    private String constructors() {
        return "    public NettyRemotingServer(final NettyServerConfig nettyServerConfig) {\n" +
                "        this(nettyServerConfig, null);\n" +
                "    }\n" +
                "\n" +
                "    public NettyRemotingServer(final NettyServerConfig nettyServerConfig,\n" +
                "                               final ChannelEventListener channelEventListener) {\n" +
                "        super(nettyServerConfig.getServerOnewaySemaphoreValue(), nettyServerConfig.getServerAsyncSemaphoreValue());\n" +
                "        this.serverBootstrap = new ServerBootstrap();\n" +
                "        this.nettyServerConfig = nettyServerConfig;\n" +
                "        this.channelEventListener = channelEventListener;\n" +
                "\n" +
                "        int publicThreadNums = nettyServerConfig.getServerCallbackExecutorThreads();\n" +
                "        if (publicThreadNums <= 0) {\n" +
                "            publicThreadNums = 4;\n" +
                "        }\n" +
                "\n" +
                "        this.publicExecutor = Executors.newFixedThreadPool(publicThreadNums, new ThreadFactory() {\n" +
                "            private AtomicInteger threadIndex = new AtomicInteger(0);\n" +
                "\n" +
                "            @Override\n" +
                "            public Thread newThread(Runnable r) {\n" +
                "                return new Thread(r, \"NettyServerPublicExecutor_\" + this.threadIndex.incrementAndGet());\n" +
                "            }\n" +
                "        });\n" +
                "\n" +
                "        if (useEpoll()) {\n" +
                "            this.eventLoopGroupBoss = new EpollEventLoopGroup(1, new ThreadFactory() {\n" +
                "                private AtomicInteger threadIndex = new AtomicInteger(0);\n" +
                "\n" +
                "                @Override\n" +
                "                public Thread newThread(Runnable r) {\n" +
                "                    return new Thread(r, String.format(\"NettyEPOLLBoss_%d\", this.threadIndex.incrementAndGet()));\n" +
                "                }\n" +
                "            });\n" +
                "\n" +
                "            this.eventLoopGroupSelector = new EpollEventLoopGroup(nettyServerConfig.getServerSelectorThreads(), new ThreadFactory() {\n" +
                "                private AtomicInteger threadIndex = new AtomicInteger(0);\n" +
                "                private int threadTotal = nettyServerConfig.getServerSelectorThreads();\n" +
                "\n" +
                "                @Override\n" +
                "                public Thread newThread(Runnable r) {\n" +
                "                    return new Thread(r, String.format(\"NettyServerEPOLLSelector_%d_%d\", threadTotal, this.threadIndex.incrementAndGet()));\n" +
                "                }\n" +
                "            });\n" +
                "        } else {\n" +
                "            this.eventLoopGroupBoss = new NioEventLoopGroup(1, new ThreadFactory() {\n" +
                "                private AtomicInteger threadIndex = new AtomicInteger(0);\n" +
                "\n" +
                "                @Override\n" +
                "                public Thread newThread(Runnable r) {\n" +
                "                    return new Thread(r, String.format(\"NettyNIOBoss_%d\", this.threadIndex.incrementAndGet()));\n" +
                "                }\n" +
                "            });\n" +
                "\n" +
                "            this.eventLoopGroupSelector = new NioEventLoopGroup(nettyServerConfig.getServerSelectorThreads(), new ThreadFactory() {\n" +
                "                private AtomicInteger threadIndex = new AtomicInteger(0);\n" +
                "                private int threadTotal = nettyServerConfig.getServerSelectorThreads();\n" +
                "\n" +
                "                @Override\n" +
                "                public Thread newThread(Runnable r) {\n" +
                "                    return new Thread(r, String.format(\"NettyServerNIOSelector_%d_%d\", threadTotal, this.threadIndex.incrementAndGet()));\n" +
                "                }\n" +
                "            });\n" +
                "        }\n" +
                "\n" +
                "        loadSslContext();\n" +
                "    }\n\n";
    }

    private String loadSslContext() {
        return "    public void loadSslContext() {\n" +
                "        TlsMode tlsMode = TlsSystemConfig.tlsMode;\n" +
                "        log.info(\"Server is running in TLS {} mode\", tlsMode.getName());\n" +
                "\n" +
                "        if (tlsMode != TlsMode.DISABLED) {\n" +
                "            try {\n" +
                "                sslContext = TlsHelper.buildSslContext(false);\n" +
                "                log.info(\"SSLContext created for server\");\n" +
                "            } catch (CertificateException | IOException e) {\n" +
                "                log.error(\"Failed to create SSLContext for server\", e);\n" +
                "            }\n" +
                "        }\n" +
                "    }\n\n";
    }

    private String useEpoll() {
        return "    private boolean useEpoll() {\n" +
                "        return RemotingUtil.isLinuxPlatform()\n" +
                "                && nettyServerConfig.isUseEpollNativeSelector()\n" +
                "                && Epoll.isAvailable();\n" +
                "    }\n\n";
    }

    private String start() {
        return "    @Override\n" +
                "    public void start() {\n" +
                "        this.defaultEventExecutorGroup = new DefaultEventExecutorGroup(\n" +
                "                nettyServerConfig.getServerWorkerThreads(),\n" +
                "                new ThreadFactory() {\n" +
                "\n" +
                "                    private AtomicInteger threadIndex = new AtomicInteger(0);\n" +
                "\n" +
                "                    @Override\n" +
                "                    public Thread newThread(Runnable r) {\n" +
                "                        return new Thread(r, \"NettyServerCodecThread_\" + this.threadIndex.incrementAndGet());\n" +
                "                    }\n" +
                "                });\n" +
                "\n" +
                "        prepareSharableHandlers();\n" +
                "\n" +
                "        ServerBootstrap childHandler =\n" +
                "                this.serverBootstrap.group(this.eventLoopGroupBoss, this.eventLoopGroupSelector)\n" +
                "                        .channel(useEpoll() ? EpollServerSocketChannel.class : NioServerSocketChannel.class)\n" +
                "                        .option(ChannelOption.SO_BACKLOG, 1024)\n" +
                "                        .option(ChannelOption.SO_REUSEADDR, true)\n" +
                "                        .option(ChannelOption.SO_KEEPALIVE, false)\n" +
                "                        .childOption(ChannelOption.TCP_NODELAY, true)\n" +
                "                        .childOption(ChannelOption.SO_SNDBUF, nettyServerConfig.getServerSocketSndBufSize())\n" +
                "                        .childOption(ChannelOption.SO_RCVBUF, nettyServerConfig.getServerSocketRcvBufSize())\n" +
                "                        .localAddress(new InetSocketAddress(this.nettyServerConfig.getListenPort()))\n" +
                "                        .childHandler(new ChannelInitializer<SocketChannel>() {\n" +
                "                            @Override\n" +
                "                            public void initChannel(SocketChannel ch) throws Exception {\n" +
                "                                ch.pipeline()\n" +
                "                                        .addLast(defaultEventExecutorGroup, HANDSHAKE_HANDLER_NAME, handshakeHandler)\n" +
                "                                        .addLast(defaultEventExecutorGroup,\n" +
                "                                                encoder,\n" +
                "                                                new NettyDecoder(),\n" +
                "                                                new IdleStateHandler(0, 0, nettyServerConfig.getServerChannelMaxIdleTimeSeconds()),\n" +
                "                                                connectionManageHandler,\n" +
                "                                                serverHandler\n" +
                "                                        );\n" +
                "                            }\n" +
                "                        });\n" +
                "\n" +
                "        if (nettyServerConfig.isServerPooledByteBufAllocatorEnable()) {\n" +
                "            childHandler.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);\n" +
                "        }\n" +
                "\n" +
                "        try {\n" +
                "            ChannelFuture sync = this.serverBootstrap.bind().sync();\n" +
                "            InetSocketAddress addr = (InetSocketAddress) sync.channel().localAddress();\n" +
                "            this.port = addr.getPort();\n" +
                "        } catch (InterruptedException e1) {\n" +
                "            throw new RuntimeException(\"this.serverBootstrap.bind().sync() InterruptedException\", e1);\n" +
                "        }\n" +
                "\n" +
                "        if (this.channelEventListener != null) {\n" +
                "            this.nettyEventExecutor.start();\n" +
                "        }\n" +
                "\n" +
                "        this.timer.scheduleAtFixedRate(() -> {\n" +
                "            try {\n" +
                "                NettyRemotingServer.this.scanResponseTable();\n" +
                "            } catch (Throwable e) {\n" +
                "                log.error(\"scanResponseTable exception\", e);\n" +
                "            }\n" +
                "        }, 1000 * 3, 1000, TimeUnit.MILLISECONDS);\n" +
                "    }\n\n";
    }

    private String shutdown() {
        return "    @Override\n" +
                "    public void shutdown() {\n" +
                "        try {\n" +
                "            this.timer.shutdown();\n" +
                "            this.eventLoopGroupBoss.shutdownGracefully();\n" +
                "            this.eventLoopGroupSelector.shutdownGracefully();\n" +
                "            this.nettyEventExecutor.shutdown();\n" +
                "\n" +
                "            if (this.defaultEventExecutorGroup != null) {\n" +
                "                this.defaultEventExecutorGroup.shutdownGracefully();\n" +
                "            }\n" +
                "        } catch (Exception e) {\n" +
                "            log.error(\"NettyRemotingServer shutdown exception, \", e);\n" +
                "        }\n" +
                "\n" +
                "        if (this.publicExecutor != null) {\n" +
                "            try {\n" +
                "                this.publicExecutor.shutdown();\n" +
                "            } catch (Exception e) {\n" +
                "                log.error(\"NettyRemotingServer shutdown exception, \", e);\n" +
                "            }\n" +
                "        }\n" +
                "    }\n\n";
    }

    private String registerRPCHook() {
        return "    @Override\n" +
                "    public void registerRPCHook(RPCHook rpcHook) {\n" +
                "        if (rpcHook != null && !rpcHooks.contains(rpcHook)) {\n" +
                "            rpcHooks.add(rpcHook);\n" +
                "        }\n" +
                "    }\n\n";
    }

    private String registerProcessor() {
        return "    @Override\n" +
                "    public void registerProcessor(int requestCode, NettyRequestProcessor processor, ExecutorService executor) {\n" +
                "        ExecutorService executorThis = executor;\n" +
                "        if (null == executor) {\n" +
                "            executorThis = this.publicExecutor;\n" +
                "        }\n" +
                "\n" +
                "        Pair<NettyRequestProcessor, ExecutorService> pair = new Pair<>(processor, executorThis);\n" +
                "        this.processorTable.put(requestCode, pair);\n" +
                "    }\n\n";
    }

    private String registerDefaultProcessor() {
        return "    @Override\n" +
                "    public void registerDefaultProcessor(NettyRequestProcessor processor, ExecutorService executor) {\n" +
                "        this.defaultRequestProcessor = new Pair<>(processor, executor);\n" +
                "    }\n\n";
    }

    private String localListenPort() {
        return "    @Override\n" +
                "    public int localListenPort() {\n" +
                "        return this.port;\n" +
                "    }\n\n";
    }

    private String getProcessorPair() {
        return "    @Override\n" +
                "    public Pair<NettyRequestProcessor, ExecutorService> getProcessorPair(int requestCode) {\n" +
                "        return processorTable.get(requestCode);\n" +
                "    }\n\n";
    }

    private String invoke() {
        return "    @Override\n" +
                "    public RemotingCommand invokeSync(final Channel channel, final RemotingCommand request, final long timeoutMillis)\n" +
                "            throws InterruptedException, RemotingSendRequestException, RemotingTimeoutException {\n" +
                "        return this.invokeSyncImpl(channel, request, timeoutMillis);\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public void invokeAsync(Channel channel, RemotingCommand request, long timeoutMillis, InvokeCallback invokeCallback)\n" +
                "            throws InterruptedException, RemotingTooMuchRequestException, RemotingTimeoutException, RemotingSendRequestException {\n" +
                "        this.invokeAsyncImpl(channel, request, timeoutMillis, invokeCallback);\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public void invokeOneway(Channel channel, RemotingCommand request, long timeoutMillis) throws InterruptedException,\n" +
                "            RemotingTooMuchRequestException, RemotingTimeoutException, RemotingSendRequestException {\n" +
                "        this.invokeOnewayImpl(channel, request, timeoutMillis);\n" +
                "    }\n\n";
    }

    private String get() {
        return "    @Override\n" +
                "    public ChannelEventListener getChannelEventListener() {\n" +
                "        return channelEventListener;\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    @Override\n" +
                "    public ExecutorService getCallbackExecutor() {\n" +
                "        return this.publicExecutor;\n" +
                "    }\n\n";
    }

    private String prepareSharableHandlers() {
        return "    private void prepareSharableHandlers() {\n" +
                "        handshakeHandler = new HandshakeHandler(TlsSystemConfig.tlsMode);\n" +
                "        encoder = new NettyEncoder();\n" +
                "        connectionManageHandler = new NettyConnectManageHandler();\n" +
                "        serverHandler = new NettyServerHandler();\n" +
                "    }\n\n";
    }



    private static class HandshakeHandlerCodeGenerator extends ClassCodeGenerator {
        private static final String CLASS_NAME = "HandshakeHandler";
        private static final String PARENT_NAME = "SimpleChannelInboundHandler<ByteBuf>";

        HandshakeHandlerCodeGenerator() {
            super(CLASS_NAME, PARENT_NAME);
            setModifier(PACKAGE);

            List<FieldInfo> fields = new ArrayList<>(2);
            fields.add(new FieldInfo(PRIVATE_FINAL, "TlsMode", " tlsMode"));
            fields.add(new FieldInfo(PRIVATE_STATIC_FINAL, "byte", "HANDSHAKE_MAGIC_CODE", "0x16"));
            setFields(fields);
        }

        @Override
        public String buildClassComment() {
            return "@ChannelHandler.Sharable\n";
        }

        @Override
        public String buildMethods() {
            return "    HandshakeHandler(TlsMode tlsMode) {\n" +
                    "        this.tlsMode = tlsMode;\n" +
                    "    }\n" +
                    "\n" +
                    "    @Override\n" +
                    "    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {\n" +
                    "\n" +
                    "        // mark the current position so that we can peek the first byte to determine if the content is starting with\n" +
                    "        // TLS handshake\n" +
                    "        msg.markReaderIndex();\n" +
                    "\n" +
                    "        byte b = msg.getByte(0);\n" +
                    "\n" +
                    "        if (b == HANDSHAKE_MAGIC_CODE) {\n" +
                    "            switch (tlsMode) {\n" +
                    "                case DISABLED:\n" +
                    "                    ctx.close();\n" +
                    "                    log.warn(\"Clients intend to establish a SSL connection while this server is running in SSL disabled mode\");\n" +
                    "                    break;\n" +
                    "                case PERMISSIVE:\n" +
                    "                case ENFORCING:\n" +
                    "                    if (null != sslContext) {\n" +
                    "                        ctx.pipeline()\n" +
                    "                                .addAfter(defaultEventExecutorGroup, HANDSHAKE_HANDLER_NAME, TLS_HANDLER_NAME, sslContext.newHandler(ctx.channel().alloc()))\n" +
                    "                                .addAfter(defaultEventExecutorGroup, TLS_HANDLER_NAME, FILE_REGION_ENCODER_NAME, new FileRegionEncoder());\n" +
                    "                        log.info(\"Handlers prepended to channel pipeline to establish SSL connection\");\n" +
                    "                    } else {\n" +
                    "                        ctx.close();\n" +
                    "                        log.error(\"Trying to establish a SSL connection but sslContext is null\");\n" +
                    "                    }\n" +
                    "                    break;\n" +
                    "\n" +
                    "                default:\n" +
                    "                    log.warn(\"Unknown TLS mode\");\n" +
                    "                    break;\n" +
                    "            }\n" +
                    "        } else if (tlsMode == TlsMode.ENFORCING) {\n" +
                    "            ctx.close();\n" +
                    "            log.warn(\"Clients intend to establish an insecure connection while this server is running in SSL enforcing mode\");\n" +
                    "        }\n" +
                    "\n" +
                    "        // reset the reader index so that handshake negotiation may proceed as normal.\n" +
                    "        msg.resetReaderIndex();\n" +
                    "\n" +
                    "        try {\n" +
                    "            // Remove this handler\n" +
                    "            ctx.pipeline().remove(this);\n" +
                    "        } catch (NoSuchElementException e) {\n" +
                    "            log.error(\"Error while removing HandshakeHandler\", e);\n" +
                    "        }\n" +
                    "\n" +
                    "        // Hand over this message to the next .\n" +
                    "        ctx.fireChannelRead(msg.retain());\n" +
                    "    }\n";
        }
    }

    private static class NettyServerHandlerCodeGenerator extends ClassCodeGenerator {
        private static final String CLASS_NAME = "NettyServerHandler";
        private static final String PARENT_NAME = "SimpleChannelInboundHandler<RemotingCommand>";

        NettyServerHandlerCodeGenerator() {
            super(CLASS_NAME, PARENT_NAME);
            setModifier(PACKAGE);
        }

        @Override
        public String buildClassComment() {
            return "@ChannelHandler.Sharable\n";
        }

        @Override
        public String buildMethods() {
            return "    @Override\n" +
                    "    protected void channelRead0(ChannelHandlerContext ctx, RemotingCommand msg) throws Exception {\n" +
                    "        processMessageReceived(ctx, msg);\n" +
                    "    }\n";
        }
    }

    private static class NettyConnectManageHandlerCodeGenerator extends ClassCodeGenerator {
        private static final String CLASS_NAME = "NettyConnectManageHandler";
        private static final String PARENT_NAME = "ChannelDuplexHandler";

        NettyConnectManageHandlerCodeGenerator() {
            super(CLASS_NAME, PARENT_NAME);
            setModifier(PACKAGE);
        }

        @Override
        public String buildClassComment() {
            return "@ChannelHandler.Sharable\n";
        }

        @Override
        public String buildMethods() {
            return "    @Override\n" +
                    "    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {\n" +
                    "        final String remoteAddress = RemotingHelper.parseChannelRemoteAddr(ctx.channel());\n" +
                    "        log.info(\"NETTY SERVER PIPELINE: channelRegistered {}\", remoteAddress);\n" +
                    "        super.channelRegistered(ctx);\n" +
                    "    }\n" +
                    "\n" +
                    "    @Override\n" +
                    "    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {\n" +
                    "        final String remoteAddress = RemotingHelper.parseChannelRemoteAddr(ctx.channel());\n" +
                    "        log.info(\"NETTY SERVER PIPELINE: channelUnregistered, the channel[{}]\", remoteAddress);\n" +
                    "        super.channelUnregistered(ctx);\n" +
                    "    }\n" +
                    "\n" +
                    "    @Override\n" +
                    "    public void channelActive(ChannelHandlerContext ctx) throws Exception {\n" +
                    "        final String remoteAddress = RemotingHelper.parseChannelRemoteAddr(ctx.channel());\n" +
                    "        log.info(\"NETTY SERVER PIPELINE: channelActive, the channel[{}]\", remoteAddress);\n" +
                    "        super.channelActive(ctx);\n" +
                    "\n" +
                    "        if (NettyRemotingServer.this.channelEventListener != null) {\n" +
                    "            NettyRemotingServer.this.putNettyEvent(new NettyEvent(NettyEventType.CONNECT, remoteAddress, ctx.channel()));\n" +
                    "        }\n" +
                    "    }\n" +
                    "\n" +
                    "    @Override\n" +
                    "    public void channelInactive(ChannelHandlerContext ctx) throws Exception {\n" +
                    "        final String remoteAddress = RemotingHelper.parseChannelRemoteAddr(ctx.channel());\n" +
                    "        log.info(\"NETTY SERVER PIPELINE: channelInactive, the channel[{}]\", remoteAddress);\n" +
                    "        super.channelInactive(ctx);\n" +
                    "\n" +
                    "        if (NettyRemotingServer.this.channelEventListener != null) {\n" +
                    "            NettyRemotingServer.this.putNettyEvent(new NettyEvent(NettyEventType.CLOSE, remoteAddress, ctx.channel()));\n" +
                    "        }\n" +
                    "    }\n" +
                    "\n" +
                    "    @Override\n" +
                    "    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {\n" +
                    "        if (evt instanceof IdleStateEvent) {\n" +
                    "            IdleStateEvent event = (IdleStateEvent) evt;\n" +
                    "            if (event.state().equals(IdleState.ALL_IDLE)) {\n" +
                    "                final String remoteAddress = RemotingHelper.parseChannelRemoteAddr(ctx.channel());\n" +
                    "                log.warn(\"NETTY SERVER PIPELINE: IDLE exception [{}]\", remoteAddress);\n" +
                    "                RemotingUtil.closeChannel(ctx.channel());\n" +
                    "                if (NettyRemotingServer.this.channelEventListener != null) {\n" +
                    "                    NettyRemotingServer.this\n" +
                    "                            .putNettyEvent(new NettyEvent(NettyEventType.IDLE, remoteAddress, ctx.channel()));\n" +
                    "                }\n" +
                    "            }\n" +
                    "        }\n" +
                    "\n" +
                    "        ctx.fireUserEventTriggered(evt);\n" +
                    "    }\n" +
                    "\n" +
                    "    @Override\n" +
                    "    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {\n" +
                    "        final String remoteAddress = RemotingHelper.parseChannelRemoteAddr(ctx.channel());\n" +
                    "        log.warn(\"NETTY SERVER PIPELINE: exceptionCaught {}\", remoteAddress);\n" +
                    "        log.warn(\"NETTY SERVER PIPELINE: exceptionCaught exception.\", cause);\n" +
                    "\n" +
                    "        if (NettyRemotingServer.this.channelEventListener != null) {\n" +
                    "            NettyRemotingServer.this.putNettyEvent(new NettyEvent(NettyEventType.EXCEPTION, remoteAddress, ctx.channel()));\n" +
                    "        }\n" +
                    "\n" +
                    "        RemotingUtil.closeChannel(ctx.channel());\n" +
                    "    }\n";
        }
    }


}
