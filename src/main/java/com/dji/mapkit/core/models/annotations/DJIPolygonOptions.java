package com.dji.mapkit.core.models.annotations;

import android.support.annotation.ColorInt;
import android.support.v4.view.ViewCompat;
import com.dji.mapkit.core.models.BasePointCollection;
import com.dji.mapkit.core.models.DJILatLng;
import java.util.List;

public class DJIPolygonOptions extends BasePointCollection {
    private static final String TAG = "DJIPolygonOptions";
    @ColorInt
    private int mFillColor = ViewCompat.MEASURED_STATE_MASK;
    @ColorInt
    private int mStrokeColor = ViewCompat.MEASURED_STATE_MASK;
    private boolean mVisible = true;
    private float mWidth;
    private float mZIndex;

    public DJIPolygonOptions strokeWidth(float width) {
        this.mWidth = width;
        return this;
    }

    public DJIPolygonOptions ZIndex(float zIndex) {
        this.mZIndex = zIndex;
        return this;
    }

    public DJIPolygonOptions strokeColor(@ColorInt int color) {
        this.mStrokeColor = color;
        return this;
    }

    public DJIPolygonOptions fillColor(@ColorInt int color) {
        this.mFillColor = color;
        return this;
    }

    public float getStrokeWidth() {
        return this.mWidth;
    }

    public float getZIndex() {
        return this.mZIndex;
    }

    @ColorInt
    public int getStrokeColor() {
        return this.mStrokeColor;
    }

    @ColorInt
    public int getFillColor() {
        return this.mFillColor;
    }

    public boolean isVisible() {
        return this.mVisible;
    }

    public DJIPolygonOptions add(DJILatLng point) {
        addPoint(point);
        return this;
    }

    public DJIPolygonOptions add(DJILatLng... points) {
        for (DJILatLng point : points) {
            addPoint(point);
        }
        return this;
    }

    public DJIPolygonOptions addAll(List<DJILatLng> points) {
        for (DJILatLng point : points) {
            addPoint(point);
        }
        return this;
    }
}
