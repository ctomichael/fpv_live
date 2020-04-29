package com.mapbox.android.core.location;

public class LocationEngineRequest {
    public static final int PRIORITY_BALANCED_POWER_ACCURACY = 1;
    public static final int PRIORITY_HIGH_ACCURACY = 0;
    public static final int PRIORITY_LOW_POWER = 2;
    public static final int PRIORITY_NO_POWER = 3;
    private final float displacement;
    private final long fastestInterval;
    private final long interval;
    private final long maxWaitTime;
    private final int priority;

    private LocationEngineRequest(Builder builder) {
        this.interval = builder.interval;
        this.priority = builder.priority;
        this.displacement = builder.displacement;
        this.maxWaitTime = builder.maxWaitTime;
        this.fastestInterval = builder.fastestInterval;
    }

    public long getInterval() {
        return this.interval;
    }

    public int getPriority() {
        return this.priority;
    }

    public float getDisplacemnt() {
        return this.displacement;
    }

    public long getMaxWaitTime() {
        return this.maxWaitTime;
    }

    public long getFastestInterval() {
        return this.fastestInterval;
    }

    public static final class Builder {
        /* access modifiers changed from: private */
        public float displacement = 0.0f;
        /* access modifiers changed from: private */
        public long fastestInterval = 0;
        /* access modifiers changed from: private */
        public final long interval;
        /* access modifiers changed from: private */
        public long maxWaitTime = 0;
        /* access modifiers changed from: private */
        public int priority = 0;

        public Builder(long interval2) {
            this.interval = interval2;
        }

        public Builder setPriority(int priority2) {
            this.priority = priority2;
            return this;
        }

        public Builder setDisplacement(float displacement2) {
            this.displacement = displacement2;
            return this;
        }

        public Builder setMaxWaitTime(long maxWaitTime2) {
            this.maxWaitTime = maxWaitTime2;
            return this;
        }

        public Builder setFastestInterval(long interval2) {
            this.fastestInterval = interval2;
            return this;
        }

        public LocationEngineRequest build() {
            return new LocationEngineRequest(this);
        }
    }
}
