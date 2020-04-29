package com.dji.mapkit.core.utils;

import dji.component.accountcenter.IMemberProtocol;
import dji.log.DJILog;

public class MapkitLog {
    private static final String TAG = "MapkitLog";

    public static void logWriteI(String tag, String msg) {
        DJILog.logWriteI(TAG, IMemberProtocol.STRING_SEPERATOR_LEFT + tag + "] " + msg, new Object[0]);
    }

    public static void logWriteD(String tag, String msg) {
        DJILog.logWriteD(TAG, IMemberProtocol.STRING_SEPERATOR_LEFT + tag + "] " + msg, new Object[0]);
    }

    public static void logWriteE(String tag, String msg) {
        DJILog.logWriteE(TAG, IMemberProtocol.STRING_SEPERATOR_LEFT + tag + "] " + msg, new Object[0]);
    }

    public static void i(String tag, String msg) {
        DJILog.i(TAG, IMemberProtocol.STRING_SEPERATOR_LEFT + tag + "] " + msg, new Object[0]);
    }

    public static void d(String tag, String msg) {
        DJILog.d(TAG, IMemberProtocol.STRING_SEPERATOR_LEFT + tag + "] " + msg, new Object[0]);
    }

    public static void e(String tag, String msg) {
        DJILog.e(TAG, IMemberProtocol.STRING_SEPERATOR_LEFT + tag + "] " + msg, new Object[0]);
    }
}
