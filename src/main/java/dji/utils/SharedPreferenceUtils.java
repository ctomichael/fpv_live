package dji.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.util.SimpleArrayMap;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class SharedPreferenceUtils {
    private static final SimpleArrayMap<String, SharedPreferenceUtils> SP_UTILS_MAP = new SimpleArrayMap<>();
    private SharedPreferences sp;

    public static SharedPreferenceUtils getInstance() {
        return getInstance(AppUtils.getApp(), "", 0);
    }

    public static SharedPreferenceUtils getInstance(Context context) {
        return getInstance(context, "", 0);
    }

    public static SharedPreferenceUtils getInstance(Context context, int mode) {
        return getInstance(context, "", mode);
    }

    public static SharedPreferenceUtils getInstance(Context context, String spName) {
        return getInstance(context, spName, 0);
    }

    public static SharedPreferenceUtils getInstance(Context context, String spName, int mode) {
        if (hasSpace(spName)) {
            spName = context.getPackageName();
        }
        SharedPreferenceUtils spUtils = SP_UTILS_MAP.get(spName);
        if (spUtils != null) {
            return spUtils;
        }
        SharedPreferenceUtils spUtils2 = new SharedPreferenceUtils(context, spName, mode);
        SP_UTILS_MAP.put(spName, spUtils2);
        return spUtils2;
    }

    private SharedPreferenceUtils(Context context, String spName) {
        this.sp = context.getApplicationContext().getSharedPreferences(spName, 0);
    }

    private SharedPreferenceUtils(Context context, String spName, int mode) {
        this.sp = context.getApplicationContext().getSharedPreferences(spName, mode);
    }

    public void put(@NonNull String key, String value, boolean isCommit) {
        if (isCommit) {
            this.sp.edit().putString(key, value).commit();
        } else {
            this.sp.edit().putString(key, value).apply();
        }
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.utils.SharedPreferenceUtils.put(java.lang.String, java.lang.String, boolean):void
     arg types: [java.lang.String, java.lang.String, int]
     candidates:
      dji.utils.SharedPreferenceUtils.put(java.lang.String, float, boolean):void
      dji.utils.SharedPreferenceUtils.put(java.lang.String, int, boolean):void
      dji.utils.SharedPreferenceUtils.put(java.lang.String, long, boolean):void
      dji.utils.SharedPreferenceUtils.put(java.lang.String, java.util.Set<java.lang.String>, boolean):void
      dji.utils.SharedPreferenceUtils.put(java.lang.String, boolean, boolean):void
      dji.utils.SharedPreferenceUtils.put(java.lang.String, java.lang.String, boolean):void */
    public void put(@NonNull String key, String value) {
        put(key, value, true);
    }

    public String getString(@NonNull String key) {
        return getString(key, "");
    }

    public String getString(@NonNull String key, String defaultValue) {
        return this.sp.getString(key, defaultValue);
    }

    public void put(@NonNull String key, int value, boolean isCommit) {
        if (isCommit) {
            this.sp.edit().putInt(key, value).commit();
        } else {
            this.sp.edit().putInt(key, value).apply();
        }
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.utils.SharedPreferenceUtils.put(java.lang.String, int, boolean):void
     arg types: [java.lang.String, int, int]
     candidates:
      dji.utils.SharedPreferenceUtils.put(java.lang.String, float, boolean):void
      dji.utils.SharedPreferenceUtils.put(java.lang.String, long, boolean):void
      dji.utils.SharedPreferenceUtils.put(java.lang.String, java.lang.String, boolean):void
      dji.utils.SharedPreferenceUtils.put(java.lang.String, java.util.Set<java.lang.String>, boolean):void
      dji.utils.SharedPreferenceUtils.put(java.lang.String, boolean, boolean):void
      dji.utils.SharedPreferenceUtils.put(java.lang.String, int, boolean):void */
    public void put(@NonNull String key, int value) {
        put(key, value, true);
    }

    public int getInt(@NonNull String key) {
        return getInt(key, -1);
    }

    public int getInt(@NonNull String key, int defaultValue) {
        return this.sp.getInt(key, defaultValue);
    }

    public void put(@NonNull String key, long value, boolean isCommit) {
        if (isCommit) {
            this.sp.edit().putLong(key, value).commit();
        } else {
            this.sp.edit().putLong(key, value).apply();
        }
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.utils.SharedPreferenceUtils.put(java.lang.String, long, boolean):void
     arg types: [java.lang.String, long, int]
     candidates:
      dji.utils.SharedPreferenceUtils.put(java.lang.String, float, boolean):void
      dji.utils.SharedPreferenceUtils.put(java.lang.String, int, boolean):void
      dji.utils.SharedPreferenceUtils.put(java.lang.String, java.lang.String, boolean):void
      dji.utils.SharedPreferenceUtils.put(java.lang.String, java.util.Set<java.lang.String>, boolean):void
      dji.utils.SharedPreferenceUtils.put(java.lang.String, boolean, boolean):void
      dji.utils.SharedPreferenceUtils.put(java.lang.String, long, boolean):void */
    public void put(@NonNull String key, long value) {
        put(key, value, true);
    }

    public long getLong(@NonNull String key) {
        return getLong(key, -1);
    }

    public long getLong(@NonNull String key, long defaultValue) {
        return this.sp.getLong(key, defaultValue);
    }

    public void put(@NonNull String key, float value, boolean isCommit) {
        if (isCommit) {
            this.sp.edit().putFloat(key, value).commit();
        } else {
            this.sp.edit().putFloat(key, value).apply();
        }
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.utils.SharedPreferenceUtils.put(java.lang.String, float, boolean):void
     arg types: [java.lang.String, float, int]
     candidates:
      dji.utils.SharedPreferenceUtils.put(java.lang.String, int, boolean):void
      dji.utils.SharedPreferenceUtils.put(java.lang.String, long, boolean):void
      dji.utils.SharedPreferenceUtils.put(java.lang.String, java.lang.String, boolean):void
      dji.utils.SharedPreferenceUtils.put(java.lang.String, java.util.Set<java.lang.String>, boolean):void
      dji.utils.SharedPreferenceUtils.put(java.lang.String, boolean, boolean):void
      dji.utils.SharedPreferenceUtils.put(java.lang.String, float, boolean):void */
    public void put(@NonNull String key, float value) {
        put(key, value, true);
    }

    public float getFloat(@NonNull String key) {
        return getFloat(key, -1.0f);
    }

    public float getFloat(@NonNull String key, float defaultValue) {
        return this.sp.getFloat(key, defaultValue);
    }

    public void put(@NonNull String key, boolean value, boolean isCommit) {
        if (isCommit) {
            this.sp.edit().putBoolean(key, value).commit();
        } else {
            this.sp.edit().putBoolean(key, value).apply();
        }
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.utils.SharedPreferenceUtils.put(java.lang.String, boolean, boolean):void
     arg types: [java.lang.String, boolean, int]
     candidates:
      dji.utils.SharedPreferenceUtils.put(java.lang.String, float, boolean):void
      dji.utils.SharedPreferenceUtils.put(java.lang.String, int, boolean):void
      dji.utils.SharedPreferenceUtils.put(java.lang.String, long, boolean):void
      dji.utils.SharedPreferenceUtils.put(java.lang.String, java.lang.String, boolean):void
      dji.utils.SharedPreferenceUtils.put(java.lang.String, java.util.Set<java.lang.String>, boolean):void
      dji.utils.SharedPreferenceUtils.put(java.lang.String, boolean, boolean):void */
    public void put(@NonNull String key, boolean value) {
        put(key, value, true);
    }

    public boolean getBoolean(@NonNull String key) {
        return getBoolean(key, false);
    }

    public boolean getBoolean(@NonNull String key, boolean defaultValue) {
        return this.sp.getBoolean(key, defaultValue);
    }

    public void put(@NonNull String key, Set<String> value, boolean isCommit) {
        if (isCommit) {
            this.sp.edit().putStringSet(key, value).commit();
        } else {
            this.sp.edit().putStringSet(key, value).apply();
        }
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.utils.SharedPreferenceUtils.put(java.lang.String, java.util.Set<java.lang.String>, boolean):void
     arg types: [java.lang.String, java.util.Set<java.lang.String>, int]
     candidates:
      dji.utils.SharedPreferenceUtils.put(java.lang.String, float, boolean):void
      dji.utils.SharedPreferenceUtils.put(java.lang.String, int, boolean):void
      dji.utils.SharedPreferenceUtils.put(java.lang.String, long, boolean):void
      dji.utils.SharedPreferenceUtils.put(java.lang.String, java.lang.String, boolean):void
      dji.utils.SharedPreferenceUtils.put(java.lang.String, boolean, boolean):void
      dji.utils.SharedPreferenceUtils.put(java.lang.String, java.util.Set<java.lang.String>, boolean):void */
    public void put(@NonNull String key, Set<String> value) {
        put(key, value, true);
    }

    public Set<String> getStringSet(@NonNull String key) {
        return getStringSet(key, Collections.emptySet());
    }

    public Set<String> getStringSet(@NonNull String key, Set<String> defaultValue) {
        return this.sp.getStringSet(key, defaultValue);
    }

    public Map<String, ?> getAll() {
        return this.sp.getAll();
    }

    public boolean contains(@NonNull String key) {
        return this.sp.contains(key);
    }

    public void remove(@NonNull String key, boolean isCommit) {
        if (isCommit) {
            this.sp.edit().remove(key).commit();
        } else {
            this.sp.edit().remove(key).apply();
        }
    }

    public void remove(@NonNull String key) {
        remove(key, true);
    }

    public void clear(boolean isCommit) {
        if (isCommit) {
            this.sp.edit().clear().commit();
        } else {
            this.sp.edit().clear().apply();
        }
    }

    public void clear() {
        clear(true);
    }

    private static boolean hasSpace(String s) {
        if (s == null) {
            return true;
        }
        int len = s.length();
        for (int i = 0; i < len; i++) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
