package dji.sdksharedlib.hardware.abstractions.airlink.lb;

import dji.common.LightbridgePIPPosition;
import dji.common.LightbridgeSecondaryVideoFormat;
import dji.common.airlink.ChannelSelectionMode;
import dji.common.airlink.LightbridgeDataRate;
import dji.common.airlink.LightbridgeFrequencyBand;
import dji.common.airlink.LightbridgeSecondaryVideoDisplayMode;
import dji.common.airlink.LightbridgeSecondaryVideoOutputPort;
import dji.common.airlink.LightbridgeTransmissionMode;
import dji.common.airlink.LightbridgeUnit;
import dji.common.error.DJIAirLinkError;
import dji.common.error.DJIError;
import dji.common.util.CallbackUtils;
import dji.fieldAnnotation.EXClassNullAway;
import dji.internal.airlink.WorkingFrequency;
import dji.log.DJILog;
import dji.midware.component.DJIComponentManager;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.model.P3.DataDm368GetGParams;
import dji.midware.data.model.P3.DataDm368SetGParams;
import dji.midware.data.model.P3.DataDm368_gGetPushCheckStatus;
import dji.midware.data.model.P3.DataOsdGetPushCommon;
import dji.midware.data.model.P3.DataOsdGetPushConfig;
import dji.midware.data.model.P3.DataOsdSetConfig;
import dji.midware.interfaces.DJIDataCallBack;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Getter;
import dji.sdksharedlib.hardware.abstractions.Setter;
import dji.sdksharedlib.keycatalog.airlink.LightbridgeLinkKeys;
import dji.sdksharedlib.store.DJISDKCacheStoreLayer;

@EXClassNullAway
public class Lightbridge1Abstraction extends LightbridgeAbstraction {
    private static final int CHANNEL_OFFSET_2Dot4G = 12;
    private static final String TAG = "DJISDKCacheLightBridge1AirLinkAbstraction";

    public void init(String component, int index, String subComponent, int subComponentIndex, DJISDKCacheStoreLayer storeLayer, DJISDKCacheHWAbstraction.OnValueChangeListener onValueChangeListener) {
        super.init(component, index, subComponent, subComponentIndex, storeLayer, onValueChangeListener);
    }

    public void syncPushDataFromMidware() {
        super.syncPushDataFromMidware();
        this.currentFrequencyBand = LightbridgeFrequencyBand.FREQUENCY_BAND_2_DOT_4_GHZ;
        notifyValueChangeForKeyPath(LightbridgeFrequencyBand.FREQUENCY_BAND_2_DOT_4_GHZ, convertKeyToPath("FrequencyBand"));
    }

    public void setChannelSelectionMode(ChannelSelectionMode mode, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (mode != null && !mode.equals(ChannelSelectionMode.UNKNOWN)) {
            DataOsdSetConfig.getInstance().setAutoChannel(mode.equals(ChannelSelectionMode.AUTO)).start(CallbackUtils.defaultCB(callback, DJIAirLinkError.class));
        } else if (callback != null) {
            callback.onFails(DJIAirLinkError.COMMON_PARAM_ILLEGAL);
        }
    }

