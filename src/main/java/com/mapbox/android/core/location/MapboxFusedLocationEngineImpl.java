package com.mapbox.android.core.location;

import android.app.PendingIntent;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

class MapboxFusedLocationEngineImpl extends AndroidLocationEngineImpl {
    private static final String TAG = "MapboxLocationEngine";

    MapboxFusedLocationEngineImpl(@NonNull Context context) {
        super(context);
    }

    @NonNull
    public LocationListener createListener(LocationEngineCallback<LocationEngineResult> callback) {
        return new MapboxLocationEngineCallbackTransport(callback);
    }

    public void getLastLocation(@NonNull LocationEngineCallback<LocationEngineResult> callback) throws SecurityException {
        Location bestLastLocation = getBestLastLocation();
        if (bestLastLocation != null) {
            callback.onSuccess(LocationEngineResult.create(bestLastLocation));
        } else {
            callback.onFailure(new Exception("Last location unavailable"));
        }
    }

    public void requestLocationUpdates(@NonNull LocationEngineRequest request, @NonNull LocationListener listener, @Nullable Looper looper) throws SecurityException {
        super.requestLocationUpdates(request, listener, looper);
        if (shouldStartNetworkProvider(request.getPriority())) {
            try {
                this.locationManager.requestLocationUpdates("network", request.getInterval(), request.getDisplacemnt(), listener, looper);
            } catch (IllegalArgumentException iae) {
                iae.printStackTrace();
            }
        }
    }

    public void requestLocationUpdates(@NonNull LocationEngineRequest request, @NonNull PendingIntent pendingIntent) throws SecurityException {
        super.requestLocationUpdates(request, pendingIntent);
        if (shouldStartNetworkProvider(request.getPriority())) {
            try {
                this.locationManager.requestLocationUpdates("network", request.getInterval(), request.getDisplacemnt(), pendingIntent);
            } catch (IllegalArgumentException iae) {
                iae.printStackTrace();
            }
        }
    }

    private Location getBestLastLocation() {
        Location bestLastLocation = null;
        for (String provider : this.locationManager.getAllProviders()) {
            Location location = getLastLocationFor(provider);
            if (location != null && Utils.isBetterLocation(location, bestLastLocation)) {
                bestLastLocation = location;
            }
        }
        return bestLastLocation;
    }

    private boolean shouldStartNetworkProvider(int priority) {
        if ((priority == 0 || priority == 1) && this.currentProvider.equals("gps")) {
            return true;
        }
        return false;
    }

    private static final class MapboxLocationEngineCallbackTransport implements LocationListener {
        private final LocationEngineCallback<LocationEngineResult> callback;
        private Location currentBestLocation;

        MapboxLocationEngineCallbackTransport(LocationEngineCallback<LocationEngineResult> callback2) {
            this.callback = callback2;
        }

        public void onLocationChanged(Location location) {
            if (Utils.isBetterLocation(location, this.currentBestLocation)) {
                this.currentBestLocation = location;
            }
            if (this.callback != null) {
                this.callback.onSuccess(LocationEngineResult.create(this.currentBestLocation));
            }
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d(MapboxFusedLocationEngineImpl.TAG, "onStatusChanged: " + provider);
        }

        public void onProviderEnabled(String provider) {
            Log.d(MapboxFusedLocationEngineImpl.TAG, "onProviderEnabled: " + provider);
        }

        public void onProviderDisabled(String provider) {
            Log.d(MapboxFusedLocationEngineImpl.TAG, "onProviderDisabled: " + provider);
        }
    }
}
