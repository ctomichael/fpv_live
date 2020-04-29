package com.dji.mapkit.core.models.annotations;

import com.dji.mapkit.core.models.DJILatLng;
import java.util.List;

public interface DJIGroupCircle {
    DJIGroupCircleOptions getOptions();

    float getZIndex();

    boolean isVisible();

    void remove();

    void setCircles(List<DJILatLng> list, List<Double> list2);

    void setFillColor(int i);

    void setStrokeColor(int i);

    void setVisible(boolean z);

    void setZIndex(float f);
}
