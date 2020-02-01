package com.shallowinggg.narep.core.generators.netty;

import com.shallowinggg.narep.core.annotation.Generator;
import com.shallowinggg.narep.core.common.CodeGeneratorHelper;
import com.shallowinggg.narep.core.generators.ClassCodeGenerator;
import com.shallowinggg.narep.core.generators.InnerClassCodeGenerator;
import com.shallowinggg.narep.core.lang.FieldInfo;
import com.shallowinggg.narep.core.lang.Modifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author shallowinggg
 */
@Generator
public class AbstractNettyRemotingCodeGenerator extends ClassCodeGenerator {
    private static final String CLASS_NAME = "AbstractNettyRemoting";
    private static final String SUB_PACKAGE = "netty";
    private static final List<String> DEPENDENCY_NAMES = Arrays.asList("ChannelEventListener",
            "InvokeCallback", "RPCHook", "Pair", "RemotingHelper",
            "SemaphoreReleaseOnlyOnce", "ServiceThread", "RemotingCommand",
            "RemotingSendRequestException", "RemotingTimeoutException",
            "RemotingTooMuchRequestException", "RemotingSysResponseCode");

    public AbstractNettyRemotingCodeGenerator() {
        super(CLASS_NAME, null, SUB_PACKAGE);
        setModifier(Modifier.PUBLIC_ABSTRACT);
        setDependencyNames(DEPENDENCY_NAMES);

        List<FieldInfo> fields = new ArrayList<>();
        fields.add(new FieldInfo.Builder().modifier(Modifier.PRIVATE_STATIC_FINAL)
                .type("Logger").name("log").initValue(CodeGeneratorHelper.buildLoggerField(CLASS_NAME))
                .comment("    /**\n" +
                        "     * Remoting logger instance.\n" +
                        "     */\n").build());
        fields.add(new FieldInfo.Builder().modifier(Modifier.PROTECTED_FINAL)
                .type("Semaphore").name("semaphoreOneway")
                .comment("    /**\n" +
                        "     * Semaphore to limit maximum number of on-going one-way requests, which protects system memory footprint.\n" +
                        "     */\n").build());
        fields.add(new FieldInfo.Builder().modifier(Modifier.PROTECTED_FINAL)
                .type("Semaphore").name("semaphoreAsync")
                .comment("    /**\n" +
                        "     * Semaphore to limit maximum number of on-going asynchronous requests, which protects system memory footprint.\n" +
                        "     */\n").build());
        fields.add(new FieldInfo.Builder().modifier(Modifier.PROTECTED_FINAL)
                .type("ConcurrentMap<Integer, ResponseFuture>").name("responseTable")
                .initValue("new ConcurrentHashMap<>(256)")
                .comment("    /**\n" +
                        "     * This map caches all on-going requests.\n" +
                        "     */\n").build());
        fields.add(new FieldInfo.Builder().modifier(Modifier.PROTECTED_FINAL)
                .type("HashMap<Integer, Pair<NettyRequestProcessor, ExecutorService>>").name("processorTable")
                .initValue("new HashMap<>(64)")
                .comment("    /**\n" +
                        "     * This container holds all processors per request code, aka, for each incoming request, we may look up the\n" +
                        "     * responding processor in this map to handle the request.\n" +
                        "     */\n").build());
        fields.add(new FieldInfo.Builder().modifier(Modifier.PROTECTED_FINAL)
                .type("NettyEventExecutor").name("nettyEventExecutor").initValue("new NettyEventExecutor()")
                .comment("    /**\n" +
                        "     * Executor to feed netty events to user defined {@link ChannelEventListener}.\n" +
                        "     */\n").build());
        fields.add(new FieldInfo.Builder().modifier(Modifier.PROTECTED)
                .type("Pair<NettyRequestProcessor, ExecutorService>").name("defaultRequestProcessor")
                .comment("    /**\n" +
                        "     * The default request processor to use in case there is no exact match in {@link #processorTable} per request code.\n" +
                        "     */\n").build());
        fields.add(new FieldInfo.Builder().modifier(Modifier.PROTECTED_VOLATILE)
                .type("SslContext").name("sslContext")
                .comment("    /**\n" +
                        "     * SSL context via which to create {@link SslHandler}.\n" +
                        "     */\n").build());
        fields.add(new FieldInfo.Builder().modifier(Modifier.PROTECTED_VOLATILE)
                .type("List<RPCHook>").name("rpcHooks").initValue("new ArrayList<>()")
                .comment("    /**\n" +
                        "     * custom rpc hooks.\n" +
                        "     */\n").build());
        setFields(fields);
    }

