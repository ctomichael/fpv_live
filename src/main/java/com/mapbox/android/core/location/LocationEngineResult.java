package com.mapbox.android.core.location;

import android.content.Intent;
import android.location.Location;
import android.support.annotation.Nullable;
import com.google.android.gms.location.LocationResult;
import dji.publics.protocol.ResponseBase;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class LocationEngineResult {
    private static final String GOOGLE_PLAY_LOCATION_RESULT = "com.google.android.gms.location.LocationResult";
    private final List<Location> locations;

    private LocationEngineResult(List<Location> locations2) {
        this.locations = Collections.unmodifiableList(locations2);
    }

    public static LocationEngineResult create(Location location) {
        Utils.checkNotNull(location, "location can't be null");
        List<Location> locations2 = new ArrayList<>();
        locations2.add(location);
        return new LocationEngineResult(locations2);
    }

    public static LocationEngineResult create(List<Location> locations2) {
        Utils.checkNotNull(locations2, "locations can't be null");
        return new LocationEngineResult(locations2);
    }

    @Nullable
    public Location getLastLocation() {
        if (this.locations.isEmpty()) {
            return null;
        }
        return this.locations.get(0);
    }

    public List<Location> getLocations() {
        return Collections.unmodifiableList(this.locations);
    }

    @Nullable
    public static LocationEngineResult extractResult(Intent intent) {
        LocationEngineResult result = null;
        if (Utils.isOnClasspath(GOOGLE_PLAY_LOCATION_RESULT)) {
            result = extractGooglePlayResult(intent);
        }
        return result == null ? extractAndroidResult(intent) : result;
    }

    private static LocationEngineResult extractGooglePlayResult(Intent intent) {
        LocationResult result = LocationResult.extractResult(intent);
        if (result != null) {
            return create(result.getLocations());
        }
        return null;
    }

    private static LocationEngineResult extractAndroidResult(Intent intent) {
        if (!hasResult(intent)) {
            return null;
        }
        return create((Location) intent.getExtras().getParcelable(ResponseBase.STRING_LOCATION));
    }

    private static boolean hasResult(Intent intent) {
        return intent != null && intent.hasExtra(ResponseBase.STRING_LOCATION);
    }
}
