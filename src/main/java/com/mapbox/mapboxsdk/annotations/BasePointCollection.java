package com.mapbox.mapboxsdk.annotations;

import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import com.mapbox.mapboxsdk.geometry.LatLng;
import java.util.ArrayList;
import java.util.List;

@Deprecated
public abstract class BasePointCollection extends Annotation {
    @Keep
    private float alpha = 1.0f;
    @Keep
    private List<LatLng> points = new ArrayList();

    /* access modifiers changed from: package-private */
    public abstract void update();

    protected BasePointCollection() {
    }

    @NonNull
    public List<LatLng> getPoints() {
        return new ArrayList(this.points);
    }

    public void setPoints(@NonNull List<LatLng> points2) {
        this.points = new ArrayList(points2);
        update();
    }

    public void addPoint(LatLng point) {
        this.points.add(point);
        update();
    }

    public float getAlpha() {
        return this.alpha;
    }

    public void setAlpha(float alpha2) {
        this.alpha = alpha2;
        update();
    }
}
