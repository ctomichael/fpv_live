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
public final class MultiPolygon implements CoordinateContainer<List<List<List<Point>>>> {
    private static final String TYPE = "MultiPolygon";
    private final BoundingBox bbox;
    private final List<List<List<Point>>> coordinates;
    private final String type;

    public static MultiPolygon fromJson(String json) {
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapterFactory(GeoJsonAdapterFactory.create());
        return (MultiPolygon) gson.create().fromJson(json, MultiPolygon.class);
    }

    public static MultiPolygon fromPolygons(@NonNull List<Polygon> polygons) {
        List<List<List<Point>>> coordinates2 = new ArrayList<>(polygons.size());
        for (Polygon polygon : polygons) {
            coordinates2.add(polygon.coordinates());
        }
        return new MultiPolygon(TYPE, null, coordinates2);
    }

    public static MultiPolygon fromPolygons(@NonNull List<Polygon> polygons, @Nullable BoundingBox bbox2) {
        List<List<List<Point>>> coordinates2 = new ArrayList<>(polygons.size());
        for (Polygon polygon : polygons) {
            coordinates2.add(polygon.coordinates());
        }
        return new MultiPolygon(TYPE, bbox2, coordinates2);
    }

    public static MultiPolygon fromPolygon(@NonNull Polygon polygon) {
        return new MultiPolygon(TYPE, null, Arrays.asList(polygon.coordinates()));
    }

    public static MultiPolygon fromPolygon(@NonNull Polygon polygon, @Nullable BoundingBox bbox2) {
        return new MultiPolygon(TYPE, bbox2, Arrays.asList(polygon.coordinates()));
    }

    public static MultiPolygon fromLngLats(@NonNull List<List<List<Point>>> points) {
        return new MultiPolygon(TYPE, null, points);
    }

    public static MultiPolygon fromLngLats(@NonNull List<List<List<Point>>> points, @Nullable BoundingBox bbox2) {
        return new MultiPolygon(TYPE, bbox2, points);
    }

    static MultiPolygon fromLngLats(@NonNull double[][][][] coordinates2) {
        List<List<List<Point>>> converted = new ArrayList<>(coordinates2.length);
        for (int i = 0; i < coordinates2.length; i++) {
            List<List<Point>> innerOneList = new ArrayList<>(coordinates2[i].length);
            for (int j = 0; j < coordinates2[i].length; j++) {
                List<Point> innerTwoList = new ArrayList<>(coordinates2[i][j].length);
                for (int k = 0; k < coordinates2[i][j].length; k++) {
                    innerTwoList.add(Point.fromLngLat(coordinates2[i][j][k]));
                }
                innerOneList.add(innerTwoList);
            }
            converted.add(innerOneList);
        }
        return new MultiPolygon(TYPE, null, converted);
    }

    MultiPolygon(String type2, @Nullable BoundingBox bbox2, List<List<List<Point>>> coordinates2) {
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

    public List<Polygon> polygons() {
        List<List<List<Point>>> coordinates2 = coordinates();
        List<Polygon> polygons = new ArrayList<>(coordinates2.size());
        for (List<List<Point>> points : coordinates2) {
            polygons.add(Polygon.fromLngLats(points));
        }
        return polygons;
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
    public List<List<List<Point>>> coordinates() {
        return this.coordinates;
    }

    public String toJson() {
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapterFactory(GeoJsonAdapterFactory.create());
        return gson.create().toJson(this);
    }

    public static TypeAdapter<MultiPolygon> typeAdapter(Gson gson) {
        return new GsonTypeAdapter(gson);
    }

    public String toString() {
        return "Polygon{type=" + this.type + ", bbox=" + this.bbox + ", coordinates=" + this.coordinates + "}";
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof MultiPolygon)) {
            return false;
        }
        MultiPolygon that = (MultiPolygon) obj;
        if (!this.type.equals(that.type()) || (this.bbox != null ? !this.bbox.equals(that.bbox()) : that.bbox() != null) || !this.coordinates.equals(that.coordinates())) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return (((((1 * 1000003) ^ this.type.hashCode()) * 1000003) ^ (this.bbox == null ? 0 : this.bbox.hashCode())) * 1000003) ^ this.coordinates.hashCode();
    }

    static final class GsonTypeAdapter extends BaseGeometryTypeAdapter<MultiPolygon, List<List<List<Point>>>> {
        /* access modifiers changed from: package-private */
        public /* bridge */ /* synthetic */ CoordinateContainer createCoordinateContainer(String str, BoundingBox boundingBox, Object obj) {
            return createCoordinateContainer(str, boundingBox, (List<List<List<Point>>>) ((List) obj));
        }

        GsonTypeAdapter(Gson gson) {
            super(gson, new ListofListofListOfPointCoordinatesTypeAdapter());
        }

        public void write(JsonWriter jsonWriter, MultiPolygon object) throws IOException {
            writeCoordinateContainer(jsonWriter, object);
        }

        public MultiPolygon read(JsonReader jsonReader) throws IOException {
            return (MultiPolygon) readCoordinateContainer(jsonReader);
        }

        /* access modifiers changed from: package-private */
        public CoordinateContainer<List<List<List<Point>>>> createCoordinateContainer(String type, BoundingBox bbox, List<List<List<Point>>> coords) {
            if (type == null) {
                type = MultiPolygon.TYPE;
            }
            return new MultiPolygon(type, bbox, coords);
        }
    }
}
