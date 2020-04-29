package dji.pilot.publics.util;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class DJIUnitUtil {
    public static final String DjiFormat = "DjiFormat";
    public static final float LENGTH_METRIC2IMPERIAL = 3.28f;
    public static final float LENGTH_METRIC2INCH = 39.4f;
    public static final float SPEED_METRIC2IMPERIAL = 2.237f;
    public static final float SPEED_MS_TO_KMH = 3.6f;
    public static final float TEMPERATURE_K2C = 273.15f;
    public static final String UNIT_IMPERIAL_DIS_STR = "ft";
    public static final String UNIT_IMPERIAL_SPEED_STR = "mph";
    public static final String UNIT_KILO_METRIC_DIS_STR = "km";
    public static final String UNIT_METRIC_DIS_STR = "m";
    public static final String UNIT_METRIC_SPEED_KM_STR = "km/h";
    public static final String UNIT_METRIC_SPEED_STR = "m/s";

    public static final float fromMeterPerSecondToKiloMeterPerHour(float speed) {
        return 3.6f * speed;
    }

    public static final float kelvinToCelsius(float kelvin) {
        return kelvin - 273.15f;
    }

    public static final float celsiusToKelvin(float celsius) {
        return 273.15f + celsius;
    }

    public static final float celsiusToFahrenheit(float celsius) {
        return (1.8f * celsius) + 32.0f;
    }

    public static final float fahrenheitToCelsius(float fahrenheit) {
        return (fahrenheit - 32.0f) / 1.8f;
    }

    public static float metricToImperialByLength(float value) {
        return 3.28f * value;
    }

    public static float imperialToMetricByLength(float value) {
        return value / 3.28f;
    }

    public static float metricToImperialBySpeed(float value) {
        return 2.237f * value;
    }

    public static float imperialToMetricBySpeed(float value) {
        return value / 2.237f;
    }

    public static float getValueFromMetricByLength(float value) {
        if (!isMetric()) {
            return metricToImperialByLength(value);
        }
        return value;
    }

    public static float getValueFromImperialByLength(float value) {
        if (isMetric()) {
            return imperialToMetricByLength(value);
        }
        return value;
    }

    public static float getValueToMetricByLength(float value) {
        if (!isMetric()) {
            return imperialToMetricByLength(value);
        }
        return value;
    }

    public static float getValueToImperialByLength(float value) {
        if (isMetric()) {
            return metricToImperialByLength(value);
        }
        return value;
    }

    public static String getUintStrByLength() {
        return isMetric() ? "m" : "ft";
    }

    public static String getUintStrBySpeed() {
        return isMetric() ? "m/s" : "mile/h";
    }

    public static boolean isMetric() {
        return true;
    }
}
