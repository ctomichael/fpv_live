package dji.internal;

import android.content.Context;
import android.content.SharedPreferences;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class DJIAnalyticsSharedPrefs {
    private static final boolean BOOLEAN_DEFAULT_VALUE = false;
    private static final int INTEGER_DEFAULT_VALUE = 0;
    private static final long LONG_DEFAULT_VALUE = 0;
    private static final String PREFERENCE_FILE_KEY = "dji.internal.analytics";
    private static final String STRING_DEFAULT_VALUE = "";
    private static SharedPreferences.Editor sEditor;
    private static SharedPreferences sSharedPref;

    public static void init(Context context) {
        if (context != null) {
            sSharedPref = context.getSharedPreferences(PREFERENCE_FILE_KEY, 0);
        }
    }

    public static String getStringPref(String prefId) {
        if (sSharedPref != null) {
            return sSharedPref.getString(prefId, "");
        }
        return "";
    }

    public static void setStringPref(String prefId, String stringValue) {
        if (sSharedPref != null) {
            sEditor = sSharedPref.edit();
            sEditor.putString(prefId, stringValue);
            sEditor.apply();
        }
    }

    public static void setIntegerPref(String prefId, int intValue) {
        if (sSharedPref != null) {
            sEditor = sSharedPref.edit();
            sEditor.putInt(prefId, intValue);
            sEditor.apply();
        }
    }

    public static void setLongPref(String prefId, long longValue) {
        if (sSharedPref != null) {
            sEditor = sSharedPref.edit();
            sEditor.putLong(prefId, longValue);
            sEditor.apply();
        }
    }

    public static int getIntegerPref(String prefId) {
        if (sSharedPref != null) {
            return sSharedPref.getInt(prefId, 0);
        }
        return 0;
    }

    public static long getLongPref(String prefId) {
        if (sSharedPref != null) {
            return sSharedPref.getLong(prefId, 0);
        }
        return 0;
    }

    public static void setBooleanPref(String prefId, boolean booleanValue) {
        if (sSharedPref != null) {
            sEditor = sSharedPref.edit();
            sEditor.putBoolean(prefId, booleanValue);
            sEditor.apply();
        }
    }

    public static boolean getBooleanPref(String prefId) {
        if (sSharedPref != null) {
            return sSharedPref.getBoolean(prefId, false);
        }
        return false;
    }

    public static boolean containsKey(String prefId) {
        if (sSharedPref != null) {
            return sSharedPref.contains(prefId);
        }
        return false;
    }
}
