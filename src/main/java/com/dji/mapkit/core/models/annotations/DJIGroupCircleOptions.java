package com.dji.mapkit.core.models.annotations;

import android.support.annotation.ColorInt;
import com.dji.mapkit.core.models.DJILatLng;
import java.util.List;

public class DJIGroupCircleOptions {
    private float alpha;
    private List<DJILatLng> centers;
    private int fillColor;
    private List<Double> radius;
    private int strokeColor;
    private float strokeWidth;
    private float zIndex;

    public DJIGroupCircleOptions centers(List<DJILatLng> centers2) {
        this.centers = centers2;
        return this;
    }

    public DJIGroupCircleOptions radius(List<Double> radius2) {
        this.radius = radius2;
        return this;
    }

    public DJIGroupCircleOptions ZIndex(float zIndex2) {
        this.zIndex = zIndex2;
        return this;
    }

    public DJIGroupCircleOptions strokeWidth(float width) {
        this.strokeWidth = width;
        return this;
    }

    public DJIGroupCircleOptions strokeColor(@ColorInt int color) {
        this.strokeColor = color;
        return this;
    }

    public DJIGroupCircleOptions fillColor(@ColorInt int color) {
        this.fillColor = color;
        return this;
    }

    public DJIGroupCircleOptions alpha(float alpha2) {
        this.alpha = alpha2;
        return this;
    }

    public List<DJILatLng> getCenters() {
        return this.centers;
    }

    public List<Double> getRadius() {
        return this.radius;
    }

    public float getStrokeWidth() {
        return this.strokeWidth;
    }

    @ColorInt
    public int getStrokeColor() {
        return this.strokeColor;
    }

    @ColorInt
    public int getFillColor() {
        return this.fillColor;
    }

    public float getAlpha() {
        return this.alpha;
    }

    public float getZIndex() {
        return this.zIndex;
    }
}
