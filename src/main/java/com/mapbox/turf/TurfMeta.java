package com.mapbox.turf;

import android.support.annotation.NonNull;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Geometry;
import com.mapbox.geojson.GeometryCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.MultiLineString;
import com.mapbox.geojson.MultiPoint;
import com.mapbox.geojson.MultiPolygon;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.Polygon;
import java.util.ArrayList;
import java.util.List;

public final class TurfMeta {
    private TurfMeta() {
    }

    @NonNull
    public static List<Point> coordAll(@NonNull Point point) {
        return coordAll(new ArrayList(), point);
    }

    @NonNull
    private static List<Point> coordAll(@NonNull List<Point> coords, @NonNull Point point) {
        coords.add(point);
        return coords;
    }

    @NonNull
    public static List<Point> coordAll(@NonNull MultiPoint multiPoint) {
        return coordAll(new ArrayList(), multiPoint);
    }

    @NonNull
    private static List<Point> coordAll(@NonNull List<Point> coords, @NonNull MultiPoint multiPoint) {
        coords.addAll(multiPoint.coordinates());
        return coords;
    }

    @NonNull
    public static List<Point> coordAll(@NonNull LineString lineString) {
        return coordAll(new ArrayList(), lineString);
    }

    @NonNull
    private static List<Point> coordAll(@NonNull List<Point> coords, @NonNull LineString lineString) {
        coords.addAll(lineString.coordinates());
        return coords;
    }

    @NonNull
    public static List<Point> coordAll(@NonNull Polygon polygon, @NonNull boolean excludeWrapCoord) {
        return coordAll(new ArrayList(), polygon, excludeWrapCoord);
    }

    @NonNull
    private static List<Point> coordAll(@NonNull List<Point> coords, @NonNull Polygon polygon, @NonNull boolean excludeWrapCoord) {
        int wrapShrink = excludeWrapCoord ? 1 : 0;
        for (int i = 0; i < polygon.coordinates().size(); i++) {
            for (int j = 0; j < polygon.coordinates().get(i).size() - wrapShrink; j++) {
                coords.add(polygon.coordinates().get(i).get(j));
            }
        }
        return coords;
    }

    @NonNull
    public static List<Point> coordAll(@NonNull MultiLineString multiLineString) {
        return coordAll(new ArrayList(), multiLineString);
    }

    @NonNull
    private static List<Point> coordAll(@NonNull List<Point> coords, @NonNull MultiLineString multiLineString) {
        for (int i = 0; i < multiLineString.coordinates().size(); i++) {
            coords.addAll(multiLineString.coordinates().get(i));
        }
        return coords;
    }

    @NonNull
    public static List<Point> coordAll(@NonNull MultiPolygon multiPolygon, @NonNull boolean excludeWrapCoord) {
        return coordAll(new ArrayList(), multiPolygon, excludeWrapCoord);
    }

    @NonNull
    private static List<Point> coordAll(@NonNull List<Point> coords, @NonNull MultiPolygon multiPolygon, @NonNull boolean excludeWrapCoord) {
        int wrapShrink = excludeWrapCoord ? 1 : 0;
        for (int i = 0; i < multiPolygon.coordinates().size(); i++) {
            for (int j = 0; j < multiPolygon.coordinates().get(i).size(); j++) {
                for (int k = 0; k < ((List) multiPolygon.coordinates().get(i).get(j)).size() - wrapShrink; k++) {
                    coords.add(((List) multiPolygon.coordinates().get(i).get(j)).get(k));
                }
            }
        }
        return coords;
    }

    @NonNull
    public static List<Point> coordAll(@NonNull Feature feature, @NonNull boolean excludeWrapCoord) {
        return addCoordAll(new ArrayList(), feature, excludeWrapCoord);
    }

    @NonNull
    public static List<Point> coordAll(@NonNull FeatureCollection featureCollection, @NonNull boolean excludeWrapCoord) {
        List<Point> finalCoordsList = new ArrayList<>();
        for (Feature singleFeature : featureCollection.features()) {
            addCoordAll(finalCoordsList, singleFeature, excludeWrapCoord);
        }
        return finalCoordsList;
    }

    @NonNull
    private static List<Point> addCoordAll(@NonNull List<Point> pointList, @NonNull Feature feature, @NonNull boolean excludeWrapCoord) {
        return coordAllFromSingleGeometry(pointList, feature.geometry(), excludeWrapCoord);
    }

    @NonNull
    private static List<Point> coordAllFromSingleGeometry(@NonNull List<Point> pointList, @NonNull Geometry geometry, @NonNull boolean excludeWrapCoord) {
        if (geometry instanceof Point) {
            pointList.add((Point) geometry);
        } else if (geometry instanceof MultiPoint) {
            pointList.addAll(((MultiPoint) geometry).coordinates());
        } else if (geometry instanceof LineString) {
            pointList.addAll(((LineString) geometry).coordinates());
        } else if (geometry instanceof MultiLineString) {
            coordAll(pointList, (MultiLineString) geometry);
        } else if (geometry instanceof Polygon) {
            coordAll(pointList, (Polygon) geometry, excludeWrapCoord);
        } else if (geometry instanceof MultiPolygon) {
            coordAll(pointList, (MultiPolygon) geometry, excludeWrapCoord);
        } else if (geometry instanceof GeometryCollection) {
            for (Geometry singleGeometry : ((GeometryCollection) geometry).geometries()) {
                coordAllFromSingleGeometry(pointList, singleGeometry, excludeWrapCoord);
            }
        }
        return pointList;
    }

    public static Point getCoord(Feature obj) {
        if (obj.geometry() instanceof Point) {
            return (Point) obj.geometry();
        }
        throw new TurfException("A Feature with a Point geometry is required.");
    }
}
