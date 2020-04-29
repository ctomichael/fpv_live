package com.amap.location.common;

public class HeaderConfig {
    public static final byte PRODUCT_FLP = 1;
    public static final byte PRODUCT_NLP = 2;
    public static final byte PRODUCT_OPEN_SDK = 4;
    public static final byte PRODUCT_SDK_AMAP = 0;
    public static final byte PRODUCT_SDK_AUTO = 3;
    public static final byte PRODUCT_SDK_MANU = 5;
    public static final byte PRODUCT_UNKNOWN = -1;
    private static volatile String sLicense;
    private static volatile String sMapkey;
    private static volatile String sProcessName;
    private static volatile byte sProductId = -1;
    private static volatile String sProductVerion;
    private static volatile String sVersionCode;
    private static volatile String sVersionName;

    public static String getLicense() {
        return sLicense;
    }

    public static String getMapkey() {
        return sMapkey;
    }

    public static String getProcessName() {
        return sProcessName;
    }

    public static byte getProductId() {
        return sProductId;
    }

    public static String getProductVerion() {
        return sProductVerion;
    }

    public static String getVersionCode() {
        return sVersionCode;
    }

    public static String getVersionName() {
        return sVersionName;
    }

    public static void setLicense(String str) {
        sLicense = str;
    }

    public static void setMapkey(String str) {
        sMapkey = str;
    }

    public static void setProcessName(String str) {
        sProcessName = str;
    }

    public static void setProductId(byte b) {
        sProductId = b;
    }

    public static void setProductVerion(String str) {
        sProductVerion = str;
    }

    public static void setVersionCode(String str) {
        sVersionCode = str;
    }

    public static void setVersionName(String str) {
        sVersionName = str;
    }
}