    @Override
    public String buildImports() {
        StringBuilder builder = new StringBuilder(1200);
        CodeGeneratorHelper.buildDependencyImports(builder, getDependencies());
        builder.append("import io.netty.channel.Channel;\n" +
                "import io.netty.channel.ChannelFuture;\n" +
                "import io.netty.channel.ChannelFutureListener;\n" +
                "import io.netty.channel.ChannelHandlerContext;\n" +
                "import io.netty.handler.ssl.SslContext;\n" +
                "import io.netty.handler.ssl.SslHandler;\n" +
                "import org.apache.logging.log4j.LogManager;\n" +
                "import org.apache.logging.log4j.Logger;\n" +
                "\n" +
                "import java.net.SocketAddress;\n" +
                "import java.util.*;\n" +
                "import java.util.concurrent.*;\n\n");
        return builder.toString();
    }

    @Override
    public String buildMethods() {
        return staticInit() +
                constructors() +
                getChannelEventListener() +
                putNettyEvent() +
                processMessageReceived() +
                doBeforeRpcHooks() +
                doAfterRpcHooks() +
                processRequestCommand() +
                processResponseCommand() +
                executeInvokeCallback() +
                getRPCHooks() +
                getCallbackExecutor() +
                scanResponseTable() +
                invokeSyncImpl() +
                invokeAsyncImpl() +
                requestFail() +
                failFast() +
                invokeAsyncImpl() +
                "\n";
    }

    @Override
    public String buildInnerClass() {
        InnerClassCodeGenerator nettyEventExecutor = new InnerClassCodeGenerator(this,
                new NettyEventExecutorCodeGenerator());
        setInnerClass(Collections.singletonList(nettyEventExecutor));
        return super.buildInnerClass();
    }

    private String staticInit() {
        return "    static {\n" +
                "        NettyLogger.initNettyLogger();\n" +
                "    }\n\n";
    }

    private String constructors() {
        return "    /**\n" +
                "     * Constructor, specifying capacity of one-way and asynchronous semaphores.\n" +
                "     *\n" +
                "     * @param permitsOneway Number of permits for one-way requests.\n" +
                "     * @param permitsAsync Number of permits for asynchronous requests.\n" +
                "     */\n" +
                "    public AbstractNettyRemoting(final int permitsOneway, final int permitsAsync) {\n" +
                "        this.semaphoreOneway = new Semaphore(permitsOneway, true);\n" +
                "        this.semaphoreAsync = new Semaphore(permitsAsync, true);\n" +
                "    }\n\n";
    }

    private String getChannelEventListener() {
        return "    /**\n" +
                "     * Custom channel event listener.\n" +
                "     *\n" +
                "     * @return custom channel event listener if defined; null otherwise.\n" +
                "     */\n" +
                "    public abstract ChannelEventListener getChannelEventListener();\n\n";
    }

    private String putNettyEvent() {
        return "    /**\n" +
                "     * Put a netty event to the executor.\n" +
                "     *\n" +
                "     * @param event Netty event instance.\n" +
                "     */\n" +
                "    public void putNettyEvent(final NettyEvent event) {\n" +
                "        this.nettyEventExecutor.putNettyEvent(event);\n" +
                "    }\n\n";
    }

