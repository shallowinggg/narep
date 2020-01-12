package com.shallowinggg.narep.core.generators.common;

import com.shallowinggg.narep.core.generators.ClassCodeGenerator;

/**
 * @author shallowinggg
 */
public class SemaphoreReleaseOnlyOnceCodeGenerator extends ClassCodeGenerator {
    private static final String CLASS_NAME = "SemaphoreReleaseOnlyOnce";
    private static final String SUB_PACKAGE = "common";

    public SemaphoreReleaseOnlyOnceCodeGenerator() {
        super(CLASS_NAME, SUB_PACKAGE);
    }

    @Override
    public String buildImports() {
        return "import java.util.concurrent.Semaphore;\n" +
                "import java.util.concurrent.atomic.AtomicBoolean;\n\n";
    }

    @Override
    public String buildFields() {
        return "    private final AtomicBoolean released = new AtomicBoolean(false);\n" +
                "    private final Semaphore semaphore;\n\n";
    }

    @Override
    public String buildMethods() {
        return "    public SemaphoreReleaseOnlyOnce(Semaphore semaphore) {\n" +
                "        this.semaphore = semaphore;\n" +
                "    }\n" +
                "\n" +
                "    public void release() {\n" +
                "        if (this.semaphore != null) {\n" +
                "            if (this.released.compareAndSet(false, true)) {\n" +
                "                this.semaphore.release();\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    public Semaphore getSemaphore() {\n" +
                "        return semaphore;\n" +
                "    }\n";
    }
}
