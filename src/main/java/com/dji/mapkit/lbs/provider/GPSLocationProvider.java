package com.dji.mapkit.lbs.provider;

import com.dji.mapkit.lbs.DJILocationLog;

public class GPSLocationProvider extends DefaultLocationProvider {
    public void get() {
        setWaiting(true);
        if (isGPSProviderEnabled()) {
            DJILocationLog.LOGI("GPS is already enabled, getting location...");
            askForLocation("gps");
        }
    }
}
