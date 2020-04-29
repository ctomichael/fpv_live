package com.dji.mapkit.lbs.constants;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
public @interface ProviderType {
    public static final int AMAP_SERVICE = 5;
    public static final int DEFAULT_PROVIDERS = 4;
    public static final int GOOGLE_PLAY_SERVICES = 1;
    public static final int GPS = 2;
    public static final int LOST_SERVICE = 6;
    public static final int NETWORK = 3;
    public static final int NONE = 0;
}
