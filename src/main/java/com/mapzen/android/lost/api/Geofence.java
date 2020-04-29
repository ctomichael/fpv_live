package com.mapzen.android.lost.api;

import com.mapzen.android.lost.internal.ParcelableGeofence;

public interface Geofence {
    public static final int GEOFENCE_TRANSITION_DWELL = 4;
    public static final int GEOFENCE_TRANSITION_ENTER = 1;
    public static final int GEOFENCE_TRANSITION_EXIT = 2;
    public static final int LOITERING_DELAY_NONE = -1;
    public static final long NEVER_EXPIRE = -1;

    String getRequestId();

    public static final class Builder {
        private long durationMillis = -1;
        private double latitude;
        private int loiteringDelayMs = -1;
        private double longitude;
        private float radius;
        private String requestId;
        private int transitionTypes;

        public Geofence build() {
            return new ParcelableGeofence(this.requestId, this.latitude, this.longitude, this.radius, this.durationMillis, this.transitionTypes, this.loiteringDelayMs);
        }

        public Builder setCircularRegion(double latitude2, double longitude2, float radius2) {
            this.latitude = latitude2;
            this.longitude = longitude2;
            this.radius = radius2;
            return this;
        }

        public Builder setExpirationDuration(long durationMillis2) {
            this.durationMillis = durationMillis2;
            return this;
        }

        public Builder setLoiteringDelay(int loiteringDelayMs2) {
            this.loiteringDelayMs = loiteringDelayMs2;
            return this;
        }

        public Builder setNotificationResponsiveness(int notificationResponsivenessMs) {
            throw new RuntimeException("Sorry, not yet implemented");
        }

        public Builder setRequestId(String requestId2) {
            this.requestId = requestId2;
            return this;
        }

        public Builder setTransitionTypes(int transitionTypes2) {
            this.transitionTypes = transitionTypes2;
            return this;
        }
    }
}
