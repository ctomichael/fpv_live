package com.mapbox.android.core.location;

import android.app.PendingIntent;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class LocationEngineProxy<T> implements LocationEngine {
    private Map<LocationEngineCallback<LocationEngineResult>, T> listeners;
    private final LocationEngineImpl<T> locationEngineImpl;

    LocationEngineProxy(LocationEngineImpl<T> locationEngineImpl2) {
        this.locationEngineImpl = locationEngineImpl2;
    }

    public void getLastLocation(@NonNull LocationEngineCallback<LocationEngineResult> callback) throws SecurityException {
        Utils.checkNotNull(callback, "callback == null");
        this.locationEngineImpl.getLastLocation(callback);
    }

    public void requestLocationUpdates(@NonNull LocationEngineRequest request, @NonNull LocationEngineCallback<LocationEngineResult> callback, @Nullable Looper looper) throws SecurityException {
        Utils.checkNotNull(request, "request == null");
        Utils.checkNotNull(callback, "callback == null");
        LocationEngineImpl<T> locationEngineImpl2 = this.locationEngineImpl;
        Object listener = getListener(callback);
        if (looper == null) {
            looper = Looper.getMainLooper();
        }
        locationEngineImpl2.requestLocationUpdates(request, listener, looper);
    }

    public void requestLocationUpdates(@NonNull LocationEngineRequest request, PendingIntent pendingIntent) throws SecurityException {
        Utils.checkNotNull(request, "request == null");
        this.locationEngineImpl.requestLocationUpdates(request, pendingIntent);
    }

    public void removeLocationUpdates(@NonNull LocationEngineCallback<LocationEngineResult> callback) {
        Utils.checkNotNull(callback, "callback == null");
        this.locationEngineImpl.removeLocationUpdates(removeListener(callback));
    }

    public void removeLocationUpdates(PendingIntent pendingIntent) {
        this.locationEngineImpl.removeLocationUpdates(pendingIntent);
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public int getListenersCount() {
        if (this.listeners != null) {
            return this.listeners.size();
        }
        return 0;
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public T getListener(@NonNull LocationEngineCallback<LocationEngineResult> callback) {
        if (this.listeners == null) {
            this.listeners = new ConcurrentHashMap();
        }
        T listener = this.listeners.get(callback);
        if (listener == null) {
            listener = this.locationEngineImpl.createListener(callback);
        }
        this.listeners.put(callback, listener);
        return listener;
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public T removeListener(@NonNull LocationEngineCallback<LocationEngineResult> callback) {
        if (this.listeners != null) {
            return this.listeners.remove(callback);
        }
        return null;
    }
}
