package com.tencent.mmkv;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MMKVContentProvider extends ContentProvider {
    protected static final String FUNCTION_NAME = "mmkvFromAshmemID";
    protected static final String KEY = "KEY";
    protected static final String KEY_CRYPT = "KEY_CRYPT";
    protected static final String KEY_MODE = "KEY_MODE";
    protected static final String KEY_SIZE = "KEY_SIZE";
    private static Uri gUri;

    @Nullable
    protected static Uri contentUri(Context context) {
        String authority;
        if (gUri != null) {
            return gUri;
        }
        if (context == null || (authority = queryAuthority(context)) == null) {
            return null;
        }
        gUri = Uri.parse("content://" + authority);
        return gUri;
    }

    private Bundle mmkvFromAshmemID(String ashmemID, int size, int mode, String cryptKey) {
        MMKV mmkv = MMKV.mmkvWithAshmemID(getContext(), ashmemID, size, mode, cryptKey);
        if (mmkv == null) {
            return null;
        }
        ParcelableMMKV parcelableMMKV = new ParcelableMMKV(mmkv);
        Log.i("MMKV", ashmemID + " fd = " + mmkv.ashmemFD() + ", meta fd = " + mmkv.ashmemMetaFD());
        Bundle result = new Bundle();
        result.putParcelable(KEY, parcelableMMKV);
        return result;
    }

    private static String queryAuthority(Context context) {
        ProviderInfo providerInfo;
        try {
            ComponentName componentName = new ComponentName(context, MMKVContentProvider.class.getName());
            PackageManager mgr = context.getPackageManager();
            if (!(mgr == null || (providerInfo = mgr.getProviderInfo(componentName, 0)) == null)) {
                return providerInfo.authority;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean onCreate() {
        String authority;
        Context context = getContext();
        if (context == null || (authority = queryAuthority(context)) == null) {
            return false;
        }
        if (gUri == null) {
            gUri = Uri.parse("content://" + authority);
        }
        return true;
    }

    protected static String getProcessNameByPID(Context context, int pid) {
        ActivityManager manager = (ActivityManager) context.getSystemService("activity");
        if (manager != null) {
            for (ActivityManager.RunningAppProcessInfo processInfo : manager.getRunningAppProcesses()) {
                if (processInfo.pid == pid) {
                    return processInfo.processName;
                }
            }
        }
        return "";
    }

    @Nullable
    public Bundle call(@NonNull String method, @Nullable String mmapID, @Nullable Bundle extras) {
        if (!method.equals(FUNCTION_NAME) || extras == null) {
            return null;
        }
        return mmkvFromAshmemID(mmapID, extras.getInt(KEY_SIZE), extras.getInt(KEY_MODE), extras.getString(KEY_CRYPT));
    }

    @Nullable
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        throw new UnsupportedOperationException("Not implement in MMKV");
    }

    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new UnsupportedOperationException("Not implement in MMKV");
    }

    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new UnsupportedOperationException("Not implement in MMKV");
    }

    @Nullable
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        throw new UnsupportedOperationException("Not implement in MMKV");
    }
}
