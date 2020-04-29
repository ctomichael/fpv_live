package dji.sdksharedlib.hardware.abstractions.airlink.wifi;

import dji.common.airlink.WiFiFrequencyBand;
import dji.common.airlink.WiFiSelectionMode;
import dji.common.error.DJIAirLinkError;
import dji.common.error.DJIError;
import dji.common.util.CallbackUtils;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.model.P3.DataWifiGetPower;
import dji.midware.data.model.P3.DataWifiGetPushSignal;
import dji.midware.data.model.P3.DataWifiGetPushWifiComplexInfo;
import dji.midware.data.model.P3.DataWifiSetPower;
import dji.midware.data.model.P3.DataWifiSetSweepFrequency;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.util.DJIEventBusUtil;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Getter;
import dji.sdksharedlib.hardware.abstractions.Setter;
import dji.sdksharedlib.hardware.abstractions.airlink.wifi.DJIWifiLinkFoldingDroneAbstraction;
import dji.sdksharedlib.keycatalog.airlink.WifiLinkKeys;
import dji.sdksharedlib.store.DJISDKCacheStoreLayer;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class DJIWifiLinkWM160Abstraction extends DJIWifiLinkWm100GroundAbstraction {
    public void init(String component, int index, DJISDKCacheStoreLayer storeLayer, DJISDKCacheHWAbstraction.OnValueChangeListener onValueChangeListener) {
        super.init(component, index, storeLayer, onValueChangeListener);
    }

    public void syncPushDataFromMidware() {
        super.syncPushDataFromMidware();
        if (DataWifiGetPushWifiComplexInfo.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataWifiGetPushWifiComplexInfo.getInstance());
        }
        if (this.rssiEventHandler == null) {
            this.rssiEventHandler = new DJIWifiLinkFoldingDroneAbstraction.RSSIEventHandler();
        }
        DJIEventBusUtil.register(this.rssiEventHandler);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataWifiGetPushWifiComplexInfo wifiComplexInfo) {
        notifyValueChangeForKeyPath(Integer.valueOf(wifiComplexInfo.getCurChannel()), convertKeyToPath(WifiLinkKeys.CHANNEL_NUMBER));
        if (wifiComplexInfo.getCurChannel() > 100) {
            notifyValueChangeForKeyPath(WiFiFrequencyBand.FREQUENCY_BAND_5_GHZ, convertKeyToPath("FrequencyBand"));
        } else {
            notifyValueChangeForKeyPath(WiFiFrequencyBand.FREQUENCY_BAND_2_DOT_4_GHZ, convertKeyToPath("FrequencyBand"));
        }
        int[] c24GArray = wifiComplexInfo.get2dot4GChannleList();
        notifyValueChangeForKeyPath(c24GArray, convertKeyToPath(WifiLinkKeys.CHANNEL_LIST_24G));
        int[] c5GArray = wifiComplexInfo.get5GChannelList();
        notifyValueChangeForKeyPath(c5GArray, convertKeyToPath(WifiLinkKeys.CHANNEL_LIST_5G));
        notifyValueChangeForKeyPath(concat(c24GArray, c5GArray), convertKeyToPath(WifiLinkKeys.AVAILABLE_CHANNEL_NUMBERS));
        notifyValueChangeForKeyPath(wifiComplexInfo.getChannelMode() == 0 ? WiFiSelectionMode.AUTO : WiFiSelectionMode.CUSTOM, convertKeyToPath(WifiLinkKeys.SELECTION_MODE));
    }

    @Setter(WifiLinkKeys.CHN_INTERFERENCE_SWITCH)
    public void setChnIntfSwitch(Boolean open, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (open == null) {
            callback.onFails(DJIError.COMMON_PARAM_ILLEGAL);
        }
        new DataWifiSetSweepFrequency().setIsOpen(open.booleanValue()).start(CallbackUtils.getSetterDJIDataCallback(callback));
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataWifiGetPushSignal params) {
    }

    @Setter(WifiLinkKeys.GROUND_WIFI_POWER)
    public void setGroundPower(Integer power, DJISDKCacheHWAbstraction.InnerCallback callback) {
        new DataWifiSetPower().setSendToSky(false).setPower(power.intValue()).start(CallbackUtils.getSetterDJIDataCallback(callback));
    }

    @Getter(WifiLinkKeys.GROUND_WIFI_POWER)
    public void getGroundPower(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        new DataWifiGetPower().setSendToSky(false).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.airlink.wifi.DJIWifiLinkWM160Abstraction.AnonymousClass1 */

            public void onSuccess(Object model) {
                callback.onSuccess(Integer.valueOf(((DataWifiGetPower) model).getPower()));
            }

            public void onFailure(Ccode ccode) {
                callback.onFails(DJIAirLinkError.getDJIError(ccode));
            }
        });
    }

    @Setter(WifiLinkKeys.SKY_WIFI_POWER)
    public void setSkyPower(Integer power, DJISDKCacheHWAbstraction.InnerCallback callback) {
        new DataWifiSetPower().setSendToSky(true).setPower(power.intValue()).start(CallbackUtils.getSetterDJIDataCallback(callback));
    }

    @Getter(WifiLinkKeys.SKY_WIFI_POWER)
    public void getSkyPower(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        new DataWifiGetPower().setSendToSky(true).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.airlink.wifi.DJIWifiLinkWM160Abstraction.AnonymousClass2 */

            public void onSuccess(Object model) {
                callback.onSuccess(Integer.valueOf(((DataWifiGetPower) model).getPower()));
            }

            public void onFailure(Ccode ccode) {
                callback.onFails(DJIAirLinkError.getDJIError(ccode));
            }
        });
    }

    public void destroy() {
        if (this.rssiEventHandler != null) {
            DJIEventBusUtil.unRegister(this.rssiEventHandler);
        }
        super.destroy();
    }
}
