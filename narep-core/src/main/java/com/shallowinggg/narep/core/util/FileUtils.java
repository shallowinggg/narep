package com.shallowinggg.narep.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

/**
 * @author shallowinggg
 */
public class FileUtils {
    private static final Logger LOG = LoggerFactory.getLogger(FileUtils.class);

    public static void writeFile(String path, String content, String charset) throws IOException {
        if (StringTinyUtils.isEmpty(content)) {
            return;
        }
        File file = new File(path);
        ensureFileOk(file);
        try (OutputStream out = new FileOutputStream(file)) {
            out.write(content.getBytes(charset));
        }
    }

    public static void ensureFileOk(final File file) {
        if(file != null) {
            if(!file.exists()) {
                try {
                    File parent = file.getParentFile();
                    ensureDirOk(parent);
                    Files.createFile(file.toPath());
                } catch (IOException e) {
                    LOG.error("create file " + file.getName() + " fail", e);
                }
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


    private FileUtils() {
    }
}
