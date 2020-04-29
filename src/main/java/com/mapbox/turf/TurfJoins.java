package com.mapbox.turf;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.MultiPolygon;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.Polygon;
import java.util.ArrayList;
import java.util.List;

public final class TurfJoins {
    private TurfJoins() {
    }

    public static boolean inside(Point point, Polygon polygon) {
        List<List<Point>> coordinates = polygon.coordinates();
        List<List<List<Point>>> multiCoordinates = new ArrayList<>();
        multiCoordinates.add(coordinates);
        return inside(point, MultiPolygon.fromLngLats(multiCoordinates));
    }

    public static boolean inside(Point point, MultiPolygon multiPolygon) {
        List<List<List<Point>>> polys = multiPolygon.coordinates();
        boolean insidePoly = false;
        for (int i = 0; i < polys.size() && !insidePoly; i++) {
            if (inRing(point, (List) polys.get(i).get(0))) {
                boolean inHole = false;
                for (int temp = 1; temp < polys.get(i).size() && !inHole; temp++) {
                    if (inRing(point, (List) polys.get(i).get(temp))) {
                        inHole = true;
                    }
                }
                if (!inHole) {
                    insidePoly = true;
                }
            }
        }
        return insidePoly;
    }

    public static FeatureCollection pointsWithinPolygon(FeatureCollection points, FeatureCollection polygons) {
        ArrayList<Feature> features = new ArrayList<>();
        for (int i = 0; i < polygons.features().size(); i++) {
            for (int j = 0; j < points.features().size(); j++) {
                Point point = (Point) points.features().get(j).geometry();
                if (inside(point, (Polygon) polygons.features().get(i).geometry())) {
                    features.add(Feature.fromGeometry(point));
                }
            }
        }
        return FeatureCollection.fromFeatures(features);
    }

    private static boolean inRing(Point pt, List<Point> ring) {
        boolean isInside = false;
        int j = ring.size() - 1;
        for (int i = 0; i < ring.size(); i++) {
            double xi = ring.get(i).longitude();
            double yi = ring.get(i).latitude();
            double xj = ring.get(j).longitude();
            double yj = ring.get(j).latitude();
            if (((yi > pt.latitude() ? 1 : (yi == pt.latitude() ? 0 : -1)) > 0) != ((yj > pt.latitude() ? 1 : (yj == pt.latitude() ? 0 : -1)) > 0) && pt.longitude() < (((xj - xi) * (pt.latitude() - yi)) / (yj - yi)) + xi) {
                if (!isInside) {
                    isInside = true;
                } else {
                    isInside = false;
                }
            }
            j = i;
        }
        return isInside;
    }
}
