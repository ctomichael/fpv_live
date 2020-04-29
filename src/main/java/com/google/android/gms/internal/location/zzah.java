package com.google.android.gms.internal.location;

import android.os.RemoteException;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.zzal;

final class zzah extends zzai {
    private final /* synthetic */ zzal zzct;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    zzah(zzaf zzaf, GoogleApiClient googleApiClient, zzal zzal) {
        super(googleApiClient);
        this.zzct = zzal;
    }

    /* access modifiers changed from: protected */
    public final /* synthetic */ void doExecute(Api.AnyClient anyClient) throws RemoteException {
        ((zzaz) anyClient).zza(this.zzct, this);
    }
}
