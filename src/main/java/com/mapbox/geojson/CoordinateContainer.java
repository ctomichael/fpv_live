package com.mapbox.geojson;

import android.support.annotation.Keep;

@Keep
public interface CoordinateContainer<T> extends Geometry {
    T coordinates();
}
