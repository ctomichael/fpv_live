package com.google.android.gms.common.api.internal;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.android.gms.common.ConnectionResult;

final class zav implements zabt {
    private final /* synthetic */ zas zaep;

    private zav(zas zas) {
        this.zaep = zas;
    }

    public final void zab(@Nullable Bundle bundle) {
        this.zaep.zaen.lock();
        try {
            ConnectionResult unused = this.zaep.zael = ConnectionResult.RESULT_SUCCESS;
            this.zaep.zax();
        } finally {
            this.zaep.zaen.unlock();
        }
    }

    public final void zac(@NonNull ConnectionResult connectionResult) {
        this.zaep.zaen.lock();
        try {
            ConnectionResult unused = this.zaep.zael = connectionResult;
            this.zaep.zax();
        } finally {
            this.zaep.zaen.unlock();
        }
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.google.android.gms.common.api.internal.zas.zaa(com.google.android.gms.common.api.internal.zas, boolean):boolean
     arg types: [com.google.android.gms.common.api.internal.zas, int]
     candidates:
      com.google.android.gms.common.api.internal.zas.zaa(com.google.android.gms.common.api.internal.zas, com.google.android.gms.common.ConnectionResult):com.google.android.gms.common.ConnectionResult
      com.google.android.gms.common.api.internal.zas.zaa(int, boolean):void
      com.google.android.gms.common.api.internal.zas.zaa(com.google.android.gms.common.api.internal.zas, android.os.Bundle):void
      com.google.android.gms.common.api.internal.zas.zaa(com.google.android.gms.common.api.internal.zas, boolean):boolean */
    public final void zab(int i, boolean z) {
        this.zaep.zaen.lock();
        try {
            if (this.zaep.zaem) {
                boolean unused = this.zaep.zaem = false;
                this.zaep.zaa(i, z);
                return;
            }
            boolean unused2 = this.zaep.zaem = true;
            this.zaep.zaee.onConnectionSuspended(i);
            this.zaep.zaen.unlock();
        } finally {
            this.zaep.zaen.unlock();
        }
    }

    /* synthetic */ zav(zas zas, zat zat) {
        this(zas);
    }
}
