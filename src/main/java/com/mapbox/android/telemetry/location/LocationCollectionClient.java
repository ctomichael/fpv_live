package com.mapbox.android.telemetry.location;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.util.Log;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.telemetry.BuildConfig;
import com.mapbox.android.telemetry.MapboxTelemetry;
import com.mapbox.android.telemetry.MapboxTelemetryConstants;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class LocationCollectionClient implements SharedPreferences.OnSharedPreferenceChangeListener {
    public static final int DEFAULT_SESSION_ROTATION_INTERVAL_HOURS = 24;
    private static final int LOCATION_COLLECTION_STATUS_UPDATED = 0;
    private static final String LOCATION_COLLECTOR_USER_AGENT = "mapbox-android-location";
    private static final String TAG = "LocationCollectionCli";
    private static LocationCollectionClient locationCollectionClient;
    private static final Object lock = new Object();
    private final AtomicBoolean isEnabled = new AtomicBoolean(false);
    @VisibleForTesting
    final LocationEngineController locationEngineController;
    private final AtomicReference<SessionIdentifier> sessionIdentifier = new AtomicReference<>();
    private Handler settingsChangeHandler;
    private final HandlerThread settingsChangeHandlerThread;
    private final SharedPreferences sharedPreferences;
    private final MapboxTelemetry telemetry;

    @VisibleForTesting
    LocationCollectionClient(@NonNull LocationEngineController collectionController, @NonNull HandlerThread handlerThread, @NonNull SessionIdentifier sessionIdentifier2, @NonNull SharedPreferences sharedPreferences2, @NonNull MapboxTelemetry telemetry2) {
        this.locationEngineController = collectionController;
        this.settingsChangeHandlerThread = handlerThread;
        this.sessionIdentifier.set(sessionIdentifier2);
        this.telemetry = telemetry2;
        this.settingsChangeHandlerThread.start();
        this.settingsChangeHandler = new Handler(handlerThread.getLooper()) {
            /* class com.mapbox.android.telemetry.location.LocationCollectionClient.AnonymousClass1 */

            public void handleMessage(Message msg) {
                try {
                    LocationCollectionClient.this.handleSettingsChangeMessage(msg);
                } catch (Throwable throwable) {
                    Log.e(LocationCollectionClient.TAG, throwable.toString());
                }
            }
        };
        this.sharedPreferences = sharedPreferences2;
        initializeSharedPreferences(sharedPreferences2);
    }

    public static LocationCollectionClient install(@NonNull Context context, long defaultInterval) {
        Context applicationContext;
        if (context.getApplicationContext() == null) {
            applicationContext = context;
        } else {
            applicationContext = context.getApplicationContext();
        }
        synchronized (lock) {
            if (locationCollectionClient == null) {
                locationCollectionClient = new LocationCollectionClient(new LocationEngineControllerImpl(applicationContext, LocationEngineProvider.getBestLocationEngine(applicationContext), new LocationUpdatesBroadcastReceiver()), new HandlerThread("LocationSettingsChangeThread"), new SessionIdentifier(defaultInterval), applicationContext.getSharedPreferences("MapboxSharedPreferences", 0), new MapboxTelemetry(applicationContext, "", String.format("%s/%s", LOCATION_COLLECTOR_USER_AGENT, BuildConfig.VERSION_NAME)));
            }
        }
        return locationCollectionClient;
    }

    static boolean uninstall() {
        boolean uninstalled = false;
        synchronized (lock) {
            if (locationCollectionClient != null) {
                locationCollectionClient.locationEngineController.onDestroy();
                locationCollectionClient.settingsChangeHandlerThread.quit();
                locationCollectionClient.sharedPreferences.unregisterOnSharedPreferenceChangeListener(locationCollectionClient);
                locationCollectionClient = null;
                uninstalled = true;
            }
        }
        return uninstalled;
    }

    @NonNull
    static LocationCollectionClient getInstance() {
        LocationCollectionClient locationCollectionClient2;
        synchronized (lock) {
            if (locationCollectionClient != null) {
                locationCollectionClient2 = locationCollectionClient;
            } else {
                throw new IllegalStateException("LocationCollectionClient is not installed.");
            }
        }
        return locationCollectionClient2;
    }

    /* access modifiers changed from: package-private */
    public void setSessionRotationInterval(long interval) {
        this.sessionIdentifier.set(new SessionIdentifier(interval));
    }

    /* access modifiers changed from: package-private */
    public long getSessionRotationInterval() {
        return this.sessionIdentifier.get().getInterval();
    }

    /* access modifiers changed from: package-private */
    public String getSessionId() {
        return this.sessionIdentifier.get().getSessionId();
    }

    /* access modifiers changed from: package-private */
    public boolean isEnabled() {
        return this.isEnabled.get();
    }

    /* access modifiers changed from: package-private */
    public void setEnabled(boolean enabled) {
        boolean z;
        AtomicBoolean atomicBoolean = this.isEnabled;
        if (!enabled) {
            z = true;
        } else {
            z = false;
        }
        if (atomicBoolean.compareAndSet(z, enabled)) {
            this.settingsChangeHandler.sendEmptyMessage(0);
        }
    }

    /* access modifiers changed from: package-private */
    public MapboxTelemetry getTelemetry() {
        return this.telemetry;
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public void setMockHandler(Handler mockHandler) {
        this.settingsChangeHandler = mockHandler;
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public void handleSettingsChangeMessage(Message msg) {
        switch (msg.what) {
            case 0:
                if (isEnabled()) {
                    this.locationEngineController.onResume();
                    this.telemetry.enable();
                    return;
                }
                this.locationEngineController.onDestroy();
                this.telemetry.disable();
                return;
            default:
                return;
        }
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences2, String key) {
        try {
            if (MapboxTelemetryConstants.LOCATION_COLLECTOR_ENABLED.equals(key)) {
                setEnabled(sharedPreferences2.getBoolean(MapboxTelemetryConstants.LOCATION_COLLECTOR_ENABLED, false));
            } else if (MapboxTelemetryConstants.SESSION_ROTATION_INTERVAL_MILLIS.equals(key)) {
                setSessionRotationInterval(sharedPreferences2.getLong(MapboxTelemetryConstants.SESSION_ROTATION_INTERVAL_MILLIS, TimeUnit.HOURS.toMillis(24)));
            }
        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
        }
    }

    private void initializeSharedPreferences(SharedPreferences sharedPreferences2) {
        SharedPreferences.Editor editor = sharedPreferences2.edit();
        editor.putBoolean(MapboxTelemetryConstants.LOCATION_COLLECTOR_ENABLED, this.isEnabled.get());
        editor.putLong(MapboxTelemetryConstants.SESSION_ROTATION_INTERVAL_MILLIS, this.sessionIdentifier.get().getInterval());
        editor.apply();
        sharedPreferences2.registerOnSharedPreferenceChangeListener(this);
    }
}
