package com.dji.mapkit.lbs.constants;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
public @interface ProcessType {
    public static final int GETTING_LOCATION_FROM_AMAP_SERVICE = 1;
    public static final int GETTING_LOCATION_FROM_GOOGLE_PLAY_SERVICES = 2;
    public static final int GETTING_LOCATION_FROM_GPS_PROVIDER = 3;
    public static final int GETTING_LOCATION_FROM_LOST_SERVICES = 6;
    public static final int GETTING_LOCATION_FROM_MAPBOX_SERVICES = 5;
    public static final int GETTING_LOCATION_FROM_NETWORK_PROVIDER = 4;
}
