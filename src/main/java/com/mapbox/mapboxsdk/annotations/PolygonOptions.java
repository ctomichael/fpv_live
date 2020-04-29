package com.mapbox.mapboxsdk.annotations;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.mapbox.mapboxsdk.geometry.LatLng;
import java.util.ArrayList;
import java.util.List;

@Deprecated
public final class PolygonOptions implements Parcelable {
    public static final Parcelable.Creator<PolygonOptions> CREATOR = new Parcelable.Creator<PolygonOptions>() {
        /* class com.mapbox.mapboxsdk.annotations.PolygonOptions.AnonymousClass1 */

        public PolygonOptions createFromParcel(@NonNull Parcel in2) {
            return new PolygonOptions(in2);
        }

        public PolygonOptions[] newArray(int size) {
            return new PolygonOptions[size];
        }
    };
    private Polygon polygon;

    private PolygonOptions(Parcel in2) {
        this.polygon = new Polygon();
        List<LatLng> pointsList = new ArrayList<>();
        in2.readList(pointsList, LatLng.class.getClassLoader());
        addAll(pointsList);
        List<List<LatLng>> holes = new ArrayList<>();
        in2.readList(holes, LatLng.class.getClassLoader());
        addAllHoles(holes);
        alpha(in2.readFloat());
        fillColor(in2.readInt());
        strokeColor(in2.readInt());
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeList(getPoints());
        out.writeList(getHoles());
        out.writeFloat(getAlpha());
        out.writeInt(getFillColor());
        out.writeInt(getStrokeColor());
    }

    public PolygonOptions() {
        this.polygon = new Polygon();
    }

    @NonNull
    public PolygonOptions add(LatLng point) {
        this.polygon.addPoint(point);
        return this;
    }

    @NonNull
    public PolygonOptions add(LatLng... points) {
        for (LatLng point : points) {
            add(point);
        }
        return this;
    }

    @NonNull
    public PolygonOptions addAll(Iterable<LatLng> points) {
        for (LatLng point : points) {
            add(point);
        }
        return this;
    }

    @NonNull
    public PolygonOptions addHole(List<LatLng> hole) {
        this.polygon.addHole(hole);
        return this;
    }

    @NonNull
    public PolygonOptions addHole(List<LatLng>... holes) {
        for (List<LatLng> hole : holes) {
            addHole(hole);
        }
        return this;
    }

    @NonNull
    public PolygonOptions addAllHoles(Iterable<List<LatLng>> holes) {
        for (List<LatLng> hole : holes) {
            addHole(hole);
        }
        return this;
    }

    @NonNull
    public PolygonOptions alpha(float alpha) {
        this.polygon.setAlpha(alpha);
        return this;
    }

    public float getAlpha() {
        return this.polygon.getAlpha();
    }

    @NonNull
    public PolygonOptions fillColor(int color) {
        this.polygon.setFillColor(color);
        return this;
    }

    public int getFillColor() {
        return this.polygon.getFillColor();
    }

    public Polygon getPolygon() {
        return this.polygon;
    }

    @NonNull
    public PolygonOptions strokeColor(int color) {
        this.polygon.setStrokeColor(color);
        return this;
    }

    public int getStrokeColor() {
        return this.polygon.getStrokeColor();
    }

    public List<LatLng> getPoints() {
        return this.polygon.getPoints();
    }

    public List<List<LatLng>> getHoles() {
        return this.polygon.getHoles();
    }

    public boolean equals(@Nullable Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PolygonOptions polygon2 = (PolygonOptions) o;
        if (Float.compare(polygon2.getAlpha(), getAlpha()) != 0 || getFillColor() != polygon2.getFillColor() || getStrokeColor() != polygon2.getStrokeColor()) {
            return false;
        }
        if (getPoints() != null) {
            if (!getPoints().equals(polygon2.getPoints())) {
                return false;
            }
        } else if (polygon2.getPoints() != null) {
            return false;
        }
        if (getHoles() == null ? polygon2.getHoles() != null : !getHoles().equals(polygon2.getHoles())) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        int i;
        int i2 = 0;
        int floatToIntBits = ((((((getAlpha() != 0.0f ? Float.floatToIntBits(getAlpha()) : 0) + 31) * 31) + getFillColor()) * 31) + getStrokeColor()) * 31;
        if (getPoints() != null) {
            i = getPoints().hashCode();
        } else {
            i = 0;
        }
        int i3 = (floatToIntBits + i) * 31;
        if (getHoles() != null) {
            i2 = getHoles().hashCode();
        }
        return i3 + i2;
    }
}
