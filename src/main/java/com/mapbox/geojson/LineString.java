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
import com.mapbox.geojson.utils.PolylineUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Keep
public final class LineString implements CoordinateContainer<List<Point>> {
    private static final String TYPE = "LineString";
    private final BoundingBox bbox;
    private final List<Point> coordinates;
    private final String type;

    public static LineString fromJson(String json) {
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapterFactory(GeoJsonAdapterFactory.create());
        return (LineString) gson.create().fromJson(json, LineString.class);
    }

    public static LineString fromLngLats(@NonNull MultiPoint multiPoint) {
        return new LineString(TYPE, null, multiPoint.coordinates());
    }

    public static LineString fromLngLats(@NonNull List<Point> points) {
        return new LineString(TYPE, null, points);
    }

    public static LineString fromLngLats(@NonNull List<Point> points, @Nullable BoundingBox bbox2) {
        return new LineString(TYPE, bbox2, points);
    }

    public static LineString fromLngLats(@NonNull MultiPoint multiPoint, @Nullable BoundingBox bbox2) {
        return new LineString(TYPE, bbox2, multiPoint.coordinates());
    }

    LineString(String type2, @Nullable BoundingBox bbox2, List<Point> coordinates2) {
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

    static LineString fromLngLats(double[][] coordinates2) {
        ArrayList<Point> converted = new ArrayList<>(coordinates2.length);
        for (double[] dArr : coordinates2) {
            converted.add(Point.fromLngLat(dArr));
        }
        return fromLngLats(converted);
    }

    public static LineString fromPolyline(@NonNull String polyline, int precision) {
        return fromLngLats(PolylineUtils.decode(polyline, precision), (BoundingBox) null);
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

    public String toPolyline(int precision) {
        return PolylineUtils.encode(coordinates(), precision);
    }

    public static TypeAdapter<LineString> typeAdapter(Gson gson) {
        return new GsonTypeAdapter(gson);
    }

    public String toString() {
        return "LineString{type=" + this.type + ", bbox=" + this.bbox + ", coordinates=" + this.coordinates + "}";
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof LineString)) {
            return false;
        }
        LineString that = (LineString) obj;
        if (!this.type.equals(that.type()) || (this.bbox != null ? !this.bbox.equals(that.bbox()) : that.bbox() != null) || !this.coordinates.equals(that.coordinates())) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return (((((1 * 1000003) ^ this.type.hashCode()) * 1000003) ^ (this.bbox == null ? 0 : this.bbox.hashCode())) * 1000003) ^ this.coordinates.hashCode();
    }

    static final class GsonTypeAdapter extends BaseGeometryTypeAdapter<LineString, List<Point>> {
        /* access modifiers changed from: package-private */
        public /* bridge */ /* synthetic */ CoordinateContainer createCoordinateContainer(String str, BoundingBox boundingBox, Object obj) {
            return createCoordinateContainer(str, boundingBox, (List<Point>) ((List) obj));
        }

        GsonTypeAdapter(Gson gson) {
            super(gson, new ListOfPointCoordinatesTypeAdapter());
        }

        public void write(JsonWriter jsonWriter, LineString object) throws IOException {
            writeCoordinateContainer(jsonWriter, object);
        }

        public LineString read(JsonReader jsonReader) throws IOException {
            return (LineString) readCoordinateContainer(jsonReader);
        }

        /* access modifiers changed from: package-private */
        public CoordinateContainer<List<Point>> createCoordinateContainer(String type, BoundingBox bbox, List<Point> coordinates) {
            if (type == null) {
                type = LineString.TYPE;
            }
            return new LineString(type, bbox, coordinates);
        }
    }
}
