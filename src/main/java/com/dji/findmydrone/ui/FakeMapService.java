package com.dji.findmydrone.ui;

import android.content.Context;
import com.dji.mapkit.core.Mapkit;
import com.dji.mapkit.lbs.DJILocationManager;
import com.dji.mapkit.lbs.DJILocationUtils;
import com.dji.mapkit.lbs.configuration.AMapServiceConfiguration;
import com.dji.mapkit.lbs.configuration.DJILocationConfigurations;
import com.dji.mapkit.lbs.configuration.DefaultProviderConfiguration;
import com.dji.mapkit.lbs.configuration.LocationConfiguration;
import dji.component.map.IMapService;
import dji.service.IDJIService;

class FakeMapService implements IDJIService, IMapService {
    FakeMapService() {
    }

    public boolean isAnyProviderEnabled() {
        return true;
    }

    public boolean isSupportingAMap() {
        return true;
    }

    public boolean canUseAMap() {
        return true;
    }

    public boolean canUseMapbox() {
        return false;
    }

    public void amapAvailability(boolean availability) {
    }

    public LocationConfiguration getDefaultConfiguration(boolean keepTracking, boolean requestUseAMapService) {
        return new LocationConfiguration.Builder().useDefaultProviders(new DefaultProviderConfiguration.Builder().build()).build();
    }

    public LocationConfiguration dynamicHomeConfiguration(boolean requestUseAmapService) {
        return new LocationConfiguration.Builder().useAMapService(new AMapServiceConfiguration.Builder().build()).build();
    }

    public void init(Context context) {
        Mapkit.inMainlandChina(true);
        Mapkit.inHongKong(false);
        Mapkit.inMacau(false);
        DJILocationUtils.injectLocationManager(generateLocMgrWithPrivacyCheck(context));
    }

    public String getName() {
        return IMapService.COMPONENT_NAME;
    }

    public int priority() {
        return 0;
    }

    private DJILocationManager generateLocMgrWithPrivacyCheck(Context context) {
        return new DJILocationManager.Builder(context).configuration(DJILocationConfigurations.defaultConfiguration(false, isSupportingAMap())).build();
    }
}
