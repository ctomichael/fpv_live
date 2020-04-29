package com.mapzen.android.lost.internal;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

class PendingIntentGenerator {
    private final Context context;

    PendingIntentGenerator(Context context2) {
        this.context = context2;
    }

    /* access modifiers changed from: package-private */
    public PendingIntent generatePendingIntent() {
        return PendingIntent.getActivity(this.context, 0, new Intent("android.settings.LOCATION_SOURCE_SETTINGS"), 0);
    }
}
