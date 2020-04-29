package com.dji.mapkit.core.camera;

import android.support.annotation.NonNull;
import com.dji.mapkit.core.maps.DJIMap;
import com.dji.mapkit.core.models.DJICameraPosition;
import com.dji.mapkit.core.models.DJILatLng;

public interface DJICameraUpdate {
    float getBearing();

    DJICameraPosition getCameraPosition(@NonNull DJIMap dJIMap);

    DJILatLng getTarget();

    float getTilt();

    float getZoom();
}
