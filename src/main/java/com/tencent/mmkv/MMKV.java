package com.tencent.mmkv;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Process;
import android.util.Log;
import androidx.annotation.Nullable;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MMKV implements SharedPreferences, SharedPreferences.Editor {
    private static final int ASHMEM_MODE = 8;
    private static final int CONTEXT_MODE_MULTI_PROCESS = 4;
    public static final int MULTI_PROCESS_MODE = 2;
    public static final int SINGLE_PROCESS_MODE = 1;
    private static MMKVHandler gCallbackHandler;
    private static MMKVContentChangeNotification gContentChangeNotify;
    private static boolean gWantLogReDirecting = false;
    private static MMKVLogLevel[] index2LogLevel = {MMKVLogLevel.LevelDebug, MMKVLogLevel.LevelInfo, MMKVLogLevel.LevelWarning, MMKVLogLevel.LevelError, MMKVLogLevel.LevelNone};
    private static EnumMap<MMKVLogLevel, Integer> logLevel2Index = new EnumMap<>(MMKVLogLevel.class);
    private static final HashMap<String, Parcelable.Creator<?>> mCreators = new HashMap<>();
    private static EnumMap<MMKVRecoverStrategic, Integer> recoverIndex = new EnumMap<>(MMKVRecoverStrategic.class);
    private static String rootDir = null;
    private long nativeHandle;

    public interface LibLoader {
        void loadLibrary(String str);
    }

    private native boolean containsKey(long j, String str);

    private native long count(long j);

    private static native long createNB(int i);

    private native boolean decodeBool(long j, String str, boolean z);

    private native byte[] decodeBytes(long j, String str);

    private native double decodeDouble(long j, String str, double d);

    private native float decodeFloat(long j, String str, float f);

    private native int decodeInt(long j, String str, int i);

    private native long decodeLong(long j, String str, long j2);

    private native String decodeString(long j, String str, String str2);

    private native String[] decodeStringSet(long j, String str);

    private static native void destroyNB(long j, int i);

    private native boolean encodeBool(long j, String str, boolean z);

    private native boolean encodeBytes(long j, String str, byte[] bArr);

    private native boolean encodeDouble(long j, String str, double d);

    private native boolean encodeFloat(long j, String str, float f);

    private native boolean encodeInt(long j, String str, int i);

    private native boolean encodeLong(long j, String str, long j2);

    private native boolean encodeSet(long j, String str, String[] strArr);

    private native boolean encodeString(long j, String str, String str2);

    private static native long getDefaultMMKV(int i, String str);

    private static native long getMMKVWithAshmemFD(String str, int i, int i2, String str2);

    private static native long getMMKVWithID(String str, int i, String str2, String str3);

    private static native long getMMKVWithIDAndSize(String str, int i, int i2, String str2);

    public static native boolean isFileValid(String str);

    private static native void jniInitialize(String str, int i);

    public static native void onExit();

    public static native int pageSize();

    private native void removeValueForKey(long j, String str);

    private static native void setLogLevel(int i);

    private static native void setLogReDirecting(boolean z);

    private static native void setWantsContentChangeNotify(boolean z);

    private native void sync(boolean z);

    private native long totalSize(long j);

    private native int valueSize(long j, String str, boolean z);

    private native int writeValueToNB(long j, String str, long j2, int i);

    public native String[] allKeys();

    public native int ashmemFD();

    public native int ashmemMetaFD();

    public native void checkContentChangedByOuterProcess();

    public native void checkReSetCryptKey(String str);

    public native void clearAll();

    public native void clearMemoryCache();

    public native void close();

    public native String cryptKey();

    public native void lock();

    public native String mmapID();

    public native boolean reKey(String str);

    public native void removeValuesForKeys(String[] strArr);

    public native void trim();

    public native boolean tryLock();

    public native void unlock();

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{java.util.EnumMap.put(com.tencent.mmkv.MMKVRecoverStrategic, java.lang.Integer):V}
     arg types: [com.tencent.mmkv.MMKVRecoverStrategic, int]
     candidates:
      ClspMth{java.util.EnumMap.put(java.lang.Object, java.lang.Object):java.lang.Object}
      ClspMth{java.util.AbstractMap.put(com.tencent.mmkv.MMKVRecoverStrategic, java.lang.Integer):V}
      ClspMth{java.util.Map.put(com.tencent.mmkv.MMKVRecoverStrategic, java.lang.Integer):V}
      ClspMth{java.util.EnumMap.put(com.tencent.mmkv.MMKVRecoverStrategic, java.lang.Integer):V} */
    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{java.util.EnumMap.put(com.tencent.mmkv.MMKVRecoverStrategic, java.lang.Integer):V}
     arg types: [com.tencent.mmkv.MMKVLogLevel, int]
     candidates:
      ClspMth{java.util.EnumMap.put(java.lang.Object, java.lang.Object):java.lang.Object}
      ClspMth{java.util.AbstractMap.put(com.tencent.mmkv.MMKVRecoverStrategic, java.lang.Integer):V}
      ClspMth{java.util.Map.put(com.tencent.mmkv.MMKVRecoverStrategic, java.lang.Integer):V}
      ClspMth{java.util.EnumMap.put(com.tencent.mmkv.MMKVRecoverStrategic, java.lang.Integer):V} */
    static {
        recoverIndex.put(MMKVRecoverStrategic.OnErrorDiscard, (Integer) 0);
        recoverIndex.put(MMKVRecoverStrategic.OnErrorRecover, (Integer) 1);
        logLevel2Index.put((MMKVRecoverStrategic) MMKVLogLevel.LevelDebug, (Integer) 0);
        logLevel2Index.put((MMKVRecoverStrategic) MMKVLogLevel.LevelInfo, (Integer) 1);
        logLevel2Index.put((MMKVRecoverStrategic) MMKVLogLevel.LevelWarning, (Integer) 2);
        logLevel2Index.put((MMKVRecoverStrategic) MMKVLogLevel.LevelError, (Integer) 3);
        logLevel2Index.put((MMKVRecoverStrategic) MMKVLogLevel.LevelNone, (Integer) 4);
    }

    public static String initialize(Context context) {
        return initialize(context.getFilesDir().getAbsolutePath() + "/mmkv", null, MMKVLogLevel.LevelInfo);
    }

    public static String initialize(Context context, MMKVLogLevel logLevel) {
        return initialize(context.getFilesDir().getAbsolutePath() + "/mmkv", null, logLevel);
    }

    public static String initialize(String rootDir2) {
        return initialize(rootDir2, null, MMKVLogLevel.LevelInfo);
    }

    public static String initialize(String rootDir2, MMKVLogLevel logLevel) {
        return initialize(rootDir2, null, logLevel);
    }

    public static String initialize(String rootDir2, LibLoader loader) {
        return initialize(rootDir2, loader, MMKVLogLevel.LevelInfo);
    }

    public static String initialize(String rootDir2, LibLoader loader, MMKVLogLevel logLevel) {
        if (loader != null) {
            if (BuildConfig.FLAVOR.equals(BuildConfig.FLAVOR)) {
                loader.loadLibrary("c++_shared");
            }
            loader.loadLibrary("mmkv");
        } else {
            if (BuildConfig.FLAVOR.equals(BuildConfig.FLAVOR)) {
                System.loadLibrary("c++_shared");
            }
            System.loadLibrary("mmkv");
        }
        rootDir = rootDir2;
        jniInitialize(rootDir, logLevel2Int(logLevel));
        return rootDir2;
    }

    public static String getRootDir() {
        return rootDir;
    }

    private static int logLevel2Int(MMKVLogLevel level) {
        switch (level) {
            case LevelDebug:
                return 0;
            case LevelInfo:
                return 1;
            case LevelWarning:
                return 2;
            case LevelError:
                return 3;
            case LevelNone:
                return 4;
            default:
                return 1;
        }
    }

    public static void setLogLevel(MMKVLogLevel level) {
        setLogLevel(logLevel2Int(level));
    }

    public static MMKV mmkvWithID(String mmapID) {
        if (rootDir != null) {
            return new MMKV(getMMKVWithID(mmapID, 1, null, null));
        }
        throw new IllegalStateException("You should Call MMKV.initialize() first.");
    }

    public static MMKV mmkvWithID(String mmapID, int mode) {
        if (rootDir != null) {
            return new MMKV(getMMKVWithID(mmapID, mode, null, null));
        }
        throw new IllegalStateException("You should Call MMKV.initialize() first.");
    }

    public static MMKV mmkvWithID(String mmapID, int mode, String cryptKey) {
        if (rootDir != null) {
            return new MMKV(getMMKVWithID(mmapID, mode, cryptKey, null));
        }
        throw new IllegalStateException("You should Call MMKV.initialize() first.");
    }

    @Nullable
    public static MMKV mmkvWithID(String mmapID, String relativePath) {
        if (rootDir == null) {
            throw new IllegalStateException("You should Call MMKV.initialize() first.");
        }
        long handle = getMMKVWithID(mmapID, 1, null, relativePath);
        if (handle == 0) {
            return null;
        }
        return new MMKV(handle);
    }

    @Nullable
    public static MMKV mmkvWithID(String mmapID, int mode, String cryptKey, String relativePath) {
        if (rootDir == null) {
            throw new IllegalStateException("You should Call MMKV.initialize() first.");
        }
        long handle = getMMKVWithID(mmapID, mode, cryptKey, relativePath);
        if (handle == 0) {
            return null;
        }
        return new MMKV(handle);
    }

    @Nullable
    public static MMKV mmkvWithAshmemID(Context context, String mmapID, int size, int mode, String cryptKey) {
        if (rootDir == null) {
            throw new IllegalStateException("You should Call MMKV.initialize() first.");
        }
        String processName = MMKVContentProvider.getProcessNameByPID(context, Process.myPid());
        if (processName == null || processName.length() == 0) {
            simpleLog(MMKVLogLevel.LevelError, "process name detect fail, try again later");
            return null;
        } else if (processName.contains(":")) {
            Uri uri = MMKVContentProvider.contentUri(context);
            if (uri == null) {
                simpleLog(MMKVLogLevel.LevelError, "MMKVContentProvider has invalid authority");
                return null;
            }
            simpleLog(MMKVLogLevel.LevelInfo, "getting parcelable mmkv in process, Uri = " + uri);
            Bundle extras = new Bundle();
            extras.putInt("KEY_SIZE", size);
            extras.putInt("KEY_MODE", mode);
            if (cryptKey != null) {
                extras.putString("KEY_CRYPT", cryptKey);
            }
            Bundle result = context.getContentResolver().call(uri, "mmkvFromAshmemID", mmapID, extras);
            if (result != null) {
                result.setClassLoader(ParcelableMMKV.class.getClassLoader());
                ParcelableMMKV parcelableMMKV = (ParcelableMMKV) result.getParcelable("KEY");
                if (parcelableMMKV != null) {
                    MMKV mmkv = parcelableMMKV.toMMKV();
                    if (mmkv == null) {
                        return mmkv;
                    }
                    simpleLog(MMKVLogLevel.LevelInfo, mmkv.mmapID() + " fd = " + mmkv.ashmemFD() + ", meta fd = " + mmkv.ashmemMetaFD());
                    return mmkv;
                }
            }
            return null;
        } else {
            simpleLog(MMKVLogLevel.LevelInfo, "getting mmkv in main process");
            return new MMKV(getMMKVWithIDAndSize(mmapID, size, mode | 8, cryptKey));
        }
    }

    public static MMKV defaultMMKV() {
        if (rootDir != null) {
            return new MMKV(getDefaultMMKV(1, null));
        }
        throw new IllegalStateException("You should Call MMKV.initialize() first.");
    }

    public static MMKV defaultMMKV(int mode, String cryptKey) {
        if (rootDir != null) {
            return new MMKV(getDefaultMMKV(mode, cryptKey));
        }
        throw new IllegalStateException("You should Call MMKV.initialize() first.");
    }

    public boolean encode(String key, boolean value) {
        return encodeBool(this.nativeHandle, key, value);
    }

    public boolean decodeBool(String key) {
        return decodeBool(this.nativeHandle, key, false);
    }

    public boolean decodeBool(String key, boolean defaultValue) {
        return decodeBool(this.nativeHandle, key, defaultValue);
    }

    public boolean encode(String key, int value) {
        return encodeInt(this.nativeHandle, key, value);
    }

    public int decodeInt(String key) {
        return decodeInt(this.nativeHandle, key, 0);
    }

    public int decodeInt(String key, int defaultValue) {
        return decodeInt(this.nativeHandle, key, defaultValue);
    }

    public boolean encode(String key, long value) {
        return encodeLong(this.nativeHandle, key, value);
    }

    public long decodeLong(String key) {
        return decodeLong(this.nativeHandle, key, 0);
    }

    public long decodeLong(String key, long defaultValue) {
        return decodeLong(this.nativeHandle, key, defaultValue);
    }

    public boolean encode(String key, float value) {
        return encodeFloat(this.nativeHandle, key, value);
    }

    public float decodeFloat(String key) {
        return decodeFloat(this.nativeHandle, key, 0.0f);
    }

    public float decodeFloat(String key, float defaultValue) {
        return decodeFloat(this.nativeHandle, key, defaultValue);
    }

    public boolean encode(String key, double value) {
        return encodeDouble(this.nativeHandle, key, value);
    }

    public double decodeDouble(String key) {
        return decodeDouble(this.nativeHandle, key, 0.0d);
    }

    public double decodeDouble(String key, double defaultValue) {
        return decodeDouble(this.nativeHandle, key, defaultValue);
    }

    public boolean encode(String key, String value) {
        return encodeString(this.nativeHandle, key, value);
    }

    public String decodeString(String key) {
        return decodeString(this.nativeHandle, key, null);
    }

    public String decodeString(String key, String defaultValue) {
        return decodeString(this.nativeHandle, key, defaultValue);
    }

    public boolean encode(String key, Set<String> value) {
        return encodeSet(this.nativeHandle, key, (String[]) value.toArray(new String[0]));
    }

    public Set<String> decodeStringSet(String key) {
        return decodeStringSet(key, (Set<String>) null);
    }

    public Set<String> decodeStringSet(String key, Set<String> defaultValue) {
        return decodeStringSet(key, defaultValue, HashSet.class);
    }

    public Set<String> decodeStringSet(String key, Set<String> defaultValue, Class<? extends Set> cls) {
        String[] result = decodeStringSet(this.nativeHandle, key);
        if (result == null) {
            return defaultValue;
        }
        try {
            Set<String> a = (Set) cls.newInstance();
            a.addAll(Arrays.asList(result));
            return a;
        } catch (IllegalAccessException | InstantiationException e) {
            return defaultValue;
        }
    }

    public boolean encode(String key, byte[] value) {
        return encodeBytes(this.nativeHandle, key, value);
    }

    public byte[] decodeBytes(String key) {
        return decodeBytes(key, (byte[]) null);
    }

    public byte[] decodeBytes(String key, byte[] defaultValue) {
        byte[] ret = decodeBytes(this.nativeHandle, key);
        return ret != null ? ret : defaultValue;
    }

    public boolean encode(String key, Parcelable value) {
        Parcel source = Parcel.obtain();
        value.writeToParcel(source, value.describeContents());
        byte[] bytes = source.marshall();
        source.recycle();
        return encodeBytes(this.nativeHandle, key, bytes);
    }

    public <T extends Parcelable> T decodeParcelable(String key, Class<T> tClass) {
        return decodeParcelable(key, tClass, null);
    }

    public <T extends Parcelable> T decodeParcelable(String key, Class<T> tClass, T defaultValue) {
        byte[] bytes;
        Parcelable.Creator<T> creator;
        if (tClass == null || (bytes = decodeBytes(this.nativeHandle, key)) == null) {
            return defaultValue;
        }
        Parcel source = Parcel.obtain();
        source.unmarshall(bytes, 0, bytes.length);
        source.setDataPosition(0);
        try {
            String name = tClass.toString();
            synchronized (mCreators) {
                creator = mCreators.get(name);
                if (creator == null && (creator = (Parcelable.Creator) tClass.getField("CREATOR").get(null)) != null) {
                    mCreators.put(name, creator);
                }
            }
            if (creator != null) {
                T defaultValue2 = (Parcelable) creator.createFromParcel(source);
                source.recycle();
                return defaultValue2;
            }
            throw new Exception("Parcelable protocol requires a non-null static Parcelable.Creator object called CREATOR on class " + name);
        } catch (Exception e) {
            try {
                simpleLog(MMKVLogLevel.LevelError, e.toString());
                return defaultValue;
            } finally {
                source.recycle();
            }
        }
    }

    public int getValueSize(String key) {
        return valueSize(this.nativeHandle, key, false);
    }

    public int getValueActualSize(String key) {
        return valueSize(this.nativeHandle, key, true);
    }

    public boolean containsKey(String key) {
        return containsKey(this.nativeHandle, key);
    }

    public long count() {
        return count(this.nativeHandle);
    }

    public long totalSize() {
        return totalSize(this.nativeHandle);
    }

    public void removeValueForKey(String key) {
        removeValueForKey(this.nativeHandle, key);
    }

    public void sync() {
        sync(true);
    }

    public void async() {
        sync(false);
    }

    public int importFromSharedPreferences(SharedPreferences preferences) {
        Map<String, ?> kvs = preferences.getAll();
        if (kvs == null || kvs.size() <= 0) {
            return 0;
        }
        for (Map.Entry<String, ?> entry : kvs.entrySet()) {
            String key = (String) entry.getKey();
            Object value = entry.getValue();
            if (!(key == null || value == null)) {
                if (value instanceof Boolean) {
                    encodeBool(this.nativeHandle, key, ((Boolean) value).booleanValue());
                } else if (value instanceof Integer) {
                    encodeInt(this.nativeHandle, key, ((Integer) value).intValue());
                } else if (value instanceof Long) {
                    encodeLong(this.nativeHandle, key, ((Long) value).longValue());
                } else if (value instanceof Float) {
                    encodeFloat(this.nativeHandle, key, ((Float) value).floatValue());
                } else if (value instanceof Double) {
                    encodeDouble(this.nativeHandle, key, ((Double) value).doubleValue());
                } else if (value instanceof String) {
                    encodeString(this.nativeHandle, key, (String) value);
                } else if (value instanceof Set) {
                    encode(key, (Set) value);
                } else {
                    simpleLog(MMKVLogLevel.LevelError, "unknown type: " + value.getClass());
                }
            }
        }
        return kvs.size();
    }

    public Map<String, ?> getAll() {
        throw new UnsupportedOperationException("use allKeys() instead, getAll() not implement because type-erasure inside mmkv");
    }

    @Nullable
    public String getString(String key, @Nullable String defValue) {
        return decodeString(this.nativeHandle, key, defValue);
    }

    public SharedPreferences.Editor putString(String key, @Nullable String value) {
        encodeString(this.nativeHandle, key, value);
        return this;
    }

    @Nullable
    public Set<String> getStringSet(String key, @Nullable Set<String> defValues) {
        return decodeStringSet(key, defValues);
    }

    public SharedPreferences.Editor putStringSet(String key, @Nullable Set<String> values) {
        encode(key, values);
        return this;
    }

    public SharedPreferences.Editor putBytes(String key, @Nullable byte[] bytes) {
        encode(key, bytes);
        return this;
    }

    public byte[] getBytes(String key, @Nullable byte[] defValue) {
        return decodeBytes(key, defValue);
    }

    public int getInt(String key, int defValue) {
        return decodeInt(this.nativeHandle, key, defValue);
    }

    public SharedPreferences.Editor putInt(String key, int value) {
        encodeInt(this.nativeHandle, key, value);
        return this;
    }

    public long getLong(String key, long defValue) {
        return decodeLong(this.nativeHandle, key, defValue);
    }

    public SharedPreferences.Editor putLong(String key, long value) {
        encodeLong(this.nativeHandle, key, value);
        return this;
    }

    public float getFloat(String key, float defValue) {
        return decodeFloat(this.nativeHandle, key, defValue);
    }

    public SharedPreferences.Editor putFloat(String key, float value) {
        encodeFloat(this.nativeHandle, key, value);
        return this;
    }

    public boolean getBoolean(String key, boolean defValue) {
        return decodeBool(this.nativeHandle, key, defValue);
    }

    public SharedPreferences.Editor putBoolean(String key, boolean value) {
        encodeBool(this.nativeHandle, key, value);
        return this;
    }

    public SharedPreferences.Editor remove(String key) {
        removeValueForKey(key);
        return this;
    }

    public SharedPreferences.Editor clear() {
        clearAll();
        return this;
    }

    public boolean commit() {
        sync(true);
        return true;
    }

    public void apply() {
        sync(false);
    }

    public boolean contains(String key) {
        return containsKey(key);
    }

    public SharedPreferences.Editor edit() {
        return this;
    }

    public void registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        throw new UnsupportedOperationException("Not implement in MMKV");
    }

    public void unregisterOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        throw new UnsupportedOperationException("Not implement in MMKV");
    }

    public static MMKV mmkvWithAshmemFD(String mmapID, int fd, int metaFD, String cryptKey) {
        return new MMKV(getMMKVWithAshmemFD(mmapID, fd, metaFD, cryptKey));
    }

    public static NativeBuffer createNativeBuffer(int size) {
        long pointer = createNB(size);
        if (pointer <= 0) {
            return null;
        }
        return new NativeBuffer(pointer, size);
    }

    public static void destroyNativeBuffer(NativeBuffer buffer) {
        destroyNB(buffer.pointer, buffer.size);
    }

    public int writeValueToNativeBuffer(String key, NativeBuffer buffer) {
        return writeValueToNB(this.nativeHandle, key, buffer.pointer, buffer.size);
    }

    public static void registerHandler(MMKVHandler handler) {
        gCallbackHandler = handler;
        if (gCallbackHandler.wantLogRedirecting()) {
            setLogReDirecting(true);
            gWantLogReDirecting = true;
            return;
        }
        setLogReDirecting(false);
        gWantLogReDirecting = false;
    }

    public static void unregisterHandler() {
        gCallbackHandler = null;
        setLogReDirecting(false);
        gWantLogReDirecting = false;
    }

    private static int onMMKVCRCCheckFail(String mmapID) {
        MMKVRecoverStrategic strategic = MMKVRecoverStrategic.OnErrorDiscard;
        if (gCallbackHandler != null) {
            strategic = gCallbackHandler.onMMKVCRCCheckFail(mmapID);
        }
        simpleLog(MMKVLogLevel.LevelInfo, "Recover strategic for " + mmapID + " is " + strategic);
        Integer value = recoverIndex.get(strategic);
        if (value == null) {
            return 0;
        }
        return value.intValue();
    }

    private static int onMMKVFileLengthError(String mmapID) {
        MMKVRecoverStrategic strategic = MMKVRecoverStrategic.OnErrorDiscard;
        if (gCallbackHandler != null) {
            strategic = gCallbackHandler.onMMKVFileLengthError(mmapID);
        }
        simpleLog(MMKVLogLevel.LevelInfo, "Recover strategic for " + mmapID + " is " + strategic);
        Integer value = recoverIndex.get(strategic);
        if (value == null) {
            return 0;
        }
        return value.intValue();
    }

    private static void mmkvLogImp(int level, String file, int line, String function, String message) {
        if (gCallbackHandler == null || !gWantLogReDirecting) {
            switch (index2LogLevel[level]) {
                case LevelDebug:
                    Log.d("MMKV", message);
                    return;
                case LevelInfo:
                    Log.i("MMKV", message);
                    return;
                case LevelWarning:
                    Log.w("MMKV", message);
                    return;
                case LevelError:
                    Log.e("MMKV", message);
                    return;
                default:
                    return;
            }
        } else {
            gCallbackHandler.mmkvLog(index2LogLevel[level], file, line, function, message);
        }
    }

    private static void simpleLog(MMKVLogLevel level, String message) {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement e = stacktrace[stacktrace.length - 1];
        Integer i = logLevel2Index.get(level);
        mmkvLogImp(i == null ? 0 : i.intValue(), e.getFileName(), e.getLineNumber(), e.getMethodName(), message);
    }

    public static void registerContentChangeNotify(MMKVContentChangeNotification notify) {
        gContentChangeNotify = notify;
        setWantsContentChangeNotify(gContentChangeNotify != null);
    }

    public static void unregisterContentChangeNotify() {
        gContentChangeNotify = null;
        setWantsContentChangeNotify(false);
    }

    private static void onContentChangedByOuterProcess(String mmapID) {
        if (gContentChangeNotify != null) {
            gContentChangeNotify.onContentChangedByOuterProcess(mmapID);
        }
    }

    private MMKV(long handle) {
        this.nativeHandle = handle;
    }
}
