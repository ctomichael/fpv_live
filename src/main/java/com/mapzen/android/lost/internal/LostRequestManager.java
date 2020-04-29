package com.mapzen.android.lost.internal;

import android.app.PendingIntent;
import com.mapzen.android.lost.api.LocationCallback;
import com.mapzen.android.lost.api.LocationListener;
import com.mapzen.android.lost.api.LocationRequest;
import com.mapzen.android.lost.api.LostApiClient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LostRequestManager implements RequestManager {
    private static LostRequestManager instance;
    private Map<ClientCallbackWrapper, List<LocationRequest>> clientCallbackToLocationRequests = new HashMap();

    LostRequestManager() {
    }

    public static LostRequestManager shared() {
        if (instance == null) {
            instance = new LostRequestManager();
        }
        return instance;
    }

    public void requestLocationUpdates(LostApiClient client, LocationRequest request, LocationListener listener) {
        registerRequest(getWrapper(client, listener), request);
    }

    public void requestLocationUpdates(LostApiClient client, LocationRequest request, LocationCallback callback) {
        registerRequest(getWrapper(client, callback), request);
    }

    public void requestLocationUpdates(LostApiClient client, LocationRequest request, PendingIntent callbackIntent) {
        registerRequest(getWrapper(client, callbackIntent), request);
    }

    public List<LocationRequest> removeLocationUpdates(LostApiClient client, LocationListener listener) {
        return getRequestOnlyUsedBy(getWrapper(client, listener));
    }

    public List<LocationRequest> removeLocationUpdates(LostApiClient client, PendingIntent callbackIntent) {
        return getRequestOnlyUsedBy(getWrapper(client, callbackIntent));
    }

    public List<LocationRequest> removeLocationUpdates(LostApiClient client, LocationCallback callback) {
        return getRequestOnlyUsedBy(getWrapper(client, callback));
    }

    private <T> ClientCallbackWrapper getWrapper(LostApiClient client, T callback) {
        return new ClientCallbackWrapper(client, callback);
    }

    private void registerRequest(ClientCallbackWrapper wrapper, LocationRequest request) {
        List<LocationRequest> requests = this.clientCallbackToLocationRequests.get(wrapper);
        if (requests == null) {
            requests = new ArrayList<>();
            this.clientCallbackToLocationRequests.put(wrapper, requests);
        }
        requests.add(new LocationRequest(request));
    }

    private List<LocationRequest> getRequestOnlyUsedBy(ClientCallbackWrapper wrapper) {
        List<LocationRequest> requestsToRemove = this.clientCallbackToLocationRequests.get(wrapper);
        if (requestsToRemove == null) {
            return null;
        }
        this.clientCallbackToLocationRequests.remove(wrapper);
        return requestsToRemove;
    }

    /* access modifiers changed from: package-private */
    public Map<ClientCallbackWrapper, List<LocationRequest>> getClientCallbackMap() {
        return this.clientCallbackToLocationRequests;
    }
}
