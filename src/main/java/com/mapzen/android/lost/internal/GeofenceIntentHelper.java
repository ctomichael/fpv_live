package com.mapzen.android.lost.internal;

import android.content.Intent;
import android.os.Bundle;

public class GeofenceIntentHelper {
    public static final String EXTRA_ENTERING = "entering";

    public int transitionForIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        if (!extras.containsKey(EXTRA_ENTERING)) {
            return 4;
        }
        if (extras.getBoolean(EXTRA_ENTERING)) {
            return 1;
        }
        return 2;
    }

    public int extractIntentId(Intent intent) {
        return Integer.valueOf(intent.getCategories().iterator().next()).intValue();
    }
}
