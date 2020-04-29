package com.mapbox.mapboxsdk.constants;

public class GeometryConstants {
    public static final double LATITUDE_SPAN = 180.0d;
    public static final double LONGITUDE_SPAN = 360.0d;
    public static final double MAX_LATITUDE = 90.0d;
    public static final double MAX_LONGITUDE = Double.MAX_VALUE;
    public static final double MAX_MERCATOR_LATITUDE = 85.05112877980659d;
    public static final double MAX_WRAP_LONGITUDE = 180.0d;
    public static final double MIN_LATITUDE = -90.0d;
    public static final double MIN_LONGITUDE = -1.7976931348623157E308d;
    public static final double MIN_MERCATOR_LATITUDE = -85.05112877980659d;
    public static final double MIN_WRAP_LONGITUDE = -180.0d;
    public static final int RADIUS_EARTH_METERS = 6378137;

    private GeometryConstants() {
    }
}
