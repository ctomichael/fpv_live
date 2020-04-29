package com.mapzen.android.lost.api;

public class LocationSettingsResult implements Result {
    private final LocationSettingsStates locationSettingsStates;
    private final Status status;

    public LocationSettingsResult(Status status2, LocationSettingsStates states) {
        this.status = status2;
        this.locationSettingsStates = states;
    }

    public LocationSettingsStates getLocationSettingsStates() {
        return this.locationSettingsStates;
    }

    public Status getStatus() {
        return this.status;
    }
}
