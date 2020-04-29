package com.dji.mapkit.lbs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import com.dji.mapkit.core.models.DJILatLng;
import com.dji.mapkit.lbs.configuration.LocationConfiguration;
import com.dji.mapkit.lbs.listener.DJILocationListener;
import com.dji.mapkit.lbs.provider.DispatcherLocationProvider;
import com.dji.mapkit.lbs.provider.LocationProvider;
import com.dji.mapkit.lbs.view.ContextProcessor;
import com.dji.permission.Permission;

public class DJILocationManager {
    private static boolean hasPermissionGranted = false;
    private LocationProvider activeProvider;
    private LocationConfiguration configuration;
    private ContextProcessor contextProcessor;
    private DJILocationListener listener;

    private DJILocationManager(Builder builder) {
        this.listener = builder.listener;
        this.configuration = builder.configuration;
        this.activeProvider = builder.activeProvider;
        this.contextProcessor = builder.contextProcessor;
        if (!hasPermissionGranted) {
            hasPermissionGranted = isPermissionGranted();
        }
    }

    public LocationConfiguration getConfiguration() {
        return this.configuration;
    }

    public void onPause() {
        this.activeProvider.onPause();
    }

    public void onResume() {
        this.activeProvider.onResume();
    }

    public void onDestroy() {
        this.activeProvider.onDestroy();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        this.activeProvider.onActivityResult(requestCode, resultCode, data);
    }

    public boolean isWaitingForLocation() {
        return this.activeProvider.isWaiting();
    }

    public void cancel() {
        this.activeProvider.cancel();
    }

    public boolean isProviderGot() {
        if (!hasPermissionGranted) {
            return false;
        }
        this.activeProvider.get();
        return true;
    }

    @Nullable
    public DJILatLng getLastKnownLocation() {
        if (hasPermissionGranted) {
            return this.activeProvider.getLastKnownLocation();
        }
        return null;
    }

    public void setWifiScanEnable(boolean enable) {
        this.activeProvider.setWifiScanEnable(enable);
    }

    public float getLastKnownLocationAccuracy() {
        DJILatLng lastKnownLocation = getLastKnownLocation();
        if (lastKnownLocation != null) {
            return lastKnownLocation.getAccuracy();
        }
        return 10000.0f;
    }

    public static class Builder {
        /* access modifiers changed from: private */
        public LocationProvider activeProvider;
        /* access modifiers changed from: private */
        public LocationConfiguration configuration;
        /* access modifiers changed from: private */
        public ContextProcessor contextProcessor;
        /* access modifiers changed from: private */
        public DJILocationListener listener;

        public Builder(@NonNull Context applicationContext) {
            this.contextProcessor = new ContextProcessor(applicationContext);
        }

        public Builder(@NonNull ContextProcessor contextProcessor2) {
            this.contextProcessor = contextProcessor2;
        }

        public Builder activity(Activity activity) {
            this.contextProcessor.setActivity(activity);
            return this;
        }

        public Builder fragment(Fragment fragment) {
            this.contextProcessor.setFragment(fragment);
            return this;
        }

        public Builder locationEnableRationale(Dialog dialog) {
            this.contextProcessor.setDialog(dialog);
            return this;
        }

        public Builder configuration(@NonNull LocationConfiguration locationConfiguration) {
            this.configuration = locationConfiguration;
            return this;
        }

        public Builder locationProvider(@NonNull LocationProvider provider) {
            this.activeProvider = provider;
            return this;
        }

        public Builder notify(DJILocationListener listener2) {
            this.listener = listener2;
            return this;
        }

        public DJILocationManager build() {
            if (this.contextProcessor == null) {
                throw new IllegalStateException("You must set a context to LocationManager.");
            } else if (this.configuration == null) {
                throw new IllegalStateException("You must set a configuration object.");
            } else {
                if (this.activeProvider == null) {
                    locationProvider(new DispatcherLocationProvider());
                }
                this.activeProvider.configure(this.contextProcessor, this.configuration, this.listener);
                return new DJILocationManager(this);
            }
        }
    }

    private boolean isPermissionGranted() {
        int permissionCheck = ContextCompat.checkSelfPermission(this.contextProcessor.getContext(), Permission.ACCESS_FINE_LOCATION);
        if (Build.VERSION.SDK_INT < 23 || permissionCheck == 0) {
            return true;
        }
        return false;
    }
}