    private String processMessageReceived() {
        return "    /**\n" +
                "     * Entry of incoming command processing.\n" +
                "     *\n" +
                "     * <p>\n" +
                "     * <strong>Note:</strong>\n" +
                "     * The incoming remoting command may be\n" +
                "     * <ul>\n" +
                "     * <li>An inquiry request from a remote peer component;</li>\n" +
                "     * <li>A response to a previous request issued by this very participant.</li>\n" +
                "     * </ul>\n" +
                "     * </p>\n" +
                "     *\n" +
                "     * @param ctx Channel handler context.\n" +
                "     * @param msg incoming remoting command.\n" +
                "     * @throws Exception if there were any error while processing the incoming command.\n" +
                "     */\n" +
                "    public void processMessageReceived(ChannelHandlerContext ctx, RemotingCommand msg) throws Exception {\n" +
                "        final RemotingCommand cmd = msg;\n" +
                "        if (cmd != null) {\n" +
                "            switch (cmd.getType()) {\n" +
                "                case REQUEST_COMMAND:\n" +
                "                    processRequestCommand(ctx, cmd);\n" +
                "                    break;\n" +
                "                case RESPONSE_COMMAND:\n" +
                "                    processResponseCommand(ctx, cmd);\n" +
                "                    break;\n" +
                "                default:\n" +
                "                    break;\n" +
                "            }\n" +
                "        }\n" +
                "    }\n\n";
    }

    private String doBeforeRpcHooks() {
        return "    protected void doBeforeRpcHooks(String addr, RemotingCommand request) {\n" +
                "        if (rpcHooks.size() > 0) {\n" +
                "            for (RPCHook rpcHook: rpcHooks) {\n" +
                "                rpcHook.doBeforeRequest(addr, request);\n" +
                "            }\n" +
                "        }\n" +
                "    }\n\n";
    }

    private String doAfterRpcHooks() {
        return "    protected void doAfterRpcHooks(String addr, RemotingCommand request, RemotingCommand response) {\n" +
                "        if (rpcHooks.size() > 0) {\n" +
                "            for (RPCHook rpcHook: rpcHooks) {\n" +
                "                rpcHook.doAfterResponse(addr, request, response);\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "\n";
    }

