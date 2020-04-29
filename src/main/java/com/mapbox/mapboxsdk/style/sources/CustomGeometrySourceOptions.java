package com.mapbox.mapboxsdk.style.sources;

import android.support.annotation.NonNull;
import java.util.HashMap;

public class CustomGeometrySourceOptions extends HashMap<String, Object> {
    @NonNull
    public CustomGeometrySourceOptions withWrap(boolean wrap) {
        put("wrap", Boolean.valueOf(wrap));
        return this;
    }

    @NonNull
    public CustomGeometrySourceOptions withClip(boolean clip) {
        put("clip", Boolean.valueOf(clip));
        return this;
    }

    @NonNull
    public CustomGeometrySourceOptions withMinZoom(int minZoom) {
        put("minzoom", Integer.valueOf(minZoom));
        return this;
    }

    @NonNull
    public CustomGeometrySourceOptions withMaxZoom(int maxZoom) {
        put("maxzoom", Integer.valueOf(maxZoom));
        return this;
    }

    @NonNull
    public CustomGeometrySourceOptions withBuffer(int buffer) {
        put("buffer", Integer.valueOf(buffer));
        return this;
    }

    @NonNull
    public CustomGeometrySourceOptions withTolerance(float tolerance) {
        put("tolerance", Float.valueOf(tolerance));
        return this;
    }
}
