package com.mapzen.android.lost.api;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import com.mapzen.android.lost.internal.FusionEngine;
import com.mapzen.android.lost.internal.GeofenceIntentHelper;
import com.mapzen.android.lost.internal.GeofencingApiImpl;
import com.mapzen.android.lost.internal.ParcelableGeofence;
import java.util.ArrayList;

public class GeofencingIntentSender {
    private Context context;
    private FusionEngine engine;
    private GeofencingApiImpl geofencingApi;
    private GeofenceIntentHelper intentHelper = new GeofenceIntentHelper();

    public GeofencingIntentSender(Context context2, GeofencingApi geofencingApi2) {
        this.context = context2;
        this.geofencingApi = (GeofencingApiImpl) geofencingApi2;
        this.engine = new FusionEngine(context2, null);
    }

    public void sendIntent(Intent intent) {
        if (shouldSendIntent(intent)) {
            Intent toSend = generateIntent(intent, this.engine.getLastLocation());
            try {
                this.geofencingApi.pendingIntentForIntentId(this.intentHelper.extractIntentId(intent)).send(this.context, 0, toSend);
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean shouldSendIntent(Intent intent) {
        return (((ParcelableGeofence) this.geofencingApi.geofenceForIntentId(this.intentHelper.extractIntentId(intent))).getTransitionTypes() & this.intentHelper.transitionForIntent(intent)) != 0;
    }

    public Intent generateIntent(Intent intent, Location location) {
        Geofence geofence = this.geofencingApi.geofenceForIntentId(this.intentHelper.extractIntentId(intent));
        ArrayList<Geofence> geofences = new ArrayList<>();
        geofences.add(geofence);
        Intent toSend = new Intent();
        toSend.putExtra(GeofencingApi.EXTRA_TRANSITION, this.intentHelper.transitionForIntent(intent));
        toSend.putExtra(GeofencingApi.EXTRA_GEOFENCE_LIST, geofences);
        toSend.putExtra(GeofencingApi.EXTRA_TRIGGERING_LOCATION, location);
        return toSend;
    }
}
