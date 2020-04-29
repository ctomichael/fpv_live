package com.mapbox.android.telemetry;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.VisibleForTesting;

class AlarmSchedulerFlusher implements SchedulerFlusher {
    private final Context context;
    private final AlarmManager manager;
    private PendingIntent pendingIntent;
    private final AlarmReceiver receiver;

    AlarmSchedulerFlusher(Context context2, AlarmManager manager2, AlarmReceiver receiver2) {
        this.context = context2;
        this.manager = manager2;
        this.receiver = receiver2;
    }

    public void register() {
        this.pendingIntent = PendingIntent.getBroadcast(this.context, 0, this.receiver.supplyIntent(), 134217728);
        this.context.registerReceiver(this.receiver, new IntentFilter("com.mapbox.scheduler_flusher"));
    }

    public void schedule(long elapsedRealTime) {
        this.manager.setInexactRepeating(3, elapsedRealTime + SchedulerFlusherFactory.flushingPeriod, SchedulerFlusherFactory.flushingPeriod, this.pendingIntent);
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public boolean scheduleExact(long interval) {
        if (Build.VERSION.SDK_INT < 19) {
            return false;
        }
        this.manager.setExact(3, SystemClock.elapsedRealtime() + interval, this.pendingIntent);
        return true;
    }

    public void unregister() {
        if (this.pendingIntent != null) {
            this.manager.cancel(this.pendingIntent);
        }
        try {
            this.context.unregisterReceiver(this.receiver);
        } catch (IllegalArgumentException e) {
        }
    }

    /* access modifiers changed from: package-private */
    public PendingIntent obtainPendingIntent() {
        return this.pendingIntent;
    }
}
