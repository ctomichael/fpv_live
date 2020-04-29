package com.mapbox.android.telemetry;

public class SessionInterval {
    private static final int HIGH_INTERVAL_VALUE = 24;
    private static final String INTERVAL_NOT_IN_THE_RANGE = "The interval passed in must be an an integer in the range of 1 to 24 hours.";
    private static final int LOW_INTERVAL_VALUE = 1;
    private final int interval;

    public SessionInterval(int interval2) {
        check(interval2);
        this.interval = interval2;
    }

    private void check(int interval2) {
        if (interval2 < 1 || interval2 > 24) {
            throw new IllegalArgumentException(INTERVAL_NOT_IN_THE_RANGE);
        }
    }

    /* access modifiers changed from: package-private */
    public int obtainInterval() {
        return this.interval;
    }
}
