package com.mapzen.android.lost.internal;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Looper;
import android.support.annotation.RequiresPermission;
import com.dji.permission.Permission;
import com.mapzen.android.lost.api.LocationAvailability;
import com.mapzen.android.lost.api.LocationRequest;
import dji.publics.protocol.ResponseBase;
import java.util.List;

public abstract class LocationEngine {
    private final Callback callback;
    private final Context context;
    private LocationRequestUnbundled request = new LocationRequestUnbundled();

    public interface Callback {
        void reportLocation(Location location);

        void reportProviderDisabled(String str);

        void reportProviderEnabled(String str);
    }

    /* access modifiers changed from: protected */
    public abstract void disable();

    /* access modifiers changed from: protected */
    public abstract void enable();

    public abstract Location getLastLocation();

    public abstract boolean isProviderEnabled(String str);

    public LocationEngine(Context context2, Callback callback2) {
        this.context = context2;
        this.callback = callback2;
    }

    public void addRequest(LocationRequest request2) {
        if (request2 != null) {
            this.request.addRequest(request2);
            enable();
        }
    }

    public void removeRequests(List<LocationRequest> requests) {
        if (this.request != null) {
            this.request.removeRequests(requests);
            disable();
            if (!this.request.getRequests().isEmpty()) {
                enable();
            }
        }
    }

    public void removeAllRequests() {
        this.request.removeAllRequests();
        disable();
    }

    /* access modifiers changed from: protected */
    public Context getContext() {
        return this.context;
    }

    /* access modifiers changed from: protected */
    public Looper getLooper() {
        return this.context.getMainLooper();
    }

    /* access modifiers changed from: protected */
    public Callback getCallback() {
        return this.callback;
    }

    /* access modifiers changed from: protected */
    public LocationRequestUnbundled getRequest() {
        return this.request;
    }

    /* access modifiers changed from: protected */
    @RequiresPermission(anyOf = {Permission.ACCESS_COARSE_LOCATION, Permission.ACCESS_FINE_LOCATION})
    public LocationAvailability createLocationAvailability() {
        boolean gpsLocationExists;
        boolean networkLocationExists;
        boolean z = true;
        LocationManager locationManager = (LocationManager) this.context.getSystemService(ResponseBase.STRING_LOCATION);
        boolean gpsEnabled = locationManager.isProviderEnabled("gps");
        boolean networkEnabled = locationManager.isProviderEnabled("network");
        if (locationManager.getLastKnownLocation("gps") != null) {
            gpsLocationExists = true;
        } else {
            gpsLocationExists = false;
        }
        if (locationManager.getLastKnownLocation("network") != null) {
            networkLocationExists = true;
        } else {
            networkLocationExists = false;
        }
        if ((!gpsEnabled || !gpsLocationExists) && (!networkEnabled || !networkLocationExists)) {
            z = false;
        }
        return new LocationAvailability(z);
    }
}
