package com.dji.frame.util;

import android.util.Log;

public class V_LogUtil {
    private static final boolean ON = true;

    public static void d(String tag, String msg, boolean parentON, boolean myON) {
        if (parentON && myON) {
            Log.d(tag, msg);
        }
    }

    public static void e(String tag, String msg, boolean parentON, boolean myON) {
        if (parentON && myON) {
            Log.e(tag, msg);
        }
    }

    public static void i(String tag, String msg, boolean parentON, boolean myON) {
        if (parentON && myON) {
            Log.i(tag, msg);
        }
    }

    public static void v(String tag, String msg, boolean parentON, boolean myON) {
        if (parentON && myON) {
            Log.v(tag, msg);
        }
    }

    public static void w(String tag, String msg, boolean parentON, boolean myON) {
        if (parentON && myON) {
            Log.w(tag, msg);
        }
    }
}
