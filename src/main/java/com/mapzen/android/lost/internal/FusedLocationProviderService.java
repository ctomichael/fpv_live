package com.mapzen.android.lost.internal;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import com.mapzen.android.lost.api.LocationAvailability;
import com.mapzen.android.lost.api.LocationRequest;
import com.mapzen.android.lost.internal.IFusedLocationProviderService;
import java.util.List;

public class FusedLocationProviderService extends Service {
    private final IFusedLocationProviderService.Stub binder = new IFusedLocationProviderService.Stub() {
        /* class com.mapzen.android.lost.internal.FusedLocationProviderService.AnonymousClass1 */

        public void add(IFusedLocationProviderCallback callback) throws RemoteException {
            FusedLocationProviderService.this.delegate.add(callback);
        }

        public void remove(IFusedLocationProviderCallback callback) throws RemoteException {
            FusedLocationProviderService.this.delegate.remove(callback);
        }

        public Location getLastLocation() throws RemoteException {
            return FusedLocationProviderService.this.delegate.getLastLocation();
        }

        public LocationAvailability getLocationAvailability() throws RemoteException {
            return FusedLocationProviderService.this.delegate.getLocationAvailability();
        }

        public void requestLocationUpdates(LocationRequest request) throws RemoteException {
            FusedLocationProviderService.this.delegate.requestLocationUpdates(request);
        }

        public void removeLocationUpdates(List<LocationRequest> requests) throws RemoteException {
            FusedLocationProviderService.this.delegate.removeLocationUpdates(requests);
        }

        public void setMockMode(boolean isMockMode) throws RemoteException {
            FusedLocationProviderService.this.delegate.setMockMode(isMockMode);
        }

        public void setMockLocation(Location mockLocation) throws RemoteException {
            FusedLocationProviderService.this.delegate.setMockLocation(mockLocation);
        }

        public void setMockTrace(String path, String filename) throws RemoteException {
            FusedLocationProviderService.this.delegate.setMockTrace(path, filename);
        }
    };
    /* access modifiers changed from: private */
    public FusedLocationProviderServiceDelegate delegate;

    @Nullable
    public IBinder onBind(Intent intent) {
        return this.binder;
    }

    public void onCreate() {
        super.onCreate();
        this.delegate = new FusedLocationProviderServiceDelegate(this);
    }
}
