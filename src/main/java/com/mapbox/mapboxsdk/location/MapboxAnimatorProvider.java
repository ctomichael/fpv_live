package com.mapbox.mapboxsdk.location;

import android.support.annotation.Nullable;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.MapboxAnimator;
import com.mapbox.mapboxsdk.maps.MapboxMap;

final class MapboxAnimatorProvider {
    private static MapboxAnimatorProvider INSTANCE;

    private MapboxAnimatorProvider() {
    }

    public static MapboxAnimatorProvider getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MapboxAnimatorProvider();
        }
        return INSTANCE;
    }

    /* access modifiers changed from: package-private */
    public MapboxLatLngAnimator latLngAnimator(LatLng previous, LatLng target, MapboxAnimator.AnimationsValueChangeListener updateListener, int maxAnimationFps) {
        return new MapboxLatLngAnimator(previous, target, updateListener, maxAnimationFps);
    }

    /* access modifiers changed from: package-private */
    public MapboxFloatAnimator floatAnimator(Float previous, Float target, MapboxAnimator.AnimationsValueChangeListener updateListener, int maxAnimationFps) {
        return new MapboxFloatAnimator(previous, target, updateListener, maxAnimationFps);
    }

    /* access modifiers changed from: package-private */
    public MapboxCameraAnimatorAdapter cameraAnimator(Float previous, Float target, MapboxAnimator.AnimationsValueChangeListener updateListener, @Nullable MapboxMap.CancelableCallback cancelableCallback) {
        return new MapboxCameraAnimatorAdapter(previous, target, updateListener, cancelableCallback);
    }
}
