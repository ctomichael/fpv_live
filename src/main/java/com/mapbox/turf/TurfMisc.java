package com.mapbox.turf;

import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.turf.models.LineIntersectsResult;
import java.util.ArrayList;
import java.util.List;

public final class TurfMisc {
    private static final String INDEX_KEY = "index";

    private TurfMisc() {
        throw new AssertionError("No Instances.");
    }

    @NonNull
    public static LineString lineSlice(@NonNull Point startPt, @NonNull Point stopPt, @NonNull Feature line) {
        if (line.geometry() == null) {
            throw new NullPointerException("Feature.geometry() == null");
        } else if (line.geometry().type().equals("LineString")) {
            return lineSlice(startPt, stopPt, (LineString) line.geometry());
        } else {
            throw new TurfException("input must be a LineString Feature or Geometry");
        }
    }

    @NonNull
    public static LineString lineSlice(@NonNull Point startPt, @NonNull Point stopPt, @NonNull LineString line) {
        List<Point> coords = line.coordinates();
        if (coords.size() < 2) {
            throw new TurfException("Turf lineSlice requires a LineString made up of at least 2 coordinates.");
        } else if (startPt.equals(stopPt)) {
            throw new TurfException("Start and stop points in Turf lineSlice cannot equal each other.");
        } else {
            Feature startVertex = nearestPointOnLine(startPt, coords);
            Feature stopVertex = nearestPointOnLine(stopPt, coords);
            List<Feature> ends = new ArrayList<>();
            if (((Integer) startVertex.getNumberProperty("index")).intValue() <= ((Integer) stopVertex.getNumberProperty("index")).intValue()) {
                ends.add(startVertex);
                ends.add(stopVertex);
            } else {
                ends.add(stopVertex);
                ends.add(startVertex);
            }
            List<Point> points = new ArrayList<>();
            points.add((Point) ((Feature) ends.get(0)).geometry());
            for (int i = ((Integer) ((Feature) ends.get(0)).getNumberProperty("index")).intValue() + 1; i < ((Integer) ((Feature) ends.get(1)).getNumberProperty("index")).intValue() + 1; i++) {
                points.add(coords.get(i));
            }
            points.add((Point) ((Feature) ends.get(1)).geometry());
            return LineString.fromLngLats(points);
        }
    }

    @NonNull
    public static LineString lineSliceAlong(@NonNull Feature line, @FloatRange(from = 0.0d) double startDist, @FloatRange(from = 0.0d) double stopDist, @NonNull String units) {
        if (line.geometry() == null) {
            throw new NullPointerException("Feature.geometry() == null");
        } else if (line.geometry().type().equals("LineString")) {
            return lineSliceAlong((LineString) line.geometry(), startDist, stopDist, units);
        } else {
            throw new TurfException("input must be a LineString Feature or Geometry");
        }
    }

    @NonNull
    public static LineString lineSliceAlong(@NonNull LineString line, @FloatRange(from = 0.0d) double startDist, @FloatRange(from = 0.0d) double stopDist, @NonNull String units) {
        List<Point> coords = line.coordinates();
        if (coords.size() < 2) {
            throw new TurfException("Turf lineSlice requires a LineString Geometry made up of at least 2 coordinates. The LineString passed in only contains " + coords.size() + ".");
        } else if (startDist == stopDist) {
            throw new TurfException("Start and stop distance in Turf lineSliceAlong cannot equal each other.");
        } else {
            List<Point> slice = new ArrayList<>(2);
            double travelled = 0.0d;
            int i = 0;
            while (i < coords.size() && (startDist < travelled || i != coords.size() - 1)) {
                if (travelled > startDist && slice.size() == 0) {
                    double overshot = startDist - travelled;
                    if (overshot == 0.0d) {
                        slice.add(coords.get(i));
                        return LineString.fromLngLats(slice);
                    }
                    slice.add(TurfMeasurement.destination(coords.get(i), overshot, TurfMeasurement.bearing(coords.get(i), coords.get(i - 1)) - 180.0d, units));
                }
                if (travelled >= stopDist) {
                    double overshot2 = stopDist - travelled;
                    if (overshot2 == 0.0d) {
                        slice.add(coords.get(i));
                        return LineString.fromLngLats(slice);
                    }
                    slice.add(TurfMeasurement.destination(coords.get(i), overshot2, TurfMeasurement.bearing(coords.get(i), coords.get(i - 1)) - 180.0d, units));
                    return LineString.fromLngLats(slice);
                }
                if (travelled >= startDist) {
                    slice.add(coords.get(i));
                }
                if (i == coords.size() - 1) {
                    return LineString.fromLngLats(slice);
                }
                travelled += TurfMeasurement.distance(coords.get(i), coords.get(i + 1), units);
                i++;
            }
            if (travelled >= startDist) {
                return LineString.fromLngLats(slice);
            }
            throw new TurfException("Start position is beyond line");
        }
    }

    @NonNull
    public static Feature nearestPointOnLine(@NonNull Point pt, @NonNull List<Point> coords) {
        return nearestPointOnLine(pt, coords, null);
    }

