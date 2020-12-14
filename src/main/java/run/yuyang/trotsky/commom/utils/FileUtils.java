package run.yuyang.trotsky.commom.utils;

import java.io.File;

public class FileUtils {

    /**
     * 获取绝对路径
     */
    public static String getRelPath(String path) {
        if (null == path || "".equals(path) || ".".equals(path)) {
            return new File("").getAbsolutePath();
        } else {
            File file = new File(path);

            if (file.exists() && file.isDirectory()) {
                return file.getAbsolutePath();
            }
            if (file.mkdir()) {
                return file.getAbsolutePath();
            } else {
                return null;
            }
        }
    }

}
