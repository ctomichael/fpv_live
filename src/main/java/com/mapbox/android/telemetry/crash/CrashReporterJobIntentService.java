package com.mapbox.android.telemetry.crash;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.JobIntentService;
import android.util.Log;
import com.mapbox.android.core.FileUtils;
import com.mapbox.android.telemetry.CrashEvent;
import java.io.File;

public final class CrashReporterJobIntentService extends JobIntentService {
    private static final int JOB_ID = 666;
    private static final String LOG_TAG = "CrashJobIntentService";

    static void enqueueWork(@NonNull Context context) {
        enqueueWork(context, CrashReporterJobIntentService.class, 666, new Intent(context, CrashReporterJobIntentService.class));
    }

    /* access modifiers changed from: protected */
    public void onHandleWork(@NonNull Intent intent) {
        Log.d(LOG_TAG, "onHandleWork");
        try {
            File rootDirectory = FileUtils.getFile(getApplicationContext(), "com.mapbox.android.telemetry");
            if (!rootDirectory.exists()) {
                Log.w(LOG_TAG, "Root directory doesn't exist");
            } else {
                handleCrashReports(CrashReporterClient.create(getApplicationContext()).loadFrom(rootDirectory));
            }
        } catch (Throwable throwable) {
            Log.e(LOG_TAG, throwable.toString());
        }
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public void handleCrashReports(@NonNull CrashReporterClient client) {
        if (!client.isEnabled()) {
            Log.w(LOG_TAG, "Crash reporter is disabled");
            return;
        }
        while (client.hasNextEvent()) {
            CrashEvent event = client.nextEvent();
            if (client.isDuplicate(event)) {
                Log.d(LOG_TAG, "Skip duplicate crash in this batch: " + event.getHash());
                client.delete(event);
            } else if (client.send(event)) {
                client.delete(event);
            } else {
                Log.w(LOG_TAG, "Failed to deliver crash event");
            }
        }
    }
}
