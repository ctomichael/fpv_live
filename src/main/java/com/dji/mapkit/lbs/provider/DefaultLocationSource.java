package com.dji.mapkit.lbs.provider;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import com.dji.mapkit.lbs.helper.UpdateRequest;
import com.dji.mapkit.lbs.helper.continuoustask.ContinuousTask;
import dji.publics.protocol.ResponseBase;
import java.util.Date;

class DefaultLocationSource {
    static final String PROVIDER_SWITCH_TASK = "providerSwitchTask";
    private ContinuousTask cancelTask;
    private LocationManager locationManager;
    private UpdateRequest updateRequest;

    DefaultLocationSource() {
    }

    /* access modifiers changed from: package-private */
    public void createLocationManager(Context context) {
        this.locationManager = (LocationManager) context.getSystemService(ResponseBase.STRING_LOCATION);
    }

    /* access modifiers changed from: package-private */
    public void createUpdateRequest(LocationListener locationListener) {
        this.updateRequest = new UpdateRequest(this.locationManager, locationListener);
    }

    /* access modifiers changed from: package-private */
    public void createProviderSwitchTask(ContinuousTask.ContinuousTaskRunner continuousTaskRunner) {
        this.cancelTask = new ContinuousTask(PROVIDER_SWITCH_TASK, continuousTaskRunner);
    }

    /* access modifiers changed from: package-private */
    public boolean isProviderEnabled(String provider) {
        if (this.locationManager != null) {
            return this.locationManager.isProviderEnabled(provider);
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public Location getLastKnownLocation(String provider) {
        if (this.locationManager != null) {
            return this.locationManager.getLastKnownLocation(provider);
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public void removeLocationUpdates(LocationListener locationListener) {
        if (locationListener != null) {
            this.locationManager.removeUpdates(locationListener);
        }
    }

    /* access modifiers changed from: package-private */
    public void removeUpdateRequest() {
        this.updateRequest.release();
        this.updateRequest = null;
    }

    /* access modifiers changed from: package-private */
    public void removeSwitchTask() {
        this.cancelTask.stop();
        this.cancelTask = null;
    }

    /* access modifiers changed from: package-private */
    public boolean switchTaskIsRemoved() {
        return this.cancelTask == null;
    }

    /* access modifiers changed from: package-private */
    public boolean updateRequestIsRemoved() {
        return this.updateRequest == null;
    }

    /* access modifiers changed from: package-private */
    public ContinuousTask getProviderSwitchTask() {
        return this.cancelTask;
    }

    /* access modifiers changed from: package-private */
    public UpdateRequest getUpdateRequest() {
        return this.updateRequest;
    }

    /* access modifiers changed from: package-private */
    public boolean isLocationSufficient(Location location, long acceptableTimePeriod, float acceptableAccuracy) {
        if (location == null) {
            return false;
        }
        float givenAccuracy = location.getAccuracy();
        if (new Date().getTime() - acceptableTimePeriod > location.getTime() || acceptableAccuracy < givenAccuracy) {
            return false;
        }
        return true;
    }
}
