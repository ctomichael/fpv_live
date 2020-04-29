package com.mapzen.android.lost.api;

import android.app.PendingIntent;
import android.location.Location;
import android.os.Looper;
import android.support.annotation.RequiresPermission;
import com.dji.permission.Permission;

public interface FusedLocationProviderApi {
    @Deprecated
    public static final String KEY_LOCATION_CHANGED = "com.mapzen.android.lost.LOCATION";

    @RequiresPermission(anyOf = {Permission.ACCESS_COARSE_LOCATION, Permission.ACCESS_FINE_LOCATION})
    Location getLastLocation(LostApiClient lostApiClient);

    @RequiresPermission(anyOf = {Permission.ACCESS_COARSE_LOCATION, Permission.ACCESS_FINE_LOCATION})
    LocationAvailability getLocationAvailability(LostApiClient lostApiClient);

    PendingResult<Status> removeLocationUpdates(LostApiClient lostApiClient, PendingIntent pendingIntent);

    PendingResult<Status> removeLocationUpdates(LostApiClient lostApiClient, LocationCallback locationCallback);

    PendingResult<Status> removeLocationUpdates(LostApiClient lostApiClient, LocationListener locationListener);

    @RequiresPermission(anyOf = {Permission.ACCESS_COARSE_LOCATION, Permission.ACCESS_FINE_LOCATION})
    PendingResult<Status> requestLocationUpdates(LostApiClient lostApiClient, LocationRequest locationRequest, PendingIntent pendingIntent);

    @RequiresPermission(anyOf = {Permission.ACCESS_COARSE_LOCATION, Permission.ACCESS_FINE_LOCATION})
    PendingResult<Status> requestLocationUpdates(LostApiClient lostApiClient, LocationRequest locationRequest, LocationCallback locationCallback, Looper looper);

    @RequiresPermission(anyOf = {Permission.ACCESS_COARSE_LOCATION, Permission.ACCESS_FINE_LOCATION})
    PendingResult<Status> requestLocationUpdates(LostApiClient lostApiClient, LocationRequest locationRequest, LocationListener locationListener);

    @RequiresPermission(anyOf = {Permission.ACCESS_COARSE_LOCATION, Permission.ACCESS_FINE_LOCATION})
    PendingResult<Status> requestLocationUpdates(LostApiClient lostApiClient, LocationRequest locationRequest, LocationListener locationListener, Looper looper);

    PendingResult<Status> setMockLocation(LostApiClient lostApiClient, Location location);

    PendingResult<Status> setMockMode(LostApiClient lostApiClient, boolean z);

    PendingResult<Status> setMockTrace(LostApiClient lostApiClient, String str, String str2);
}
