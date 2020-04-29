package com.mapbox.geojson.utils;

import android.support.annotation.NonNull;
import com.mapbox.geojson.Point;
import java.util.ArrayList;
import java.util.List;

public final class PolylineUtils {
    private static final boolean SIMPLIFY_DEFAULT_HIGHEST_QUALITY = false;
    private static final double SIMPLIFY_DEFAULT_TOLERANCE = 1.0d;

    private PolylineUtils() {
    }

    @NonNull
    public static List<Point> decode(@NonNull String encodedPath, int precision) {
        int index;
        int len = encodedPath.length();
        double factor = Math.pow(10.0d, (double) precision);
        List<Point> path = new ArrayList<>();
        int index2 = 0;
        int lat = 0;
        int lng = 0;
        while (index2 < len) {
            int result = 1;
            int shift = 0;
            while (true) {
                index = index2 + 1;
                int temp = (encodedPath.charAt(index2) - '?') - 1;
                result += temp << shift;
                shift += 5;
                if (temp < 31) {
                    break;
                }
                index2 = index;
            }
            lat += (result & 1) != 0 ? (result >> 1) ^ -1 : result >> 1;
            int result2 = 1;
            int shift2 = 0;
            while (true) {
                int index3 = index;
                index = index3 + 1;
                int temp2 = (encodedPath.charAt(index3) - '?') - 1;
                result2 += temp2 << shift2;
                shift2 += 5;
                if (temp2 < 31) {
                    break;
                }
            }
            lng += (result2 & 1) != 0 ? (result2 >> 1) ^ -1 : result2 >> 1;
            path.add(Point.fromLngLat(((double) lng) / factor, ((double) lat) / factor));
            index2 = index;
        }
        return path;
    }

    @NonNull
    public static String encode(@NonNull List<Point> path, int precision) {
        long lastLat = 0;
        long lastLng = 0;
        StringBuilder result = new StringBuilder();
        double factor = Math.pow(10.0d, (double) precision);
        for (Point point : path) {
            long lat = Math.round(point.latitude() * factor);
            long lng = Math.round(point.longitude() * factor);
            encode(lat - lastLat, result);
            encode(lng - lastLng, result);
            lastLat = lat;
            lastLng = lng;
        }
        return result.toString();
    }

    private static void encode(long variable, StringBuilder result) {
        long variable2 = variable < 0 ? (variable << 1) ^ -1 : variable << 1;
        while (variable2 >= 32) {
            result.append(Character.toChars((int) (((31 & variable2) | 32) + 63)));
            variable2 >>= 5;
        }
        result.append(Character.toChars((int) (variable2 + 63)));
    }

    @NonNull
    public static List<Point> simplify(@NonNull List<Point> points) {
        return simplify(points, SIMPLIFY_DEFAULT_TOLERANCE, false);
    }

    @NonNull
    public static List<Point> simplify(@NonNull List<Point> points, double tolerance) {
        return simplify(points, tolerance, false);
    }

    @NonNull
    public static List<Point> simplify(@NonNull List<Point> points, boolean highestQuality) {
        return simplify(points, SIMPLIFY_DEFAULT_TOLERANCE, highestQuality);
    }

    @NonNull
    public static List<Point> simplify(@NonNull List<Point> points, double tolerance, boolean highestQuality) {
        if (points.size() <= 2) {
            return points;
        }
        double sqTolerance = tolerance * tolerance;
        if (!highestQuality) {
            points = simplifyRadialDist(points, sqTolerance);
        }
        return simplifyDouglasPeucker(points, sqTolerance);
    }

    private static double getSqDist(Point p1, Point p2) {
        double dx = p1.longitude() - p2.longitude();
        double dy = p1.latitude() - p2.latitude();
        return (dx * dx) + (dy * dy);
    }

    private static double getSqSegDist(Point point, Point p1, Point p2) {
        double horizontal = p1.longitude();
        double vertical = p1.latitude();
        double diffHorizontal = p2.longitude() - horizontal;
        double diffVertical = p2.latitude() - vertical;
        if (!(diffHorizontal == 0.0d && diffVertical == 0.0d)) {
            double total = (((point.longitude() - horizontal) * diffHorizontal) + ((point.latitude() - vertical) * diffVertical)) / ((diffHorizontal * diffHorizontal) + (diffVertical * diffVertical));
            if (total > SIMPLIFY_DEFAULT_TOLERANCE) {
                horizontal = p2.longitude();
                vertical = p2.latitude();
            } else if (total > 0.0d) {
                horizontal += diffHorizontal * total;
                vertical += diffVertical * total;
            }
        }
        double diffHorizontal2 = point.longitude() - horizontal;
        double diffVertical2 = point.latitude() - vertical;
        return (diffHorizontal2 * diffHorizontal2) + (diffVertical2 * diffVertical2);
    }

    private static List<Point> simplifyRadialDist(List<Point> points, double sqTolerance) {
        Point prevPoint = points.get(0);
        ArrayList<Point> newPoints = new ArrayList<>();
        newPoints.add(prevPoint);
        Point point = null;
        int len = points.size();
        for (int i = 1; i < len; i++) {
            point = points.get(i);
            if (getSqDist(point, prevPoint) > sqTolerance) {
                newPoints.add(point);
                prevPoint = point;
            }
        }
        if (!prevPoint.equals(point)) {
            newPoints.add(point);
        }
        return newPoints;
    }

    private static List<Point> simplifyDpStep(List<Point> points, int first, int last, double sqTolerance, List<Point> simplified) {
        double maxSqDist = sqTolerance;
        int index = 0;
        ArrayList<Point> stepList = new ArrayList<>();
        for (int i = first + 1; i < last; i++) {
            double sqDist = getSqSegDist(points.get(i), points.get(first), points.get(last));
            if (sqDist > maxSqDist) {
                index = i;
                maxSqDist = sqDist;
            }
        }
        if (maxSqDist > sqTolerance) {
            if (index - first > 1) {
                stepList.addAll(simplifyDpStep(points, first, index, sqTolerance, simplified));
            }
            stepList.add(points.get(index));
            if (last - index > 1) {
                stepList.addAll(simplifyDpStep(points, index, last, sqTolerance, simplified));
            }
        }
        return stepList;
    }

    private static List<Point> simplifyDouglasPeucker(List<Point> points, double sqTolerance) {
        int last = points.size() - 1;
        ArrayList<Point> simplified = new ArrayList<>();
        simplified.add(points.get(0));
        simplified.addAll(simplifyDpStep(points, 0, last, sqTolerance, simplified));
        simplified.add(points.get(last));
        return simplified;
    }
}
