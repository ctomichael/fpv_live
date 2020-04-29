package com.dji.mapkit.lbs.provider;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;

class GooglePlayServicesLocationSource implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<LocationSettingsResult> {
    private final GoogleApiClient googleApiClient;
    private final LocationRequest locationRequest;
    private final SourceListener sourceListener;

    interface SourceListener {
        void onConnected(Bundle bundle);

        void onConnectionFailed(@NonNull ConnectionResult connectionResult);

        void onConnectionSuspended(int i);

        void onLocationChanged(Location location);

        void onResult(@NonNull LocationSettingsResult locationSettingsResult);
    }

    GooglePlayServicesLocationSource(Context context, LocationRequest locationRequest2, SourceListener sourceListener2) {
        this.sourceListener = sourceListener2;
        this.locationRequest = locationRequest2;
        this.googleApiClient = new GoogleApiClient.Builder(context).addApi(LocationServices.API).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
    }

    /* access modifiers changed from: package-private */
    public boolean isGoogleApiClientConnected() {
        return this.googleApiClient.isConnected();
    }

    /* access modifiers changed from: package-private */
    public void connectGoogleApiClient() {
        this.googleApiClient.connect();
    }

    /* access modifiers changed from: package-private */
    public void disconnectGoogleApiClient() {
        this.googleApiClient.disconnect();
    }

    /* access modifiers changed from: package-private */
    public void clearGoogleApiClient() {
        this.googleApiClient.unregisterConnectionCallbacks(this);
        this.googleApiClient.unregisterConnectionFailedListener(this);
        if (this.googleApiClient.isConnected()) {
            removeLocationUpdates();
        }
        this.googleApiClient.disconnect();
    }

    /* access modifiers changed from: package-private */
    public void checkLocationSettings() {
        LocationServices.SettingsApi.checkLocationSettings(this.googleApiClient, new LocationSettingsRequest.Builder().addLocationRequest(this.locationRequest).build()).setResultCallback(this);
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
        LocationServices.FusedLocationApi.requestLocationUpdates(this.googleApiClient, this.locationRequest, this);
    }

    /* access modifiers changed from: package-private */
    public void removeLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(this.googleApiClient, this);
    }

    /* access modifiers changed from: package-private */
    public boolean getLocationAvailability() {
        LocationAvailability locationAvailability = LocationServices.FusedLocationApi.getLocationAvailability(this.googleApiClient);
        return locationAvailability != null && locationAvailability.isLocationAvailable();
    }

    /* access modifiers changed from: package-private */
    public Location getLastLocation() {
        if (!this.googleApiClient.isConnected()) {
            connectGoogleApiClient();
        }
        return LocationServices.FusedLocationApi.getLastLocation(this.googleApiClient);
    }

    public void onConnected(Bundle bundle) {
        if (this.sourceListener != null) {
            this.sourceListener.onConnected(bundle);
        }
    }

    public void onConnectionSuspended(int i) {
        if (this.sourceListener != null) {
            this.sourceListener.onConnectionSuspended(i);
        }
    }

    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (this.sourceListener != null) {
            this.sourceListener.onConnectionFailed(connectionResult);
        }
    }

    public void onLocationChanged(Location location) {
        if (this.sourceListener != null) {
            this.sourceListener.onLocationChanged(location);
        }
    }

    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
        if (this.sourceListener != null) {
            this.sourceListener.onResult(locationSettingsResult);
        }
    }
}
