package com.mapzen.android.lost.internal;

import android.app.IntentService;
import android.content.Intent;
import com.mapzen.android.lost.api.GeofencingIntentSender;
import com.mapzen.android.lost.api.LocationServices;

public class GeofencingIntentService extends IntentService {
    public GeofencingIntentService() {
        super("GeofencingIntentService");
    }

    /* access modifiers changed from: protected */
    public void onHandleIntent(Intent intent) {
        new GeofencingIntentSender(this, LocationServices.GeofencingApi).sendIntent(intent);
        new GeofencingDwellManager(LocationServices.GeofencingApi).handleIntent(intent);
    }
}
