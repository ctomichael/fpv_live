package com.mapbox.mapboxsdk.geometry;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import dji.component.accountcenter.IMemberProtocol;

public class ProjectedMeters implements Parcelable {
    public static final Parcelable.Creator<ProjectedMeters> CREATOR = new Parcelable.Creator<ProjectedMeters>() {
        /* class com.mapbox.mapboxsdk.geometry.ProjectedMeters.AnonymousClass1 */

        public ProjectedMeters createFromParcel(@NonNull Parcel in2) {
            return new ProjectedMeters(in2);
        }

        public ProjectedMeters[] newArray(int size) {
            return new ProjectedMeters[size];
        }
    };
    private double easting;
    private double northing;

    @Keep
    public ProjectedMeters(double northing2, double easting2) {
        this.northing = northing2;
        this.easting = easting2;
    }

    public ProjectedMeters(ProjectedMeters projectedMeters) {
        this.northing = projectedMeters.northing;
        this.easting = projectedMeters.easting;
    }

    private ProjectedMeters(Parcel in2) {
        this.northing = in2.readDouble();
        this.easting = in2.readDouble();
    }

    public double getNorthing() {
        return this.northing;
    }

    public double getEasting() {
        return this.easting;
    }

    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        ProjectedMeters projectedMeters = (ProjectedMeters) other;
        if (Double.compare(projectedMeters.easting, this.easting) == 0 && Double.compare(projectedMeters.northing, this.northing) == 0) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        long temp = Double.doubleToLongBits(this.easting);
        int result = (int) ((temp >>> 32) ^ temp);
        long temp2 = Double.doubleToLongBits(this.northing);
        return (result * 31) + ((int) ((temp2 >>> 32) ^ temp2));
    }

    @NonNull
    public String toString() {
        return "ProjectedMeters [northing=" + this.northing + ", easting=" + this.easting + IMemberProtocol.STRING_SEPERATOR_RIGHT;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(@NonNull Parcel out, int flags) {
        out.writeDouble(this.northing);
        out.writeDouble(this.easting);
    }
}
