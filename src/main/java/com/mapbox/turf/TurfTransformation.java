package com.mapbox.turf;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.Polygon;
import java.util.ArrayList;
import java.util.List;

public final class TurfTransformation {
    private static final int DEFAULT_STEPS = 64;

    private TurfTransformation() {
    }

    public static Polygon circle(@NonNull Point center, double radius) {
        return circle(center, radius, 64, "kilometers");
    }

    public static Polygon circle(@NonNull Point center, double radius, String units) {
        return circle(center, radius, 64, units);
    }

    public static Polygon circle(@NonNull Point center, double radius, @IntRange(from = 1) int steps, String units) {
        List<Point> coordinates = new ArrayList<>();
        for (int i = 0; i < steps; i++) {
            coordinates.add(TurfMeasurement.destination(center, radius, (((double) i) * 360.0d) / ((double) steps), units));
        }
        if (coordinates.size() > 0) {
            coordinates.add(coordinates.get(0));
        }
        List<List<Point>> coordinate = new ArrayList<>();
        coordinate.add(coordinates);
        return Polygon.fromLngLats(coordinate);
    }
}
