package com.mapbox.mapboxsdk.maps;

import android.support.annotation.NonNull;
import com.mapbox.mapboxsdk.annotations.Polyline;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import java.util.List;

interface Polylines {
    Polyline addBy(@NonNull PolylineOptions polylineOptions, @NonNull MapboxMap mapboxMap);

    List<Polyline> addBy(@NonNull List<PolylineOptions> list, @NonNull MapboxMap mapboxMap);

    List<Polyline> obtainAll();

    void update(Polyline polyline);
}
