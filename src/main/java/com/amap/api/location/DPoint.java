package com.amap.api.location;

import android.os.Parcel;
import android.os.Parcelable;

public class DPoint implements Parcelable {
    public static final Parcelable.Creator<DPoint> CREATOR = new Parcelable.Creator<DPoint>() {
        /* class com.amap.api.location.DPoint.AnonymousClass1 */

        public final /* synthetic */ Object createFromParcel(Parcel parcel) {
            return new DPoint(parcel);
        }

        public final /* bridge */ /* synthetic */ Object[] newArray(int i) {
            return new DPoint[i];
        }
    };
    private double a = 0.0d;
    private double b = 0.0d;

    public DPoint() {
    }

    public DPoint(double d, double d2) {
        double d3 = 180.0d;
        double d4 = 90.0d;
        double d5 = -180.0d;
        double d6 = -90.0d;
        d3 = d2 <= 180.0d ? d2 : d3;
        d5 = d3 >= -180.0d ? d3 : d5;
        d4 = d <= 90.0d ? d : d4;
        d6 = d4 >= -90.0d ? d4 : d6;
        this.a = d5;
        this.b = d6;
    }

    protected DPoint(Parcel parcel) {
        this.a = parcel.readDouble();
        this.b = parcel.readDouble();
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof DPoint)) {
            return false;
        }
        DPoint dPoint = (DPoint) obj;
        return this.b == dPoint.b && this.a == dPoint.a;
    }

    public double getLatitude() {
        return this.b;
    }

    public double getLongitude() {
        return this.a;
    }

    public int hashCode() {
        return Double.valueOf((this.b + this.a) * 1000000.0d).intValue();
    }

    public void setLatitude(double d) {
        double d2 = 90.0d;
        double d3 = -90.0d;
        if (d <= 90.0d) {
            d2 = d;
        }
        if (d2 >= -90.0d) {
            d3 = d2;
        }
        this.b = d3;
    }

    public void setLongitude(double d) {
        double d2 = 180.0d;
        double d3 = -180.0d;
        if (d <= 180.0d) {
            d2 = d;
        }
        if (d2 >= -180.0d) {
            d3 = d2;
        }
        this.a = d3;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(this.a);
        parcel.writeDouble(this.b);
    }
}
