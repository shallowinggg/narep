package com.shallowinggg.narep.core.generators.common;

import com.shallowinggg.narep.core.common.CodeGeneratorHelper;
import com.shallowinggg.narep.core.common.FieldMetaData;
import com.shallowinggg.narep.core.generators.ClassCodeGenerator;

import java.util.ArrayList;
import java.util.List;

import static com.shallowinggg.narep.core.common.FieldMetaData.Modifier.PRIVATE_FINAL;

/**
 * @author shallowinggg
 */
public class SemaphoreReleaseOnlyOnceCodeGenerator extends ClassCodeGenerator {
    private static final String CLASS_NAME = "SemaphoreReleaseOnlyOnce";
    private static final String SUB_PACKAGE = "common";
    private List<FieldMetaData> fields = new ArrayList<>(2);

    public SemaphoreReleaseOnlyOnceCodeGenerator() {
        super(CLASS_NAME, null, SUB_PACKAGE);

        fields.add(new FieldMetaData(PRIVATE_FINAL, "AtomicBoolean", "released", "new AtomicBoolean(false)"));
        fields.add(new FieldMetaData(PRIVATE_FINAL, "Semaphore", "semaphore"));
    }

    @Override
    public String buildImports() {
        return "import java.util.concurrent.Semaphore;\n" +
                "import java.util.concurrent.atomic.AtomicBoolean;\n\n";
    }

    @Override
    public String buildFields() {
        return CodeGeneratorHelper.buildFieldsByMetaData(fields);
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
