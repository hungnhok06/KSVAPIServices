package vn.backend.ksv.common;

import vn.backend.ksv.common.exception.CommonException;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Creator: Nguyen Quang Hung
 * Date: 8/8/23
 * Time: 4:10 PM
 */
public class FileHelper {

    private static final LogAdapter LOGGER = LogAdapter.newInstance(Configuration.class);

    public static String readFile(String url, String... more) throws CommonException.FileException {
        try {
            byte[] encoded = Files.readAllBytes(Paths.get(url, more));
            return new String(encoded, Charset.defaultCharset());
        } catch (IOException e) {
            LOGGER.error("Not found file {}", url);
            throw new CommonException.FileException("Not found file");
        }
    }
}