    private String processRequestCommand() {
        return "    /**\n" +
                "     * Process incoming request command issued by remote peer.\n" +
                "     *\n" +
                "     * @param ctx channel handler context.\n" +
                "     * @param cmd request command.\n" +
                "     */\n" +
                "    public void processRequestCommand(final ChannelHandlerContext ctx, final RemotingCommand cmd) {\n" +
                "        final Pair<NettyRequestProcessor, ExecutorService> matched = this.processorTable.get(cmd.getCode());\n" +
                "        final Pair<NettyRequestProcessor, ExecutorService> pair = null == matched ? this.defaultRequestProcessor : matched;\n" +
                "        final int opaque = cmd.getOpaque();\n" +
                "\n" +
                "        if (pair != null) {\n" +
                "            Runnable run = () -> {\n" +
                "                try {\n" +
                "                    doBeforeRpcHooks(RemotingHelper.parseChannelRemoteAddr(ctx.channel()), cmd);\n" +
                "                    final RemotingCommand response = pair.getObject1().processRequest(ctx, cmd);\n" +
                "                    doAfterRpcHooks(RemotingHelper.parseChannelRemoteAddr(ctx.channel()), cmd, response);\n" +
                "\n" +
                "                    if (!cmd.isOnewayRPC()) {\n" +
                "                        if (response != null) {\n" +
                "                            response.setOpaque(opaque);\n" +
                "                            response.markResponseType();\n" +
                "                            try {\n" +
                "                                ctx.writeAndFlush(response);\n" +
                "                            } catch (Throwable e) {\n" +
                "                                log.error(\"process request over, but response failed\", e);\n" +
                "                                log.error(cmd.toString());\n" +
                "                                log.error(response.toString());\n" +
                "                            }\n" +
                "                        }\n" +
                "                    }\n" +
                "                } catch (Throwable e) {\n" +
                "                    log.error(\"process request exception\", e);\n" +
                "                    log.error(cmd.toString());\n" +
                "\n" +
                "                    if (!cmd.isOnewayRPC()) {\n" +
                "                        final RemotingCommand response = RemotingCommand.createResponseCommand(RemotingSysResponseCode.SYSTEM_ERROR,\n" +
                "                                RemotingHelper.exceptionSimpleDesc(e));\n" +
                "                        response.setOpaque(opaque);\n" +
                "                        ctx.writeAndFlush(response);\n" +
                "                    }\n" +
                "                }\n" +
                "            };\n" +
                "\n" +
                "            if (pair.getObject1().rejectRequest()) {\n" +
                "                final RemotingCommand response = RemotingCommand.createResponseCommand(RemotingSysResponseCode.SYSTEM_BUSY,\n" +
                "                        \"[REJECTREQUEST]system busy, start flow control for a while\");\n" +
                "                response.setOpaque(opaque);\n" +
                "                ctx.writeAndFlush(response);\n" +
                "                return;\n" +
                "            }\n" +
                "\n" +
                "            try {\n" +
                "                final RequestTask requestTask = new RequestTask(run, ctx.channel(), cmd);\n" +
                "                pair.getObject2().submit(requestTask);\n" +
                "            } catch (RejectedExecutionException e) {\n" +
                "                if ((System.currentTimeMillis() % 10000) == 0) {\n" +
                "                    log.warn(RemotingHelper.parseChannelRemoteAddr(ctx.channel())\n" +
                "                            + \", too many requests and system thread pool busy, RejectedExecutionException \"\n" +
                "                            + pair.getObject2().toString()\n" +
                "                            + \" request code: \" + cmd.getCode());\n" +
                "                }\n" +
                "\n" +
                "                if (!cmd.isOnewayRPC()) {\n" +
                "                    final RemotingCommand response = RemotingCommand.createResponseCommand(RemotingSysResponseCode.SYSTEM_BUSY,\n" +
                "                            \"[OVERLOAD]system busy, start flow control for a while\");\n" +
                "                    response.setOpaque(opaque);\n" +
                "                    ctx.writeAndFlush(response);\n" +
                "                }\n" +
                "            }\n" +
                "        } else {\n" +
                "            String error = \" request type \" + cmd.getCode() + \" not supported\";\n" +
                "            final RemotingCommand response =\n" +
                "                    RemotingCommand.createResponseCommand(RemotingSysResponseCode.REQUEST_CODE_NOT_SUPPORTED, error);\n" +
                "            response.setOpaque(opaque);\n" +
                "            ctx.writeAndFlush(response);\n" +
                "            log.error(RemotingHelper.parseChannelRemoteAddr(ctx.channel()) + error);\n" +
                "        }\n" +
                "    }\n\n";
    }

    private String processResponseCommand() {
        return "    /**\n" +
                "     * Process response from remote peer to the previous issued requests.\n" +
                "     *\n" +
                "     * @param ctx channel handler context.\n" +
                "     * @param cmd response command instance.\n" +
                "     */\n" +
                "    public void processResponseCommand(ChannelHandlerContext ctx, RemotingCommand cmd) {\n" +
                "        final int opaque = cmd.getOpaque();\n" +
                "        final ResponseFuture responseFuture = responseTable.get(opaque);\n" +
                "        if (responseFuture != null) {\n" +
                "            responseFuture.setResponseCommand(cmd);\n" +
                "\n" +
                "            responseTable.remove(opaque);\n" +
                "\n" +
                "            if (responseFuture.getInvokeCallback() != null) {\n" +
                "                executeInvokeCallback(responseFuture);\n" +
                "            } else {\n" +
                "                responseFuture.putResponse(cmd);\n" +
                "                responseFuture.release();\n" +
                "            }\n" +
                "        } else {\n" +
                "            log.warn(\"receive response, but not matched any request, \" + RemotingHelper.parseChannelRemoteAddr(ctx.channel()));\n" +
                "            log.warn(cmd.toString());\n" +
                "        }\n" +
                "    }\n\n";
    }

