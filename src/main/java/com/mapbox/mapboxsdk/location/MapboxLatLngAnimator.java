package com.mapbox.mapboxsdk.location;

import android.animation.TypeEvaluator;
import android.support.annotation.NonNull;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.MapboxAnimator;

class MapboxLatLngAnimator extends MapboxAnimator<LatLng> {
    MapboxLatLngAnimator(LatLng previous, LatLng target, MapboxAnimator.AnimationsValueChangeListener updateListener, int maxAnimationFps) {
        super(previous, target, updateListener, maxAnimationFps);
    }

    /* access modifiers changed from: package-private */
    @NonNull
    public TypeEvaluator provideEvaluator() {
        return new LatLngEvaluator();
    }
}
