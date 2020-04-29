package com.dji.dynamic_assets;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import dji.component.accountcenter.IMemberProtocol;
import dji.fieldAnnotation.EXClassNullAway;
import java.io.File;

@EXClassNullAway
public class DynamicAssetsHelper {
    public static String ABI = "armeabi-v7a";
    public static String DJI_PATH = "";
    private static String INTERNAL_PATH = "";
    public static final String LIB_HEAD = "libs";

    public static void init(Context context) {
        INTERNAL_PATH = addTrailingSlash(context.getFilesDir().getAbsolutePath());
        DJI_PATH = Environment.getExternalStorageDirectory() + "/DJI/" + addTrailingSlash(context.getPackageName());
        ABI = Build.CPU_ABI;
    }

    public static void loadLibrary(String library) {
        System.load(INTERNAL_PATH + addTrailingSlash(LIB_HEAD) + addTrailingSlash(ABI) + library);
    }

    public static void loadLibrary(String folder, String library) {
        System.load(INTERNAL_PATH + addTrailingSlash(folder) + library);
    }

    public static File getInternalFile(String path) {
        File file = new File(INTERNAL_PATH + path);
        if (!file.exists() || !file.isFile()) {
            return null;
        }
        return file;
    }

    public static File getExternalFile(String path) {
        File file = new File(addTrailingSlash(DJI_PATH) + path);
        if (!file.exists() || !file.isFile()) {
            return null;
        }
        return file;
    }

    public static String getInternalPath() {
        return INTERNAL_PATH;
    }

    public static String getExternalPath() {
        return DJI_PATH;
    }

    private static String addTrailingSlash(String path) {
        if (path.charAt(path.length() - 1) != '/') {
            return path + IMemberProtocol.PARAM_SEPERATOR;
        }
        return path;
    }

    private static String addLeadingSlash(String path) {
        if (path.charAt(0) != '/') {
            return IMemberProtocol.PARAM_SEPERATOR + path;
        }
        return path;
    }
}
