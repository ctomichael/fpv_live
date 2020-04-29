package com.dji.mapkit.core.utils;

import android.location.Location;
import android.text.TextUtils;
import com.dji.mapkit.core.models.DJILatLng;

public class DJIMapLocationUtils {
    private static final int MAX_ACCURACY = 40;

    public static Location getBestLocation(Location previous, Location last) {
        Location location;
        if (previous == null && last == null) {
            return null;
        }
        if (previous == null) {
            return last;
        }
        if (last == null) {
            return previous;
        }
        if (previous.getAccuracy() == 0.0f && last.getAccuracy() == 0.0f) {
            return last;
        }
        if (previous.getAccuracy() == 0.0f && last.getAccuracy() != 0.0f) {
            return last;
        }
        if (previous.getAccuracy() != 0.0f && last.getAccuracy() == 0.0f) {
            return previous;
        }
        if (previous.getAccuracy() >= 40.0f || last.getAccuracy() >= 40.0f) {
            location = previous.getAccuracy() < last.getAccuracy() ? previous : last;
        } else if (TextUtils.equals(previous.getProvider(), last.getProvider())) {
            location = previous.getTime() < last.getTime() ? last : previous;
        } else if ("gps".equalsIgnoreCase(previous.getProvider())) {
            return previous;
        } else {
            if ("gps".equalsIgnoreCase(last.getProvider())) {
                return last;
            }
            location = previous.getAccuracy() < last.getAccuracy() ? previous : last;
        }
        return location;
    }

    public static float getDistance(DJILatLng p1, DJILatLng p2) {
        float[] results = new float[1];
        Location.distanceBetween(p1.latitude, p1.longitude, p2.latitude, p2.longitude, results);
        return results[0];
    }
}
