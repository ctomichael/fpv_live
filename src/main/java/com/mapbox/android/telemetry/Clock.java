package com.mapbox.android.telemetry;

import android.os.SystemClock;

class Clock {
    Clock() {
    }

    /* access modifiers changed from: package-private */
    public long giveMeTheElapsedRealtime() {
        return SystemClock.elapsedRealtime();
    }
}
