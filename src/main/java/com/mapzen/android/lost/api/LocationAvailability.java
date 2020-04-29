package com.mapzen.android.lost.api;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

public class LocationAvailability implements Parcelable {
    public static final Parcelable.Creator<LocationAvailability> CREATOR = new Parcelable.Creator<LocationAvailability>() {
        /* class com.mapzen.android.lost.api.LocationAvailability.AnonymousClass1 */

        public LocationAvailability createFromParcel(Parcel in2) {
            return new LocationAvailability(in2);
        }

        public LocationAvailability[] newArray(int size) {
            return new LocationAvailability[size];
        }
    };
    public static final String EXTRA_LOCATION_AVAILABILITY = "com.mapzen.android.lost.EXTRA_LOCATION_AVAILABILITY";
    boolean locationAvailable = false;

    public LocationAvailability(boolean available) {
        this.locationAvailable = available;
    }

    protected LocationAvailability(Parcel in2) {
        boolean z = false;
        this.locationAvailable = in2.readByte() != 0 ? true : z;
    }

    public static LocationAvailability extractLocationAvailability(Intent intent) {
        if (hasLocationAvailability(intent)) {
            return (LocationAvailability) intent.getExtras().getParcelable(EXTRA_LOCATION_AVAILABILITY);
        }
        return null;
    }

    public static boolean hasLocationAvailability(Intent intent) {
        return intent.hasExtra(EXTRA_LOCATION_AVAILABILITY);
    }

    public boolean isLocationAvailable() {
        return this.locationAvailable;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (this.locationAvailable != ((LocationAvailability) o).locationAvailable) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return this.locationAvailable ? 1 : 0;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (this.locationAvailable ? 1 : 0));
    }
}
