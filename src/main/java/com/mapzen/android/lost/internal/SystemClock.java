package com.mapzen.android.lost.internal;

import android.location.Location;
import android.os.Build;

public class SystemClock implements Clock {
    public long getSystemElapsedTimeInNanos() {
        if (Build.VERSION.SDK_INT >= 17) {
            return android.os.SystemClock.elapsedRealtimeNanos();
        }
        return android.os.SystemClock.elapsedRealtime() * Clock.MS_TO_NS;
    }

    public long getElapsedTimeInNanos(Location location) {
        if (Build.VERSION.SDK_INT >= 17) {
            return location.getElapsedRealtimeNanos();
        }
        return location.getTime() * Clock.MS_TO_NS;
    }
}
