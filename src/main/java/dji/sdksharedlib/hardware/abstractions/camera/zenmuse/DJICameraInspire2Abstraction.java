package dji.sdksharedlib.hardware.abstractions.camera.zenmuse;

import dji.common.camera.ColorWaveformSettings;
import dji.common.camera.ResolutionAndFrameRate;
import dji.common.camera.SettingsDefinitions;
import dji.common.error.DJICameraError;
import dji.common.error.DJIError;
import dji.common.util.CallbackUtils;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.model.P3.DataCameraGetCalibrationControl;
import dji.midware.data.model.P3.DataCameraGetCapabilityRange;
import dji.midware.data.model.P3.DataCameraGetPushRawNewParam;
import dji.midware.data.model.P3.DataCameraGetPushShotParams;
import dji.midware.data.model.P3.DataCameraGetPushStateInfo;
import dji.midware.data.model.P3.DataCameraSetCalibrationControl;
import dji.midware.data.model.P3.DataCameraSetPhoto;
import dji.midware.data.model.P3.DataCameraSetPhotoMode;
import dji.midware.data.model.P3.DataCameraSetRawNewParam;
import dji.midware.data.model.P3.DataCameraSetSSDRawVideoDigitalFilter;
import dji.midware.data.model.P3.DataCameraSetVOutParams;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.media.colors.ColorOscilloscopeDisplayView;
import dji.midware.media.colors.ColorOscilloscopeUtils;
import dji.sdksharedlib.DJISDKCache;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.extension.KeyHelper;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Getter;
import dji.sdksharedlib.hardware.abstractions.Setter;
import dji.sdksharedlib.keycatalog.CameraKeys;
import dji.sdksharedlib.store.DJISDKCacheParamValue;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class DJICameraInspire2Abstraction extends DJICameraX5BaseAbstraction {
    private static final int MAX_RETRY_TIMES = 8;
    protected static final int PROTECTION_PERIOD_IN_MILISECOND = 1000;
    private static final String TAG = "DJICameraInspire2Abstraction";
    protected SettingsDefinitions.SensorCleaningState currentDedustingState = SettingsDefinitions.SensorCleaningState.UNKNOWN;
    private long lastUpdateTime = 0;
    /* access modifiers changed from: private */
    public int retry = 0;
    protected SettingsDefinitions.ExposureSensitivityMode sensitivityMode = SettingsDefinitions.ExposureSensitivityMode.UNKNOWN;

    static /* synthetic */ int access$1208(DJICameraInspire2Abstraction x0) {
        int i = x0.retry;
        x0.retry = i + 1;
        return i;
    }

    /* access modifiers changed from: protected */
    public boolean isVideoDewarpingSupported() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isEIModeSupported() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isMediaDownloadModeSupported() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isVideoPlaybackSupported() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isHDRPhotoSupported() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isNewProcessOfActivation() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isSlowMotionSupported() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isColorWaveformSupported() {
        return true;
    }

    /* access modifiers changed from: protected */
    public void initializeCustomizedKey() {
        super.initializeCustomizedKey();
        updateSomeSupportedKey();
        if (DataCameraGetPushRawNewParam.getInstance().isGetted()) {
            DataCameraGetPushRawNewParam.getInstance().swapValidData(getExpectedSenderIdByIndex());
            onEvent3BackgroundThread(DataCameraGetPushRawNewParam.getInstance());
        }
        if (ColorOscilloscopeUtils.getOscilloscopeSwitchFromSp()) {
            onEvent3BackgroundThread(ColorOscilloscopeDisplayView.OscilloscopeDisplayEvent.Open);
        } else {
            onEvent3BackgroundThread(ColorOscilloscopeDisplayView.OscilloscopeDisplayEvent.Close);
        }
        if (ColorOscilloscopeUtils.getOscilloscopeIsExpFromSp()) {
            onEvent3BackgroundThread(ColorOscilloscopeDisplayView.OscilloscopeDisplayEvent.SwitchToExp);
        } else {
            onEvent3BackgroundThread(ColorOscilloscopeDisplayView.OscilloscopeDisplayEvent.SwitchToColor);
        }
    }

    /* access modifiers changed from: protected */
    public boolean isPhotoIntervalParamValid(int interval, int count) {
        SettingsDefinitions.PhotoFileFormat fileFormat = SettingsDefinitions.PhotoFileFormat.values()[DataCameraGetPushShotParams.getInstance().getImageFormat(getExpectedSenderIdByIndex())];
        if (fileFormat == SettingsDefinitions.PhotoFileFormat.JPEG && interval < 2) {
            return false;
        }
        if ((fileFormat == SettingsDefinitions.PhotoFileFormat.RAW || fileFormat == SettingsDefinitions.PhotoFileFormat.RAW_AND_JPEG) && interval < 5) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isBroadcastModeSupported() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isMediaDownModeMapValue2() {
        return true;
    }

    /* access modifiers changed from: protected */
    public int mapCameraModeToProtocolValue(SettingsDefinitions.CameraMode mode) {
        if (SettingsDefinitions.CameraMode.MEDIA_DOWNLOAD == mode) {
            return 2;
        }
        if (SettingsDefinitions.CameraMode.BROADCAST == mode) {
            return 8;
        }
        return mode.value();
    }

    @Setter(CameraKeys.SSD_LEGACY_COLOR)
    public void setSSDRawVideoDigitalFilter(SettingsDefinitions.SSDLegacyColor ssdDigitalFilter, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!isSSDSupported()) {
            if (callback != null) {
                callback.onFails(DJICameraError.COMMON_UNSUPPORTED);
            }
        } else if (ssdDigitalFilter != SettingsDefinitions.SSDLegacyColor.UNKNOWN) {
            DataCameraSetSSDRawVideoDigitalFilter setter = DataCameraSetSSDRawVideoDigitalFilter.getInstance();
            setter.setReceiverId(getReceiverIdByIndex());
            setter.setSSDDigitalFilter(ssdDigitalFilter.value()).start(CallbackUtils.getSetterDJIDataCallback(callback));
        } else if (callback != null) {
            callback.onFails(DJICameraError.COMMON_PARAM_ILLEGAL);
        }
    }

    @Setter(CameraKeys.PHOTO_RAW_BURST_COUNT)
    public void setPhotoRawBurstCount(SettingsDefinitions.PhotoBurstCount count, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!isSSDSupported()) {
            if (callback != null) {
                callback.onFails(DJICameraError.COMMON_UNSUPPORTED);
            }
        } else if (count != SettingsDefinitions.PhotoBurstCount.UNKNOWN) {
            DataCameraSetPhotoMode setter = DataCameraSetPhotoMode.getInstance();
            setter.setReceiverId(getReceiverIdByIndex());
            setter.setType(DataCameraSetPhoto.TYPE.RAWBURST).setContinueNum(count.value()).start(CallbackUtils.getSetterDJIDataCallback(callback));
        } else if (callback != null) {
            callback.onFails(DJICameraError.COMMON_PARAM_ILLEGAL);
        }
    }

    public void setLiveViewOutputMode(boolean enabled, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        int value;
        if (enabled) {
            value = 2;
        } else {
            value = 1;
        }
        ((DataCameraSetVOutParams) DataCameraSetVOutParams.getInstance().setStream(value).setReceiverId(getReceiverIdByIndex(), DataCameraSetVOutParams.class)).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraInspire2Abstraction.AnonymousClass1 */

            public void onSuccess(Object model) {
                if (callback != null) {
                    callback.onSuccess(null);
                }
            }

            public void onFailure(Ccode ccode) {
                if (callback != null) {
                    callback.onFails(DJICameraError.getDJIError(ccode));
                }
            }
        });
    }

    /* access modifiers changed from: protected */
    public boolean checkTrueColorDigitalFilterSupported() {
        return true;
    }

    public void setVideoResolutionAndFrameRate(ResolutionAndFrameRate resolutionAndFrameRate, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (isResolutionAndFrameRateAlreadySet(resolutionAndFrameRate)) {
            CallbackUtils.onSuccess(callback, (Object) null);
        } else if (!isResolutionAndFrameRateValid(resolutionAndFrameRate)) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_ILLEGAL);
        } else {
            super.setVideoResolutionAndFrameRate(resolutionAndFrameRate, callback);
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCameraGetPushRawNewParam pushInfo) {
        if (pushInfo.isGetted() && isValidSender(pushInfo.getSenderId())) {
            this.sensitivityMode = SettingsDefinitions.ExposureSensitivityMode.find(pushInfo.getShootingMode().getCmd());
            notifyValueChangeForKeyPath(this.sensitivityMode, CameraKeys.EXPOSURE_SENSITIVITY_MODE);
            notifyValueChangeForKeyPath(Integer.valueOf(pushInfo.getEIValue()), CameraKeys.EI_VALUE);
            notifyValueChangeForKeyPath(SettingsDefinitions.EIColor.find(pushInfo.getProxyLooksInt()), CameraKeys.EI_COLOR);
        }
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, java.lang.String):void
     arg types: [boolean, java.lang.String]
     candidates:
      dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, dji.sdksharedlib.keycatalog.DJISDKCacheKey):void
      dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, java.lang.String):void */
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(ColorOscilloscopeDisplayView.OscilloscopeDisplayEvent event) {
        ColorWaveformSettings.ColorWaveformDisplayState colorWaveformDisplayState = ColorWaveformSettings.ColorWaveformDisplayState.UNKNOWN;
        switch (event) {
            case Open:
                DJILog.d("LWF", "OscilloscopeDisplayEvent value is true", new Object[0]);
                notifyValueChangeForKeyPath((Object) true, CameraKeys.COLOR_WAVEFORM_ENABLED);
                return;
            case Close:
                DJILog.d("LWF", "OscilloscopeDisplayEvent value is false", new Object[0]);
                notifyValueChangeForKeyPath((Object) false, CameraKeys.COLOR_WAVEFORM_ENABLED);
                return;
            case SwitchToExp:
                ColorWaveformSettings.ColorWaveformDisplayState waveformState = ColorWaveformSettings.ColorWaveformDisplayState.SWITCH_TO_EXP;
                DJILog.d("LWF", "OscilloscopeDisplayEvent value is SWITCH_TO_EXP", new Object[0]);
                notifyValueChangeForKeyPath(waveformState, CameraKeys.COLOR_WAVEFORM_DISPLAY_STATE);
                return;
            case SwitchToColor:
                ColorWaveformSettings.ColorWaveformDisplayState waveformState2 = ColorWaveformSettings.ColorWaveformDisplayState.SWITCH_TO_COLOR;
                DJILog.d("LWF", "OscilloscopeDisplayEvent value is SWITCH_TO_COLOR", new Object[0]);
                notifyValueChangeForKeyPath(waveformState2, CameraKeys.COLOR_WAVEFORM_DISPLAY_STATE);
                return;
            default:
                return;
        }
    }

    @Setter(CameraKeys.COLOR_WAVEFORM_ENABLED)
    public void setColorWaveformEnabled(boolean enabled, DJISDKCacheHWAbstraction.InnerCallback callback) {
        DJILog.d("LWF", "setColorWaveformEnabled value " + enabled, new Object[0]);
        if (enabled) {
            ColorOscilloscopeUtils.switchColorOscilloscope(true);
        } else {
            ColorOscilloscopeUtils.switchColorOscilloscope(false);
        }
        callback.onSuccess(null);
    }

    @Setter(CameraKeys.COLOR_WAVEFORM_DISPLAY_STATE)
    public void setColorWaveformDisplayState(ColorWaveformSettings.ColorWaveformDisplayState state, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (state == ColorWaveformSettings.ColorWaveformDisplayState.SWITCH_TO_EXP) {
            ColorOscilloscopeUtils.setOscilloscopeIsExpToSp(true);
        } else if (state == ColorWaveformSettings.ColorWaveformDisplayState.SWITCH_TO_COLOR) {
            ColorOscilloscopeUtils.setOscilloscopeIsExpToSp(false);
        }
    }

    @Setter(CameraKeys.COLOR_WAVEFORM_DISPLAY_MODE)
    public void setColorWaveformDisplayMode(ColorWaveformSettings.ColorWaveformDisplayMode mode, DJISDKCacheHWAbstraction.InnerCallback callback) {
    }

    @Getter(CameraKeys.COLOR_WAVEFORM_DISPLAY_MODE)
    public void getColorWaveformDisplayMode(DJISDKCacheHWAbstraction.InnerCallback callback) {
    }

    /* access modifiers changed from: private */
    public void updateSomeSupportedKey() {
        long now = System.currentTimeMillis();
        if (now - this.lastUpdateTime >= 1000) {
            this.lastUpdateTime = now;
            DataCameraGetCapabilityRange getter = new DataCameraGetCapabilityRange();
            getter.setReceiverId(getReceiverIdByIndex());
            getter.setCmdSet(CmdSet.CAMERA.value()).setOperation(1).setParamId(DataCameraGetCapabilityRange.ParamType.LensInfo).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraInspire2Abstraction.AnonymousClass2 */

                /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
                 method: dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraInspire2Abstraction.access$300(dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraInspire2Abstraction, java.lang.Object, dji.sdksharedlib.keycatalog.DJISDKCacheKey):void
                 arg types: [dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraInspire2Abstraction, java.lang.Boolean, dji.sdksharedlib.keycatalog.DJISDKCacheKey]
                 candidates:
                  dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.access$300(dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction, java.lang.Object, dji.sdksharedlib.keycatalog.DJISDKCacheKey):void
                  dji.sdksharedlib.hardware.abstractions.camera.DJICameraAbstraction.access$300(dji.sdksharedlib.hardware.abstractions.camera.DJICameraAbstraction, java.lang.String, int):java.lang.String
                  dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraInspire2Abstraction.access$300(dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraInspire2Abstraction, java.lang.Object, dji.sdksharedlib.keycatalog.DJISDKCacheKey):void */
                /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
                 method: dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraInspire2Abstraction.access$500(dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraInspire2Abstraction, java.lang.Object, dji.sdksharedlib.keycatalog.DJISDKCacheKey):void
                 arg types: [dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraInspire2Abstraction, boolean, dji.sdksharedlib.keycatalog.DJISDKCacheKey]
                 candidates:
                  dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.access$500(dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction, java.lang.Object, dji.sdksharedlib.keycatalog.DJISDKCacheKey):void
                  dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraInspire2Abstraction.access$500(dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraInspire2Abstraction, java.lang.Object, dji.sdksharedlib.keycatalog.DJISDKCacheKey):void */
                public void onSuccess(Object model) {
                    boolean supportMechShutter = ((DataCameraGetCapabilityRange) model).isSupportMechShutter();
                    boolean supportNDFilter = ((DataCameraGetCapabilityRange) model).isSupportNDFilter();
                    DJICameraInspire2Abstraction.this.notifyValueChangeForKeyPath(Boolean.valueOf(supportMechShutter), CameraKeys.IS_MECHANICAL_SHUTTER_SUPPORTED);
                    if (supportMechShutter) {
                        DJICameraInspire2Abstraction.this.notifyValueChangeForKeyPath((Object) Boolean.valueOf(DataCameraGetPushShotParams.getInstance().isMechanicShutterEnable(DJICameraInspire2Abstraction.this.getExpectedSenderIdByIndex())), DJICameraInspire2Abstraction.this.convertKeyToPath(CameraKeys.MECHANICAL_SHUTTER_ENABLED));
                    } else {
                        DJICameraInspire2Abstraction.this.notifyValueChangeForKeyPath((Object) false, DJICameraInspire2Abstraction.this.convertKeyToPath(CameraKeys.MECHANICAL_SHUTTER_ENABLED));
                    }
                    DJICameraInspire2Abstraction.this.notifyValueChangeForKeyPath(Boolean.valueOf(supportNDFilter), CameraKeys.IS_ND_FILTER_MODE_SUPPORTED);
                    if (supportNDFilter) {
                        DJICameraInspire2Abstraction.this.notifyValueChangeForKeyPath(SettingsDefinitions.NDFilterMode.find(DataCameraGetPushStateInfo.getInstance().getNDFilter(DJICameraInspire2Abstraction.this.getExpectedSenderIdByIndex()).getCMD()), DJICameraInspire2Abstraction.this.convertKeyToPath(CameraKeys.NDFILTER_MODE));
                    } else {
                        DJICameraInspire2Abstraction.this.notifyValueChangeForKeyPath(SettingsDefinitions.NDFilterMode.UNKNOWN, DJICameraInspire2Abstraction.this.convertKeyToPath(CameraKeys.NDFILTER_MODE));
                    }
                    int unused = DJICameraInspire2Abstraction.this.retry = 0;
                }

                public void onFailure(Ccode ccode) {
                    DJICameraInspire2Abstraction.access$1208(DJICameraInspire2Abstraction.this);
                    if (DJICameraInspire2Abstraction.this.retry < 8) {
                        DJICameraInspire2Abstraction.this.handler.postDelayed(new Runnable() {
                            /* class dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraInspire2Abstraction.AnonymousClass2.AnonymousClass1 */

                            public void run() {
                                DJICameraInspire2Abstraction.this.updateSomeSupportedKey();
                            }
                        }, 1000);
                    } else {
                        DJILog.e(DJICameraInspire2Abstraction.TAG, "update support key failed! retry=" + DJICameraInspire2Abstraction.this.retry, new Object[0]);
                    }
                }
            });
        }
    }

    @Setter(CameraKeys.EXPOSURE_SENSITIVITY_MODE)
    public void setExposureSensitivityMode(SettingsDefinitions.ExposureSensitivityMode data, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!isEIModeSupported()) {
            CallbackUtils.onFailure(callback, DJIError.COMMAND_NOT_SUPPORTED_BY_FIRMWARE);
        } else if (CacheHelper.getCamera(this.index, "Mode") != SettingsDefinitions.CameraMode.RECORD_VIDEO) {
            CallbackUtils.onFailure(callback, DJICameraError.CANNOT_SET_PARAMETERS_IN_THIS_STATE);
        } else {
            DataCameraSetRawNewParam setter = new DataCameraSetRawNewParam();
            setter.setReceiverId(getReceiverIdByIndex());
            setter.setCmdType(DataCameraSetRawNewParam.CMD_TYPE.ShootMode);
            setter.setShootingMode(DataCameraGetPushRawNewParam.DJIShootingMode.find(data.value()));
            setter.start(CallbackUtils.defaultCB(callback));
        }
    }

    @Setter(CameraKeys.EI_VALUE)
    public void setEIValue(Integer data, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!isEIModeSupported()) {
            CallbackUtils.onFailure(callback, DJIError.COMMAND_NOT_SUPPORTED_BY_FIRMWARE);
        } else if (this.sensitivityMode != SettingsDefinitions.ExposureSensitivityMode.EI) {
            CallbackUtils.onFailure(callback, DJICameraError.CANNOT_SET_PARAMETERS_IN_THIS_STATE);
        } else {
            DataCameraSetRawNewParam setter = new DataCameraSetRawNewParam();
            setter.setReceiverId(getReceiverIdByIndex());
            setter.setCmdType(DataCameraSetRawNewParam.CMD_TYPE.EIValue);
            setter.setEiValue(data.intValue());
            setter.start(CallbackUtils.defaultCB(callback));
        }
    }

    @Getter(CameraKeys.RECOMMENDED_EI_VALUE)
    public void getRecommendEIValue(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!isEIModeSupported()) {
            CallbackUtils.onFailure(callback, DJIError.COMMAND_NOT_SUPPORTED_BY_FIRMWARE);
        } else if (this.sensitivityMode != SettingsDefinitions.ExposureSensitivityMode.EI) {
            CallbackUtils.onFailure(callback, DJICameraError.CANNOT_SET_PARAMETERS_IN_THIS_STATE);
        } else {
            final DataCameraGetCapabilityRange getter = new DataCameraGetCapabilityRange();
            getter.setReceiverId(getReceiverIdByIndex());
            getter.setCmdSet(CmdSet.CAMERA.value()).setOperation(1).setParamId(DataCameraGetCapabilityRange.ParamType.NativeEI);
            getter.start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraInspire2Abstraction.AnonymousClass3 */

                public void onSuccess(Object model) {
                    CallbackUtils.onSuccess(callback, Integer.valueOf(getter.getNativeEI()));
                }

                public void onFailure(Ccode ccode) {
                    CallbackUtils.onFailure(callback, ccode);
                }
            });
        }
    }

    @Getter(CameraKeys.EI_VALUE_RANGE)
    public void getRecommendEIValueRange(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!isEIModeSupported()) {
            CallbackUtils.onFailure(callback, DJIError.COMMAND_NOT_SUPPORTED_BY_FIRMWARE);
        } else if (this.sensitivityMode != SettingsDefinitions.ExposureSensitivityMode.EI) {
            CallbackUtils.onFailure(callback, DJICameraError.CANNOT_SET_PARAMETERS_IN_THIS_STATE);
        } else {
            final DataCameraGetCapabilityRange getter = new DataCameraGetCapabilityRange();
            getter.setReceiverId(getReceiverIdByIndex());
            getter.setCmdSet(CmdSet.CAMERA.value()).setOperation(1).setParamId(DataCameraGetCapabilityRange.ParamType.EIRange);
            getter.start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraInspire2Abstraction.AnonymousClass4 */

                public void onSuccess(Object model) {
                    CallbackUtils.onSuccess(callback, getter.getEIRange());
                }

                public void onFailure(Ccode ccode) {
                    CallbackUtils.onFailure(callback, ccode);
                }
            });
        }
    }

    @Setter(CameraKeys.EI_COLOR)
    public void setEIDigitalFilter(SettingsDefinitions.EIColor data, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!isEIModeSupported()) {
            CallbackUtils.onFailure(callback, DJIError.COMMAND_NOT_SUPPORTED_BY_FIRMWARE);
        } else if (this.sensitivityMode != SettingsDefinitions.ExposureSensitivityMode.EI) {
            CallbackUtils.onFailure(callback, DJICameraError.CANNOT_SET_PARAMETERS_IN_THIS_STATE);
        } else {
            DataCameraSetRawNewParam setter = new DataCameraSetRawNewParam();
            setter.setReceiverId(getReceiverIdByIndex());
            setter.setCmdType(DataCameraSetRawNewParam.CMD_TYPE.ProxyLook);
            setter.setProxyLooks(data.value());
            setter.start(CallbackUtils.defaultCB(callback));
        }
    }

    @Setter(CameraKeys.DEWARPING_ENABLED)
    public void setDewarpingEnabled(Boolean data, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!isVideoDewarpingSupported()) {
            CallbackUtils.onFailure(callback, DJIError.COMMAND_NOT_SUPPORTED_BY_FIRMWARE);
            return;
        }
        DataCameraSetCalibrationControl setter = new DataCameraSetCalibrationControl();
        setter.setReceiverId(getReceiverIdByIndex());
        setter.setDeWarpCalibration(data.booleanValue());
        setter.start(CallbackUtils.defaultCB(callback));
    }

    @Getter(CameraKeys.DEWARPING_ENABLED)
    public void getDewarpingEnabled(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!isVideoDewarpingSupported()) {
            CallbackUtils.onFailure(callback, DJIError.COMMAND_NOT_SUPPORTED_BY_FIRMWARE);
            return;
        }
        final DataCameraGetCalibrationControl getter = new DataCameraGetCalibrationControl();
        getter.setReceiverId(getReceiverIdByIndex());
        getter.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraInspire2Abstraction.AnonymousClass5 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, Boolean.valueOf(getter.isDeWarpCalibrationEnabled()));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJICameraError.getDJIError(ccode));
            }
        });
    }

    /* access modifiers changed from: protected */
    public void trigerLensChanged() {
        super.trigerLensChanged();
        this.handler.postDelayed(new Runnable() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraInspire2Abstraction.AnonymousClass6 */

            public void run() {
                DJICameraInspire2Abstraction.this.updateSomeSupportedKey();
            }
        }, 1000);
    }

    /* access modifiers changed from: protected */
    public boolean isResolutionAndFrameRateAlreadySet(ResolutionAndFrameRate resolutionAndFrameRate) {
        SettingsDefinitions.VideoResolution resolution = resolutionAndFrameRate.getResolution();
        SettingsDefinitions.VideoFrameRate frameRate = resolutionAndFrameRate.getFrameRate();
        DJISDKCacheParamValue value = DJISDKCache.getInstance().getAvailableValue(KeyHelper.getCameraKey(this.index, CameraKeys.RESOLUTION_FRAME_RATE));
        if (value != null && (value.getData() instanceof ResolutionAndFrameRate)) {
            ResolutionAndFrameRate data = (ResolutionAndFrameRate) value.getData();
            if (data.getFrameRate() == frameRate && data.getResolution() == resolution) {
                return true;
            }
            return false;
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isResolutionAndFrameRateValid(ResolutionAndFrameRate value) {
        DJISDKCacheParamValue rangeValue = DJISDKCache.getInstance().getAvailableValue(convertKeyToPath(CameraKeys.VIDEO_RESOLUTION_FRAME_RATE_RANGE));
        if (!(rangeValue == null || rangeValue.getData() == null)) {
            for (ResolutionAndFrameRate resolutionAndFrameRate : (ResolutionAndFrameRate[]) rangeValue.getData()) {
                if (resolutionAndFrameRate.equals(value)) {
                    return true;
                }
            }
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isSSDResolutionAndFrameRateValid(ResolutionAndFrameRate value) {
        DJISDKCacheParamValue rangeValue = DJISDKCache.getInstance().getAvailableValue(convertKeyToPath(CameraKeys.SSD_VIDEO_RESOLUTION_FRAME_RATE_RANGE));
        if (!(rangeValue == null || rangeValue.getData() == null)) {
            for (ResolutionAndFrameRate resolutionAndFrameRate : (ResolutionAndFrameRate[]) rangeValue.getData()) {
                if (resolutionAndFrameRate.equals(value)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void destroy() {
        super.destroy();
        this.retry = 0;
    }
}
