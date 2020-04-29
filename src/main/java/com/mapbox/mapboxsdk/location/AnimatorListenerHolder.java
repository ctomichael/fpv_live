package com.mapbox.mapboxsdk.location;

import com.mapbox.mapboxsdk.location.MapboxAnimator;

class AnimatorListenerHolder {
    private final int animatorType;
    private final MapboxAnimator.AnimationsValueChangeListener listener;

    AnimatorListenerHolder(int animatorType2, MapboxAnimator.AnimationsValueChangeListener listener2) {
        this.animatorType = animatorType2;
        this.listener = listener2;
    }

    public int getAnimatorType() {
        return this.animatorType;
    }

    public MapboxAnimator.AnimationsValueChangeListener getListener() {
        return this.listener;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AnimatorListenerHolder that = (AnimatorListenerHolder) o;
        if (this.animatorType != that.animatorType) {
            return false;
        }
        if (this.listener != null) {
            return this.listener.equals(that.listener);
        }
        if (that.listener != null) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return (this.animatorType * 31) + (this.listener != null ? this.listener.hashCode() : 0);
    }
}
