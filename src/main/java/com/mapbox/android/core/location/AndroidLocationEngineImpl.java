package com.mapbox.android.core.location;

import android.app.PendingIntent;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.util.Log;
import dji.publics.protocol.ResponseBase;

class AndroidLocationEngineImpl implements LocationEngineImpl<LocationListener> {
    private static final String TAG = "AndroidLocationEngine";
    String currentProvider = "passive";
    final LocationManager locationManager;

    AndroidLocationEngineImpl(@NonNull Context context) {
        this.locationManager = (LocationManager) context.getSystemService(ResponseBase.STRING_LOCATION);
    }

    @NonNull
    public LocationListener createListener(LocationEngineCallback<LocationEngineResult> callback) {
        return new AndroidLocationEngineCallbackTransport(callback);
    }

    public void getLastLocation(@NonNull LocationEngineCallback<LocationEngineResult> callback) throws SecurityException {
        Location lastLocation = getLastLocationFor(this.currentProvider);
        if (lastLocation != null) {
            callback.onSuccess(LocationEngineResult.create(lastLocation));
            return;
        }
        for (String provider : this.locationManager.getAllProviders()) {
            Location lastLocation2 = getLastLocationFor(provider);
            if (lastLocation2 != null) {
                callback.onSuccess(LocationEngineResult.create(lastLocation2));
                return;
            }
        }
        callback.onFailure(new Exception("Last location unavailable"));
    }

    /* access modifiers changed from: package-private */
    public Location getLastLocationFor(String provider) throws SecurityException {
        try {
            return this.locationManager.getLastKnownLocation(provider);
        } catch (IllegalArgumentException iae) {
            Log.e(TAG, iae.toString());
            return null;
        }
    }

    public void requestLocationUpdates(@NonNull LocationEngineRequest request, @NonNull LocationListener listener, @Nullable Looper looper) throws SecurityException {
        this.currentProvider = getBestProvider(request.getPriority());
        this.locationManager.requestLocationUpdates(this.currentProvider, request.getInterval(), request.getDisplacemnt(), listener, looper);
    }

    public void requestLocationUpdates(@NonNull LocationEngineRequest request, @NonNull PendingIntent pendingIntent) throws SecurityException {
        this.currentProvider = getBestProvider(request.getPriority());
        this.locationManager.requestLocationUpdates(this.currentProvider, request.getInterval(), request.getDisplacemnt(), pendingIntent);
    }

    public void removeLocationUpdates(@NonNull LocationListener listener) {
        if (listener != null) {
            this.locationManager.removeUpdates(listener);
        }
    }

    public void removeLocationUpdates(PendingIntent pendingIntent) {
        if (pendingIntent != null) {
            this.locationManager.removeUpdates(pendingIntent);
        }
    }

    private String getBestProvider(int priority) {
        String provider = null;
        if (priority != 3) {
            provider = this.locationManager.getBestProvider(getCriteria(priority), true);
        }
        return provider != null ? provider : "passive";
    }

    @VisibleForTesting
    static Criteria getCriteria(int priority) {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(priorityToAccuracy(priority));
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(priorityToPowerRequirement(priority));
        return criteria;
    }

    private static int priorityToAccuracy(int priority) {
        switch (priority) {
            case 0:
            case 1:
                return 1;
            default:
                return 2;
        }
    }

    private static int priorityToPowerRequirement(int priority) {
        switch (priority) {
            case 0:
                return 3;
            case 1:
                return 2;
            default:
                return 1;
        }
    }

    @VisibleForTesting
    static final class AndroidLocationEngineCallbackTransport implements LocationListener {
        private final LocationEngineCallback<LocationEngineResult> callback;

        AndroidLocationEngineCallbackTransport(LocationEngineCallback<LocationEngineResult> callback2) {
            this.callback = callback2;
        }

        public void onLocationChanged(Location location) {
            this.callback.onSuccess(LocationEngineResult.create(location));
        }

        public void onStatusChanged(String s, int i, Bundle bundle) {
        }

        public void onProviderEnabled(String s) {
        }

        public void onProviderDisabled(String s) {
            this.callback.onFailure(new Exception("Current provider disabled"));
        }
    }
}
