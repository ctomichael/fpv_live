package com.mapzen.android.lost.api;

import java.util.ArrayList;
import java.util.List;

public class GeofencingRequest {
    private List<Geofence> geofences;

    private GeofencingRequest(List<Geofence> geofences2) {
        this.geofences = geofences2;
    }

    public List<Geofence> getGeofences() {
        return this.geofences;
    }

    public static final class Builder {
        private List<Geofence> geofences = new ArrayList();

        public GeofencingRequest build() {
            if (!this.geofences.isEmpty()) {
                return new GeofencingRequest(this.geofences);
            }
            throw new IllegalArgumentException("No geofence has been added to this request.");
        }

        public Builder addGeofence(Geofence geofence) {
            if (geofence == null) {
                throw new IllegalArgumentException("Geofence cannot be null");
            }
            this.geofences.add(geofence);
            return this;
        }

        public Builder addGeofences(List<Geofence> geofences2) {
            if (geofences2 == null) {
                throw new IllegalArgumentException("Geofence cannot be null");
            }
            this.geofences.addAll(geofences2);
            return this;
        }
    }
}
