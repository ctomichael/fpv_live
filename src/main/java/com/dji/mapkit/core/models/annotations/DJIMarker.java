package com.dji.mapkit.core.models.annotations;

import com.dji.mapkit.core.models.DJIBitmapDescriptor;
import com.dji.mapkit.core.models.DJILatLng;

public abstract class DJIMarker {
    private Object object;
    private DJIMarkerOptions options;
    protected DJILatLng positionCache = new DJILatLng(0.0d, 0.0d, 0.0d);
    private float rotationCache;

    public abstract String getTitle();

    public abstract void hideInfoWindow();

    public abstract boolean isDraggable();

    public abstract boolean isInfoWindowShown();

    public abstract boolean isVisible();

    public abstract void remove();

    public abstract void setAnchor(float f, float f2);

    public abstract void setDraggable(boolean z);

    public abstract void setIcon(DJIBitmapDescriptor dJIBitmapDescriptor);

    public abstract void setRotation(float f);

    public abstract void setTitle(String str);

    public abstract void setVisible(boolean z);

    public abstract void showInfoWindow();

    public void setPosition(DJILatLng latLng) {
        this.positionCache.copyFrom(latLng);
    }

    public void setPositionCache(DJILatLng latLng) {
        this.positionCache = latLng;
    }

    public void setRotationCache(float rotation) {
        this.rotationCache = rotation;
    }

    public float getRotation() {
        return this.rotationCache;
    }

    public DJILatLng getPosition() {
        return new DJILatLng(this.positionCache);
    }

    public void setTag(Object o) {
        this.object = o;
    }

    public Object getTag() {
        return this.object;
    }
}
