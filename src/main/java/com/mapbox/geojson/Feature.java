package com.mapbox.geojson;

import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.mapbox.geojson.gson.BoundingBoxTypeAdapter;
import com.mapbox.geojson.gson.GeoJsonAdapterFactory;
import dji.publics.protocol.ResponseBase;
import java.io.IOException;

@Keep
public final class Feature implements GeoJson {
    private static final String TYPE = "Feature";
    @JsonAdapter(BoundingBoxTypeAdapter.class)
    private final BoundingBox bbox;
    private final Geometry geometry;
    private final String id;
    private final JsonObject properties;
    private final String type;

    public static Feature fromJson(@NonNull String json) {
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapterFactory(GeoJsonAdapterFactory.create());
        gson.registerTypeAdapterFactory(GeometryAdapterFactory.create());
        Feature feature = (Feature) gson.create().fromJson(json, Feature.class);
        if (feature.properties() != null) {
            return feature;
        }
        return new Feature(TYPE, feature.bbox(), feature.id(), feature.geometry(), new JsonObject());
    }

    public static Feature fromGeometry(@Nullable Geometry geometry2) {
        return new Feature(TYPE, null, null, geometry2, new JsonObject());
    }

    public static Feature fromGeometry(@Nullable Geometry geometry2, @Nullable BoundingBox bbox2) {
        return new Feature(TYPE, bbox2, null, geometry2, new JsonObject());
    }

    public static Feature fromGeometry(@Nullable Geometry geometry2, @Nullable JsonObject properties2) {
        JsonObject jsonObject;
        if (properties2 == null) {
            jsonObject = new JsonObject();
        } else {
            jsonObject = properties2;
        }
        return new Feature(TYPE, null, null, geometry2, jsonObject);
    }

    public static Feature fromGeometry(@Nullable Geometry geometry2, @Nullable JsonObject properties2, @Nullable BoundingBox bbox2) {
        JsonObject jsonObject;
        if (properties2 == null) {
            jsonObject = new JsonObject();
        } else {
            jsonObject = properties2;
        }
        return new Feature(TYPE, bbox2, null, geometry2, jsonObject);
    }

    public static Feature fromGeometry(@Nullable Geometry geometry2, @Nullable JsonObject properties2, @Nullable String id2) {
        JsonObject jsonObject;
        if (properties2 == null) {
            jsonObject = new JsonObject();
        } else {
            jsonObject = properties2;
        }
        return new Feature(TYPE, null, id2, geometry2, jsonObject);
    }

    public static Feature fromGeometry(@Nullable Geometry geometry2, @NonNull JsonObject properties2, @Nullable String id2, @Nullable BoundingBox bbox2) {
        JsonObject jsonObject;
        if (properties2 == null) {
            jsonObject = new JsonObject();
        } else {
            jsonObject = properties2;
        }
        return new Feature(TYPE, bbox2, id2, geometry2, jsonObject);
    }

