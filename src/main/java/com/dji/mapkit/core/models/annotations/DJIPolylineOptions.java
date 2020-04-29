package com.dji.mapkit.core.models.annotations;

import android.support.annotation.ColorInt;
import android.support.v4.view.ViewCompat;
import com.dji.mapkit.core.models.BasePointCollection;
import com.dji.mapkit.core.models.DJIBitmapDescriptor;
import com.dji.mapkit.core.models.DJILatLng;
import java.util.List;

public class DJIPolylineOptions extends BasePointCollection {
    private static final float DASH_LENGTH = 3.0f;
    private static final String TAG = "DJIPolylineOptions";
    private DJIBitmapDescriptor mBitmapDescriptor;
    private boolean mClickable;
    @ColorInt
    private int mColor = ViewCompat.MEASURED_STATE_MASK;
    private float mDashLength = DASH_LENGTH;
    private boolean mDashed;
    private boolean mEnableTexture;
    private boolean mGeodesic;
    private boolean mVisible = true;
    private float mWidth;
    private float mZIndex;

    public DJIPolylineOptions width(float width) {
        this.mWidth = width;
        return this;
    }

    public DJIPolylineOptions ZIndex(float zIndex) {
        this.mZIndex = zIndex;
        return this;
    }

    public DJIPolylineOptions color(@ColorInt int color) {
        this.mColor = color;
        return this;
    }

    public DJIPolylineOptions clickable(boolean clickable) {
        this.mClickable = clickable;
        return this;
    }

    public DJIPolylineOptions geodesic(boolean geodesic) {
        this.mGeodesic = geodesic;
        return this;
    }

    public DJIPolylineOptions visible(boolean visible) {
        this.mVisible = visible;
        return this;
    }

    public DJIPolylineOptions setUseTexture(boolean enableTexture) {
        this.mEnableTexture = enableTexture;
        return this;
    }

    public DJIPolylineOptions setCustomTexture(DJIBitmapDescriptor bitmapDescriptor) {
        this.mBitmapDescriptor = bitmapDescriptor;
        return this;
    }

    public DJIPolylineOptions setDashed(boolean dashed) {
        this.mDashed = dashed;
        return this;
    }

    public DJIPolylineOptions setDashLength(float length) {
        this.mDashLength = length;
        return this;
    }

    public float getWidth() {
        return this.mWidth;
    }

    public float getZIndex() {
        return this.mZIndex;
    }

    @ColorInt
    public int getColor() {
        return this.mColor;
    }

    public boolean isClickable() {
        return this.mClickable;
    }

    public boolean isGeodesic() {
        return this.mGeodesic;
    }

    public boolean isVisible() {
        return this.mVisible;
    }

    public boolean isEnableTexture() {
        return this.mEnableTexture;
    }

    public boolean isDashed() {
        return this.mDashed;
    }

    public float getDashLength() {
        return this.mDashLength;
    }

    public DJIBitmapDescriptor getBitmapDescriptor() {
        return this.mBitmapDescriptor;
    }

    public DJIPolylineOptions add(DJILatLng point) {
        addPoint(point);
        return this;
    }

    public DJIPolylineOptions add(DJILatLng... points) {
        for (DJILatLng point : points) {
            addPoint(point);
        }
        return this;
    }

    public DJIPolylineOptions addAll(List<DJILatLng> points) {
        this.points = points;
        return this;
    }
}