    @NonNull
    public static Feature nearestPointOnLine(@NonNull Point pt, @NonNull List<Point> coords, @Nullable String units) {
        if (coords.size() < 2) {
            throw new TurfException("Turf nearestPointOnLine requires a List of Points made up of at least 2 coordinates.");
        }
        if (units == null) {
            units = "kilometers";
        }
        Feature closestPt = Feature.fromGeometry(Point.fromLngLat(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        closestPt.addNumberProperty("dist", Double.valueOf(Double.POSITIVE_INFINITY));
        for (int i = 0; i < coords.size() - 1; i++) {
            Feature start = Feature.fromGeometry(coords.get(i));
            Feature stop = Feature.fromGeometry(coords.get(i + 1));
            Feature feature = start;
            feature.addNumberProperty("dist", Double.valueOf(TurfMeasurement.distance(pt, (Point) start.geometry(), units)));
            Feature feature2 = stop;
            feature2.addNumberProperty("dist", Double.valueOf(TurfMeasurement.distance(pt, (Point) stop.geometry(), units)));
            double heightDistance = Math.max(start.properties().get("dist").getAsDouble(), stop.properties().get("dist").getAsDouble());
            double direction = TurfMeasurement.bearing((Point) start.geometry(), (Point) stop.geometry());
            Feature perpendicularPt1 = Feature.fromGeometry(TurfMeasurement.destination(pt, heightDistance, 90.0d + direction, units));
            Feature perpendicularPt2 = Feature.fromGeometry(TurfMeasurement.destination(pt, heightDistance, direction - 90.0d, units));
            LineIntersectsResult intersect = lineIntersects(((Point) perpendicularPt1.geometry()).longitude(), ((Point) perpendicularPt1.geometry()).latitude(), ((Point) perpendicularPt2.geometry()).longitude(), ((Point) perpendicularPt2.geometry()).latitude(), ((Point) start.geometry()).longitude(), ((Point) start.geometry()).latitude(), ((Point) stop.geometry()).longitude(), ((Point) stop.geometry()).latitude());
            Feature intersectPt = null;
            if (intersect != null) {
                intersectPt = Feature.fromGeometry(Point.fromLngLat(intersect.horizontalIntersection().doubleValue(), intersect.verticalIntersection().doubleValue()));
                Feature feature3 = intersectPt;
                feature3.addNumberProperty("dist", Double.valueOf(TurfMeasurement.distance(pt, (Point) intersectPt.geometry(), units)));
            }
            if (((Double) start.getNumberProperty("dist")).doubleValue() < ((Double) closestPt.getNumberProperty("dist")).doubleValue()) {
                closestPt = start;
                closestPt.addNumberProperty("index", Integer.valueOf(i));
            }
            if (((Double) stop.getNumberProperty("dist")).doubleValue() < ((Double) closestPt.getNumberProperty("dist")).doubleValue()) {
                closestPt = stop;
                closestPt.addNumberProperty("index", Integer.valueOf(i));
            }
            if (intersectPt != null && ((Double) intersectPt.getNumberProperty("dist")).doubleValue() < ((Double) closestPt.getNumberProperty("dist")).doubleValue()) {
                closestPt = intersectPt;
                closestPt.addNumberProperty("index", Integer.valueOf(i));
            }
        }
        return closestPt;
    }

    private static LineIntersectsResult lineIntersects(double line1StartX, double line1StartY, double line1EndX, double line1EndY, double line2StartX, double line2StartY, double line2EndX, double line2EndY) {
        LineIntersectsResult result = LineIntersectsResult.builder().onLine1(false).onLine2(false).build();
        double denominator = ((line2EndY - line2StartY) * (line1EndX - line1StartX)) - ((line2EndX - line2StartX) * (line1EndY - line1StartY));
        if (denominator != 0.0d) {
            double varA = line1StartY - line2StartY;
            double varB = line1StartX - line2StartX;
            double varA2 = (((line2EndX - line2StartX) * varA) - ((line2EndY - line2StartY) * varB)) / denominator;
            double varB2 = (((line1EndX - line1StartX) * varA) - ((line1EndY - line1StartY) * varB)) / denominator;
            LineIntersectsResult result2 = result.toBuilder().horizontalIntersection(Double.valueOf(((line1EndX - line1StartX) * varA2) + line1StartX)).build().toBuilder().verticalIntersection(Double.valueOf(((line1EndY - line1StartY) * varA2) + line1StartY)).build();
            if (varA2 > 0.0d && varA2 < 1.0d) {
                result2 = result2.toBuilder().onLine1(true).build();
            }
            if (varB2 > 0.0d && varB2 < 1.0d) {
                result2 = result2.toBuilder().onLine2(true).build();
            }
            if (!result2.onLine1() || !result2.onLine2()) {
                return null;
            }
            return result2;
        } else if (result.horizontalIntersection() == null || result.verticalIntersection() == null) {
            return null;
        } else {
            return result;
        }
    }
}
