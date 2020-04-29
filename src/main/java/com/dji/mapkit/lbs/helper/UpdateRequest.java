package com.dji.mapkit.lbs.helper;

import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Looper;

public class UpdateRequest {
    private final LocationListener locationListener;
    private final LocationManager locationManager;
    private float minDistance;
    private long minTime;
    private String provider;

    public UpdateRequest(LocationManager locationManager2, LocationListener locationListener2) {
        this.locationManager = locationManager2;
        this.locationListener = locationListener2;
    }

    public boolean isRequiredImmediately() {
        return this.minTime == 0;
    }

    public void run(String provider2, long minTime2, float minDistance2) {
        this.provider = provider2;
        this.minTime = minTime2;
        this.minDistance = minDistance2;
        run();
    }

    public void run() {
        if (StringUtils.isNotEmpty(this.provider) && this.locationManager.isProviderEnabled(this.provider)) {
            this.locationManager.requestLocationUpdates(this.provider, this.minTime, this.minDistance, this.locationListener, Looper.getMainLooper());
        }
    }

    public void release() {
        this.locationManager.removeUpdates(this.locationListener);
    }
}
