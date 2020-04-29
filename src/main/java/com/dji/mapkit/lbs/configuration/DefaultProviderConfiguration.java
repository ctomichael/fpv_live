package com.dji.mapkit.lbs.configuration;

import com.mapzen.android.lost.internal.FusionEngine;

public class DefaultProviderConfiguration {
    private final float acceptableAccuracy;
    private final long acceptableTimePeriod;
    private final long gpsWaitPeriod;
    private final long networkWaitPeriod;
    private final long requiredDistanceInterval;
    private final long requiredTimeInterval;

    public DefaultProviderConfiguration(Builder builder) {
        this.requiredTimeInterval = builder.requiredTimeInterval;
        this.requiredDistanceInterval = builder.requiredDistanceInterval;
        this.acceptableAccuracy = builder.acceptableAccuracy;
        this.acceptableTimePeriod = builder.acceptableTimePeriod;
        this.gpsWaitPeriod = builder.gpsWaitPeriod;
        this.networkWaitPeriod = builder.networkWaitPeriod;
    }

    public long requiredTimeInterval() {
        return this.requiredTimeInterval;
    }

    public long requiredDistanceInterval() {
        return this.requiredDistanceInterval;
    }

    public float acceptableAccuracy() {
        return this.acceptableAccuracy;
    }

    public long acceptableTimePeriod() {
        return this.acceptableTimePeriod;
    }

    public long gpsWaitPeriod() {
        return this.gpsWaitPeriod;
    }

    public long networkWaitPeriod() {
        return this.networkWaitPeriod;
    }

    public static class Builder {
        /* access modifiers changed from: private */
        public float acceptableAccuracy = 5.0f;
        /* access modifiers changed from: private */
        public long acceptableTimePeriod = FusionEngine.RECENT_UPDATE_THRESHOLD_IN_MILLIS;
        /* access modifiers changed from: private */
        public long gpsWaitPeriod = 20000;
        /* access modifiers changed from: private */
        public long networkWaitPeriod = 20000;
        /* access modifiers changed from: private */
        public long requiredDistanceInterval = 0;
        /* access modifiers changed from: private */
        public long requiredTimeInterval = 10000;

        public Builder requiredTimeInterval(long requiredTimeInterval2) {
            if (requiredTimeInterval2 < 0) {
                throw new IllegalArgumentException("requiredTimeInterval cannot be set to negative value.");
            }
            this.requiredTimeInterval = requiredTimeInterval2;
            return this;
        }

        public Builder requiredDistanceInterval(long requitedDistanceInterval) {
            if (requitedDistanceInterval < 0) {
                throw new IllegalArgumentException("requiredDistanceInterval cannot be set to negative value.");
            }
            this.requiredDistanceInterval = requitedDistanceInterval;
            return this;
        }

        public Builder acceptableAccuracy(float acceptableAccuracy2) {
            if (acceptableAccuracy2 < 0.0f) {
                throw new IllegalArgumentException("acceptableAccuracy cannot be set to negative value.");
            }
            this.acceptableAccuracy = acceptableAccuracy2;
            return this;
        }

        public Builder acceptableTimePeriod(long acceptableTimePeriod2) {
            if (acceptableTimePeriod2 < 0) {
                throw new IllegalArgumentException("acceptableTimePeriod cannot be set to negative value.");
            }
            this.acceptableTimePeriod = acceptableTimePeriod2;
            return this;
        }

        public Builder setWaitPeriod(int providerType, long milliseconds) {
            if (milliseconds < 0) {
                throw new IllegalArgumentException("waitPeriod cannot be set to negative value.");
            }
            switch (providerType) {
                case 1:
                    throw new IllegalStateException("GooglePlayServices waiting time period should be set on GooglePlayServicesConfiguration");
                case 2:
                    this.gpsWaitPeriod = milliseconds;
                    break;
                case 3:
                    this.networkWaitPeriod = milliseconds;
                    break;
                case 4:
                    this.gpsWaitPeriod = milliseconds;
                    this.networkWaitPeriod = milliseconds;
                    break;
            }
            return this;
        }

        public DefaultProviderConfiguration build() {
            return new DefaultProviderConfiguration(this);
        }
    }
}
