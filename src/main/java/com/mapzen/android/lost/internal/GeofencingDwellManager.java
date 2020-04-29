package com.mapzen.android.lost.internal;

import android.content.Intent;
import com.mapzen.android.lost.api.GeofencingApi;

public class GeofencingDwellManager {
    GeofencingApiImpl geofencingApi;
    GeofenceIntentHelper intentHelper = new GeofenceIntentHelper();

    public GeofencingDwellManager(GeofencingApi geofencingApi2) {
        this.geofencingApi = (GeofencingApiImpl) geofencingApi2;
    }

    public void handleIntent(Intent intent) {
        int transition = this.intentHelper.transitionForIntent(intent);
        int intentId = this.intentHelper.extractIntentId(intent);
        ParcelableGeofence geofence = (ParcelableGeofence) this.geofencingApi.geofenceForIntentId(intentId);
        switch (transition) {
            case 1:
                this.geofencingApi.geofenceEntered(geofence, intentId);
                return;
            case 2:
                this.geofencingApi.geofenceExited(geofence);
                return;
            default:
                return;
        }
    }
}
