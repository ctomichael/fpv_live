package dji.logic.utils;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class DJILocationUtils {
    public static boolean isAvailable(double latitude, double longitude) {
        return Math.abs(latitude) > 1.0E-8d && Math.abs(longitude) > 1.0E-8d && Math.abs(latitude) <= 90.0d && Math.abs(longitude) <= 180.0d;
    }
}
