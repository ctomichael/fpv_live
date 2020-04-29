package com.dji.mapkit.core.places;

import android.support.annotation.NonNull;
import com.dji.mapkit.core.models.DJILatLng;

public class DJIPoiItem {
    String address;
    DJILatLng location;
    String name;

    private DJIPoiItem() {
    }

    public DJIPoiItem(@NonNull String name2, @NonNull String address2, @NonNull DJILatLng location2) {
        this.name = name2;
        this.address = address2;
        this.location = location2;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address2) {
        this.address = address2;
    }

    public DJILatLng getLocation() {
        return this.location;
    }

    public void setLocation(DJILatLng location2) {
        this.location = location2;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DJIPoiItem poiItem = (DJIPoiItem) o;
        if (!this.name.equals(poiItem.name) || !this.address.equals(poiItem.address)) {
            return false;
        }
        return this.location.equals(poiItem.location);
    }

    public int hashCode() {
        return (((this.name.hashCode() * 31) + this.address.hashCode()) * 31) + this.location.hashCode();
    }

    public String toString() {
        return "Name: " + this.name + ", Address: " + this.address + ", location: " + this.location;
    }
}
