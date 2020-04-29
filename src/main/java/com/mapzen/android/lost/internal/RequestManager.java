package com.mapzen.android.lost.internal;

import android.app.PendingIntent;
import com.mapzen.android.lost.api.LocationCallback;
import com.mapzen.android.lost.api.LocationListener;
import com.mapzen.android.lost.api.LocationRequest;
import com.mapzen.android.lost.api.LostApiClient;
import java.util.List;

interface RequestManager {
    List<LocationRequest> removeLocationUpdates(LostApiClient lostApiClient, PendingIntent pendingIntent);

    List<LocationRequest> removeLocationUpdates(LostApiClient lostApiClient, LocationCallback locationCallback);

    List<LocationRequest> removeLocationUpdates(LostApiClient lostApiClient, LocationListener locationListener);

    void requestLocationUpdates(LostApiClient lostApiClient, LocationRequest locationRequest, PendingIntent pendingIntent);

    void requestLocationUpdates(LostApiClient lostApiClient, LocationRequest locationRequest, LocationCallback locationCallback);

    void requestLocationUpdates(LostApiClient lostApiClient, LocationRequest locationRequest, LocationListener locationListener);
}
