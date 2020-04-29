package com.mapbox.geojson;

import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.mapbox.geojson.gson.BoundingBoxTypeAdapter;
import com.mapbox.geojson.gson.GeoJsonAdapterFactory;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Keep
public final class FeatureCollection implements GeoJson {
    private static final String TYPE = "FeatureCollection";
    @JsonAdapter(BoundingBoxTypeAdapter.class)
    private final BoundingBox bbox;
    private final List<Feature> features;
    private final String type;

    public static FeatureCollection fromJson(@NonNull String json) {
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapterFactory(GeoJsonAdapterFactory.create());
        gson.registerTypeAdapterFactory(GeometryAdapterFactory.create());
        return (FeatureCollection) gson.create().fromJson(json, FeatureCollection.class);
    }

    public static FeatureCollection fromFeatures(@NonNull Feature[] features2) {
        return new FeatureCollection(TYPE, null, Arrays.asList(features2));
    }

    public static FeatureCollection fromFeatures(@NonNull List<Feature> features2) {
        return new FeatureCollection(TYPE, null, features2);
    }

    public static FeatureCollection fromFeatures(@NonNull Feature[] features2, @Nullable BoundingBox bbox2) {
        return new FeatureCollection(TYPE, bbox2, Arrays.asList(features2));
    }

    public static FeatureCollection fromFeatures(@NonNull List<Feature> features2, @Nullable BoundingBox bbox2) {
        return new FeatureCollection(TYPE, bbox2, features2);
    }

    public static FeatureCollection fromFeature(@NonNull Feature feature) {
        return new FeatureCollection(TYPE, null, Arrays.asList(feature));
    }

    public static FeatureCollection fromFeature(@NonNull Feature feature, @Nullable BoundingBox bbox2) {
        return new FeatureCollection(TYPE, bbox2, Arrays.asList(feature));
    }

    FeatureCollection(String type2, @Nullable BoundingBox bbox2, @Nullable List<Feature> features2) {
        if (type2 == null) {
            throw new NullPointerException("Null type");
        }
        this.type = type2;
        this.bbox = bbox2;
        this.features = features2;
    }

    @NonNull
    public String type() {
        return this.type;
    }

    @Nullable
    public BoundingBox bbox() {
        return this.bbox;
    }

    @Nullable
    public List<Feature> features() {
        return this.features;
    }

    public String toJson() {
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapterFactory(GeoJsonAdapterFactory.create());
        gson.registerTypeAdapterFactory(GeometryAdapterFactory.create());
        return gson.create().toJson(this);
    }

    public static TypeAdapter<FeatureCollection> typeAdapter(Gson gson) {
        return new GsonTypeAdapter(gson);
    }

