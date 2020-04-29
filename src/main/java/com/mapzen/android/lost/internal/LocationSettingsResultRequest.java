package com.mapzen.android.lost.internal;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import com.mapzen.android.lost.api.LocationRequest;
import com.mapzen.android.lost.api.LocationSettingsRequest;
import com.mapzen.android.lost.api.LocationSettingsResult;
import com.mapzen.android.lost.api.LocationSettingsStates;
import com.mapzen.android.lost.api.PendingResult;
import com.mapzen.android.lost.api.ResultCallback;
import com.mapzen.android.lost.api.Status;
import dji.publics.protocol.ResponseBase;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

class LocationSettingsResultRequest extends PendingResult<LocationSettingsResult> {
    private SettingsDialogDisplayer dialogDisplayer = new SettingsDialogDisplayer();
    private Future<LocationSettingsResult> future;
    private final LocationManager locationManager;
    private final PackageManager packageManager;
    private final PendingIntentGenerator pendingIntentGenerator;
    private ResultCallback<? super LocationSettingsResult> resultCallback;
    private final LocationSettingsRequest settingsRequest;

    LocationSettingsResultRequest(Context context, PendingIntentGenerator generator, LocationSettingsRequest request) {
        this.packageManager = context.getPackageManager();
        this.locationManager = (LocationManager) context.getSystemService(ResponseBase.STRING_LOCATION);
        this.pendingIntentGenerator = generator;
        this.settingsRequest = request;
    }

    @NonNull
    public LocationSettingsResult await() {
        return generateLocationSettingsResult();
    }

    @NonNull
    public LocationSettingsResult await(long time, @NonNull TimeUnit timeUnit) {
        return generateLocationSettingsResult(time, timeUnit);
    }

    public void cancel() {
        if (this.future != null && !this.future.isCancelled()) {
            this.future.cancel(true);
        }
    }

    public boolean isCanceled() {
        if (this.future == null) {
            return false;
        }
        return this.future.isCancelled();
    }

    public void setResultCallback(@NonNull ResultCallback<? super LocationSettingsResult> callback) {
        this.resultCallback = callback;
        this.resultCallback.onResult(generateLocationSettingsResult());
    }

    public void setResultCallback(@NonNull ResultCallback<? super LocationSettingsResult> callback, long time, @NonNull TimeUnit timeUnit) {
        this.resultCallback = callback;
        this.resultCallback.onResult(generateLocationSettingsResult(time, timeUnit));
    }

    /* access modifiers changed from: private */
    public LocationSettingsResult generateLocationSettingsResult() {
        Status status;
        boolean needGps = false;
        boolean needNetwork = false;
        for (LocationRequest request : this.settingsRequest.getLocationRequests()) {
            switch (request.getPriority()) {
                case 100:
                    needGps = true;
                    needNetwork = true;
                    break;
                case 102:
                case 104:
                    needNetwork = true;
                    break;
            }
        }
        boolean needBle = this.settingsRequest.getNeedBle();
        boolean gpsUsable = this.locationManager.isProviderEnabled("gps");
        boolean gpsPresent = this.packageManager.hasSystemFeature("android.hardware.location.gps");
        boolean networkUsable = this.locationManager.isProviderEnabled("network");
        boolean networkPresent = this.packageManager.hasSystemFeature("android.hardware.location.network");
        boolean bleUsable = networkUsable;
        boolean blePresent = this.packageManager.hasSystemFeature("android.hardware.bluetooth_le");
        boolean hasResolution = (needGps && gpsPresent && !gpsUsable) || (needNetwork && networkPresent && !networkUsable) || (needBle && blePresent && !bleUsable);
        boolean resolutionUnavailable = (needGps && !gpsPresent) || (needNetwork && !networkPresent) || (needBle && !blePresent);
        if (hasResolution) {
            status = new Status(6, this.dialogDisplayer, this.pendingIntentGenerator.generatePendingIntent());
        } else if (resolutionUnavailable) {
            status = new Status(8502, this.dialogDisplayer);
        } else {
            status = new Status(0, this.dialogDisplayer);
        }
        return new LocationSettingsResult(status, new LocationSettingsStates(gpsUsable, networkUsable, bleUsable, gpsPresent, networkPresent, blePresent));
    }

    private LocationSettingsResult generateLocationSettingsResult(long time, TimeUnit timeUnit) {
        LocationSettingsResult result;
        ExecutorService executor = Executors.newSingleThreadExecutor();
        this.future = executor.submit(new Callable() {
            /* class com.mapzen.android.lost.internal.LocationSettingsResultRequest.AnonymousClass1 */

            public LocationSettingsResult call() throws Exception {
                return LocationSettingsResultRequest.this.generateLocationSettingsResult();
            }
        });
        try {
            result = this.future.get(time, timeUnit);
        } catch (TimeoutException e) {
            result = createResultForStatus(15);
        } catch (InterruptedException e2) {
            result = createResultForStatus(14);
        } catch (ExecutionException e3) {
            result = createResultForStatus(8);
        }
        executor.shutdownNow();
        return result;
    }

    private LocationSettingsResult createResultForStatus(int statusType) {
        return new LocationSettingsResult(new Status(statusType, this.dialogDisplayer), null);
    }
}
