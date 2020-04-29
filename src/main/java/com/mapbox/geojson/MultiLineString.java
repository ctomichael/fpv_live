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
import java.util.Arrays;
import java.util.List;

@Keep
public final class MultiLineString implements CoordinateContainer<List<List<Point>>> {
    private static final String TYPE = "MultiLineString";
    private final BoundingBox bbox;
    private final List<List<Point>> coordinates;
    private final String type;

    public static MultiLineString fromJson(@NonNull String json) {
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapterFactory(GeoJsonAdapterFactory.create());
        return (MultiLineString) gson.create().fromJson(json, MultiLineString.class);
    }

    public static MultiLineString fromLineStrings(@NonNull List<LineString> lineStrings) {
        List<List<Point>> coordinates2 = new ArrayList<>(lineStrings.size());
        for (LineString lineString : lineStrings) {
            coordinates2.add(lineString.coordinates());
        }
        return new MultiLineString(TYPE, null, coordinates2);
    }

    public static MultiLineString fromLineStrings(@NonNull List<LineString> lineStrings, @Nullable BoundingBox bbox2) {
        List<List<Point>> coordinates2 = new ArrayList<>(lineStrings.size());
        for (LineString lineString : lineStrings) {
            coordinates2.add(lineString.coordinates());
        }
        return new MultiLineString(TYPE, bbox2, coordinates2);
    }

    public static MultiLineString fromLineString(@NonNull LineString lineString) {
        return new MultiLineString(TYPE, null, Arrays.asList(lineString.coordinates()));
    }

    public static MultiLineString fromLineString(@NonNull LineString lineString, @Nullable BoundingBox bbox2) {
        return new MultiLineString(TYPE, bbox2, Arrays.asList(lineString.coordinates()));
    }

    public static MultiLineString fromLngLats(@NonNull List<List<Point>> points) {
        return new MultiLineString(TYPE, null, points);
    }

    public static MultiLineString fromLngLats(@NonNull List<List<Point>> points, @Nullable BoundingBox bbox2) {
        return new MultiLineString(TYPE, bbox2, points);
    }

    static MultiLineString fromLngLats(double[][][] coordinates2) {
        List<List<Point>> multiLine = new ArrayList<>(coordinates2.length);
        for (int i = 0; i < coordinates2.length; i++) {
            List<Point> lineString = new ArrayList<>(coordinates2[i].length);
            for (int j = 0; j < coordinates2[i].length; j++) {
                lineString.add(Point.fromLngLat(coordinates2[i][j]));
            }
            multiLine.add(lineString);
        }
        return new MultiLineString(TYPE, null, multiLine);
    }

    MultiLineString(String type2, @Nullable BoundingBox bbox2, List<List<Point>> coordinates2) {
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
    public List<List<Point>> coordinates() {
        return this.coordinates;
    }

    public List<LineString> lineStrings() {
        List<List<Point>> coordinates2 = coordinates();
        List<LineString> lineStrings = new ArrayList<>(coordinates2.size());
        for (List<Point> points : coordinates2) {
            lineStrings.add(LineString.fromLngLats(points));
        }
        return lineStrings;
    }

    public String toJson() {
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapterFactory(GeoJsonAdapterFactory.create());
        return gson.create().toJson(this);
    }

    public static TypeAdapter<MultiLineString> typeAdapter(Gson gson) {
        return new GsonTypeAdapter(gson);
    }

    public String toString() {
        return "MultiLineString{type=" + this.type + ", bbox=" + this.bbox + ", coordinates=" + this.coordinates + "}";
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof MultiLineString)) {
            return false;
        }
        MultiLineString that = (MultiLineString) obj;
        if (!this.type.equals(that.type()) || (this.bbox != null ? !this.bbox.equals(that.bbox()) : that.bbox() != null) || !this.coordinates.equals(that.coordinates())) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return (((((1 * 1000003) ^ this.type.hashCode()) * 1000003) ^ (this.bbox == null ? 0 : this.bbox.hashCode())) * 1000003) ^ this.coordinates.hashCode();
    }

    static final class GsonTypeAdapter extends BaseGeometryTypeAdapter<MultiLineString, List<List<Point>>> {
        /* access modifiers changed from: package-private */
        public /* bridge */ /* synthetic */ CoordinateContainer createCoordinateContainer(String str, BoundingBox boundingBox, Object obj) {
            return createCoordinateContainer(str, boundingBox, (List<List<Point>>) ((List) obj));
        }

        GsonTypeAdapter(Gson gson) {
            super(gson, new ListOfListOfPointCoordinatesTypeAdapter());
        }

        public void write(JsonWriter jsonWriter, MultiLineString object) throws IOException {
            writeCoordinateContainer(jsonWriter, object);
        }

        public MultiLineString read(JsonReader jsonReader) throws IOException {
            return (MultiLineString) readCoordinateContainer(jsonReader);
        }

        /* access modifiers changed from: package-private */
        public CoordinateContainer<List<List<Point>>> createCoordinateContainer(String type, BoundingBox bbox, List<List<Point>> coords) {
            if (type == null) {
                type = MultiLineString.TYPE;
            }
            return new MultiLineString(type, bbox, coords);
        }
    }
}
