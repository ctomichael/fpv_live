package com.mapbox.geojson.gson;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.mapbox.geojson.BoundingBox;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.shifter.CoordinateShifterManager;
import com.mapbox.geojson.utils.GeoJsonUtils;
import java.lang.reflect.Type;
import java.util.List;

@Deprecated
public class BoundingBoxSerializer implements JsonSerializer<BoundingBox> {
    public JsonElement serialize(BoundingBox src, Type typeOfSrc, JsonSerializationContext context) {
        JsonArray bbox = new JsonArray();
        Point point = src.southwest();
        List<Double> unshiftedCoordinates = CoordinateShifterManager.getCoordinateShifter().unshiftPoint(point);
        bbox.add(new JsonPrimitive((Number) Double.valueOf(GeoJsonUtils.trim(unshiftedCoordinates.get(0).doubleValue()))));
        bbox.add(new JsonPrimitive((Number) Double.valueOf(GeoJsonUtils.trim(unshiftedCoordinates.get(1).doubleValue()))));
        if (point.hasAltitude()) {
            bbox.add(new JsonPrimitive((Number) unshiftedCoordinates.get(2)));
        }
        Point point2 = src.northeast();
        List<Double> unshiftedCoordinates2 = CoordinateShifterManager.getCoordinateShifter().unshiftPoint(point2);
        bbox.add(new JsonPrimitive((Number) Double.valueOf(GeoJsonUtils.trim(unshiftedCoordinates2.get(0).doubleValue()))));
        bbox.add(new JsonPrimitive((Number) Double.valueOf(GeoJsonUtils.trim(unshiftedCoordinates2.get(1).doubleValue()))));
        if (point2.hasAltitude()) {
            bbox.add(new JsonPrimitive((Number) unshiftedCoordinates2.get(2)));
        }
        return bbox;
    }
}
