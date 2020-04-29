package com.dji.mapkit.lbs.provider;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.IntentSender;
import android.location.Location;
import android.support.annotation.NonNull;
import com.mapzen.android.lost.api.LocationAvailability;
import com.mapzen.android.lost.api.LocationListener;
import com.mapzen.android.lost.api.LocationRequest;
import com.mapzen.android.lost.api.LocationServices;
import com.mapzen.android.lost.api.LocationSettingsRequest;
import com.mapzen.android.lost.api.LocationSettingsResult;
import com.mapzen.android.lost.api.LostApiClient;
import com.mapzen.android.lost.api.ResultCallback;
import com.mapzen.android.lost.api.Status;

class LostServicesLocationSource implements LocationListener, LostApiClient.ConnectionCallbacks, ResultCallback<LocationSettingsResult> {
    private final LocationRequest locationRequest;
    private final LostApiClient lostApiClient;
    private final SourceListener sourceListener;

    interface SourceListener {
        void onConnected();

        void onConnectionSuspended();

        void onLocationChanged(Location location);

        void onResult(@NonNull LocationSettingsResult locationSettingsResult);
    }

    LostServicesLocationSource(Context context, LocationRequest locationRequest2, SourceListener sourceListener2) {
        this.sourceListener = sourceListener2;
        this.locationRequest = locationRequest2;
        this.lostApiClient = new LostApiClient.Builder(context).addConnectionCallbacks(this).build();
    }

    /* access modifiers changed from: package-private */
    public boolean isLostApiClientConnected() {
        return this.lostApiClient.isConnected();
    }

    /* access modifiers changed from: package-private */
    public void connectLostApiClient() {
        this.lostApiClient.connect();
    }

    /* access modifiers changed from: package-private */
    public void disconnectLostApiClient() {
        this.lostApiClient.disconnect();
    }

    /* access modifiers changed from: package-private */
    public void clearLostApiClient() {
        this.lostApiClient.unregisterConnectionCallbacks(this);
        if (this.lostApiClient.isConnected()) {
            removeLocationUpdates();
        }
        this.lostApiClient.disconnect();
    }

    /* access modifiers changed from: package-private */
    public void checkLocationSettings() {
        LocationServices.SettingsApi.checkLocationSettings(this.lostApiClient, new LocationSettingsRequest.Builder().addLocationRequest(this.locationRequest).build()).setResultCallback(this);
    }

    /* access modifiers changed from: package-private */
    public void startSettingsApiResolutionForResult(Status status, Activity activity) throws IntentSender.SendIntentException {
        status.startResolutionForResult(activity, 2);
    }

    /* access modifiers changed from: package-private */
    public void startSettingsApiResolutionForResult(Dialog rationale) {
        rationale.show();
    }

    /* access modifiers changed from: package-private */
    public void requestLocationUpdate() {
        LocationServices.FusedLocationApi.requestLocationUpdates(this.lostApiClient, this.locationRequest, this);
    }

    /* access modifiers changed from: package-private */
    public void removeLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(this.lostApiClient, this);
    }

    /* access modifiers changed from: package-private */
    public boolean getLocationAvailability() {
        LocationAvailability locationAvailability = LocationServices.FusedLocationApi.getLocationAvailability(this.lostApiClient);
        return locationAvailability != null && locationAvailability.isLocationAvailable();
    }

    /* access modifiers changed from: package-private */
    public Location getLastLocation() {
        if (this.lostApiClient.isConnected()) {
            return LocationServices.FusedLocationApi.getLastLocation(this.lostApiClient);
        }
        connectLostApiClient();
        return null;
    }

    public void onLocationChanged(Location location) {
        if (this.sourceListener != null) {
            this.sourceListener.onLocationChanged(location);
        }
    }

    public void onConnected() {
        if (this.sourceListener != null) {
            this.sourceListener.onConnected();
        }
    }

    public void onConnectionSuspended() {
        if (this.sourceListener != null) {
            this.sourceListener.onConnectionSuspended();
        }
    }

    public void onResult(@NonNull LocationSettingsResult result) {
        if (this.sourceListener != null) {
            this.sourceListener.onResult(result);
        }
    }
}
