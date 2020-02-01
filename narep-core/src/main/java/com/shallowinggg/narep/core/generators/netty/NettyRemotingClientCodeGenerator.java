package com.shallowinggg.narep.core.generators.netty;

import com.shallowinggg.narep.core.annotation.Generator;
import com.shallowinggg.narep.core.common.CodeGeneratorHelper;
import com.shallowinggg.narep.core.generators.ClassCodeGenerator;
import com.shallowinggg.narep.core.generators.InnerClassCodeGenerator;
import com.shallowinggg.narep.core.lang.FieldInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.shallowinggg.narep.core.lang.Modifier.*;

/**
 * @author shallowinggg
 */
@Generator
public class NettyRemotingClientCodeGenerator extends ClassCodeGenerator {
    private static final String CLASS_NAME = "NettyRemotingClient";
    private static final String PARENT_NAME = "AbstractNettyRemoting";
    private static final String[] INTERFACE_NAMES = new String[]{"RemotingClient"};
    private static final String SUB_PACKAGE = "netty";
    private static final List<String> DEPENDENCY_NAMES = Arrays.asList("ChannelEventListener",
            "InvokeCallback", "RPCHook", "RemotingClient.java", "Pair",
            "RemotingHelper", "RemotingUtil", "RemotingCommand",
            "RemotingSendRequestException", "RemotingTimeoutException",
            "RemotingTooMuchRequestException", "RemotingConnectException");

    public NettyRemotingClientCodeGenerator() {
        super(CLASS_NAME, PARENT_NAME, SUB_PACKAGE, INTERFACE_NAMES);
        setDependencyNames(DEPENDENCY_NAMES);

        List<FieldInfo> fields = new ArrayList<>(16);
        fields.add(new FieldInfo(PRIVATE_STATIC_FINAL, "Logger", "log", CodeGeneratorHelper.buildLoggerField(CLASS_NAME)));
        fields.add(new FieldInfo(PRIVATE_STATIC_FINAL, "long", "LOCK_TIMEOUT_MILLIS", "3000"));

        fields.add(new FieldInfo(PRIVATE_FINAL, "NettyClientConfig", "nettyClientConfig"));
        fields.add(new FieldInfo(PRIVATE_FINAL, "Bootstrap", "bootstrap", "new Bootstrap()"));
        fields.add(new FieldInfo(PRIVATE_FINAL, "EventLoopGroup", "eventLoopGroupWorker"));
        fields.add(new FieldInfo(PRIVATE_FINAL, "Lock", "lockChannelTables"));
        fields.add(new FieldInfo(PRIVATE_FINAL, "ConcurrentMap<String, ChannelWrapper>", "channelTables", "new ConcurrentHashMap<>()"));
        fields.add(new FieldInfo(PRIVATE_FINAL, "ScheduledExecutorService", "timer",
                "Executors.newSingleThreadScheduledExecutor(r -> {\n" +
                        "        Thread t = new Thread(r, \"ClientHouseKeepingService\");\n" +
                        "        t.setDaemon(true);\n" +
                        "        return t;\n" +
                        "    })"));
        fields.add(new FieldInfo(PRIVATE_FINAL, "AtomicReference<List<String>>", "namesrvAddrList", "new AtomicReference<>()"));
        fields.add(new FieldInfo(PRIVATE_FINAL, "AtomicReference<String>", "namesrvAddrChoosed", "new AtomicReference<>()"));
        fields.add(new FieldInfo(PRIVATE_FINAL, "AtomicInteger", "namesrvIndex", "new AtomicInteger(initValueIndex())"));
        fields.add(new FieldInfo(PRIVATE_FINAL, "Lock", "lockNamesrvChannel", "new ReentrantLock()"));
        fields.add(new FieldInfo(PRIVATE_FINAL, "ExecutorService", "publicExecutor"));

        fields.add(new FieldInfo(PRIVATE, "ExecutorService", "callbackExecutor"));
        fields.add(new FieldInfo(PRIVATE_FINAL, "ChannelEventListener", "channelEventListener"));
        fields.add(new FieldInfo(PRIVATE, "DefaultEventExecutorGroup", "defaultEventExecutorGroup"));
        setFields(fields);
    }

