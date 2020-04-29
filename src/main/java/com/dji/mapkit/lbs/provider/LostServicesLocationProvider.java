package com.dji.mapkit.lbs.provider;

import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.dji.mapkit.core.models.DJILatLng;
import com.dji.mapkit.lbs.DJILocationLog;
import com.dji.mapkit.lbs.listener.FallbackListener;
import com.dji.mapkit.lbs.provider.LostServicesLocationSource;
import com.mapzen.android.lost.api.LocationSettingsResult;
import com.mapzen.android.lost.api.Status;
import java.lang.ref.WeakReference;

public class LostServicesLocationProvider extends LocationProvider implements LostServicesLocationSource.SourceListener {
    private final WeakReference<FallbackListener> fallbackListener;
    private LostServicesLocationSource lostServicesLocationSource;
    private boolean settingsDialogIsOn = false;
    private int suspendedConnectionIteration = 0;
    private boolean waitingForConnectionToRequestLocationUpdate = true;

    LostServicesLocationProvider(FallbackListener fallbackListener2) {
        this.fallbackListener = new WeakReference<>(fallbackListener2);
    }

    public void onResume() {
        super.onResume();
        if (!this.settingsDialogIsOn && this.lostServicesLocationSource != null) {
            if (isWaiting() || getConfiguration().keepTracking()) {
                this.lostServicesLocationSource.connectLostApiClient();
            }
        }
    }

    public void onPause() {
        super.onPause();
        if (!this.settingsDialogIsOn && this.lostServicesLocationSource != null && this.lostServicesLocationSource.isLostApiClientConnected()) {
            this.lostServicesLocationSource.disconnectLostApiClient();
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.lostServicesLocationSource != null) {
            this.lostServicesLocationSource.clearLostApiClient();
        }
    }

    public void get() {
        setWaiting(true);
        if (getContext() != null) {
            getSourceProvider().connectLostApiClient();
        } else {
            failed(8);
        }
    }

    @Nullable
    public DJILatLng getLastKnownLocation() {
        Location location = getSourceProvider().getLastLocation();
        if (location != null) {
            return new DJILatLng(location.getLatitude(), location.getLongitude(), location.getAltitude(), location.getAccuracy(), location.getTime(), location.getElapsedRealtimeNanos());
        }
        return null;
    }

    public void setWifiScanEnable(boolean enable) {
    }

    public void cancel() {
        DJILocationLog.LOGI("Cancelling LostServiceLocationProvider...");
        if (this.lostServicesLocationSource != null && this.lostServicesLocationSource.isLostApiClientConnected()) {
            this.lostServicesLocationSource.removeLocationUpdates();
            this.lostServicesLocationSource.disconnectLostApiClient();
        }
    }

    public void onConnected() {
        DJILocationLog.LOGI("LostApiClient is connected.");
        boolean locationIsAlreadyAvailable = false;
        if (getConfiguration().lostServicesConfiguration().ignoreLastKnownLocation()) {
            DJILocationLog.LOGI("Configuration requires to ignore last known location from LostServices Api.");
        } else {
            locationIsAlreadyAvailable = checkLastKnownLocation();
        }
        if (getConfiguration().keepTracking() || !locationIsAlreadyAvailable || this.waitingForConnectionToRequestLocationUpdate) {
            this.waitingForConnectionToRequestLocationUpdate = false;
            locationRequired();
            return;
        }
        DJILocationLog.LOGI("We got location, no need to ask for location updates.");
    }

    public void onConnectionSuspended() {
        if (getConfiguration().lostServicesConfiguration().failOnConnectionSuspended() || this.suspendedConnectionIteration >= getConfiguration().lostServicesConfiguration().suspendedConnectionRetryCount()) {
            DJILocationLog.LOGI("LostApiClient connection is suspended, calling fail...");
            failed(9);
            return;
        }
        DJILocationLog.LOGI("LostApiClient connection is suspended, try to connect again.");
        this.suspendedConnectionIteration++;
        getSourceProvider().connectLostApiClient();
    }

    public void onResult(@NonNull LocationSettingsResult result) {
        Status status = result.getStatus();
        switch (status.getStatusCode()) {
            case 0:
                DJILocationLog.LOGI("We got GPS, Wifi and/or Cell network providers enabled enough to receive location as we needed. Requesting location update...");
                requestLocationUpdate();
                return;
            case 6:
                resolveSettingsApi(status);
                return;
            case 8502:
                DJILocationLog.LOGE("Settings change is not available, SettingsApi failing...");
                settingsApiFail(11);
                return;
            default:
                return;
        }
    }

