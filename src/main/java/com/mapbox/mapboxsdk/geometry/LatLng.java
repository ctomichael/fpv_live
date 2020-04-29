package com.mapbox.mapboxsdk.geometry;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.FloatRange;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.mapbox.geojson.Point;
import com.mapbox.turf.TurfConstants;
import com.mapbox.turf.TurfMeasurement;
import dji.component.accountcenter.IMemberProtocol;

public class LatLng implements Parcelable {
    public static final Parcelable.Creator<LatLng> CREATOR = new Parcelable.Creator<LatLng>() {
        /* class com.mapbox.mapboxsdk.geometry.LatLng.AnonymousClass1 */

        public LatLng createFromParcel(@NonNull Parcel in2) {
            return new LatLng(in2);
        }

        public LatLng[] newArray(int size) {
            return new LatLng[size];
        }
    };
    private double altitude;
    @Keep
    private double latitude;
    @Keep
    private double longitude;

    public LatLng() {
        this.altitude = 0.0d;
        this.latitude = 0.0d;
        this.longitude = 0.0d;
    }

    @Keep
    public LatLng(double latitude2, double longitude2) {
        this.altitude = 0.0d;
        setLatitude(latitude2);
        setLongitude(longitude2);
    }

    public LatLng(double latitude2, double longitude2, double altitude2) {
        this.altitude = 0.0d;
        setLatitude(latitude2);
        setLongitude(longitude2);
        setAltitude(altitude2);
    }

    public LatLng(Location location) {
        this(location.getLatitude(), location.getLongitude(), location.getAltitude());
    }

    public LatLng(LatLng latLng) {
        this.altitude = 0.0d;
        this.latitude = latLng.latitude;
        this.longitude = latLng.longitude;
        this.altitude = latLng.altitude;
    }

    protected LatLng(Parcel in2) {
        this.altitude = 0.0d;
        setLatitude(in2.readDouble());
        setLongitude(in2.readDouble());
        setAltitude(in2.readDouble());
    }

    public void setLatitude(@FloatRange(from = -90.0d, to = 90.0d) double latitude2) {
        if (Double.isNaN(latitude2)) {
            throw new IllegalArgumentException("latitude must not be NaN");
        } else if (Math.abs(latitude2) > 90.0d) {
            throw new IllegalArgumentException("latitude must be between -90 and 90");
        } else {
            this.latitude = latitude2;
        }
    }

    public double getLatitude() {
        return this.latitude;
    }

    public void setLongitude(@FloatRange(from = -1.7976931348623157E308d, to = Double.MAX_VALUE) double longitude2) {
        if (Double.isNaN(longitude2)) {
            throw new IllegalArgumentException("longitude must not be NaN");
        } else if (Double.isInfinite(longitude2)) {
            throw new IllegalArgumentException("longitude must not be infinite");
        } else {
            this.longitude = longitude2;
        }
    }

    public double getLongitude() {
        return this.longitude;
    }

    public void setAltitude(double altitude2) {
        this.altitude = altitude2;
    }

    public double getAltitude() {
        return this.altitude;
    }

    @NonNull
    public LatLng wrap() {
        return new LatLng(this.latitude, wrap(this.longitude, -180.0d, 180.0d));
    }

    static double wrap(double value, double min, double max) {
        double delta = max - min;
        double secondMod = (((value - min) % delta) + delta) % delta;
        return (value < max || secondMod != 0.0d) ? secondMod + min : max;
    }

    public boolean equals(@Nullable Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        LatLng latLng = (LatLng) object;
        if (Double.compare(latLng.altitude, this.altitude) == 0 && Double.compare(latLng.latitude, this.latitude) == 0 && Double.compare(latLng.longitude, this.longitude) == 0) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        long temp = Double.doubleToLongBits(this.latitude);
        int result = (int) ((temp >>> 32) ^ temp);
        long temp2 = Double.doubleToLongBits(this.longitude);
        int result2 = (result * 31) + ((int) ((temp2 >>> 32) ^ temp2));
        long temp3 = Double.doubleToLongBits(this.altitude);
        return (result2 * 31) + ((int) ((temp3 >>> 32) ^ temp3));
    }

    @NonNull
    public String toString() {
        return "LatLng [latitude=" + this.latitude + ", longitude=" + this.longitude + ", altitude=" + this.altitude + IMemberProtocol.STRING_SEPERATOR_RIGHT;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(@NonNull Parcel out, int flags) {
        out.writeDouble(this.latitude);
        out.writeDouble(this.longitude);
        out.writeDouble(this.altitude);
    }

    public double distanceTo(@NonNull LatLng other) {
        return TurfMeasurement.distance(Point.fromLngLat(this.longitude, this.latitude), Point.fromLngLat(other.getLongitude(), other.getLatitude()), TurfConstants.UNIT_METRES);
    }
}
