package com.dji.mapkit.lbs;

import android.content.Context;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.dji.mapkit.core.models.DJILatLng;
import com.dji.mapkit.lbs.DJILocationManager;
import com.dji.mapkit.lbs.configuration.DJILocationConfigurations;

public class DJILocationUtils {
    private static DJILocationManager sLocationManager;

    public static void injectLocationManager(DJILocationManager sLocationManager2) {
        sLocationManager = sLocationManager2;
    }

    @MainThread
    public static void init(@NonNull Context applicationContext, boolean useAMapService) {
        if (sLocationManager == null) {
            sLocationManager = new DJILocationManager.Builder(applicationContext).configuration(DJILocationConfigurations.defaultConfiguration(false, useAMapService)).build();
        }
    }

    @Nullable
    public static DJILatLng getLastKnownLocation() {
        return sLocationManager.getLastKnownLocation();
    }

    public static float getLastKnownLocationAccuracy() {
        DJILatLng lastKnownLocation = getLastKnownLocation();
        if (lastKnownLocation != null) {
            return lastKnownLocation.getAccuracy();
        }
        return 10000.0f;
    }

    public static boolean isAvailable(@Nullable DJILatLng latLng) {
        return latLng != null && Math.abs(latLng.latitude) > 1.0E-8d && Math.abs(latLng.longitude) > 1.0E-8d && Math.abs(latLng.latitude) <= 90.0d && Math.abs(latLng.longitude) <= 180.0d;
    }

    public static void destroy() {
        sLocationManager.onDestroy();
    }
}
