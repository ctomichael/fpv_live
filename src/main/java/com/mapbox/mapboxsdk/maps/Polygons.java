package com.mapbox.mapboxsdk.maps;

import android.support.annotation.NonNull;
import com.mapbox.mapboxsdk.annotations.Polygon;
import com.mapbox.mapboxsdk.annotations.PolygonOptions;
import java.util.List;

interface Polygons {
    Polygon addBy(@NonNull PolygonOptions polygonOptions, @NonNull MapboxMap mapboxMap);

    List<Polygon> addBy(@NonNull List<PolygonOptions> list, @NonNull MapboxMap mapboxMap);

    List<Polygon> obtainAll();

    void update(Polygon polygon);
}
