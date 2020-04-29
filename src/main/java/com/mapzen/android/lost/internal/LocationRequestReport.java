package com.mapzen.android.lost.internal;

import android.location.Location;
import com.mapzen.android.lost.api.LocationRequest;

class LocationRequestReport {
    Location location;
    final LocationRequest locationRequest;

    LocationRequestReport(LocationRequest request) {
        this.locationRequest = request;
    }
}
