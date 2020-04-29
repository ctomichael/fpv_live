package dji.sdksharedlib.hardware.abstractions.airlink.lb;

import android.text.TextUtils;
import dji.common.airlink.ChannelSelectionMode;
import dji.common.airlink.LightbridgeDataRate;
import dji.common.airlink.LightbridgeFrequencyBand;
import dji.common.error.DJIAirLinkError;
import dji.common.error.DJIError;
import dji.common.util.CallbackUtils;
import dji.component.areacode.IAreaCode;
import dji.fieldAnnotation.EXClassNullAway;
import dji.internal.logics.CommonUtil;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.model.P3.DataOsdGetPushConfig;
import dji.midware.data.model.P3.DataOsdSetConfig;
import dji.midware.data.model.P3.DataRemoteControllerGetParam;
import dji.midware.interfaces.DJIDataCallBack;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Getter;
import dji.sdksharedlib.hardware.abstractions.Setter;
import dji.sdksharedlib.keycatalog.DJISDKCacheKey;
import dji.sdksharedlib.keycatalog.airlink.LightbridgeLinkKeys;
import dji.sdksharedlib.store.DJISDKCacheParamValue;
import dji.sdksharedlib.store.DJISDKCacheStoreLayer;
import java.util.ArrayList;

@EXClassNullAway
public class Lightbridge2Phantom4PAbstraction extends Lightbridge2Abstraction {
    public void init(String component, int index, String subComponent, int subComponentIndex, DJISDKCacheStoreLayer storeLayer, DJISDKCacheHWAbstraction.OnValueChangeListener onValueChangeListener) {
        super.init(component, index, subComponent, subComponentIndex, storeLayer, onValueChangeListener);
        CacheHelper.addLightbridgeLinkListener(this, "FrequencyBand", LightbridgeLinkKeys.CHANNEL_RANGE, "ChannelSelectionMode", "SupportedFrequencyBands");
    }

    public void setDataRate(LightbridgeDataRate dataRate, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (dataRate == null || dataRate.equals(LightbridgeDataRate.UNKNOWN)) {
            CallbackUtils.onFailure(callback, DJIAirLinkError.COMMON_PARAM_ILLEGAL);
            return;
        }
        int setValue = (dataRate.value() - 1) * 2;
        if (setValue >= 7) {
            setValue = 7;
        }
        DataOsdSetConfig.getInstance().setMcs(setValue).start(CallbackUtils.defaultCB(callback, DJIAirLinkError.class));
    }