    private String executeInvokeCallback() {
        return "    /**\n" +
                "     * Execute callback in callback executor. If callback executor is null, run directly in current thread\n" +
                "     */\n" +
                "    private void executeInvokeCallback(final ResponseFuture responseFuture) {\n" +
                "        boolean runInThisThread = false;\n" +
                "        ExecutorService executor = this.getCallbackExecutor();\n" +
                "        if (executor != null) {\n" +
                "            try {\n" +
                "                executor.submit(() -> {\n" +
                "                    try {\n" +
                "                        responseFuture.executeInvokeCallback();\n" +
                "                    } catch (Throwable e) {\n" +
                "                        log.warn(\"execute callback in executor exception, and callback throw\", e);\n" +
                "                    } finally {\n" +
                "                        responseFuture.release();\n" +
                "                    }\n" +
                "                });\n" +
                "            } catch (Exception e) {\n" +
                "                runInThisThread = true;\n" +
                "                log.warn(\"execute callback in executor exception, maybe executor busy\", e);\n" +
                "            }\n" +
                "        } else {\n" +
                "            runInThisThread = true;\n" +
                "        }\n" +
                "\n" +
                "        if (runInThisThread) {\n" +
                "            try {\n" +
                "                responseFuture.executeInvokeCallback();\n" +
                "            } catch (Throwable e) {\n" +
                "                log.warn(\"executeInvokeCallback Exception\", e);\n" +
                "            } finally {\n" +
                "                responseFuture.release();\n" +
                "            }\n" +
                "        }\n" +
                "    }\n\n";
    }

    private String getRPCHooks() {
        return "    /**\n" +
                "     * Custom RPC hooks.\n" +
                "     *\n" +
                "     * @return RPC hooks if specified; null otherwise.\n" +
                "     */\n" +
                "    public List<RPCHook> getRPCHooks() {\n" +
                "        return rpcHooks;\n" +
                "    }\n\n";
    }

    private String getCallbackExecutor() {
        return "    /**\n" +
                "     * This method specifies thread pool to use while invoking callback methods.\n" +
                "     *\n" +
                "     * @return Dedicated thread pool instance if specified; or null if the callback is supposed to be executed in the\n" +
                "     * netty event-loop thread.\n" +
                "     */\n" +
                "    public abstract ExecutorService getCallbackExecutor();\n\n";
    }

    private String scanResponseTable() {
        return "    /**\n" +
                "     * <p>\n" +
                "     * This method is periodically invoked to scan and expire deprecated request.\n" +
                "     * </p>\n" +
                "     */\n" +
                "    public void scanResponseTable() {\n" +
                "        final List<ResponseFuture> rfList = new LinkedList<ResponseFuture>();\n" +
                "        Iterator<Map.Entry<Integer, ResponseFuture>> it = this.responseTable.entrySet().iterator();\n" +
                "        while (it.hasNext()) {\n" +
                "            Map.Entry<Integer, ResponseFuture> next = it.next();\n" +
                "            ResponseFuture rep = next.getValue();\n" +
                "\n" +
                "            if ((rep.getBeginTimestamp() + rep.getTimeoutMillis() + 1000) <= System.currentTimeMillis()) {\n" +
                "                rep.release();\n" +
                "                it.remove();\n" +
                "                rfList.add(rep);\n" +
                "                log.warn(\"remove timeout request, \" + rep);\n" +
                "            }\n" +
                "        }\n" +
                "\n" +
                "        for (ResponseFuture rf : rfList) {\n" +
                "            try {\n" +
                "                executeInvokeCallback(rf);\n" +
                "            } catch (Throwable e) {\n" +
                "                log.warn(\"scanResponseTable, operationComplete Exception\", e);\n" +
                "            }\n" +
                "        }\n" +
                "    }\n\n";
    }

