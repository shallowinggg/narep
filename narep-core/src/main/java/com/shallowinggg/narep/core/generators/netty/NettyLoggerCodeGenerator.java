package com.shallowinggg.narep.core.generators.netty;

import com.shallowinggg.narep.core.annotation.Generator;
import com.shallowinggg.narep.core.generators.ClassCodeGenerator;
import com.shallowinggg.narep.core.generators.InnerClassCodeGenerator;
import com.shallowinggg.narep.core.lang.FieldInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.shallowinggg.narep.core.lang.Modifier.PRIVATE;
import static com.shallowinggg.narep.core.lang.Modifier.PRIVATE_STATIC;

/**
 * @author shallowinggg
 */
@Generator
public class NettyLoggerCodeGenerator extends ClassCodeGenerator {
    private static final String CLASS_NAME = "NettyLogger";
    private static final String SUB_PACKAGE = "netty";

    public NettyLoggerCodeGenerator() {
        super(CLASS_NAME, null, SUB_PACKAGE);

        List<FieldInfo> fields = new ArrayList<>(2);
        fields.add(new FieldInfo(PRIVATE_STATIC, "AtomicBoolean", "nettyLoggerSeted", "new AtomicBoolean(false)"));
        fields.add(new FieldInfo(PRIVATE_STATIC, "InternalLogLevel", "nettyLogLevel", "InternalLogLevel.ERROR"));
        setFields(fields);
    }

    @Override
    public String buildImports() {
        return "import io.netty.util.internal.logging.InternalLogLevel;\n" +
                "import org.apache.logging.log4j.LogManager;\n" +
                "import org.apache.logging.log4j.Logger;\n" +
                "\n" +
                "import java.util.concurrent.atomic.AtomicBoolean;\n\n";
    }

    @Override
    public String buildMethods() {
        return "    public static void initNettyLogger() {\n" +
                "        if (!nettyLoggerSeted.get()) {\n" +
                "            try {\n" +
                "                io.netty.util.internal.logging.InternalLoggerFactory.setDefaultFactory(new NettyBridgeLoggerFactory());\n" +
                "            } catch (Throwable e) {\n" +
                "                //ignore\n" +
                "            }\n" +
                "            nettyLoggerSeted.set(true);\n" +
                "        }\n" +
                "    }\n\n";
    }

    @Override
    public String buildInnerClass() {
        InnerClassCodeGenerator nettyBridgeLoggerFactory = new InnerClassCodeGenerator(this,
                new NettyBridgeLoggerFactoryCodeGenerator());
        InnerClassCodeGenerator nettyBridgeLogger = new InnerClassCodeGenerator(this,
                new NettyBridgeLoggerCodeGenerator());
        setInnerClass(Arrays.asList(nettyBridgeLoggerFactory, nettyBridgeLogger));
        return super.buildInnerClass();
    }

    private static class NettyBridgeLoggerFactoryCodeGenerator extends ClassCodeGenerator {
        private static final String CLASS_NAME = "NettyBridgeLoggerFactory";
        private static final String PARENT_NAME = "io.netty.util.internal.logging.InternalLoggerFactory";

        NettyBridgeLoggerFactoryCodeGenerator() {
            super(CLASS_NAME, PARENT_NAME);
            setModifier(PRIVATE_STATIC);
        }

        @Override
        public String buildMethods() {
            return "    @Override\n" +
                    "        protected io.netty.util.internal.logging.InternalLogger newInstance(String s) {\n" +
                    "            return new NettyBridgeLogger(s);\n" +
                    "        }\n";
        }
    }

    private static class NettyBridgeLoggerCodeGenerator extends ClassCodeGenerator {
        private static final String CLASS_NAME = "NettyBridgeLogger";
        private static final String[] INTERFACE_NAMES = new String[]{"io.netty.util.internal.logging.InternalLogger"};

