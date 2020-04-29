package com.google.android.gms.common.api.internal;

import android.os.Bundle;
import android.support.annotation.NonNull;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

final class zaat implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private final /* synthetic */ zaak zagi;

    private zaat(zaak zaak) {
        this.zagi = zaak;
    }

    public final void onConnected(Bundle bundle) {
        this.zagi.zaga.zaa(new zaar(this.zagi));
    }

    public final void onConnectionSuspended(int i) {
    }

    public final void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        this.zagi.zaen.lock();
        try {
            if (this.zagi.zad(connectionResult)) {
                this.zagi.zaar();
                this.zagi.zaap();
            } else {
                this.zagi.zae(connectionResult);
            }
        } finally {
            this.zagi.zaen.unlock();
        }
    }

    /* synthetic */ zaat(zaak zaak, zaal zaal) {
        this(zaak);
    }
}
