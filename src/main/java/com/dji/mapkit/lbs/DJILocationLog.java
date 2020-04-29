package com.dji.mapkit.lbs;

import android.util.Log;

public final class DJILocationLog {
    private static final String TAG = "DJILocationManager";

    public static void LOGI(String log) {
        Log.i(TAG, log);
    }

    public static void LOGD(String log) {
        Log.d(TAG, log);
    }

    public static void LOGE(String log) {
        Log.e(TAG, log);
    }
}
