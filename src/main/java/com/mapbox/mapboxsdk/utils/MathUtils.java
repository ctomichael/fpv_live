package com.mapbox.mapboxsdk.utils;

public class MathUtils {
    public static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    public static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }

    public static double wrap(double value, double min, double max) {
        double delta = max - min;
        return ((((value - min) % delta) + delta) % delta) + min;
    }

    public static double normalize(double x, double dataLow, double dataHigh, double normalizedLow, double normalizedHigh) {
        return (((x - dataLow) / (dataHigh - dataLow)) * (normalizedHigh - normalizedLow)) + normalizedLow;
    }
}
