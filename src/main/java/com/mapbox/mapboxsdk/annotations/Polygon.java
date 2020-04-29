package com.mapbox.mapboxsdk.annotations;

import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import java.util.ArrayList;
import java.util.List;

@Deprecated
public final class Polygon extends BasePointCollection {
    @Keep
    private int fillColor = ViewCompat.MEASURED_STATE_MASK;
    @Keep
    private List<List<LatLng>> holes = new ArrayList();
    @Keep
    private int strokeColor = ViewCompat.MEASURED_STATE_MASK;

    Polygon() {
    }

    public int getFillColor() {
        return this.fillColor;
    }

    public int getStrokeColor() {
        return this.strokeColor;
    }

    public List<List<LatLng>> getHoles() {
        return new ArrayList(this.holes);
    }

    public void setFillColor(int color) {
        this.fillColor = color;
        update();
    }

    public void setStrokeColor(int color) {
        this.strokeColor = color;
        update();
    }

    public void setHoles(@NonNull List<? extends List<LatLng>> holes2) {
        this.holes = new ArrayList(holes2);
        update();
    }

    /* access modifiers changed from: package-private */
    public void addHole(List<LatLng> hole) {
        this.holes.add(hole);
        update();
    }

    /* access modifiers changed from: package-private */
    public void update() {
        MapboxMap mapboxMap = getMapboxMap();
        if (mapboxMap != null) {
            mapboxMap.updatePolygon(this);
        }
    }
}
