package com.mapbox.turf;

import android.support.annotation.NonNull;
import com.mapbox.geojson.Point;
import java.util.List;

public class TurfClassification {
    private TurfClassification() {
    }

    @NonNull
    public static Point nearestPoint(@NonNull Point targetPoint, @NonNull List<Point> points) {
        if (points.isEmpty()) {
            return targetPoint;
        }
        Point nearestPoint = points.get(0);
        double minDist = Double.POSITIVE_INFINITY;
        for (Point point : points) {
            double distanceToPoint = TurfMeasurement.distance(targetPoint, point);
            if (distanceToPoint < minDist) {
                nearestPoint = point;
                minDist = distanceToPoint;
            }
        }
        return nearestPoint;
    }
}
