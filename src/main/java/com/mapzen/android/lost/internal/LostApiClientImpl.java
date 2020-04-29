package com.mapzen.android.lost.internal;

import android.content.Context;
import com.mapzen.android.lost.api.LocationServices;
import com.mapzen.android.lost.api.LostApiClient;

public class LostApiClientImpl implements LostApiClient {
    private final ClientManager clientManager;
    private LostApiClient.ConnectionCallbacks connectionCallbacks;
    private final Context context;

    public LostApiClientImpl(Context context2, LostApiClient.ConnectionCallbacks callbacks, ClientManager clientManager2) {
        this.context = context2;
        this.connectionCallbacks = callbacks;
        this.clientManager = clientManager2;
    }

    public void connect() {
        this.clientManager.addClient(this);
        GeofencingApiImpl geofencingApi = getGeofencingImpl();
        if (!geofencingApi.isConnected()) {
            geofencingApi.connect(this.context);
        }
        SettingsApiImpl settingsApi = getSettingsApiImpl();
        if (!settingsApi.isConnected()) {
            settingsApi.connect(this.context);
        }
        FusedLocationProviderApiImpl fusedApi = getFusedLocationProviderApiImpl();
        if (fusedApi.isConnected()) {
            if (this.connectionCallbacks != null) {
                this.connectionCallbacks.onConnected();
                fusedApi.addConnectionCallbacks(this.connectionCallbacks);
            }
        } else if (!fusedApi.isConnecting()) {
            fusedApi.connect(this.context, this.connectionCallbacks);
        } else if (this.connectionCallbacks != null) {
            fusedApi.addConnectionCallbacks(this.connectionCallbacks);
        }
    }

    public void disconnect() {
        getFusedLocationProviderApiImpl().removeConnectionCallbacks(this.connectionCallbacks);
        this.clientManager.removeClient(this);
        if (this.clientManager.numberOfClients() <= 0) {
            getSettingsApiImpl().disconnect();
            getGeofencingImpl().disconnect();
            getFusedLocationProviderApiImpl().disconnect();
        }
    }

    public boolean isConnected() {
        return getGeofencingImpl().isConnected() && getSettingsApiImpl().isConnected() && getFusedLocationProviderApiImpl().isConnected() && this.clientManager.containsClient(this);
    }

    public void unregisterConnectionCallbacks(LostApiClient.ConnectionCallbacks callbacks) {
        getFusedLocationProviderApiImpl().removeConnectionCallbacks(this.connectionCallbacks);
        this.connectionCallbacks = null;
    }

    private GeofencingApiImpl getGeofencingImpl() {
        return (GeofencingApiImpl) LocationServices.GeofencingApi;
    }

    private SettingsApiImpl getSettingsApiImpl() {
        return (SettingsApiImpl) LocationServices.SettingsApi;
    }

    private FusedLocationProviderApiImpl getFusedLocationProviderApiImpl() {
        return (FusedLocationProviderApiImpl) LocationServices.FusedLocationApi;
    }
}
