package com.mapzen.android.lost.internal;

import com.mapzen.android.lost.api.LostApiClient;

class ApiImpl {
    ApiImpl() {
    }

    /* access modifiers changed from: package-private */
    public void throwIfNotConnected(LostApiClient client) {
        if (!client.isConnected()) {
            throw new IllegalStateException("LostApiClient is not connected.");
        }
    }
}
