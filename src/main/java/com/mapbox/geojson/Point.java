package com.mapbox.geojson;

import android.support.annotation.FloatRange;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.mapbox.geojson.gson.GeoJsonAdapterFactory;
import com.mapbox.geojson.shifter.CoordinateShifterManager;
import java.io.IOException;
import java.util.List;

@Keep
public final class Point implements CoordinateContainer<List<Double>> {
    private static final String TYPE = "Point";
    @Nullable
    private final BoundingBox bbox;
    @NonNull
    private final List<Double> coordinates;
    @NonNull
    private final String type;

    public static Point fromJson(@NonNull String json) {
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapterFactory(GeoJsonAdapterFactory.create());
        return (Point) gson.create().fromJson(json, Point.class);
    }

    public static Point fromLngLat(@FloatRange(from = -180.0d, to = 180.0d) double longitude, @FloatRange(from = -90.0d, to = 90.0d) double latitude) {
        return new Point(TYPE, null, CoordinateShifterManager.getCoordinateShifter().shiftLonLat(longitude, latitude));
    }

    public static Point fromLngLat(@FloatRange(from = -180.0d, to = 180.0d) double longitude, @FloatRange(from = -90.0d, to = 90.0d) double latitude, @Nullable BoundingBox bbox2) {
        return new Point(TYPE, bbox2, CoordinateShifterManager.getCoordinateShifter().shiftLonLat(longitude, latitude));
    }

    public static Point fromLngLat(@FloatRange(from = -180.0d, to = 180.0d) double longitude, @FloatRange(from = -90.0d, to = 90.0d) double latitude, double altitude) {
        return new Point(TYPE, null, CoordinateShifterManager.getCoordinateShifter().shiftLonLatAlt(longitude, latitude, altitude));
    }

    public static Point fromLngLat(@FloatRange(from = -180.0d, to = 180.0d) double longitude, @FloatRange(from = -90.0d, to = 90.0d) double latitude, double altitude, @Nullable BoundingBox bbox2) {
        return new Point(TYPE, bbox2, CoordinateShifterManager.getCoordinateShifter().shiftLonLatAlt(longitude, latitude, altitude));
    }

    static Point fromLngLat(@NonNull double[] coords) {
        if (coords.length == 2) {
            return fromLngLat(coords[0], coords[1]);
        }
        if (coords.length > 2) {
            return fromLngLat(coords[0], coords[1], coords[2]);
        }
        return null;
    }

    Point(String type2, @Nullable BoundingBox bbox2, List<Double> coordinates2) {
        if (type2 == null) {
            throw new NullPointerException("Null type");
        }
        this.type = type2;
        this.bbox = bbox2;
        if (coordinates2 == null || coordinates2.size() == 0) {
            throw new NullPointerException("Null coordinates");
        }
        this.coordinates = coordinates2;
    }

    public double longitude() {
        return coordinates().get(0).doubleValue();
    }

    public double latitude() {
        return coordinates().get(1).doubleValue();
    }

    public double altitude() {
        if (coordinates().size() < 3) {
            return Double.NaN;
        }
        return coordinates().get(2).doubleValue();
    }

    public boolean hasAltitude() {
        return !Double.isNaN(altitude());
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
    public List<Double> coordinates() {
        return this.coordinates;
    }

    public String toJson() {
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapterFactory(GeoJsonAdapterFactory.create());
        return gson.create().toJson(this);
    }

    public static TypeAdapter<Point> typeAdapter(Gson gson) {
        return new GsonTypeAdapter(gson);
    }

    public String toString() {
        return "Point{type=" + this.type + ", bbox=" + this.bbox + ", coordinates=" + this.coordinates + "}";
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Point)) {
            return false;
        }
        Point that = (Point) obj;
        if (!this.type.equals(that.type()) || (this.bbox != null ? !this.bbox.equals(that.bbox()) : that.bbox() != null) || !this.coordinates.equals(that.coordinates())) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return (((((1 * 1000003) ^ this.type.hashCode()) * 1000003) ^ (this.bbox == null ? 0 : this.bbox.hashCode())) * 1000003) ^ this.coordinates.hashCode();
    }

    static final class GsonTypeAdapter extends BaseGeometryTypeAdapter<Point, List<Double>> {
        /* access modifiers changed from: package-private */
        public /* bridge */ /* synthetic */ CoordinateContainer createCoordinateContainer(String str, BoundingBox boundingBox, Object obj) {
            return createCoordinateContainer(str, boundingBox, (List<Double>) ((List) obj));
        }

        GsonTypeAdapter(Gson gson) {
            super(gson, new ListOfDoublesCoordinatesTypeAdapter());
        }

        public void write(JsonWriter jsonWriter, Point object) throws IOException {
            writeCoordinateContainer(jsonWriter, object);
        }

        public Point read(JsonReader jsonReader) throws IOException {
            return (Point) readCoordinateContainer(jsonReader);
        }

        /* access modifiers changed from: package-private */
        public CoordinateContainer<List<Double>> createCoordinateContainer(String type, BoundingBox bbox, List<Double> coordinates) {
            if (type == null) {
                type = Point.TYPE;
            }
            return new Point(type, bbox, coordinates);
        }
    }
}