    @Override
    public String buildMethods() {
        return constructors() +
                initValueIndex() +
                start() +
                shutdown() +
                closeChannel() +
                registerRPCHook() +
                closeChannel2() +
                updateNameServerAddressList() +
                invokeSync() +
                getAndCreateChannel() +
                getAndCreateNameServerChannel() +
                createChannel() +
                invokeAsync() +
                invokeOneway() +
                registerProcessor() +
                isChannelWritable() +
                getters() +
                setters() +
                "\n";
    }

    @Override
    public String buildInnerClass() {
        InnerClassCodeGenerator channelWrapper = new InnerClassCodeGenerator(this,
                new ChannelWrapperCodeGenerator());
        InnerClassCodeGenerator nettyClientHandler = new InnerClassCodeGenerator(this,
                new NettyClientHandlerCodeGenerator());
        InnerClassCodeGenerator nettyConnectManageHandler = new InnerClassCodeGenerator(this,
                new NettyConnectManageHandlerCodeGenerator());
        setInnerClass(Arrays.asList(channelWrapper, nettyClientHandler, nettyConnectManageHandler));
        return super.buildInnerClass();
    }

    private String constructors() {
        return "    public NettyRemotingClient(final NettyClientConfig nettyClientConfig) {\n" +
                "        this(nettyClientConfig, null);\n" +
                "    }\n" +
                "\n" +
                "    public NettyRemotingClient(final NettyClientConfig nettyClientConfig,\n" +
                "                               final ChannelEventListener channelEventListener) {\n" +
                "        super(nettyClientConfig.getClientOnewaySemaphoreValue(), nettyClientConfig.getClientAsyncSemaphoreValue());\n" +
                "        this.nettyClientConfig = nettyClientConfig;\n" +
                "        this.channelEventListener = channelEventListener;\n" +
                "\n" +
                "        int publicThreadNums = nettyClientConfig.getClientCallbackExecutorThreads();\n" +
                "        if (publicThreadNums <= 0) {\n" +
                "            publicThreadNums = 4;\n" +
                "        }\n" +
                "\n" +
                "        this.publicExecutor = Executors.newFixedThreadPool(publicThreadNums, new ThreadFactory() {\n" +
                "            private AtomicInteger threadIndex = new AtomicInteger(0);\n" +
                "\n" +
                "            @Override\n" +
                "            public Thread newThread(Runnable r) {\n" +
                "                return new Thread(r, \"NettyClientPublicExecutor_\" + this.threadIndex.incrementAndGet());\n" +
                "            }\n" +
                "        });\n" +
                "\n" +
                "        this.eventLoopGroupWorker = new NioEventLoopGroup(1, new ThreadFactory() {\n" +
                "            private AtomicInteger threadIndex = new AtomicInteger(0);\n" +
                "\n" +
                "            @Override\n" +
                "            public Thread newThread(Runnable r) {\n" +
                "                return new Thread(r, String.format(\"NettyClientSelector_%d\", this.threadIndex.incrementAndGet()));\n" +
                "            }\n" +
                "        });\n" +
                "\n" +
                "        if (nettyClientConfig.isUseTLS()) {\n" +
                "            try {\n" +
                "                sslContext = TlsHelper.buildSslContext(true);\n" +
                "                log.info(\"SSL enabled for client\");\n" +
                "            } catch (IOException e) {\n" +
                "                log.error(\"Failed to create SSLContext\", e);\n" +
                "            } catch (CertificateException e) {\n" +
                "                log.error(\"Failed to create SSLContext\", e);\n" +
                "                throw new RuntimeException(\"Failed to create SSLContext\", e);\n" +
                "            }\n" +
                "        }\n" +
                "    }\n\n";
    }

    private String initValueIndex() {
        return "    private static int initValueIndex() {\n" +
                "        Random r = new Random();\n" +
                "        return Math.abs(r.nextInt() % 999) % 999;\n" +
                "    }\n\n";
    }

