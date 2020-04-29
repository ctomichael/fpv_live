package com.mapbox.mapboxsdk.location;

import android.support.annotation.NonNull;

public interface CompassEngine {
    void addCompassListener(@NonNull CompassListener compassListener);

    int getLastAccuracySensorStatus();

    float getLastHeading();

    void removeCompassListener(@NonNull CompassListener compassListener);
}
