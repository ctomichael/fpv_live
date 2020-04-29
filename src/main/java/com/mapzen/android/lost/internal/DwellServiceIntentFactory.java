package com.mapzen.android.lost.internal;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class DwellServiceIntentFactory implements IntentFactory {
    public Intent createIntent(Context context) {
        return new Intent(context, DwellIntentService.class);
    }

    public PendingIntent createPendingIntent(Context context, int pendingIntentId, Intent intent) {
        return PendingIntent.getService(context, pendingIntentId, intent, 0);
    }
}
