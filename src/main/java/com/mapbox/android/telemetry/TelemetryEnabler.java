package com.mapbox.android.telemetry;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import java.util.HashMap;
import java.util.Map;

public class TelemetryEnabler {
    private static final String KEY_META_DATA_ENABLED = "com.mapbox.EnableEvents";
    static final String MAPBOX_SHARED_PREFERENCE_KEY_TELEMETRY_STATE = "mapboxTelemetryState";
    private static final Map<String, State> STATES = new HashMap<String, State>() {
        /* class com.mapbox.android.telemetry.TelemetryEnabler.AnonymousClass2 */

        {
            put(State.ENABLED.name(), State.ENABLED);
            put(State.DISABLED.name(), State.DISABLED);
        }
    };
    static final Map<State, Boolean> TELEMETRY_STATES = new HashMap<State, Boolean>() {
        /* class com.mapbox.android.telemetry.TelemetryEnabler.AnonymousClass1 */

        {
            put(State.ENABLED, true);
            put(State.DISABLED, false);
        }
    };
    private State currentTelemetryState = State.ENABLED;
    private boolean isFromPreferences = true;

    public enum State {
        ENABLED,
        DISABLED
    }

    TelemetryEnabler(boolean isFromPreferences2) {
        this.isFromPreferences = isFromPreferences2;
    }

    public static State retrieveTelemetryStateFromPreferences() {
        if (MapboxTelemetry.applicationContext == null) {
            return STATES.get(State.ENABLED.name());
        }
        return STATES.get(TelemetryUtils.obtainSharedPreferences(MapboxTelemetry.applicationContext).getString(MAPBOX_SHARED_PREFERENCE_KEY_TELEMETRY_STATE, State.ENABLED.name()));
    }

    public static State updateTelemetryState(State telemetryState) {
        if (MapboxTelemetry.applicationContext != null) {
            SharedPreferences.Editor editor = TelemetryUtils.obtainSharedPreferences(MapboxTelemetry.applicationContext).edit();
            editor.putString(MAPBOX_SHARED_PREFERENCE_KEY_TELEMETRY_STATE, telemetryState.name());
            editor.apply();
        }
        return telemetryState;
    }

    /* access modifiers changed from: package-private */
    public State obtainTelemetryState() {
        if (this.isFromPreferences) {
            return retrieveTelemetryStateFromPreferences();
        }
        return this.currentTelemetryState;
    }

    /* access modifiers changed from: package-private */
    public State updatePreferences(State telemetryState) {
        if (this.isFromPreferences) {
            return updateTelemetryState(telemetryState);
        }
        this.currentTelemetryState = telemetryState;
        return this.currentTelemetryState;
    }

    static boolean isEventsEnabled(Context context) {
        try {
            ApplicationInfo appInformation = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128);
            if (appInformation == null || appInformation.metaData == null) {
                return true;
            }
            return appInformation.metaData.getBoolean(KEY_META_DATA_ENABLED, true);
        } catch (PackageManager.NameNotFoundException exception) {
            exception.printStackTrace();
            return true;
        }
    }
}
