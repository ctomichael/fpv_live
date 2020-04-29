package com.mapbox.turf;

import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.gson.JsonObject;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Geometry;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.MultiLineString;
import com.mapbox.geojson.MultiPolygon;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.Polygon;
import com.sun.istack.internal.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class TurfConversion {
    private static final Map<String, Double> FACTORS = new HashMap();

    static {
        FACTORS.put(TurfConstants.UNIT_MILES, Double.valueOf(3960.0d));
        FACTORS.put(TurfConstants.UNIT_NAUTICAL_MILES, Double.valueOf(3441.145d));
        FACTORS.put(TurfConstants.UNIT_DEGREES, Double.valueOf(57.2957795d));
        FACTORS.put(TurfConstants.UNIT_RADIANS, Double.valueOf(1.0d));
        FACTORS.put(TurfConstants.UNIT_INCHES, Double.valueOf(2.509056E8d));
        FACTORS.put(TurfConstants.UNIT_YARDS, Double.valueOf(6969600.0d));
        FACTORS.put(TurfConstants.UNIT_METERS, Double.valueOf(6373000.0d));
        FACTORS.put(TurfConstants.UNIT_CENTIMETERS, Double.valueOf(6.373E8d));
        FACTORS.put("kilometers", Double.valueOf(6373.0d));
        FACTORS.put(TurfConstants.UNIT_FEET, Double.valueOf(2.090879265E7d));
        FACTORS.put(TurfConstants.UNIT_CENTIMETRES, Double.valueOf(6.373E8d));
        FACTORS.put(TurfConstants.UNIT_METRES, Double.valueOf(6373000.0d));
        FACTORS.put(TurfConstants.UNIT_KILOMETRES, Double.valueOf(6373.0d));
    }

    private TurfConversion() {
    }

    public static double lengthToDegrees(double distance, String units) {
        return radiansToDegrees(lengthToRadians(distance, units));
    }

    public static double degreesToRadians(double degrees) {
        return (3.141592653589793d * (degrees % 360.0d)) / 180.0d;
    }

    public static double radiansToDegrees(double radians) {
        return (180.0d * (radians % 6.283185307179586d)) / 3.141592653589793d;
    }

    public static double radiansToLength(double radians) {
        return radiansToLength(radians, "kilometers");
    }

    public static double radiansToLength(double radians, @NonNull String units) {
        return FACTORS.get(units).doubleValue() * radians;
    }

    public static double lengthToRadians(double distance) {
        return lengthToRadians(distance, "kilometers");
    }

    public static double lengthToRadians(double distance, @NonNull String units) {
        return distance / FACTORS.get(units).doubleValue();
    }

    public static double convertLength(@FloatRange(from = 0.0d) double distance, @NonNull String originalUnit) {
        return convertLength(distance, originalUnit, "kilometers");
    }

    public static double convertLength(@FloatRange(from = 0.0d) double distance, @NonNull String originalUnit, @Nullable String finalUnit) {
        if (finalUnit == null) {
            finalUnit = "kilometers";
        }
        return radiansToLength(lengthToRadians(distance, originalUnit), finalUnit);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.mapbox.turf.TurfMeta.coordAll(com.mapbox.geojson.FeatureCollection, boolean):java.util.List<com.mapbox.geojson.Point>
     arg types: [com.mapbox.geojson.FeatureCollection, int]
     candidates:
      com.mapbox.turf.TurfMeta.coordAll(com.mapbox.geojson.Feature, boolean):java.util.List<com.mapbox.geojson.Point>
      com.mapbox.turf.TurfMeta.coordAll(com.mapbox.geojson.MultiPolygon, boolean):java.util.List<com.mapbox.geojson.Point>
      com.mapbox.turf.TurfMeta.coordAll(com.mapbox.geojson.Polygon, boolean):java.util.List<com.mapbox.geojson.Point>
      com.mapbox.turf.TurfMeta.coordAll(java.util.List<com.mapbox.geojson.Point>, com.mapbox.geojson.LineString):java.util.List<com.mapbox.geojson.Point>
      com.mapbox.turf.TurfMeta.coordAll(java.util.List<com.mapbox.geojson.Point>, com.mapbox.geojson.MultiLineString):java.util.List<com.mapbox.geojson.Point>
      com.mapbox.turf.TurfMeta.coordAll(java.util.List<com.mapbox.geojson.Point>, com.mapbox.geojson.MultiPoint):java.util.List<com.mapbox.geojson.Point>
      com.mapbox.turf.TurfMeta.coordAll(java.util.List<com.mapbox.geojson.Point>, com.mapbox.geojson.Point):java.util.List<com.mapbox.geojson.Point>
      com.mapbox.turf.TurfMeta.coordAll(com.mapbox.geojson.FeatureCollection, boolean):java.util.List<com.mapbox.geojson.Point> */
    public static FeatureCollection explode(@NonNull FeatureCollection featureCollection) {
        List<Feature> finalFeatureList = new ArrayList<>();
        for (Point singlePoint : TurfMeta.coordAll(featureCollection, true)) {
            finalFeatureList.add(Feature.fromGeometry(singlePoint));
        }
        return FeatureCollection.fromFeatures(finalFeatureList);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.mapbox.turf.TurfMeta.coordAll(com.mapbox.geojson.Feature, boolean):java.util.List<com.mapbox.geojson.Point>
     arg types: [com.mapbox.geojson.Feature, int]
     candidates:
      com.mapbox.turf.TurfMeta.coordAll(com.mapbox.geojson.FeatureCollection, boolean):java.util.List<com.mapbox.geojson.Point>
      com.mapbox.turf.TurfMeta.coordAll(com.mapbox.geojson.MultiPolygon, boolean):java.util.List<com.mapbox.geojson.Point>
      com.mapbox.turf.TurfMeta.coordAll(com.mapbox.geojson.Polygon, boolean):java.util.List<com.mapbox.geojson.Point>
      com.mapbox.turf.TurfMeta.coordAll(java.util.List<com.mapbox.geojson.Point>, com.mapbox.geojson.LineString):java.util.List<com.mapbox.geojson.Point>
      com.mapbox.turf.TurfMeta.coordAll(java.util.List<com.mapbox.geojson.Point>, com.mapbox.geojson.MultiLineString):java.util.List<com.mapbox.geojson.Point>
      com.mapbox.turf.TurfMeta.coordAll(java.util.List<com.mapbox.geojson.Point>, com.mapbox.geojson.MultiPoint):java.util.List<com.mapbox.geojson.Point>
      com.mapbox.turf.TurfMeta.coordAll(java.util.List<com.mapbox.geojson.Point>, com.mapbox.geojson.Point):java.util.List<com.mapbox.geojson.Point>
      com.mapbox.turf.TurfMeta.coordAll(com.mapbox.geojson.Feature, boolean):java.util.List<com.mapbox.geojson.Point> */
    public static FeatureCollection explode(@NonNull Feature feature) {
        List<Feature> finalFeatureList = new ArrayList<>();
        for (Point singlePoint : TurfMeta.coordAll(feature, true)) {
            finalFeatureList.add(Feature.fromGeometry(singlePoint));
        }
        return FeatureCollection.fromFeatures(finalFeatureList);
    }

    public static Feature polygonToLine(@NotNull Feature feature) {
        return polygonToLine(feature, (JsonObject) null);
    }

    public static Feature polygonToLine(@NotNull Feature feature, @Nullable JsonObject properties) {
        Geometry geometry = feature.geometry();
        if (geometry instanceof Polygon) {
            Polygon polygon = (Polygon) geometry;
            if (properties == null) {
                properties = feature.type().equals("Feature") ? feature.properties() : new JsonObject();
            }
            return polygonToLine(polygon, properties);
        }
        throw new TurfException("Feature's geometry must be Polygon");
    }

    public static Feature polygonToLine(@NotNull Polygon polygon) {
        return polygonToLine(polygon, (JsonObject) null);
    }

    public static FeatureCollection polygonToLine(@NotNull MultiPolygon multiPolygon) {
        return polygonToLine(multiPolygon, (JsonObject) null);
    }

    public static Feature polygonToLine(@NotNull Polygon polygon, @Nullable JsonObject properties) {
        return coordsToLine(polygon.coordinates(), properties);
    }

    public static FeatureCollection polygonToLine(@NotNull MultiPolygon multiPolygon, @Nullable JsonObject properties) {
        List<List<List<Point>>> coordinates = multiPolygon.coordinates();
        List<Feature> finalFeatureList = new ArrayList<>();
        for (List<List<Point>> polygonCoordinates : coordinates) {
            finalFeatureList.add(coordsToLine(polygonCoordinates, properties));
        }
        return FeatureCollection.fromFeatures(finalFeatureList);
    }

    public static FeatureCollection multiPolygonToLine(@NotNull Feature feature) {
        return multiPolygonToLine(feature, null);
    }

    public static FeatureCollection multiPolygonToLine(@NotNull Feature feature, @Nullable JsonObject properties) {
        Geometry geometry = feature.geometry();
        if (geometry instanceof MultiPolygon) {
            MultiPolygon multiPolygon = (MultiPolygon) geometry;
            if (properties == null) {
                properties = feature.type().equals("Feature") ? feature.properties() : new JsonObject();
            }
            return polygonToLine(multiPolygon, properties);
        }
        throw new TurfException("Feature's geometry must be MultiPolygon");
    }

    @Nullable
    private static Feature coordsToLine(@NotNull List<List<Point>> coordinates, @Nullable JsonObject properties) {
        if (coordinates.size() > 1) {
            return Feature.fromGeometry(MultiLineString.fromLngLats(coordinates), properties);
        }
        if (coordinates.size() == 1) {
            return Feature.fromGeometry(LineString.fromLngLats(coordinates.get(0)), properties);
        }
        return null;
    }
}
