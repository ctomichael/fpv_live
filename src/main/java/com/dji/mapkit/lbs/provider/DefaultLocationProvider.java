package com.dji.mapkit.lbs.provider;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.dji.mapkit.core.models.DJILatLng;
import com.dji.mapkit.lbs.DJILocationLog;
import com.dji.mapkit.lbs.helper.continuoustask.ContinuousTask;

public class DefaultLocationProvider extends LocationProvider implements ContinuousTask.ContinuousTaskRunner, LocationListener {
    private static final String TAG = DefaultLocationProvider.class.getSimpleName();
    private DefaultLocationSource defaultLocationSource;
    private boolean isRequestContinuousLocationRequest = false;
    private String provider;

    public void initialize() {
        super.initialize();
        getSourceProvider().createLocationManager(getContext());
        getSourceProvider().createProviderSwitchTask(this);
        getSourceProvider().createUpdateRequest(this);
    }

    public void onDestroy() {
        super.onDestroy();
        getSourceProvider().removeUpdateRequest();
        getSourceProvider().removeLocationUpdates(this);
        getSourceProvider().removeSwitchTask();
    }

    public void cancel() {
        getSourceProvider().getUpdateRequest().release();
        getSourceProvider().getProviderSwitchTask().stop();
    }

    public void onPause() {
        super.onPause();
        getSourceProvider().getUpdateRequest().release();
        getSourceProvider().getProviderSwitchTask().pause();
    }

    public void onResume() {
        super.onResume();
        this.isRequestContinuousLocationRequest = false;
        getSourceProvider().getUpdateRequest().run();
        if (isWaiting()) {
            getSourceProvider().getProviderSwitchTask().resume();
        }
    }

    public void get() {
        setWaiting(true);
        if (isGPSProviderEnabled()) {
            DJILocationLog.LOGI("GPS is already enabled, getting location...");
            if (getListener() != null) {
                getListener().onProcessTypeChanged(3);
            }
            askForLocation("gps");
            return;
        }
        if (getListener() != null) {
            getListener().onProcessTypeChanged(4);
        }
        getLocationByNetwork();
    }

    public DJILatLng getLastKnownLocation() {
        Location location;
        Location networkLocation = getSourceProvider().getLastKnownLocation("network");
        Location gpsLocation = getSourceProvider().getLastKnownLocation("gps");
        if (networkLocation == null) {
            location = gpsLocation;
        } else if (gpsLocation == null) {
            location = networkLocation;
        } else if (gpsLocation.getElapsedRealtimeNanos() > networkLocation.getElapsedRealtimeNanos()) {
            location = gpsLocation;
        } else {
            location = networkLocation;
        }
        if (location != null) {
            return new DJILatLng(location.getLatitude(), location.getLongitude(), location.getAltitude(), location.getAccuracy(), location.getTime(), location.getElapsedRealtimeNanos());
        }
        return null;
    }

    public void setWifiScanEnable(boolean enable) {
        askForLocation("gps");
    }

    /* access modifiers changed from: package-private */
    public void getLocationByNetwork() {
        if (isNetworkProviderEnabled()) {
            DJILocationLog.LOGI("Network is enabled, getting location...");
            askForLocation("network");
            return;
        }
        DJILocationLog.LOGI("Network is not enabled, calling fail...");
        onLocationFailed(3);
    }

    /* access modifiers changed from: package-private */
    public void askForLocation(String provider2) {
        getSourceProvider().getProviderSwitchTask().stop();
        setCurrentProvider(provider2);
        boolean locationIsAlreadyAvailable = checkForLastKnownLocation();
        if (getConfiguration().keepTracking() || !locationIsAlreadyAvailable) {
            DJILocationLog.LOGI("Ask for location update...");
            notifyProcessChange();
            requestUpdateLocation(0, 0, !locationIsAlreadyAvailable);
            return;
        }
        DJILocationLog.LOGI("We got location, no need to ask for location updates.");
    }

    /* access modifiers changed from: package-private */
    public void requestUpdateLocation(long timeInterval, long distanceInterval, boolean setCancelTask) {
        if (setCancelTask) {
            getSourceProvider().getProviderSwitchTask().delayed(getWaitPeriod());
        }
        getSourceProvider().getUpdateRequest().run(this.provider, timeInterval, (float) distanceInterval);
    }

