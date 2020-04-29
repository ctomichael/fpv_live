package com.mapbox.mapboxsdk.location;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.support.annotation.NonNull;

abstract class MapboxAnimator<K> extends ValueAnimator implements ValueAnimator.AnimatorUpdateListener {
    static final int ANIMATOR_CAMERA_COMPASS_BEARING = 5;
    static final int ANIMATOR_CAMERA_GPS_BEARING = 4;
    static final int ANIMATOR_CAMERA_LATLNG = 1;
    static final int ANIMATOR_LAYER_ACCURACY = 6;
    static final int ANIMATOR_LAYER_COMPASS_BEARING = 3;
    static final int ANIMATOR_LAYER_GPS_BEARING = 2;
    static final int ANIMATOR_LAYER_LATLNG = 0;
    static final int ANIMATOR_TILT = 8;
    static final int ANIMATOR_ZOOM = 7;
    private K animatedValue;
    private final double minUpdateInterval;
    private final K target;
    private long timeElapsed;
    private final AnimationsValueChangeListener<K> updateListener;

    interface AnimationsValueChangeListener<K> {
        void onNewAnimationValue(K k);
    }

    /* access modifiers changed from: package-private */
    public abstract TypeEvaluator provideEvaluator();

    MapboxAnimator(@NonNull K previous, @NonNull K target2, @NonNull AnimationsValueChangeListener<K> updateListener2, int maxAnimationFps) {
        this.minUpdateInterval = 1.0E9d / ((double) maxAnimationFps);
        setObjectValues(previous, target2);
        setEvaluator(provideEvaluator());
        this.updateListener = updateListener2;
        this.target = target2;
        addUpdateListener(this);
        addListener(new AnimatorListener());
    }

    public void onAnimationUpdate(ValueAnimator animation) {
        this.animatedValue = animation.getAnimatedValue();
        long currentTime = System.nanoTime();
        if (((double) (currentTime - this.timeElapsed)) >= this.minUpdateInterval) {
            postUpdates();
            this.timeElapsed = currentTime;
        }
    }

    private class AnimatorListener extends AnimatorListenerAdapter {
        private AnimatorListener() {
        }

        public void onAnimationEnd(Animator animation) {
            MapboxAnimator.this.postUpdates();
        }
    }

    /* access modifiers changed from: private */
    public void postUpdates() {
        this.updateListener.onNewAnimationValue(this.animatedValue);
    }

    /* access modifiers changed from: package-private */
    public K getTarget() {
        return this.target;
    }
}
