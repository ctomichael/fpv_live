package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.ConnectionResult;
import javax.annotation.concurrent.GuardedBy;

final class zaao extends zabf {
    private final /* synthetic */ ConnectionResult zagl;
    private final /* synthetic */ zaan zagm;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    zaao(zaan zaan, zabd zabd, ConnectionResult connectionResult) {
        super(zabd);
        this.zagm = zaan;
        this.zagl = connectionResult;
    }

    @GuardedBy("mLock")
    public final void zaan() {
        this.zagm.zagi.zae(this.zagl);
    }
}
