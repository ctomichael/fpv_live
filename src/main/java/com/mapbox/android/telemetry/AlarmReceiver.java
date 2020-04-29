package com.mapbox.android.telemetry;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

class AlarmReceiver extends BroadcastReceiver {
    private static final String TAG = "AlarmReceiver";
    private final SchedulerCallback callback;

    AlarmReceiver(@NonNull SchedulerCallback callback2) {
        this.callback = callback2;
    }

    public void onReceive(Context context, Intent intent) {
        try {
            if ("com.mapbox.scheduler_flusher".equals(intent.getAction())) {
                this.callback.onPeriodRaised();
            }
        } catch (Throwable throwable) {
            Log.e(TAG, throwable.toString());
        }
    }

    /* access modifiers changed from: package-private */
    public Intent supplyIntent() {
        return new Intent("com.mapbox.scheduler_flusher");
    }
}
