package com.mapbox.android.telemetry.crash;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.mapbox.android.telemetry.MapboxTelemetryConstants;

public class TokenChangeBroadcastReceiver extends BroadcastReceiver {
    private static final String LOG_TAG = "TknBroadcastReceiver";

    public static void register(@NonNull Context context) {
        LocalBroadcastManager.getInstance(context).registerReceiver(new TokenChangeBroadcastReceiver(), new IntentFilter(MapboxTelemetryConstants.ACTION_TOKEN_CHANGED));
    }

    public void onReceive(Context context, Intent intent) {
        try {
            CrashReporterJobIntentService.enqueueWork(context);
            LocalBroadcastManager.getInstance(context).unregisterReceiver(this);
        } catch (Throwable throwable) {
            Log.e(LOG_TAG, throwable.toString());
        }
    }
}
