package com.dji.mapkit.lbs.configuration;

import com.amap.api.location.AMapLocationClientOption;
import com.dji.permission.Permission;
import com.google.android.gms.location.LocationRequest;

public final class Defaults {
    static final boolean ASK_FOR_AMAP_SERVICE = true;
    static final boolean ASK_FOR_GP_SERVICES = true;
    static final boolean ASK_FOR_SETTINGS_API = true;
    static final String EMPTY_STRING = "";
    static final boolean FAIL_ON_CONNECTION_SUSPENDED = true;
    static final boolean FAIL_ON_SETTINGS_API_SUSPENDED = false;
    static final boolean FALLBACK_TO_AMAP = true;
    static final boolean FALLBACK_TO_DEFAULT = true;
    private static final int GOOGLE_PLAY_LOCATION_PRIORITY = 100;
    static final boolean IGNORE_LAST_KNOWN_LOCATION = false;
    static final boolean KEEP_TRACKING = false;
    static final int LOCATION_DISTANCE_INTERVAL = 0;
    private static final int LOCATION_FASTEST_INTERVAL = 5000;
    static final int LOCATION_INTERVAL = 10000;
    public static final AMapLocationClientOption.AMapLocationMode LOCATION_MODE = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy;
    public static final String[] LOCATION_PERMISSIONS = {Permission.ACCESS_COARSE_LOCATION, Permission.ACCESS_FINE_LOCATION};
    private static final int LOST_LOCATION_PRIORITY = 100;
    public static final int MINUTE = 60000;
    static final float MIN_ACCURACY = 5.0f;
    public static final boolean NEED_ADDRESS = false;
    public static final int SECOND = 1000;
    public static final int SECOND_IN_NANOS = 1000000;
    static final int SUSPENDED_CONNECTION_RETRY_COUNT = 2;
    static final int TIME_PERIOD = 60000;
    static final int WAIT_PERIOD = 20000;

    public static LocationRequest createDefaultLocationRequestForGoogleServices() {
        return LocationRequest.create().setPriority(100).setInterval(10000).setFastestInterval(5000);
    }

    public static com.mapzen.android.lost.api.LocationRequest createDefaultLocationRequestForLostServices() {
        return com.mapzen.android.lost.api.LocationRequest.create().setPriority(100).setInterval(10000).setFastestInterval(5000);
    }

    @Deprecated
    public static AMapLocationClientOption createDefaultAMapLocationClientOption() {
        AMapLocationClientOption clientOption = new AMapLocationClientOption().setNeedAddress(false).setLocationMode(LOCATION_MODE).setInterval(10000);
        clientOption.setGpsFirst(true).setWifiActiveScan(false);
        clientOption.setWifiScan(false);
        return clientOption;
    }

    static AMapLocationClientOption createDefaultAMapLocationClientOption(boolean shouldCloseWifiScan) {
        AMapLocationClientOption clientOption = new AMapLocationClientOption().setNeedAddress(false).setLocationMode(LOCATION_MODE).setInterval(10000);
        if (shouldCloseWifiScan) {
            clientOption.setGpsFirst(true).setWifiActiveScan(false);
            clientOption.setWifiScan(false);
        }
        return clientOption;
    }

    private Defaults() {
    }
}
