package com.dji.frame.util;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import dji.component.accountcenter.IMemberProtocol;
import java.io.File;
import java.util.HashMap;

public class V_DiskUtil {
    public static String MNTRoot = "/mnt/";
    public static String SDCardRoot = Environment.getExternalStorageDirectory().getAbsolutePath();
    private static String TAG = "V_DiskUtil";
    private static String dirName = (Environment.getExternalStorageDirectory() + "/DJI/");

    public static String getExternalCacheDirPath(Context context, String path) {
        return dirName + context.getPackageName() + IMemberProtocol.PARAM_SEPERATOR + path;
    }

    public static void mkdirs(String dirPath) {
        File file = new File(dirPath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static File getDiskCacheDir(Context context, String uniqueName) {
        return new File(getDiskCacheDirPath(context, uniqueName));
    }

    public static String getDiskCacheDirPath(Context context, String uniqueName) {
        String mountPath = getBestMountPath();
        return (mountPath != null ? getExternalCacheDirPath(context, mountPath) : context.getCacheDir().getPath()) + File.separator + uniqueName;
    }

    public static HashMap<String, String> getAllMountPath() {
        HashMap<String, String> paths = new HashMap<>();
        int i = 1;
        try {
            File dir = new File("/mnt");
            if (dir.exists() && dir.isDirectory() && dir.listFiles() != null && dir.listFiles().length > 0) {
                File[] listFiles = dir.listFiles();
                for (File file : listFiles) {
                    if (file.isDirectory()) {
                        String filepath = file.getAbsolutePath();
                        String key = "";
                        if (filepath.startsWith("/mnt/sdcard")) {
                            key = "sdcard";
                        } else if (filepath.startsWith("/mnt/sda")) {
                            key = "sda" + i;
                            i++;
                        } else if (filepath.startsWith("/mnt/nand")) {
                            key = "nand";
                        }
                        if (!key.equals("")) {
                            paths.put(key, filepath);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return paths;
    }

    private static String getBestMountPath() {
        HashMap<String, String> paths = getAllMountPath();
        if (paths.containsKey("sdcard")) {
            return paths.get("sdcard");
        }
        if (paths.containsKey("nand")) {
            return paths.get("nand");
        }
        if (paths.containsKey("sda1")) {
            return paths.get("sda1");
        }
        return null;
    }

    public static File getExternalCacheDir(Context context, String path) {
        return new File(SDCardRoot + getExternalCacheDirPath(context, path));
    }

    public static long getUsableSpace(File path) {
        try {
            StatFs stats = new StatFs(path.getPath());
            return ((long) stats.getBlockSize()) * ((long) stats.getAvailableBlocks());
        } catch (Exception e) {
            Log.e(TAG, "获取 sdcard 缓存大小 出错，请查看AndroidManifest.xml 是否添加了sdcard的访问权限");
            e.printStackTrace();
            return -1;
        }
    }
}
