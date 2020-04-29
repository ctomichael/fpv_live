package com.google.android.gms.internal.location;

import android.os.DeadObjectException;
import android.os.IInterface;

final class zzl implements zzbj<zzao> {
    private final /* synthetic */ zzk zzcc;

    zzl(zzk zzk) {
        this.zzcc = zzk;
    }

    public final void checkConnected() {
        this.zzcc.checkConnected();
    }

    public final /* synthetic */ IInterface getService() throws DeadObjectException {
        return (zzao) this.zzcc.getService();
    }
}
