package com.mapbox.android.core.crashreporter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.util.Log;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import org.json.JSONException;
import org.json.JSONObject;

public class CrashReport {
    private static final String CRASH_EVENT = "mobile.crash";
    private static final String TAG = "MapboxCrashReport";
    private final JSONObject content;

    CrashReport(Calendar created) {
        this.content = new JSONObject();
        put("event", CRASH_EVENT);
        put("created", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US).format(Long.valueOf(created.getTimeInMillis())));
    }

    CrashReport(@NonNull String json) throws JSONException {
        this.content = new JSONObject(json);
    }

    public synchronized void put(@NonNull String key, @Nullable String value) {
        if (value == null) {
            putNull(key);
        } else {
            try {
                this.content.put(key, value);
            } catch (JSONException e) {
                Log.e(TAG, "Failed json encode value: " + String.valueOf(value));
            }
        }
        return;
    }

    @NonNull
    public String getDateString() {
        return getString("created");
    }

    @NonNull
    public String toJson() {
        return this.content.toString();
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    @NonNull
    public String getString(@NonNull String key) {
        return this.content.optString(key);
    }

    private void putNull(@NonNull String key) {
        try {
            this.content.put(key, "null");
        } catch (JSONException e) {
            Log.e(TAG, "Failed json encode null value");
        }
    }
}
