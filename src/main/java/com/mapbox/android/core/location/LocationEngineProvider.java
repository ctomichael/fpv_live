package com.mapbox.android.core.location;

import android.content.Context;
import android.support.annotation.NonNull;
import com.google.android.gms.common.GoogleApiAvailability;

public final class LocationEngineProvider {
    private static final String GOOGLE_API_AVAILABILITY = "com.google.android.gms.common.GoogleApiAvailability";
    private static final String GOOGLE_LOCATION_SERVICES = "com.google.android.gms.location.LocationServices";

    private LocationEngineProvider() {
    }

    @Deprecated
    @NonNull
    public static LocationEngine getBestLocationEngine(@NonNull Context context, boolean background) {
        return getBestLocationEngine(context);
    }

    @NonNull
    public static LocationEngine getBestLocationEngine(@NonNull Context context) {
        Utils.checkNotNull(context, "context == null");
        boolean hasGoogleLocationServices = Utils.isOnClasspath(GOOGLE_LOCATION_SERVICES);
        if (Utils.isOnClasspath(GOOGLE_API_AVAILABILITY)) {
            hasGoogleLocationServices &= GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context) == 0;
        }
        return getLocationEngine(context, hasGoogleLocationServices);
    }

    private static LocationEngine getLocationEngine(Context context, boolean isGoogle) {
        if (isGoogle) {
            return new LocationEngineProxy(new GoogleLocationEngineImpl(context.getApplicationContext()));
        }
        return new LocationEngineProxy(new MapboxFusedLocationEngineImpl(context.getApplicationContext()));
    }
}
