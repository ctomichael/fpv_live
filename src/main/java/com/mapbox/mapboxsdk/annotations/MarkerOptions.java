package com.mapbox.mapboxsdk.annotations;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.mapbox.mapboxsdk.exceptions.InvalidMarkerPositionException;
import com.mapbox.mapboxsdk.geometry.LatLng;

@Deprecated
public final class MarkerOptions extends BaseMarkerOptions<Marker, MarkerOptions> implements Parcelable {
    public static final Parcelable.Creator<MarkerOptions> CREATOR = new Parcelable.Creator<MarkerOptions>() {
        /* class com.mapbox.mapboxsdk.annotations.MarkerOptions.AnonymousClass1 */

        public MarkerOptions createFromParcel(@NonNull Parcel in2) {
            return new MarkerOptions(in2);
        }

        public MarkerOptions[] newArray(int size) {
            return new MarkerOptions[size];
        }
    };

    public MarkerOptions() {
    }

    protected MarkerOptions(Parcel in2) {
        position((LatLng) in2.readParcelable(LatLng.class.getClassLoader()));
        snippet(in2.readString());
        title(in2.readString());
        if (in2.readByte() != 0) {
            icon(new Icon(in2.readString(), (Bitmap) in2.readParcelable(Bitmap.class.getClassLoader())));
        }
    }

    @NonNull
    public MarkerOptions getThis() {
        return this;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeParcelable(getPosition(), flags);
        out.writeString(getSnippet());
        out.writeString(getTitle());
        Icon icon = getIcon();
        out.writeByte((byte) (icon != null ? 1 : 0));
        if (icon != null) {
            out.writeString(getIcon().getId());
            out.writeParcelable(getIcon().getBitmap(), flags);
        }
    }

    public Marker getMarker() {
        if (this.position != null) {
            return new Marker(this.position, this.icon, this.title, this.snippet);
        }
        throw new InvalidMarkerPositionException();
    }

    public LatLng getPosition() {
        return this.position;
    }

    public String getSnippet() {
        return this.snippet;
    }

    public String getTitle() {
        return this.title;
    }

    public Icon getIcon() {
        return this.icon;
    }

    public boolean equals(@Nullable Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MarkerOptions marker = (MarkerOptions) o;
        if (getPosition() != null) {
            if (!getPosition().equals(marker.getPosition())) {
                return false;
            }
        } else if (marker.getPosition() != null) {
            return false;
        }
        if (getSnippet() != null) {
            if (!getSnippet().equals(marker.getSnippet())) {
                return false;
            }
        } else if (marker.getSnippet() != null) {
            return false;
        }
        if (getIcon() != null) {
            if (!getIcon().equals(marker.getIcon())) {
                return false;
            }
        } else if (marker.getIcon() != null) {
            return false;
        }
        if (getTitle() == null ? marker.getTitle() != null : !getTitle().equals(marker.getTitle())) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        int i;
        int i2;
        int i3;
        int i4 = 0;
        if (getPosition() != null) {
            i = getPosition().hashCode();
        } else {
            i = 0;
        }
        int i5 = (i + 31) * 31;
        if (getSnippet() != null) {
            i2 = getSnippet().hashCode();
        } else {
            i2 = 0;
        }
        int i6 = (i5 + i2) * 31;
        if (getIcon() != null) {
            i3 = getIcon().hashCode();
        } else {
            i3 = 0;
        }
        int i7 = (i6 + i3) * 31;
        if (getTitle() != null) {
            i4 = getTitle().hashCode();
        }
        return i7 + i4;
    }
}
