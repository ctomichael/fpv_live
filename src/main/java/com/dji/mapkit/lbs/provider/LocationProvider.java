package com.dji.mapkit.lbs.provider;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import com.dji.mapkit.core.models.DJILatLng;
import com.dji.mapkit.lbs.DJILocationLog;
import com.dji.mapkit.lbs.configuration.LocationConfiguration;
import com.dji.mapkit.lbs.listener.DJILocationListener;
import com.dji.mapkit.lbs.view.ContextProcessor;
import dji.publics.protocol.ResponseBase;
import java.lang.ref.WeakReference;

public abstract class LocationProvider extends BroadcastReceiver {
    private static final String PROVIDERS_ACTION = "android.location.PROVIDERS_CHANGED";
    private LocationConfiguration configuration;
    private ContextProcessor contextProcessor;
    private boolean isWaiting = false;
    protected int provider = 0;
    private WeakReference<DJILocationListener> weakLocationListener;

    public abstract void cancel();

    public abstract void get();

    @Nullable
    public abstract DJILatLng getLastKnownLocation();

    public abstract void setWifiScanEnable(boolean z);

    @CallSuper
    public void configure(ContextProcessor contextProcessor2, LocationConfiguration configuration2, DJILocationListener listener) {
        this.configuration = configuration2;
        this.contextProcessor = contextProcessor2;
        this.weakLocationListener = new WeakReference<>(listener);
        contextProcessor2.getContext().registerReceiver(this, new IntentFilter(PROVIDERS_ACTION));
        initialize();
    }

    @CallSuper
    public void configure(LocationProvider locationProvider) {
        this.configuration = locationProvider.configuration;
        this.contextProcessor = locationProvider.contextProcessor;
        this.weakLocationListener = locationProvider.weakLocationListener;
        initialize();
    }

    public void setWaiting(boolean waiting) {
        this.isWaiting = waiting;
    }

    public boolean isWaiting() {
        return this.isWaiting;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    @CallSuper
    public void onDestroy() {
        DJILocationLog.LOGI("provider destroy");
        this.weakLocationListener.clear();
        try {
            this.contextProcessor.getContext().unregisterReceiver(this);
        } catch (IllegalArgumentException e) {
        }
    }

    public void onPause() {
        DJILocationLog.LOGI("provider pause");
    }

    public void onResume() {
        DJILocationLog.LOGI("provider resume");
    }

    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().matches(PROVIDERS_ACTION) && getListener() != null) {
            if (isGpsProviderEnabled()) {
                getListener().onProviderEnabled("gps");
            } else {
                getListener().onProviderDisabled("gps");
            }
            if (isNetworkProviderEnabled()) {
                getListener().onProviderEnabled("network");
            } else {
                getListener().onProviderDisabled("network");
            }
        }
    }

    /* access modifiers changed from: protected */
    public LocationConfiguration getConfiguration() {
        return this.configuration;
    }

    /* access modifiers changed from: protected */
    @Nullable
    public DJILocationListener getListener() {
        return this.weakLocationListener.get();
    }

    /* access modifiers changed from: protected */
    @Nullable
    public Context getContext() {
        return this.contextProcessor.getContext();
    }

    /* access modifiers changed from: protected */
    @Nullable
    public Activity getActivity() {
        return this.contextProcessor.getActivity();
    }

    /* access modifiers changed from: protected */
    @Nullable
    public Fragment getFragment() {
        return this.contextProcessor.getFragment();
    }

    /* access modifiers changed from: protected */
    @Nullable
    public Dialog getDialog() {
        return this.contextProcessor.getDialog();
    }

    public void initialize() {
    }

    private boolean isGpsProviderEnabled() {
        return ((LocationManager) getContext().getApplicationContext().getSystemService(ResponseBase.STRING_LOCATION)).isProviderEnabled("gps");
    }

    private boolean isNetworkProviderEnabled() {
        return ((LocationManager) getContext().getApplicationContext().getSystemService(ResponseBase.STRING_LOCATION)).isProviderEnabled("network");
    }
}
