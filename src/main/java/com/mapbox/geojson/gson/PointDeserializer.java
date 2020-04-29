package com.mapbox.geojson.gson;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.mapbox.geojson.Point;
import java.lang.reflect.Type;

@Deprecated
public class PointDeserializer implements JsonDeserializer<Point> {
    public Point deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        JsonArray rawCoordinates = json.getAsJsonArray();
        double longitude = rawCoordinates.get(0).getAsDouble();
        double latitude = rawCoordinates.get(1).getAsDouble();
        if (rawCoordinates.size() > 2) {
            return Point.fromLngLat(longitude, latitude, rawCoordinates.get(2).getAsDouble());
        }
        return Point.fromLngLat(longitude, latitude);
    }
}
