package com.mapzen.android.lost.internal;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.os.RemoteException;
import android.support.annotation.RequiresPermission;
import android.support.annotation.VisibleForTesting;
import android.util.Log;
import com.dji.permission.Permission;
import com.mapzen.android.lost.api.LocationAvailability;
import com.mapzen.android.lost.api.LocationRequest;
import com.mapzen.android.lost.internal.LocationEngine;
import dji.publics.protocol.ResponseBase;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FusedLocationProviderServiceDelegate implements LocationEngine.Callback {
    private static final String TAG = FusedLocationProviderServiceDelegate.class.getSimpleName();
    private Map<Long, IFusedLocationProviderCallback> callbacks = new HashMap();
    private Context context;
    private LocationEngine locationEngine;
    private boolean mockMode;

    public FusedLocationProviderServiceDelegate(Context context2) {
        this.context = context2;
        this.locationEngine = new FusionEngine(context2, this);
    }

    public void add(IFusedLocationProviderCallback callback) {
        try {
            this.callbacks.put(Long.valueOf(callback.pid()), callback);
        } catch (RemoteException e) {
            Log.e(TAG, "Error getting callback's unique id", e);
        }
    }

    public void remove(IFusedLocationProviderCallback callback) {
        try {
            this.callbacks.remove(Long.valueOf(callback.pid()));
        } catch (RemoteException e) {
            Log.e(TAG, "Error getting callback's unique id", e);
        }
    }

    public Location getLastLocation() {
        return this.locationEngine.getLastLocation();
    }

    @RequiresPermission(anyOf = {Permission.ACCESS_COARSE_LOCATION, Permission.ACCESS_FINE_LOCATION})
    public LocationAvailability getLocationAvailability() {
        return this.locationEngine.createLocationAvailability();
    }

    public void requestLocationUpdates(LocationRequest request) {
        this.locationEngine.addRequest(request);
    }

    public void removeLocationUpdates(List<LocationRequest> requests) {
        this.locationEngine.removeRequests(requests);
    }

    public void setMockMode(boolean isMockMode) {
        if (this.mockMode != isMockMode) {
            toggleMockMode();
        }
    }

    public void setMockLocation(Location mockLocation) {
        if (this.mockMode) {
            ((MockEngine) this.locationEngine).setLocation(mockLocation);
        }
    }

    public void setMockTrace(String path, String filename) {
        if (this.mockMode) {
            ((MockEngine) this.locationEngine).setTrace(new File(path, filename));
        }
    }

    @RequiresPermission(anyOf = {Permission.ACCESS_COARSE_LOCATION, Permission.ACCESS_FINE_LOCATION})
    public void reportLocation(Location location) {
        for (IFusedLocationProviderCallback callback : this.callbacks.values()) {
            try {
                callback.onLocationChanged(location);
            } catch (RemoteException e) {
                Log.e(TAG, "Error occurred trying to report a new Location", e);
            }
        }
    }

    @RequiresPermission(anyOf = {Permission.ACCESS_COARSE_LOCATION, Permission.ACCESS_FINE_LOCATION})
    public void reportProviderDisabled(String provider) {
        notifyLocationAvailabilityChanged();
    }

    @RequiresPermission(anyOf = {Permission.ACCESS_COARSE_LOCATION, Permission.ACCESS_FINE_LOCATION})
    public void reportProviderEnabled(String provider) {
        notifyLocationAvailabilityChanged();
        ((LocationManager) this.context.getSystemService(ResponseBase.STRING_LOCATION)).requestSingleUpdate(provider, new LocationListener() {
            /* class com.mapzen.android.lost.internal.FusedLocationProviderServiceDelegate.AnonymousClass1 */

            public void onLocationChanged(Location location) {
                FusedLocationProviderServiceDelegate.this.notifyLocationAvailabilityChanged();
            }

            public void onStatusChanged(String s, int i, Bundle bundle) {
            }

            public void onProviderEnabled(String s) {
            }

            public void onProviderDisabled(String s) {
            }
        }, Looper.myLooper());
    }

    private void toggleMockMode() {
        this.mockMode = !this.mockMode;
        this.locationEngine.removeAllRequests();
        if (this.mockMode) {
            this.locationEngine = new MockEngine(this.context, this, new GpxTraceThreadFactory());
        } else {
            this.locationEngine = new FusionEngine(this.context, this);
        }
    }

    /* access modifiers changed from: private */
    @RequiresPermission(anyOf = {Permission.ACCESS_COARSE_LOCATION, Permission.ACCESS_FINE_LOCATION})
    public void notifyLocationAvailabilityChanged() {
        LocationAvailability availability = this.locationEngine.createLocationAvailability();
        for (IFusedLocationProviderCallback callback : this.callbacks.values()) {
            try {
                callback.onLocationAvailabilityChanged(availability);
            } catch (RemoteException e) {
                Log.e(TAG, "Error occurred trying to report a new LocationAvailability", e);
            }
        }
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public Map getCallbacks() {
        return this.callbacks;
    }
}
