package com.dji.mapkit.lbs.provider;

import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import com.dji.mapkit.core.models.DJILatLng;
import com.dji.mapkit.lbs.DJILocationLog;
import com.dji.mapkit.lbs.listener.FallbackListener;
import com.dji.mapkit.lbs.provider.GooglePlayServicesLocationSource;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationSettingsResult;
import java.lang.ref.WeakReference;

public class GooglePlayServicesLocationProvider extends LocationProvider implements GooglePlayServicesLocationSource.SourceListener {
    private final WeakReference<FallbackListener> fallbackListener;
    private GooglePlayServicesLocationSource googlePlayServicesLocationSource;
    private boolean settingsDialogIsOn = false;
    private int suspendedConnectionIteration = 0;
    private boolean waitingForConnectionToRequestLocationUpdate = true;

    GooglePlayServicesLocationProvider(FallbackListener fallbackListener2) {
        this.fallbackListener = new WeakReference<>(fallbackListener2);
    }

    public void onResume() {
        super.onResume();
        if (!this.settingsDialogIsOn && this.googlePlayServicesLocationSource != null) {
            if (isWaiting() || getConfiguration().keepTracking()) {
                this.googlePlayServicesLocationSource.connectGoogleApiClient();
            }
        }
    }

    public void onPause() {
        super.onPause();
        if (!this.settingsDialogIsOn && this.googlePlayServicesLocationSource != null && this.googlePlayServicesLocationSource.isGoogleApiClientConnected()) {
            this.googlePlayServicesLocationSource.disconnectGoogleApiClient();
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.googlePlayServicesLocationSource != null) {
            this.googlePlayServicesLocationSource.clearGoogleApiClient();
        }
    }

    public void get() {
        setWaiting(true);
        if (getContext() != null) {
            getSourceProvider().connectGoogleApiClient();
        } else {
            failed(8);
        }
    }

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
        DJILocationLog.LOGI("Cancelling GooglePlayServiceLocationProvider...");
        if (this.googlePlayServicesLocationSource != null && this.googlePlayServicesLocationSource.isGoogleApiClientConnected()) {
            this.googlePlayServicesLocationSource.removeLocationUpdates();
            this.googlePlayServicesLocationSource.disconnectGoogleApiClient();
        }
    }

    public void onConnected(Bundle bundle) {
        DJILocationLog.LOGI("GoogleApiClient is connected.");
        boolean locationIsAlreadyAvailable = false;
        if (getConfiguration().googlePlayServicesConfiguration().ignoreLastKnownLocation()) {
            DJILocationLog.LOGI("Configuration requires to ignore last known location from GooglePlayServices Api.");
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

    public void onConnectionSuspended(int i) {
        if (getConfiguration().googlePlayServicesConfiguration().failOnConnectionSuspended() || this.suspendedConnectionIteration >= getConfiguration().googlePlayServicesConfiguration().suspendedConnectionRetryCount()) {
            DJILocationLog.LOGI("GoogleApiClient connection is suspended, calling fail...");
            failed(5);
            return;
        }
        DJILocationLog.LOGI("GoogleApiClient connection is suspended, try to connect again.");
        this.suspendedConnectionIteration++;
        getSourceProvider().connectGoogleApiClient();
    }

    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        DJILocationLog.LOGI("GoogleApiClient connection is failed.");
        failed(5);
    }

    public void onResult(@NonNull LocationSettingsResult result) {
        Status status = result.getStatus();
        switch (status.getStatusCode()) {
            case 0:
                DJILocationLog.LOGI("We got GPS, Wi-Fi and/or Cell network providers enabled enough to receive location as we need. Requesting location update..");
                requestLocationUpdate();
                return;
            case 6:
                DJILocationLog.LOGI("Location settings are not satisfied. But we can fix it by showing the user a dialog.");
                resolveSettingsApi(status);
                return;
            case 8502:
                DJILocationLog.LOGI("Location settings are not satisfied. However we have no way to fix the settings.");
                settingsApiFail(14);
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
            DJILocationLog.LOGE("Error on displaying SettingApi dialog, SettingsApi failing...");
            settingsApiFail(14);
        }
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
        if (!getConfiguration().keepTracking() && getSourceProvider().isGoogleApiClientConnected()) {
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
        if (getConfiguration().googlePlayServicesConfiguration().askForSettingsApi()) {
            DJILocationLog.LOGI("Asking for SettingApi...");
            getSourceProvider().checkLocationSettings();
            return;
        }
        DJILocationLog.LOGI("SettingsApi is not enabled, requesting for location update...");
        requestLocationUpdate();
    }

    /* access modifiers changed from: package-private */
    public void requestLocationUpdate() {
        if (getListener() != null) {
            getListener().onProcessTypeChanged(2);
        }
        if (getSourceProvider().isGoogleApiClientConnected()) {
            DJILocationLog.LOGI("Requesting location update...");
            getSourceProvider().requestLocationUpdate();
            return;
        }
        DJILocationLog.LOGI("Tried to requestLocationUpdate, but GoogleApiClient wasn't connected. Trying to connect...");
        this.waitingForConnectionToRequestLocationUpdate = true;
        getSourceProvider().connectGoogleApiClient();
    }

    /* access modifiers changed from: package-private */
    public void settingsApiFail(int failType) {
        if (getConfiguration().googlePlayServicesConfiguration().failOnSettingsApiSuspended()) {
            failed(failType);
            return;
        }
        DJILocationLog.LOGE("Even though settingsApi failed, configuration requires moving on.So requesting location update...");
        if (getSourceProvider().isGoogleApiClientConnected()) {
            requestLocationUpdate();
            return;
        }
        DJILocationLog.LOGE("GoogleApiClient is not connected. Aborting...");
        failed(failType);
    }

    /* access modifiers changed from: package-private */
    public void failed(int type) {
        if (getConfiguration().googlePlayServicesConfiguration().fallbackToDefault() && this.fallbackListener != null) {
            this.fallbackListener.get().onFallback();
        } else if (getListener() != null) {
            getListener().onLocationFailed(type);
        }
        setWaiting(false);
    }

    private GooglePlayServicesLocationSource getSourceProvider() {
        if (this.googlePlayServicesLocationSource == null) {
            this.googlePlayServicesLocationSource = new GooglePlayServicesLocationSource(getContext(), getConfiguration().googlePlayServicesConfiguration().locationRequest(), this);
        }
        return this.googlePlayServicesLocationSource;
    }
}
