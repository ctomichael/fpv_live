package com.mapbox.geojson;

import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.Size;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.mapbox.geojson.exception.GeoJsonException;
import com.mapbox.geojson.gson.GeoJsonAdapterFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Keep
public final class Polygon implements CoordinateContainer<List<List<Point>>> {
    private static final String TYPE = "Polygon";
    private final BoundingBox bbox;
    private final List<List<Point>> coordinates;
    private final String type;

    public static Polygon fromJson(@NonNull String json) {
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapterFactory(GeoJsonAdapterFactory.create());
        return (Polygon) gson.create().fromJson(json, Polygon.class);
    }

    public static Polygon fromLngLats(@NonNull List<List<Point>> coordinates2) {
        return new Polygon(TYPE, null, coordinates2);
    }

    public static Polygon fromLngLats(@NonNull List<List<Point>> coordinates2, @Nullable BoundingBox bbox2) {
        return new Polygon(TYPE, bbox2, coordinates2);
    }

    static Polygon fromLngLats(@NonNull double[][][] coordinates2) {
        List<List<Point>> converted = new ArrayList<>(coordinates2.length);
        for (double[][] coordinate : coordinates2) {
            List<Point> innerList = new ArrayList<>(coordinate.length);
            for (double[] pointCoordinate : coordinate) {
                innerList.add(Point.fromLngLat(pointCoordinate));
            }
            converted.add(innerList);
        }
        return new Polygon(TYPE, null, converted);
    }

    public static Polygon fromOuterInner(@NonNull LineString outer, @Nullable LineString... inner) {
        isLinearRing(outer);
        List<List<Point>> coordinates2 = new ArrayList<>();
        coordinates2.add(outer.coordinates());
        if (inner == null) {
            return new Polygon(TYPE, null, coordinates2);
        }
        for (LineString lineString : inner) {
            isLinearRing(lineString);
            coordinates2.add(lineString.coordinates());
        }
        return new Polygon(TYPE, null, coordinates2);
    }

    public static Polygon fromOuterInner(@NonNull LineString outer, @Nullable BoundingBox bbox2, @Nullable LineString... inner) {
        isLinearRing(outer);
        List<List<Point>> coordinates2 = new ArrayList<>();
        coordinates2.add(outer.coordinates());
        if (inner == null) {
            return new Polygon(TYPE, bbox2, coordinates2);
        }
        for (LineString lineString : inner) {
            isLinearRing(lineString);
            coordinates2.add(lineString.coordinates());
        }
        return new Polygon(TYPE, bbox2, coordinates2);
    }

    public static Polygon fromOuterInner(@NonNull LineString outer, @Nullable @Size(min = 1) List<LineString> inner) {
        isLinearRing(outer);
        List<List<Point>> coordinates2 = new ArrayList<>();
        coordinates2.add(outer.coordinates());
        if (inner == null || inner.isEmpty()) {
            return new Polygon(TYPE, null, coordinates2);
        }
        for (LineString lineString : inner) {
            isLinearRing(lineString);
            coordinates2.add(lineString.coordinates());
        }
        return new Polygon(TYPE, null, coordinates2);
    }

    public static Polygon fromOuterInner(@NonNull LineString outer, @Nullable BoundingBox bbox2, @Nullable @Size(min = 1) List<LineString> inner) {
        isLinearRing(outer);
        List<List<Point>> coordinates2 = new ArrayList<>();
        coordinates2.add(outer.coordinates());
        if (inner == null) {
            return new Polygon(TYPE, bbox2, coordinates2);
        }
        for (LineString lineString : inner) {
            isLinearRing(lineString);
            coordinates2.add(lineString.coordinates());
        }
        return new Polygon(TYPE, bbox2, coordinates2);
    }

    Polygon(String type2, @Nullable BoundingBox bbox2, List<List<Point>> coordinates2) {
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

    @Nullable
    public LineString outer() {
        return LineString.fromLngLats(coordinates().get(0));
    }

    @Nullable
    public List<LineString> inner() {
        List<List<Point>> coordinates2 = coordinates();
        if (coordinates2.size() <= 1) {
            return new ArrayList(0);
        }
        List<LineString> inner = new ArrayList<>(coordinates2.size() - 1);
        for (List<Point> points : coordinates2.subList(1, coordinates2.size())) {
            inner.add(LineString.fromLngLats(points));
        }
        return inner;
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

    public String toJson() {
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapterFactory(GeoJsonAdapterFactory.create());
        return gson.create().toJson(this);
    }

    public static TypeAdapter<Polygon> typeAdapter(Gson gson) {
        return new GsonTypeAdapter(gson);
    }

    private static boolean isLinearRing(LineString lineString) {
        if (lineString.coordinates().size() < 4) {
            throw new GeoJsonException("LinearRings need to be made up of 4 or more coordinates.");
        } else if (lineString.coordinates().get(0).equals(lineString.coordinates().get(lineString.coordinates().size() - 1))) {
            return true;
        } else {
            throw new GeoJsonException("LinearRings require first and last coordinate to be identical.");
        }
    }

    public String toString() {
        return "Polygon{type=" + this.type + ", bbox=" + this.bbox + ", coordinates=" + this.coordinates + "}";
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Polygon)) {
            return false;
        }
        Polygon that = (Polygon) obj;
        if (!this.type.equals(that.type()) || (this.bbox != null ? !this.bbox.equals(that.bbox()) : that.bbox() != null) || !this.coordinates.equals(that.coordinates())) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return (((((1 * 1000003) ^ this.type.hashCode()) * 1000003) ^ (this.bbox == null ? 0 : this.bbox.hashCode())) * 1000003) ^ this.coordinates.hashCode();
    }

    static final class GsonTypeAdapter extends BaseGeometryTypeAdapter<Polygon, List<List<Point>>> {
        /* access modifiers changed from: package-private */
        public /* bridge */ /* synthetic */ CoordinateContainer createCoordinateContainer(String str, BoundingBox boundingBox, Object obj) {
            return createCoordinateContainer(str, boundingBox, (List<List<Point>>) ((List) obj));
        }

        GsonTypeAdapter(Gson gson) {
            super(gson, new ListOfListOfPointCoordinatesTypeAdapter());
        }

        public void write(JsonWriter jsonWriter, Polygon object) throws IOException {
            writeCoordinateContainer(jsonWriter, object);
        }

        public Polygon read(JsonReader jsonReader) throws IOException {
            return (Polygon) readCoordinateContainer(jsonReader);
        }

        /* access modifiers changed from: package-private */
        public CoordinateContainer<List<List<Point>>> createCoordinateContainer(String type, BoundingBox bbox, List<List<Point>> coords) {
            if (type == null) {
                type = Polygon.TYPE;
            }
            return new Polygon(type, bbox, coords);
        }
    }
}
