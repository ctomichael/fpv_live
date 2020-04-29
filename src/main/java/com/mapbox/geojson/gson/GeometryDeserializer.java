package com.mapbox.geojson.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mapbox.geojson.Geometry;
import java.lang.reflect.Type;

@Deprecated
public class GeometryDeserializer implements JsonDeserializer<Geometry> {
    public Geometry deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        String geometryType;
        if (json.isJsonObject()) {
            geometryType = json.getAsJsonObject().get("type").getAsString();
        } else {
            geometryType = json.getAsJsonArray().get(0).getAsJsonObject().get("type").getAsString();
        }
        try {
            return (Geometry) context.deserialize(json, Class.forName("com.mapbox.geojson." + geometryType));
        } catch (ClassNotFoundException classNotFoundException) {
            throw new JsonParseException(classNotFoundException);
        }
    }
}
