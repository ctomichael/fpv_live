package com.mapbox.mapboxsdk.location;

import android.animation.FloatEvaluator;
import android.animation.TypeEvaluator;
import android.support.annotation.NonNull;
import com.mapbox.mapboxsdk.location.MapboxAnimator;

class MapboxFloatAnimator extends MapboxAnimator<Float> {
    MapboxFloatAnimator(Float previous, Float target, MapboxAnimator.AnimationsValueChangeListener updateListener, int maxAnimationFps) {
        super(previous, target, updateListener, maxAnimationFps);
    }

    /* access modifiers changed from: package-private */
    @NonNull
    public TypeEvaluator provideEvaluator() {
        return new FloatEvaluator();
    }
}
