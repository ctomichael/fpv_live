package com.google.android.gms.internal.location;

import com.google.android.gms.common.api.internal.ListenerHolder;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;

final class zzav implements ListenerHolder.Notifier<LocationCallback> {
    private final /* synthetic */ LocationAvailability zzdc;

    zzav(zzat zzat, LocationAvailability locationAvailability) {
        this.zzdc = locationAvailability;
    }

    public final /* synthetic */ void notifyListener(Object obj) {
        ((LocationCallback) obj).onLocationAvailability(this.zzdc);
    }

    public final void onNotifyListenerFailed() {
    }
}
