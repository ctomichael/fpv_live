package org.xeustechnologies.jtar;

import android.support.v4.media.session.PlaybackStateCompat;
import java.io.File;

public class TarUtils {
    public static long calculateTarSize(File path) {
        return tarSize(path) + PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID;
    }

    private static long tarSize(File dir) {
        long tarSize;
        if (dir.isFile()) {
            return entrySize(dir.length());
        }
        File[] subFiles = dir.listFiles();
        if (subFiles == null || subFiles.length <= 0) {
            return 512;
        }
        File[] arr$ = subFiles;
        int len$ = arr$.length;
        int i$ = 0;
        long size = 0;
        while (i$ < len$) {
            File file = arr$[i$];
            if (file.isFile()) {
                tarSize = entrySize(file.length());
            } else {
                tarSize = tarSize(file);
            }
            i$++;
            size += tarSize;
        }
        return size;
    }

    private static long entrySize(long fileSize) {
        long size = 0 + 512 + fileSize;
        long extra = size % 512;
        if (extra > 0) {
            return size + (512 - extra);
        }
        return size;
    }
}
