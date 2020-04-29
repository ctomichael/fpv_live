package com.mapzen.android.lost.api;

public interface LocationCallback {
    void onLocationAvailability(LocationAvailability locationAvailability);

    void onLocationResult(LocationResult locationResult);
}
