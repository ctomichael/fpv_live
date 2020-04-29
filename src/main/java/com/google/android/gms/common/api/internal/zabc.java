package com.google.android.gms.common.api.internal;

import java.lang.ref.WeakReference;

final class zabc extends zabr {
    private WeakReference<zaaw> zahl;

    zabc(zaaw zaaw) {
        this.zahl = new WeakReference<>(zaaw);
    }

    public final void zas() {
        zaaw zaaw = this.zahl.get();
        if (zaaw != null) {
            zaaw.resume();
        }
    }
}
