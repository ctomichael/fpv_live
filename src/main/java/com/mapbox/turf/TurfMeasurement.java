package com.mapbox.turf;

import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.gson.JsonObject;
import com.mapbox.geojson.BoundingBox;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.GeoJson;
import com.mapbox.geojson.Geometry;
import com.mapbox.geojson.GeometryCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.MultiLineString;
import com.mapbox.geojson.MultiPoint;
import com.mapbox.geojson.MultiPolygon;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.Polygon;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class TurfMeasurement {
    private TurfMeasurement() {
        throw new AssertionError("No Instances.");
    }

    public static double bearing(@NonNull Point point1, @NonNull Point point2) {
        double lon1 = TurfConversion.degreesToRadians(point1.longitude());
        double lon2 = TurfConversion.degreesToRadians(point2.longitude());
        double lat1 = TurfConversion.degreesToRadians(point1.latitude());
        double lat2 = TurfConversion.degreesToRadians(point2.latitude());
        return TurfConversion.radiansToDegrees(Math.atan2(Math.sin(lon2 - lon1) * Math.cos(lat2), (Math.cos(lat1) * Math.sin(lat2)) - ((Math.sin(lat1) * Math.cos(lat2)) * Math.cos(lon2 - lon1))));
    }

    @NonNull
    public static Point destination(@NonNull Point point, @FloatRange(from = 0.0d) double distance, @FloatRange(from = -180.0d, to = 180.0d) double bearing, @NonNull String units) {
        double longitude1 = TurfConversion.degreesToRadians(point.longitude());
        double latitude1 = TurfConversion.degreesToRadians(point.latitude());
        double bearingRad = TurfConversion.degreesToRadians(bearing);
        double radians = TurfConversion.lengthToRadians(distance, units);
        double latitude2 = Math.asin((Math.sin(latitude1) * Math.cos(radians)) + (Math.cos(latitude1) * Math.sin(radians) * Math.cos(bearingRad)));
        return Point.fromLngLat(TurfConversion.radiansToDegrees(longitude1 + Math.atan2(Math.sin(bearingRad) * Math.sin(radians) * Math.cos(latitude1), Math.cos(radians) - (Math.sin(latitude1) * Math.sin(latitude2)))), TurfConversion.radiansToDegrees(latitude2));
    }

    public static double distance(@NonNull Point point1, @NonNull Point point2) {
        return distance(point1, point2, "kilometers");
    }

    public static double distance(@NonNull Point point1, @NonNull Point point2, @NonNull String units) {
        double value = Math.pow(Math.sin(TurfConversion.degreesToRadians(point2.latitude() - point1.latitude()) / 2.0d), 2.0d) + (Math.pow(Math.sin(TurfConversion.degreesToRadians(point2.longitude() - point1.longitude()) / 2.0d), 2.0d) * Math.cos(TurfConversion.degreesToRadians(point1.latitude())) * Math.cos(TurfConversion.degreesToRadians(point2.latitude())));
        return TurfConversion.radiansToLength(2.0d * Math.atan2(Math.sqrt(value), Math.sqrt(1.0d - value)), units);
    }

    public static double length(@NonNull LineString lineString, @NonNull String units) {
        return length(lineString.coordinates(), units);
    }

    public static double length(@NonNull MultiLineString multiLineString, @NonNull String units) {
        double len = 0.0d;
        for (List<Point> points : multiLineString.coordinates()) {
            len += length(points, units);
        }
        return len;
    }

    public static double length(@NonNull Polygon polygon, @NonNull String units) {
        double len = 0.0d;
        for (List<Point> points : polygon.coordinates()) {
            len += length(points, units);
        }
        return len;
    }

    public static double length(@NonNull MultiPolygon multiPolygon, @NonNull String units) {
        double len = 0.0d;
        for (List<List<Point>> coordinate : multiPolygon.coordinates()) {
            for (List<Point> theCoordinate : coordinate) {
                len += length(theCoordinate, units);
            }
        }
        return len;
    }

    private static double length(List<Point> coords, String units) {
        double travelled = 0.0d;
        Point prevCoords = coords.get(0);
        for (int i = 1; i < coords.size(); i++) {
            Point curCoords = coords.get(i);
            travelled += distance(prevCoords, curCoords, units);
            prevCoords = curCoords;
        }
        return travelled;
    }

    public static Point midpoint(@NonNull Point from, @NonNull Point to) {
        return destination(from, distance(from, to, TurfConstants.UNIT_MILES) / 2.0d, bearing(from, to), TurfConstants.UNIT_MILES);
    }

    public static Point along(@NonNull LineString line, @FloatRange(from = 0.0d) double distance, @NonNull String units) {
        List<Point> coords = line.coordinates();
        double travelled = 0.0d;
        int i = 0;
        while (i < coords.size() && (distance < travelled || i != coords.size() - 1)) {
            if (travelled >= distance) {
                double overshot = distance - travelled;
                if (overshot == 0.0d) {
                    return coords.get(i);
                }
                return destination(coords.get(i), overshot, bearing(coords.get(i), coords.get(i - 1)) - 180.0d, units);
            }
            travelled += distance(coords.get(i), coords.get(i + 1), units);
            i++;
        }
        return coords.get(coords.size() - 1);
    }

    public static double[] bbox(@NonNull Point point) {
        return bboxCalculator(TurfMeta.coordAll(point));
    }

    public static double[] bbox(@NonNull LineString lineString) {
        return bboxCalculator(TurfMeta.coordAll(lineString));
    }

    public static double[] bbox(@NonNull MultiPoint multiPoint) {
        return bboxCalculator(TurfMeta.coordAll(multiPoint));
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.mapbox.turf.TurfMeta.coordAll(com.mapbox.geojson.Polygon, boolean):java.util.List<com.mapbox.geojson.Point>
     arg types: [com.mapbox.geojson.Polygon, int]
     candidates:
      com.mapbox.turf.TurfMeta.coordAll(com.mapbox.geojson.Feature, boolean):java.util.List<com.mapbox.geojson.Point>
      com.mapbox.turf.TurfMeta.coordAll(com.mapbox.geojson.FeatureCollection, boolean):java.util.List<com.mapbox.geojson.Point>
      com.mapbox.turf.TurfMeta.coordAll(com.mapbox.geojson.MultiPolygon, boolean):java.util.List<com.mapbox.geojson.Point>
      com.mapbox.turf.TurfMeta.coordAll(java.util.List<com.mapbox.geojson.Point>, com.mapbox.geojson.LineString):java.util.List<com.mapbox.geojson.Point>
      com.mapbox.turf.TurfMeta.coordAll(java.util.List<com.mapbox.geojson.Point>, com.mapbox.geojson.MultiLineString):java.util.List<com.mapbox.geojson.Point>
      com.mapbox.turf.TurfMeta.coordAll(java.util.List<com.mapbox.geojson.Point>, com.mapbox.geojson.MultiPoint):java.util.List<com.mapbox.geojson.Point>
      com.mapbox.turf.TurfMeta.coordAll(java.util.List<com.mapbox.geojson.Point>, com.mapbox.geojson.Point):java.util.List<com.mapbox.geojson.Point>
      com.mapbox.turf.TurfMeta.coordAll(com.mapbox.geojson.Polygon, boolean):java.util.List<com.mapbox.geojson.Point> */
    public static double[] bbox(@NonNull Polygon polygon) {
        return bboxCalculator(TurfMeta.coordAll(polygon, false));
    }

    public static double[] bbox(@NonNull MultiLineString multiLineString) {
        return bboxCalculator(TurfMeta.coordAll(multiLineString));
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.mapbox.turf.TurfMeta.coordAll(com.mapbox.geojson.MultiPolygon, boolean):java.util.List<com.mapbox.geojson.Point>
     arg types: [com.mapbox.geojson.MultiPolygon, int]
     candidates:
      com.mapbox.turf.TurfMeta.coordAll(com.mapbox.geojson.Feature, boolean):java.util.List<com.mapbox.geojson.Point>
      com.mapbox.turf.TurfMeta.coordAll(com.mapbox.geojson.FeatureCollection, boolean):java.util.List<com.mapbox.geojson.Point>
      com.mapbox.turf.TurfMeta.coordAll(com.mapbox.geojson.Polygon, boolean):java.util.List<com.mapbox.geojson.Point>
      com.mapbox.turf.TurfMeta.coordAll(java.util.List<com.mapbox.geojson.Point>, com.mapbox.geojson.LineString):java.util.List<com.mapbox.geojson.Point>
      com.mapbox.turf.TurfMeta.coordAll(java.util.List<com.mapbox.geojson.Point>, com.mapbox.geojson.MultiLineString):java.util.List<com.mapbox.geojson.Point>
      com.mapbox.turf.TurfMeta.coordAll(java.util.List<com.mapbox.geojson.Point>, com.mapbox.geojson.MultiPoint):java.util.List<com.mapbox.geojson.Point>
      com.mapbox.turf.TurfMeta.coordAll(java.util.List<com.mapbox.geojson.Point>, com.mapbox.geojson.Point):java.util.List<com.mapbox.geojson.Point>
      com.mapbox.turf.TurfMeta.coordAll(com.mapbox.geojson.MultiPolygon, boolean):java.util.List<com.mapbox.geojson.Point> */
    public static double[] bbox(MultiPolygon multiPolygon) {
        return bboxCalculator(TurfMeta.coordAll(multiPolygon, false));
    }

    public static double[] bbox(GeoJson geoJson) {
        BoundingBox boundingBox = geoJson.bbox();
        if (boundingBox != null) {
            return new double[]{boundingBox.west(), boundingBox.south(), boundingBox.east(), boundingBox.north()};
        } else if (geoJson instanceof Geometry) {
            return bbox((Geometry) geoJson);
        } else {
            if (geoJson instanceof FeatureCollection) {
                return bbox((FeatureCollection) geoJson);
            }
            if (geoJson instanceof Feature) {
                return bbox((Feature) geoJson);
            }
            throw new UnsupportedOperationException("bbox type not supported for GeoJson instance");
        }
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
    public static double[] bbox(FeatureCollection featureCollection) {
        return bboxCalculator(TurfMeta.coordAll(featureCollection, false));
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
    public static double[] bbox(Feature feature) {
        return bboxCalculator(TurfMeta.coordAll(feature, false));
    }

    public static double[] bbox(Geometry geometry) {
        if (geometry instanceof Point) {
            return bbox((Point) geometry);
        }
        if (geometry instanceof MultiPoint) {
            return bbox((MultiPoint) geometry);
        }
        if (geometry instanceof LineString) {
            return bbox((LineString) geometry);
        }
        if (geometry instanceof MultiLineString) {
            return bbox((MultiLineString) geometry);
        }
        if (geometry instanceof Polygon) {
            return bbox((Polygon) geometry);
        }
        if (geometry instanceof MultiPolygon) {
            return bbox((MultiPolygon) geometry);
        }
        if (geometry instanceof GeometryCollection) {
            List<Point> points = new ArrayList<>();
            for (Geometry geo : ((GeometryCollection) geometry).geometries()) {
                double[] bbox = bbox(geo);
                points.add(Point.fromLngLat(bbox[0], bbox[1]));
                points.add(Point.fromLngLat(bbox[2], bbox[1]));
                points.add(Point.fromLngLat(bbox[2], bbox[3]));
                points.add(Point.fromLngLat(bbox[0], bbox[3]));
            }
            return bbox(MultiPoint.fromLngLats(points));
        }
        throw new RuntimeException("Unknown geometry class: " + geometry.getClass());
    }

    private static double[] bboxCalculator(List<Point> resultCoords) {
        double[] bbox = {Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY};
        for (Point point : resultCoords) {
            if (bbox[0] > point.longitude()) {
                bbox[0] = point.longitude();
            }
            if (bbox[1] > point.latitude()) {
                bbox[1] = point.latitude();
            }
            if (bbox[2] < point.longitude()) {
                bbox[2] = point.longitude();
            }
            if (bbox[3] < point.latitude()) {
                bbox[3] = point.latitude();
            }
        }
        return bbox;
    }

    public static Feature bboxPolygon(@NonNull BoundingBox boundingBox) {
        return bboxPolygon(boundingBox, (JsonObject) null, (String) null);
    }

    public static Feature bboxPolygon(@NonNull BoundingBox boundingBox, @Nullable JsonObject properties, @Nullable String id) {
        return Feature.fromGeometry(Polygon.fromLngLats(Collections.singletonList(Arrays.asList(Point.fromLngLat(boundingBox.west(), boundingBox.south()), Point.fromLngLat(boundingBox.east(), boundingBox.south()), Point.fromLngLat(boundingBox.east(), boundingBox.north()), Point.fromLngLat(boundingBox.west(), boundingBox.north()), Point.fromLngLat(boundingBox.west(), boundingBox.south())))), properties, id);
    }

    public static Feature bboxPolygon(@NonNull double[] bbox) {
        return bboxPolygon(bbox, (JsonObject) null, (String) null);
    }

    public static Feature bboxPolygon(@NonNull double[] bbox, @Nullable JsonObject properties, @Nullable String id) {
        return Feature.fromGeometry(Polygon.fromLngLats(Collections.singletonList(Arrays.asList(Point.fromLngLat(bbox[0], bbox[1]), Point.fromLngLat(bbox[2], bbox[1]), Point.fromLngLat(bbox[2], bbox[3]), Point.fromLngLat(bbox[0], bbox[3]), Point.fromLngLat(bbox[0], bbox[1])))), properties, id);
    }

    public static Polygon envelope(GeoJson geoJson) {
        return (Polygon) bboxPolygon(bbox(geoJson)).geometry();
    }

    public static BoundingBox square(@NonNull BoundingBox boundingBox) {
        if (distance(boundingBox.southwest(), Point.fromLngLat(boundingBox.east(), boundingBox.south())) >= distance(Point.fromLngLat(boundingBox.west(), boundingBox.south()), Point.fromLngLat(boundingBox.west(), boundingBox.north()))) {
            double verticalMidpoint = (boundingBox.south() + boundingBox.north()) / 2.0d;
            return BoundingBox.fromLngLats(boundingBox.west(), verticalMidpoint - ((boundingBox.east() - boundingBox.west()) / 2.0d), boundingBox.east(), ((boundingBox.east() - boundingBox.west()) / 2.0d) + verticalMidpoint);
        }
        double horizontalMidpoint = (boundingBox.west() + boundingBox.east()) / 2.0d;
        return BoundingBox.fromLngLats(horizontalMidpoint - ((boundingBox.north() - boundingBox.south()) / 2.0d), boundingBox.south(), ((boundingBox.north() - boundingBox.south()) / 2.0d) + horizontalMidpoint, boundingBox.north());
    }
}