        NettyBridgeLoggerCodeGenerator() {
            super(PRIVATE_STATIC, CLASS_NAME);
            setInterfaceNames(INTERFACE_NAMES);
            setFields(Collections.singletonList(new FieldInfo(PRIVATE, "Logger", "logger")));
        }

        @Override
        public String buildMethods() {
            return "    public NettyBridgeLogger(String name) {\n" +
                    "            logger = LogManager.getLogger(name);\n" +
                    "        }\n" +
                    "\n" +
                    "        @Override\n" +
                    "        public String name() {\n" +
                    "            return logger.getName();\n" +
                    "        }\n" +
                    "\n" +
                    "        @Override\n" +
                    "        public boolean isEnabled(InternalLogLevel internalLogLevel) {\n" +
                    "            return nettyLogLevel.ordinal() <= internalLogLevel.ordinal();\n" +
                    "        }\n" +
                    "\n" +
                    "        @Override\n" +
                    "        public void log(InternalLogLevel internalLogLevel, String s) {\n" +
                    "            if (internalLogLevel.equals(InternalLogLevel.DEBUG)) {\n" +
                    "                logger.debug(s);\n" +
                    "            }\n" +
                    "            if (internalLogLevel.equals(InternalLogLevel.TRACE)) {\n" +
                    "                logger.info(s);\n" +
                    "            }\n" +
                    "            if (internalLogLevel.equals(InternalLogLevel.INFO)) {\n" +
                    "                logger.info(s);\n" +
                    "            }\n" +
                    "            if (internalLogLevel.equals(InternalLogLevel.WARN)) {\n" +
                    "                logger.warn(s);\n" +
                    "            }\n" +
                    "            if (internalLogLevel.equals(InternalLogLevel.ERROR)) {\n" +
                    "                logger.error(s);\n" +
                    "            }\n" +
                    "        }\n" +
                    "\n" +
                    "        @Override\n" +
                    "        public void log(InternalLogLevel internalLogLevel, String s, Object o) {\n" +
                    "            if (internalLogLevel.equals(InternalLogLevel.DEBUG)) {\n" +
                    "                logger.debug(s, o);\n" +
                    "            }\n" +
                    "            if (internalLogLevel.equals(InternalLogLevel.TRACE)) {\n" +
                    "                logger.info(s, o);\n" +
                    "            }\n" +
                    "            if (internalLogLevel.equals(InternalLogLevel.INFO)) {\n" +
                    "                logger.info(s, o);\n" +
                    "            }\n" +
                    "            if (internalLogLevel.equals(InternalLogLevel.WARN)) {\n" +
                    "                logger.warn(s, o);\n" +
                    "            }\n" +
                    "            if (internalLogLevel.equals(InternalLogLevel.ERROR)) {\n" +
                    "                logger.error(s, o);\n" +
                    "            }\n" +
                    "        }\n" +
                    "\n" +
                    "        @Override\n" +
                    "        public void log(InternalLogLevel internalLogLevel, String s, Object o, Object o1) {\n" +
                    "            if (internalLogLevel.equals(InternalLogLevel.DEBUG)) {\n" +
                    "                logger.debug(s, o, o1);\n" +
                    "            }\n" +
                    "            if (internalLogLevel.equals(InternalLogLevel.TRACE)) {\n" +
                    "                logger.info(s, o, o1);\n" +
                    "            }\n" +
                    "            if (internalLogLevel.equals(InternalLogLevel.INFO)) {\n" +
                    "                logger.info(s, o, o1);\n" +
                    "            }\n" +
                    "            if (internalLogLevel.equals(InternalLogLevel.WARN)) {\n" +
                    "                logger.warn(s, o, o1);\n" +
                    "            }\n" +
                    "            if (internalLogLevel.equals(InternalLogLevel.ERROR)) {\n" +
                    "                logger.error(s, o, o1);\n" +
                    "            }\n" +
                    "        }\n" +
                    "\n" +
                    "        @Override\n" +
                    "        public void log(InternalLogLevel internalLogLevel, String s, Object... objects) {\n" +
                    "            if (internalLogLevel.equals(InternalLogLevel.DEBUG)) {\n" +
                    "                logger.debug(s, objects);\n" +
                    "            }\n" +
                    "            if (internalLogLevel.equals(InternalLogLevel.TRACE)) {\n" +
                    "                logger.info(s, objects);\n" +
                    "            }\n" +
                    "            if (internalLogLevel.equals(InternalLogLevel.INFO)) {\n" +
                    "                logger.info(s, objects);\n" +
                    "            }\n" +
                    "            if (internalLogLevel.equals(InternalLogLevel.WARN)) {\n" +
                    "                logger.warn(s, objects);\n" +
                    "            }\n" +
                    "            if (internalLogLevel.equals(InternalLogLevel.ERROR)) {\n" +
                    "                logger.error(s, objects);\n" +
                    "            }\n" +
                    "        }\n" +
                    "\n" +
                    "        @Override\n" +
                    "        public void log(InternalLogLevel internalLogLevel, String s, Throwable throwable) {\n" +
                    "            if (internalLogLevel.equals(InternalLogLevel.DEBUG)) {\n" +
                    "                logger.debug(s, throwable);\n" +
                    "            }\n" +
                    "            if (internalLogLevel.equals(InternalLogLevel.TRACE)) {\n" +
                    "                logger.info(s, throwable);\n" +
                    "            }\n" +
                    "            if (internalLogLevel.equals(InternalLogLevel.INFO)) {\n" +
                    "                logger.info(s, throwable);\n" +
                    "            }\n" +
                    "            if (internalLogLevel.equals(InternalLogLevel.WARN)) {\n" +
                    "                logger.warn(s, throwable);\n" +
                    "            }\n" +
                    "            if (internalLogLevel.equals(InternalLogLevel.ERROR)) {\n" +
                    "                logger.error(s, throwable);\n" +
                    "            }\n" +
                    "        }\n" +
                    "\n" +
                    "        @Override\n" +
                    "        public boolean isTraceEnabled() {\n" +
                    "            return isEnabled(InternalLogLevel.TRACE);\n" +
                    "        }\n" +
                    "\n" +
                    "        @Override\n" +
                    "        public void trace(String var1) {\n" +
                    "            logger.info(var1);\n" +
                    "        }\n" +
                    "\n" +
                    "        @Override\n" +
                    "        public void trace(String var1, Object var2) {\n" +
                    "            logger.info(var1, var2);\n" +
                    "        }\n" +
                    "\n" +
                    "        @Override\n" +
                    "        public void trace(String var1, Object var2, Object var3) {\n" +
                    "            logger.info(var1, var2, var3);\n" +
                    "        }\n" +
                    "\n" +
                    "        @Override\n" +
                    "        public void trace(String var1, Object... var2) {\n" +
                    "            logger.info(var1, var2);\n" +
                    "        }\n" +
                    "\n" +
                    "        @Override\n" +
                    "        public void trace(String var1, Throwable var2) {\n" +
                    "            logger.info(var1, var2);\n" +
                    "        }\n" +
                    "\n" +
                    "        @Override\n" +
                    "        public boolean isDebugEnabled() {\n" +
                    "            return isEnabled(InternalLogLevel.DEBUG);\n" +
                    "        }\n" +
                    "\n" +
                    "        @Override\n" +
                    "        public void debug(String var1) {\n" +
                    "            logger.debug(var1);\n" +
                    "        }\n" +
                    "\n" +
                    "        @Override\n" +
                    "        public void debug(String var1, Object var2) {\n" +
                    "            logger.debug(var1, var2);\n" +
                    "        }\n" +
                    "\n" +
                    "        @Override\n" +
                    "        public void debug(String var1, Object var2, Object var3) {\n" +
                    "            logger.debug(var1, var2, var3);\n" +
                    "        }\n" +
                    "\n" +
                    "        @Override\n" +
                    "        public void debug(String var1, Object... var2) {\n" +
                    "            logger.debug(var1, var2);\n" +
                    "        }\n" +
                    "\n" +
                    "        @Override\n" +
                    "        public void debug(String var1, Throwable var2) {\n" +
                    "            logger.debug(var1, var2);\n" +
                    "        }\n" +
                    "\n" +
                    "        @Override\n" +
                    "        public boolean isInfoEnabled() {\n" +
                    "            return isEnabled(InternalLogLevel.INFO);\n" +
                    "        }\n" +
                    "\n" +
                    "        @Override\n" +
                    "        public void info(String var1) {\n" +
                    "            logger.info(var1);\n" +
                    "        }\n" +
                    "\n" +
                    "        @Override\n" +
                    "        public void info(String var1, Object var2) {\n" +
                    "            logger.info(var1, var2);\n" +
                    "        }\n" +
                    "\n" +
                    "        @Override\n" +
                    "        public void info(String var1, Object var2, Object var3) {\n" +
                    "            logger.info(var1, var2, var3);\n" +
                    "        }\n" +
                    "\n" +
                    "        @Override\n" +
                    "        public void info(String var1, Object... var2) {\n" +
                    "            logger.info(var1, var2);\n" +
                    "        }\n" +
                    "\n" +
                    "        @Override\n" +
                    "        public void info(String var1, Throwable var2) {\n" +
                    "            logger.info(var1, var2);\n" +
                    "        }\n" +
                    "\n" +
                    "        @Override\n" +
                    "        public boolean isWarnEnabled() {\n" +
                    "            return isEnabled(InternalLogLevel.WARN);\n" +
                    "        }\n" +
                    "\n" +
                    "        @Override\n" +
                    "        public void warn(String var1) {\n" +
                    "            logger.warn(var1);\n" +
                    "        }\n" +
                    "\n" +
                    "        @Override\n" +
                    "        public void warn(String var1, Object var2) {\n" +
                    "            logger.warn(var1, var2);\n" +
                    "        }\n" +
                    "\n" +
                    "        @Override\n" +
                    "        public void warn(String var1, Object... var2) {\n" +
                    "            logger.warn(var1, var2);\n" +
                    "        }\n" +
                    "\n" +
                    "        @Override\n" +
                    "        public void warn(String var1, Object var2, Object var3) {\n" +
                    "            logger.warn(var1, var2, var3);\n" +
                    "        }\n" +
                    "\n" +
                    "        @Override\n" +
                    "        public void warn(String var1, Throwable var2) {\n" +
                    "            logger.warn(var1, var2);\n" +
                    "        }\n" +
                    "\n" +
                    "        @Override\n" +
                    "        public boolean isErrorEnabled() {\n" +
                    "            return isEnabled(InternalLogLevel.ERROR);\n" +
                    "        }\n" +
                    "\n" +
                    "        @Override\n" +
                    "        public void error(String var1) {\n" +
                    "            logger.error(var1);\n" +
                    "        }\n" +
                    "\n" +
                    "        @Override\n" +
                    "        public void error(String var1, Object var2) {\n" +
                    "            logger.error(var1, var2);\n" +
                    "        }\n" +
                    "\n" +
                    "        @Override\n" +
                    "        public void error(String var1, Object var2, Object var3) {\n" +
                    "            logger.error(var1, var2, var3);\n" +
                    "        }\n" +
                    "\n" +
                    "        @Override\n" +
                    "        public void error(String var1, Object... var2) {\n" +
                    "            logger.error(var1, var2);\n" +
                    "        }\n" +
                    "\n" +
                    "        @Override\n" +
                    "        public void error(String var1, Throwable var2) {\n" +
                    "            logger.error(var1, var2);\n" +
                    "        }\n\n";
        }
    }
}
