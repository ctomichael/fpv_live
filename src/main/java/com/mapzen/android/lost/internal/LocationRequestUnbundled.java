package com.mapzen.android.lost.internal;

import com.mapzen.android.lost.api.LocationRequest;
import java.util.ArrayList;
import java.util.List;
import kotlin.jvm.internal.LongCompanionObject;

public class LocationRequestUnbundled {
    private long fastestInterval = LongCompanionObject.MAX_VALUE;
    private List<LocationRequest> requests = new ArrayList();

    public void addRequest(LocationRequest request) {
        if (request.getFastestInterval() < this.fastestInterval) {
            this.fastestInterval = request.getFastestInterval();
        }
        this.requests.add(request);
    }

    public void removeRequests(List<LocationRequest> requests2) {
        this.requests.removeAll(requests2);
        this.fastestInterval = calculateFastestInterval();
    }

    public void removeAllRequests() {
        this.fastestInterval = LongCompanionObject.MAX_VALUE;
        this.requests.clear();
    }

    public List<LocationRequest> getRequests() {
        return this.requests;
    }

    public long getFastestInterval() {
        return this.fastestInterval;
    }

    public long calculateFastestInterval() {
        long interval = LongCompanionObject.MAX_VALUE;
        for (LocationRequest request : this.requests) {
            if (request.getFastestInterval() < interval) {
                interval = request.getFastestInterval();
            }
        }
        return interval;
    }
}
