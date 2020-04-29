package com.mapzen.android.lost.internal;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.NotificationCompat;
import com.dji.permission.Permission;
import com.mapzen.android.lost.api.Geofence;
import com.mapzen.android.lost.api.GeofencingApi;
import com.mapzen.android.lost.api.GeofencingRequest;
import com.mapzen.android.lost.api.LostApiClient;
import com.mapzen.android.lost.api.PendingResult;
import com.mapzen.android.lost.api.Status;
import dji.publics.protocol.ResponseBase;
import java.util.HashMap;
import java.util.List;

public class GeofencingApiImpl extends ApiImpl implements GeofencingApi {
    private Context context;
    private IntentFactory dwellServiceIntentFactory;
    private HashMap<Geofence, PendingIntent> enteredFences = new HashMap<>();
    private IntentFactory geofencingServiceIntentFactory;
    private IdGenerator idGenerator;
    private HashMap<Integer, Geofence> idToGeofence = new HashMap<>();
    private HashMap<Integer, PendingIntent> idToPendingIntent = new HashMap<>();
    private Intent internalIntent;
    private LocationManager locationManager;
    private final HashMap<String, PendingIntent> pendingIntentMap;

    public GeofencingApiImpl(IntentFactory geofenceFactory, IntentFactory dwellFactory, IdGenerator generator) {
        this.geofencingServiceIntentFactory = geofenceFactory;
        this.dwellServiceIntentFactory = dwellFactory;
        this.idGenerator = generator;
        this.pendingIntentMap = new HashMap<>();
    }

    public void connect(Context context2) {
        this.context = context2;
        this.locationManager = (LocationManager) context2.getSystemService(ResponseBase.STRING_LOCATION);
    }

    public boolean isConnected() {
        return this.locationManager != null;
    }

    public void disconnect() {
        this.locationManager = null;
    }

    public PendingResult<Status> addGeofences(LostApiClient client, GeofencingRequest geofencingRequest, PendingIntent pendingIntent) throws SecurityException {
        throwIfNotConnected(client);
        addGeofences(client, geofencingRequest.getGeofences(), pendingIntent);
        return new SimplePendingResult(true);
    }

    @RequiresPermission(anyOf = {Permission.ACCESS_COARSE_LOCATION, Permission.ACCESS_FINE_LOCATION})
    public PendingResult<Status> addGeofences(LostApiClient client, List<Geofence> geofences, PendingIntent pendingIntent) throws SecurityException {
        throwIfNotConnected(client);
        for (Geofence geofence : geofences) {
            addGeofence(client, geofence, pendingIntent);
        }
        return new SimplePendingResult(true);
    }

    @RequiresPermission(anyOf = {Permission.ACCESS_COARSE_LOCATION, Permission.ACCESS_FINE_LOCATION})
    private PendingResult<Status> addGeofence(LostApiClient client, Geofence geofence, PendingIntent pendingIntent) throws SecurityException {
        checkGeofence(geofence);
        int pendingIntentId = this.idGenerator.generateId();
        this.internalIntent = this.geofencingServiceIntentFactory.createIntent(this.context);
        this.internalIntent.addCategory(String.valueOf(pendingIntentId));
        ParcelableGeofence pGeofence = (ParcelableGeofence) geofence;
        this.idToPendingIntent.put(Integer.valueOf(pendingIntentId), pendingIntent);
        this.idToGeofence.put(Integer.valueOf(pendingIntentId), pGeofence);
        PendingIntent internalPendingIntent = this.geofencingServiceIntentFactory.createPendingIntent(this.context, pendingIntentId, this.internalIntent);
        String requestId = String.valueOf(pGeofence.hashCode());
        this.locationManager.addProximityAlert(pGeofence.getLatitude(), pGeofence.getLongitude(), pGeofence.getRadius(), pGeofence.getDuration(), internalPendingIntent);
        if (pGeofence.getRequestId() != null && !pGeofence.getRequestId().isEmpty()) {
            requestId = pGeofence.getRequestId();
        }
        this.pendingIntentMap.put(requestId, pendingIntent);
        return new SimplePendingResult(true);
    }

    @RequiresPermission(anyOf = {Permission.ACCESS_COARSE_LOCATION, Permission.ACCESS_FINE_LOCATION})
    public PendingResult<Status> removeGeofences(LostApiClient client, List<String> geofenceRequestIds) {
        throwIfNotConnected(client);
        boolean hasResult = false;
        for (String geofenceRequestId : geofenceRequestIds) {
            if (this.pendingIntentMap.containsKey(geofenceRequestId)) {
                hasResult = true;
            }
            removeGeofences(client, geofenceRequestId);
        }
        return new SimplePendingResult(hasResult);
    }

    @RequiresPermission(anyOf = {Permission.ACCESS_COARSE_LOCATION, Permission.ACCESS_FINE_LOCATION})
    private void removeGeofences(LostApiClient client, String geofenceRequestId) throws SecurityException {
        removeGeofences(client, this.pendingIntentMap.get(geofenceRequestId));
    }

    @RequiresPermission(anyOf = {Permission.ACCESS_COARSE_LOCATION, Permission.ACCESS_FINE_LOCATION})
    public PendingResult<Status> removeGeofences(LostApiClient client, PendingIntent pendingIntent) throws SecurityException {
        throwIfNotConnected(client);
        boolean hasResult = false;
        if (this.pendingIntentMap.values().contains(pendingIntent)) {
            hasResult = true;
        }
        this.locationManager.removeProximityAlert(pendingIntent);
        this.pendingIntentMap.values().remove(pendingIntent);
        return new SimplePendingResult(hasResult);
    }

    public PendingIntent pendingIntentForIntentId(int intentId) {
        return this.idToPendingIntent.get(Integer.valueOf(intentId));
    }

    public Geofence geofenceForIntentId(int intentId) {
        return this.idToGeofence.get(Integer.valueOf(intentId));
    }

    public void geofenceEntered(Geofence geofence, int pendingIntentId) {
        long loiterDelay = (long) ((ParcelableGeofence) geofence).getLoiteringDelayMs();
        Intent intent = this.dwellServiceIntentFactory.createIntent(this.context);
        intent.addCategory(String.valueOf(pendingIntentId));
        PendingIntent pendingIntent = this.dwellServiceIntentFactory.createPendingIntent(this.context, pendingIntentId, intent);
        ((AlarmManager) this.context.getSystemService(NotificationCompat.CATEGORY_ALARM)).set(0, System.currentTimeMillis() + loiterDelay, pendingIntent);
        this.enteredFences.put(geofence, pendingIntent);
    }

    public void geofenceExited(Geofence geofence) {
        PendingIntent pendingIntent = this.enteredFences.get(geofence);
        if (pendingIntent != null) {
            ((AlarmManager) this.context.getSystemService(NotificationCompat.CATEGORY_ALARM)).cancel(pendingIntent);
            this.enteredFences.remove(geofence);
        }
    }

    private void checkGeofence(Geofence geofence) {
        ParcelableGeofence pGeofence = (ParcelableGeofence) geofence;
        if ((pGeofence.getTransitionTypes() & 4) != 0 && pGeofence.getLoiteringDelayMs() == -1) {
            throw new IllegalStateException("Dwell transition type requested without loitering delay. Please set a loitering delay for this geofence.");
        }
    }
}
