package com.dji.mapkit.lbs.provider;

import android.location.Location;
import android.support.annotation.NonNull;
import com.amap.api.location.AMapLocationClientOption;
import com.dji.mapkit.core.models.DJILatLng;
import com.dji.mapkit.core.utils.DJIGpsUtils;
import com.dji.mapkit.lbs.DJILocationLog;
import com.dji.mapkit.lbs.listener.FallbackListener;
import com.dji.mapkit.lbs.provider.AMapServiceLocationSource;
import java.lang.ref.WeakReference;

public class AMapServiceLocationProvider extends LocationProvider implements AMapServiceLocationSource.SourceListener {
    private static final String TAG = AMapServiceLocationProvider.class.getSimpleName();
    private AMapServiceLocationSource amapServiceLocationSource;
    private int failedConnectionIteration = 0;
    private final WeakReference<FallbackListener> fallbackListener;

    public AMapServiceLocationProvider(FallbackListener fallbackListener2) {
        this.fallbackListener = new WeakReference<>(fallbackListener2);
    }

    public void onResume() {
        super.onResume();
        getSourceProvider().startLocation();
    }

    public void onPause() {
        super.onPause();
        getSourceProvider().stopLocation();
    }

    public void onDestroy() {
        super.onDestroy();
        cancel();
    }

    @NonNull
    public void onLocationChanged(DJILatLng location) {
        if (getListener() != null) {
            getListener().onLocationChanged(DJIGpsUtils.gcj2wgsInChina(location));
        }
        setWaiting(false);
    }

    public void onConnectionFailed(int errorCode) {
        if (!getSourceProvider().isCanceled()) {
            getSourceProvider().stopLocation();
            getSourceProvider().unregisterLocationListener();
            if (this.failedConnectionIteration < getConfiguration().amapServiceConfiguration().suspendedConnectionRetryCount()) {
                DJILocationLog.LOGI("AMapLocationClient failed, try to connect again. errorCode=" + errorCode);
                this.failedConnectionIteration++;
                getSourceProvider().registerLocationListener();
                getSourceProvider().startLocation();
                return;
            }
            DJILocationLog.LOGI("AMapLocationClient failed, calling fail...");
            failed(6);
        }
    }

    /* access modifiers changed from: package-private */
    public void failed(int type) {
        if (getConfiguration().amapServiceConfiguration().fallbackToDefault() && this.fallbackListener.get() != null) {
            this.fallbackListener.get().onFallback();
        } else if (getListener() != null) {
            getListener().onLocationFailed(type);
        }
        setWaiting(false);
    }

    public void get() {
        setWaiting(true);
        getSourceProvider().registerLocationListener();
        getSourceProvider().startLocation();
        if (getListener() != null) {
            getListener().onProcessTypeChanged(1);
        }
    }

    public DJILatLng getLastKnownLocation() {
        Location location = getSourceProvider().getLastKnownLocation();
        if (location != null) {
            return DJIGpsUtils.gcj2wgsInChina(new DJILatLng(location.getLatitude(), location.getLongitude(), location.getAltitude(), location.getAccuracy(), location.getTime(), location.getElapsedRealtimeNanos()));
        }
        return null;
    }

    public void setWifiScanEnable(boolean enable) {
        AMapLocationClientOption clientOption = getConfiguration().amapServiceConfiguration().clientOption();
        if (enable) {
            clientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            clientOption.setGpsFirst(false).setWifiActiveScan(true);
            clientOption.setWifiScan(true);
            return;
        }
        clientOption.setGpsFirst(true).setWifiActiveScan(false);
        clientOption.setWifiScan(false);
    }

    public void cancel() {
        DJILocationLog.LOGI("AMapServiceLocationProvider: cancel()");
        getSourceProvider().unregisterLocationListener();
        getSourceProvider().stopLocation();
        getSourceProvider().destroy();
    }

    private AMapServiceLocationSource getSourceProvider() {
        if (this.amapServiceLocationSource == null) {
            DJILocationLog.LOGI("AMapServiceLocationProvider: getSourceProvider()");
            this.amapServiceLocationSource = new AMapServiceLocationSource(getContext(), getConfiguration().amapServiceConfiguration().clientOption(), this);
        }
        return this.amapServiceLocationSource;
    }
}
