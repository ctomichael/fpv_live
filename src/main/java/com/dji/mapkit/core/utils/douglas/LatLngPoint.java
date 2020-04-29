package com.dji.mapkit.core.utils.douglas;

import android.support.annotation.NonNull;
import com.dji.mapkit.core.models.DJILatLng;

public class LatLngPoint implements Comparable<LatLngPoint> {
    public int id;
    public DJILatLng latLng;

    public LatLngPoint(int id2, DJILatLng latLng2) {
        this.id = id2;
        this.latLng = latLng2;
    }

    public int compareTo(@NonNull LatLngPoint o) {
        if (this.id < o.id) {
            return -1;
        }
        if (this.id > o.id) {
            return 1;
        }
        return 0;
    }
}
