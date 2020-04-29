package com.dji.mapkit.lbs.provider;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import com.dji.mapkit.lbs.helper.continuoustask.ContinuousTask;
import com.dji.mapkit.lbs.listener.FallbackListener;
import com.google.android.gms.common.GoogleApiAvailability;

class DispatcherLocationSource {
    static final String AMAP_SERVICES_SWITCH_TASK = "amapServiceSwitchTask";
    static final String GOOGLE_PLAY_SERVICE_SWITCH_TASK = "googlePlayServiceSwitchTask";
    static final String LOST_SERVICE_SWITCH_TASK = "lostServiceSwitchTask";
    private ContinuousTask amapServiceSwitchTask;
    private ContinuousTask gpServicesSwitchTask;
    private ContinuousTask lostServiceSwitchTask;

    DispatcherLocationSource() {
    }

    /* access modifiers changed from: package-private */
    public GooglePlayServicesLocationProvider createGooglePlayServicesLocationProvider(FallbackListener fallbackListener) {
        return new GooglePlayServicesLocationProvider(fallbackListener);
    }

    /* access modifiers changed from: package-private */
    public DefaultLocationProvider createDefaultLocationProvider() {
        return new DefaultLocationProvider();
    }

    /* access modifiers changed from: package-private */
    public AMapServiceLocationProvider createAMapServiceLocationProvider(FallbackListener fallbackListener) {
        return new AMapServiceLocationProvider(fallbackListener);
    }

    /* access modifiers changed from: package-private */
    public LostServicesLocationProvider createLostServicesLocationProvider(FallbackListener fallbackListener) {
        return new LostServicesLocationProvider(fallbackListener);
    }

    /* access modifiers changed from: package-private */
    public void createSwitchTask(ContinuousTask.ContinuousTaskRunner continuousTaskRunner) {
        this.gpServicesSwitchTask = new ContinuousTask(GOOGLE_PLAY_SERVICE_SWITCH_TASK, continuousTaskRunner);
        this.amapServiceSwitchTask = new ContinuousTask(AMAP_SERVICES_SWITCH_TASK, continuousTaskRunner);
        this.lostServiceSwitchTask = new ContinuousTask(LOST_SERVICE_SWITCH_TASK, continuousTaskRunner);
    }

    /* access modifiers changed from: package-private */
    public ContinuousTask gpServicesSwitchTask() {
        return this.gpServicesSwitchTask;
    }

    /* access modifiers changed from: package-private */
    public ContinuousTask amapServiceSwitchTask() {
        return this.amapServiceSwitchTask;
    }

    /* access modifiers changed from: package-private */
    public ContinuousTask lostServiceSwitchTask() {
        return this.lostServiceSwitchTask;
    }

    /* access modifiers changed from: package-private */
    public int isGoogleApiAvailable(Context context) {
        if (context == null) {
            return -1;
        }
        return GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context);
    }

    /* access modifiers changed from: package-private */
    public boolean isGoogleApiErrorUserResolvable(int gpServicesAvailability) {
        return GoogleApiAvailability.getInstance().isUserResolvableError(gpServicesAvailability);
    }

    /* access modifiers changed from: package-private */
    @Nullable
    public Dialog getGoogleApiErrorDialog(Activity activity, int gpServicesAvailability, int requestCode, DialogInterface.OnCancelListener onCancelListener) {
        if (activity == null) {
            return null;
        }
        return GoogleApiAvailability.getInstance().getErrorDialog(activity, gpServicesAvailability, requestCode, onCancelListener);
    }
}