    private String invokeSyncImpl() {
        return "    public RemotingCommand invokeSyncImpl(final Channel channel, final RemotingCommand request,\n" +
                "                                          final long timeoutMillis)\n" +
                "            throws InterruptedException, RemotingSendRequestException, RemotingTimeoutException {\n" +
                "        final int opaque = request.getOpaque();\n" +
                "\n" +
                "        try {\n" +
                "            final ResponseFuture responseFuture = new ResponseFuture(channel, opaque, timeoutMillis, null, null);\n" +
                "            this.responseTable.put(opaque, responseFuture);\n" +
                "            final SocketAddress addr = channel.remoteAddress();\n" +
                "            channel.writeAndFlush(request).addListener((ChannelFutureListener) f -> {\n" +
                "                if (f.isSuccess()) {\n" +
                "                    responseFuture.setSendRequestOK(true);\n" +
                "                    return;\n" +
                "                } else {\n" +
                "                    responseFuture.setSendRequestOK(false);\n" +
                "                }\n" +
                "\n" +
                "                responseTable.remove(opaque);\n" +
                "                responseFuture.setCause(f.cause());\n" +
                "                responseFuture.putResponse(null);\n" +
                "                log.warn(\"send a request command to channel <\" + addr + \"> failed.\");\n" +
                "            });\n" +
                "\n" +
                "            RemotingCommand responseCommand = responseFuture.waitResponse(timeoutMillis);\n" +
                "            if (null == responseCommand) {\n" +
                "                if (responseFuture.isSendRequestOK()) {\n" +
                "                    throw new RemotingTimeoutException(RemotingHelper.parseSocketAddressAddr(addr), timeoutMillis,\n" +
                "                            responseFuture.getCause());\n" +
                "                } else {\n" +
                "                    throw new RemotingSendRequestException(RemotingHelper.parseSocketAddressAddr(addr), responseFuture.getCause());\n" +
                "                }\n" +
                "            }\n" +
                "\n" +
                "            return responseCommand;\n" +
                "        } finally {\n" +
                "            this.responseTable.remove(opaque);\n" +
                "        }\n" +
                "    }\n\n";
    }

    private String invokeAsyncImpl() {
        return "    public void invokeAsyncImpl(final Channel channel, final RemotingCommand request, final long timeoutMillis,\n" +
                "                                final InvokeCallback invokeCallback)\n" +
                "            throws InterruptedException, RemotingTooMuchRequestException, RemotingTimeoutException, RemotingSendRequestException {\n" +
                "        long beginStartTime = System.currentTimeMillis();\n" +
                "        final int opaque = request.getOpaque();\n" +
                "        boolean acquired = this.semaphoreAsync.tryAcquire(timeoutMillis, TimeUnit.MILLISECONDS);\n" +
                "        if (acquired) {\n" +
                "            final SemaphoreReleaseOnlyOnce once = new SemaphoreReleaseOnlyOnce(this.semaphoreAsync);\n" +
                "            long costTime = System.currentTimeMillis() - beginStartTime;\n" +
                "            if (timeoutMillis < costTime) {\n" +
                "                once.release();\n" +
                "                throw new RemotingTimeoutException(\"invokeAsyncImpl call timeout\");\n" +
                "            }\n" +
                "\n" +
                "            final ResponseFuture responseFuture = new ResponseFuture(channel, opaque, timeoutMillis - costTime, invokeCallback, once);\n" +
                "            this.responseTable.put(opaque, responseFuture);\n" +
                "            try {\n" +
                "                channel.writeAndFlush(request).addListener(new ChannelFutureListener() {\n" +
                "                    @Override\n" +
                "                    public void operationComplete(ChannelFuture f) throws Exception {\n" +
                "                        if (f.isSuccess()) {\n" +
                "                            responseFuture.setSendRequestOK(true);\n" +
                "                            return;\n" +
                "                        }\n" +
                "                        requestFail(opaque);\n" +
                "                        log.warn(\"send a request command to channel <{}> failed.\", RemotingHelper.parseChannelRemoteAddr(channel));\n" +
                "                    }\n" +
                "                });\n" +
                "            } catch (Exception e) {\n" +
                "                responseFuture.release();\n" +
                "                log.warn(\"send a request command to channel <\" + RemotingHelper.parseChannelRemoteAddr(channel) + \"> Exception\", e);\n" +
                "                throw new RemotingSendRequestException(RemotingHelper.parseChannelRemoteAddr(channel), e);\n" +
                "            }\n" +
                "        } else {\n" +
                "            if (timeoutMillis <= 0) {\n" +
                "                throw new RemotingTooMuchRequestException(\"invokeAsyncImpl invoke too fast\");\n" +
                "            } else {\n" +
                "                String info =\n" +
                "                        String.format(\"invokeAsyncImpl tryAcquire semaphore timeout, %dms, waiting thread nums: %d semaphoreAsyncValue: %d\",\n" +
                "                                timeoutMillis,\n" +
                "                                this.semaphoreAsync.getQueueLength(),\n" +
                "                                this.semaphoreAsync.availablePermits()\n" +
                "                        );\n" +
                "                log.warn(info);\n" +
                "                throw new RemotingTimeoutException(info);\n" +
                "            }\n" +
                "        }\n" +
                "    }\n\n";
    }

