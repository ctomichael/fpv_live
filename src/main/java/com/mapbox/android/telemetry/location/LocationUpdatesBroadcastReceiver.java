package com.mapbox.android.telemetry.location;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.android.telemetry.MapboxTelemetry;

public class LocationUpdatesBroadcastReceiver extends BroadcastReceiver {
    static final String ACTION_LOCATION_UPDATED = "com.mapbox.android.telemetry.location.locationupdatespendingintent.action.LOCATION_UPDATED";
    private static final String TAG = "LocationUpdateReceiver";

    public void onReceive(Context context, Intent intent) {
        if (intent == null) {
            try {
                Log.w(TAG, "intent == null");
            } catch (Throwable throwable) {
                Log.e(TAG, throwable.toString());
            }
        } else if (ACTION_LOCATION_UPDATED.equals(intent.getAction())) {
            LocationEngineResult result = LocationEngineResult.extractResult(intent);
            if (result == null) {
                Log.w(TAG, "LocationEngineResult == null");
                return;
            }
            LocationCollectionClient collectionClient = LocationCollectionClient.getInstance();
            MapboxTelemetry telemetry = collectionClient.getTelemetry();
            String sessionId = collectionClient.getSessionId();
            for (Location location : result.getLocations()) {
                if (!isThereAnyNaN(location) && !isThereAnyInfinite(location)) {
                    telemetry.push(LocationMapper.create(location, sessionId));
                }
            }
        }
    }

    private static boolean isThereAnyNaN(Location location) {
        return Double.isNaN(location.getLatitude()) || Double.isNaN(location.getLongitude()) || Double.isNaN(location.getAltitude()) || Float.isNaN(location.getAccuracy());
    }

    private static boolean isThereAnyInfinite(Location location) {
        return Double.isInfinite(location.getLatitude()) || Double.isInfinite(location.getLongitude()) || Double.isInfinite(location.getAltitude()) || Float.isInfinite(location.getAccuracy());
    }
}
