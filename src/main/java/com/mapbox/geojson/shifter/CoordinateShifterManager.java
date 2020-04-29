package com.mapbox.geojson.shifter;

import com.mapbox.geojson.Point;
import java.util.Arrays;
import java.util.List;

public final class CoordinateShifterManager {
    private static final CoordinateShifter DEFAULT = new CoordinateShifter() {
        /* class com.mapbox.geojson.shifter.CoordinateShifterManager.AnonymousClass1 */

        public List<Double> shiftLonLat(double lon, double lat) {
            return Arrays.asList(Double.valueOf(lon), Double.valueOf(lat));
        }

        public List<Double> shiftLonLatAlt(double lon, double lat, double alt) {
            if (Double.isNaN(alt)) {
                return Arrays.asList(Double.valueOf(lon), Double.valueOf(lat));
            }
            return Arrays.asList(Double.valueOf(lon), Double.valueOf(lat), Double.valueOf(alt));
        }

        public List<Double> unshiftPoint(Point point) {
            return point.coordinates();
        }

        public List<Double> unshiftPoint(List<Double> coordinates) {
            return coordinates;
        }
    };
    private static volatile CoordinateShifter coordinateShifter = DEFAULT;

    public static CoordinateShifter getCoordinateShifter() {
        return coordinateShifter;
    }

    public static void setCoordinateShifter(CoordinateShifter coordinateShifter2) {
        if (coordinateShifter2 == null) {
            coordinateShifter2 = DEFAULT;
        }
        coordinateShifter = coordinateShifter2;
    }

    public static boolean isUsingDefaultShifter() {
        return coordinateShifter == DEFAULT;
    }
}
