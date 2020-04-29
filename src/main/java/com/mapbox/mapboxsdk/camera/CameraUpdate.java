package com.mapbox.mapboxsdk.camera;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.mapbox.mapboxsdk.maps.MapboxMap;

public interface CameraUpdate {
    @Nullable
    CameraPosition getCameraPosition(@NonNull MapboxMap mapboxMap);
}
