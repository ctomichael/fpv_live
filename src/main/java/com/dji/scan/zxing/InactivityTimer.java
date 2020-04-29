package com.dji.scan.zxing;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public final class InactivityTimer {
    private static final long INACTIVITY_DELAY_MS = 300000;
    private static final String TAG = InactivityTimer.class.getSimpleName();
    private Runnable callback;
    private final Context context;
    /* access modifiers changed from: private */
    public Handler handler;
    private boolean onBattery;
    private final BroadcastReceiver powerStatusReceiver;
    private boolean registered = false;

    public InactivityTimer(Context context2, Runnable callback2) {
        this.context = context2;
        this.callback = callback2;
        this.powerStatusReceiver = new PowerStatusReceiver();
        this.handler = new Handler();
    }

    public void activity() {
        cancelCallback();
        if (this.onBattery) {
            this.handler.postDelayed(this.callback, 300000);
        }
    }

    public void start() {
        registerReceiver();
        activity();
    }

    public void cancel() {
        cancelCallback();
        unregisterReceiver();
    }

    private void unregisterReceiver() {
        if (this.registered) {
            this.context.unregisterReceiver(this.powerStatusReceiver);
            this.registered = false;
        }
    }

    private void registerReceiver() {
        if (!this.registered) {
            this.context.registerReceiver(this.powerStatusReceiver, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
            this.registered = true;
        }
    }

    private void cancelCallback() {
        this.handler.removeCallbacksAndMessages(null);
    }

    /* access modifiers changed from: private */
    public void onBattery(boolean onBattery2) {
        this.onBattery = onBattery2;
        if (this.registered) {
            activity();
        }
    }

    private final class PowerStatusReceiver extends BroadcastReceiver {
        private PowerStatusReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if ("android.intent.action.BATTERY_CHANGED".equals(intent.getAction())) {
                final boolean onBatteryNow = intent.getIntExtra("plugged", -1) <= 0;
                InactivityTimer.this.handler.post(new Runnable() {
                    /* class com.dji.scan.zxing.InactivityTimer.PowerStatusReceiver.AnonymousClass1 */

                    public void run() {
                        InactivityTimer.this.onBattery(onBatteryNow);
                    }
                });
            }
        }
    }
}
