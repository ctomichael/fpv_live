package dji.sdksharedlib.hardware.abstractions.camera.GD600;

import android.graphics.PointF;
import dji.common.camera.CameraUtils;
import dji.common.camera.ResolutionAndFrameRate;
import dji.common.camera.SettingsDefinitions;
import dji.common.camera.StabilizationState;
import dji.common.error.DJICameraError;
import dji.common.error.DJIError;
import dji.common.flightcontroller.ControlGimbalBehavior;
import dji.common.flightcontroller.DJIMultiLEDControlMode;
import dji.common.util.CallbackUtils;
import dji.fieldAnnotation.EXClassNullAway;
import dji.internal.logics.CommonUtil;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.model.P3.DataCameraGetDefogEnabled;
import dji.midware.data.model.P3.DataCameraGetPushShotParams;
import dji.midware.data.model.P3.DataCameraGetPushStateInfo;
import dji.midware.data.model.P3.DataCameraGetPushTapZoomStateInfo;
import dji.midware.data.model.P3.DataCameraGetTapZoom;
import dji.midware.data.model.P3.DataCameraIRCEnable;
import dji.midware.data.model.P3.DataCameraSetDefogEnabled;
import dji.midware.data.model.P3.DataCameraSetExposureMode;
import dji.midware.data.model.P3.DataCameraSetOpticsZoomMode;
import dji.midware.data.model.P3.DataCameraSetPhoto;
import dji.midware.data.model.P3.DataCameraSetTapZoom;
import dji.midware.data.model.P3.DataCameraSetTapZoomTarget;
import dji.midware.data.model.P3.DataEyeGetPushStabilizationState;
import dji.midware.data.model.P3.DataSingleSendAppStateForStabilization;
import dji.midware.data.model.P3.DataSingleStabilizationCtrl;
import dji.midware.data.model.P3.DataSpecialControl;
import dji.midware.interfaces.DJIDataCallBack;
import dji.sdksharedlib.DJISDKCache;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.extension.KeyHelper;
import dji.sdksharedlib.hardware.abstractions.Action;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Getter;
import dji.sdksharedlib.hardware.abstractions.Setter;
import dji.sdksharedlib.hardware.abstractions.camera.DJICameraAbstraction;
import dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraZ3Abstraction;
import dji.sdksharedlib.keycatalog.CameraKeys;
import dji.sdksharedlib.keycatalog.FlightControllerKeys;
import dji.sdksharedlib.listener.DJIParamAccessListener;
import dji.sdksharedlib.store.DJISDKCacheParamValue;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class DJICameraGD600Abstraction extends DJICameraZ3Abstraction implements DJIParamAccessListener {
    private StabilizationState stabilizationState;

    /* access modifiers changed from: protected */
    public boolean isVideoPlaybackSupported() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isNewProcessOfActivation() {
        return true;
    }

    public void syncPushDataFromMidware() {
        super.syncPushDataFromMidware();
        if (DataCameraGetPushTapZoomStateInfo.getInstance().isGetted()) {
            DataCameraGetPushTapZoomStateInfo.getInstance().swapValidData(getExpectedSenderIdByIndex());
            onEvent3BackgroundThread(DataCameraGetPushTapZoomStateInfo.getInstance());
        }
        if (DataCameraGetPushShotParams.getInstance().isGetted()) {
            DataCameraGetPushShotParams.getInstance().swapValidData(getExpectedSenderIdByIndex());
            onEvent3BackgroundThread(DataCameraGetPushShotParams.getInstance());
        }
    }

    /* access modifiers changed from: protected */
    public boolean isPlaybackSupported() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isMediaDownloadModeSupported() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isAdjustableApertureSupported() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isAFSupported() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isMFSupported() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isTapZoomSupported() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isAFTargetSupported() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isOpticalZoomSupported() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isOpticalZoomScaleSupported() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isDigitalZoomScaleSupported() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isMediaDownModeMapValue2() {
        return true;
    }

    /* access modifiers changed from: protected */
    public String getDisplayName() {
        return DJICameraAbstraction.DisplayNameZ30;
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCameraGetPushTapZoomStateInfo tapZoomStateInfo) {
        if (tapZoomStateInfo.isGetted() && isValidSender(tapZoomStateInfo.getSenderId())) {
            int multiplier = tapZoomStateInfo.getMultiplier();
            SettingsDefinitions.TapZoomState tapZoomState = SettingsDefinitions.TapZoomState.find(tapZoomStateInfo.getTapZoomState());
            notifyValueChangeForKeyPath(Integer.valueOf(multiplier), convertKeyToPath(CameraKeys.TAP_ZOOM_MULTIPLIER));
            notifyValueChangeForKeyPath(tapZoomState, convertKeyToPath(CameraKeys.TAP_ZOOM_STATE));
        }
    }

    @Setter("Mode")
    public void setCameraMode(SettingsDefinitions.CameraMode mode, DJISDKCacheHWAbstraction.InnerCallback callback) {
        int value = mode.value();
        if (mode == SettingsDefinitions.CameraMode.MEDIA_DOWNLOAD) {
            value = 2;
        }
        if (mode == SettingsDefinitions.CameraMode.PLAYBACK) {
            value = 2;
        }
        setCameraModeProtocol(value, callback);
    }

    /* access modifiers changed from: protected */
    public int mapCameraModeToProtocolValue(SettingsDefinitions.CameraMode mode) {
        if (SettingsDefinitions.CameraMode.MEDIA_DOWNLOAD == mode) {
            return 2;
        }
        return mode.value();
    }

    /* access modifiers changed from: protected */
    public boolean checkApertureValid(SettingsDefinitions.Aperture aperture) {
        return true;
    }

    @Setter(CameraKeys.TAP_ZOOM_AT_TARGET)
    public void setTapZoomTarget(PointF point, DJISDKCacheHWAbstraction.InnerCallback callback) {
        float x = point.x;
        float y = point.y;
        if (((Integer) CacheHelper.getValue(convertKeyToPath(CameraKeys.TAP_ZOOM_MULTIPLIER), Integer.MAX_VALUE)).intValue() > 1) {
            checkIfNeedWaitForPauseTheStabilization(callback, DataSingleSendAppStateForStabilization.CameraState.ZOOM_IN);
        } else {
            checkIfNeedWaitForPauseTheStabilization(callback, DataSingleSendAppStateForStabilization.CameraState.ZOOM_OUT);
        }
        ((DataCameraSetTapZoomTarget) DataCameraSetTapZoomTarget.getInstance().setReceiverId(getReceiverIdByIndex(), DataCameraSetTapZoomTarget.class)).setXAxis(x).setYAxis(y).start(CallbackUtils.getSetterDJIDataCallback(callback));
    }

    /* access modifiers changed from: protected */
    public boolean checkWhiteBaLanceValid(SettingsDefinitions.WhiteBalancePreset whiteBalancePreset, int colorTemperature) {
        if (whiteBalancePreset == SettingsDefinitions.WhiteBalancePreset.WATER_SURFACE || whiteBalancePreset == SettingsDefinitions.WhiteBalancePreset.INDOOR_FLUORESCENT) {
            return false;
        }
        return super.checkWhiteBaLanceValid(whiteBalancePreset, colorTemperature);
    }

    @Setter(CameraKeys.TAP_ZOOM_ENABLED)
    public void setTapZoomEnabled(final boolean enabled, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        int value = ((Integer) CacheHelper.getValue(convertKeyToPath(CameraKeys.TAP_ZOOM_MULTIPLIER), Integer.MAX_VALUE)).intValue();
        if (Integer.MAX_VALUE != value) {
            ((DataCameraSetTapZoom) DataCameraSetTapZoom.getInstance().setEnabled(enabled).setMultiplier(value).setReceiverId(getReceiverIdByIndex(), DataCameraSetTapZoom.class)).start(CallbackUtils.getSetterDJIDataCallback(callback));
        } else {
            getTapZoomMultiplier(new DJISDKCacheHWAbstraction.InnerCallback() {
                /* class dji.sdksharedlib.hardware.abstractions.camera.GD600.DJICameraGD600Abstraction.AnonymousClass1 */

                public void onSuccess(Object o) {
                    if (o instanceof Integer) {
                        ((DataCameraSetTapZoom) DataCameraSetTapZoom.getInstance().setEnabled(enabled).setMultiplier(((Integer) o).intValue()).setReceiverId(DJICameraGD600Abstraction.this.getReceiverIdByIndex(), DataCameraSetTapZoom.class)).start(CallbackUtils.getSetterDJIDataCallback(callback));
                    } else {
                        CallbackUtils.onFailure(callback, DJIError.COMMON_SYSTEM_BUSY);
                    }
                }

                public void onFails(DJIError error) {
                    CallbackUtils.onFailure(callback, error);
                }
            });
        }
    }

    @Getter(CameraKeys.TAP_ZOOM_ENABLED)
    public void getTapZoomEnabled(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        ((DataCameraGetTapZoom) DataCameraGetTapZoom.getInstance().setReceiverId(getReceiverIdByIndex(), DataCameraGetTapZoom.class)).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.GD600.DJICameraGD600Abstraction.AnonymousClass2 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, Boolean.valueOf(DataCameraGetTapZoom.getInstance().getEnabled()));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    @Setter(CameraKeys.TAP_ZOOM_MULTIPLIER)
    public void setTapZoomMultiplier(int multiplier, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (multiplier >= 1 && multiplier <= 5) {
            ((DataCameraSetTapZoom) DataCameraSetTapZoom.getInstance().setMultiplier(multiplier).setReceiverId(getReceiverIdByIndex(), DataCameraSetTapZoom.class)).start(CallbackUtils.getSetterDJIDataCallback(callback));
        } else if (callback != null) {
            callback.onFails(DJICameraError.COMMON_PARAM_ILLEGAL);
        }
    }

    @Getter(CameraKeys.TAP_ZOOM_MULTIPLIER)
    public void getTapZoomMultiplier(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            ((DataCameraGetTapZoom) DataCameraGetTapZoom.getInstance().setReceiverId(getReceiverIdByIndex(), DataCameraGetTapZoom.class)).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.camera.GD600.DJICameraGD600Abstraction.AnonymousClass3 */

                public void onSuccess(Object model) {
                    CallbackUtils.onSuccess(callback, Integer.valueOf(DataCameraGetTapZoom.getInstance().getMultiplier()));
                }

                public void onFailure(Ccode ccode) {
                    CallbackUtils.onFailure(callback, ccode);
                }
            });
        }
    }

    @Setter(CameraKeys.DEFOG_ENABLED)
    public void setDefogEnabled(boolean enabled, DJISDKCacheHWAbstraction.InnerCallback callback) {
        ((DataCameraSetDefogEnabled) DataCameraSetDefogEnabled.getInstance().setEnabled(enabled).setReceiverId(getReceiverIdByIndex(), DataCameraSetDefogEnabled.class)).start(CallbackUtils.getSetterDJIDataCallback(callback));
    }

    @Getter(CameraKeys.DEFOG_ENABLED)
    public void getDefogEnabled(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            ((DataCameraGetDefogEnabled) DataCameraGetDefogEnabled.getInstance().setReceiverId(getReceiverIdByIndex(), DataCameraGetDefogEnabled.class)).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.camera.GD600.DJICameraGD600Abstraction.AnonymousClass4 */

                public void onSuccess(Object model) {
                    CallbackUtils.onSuccess(callback, Boolean.valueOf(DataCameraGetDefogEnabled.getInstance().getEnabled()));
                }

                public void onFailure(Ccode ccode) {
                    CallbackUtils.onFailure(callback, ccode);
                }
            });
        }
    }

    /* access modifiers changed from: protected */
    public boolean isPhotoIntervalParamValid(int interval, int count) {
        if (count != 255) {
            return false;
        }
        return isIntervalValueSupported(interval);
    }

    public void setVideoResolutionAndFrameRate(ResolutionAndFrameRate resolutionAndFrameRate, DJISDKCacheHWAbstraction.InnerCallback callback) {
        SettingsDefinitions.VideoResolution resolution = resolutionAndFrameRate.getResolution();
        SettingsDefinitions.VideoFrameRate frameRate = resolutionAndFrameRate.getFrameRate();
        if (resolution != SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080) {
            if (callback != null) {
                callback.onFails(DJIError.COMMON_PARAM_ILLEGAL);
            }
        } else if (frameRate == SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS || frameRate == SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS) {
            DJISDKCacheParamValue value = DJISDKCache.getInstance().getAvailableValue(KeyHelper.getCameraKey(this.index, CameraKeys.RESOLUTION_FRAME_RATE));
            if (value != null && (value.getData() instanceof ResolutionAndFrameRate)) {
                ResolutionAndFrameRate data = (ResolutionAndFrameRate) value.getData();
                if (data.getFrameRate() == frameRate && data.getResolution() == resolution) {
                    CallbackUtils.onSuccess(callback, (Object) null);
                    return;
                }
            }
            super.setVideoResolutionAndFrameRate(resolutionAndFrameRate, callback);
        } else if (callback != null) {
            callback.onFails(DJIError.COMMON_PARAM_ILLEGAL);
        }
    }

    public void setPhotoRatio(SettingsDefinitions.PhotoAspectRatio photoAspectRatio, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback == null) {
            return;
        }
        if (photoAspectRatio != SettingsDefinitions.PhotoAspectRatio.RATIO_16_9) {
            callback.onFails(DJIError.COMMON_UNSUPPORTED);
        } else {
            callback.onSuccess(SettingsDefinitions.PhotoAspectRatio.RATIO_16_9);
        }
    }

    public void setPhotoFileFormat(SettingsDefinitions.PhotoFileFormat photoFormat, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback == null) {
            return;
        }
        if (photoFormat != SettingsDefinitions.PhotoFileFormat.JPEG) {
            callback.onFails(DJIError.COMMON_UNSUPPORTED);
        } else {
            callback.onSuccess(SettingsDefinitions.PhotoFileFormat.JPEG);
        }
    }

    /* access modifiers changed from: protected */
    public boolean isBurstCountSupported(SettingsDefinitions.PhotoBurstCount count) {
        return count == SettingsDefinitions.PhotoBurstCount.BURST_COUNT_3 || count == SettingsDefinitions.PhotoBurstCount.BURST_COUNT_5;
    }

    public void setAntiFlicker(SettingsDefinitions.AntiFlickerFrequency antiFlicker, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (antiFlicker != SettingsDefinitions.AntiFlickerFrequency.AUTO) {
            super.setAntiFlicker(antiFlicker, callback);
        } else if (callback != null) {
            callback.onFails(DJIError.COMMON_PARAM_ILLEGAL);
        }
    }

    /* access modifiers changed from: protected */
    public boolean checkPhotoShootMode(SettingsDefinitions.ShootPhotoMode shootPhotoMode) {
        return shootPhotoMode == SettingsDefinitions.ShootPhotoMode.SINGLE || shootPhotoMode == SettingsDefinitions.ShootPhotoMode.INTERVAL || shootPhotoMode == SettingsDefinitions.ShootPhotoMode.BURST;
    }

    public void setDigitalFilter(SettingsDefinitions.CameraColor cameraColor, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (cameraColor == SettingsDefinitions.CameraColor.NONE || cameraColor == SettingsDefinitions.CameraColor.INVERSE || cameraColor == SettingsDefinitions.CameraColor.BLACK_AND_WHITE) {
            super.setDigitalFilter(cameraColor, callback);
        } else if (callback != null) {
            callback.onFails(DJICameraError.COMMON_PARAM_ILLEGAL);
        }
    }

    public void setVideoFileFormat(SettingsDefinitions.VideoFileFormat videoFileFormat, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (videoFileFormat == SettingsDefinitions.VideoFileFormat.MOV || videoFileFormat == SettingsDefinitions.VideoFileFormat.MP4) {
            super.setVideoFileFormat(videoFileFormat, callback);
        } else if (callback != null) {
            callback.onFails(DJIError.COMMON_PARAM_ILLEGAL);
        }
    }

    public void setSharpness(int sharpness, DJISDKCacheHWAbstraction.InnerCallback callback) {
        CallbackUtils.onFailure(callback, DJICameraError.COMMON_UNSUPPORTED);
    }

    public void setContrast(int contrast, DJISDKCacheHWAbstraction.InnerCallback callback) {
        CallbackUtils.onFailure(callback, DJICameraError.COMMON_UNSUPPORTED);
    }

    public void setSaturation(int value, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            callback.onFails(DJIError.COMMON_UNSUPPORTED);
        }
    }

    public void setHue(int hue, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            callback.onFails(DJIError.COMMON_UNSUPPORTED);
        }
    }

    @Action(CameraKeys.ONE_KEY_ZOOM)
    public void doOneKeyZoom(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        new DataCameraSetOpticsZoomMode().setOpticsZoomMode(DataCameraSetOpticsZoomMode.OpticsZommMode.SETZOOM, DataCameraSetOpticsZoomMode.ZoomSpeed.FASTEST, 0, 0).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.GD600.DJICameraGD600Abstraction.AnonymousClass5 */

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

    @Setter(CameraKeys.VISION_STABILIZATION_ENABLED)
    public void enableStabilization(boolean enable, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        new DataSingleStabilizationCtrl().setCtrlCmd(enable).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.GD600.DJICameraGD600Abstraction.AnonymousClass6 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, (Object) null);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    @Getter(CameraKeys.VISION_STABILIZATION_ENABLED)
    public void getStabilization(DJISDKCacheHWAbstraction.InnerCallback callback) {
        CallbackUtils.onSuccess(callback, Boolean.valueOf(DataEyeGetPushStabilizationState.getInstance().isGetted() && DataEyeGetPushStabilizationState.getInstance().getStateIsTurnOn()));
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataEyeGetPushStabilizationState state) {
        this.stabilizationState = new StabilizationState(state.getStateIsTurnOn(), state.getStateIsPaused(), StabilizationState.StabilizationPauseReason.find(state.getPauseReasonValue()));
        notifyValueChangeForKeyPath(this.stabilizationState, convertKeyToPath(CameraKeys.VISION_STABILIZATION_STATE));
        notifyValueChangeForKeyPath(Boolean.valueOf(state.getStateIsTurnOn()), convertKeyToPath(CameraKeys.VISION_STABILIZATION_ENABLED));
    }

    /* access modifiers changed from: protected */
    public void startShootPhoto(DJISDKCacheHWAbstraction.InnerCallback callback, SettingsDefinitions.ShootPhotoMode mode) {
        Object gimbalBehavior = CacheHelper.getValue(KeyHelper.getFlightControllerKey(FlightControllerKeys.CONTROL_GIMBAL_BEHAVIOR));
        if (gimbalBehavior != null && gimbalBehavior == ControlGimbalBehavior.BOTH_MOVE) {
            super.startShootPhoto(callback, mode);
        } else if (getReceiverIdByIndex() != 0 || CommonUtil.isPM420Platform()) {
            super.startShootPhoto(callback, mode);
        } else if (this instanceof DJICameraXT2Abstraction) {
            super.startShootPhoto(callback, mode);
        } else if (!checkPhotoShootMode(mode)) {
            CallbackUtils.onFailure(callback, DJICameraError.INVALID_PARAMETERS);
        } else if (DataCameraGetPushStateInfo.getInstance().getIsStoring(getExpectedSenderIdByIndex())) {
            CallbackUtils.onFailure(callback, DJICameraError.UNSUPPORTED_CMD_STATE);
        } else if (!CameraUtils.isSDCardReady(this.index)) {
            CallbackUtils.onFailure(callback, DJICameraError.SD_CARD_ERROR);
        } else if (!CameraUtils.isInActionMode(this.index) || !CameraUtils.isPhotoActionExecutable(this.index)) {
            CallbackUtils.onFailure(callback, DJICameraError.UNSUPPORTED_CMD_STATE);
        } else {
            int number = 1;
            int interval = 0;
            DataCameraSetPhoto.TYPE type = DataCameraSetPhoto.TYPE.find(mode.getInternalTypeValue());
            if (mode == SettingsDefinitions.ShootPhotoMode.BURST) {
                Object value = CacheHelper.getValue(KeyHelper.getCameraKey(this.index, CameraKeys.PHOTO_BURST_COUNT));
                if (value != null) {
                    number = ((SettingsDefinitions.PhotoBurstCount) value).value();
                } else {
                    CallbackUtils.onFailure(callback, DJICameraError.INVALID_PARAMETERS);
                    return;
                }
            } else if (mode == SettingsDefinitions.ShootPhotoMode.INTERVAL) {
                Object value2 = CacheHelper.getValue(KeyHelper.getCameraKey(this.index, CameraKeys.PHOTO_TIME_INTERVAL_SETTINGS));
                if (value2 != null) {
                    interval = ((SettingsDefinitions.PhotoTimeIntervalSettings) value2).getTimeIntervalInSeconds();
                    number = 255;
                } else {
                    CallbackUtils.onFailure(callback, DJICameraError.INVALID_PARAMETERS);
                    return;
                }
            }
            DataSpecialControl.getInstance().setPhotoType(type, number, interval).start(20);
            CallbackUtils.onSuccess(callback, (Object) null);
        }
    }

    @Action(CameraKeys.STOP_SHOOT_PHOTO)
    public void stopShootPhoto(DJISDKCacheHWAbstraction.InnerCallback callback) {
        Object gimbalBehavior = CacheHelper.getValue(KeyHelper.getFlightControllerKey(FlightControllerKeys.CONTROL_GIMBAL_BEHAVIOR));
        if (gimbalBehavior != null && gimbalBehavior == ControlGimbalBehavior.BOTH_MOVE) {
            super.stopShootPhoto(callback);
        } else if (getReceiverIdByIndex() != 0 || CommonUtil.isPM420Platform()) {
            super.stopShootPhoto(callback);
        } else if (!CameraUtils.isPhotoActionExecutable(this.index)) {
            DataSpecialControl.getInstance().setPhotoType(DataCameraSetPhoto.TYPE.STOP).start(20);
            CallbackUtils.onSuccess(callback, (Object) null);
        } else {
            CallbackUtils.onFailure(callback, DJICameraError.COMMON_EXECUTION_FAILED);
        }
    }

    @Action(CameraKeys.START_RECORD_VIDEO)
    public void startRecordVideo(DJISDKCacheHWAbstraction.InnerCallback callback) {
        Object gimbalBehavior = CacheHelper.getValue(KeyHelper.getFlightControllerKey(FlightControllerKeys.CONTROL_GIMBAL_BEHAVIOR));
        if (gimbalBehavior != null && gimbalBehavior == ControlGimbalBehavior.BOTH_MOVE) {
            super.startRecordVideo(callback);
        } else if (getReceiverIdByIndex() != 0 || CommonUtil.isPM420Platform()) {
            super.startRecordVideo(callback);
        } else if (this instanceof DJICameraWM245DualLightVLAbstraction) {
            super.startRecordVideo(callback);
        } else if (CacheHelper.getCamera(this.index, "Mode") != SettingsDefinitions.CameraMode.RECORD_VIDEO && CacheHelper.getCamera(this.index, "Mode") != SettingsDefinitions.CameraMode.BROADCAST) {
            CallbackUtils.onFailure(callback, DJICameraError.UNSUPPORTED_CMD_STATE);
        } else if (!CameraUtils.isSDCardReady(this.index)) {
            CallbackUtils.onFailure(callback, DJICameraError.SD_CARD_ERROR);
        } else if (!CameraUtils.isInActionMode(this.index) || !CameraUtils.isRecordActionExecutable(this.index)) {
            CallbackUtils.onFailure(callback, DJICameraError.UNSUPPORTED_CMD_STATE);
        } else {
            DataSpecialControl.getInstance().setRecordType(true).start(20);
            CallbackUtils.onSuccess(callback, (Object) null);
        }
    }

    @Setter(CameraKeys.IRC_ENABLE)
    public void setIRCuttingEnabled(boolean enabled, DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataCameraIRCEnable setter = new DataCameraIRCEnable();
        setter.setMode(true);
        setter.setReceiverId(getReceiverIdByIndex());
        setter.setEnable(enabled);
        setter.start(CallbackUtils.defaultCB(callback));
    }

    @Getter(CameraKeys.IRC_ENABLE)
    public void getIRCuttingEnabled(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataCameraIRCEnable getter = new DataCameraIRCEnable();
        getter.setMode(false);
        getter.setReceiverId(getReceiverIdByIndex());
        getter.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.GD600.DJICameraGD600Abstraction.AnonymousClass7 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, Boolean.valueOf(getter.isEnabled()));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    @Setter(CameraKeys.ISO)
    public void setISO(SettingsDefinitions.ISO iso, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (DataCameraGetPushShotParams.getInstance().getExposureMode(getExpectedSenderIdByIndex()) == DataCameraSetExposureMode.ExposureMode.M || iso != SettingsDefinitions.ISO.AUTO) {
            super.setISO(iso, callback);
        } else if (callback != null) {
            callback.onSuccess(iso);
        }
    }

    @Getter(CameraKeys.MULTI_LEDS_AUTO_ENABLED)
    public void getMultiLEDsAutoEnabled(DJISDKCacheHWAbstraction.InnerCallback callback) {
        CallbackUtils.onSuccess(callback, new DJIMultiLEDControlMode(DataCameraGetPushShotParams.getInstance().getLedArmControl()));
    }
}
