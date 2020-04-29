package com.dji.mapkit.core.models;

import java.util.ArrayList;
import java.util.List;

public class BasePointCollection {
    private float alpha = 1.0f;
    protected List<DJILatLng> points = new ArrayList();

    protected BasePointCollection() {
    }

    public List<DJILatLng> getPoints() {
        return new ArrayList(this.points);
    }

    public void setPoints(List<DJILatLng> points2) {
        this.points = new ArrayList(points2);
    }

    public void addPoint(DJILatLng point) {
        this.points.add(point);
    }

    public float getAlpha() {
        return this.alpha;
    }

    public void setAlpha(float alpha2) {
        this.alpha = alpha2;
    }
}
