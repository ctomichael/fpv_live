package com.google.android.gms.common.api.internal;

import android.os.Looper;
import android.support.annotation.NonNull;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.internal.BaseGmsClient;
import com.google.android.gms.common.internal.Preconditions;
import java.lang.ref.WeakReference;

final class zaam implements BaseGmsClient.ConnectionProgressReportCallbacks {
    private final Api<?> mApi;
    /* access modifiers changed from: private */
    public final boolean zaeb;
    private final WeakReference<zaak> zagj;

    public zaam(zaak zaak, Api<?> api, boolean z) {
        this.zagj = new WeakReference<>(zaak);
        this.mApi = api;
        this.zaeb = z;
    }

    public final void onReportServiceBinding(@NonNull ConnectionResult connectionResult) {
        boolean z = false;
        zaak zaak = this.zagj.get();
        if (zaak != null) {
            if (Looper.myLooper() == zaak.zafs.zaed.getLooper()) {
                z = true;
            }
            Preconditions.checkState(z, "onReportServiceBinding must be called on the GoogleApiClient handler thread");
            zaak.zaen.lock();
            try {
                if (zaak.zac(0)) {
                    if (!connectionResult.isSuccess()) {
                        zaak.zab(connectionResult, this.mApi, this.zaeb);
                    }
                    if (zaak.zaao()) {
                        zaak.zaap();
                    }
                    zaak.zaen.unlock();
                }
            } finally {
                zaak.zaen.unlock();
            }
        }
    }
}
