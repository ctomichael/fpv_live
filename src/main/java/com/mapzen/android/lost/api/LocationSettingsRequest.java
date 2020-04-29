package com.mapzen.android.lost.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class LocationSettingsRequest {
    private final List<LocationRequest> locationRequests;
    private final boolean needBle;

    LocationSettingsRequest(List<LocationRequest> locationRequests2, boolean needBle2) {
        this.locationRequests = locationRequests2;
        this.needBle = needBle2;
    }

    public List<LocationRequest> getLocationRequests() {
        return Collections.unmodifiableList(this.locationRequests);
    }

    public boolean getNeedBle() {
        return this.needBle;
    }

    public static final class Builder {
        private final ArrayList<LocationRequest> locationRequests = new ArrayList<>();
        private boolean needBle = false;

        public Builder addLocationRequest(LocationRequest request) {
            this.locationRequests.add(request);
            return this;
        }

        public Builder addAllLocationRequests(Collection<LocationRequest> requests) {
            this.locationRequests.addAll(requests);
            return this;
        }

        public Builder setNeedBle(boolean needBle2) {
            this.needBle = needBle2;
            return this;
        }

        public LocationSettingsRequest build() {
            return new LocationSettingsRequest(this.locationRequests, this.needBle);
        }
    }
}