    Feature(String type2, @Nullable BoundingBox bbox2, @Nullable String id2, @Nullable Geometry geometry2, @Nullable JsonObject properties2) {
        if (type2 == null) {
            throw new NullPointerException("Null type");
        }
        this.type = type2;
        this.bbox = bbox2;
        this.id = id2;
        this.geometry = geometry2;
        this.properties = properties2;
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
    public String id() {
        return this.id;
    }

    @Nullable
    public Geometry geometry() {
        return this.geometry;
    }

    @Nullable
    public JsonObject properties() {
        return this.properties;
    }

    public String toJson() {
        Gson gson = new GsonBuilder().registerTypeAdapterFactory(GeoJsonAdapterFactory.create()).registerTypeAdapterFactory(GeometryAdapterFactory.create()).create();
        Feature feature = this;
        if (properties().size() == 0) {
            feature = new Feature(TYPE, bbox(), id(), geometry(), null);
        }
        return gson.toJson(feature);
    }

    public static TypeAdapter<Feature> typeAdapter(Gson gson) {
        return new GsonTypeAdapter(gson);
    }

    public void addStringProperty(String key, String value) {
        properties().addProperty(key, value);
    }

    public void addNumberProperty(String key, Number value) {
        properties().addProperty(key, value);
    }

    public void addBooleanProperty(String key, Boolean value) {
        properties().addProperty(key, value);
    }

    public void addCharacterProperty(String key, Character value) {
        properties().addProperty(key, value);
    }

    public void addProperty(String key, JsonElement value) {
        properties().add(key, value);
    }

    public String getStringProperty(String key) {
        JsonElement propertyKey = properties().get(key);
        if (propertyKey == null) {
            return null;
        }
        return propertyKey.getAsString();
    }

    public Number getNumberProperty(String key) {
        JsonElement propertyKey = properties().get(key);
        if (propertyKey == null) {
            return null;
        }
        return propertyKey.getAsNumber();
    }

    public Boolean getBooleanProperty(String key) {
        JsonElement propertyKey = properties().get(key);
        if (propertyKey == null) {
            return null;
        }
        return Boolean.valueOf(propertyKey.getAsBoolean());
    }

    public Character getCharacterProperty(String key) {
        JsonElement propertyKey = properties().get(key);
        if (propertyKey == null) {
            return null;
        }
        return Character.valueOf(propertyKey.getAsCharacter());
    }

    public JsonElement getProperty(String key) {
        return properties().get(key);
    }

    public JsonElement removeProperty(String key) {
        return properties().remove(key);
    }

    public boolean hasProperty(String key) {
        return properties().has(key);
    }

    public boolean hasNonNullValueForProperty(String key) {
        return hasProperty(key) && !getProperty(key).isJsonNull();
    }

    public String toString() {
        return "Feature{type=" + this.type + ", bbox=" + this.bbox + ", id=" + this.id + ", geometry=" + this.geometry + ", properties=" + this.properties + "}";
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Feature)) {
            return false;
        }
        Feature that = (Feature) obj;
        if (this.type.equals(that.type()) && (this.bbox != null ? this.bbox.equals(that.bbox()) : that.bbox() == null) && (this.id != null ? this.id.equals(that.id()) : that.id() == null) && (this.geometry != null ? this.geometry.equals(that.geometry()) : that.geometry() == null)) {
            if (this.properties == null) {
                if (that.properties() == null) {
                    return true;
                }
            } else if (this.properties.equals(that.properties())) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        int i = 0;
        int hashCode = ((((((((1 * 1000003) ^ this.type.hashCode()) * 1000003) ^ (this.bbox == null ? 0 : this.bbox.hashCode())) * 1000003) ^ (this.id == null ? 0 : this.id.hashCode())) * 1000003) ^ (this.geometry == null ? 0 : this.geometry.hashCode())) * 1000003;
        if (this.properties != null) {
            i = this.properties.hashCode();
        }
        return hashCode ^ i;
    }

    static final class GsonTypeAdapter extends TypeAdapter<Feature> {
        private volatile TypeAdapter<BoundingBox> boundingBoxTypeAdapter;
        private volatile TypeAdapter<Geometry> geometryTypeAdapter;
        private final Gson gson;
        private volatile TypeAdapter<JsonObject> jsonObjectTypeAdapter;
        private volatile TypeAdapter<String> stringTypeAdapter;

        GsonTypeAdapter(Gson gson2) {
            this.gson = gson2;
        }