    public void getChannelSelectionMode(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        DJILog.d("ProgressTAG", "start getChannel", new Object[0]);
        if (callback != null) {
            DJILog.d("ProgressTAG", "Not null", new Object[0]);
            DataOsdGetPushConfig.getInstance().start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.airlink.lb.Lightbridge1Abstraction.AnonymousClass1 */

                public void onSuccess(Object model) {
                    DJILog.d("ProgressTAG", "I Get", new Object[0]);
                    callback.onSuccess(DataOsdGetPushConfig.getInstance().getIsAuto() ? ChannelSelectionMode.AUTO : ChannelSelectionMode.MANUAL);
                }

                public void onFailure(Ccode ccode) {
                    DJILog.d("ProgressTAG", "Error ccode ：" + ccode.name(), new Object[0]);
                    callback.onFails(DJIAirLinkError.getDJIError(ccode));
                }
            });
        }
    }

    public void setChannel(int channelNumber, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (channelNumber < 13 || channelNumber > 20) {
            CallbackUtils.onFailure(callback, DJIAirLinkError.COMMON_PARAM_ILLEGAL);
        } else if (this.mode == null || this.mode != ChannelSelectionMode.AUTO) {
            DataOsdSetConfig.getInstance().setChannel(channelNumber - 1).start(CallbackUtils.defaultCB(callback, DJIAirLinkError.class));
        } else {
            callback.onFails(DJIAirLinkError.COMMON_PARAM_INVALID);
        }
    }

    public void getChannel(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            DataOsdGetPushConfig.getInstance().start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.airlink.lb.Lightbridge1Abstraction.AnonymousClass2 */

                public void onSuccess(Object model) {
                    CallbackUtils.onSuccess(callback, Integer.valueOf(DataOsdGetPushConfig.getInstance().getChannel() + 1));
                }

                public void onFailure(Ccode ccode) {
                    callback.onFails(DJIAirLinkError.getDJIError(ccode));
                }
            });
        }
    }

    public void setDataRate(LightbridgeDataRate dataRate, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (dataRate != null && !dataRate.equals(LightbridgeDataRate.UNKNOWN)) {
            DataOsdSetConfig.getInstance().setMcs(dataRate.value()).start(CallbackUtils.defaultCB(callback, DJIAirLinkError.class));
        } else if (callback != null) {
            callback.onFails(DJIAirLinkError.COMMON_PARAM_ILLEGAL);
        }
    }

    public void getDataRate(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            DataOsdGetPushConfig.getInstance().start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.airlink.lb.Lightbridge1Abstraction.AnonymousClass3 */

                public void onSuccess(Object model) {
                    int value = DataOsdGetPushConfig.getInstance().getMcs();
                    if (value >= LightbridgeDataRate.BANDWIDTH_4_MBPS.value() && value <= LightbridgeDataRate.BANDWIDTH_10_MBPS.value()) {
                        callback.onSuccess(LightbridgeDataRate.find(value));
                    } else if (value > LightbridgeDataRate.BANDWIDTH_10_MBPS.value()) {
                        callback.onSuccess(LightbridgeDataRate.BANDWIDTH_10_MBPS);
                    } else {
                        callback.onSuccess(LightbridgeDataRate.BANDWIDTH_4_MBPS);
                    }
                }

                public void onFailure(Ccode ccode) {
                    if (callback != null) {
                        callback.onFails(DJIAirLinkError.getDJIError(ccode));
                    }
                }
            });
        }
    }

    public void setTransmissionMode(LightbridgeTransmissionMode mode, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            callback.onFails(DJIError.COMMON_UNSUPPORTED);
        }
    }

    public void getTransmissionMode(DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            callback.onFails(DJIError.COMMON_UNSUPPORTED);
        }
    }

    public void setSecondaryVideoOutputEnabled(boolean enable, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (DataOsdGetPushCommon.getInstance().getDroneType() != DataOsdGetPushCommon.DroneType.Inspire && DataOsdGetPushCommon.getInstance().getDroneType() != DataOsdGetPushCommon.DroneType.OpenFrame) {
            callback.onFails(DJIError.COMMON_UNSUPPORTED);
        } else if (isSecondaryVideoOutputSupported() || !DataDm368_gGetPushCheckStatus.getInstance().isGetted() || DataDm368_gGetPushCheckStatus.getInstance().getHDMIExist()) {
            DataDm368SetGParams setter = new DataDm368SetGParams();
            setter.set(DataDm368SetGParams.CmdId.ShowDouble, enable ? 1 : 0);
            setter.start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.airlink.lb.Lightbridge1Abstraction.AnonymousClass4 */

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
        } else {
            callback.onFails(DJIError.COMMON_UNSUPPORTED);
        }
    }

    public void getSecondaryVideoOutputEnabled(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (DataOsdGetPushCommon.getInstance().getDroneType() != DataOsdGetPushCommon.DroneType.Inspire && DataOsdGetPushCommon.getInstance().getDroneType() != DataOsdGetPushCommon.DroneType.OpenFrame) {
            callback.onFails(DJIError.COMMON_UNSUPPORTED);
        } else if (isSecondaryVideoOutputSupported() || !DataDm368_gGetPushCheckStatus.getInstance().isGetted() || DataDm368_gGetPushCheckStatus.getInstance().getHDMIExist()) {
            final DataDm368GetGParams getter = DataDm368GetGParams.getInstance();
            getter.setType(false).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.airlink.lb.Lightbridge1Abstraction.AnonymousClass5 */

                public void onSuccess(Object model) {
                    if (callback != null) {
                        callback.onSuccess(Boolean.valueOf(getter.getIsDouble()));
                    }
                }

                public void onFailure(Ccode ccode) {
                    if (callback != null) {
                        callback.onFails(DJIAirLinkError.getDJIError(ccode));
                    }
                }
            });
        } else {
            callback.onFails(DJIError.COMMON_UNSUPPORTED);
        }
    }

    public void setSecondaryVideoOutputPort(LightbridgeSecondaryVideoOutputPort port, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            callback.onFails(DJIError.COMMON_UNSUPPORTED);
        }
    }

    public void getSecondaryVideoOutputPort(DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            callback.onFails(DJIError.COMMON_UNSUPPORTED);
        }
    }

    public void setSecondaryVideoDisplayMode(LightbridgeSecondaryVideoDisplayMode outputMode, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            callback.onFails(DJIError.COMMON_UNSUPPORTED);
        }
    }

    public void getSecondaryVideoDisplayMode(DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            callback.onFails(DJIError.COMMON_UNSUPPORTED);
        }
    }

    public void setFPVVideoBandwidthPercent(float percent, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            callback.onFails(DJIError.COMMON_UNSUPPORTED);
        }
    }

    public void setDisplayOSDEnabled(boolean isDisplay, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (DataOsdGetPushCommon.getInstance().getDroneType() != DataOsdGetPushCommon.DroneType.Inspire && DataOsdGetPushCommon.getInstance().getDroneType() != DataOsdGetPushCommon.DroneType.OpenFrame) {
            callback.onFails(DJIError.COMMON_UNSUPPORTED);
        } else if (isSecondaryVideoOutputSupported() || !DataDm368_gGetPushCheckStatus.getInstance().isGetted() || DataDm368_gGetPushCheckStatus.getInstance().getHDMIExist()) {
            setter(DataDm368SetGParams.CmdId.ShowOsd, isDisplay ? 1 : 0, callback);
        } else {
            callback.onFails(DJIError.COMMON_UNSUPPORTED);
        }
    }

    public void getDisplayOSDEnabled(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (DataOsdGetPushCommon.getInstance().getDroneType() != DataOsdGetPushCommon.DroneType.Inspire && DataOsdGetPushCommon.getInstance().getDroneType() != DataOsdGetPushCommon.DroneType.OpenFrame) {
            callback.onFails(DJIError.COMMON_UNSUPPORTED);
        } else if (isSecondaryVideoOutputSupported() || !DataDm368_gGetPushCheckStatus.getInstance().isGetted() || DataDm368_gGetPushCheckStatus.getInstance().getHDMIExist()) {
            final DataDm368GetGParams getter = DataDm368GetGParams.getInstance();
            getter.setType(false).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.airlink.lb.Lightbridge1Abstraction.AnonymousClass6 */

                public void onSuccess(Object model) {
                    if (callback != null) {
                        callback.onSuccess(Boolean.valueOf(getter.getIsShowOsd()));
                    }
                }

                public void onFailure(Ccode ccode) {
                    if (callback != null) {
                        callback.onFails(DJIAirLinkError.getDJIError(ccode));
                    }
                }
            });
        } else {
            callback.onFails(DJIError.COMMON_UNSUPPORTED);
        }
    }

    public void setOSDTopMargin(int margin, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            callback.onFails(DJIError.COMMON_UNSUPPORTED);
        }
    }

    public void getOSDTopMargin(DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            callback.onFails(DJIError.COMMON_UNSUPPORTED);
        }
    }

    public void setOSDLeftMargin(int margin, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            callback.onFails(DJIError.COMMON_UNSUPPORTED);
        }
    }

    public void getOSDLeftMargin(DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            callback.onFails(DJIError.COMMON_UNSUPPORTED);
        }
    }

    public void setOSDBottomMargin(int margin, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            callback.onFails(DJIError.COMMON_UNSUPPORTED);
        }
    }

    public void getOSDBottomMargin(DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            callback.onFails(DJIError.COMMON_UNSUPPORTED);
        }
    }

    public void setOSDRightMargin(int margin, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            callback.onFails(DJIError.COMMON_UNSUPPORTED);
        }
    }

    public void getOSDRightMargin(DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            callback.onFails(DJIError.COMMON_UNSUPPORTED);
        }
    }

    public void setOSDUnit(LightbridgeUnit unit, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            callback.onFails(DJIError.COMMON_UNSUPPORTED);
        }
    }

    public void getOSDUnit(DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            callback.onFails(DJIError.COMMON_UNSUPPORTED);
        }
    }

    public void setHDMIOutputFormat(LightbridgeSecondaryVideoFormat outputFormat, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            callback.onFails(DJIError.COMMON_UNSUPPORTED);
        }
    }

    public void getHDMIOutputFormat(DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            callback.onFails(DJIError.COMMON_UNSUPPORTED);
        }
    }

    public void setSDIOutputFormat(LightbridgeSecondaryVideoFormat outputFormat, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            callback.onFails(DJIError.COMMON_UNSUPPORTED);
        }
    }

    public void getSDIOutputFormat(DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            callback.onFails(DJIError.COMMON_UNSUPPORTED);
        }
    }

    public void setPIPPosition(LightbridgePIPPosition position, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            callback.onFails(DJIError.COMMON_UNSUPPORTED);
        }
    }

    public void getPIPPosition(DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            callback.onFails(DJIError.COMMON_UNSUPPORTED);
        }
    }

    public void setEXTVideoInputPortEnabled(boolean enabled, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            callback.onFails(DJIError.COMMON_UNSUPPORTED);
        }
    }

    public void getEXTVideoInputPortEnabled(DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            callback.onFails(DJIError.COMMON_UNSUPPORTED);
        }
    }

    public void setBandwidthAllocationForHDMIVideoInputPort(float percent, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            callback.onFails(DJIError.COMMON_UNSUPPORTED);
        }
    }

    public void getBandwidthAllocationForHDMIVideoInputPort(DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            callback.onFails(DJIError.COMMON_UNSUPPORTED);
        }
    }

    public boolean isSecondaryVideoOutputSupported() {
        return DJIComponentManager.getInstance().getPlatformType().equals(DJIComponentManager.PlatformType.Inspire);
    }

    @Setter(LightbridgeLinkKeys.WORKING_FREQUENCY)
    public void setWorkingFrequency(WorkingFrequency freq, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (freq != null) {
            DataOsdSetConfig.getInstance().setWorkingFreq(freq.value()).start(CallbackUtils.defaultCB(callback));
        } else if (callback != null) {
            callback.onFails(DJIError.COMMON_PARAM_ILLEGAL);
        }
    }

    @Getter(LightbridgeLinkKeys.WORKING_FREQUENCY)
    public void getWorkingFrequency(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataOsdGetPushConfig.getInstance().start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.airlink.lb.Lightbridge1Abstraction.AnonymousClass7 */

            public void onSuccess(Object model) {
                int value = DataOsdGetPushConfig.getInstance().getWorkingFreq();
                if (callback != null) {
                    callback.onSuccess(WorkingFrequency.find(value));
                }
            }

            public void onFailure(Ccode ccode) {
                if (callback != null) {
                    callback.onFails(DJIError.getDJIError(ccode));
                }
            }
        });
    }

    @Getter(LightbridgeLinkKeys.CHANNEL_RANGE)
    public void getChannelRange(DJISDKCacheHWAbstraction.InnerCallback callback) {
        CallbackUtils.onSuccess(callback, this.DEFAULT_RANGE);
    }

    @Getter("SupportedFrequencyBands")
    public void getSupportedFrequencyBands(DJISDKCacheHWAbstraction.InnerCallback callback) {
        CallbackUtils.onSuccess(callback, new LightbridgeFrequencyBand[]{LightbridgeFrequencyBand.FREQUENCY_BAND_2_DOT_4_GHZ});
    }

    @Setter("FrequencyBand")
    public void setFrequencyBand(LightbridgeFrequencyBand frequencyBand, DJISDKCacheHWAbstraction.InnerCallback callback) {
        CallbackUtils.onFailure(callback, DJIAirLinkError.COMMON_UNSUPPORTED);
    }

    public void setBandwidthAllocationForMainCamera(float percent, DJISDKCacheHWAbstraction.InnerCallback callback) {
        CallbackUtils.onFailure(callback, DJIAirLinkError.COMMON_UNSUPPORTED);
    }

    public void setBandwidthAllocationForLeftCamera(float percent, DJISDKCacheHWAbstraction.InnerCallback callback) {
        CallbackUtils.onFailure(callback, DJIAirLinkError.COMMON_UNSUPPORTED);
    }
}
