package com.mapbox.android.core.crashreporter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.text.TextUtils;
import android.util.Log;
import com.mapbox.android.core.FileUtils;
import java.io.File;
import java.lang.Thread;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class MapboxUncaughtExceptionHanlder implements Thread.UncaughtExceptionHandler, SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String CRASH_FILENAME_FORMAT = "%s/%s.crash";
    private static final int DEFAULT_EXCEPTION_CHAIN_DEPTH = 2;
    private static final int DEFAULT_MAX_REPORTS = 10;
    public static final String MAPBOX_CRASH_REPORTER_PREFERENCES = "MapboxCrashReporterPrefs";
    public static final String MAPBOX_PREF_ENABLE_CRASH_REPORTER = "mapbox.crash.enable";
    private static final String TAG = "MbUncaughtExcHandler";
    private final Context applicationContext;
    private final Thread.UncaughtExceptionHandler defaultExceptionHandler;
    private int exceptionChainDepth;
    private final AtomicBoolean isEnabled = new AtomicBoolean(true);
    private final String mapboxPackage;
    private final String version;

    @VisibleForTesting
    MapboxUncaughtExceptionHanlder(@NonNull Context applicationContext2, @NonNull SharedPreferences sharedPreferences, @NonNull String mapboxPackage2, @NonNull String version2, Thread.UncaughtExceptionHandler defaultExceptionHandler2) {
        if (TextUtils.isEmpty(mapboxPackage2) || TextUtils.isEmpty(version2)) {
            throw new IllegalArgumentException("Invalid package name: " + mapboxPackage2 + " or version: " + version2);
        }
        this.applicationContext = applicationContext2;
        this.mapboxPackage = mapboxPackage2;
        this.version = version2;
        this.exceptionChainDepth = 2;
        this.defaultExceptionHandler = defaultExceptionHandler2;
        initializeSharedPreferences(sharedPreferences);
    }

    public static void install(@NonNull Context context, @NonNull String mapboxPackage2, @NonNull String version2) {
        Context applicationContext2;
        if (context.getApplicationContext() == null) {
            applicationContext2 = context;
        } else {
            applicationContext2 = context.getApplicationContext();
        }
        Thread.setDefaultUncaughtExceptionHandler(new MapboxUncaughtExceptionHanlder(applicationContext2, applicationContext2.getSharedPreferences(MAPBOX_CRASH_REPORTER_PREFERENCES, 0), mapboxPackage2, version2, Thread.getDefaultUncaughtExceptionHandler()));
    }

    public void uncaughtException(Thread thread, Throwable throwable) {
        if (this.isEnabled.get()) {
            List<Throwable> causalChain = getCausalChain(throwable);
            if (isMapboxCrash(causalChain)) {
                try {
                    CrashReport report = CrashReportBuilder.setup(this.applicationContext, this.mapboxPackage, this.version).addExceptionThread(thread).addCausalChain(causalChain).build();
                    ensureDirectoryWritable(this.applicationContext, this.mapboxPackage);
                    FileUtils.writeToFile(FileUtils.getFile(this.applicationContext, getReportFileName(this.mapboxPackage, report.getDateString())), report.toJson());
                } catch (Exception ex) {
                    Log.e(TAG, ex.toString());
                }
            }
        }
        if (this.defaultExceptionHandler != null) {
            this.defaultExceptionHandler.uncaughtException(thread, throwable);
        } else {
            Log.i(TAG, "Default exception handler is null");
        }
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (MAPBOX_PREF_ENABLE_CRASH_REPORTER.equals(key)) {
            try {
                this.isEnabled.set(sharedPreferences.getBoolean(MAPBOX_PREF_ENABLE_CRASH_REPORTER, false));
            } catch (Exception ex) {
                Log.e(TAG, ex.toString());
            }
        }
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public boolean isEnabled() {
        return this.isEnabled.get();
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public void setExceptionChainDepth(@IntRange(from = 1, to = 256) int depth) {
        this.exceptionChainDepth = depth;
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public boolean isMapboxCrash(List<Throwable> throwables) {
        for (Throwable cause : throwables) {
            StackTraceElement[] stackTraceElements = cause.getStackTrace();
            int length = stackTraceElements.length;
            int i = 0;
            while (true) {
                if (i < length) {
                    if (isMapboxStackTraceElement(stackTraceElements[i])) {
                        return true;
                    }
                    i++;
                }
            }
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public List<Throwable> getCausalChain(@Nullable Throwable throwable) {
        List<Throwable> causes = new ArrayList<>(4);
        int level = 0;
        while (throwable != null) {
            level++;
            if (isMidOrLowLevelException(level)) {
                causes.add(throwable);
            }
            throwable = throwable.getCause();
        }
        return Collections.unmodifiableList(causes);
    }

    private boolean isMapboxStackTraceElement(@NonNull StackTraceElement element) {
        return element.getClassName().startsWith(this.mapboxPackage);
    }

    private boolean isMidOrLowLevelException(int level) {
        return level >= this.exceptionChainDepth;
    }

    @VisibleForTesting
    static void ensureDirectoryWritable(@NonNull Context context, @NonNull String dirPath) {
        File directory = FileUtils.getFile(context, dirPath);
        if (!directory.exists()) {
            directory.mkdir();
        }
        File[] allFiles = FileUtils.listAllFiles(directory);
        if (allFiles.length >= 10) {
            FileUtils.deleteFirst(allFiles, new FileUtils.LastModifiedComparator(), 9);
        }
    }

    @VisibleForTesting
    @NonNull
    static String getReportFileName(@NonNull String mapboxPackage2, @NonNull String timestamp) {
        return String.format(CRASH_FILENAME_FORMAT, mapboxPackage2, timestamp);
    }

    private void initializeSharedPreferences(SharedPreferences sharedPreferences) {
        try {
            this.isEnabled.set(sharedPreferences.getBoolean(MAPBOX_PREF_ENABLE_CRASH_REPORTER, true));
        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
        }
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }
}
