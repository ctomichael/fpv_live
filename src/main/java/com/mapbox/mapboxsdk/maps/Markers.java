package com.mapbox.mapboxsdk.maps;

import android.graphics.RectF;
import android.support.annotation.NonNull;
import com.mapbox.mapboxsdk.annotations.BaseMarkerOptions;
import com.mapbox.mapboxsdk.annotations.Marker;
import java.util.List;

interface Markers {
    Marker addBy(@NonNull BaseMarkerOptions baseMarkerOptions, @NonNull MapboxMap mapboxMap);

    List<Marker> addBy(@NonNull List<? extends BaseMarkerOptions> list, @NonNull MapboxMap mapboxMap);

    List<Marker> obtainAll();

    @NonNull
    List<Marker> obtainAllIn(@NonNull RectF rectF);

    void reload();

    void update(@NonNull Marker marker, @NonNull MapboxMap mapboxMap);
}
