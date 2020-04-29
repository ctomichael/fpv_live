package com.google.android.gms.common.api.internal;

final class zaal implements Runnable {
    private final /* synthetic */ zaak zagi;

    zaal(zaak zaak) {
        this.zagi = zaak;
    }

    public final void run() {
        this.zagi.zaex.cancelAvailabilityErrorNotifications(this.zagi.mContext);
    }
}
