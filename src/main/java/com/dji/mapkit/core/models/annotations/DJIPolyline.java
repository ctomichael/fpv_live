package com.dji.mapkit.core.models.annotations;

import com.dji.mapkit.core.models.DJILatLng;
import java.util.List;

public interface DJIPolyline {
    void remove();

    void setColor(int i);

    void setPoints(List<DJILatLng> list);

    void setWidth(float f);
}
