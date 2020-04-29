package com.mapbox.geojson.gson;

import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import com.google.gson.GsonBuilder;
import com.mapbox.geojson.Geometry;
import com.mapbox.geojson.GeometryAdapterFactory;

@Keep
public class GeometryGeoJson {
    public static Geometry fromJson(@NonNull String json) {
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapterFactory(GeoJsonAdapterFactory.create());
        gson.registerTypeAdapterFactory(GeometryAdapterFactory.create());
        return (Geometry) gson.create().fromJson(json, Geometry.class);
    }
}
