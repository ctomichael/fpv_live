package com.mapbox.mapboxsdk.module.telemetry;

import android.os.Bundle;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import com.mapbox.android.telemetry.TelemetryUtils;

class MapEventFactory {
    MapEventFactory() {
    }

    static MapLoadEvent buildMapLoadEvent(@NonNull PhoneState phoneState) {
        return new MapLoadEvent(TelemetryUtils.retrieveVendorId(), phoneState);
    }

    static OfflineDownloadStartEvent buildOfflineDownloadStartEvent(PhoneState phoneState, String shapeForOfflineRegion, @FloatRange(from = 0.0d, to = 25.5d) Double minZoom, @FloatRange(from = 0.0d, to = 25.5d) Double maxZoom, String styleURL) {
        OfflineDownloadStartEvent offlineEvent = new OfflineDownloadStartEvent(phoneState, shapeForOfflineRegion, minZoom, maxZoom);
        offlineEvent.setStyleURL(styleURL);
        return offlineEvent;
    }

    static OfflineDownloadEndEvent buildOfflineDownloadCompleteEvent(PhoneState phoneState, String shapeForOfflineRegion, @FloatRange(from = 0.0d, to = 25.5d) Double minZoom, @FloatRange(from = 0.0d, to = 25.5d) Double maxZoom, String styleURL, long sizeOfResourcesCompleted, long numberOfTilesCompleted, int state) {
        OfflineDownloadEndEvent offlineEvent = new OfflineDownloadEndEvent(phoneState, shapeForOfflineRegion, minZoom, maxZoom);
        offlineEvent.setStyleURL(styleURL);
        offlineEvent.setSizeOfResourcesCompleted(sizeOfResourcesCompleted);
        offlineEvent.setNumberOfTilesCompleted(numberOfTilesCompleted);
        offlineEvent.setState(state);
        return offlineEvent;
    }

    static PerformanceEvent buildPerformanceEvent(@NonNull PhoneState phoneState, @NonNull String sessionId, @NonNull Bundle data) {
        return new PerformanceEvent(phoneState, sessionId, data);
    }
}
