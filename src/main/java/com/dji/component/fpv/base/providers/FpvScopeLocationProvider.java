package com.dji.component.fpv.base.providers;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import com.dji.component.fpv.base.BulletinBoard;
import com.dji.component.fpv.base.BulletinBoardKey;
import com.dji.component.fpv.base.BulletinBoardProvider;
import com.dji.mapkit.core.models.DJILatLng;
import com.dji.mapkit.lbs.DJILocationManager;
import com.dji.mapkit.lbs.DJILocationUtils;
import com.dji.mapkit.lbs.listener.DJILocationListener;
import dji.component.map.IMapService;
import dji.log.DJILog;
import dji.publics.protocol.ResponseBase;
import dji.service.DJIAppServiceManager;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class FpvScopeLocationProvider implements BulletinBoardProvider, FpvScopeProvider, LifecycleObserver, DJILocationListener {
    private static final DJILatLng INVALID_LOCATION = new DJILatLng(0.0d, 0.0d, 0.0d, 1000.0f);
    private static final String TAG = "FpvScopeLocationProvide";
    private BulletinBoardProvider mBridge;
    private Context mContext;
    private boolean mGpsProviderEnabled;
    private final DJILocationManager mLocationManager;
    private BehaviorSubject<DJILatLng> mLocationSubject;
    private boolean mNetworkProviderEnabled;
    private BehaviorSubject<Boolean> mProvidersEnabledSubject = BehaviorSubject.create();

    public FpvScopeLocationProvider(Context context, BulletinBoardProvider bridge) {
        this.mContext = context;
        this.mBridge = bridge;
        IMapService mapService = (IMapService) DJIAppServiceManager.getInstance().getService(IMapService.COMPONENT_NAME);
        this.mLocationSubject = BehaviorSubject.createDefault(INVALID_LOCATION);
        this.mLocationManager = new DJILocationManager.Builder(context.getApplicationContext()).configuration(mapService.getDefaultConfiguration(true, mapService.isSupportingAMap())).notify(this).build();
        this.mLocationManager.isProviderGot();
        initProviderState();
    }

    public void onNecessaryComponentFinishRender() {
        DJILatLng lastKnownLocation = DJILocationUtils.getLastKnownLocation();
        if (lastKnownLocation != null) {
            this.mLocationSubject.onNext(lastKnownLocation);
            getBulletinBoard().putSerializable(BulletinBoardKey.LatLng.DJI_PILOT_LAT_LNG, lastKnownLocation);
        }
    }

    public Observable<DJILatLng> getLocationObservable() {
        return this.mLocationSubject.hide();
    }

    public Observable<Boolean> getProvidersEnabledObservable() {
        return this.mProvidersEnabledSubject.hide();
    }

    public DJILatLng getLocation() {
        return this.mLocationSubject.getValue();
    }

    public void onProcessTypeChanged(int processType) {
        DJILog.logWriteI(TAG, "onProcessTypeChanged: " + processType, new Object[0]);
    }

    public void onLocationChanged(DJILatLng latLng) {
        DJILog.logWriteI(TAG, "onLocationChanged:" + latLng, new Object[0]);
        if (latLng != null) {
            this.mLocationSubject.onNext(latLng);
            getBulletinBoard().putSerializable(BulletinBoardKey.LatLng.DJI_PILOT_LAT_LNG, latLng);
        }
    }

    public void onLocationFailed(int type) {
        DJILog.logWriteI(TAG, "onLocationFailed: " + type, new Object[0]);
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    public void onProviderEnabled(String provider) {
        boolean z = false;
        DJILog.logWriteI(TAG, "onProviderEnabled " + provider, new Object[0]);
        if (provider.equals("gps")) {
            this.mGpsProviderEnabled = true;
        }
        if (provider.equals("network")) {
            this.mNetworkProviderEnabled = true;
        }
        BehaviorSubject<Boolean> behaviorSubject = this.mProvidersEnabledSubject;
        if (this.mGpsProviderEnabled || this.mNetworkProviderEnabled) {
            z = true;
        }
        behaviorSubject.onNext(Boolean.valueOf(z));
    }

    public void onProviderDisabled(String provider) {
        boolean z = false;
        DJILog.logWriteI(TAG, "onProviderDisabled " + provider, new Object[0]);
        if (provider.equals("gps")) {
            this.mGpsProviderEnabled = false;
        }
        if (provider.equals("network")) {
            this.mNetworkProviderEnabled = false;
        }
        BehaviorSubject<Boolean> behaviorSubject = this.mProvidersEnabledSubject;
        if (this.mGpsProviderEnabled || this.mNetworkProviderEnabled) {
            z = true;
        }
        behaviorSubject.onNext(Boolean.valueOf(z));
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        this.mLocationManager.onResume();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        this.mLocationManager.onPause();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
        this.mLocationManager.onDestroy();
    }

    public void setWifiScanEnable(boolean enable) {
        this.mLocationManager.setWifiScanEnable(enable);
    }

    public BulletinBoard getBulletinBoard() {
        return this.mBridge.getBulletinBoard();
    }

    private void initProviderState() {
        LocationManager locationManager = (LocationManager) this.mContext.getApplicationContext().getSystemService(ResponseBase.STRING_LOCATION);
        this.mGpsProviderEnabled = locationManager.isProviderEnabled("gps");
        this.mNetworkProviderEnabled = locationManager.isProviderEnabled("network");
        this.mProvidersEnabledSubject.onNext(Boolean.valueOf(this.mGpsProviderEnabled || this.mNetworkProviderEnabled));
    }
}
