package com.mapbox.mapboxsdk.geometry;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

public class LatLngSpan implements Parcelable {
    public static final Parcelable.Creator<LatLngSpan> CREATOR = new Parcelable.Creator<LatLngSpan>() {
        /* class com.mapbox.mapboxsdk.geometry.LatLngSpan.AnonymousClass1 */

        public LatLngSpan createFromParcel(@NonNull Parcel in2) {
            return new LatLngSpan(in2);
        }

        public LatLngSpan[] newArray(int size) {
            return new LatLngSpan[size];
        }
    };
    private double mLatitudeSpan;
    private double mLongitudeSpan;

    private LatLngSpan(@NonNull Parcel in2) {
        this.mLatitudeSpan = in2.readDouble();
        this.mLongitudeSpan = in2.readDouble();
    }

    public LatLngSpan(double latitudeSpan, double longitudeSpan) {
        this.mLatitudeSpan = latitudeSpan;
        this.mLongitudeSpan = longitudeSpan;
    }

    public double getLatitudeSpan() {
        return this.mLatitudeSpan;
    }

    public void setLatitudeSpan(double latitudeSpan) {
        this.mLatitudeSpan = latitudeSpan;
    }

    public double getLongitudeSpan() {
        return this.mLongitudeSpan;
    }

    public void setLongitudeSpan(double longitudeSpan) {
        this.mLongitudeSpan = longitudeSpan;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof LatLngSpan)) {
            return false;
        }
        LatLngSpan other = (LatLngSpan) object;
        if (this.mLongitudeSpan == other.getLongitudeSpan() && this.mLatitudeSpan == other.getLatitudeSpan()) {
            return true;
        }
        return false;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(@NonNull Parcel out, int flags) {
        out.writeDouble(this.mLatitudeSpan);
        out.writeDouble(this.mLongitudeSpan);
    }

    public int hashCode() {
        long temp = Double.doubleToLongBits(this.mLatitudeSpan);
        int result = (int) ((temp >>> 32) ^ temp);
        long temp2 = Double.doubleToLongBits(this.mLongitudeSpan);
        return (result * 31) + ((int) ((temp2 >>> 32) ^ temp2));
    }
}
