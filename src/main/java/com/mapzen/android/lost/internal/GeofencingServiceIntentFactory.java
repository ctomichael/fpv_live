package com.mapzen.android.lost.internal;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class GeofencingServiceIntentFactory implements IntentFactory {
    public Intent createIntent(Context context) {
        return new Intent(context, GeofencingIntentService.class);
    }

    public PendingIntent createPendingIntent(Context context, int pendingIntentId, Intent intent) {
        return PendingIntent.getService(context, pendingIntentId, intent, 0);
    }
}
