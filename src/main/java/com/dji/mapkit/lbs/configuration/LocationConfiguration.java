package com.dji.mapkit.lbs.configuration;

import android.support.annotation.Nullable;

public class LocationConfiguration {
    private final AMapServiceConfiguration amapServiceConfiguration;
    private final DefaultProviderConfiguration defaultProviderConfiguration;
    private final GooglePlayServicesConfiguration googlePlayServicesConfiguration;
    private final boolean keepTracking;
    private final LostServicesConfiguration lostServicesConfiguration;

    private LocationConfiguration(Builder builder) {
        this.keepTracking = builder.keepTracking;
        this.googlePlayServicesConfiguration = builder.googlePlayServicesConfiguration;
        this.lostServicesConfiguration = builder.lostServicesConfiguration;
        this.defaultProviderConfiguration = builder.defaultProviderConfiguration;
        this.amapServiceConfiguration = builder.amapServiceConfiguration;
        if (this.amapServiceConfiguration != null) {
            this.amapServiceConfiguration.clientOption().setOnceLocation(!this.keepTracking);
        }
    }

    public boolean keepTracking() {
        return this.keepTracking;
    }

    @Nullable
    public GooglePlayServicesConfiguration googlePlayServicesConfiguration() {
        return this.googlePlayServicesConfiguration;
    }

    @Nullable
    public LostServicesConfiguration lostServicesConfiguration() {
        return this.lostServicesConfiguration;
    }

    @Nullable
    public DefaultProviderConfiguration defaultProviderConfiguration() {
        return this.defaultProviderConfiguration;
    }

    @Nullable
    public AMapServiceConfiguration amapServiceConfiguration() {
        return this.amapServiceConfiguration;
    }

    public static class Builder {
        /* access modifiers changed from: private */
        public AMapServiceConfiguration amapServiceConfiguration;
        /* access modifiers changed from: private */
        public DefaultProviderConfiguration defaultProviderConfiguration;
        /* access modifiers changed from: private */
        public GooglePlayServicesConfiguration googlePlayServicesConfiguration;
        /* access modifiers changed from: private */
        public boolean keepTracking = false;
        /* access modifiers changed from: private */
        public LostServicesConfiguration lostServicesConfiguration;

        public Builder keepTracking(boolean keepTracking2) {
            this.keepTracking = keepTracking2;
            return this;
        }

        public Builder useGooglePlayServices(GooglePlayServicesConfiguration googlePlayServicesConfiguration2) {
            this.googlePlayServicesConfiguration = googlePlayServicesConfiguration2;
            return this;
        }

        public Builder useLostServices(LostServicesConfiguration lostServicesConfiguration2) {
            this.lostServicesConfiguration = lostServicesConfiguration2;
            return this;
        }

        public Builder useDefaultProviders(DefaultProviderConfiguration defaultProviderConfiguration2) {
            this.defaultProviderConfiguration = defaultProviderConfiguration2;
            return this;
        }

        public Builder useAMapService(AMapServiceConfiguration amapServiceConfiguration2) {
            this.amapServiceConfiguration = amapServiceConfiguration2;
            return this;
        }

        public LocationConfiguration build() {
            if (this.googlePlayServicesConfiguration != null || this.defaultProviderConfiguration != null || this.amapServiceConfiguration != null || this.lostServicesConfiguration != null) {
                return new LocationConfiguration(this);
            }
            throw new IllegalStateException("You need to specify one of the provider configurations. Please see GooglePlayServicesConfiguration, AMapServiceConfiguration and DefaultProviderConfiguration");
        }
    }
}
