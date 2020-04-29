package com.dji.mapkit.core.maps;

import android.graphics.Point;
import com.dji.mapkit.core.models.DJILatLng;

public interface DJIProjection {
    DJILatLng fromScreenLocation(Point point);

    Point toScreenLocation(DJILatLng dJILatLng);
}