    public void getDataRate(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataOsdGetPushConfig.getInstance().start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.airlink.lb.Lightbridge2Phantom4PAbstraction.AnonymousClass1 */

            public void onSuccess(Object model) {
                if (callback != null) {
                    int getValue = DataOsdGetPushConfig.getInstance().getMcs();
                    if (getValue >= 7) {
                        getValue = 7;
                    }
                    callback.onSuccess(LightbridgeDataRate.find((getValue / 2) + 1));
                }
            }

            public void onFailure(Ccode ccode) {
                if (callback != null) {
                    callback.onFails(DJIAirLinkError.getDJIError(ccode));
                }
            }
        });
    }

    @Setter("FrequencyBand")
    public void setFrequencyBand(final LightbridgeFrequencyBand frequencyBand, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        boolean validFlag = false;
        if (this.supportBand != null) {
            LightbridgeFrequencyBand[] lightbridgeFrequencyBandArr = this.supportBand;
            int length = lightbridgeFrequencyBandArr.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                } else if (lightbridgeFrequencyBandArr[i].equals(frequencyBand)) {
                    validFlag = true;
                    break;
                } else {
                    i++;
                }
            }
        }
        if (validFlag) {
            DataOsdSetConfig.getInstance().setWorkingFreq(frequencyBand.value()).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.airlink.lb.Lightbridge2Phantom4PAbstraction.AnonymousClass2 */

                public void onSuccess(Object model) {
                    if (callback != null) {
                        Lightbridge2Phantom4PAbstraction.this.currentFrequencyBand = frequencyBand;
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
            callback.onFails(DJIError.COMMON_PARAM_INVALID);
        }
    }

    @Getter(LightbridgeLinkKeys.CHANNEL_RANGE)
    public void getChannelRange(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataRemoteControllerGetParam getter = DataRemoteControllerGetParam.getInstance();
        DataRemoteControllerGetParam.ParamType param = DataRemoteControllerGetParam.ParamType.UNKNOWN;
        if (this.currentFrequencyBand == LightbridgeFrequencyBand.FREQUENCY_BAND_2_DOT_4_GHZ) {
            param = DataRemoteControllerGetParam.ParamType.FREQUENCY_BAND_2POINT4G_RANGE;
        } else if (this.currentFrequencyBand == LightbridgeFrequencyBand.FREQUENCY_BAND_5_DOT_7_GHZ) {
            param = DataRemoteControllerGetParam.ParamType.FREQUENCY_BAND_5POINT7G_RANGE;
        } else if (this.currentFrequencyBand == LightbridgeFrequencyBand.FREQUENCY_BAND_5_DOT_8_GHZ) {
            param = DataRemoteControllerGetParam.ParamType.FREQUENCY_BAND_5POINT8G_RANGE;
        }
        if (param == DataRemoteControllerGetParam.ParamType.UNKNOWN) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_SYSTEM_BUSY);
            return;
        }
        final DataRemoteControllerGetParam.ParamType settingType = param;
        getter.setType(param).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.airlink.lb.Lightbridge2Phantom4PAbstraction.AnonymousClass3 */

            public void onSuccess(Object model) {
                if (getter.getParamType() != settingType) {
                    CallbackUtils.onFailure(callback, DJIAirLinkError.COMMON_EXECUTION_FAILED);
                    return;
                }
                int[] rangeBeginEnd = getter.getChannelRange();
                int length = (rangeBeginEnd[1] - rangeBeginEnd[0]) + 1;
                if (length >= 0) {
                    Integer[] array = new Integer[length];
                    for (int i = 0; i < length; i++) {
                        array[i] = Integer.valueOf(rangeBeginEnd[0] + i + 1);
                    }
                    CallbackUtils.onSuccess(callback, array);
                }
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    public void getSupportedFrequencyBands(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataRemoteControllerGetParam getter = DataRemoteControllerGetParam.getInstance();
        getter.setType(DataRemoteControllerGetParam.ParamType.SUPPORTED_FREQUENCY_BAND).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.airlink.lb.Lightbridge2Phantom4PAbstraction.AnonymousClass4 */

            public void onSuccess(Object model) {
                if (getter.getParamType() != DataRemoteControllerGetParam.ParamType.SUPPORTED_FREQUENCY_BAND) {
                    CallbackUtils.onFailure(callback, DJIAirLinkError.COMMON_EXECUTION_FAILED);
                    return;
                }
                ArrayList<LightbridgeFrequencyBand> bands = new ArrayList<>(3);
                if (getter.is2point4GSupported()) {
                    bands.add(LightbridgeFrequencyBand.FREQUENCY_BAND_2_DOT_4_GHZ);
                }
                String areaCode = CommonUtil.getAreaCodeFromCache();
                if (!TextUtils.isEmpty(areaCode) && !IAreaCode.STR_AREA_CODE_JAPAN.equals(areaCode) && getter.is5point7GSupported()) {
                    bands.add(LightbridgeFrequencyBand.FREQUENCY_BAND_5_DOT_7_GHZ);
                }
                if (getter.is5point8GSupported()) {
                    bands.add(LightbridgeFrequencyBand.FREQUENCY_BAND_5_DOT_8_GHZ);
                }
                CallbackUtils.onSuccess(callback, (LightbridgeFrequencyBand[]) bands.toArray(new LightbridgeFrequencyBand[bands.size()]));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    public void onValueChange(DJISDKCacheKey key, DJISDKCacheParamValue oldValue, DJISDKCacheParamValue newValue) {
        if (newValue != null && newValue.getData() != null) {
            if (key.getParamKey().equals("FrequencyBand")) {
                getChannelRange(new DJISDKCacheHWAbstraction.InnerCallback() {
                    /* class dji.sdksharedlib.hardware.abstractions.airlink.lb.Lightbridge2Phantom4PAbstraction.AnonymousClass5 */

                    public void onSuccess(Object o) {
                        Lightbridge2Phantom4PAbstraction.this.setRange();
                    }

                    public void onFails(DJIError error) {
                    }
                });
            } else if (key.getParamKey().equals(LightbridgeLinkKeys.CHANNEL_RANGE)) {
                setRange();
            } else if (key.getParamKey().equals("ChannelSelectionMode")) {
                this.mode = (ChannelSelectionMode) CacheHelper.getLightbridgeLink("ChannelSelectionMode");
            } else if (key.getParamKey().equals("SupportedFrequencyBands")) {
                this.supportBand = (LightbridgeFrequencyBand[]) CacheHelper.getLightbridgeLink("SupportedFrequencyBands");
            }
        }
    }

    public void setBandwidthAllocationForMainCamera(float percent, DJISDKCacheHWAbstraction.InnerCallback callback) {
        super.setFPVVideoBandwidthPercent(percent, callback);
    }
}
