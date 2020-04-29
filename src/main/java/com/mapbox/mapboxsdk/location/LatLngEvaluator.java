package com.mapbox.mapboxsdk.location;

import android.animation.TypeEvaluator;
import android.support.annotation.NonNull;
import com.mapbox.mapboxsdk.geometry.LatLng;

class LatLngEvaluator implements TypeEvaluator<LatLng> {
    private final LatLng latLng = new LatLng();

    LatLngEvaluator() {
    }

    @NonNull
    public LatLng evaluate(float fraction, @NonNull LatLng startValue, @NonNull LatLng endValue) {
        this.latLng.setLatitude(startValue.getLatitude() + ((endValue.getLatitude() - startValue.getLatitude()) * ((double) fraction)));
        this.latLng.setLongitude(startValue.getLongitude() + ((endValue.getLongitude() - startValue.getLongitude()) * ((double) fraction)));
        return this.latLng;
    }
}
