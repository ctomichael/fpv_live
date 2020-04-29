package com.mapbox.mapboxsdk.style.sources;

import android.support.annotation.WorkerThread;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;

public interface GeometryTileProvider {
    @WorkerThread
    FeatureCollection getFeaturesForBounds(LatLngBounds latLngBounds, int i);
}
