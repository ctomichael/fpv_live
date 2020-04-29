package com.mapbox.mapboxsdk.location;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.annotation.Nullable;
import com.mapbox.mapboxsdk.location.MapboxAnimator;
import com.mapbox.mapboxsdk.maps.MapboxMap;

class MapboxCameraAnimatorAdapter extends MapboxFloatAnimator {
    /* access modifiers changed from: private */
    @Nullable
    public final MapboxMap.CancelableCallback cancelableCallback;

    MapboxCameraAnimatorAdapter(Float previous, Float target, MapboxAnimator.AnimationsValueChangeListener updateListener, @Nullable MapboxMap.CancelableCallback cancelableCallback2) {
        super(previous, target, updateListener, Integer.MAX_VALUE);
        this.cancelableCallback = cancelableCallback2;
        addListener(new MapboxAnimatorListener());
    }

    private final class MapboxAnimatorListener extends AnimatorListenerAdapter {
        private MapboxAnimatorListener() {
        }

        public void onAnimationCancel(Animator animation) {
            if (MapboxCameraAnimatorAdapter.this.cancelableCallback != null) {
                MapboxCameraAnimatorAdapter.this.cancelableCallback.onCancel();
            }
        }

        public void onAnimationEnd(Animator animation) {
            if (MapboxCameraAnimatorAdapter.this.cancelableCallback != null) {
                MapboxCameraAnimatorAdapter.this.cancelableCallback.onFinish();
            }
        }
    }
}
