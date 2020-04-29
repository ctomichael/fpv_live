package com.mapbox.mapboxsdk.geometry;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import com.dji.mapkit.lbs.configuration.Defaults;

public class VisibleRegion implements Parcelable {
    public static final Parcelable.Creator<VisibleRegion> CREATOR = new Parcelable.Creator<VisibleRegion>() {
        /* class com.mapbox.mapboxsdk.geometry.VisibleRegion.AnonymousClass1 */

        public VisibleRegion createFromParcel(@NonNull Parcel in2) {
            return new VisibleRegion(in2);
        }

        public VisibleRegion[] newArray(int size) {
            return new VisibleRegion[size];
        }
    };
    public final LatLng farLeft;
    public final LatLng farRight;
    public final LatLngBounds latLngBounds;
    public final LatLng nearLeft;
    public final LatLng nearRight;

    private VisibleRegion(Parcel in2) {
        this.farLeft = (LatLng) in2.readParcelable(LatLng.class.getClassLoader());
        this.farRight = (LatLng) in2.readParcelable(LatLng.class.getClassLoader());
        this.nearLeft = (LatLng) in2.readParcelable(LatLng.class.getClassLoader());
        this.nearRight = (LatLng) in2.readParcelable(LatLng.class.getClassLoader());
        this.latLngBounds = (LatLngBounds) in2.readParcelable(LatLngBounds.class.getClassLoader());
    }

    public VisibleRegion(LatLng farLeft2, LatLng farRight2, LatLng nearLeft2, LatLng nearRight2, LatLngBounds latLngBounds2) {
        this.farLeft = farLeft2;
        this.farRight = farRight2;
        this.nearLeft = nearLeft2;
        this.nearRight = nearRight2;
        this.latLngBounds = latLngBounds2;
    }

    public boolean equals(Object o) {
        if (!(o instanceof VisibleRegion)) {
            return false;
        }
        if (o == this) {
            return true;
        }
        VisibleRegion visibleRegion = (VisibleRegion) o;
        if (!this.farLeft.equals(visibleRegion.farLeft) || !this.farRight.equals(visibleRegion.farRight) || !this.nearLeft.equals(visibleRegion.nearLeft) || !this.nearRight.equals(visibleRegion.nearRight) || !this.latLngBounds.equals(visibleRegion.latLngBounds)) {
            return false;
        }
        return true;
    }

    @NonNull
    public String toString() {
        return "[farLeft [" + this.farLeft + "], farRight [" + this.farRight + "], nearLeft [" + this.nearLeft + "], nearRight [" + this.nearRight + "], latLngBounds [" + this.latLngBounds + "]]";
    }

    public int hashCode() {
        return this.farLeft.hashCode() + 90 + ((this.farRight.hashCode() + 90) * 1000) + ((this.nearLeft.hashCode() + 180) * Defaults.SECOND_IN_NANOS) + ((this.nearRight.hashCode() + 180) * 1000000000);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(@NonNull Parcel out, int flags) {
        out.writeParcelable(this.farLeft, flags);
        out.writeParcelable(this.farRight, flags);
        out.writeParcelable(this.nearLeft, flags);
        out.writeParcelable(this.nearRight, flags);
        out.writeParcelable(this.latLngBounds, flags);
    }
}
