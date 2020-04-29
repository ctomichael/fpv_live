package dji.sdksharedlib.hardware.abstractions.airlink.wifi;

import dji.common.airlink.WiFiFrequencyBand;
import dji.common.airlink.WiFiMagneticInterferenceLevel;
import dji.common.airlink.WiFiSelectionMode;
import dji.common.util.CallbackUtils;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.model.P3.DataOsdGetPushSignalQuality;
import dji.midware.data.model.P3.DataRcGetWifiFreqInfo;
import dji.midware.data.model.P3.DataWifiGetPushElecSignal;
import dji.midware.data.model.P3.DataWifiGetPushSignal;
import dji.midware.data.model.P3.DataWifiGetSelectionMode;
import dji.midware.data.model.P3.DataWifiSetSelectionMode;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.util.DJIEventBusUtil;
import dji.sdksharedlib.hardware.abstractions.Action;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.DJISubComponentHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Getter;
import dji.sdksharedlib.hardware.abstractions.Setter;
import dji.sdksharedlib.keycatalog.airlink.AirLinkKeys;
import dji.sdksharedlib.keycatalog.airlink.WifiLinkKeys;
import dji.sdksharedlib.store.DJISDKCacheStoreLayer;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public abstract class DJIWifiLinkAbstraction extends DJISubComponentHWAbstraction {
    private static final String TAG = "DJISDKCacheWifiLinkSeriesAbstraction";

    @Getter("FrequencyBand")
    public abstract void getFrequencyBand(DJISDKCacheHWAbstraction.InnerCallback innerCallback);

    @Getter("Password")
    public abstract void getPassword(DJISDKCacheHWAbstraction.InnerCallback innerCallback);

    @Getter(WifiLinkKeys.SSID)
    public abstract void getSSID(DJISDKCacheHWAbstraction.InnerCallback innerCallback);

    @Action(WifiLinkKeys.REBOOT)
    public abstract void reboot(DJISDKCacheHWAbstraction.InnerCallback innerCallback);

    @Setter("FrequencyBand")
    public abstract void setFrequencyBand(WiFiFrequencyBand wiFiFrequencyBand, DJISDKCacheHWAbstraction.InnerCallback innerCallback);

    @Setter("Password")
    public abstract void setPassword(String str, DJISDKCacheHWAbstraction.InnerCallback innerCallback);

    @Setter(WifiLinkKeys.SSID)
    public abstract void setSSID(String str, DJISDKCacheHWAbstraction.InnerCallback innerCallback);

    public void init(String component, int index, String subComponent, int subComponentIndex, DJISDKCacheStoreLayer storeLayer, DJISDKCacheHWAbstraction.OnValueChangeListener onValueChangeListener) {
        super.init(component, index, subComponent, subComponentIndex, storeLayer, onValueChangeListener);
        DJIEventBusUtil.register(this);
    }

    public void destroy() {
        DJIEventBusUtil.unRegister(this);
        super.destroy();
    }

    /* access modifiers changed from: protected */
    public void initializeComponentCharacteristics() {
        addCharacteristics(WifiLinkKeys.class, getClass());
    }

    public void syncPushDataFromMidware() {
        if (DataWifiGetPushSignal.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataWifiGetPushSignal.getInstance());
        }
        if (DataWifiGetPushElecSignal.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataWifiGetPushElecSignal.getInstance());
        }
        if (DataOsdGetPushSignalQuality.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataOsdGetPushSignalQuality.getInstance());
        }
    }

    @Getter(WifiLinkKeys.COUNTRY_FROM_GROUND)
    public void getCountryCodeFromGround(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataRcGetWifiFreqInfo.getCcInstance().start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.airlink.wifi.DJIWifiLinkAbstraction.AnonymousClass1 */

            public void onSuccess(Object model) {
                if (callback != null) {
                    callback.onSuccess(DataRcGetWifiFreqInfo.getCcInstance().getCountryCode());
                }
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback);
            }
        });
    }

    @Getter(WifiLinkKeys.COUNTRY_FROM_SKY)
    public void getCountryCodeFromSky(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataRcGetWifiFreqInfo.getSkyCcInstance().start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.airlink.wifi.DJIWifiLinkAbstraction.AnonymousClass2 */

            public void onSuccess(Object model) {
                if (callback != null) {
                    callback.onSuccess(DataRcGetWifiFreqInfo.getSkyCcInstance().getCountryCode());
                }
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback);
            }
        });
    }

    @Setter(WifiLinkKeys.SELECTION_MODE)
    public void setSelectionMode(WiFiSelectionMode mode, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        int i;
        DataWifiSetSelectionMode setter = new DataWifiSetSelectionMode();
        if (mode == WiFiSelectionMode.AUTO) {
            i = 0;
        } else {
            i = 1;
        }
        setter.setSelectionMode(i).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.airlink.wifi.DJIWifiLinkAbstraction.AnonymousClass3 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, (Object) null);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    @Getter(WifiLinkKeys.SELECTION_MODE)
    public void getSelectionMode(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataWifiGetSelectionMode getter = new DataWifiGetSelectionMode();
        getter.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.airlink.wifi.DJIWifiLinkAbstraction.AnonymousClass4 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, getter.getSelectionMode() == 0 ? WiFiSelectionMode.AUTO : WiFiSelectionMode.CUSTOM);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataOsdGetPushSignalQuality mParams) {
        if (mParams.isGetRcQuality()) {
            notifyValueChangeForKeyPath(Integer.valueOf(mParams.getUpSignalQuality()), AirLinkKeys.UPLINK_SIGNAL_QUALITY);
        } else {
            notifyValueChangeForKeyPath(Integer.valueOf(mParams.getUpSignalQuality()), AirLinkKeys.DOWNLINK_SIGNAL_QUALITY);
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataWifiGetPushSignal params) {
        if (params != null) {
            notifyValueChangeForKeyPath(Integer.valueOf(params.getSignal()), AirLinkKeys.DOWNLINK_SIGNAL_QUALITY);
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataWifiGetPushElecSignal params) {
        if (params != null) {
            notifyValueChangeForKeyPath(WiFiMagneticInterferenceLevel.find(params.getSignalStatus().value()), WifiLinkKeys.MAGNETIC_INTERFERENCE);
            notifyValueChangeForKeyPath(params.getSignalStatus(), WifiLinkKeys.WIFI_SIGNAL);
        }
    }
}
