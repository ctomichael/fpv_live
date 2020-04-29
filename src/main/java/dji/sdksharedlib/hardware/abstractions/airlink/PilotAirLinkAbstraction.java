package dji.sdksharedlib.hardware.abstractions.airlink;

import dji.internal.PilotLinkHighPowerHelper;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.DJISubComponentHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.airlink.lte.DJILteLinkAbstraction;
import dji.sdksharedlib.hardware.abstractions.airlink.wifi.DJIWifiLinkAbstraction;
import dji.sdksharedlib.store.DJISDKCacheStoreLayer;

public class PilotAirLinkAbstraction extends DJIAirLinkAbstraction {
    private PilotLinkHighPowerHelper mLinkHighPowerHelper;

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public PilotAirLinkAbstraction(DJIAirLinkAbstraction airlink) {
        this(airlink.lteLink, airlink.wifiLink, airlink.lightbridgeLink == null ? airlink.ocuSyncLink : airlink.lightbridgeLink);
    }

    public PilotAirLinkAbstraction(DJIWifiLinkAbstraction djiWiFiLink, DJISubComponentHWAbstraction airlink) {
        super(djiWiFiLink, airlink);
    }

    public PilotAirLinkAbstraction(DJILteLinkAbstraction djiLteLink, DJIWifiLinkAbstraction djiWifiLink, DJISubComponentHWAbstraction airLink) {
        super(djiLteLink, djiWifiLink, airLink);
    }

    public void init(String component, int index, DJISDKCacheStoreLayer storeLayer, DJISDKCacheHWAbstraction.OnValueChangeListener onValueChangeListener) {
        super.init(component, index, storeLayer, onValueChangeListener);
        this.mLinkHighPowerHelper = PilotLinkHighPowerHelper.getInstance();
        this.mLinkHighPowerHelper.onCreate();
        if (this.ocuSyncLink != null && this.mLinkHighPowerHelper.isSupportSdrHighPowerMode()) {
            this.mLinkHighPowerHelper.openHighPowerByWriteConfig();
        }
    }

    public void setCountryCode(String countryCode, DJISDKCacheHWAbstraction.InnerCallback callback) {
        String newCountryCode = countryCode;
        if (this.mLinkHighPowerHelper != null && this.mLinkHighPowerHelper.isSupportLbHighPowerMode()) {
            newCountryCode = this.mLinkHighPowerHelper.openHighPowerByCountryCode(countryCode);
        }
        super.setCountryCode(newCountryCode, callback);
    }

    public synchronized void destroy() {
        super.destroy();
        this.mLinkHighPowerHelper.destroy();
        this.mLinkHighPowerHelper = null;
    }
}
