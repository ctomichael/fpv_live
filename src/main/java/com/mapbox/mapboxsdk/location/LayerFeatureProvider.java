package com.mapbox.mapboxsdk.location;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;

class LayerFeatureProvider {
    LayerFeatureProvider() {
    }

    /* access modifiers changed from: package-private */
    @NonNull
    public Feature generateLocationFeature(@Nullable Feature locationFeature, @NonNull LocationComponentOptions options) {
        if (locationFeature != null) {
            return locationFeature;
        }
        Feature locationFeature2 = Feature.fromGeometry(Point.fromLngLat(0.0d, 0.0d));
        locationFeature2.addNumberProperty("mapbox-property-gps-bearing", Float.valueOf(0.0f));
        locationFeature2.addNumberProperty("mapbox-property-compass-bearing", Float.valueOf(0.0f));
        locationFeature2.addBooleanProperty("mapbox-property-location-stale", Boolean.valueOf(options.enableStaleState()));
        return locationFeature2;
    }
}
