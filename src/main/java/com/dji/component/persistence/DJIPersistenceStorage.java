package com.dji.component.persistence;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.tencent.mmkv.MMKV;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DJIPersistenceStorage {
    private static final String DEFAULT_ID = "default";
    private static Handler sCallbackHandler;
    private static Lock sLock;
    private static ConcurrentHashMap<String, ConcurrentHashMap<String, CopyOnWriteArrayList<DJIPersistenceDataListener>>> sMap;

    public static void initialize(@NonNull Context context) {
        MMKV.initialize(context);
        sMap = new ConcurrentHashMap<>();
        sLock = new ReentrantLock();
        sCallbackHandler = new Handler(Looper.getMainLooper());
    }

    public static boolean containKey(@NonNull String key) {
        return MMKV.defaultMMKV().containsKey(key);
    }

    public static boolean containKey(@NonNull String id, @NonNull String key) {
        return MMKV.mmkvWithID(id).containsKey(key);
    }

    public static void putBoolean(@NonNull String key, boolean value) {
        MMKV.defaultMMKV().encode(key, value);
        notifyDataUpdate(key);
    }

    public static void putBooleanTo(@NonNull String id, @NonNull String key, boolean value) {
        MMKV.mmkvWithID(id).encode(key, value);
        notifyDataUpdate(id, key);
    }

    public static boolean getBoolean(@NonNull String key) {
        return MMKV.defaultMMKV().decodeBool(key);
    }

    public static boolean getBoolean(@NonNull String key, boolean defaultValue) {
        return MMKV.defaultMMKV().decodeBool(key, defaultValue);
    }

    public static boolean getBooleanFrom(@NonNull String id, @NonNull String key) {
        return MMKV.mmkvWithID(id).decodeBool(key);
    }

    public static boolean getBooleanFrom(@NonNull String id, @NonNull String key, boolean defaultValue) {
        return MMKV.mmkvWithID(id).decodeBool(key, defaultValue);
    }

    public static void putInt(@NonNull String key, int value) {
        MMKV.defaultMMKV().encode(key, value);
        notifyDataUpdate(key);
    }

    public static void putIntTo(@NonNull String id, @NonNull String key, int value) {
        MMKV.mmkvWithID(id).encode(key, value);
        notifyDataUpdate(id, key);
    }

    public static int getInt(@NonNull String key) {
        return MMKV.defaultMMKV().decodeInt(key);
    }

    public static int getInt(@NonNull String key, int defaultValue) {
        return MMKV.defaultMMKV().decodeInt(key, defaultValue);
    }

    public static int getIntFrom(@NonNull String id, @NonNull String key) {
        return MMKV.mmkvWithID(id).decodeInt(key);
    }

    public static int getIntFrom(@NonNull String id, @NonNull String key, int defaultValue) {
        return MMKV.mmkvWithID(id).decodeInt(key, defaultValue);
    }

    public static void putLong(@NonNull String key, long value) {
        MMKV.defaultMMKV().encode(key, value);
        notifyDataUpdate(key);
    }

    public static void putLongTo(@NonNull String key, long value, @NonNull String id) {
        MMKV.mmkvWithID(id).encode(key, value);
        notifyDataUpdate(id, key);
    }

    public static long getLong(@NonNull String key) {
        return MMKV.defaultMMKV().decodeLong(key);
    }

    public static long getLong(@NonNull String key, long defaultValue) {
        return MMKV.defaultMMKV().decodeLong(key, defaultValue);
    }

    public static long getLongFrom(@NonNull String id, @NonNull String key) {
        return MMKV.mmkvWithID(id).decodeLong(key);
    }

    public static long getLongFrom(@NonNull String id, @NonNull String key, long defaultValue) {
        return MMKV.mmkvWithID(id).decodeLong(key, defaultValue);
    }

    public static void putFloat(@NonNull String key, float value) {
        MMKV.defaultMMKV().encode(key, value);
        notifyDataUpdate(key);
    }

    public static void putFloatTo(@NonNull String key, float value, @NonNull String id) {
        MMKV.mmkvWithID(id).encode(key, value);
        notifyDataUpdate(id, key);
    }

    public static float getFloat(@NonNull String key) {
        return MMKV.defaultMMKV().decodeFloat(key);
    }

    public static float getFloat(@NonNull String key, float defaultValue) {
        return MMKV.defaultMMKV().decodeFloat(key, defaultValue);
    }

    public static float getFloatFrom(@NonNull String id, @NonNull String key) {
        return MMKV.mmkvWithID(id).decodeFloat(key);
    }

    public static float getFloatFrom(@NonNull String id, @NonNull String key, long defaultValue) {
        return MMKV.mmkvWithID(id).decodeFloat(key, (float) defaultValue);
    }

    public static void putDouble(@NonNull String key, double value) {
        MMKV.defaultMMKV().encode(key, value);
        notifyDataUpdate(key);
    }

    public static void putDoubleTo(@NonNull String key, double value, @NonNull String id) {
        MMKV.mmkvWithID(id).encode(key, value);
        notifyDataUpdate(id, key);
    }

    public static double getDouble(@NonNull String key) {
        return MMKV.defaultMMKV().decodeDouble(key);
    }

    public static double getDouble(@NonNull String key, double defaultValue) {
        return MMKV.defaultMMKV().decodeDouble(key, defaultValue);
    }

    public static double getDoubleFrom(@NonNull String id, @NonNull String key) {
        return MMKV.mmkvWithID(id).decodeDouble(key);
    }

    public static double getDoubleFrom(@NonNull String id, @NonNull String key, double defaultValue) {
        return MMKV.mmkvWithID(id).decodeDouble(key, defaultValue);
    }

    public static void putString(@NonNull String key, @NonNull String value) {
        MMKV.defaultMMKV().encode(key, value);
        notifyDataUpdate(key);
    }

    public static void putStringTo(@NonNull String id, @NonNull String key, @NonNull String value) {
        MMKV.mmkvWithID(id).encode(key, value);
        notifyDataUpdate(id, key);
    }

    @Nullable
    public static String getString(@NonNull String key) {
        return MMKV.defaultMMKV().decodeString(key);
    }

    @NonNull
    public static String getString(@NonNull String key, @NonNull String defaultValue) {
        return MMKV.defaultMMKV().decodeString(key, defaultValue);
    }

    @Nullable
    public static String getStringFrom(@NonNull String id, @NonNull String key) {
        return MMKV.mmkvWithID(id).decodeString(key);
    }

    @NonNull
    public static String getStringFrom(@NonNull String id, @NonNull String key, @NonNull String defaultValue) {
        return MMKV.mmkvWithID(id).decodeString(key, defaultValue);
    }

    public static void putByteArray(@NonNull String key, @NonNull byte[] value) {
        MMKV.defaultMMKV().encode(key, value);
        notifyDataUpdate(key);
    }

    public static void putByteArrayTo(@NonNull String id, @NonNull String key, @NonNull byte[] value) {
        MMKV.mmkvWithID(id).encode(key, value);
        notifyDataUpdate(id, key);
    }

    @Nullable
    public static byte[] getByteArray(@NonNull String key) {
        return MMKV.defaultMMKV().decodeBytes(key);
    }

    @Nullable
    public static byte[] getByteArrayFrom(@NonNull String id, @NonNull String key) {
        return MMKV.mmkvWithID(id).decodeBytes(key);
    }

    public static void putParcelable(@NonNull String key, @NonNull Parcelable value) {
        MMKV.defaultMMKV().encode(key, value);
        notifyDataUpdate(key);
    }

    public static void putParcelableTo(@NonNull String id, @NonNull String key, @NonNull Parcelable value) {
        MMKV.mmkvWithID(id).encode(key, value);
        notifyDataUpdate(id, key);
    }

    @Nullable
    public static <T extends Parcelable> T getParacelable(@NonNull String key, @NonNull Class<T> tClass) {
        return MMKV.defaultMMKV().decodeParcelable(key, tClass);
    }

    @NonNull
    public static <T extends Parcelable> T getParacelable(@NonNull String key, @NonNull Class<T> tClass, @NonNull T defaultValue) {
        return MMKV.defaultMMKV().decodeParcelable(key, tClass, defaultValue);
    }

    @Nullable
    public static <T extends Parcelable> T getParcelableFrom(@NonNull String id, @NonNull String key, @NonNull Class<T> tClass) {
        return MMKV.mmkvWithID(id).decodeParcelable(key, tClass);
    }

    @NonNull
    public static <T extends Parcelable> T getParcelanleFrom(@NonNull String id, @NonNull String key, @NonNull Class<T> tClass, @NonNull T defaultValue) {
        return MMKV.mmkvWithID(id).decodeParcelable(key, tClass, defaultValue);
    }

    public static void putStringSet(@NonNull String key, @NonNull Set<String> value) {
        MMKV.defaultMMKV().encode(key, value);
        notifyDataUpdate(key);
    }

    public static void putStringSet(@NonNull String id, @NonNull String key, @NonNull Set<String> value) {
        MMKV.mmkvWithID(id).encode(key, value);
        notifyDataUpdate(id, key);
    }

    @Nullable
    public static Set<String> getStringSet(@NonNull String key) {
        return MMKV.defaultMMKV().decodeStringSet(key);
    }

    @NonNull
    public static Set<String> getStringSet(@NonNull String key, @NonNull Set<String> defaultValue) {
        return MMKV.defaultMMKV().decodeStringSet(key, defaultValue);
    }

    @Nullable
    public static Set<String> getStringSetFrom(@NonNull String id, @NonNull String key) {
        return MMKV.mmkvWithID(id).decodeStringSet(key);
    }

    @NonNull
    public static Set<String> getStringSetFrom(@NonNull String id, @NonNull String key, Set<String> defaultValue) {
        return MMKV.mmkvWithID(id).decodeStringSet(key, defaultValue);
    }

    public static void removeValueForKey(@NonNull String key) {
        MMKV.defaultMMKV().removeValueForKey(key);
        notifyDataUpdate(key);
    }

    public static void removeValueForKeyFrom(@NonNull String id, @NonNull String key) {
        MMKV.mmkvWithID(id).remove(key);
        notifyDataUpdate(id, key);
    }

    public static void removeValuesForKeys(@NonNull String... keys) {
        MMKV.defaultMMKV().removeValuesForKeys(keys);
        for (String key : keys) {
            notifyDataUpdate(key);
        }
    }

    public static void removeValuesForKeysFrom(@NonNull String id, @NonNull String... keys) {
        MMKV.mmkvWithID(id).removeValuesForKeys(keys);
        for (String key : keys) {
            notifyDataUpdate(id, key);
        }
    }

    public static void addListener(@NonNull DJIPersistenceDataListener listener, @NonNull String... keys) {
        addListener(DEFAULT_ID, listener, keys);
    }

    public static void addListener(@NonNull String id, @NonNull DJIPersistenceDataListener listener, @NonNull String... keys) {
        sLock.lock();
        try {
            ConcurrentHashMap<String, CopyOnWriteArrayList<DJIPersistenceDataListener>> keyListenerMap = sMap.get(id);
            if (keyListenerMap == null) {
                keyListenerMap = new ConcurrentHashMap<>();
                sMap.put(DEFAULT_ID, keyListenerMap);
            }
            for (String key : keys) {
                CopyOnWriteArrayList<DJIPersistenceDataListener> listeners = keyListenerMap.get(key);
                if (listeners == null) {
                    listeners = new CopyOnWriteArrayList<>();
                    keyListenerMap.put(key, listeners);
                }
                listeners.add(listener);
            }
        } finally {
            sLock.unlock();
        }
    }

    public static void removeListener(@NonNull DJIPersistenceDataListener listener) {
        removeListener(DEFAULT_ID, listener);
    }

    public static void removeListener(@NonNull String id, @NonNull DJIPersistenceDataListener listener) {
        sLock.lock();
        try {
            ConcurrentHashMap<String, CopyOnWriteArrayList<DJIPersistenceDataListener>> keyListenerMap = sMap.get(id);
            if (keyListenerMap != null) {
                for (String key : keyListenerMap.keySet()) {
                    CopyOnWriteArrayList<DJIPersistenceDataListener> listeners = keyListenerMap.get(key);
                    if (listeners != null) {
                        listeners.remove(listener);
                    }
                }
            }
        } finally {
            sLock.unlock();
        }
    }

    public static void removeListener(@NonNull DJIPersistenceDataListener listener, @NonNull String... keys) {
        removeListener(DEFAULT_ID, listener, keys);
    }

    public static void removeListener(@NonNull String id, @NonNull DJIPersistenceDataListener listener, @NonNull String... keys) {
        sLock.lock();
        try {
            ConcurrentHashMap<String, CopyOnWriteArrayList<DJIPersistenceDataListener>> keyListenerMap = sMap.get(id);
            if (keyListenerMap != null) {
                for (String key : keys) {
                    CopyOnWriteArrayList<DJIPersistenceDataListener> listeners = keyListenerMap.get(key);
                    if (listeners != null) {
                        listeners.remove(listener);
                    }
                }
            }
        } finally {
            sLock.unlock();
        }
    }

    private static void notifyDataUpdate(@NonNull String key) {
        notifyDataUpdate(DEFAULT_ID, key);
    }

    private static void notifyDataUpdate(@NonNull String id, String key) {
        CopyOnWriteArrayList<DJIPersistenceDataListener> listeners;
        sLock.lock();
        try {
            ConcurrentHashMap<String, CopyOnWriteArrayList<DJIPersistenceDataListener>> keyListenerMap = sMap.get(id);
            if (!(keyListenerMap == null || (listeners = keyListenerMap.get(key)) == null)) {
                Iterator it2 = listeners.iterator();
                while (it2.hasNext()) {
                    sCallbackHandler.post(new DJIPersistenceStorage$$Lambda$0((DJIPersistenceDataListener) it2.next(), id, key));
                }
            }
        } finally {
            sLock.unlock();
        }
    }
}