    private String start() {
        return "    @Override\n" +
                "    public void start() {\n" +
                "        this.defaultEventExecutorGroup = new DefaultEventExecutorGroup(\n" +
                "                nettyClientConfig.getClientWorkerThreads(),\n" +
                "                new ThreadFactory() {\n" +
                "                    private AtomicInteger threadIndex = new AtomicInteger(0);\n" +
                "\n" +
                "                    @Override\n" +
                "                    public Thread newThread(Runnable r) {\n" +
                "                        return new Thread(r, \"NettyClientWorkerThread_\" + this.threadIndex.incrementAndGet());\n" +
                "                    }\n" +
                "                });\n" +
                "\n" +
                "        this.bootstrap.group(this.eventLoopGroupWorker).channel(NioSocketChannel.class)\n" +
                "                .option(ChannelOption.TCP_NODELAY, true)\n" +
                "                .option(ChannelOption.SO_KEEPALIVE, false)\n" +
                "                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, nettyClientConfig.getConnectTimeoutMillis())\n" +
                "                .option(ChannelOption.SO_SNDBUF, nettyClientConfig.getClientSocketSndBufSize())\n" +
                "                .option(ChannelOption.SO_RCVBUF, nettyClientConfig.getClientSocketRcvBufSize())\n" +
                "                .handler(new ChannelInitializer<SocketChannel>() {\n" +
                "                    @Override\n" +
                "                    public void initChannel(SocketChannel ch) throws Exception {\n" +
                "                        ChannelPipeline pipeline = ch.pipeline();\n" +
                "                        if (nettyClientConfig.isUseTLS()) {\n" +
                "                            if (null != sslContext) {\n" +
                "                                pipeline.addFirst(defaultEventExecutorGroup, \"sslHandler\", sslContext.newHandler(ch.alloc()));\n" +
                "                                log.info(\"Prepend SSL handler\");\n" +
                "                            } else {\n" +
                "                                log.warn(\"Connections are insecure as SSLContext is null!\");\n" +
                "                            }\n" +
                "                        }\n" +
                "                        pipeline.addLast(\n" +
                "                                defaultEventExecutorGroup,\n" +
                "                                new NettyEncoder(),\n" +
                "                                new NettyDecoder(),\n" +
                "                                new IdleStateHandler(0, 0, nettyClientConfig.getClientChannelMaxIdleTimeSeconds()),\n" +
                "                                new NettyConnectManageHandler(),\n" +
                "                                new NettyClientHandler());\n" +
                "                    }\n" +
                "                });\n" +
                "\n" +
                "        this.timer.scheduleAtFixedRate(() -> {\n" +
                "            try {\n" +
                "                NettyRemotingClient.this.scanResponseTable();\n" +
                "            } catch (Throwable e) {\n" +
                "                log.error(\"scanResponseTable exception\", e);\n" +
                "            }\n" +
                "        }, 1000 * 3, 1000, TimeUnit.MILLISECONDS);\n" +
                "\n" +
                "        if (this.channelEventListener != null) {\n" +
                "            this.nettyEventExecutor.start();\n" +
                "        }\n" +
                "    }\n\n";
    }

