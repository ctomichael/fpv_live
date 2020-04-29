package com.mapbox.android.telemetry;

interface SchedulerCallback {
    void onError();

    void onPeriodRaised();
}
