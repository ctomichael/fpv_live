package com.mapbox.mapboxsdk.module.telemetry;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import com.mapbox.android.telemetry.AppUserTurnstile;
import com.mapbox.android.telemetry.MapboxTelemetry;
import com.mapbox.android.telemetry.SessionInterval;
import com.mapbox.android.telemetry.TelemetryEnabler;
import com.mapbox.mapboxsdk.BuildConfig;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.TelemetryDefinition;
import com.mapbox.mapboxsdk.offline.OfflineRegionDefinition;
import com.mapbox.mapboxsdk.offline.OfflineTilePyramidRegionDefinition;
import java.util.UUID;

public class TelemetryImpl implements TelemetryDefinition {
    private final Context appContext = Mapbox.getApplicationContext();
    private final MapboxTelemetry telemetry = new MapboxTelemetry(this.appContext, Mapbox.getAccessToken(), BuildConfig.MAPBOX_EVENTS_USER_AGENT);

    public TelemetryImpl() {
        if (TelemetryEnabler.State.ENABLED.equals(TelemetryEnabler.retrieveTelemetryStateFromPreferences())) {
            this.telemetry.enable();
        }
    }

    public void onAppUserTurnstileEvent() {
        AppUserTurnstile turnstileEvent = new AppUserTurnstile(BuildConfig.MAPBOX_SDK_IDENTIFIER, BuildConfig.MAPBOX_SDK_VERSION);
        turnstileEvent.setSkuId("00");
        this.telemetry.push(turnstileEvent);
        this.telemetry.push(MapEventFactory.buildMapLoadEvent(new PhoneState(this.appContext)));
    }

    @Deprecated
    public void onGestureInteraction(String eventType, double latitude, double longitude, @FloatRange(from = 0.0d, to = 25.5d) double zoom) {
    }

    public void setUserTelemetryRequestState(boolean enabledTelemetry) {
        if (enabledTelemetry) {
            TelemetryEnabler.updateTelemetryState(TelemetryEnabler.State.ENABLED);
            this.telemetry.enable();
            return;
        }
        this.telemetry.disable();
        TelemetryEnabler.updateTelemetryState(TelemetryEnabler.State.DISABLED);
    }

    public void disableTelemetrySession() {
        this.telemetry.disable();
    }

    public void setDebugLoggingEnabled(boolean debugLoggingEnabled) {
        this.telemetry.updateDebugLoggingEnabled(debugLoggingEnabled);
    }

    public boolean setSessionIdRotationInterval(int interval) {
        return this.telemetry.updateSessionIdRotationInterval(new SessionInterval(interval));
    }

    public void onCreateOfflineRegion(@NonNull OfflineRegionDefinition offlineDefinition) {
        this.telemetry.push(MapEventFactory.buildOfflineDownloadStartEvent(new PhoneState(this.appContext), offlineDefinition instanceof OfflineTilePyramidRegionDefinition ? "tileregion" : "shaperegion", Double.valueOf(offlineDefinition.getMinZoom()), Double.valueOf(offlineDefinition.getMaxZoom()), offlineDefinition.getStyleURL()));
    }

    public void onPerformanceEvent(Bundle data) {
        if (data == null) {
            data = new Bundle();
        }
        this.telemetry.push(MapEventFactory.buildPerformanceEvent(new PhoneState(this.appContext), UUID.randomUUID().toString(), data));
    }
}
