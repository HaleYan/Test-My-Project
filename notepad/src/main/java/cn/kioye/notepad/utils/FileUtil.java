package cn.kioye.notepad.utils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.TreeMap;

public class FileUtil {

    public static String readToStr(File file) throws IOException {
        if (!file.exists()) {
            throw new FileNotFoundException("文件不存在！"+file.getAbsoluteFile());
        }
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        try (InputStream inputStream = new FileInputStream(file)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
        }
        return result.toString("UTF-8");
    }

    public static void saveOfStr(File file,String content) throws IOException {
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        try (OutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(content.getBytes(Charset.defaultCharset()));
            outputStream.flush();
        }
    }
}
