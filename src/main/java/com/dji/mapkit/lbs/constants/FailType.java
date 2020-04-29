package com.dji.mapkit.lbs.constants;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
public @interface FailType {
    public static final int AMAP_SERVICE_FAIL = 6;
    public static final int DEFAULT_SERVICE_FAIL = 7;
    public static final int GOOGLE_PLAY_SERVICES_CONNECTION_FAIL = 5;
    public static final int GOOGLE_PLAY_SERVICES_NOT_AVAILABLE = 4;
    public static final int GOOGLE_PLAY_SERVICES_SETTINGS_DIALOG = 14;
    public static final int LOST_SERVICES_CONNECTION_FAIL = 9;
    public static final int LOST_SERVICES_FAIL = 10;
    public static final int LOST_SERVICES_SETTINGS_DENIED = 13;
    public static final int LOST_SERVICES_SETTINGS_DIALOG = 11;
    public static final int NETWORK_NOT_AVAILABLE = 3;
    public static final int PERMISSION_DENIED = 2;
    public static final int TIMEOUT = 1;
    public static final int UNKOWN = -1;
    public static final int VIEW_DETACHED = 8;
    public static final int VIEW_NOT_REQUIRED_TYPE = 12;
}
