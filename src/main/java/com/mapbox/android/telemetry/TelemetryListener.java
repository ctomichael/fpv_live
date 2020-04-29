package com.mapbox.android.telemetry;

public interface TelemetryListener {
    void onHttpFailure(String str);

    void onHttpResponse(boolean z, int i);
}
