package dji.sdksharedlib.hardware.abstractions.airlink.wifi;

import dji.common.airlink.WiFiFrequencyBand;
import dji.common.error.DJIAirLinkError;
import dji.common.error.DJIError;
import dji.common.util.AirLinkUtils;
import dji.common.util.CallbackUtils;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.model.P3.DataWifiGetPushSweepFrequency;
import dji.midware.data.model.P3.DataWifiSetModeChannel;
import dji.midware.data.model.P3.DataWifiSetWifiFreq5GMode;
import dji.midware.interfaces.DJIDataCallBack;
import dji.sdksharedlib.DJISDKCache;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.extension.KeyHelper;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Setter;
import dji.sdksharedlib.hardware.abstractions.airlink.wifi.DJIWifiLinkFoldingDroneAbstraction;
import dji.sdksharedlib.keycatalog.airlink.WifiLinkKeys;
import dji.sdksharedlib.listener.DJIGetCallback;
import dji.sdksharedlib.store.DJISDKCacheParamValue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.greenrobot.eventbus.EventBus;

@EXClassNullAway
public class DJIWifiLinkWm100GroundAbstraction extends DJIWifiLinkFoldingDroneAbstraction {
    public void getFrequencyPointRSSI(DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (this.rssiEventHandler == null) {
            this.rssiEventHandler = new DJIWifiLinkFoldingDroneAbstraction.RSSIEventHandler();
            this.rssiEventHandler.onEvent3BackgroundThread(DataWifiGetPushSweepFrequency.getInstance());
        }
        if (!EventBus.getDefault().isRegistered(this.rssiEventHandler)) {
            EventBus.getDefault().register(this.rssiEventHandler);
        }
        if (this.subscription != null) {
            this.subscription.start();
        }
    }

    public void setFrequencyBand(WiFiFrequencyBand frequency, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (frequency != null && !frequency.equals(WiFiFrequencyBand.UNKNOWN)) {
            new DataWifiSetWifiFreq5GMode().setFrequencyMode(frequency.value()).start(CallbackUtils.getSetterDJIDataCallback(new DJISDKCacheHWAbstraction.InnerCallback() {
                /* class dji.sdksharedlib.hardware.abstractions.airlink.wifi.DJIWifiLinkWm100GroundAbstraction.AnonymousClass1 */

                public void onSuccess(Object o) {
                    DJIWifiLinkWm100GroundAbstraction.this.getAvailableChannelNumbers(null);
                    if (callback != null) {
                        callback.onSuccess(null);
                    }
                }

                public void onFails(DJIError error) {
                    DJIWifiLinkWm100GroundAbstraction.this.getAvailableChannelNumbers(null);
                    if (callback != null) {
                        callback.onFails(error);
                    }
                }
            }));
        } else if (callback != null) {
            callback.onFails(DJIAirLinkError.COMMON_PARAM_ILLEGAL);
        }
    }

    public void getFrequencyBand(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        DJISDKCache.getInstance().getValue(KeyHelper.getWiFiAirLinkKey(WifiLinkKeys.CHANNEL_NUMBER), new DJIGetCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.airlink.wifi.DJIWifiLinkWm100GroundAbstraction.AnonymousClass2 */

            public void onSuccess(DJISDKCacheParamValue value) {
                Integer channelNumber = (Integer) CacheHelper.getWiFiAirLink(WifiLinkKeys.CHANNEL_NUMBER);
                if (channelNumber == null) {
                    CallbackUtils.onFailure(callback, DJIAirLinkError.COMMON_EXECUTION_FAILED);
                } else if (channelNumber.intValue() > 100) {
                    CallbackUtils.onSuccess(callback, WiFiFrequencyBand.FREQUENCY_BAND_5_GHZ);
                } else {
                    CallbackUtils.onSuccess(callback, WiFiFrequencyBand.FREQUENCY_BAND_2_DOT_4_GHZ);
                }
            }

            public void onFails(DJIError error) {
                CallbackUtils.onFailure(callback, error);
            }
        });
    }

    @Setter(WifiLinkKeys.CHANNEL_NUMBER)
    public void setChannel(int channelNumber, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        DJISDKCacheParamValue value = DJISDKCache.getInstance().getAvailableValue(KeyHelper.getWiFiAirLinkKey(WifiLinkKeys.AVAILABLE_CHANNEL_NUMBERS));
        List<Integer> validRange = new ArrayList<>();
        if (value != null) {
            Collections.addAll(validRange, (Integer[]) value.getData());
        }
        if (!validRange.isEmpty() && validRange.contains(Integer.valueOf(channelNumber))) {
            WiFiFrequencyBand validBand = AirLinkUtils.getValidFrequencyBandForChannel(channelNumber);
            if (validBand != WiFiFrequencyBand.UNKNOWN) {
                DataWifiSetModeChannel.getInstance().setWifiMode(validBand.value()).setWifiChannel(channelNumber).start(new DJIDataCallBack() {
                    /* class dji.sdksharedlib.hardware.abstractions.airlink.wifi.DJIWifiLinkWm100GroundAbstraction.AnonymousClass3 */

                    public void onSuccess(Object model) {
                        if (callback != null) {
                            callback.onSuccess(null);
                        }
                    }

                    public void onFailure(Ccode ccode) {
                        if (callback != null) {
                            callback.onFails(DJIAirLinkError.getDJIError(ccode));
                        }
                    }
                });
            } else if (callback != null) {
                callback.onFails(DJIAirLinkError.COMMON_PARAM_ILLEGAL);
            }
        } else if (callback != null) {
            callback.onFails(DJIAirLinkError.COMMON_PARAM_ILLEGAL);
        }
    }
}
