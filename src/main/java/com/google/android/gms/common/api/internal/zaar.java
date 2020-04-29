package com.google.android.gms.common.api.internal;

import android.support.annotation.BinderThread;
import com.google.android.gms.signin.internal.zac;
import com.google.android.gms.signin.internal.zaj;
import java.lang.ref.WeakReference;

final class zaar extends zac {
    private final WeakReference<zaak> zagj;

    zaar(zaak zaak) {
        this.zagj = new WeakReference<>(zaak);
    }

    @BinderThread
    public final void zab(zaj zaj) {
        zaak zaak = this.zagj.get();
        if (zaak != null) {
            zaak.zafs.zaa(new zaas(this, zaak, zaak, zaj));
        }
    }
}