    private String requestFail() {
        return "    private void requestFail(final int opaque) {\n" +
                "        ResponseFuture responseFuture = responseTable.remove(opaque);\n" +
                "        if (responseFuture != null) {\n" +
                "            responseFuture.setSendRequestOK(false);\n" +
                "            responseFuture.putResponse(null);\n" +
                "            try {\n" +
                "                executeInvokeCallback(responseFuture);\n" +
                "            } catch (Throwable e) {\n" +
                "                log.warn(\"execute callback in requestFail, and callback throw\", e);\n" +
                "            } finally {\n" +
                "                responseFuture.release();\n" +
                "            }\n" +
                "        }\n" +
                "    }\n\n";
    }

    private String failFast() {
        return "    /**\n" +
                "     * mark the request of the specified channel as fail and to invoke fail callback immediately\n" +
                "     * @param channel the channel which is close already\n" +
                "     */\n" +
                "    protected void failFast(final Channel channel) {\n" +
                "        Iterator<Map.Entry<Integer, ResponseFuture>> it = responseTable.entrySet().iterator();\n" +
                "        while (it.hasNext()) {\n" +
                "            Map.Entry<Integer, ResponseFuture> entry = it.next();\n" +
                "            if (entry.getValue().getProcessChannel() == channel) {\n" +
                "                Integer opaque = entry.getKey();\n" +
                "                if (opaque != null) {\n" +
                "                    requestFail(opaque);\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    }\n\n";
    }

    private String invokeOnewayImpl() {
        return "    public void invokeOnewayImpl(final Channel channel, final RemotingCommand request, final long timeoutMillis)\n" +
                "            throws InterruptedException, RemotingTooMuchRequestException, RemotingTimeoutException, RemotingSendRequestException {\n" +
                "        request.markOnewayRPC();\n" +
                "        boolean acquired = this.semaphoreOneway.tryAcquire(timeoutMillis, TimeUnit.MILLISECONDS);\n" +
                "        if (acquired) {\n" +
                "            final SemaphoreReleaseOnlyOnce once = new SemaphoreReleaseOnlyOnce(this.semaphoreOneway);\n" +
                "            try {\n" +
                "                channel.writeAndFlush(request).addListener(new ChannelFutureListener() {\n" +
                "                    @Override\n" +
                "                    public void operationComplete(ChannelFuture f) throws Exception {\n" +
                "                        once.release();\n" +
                "                        if (!f.isSuccess()) {\n" +
                "                            log.warn(\"send a request command to channel <\" + channel.remoteAddress() + \"> failed.\");\n" +
                "                        }\n" +
                "                    }\n" +
                "                });\n" +
                "            } catch (Exception e) {\n" +
                "                once.release();\n" +
                "                log.warn(\"write send a request command to channel <\" + channel.remoteAddress() + \"> failed.\");\n" +
                "                throw new RemotingSendRequestException(RemotingHelper.parseChannelRemoteAddr(channel), e);\n" +
                "            }\n" +
                "        } else {\n" +
                "            if (timeoutMillis <= 0) {\n" +
                "                throw new RemotingTooMuchRequestException(\"invokeOnewayImpl invoke too fast\");\n" +
                "            } else {\n" +
                "                String info = String.format(\n" +
                "                        \"invokeOnewayImpl tryAcquire semaphore timeout, %dms, waiting thread nums: %d semaphoreAsyncValue: %d\",\n" +
                "                        timeoutMillis,\n" +
                "                        this.semaphoreOneway.getQueueLength(),\n" +
                "                        this.semaphoreOneway.availablePermits()\n" +
                "                );\n" +
                "                log.warn(info);\n" +
                "                throw new RemotingTimeoutException(info);\n" +
                "            }\n" +
                "        }\n" +
                "    }\n\n";
    }

