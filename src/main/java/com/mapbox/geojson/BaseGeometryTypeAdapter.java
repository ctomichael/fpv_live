package com.mapbox.geojson;

import android.support.annotation.Keep;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.mapbox.geojson.exception.GeoJsonException;
import com.mapbox.geojson.gson.BoundingBoxTypeAdapter;
import java.io.IOException;

@Keep
abstract class BaseGeometryTypeAdapter<G, T> extends TypeAdapter<G> {
    private volatile TypeAdapter<BoundingBox> boundingBoxAdapter = new BoundingBoxTypeAdapter();
    private volatile TypeAdapter<T> coordinatesAdapter;
    private final Gson gson;
    private volatile TypeAdapter<String> stringAdapter;

    /* access modifiers changed from: package-private */
    public abstract CoordinateContainer<T> createCoordinateContainer(String str, BoundingBox boundingBox, Object obj);

    BaseGeometryTypeAdapter(Gson gson2, TypeAdapter<T> coordinatesAdapter2) {
        this.gson = gson2;
        this.coordinatesAdapter = coordinatesAdapter2;
    }

    public void writeCoordinateContainer(JsonWriter jsonWriter, CoordinateContainer<T> object) throws IOException {
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
            TypeAdapter<BoundingBox> boundingBoxAdapter2 = this.boundingBoxAdapter;
            if (boundingBoxAdapter2 == null) {
                boundingBoxAdapter2 = this.gson.getAdapter(BoundingBox.class);
                this.boundingBoxAdapter = boundingBoxAdapter2;
            }
            boundingBoxAdapter2.write(jsonWriter, object.bbox());
        }
        jsonWriter.name("coordinates");
        if (object.coordinates() == null) {
            jsonWriter.nullValue();
        } else {
            TypeAdapter<T> coordinatesAdapter2 = this.coordinatesAdapter;
            if (coordinatesAdapter2 == null) {
                throw new GeoJsonException("Coordinates type adapter is null");
            }
            coordinatesAdapter2.write(jsonWriter, object.coordinates());
        }
        jsonWriter.endObject();
    }

    public CoordinateContainer<T> readCoordinateContainer(JsonReader jsonReader) throws IOException {
        if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            return null;
        }
        jsonReader.beginObject();
        String type = null;
        BoundingBox bbox = null;
        T t = null;
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
                    case 1871919611:
                        if (name.equals("coordinates")) {
                            c = 2;
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
                        TypeAdapter<T> coordinatesAdapter2 = this.coordinatesAdapter;
                        if (coordinatesAdapter2 == null) {
                            throw new GeoJsonException("Coordinates type adapter is null");
                        }
                        t = coordinatesAdapter2.read(jsonReader);
                        continue;
                    default:
                        jsonReader.skipValue();
                        continue;
                }
            }
        }
        jsonReader.endObject();
        return createCoordinateContainer(type, bbox, t);
    }
}
