package com.shallowinggg.narep.core.generators.common;

import com.shallowinggg.narep.core.common.CodeGeneratorHelper;
import com.shallowinggg.narep.core.generators.ClassCodeGenerator;
import com.shallowinggg.narep.core.lang.FieldInfo;

import java.util.ArrayList;
import java.util.List;

import static com.shallowinggg.narep.core.lang.Modifier.*;


/**
 * @author shallowinggg
 */
public class ServiceThreadCodeGenerator extends ClassCodeGenerator {
    private static final String CLASS_NAME = "ServiceThread";
    private static final String[] INTERFACES = new String[]{"Runnable"};
    private static final String SUB_PACKAGE = "common";

    public ServiceThreadCodeGenerator() {
        super(CLASS_NAME, null, SUB_PACKAGE, INTERFACES);

        List<FieldInfo> fields = new ArrayList<>(5);
        fields.add(new FieldInfo(PRIVATE_STATIC_FINAL, "long", "JOIN_TIME", "90 * 1000"));
        fields.add(new FieldInfo(PRIVATE_STATIC_FINAL, "Logger", "log", CodeGeneratorHelper.buildLoggerField(CLASS_NAME)));
        fields.add(new FieldInfo(PROTECTED_FINAL, "Thread", "thread"));
        fields.add(new FieldInfo(PROTECTED_VOLATILE, "boolean", "hasNotified", "false"));
        fields.add(new FieldInfo(PROTECTED_VOLATILE, "boolean", "stopped", "false"));
        setFields(fields);
    }

    @Override
    public String buildImports() {
        return "import org.apache.logging.log4j.LogManager;\n" +
                "import org.apache.logging.log4j.Logger;\n\n";
    }

    @Override
    public String buildMethods() {
        return "    public ServiceThread() {\n" +
                "        this.thread = new Thread(this, this.getServiceName());\n" +
                "    }\n" +
                "\n" +
                "    public abstract String getServiceName();\n" +
                "\n" +
                "    public void start() {\n" +
                "        this.thread.start();\n" +
                "    }\n" +
                "\n" +
                "    public void shutdown() {\n" +
                "        this.shutdown(false);\n" +
                "    }\n" +
                "\n" +
                "    public void shutdown(final boolean interrupt) {\n" +
                "        this.stopped = true;\n" +
                "        log.info(\"shutdown thread \" + this.getServiceName() + \" interrupt \" + interrupt);\n" +
                "        synchronized (this) {\n" +
                "            if (!this.hasNotified) {\n" +
                "                this.hasNotified = true;\n" +
                "                this.notify();\n" +
                "            }\n" +
                "        }\n" +
                "\n" +
                "        try {\n" +
                "            if (interrupt) {\n" +
                "                this.thread.interrupt();\n" +
                "            }\n" +
                "\n" +
                "            long beginTime = System.currentTimeMillis();\n" +
                "            this.thread.join(this.getJointime());\n" +
                "            long elapsedTime = System.currentTimeMillis() - beginTime;\n" +
                "            log.info(\"join thread \" + this.getServiceName() + \" elapsed time(ms) \" + elapsedTime + \" \"\n" +
                "                    + this.getJointime());\n" +
                "        } catch (InterruptedException e) {\n" +
                "            log.error(\"Interrupted\", e);\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    public long getJointime() {\n" +
                "        return JOIN_TIME;\n" +
                "    }\n" +
                "\n" +
                "    public boolean isStopped() {\n" +
                "        return stopped;\n" +
                "    }\n";
    }
}
