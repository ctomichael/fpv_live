package com.mapzen.android.lost.api;

import android.content.Intent;
import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class LocationResult implements Parcelable {
    public static final Parcelable.Creator<LocationResult> CREATOR = new Parcelable.Creator<LocationResult>() {
        /* class com.mapzen.android.lost.api.LocationResult.AnonymousClass1 */

        public LocationResult createFromParcel(Parcel in2) {
            return new LocationResult(in2);
        }

        public LocationResult[] newArray(int size) {
            return new LocationResult[size];
        }
    };
    public static final String EXTRA_LOCATION_RESULT = "com.mapzen.android.lost.EXTRA_LOCATION_RESULT";
    private final List<Location> locations;

    public static LocationResult create(List<Location> locations2) {
        if (locations2 == null) {
            locations2 = Collections.emptyList();
        }
        return new LocationResult(locations2);
    }

    LocationResult(List<Location> locations2) {
        this.locations = locations2;
    }

    @NonNull
    public List<Location> getLocations() {
        return this.locations;
    }

    public Location getLastLocation() {
        if (this.locations.size() == 0) {
            return null;
        }
        return this.locations.get(this.locations.size() - 1);
    }

    public static boolean hasResult(Intent intent) {
        return intent != null && intent.hasExtra(EXTRA_LOCATION_RESULT);
    }

    public static LocationResult extractResult(Intent intent) {
        if (!hasResult(intent)) {
            return null;
        }
        return (LocationResult) intent.getParcelableExtra(EXTRA_LOCATION_RESULT);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LocationResult that = (LocationResult) o;
        if (this.locations.size() != that.locations.size()) {
            return false;
        }
        Iterator<Location> thatIterator = that.locations.iterator();
        for (Location location : this.locations) {
            if (location.getTime() != thatIterator.next().getTime()) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        return this.locations.hashCode();
    }

    protected LocationResult(Parcel in2) {
        this.locations = in2.createTypedArrayList(Location.CREATOR);
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.locations);
    }

    public int describeContents() {
        return 0;
    }
}
