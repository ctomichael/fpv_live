package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.internal.GoogleApiManager;
import java.util.Collections;

final class zabo implements Runnable {
    private final /* synthetic */ ConnectionResult zaiy;
    private final /* synthetic */ GoogleApiManager.zac zajf;

    zabo(GoogleApiManager.zac zac, ConnectionResult connectionResult) {
        this.zajf = zac;
        this.zaiy = connectionResult;
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.google.android.gms.common.api.internal.GoogleApiManager.zac.zaa(com.google.android.gms.common.api.internal.GoogleApiManager$zac, boolean):boolean
     arg types: [com.google.android.gms.common.api.internal.GoogleApiManager$zac, int]
     candidates:
      com.google.android.gms.common.api.internal.GoogleApiManager.zac.zaa(com.google.android.gms.common.internal.IAccountAccessor, java.util.Set<com.google.android.gms.common.api.Scope>):void
      com.google.android.gms.common.api.internal.zach.zaa(com.google.android.gms.common.internal.IAccountAccessor, java.util.Set<com.google.android.gms.common.api.Scope>):void
      com.google.android.gms.common.api.internal.GoogleApiManager.zac.zaa(com.google.android.gms.common.api.internal.GoogleApiManager$zac, boolean):boolean */
    public final void run() {
        if (this.zaiy.isSuccess()) {
            boolean unused = this.zajf.zaje = true;
            if (this.zajf.zain.requiresSignIn()) {
                this.zajf.zabr();
                return;
            }
            try {
                this.zajf.zain.getRemoteService(null, Collections.emptySet());
            } catch (SecurityException e) {
                ((GoogleApiManager.zaa) GoogleApiManager.this.zaih.get(this.zajf.zafp)).onConnectionFailed(new ConnectionResult(10));
            }
        } else {
            ((GoogleApiManager.zaa) GoogleApiManager.this.zaih.get(this.zajf.zafp)).onConnectionFailed(this.zaiy);
        }
    }
}
