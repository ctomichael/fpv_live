package com.dji.mapkit.core;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import com.dji.mapkit.core.exceptions.MapkitInitializerException;
import java.util.HashMap;

public class Mapkit {
    private static final String CLASS_PROVIDER_AMAP = "com.dji.mapkit.amap.provider.AMapProvider";
    private static final String CLASS_PROVIDER_GOOGLE = "com.dji.mapkit.google.provider.GoogleProvider";
    private static final String CLASS_PROVIDER_MAPBOX = "com.dji.mapkit.mapbox.provider.MapboxProvider";
    private static String MAPBOX_ACCESS_TOKEN = null;
    private static final String MAPBOX_TOKEN_KEY = "com.dji.mapkit.mapbox.apikey";
    private static final String TAG = Mapkit.class.getSimpleName();
    private static final HashMap<Integer, String> providerClassName = new HashMap<>();
    private static int sGeocodingProvider = 0;
    private static volatile Boolean sIsInHongKong;
    private static volatile Boolean sIsInMacau;
    private static volatile Boolean sIsInMainlandChina;
    private static int sMapProvider = 0;
    private static int sMapType = 1;

    public @interface MapProviderConstant {
        public static final int AMAP_PROVIDER = 2;
        public static final int GOOGLE_MAP_PROVIDER = 1;
        public static final int HERE_MAP_PROVIDER = 4;
        public static final int INVALID_PROVIDER = 0;
        public static final int MAPBOX_MAP_PROVIDER = 3;
    }

    static {
        providerClassName.put(1, CLASS_PROVIDER_GOOGLE);
        providerClassName.put(3, CLASS_PROVIDER_MAPBOX);
        providerClassName.put(2, CLASS_PROVIDER_AMAP);
    }

    public static void init(Context context) {
        try {
            MAPBOX_ACCESS_TOKEN = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128).metaData.getString(MAPBOX_TOKEN_KEY);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Mapkit() {
    }

    public static void mapboxAccessToken(@NonNull String accessToken) {
        MAPBOX_ACCESS_TOKEN = accessToken;
    }

    public static String getMapboxAccessToken() {
        if (MAPBOX_ACCESS_TOKEN != null) {
            return MAPBOX_ACCESS_TOKEN;
        }
        throw new MapkitInitializerException("Mapbox token is not set yet.");
    }

    public static void inMainlandChina(boolean inMainlandChina) {
        sIsInMainlandChina = Boolean.valueOf(inMainlandChina);
    }

    public static boolean isInMainlandChina() {
        if (sIsInMainlandChina != null) {
            return sIsInMainlandChina.booleanValue();
        }
        throw new MapkitInitializerException("You should set if Mapkit is used in mainland China, so that Mapkit can correct the coordinate offset.");
    }

    public static void inHongKong(boolean inHongKong) {
        sIsInHongKong = Boolean.valueOf(inHongKong);
    }

    public static boolean isInHongKong() {
        if (sIsInHongKong != null) {
            return sIsInHongKong.booleanValue();
        }
        throw new MapkitInitializerException("You should set if Mapkit is used in Hong Kong, so that Mapkit can correct the coordinate offset.");
    }

    public static void inMacau(boolean inMacau) {
        sIsInMacau = Boolean.valueOf(inMacau);
    }

    public static boolean isInMacau() {
        if (sIsInMacau != null) {
            return sIsInMacau.booleanValue();
        }
        throw new MapkitInitializerException("You should set if Mapkit is used in Macau, so that Mapkit can correct the coordinate offset.");
    }

    @MapProviderConstant
    public static int getMapProvider() {
        return sMapProvider;
    }

    public static int getMapType() {
        return sMapType;
    }

    public static int getGeocodingProvider() {
        return sGeocodingProvider;
    }

    public static void mapProvider(@MapProviderConstant int provider) {
        sMapProvider = provider;
    }

    public static void mapType(int mapType) {
        sMapType = mapType;
    }

    public static void geocodingProvider(int provider) {
        sGeocodingProvider = provider;
    }

    public static String getMapProviderClassName(@MapProviderConstant int provider) {
        return providerClassName.get(Integer.valueOf(provider));
    }
}
