package com.google.android.gms.common.api.internal;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.internal.Preconditions;

public final class zaq implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    public final Api<?> mApi;
    private final boolean zaeb;
    private zar zaec;

    public zaq(Api<?> api, boolean z) {
        this.mApi = api;
        this.zaeb = z;
    }

    public final void onConnected(@Nullable Bundle bundle) {
        zav();
        this.zaec.onConnected(bundle);
    }

    public final void onConnectionSuspended(int i) {
        zav();
        this.zaec.onConnectionSuspended(i);
    }

    public final void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        zav();
        this.zaec.zaa(connectionResult, this.mApi, this.zaeb);
    }

    public final void zaa(zar zar) {
        this.zaec = zar;
    }

    private final void zav() {
        Preconditions.checkNotNull(this.zaec, "Callbacks must be attached to a ClientConnectionHelper instance before connecting the client.");
    }
}
