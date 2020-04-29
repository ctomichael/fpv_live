package com.mapzen.android.lost.internal;

import android.content.Context;
import android.location.Location;
import android.os.RemoteException;
import android.util.Log;
import com.mapzen.android.lost.api.LocationAvailability;
import com.mapzen.android.lost.api.LocationResult;
import java.util.ArrayList;

public class FusedLocationServiceCallbackManager {
    /* access modifiers changed from: package-private */
    public void onLocationChanged(Context context, Location location, ClientManager clientManager, IFusedLocationProviderService service) {
        clientManager.reportLocationChanged(location);
        LocationAvailability availability = null;
        try {
            availability = service.getLocationAvailability();
        } catch (RemoteException e) {
            Log.e("ContentValues", "Error occurred trying to get LocationAvailability", e);
        }
        ArrayList<Location> locations = new ArrayList<>();
        locations.add(location);
        LocationResult result = LocationResult.create(locations);
        clientManager.sendPendingIntent(context, location, availability, result);
        clientManager.reportLocationResult(location, result);
    }

    /* access modifiers changed from: package-private */
    public void onLocationAvailabilityChanged(LocationAvailability locationAvailability, ClientManager clientManager) {
        clientManager.notifyLocationAvailability(locationAvailability);
    }
}
