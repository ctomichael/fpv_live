package dji.internal.diagnostics.handler.util;

import android.util.Log;
import dji.log.DJILog;

public class DiagnosticsLog {
    private static final String FILE_NAME = "diagnostics";

    public static void loge(String TAG, String msg) {
        DJILog.logWriteE(TAG, msg, FILE_NAME, new Object[0]);
    }

    public static void loge(String TAG, String msg, Throwable throwable) {
        DJILog.logWriteE(TAG, msg + "\n" + Log.getStackTraceString(throwable), FILE_NAME, new Object[0]);
    }

    public static void logi(String TAG, String msg) {
        DJILog.logWriteI(TAG, msg, FILE_NAME, new Object[0]);
    }
}
