package com.mapzen.android.lost.api;

import android.content.Intent;
import android.location.Location;
import java.util.List;

public class GeofencingEvent {
    public static final int GEOFENCE_NOT_AVAILABLE = 1000;
    public static final int GEOFENCE_TOO_MANY_GEOFENCES = 1001;
    public static final int GEOFENCE_TOO_MANY_PENDING_INTENTS = 1002;
    private static final int NO_ERROR = -1;
    private static final int NO_TRANSITION_ALERT = -1;

    private GeofencingEvent() {
    }

    public static GeofencingEvent fromIntent(Intent intent) {
        if (intent == null) {
            return null;
        }
        return new GeofencingEvent();
    }

    public int getErrorCode() {
        if (!hasError()) {
            return -1;
        }
        return 0;
    }

    public int getGeofenceTransition() {
        return -1;
    }

    public List<Geofence> getTriggeringGeofences() {
        return null;
    }

    public Location getTriggeringLocation() {
        return null;
    }

    public boolean hasError() {
        return false;
    }
}
