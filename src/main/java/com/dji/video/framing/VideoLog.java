package com.dji.video.framing;

import android.util.Log;
import dji.log.DJILog;
import dji.log.DJILogPaths;
import dji.log.DJILogUtils;
import java.text.SimpleDateFormat;
import java.util.Date;

public class VideoLog {
    private static boolean DEBUG_ACTIVE = false;
    private static final String TAG = "VideoLog";
    private static SimpleDateFormat df = new SimpleDateFormat(DJILogUtils.FORMAT_2);

    public static void setVideoDebugSwitch(boolean isOpen) {
        DEBUG_ACTIVE = isOpen;
    }

    public static boolean getVideoDebugSwitch() {
        return DEBUG_ACTIVE;
    }

    public static void v(String msg, Object... args) {
        if (DEBUG_ACTIVE) {
            DJILog.logWriteV(TAG, df.format(new Date()) + ":  " + msg, DJILogPaths.LOG_DECODER, args);
        }
    }

    public static void v(String tag, String msg, Object... args) {
        if (DEBUG_ACTIVE) {
            DJILog.logWriteV(tag, df.format(new Date()) + ":  " + msg, DJILogPaths.LOG_DECODER, args);
        }
    }

    public static void v(String tag, String msg, Throwable throwable, Object... args) {
        if (DEBUG_ACTIVE) {
            DJILog.logWriteV(tag, df.format(new Date()) + ":  " + msg + Log.getStackTraceString(throwable), DJILogPaths.LOG_DECODER, args);
        }
    }

    public static void d(String msg, Object... args) {
        if (DEBUG_ACTIVE) {
            DJILog.logWriteD(TAG, df.format(new Date()) + ":  " + msg, DJILogPaths.LOG_DECODER, args);
        }
    }

    public static void d(String tag, String msg, Object... args) {
        if (DEBUG_ACTIVE) {
            DJILog.logWriteD(tag, df.format(new Date()) + ":  " + msg, DJILogPaths.LOG_DECODER, args);
        }
    }

    public static void d(String tag, String msg, Throwable throwable, Object... args) {
        if (DEBUG_ACTIVE) {
            DJILog.logWriteD(tag, df.format(new Date()) + ":  " + msg + Log.getStackTraceString(throwable), DJILogPaths.LOG_DECODER, args);
        }
    }

    public static void i(String msg, Object... args) {
        if (DEBUG_ACTIVE) {
            DJILog.logWriteI(TAG, df.format(new Date()) + ":  " + msg, DJILogPaths.LOG_DECODER, args);
        }
    }

    public static void i(String tag, String msg, Object... args) {
        if (DEBUG_ACTIVE) {
            DJILog.logWriteI(tag, df.format(new Date()) + ":  " + msg, DJILogPaths.LOG_DECODER, args);
        }
    }

    public static void i(String tag, String msg, Throwable throwable, Object... args) {
        if (DEBUG_ACTIVE) {
            DJILog.logWriteI(tag, df.format(new Date()) + ":  " + msg + Log.getStackTraceString(throwable), DJILogPaths.LOG_DECODER, args);
        }
    }

    public static void w(String msg, Object... args) {
        DJILog.logWriteW(TAG, df.format(new Date()) + ":  " + msg, DJILogPaths.LOG_DECODER, args);
    }

    public static void w(String tag, String msg, Object... args) {
        DJILog.logWriteW(tag, df.format(new Date()) + ":  " + msg, DJILogPaths.LOG_DECODER, args);
    }

    public static void w(String tag, String msg, Throwable throwable, Object... args) {
        DJILog.logWriteW(tag, df.format(new Date()) + ":  " + msg + Log.getStackTraceString(throwable), DJILogPaths.LOG_DECODER, args);
    }

    public static void e(String msg, Object... args) {
        DJILog.logWriteE(TAG, df.format(new Date()) + ":  " + msg, DJILogPaths.LOG_DECODER, args);
    }

    public static void e(String tag, String msg, Object... args) {
        DJILog.logWriteE(tag, df.format(new Date()) + ":  " + msg, DJILogPaths.LOG_DECODER, args);
    }

    public static void e(String tag, String msg, Throwable throwable, Object... args) {
        DJILog.logWriteE(tag, df.format(new Date()) + ":  " + msg + Log.getStackTraceString(throwable), DJILogPaths.LOG_DECODER, args);
    }
}
