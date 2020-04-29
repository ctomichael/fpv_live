package com.dji.mapkit.lbs.provider;

import android.content.Context;
import android.location.Location;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.dji.mapkit.core.models.DJILatLng;
import com.dji.mapkit.core.utils.DJIMapLocationUtils;
import com.dji.mapkit.lbs.DJILocationLog;

public class AMapServiceLocationSource implements AMapLocationListener {
    private AMapLocationClient amapLocationClient;
    private AMapLocationClientOption amapLocationClientOption;
    private boolean isCanceled = false;
    private Location mLastLocation = null;
    private final SourceListener sourceListener;

    interface SourceListener {
        void onConnectionFailed(int i);

        void onLocationChanged(DJILatLng dJILatLng);
    }

    public AMapServiceLocationSource(Context context, AMapLocationClientOption amapLocationClientOption2, SourceListener sourceListener2) {
        this.amapLocationClientOption = amapLocationClientOption2;
        this.sourceListener = sourceListener2;
        this.amapLocationClient = new AMapLocationClient(context);
    }

    /* access modifiers changed from: package-private */
    public void startLocation() {
        this.amapLocationClient.setLocationOption(this.amapLocationClientOption);
        this.amapLocationClient.startLocation();
        this.isCanceled = false;
    }

    /* access modifiers changed from: package-private */
    public void stopLocation() {
        this.amapLocationClient.stopLocation();
    }

    /* access modifiers changed from: package-private */
    public void registerLocationListener() {
        this.amapLocationClient.setLocationListener(this);
    }

    /* access modifiers changed from: package-private */
    public void unregisterLocationListener() {
        this.amapLocationClient.unRegisterLocationListener(this);
    }

    /* access modifiers changed from: package-private */
    public void destroy() {
        DJILocationLog.LOGI("AMap destroyed");
        this.amapLocationClient.onDestroy();
        this.isCanceled = true;
    }

    /* access modifiers changed from: package-private */
    public boolean isCanceled() {
        return this.isCanceled;
    }

    /* access modifiers changed from: package-private */
    public AMapLocation getLastKnownLocation() {
        this.amapLocationClient.setLocationOption(this.amapLocationClientOption);
        return this.amapLocationClient.getLastKnownLocation();
    }

    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation == null) {
            return;
        }
        if (aMapLocation.getErrorCode() == 0) {
            AMapLocation location = (AMapLocation) DJIMapLocationUtils.getBestLocation(this.mLastLocation, aMapLocation);
            if (this.mLastLocation != location) {
                this.mLastLocation = location;
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                double altitude = location.getAltitude();
                float horizontalAccuracy = location.getAccuracy();
                if (horizontalAccuracy == 0.0f && this.amapLocationClient.getLastKnownLocation() != null) {
                    horizontalAccuracy = this.amapLocationClient.getLastKnownLocation().getAccuracy();
                }
                this.sourceListener.onLocationChanged(new DJILatLng(latitude, longitude, altitude, horizontalAccuracy));
                return;
            }
            return;
        }
        this.sourceListener.onConnectionFailed(aMapLocation.getErrorCode());
    }
}
