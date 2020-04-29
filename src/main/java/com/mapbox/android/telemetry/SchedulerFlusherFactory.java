package com.mapbox.android.telemetry;

import android.app.AlarmManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

class SchedulerFlusherFactory {
    static final String SCHEDULER_FLUSHER_INTENT = "com.mapbox.scheduler_flusher";
    static long flushingPeriod = 180000;
    private final AlarmReceiver alarmReceiver;
    private final Context context;

    SchedulerFlusherFactory(Context context2, AlarmReceiver alarmReceiver2) {
        this.context = context2;
        this.alarmReceiver = alarmReceiver2;
        checkUpdatePeriod(context2);
    }

    /* access modifiers changed from: package-private */
    public SchedulerFlusher supply() {
        return new AlarmSchedulerFlusher(this.context, (AlarmManager) this.context.getSystemService(NotificationCompat.CATEGORY_ALARM), this.alarmReceiver);
    }

    private void checkUpdatePeriod(Context context2) {
        if (TelemetryUtils.adjustWakeUpMode(context2)) {
            flushingPeriod = 600000;
        }
    }
}
