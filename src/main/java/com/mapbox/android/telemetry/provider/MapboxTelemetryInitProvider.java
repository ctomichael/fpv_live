package com.mapbox.android.telemetry.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import com.mapbox.android.core.crashreporter.MapboxUncaughtExceptionHanlder;
import com.mapbox.android.telemetry.BuildConfig;
import com.mapbox.android.telemetry.crash.TokenChangeBroadcastReceiver;
import com.mapbox.android.telemetry.location.LocationCollectionClient;
import java.util.concurrent.TimeUnit;

public class MapboxTelemetryInitProvider extends ContentProvider {
    private static final String EMPTY_APPLICATION_ID_PROVIDER_AUTHORITY = "com.mapbox.android.telemetry.provider.mapboxtelemetryinitprovider";
    private static final String TAG = "MbxTelemInitProvider";

    public boolean onCreate() {
        try {
            TokenChangeBroadcastReceiver.register(getContext());
            MapboxUncaughtExceptionHanlder.install(getContext(), "com.mapbox.android.telemetry", BuildConfig.VERSION_NAME);
            LocationCollectionClient.install(getContext(), TimeUnit.HOURS.toMillis(24));
            return false;
        } catch (Throwable throwable) {
            Log.e(TAG, throwable.toString());
            return false;
        }
    }

    public void attachInfo(Context context, ProviderInfo info) {
        checkContentProviderAuthority(info);
        super.attachInfo(context, info);
    }

    @Nullable
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    private static void checkContentProviderAuthority(@NonNull ProviderInfo info) {
        if (info == null) {
            throw new IllegalStateException("MapboxTelemetryInitProvider: ProviderInfo cannot be null.");
        } else if (EMPTY_APPLICATION_ID_PROVIDER_AUTHORITY.equals(info.authority)) {
            throw new IllegalStateException("Incorrect provider authority in manifest. Most likely due to a missing applicationId variable in application's build.gradle.");
        }
    }
}
