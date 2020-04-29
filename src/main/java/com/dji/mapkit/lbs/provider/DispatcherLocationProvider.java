package com.dji.mapkit.lbs.provider;

import android.app.Dialog;
import android.content.Intent;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.amap.api.location.AMapLocationClientOption;
import com.dji.mapkit.core.models.DJILatLng;
import com.dji.mapkit.lbs.DJILocationLog;
import com.dji.mapkit.lbs.configuration.AMapServiceConfiguration;
import com.dji.mapkit.lbs.configuration.DJILocationConfigurations;
import com.dji.mapkit.lbs.helper.continuoustask.ContinuousTask;
import com.dji.mapkit.lbs.listener.FallbackListener;

public class DispatcherLocationProvider extends LocationProvider implements ContinuousTask.ContinuousTaskRunner, FallbackListener {
    private static final String TAG = DispatcherLocationProvider.class.getSimpleName();
    private LocationProvider activeProvider;
    private AMapServiceLocationProvider amapServiceLocationProvider;
    private DefaultLocationProvider defaultLocationProvider;
    private DispatcherLocationSource dispatcherLocationSource;
    private GooglePlayServicesLocationProvider googlePlayServicesLocationProvider;
    private Dialog gpServiceDialog;
    private LostServicesLocationProvider lostServicesLocationProvider;

    public void initialize() {
        super.initialize();
        getSourceProvider().createSwitchTask(this);
    }

    public void onPause() {
        super.onPause();
        if (this.activeProvider != null) {
            this.activeProvider.onPause();
        }
        getSourceProvider().gpServicesSwitchTask().pause();
        getSourceProvider().amapServiceSwitchTask().pause();
        getSourceProvider().lostServiceSwitchTask().pause();
    }

