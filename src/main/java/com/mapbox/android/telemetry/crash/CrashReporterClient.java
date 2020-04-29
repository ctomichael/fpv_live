package com.mapbox.android.telemetry.crash;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.util.Log;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.mapbox.android.core.FileUtils;
import com.mapbox.android.core.crashreporter.MapboxUncaughtExceptionHanlder;
import com.mapbox.android.telemetry.BuildConfig;
import com.mapbox.android.telemetry.CrashEvent;
import com.mapbox.android.telemetry.MapboxTelemetry;
import com.mapbox.android.telemetry.TelemetryListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

final class CrashReporterClient {
    private static final String CRASH_REPORTER_CLIENT_USER_AGENT = "mapbox-android-crash";
    private static final String LOG_TAG = "CrashReporterClient";
    private final HashSet<String> crashHashSet = new HashSet<>();
    private File[] crashReports;
    private final HashMap<CrashEvent, File> eventFileHashMap = new HashMap<>();
    private int fileCursor;
    private boolean isDebug;
    private final SharedPreferences sharedPreferences;
    /* access modifiers changed from: private */
    public final MapboxTelemetry telemetry;

    @VisibleForTesting
    CrashReporterClient(@NonNull SharedPreferences sharedPreferences2, @NonNull MapboxTelemetry telemetry2, File[] crashReports2) {
        this.sharedPreferences = sharedPreferences2;
        this.telemetry = telemetry2;
        this.crashReports = crashReports2;
        this.fileCursor = 0;
        this.isDebug = false;
    }

    static CrashReporterClient create(@NonNull Context context) {
        return new CrashReporterClient(context.getSharedPreferences(MapboxUncaughtExceptionHanlder.MAPBOX_CRASH_REPORTER_PREFERENCES, 0), new MapboxTelemetry(context, "", String.format("%s/%s", CRASH_REPORTER_CLIENT_USER_AGENT, BuildConfig.VERSION_NAME)), new File[0]);
    }

    /* access modifiers changed from: package-private */
    public CrashReporterClient loadFrom(@NonNull File rootDir) {
        this.fileCursor = 0;
        this.crashReports = FileUtils.listAllFiles(rootDir);
        Arrays.sort(this.crashReports, new FileUtils.LastModifiedComparator());
        return this;
    }

    /* access modifiers changed from: package-private */
    public CrashReporterClient debug(boolean isDebug2) {
        this.isDebug = isDebug2;
        return this;
    }

    /* access modifiers changed from: package-private */
    public boolean isEnabled() {
        try {
            return this.sharedPreferences.getBoolean(MapboxUncaughtExceptionHanlder.MAPBOX_PREF_ENABLE_CRASH_REPORTER, true);
        } catch (Exception ex) {
            Log.e(LOG_TAG, ex.toString());
            return false;
        }
    }

    /* access modifiers changed from: package-private */
    public boolean hasNextEvent() {
        return this.fileCursor < this.crashReports.length;
    }

    /* access modifiers changed from: package-private */
    public boolean isDuplicate(CrashEvent crashEvent) {
        return this.crashHashSet.contains(crashEvent.getHash());
    }

    /* access modifiers changed from: package-private */
    @NonNull
    public CrashEvent nextEvent() {
        if (!hasNextEvent()) {
            throw new IllegalStateException("No more events can be read");
        }
        try {
            File file = this.crashReports[this.fileCursor];
            CrashEvent event = parseJsonCrashEvent(FileUtils.readFromFile(file));
            if (event.isValid()) {
                this.eventFileHashMap.put(event, file);
            }
            this.fileCursor++;
            return event;
        } catch (FileNotFoundException fileException) {
            throw new IllegalStateException("File cannot be read: " + fileException.toString());
        } catch (Throwable th) {
            this.fileCursor++;
            throw th;
        }
    }

    /* access modifiers changed from: package-private */
    public boolean send(CrashEvent event) {
        if (!event.isValid()) {
            return false;
        }
        return sendSync(event, new AtomicBoolean(this.isDebug), new CountDownLatch(1));
    }

    /* access modifiers changed from: package-private */
    public boolean delete(CrashEvent event) {
        File file = this.eventFileHashMap.get(event);
        return file != null && file.delete();
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public boolean sendSync(CrashEvent event, AtomicBoolean status, CountDownLatch latch) {
        setupTelemetryListener(status, latch);
        this.telemetry.push(event);
        try {
            latch.await(10, TimeUnit.SECONDS);
            if (status.get()) {
                this.crashHashSet.add(event.getHash());
            }
            return status.get();
        } catch (InterruptedException e) {
            if (!status.get()) {
                return false;
            }
            this.crashHashSet.add(event.getHash());
            return false;
        } catch (Throwable th) {
            if (status.get()) {
                this.crashHashSet.add(event.getHash());
            }
            throw th;
        }
    }

    private void setupTelemetryListener(final AtomicBoolean success, final CountDownLatch latch) {
        this.telemetry.addTelemetryListener(new TelemetryListener() {
            /* class com.mapbox.android.telemetry.crash.CrashReporterClient.AnonymousClass1 */

            public void onHttpResponse(boolean successful, int code) {
                Log.d(CrashReporterClient.LOG_TAG, "Response: " + code);
                success.set(successful);
                latch.countDown();
                CrashReporterClient.this.telemetry.removeTelemetryListener(this);
            }

            public void onHttpFailure(String message) {
                Log.d(CrashReporterClient.LOG_TAG, "Response: " + message);
                latch.countDown();
                CrashReporterClient.this.telemetry.removeTelemetryListener(this);
            }
        });
    }

    private static CrashEvent parseJsonCrashEvent(String json) {
        try {
            return (CrashEvent) new GsonBuilder().create().fromJson(json, CrashEvent.class);
        } catch (JsonSyntaxException jse) {
            Log.e(LOG_TAG, jse.toString());
            return new CrashEvent(null, null);
        }
    }
}
