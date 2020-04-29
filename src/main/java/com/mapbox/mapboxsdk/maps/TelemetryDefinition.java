package com.mapbox.mapboxsdk.maps;

import android.os.Bundle;
import com.mapbox.mapboxsdk.offline.OfflineRegionDefinition;

public interface TelemetryDefinition {
    void disableTelemetrySession();

    void onAppUserTurnstileEvent();

    void onCreateOfflineRegion(OfflineRegionDefinition offlineRegionDefinition);

    @Deprecated
    void onGestureInteraction(String str, double d, double d2, double d3);

    void onPerformanceEvent(Bundle bundle);

    void setDebugLoggingEnabled(boolean z);

    boolean setSessionIdRotationInterval(int i);

    void setUserTelemetryRequestState(boolean z);
}
