package dji.midware.util;

import android.content.Context;
import com.dji.component.persistence.DJIPersistenceStorage;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class DjiSharedPreferencesManager {
    public static boolean putInt(Context ctx, String key, int value) {
        DJIPersistenceStorage.putInt(key, value);
        return true;
    }

    public static boolean putLong(Context ctx, String key, long value) {
        DJIPersistenceStorage.putLong(key, value);
        return true;
    }

    public static boolean putFloat(Context ctx, String key, float value) {
        DJIPersistenceStorage.putFloat(key, value);
        return true;
    }

    public static boolean putDouble(Context ctx, String key, double value) {
        DJIPersistenceStorage.putDouble(key, value);
        return true;
    }

    public static boolean putBoolean(Context ctx, String key, boolean value) {
        DJIPersistenceStorage.putBoolean(key, value);
        return true;
    }

    public static boolean putString(Context ctx, String key, String value) {
        DJIPersistenceStorage.putString(key, value);
        return true;
    }

    public static int getInt(Context ctx, String key, int defValue) {
        return DJIPersistenceStorage.getInt(key, defValue);
    }

    public static long getLong(Context ctx, String key, long defValue) {
        return DJIPersistenceStorage.getLong(key, defValue);
    }

    public static float getFloat(Context ctx, String key, float defValue) {
        return DJIPersistenceStorage.getFloat(key, defValue);
    }

    public static boolean getBoolean(Context ctx, String key, boolean defValue) {
        return DJIPersistenceStorage.getBoolean(key, defValue);
    }

    public static String getString(Context ctx, String key, String defValue) {
        return DJIPersistenceStorage.getString(key, defValue);
    }

    public static double getDouble(Context ctx, String key, double defValue) {
        return DJIPersistenceStorage.getDouble(key, defValue);
    }

    public static boolean removeString(Context ctx, String key) {
        DJIPersistenceStorage.removeValueForKey(key);
        return true;
    }

    public static boolean removeValue(Context ctx, String key) {
        DJIPersistenceStorage.removeValueForKey(key);
        return true;
    }
}
