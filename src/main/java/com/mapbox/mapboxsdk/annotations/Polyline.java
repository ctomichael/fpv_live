package com.mapbox.mapboxsdk.annotations;

import android.support.annotation.Keep;
import android.support.v4.view.ViewCompat;
import com.mapbox.mapboxsdk.maps.MapboxMap;

@Deprecated
public final class Polyline extends BasePointCollection {
    @Keep
    private int color = ViewCompat.MEASURED_STATE_MASK;
    @Keep
    private float width = 10.0f;

    Polyline() {
    }

    public int getColor() {
        return this.color;
    }

    public float getWidth() {
        return this.width;
    }

    public void setColor(int color2) {
        this.color = color2;
        update();
    }

    public void setWidth(float width2) {
        this.width = width2;
        update();
    }

    /* access modifiers changed from: package-private */
    public void update() {
        MapboxMap mapboxMap = getMapboxMap();
        if (mapboxMap != null) {
            mapboxMap.updatePolyline(this);
        }
    }
}