        public void write(JsonWriter jsonWriter, Feature object) throws IOException {
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
            jsonWriter.name(ResponseBase.STRING_ID);
            if (object.id() == null) {
                jsonWriter.nullValue();
            } else {
                TypeAdapter<String> stringTypeAdapter3 = this.stringTypeAdapter;
                if (stringTypeAdapter3 == null) {
                    stringTypeAdapter3 = this.gson.getAdapter(String.class);
                    this.stringTypeAdapter = stringTypeAdapter3;
                }
                stringTypeAdapter3.write(jsonWriter, object.id());
            }
            jsonWriter.name("geometry");
            if (object.geometry() == null) {
                jsonWriter.nullValue();
            } else {
                TypeAdapter<Geometry> geometryTypeAdapter2 = this.geometryTypeAdapter;
                if (geometryTypeAdapter2 == null) {
                    geometryTypeAdapter2 = this.gson.getAdapter(Geometry.class);
                    this.geometryTypeAdapter = geometryTypeAdapter2;
                }
                geometryTypeAdapter2.write(jsonWriter, object.geometry());
            }
            jsonWriter.name("properties");
            if (object.properties() == null) {
                jsonWriter.nullValue();
            } else {
                TypeAdapter<JsonObject> jsonObjectTypeAdapter2 = this.jsonObjectTypeAdapter;
                if (jsonObjectTypeAdapter2 == null) {
                    jsonObjectTypeAdapter2 = this.gson.getAdapter(JsonObject.class);
                    this.jsonObjectTypeAdapter = jsonObjectTypeAdapter2;
                }
                jsonObjectTypeAdapter2.write(jsonWriter, object.properties());
            }
            jsonWriter.endObject();
        }

        public Feature read(JsonReader jsonReader) throws IOException {
            if (jsonReader.peek() == JsonToken.NULL) {
                jsonReader.nextNull();
                return null;
            }
            jsonReader.beginObject();
            String type = null;
            BoundingBox bbox = null;
            String id = null;
            Geometry geometry = null;
            JsonObject properties = null;
            while (jsonReader.hasNext()) {
                String name = jsonReader.nextName();
                if (jsonReader.peek() == JsonToken.NULL) {
                    jsonReader.nextNull();
                } else {
                    char c = 65535;
                    switch (name.hashCode()) {
                        case -926053069:
                            if (name.equals("properties")) {
                                c = 4;
                                break;
                            }
                            break;
                        case 3355:
                            if (name.equals(ResponseBase.STRING_ID)) {
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
                        case 1846020210:
                            if (name.equals("geometry")) {
                                c = 3;
                                break;
                            }
                            break;
                    }
                    switch (c) {
                        case 0:
                            TypeAdapter<String> strTypeAdapter = this.stringTypeAdapter;
                            if (strTypeAdapter == null) {
                                strTypeAdapter = this.gson.getAdapter(String.class);
                                this.stringTypeAdapter = strTypeAdapter;
                            }
                            type = strTypeAdapter.read(jsonReader);
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
                            TypeAdapter<String> strTypeAdapter2 = this.stringTypeAdapter;
                            if (strTypeAdapter2 == null) {
                                strTypeAdapter2 = this.gson.getAdapter(String.class);
                                this.stringTypeAdapter = strTypeAdapter2;
                            }
                            id = strTypeAdapter2.read(jsonReader);
                            continue;
                        case 3:
                            TypeAdapter<Geometry> geometryTypeAdapter2 = this.geometryTypeAdapter;
                            if (geometryTypeAdapter2 == null) {
                                geometryTypeAdapter2 = this.gson.getAdapter(Geometry.class);
                                this.geometryTypeAdapter = geometryTypeAdapter2;
                            }
                            geometry = geometryTypeAdapter2.read(jsonReader);
                            continue;
                        case 4:
                            TypeAdapter<JsonObject> jsonObjectTypeAdapter2 = this.jsonObjectTypeAdapter;
                            if (jsonObjectTypeAdapter2 == null) {
                                jsonObjectTypeAdapter2 = this.gson.getAdapter(JsonObject.class);
                                this.jsonObjectTypeAdapter = jsonObjectTypeAdapter2;
                            }
                            properties = jsonObjectTypeAdapter2.read(jsonReader);
                            continue;
                        default:
                            jsonReader.skipValue();
                            continue;
                    }
                }
            }
            jsonReader.endObject();
            return new Feature(type, bbox, id, geometry, properties);
        }
    }
}
