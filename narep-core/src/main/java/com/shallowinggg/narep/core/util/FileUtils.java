package com.shallowinggg.narep.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author shallowinggg
 */
public class FileUtils {
    private static final Logger LOG = LoggerFactory.getLogger(FileUtils.class);

    public static void writeFile(String path, String content, String charset) throws IOException {
        if(StringTinyUtils.isEmpty(content)) {
            return;
        }
        File file = new File(path);
        ensureDirOk(file);
        try(OutputStream out = new FileOutputStream(file)) {
            out.write(content.getBytes(charset));
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
