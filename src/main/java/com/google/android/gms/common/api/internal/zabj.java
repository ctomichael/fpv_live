package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.api.internal.GoogleApiManager;

final class zabj implements Runnable {
    private final /* synthetic */ GoogleApiManager.zaa zaix;

    zabj(GoogleApiManager.zaa zaa) {
        this.zaix = zaa;
    }

    public final void run() {
        this.zaix.zabg();
    }
}
