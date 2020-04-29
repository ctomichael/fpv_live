package com.mapzen.android.lost.internal;

import android.content.Context;
import com.mapzen.android.lost.api.LocationSettingsRequest;
import com.mapzen.android.lost.api.LocationSettingsResult;
import com.mapzen.android.lost.api.LostApiClient;
import com.mapzen.android.lost.api.PendingResult;
import com.mapzen.android.lost.api.SettingsApi;

public class SettingsApiImpl implements SettingsApi {
    private Context context;

    public void connect(Context context2) {
        this.context = context2;
    }

    public boolean isConnected() {
        return this.context != null;
    }

    public void disconnect() {
        this.context = null;
    }

    public PendingResult<LocationSettingsResult> checkLocationSettings(LostApiClient client, LocationSettingsRequest request) {
        return new LocationSettingsResultRequest(this.context, new PendingIntentGenerator(this.context), request);
    }
}
