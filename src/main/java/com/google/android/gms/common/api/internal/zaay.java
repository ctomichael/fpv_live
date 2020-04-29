package com.google.android.gms.common.api.internal;

import android.os.Bundle;
import com.google.android.gms.common.api.GoogleApiClient;
import java.util.concurrent.atomic.AtomicReference;

final class zaay implements GoogleApiClient.ConnectionCallbacks {
    private final /* synthetic */ zaaw zahg;
    private final /* synthetic */ AtomicReference zahh;
    private final /* synthetic */ StatusPendingResult zahi;

    zaay(zaaw zaaw, AtomicReference atomicReference, StatusPendingResult statusPendingResult) {
        this.zahg = zaaw;
        this.zahh = atomicReference;
        this.zahi = statusPendingResult;
    }

    public final void onConnected(Bundle bundle) {
        this.zahg.zaa((GoogleApiClient) this.zahh.get(), this.zahi, true);
    }

    public final void onConnectionSuspended(int i) {
    }
}
