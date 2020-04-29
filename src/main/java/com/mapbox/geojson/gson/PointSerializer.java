package com.mapbox.geojson.gson;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.shifter.CoordinateShifterManager;
import com.mapbox.geojson.utils.GeoJsonUtils;
import java.lang.reflect.Type;
import java.util.List;

@Deprecated
public class PointSerializer implements JsonSerializer<Point> {
    public JsonElement serialize(Point src, Type typeOfSrc, JsonSerializationContext context) {
        JsonArray rawCoordinates = new JsonArray();
        List<Double> unshiftedCoordinates = CoordinateShifterManager.getCoordinateShifter().unshiftPoint(src);
        rawCoordinates.add(new JsonPrimitive((Number) Double.valueOf(GeoJsonUtils.trim(unshiftedCoordinates.get(0).doubleValue()))));
        rawCoordinates.add(new JsonPrimitive((Number) Double.valueOf(GeoJsonUtils.trim(unshiftedCoordinates.get(1).doubleValue()))));
        if (src.hasAltitude()) {
            rawCoordinates.add(new JsonPrimitive((Number) unshiftedCoordinates.get(2)));
        }
        return rawCoordinates;
    }
}
