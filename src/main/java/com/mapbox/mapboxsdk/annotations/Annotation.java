package com.mapbox.mapboxsdk.annotations;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;

@Deprecated
public abstract class Annotation implements Comparable<Annotation> {
    private long id = -1;
    protected MapView mapView;
    protected MapboxMap mapboxMap;

    protected Annotation() {
    }

    public long getId() {
        return this.id;
    }

    public void remove() {
        if (this.mapboxMap != null) {
            this.mapboxMap.removeAnnotation(this);
        }
    }

    public void setId(long id2) {
        this.id = id2;
    }

    public void setMapboxMap(MapboxMap mapboxMap2) {
        this.mapboxMap = mapboxMap2;
    }

    /* access modifiers changed from: protected */
    public MapboxMap getMapboxMap() {
        return this.mapboxMap;
    }

    public void setMapView(MapView mapView2) {
        this.mapView = mapView2;
    }

    /* access modifiers changed from: protected */
    public MapView getMapView() {
        return this.mapView;
    }

    public int compareTo(@NonNull Annotation annotation) {
        if (this.id < annotation.getId()) {
            return 1;
        }
        if (this.id > annotation.getId()) {
            return -1;
        }
        return 0;
    }

    public boolean equals(@Nullable Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || !(object instanceof Annotation)) {
            return false;
        }
        if (this.id != ((Annotation) object).getId()) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return (int) (getId() ^ (getId() >>> 32));
    }
}
