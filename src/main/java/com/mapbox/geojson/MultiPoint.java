package com.mapbox.geojson;

import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.mapbox.geojson.gson.GeoJsonAdapterFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Keep
public final class MultiPoint implements CoordinateContainer<List<Point>> {
    private static final String TYPE = "MultiPoint";
    private final BoundingBox bbox;
    private final List<Point> coordinates;
    private final String type;

    public static MultiPoint fromJson(@NonNull String json) {
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapterFactory(GeoJsonAdapterFactory.create());
        return (MultiPoint) gson.create().fromJson(json, MultiPoint.class);
    }

    public static MultiPoint fromLngLats(@NonNull List<Point> points) {
        return new MultiPoint(TYPE, null, points);
    }

    public static MultiPoint fromLngLats(@NonNull List<Point> points, @Nullable BoundingBox bbox2) {
        return new MultiPoint(TYPE, bbox2, points);
    }

    static MultiPoint fromLngLats(@NonNull double[][] coordinates2) {
        ArrayList<Point> converted = new ArrayList<>(coordinates2.length);
        for (double[] dArr : coordinates2) {
            converted.add(Point.fromLngLat(dArr));
        }
        return new MultiPoint(TYPE, null, converted);
    }

    MultiPoint(String type2, @Nullable BoundingBox bbox2, List<Point> coordinates2) {
        if (type2 == null) {
            throw new NullPointerException("Null type");
        }
        this.type = type2;
        this.bbox = bbox2;
        if (coordinates2 == null) {
            throw new NullPointerException("Null coordinates");
        }
        this.coordinates = coordinates2;
    }

    @NonNull
    public String type() {
        return this.type;
    }

    @Nullable
    public BoundingBox bbox() {
        return this.bbox;
    }

    @NonNull
    public List<Point> coordinates() {
        return this.coordinates;
    }

    public String toJson() {
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapterFactory(GeoJsonAdapterFactory.create());
        return gson.create().toJson(this);
    }

    public static TypeAdapter<MultiPoint> typeAdapter(Gson gson) {
        return new GsonTypeAdapter(gson);
    }

    public String toString() {
        return "MultiPoint{type=" + this.type + ", bbox=" + this.bbox + ", coordinates=" + this.coordinates + "}";
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof MultiPoint)) {
            return false;
        }
        MultiPoint that = (MultiPoint) obj;
        if (!this.type.equals(that.type()) || (this.bbox != null ? !this.bbox.equals(that.bbox()) : that.bbox() != null) || !this.coordinates.equals(that.coordinates())) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return (((((1 * 1000003) ^ this.type.hashCode()) * 1000003) ^ (this.bbox == null ? 0 : this.bbox.hashCode())) * 1000003) ^ this.coordinates.hashCode();
    }

    static final class GsonTypeAdapter extends BaseGeometryTypeAdapter<MultiPoint, List<Point>> {
        /* access modifiers changed from: package-private */
        public /* bridge */ /* synthetic */ CoordinateContainer createCoordinateContainer(String str, BoundingBox boundingBox, Object obj) {
            return createCoordinateContainer(str, boundingBox, (List<Point>) ((List) obj));
        }

        GsonTypeAdapter(Gson gson) {
            super(gson, new ListOfPointCoordinatesTypeAdapter());
        }

        public void write(JsonWriter jsonWriter, MultiPoint object) throws IOException {
            writeCoordinateContainer(jsonWriter, object);
        }

        public MultiPoint read(JsonReader jsonReader) throws IOException {
            return (MultiPoint) readCoordinateContainer(jsonReader);
        }

        /* access modifiers changed from: package-private */
        public CoordinateContainer<List<Point>> createCoordinateContainer(String type, BoundingBox bbox, List<Point> coordinates) {
            if (type == null) {
                type = MultiPoint.TYPE;
            }
            return new MultiPoint(type, bbox, coordinates);
        }
    }
}
