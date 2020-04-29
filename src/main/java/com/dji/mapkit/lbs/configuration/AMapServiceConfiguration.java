package com.dji.mapkit.lbs.configuration;

import android.support.annotation.NonNull;
import com.amap.api.location.AMapLocationClientOption;

public class AMapServiceConfiguration {
    private final long amapServiceWaitPeriod;
    private final AMapLocationClientOption clientOption;
    private final boolean failOnConnectionSuspended;
    private final boolean fallbackToDefault;
    private final int suspendedConnectionRetryCount;

    private AMapServiceConfiguration(Builder builder) {
        this.clientOption = builder.clientOption;
        this.clientOption.setOnceLocation(!builder.keepTracking);
        this.fallbackToDefault = builder.fallbackToDefault;
        this.failOnConnectionSuspended = builder.failOnConnectionSuspended;
        this.amapServiceWaitPeriod = builder.amapServiceWaitPeriod;
        this.suspendedConnectionRetryCount = builder.suspendedConnectionRetryCount;
    }

    public AMapLocationClientOption clientOption() {
        return this.clientOption;
    }

    public boolean fallbackToDefault() {
        return this.fallbackToDefault;
    }

    public boolean failOnConnectionSuspended() {
        return this.failOnConnectionSuspended;
    }

    public long amapServiceWaitPeriod() {
        return this.amapServiceWaitPeriod;
    }

    public int suspendedConnectionRetryCount() {
        return this.suspendedConnectionRetryCount;
    }

    public static class Builder {
        /* access modifiers changed from: private */
        public long amapServiceWaitPeriod = 20000;
        private boolean askForAmapService = true;
        /* access modifiers changed from: private */
        public AMapLocationClientOption clientOption = Defaults.createDefaultAMapLocationClientOption(true);
        /* access modifiers changed from: private */
        public boolean failOnConnectionSuspended = true;
        /* access modifiers changed from: private */
        public boolean fallbackToDefault = true;
        /* access modifiers changed from: private */
        public boolean keepTracking = false;
        /* access modifiers changed from: private */
        public int suspendedConnectionRetryCount = 2;

        public Builder keepTracking(boolean keepTracking2) {
            this.keepTracking = keepTracking2;
            return this;
        }

        public Builder clientOption(@NonNull AMapLocationClientOption clientOption2) {
            this.clientOption = clientOption2;
            return this;
        }

        public Builder fallbackToDefault(boolean fallbackToDefault2) {
            this.fallbackToDefault = fallbackToDefault2;
            return this;
        }

        public Builder failOnConnectionSuspended(boolean failOnConnectionSuspended2) {
            this.failOnConnectionSuspended = failOnConnectionSuspended2;
            return this;
        }

        public Builder setWaitPeriod(long milliseconds) {
            if (milliseconds < 0) {
                throw new IllegalArgumentException("waitPeriod cannot be set to negative value.");
            }
            this.amapServiceWaitPeriod = milliseconds;
            return this;
        }

        public Builder suspendedConnectionRetryCount(int suspendedConnectionRetryCount2) {
            if (suspendedConnectionRetryCount2 < 1) {
                throw new IllegalArgumentException("suspendedConnectionRetryCount cannot be smaller than 1");
            }
            this.suspendedConnectionRetryCount = suspendedConnectionRetryCount2;
            return this;
        }

        public AMapServiceConfiguration build() {
            return new AMapServiceConfiguration(this);
        }
    }
}
