package com.mapbox.android.telemetry.location;

import com.mapbox.android.telemetry.TelemetryUtils;
import dji.pilot.publics.util.DJITimeUtils;

public class SessionIdentifier {
    private static final int DEFAULT_ROTATION_HOURS = 24;
    private static final long HOURS_TO_MILLISECONDS = 3600000;
    private long lastSessionIdUpdate;
    private final long rotationInterval;
    private String sessionId;

    public SessionIdentifier() {
        this((long) DJITimeUtils.MILLIS_IN_DAY);
    }

    public SessionIdentifier(long intervalMillis) {
        this.sessionId = null;
        this.rotationInterval = intervalMillis;
    }

    @Deprecated
    public SessionIdentifier(int rotationInterval2) {
        this.sessionId = null;
        this.rotationInterval = ((long) rotationInterval2) * HOURS_TO_MILLISECONDS;
    }

    public long getInterval() {
        return this.rotationInterval;
    }

    /* access modifiers changed from: package-private */
    public String getSessionId() {
        if (System.currentTimeMillis() - this.lastSessionIdUpdate >= this.rotationInterval || this.sessionId == null) {
            this.sessionId = TelemetryUtils.obtainUniversalUniqueIdentifier();
            this.lastSessionIdUpdate = System.currentTimeMillis();
        }
        return this.sessionId;
    }
}
