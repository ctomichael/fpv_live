package com.mapzen.android.lost.internal;

import android.app.PendingIntent;
import android.content.Context;
import android.location.Location;
import android.os.Looper;
import com.mapzen.android.lost.api.LocationAvailability;
import com.mapzen.android.lost.api.LocationCallback;
import com.mapzen.android.lost.api.LocationListener;
import com.mapzen.android.lost.api.LocationRequest;
import com.mapzen.android.lost.api.LocationResult;
import com.mapzen.android.lost.api.LostApiClient;
import java.util.Map;
import java.util.Set;

public interface ClientManager {
    void addClient(LostApiClient lostApiClient);

    void addListener(LostApiClient lostApiClient, LocationRequest locationRequest, LocationListener locationListener);

    void addLocationCallback(LostApiClient lostApiClient, LocationRequest locationRequest, LocationCallback locationCallback, Looper looper);

    void addPendingIntent(LostApiClient lostApiClient, LocationRequest locationRequest, PendingIntent pendingIntent);

    boolean containsClient(LostApiClient lostApiClient);

    Map<LostApiClient, Set<LocationCallback>> getLocationCallbacks();

    Map<LostApiClient, Set<LocationListener>> getLocationListeners();

    Map<LostApiClient, Set<PendingIntent>> getPendingIntents();

    boolean hasNoListeners();

    void notifyLocationAvailability(LocationAvailability locationAvailability);

    int numberOfClients();

    void removeClient(LostApiClient lostApiClient);

    boolean removeListener(LostApiClient lostApiClient, LocationListener locationListener);

    boolean removeLocationCallback(LostApiClient lostApiClient, LocationCallback locationCallback);

    boolean removePendingIntent(LostApiClient lostApiClient, PendingIntent pendingIntent);

    void reportLocationChanged(Location location);

    void reportLocationResult(Location location, LocationResult locationResult);

    void sendPendingIntent(Context context, Location location, LocationAvailability locationAvailability, LocationResult locationResult);
}
