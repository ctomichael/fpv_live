package com.mapzen.android.lost.api;

import android.app.PendingIntent;
import android.support.annotation.RequiresPermission;
import com.dji.permission.Permission;
import java.util.List;

public interface GeofencingApi {
    public static final String EXTRA_GEOFENCE_LIST = "com.mapzen.lost.extra.geofence_list";
    public static final String EXTRA_TRANSITION = "com.mapzen.lost.extra.transition";
    public static final String EXTRA_TRIGGERING_LOCATION = "com.mapzen.lost.extra.triggering_location";

    @RequiresPermission(anyOf = {Permission.ACCESS_COARSE_LOCATION, Permission.ACCESS_FINE_LOCATION})
    PendingResult<Status> addGeofences(LostApiClient lostApiClient, GeofencingRequest geofencingRequest, PendingIntent pendingIntent);

    @RequiresPermission(anyOf = {Permission.ACCESS_COARSE_LOCATION, Permission.ACCESS_FINE_LOCATION})
    PendingResult<Status> addGeofences(LostApiClient lostApiClient, List<Geofence> list, PendingIntent pendingIntent);

    @RequiresPermission(anyOf = {Permission.ACCESS_COARSE_LOCATION, Permission.ACCESS_FINE_LOCATION})
    PendingResult<Status> removeGeofences(LostApiClient lostApiClient, PendingIntent pendingIntent);

    @RequiresPermission(anyOf = {Permission.ACCESS_COARSE_LOCATION, Permission.ACCESS_FINE_LOCATION})
    PendingResult<Status> removeGeofences(LostApiClient lostApiClient, List<String> list);
}
