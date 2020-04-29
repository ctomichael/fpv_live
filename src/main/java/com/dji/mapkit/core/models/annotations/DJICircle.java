package com.dji.mapkit.core.models.annotations;

import com.dji.mapkit.core.models.DJILatLng;

public abstract class DJICircle {
    protected DJILatLng positionCache = new DJILatLng(0.0d, 0.0d, 0.0d);

    public abstract boolean isVisible();

    public abstract void remove();

    public abstract void setFillColor(int i);

    public abstract void setStrokeColor(int i);

    public abstract void setVisible(boolean z);

    public DJILatLng getCenter() {
        return new DJILatLng(this.positionCache);
    }

    public void setCircle(DJILatLng center, Double radius) {
        this.positionCache.copyFrom(center);
    }

    public void setCenter(DJILatLng center) {
        this.positionCache.copyFrom(center);
    }
}
