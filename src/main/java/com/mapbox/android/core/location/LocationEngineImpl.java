package com.mapbox.android.core.location;

import android.app.PendingIntent;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

interface LocationEngineImpl<T> {
    @NonNull
    T createListener(LocationEngineCallback<LocationEngineResult> locationEngineCallback);

    void getLastLocation(@NonNull LocationEngineCallback<LocationEngineResult> locationEngineCallback) throws SecurityException;

    void removeLocationUpdates(PendingIntent pendingIntent);

    void removeLocationUpdates(Object obj);

    void requestLocationUpdates(@NonNull LocationEngineRequest locationEngineRequest, @NonNull PendingIntent pendingIntent) throws SecurityException;

    void requestLocationUpdates(@NonNull LocationEngineRequest locationEngineRequest, @NonNull Object obj, @Nullable Looper looper) throws SecurityException;
}
