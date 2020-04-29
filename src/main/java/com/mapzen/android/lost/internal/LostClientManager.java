package com.mapzen.android.lost.internal;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.VisibleForTesting;
import android.util.Log;
import com.mapzen.android.lost.api.FusedLocationProviderApi;
import com.mapzen.android.lost.api.LocationAvailability;
import com.mapzen.android.lost.api.LocationCallback;
import com.mapzen.android.lost.api.LocationListener;
import com.mapzen.android.lost.api.LocationRequest;
import com.mapzen.android.lost.api.LocationResult;
import com.mapzen.android.lost.api.LostApiClient;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class LostClientManager implements ClientManager {
    private static final String TAG = ClientManager.class.getSimpleName();
    private static LostClientManager instance;
    private Map<LocationCallback, LocationRequestReport> callbackToReports = new HashMap();
    private HashMap<LostApiClient, LostClientWrapper> clients = new HashMap<>();
    private final Clock clock;
    private final HandlerFactory handlerFactory;
    private Map<PendingIntent, LocationRequestReport> intentToReports = new HashMap();
    private Map<LocationListener, LocationRequestReport> listenerToReports = new HashMap();

    interface Notifier<T> {
        void notify(LostApiClient lostApiClient, T t);
    }

    LostClientManager(Clock clock2, HandlerFactory handlerFactory2) {
        this.clock = clock2;
        this.handlerFactory = handlerFactory2;
    }

    public static LostClientManager shared() {
        if (instance == null) {
            instance = new LostClientManager(new SystemClock(), new AndroidHandlerFactory());
        }
        return instance;
    }

    public void addClient(LostApiClient client) {
        this.clients.put(client, new LostClientWrapper(client));
    }

    public void removeClient(LostApiClient client) {
        this.clients.remove(client);
    }

    public boolean containsClient(LostApiClient client) {
        return this.clients.containsKey(client);
    }

    public int numberOfClients() {
        return this.clients.size();
    }

    public void addListener(LostApiClient client, LocationRequest request, LocationListener listener) {
        throwIfClientNotAdded(client);
        this.clients.get(client).locationListeners().add(listener);
        this.listenerToReports.put(listener, new LocationRequestReport(request));
    }

    public void addPendingIntent(LostApiClient client, LocationRequest request, PendingIntent callbackIntent) {
        throwIfClientNotAdded(client);
        this.clients.get(client).pendingIntents().add(callbackIntent);
        this.intentToReports.put(callbackIntent, new LocationRequestReport(request));
    }

    public void addLocationCallback(LostApiClient client, LocationRequest request, LocationCallback callback, Looper looper) {
        throwIfClientNotAdded(client);
        this.clients.get(client).locationCallbacks().add(callback);
        this.clients.get(client).looperMap().put(callback, looper);
        this.callbackToReports.put(callback, new LocationRequestReport(request));
    }

    private void throwIfClientNotAdded(LostApiClient client) {
        if (this.clients.get(client) == null) {
            throw new IllegalArgumentException("Client must be added before requesting location updates");
        }
    }

    public boolean removeListener(LostApiClient client, LocationListener listener) {
        Set<LocationListener> listeners = this.clients.get(client).locationListeners();
        boolean removed = false;
        if (listeners.contains(listener)) {
            listeners.remove(listener);
            removed = true;
        }
        this.listenerToReports.remove(listener);
        return removed;
    }

    public boolean removePendingIntent(LostApiClient client, PendingIntent callbackIntent) {
        Set<PendingIntent> pendingIntents = this.clients.get(client).pendingIntents();
        boolean removed = false;
        if (pendingIntents.contains(callbackIntent)) {
            pendingIntents.remove(callbackIntent);
            removed = true;
        }
        this.intentToReports.remove(callbackIntent);
        return removed;
    }

    public boolean removeLocationCallback(LostApiClient client, LocationCallback callback) {
        Set<LocationCallback> callbacks = this.clients.get(client).locationCallbacks();
        boolean removed = false;
        if (callbacks.contains(callback)) {
            callbacks.remove(callback);
            removed = true;
        }
        this.clients.get(client).looperMap().remove(callback);
        this.callbackToReports.remove(callback);
        return removed;
    }

    public void reportLocationChanged(final Location location) {
        iterateAndNotify(location, getLocationListeners(), this.listenerToReports, new Notifier<LocationListener>() {
            /* class com.mapzen.android.lost.internal.LostClientManager.AnonymousClass1 */

            public void notify(LostApiClient client, LocationListener listener) {
                listener.onLocationChanged(location);
            }
        });
    }

    public void sendPendingIntent(Context context, Location location, LocationAvailability availability, LocationResult result) {
        final Context context2 = context;
        final Location location2 = location;
        final LocationAvailability locationAvailability = availability;
        final LocationResult locationResult = result;
        iterateAndNotify(location, getPendingIntents(), this.intentToReports, new Notifier<PendingIntent>() {
            /* class com.mapzen.android.lost.internal.LostClientManager.AnonymousClass2 */

            public void notify(LostApiClient client, PendingIntent pendingIntent) {
                LostClientManager.this.fireIntent(context2, pendingIntent, location2, locationAvailability, locationResult);
            }
        });
    }

    public void reportLocationResult(Location location, final LocationResult result) {
        iterateAndNotify(location, getLocationCallbacks(), this.callbackToReports, new Notifier<LocationCallback>() {
            /* class com.mapzen.android.lost.internal.LostClientManager.AnonymousClass3 */

            public void notify(LostApiClient client, LocationCallback callback) {
                LostClientManager.this.notifyCallback(client, callback, result);
            }
        });
    }

    public void notifyLocationAvailability(LocationAvailability availability) {
        for (LostClientWrapper wrapper : this.clients.values()) {
            for (LocationCallback callback : wrapper.locationCallbacks()) {
                notifyAvailability(wrapper.client(), callback, availability);
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:3:0x0010  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean hasNoListeners() {
        /*
            r3 = this;
            java.util.HashMap<com.mapzen.android.lost.api.LostApiClient, com.mapzen.android.lost.internal.LostClientWrapper> r1 = r3.clients
            java.util.Collection r1 = r1.values()
            java.util.Iterator r1 = r1.iterator()
        L_0x000a:
            boolean r2 = r1.hasNext()
            if (r2 == 0) goto L_0x0036
            java.lang.Object r0 = r1.next()
            com.mapzen.android.lost.internal.LostClientWrapper r0 = (com.mapzen.android.lost.internal.LostClientWrapper) r0
            java.util.Set r2 = r0.locationListeners()
            boolean r2 = r2.isEmpty()
            if (r2 == 0) goto L_0x0034
            java.util.Set r2 = r0.pendingIntents()
            boolean r2 = r2.isEmpty()
            if (r2 == 0) goto L_0x0034
            java.util.Set r2 = r0.locationCallbacks()
            boolean r2 = r2.isEmpty()
            if (r2 != 0) goto L_0x000a
        L_0x0034:
            r1 = 0
        L_0x0035:
            return r1
        L_0x0036:
            r1 = 1
            goto L_0x0035
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mapzen.android.lost.internal.LostClientManager.hasNoListeners():boolean");
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public void clearClients() {
        this.clients.clear();
    }

    public Map<LostApiClient, Set<LocationListener>> getLocationListeners() {
        Map<LostApiClient, Set<LocationListener>> clientToListeners = new HashMap<>();
        for (LostApiClient client : this.clients.keySet()) {
            clientToListeners.put(client, this.clients.get(client).locationListeners());
        }
        return clientToListeners;
    }

    public Map<LostApiClient, Set<PendingIntent>> getPendingIntents() {
        Map<LostApiClient, Set<PendingIntent>> clientToPendingIntents = new HashMap<>();
        for (LostApiClient client : this.clients.keySet()) {
            clientToPendingIntents.put(client, this.clients.get(client).pendingIntents());
        }
        return clientToPendingIntents;
    }

    public Map<LostApiClient, Set<LocationCallback>> getLocationCallbacks() {
        Map<LostApiClient, Set<LocationCallback>> clientToLocationCallbacks = new HashMap<>();
        for (LostApiClient client : this.clients.keySet()) {
            clientToLocationCallbacks.put(client, this.clients.get(client).locationCallbacks());
        }
        return clientToLocationCallbacks;
    }

    /* access modifiers changed from: private */
    public void fireIntent(Context context, PendingIntent intent, Location location, LocationAvailability availability, LocationResult result) {
        try {
            Intent toSend = new Intent().putExtra(FusedLocationProviderApi.KEY_LOCATION_CHANGED, location);
            toSend.putExtra(LocationAvailability.EXTRA_LOCATION_AVAILABILITY, availability);
            toSend.putExtra(LocationResult.EXTRA_LOCATION_RESULT, result);
            intent.send(context, 0, toSend);
        } catch (PendingIntent.CanceledException e) {
            Log.e(TAG, "Unable to send pending intent: " + intent);
        }
    }

    /* access modifiers changed from: private */
    public void notifyCallback(LostApiClient client, final LocationCallback callback, final LocationResult result) {
        this.handlerFactory.run(this.clients.get(client).looperMap().get(callback), new Runnable() {
            /* class com.mapzen.android.lost.internal.LostClientManager.AnonymousClass4 */

            public void run() {
                callback.onLocationResult(result);
            }
        });
    }

    private void notifyAvailability(LostApiClient client, final LocationCallback callback, final LocationAvailability availability) {
        new Handler(this.clients.get(client).looperMap().get(callback)).post(new Runnable() {
            /* class com.mapzen.android.lost.internal.LostClientManager.AnonymousClass5 */

            public void run() {
                callback.onLocationAvailability(availability);
            }
        });
    }

    private <T> void iterateAndNotify(Location location, Map<LostApiClient, Set<T>> clientToObj, Map<T, LocationRequestReport> objToLocationRequest, Notifier notifier) {
        for (LostApiClient client : clientToObj.keySet()) {
            if (clientToObj.get(client) != null) {
                for (T obj : clientToObj.get(client)) {
                    LocationRequestReport report = objToLocationRequest.get(obj);
                    LocationRequest request = report.locationRequest;
                    Location lastReportedLocation = report.location;
                    if (lastReportedLocation == null) {
                        report.location = location;
                        notifier.notify(client, obj);
                    } else {
                        long intervalSinceLastReport = (this.clock.getElapsedTimeInNanos(location) - this.clock.getElapsedTimeInNanos(lastReportedLocation)) / Clock.MS_TO_NS;
                        long fastestInterval = request.getFastestInterval();
                        float smallestDisplacement = request.getSmallestDisplacement();
                        float displacementSinceLastReport = location.distanceTo(lastReportedLocation);
                        if (intervalSinceLastReport >= fastestInterval && displacementSinceLastReport >= smallestDisplacement) {
                            report.location = location;
                            notifier.notify(client, obj);
                        }
                    }
                }
            }
        }
    }
}
