package com.shallowinggg.narep.core.util;

import com.shallowinggg.narep.core.CodeGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class FileUtils {
    private static final Logger LOG = LoggerFactory.getLogger(FileUtils.class);
    private static final String DEFAULT_ENCODING = "utf-8";

    public static String compositeFile(CodeGenerator generator) {
        if(generator == null) {
            return "";
        }
        String content = String.valueOf(generator.buildPackage()) +
                generator.buildImports() +
                generator.buildName() +
                generator.buildFields() +
                generator.buildMethods() +
                CodeGenerator.END_OF_CLASS;
        return content;
    }

    public static void writeFile(String path, CodeGenerator generator) throws IOException {
        if(generator == null) {
            return;
        }
        File file = new File(path);
        ensureDirOk(file);
        try(OutputStream out = new FileOutputStream(file)) {
            String content = String.valueOf(generator.buildPackage()) +
                    generator.buildImports() +
                    generator.buildName() +
                    generator.buildFields() +
                    generator.buildMethods() +
                    CodeGenerator.END_OF_CLASS;
            out.write(content.getBytes(DEFAULT_ENCODING));
            if(LOG.isDebugEnabled()) {
                LOG.debug("Build file " + generator.fileName() + "success");
            }
        }
    }

    public static void ensureDirOk(final File file) {
        if (file != null) {
            if (!file.exists()) {
                boolean result = file.mkdirs();
                LOG.info(file.getPath() + " mkdir " + (result ? "OK" : "Failed"));
            }
        }
    }


    private FileUtils() {}
}
