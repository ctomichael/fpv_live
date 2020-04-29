package dji.log;

import java.io.File;

class DJIFileUtil {
    DJIFileUtil() {
    }

    public static boolean delAllFiles(File file) {
        if (file == null || !file.exists()) {
            return true;
        }
        if (file.isFile()) {
            return file.delete();
        }
        if (!file.isDirectory()) {
            return true;
        }
        File[] files = file.listFiles();
        if (files != null && files.length > 0) {
            for (File tmp : files) {
                delAllFiles(tmp);
            }
        }
        return file.delete();
    }
}