    public void onResume() {
        super.onResume();
        if (this.activeProvider != null) {
            this.activeProvider.onResume();
        }
        getSourceProvider().gpServicesSwitchTask().resume();
        getSourceProvider().amapServiceSwitchTask().resume();
        getSourceProvider().lostServiceSwitchTask().resume();
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.activeProvider != null) {
            this.activeProvider.onDestroy();
        }
        getSourceProvider().gpServicesSwitchTask().stop();
        getSourceProvider().amapServiceSwitchTask().stop();
        getSourceProvider().lostServiceSwitchTask().stop();
        this.dispatcherLocationSource = null;
        this.gpServiceDialog = null;
    }

    public void onFallback() {
        cancel();
        if (this.activeProvider instanceof AMapServiceLocationProvider) {
            if (DJILocationConfigurations.isUseLostService.booleanValue()) {
                continueWithLostServiceProvider();
            } else {
                continueWithDefaultProviders();
            }
        } else if (this.activeProvider instanceof LostServicesLocationProvider) {
            continueWithDefaultProviders();
        }
    }

    public void runScheduledTask(@NonNull String taskId) {
        if (taskId.equals("googlePlayServiceSwitchTask")) {
            if ((this.activeProvider instanceof GooglePlayServicesLocationProvider) && this.activeProvider.isWaiting()) {
                cancel();
                if (DJILocationConfigurations.isUseLostService.booleanValue()) {
                    continueWithLostServiceProvider();
                } else {
                    continueWithDefaultProviders();
                }
            }
        } else if (taskId.equals("amapServiceSwitchTask")) {
            if ((this.activeProvider instanceof AMapServiceLocationProvider) && this.activeProvider.isWaiting()) {
                cancel();
                if (DJILocationConfigurations.isUseLostService.booleanValue()) {
                    continueWithLostServiceProvider();
                } else {
                    continueWithDefaultProviders();
                }
            }
        } else if (taskId.equals("lostServiceSwitchTask") && (this.activeProvider instanceof LostServicesLocationProvider) && this.activeProvider.isWaiting()) {
            cancel();
            continueWithDefaultProviders();
        }
    }

    public void get() {
        if (getConfiguration().amapServiceConfiguration() != null) {
            DJILocationLog.LOGI("Configuration requires  to use AMap Service Location Provider ");
            continueWithAMapServiceProvider();
        } else if (getConfiguration().lostServicesConfiguration() != null) {
            DJILocationLog.LOGI("Configuration requires not to use AMap Service, so skipping that step to LOST Service Location Provider");
            continueWithLostServiceProvider();
        } else {
            DJILocationLog.LOGI("Configuration requires not to use LOST Services, so skipping that step to Default Location Providers");
            continueWithDefaultProviders();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && this.activeProvider != null) {
            this.activeProvider.onActivityResult(requestCode, resultCode, data);
        }
    }

    /* access modifiers changed from: package-private */
    public void checkGooglePlayServicesAvailability(boolean askForGooglePlayServices) {
        if (getSourceProvider().isGoogleApiAvailable(getContext()) == 0) {
            DJILocationLog.LOGI("GooglePlayServices is available on device.");
            getLocationFromGooglePlayServices();
            return;
        }
        DJILocationLog.LOGI("GooglePlayServices is NOT available on device.");
        if (!askForGooglePlayServices) {
            DJILocationLog.LOGI("continue with LOST providers");
            if (DJILocationConfigurations.isUseLostService.booleanValue()) {
                continueWithLostServiceProvider();
            } else {
                continueWithDefaultProviders();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void getLocationFromGooglePlayServices() {
        DJILocationLog.LOGI("Attempting to get location from GooglePlayServices providers...");
        this.provider = 1;
        setLocationProvider(getSourceProvider().createGooglePlayServicesLocationProvider(this));
        getSourceProvider().gpServicesSwitchTask().delayed(getConfiguration().googlePlayServicesConfiguration().googlePlayServicesWaitPeriod());
        this.activeProvider.get();
    }

    @Nullable
    public DJILatLng getLastKnownLocation() {
        DJILatLng sysLatLng;
        DJILatLng latLng;
        DJILatLng amapLatLng = null;
        DJILatLng lostLatLng = null;
        DJILatLng defaultLatLng = null;
        if (this.provider != 0 && !(this.activeProvider instanceof DispatcherLocationProvider)) {
            return this.activeProvider.getLastKnownLocation();
        }
        if (getConfiguration().amapServiceConfiguration() != null) {
            amapLatLng = getLastKnownLocationFromAMapServiceProvider();
        }
        if (getConfiguration().lostServicesConfiguration() != null) {
            lostLatLng = getLastKnownLocationFromLostServiceProvider();
        }
        if (getConfiguration().defaultProviderConfiguration() != null) {
            defaultLatLng = getLastKnownLocationFromDefaultProviders();
        }
        if (DJILocationConfigurations.isUseLostService.booleanValue()) {
            sysLatLng = lostLatLng;
        } else {
            sysLatLng = defaultLatLng;
        }
        long elapsedRealtimeNanos = SystemClock.elapsedRealtimeNanos();
        if (amapLatLng == null && sysLatLng == null) {
            return null;
        }
        if (amapLatLng == null) {
            return sysLatLng;
        }
        if (sysLatLng == null) {
            return amapLatLng;
        }
        long amapLocationElapsedRealtimeNanos = amapLatLng.getElapsedRealtimeNanos();
        long sysLocationElapsedRealtimeNanos = sysLatLng.getElapsedRealtimeNanos();
        long deltaSystemLocationTimeNanos = elapsedRealtimeNanos - sysLocationElapsedRealtimeNanos;
        if (elapsedRealtimeNanos - amapLocationElapsedRealtimeNanos >= 40000000 || deltaSystemLocationTimeNanos >= 40000000) {
            if (amapLocationElapsedRealtimeNanos > sysLocationElapsedRealtimeNanos) {
                latLng = amapLatLng;
            } else {
                latLng = sysLatLng;
            }
        } else if (amapLatLng.getAccuracy() <= sysLatLng.getAccuracy()) {
            latLng = amapLatLng;
        } else {
            latLng = sysLatLng;
        }
        return latLng;
    }

    public void setWifiScanEnable(boolean enable) {
        AMapServiceConfiguration amapServiceConfiguration;
        if (this.activeProvider != null) {
            this.activeProvider.setWifiScanEnable(enable);
        }
        if (!(this.activeProvider instanceof AMapServiceLocationProvider) && (amapServiceConfiguration = getConfiguration().amapServiceConfiguration()) != null) {
            AMapLocationClientOption option = amapServiceConfiguration.clientOption();
            if (enable) {
                option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
                option.setGpsFirst(false).setWifiActiveScan(true);
                option.setWifiScan(true);
                return;
            }
            option.setGpsFirst(true).setWifiActiveScan(false);
            option.setWifiScan(false);
        }
    }

    public void cancel() {
        if (this.activeProvider != null) {
            this.activeProvider.cancel();
        }
        getSourceProvider().gpServicesSwitchTask().stop();
        getSourceProvider().amapServiceSwitchTask().stop();
        getSourceProvider().lostServiceSwitchTask().stop();
    }

    public boolean isWaiting() {
        return this.activeProvider != null && this.activeProvider.isWaiting();
    }

    /* access modifiers changed from: package-private */
    public void continueWithDefaultProviders() {
        if (getConfiguration().defaultProviderConfiguration() == null) {
            DJILocationLog.LOGI("Configuration requires not to use default providers, abort!");
            if (getListener() != null) {
                getListener().onLocationFailed(7);
                return;
            }
            return;
        }
        DJILocationLog.LOGI("Attempting to get location from default providers...");
        this.provider = 4;
        setLocationProvider(getSourceProvider().createDefaultLocationProvider());
        this.activeProvider.get();
    }

    /* access modifiers changed from: package-private */
    public void continueWithAMapServiceProvider() {
        if (getConfiguration().amapServiceConfiguration() == null) {
            DJILocationLog.LOGI("Configuration requires not to use AMap provider, abort!");
            if (getListener() != null) {
                getListener().onLocationFailed(6);
            }
            if (DJILocationConfigurations.isUseLostService.booleanValue()) {
                continueWithLostServiceProvider();
            } else {
                continueWithDefaultProviders();
            }
        } else {
            DJILocationLog.LOGI("Attempting to get location from AMap Service Location Provider...");
            this.provider = 5;
            setLocationProvider(getSourceProvider().createAMapServiceLocationProvider(this));
            getSourceProvider().amapServiceSwitchTask().delayed(getConfiguration().amapServiceConfiguration().amapServiceWaitPeriod());
            this.activeProvider.get();
        }
    }

    /* access modifiers changed from: package-private */
    public void continueWithLostServiceProvider() {
        if (getConfiguration().lostServicesConfiguration() == null) {
            DJILocationLog.LOGI("Configuration requires not to use Lost provider, abort!");
            if (getListener() != null) {
                getListener().onLocationFailed(10);
            }
            continueWithDefaultProviders();
            return;
        }
        DJILocationLog.LOGI("Attempting to get location from LOST Service Location Provider...");
        this.provider = 6;
        setLocationProvider(getSourceProvider().createLostServicesLocationProvider(this));
        getSourceProvider().lostServiceSwitchTask().delayed(getConfiguration().lostServicesConfiguration().lostServicesWaitPeriod());
        this.activeProvider.get();
    }

    /* access modifiers changed from: package-private */
    @Nullable
    public DJILatLng getLastKnownLocationFromAMapServiceProvider() {
        DJILocationLog.LOGI("Attempting to get last known location from AMap Service Location Provider...");
        if (this.amapServiceLocationProvider == null) {
            this.amapServiceLocationProvider = getSourceProvider().createAMapServiceLocationProvider(this);
        }
        setLocationProvider(this.amapServiceLocationProvider);
        return this.activeProvider.getLastKnownLocation();
    }

    /* access modifiers changed from: package-private */
    @Nullable
    public DJILatLng getLastKnownLocationFromLostServiceProvider() {
        DJILocationLog.LOGI("Attempting to get last known location from Lost Service Provider...");
        if (this.lostServicesLocationProvider == null) {
            this.lostServicesLocationProvider = getSourceProvider().createLostServicesLocationProvider(this);
        }
        setLocationProvider(this.lostServicesLocationProvider);
        return this.activeProvider.getLastKnownLocation();
    }

    /* access modifiers changed from: package-private */
    @Nullable
    public DJILatLng getLastKnownLocationFromDefaultProviders() {
        DJILocationLog.LOGI("Attempting to get last known location from default providers...");
        if (this.defaultLocationProvider == null) {
            this.defaultLocationProvider = getSourceProvider().createDefaultLocationProvider();
        }
        setLocationProvider(this.defaultLocationProvider);
        return this.activeProvider.getLastKnownLocation();
    }

    /* access modifiers changed from: package-private */
    public DJILatLng getLastKnownLocationFromGoogleServiceProvider() {
        if (this.googlePlayServicesLocationProvider == null) {
            this.googlePlayServicesLocationProvider = getSourceProvider().createGooglePlayServicesLocationProvider(this);
        }
        setLocationProvider(this.googlePlayServicesLocationProvider);
        DJILocationLog.LOGI("Attempting to get last known location from Google Play Service provider..." + this.activeProvider.getLastKnownLocation());
        return this.activeProvider.getLastKnownLocation();
    }

    /* access modifiers changed from: package-private */
    public void setLocationProvider(LocationProvider provider) {
        this.activeProvider = provider;
        this.activeProvider.configure(this);
    }

    private DispatcherLocationSource getSourceProvider() {
        if (this.dispatcherLocationSource == null) {
            this.dispatcherLocationSource = new DispatcherLocationSource();
        }
        return this.dispatcherLocationSource;
    }

    /* access modifiers changed from: package-private */
    public void setDispatcherLocationSource(DispatcherLocationSource dispatcherLocationSource2) {
        this.dispatcherLocationSource = dispatcherLocationSource2;
    }
}
