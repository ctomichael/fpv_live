package dji.sdksharedlib.hardware.abstractions.airlink;

import android.text.TextUtils;
import dji.common.error.DJIError;
import dji.common.util.CallbackUtils;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.log.GlobalConfig;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.model.P3.DataWiFiSetWiFiCountryCode;
import dji.midware.interfaces.DJIDataCallBack;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.DJISubComponentHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Getter;
import dji.sdksharedlib.hardware.abstractions.Setter;
import dji.sdksharedlib.hardware.abstractions.airlink.lb.LightbridgeAbstraction;
import dji.sdksharedlib.hardware.abstractions.airlink.lte.DJILteLinkAbstraction;
import dji.sdksharedlib.hardware.abstractions.airlink.ocusync.DJIOcuSyncLinkAbstraction;
import dji.sdksharedlib.hardware.abstractions.airlink.wifi.DJIWifiLinkAbstraction;
import dji.sdksharedlib.keycatalog.airlink.AirLinkKeys;
import dji.sdksharedlib.keycatalog.airlink.LightbridgeLinkKeys;
import dji.sdksharedlib.keycatalog.airlink.LteLinkKeys;
import dji.sdksharedlib.keycatalog.airlink.OcuSyncLinkKeys;
import dji.sdksharedlib.keycatalog.airlink.WifiLinkKeys;
import dji.sdksharedlib.store.DJISDKCacheStoreLayer;

@EXClassNullAway
public class DJIAirLinkAbstraction extends DJISDKCacheHWAbstraction {
    private static final String TAG = "DJISDKCacheAirLinkAbstraction";
    protected LightbridgeAbstraction lightbridgeLink;
    protected DJILteLinkAbstraction lteLink;
    protected DJIOcuSyncLinkAbstraction ocuSyncLink;
    protected DJIWifiLinkAbstraction wifiLink;

    public DJIAirLinkAbstraction(DJIWifiLinkAbstraction djiWiFiLink, DJISubComponentHWAbstraction airlink) {
        this.wifiLink = djiWiFiLink;
        if (airlink == null) {
            return;
        }
        if (airlink instanceof LightbridgeAbstraction) {
            this.lightbridgeLink = (LightbridgeAbstraction) airlink;
        } else if (airlink instanceof DJIOcuSyncLinkAbstraction) {
            this.ocuSyncLink = (DJIOcuSyncLinkAbstraction) airlink;
        } else {
            throw new RuntimeException("Wrong Abstraction Class");
        }
    }

    public DJIAirLinkAbstraction(DJILteLinkAbstraction djiLteLink, DJIWifiLinkAbstraction djiWifiLink, DJISubComponentHWAbstraction airLink) {
        this(djiWifiLink, airLink);
        this.lteLink = djiLteLink;
    }

    /* access modifiers changed from: protected */
    public void initializeComponentCharacteristics() {
        addCharacteristics(AirLinkKeys.class, getClass());
    }

    /* access modifiers changed from: protected */
    public void initializeSubComponents(DJISDKCacheStoreLayer storeLayer) {
        addSubComponents(this.wifiLink, WifiLinkKeys.COMPONENT_KEY, 0, storeLayer);
        addSubComponents(this.lightbridgeLink, LightbridgeLinkKeys.COMPONENT_KEY, 0, storeLayer);
        addSubComponents(this.ocuSyncLink, OcuSyncLinkKeys.COMPONENT_KEY, 0, storeLayer);
        addSubComponents(this.lteLink, LteLinkKeys.COMPONENT_KEY, 0, storeLayer);
    }

    @Getter(AirLinkKeys.IS_LIGHTBRIDGE_LINK_SUPPORTED)
    public void isLightbridgeLinkSupported(DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            callback.onSuccess(Boolean.valueOf(this.lightbridgeLink != null));
        }
    }

    @Getter(AirLinkKeys.IS_WIFI_LINK_SUPPORTED)
    public void isWiFiLinkSupported(DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            callback.onSuccess(Boolean.valueOf(this.wifiLink != null));
        }
    }

    @Getter(AirLinkKeys.IS_LTE_LINK_SUPPORTED)
    public void isLteLinkSupported(DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            callback.onSuccess(Boolean.valueOf(this.lteLink != null));
        }
    }

    @Setter("CountryCode")
    public void setCountryCode(final String countryCode, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (TextUtils.isEmpty(countryCode)) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_INVALID);
        } else {
            DataWiFiSetWiFiCountryCode.getInstance().set2Point4GCountryCode(countryCode).set5GCountryCode(countryCode).setSupported5G(true).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.airlink.DJIAirLinkAbstraction.AnonymousClass1 */

                public void onSuccess(Object model) {
                    CallbackUtils.onSuccess(callback, (Object) null);
                    if (GlobalConfig.DEBUG) {
                        DJILog.saveLog("country code success: " + countryCode, "airlink_country_code");
                    }
                }

                public void onFailure(Ccode ccode) {
                    CallbackUtils.onFailure(callback, ccode);
                }
            });
        }
    }

    public void syncPushDataFromMidware() {
        super.syncPushDataFromMidware();
        if (this.wifiLink != null) {
            this.wifiLink.syncPushDataFromMidware();
        }
        if (this.lightbridgeLink != null) {
            this.lightbridgeLink.syncPushDataFromMidware();
        }
        if (this.ocuSyncLink != null) {
            this.ocuSyncLink.syncPushDataFromMidware();
        }
    }
}
