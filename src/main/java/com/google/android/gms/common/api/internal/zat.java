package com.google.android.gms.common.api.internal;

final class zat implements Runnable {
    private final /* synthetic */ zas zaep;

    zat(zas zas) {
        this.zaep = zas;
    }

    public final void run() {
        this.zaep.zaen.lock();
        try {
            this.zaep.zax();
        } finally {
            this.zaep.zaen.unlock();
        }
    }
}
