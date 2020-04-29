package com.mapbox.geojson.utils;

public class GeoJsonUtils {
    private static long MAX_DOUBLE_TO_ROUND = ((long) (9.223372036854776E18d / ROUND_PRECISION));
    private static double ROUND_PRECISION = 1.0E7d;

    public static double trim(double value) {
        return (value > ((double) MAX_DOUBLE_TO_ROUND) || value < ((double) (-MAX_DOUBLE_TO_ROUND))) ? value : ((double) Math.round(ROUND_PRECISION * value)) / ROUND_PRECISION;
    }
}