    /* access modifiers changed from: package-private */
    public long getWaitPeriod() {
        if ("gps".equals(this.provider)) {
            return getConfiguration().defaultProviderConfiguration().gpsWaitPeriod();
        }
        return getConfiguration().defaultProviderConfiguration().networkWaitPeriod();
    }

    /* access modifiers changed from: package-private */
    public void notifyProcessChange() {
        if (getListener() != null) {
            getListener().onProcessTypeChanged("gps".equals(this.provider) ? 3 : 4);
        }
    }

    /* access modifiers changed from: package-private */
    public boolean checkForLastKnownLocation() {
        Location lastKnownLocation = getSourceProvider().getLastKnownLocation(this.provider);
        if (getSourceProvider().isLocationSufficient(lastKnownLocation, getConfiguration().defaultProviderConfiguration().acceptableTimePeriod(), getConfiguration().defaultProviderConfiguration().acceptableAccuracy())) {
            DJILocationLog.LOGI("LastKnownLocation is usable.");
            onLocationReceived(lastKnownLocation);
            return true;
        }
        DJILocationLog.LOGI("LastKnownLocation is not usable.");
        return false;
    }

    @Nullable
    public void onLocationChanged(Location location) {
        if (!getSourceProvider().switchTaskIsRemoved() && !getSourceProvider().updateRequestIsRemoved() && location != null) {
            onLocationReceived(location);
            getSourceProvider().getProviderSwitchTask().stop();
            if (getSourceProvider().getUpdateRequest().isRequiredImmediately() || !getConfiguration().keepTracking()) {
                getSourceProvider().removeLocationUpdates(this);
            }
            if (getConfiguration().keepTracking() && !this.isRequestContinuousLocationRequest) {
                requestUpdateLocation(getConfiguration().defaultProviderConfiguration().requiredTimeInterval(), getConfiguration().defaultProviderConfiguration().requiredDistanceInterval(), false);
                this.isRequestContinuousLocationRequest = true;
            }
        }
    }

    public void onStatusChanged(String provider2, int status, Bundle extras) {
        if (getListener() != null) {
            getListener().onStatusChanged(provider2, status, extras);
        }
    }

    public void onProviderEnabled(String provider2) {
    }

    public void onProviderDisabled(String provider2) {
    }

    public void runScheduledTask(@NonNull String taskId) {
        if (taskId.equals("providerSwitchTask")) {
            getSourceProvider().getUpdateRequest().release();
            if ("gps".equals(this.provider)) {
                DJILocationLog.LOGI("We waited enough for GPS, switching to Network provider...");
                getLocationByNetwork();
                return;
            }
            DJILocationLog.LOGI("Network Provider is not provide location in required period, calling fail...");
            onLocationFailed(1);
        }
    }

    /* access modifiers changed from: package-private */
    @NonNull
    public void onLocationReceived(Location location) {
        if (getListener() != null) {
            getListener().onLocationChanged(new DJILatLng(location.getLatitude(), location.getLongitude(), location.getAltitude(), location.getAccuracy(), location.getTime()));
        }
        setWaiting(false);
    }

    /* access modifiers changed from: package-private */
    public void onLocationFailed(int type) {
        if (getListener() != null) {
            getListener().onLocationFailed(type);
        }
        setWaiting(false);
    }

    /* access modifiers changed from: package-private */
    public void setCurrentProvider(String provider2) {
        this.provider = provider2;
    }

    /* access modifiers changed from: protected */
    public boolean isGPSProviderEnabled() {
        return getSourceProvider().isProviderEnabled("gps");
    }

    /* access modifiers changed from: protected */
    public boolean isNetworkProviderEnabled() {
        return getSourceProvider().isProviderEnabled("network");
    }

    private DefaultLocationSource getSourceProvider() {
        if (this.defaultLocationSource == null) {
            this.defaultLocationSource = new DefaultLocationSource();
        }
        return this.defaultLocationSource;
    }

    /* access modifiers changed from: package-private */
    public void setDefaultLocationSource(DefaultLocationSource defaultLocationSource2) {
        this.defaultLocationSource = defaultLocationSource2;
    }
}
