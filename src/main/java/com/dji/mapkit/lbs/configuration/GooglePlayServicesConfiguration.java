package com.dji.mapkit.lbs.configuration;

import android.support.annotation.NonNull;
import com.google.android.gms.location.LocationRequest;

public class GooglePlayServicesConfiguration {
    private final boolean askForGooglePlayServices;
    private final boolean askForSettingsApi;
    private final boolean failOnConnectionSuspended;
    private final boolean failOnSettingsApiSuspended;
    private final boolean fallbackToDefault;
    private final long googlePlayServicesWaitPeriod;
    private final boolean ignoreLastKnownLocation;
    private final LocationRequest locationRequest;
    private final int suspendedConnectionRetryCount;

    private GooglePlayServicesConfiguration(Builder builder) {
        this.locationRequest = builder.locationRequest;
        this.fallbackToDefault = builder.fallbackToDefault;
        this.askForGooglePlayServices = builder.askForGooglePlayServices;
        this.askForSettingsApi = builder.askForSettingsApi;
        this.failOnConnectionSuspended = builder.failOnConnectionSuspended;
        this.failOnSettingsApiSuspended = builder.failOnSettingsApiSuspended;
        this.ignoreLastKnownLocation = builder.ignoreLastKnownLocation;
        this.googlePlayServicesWaitPeriod = builder.googlePlayServicesWaitPeriod;
        this.suspendedConnectionRetryCount = builder.suspendedConnectionRetryCount;
    }

    public LocationRequest locationRequest() {
        return this.locationRequest;
    }

    public boolean fallbackToDefault() {
        return this.fallbackToDefault;
    }

    public boolean askForGooglePlayServices() {
        return this.askForGooglePlayServices;
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

    public long googlePlayServicesWaitPeriod() {
        return this.googlePlayServicesWaitPeriod;
    }

    public int suspendedConnectionRetryCount() {
        return this.suspendedConnectionRetryCount;
    }

    public static class Builder {
        /* access modifiers changed from: private */
        public boolean askForGooglePlayServices = true;
        /* access modifiers changed from: private */
        public boolean askForSettingsApi = true;
        /* access modifiers changed from: private */
        public boolean failOnConnectionSuspended = true;
        /* access modifiers changed from: private */
        public boolean failOnSettingsApiSuspended = false;
        /* access modifiers changed from: private */
        public boolean fallbackToDefault = true;
        /* access modifiers changed from: private */
        public long googlePlayServicesWaitPeriod = 20000;
        /* access modifiers changed from: private */
        public boolean ignoreLastKnownLocation = false;
        /* access modifiers changed from: private */
        public LocationRequest locationRequest = Defaults.createDefaultLocationRequestForGoogleServices();
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

        public Builder askForGooglePlayServices(boolean askForGooglePlayServices2) {
            this.askForGooglePlayServices = askForGooglePlayServices2;
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
            this.failOnSettingsApiSuspended = failOnSettingsApiSuspended2;
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
            this.googlePlayServicesWaitPeriod = milliseconds;
            return this;
        }

        public Builder suspendedConnectionRetryCount(int suspendedConnectionRetryCount2) {
            if (suspendedConnectionRetryCount2 < 1) {
                throw new IllegalArgumentException("suspendedConnectionRetryCount cannot be smaller than 1");
            }
            this.suspendedConnectionRetryCount = suspendedConnectionRetryCount2;
            return this;
        }

        public GooglePlayServicesConfiguration build() {
            return new GooglePlayServicesConfiguration(this);
        }
    }
}
