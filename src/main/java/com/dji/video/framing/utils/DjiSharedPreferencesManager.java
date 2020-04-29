package com.dji.video.framing.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class DjiSharedPreferencesManager {
    public static SharedPreferences getSp(Context ctx) {
        return ctx.getSharedPreferences(ctx.getPackageName(), 0);
    }

    public static boolean putInt(Context ctx, String key, int value) {
        return getSp(ctx).edit().putInt(key, value).commit();
    }

    public static boolean putLong(Context ctx, String key, long value) {
        return getSp(ctx).edit().putLong(key, value).commit();
    }

    public static boolean putFloat(Context ctx, String key, float value) {
        return getSp(ctx).edit().putFloat(key, value).commit();
    }

    public static boolean putBoolean(Context ctx, String key, boolean value) {
        return getSp(ctx).edit().putBoolean(key, value).commit();
    }

    public static boolean putString(Context ctx, String key, String value) {
        return getSp(ctx).edit().putString(key, value).commit();
    }

    public static int getInt(Context ctx, String key, int defValue) {
        return getSp(ctx).getInt(key, defValue);
    }

    public static long getLong(Context ctx, String key, long defValue) {
        return getSp(ctx).getLong(key, defValue);
    }

    public static float getFloat(Context ctx, String key, float defValue) {
        return getSp(ctx).getFloat(key, defValue);
    }

    public static boolean getBoolean(Context ctx, String key, boolean defValue) {
        return getSp(ctx).getBoolean(key, defValue);
    }

    public static String getString(Context ctx, String key, String defValue) {
        return getSp(ctx).getString(key, defValue);
    }

    public static boolean removeString(Context ctx, String key) {
        return getSp(ctx).edit().remove(key).commit();
    }

    public static boolean putDouble(Context ctx, String key, double value) {
        return getSp(ctx).edit().putLong(key, Double.doubleToRawLongBits(value)).commit();
    }

    public static double getDouble(Context ctx, String key, double defaultValue) {
        return Double.longBitsToDouble(getSp(ctx).getLong(key, Double.doubleToLongBits(defaultValue)));
    }
}
