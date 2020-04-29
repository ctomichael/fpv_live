package com.mapbox.mapboxsdk.location.modes;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class CameraMode {
    public static final int NONE = 8;
    public static final int NONE_COMPASS = 16;
    public static final int NONE_GPS = 22;
    public static final int TRACKING = 24;
    public static final int TRACKING_COMPASS = 32;
    public static final int TRACKING_GPS = 34;
    public static final int TRACKING_GPS_NORTH = 36;

    @Retention(RetentionPolicy.SOURCE)
    public @interface Mode {
    }

    private CameraMode() {
    }
}
