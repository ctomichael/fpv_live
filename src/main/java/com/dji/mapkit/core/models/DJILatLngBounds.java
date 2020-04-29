package com.dji.mapkit.core.models;

import android.support.annotation.NonNull;
import com.dji.mapkit.core.exceptions.InvalidLatLngBoundsException;
import java.util.ArrayList;
import java.util.List;

public class DJILatLngBounds {
    private final DJILatLng northeast;
    private final DJILatLng southwest;

    DJILatLngBounds(DJILatLng northeast2, DJILatLng southwest2) {
        this.northeast = northeast2;
        this.southwest = southwest2;
    }

    public DJILatLng getNortheast() {
        return this.northeast;
    }

    public DJILatLng getSouthwest() {
        return this.southwest;
    }

    public static DJILatLngBounds fromLatLngs(List<DJILatLng> latLngs) {
        double minLat = 90.0d;
        double minLon = 180.0d;
        double maxLat = -90.0d;
        double maxLon = -180.0d;
        for (DJILatLng point : latLngs) {
            double latitude = point.latitude;
            double longitude = point.longitude;
            minLat = Math.min(minLat, latitude);
            minLon = Math.min(minLon, longitude);
            maxLat = Math.max(maxLat, latitude);
            maxLon = Math.max(maxLon, longitude);
        }
        return new DJILatLngBounds(new DJILatLng(maxLat, maxLon), new DJILatLng(minLat, minLon));
    }

    public static final class Builder {
        private List<DJILatLng> mLatLngList = new ArrayList();

        public DJILatLngBounds build() {
            if (this.mLatLngList.size() >= 2) {
                return DJILatLngBounds.fromLatLngs(this.mLatLngList);
            }
            throw new InvalidLatLngBoundsException(this.mLatLngList.size());
        }

        public Builder includes(List<DJILatLng> latLngs) {
            for (DJILatLng point : latLngs) {
                this.mLatLngList.add(point);
            }
            return this;
        }

        public Builder include(@NonNull DJILatLng latLng) {
            this.mLatLngList.add(latLng);
            return this;
        }
    }
}
