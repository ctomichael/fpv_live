package com.mapbox.android.telemetry.location;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import com.dji.permission.Permission;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineRequest;

class LocationEngineControllerImpl implements LocationEngineController {
    private static final long DEFAULT_INTERVAL_IN_MILLISECONDS = 1000;
    private static final long DEFAULT_MAX_WAIT_TIME = 5000;
    private static final String TAG = "LocationController";
    private final Context applicationContext;
    private final LocationEngine locationEngine;
    private final LocationUpdatesBroadcastReceiver locationUpdatesBroadcastReceiver;

    LocationEngineControllerImpl(@NonNull Context context, @NonNull LocationEngine locationEngine2, @NonNull LocationUpdatesBroadcastReceiver locationUpdatesBroadcastReceiver2) {
        this.applicationContext = context;
        this.locationEngine = locationEngine2;
        this.locationUpdatesBroadcastReceiver = locationUpdatesBroadcastReceiver2;
    }

    public void onPause() {
    }

    public void onResume() {
        registerReceiver();
        requestLocationUpdates();
    }

    public void onDestroy() {
        removeLocationUpdates();
        unregisterReceiver();
    }

    private void registerReceiver() {
        try {
            this.applicationContext.registerReceiver(this.locationUpdatesBroadcastReceiver, new IntentFilter("com.mapbox.android.telemetry.location.locationupdatespendingintent.action.LOCATION_UPDATED"));
        } catch (IllegalArgumentException iae) {
            Log.e(TAG, iae.toString());
        }
    }

    private void unregisterReceiver() {
        try {
            this.applicationContext.unregisterReceiver(this.locationUpdatesBroadcastReceiver);
        } catch (IllegalArgumentException iae) {
            Log.e(TAG, iae.toString());
        }
    }

    private void requestLocationUpdates() {
        if (!checkPermissions()) {
            Log.w(TAG, "Location permissions are not granted");
            return;
        }
        try {
            this.locationEngine.requestLocationUpdates(createRequest(1000), getPendingIntent());
        } catch (SecurityException se) {
            Log.e(TAG, se.toString());
        }
    }

    private void removeLocationUpdates() {
        this.locationEngine.removeLocationUpdates(getPendingIntent());
    }

    private PendingIntent getPendingIntent() {
        return PendingIntent.getBroadcast(this.applicationContext, 0, new Intent("com.mapbox.android.telemetry.location.locationupdatespendingintent.action.LOCATION_UPDATED"), 134217728);
    }

    private boolean checkPermissions() {
        return ContextCompat.checkSelfPermission(this.applicationContext, Permission.ACCESS_FINE_LOCATION) == 0;
    }

    private static LocationEngineRequest createRequest(long interval) {
        return new LocationEngineRequest.Builder(interval).setPriority(3).setMaxWaitTime(DEFAULT_MAX_WAIT_TIME).build();
    }
}
