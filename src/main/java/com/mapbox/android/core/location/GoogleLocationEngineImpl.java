package com.mapbox.android.core.location;

import android.app.PendingIntent;
import android.content.Context;
import android.location.Location;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import java.util.Collections;
import java.util.List;

class GoogleLocationEngineImpl implements LocationEngineImpl<LocationCallback> {
    private final FusedLocationProviderClient fusedLocationProviderClient;

    @VisibleForTesting
    GoogleLocationEngineImpl(FusedLocationProviderClient fusedLocationProviderClient2) {
        this.fusedLocationProviderClient = fusedLocationProviderClient2;
    }

    GoogleLocationEngineImpl(@NonNull Context context) {
        this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
    }

    @NonNull
    public LocationCallback createListener(LocationEngineCallback<LocationEngineResult> callback) {
        return new GoogleLocationEngineCallbackTransport(callback);
    }

    public void getLastLocation(@NonNull LocationEngineCallback<LocationEngineResult> callback) throws SecurityException {
        GoogleLastLocationEngineCallbackTransport transport = new GoogleLastLocationEngineCallbackTransport(callback);
        this.fusedLocationProviderClient.getLastLocation().addOnSuccessListener(transport).addOnFailureListener(transport);
    }

    public void requestLocationUpdates(@NonNull LocationEngineRequest request, @NonNull LocationCallback listener, @Nullable Looper looper) throws SecurityException {
        this.fusedLocationProviderClient.requestLocationUpdates(toGMSLocationRequest(request), listener, looper);
    }

    public void requestLocationUpdates(@NonNull LocationEngineRequest request, @NonNull PendingIntent pendingIntent) throws SecurityException {
        this.fusedLocationProviderClient.requestLocationUpdates(toGMSLocationRequest(request), pendingIntent);
    }

    public void removeLocationUpdates(@NonNull LocationCallback listener) {
        if (listener != null) {
            this.fusedLocationProviderClient.removeLocationUpdates(listener);
        }
    }

    public void removeLocationUpdates(PendingIntent pendingIntent) {
        if (pendingIntent != null) {
            this.fusedLocationProviderClient.removeLocationUpdates(pendingIntent);
        }
    }

    private static LocationRequest toGMSLocationRequest(LocationEngineRequest request) {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(request.getInterval());
        locationRequest.setFastestInterval(request.getFastestInterval());
        locationRequest.setSmallestDisplacement(request.getDisplacemnt());
        locationRequest.setMaxWaitTime(request.getMaxWaitTime());
        locationRequest.setPriority(toGMSLocationPriority(request.getPriority()));
        return locationRequest;
    }

    private static int toGMSLocationPriority(int enginePriority) {
        switch (enginePriority) {
            case 0:
                return 100;
            case 1:
                return 102;
            case 2:
                return 104;
            default:
                return 105;
        }
    }

    private static final class GoogleLocationEngineCallbackTransport extends LocationCallback {
        private final LocationEngineCallback<LocationEngineResult> callback;

        GoogleLocationEngineCallbackTransport(LocationEngineCallback<LocationEngineResult> callback2) {
            this.callback = callback2;
        }

        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            List<Location> locations = locationResult.getLocations();
            if (!locations.isEmpty()) {
                this.callback.onSuccess(LocationEngineResult.create(locations));
            } else {
                this.callback.onFailure(new Exception("Unavailable location"));
            }
        }
    }

    @VisibleForTesting
    static final class GoogleLastLocationEngineCallbackTransport implements OnSuccessListener<Location>, OnFailureListener {
        private final LocationEngineCallback<LocationEngineResult> callback;

        GoogleLastLocationEngineCallbackTransport(LocationEngineCallback<LocationEngineResult> callback2) {
            this.callback = callback2;
        }

        public void onSuccess(Location location) {
            LocationEngineResult create;
            LocationEngineCallback<LocationEngineResult> locationEngineCallback = this.callback;
            if (location != null) {
                create = LocationEngineResult.create(location);
            } else {
                create = LocationEngineResult.create(Collections.emptyList());
            }
            locationEngineCallback.onSuccess(create);
        }

        public void onFailure(@NonNull Exception e) {
            this.callback.onFailure(e);
        }
    }
}
