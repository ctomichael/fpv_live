package com.mapzen.android.lost.internal;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Process;
import android.os.RemoteException;
import android.util.Log;
import com.mapzen.android.lost.api.FusedLocationProviderApi;
import com.mapzen.android.lost.api.LocationAvailability;
import com.mapzen.android.lost.api.LocationCallback;
import com.mapzen.android.lost.api.LocationListener;
import com.mapzen.android.lost.api.LocationRequest;
import com.mapzen.android.lost.api.LostApiClient;
import com.mapzen.android.lost.api.PendingResult;
import com.mapzen.android.lost.api.Status;
import com.mapzen.android.lost.internal.FusedLocationServiceConnectionManager;
import com.mapzen.android.lost.internal.IFusedLocationProviderCallback;
import com.mapzen.android.lost.internal.IFusedLocationProviderService;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FusedLocationProviderApiImpl extends ApiImpl implements FusedLocationProviderApi, FusedLocationServiceConnectionManager.EventCallbacks, ServiceConnection {
    private static final String TAG = FusedLocationProviderApiImpl.class.getSimpleName();
    /* access modifiers changed from: private */
    public ClientManager clientManager;
    /* access modifiers changed from: private */
    public Context context;
    private boolean isBound;
    IFusedLocationProviderCallback.Stub remoteCallback = new IFusedLocationProviderCallback.Stub() {
        /* class com.mapzen.android.lost.internal.FusedLocationProviderApiImpl.AnonymousClass1 */

        public long pid() throws RemoteException {
            return (long) Process.myPid();
        }

        public void onLocationChanged(final Location location) throws RemoteException {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                /* class com.mapzen.android.lost.internal.FusedLocationProviderApiImpl.AnonymousClass1.AnonymousClass1 */

                public void run() {
                    if (FusedLocationProviderApiImpl.this.service != null) {
                        FusedLocationProviderApiImpl.this.serviceCallbackManager.onLocationChanged(FusedLocationProviderApiImpl.this.context, location, FusedLocationProviderApiImpl.this.clientManager, FusedLocationProviderApiImpl.this.service);
                    }
                }
            });
        }

        public void onLocationAvailabilityChanged(LocationAvailability locationAvailability) throws RemoteException {
            FusedLocationProviderApiImpl.this.serviceCallbackManager.onLocationAvailabilityChanged(locationAvailability, FusedLocationProviderApiImpl.this.clientManager);
        }
    };
    private RequestManager requestManager;
    IFusedLocationProviderService service;
    /* access modifiers changed from: private */
    public FusedLocationServiceCallbackManager serviceCallbackManager;
    private FusedLocationServiceConnectionManager serviceConnectionManager;

    public void onConnect(Context context2) {
        this.context = context2;
        context2.bindService(new Intent(context2, FusedLocationProviderService.class), this, 1);
    }

    public void onServiceConnected(IBinder binder) {
        this.service = IFusedLocationProviderService.Stub.asInterface(binder);
        this.isBound = true;
        registerRemoteCallback();
    }

    public void onDisconnect() {
        if (this.isBound) {
            unregisterRemoteCallback();
            this.context.unbindService(this);
            this.isBound = false;
        }
        this.service = null;
    }

    public void onServiceConnected(ComponentName name, IBinder binder) {
        this.serviceConnectionManager.onServiceConnected(binder);
        this.isBound = true;
    }

    public void onServiceDisconnected(ComponentName name) {
        this.serviceConnectionManager.onServiceDisconnected();
        this.isBound = false;
    }

    public FusedLocationProviderApiImpl(FusedLocationServiceConnectionManager connectionManager, FusedLocationServiceCallbackManager callbackManager, RequestManager requestManager2, ClientManager clientManager2) {
        this.serviceConnectionManager = connectionManager;
        this.serviceCallbackManager = callbackManager;
        this.requestManager = requestManager2;
        this.clientManager = clientManager2;
        this.serviceConnectionManager.setEventCallbacks(this);
    }

    public boolean isConnecting() {
        return this.serviceConnectionManager.isConnecting();
    }

    public void addConnectionCallbacks(LostApiClient.ConnectionCallbacks callbacks) {
        this.serviceConnectionManager.addCallbacks(callbacks);
    }

    public void connect(Context context2, LostApiClient.ConnectionCallbacks callbacks) {
        this.serviceConnectionManager.connect(context2, callbacks);
    }

    public void disconnect() {
        this.serviceConnectionManager.disconnect();
    }

    public boolean isConnected() {
        return this.serviceConnectionManager.isConnected();
    }

    public Location getLastLocation(LostApiClient client) {
        throwIfNotConnected(client);
        try {
            return this.service.getLastLocation();
        } catch (RemoteException e) {
            Log.e(TAG, "Error occurred trying to get last Location", e);
            return null;
        } catch (Throwable th) {
            return null;
        }
    }

    public LocationAvailability getLocationAvailability(LostApiClient client) {
        throwIfNotConnected(client);
        try {
            return this.service.getLocationAvailability();
        } catch (RemoteException e) {
            Log.e(TAG, "Error occurred trying to get LocationAvailability", e);
            return null;
        } catch (Throwable th) {
            return null;
        }
    }

    public PendingResult<Status> requestLocationUpdates(LostApiClient client, LocationRequest request, LocationListener listener) {
        throwIfNotConnected(client);
        this.requestManager.requestLocationUpdates(client, request, listener);
        this.clientManager.addListener(client, request, listener);
        requestLocationUpdatesInternal(request);
        return new SimplePendingResult(true);
    }

    public PendingResult<Status> requestLocationUpdates(LostApiClient client, LocationRequest request, LocationListener listener, Looper looper) {
        throw new RuntimeException("Sorry, not yet implemented");
    }

    public PendingResult<Status> requestLocationUpdates(LostApiClient client, LocationRequest request, LocationCallback callback, Looper looper) {
        throwIfNotConnected(client);
        this.requestManager.requestLocationUpdates(client, request, callback);
        this.clientManager.addLocationCallback(client, request, callback, looper);
        requestLocationUpdatesInternal(request);
        return new SimplePendingResult(true);
    }

    public PendingResult<Status> requestLocationUpdates(LostApiClient client, LocationRequest request, PendingIntent callbackIntent) {
        throwIfNotConnected(client);
        this.requestManager.requestLocationUpdates(client, request, callbackIntent);
        this.clientManager.addPendingIntent(client, request, callbackIntent);
        requestLocationUpdatesInternal(request);
        return new SimplePendingResult(true);
    }

    private void requestLocationUpdatesInternal(LocationRequest request) {
        try {
            this.service.requestLocationUpdates(request);
        } catch (RemoteException e) {
            Log.e(TAG, "Error occurred trying to request location updates", e);
        }
    }

    private void removeLocationUpdatesInternal(List<LocationRequest> requests) {
        if (requests != null) {
            try {
                this.service.removeLocationUpdates(requests);
            } catch (RemoteException e) {
                Log.e(TAG, "Error occurred trying to remove location updates", e);
            }
        }
    }

    public PendingResult<Status> removeLocationUpdates(LostApiClient client, LocationListener listener) {
        throwIfNotConnected(client);
        removeLocationUpdatesInternal(this.requestManager.removeLocationUpdates(client, listener));
        boolean hasResult = this.clientManager.removeListener(client, listener);
        checkAllListenersPendingIntentsAndCallbacks();
        return new SimplePendingResult(hasResult);
    }

    public PendingResult<Status> removeLocationUpdates(LostApiClient client, PendingIntent callbackIntent) {
        throwIfNotConnected(client);
        removeLocationUpdatesInternal(this.requestManager.removeLocationUpdates(client, callbackIntent));
        boolean hasResult = this.clientManager.removePendingIntent(client, callbackIntent);
        checkAllListenersPendingIntentsAndCallbacks();
        return new SimplePendingResult(hasResult);
    }

    public PendingResult<Status> removeLocationUpdates(LostApiClient client, LocationCallback callback) {
        throwIfNotConnected(client);
        removeLocationUpdatesInternal(this.requestManager.removeLocationUpdates(client, callback));
        boolean hasResult = this.clientManager.removeLocationCallback(client, callback);
        checkAllListenersPendingIntentsAndCallbacks();
        return new SimplePendingResult(hasResult);
    }

    private void checkAllListenersPendingIntentsAndCallbacks() {
    }

    public PendingResult<Status> setMockMode(LostApiClient client, boolean isMockMode) {
        throwIfNotConnected(client);
        try {
            this.service.setMockMode(isMockMode);
        } catch (RemoteException e) {
            Log.e(TAG, "Error occurred trying to set mock mode " + (isMockMode ? "enabled" : "disabled"), e);
        }
        return new SimplePendingResult(true);
    }

    public PendingResult<Status> setMockLocation(LostApiClient client, Location mockLocation) {
        throwIfNotConnected(client);
        try {
            this.service.setMockLocation(mockLocation);
        } catch (RemoteException e) {
            Log.e(TAG, "Error occurred trying to set mock location", e);
        }
        return new SimplePendingResult(true);
    }

    public PendingResult<Status> setMockTrace(LostApiClient client, String path, String filename) {
        throwIfNotConnected(client);
        try {
            this.service.setMockTrace(path, filename);
        } catch (RemoteException e) {
            Log.e(TAG, "Error occurred trying to set mock trace", e);
        }
        return new SimplePendingResult(true);
    }

    public Map<LostApiClient, Set<LocationListener>> getLocationListeners() {
        return this.clientManager.getLocationListeners();
    }

    /* access modifiers changed from: package-private */
    public void removeConnectionCallbacks(LostApiClient.ConnectionCallbacks callbacks) {
        this.serviceConnectionManager.removeCallbacks(callbacks);
    }

    /* access modifiers changed from: package-private */
    public FusedLocationServiceConnectionManager getServiceConnectionManager() {
        return this.serviceConnectionManager;
    }

    /* access modifiers changed from: package-private */
    public void registerRemoteCallback() {
        if (this.service != null) {
            try {
                this.service.add(this.remoteCallback);
            } catch (RemoteException e) {
                Log.e(TAG, "Error occurred trying to register remote callback", e);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void unregisterRemoteCallback() {
        if (this.service != null) {
            try {
                this.service.remove(this.remoteCallback);
            } catch (RemoteException e) {
                Log.e(TAG, "Error occurred trying to unregister remote callback", e);
            }
        }
    }
}
