package com.dji.mapkit.lbs.configuration;

import android.support.annotation.NonNull;
import com.mapzen.android.lost.api.LocationRequest;

public class LostServicesConfiguration {
    private final boolean askForSettingsApi;
    private final boolean failOnConnectionSuspended;
    private final boolean failOnSettingsApiSuspended;
    private final boolean fallbackToDefault;
    private final boolean ignoreLastKnownLocation;
    private final LocationRequest locationRequest;
    private final long lostServicesWaitPeriod;
    private final int suspendedConnectionRetryCount;

    private LostServicesConfiguration(Builder builder) {
        this.locationRequest = builder.locationRequest;
        this.fallbackToDefault = builder.fallbackToDefault;
        this.askForSettingsApi = builder.askForSettingsApi;
        this.failOnConnectionSuspended = builder.failOnConnectionSuspended;
        this.failOnSettingsApiSuspended = builder.failOnSettingsApiSuspended;
        this.ignoreLastKnownLocation = builder.ignoreLastKnownLocation;
        this.lostServicesWaitPeriod = builder.lostServicesWaitPeriod;
        this.suspendedConnectionRetryCount = builder.suspendedConnectionRetryCount;
    }

    public LocationRequest locationRequest() {
        return this.locationRequest;
    }

    public boolean fallbackToDefault() {
        return this.fallbackToDefault;
    }

    public boolean askForSettingsApi() {
        return this.askForSettingsApi;
    }

    public boolean failOnConnectionSuspended() {
        return this.failOnConnectionSuspended;
    }

    public boolean failOnSettingsApiSuspended() {
        return this.failOnSettingsApiSuspended;
    }

    public boolean ignoreLastKnownLocation() {
        return this.ignoreLastKnownLocation;
    }

    public long lostServicesWaitPeriod() {
        return this.lostServicesWaitPeriod;
    }

    public int suspendedConnectionRetryCount() {
        return this.suspendedConnectionRetryCount;
    }

    public static class Builder {
        /* access modifiers changed from: private */
        public boolean askForSettingsApi = true;
        /* access modifiers changed from: private */
        public boolean failOnConnectionSuspended = true;
        /* access modifiers changed from: private */
        public boolean failOnSettingsApiSuspended = false;
        /* access modifiers changed from: private */
        public boolean fallbackToDefault = true;
        /* access modifiers changed from: private */
        public boolean ignoreLastKnownLocation = false;
        /* access modifiers changed from: private */
        public LocationRequest locationRequest = Defaults.createDefaultLocationRequestForLostServices();
        /* access modifiers changed from: private */
        public long lostServicesWaitPeriod = 20000;
        /* access modifiers changed from: private */
        public int suspendedConnectionRetryCount = 2;

        public Builder locationRequest(@NonNull LocationRequest locationRequest2) {
            this.locationRequest = locationRequest2;
            return this;
        }

        public Builder fallbackToDefault(boolean fallbackToDefault2) {
            this.fallbackToDefault = fallbackToDefault2;
            return this;
        }

        public Builder askForSettingsApi(boolean askForSettingsApi2) {
            this.askForSettingsApi = askForSettingsApi2;
            return this;
        }

        public Builder failOnConnectionSuspended(boolean failOnConnectionSuspended2) {
            this.failOnConnectionSuspended = failOnConnectionSuspended2;
            return this;
        }

        public Builder failOnSettingsApiSuspended(boolean failOnSettingsApiSuspended2) {
            this.failOnConnectionSuspended = failOnSettingsApiSuspended2;
            return this;
        }

        public Builder ignoreLastKnownLocation(boolean ignore) {
            this.ignoreLastKnownLocation = ignore;
            return this;
        }

        public Builder setWaitPeriod(long milliseconds) {
            if (milliseconds < 0) {
                throw new IllegalArgumentException("waitPeriod cannot be set to negative value.");
            }
            this.lostServicesWaitPeriod = milliseconds;
            return this;
        }

        public Builder suspendedConnectionRetryCount(int suspendedConnectionRetryCount2) {
            if (suspendedConnectionRetryCount2 < 1) {
                throw new IllegalArgumentException("suspendedConnectionRetryCount cannot be smaller than 1");
            }
            this.suspendedConnectionRetryCount = suspendedConnectionRetryCount2;
            return this;
        }

        public LostServicesConfiguration build() {
            return new LostServicesConfiguration(this);
        }
    }
}
