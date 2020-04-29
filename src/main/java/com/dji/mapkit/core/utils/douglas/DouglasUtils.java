package com.dji.mapkit.core.utils.douglas;

import com.dji.mapkit.core.models.DJILatLng;
import com.dji.mapkit.core.utils.DJIGpsUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DouglasUtils {
    private static final int DEFAULT_THRESHOL = 50;

    public static List<DJILatLng> compress(List<DJILatLng> points, double threshold) {
        int end = points.size() - 1;
        int size = points.size();
        List<LatLngPoint> originPoints = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            originPoints.add(new LatLngPoint(i, points.get(i)));
        }
        List<LatLngPoint> latLngPoints = compressLine((LatLngPoint[]) originPoints.toArray(new LatLngPoint[size]), new ArrayList<>(), 0, end, threshold);
        latLngPoints.add(originPoints.get(0));
        latLngPoints.add(originPoints.get(size - 1));
        Collections.sort(latLngPoints, new Comparator<LatLngPoint>() {
            /* class com.dji.mapkit.core.utils.douglas.DouglasUtils.AnonymousClass1 */

            public int compare(LatLngPoint o1, LatLngPoint o2) {
                return o1.compareTo(o2);
            }
        });
        List<DJILatLng> latLngs = new ArrayList<>();
        for (LatLngPoint point : latLngPoints) {
            latLngs.add(point.latLng);
        }
        return latLngs;
    }

    public static List<DJILatLng> compress(List<DJILatLng> points) {
        return compress(points, 50.0d);
    }

    private static List<LatLngPoint> compressLine(LatLngPoint[] originalLatLngs, List<LatLngPoint> finalLatLngs, int start, int end, double dmax) {
        if (start < end) {
            double maxDis = 0.0d;
            int currentIndex = 0;
            for (int i = start + 1; i < end; i++) {
                double currentDist = distToSegment(originalLatLngs[start], originalLatLngs[end], originalLatLngs[i]);
                if (currentDist > maxDis) {
                    maxDis = currentDist;
                    currentIndex = i;
                }
            }
            if (maxDis >= dmax) {
                finalLatLngs.add(originalLatLngs[currentIndex]);
                compressLine(originalLatLngs, finalLatLngs, start, currentIndex, dmax);
                compressLine(originalLatLngs, finalLatLngs, currentIndex, end, dmax);
            }
        }
        return finalLatLngs;
    }

    private static double distToSegment(LatLngPoint start, LatLngPoint end, LatLngPoint mid) {
        double a = Math.abs(DJIGpsUtils.distance(start.latLng, end.latLng));
        double b = Math.abs(DJIGpsUtils.distance(start.latLng, mid.latLng));
        double c = Math.abs(DJIGpsUtils.distance(mid.latLng, end.latLng));
        double p = ((a + b) + c) / 2.0d;
        return (2.0d * Math.sqrt(Math.abs((((p - a) * p) * (p - b)) * (p - c)))) / a;
    }
}
