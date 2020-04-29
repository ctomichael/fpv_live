package com.mapbox.geojson;

import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.mapbox.geojson.gson.GeoJsonAdapterFactory;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Keep
public final class GeometryCollection implements Geometry {
    private static final String TYPE = "GeometryCollection";
    private final BoundingBox bbox;
    private final List<Geometry> geometries;
    private final String type;

    public static GeometryCollection fromJson(String json) {
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapterFactory(GeoJsonAdapterFactory.create());
        gson.registerTypeAdapterFactory(GeometryAdapterFactory.create());
        return (GeometryCollection) gson.create().fromJson(json, GeometryCollection.class);
    }

    public static GeometryCollection fromGeometries(@NonNull List<Geometry> geometries2) {
        return new GeometryCollection(TYPE, null, geometries2);
    }

    public static GeometryCollection fromGeometries(@NonNull List<Geometry> geometries2, @Nullable BoundingBox bbox2) {
        return new GeometryCollection(TYPE, bbox2, geometries2);
    }

    public static GeometryCollection fromGeometry(@NonNull Geometry geometry) {
        return new GeometryCollection(TYPE, null, Arrays.asList(geometry));
    }

    public static GeometryCollection fromGeometry(@NonNull Geometry geometry, @Nullable BoundingBox bbox2) {
        return new GeometryCollection(TYPE, bbox2, Arrays.asList(geometry));
    }

    GeometryCollection(String type2, @Nullable BoundingBox bbox2, List<Geometry> geometries2) {
        if (type2 == null) {
            throw new NullPointerException("Null type");
        }
        this.type = type2;
        this.bbox = bbox2;
        if (geometries2 == null) {
            throw new NullPointerException("Null geometries");
        }
        this.geometries = geometries2;
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
    public List<Geometry> geometries() {
        return this.geometries;
    }

    public String toJson() {
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapterFactory(GeoJsonAdapterFactory.create());
        gson.registerTypeAdapterFactory(GeometryAdapterFactory.create());
        return gson.create().toJson(this);
    }

    public static TypeAdapter<GeometryCollection> typeAdapter(Gson gson) {
        return new GsonTypeAdapter(gson);
    }

    public String toString() {
        return "GeometryCollection{type=" + this.type + ", bbox=" + this.bbox + ", geometries=" + this.geometries + "}";
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GeometryCollection)) {
            return false;
        }
        GeometryCollection that = (GeometryCollection) obj;
        if (!this.type.equals(that.type()) || (this.bbox != null ? !this.bbox.equals(that.bbox()) : that.bbox() != null) || !this.geometries.equals(that.geometries())) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return (((((1 * 1000003) ^ this.type.hashCode()) * 1000003) ^ (this.bbox == null ? 0 : this.bbox.hashCode())) * 1000003) ^ this.geometries.hashCode();
    }

    static final class GsonTypeAdapter extends TypeAdapter<GeometryCollection> {
        private volatile TypeAdapter<BoundingBox> boundingBoxTypeAdapter;
        private final Gson gson;
        private volatile TypeAdapter<List<Geometry>> listGeometryAdapter;
        private volatile TypeAdapter<String> stringTypeAdapter;

        GsonTypeAdapter(Gson gson2) {
            this.gson = gson2;
        }

        public void write(JsonWriter jsonWriter, GeometryCollection object) throws IOException {
            if (object == null) {
                jsonWriter.nullValue();
                return;
            }
            jsonWriter.beginObject();
            jsonWriter.name("type");
            if (object.type() == null) {
                jsonWriter.nullValue();
            } else {
                TypeAdapter<String> stringTypeAdapter2 = this.stringTypeAdapter;
                if (stringTypeAdapter2 == null) {
                    stringTypeAdapter2 = this.gson.getAdapter(String.class);
                    this.stringTypeAdapter = stringTypeAdapter2;
                }
                stringTypeAdapter2.write(jsonWriter, object.type());
            }
            jsonWriter.name("bbox");
            if (object.bbox() == null) {
                jsonWriter.nullValue();
            } else {
                TypeAdapter<BoundingBox> boundingBoxTypeAdapter2 = this.boundingBoxTypeAdapter;
                if (boundingBoxTypeAdapter2 == null) {
                    boundingBoxTypeAdapter2 = this.gson.getAdapter(BoundingBox.class);
                    this.boundingBoxTypeAdapter = boundingBoxTypeAdapter2;
                }
                boundingBoxTypeAdapter2.write(jsonWriter, object.bbox());
            }
            jsonWriter.name("geometries");
            if (object.geometries() == null) {
                jsonWriter.nullValue();
            } else {
                TypeAdapter<List<Geometry>> listGeometryAdapter2 = this.listGeometryAdapter;
                if (listGeometryAdapter2 == null) {
                    listGeometryAdapter2 = this.gson.getAdapter(TypeToken.getParameterized(List.class, Geometry.class));
                    this.listGeometryAdapter = listGeometryAdapter2;
                }
                listGeometryAdapter2.write(jsonWriter, object.geometries());
            }
            jsonWriter.endObject();
        }

        public GeometryCollection read(JsonReader jsonReader) throws IOException {
            if (jsonReader.peek() == JsonToken.NULL) {
                jsonReader.nextNull();
                return null;
            }
            jsonReader.beginObject();
            String type = null;
            BoundingBox bbox = null;
            List<Geometry> geometries = null;
            while (jsonReader.hasNext()) {
                String name = jsonReader.nextName();
                if (jsonReader.peek() == JsonToken.NULL) {
                    jsonReader.nextNull();
                } else {
                    char c = 65535;
                    switch (name.hashCode()) {
                        case 3017257:
                            if (name.equals("bbox")) {
                                c = 1;
                                break;
                            }
                            break;
                        case 3575610:
                            if (name.equals("type")) {
                                c = 0;
                                break;
                            }
                            break;
                        case 203916432:
                            if (name.equals("geometries")) {
                                c = 2;
                                break;
                            }
                            break;
                    }
                    switch (c) {
                        case 0:
                            TypeAdapter<String> stringTypeAdapter2 = this.stringTypeAdapter;
                            if (stringTypeAdapter2 == null) {
                                stringTypeAdapter2 = this.gson.getAdapter(String.class);
                                this.stringTypeAdapter = stringTypeAdapter2;
                            }
                            type = stringTypeAdapter2.read(jsonReader);
                            continue;
                        case 1:
                            TypeAdapter<BoundingBox> boundingBoxTypeAdapter2 = this.boundingBoxTypeAdapter;
                            if (boundingBoxTypeAdapter2 == null) {
                                boundingBoxTypeAdapter2 = this.gson.getAdapter(BoundingBox.class);
                                this.boundingBoxTypeAdapter = boundingBoxTypeAdapter2;
                            }
                            bbox = boundingBoxTypeAdapter2.read(jsonReader);
                            continue;
                        case 2:
                            TypeAdapter<List<Geometry>> listGeometryAdapter2 = this.listGeometryAdapter;
                            if (listGeometryAdapter2 == null) {
                                listGeometryAdapter2 = this.gson.getAdapter(TypeToken.getParameterized(List.class, Geometry.class));
                                this.listGeometryAdapter = listGeometryAdapter2;
                            }
                            geometries = listGeometryAdapter2.read(jsonReader);
                            continue;
                        default:
                            jsonReader.skipValue();
                            continue;
                    }
                }
            }
            jsonReader.endObject();
            if (type == null) {
                type = GeometryCollection.TYPE;
            }
            return new GeometryCollection(type, bbox, geometries);
        }
    }
}
