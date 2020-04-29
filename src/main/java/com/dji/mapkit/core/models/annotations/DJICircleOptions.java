package com.dji.mapkit.core.models.annotations;

import android.support.annotation.ColorInt;
import com.dji.mapkit.core.models.DJILatLng;

public class DJICircleOptions {
    private float alpha;
    private DJILatLng center;
    @ColorInt
    private int fillColor;
    private double radius;
    @ColorInt
    private int strokeColor;
    private float strokeWidth;
    private float zIndex;

    public DJICircleOptions center(DJILatLng center2) {
        this.center = center2;
        return this;
    }

    public DJICircleOptions radius(double radius2) {
        this.radius = radius2;
        return this;
    }

    public DJICircleOptions ZIndex(float zIndex2) {
        this.zIndex = zIndex2;
        return this;
    }

    public DJICircleOptions strokeWidth(float width) {
        this.strokeWidth = width;
        return this;
    }

    public DJICircleOptions strokeColor(@ColorInt int color) {
        this.strokeColor = color;
        return this;
    }

    public DJICircleOptions fillColor(@ColorInt int color) {
        this.fillColor = color;
        return this;
    }

    public DJICircleOptions alpha(float alpha2) {
        this.alpha = alpha2;
        return this;
    }

    public DJILatLng getCenter() {
        return this.center;
    }

    public double getRadius() {
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
