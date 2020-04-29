package com.google.android.gms.common.api.internal;

abstract class zabf {
    private final zabd zaht;

    protected zabf(zabd zabd) {
        this.zaht = zabd;
    }

    /* access modifiers changed from: protected */
    public abstract void zaan();

    public final void zac(zabe zabe) {
        zabe.zaen.lock();
        try {
            if (zabe.zahp == this.zaht) {
                zaan();
                zabe.zaen.unlock();
            }
        } finally {
            zabe.zaen.unlock();
        }
    }
}