    public String toString() {
        return "FeatureCollection{type=" + this.type + ", bbox=" + this.bbox + ", features=" + this.features + "}";
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof FeatureCollection)) {
            return false;
        }
        FeatureCollection that = (FeatureCollection) obj;
        if (this.type.equals(that.type()) && (this.bbox != null ? this.bbox.equals(that.bbox()) : that.bbox() == null)) {
            if (this.features == null) {
                if (that.features() == null) {
                    return true;
                }
            } else if (this.features.equals(that.features())) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        int i = 0;
        int hashCode = ((((1 * 1000003) ^ this.type.hashCode()) * 1000003) ^ (this.bbox == null ? 0 : this.bbox.hashCode())) * 1000003;
        if (this.features != null) {
            i = this.features.hashCode();
        }
        return hashCode ^ i;
    }

    static final class GsonTypeAdapter extends TypeAdapter<FeatureCollection> {
        private volatile TypeAdapter<BoundingBox> boundingBoxAdapter;
        private final Gson gson;
        private volatile TypeAdapter<List<Feature>> listFeatureAdapter;
        private volatile TypeAdapter<String> stringAdapter;

        GsonTypeAdapter(Gson gson2) {
            this.gson = gson2;
        }

        public void write(JsonWriter jsonWriter, FeatureCollection object) throws IOException {
            if (object == null) {
                jsonWriter.nullValue();
                return;
            }
            jsonWriter.beginObject();
            jsonWriter.name("type");
            if (object.type() == null) {
                jsonWriter.nullValue();
            } else {
                TypeAdapter<String> stringAdapter2 = this.stringAdapter;
                if (stringAdapter2 == null) {
                    stringAdapter2 = this.gson.getAdapter(String.class);
                    this.stringAdapter = stringAdapter2;
                }
                stringAdapter2.write(jsonWriter, object.type());
            }
            jsonWriter.name("bbox");
            if (object.bbox() == null) {
                jsonWriter.nullValue();
            } else {
                TypeAdapter<BoundingBox> boundingBoxTypeAdapter = this.boundingBoxAdapter;
                if (boundingBoxTypeAdapter == null) {
                    boundingBoxTypeAdapter = this.gson.getAdapter(BoundingBox.class);
                    this.boundingBoxAdapter = boundingBoxTypeAdapter;
                }
                boundingBoxTypeAdapter.write(jsonWriter, object.bbox());
            }
            jsonWriter.name("features");
            if (object.features() == null) {
                jsonWriter.nullValue();
            } else {
                TypeAdapter<List<Feature>> listFeatureAdapter2 = this.listFeatureAdapter;
                if (listFeatureAdapter2 == null) {
                    listFeatureAdapter2 = this.gson.getAdapter(TypeToken.getParameterized(List.class, Feature.class));
                    this.listFeatureAdapter = listFeatureAdapter2;
                }
                listFeatureAdapter2.write(jsonWriter, object.features());
            }
            jsonWriter.endObject();
        }

        public FeatureCollection read(JsonReader jsonReader) throws IOException {
            if (jsonReader.peek() == JsonToken.NULL) {
                jsonReader.nextNull();
                return null;
            }
            jsonReader.beginObject();
            String type = null;
            BoundingBox bbox = null;
            List<Feature> features = null;
            while (jsonReader.hasNext()) {
                String name = jsonReader.nextName();
                if (jsonReader.peek() == JsonToken.NULL) {
                    jsonReader.nextNull();
                } else {
                    char c = 65535;
                    switch (name.hashCode()) {
                        case -290659267:
                            if (name.equals("features")) {
                                c = 2;
                                break;
                            }
                            break;
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
                    }
                    switch (c) {
                        case 0:
                            TypeAdapter<String> stringAdapter2 = this.stringAdapter;
                            if (stringAdapter2 == null) {
                                stringAdapter2 = this.gson.getAdapter(String.class);
                                this.stringAdapter = stringAdapter2;
                            }
                            type = stringAdapter2.read(jsonReader);
                            continue;
                        case 1:
                            TypeAdapter<BoundingBox> boundingBoxAdapter2 = this.boundingBoxAdapter;
                            if (boundingBoxAdapter2 == null) {
                                boundingBoxAdapter2 = this.gson.getAdapter(BoundingBox.class);
                                this.boundingBoxAdapter = boundingBoxAdapter2;
                            }
                            bbox = boundingBoxAdapter2.read(jsonReader);
                            continue;
                        case 2:
                            TypeAdapter<List<Feature>> listFeatureAdapter2 = this.listFeatureAdapter;
                            if (listFeatureAdapter2 == null) {
                                listFeatureAdapter2 = this.gson.getAdapter(TypeToken.getParameterized(List.class, Feature.class));
                                this.listFeatureAdapter = listFeatureAdapter2;
                            }
                            features = listFeatureAdapter2.read(jsonReader);
                            continue;
                        default:
                            jsonReader.skipValue();
                            continue;
                    }
                }
            }
            jsonReader.endObject();
            return new FeatureCollection(type, bbox, features);
        }
    }
}
