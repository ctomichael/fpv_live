package com.mapzen.android.lost.api;

public class LocationSettingsStates {
    private final boolean blePresent;
    private final boolean bleUsable;
    private final boolean gpsPresent;
    private final boolean gpsUsable;
    private final boolean networkPresent;
    private final boolean networkUsable;

    public LocationSettingsStates(boolean gpsUsable2, boolean networkUsable2, boolean bleUsable2, boolean gpsPresent2, boolean networkPresent2, boolean blePresent2) {
        this.gpsUsable = gpsUsable2;
        this.networkUsable = networkUsable2;
        this.bleUsable = bleUsable2;
        this.gpsPresent = gpsPresent2;
        this.networkPresent = networkPresent2;
        this.blePresent = blePresent2;
    }

    public boolean isGpsUsable() {
        return this.gpsUsable;
    }

    public boolean isGpsPresent() {
        return this.gpsPresent;
    }

    public boolean isNetworkLocationUsable() {
        return this.networkUsable;
    }

    public boolean isNetworkLocationPresent() {
        return this.networkPresent;
    }

    public boolean isLocationUsable() {
        return this.gpsUsable || this.networkUsable;
    }

    public boolean isLocationPresent() {
        return this.gpsPresent || this.networkPresent;
    }

    public boolean isBleUsable() {
        return this.bleUsable;
    }

    public boolean isBlePresent() {
        return this.blePresent;
    }
}