    private String shutdown() {
        return "    @Override\n" +
                "    public void shutdown() {\n" +
                "        try {\n" +
                "            this.timer.shutdown();\n" +
                "            for (ChannelWrapper cw : this.channelTables.values()) {\n" +
                "                this.closeChannel(null, cw.getChannel());\n" +
                "            }\n" +
                "            this.channelTables.clear();\n" +
                "            this.eventLoopGroupWorker.shutdownGracefully();\n" +
                "            this.nettyEventExecutor.shutdown();\n" +
                "\n" +
                "            if (this.defaultEventExecutorGroup != null) {\n" +
                "                this.defaultEventExecutorGroup.shutdownGracefully();\n" +
                "            }\n" +
                "        } catch (Exception e) {\n" +
                "            log.error(\"NettyRemotingClient shutdown exception, \", e);\n" +
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

    private String closeChannel() {
        return "    public void closeChannel(final String addr, final Channel channel) {\n" +
                "        if (null == channel) {\n" +
                "            return;\n" +
                "        }\n" +
                "\n" +
                "        final String addrRemote = null == addr ? RemotingHelper.parseChannelRemoteAddr(channel) : addr;\n" +
                "\n" +
                "        try {\n" +
                "            if (this.lockChannelTables.tryLock(LOCK_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)) {\n" +
                "                try {\n" +
                "                    boolean removeItemFromTable = true;\n" +
                "                    final ChannelWrapper prevCW = this.channelTables.get(addrRemote);\n" +
                "\n" +
                "                    log.info(\"closeChannel: begin close the channel[{}] Found: {}\", addrRemote, prevCW != null);\n" +
                "\n" +
                "                    if (null == prevCW) {\n" +
                "                        log.info(\"closeChannel: the channel[{}] has been removed from the channel table before\", addrRemote);\n" +
                "                        removeItemFromTable = false;\n" +
                "                    } else if (prevCW.getChannel() != channel) {\n" +
                "                        log.info(\"closeChannel: the channel[{}] has been closed before, and has been created again, nothing to do.\",\n" +
                "                                addrRemote);\n" +
                "                        removeItemFromTable = false;\n" +
                "                    }\n" +
                "\n" +
                "                    if (removeItemFromTable) {\n" +
                "                        this.channelTables.remove(addrRemote);\n" +
                "                        log.info(\"closeChannel: the channel[{}] was removed from channel table\", addrRemote);\n" +
                "                    }\n" +
                "\n" +
                "                    RemotingUtil.closeChannel(channel);\n" +
                "                } catch (Exception e) {\n" +
                "                    log.error(\"closeChannel: close the channel exception\", e);\n" +
                "                } finally {\n" +
                "                    this.lockChannelTables.unlock();\n" +
                "                }\n" +
                "            } else {\n" +
                "                log.warn(\"closeChannel: try to lock channel table, but timeout, {}ms\", LOCK_TIMEOUT_MILLIS);\n" +
                "            }\n" +
                "        } catch (InterruptedException e) {\n" +
                "            log.error(\"closeChannel exception\", e);\n" +
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

    private String closeChannel2() {
        return "    public void closeChannel(final Channel channel) {\n" +
                "        if (null == channel) {\n" +
                "            return;\n" +
                "        }\n" +
                "\n" +
                "        try {\n" +
                "            if (this.lockChannelTables.tryLock(LOCK_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)) {\n" +
                "                try {\n" +
                "                    boolean removeItemFromTable = true;\n" +
                "                    ChannelWrapper prevCW = null;\n" +
                "                    String addrRemote = null;\n" +
                "                    for (Map.Entry<String, ChannelWrapper> entry : channelTables.entrySet()) {\n" +
                "                        String key = entry.getKey();\n" +
                "                        ChannelWrapper prev = entry.getValue();\n" +
                "                        if (prev.getChannel() != null) {\n" +
                "                            if (prev.getChannel() == channel) {\n" +
                "                                prevCW = prev;\n" +
                "                                addrRemote = key;\n" +
                "                                break;\n" +
                "                            }\n" +
                "                        }\n" +
                "                    }\n" +
                "\n" +
                "                    if (null == prevCW) {\n" +
                "                        log.info(\"eventCloseChannel: the channel[{}] has been removed from the channel table before\", addrRemote);\n" +
                "                        removeItemFromTable = false;\n" +
                "                    }\n" +
                "\n" +
                "                    if (removeItemFromTable) {\n" +
                "                        this.channelTables.remove(addrRemote);\n" +
                "                        log.info(\"closeChannel: the channel[{}] was removed from channel table\", addrRemote);\n" +
                "                        RemotingUtil.closeChannel(channel);\n" +
                "                    }\n" +
                "                } catch (Exception e) {\n" +
                "                    log.error(\"closeChannel: close the channel exception\", e);\n" +
                "                } finally {\n" +
                "                    this.lockChannelTables.unlock();\n" +
                "                }\n" +
                "            } else {\n" +
                "                log.warn(\"closeChannel: try to lock channel table, but timeout, {}ms\", LOCK_TIMEOUT_MILLIS);\n" +
                "            }\n" +
                "        } catch (InterruptedException e) {\n" +
                "            log.error(\"closeChannel exception\", e);\n" +
                "        }\n" +
                "    }\n\n";
    }

    private String updateNameServerAddressList() {
        return "    @Override\n" +
                "    public void updateNameServerAddressList(List<String> addrs) {\n" +
                "        List<String> old = this.namesrvAddrList.get();\n" +
                "        boolean update = false;\n" +
                "\n" +
                "        if (!addrs.isEmpty()) {\n" +
                "            if (null == old) {\n" +
                "                update = true;\n" +
                "            } else if (addrs.size() != old.size()) {\n" +
                "                update = true;\n" +
                "            } else {\n" +
                "                for (String addr : addrs) {\n" +
                "                    if (!old.contains(addr)) {\n" +
                "                        update = true;\n" +
                "                        break;\n" +
                "                    }\n" +
                "                }\n" +
                "            }\n" +
                "\n" +
                "            if (update) {\n" +
                "                Collections.shuffle(addrs);\n" +
                "                log.info(\"name server address updated. NEW : {} , OLD: {}\", addrs, old);\n" +
                "                this.namesrvAddrList.set(addrs);\n" +
                "            }\n" +
                "        }\n" +
                "    }\n\n";
    }

    private String invokeSync() {
        return "    @Override\n" +
                "    public RemotingCommand invokeSync(String addr, final RemotingCommand request, long timeoutMillis)\n" +
                "            throws InterruptedException, RemotingConnectException, RemotingSendRequestException, RemotingTimeoutException {\n" +
                "        long beginStartTime = System.currentTimeMillis();\n" +
                "\n" +
                "        final Channel channel = this.getAndCreateChannel(addr);\n" +
                "        if (channel != null && channel.isActive()) {\n" +
                "            try {\n" +
                "                doBeforeRpcHooks(addr, request);\n" +
                "                long costTime = System.currentTimeMillis() - beginStartTime;\n" +
                "                if (timeoutMillis < costTime) {\n" +
                "                    throw new RemotingTimeoutException(\"invokeSync call timeout\");\n" +
                "                }\n" +
                "                RemotingCommand response = this.invokeSyncImpl(channel, request, timeoutMillis - costTime);\n" +
                "                doAfterRpcHooks(RemotingHelper.parseChannelRemoteAddr(channel), request, response);\n" +
                "                return response;\n" +
                "            } catch (RemotingSendRequestException e) {\n" +
                "                log.warn(\"invokeSync: send request exception, so close the channel[{}]\", addr);\n" +
                "                this.closeChannel(addr, channel);\n" +
                "                throw e;\n" +
                "            } catch (RemotingTimeoutException e) {\n" +
                "                if (nettyClientConfig.isClientCloseSocketIfTimeout()) {\n" +
                "                    this.closeChannel(addr, channel);\n" +
                "                    log.warn(\"invokeSync: close socket because of timeout, {}ms, {}\", timeoutMillis, addr);\n" +
                "                }\n" +
                "                log.warn(\"invokeSync: wait response timeout exception, the channel[{}]\", addr);\n" +
                "                throw e;\n" +
                "            }\n" +
                "        } else {\n" +
                "            this.closeChannel(addr, channel);\n" +
                "            throw new RemotingConnectException(addr);\n" +
                "        }\n" +
                "    }\n\n";
    }

    private String getAndCreateChannel() {
        return "    private Channel getAndCreateChannel(final String addr) throws InterruptedException {\n" +
                "        if (null == addr) {\n" +
                "            return getAndCreateNameServerChannel();\n" +
                "        }\n" +
                "\n" +
                "        ChannelWrapper cw = this.channelTables.get(addr);\n" +
                "        if (cw != null && cw.isOK()) {\n" +
                "            return cw.getChannel();\n" +
                "        }\n" +
                "\n" +
                "        return this.createChannel(addr);\n" +
                "    }\n\n";
    }

    private String getAndCreateNameServerChannel() {
        return "    private Channel getAndCreateNameServerChannel() throws InterruptedException {\n" +
                "        String addr = this.namesrvAddrChoosed.get();\n" +
                "        if (addr != null) {\n" +
                "            ChannelWrapper cw = this.channelTables.get(addr);\n" +
                "            if (cw != null && cw.isOK()) {\n" +
                "                return cw.getChannel();\n" +
                "            }\n" +
                "        }\n" +
                "\n" +
                "        final List<String> addrList = this.namesrvAddrList.get();\n" +
                "        if (this.lockNamesrvChannel.tryLock(LOCK_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)) {\n" +
                "            try {\n" +
                "                addr = this.namesrvAddrChoosed.get();\n" +
                "                if (addr != null) {\n" +
                "                    ChannelWrapper cw = this.channelTables.get(addr);\n" +
                "                    if (cw != null && cw.isOK()) {\n" +
                "                        return cw.getChannel();\n" +
                "                    }\n" +
                "                }\n" +
                "\n" +
                "                if (addrList != null && !addrList.isEmpty()) {\n" +
                "                    for (int i = 0; i < addrList.size(); i++) {\n" +
                "                        int index = this.namesrvIndex.incrementAndGet();\n" +
                "                        index = Math.abs(index);\n" +
                "                        index = index % addrList.size();\n" +
                "                        String newAddr = addrList.get(index);\n" +
                "\n" +
                "                        this.namesrvAddrChoosed.set(newAddr);\n" +
                "                        log.info(\"new name server is chosen. OLD: {} , NEW: {}. namesrvIndex = {}\", addr, newAddr, namesrvIndex);\n" +
                "                        Channel channelNew = this.createChannel(newAddr);\n" +
                "                        if (channelNew != null) {\n" +
                "                            return channelNew;\n" +
                "                        }\n" +
                "                    }\n" +
                "                }\n" +
                "            } catch (Exception e) {\n" +
                "                log.error(\"getAndCreateNameserverChannel: create name server channel exception\", e);\n" +
                "            } finally {\n" +
                "                this.lockNamesrvChannel.unlock();\n" +
                "            }\n" +
                "        } else {\n" +
                "            log.warn(\"getAndCreateNameserverChannel: try to lock name server, but timeout, {}ms\", LOCK_TIMEOUT_MILLIS);\n" +
                "        }\n" +
                "\n" +
                "        return null;\n" +
                "    }\n\n";
    }

    private String createChannel() {
        return "    private Channel createChannel(final String addr) throws InterruptedException {\n" +
                "        ChannelWrapper cw = this.channelTables.get(addr);\n" +
                "        if (cw != null && cw.isOK()) {\n" +
                "            cw.getChannel().close();\n" +
                "            channelTables.remove(addr);\n" +
                "        }\n" +
                "\n" +
                "        if (this.lockChannelTables.tryLock(LOCK_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)) {\n" +
                "            try {\n" +
                "                boolean createNewConnection;\n" +
                "                cw = this.channelTables.get(addr);\n" +
                "                if (cw != null) {\n" +
                "\n" +
                "                    if (cw.isOK()) {\n" +
                "                        cw.getChannel().close();\n" +
                "                        this.channelTables.remove(addr);\n" +
                "                        createNewConnection = true;\n" +
                "                    } else if (!cw.getChannelFuture().isDone()) {\n" +
                "                        createNewConnection = false;\n" +
                "                    } else {\n" +
                "                        this.channelTables.remove(addr);\n" +
                "                        createNewConnection = true;\n" +
                "                    }\n" +
                "                } else {\n" +
                "                    createNewConnection = true;\n" +
                "                }\n" +
                "\n" +
                "                if (createNewConnection) {\n" +
                "                    ChannelFuture channelFuture = this.bootstrap.connect(RemotingHelper.string2SocketAddress(addr));\n" +
                "                    log.info(\"createChannel: begin to connect remote host[{}] asynchronously\", addr);\n" +
                "                    cw = new ChannelWrapper(channelFuture);\n" +
                "                    this.channelTables.put(addr, cw);\n" +
                "                }\n" +
                "            } catch (Exception e) {\n" +
                "                log.error(\"createChannel: create channel exception\", e);\n" +
                "            } finally {\n" +
                "                this.lockChannelTables.unlock();\n" +
                "            }\n" +
                "        } else {\n" +
                "            log.warn(\"createChannel: try to lock channel table, but timeout, {}ms\", LOCK_TIMEOUT_MILLIS);\n" +
                "        }\n" +
                "\n" +
                "        if (cw != null) {\n" +
                "            ChannelFuture channelFuture = cw.getChannelFuture();\n" +
                "            if (channelFuture.awaitUninterruptibly(this.nettyClientConfig.getConnectTimeoutMillis())) {\n" +
                "                if (cw.isOK()) {\n" +
                "                    log.info(\"createChannel: connect remote host[{}] success, {}\", addr, channelFuture.toString());\n" +
                "                    return cw.getChannel();\n" +
                "                } else {\n" +
                "                    log.warn(\"createChannel: connect remote host[\" + addr + \"] failed, \" + channelFuture.toString(), channelFuture.cause());\n" +
                "                }\n" +
                "            } else {\n" +
                "                log.warn(\"createChannel: connect remote host[{}] timeout {}ms, {}\", addr, this.nettyClientConfig.getConnectTimeoutMillis(),\n" +
                "                        channelFuture.toString());\n" +
                "            }\n" +
                "        }\n" +
                "\n" +
                "        return null;\n" +
                "    }\n\n";
    }

    private String invokeAsync() {
        return "    @Override\n" +
                "    public void invokeAsync(String addr, RemotingCommand request, long timeoutMillis, InvokeCallback invokeCallback)\n" +
                "            throws InterruptedException, RemotingConnectException, RemotingTooMuchRequestException, RemotingTimeoutException,\n" +
                "            RemotingSendRequestException {\n" +
                "        long beginStartTime = System.currentTimeMillis();\n" +
                "        final Channel channel = this.getAndCreateChannel(addr);\n" +
                "        if (channel != null && channel.isActive()) {\n" +
                "            try {\n" +
                "                doBeforeRpcHooks(addr, request);\n" +
                "                long costTime = System.currentTimeMillis() - beginStartTime;\n" +
                "                if (timeoutMillis < costTime) {\n" +
                "                    throw new RemotingTooMuchRequestException(\"invokeAsync call timeout\");\n" +
                "                }\n" +
                "                this.invokeAsyncImpl(channel, request, timeoutMillis - costTime, invokeCallback);\n" +
                "            } catch (RemotingSendRequestException e) {\n" +
                "                log.warn(\"invokeAsync: send request exception, so close the channel[{}]\", addr);\n" +
                "                this.closeChannel(addr, channel);\n" +
                "                throw e;\n" +
                "            }\n" +
                "        } else {\n" +
                "            this.closeChannel(addr, channel);\n" +
                "            throw new RemotingConnectException(addr);\n" +
                "        }\n" +
                "    }\n\n";
    }

    private String invokeOneway() {
        return "    @Override\n" +
                "    public void invokeOneway(String addr, RemotingCommand request, long timeoutMillis) throws InterruptedException,\n" +
                "            RemotingConnectException, RemotingTooMuchRequestException, RemotingTimeoutException, RemotingSendRequestException {\n" +
                "        final Channel channel = this.getAndCreateChannel(addr);\n" +
                "        if (channel != null && channel.isActive()) {\n" +
                "            try {\n" +
                "                doBeforeRpcHooks(addr, request);\n" +
                "                this.invokeOnewayImpl(channel, request, timeoutMillis);\n" +
                "            } catch (RemotingSendRequestException e) {\n" +
                "                log.warn(\"invokeOneway: send request exception, so close the channel[{}]\", addr);\n" +
                "                this.closeChannel(addr, channel);\n" +
                "                throw e;\n" +
                "            }\n" +
                "        } else {\n" +
                "            this.closeChannel(addr, channel);\n" +
                "            throw new RemotingConnectException(addr);\n" +
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

    private String isChannelWritable() {
        return "    @Override\n" +
                "    public boolean isChannelWritable(String addr) {\n" +
                "        ChannelWrapper cw = this.channelTables.get(addr);\n" +
                "        if (cw != null && cw.isOK()) {\n" +
                "            return cw.isWritable();\n" +
                "        }\n" +
                "        return true;\n" +
                "    }\n\n";
    }

    private String getters() {
        return "    @Override\n" +
                "    public List<String> getNameServerAddressList() {\n" +
                "        return this.namesrvAddrList.get();\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public ChannelEventListener getChannelEventListener() {\n" +
                "        return channelEventListener;\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    @Override\n" +
                "    public ExecutorService getCallbackExecutor() {\n" +
                "        return callbackExecutor != null ? callbackExecutor : publicExecutor;\n" +
                "    }\n\n";
    }

    private String setters() {
        return "    @Override\n" +
                "    public void setCallbackExecutor(final ExecutorService callbackExecutor) {\n" +
                "        this.callbackExecutor = callbackExecutor;\n" +
                "    }\n\n";
    }


    private static class ChannelWrapperCodeGenerator extends ClassCodeGenerator {
        private static final String CLASS_NAME = "ChannelWrapper";

        ChannelWrapperCodeGenerator() {
            super(STATIC, CLASS_NAME);
            setFields(Collections.singletonList(new FieldInfo(PRIVATE_FINAL, "ChannelFuture", "channelFuture")));
        }

        @Override
        public String buildMethods() {
            return "    public ChannelWrapper(ChannelFuture channelFuture) {\n" +
                    "        this.channelFuture = channelFuture;\n" +
                    "    }\n" +
                    "\n" +
                    "    public boolean isOK() {\n" +
                    "        return this.channelFuture.channel() != null && this.channelFuture.channel().isActive();\n" +
                    "    }\n" +
                    "\n" +
                    "    public boolean isWritable() {\n" +
                    "        return this.channelFuture.channel().isWritable();\n" +
                    "    }\n" +
                    "\n" +
                    "    private Channel getChannel() {\n" +
                    "        return this.channelFuture.channel();\n" +
                    "    }\n" +
                    "\n" +
                    "    public ChannelFuture getChannelFuture() {\n" +
                    "        return channelFuture;\n" +
                    "    }\n";
        }
    }

    private static class NettyClientHandlerCodeGenerator extends ClassCodeGenerator {
        private static final String CLASS_NAME = "NettyClientHandler";
        private static final String PARENT_NAME = "SimpleChannelInboundHandler<RemotingCommand>";

        NettyClientHandlerCodeGenerator() {
            super(CLASS_NAME, PARENT_NAME);
            setModifier(PACKAGE);
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
        public String buildMethods() {
            return "    @Override\n" +
                    "    public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress,\n" +
                    "                        ChannelPromise promise) throws Exception {\n" +
                    "        final String local = localAddress == null ? \"UNKNOWN\" : RemotingHelper.parseSocketAddressAddr(localAddress);\n" +
                    "        final String remote = remoteAddress == null ? \"UNKNOWN\" : RemotingHelper.parseSocketAddressAddr(remoteAddress);\n" +
                    "        log.info(\"NETTY CLIENT PIPELINE: CONNECT  {} => {}\", local, remote);\n" +
                    "\n" +
                    "        super.connect(ctx, remoteAddress, localAddress, promise);\n" +
                    "\n" +
                    "        if (NettyRemotingClient.this.channelEventListener != null) {\n" +
                    "            NettyRemotingClient.this.putNettyEvent(new NettyEvent(NettyEventType.CONNECT, remote, ctx.channel()));\n" +
                    "        }\n" +
                    "    }\n" +
                    "\n" +
                    "    @Override\n" +
                    "    public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {\n" +
                    "        final String remoteAddress = RemotingHelper.parseChannelRemoteAddr(ctx.channel());\n" +
                    "        log.info(\"NETTY CLIENT PIPELINE: DISCONNECT {}\", remoteAddress);\n" +
                    "        closeChannel(ctx.channel());\n" +
                    "        super.disconnect(ctx, promise);\n" +
                    "\n" +
                    "        if (NettyRemotingClient.this.channelEventListener != null) {\n" +
                    "            NettyRemotingClient.this.putNettyEvent(new NettyEvent(NettyEventType.CLOSE, remoteAddress, ctx.channel()));\n" +
                    "        }\n" +
                    "    }\n" +
                    "\n" +
                    "    @Override\n" +
                    "    public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {\n" +
                    "        final String remoteAddress = RemotingHelper.parseChannelRemoteAddr(ctx.channel());\n" +
                    "        log.info(\"NETTY CLIENT PIPELINE: CLOSE {}\", remoteAddress);\n" +
                    "        closeChannel(ctx.channel());\n" +
                    "        super.close(ctx, promise);\n" +
                    "        NettyRemotingClient.this.failFast(ctx.channel());\n" +
                    "        if (NettyRemotingClient.this.channelEventListener != null) {\n" +
                    "            NettyRemotingClient.this.putNettyEvent(new NettyEvent(NettyEventType.CLOSE, remoteAddress, ctx.channel()));\n" +
                    "        }\n" +
                    "    }\n" +
                    "\n" +
                    "    @Override\n" +
                    "    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {\n" +
                    "        if (evt instanceof IdleStateEvent) {\n" +
                    "            IdleStateEvent event = (IdleStateEvent) evt;\n" +
                    "            if (event.state().equals(IdleState.ALL_IDLE)) {\n" +
                    "                final String remoteAddress = RemotingHelper.parseChannelRemoteAddr(ctx.channel());\n" +
                    "                log.warn(\"NETTY CLIENT PIPELINE: IDLE exception [{}]\", remoteAddress);\n" +
                    "                closeChannel(ctx.channel());\n" +
                    "                if (NettyRemotingClient.this.channelEventListener != null) {\n" +
                    "                    NettyRemotingClient.this\n" +
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
                    "        log.warn(\"NETTY CLIENT PIPELINE: exceptionCaught {}\", remoteAddress);\n" +
                    "        log.warn(\"NETTY CLIENT PIPELINE: exceptionCaught exception.\", cause);\n" +
                    "        closeChannel(ctx.channel());\n" +
                    "        if (NettyRemotingClient.this.channelEventListener != null) {\n" +
                    "            NettyRemotingClient.this.putNettyEvent(new NettyEvent(NettyEventType.EXCEPTION, remoteAddress, ctx.channel()));\n" +
                    "        }\n" +
                    "    }\n";
        }
    }
}
