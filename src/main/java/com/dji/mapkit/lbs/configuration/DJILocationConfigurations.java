package com.dji.mapkit.lbs.configuration;

import com.dji.mapkit.lbs.configuration.AMapServiceConfiguration;
import com.dji.mapkit.lbs.configuration.DefaultProviderConfiguration;
import com.dji.mapkit.lbs.configuration.LocationConfiguration;
import com.dji.mapkit.lbs.configuration.LostServicesConfiguration;

public final class DJILocationConfigurations {
    public static Boolean isUseLostService = false;

    private DJILocationConfigurations() {
    }

    @Deprecated
    public static LocationConfiguration defaultConfiguration(boolean keepTracking) {
        LocationConfiguration.Builder builder = new LocationConfiguration.Builder();
        if (isUseLostService.booleanValue()) {
            builder.keepTracking(keepTracking).useLostServices(new LostServicesConfiguration.Builder().build()).keepTracking(keepTracking);
        } else {
            builder.keepTracking(keepTracking).useDefaultProviders(new DefaultProviderConfiguration.Builder().build());
        }
        builder.useAMapService(new AMapServiceConfiguration.Builder().keepTracking(keepTracking).build());
        return builder.build();
    }

    public static LocationConfiguration defaultConfiguration(boolean keepTracking, boolean useAMapService) {
        LocationConfiguration.Builder builder = new LocationConfiguration.Builder();
        if (isUseLostService.booleanValue()) {
            builder.keepTracking(keepTracking).useLostServices(new LostServicesConfiguration.Builder().build()).keepTracking(keepTracking);
        } else {
            builder.keepTracking(keepTracking).useDefaultProviders(new DefaultProviderConfiguration.Builder().build());
        }
        if (useAMapService) {
            builder.useAMapService(new AMapServiceConfiguration.Builder().keepTracking(keepTracking).build());
        }
        return builder.build();
    }
}
