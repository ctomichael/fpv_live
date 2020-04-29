package dji.sdksharedlib.hardware.abstractions.airlink.lb;

import android.support.annotation.NonNull;
import dji.common.LightbridgePIPPosition;
import dji.common.LightbridgeSecondaryVideoFormat;
import dji.common.VideoDataChannel;
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
import dji.midware.component.DJIComponentManager;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.model.P3.DataDm368GetGParams;
import dji.midware.data.model.P3.DataDm368GetPushStatus;
import dji.midware.data.model.P3.DataDm368SetGParams;
import dji.midware.data.model.P3.DataDm368SetParams;
import dji.midware.data.model.P3.DataDm385GetParams;
import dji.midware.data.model.P3.DataDm385SetParams;
import dji.midware.data.model.P3.DataOsdGetPushConfig;
import dji.midware.data.model.P3.DataOsdSetConfig;
import dji.midware.interfaces.DJIDataCallBack;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Getter;
import dji.sdksharedlib.hardware.abstractions.Setter;
import dji.sdksharedlib.keycatalog.airlink.LightbridgeLinkKeys;
import dji.sdksharedlib.store.DJISDKCacheStoreLayer;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class Lightbridge2Abstraction extends LightbridgeAbstraction {
    private static final int EXT_VIDEO_SOURCE = 0;
    private static final int LB_VIDEO_SOURCE = 1;
    private static final String TAG = "DJISDKCacheLightBridge2AirLinkAbstraction";
    private final int INVALID_PERCENT = -1;
    /* access modifiers changed from: private */
    public LB2AbstractionHelper lb2Helper;
    VideoDataChannel videoChannel;

    /* access modifiers changed from: protected */
    public synchronized void setRange() {
        Object result = CacheHelper.getLightbridgeLink(LightbridgeLinkKeys.CHANNEL_RANGE);
        if (result instanceof Integer[]) {
            this.currentRange = (Integer[]) result;
        }
    }

    public void syncPushDataFromMidware() {
        super.syncPushDataFromMidware();
        if (isUsingLightBridge2(DJIComponentManager.getInstance().getPlatformType())) {
            this.currentFrequencyBand = LightbridgeFrequencyBand.FREQUENCY_BAND_2_DOT_4_GHZ;
            notifyValueChangeForKeyPath(LightbridgeFrequencyBand.FREQUENCY_BAND_2_DOT_4_GHZ, convertKeyToPath("FrequencyBand"));
            this.currentRange = this.DEFAULT_RANGE;
            notifyValueChangeForKeyPath(this.currentRange, convertKeyToPath(LightbridgeLinkKeys.CHANNEL_RANGE));
            return;
        }
        onEvent3BackgroundThread(DataOsdGetPushConfig.getInstance());
    }

    /* access modifiers changed from: protected */
    public void initializeComponentCharacteristics() {
        super.initializeComponentCharacteristics();
    }

    public void init(String component, int index, String subComponent, int subComponentIndex, DJISDKCacheStoreLayer storeLayer, DJISDKCacheHWAbstraction.OnValueChangeListener onValueChangeListener) {
        super.init(component, index, subComponent, subComponentIndex, storeLayer, onValueChangeListener);
        this.lb2Helper = new LB2AbstractionHelper(this);
        this.lb2Helper.setup();
    }

    public void destroy() {
        super.destroy();
        this.lb2Helper.destroy();
        CacheHelper.removeListener(this);
    }

    public void setChannelSelectionMode(ChannelSelectionMode mode, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (mode == null || mode.equals(ChannelSelectionMode.UNKNOWN)) {
            callback.onFails(DJIAirLinkError.IMAGE_TRANSMITTER_INVALID_PARAMETER);
        }
        DataOsdSetConfig.getInstance().setAutoChannel(mode.equals(ChannelSelectionMode.AUTO)).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.airlink.lb.Lightbridge2Abstraction.AnonymousClass1 */

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
    }

    public void getChannelSelectionMode(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            DataOsdGetPushConfig.getInstance().start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.airlink.lb.Lightbridge2Abstraction.AnonymousClass2 */

                public void onSuccess(Object model) {
                    callback.onSuccess(DataOsdGetPushConfig.getInstance().getIsAuto() ? ChannelSelectionMode.AUTO : ChannelSelectionMode.MANUAL);
                }

                public void onFailure(Ccode ccode) {
                    callback.onFails(DJIAirLinkError.getDJIError(ccode));
                }
            });
        }
    }

    public void setChannel(int channelNumber, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (this.mode != null && this.mode == ChannelSelectionMode.AUTO) {
            CallbackUtils.onFailure(callback, DJIAirLinkError.COMMON_EXECUTION_FAILED);
        } else if (this.currentFrequencyBand == null || this.currentRange == null) {
            CallbackUtils.onFailure(callback, DJIAirLinkError.COMMON_EXECUTION_FAILED);
        } else if (channelNumber < this.currentRange[0].intValue() || channelNumber > this.currentRange[this.currentRange.length - 1].intValue()) {
            CallbackUtils.onFailure(callback, DJIAirLinkError.COMMON_PARAM_ILLEGAL);
        } else {
            DataOsdSetConfig.getInstance().setChannel(channelNumber - 1).start(CallbackUtils.defaultCB(callback, DJIAirLinkError.class));
        }
    }

    public void getChannel(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            DataOsdGetPushConfig.getInstance().start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.airlink.lb.Lightbridge2Abstraction.AnonymousClass3 */

                public void onSuccess(Object model) {
                    callback.onSuccess(Integer.valueOf(DataOsdGetPushConfig.getInstance().getChannel() + 1));
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
        DataOsdGetPushConfig.getInstance().start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.airlink.lb.Lightbridge2Abstraction.AnonymousClass4 */

            public void onSuccess(Object model) {
                if (callback != null) {
                    callback.onSuccess(LightbridgeDataRate.find(DataOsdGetPushConfig.getInstance().getMcs()));
                }
            }

            public void onFailure(Ccode ccode) {
                if (callback != null) {
                    callback.onFails(DJIAirLinkError.getDJIError(ccode));
                }
            }
        });
    }

    public void setTransmissionMode(LightbridgeTransmissionMode mode, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (mode != null && !mode.equals(LightbridgeTransmissionMode.UNKNOWN)) {
            DataDm385SetParams setter = new DataDm385SetParams();
            setter.set(DataDm385SetParams.DM385CmdId.SetTransmissionMode, mode.value());
            setter.start(CallbackUtils.defaultCB(callback, DJIAirLinkError.class));
        } else if (callback != null) {
            callback.onFails(DJIError.COMMON_PARAM_ILLEGAL);
        }
    }

    public void getTransmissionMode(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataDm385GetParams.getInstance().start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.airlink.lb.Lightbridge2Abstraction.AnonymousClass5 */

            public void onSuccess(Object model) {
                if (callback != null) {
                    callback.onSuccess(LightbridgeTransmissionMode.find(DataDm385GetParams.getInstance().getTransmissionMode()));
                }
            }

            public void onFailure(Ccode ccode) {
                if (callback != null) {
                    callback.onFails(DJIAirLinkError.getDJIError(ccode));
                }
            }
        });
    }

    public void setSecondaryVideoOutputEnabled(boolean enable, DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataDm368SetGParams setter = new DataDm368SetGParams();
        setter.set(DataDm368SetGParams.CmdId.SetOutputEnable, enable ? 1 : 0);
        setter.start(CallbackUtils.defaultCB(callback, DJIAirLinkError.class));
    }

    public void getSecondaryVideoOutputEnabled(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataDm368GetGParams getter = DataDm368GetGParams.getInstance();
        getter.setType(true).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.airlink.lb.Lightbridge2Abstraction.AnonymousClass6 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, Boolean.valueOf(getter.getOutputEnable()));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    public void setSecondaryVideoOutputPort(LightbridgeSecondaryVideoOutputPort port, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (port != null && !port.equals(LightbridgeSecondaryVideoOutputPort.Unknown)) {
            setter(DataDm368SetGParams.CmdId.SetOutputDevice, port.value(), callback);
        } else if (callback != null) {
            callback.onFails(DJIError.COMMON_PARAM_ILLEGAL);
        }
    }

    public void getSecondaryVideoOutputPort(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            DataDm368GetGParams.getInstance().setType(true).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.airlink.lb.Lightbridge2Abstraction.AnonymousClass7 */

                public void onSuccess(Object model) {
                    callback.onSuccess(LightbridgeSecondaryVideoOutputPort.find(DataDm368GetGParams.getInstance().getOutputDevice()));
                }

                public void onFailure(Ccode ccode) {
                    callback.onFails(DJIAirLinkError.getDJIError(ccode));
                }
            });
        }
    }

    public void setSecondaryVideoDisplayMode(LightbridgeSecondaryVideoDisplayMode outputMode, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (outputMode != null && !outputMode.equals(LightbridgeSecondaryVideoDisplayMode.UNKNOWN)) {
            setter(DataDm368SetGParams.CmdId.SetOutputMode, outputMode.value(), callback);
        } else if (callback != null) {
            callback.onFails(DJIError.COMMON_PARAM_ILLEGAL);
        }
    }

    public void getSecondaryVideoDisplayMode(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            DataDm368GetGParams.getInstance().setType(true).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.airlink.lb.Lightbridge2Abstraction.AnonymousClass8 */

                public void onSuccess(Object model) {
                    callback.onSuccess(LightbridgeSecondaryVideoDisplayMode.find(DataDm368GetGParams.getInstance().getOutputMode()));
                }

                public void onFailure(Ccode ccode) {
                    callback.onFails(DJIAirLinkError.getDJIError(ccode));
                }
            });
        }
    }

    public void setFPVVideoBandwidthPercent(float percent, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        int videoSource;
        if (percent >= 0.0f && percent <= 1.0f) {
            final int tenths = Math.round(10.0f * percent);
            switch (tenths) {
                case 0:
                    videoSource = 0;
                    break;
                case 10:
                    videoSource = 1;
                    break;
                default:
                    videoSource = 2;
                    break;
            }
            DataOsdSetConfig.getInstance().setVideoSource(videoSource).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.airlink.lb.Lightbridge2Abstraction.AnonymousClass9 */

                public void onSuccess(Object model) {
                    Lightbridge2Abstraction.this.setSingleModeBandwidthTenths(tenths, callback);
                }

                public void onFailure(Ccode ccode) {
                    CallbackUtils.onFailure(callback, DJIAirLinkError.getDJIError(ccode));
                }
            });
        } else if (callback != null) {
            callback.onFails(DJIAirLinkError.COMMON_PARAM_ILLEGAL);
        }
    }

    public void setDisplayOSDEnabled(boolean isDisplay, DJISDKCacheHWAbstraction.InnerCallback callback) {
        setter(DataDm368SetGParams.CmdId.ShowOsd, isDisplay ? 1 : 0, callback);
    }

    public void getDisplayOSDEnabled(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            DataDm368GetGParams.getInstance().setType(true).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.airlink.lb.Lightbridge2Abstraction.AnonymousClass10 */

                public void onSuccess(Object model) {
                    callback.onSuccess(Boolean.valueOf(DataDm368GetGParams.getInstance().getIsShowOsd()));
                }

                public void onFailure(Ccode ccode) {
                    callback.onFails(DJIAirLinkError.getDJIError(ccode));
                }
            });
        }
    }

    public void setOSDTopMargin(int margin, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (margin >= 0 && margin <= 50) {
            setter(DataDm368SetGParams.CmdId.SetOsdTop, margin, callback);
        } else if (callback != null) {
            callback.onFails(DJIError.COMMON_PARAM_ILLEGAL);
        }
    }

    public void getOSDTopMargin(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            DataDm368GetGParams.getInstance().setType(true).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.airlink.lb.Lightbridge2Abstraction.AnonymousClass11 */

                public void onSuccess(Object model) {
                    callback.onSuccess(Integer.valueOf(DataDm368GetGParams.getInstance().getOsdMarginTop()));
                }

                public void onFailure(Ccode ccode) {
                    callback.onFails(DJIAirLinkError.getDJIError(ccode));
                }
            });
        }
    }

    public void setOSDLeftMargin(int margin, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (margin >= 0 && margin <= 50) {
            setter(DataDm368SetGParams.CmdId.SetOsdLeft, margin, callback);
        } else if (callback != null) {
            callback.onSuccess(DJIError.COMMON_PARAM_ILLEGAL);
        }
    }

    public void getOSDLeftMargin(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            DataDm368GetGParams.getInstance().setType(true).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.airlink.lb.Lightbridge2Abstraction.AnonymousClass12 */

                public void onSuccess(Object model) {
                    callback.onSuccess(Integer.valueOf(DataDm368GetGParams.getInstance().getOsdMarginLeft()));
                }

                public void onFailure(Ccode ccode) {
                    callback.onFails(DJIAirLinkError.getDJIError(ccode));
                }
            });
        }
    }

    public void setOSDBottomMargin(int margin, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (margin >= 0 && margin <= 50) {
            setter(DataDm368SetGParams.CmdId.SetOsdBottom, margin, callback);
        } else if (callback != null) {
            callback.onFails(DJIError.COMMON_PARAM_ILLEGAL);
        }
    }

    public void getOSDBottomMargin(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            DataDm368GetGParams.getInstance().setType(true).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.airlink.lb.Lightbridge2Abstraction.AnonymousClass13 */

                public void onSuccess(Object model) {
                    callback.onSuccess(Integer.valueOf(DataDm368GetGParams.getInstance().getOsdMarginBottom()));
                }

                public void onFailure(Ccode ccode) {
                    callback.onFails(DJIAirLinkError.getDJIError(ccode));
                }
            });
        }
    }

    public void setOSDRightMargin(int margin, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (margin >= 0 && margin <= 50) {
            setter(DataDm368SetGParams.CmdId.SetOsdRight, margin, callback);
        } else if (callback != null) {
            callback.onFails(DJIError.COMMON_PARAM_ILLEGAL);
        }
    }

    public void getOSDRightMargin(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            DataDm368GetGParams.getInstance().setType(true).setType(true).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.airlink.lb.Lightbridge2Abstraction.AnonymousClass14 */

                public void onSuccess(Object model) {
                    callback.onSuccess(Integer.valueOf(DataDm368GetGParams.getInstance().getOsdMarginRight()));
                }

                public void onFailure(Ccode ccode) {
                    callback.onFails(DJIAirLinkError.getDJIError(ccode));
                }
            });
        }
    }

    public void setOSDUnit(LightbridgeUnit unit, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (unit != null && !unit.equals(LightbridgeUnit.UNKNOWN)) {
            setter(DataDm368SetGParams.CmdId.ShowUnit, unit.value(), callback);
        } else if (callback != null) {
            callback.onFails(DJIError.COMMON_PARAM_ILLEGAL);
        }
    }

    public void getOSDUnit(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            DataDm368GetGParams.getInstance().setType(true).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.airlink.lb.Lightbridge2Abstraction.AnonymousClass15 */

                public void onSuccess(Object model) {
                    callback.onSuccess(DataDm368GetGParams.getInstance().getUnit() ? LightbridgeUnit.METRIC : LightbridgeUnit.IMPERIAL);
                }

                public void onFailure(Ccode ccode) {
                    callback.onFails(DJIAirLinkError.getDJIError(ccode));
                }
            });
        }
    }

    public void setHDMIOutputFormat(LightbridgeSecondaryVideoFormat outputFormat, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (outputFormat != null && !outputFormat.equals(LightbridgeSecondaryVideoFormat.UNKNOWN)) {
            setter(DataDm368SetGParams.CmdId.SetHDMIFormat, outputFormat.value(), callback);
        } else if (callback != null) {
            callback.onFails(DJIError.COMMON_PARAM_ILLEGAL);
        }
    }

    public void getHDMIOutputFormat(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            DataDm368GetGParams.getInstance().setType(true).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.airlink.lb.Lightbridge2Abstraction.AnonymousClass16 */

                public void onSuccess(Object model) {
                    callback.onSuccess(LightbridgeSecondaryVideoFormat.find(DataDm368GetGParams.getInstance().getHDMIFormat()));
                }

                public void onFailure(Ccode ccode) {
                    callback.onFails(DJIAirLinkError.getDJIError(ccode));
                }
            });
        }
    }

    public void setSDIOutputFormat(LightbridgeSecondaryVideoFormat outputFormat, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (outputFormat != null && !outputFormat.equals(LightbridgeSecondaryVideoFormat.UNKNOWN)) {
            setter(DataDm368SetGParams.CmdId.SetSDIFormat, outputFormat.value(), callback);
        } else if (callback != null) {
            callback.onFails(DJIError.COMMON_PARAM_ILLEGAL);
        }
    }

    public void getSDIOutputFormat(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            DataDm368GetGParams.getInstance().setType(true).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.airlink.lb.Lightbridge2Abstraction.AnonymousClass17 */

                public void onSuccess(Object model) {
                    callback.onSuccess(LightbridgeSecondaryVideoFormat.find(DataDm368GetGParams.getInstance().getSDIFormat()));
                }

                public void onFailure(Ccode ccode) {
                    callback.onFails(DJIAirLinkError.getDJIError(ccode));
                }
            });
        }
    }

    public void setPIPPosition(LightbridgePIPPosition position, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (position != null && !position.equals(LightbridgePIPPosition.UNKNOWN)) {
            setter(DataDm368SetGParams.CmdId.SetOutputLoc, position.value(), callback);
        } else if (callback != null) {
            callback.onFails(DJIError.COMMON_PARAM_ILLEGAL);
        }
    }

    public void getPIPPosition(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            DataDm368GetGParams.getInstance().start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.airlink.lb.Lightbridge2Abstraction.AnonymousClass18 */

                public void onSuccess(Object model) {
                    callback.onSuccess(LightbridgePIPPosition.find(DataDm368GetGParams.getInstance().getOutputLocation()));
                }

                public void onFailure(Ccode ccode) {
                    callback.onFails(DJIAirLinkError.getDJIError(ccode));
                }
            });
        }
    }

    private boolean isExtPortEnabled(@NonNull DataDm368GetPushStatus pushStatus) {
        return pushStatus.getEncodeMode() == 0;
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataDm368GetPushStatus pushStatus) {
        notifyValueChangeForKeyPath(Boolean.valueOf(isExtPortEnabled(pushStatus)), convertKeyToPath(LightbridgeLinkKeys.IS_EXT_VIDEO_INPUT_PORT_ENABLED));
    }

    public void setEXTVideoInputPortEnabled(final boolean enabled, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!isDualEncodeModeSupported()) {
            CallbackUtils.onFailure(callback, DJIAirLinkError.COMMAND_NOT_SUPPORTED_BY_FIRMWARE);
            return;
        }
        DataDm368SetParams setter = new DataDm368SetParams();
        setter.set(DataDm368SetParams.DM368CmdId.EncodeMode, enabled ? 0 : 1);
        setter.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.airlink.lb.Lightbridge2Abstraction.AnonymousClass19 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, (Object) null);
                Lightbridge2Abstraction.this.lb2Helper.onEXTVideoInputPortEnabledChange(enabled);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIAirLinkError.getDJIError(ccode));
            }
        });
    }

    public void getEXTVideoInputPortEnabled(DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!isDualEncodeModeSupported()) {
            CallbackUtils.onFailure(callback, DJIAirLinkError.COMMAND_NOT_SUPPORTED_BY_FIRMWARE);
        } else {
            CallbackUtils.onSuccess(callback, Boolean.valueOf(isExtPortEnabled(DataDm368GetPushStatus.getInstance())));
        }
    }

    public void setBandwidthAllocationForHDMIVideoInputPort(final float percent, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!isDualEncodeModeSupported()) {
            CallbackUtils.onFailure(callback, DJIAirLinkError.COMMAND_NOT_SUPPORTED_BY_FIRMWARE);
        } else if (((double) percent) < 0.0d || ((double) percent) > 1.0d) {
            CallbackUtils.onFailure(callback, DJIAirLinkError.IMAGE_TRANSMITTER_INVALID_PARAMETER);
        } else {
            DataDm368SetParams setter = new DataDm368SetParams();
            setter.set(DataDm368SetParams.DM368CmdId.BandwidthPercentage, (int) (percent * 10.0f));
            setter.start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.airlink.lb.Lightbridge2Abstraction.AnonymousClass20 */

                public void onSuccess(Object model) {
                    CallbackUtils.onSuccess(callback, (Object) null);
                    Lightbridge2Abstraction.this.lb2Helper.onDualPercentChange(Math.round(percent * 10.0f));
                }

                public void onFailure(Ccode ccode) {
                    CallbackUtils.onFailure(callback, DJIAirLinkError.getDJIError(ccode));
                }
            });
        }
    }

    public void getBandwidthAllocationForHDMIVideoInputPort(DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!isDualEncodeModeSupported()) {
            CallbackUtils.onFailure(callback, DJIAirLinkError.COMMAND_NOT_SUPPORTED_BY_FIRMWARE);
        } else {
            CallbackUtils.onSuccess(callback, Float.valueOf(((float) DataDm368GetPushStatus.getInstance().getDualEncodeModePercentage()) / 10.0f));
        }
    }

    public boolean isDualEncodeModeSupported() {
        return DataDm368GetPushStatus.getInstance().isDualEncodeModeSupported();
    }

    public boolean isSecondaryVideoOutputSupported() {
        return true;
    }

    public void setWorkingFrequency(WorkingFrequency freq, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            callback.onFails(DJIError.COMMON_UNSUPPORTED);
        }
    }

    public void getWorkingFrequency(DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            callback.onFails(DJIError.COMMON_UNSUPPORTED);
        }
    }

    public void setVideoChannel(VideoDataChannel videoChannel2) {
    }

    public void setBandwidthAllocationForMainCamera(float percent, DJISDKCacheHWAbstraction.InnerCallback callback) {
        CallbackUtils.onFailure(callback, DJIAirLinkError.COMMON_UNSUPPORTED);
    }

    public void setBandwidthAllocationForLeftCamera(float percent, DJISDKCacheHWAbstraction.InnerCallback callback) {
        CallbackUtils.onFailure(callback, DJIAirLinkError.COMMON_UNSUPPORTED);
    }

    /* access modifiers changed from: protected */
    public void setSingleModeBandwidthTenths(final int tenths, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataOsdSetConfig.getInstance().setBandWidth(tenths).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.airlink.lb.Lightbridge2Abstraction.AnonymousClass21 */

            public void onSuccess(Object model) {
                if (callback != null) {
                    callback.onSuccess(null);
                }
                Lightbridge2Abstraction.this.lb2Helper.onLBPercentChange(tenths);
            }

            public void onFailure(Ccode ccode) {
                if (callback != null) {
                    callback.onFails(DJIAirLinkError.getDJIError(ccode));
                }
            }
        });
    }

    @Setter("FrequencyBand")
    public void setFrequencyBand(LightbridgeFrequencyBand frequencyBand, DJISDKCacheHWAbstraction.InnerCallback callback) {
        CallbackUtils.onFailure(callback, DJIAirLinkError.COMMON_UNSUPPORTED);
    }

    @Getter("SupportedFrequencyBands")
    public void getSupportedFrequencyBands(DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (isUsingLightBridge2(DJIComponentManager.getInstance().getPlatformType())) {
            CallbackUtils.onSuccess(callback, new LightbridgeFrequencyBand[]{LightbridgeFrequencyBand.FREQUENCY_BAND_2_DOT_4_GHZ});
            return;
        }
        CallbackUtils.onFailure(callback, DJIAirLinkError.COMMON_UNSUPPORTED);
    }

    @Getter(LightbridgeLinkKeys.CHANNEL_RANGE)
    public void getChannelRange(DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (isUsingLightBridge2(DJIComponentManager.getInstance().getPlatformType())) {
            CallbackUtils.onSuccess(callback, this.DEFAULT_RANGE);
        } else {
            CallbackUtils.onFailure(callback, DJIAirLinkError.COMMON_UNSUPPORTED);
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataOsdGetPushConfig params) {
        if (params.isGetted()) {
            LightbridgeFrequencyBand frequencyBand = LightbridgeFrequencyBand.find(params.getWorkingFreq());
            this.currentFrequencyBand = frequencyBand;
            notifyValueChangeForKeyPath(frequencyBand, convertKeyToPath("FrequencyBand"));
        }
    }
}
