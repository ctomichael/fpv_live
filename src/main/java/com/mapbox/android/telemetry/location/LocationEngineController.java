package com.mapbox.android.telemetry.location;

interface LocationEngineController {
    void onDestroy();

    void onPause();

    void onResume();
}
