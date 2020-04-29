package com.mapbox.android.telemetry;

import android.util.Log;

class Logger {
    Logger() {
    }

    /* access modifiers changed from: package-private */
    public int debug(String tag, String msg) {
        return Log.d(tag, msg);
    }

    /* access modifiers changed from: package-private */
    public int error(String tag, String msg) {
        return Log.e(tag, msg);
    }
}
