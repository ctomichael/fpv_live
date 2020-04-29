package com.mapbox.mapboxsdk.location;

public interface CompassListener {
    void onCompassAccuracyChange(int i);

    void onCompassChanged(float f);
}
