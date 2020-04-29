package com.mapbox.mapboxsdk.style.layers;

import android.support.annotation.Keep;

public class TransitionOptions {
    @Keep
    private long delay;
    @Keep
    private long duration;
    @Keep
    private boolean enablePlacementTransitions;

    public TransitionOptions(long duration2, long delay2) {
        this(duration2, delay2, true);
    }

    public TransitionOptions(long duration2, long delay2, boolean enablePlacementTransitions2) {
        this.duration = duration2;
        this.delay = delay2;
        this.enablePlacementTransitions = enablePlacementTransitions2;
    }

    @Keep
    @Deprecated
    public static TransitionOptions fromTransitionOptions(long duration2, long delay2) {
        return new TransitionOptions(duration2, delay2);
    }

    @Keep
    static TransitionOptions fromTransitionOptions(long duration2, long delay2, boolean enablePlacementTransitions2) {
        return new TransitionOptions(duration2, delay2, enablePlacementTransitions2);
    }

    public long getDuration() {
        return this.duration;
    }

    public long getDelay() {
        return this.delay;
    }

    public boolean isEnablePlacementTransitions() {
        return this.enablePlacementTransitions;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TransitionOptions that = (TransitionOptions) o;
        if (this.duration != that.duration || this.delay != that.delay) {
            return false;
        }
        if (this.enablePlacementTransitions != that.enablePlacementTransitions) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return (((((int) (this.duration ^ (this.duration >>> 32))) * 31) + ((int) (this.delay ^ (this.delay >>> 32)))) * 31) + (this.enablePlacementTransitions ? 1 : 0);
    }

    public String toString() {
        return "TransitionOptions{duration=" + this.duration + ", delay=" + this.delay + ", enablePlacementTransitions=" + this.enablePlacementTransitions + '}';
    }
}
