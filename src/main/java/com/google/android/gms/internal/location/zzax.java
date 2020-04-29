package com.google.android.gms.internal.location;

import android.location.Location;
import com.google.android.gms.common.api.internal.ListenerHolder;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.zzy;

final class zzax extends zzy {
    private final ListenerHolder<LocationListener> zzda;

    zzax(ListenerHolder<LocationListener> listenerHolder) {
        this.zzda = listenerHolder;
    }

    public final synchronized void onLocationChanged(Location location) {
        this.zzda.notifyListener(new zzay(this, location));
    }

    public final synchronized void release() {
        this.zzda.clear();
    }
}
