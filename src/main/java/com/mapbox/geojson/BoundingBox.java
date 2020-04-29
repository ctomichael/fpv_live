package com.mapbox.geojson;

import android.support.annotation.FloatRange;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.mapbox.geojson.gson.BoundingBoxTypeAdapter;
import java.io.Serializable;

@Keep
public class BoundingBox implements Serializable {
    private final Point northeast;
    private final Point southwest;

    public static BoundingBox fromJson(String json) {
        return (BoundingBox) new GsonBuilder().registerTypeAdapter(BoundingBox.class, new BoundingBoxTypeAdapter()).create().fromJson(json, BoundingBox.class);
    }

    public static BoundingBox fromPoints(@NonNull Point southwest2, @NonNull Point northeast2) {
        return new BoundingBox(southwest2, northeast2);
    }

    @Deprecated
    public static BoundingBox fromCoordinates(@FloatRange(from = -180.0d, to = 180.0d) double west, @FloatRange(from = -90.0d, to = 90.0d) double south, @FloatRange(from = -180.0d, to = 180.0d) double east, @FloatRange(from = -90.0d, to = 90.0d) double north) {
        return fromLngLats(west, south, east, north);
    }

    @Deprecated
    public static BoundingBox fromCoordinates(@FloatRange(from = -180.0d, to = 180.0d) double west, @FloatRange(from = -90.0d, to = 90.0d) double south, double southwestAltitude, @FloatRange(from = -180.0d, to = 180.0d) double east, @FloatRange(from = -90.0d, to = 90.0d) double north, double northEastAltitude) {
        return fromLngLats(west, south, southwestAltitude, east, north, northEastAltitude);
    }

    public static BoundingBox fromLngLats(@FloatRange(from = -180.0d, to = 180.0d) double west, @FloatRange(from = -90.0d, to = 90.0d) double south, @FloatRange(from = -180.0d, to = 180.0d) double east, @FloatRange(from = -90.0d, to = 90.0d) double north) {
        return new BoundingBox(Point.fromLngLat(west, south), Point.fromLngLat(east, north));
    }

    public static BoundingBox fromLngLats(@FloatRange(from = -180.0d, to = 180.0d) double west, @FloatRange(from = -90.0d, to = 90.0d) double south, double southwestAltitude, @FloatRange(from = -180.0d, to = 180.0d) double east, @FloatRange(from = -90.0d, to = 90.0d) double north, double northEastAltitude) {
        return new BoundingBox(Point.fromLngLat(west, south, southwestAltitude), Point.fromLngLat(east, north, northEastAltitude));
    }

    BoundingBox(Point southwest2, Point northeast2) {
        if (southwest2 == null) {
            throw new NullPointerException("Null southwest");
        }
        this.southwest = southwest2;
        if (northeast2 == null) {
            throw new NullPointerException("Null northeast");
        }
        this.northeast = northeast2;
    }

    @NonNull
    public Point southwest() {
        return this.southwest;
    }

    @NonNull
    public Point northeast() {
        return this.northeast;
    }

    public final double west() {
        return southwest().longitude();
    }

    public final double south() {
        return southwest().latitude();
    }

    public final double east() {
        return northeast().longitude();
    }

    public final double north() {
        return northeast().latitude();
    }

    public static TypeAdapter<BoundingBox> typeAdapter(Gson gson) {
        return new BoundingBoxTypeAdapter();
    }

    public final String toJson() {
        return new GsonBuilder().registerTypeAdapter(BoundingBox.class, new BoundingBoxTypeAdapter()).create().toJson(this, BoundingBox.class);
    }

    public String toString() {
        return "BoundingBox{southwest=" + this.southwest + ", northeast=" + this.northeast + "}";
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof BoundingBox)) {
            return false;
        }
        BoundingBox that = (BoundingBox) obj;
        if (!this.southwest.equals(that.southwest()) || !this.northeast.equals(that.northeast())) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return (((1 * 1000003) ^ this.southwest.hashCode()) * 1000003) ^ this.northeast.hashCode();
    }
}