    /* access modifiers changed from: package-private */
    public void resolveSettingsApi(Status status) {
        try {
            DJILocationLog.LOGI("We need settingsApi dialog to switch required settings on.");
            if (getDialog() != null) {
                getSourceProvider().startSettingsApiResolutionForResult(getDialog());
            } else if (getActivity() != null) {
                getSourceProvider().startSettingsApiResolutionForResult(status, getActivity());
                this.settingsDialogIsOn = true;
            } else {
                DJILocationLog.LOGI("Settings Api cannot show dialog if LocationManager is not running on an activity!");
                settingsApiFail(12);
            }
        } catch (IntentSender.SendIntentException e) {
            DJILocationLog.LOGE("Error on displaying SettingsApi dialog, SettingsApi failing...");
            settingsApiFail(11);
        }
    }

    /* access modifiers changed from: package-private */
    public void settingsApiFail(int failType) {
        if (getConfiguration().lostServicesConfiguration().failOnSettingsApiSuspended()) {
            failed(failType);
            return;
        }
        DJILocationLog.LOGE("Even though settingsApi failed, configuration requires moving on.So requesting location update...");
        if (getSourceProvider().isLostApiClientConnected()) {
            requestLocationUpdate();
            return;
        }
        DJILocationLog.LOGE("LostApiClient is not connected. Aborting...");
        failed(failType);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            this.settingsDialogIsOn = false;
            DJILocationLog.LOGI("We don't known whether user changed settings, just requesting location update...");
            requestLocationUpdate();
        }
    }

    public void onLocationChanged(Location location) {
        DJILatLng latLng = new DJILatLng(location.getLatitude(), location.getLongitude(), location.getAltitude(), location.getAccuracy(), location.getTime(), location.getElapsedRealtimeNanos());
        if (getListener() != null) {
            getListener().onLocationChanged(latLng);
        }
        setWaiting(false);
        if (!getConfiguration().keepTracking() && getSourceProvider().isLostApiClientConnected()) {
            DJILocationLog.LOGI("We got location and no need to keep tracking, so location update is removed.");
            getSourceProvider().removeLocationUpdates();
        }
    }

    /* access modifiers changed from: package-private */
    public boolean checkLastKnownLocation() {
        if (getSourceProvider().getLocationAvailability()) {
            Location lastKnownLocation = getSourceProvider().getLastLocation();
            if (lastKnownLocation != null) {
                DJILocationLog.LOGI("LastKnownLocation is available.");
                onLocationChanged(lastKnownLocation);
                return true;
            }
            DJILocationLog.LOGI("LastKnownLocation is not available.");
        } else {
            DJILocationLog.LOGI("LastKnownLocation is not available.");
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public void locationRequired() {
        DJILocationLog.LOGI("Ask for location update...");
        if (getConfiguration().lostServicesConfiguration().askForSettingsApi()) {
            DJILocationLog.LOGI("Asking for SettingApi...");
            getSourceProvider().checkLocationSettings();
            return;
        }
        DJILocationLog.LOGI("SettingApi is not enabled, requesting for location update...");
        requestLocationUpdate();
    }

    /* access modifiers changed from: package-private */
    public void requestLocationUpdate() {
        if (getListener() != null) {
            getListener().onProcessTypeChanged(6);
        }
        if (getSourceProvider().isLostApiClientConnected()) {
            DJILocationLog.LOGI("Requesting location update...");
            getSourceProvider().requestLocationUpdate();
            return;
        }
        DJILocationLog.LOGI("Tried to requestLocationUpdate, but LostApiClient wasn't connected. Trying to connect...");
        this.waitingForConnectionToRequestLocationUpdate = true;
        getSourceProvider().connectLostApiClient();
    }

    /* access modifiers changed from: package-private */
    public void failed(int type) {
        if (getConfiguration().lostServicesConfiguration().fallbackToDefault() && this.fallbackListener != null) {
            this.fallbackListener.get().onFallback();
        } else if (getListener() != null) {
            getListener().onLocationFailed(type);
        }
        setWaiting(false);
    }

    private LostServicesLocationSource getSourceProvider() {
        if (this.lostServicesLocationSource == null) {
            this.lostServicesLocationSource = new LostServicesLocationSource(getContext(), getConfiguration().lostServicesConfiguration().locationRequest(), this);
        }
        return this.lostServicesLocationSource;
    }
}
