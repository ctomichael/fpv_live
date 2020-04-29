package com.mapzen.android.lost.internal;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import com.mapzen.android.lost.api.LocationRequest;
import com.mapzen.android.lost.internal.LocationEngine;
import dji.publics.protocol.ResponseBase;
import java.util.List;
import kotlin.jvm.internal.LongCompanionObject;

public class FusionEngine extends LocationEngine implements LocationListener {
    public static final long RECENT_UPDATE_THRESHOLD_IN_MILLIS = 60000;
    public static final long RECENT_UPDATE_THRESHOLD_IN_NANOS = 60000000000L;
    private static final String TAG = FusionEngine.class.getSimpleName();
    static Clock clock = new SystemClock();
    private Location gpsLocation;
    private final LocationManager locationManager;
    private Location networkLocation;

    public FusionEngine(Context context, LocationEngine.Callback callback) {
        super(context, callback);
        this.locationManager = (LocationManager) context.getSystemService(ResponseBase.STRING_LOCATION);
    }

    public Location getLastLocation() {
        List<String> providers = this.locationManager.getAllProviders();
        long minTime = clock.getSystemElapsedTimeInNanos() - RECENT_UPDATE_THRESHOLD_IN_NANOS;
        Location bestLocation = null;
        float bestAccuracy = Float.MAX_VALUE;
        long bestTime = Long.MIN_VALUE;
        for (String provider : providers) {
            try {
                Location location = this.locationManager.getLastKnownLocation(provider);
                if (location != null) {
                    float accuracy = location.getAccuracy();
                    long time = clock.getElapsedTimeInNanos(location);
                    if (time > minTime && accuracy < bestAccuracy) {
                        bestLocation = location;
                        bestAccuracy = accuracy;
                        bestTime = time;
                    } else if (time < minTime && bestAccuracy == Float.MAX_VALUE && time > bestTime) {
                        bestLocation = location;
                        bestTime = time;
                    }
                }
            } catch (SecurityException e) {
                Log.e(TAG, "Permissions not granted for provider: " + provider, e);
            }
        }
        return bestLocation;
    }

    public boolean isProviderEnabled(String provider) {
        return this.locationManager.isProviderEnabled(provider);
    }

    /* access modifiers changed from: protected */
    public void enable() {
        long networkInterval = LongCompanionObject.MAX_VALUE;
        long gpsInterval = LongCompanionObject.MAX_VALUE;
        long passiveInterval = LongCompanionObject.MAX_VALUE;
        for (LocationRequest request : getRequest().getRequests()) {
            switch (request.getPriority()) {
                case 100:
                    if (request.getInterval() < gpsInterval) {
                        gpsInterval = request.getInterval();
                    }
                    if (request.getInterval() >= networkInterval) {
                        break;
                    } else {
                        networkInterval = request.getInterval();
                        break;
                    }
                case 102:
                case 104:
                    if (request.getInterval() >= networkInterval) {
                        break;
                    } else {
                        networkInterval = request.getInterval();
                        break;
                    }
                case 105:
                    if (request.getInterval() >= passiveInterval) {
                        break;
                    } else {
                        passiveInterval = request.getInterval();
                        break;
                    }
            }
        }
        boolean checkGps = false;
        if (gpsInterval < LongCompanionObject.MAX_VALUE) {
            enableGps(gpsInterval);
            checkGps = true;
        }
        if (networkInterval < LongCompanionObject.MAX_VALUE) {
            enableNetwork(networkInterval);
            if (checkGps) {
                Location lastGps = this.locationManager.getLastKnownLocation("gps");
                Location lastNetwork = this.locationManager.getLastKnownLocation("network");
                if (lastGps == null || lastNetwork == null) {
                    if (lastGps != null) {
                        checkLastKnownGps();
                    } else {
                        checkLastKnownNetwork();
                    }
                } else if (isBetterThan(lastGps, lastNetwork)) {
                    checkLastKnownGps();
                } else {
                    checkLastKnownNetwork();
                }
            } else {
                checkLastKnownNetwork();
            }
        }
        if (passiveInterval < LongCompanionObject.MAX_VALUE) {
            enablePassive(passiveInterval);
            checkLastKnownPassive();
        }
    }

    /* access modifiers changed from: protected */
    public void disable() throws SecurityException {
        if (this.locationManager != null) {
            this.locationManager.removeUpdates(this);
        }
    }

    private void enableGps(long interval) throws SecurityException {
        try {
            this.locationManager.requestLocationUpdates("gps", interval, 0.0f, this, getLooper());
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "Unable to register for GPS updates.", e);
        }
    }

    private void enableNetwork(long interval) throws SecurityException {
        try {
            this.locationManager.requestLocationUpdates("network", interval, 0.0f, this, getLooper());
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "Unable to register for network updates.", e);
        }
    }

    private void enablePassive(long interval) throws SecurityException {
        try {
            this.locationManager.requestLocationUpdates("passive", interval, 0.0f, this, getLooper());
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "Unable to register for passive updates.", e);
        }
    }

    private void checkLastKnownGps() {
        checkLastKnownAndNotify("gps");
    }

    private void checkLastKnownNetwork() {
        checkLastKnownAndNotify("network");
    }

    private void checkLastKnownPassive() {
        checkLastKnownAndNotify("passive");
    }

    private void checkLastKnownAndNotify(String provider) {
        Location location = this.locationManager.getLastKnownLocation(provider);
        if (location != null) {
            onLocationChanged(location);
        }
    }

    public void onLocationChanged(Location location) {
        if ("gps".equals(location.getProvider())) {
            this.gpsLocation = location;
            if (getCallback() != null && isBetterThan(this.gpsLocation, this.networkLocation)) {
                getCallback().reportLocation(location);
            }
        } else if ("network".equals(location.getProvider())) {
            this.networkLocation = location;
            if (getCallback() != null && isBetterThan(this.networkLocation, this.gpsLocation)) {
                getCallback().reportLocation(location);
            }
        }
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    public void onProviderEnabled(String provider) {
        LocationEngine.Callback callback = getCallback();
        if (callback != null) {
            callback.reportProviderEnabled(provider);
        }
    }

    public void onProviderDisabled(String provider) {
        LocationEngine.Callback callback = getCallback();
        if (callback != null) {
            callback.reportProviderDisabled(provider);
        }
    }

    public static boolean isBetterThan(Location locationA, Location locationB) {
        if (locationA == null) {
            return false;
        }
        if (locationB == null || clock.getElapsedTimeInNanos(locationA) > clock.getElapsedTimeInNanos(locationB) + RECENT_UPDATE_THRESHOLD_IN_NANOS) {
            return true;
        }
        if (!locationA.hasAccuracy()) {
            return false;
        }
        if (!locationB.hasAccuracy() || locationA.getAccuracy() < locationB.getAccuracy()) {
            return true;
        }
        return false;
    }
}
