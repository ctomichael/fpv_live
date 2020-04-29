package com.dji.mapkit.core.models.annotations;

import android.graphics.PointF;
import com.dji.mapkit.core.models.DJIBitmapDescriptor;
import com.dji.mapkit.core.models.DJILatLng;

public class DJIMarkerOptions {
    private boolean isCustomAnchor;
    private PointF mAnchor;
    private boolean mDraggable;
    private boolean mFlat;
    private DJIBitmapDescriptor mIcon;
    private boolean mInfoWindowEnable;
    private DJILatLng mPosition;
    private float mRotation;
    private String mTitle;
    private boolean mVisible;
    private int mZIndex;

    public DJIMarkerOptions() {
        this.mVisible = true;
        this.mAnchor = new PointF(0.5f, 0.5f);
        this.mVisible = true;
        this.mInfoWindowEnable = true;
        this.mTitle = "";
    }

    public DJIMarkerOptions draggable(boolean draggable) {
        this.mDraggable = draggable;
        this.mVisible = true;
        this.mInfoWindowEnable = true;
        this.mTitle = "";
        return this;
    }

    public DJIMarkerOptions position(DJILatLng position) {
        this.mPosition = position;
        return this;
    }

    public DJIMarkerOptions anchor(float u, float v) {
        this.isCustomAnchor = true;
        this.mAnchor = new PointF(u, v);
        return this;
    }

    public DJIMarkerOptions icon(DJIBitmapDescriptor bitmap) {
        this.mIcon = bitmap;
        return this;
    }

    public DJIMarkerOptions rotation(float rotation) {
        this.mRotation = rotation;
        return this;
    }

    public DJIMarkerOptions zIndex(int zIndex) {
        this.mZIndex = zIndex;
        return this;
    }

    public DJIMarkerOptions visible(boolean visible) {
        this.mVisible = visible;
        return this;
    }

    public DJIMarkerOptions title(String title) {
        this.mTitle = title;
        return this;
    }

    public DJIMarkerOptions flat(boolean flat) {
        this.mFlat = flat;
        return this;
    }

    public boolean getDraggable() {
        return this.mDraggable;
    }

    public DJILatLng getPosition() {
        return this.mPosition;
    }

    public float getAnchorU() {
        return this.mAnchor.x;
    }

    public float getAnchorV() {
        return this.mAnchor.y;
    }

    public DJIBitmapDescriptor getIcon() {
        return this.mIcon;
    }

    public float getRotation() {
        return this.mRotation;
    }

    public int getZIndex() {
        return this.mZIndex;
    }

    public boolean isCustomAnchor() {
        return this.isCustomAnchor;
    }

    public boolean getVisible() {
        return this.mVisible;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public boolean isFlat() {
        return this.mFlat;
    }

    public boolean isInfoWindowEnable() {
        return this.mInfoWindowEnable;
    }

    public DJIMarkerOptions setInfoWindowEnable(boolean mInfoWindowEnable2) {
        this.mInfoWindowEnable = mInfoWindowEnable2;
        return this;
    }
}