    private static class NettyEventExecutorCodeGenerator extends ClassCodeGenerator {
        private static final String CLASS_NAME = "NettyEventExecutor";
        private static final String PARENT_NAME = "ServiceThread";

        NettyEventExecutorCodeGenerator() {
            super(CLASS_NAME, PARENT_NAME);
            setModifier(Modifier.PACKAGE);

            List<FieldInfo> fields = new ArrayList<>(2);
            fields.add(new FieldInfo(Modifier.PRIVATE_FINAL, "LinkedBlockingQueue<NettyEvent>",
                    "eventQueue", "new LinkedBlockingQueue<>()"));
            fields.add(new FieldInfo(Modifier.PRIVATE_FINAL, "int", "maxSize", "10000"));
            setFields(fields);
        }

        @Override
        public String buildMethods() {
            return "    public void putNettyEvent(final NettyEvent event) {\n" +
                    "        if (this.eventQueue.size() <= maxSize) {\n" +
                    "            this.eventQueue.add(event);\n" +
                    "        } else {\n" +
                    "            log.warn(\"event queue size[{}] enough, so drop this event {}\", this.eventQueue.size(), event.toString());\n" +
                    "        }\n" +
                    "    }\n" +
                    "\n" +
                    "    @Override\n" +
                    "    public void run() {\n" +
                    "        log.info(this.getServiceName() + \" service started\");\n" +
                    "\n" +
                    "        final ChannelEventListener listener = AbstractNettyRemoting.this.getChannelEventListener();\n" +
                    "\n" +
                    "        while (!this.isStopped()) {\n" +
                    "            try {\n" +
                    "                NettyEvent event = this.eventQueue.poll(3000, TimeUnit.MILLISECONDS);\n" +
                    "                if (event != null && listener != null) {\n" +
                    "                    switch (event.getType()) {\n" +
                    "                        case IDLE:\n" +
                    "                            listener.onChannelIdle(event.getRemoteAddr(), event.getChannel());\n" +
                    "                            break;\n" +
                    "                        case CLOSE:\n" +
                    "                            listener.onChannelClose(event.getRemoteAddr(), event.getChannel());\n" +
                    "                            break;\n" +
                    "                        case CONNECT:\n" +
                    "                            listener.onChannelConnect(event.getRemoteAddr(), event.getChannel());\n" +
                    "                            break;\n" +
                    "                        case EXCEPTION:\n" +
                    "                            listener.onChannelException(event.getRemoteAddr(), event.getChannel());\n" +
                    "                            break;\n" +
                    "                        default:\n" +
                    "                            break;\n" +
                    "\n" +
                    "                    }\n" +
                    "                }\n" +
                    "            } catch (Exception e) {\n" +
                    "                log.warn(this.getServiceName() + \" service has exception. \", e);\n" +
                    "            }\n" +
                    "        }\n" +
                    "\n" +
                    "        log.info(this.getServiceName() + \" service end\");\n" +
                    "    }\n" +
                    "\n" +
                    "    @Override\n" +
                    "    public String getServiceName() {\n" +
                    "        return NettyEventExecutor.class.getSimpleName();\n" +
                    "    }\n";
        }
    }
}
