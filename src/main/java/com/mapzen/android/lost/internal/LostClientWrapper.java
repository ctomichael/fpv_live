package com.mapzen.android.lost.internal;

import android.app.PendingIntent;
import android.os.Looper;
import com.mapzen.android.lost.api.LocationCallback;
import com.mapzen.android.lost.api.LocationListener;
import com.mapzen.android.lost.api.LostApiClient;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class LostClientWrapper {
    private final LostApiClient client;
    private Set<LocationCallback> locationCallbacks = new HashSet();
    private Set<LocationListener> locationListeners = new HashSet();
    private Map<LocationCallback, Looper> looperMap = new HashMap();
    private Set<PendingIntent> pendingIntents = new HashSet();

    LostClientWrapper(LostApiClient client2) {
        this.client = client2;
    }

    public LostApiClient client() {
        return this.client;
    }

    /* access modifiers changed from: package-private */
    public Set<LocationListener> locationListeners() {
        return this.locationListeners;
    }

    /* access modifiers changed from: package-private */
    public Set<PendingIntent> pendingIntents() {
        return this.pendingIntents;
    }

    /* access modifiers changed from: package-private */
    public Set<LocationCallback> locationCallbacks() {
        return this.locationCallbacks;
    }

    /* access modifiers changed from: package-private */
    public Map<LocationCallback, Looper> looperMap() {
        return this.looperMap;
    }
}
