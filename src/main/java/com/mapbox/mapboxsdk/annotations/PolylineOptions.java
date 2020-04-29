package com.mapbox.mapboxsdk.annotations;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.mapbox.mapboxsdk.geometry.LatLng;
import java.util.ArrayList;
import java.util.List;

@Deprecated
public final class PolylineOptions implements Parcelable {
    public static final Parcelable.Creator<PolylineOptions> CREATOR = new Parcelable.Creator<PolylineOptions>() {
        /* class com.mapbox.mapboxsdk.annotations.PolylineOptions.AnonymousClass1 */

        public PolylineOptions createFromParcel(@NonNull Parcel in2) {
            return new PolylineOptions(in2);
        }

        public PolylineOptions[] newArray(int size) {
            return new PolylineOptions[size];
        }
    };
    private Polyline polyline;

    private PolylineOptions(Parcel in2) {
        this.polyline = new Polyline();
        ArrayList<LatLng> pointsList = new ArrayList<>();
        in2.readList(pointsList, LatLng.class.getClassLoader());
        addAll(pointsList);
        alpha(in2.readFloat());
        color(in2.readInt());
        width(in2.readFloat());
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeList(getPoints());
        out.writeFloat(getAlpha());
        out.writeInt(getColor());
        out.writeFloat(getWidth());
    }

    public PolylineOptions() {
        this.polyline = new Polyline();
    }

    @NonNull
    public PolylineOptions add(LatLng point) {
        this.polyline.addPoint(point);
        return this;
    }

    @NonNull
    public PolylineOptions add(LatLng... points) {
        for (LatLng point : points) {
            add(point);
        }
        return this;
    }

    @NonNull
    public PolylineOptions addAll(Iterable<LatLng> points) {
        for (LatLng point : points) {
            add(point);
        }
        return this;
    }

    @NonNull
    public PolylineOptions alpha(float alpha) {
        this.polyline.setAlpha(alpha);
        return this;
    }

    public float getAlpha() {
        return this.polyline.getAlpha();
    }

    @NonNull
    public PolylineOptions color(int color) {
        this.polyline.setColor(color);
        return this;
    }

    public int getColor() {
        return this.polyline.getColor();
    }

    public Polyline getPolyline() {
        return this.polyline;
    }

    public float getWidth() {
        return this.polyline.getWidth();
    }

    @NonNull
    public PolylineOptions width(float width) {
        this.polyline.setWidth(width);
        return this;
    }

    public List<LatLng> getPoints() {
        return this.polyline.getPoints();
    }

    public boolean equals(@Nullable Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PolylineOptions polyline2 = (PolylineOptions) o;
        if (Float.compare(polyline2.getAlpha(), getAlpha()) != 0 || getColor() != polyline2.getColor() || Float.compare(polyline2.getWidth(), getWidth()) != 0) {
            return false;
        }
        if (getPoints() == null ? polyline2.getPoints() != null : !getPoints().equals(polyline2.getPoints())) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        int i;
        int i2;
        int i3 = 0;
        if (getAlpha() != 0.0f) {
            i = Float.floatToIntBits(getAlpha());
        } else {
            i = 0;
        }
        int color = (((i + 31) * 31) + getColor()) * 31;
        if (getWidth() != 0.0f) {
            i2 = Float.floatToIntBits(getWidth());
        } else {
            i2 = 0;
        }
        int i4 = (color + i2) * 31;
        if (getPoints() != null) {
            i3 = getPoints().hashCode();
        }
        return i4 + i3;
    }
}
