package dji.sdksharedlib.hardware.abstractions.camera.zenmuse;

import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import dji.common.camera.CameraUtils;
import dji.common.camera.SettingsDefinitions;
import dji.common.camera.TauVideoFormat;
import dji.common.camera.ThermalAreaTemperatureAggregations;
import dji.common.camera.ThermalMeasurementMode;
import dji.common.camera.ThermalVersion;
import dji.common.error.DJICameraError;
import dji.common.error.DJIError;
import dji.common.flightcontroller.ControlGimbalBehavior;
import dji.common.util.CallbackUtils;
import dji.common.util.DJICameraEnumMappingUtil;
import dji.fieldAnnotation.EXClassNullAway;
import dji.internal.logics.CommonUtil;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.config.P3.CmdIdCamera;
import dji.midware.data.manager.P3.ServiceManager;
import dji.midware.data.model.P3.DataBaseCameraSetting;
import dji.midware.data.model.P3.DataCameraGetMode;
import dji.midware.data.model.P3.DataCameraGetPushShotParams;
import dji.midware.data.model.P3.DataCameraGetPushStateInfo;
import dji.midware.data.model.P3.DataCameraGetPushTauParam;
import dji.midware.data.model.P3.DataCameraGetStateInfo;
import dji.midware.data.model.P3.DataCameraLoadParams;
import dji.midware.data.model.P3.DataCameraSaveParams;
import dji.midware.data.model.P3.DataCameraSetFocusParam;
import dji.midware.data.model.P3.DataCameraSetMode;
import dji.midware.data.model.P3.DataCameraSetPhoto;
import dji.midware.data.model.P3.DataCameraSetPhotoMode;
import dji.midware.data.model.P3.DataCameraSetTimeParams;
import dji.midware.data.model.P3.DataCameraSetVideoFormat;
import dji.midware.data.model.P3.DataCameraTauAreaAxis;
import dji.midware.data.model.P3.DataCameraTauExterParamType;
import dji.midware.data.model.P3.DataCameraTauExterParams;
import dji.midware.data.model.P3.DataCameraTauFFCMode;
import dji.midware.data.model.P3.DataCameraTauParamAGC;
import dji.midware.data.model.P3.DataCameraTauParamBrightness;
import dji.midware.data.model.P3.DataCameraTauParamConstrast;
import dji.midware.data.model.P3.DataCameraTauParamDigitalInc;
import dji.midware.data.model.P3.DataCameraTauParamGainMode;
import dji.midware.data.model.P3.DataCameraTauParamIsothermEnable;
import dji.midware.data.model.P3.DataCameraTauParamIsothermUnit;
import dji.midware.data.model.P3.DataCameraTauParamIsothermValue;
import dji.midware.data.model.P3.DataCameraTauParamOptizate;
import dji.midware.data.model.P3.DataCameraTauParamROI;
import dji.midware.data.model.P3.DataCameraTauParamTempUnit;
import dji.midware.data.model.P3.DataCameraTauParamThermometric;
import dji.midware.data.model.P3.DataCameraTauParamThermometricEnable;
import dji.midware.data.model.P3.DataCameraTauParamer;
import dji.midware.data.model.P3.DataCameraTauTriggerFFC;
import dji.midware.data.model.P3.DataSingleSendAppStateForStabilization;
import dji.midware.data.model.P3.DataSpecialControl;
import dji.midware.interfaces.DJIDataCallBack;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.extension.KeyHelper;
import dji.sdksharedlib.hardware.abstractions.Action;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Getter;
import dji.sdksharedlib.hardware.abstractions.Setter;
import dji.sdksharedlib.hardware.abstractions.camera.DJICameraAbstraction;
import dji.sdksharedlib.keycatalog.CameraKeys;
import dji.sdksharedlib.keycatalog.FlightControllerKeys;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class DJICameraXTBaseAbstraction extends DJICameraAbstraction {
    private static float AREA_MULTIPLIER = 10000.0f;
    private static int EXTERNAL_SCENE_PARAMETER_MULTIPLIER = 100;
    private static final int TAU_JPEG = 1;
    private static final int TAU_RADIOMETRIC = 7;
    private static final int TAU_RADIOMETRIC_HIGH = 9;
    private static final int TAU_RADIOMETRIC_LOW = 8;
    private static final int TAU_RAW = 0;
    private static final int TAU_RAW_JPEG = 2;
    private static final int TAU_TIFF14BIT = 4;
    private static final int TAU_TIFF8BIT = 3;
    private static final int TAU_TIFF_TLINEAR_HIGH = 6;
    private static final int TAU_TIFF_TLINEAR_LOW = 5;
    private float centerTemperature;
    private DataCameraGetPushStateInfo.RecordType lastRecordVideoState = DataCameraGetPushStateInfo.RecordType.NO;
    private DataCameraGetStateInfo.PhotoState lastShootPhotoState = DataCameraGetStateInfo.PhotoState.NO;
    protected VideoRecordingState recordVideoFSM = VideoRecordingState.generate(this.index);
    protected PhotoShootingState shootPhotoFSM = PhotoShootingState.generate(this.index);

    private enum InternalState {
        Idle,
        Working
    }

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
        if (DataCameraGetPushTauParam.getInstance().isGetted()) {
            DataCameraGetPushTauParam.getInstance().swapValidData(getExpectedSenderIdByIndex());
            onEvent3BackgroundThread(DataCameraGetPushTauParam.getInstance());
        }
        if (DataCameraGetPushShotParams.getInstance().isGetted()) {
            DataCameraGetPushShotParams.getInstance().swapValidData(getExpectedSenderIdByIndex());
            onEvent3BackgroundThread(DataCameraGetPushShotParams.getInstance());
        }
    }

    /* access modifiers changed from: protected */
    public void initializeCustomizedKey() {
        super.initializeCustomizedKey();
        notifyValueChangeForKeyPath(SettingsDefinitions.ShootPhotoMode.SINGLE, convertKeyToPath(CameraKeys.SHOOT_PHOTO_MODE));
    }

    /* access modifiers changed from: protected */
    public boolean isMediaDownloadModeSupported() {
        return true;
    }

    private static class PhotoShootingState {
        private int index;
        private boolean initFlag = false;
        /* access modifiers changed from: private */
        public InternalState state;

        private PhotoShootingState() {
        }

        public static PhotoShootingState generate(int index2) {
            PhotoShootingState photoShootingState = new PhotoShootingState();
            if (!photoShootingState.initFlag) {
                photoShootingState.state = InternalState.Idle;
                photoShootingState.initFlag = true;
                photoShootingState.index = index2;
            }
            return photoShootingState;
        }

        public void setIdleToWorking() {
            if (this.state == InternalState.Idle) {
                this.state = InternalState.Working;
            }
        }

        public void setWorkingToIdle() {
            if (this.state == InternalState.Working) {
                this.state = InternalState.Idle;
            }
        }
    }

    private static class VideoRecordingState {
        private int index = 0;
        private boolean initFlag = false;
        /* access modifiers changed from: private */
        public InternalState state;

        private VideoRecordingState() {
        }

        public static VideoRecordingState generate(int index2) {
            VideoRecordingState videoRecordingState = new VideoRecordingState();
            if (!videoRecordingState.initFlag) {
                videoRecordingState.state = InternalState.Idle;
                videoRecordingState.initFlag = true;
                videoRecordingState.index = index2;
            }
            return videoRecordingState;
        }

        public void setIdleToWorking() {
            if (this.state == InternalState.Idle) {
                this.state = InternalState.Working;
            }
        }

        public void setWorkingToIdle() {
            if (this.state == InternalState.Working) {
                this.state = InternalState.Idle;
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCameraGetPushStateInfo pushStateInfo) {
        super.onEvent3BackgroundThread(pushStateInfo);
        if (isValidSender(pushStateInfo.getSenderId())) {
            checkShootPhotoStatus();
            checkRecordVideoStatus();
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCameraGetPushTauParam pushThermalCameraInfo) {
        if (pushThermalCameraInfo != null && isValidSender(pushThermalCameraInfo.getSenderId())) {
            if (pushThermalCameraInfo.getThermometricType() == DataCameraTauParamThermometricEnable.ThermometricType.SPOT) {
                this.centerTemperature = pushThermalCameraInfo.getThermometricTemp();
                notifyValueChangeForKeyPath(Float.valueOf(this.centerTemperature), convertKeyToPath(CameraKeys.THERMAL_TEMPERATURE_DATA));
            }
            SettingsDefinitions.PhotoFileFormat photoFileFormat = convertPhotoFileFormatToSDK(pushThermalCameraInfo.getImageFormat());
            SettingsDefinitions.VideoStandard videoStandard = checkVideoStandard(pushThermalCameraInfo.getVideoFormat(), pushThermalCameraInfo.getVideoFps());
            SettingsDefinitions.PhotoTimeIntervalSettings photoIntervalParam = wrapPhotoIntervalParam(pushThermalCameraInfo.getPhotoInterval());
            SettingsDefinitions.ThermalROI roi = SettingsDefinitions.ThermalROI.innerFind(pushThermalCameraInfo.getROIType().value());
            SettingsDefinitions.ThermalProfile thermalProfile = wrapThermalProfile(new SettingsDefinitions.ThermalProfile.Builder(), pushThermalCameraInfo.getLenFocusLength().value(), pushThermalCameraInfo.getVideoResolution().value(), pushThermalCameraInfo.getLenFps().value());
            int thermalDigitalZoomScale = pushThermalCameraInfo.getZoomScale();
            int thermalIsothermLowerValue = pushThermalCameraInfo.getIsothermLower();
            int thermalIsothermMiddleValue = pushThermalCameraInfo.getIsothermMiddle();
            int thermalIsothermUpperValue = pushThermalCameraInfo.getIsothermUpper();
            SettingsDefinitions.ThermalIsothermUnit isothermUnit = SettingsDefinitions.ThermalIsothermUnit.values()[pushThermalCameraInfo.getIsothermUnit()];
            boolean isothermEnabled = pushThermalCameraInfo.isIsothermEnable();
            SettingsDefinitions.ThermalScene thermalScene = SettingsDefinitions.ThermalScene.innerFind(pushThermalCameraInfo.getAGC().value());
            SettingsDefinitions.ThermalPalette thermalPalette = SettingsDefinitions.ThermalPalette.innerFind(pushThermalCameraInfo.getDigitalFilter());
            SettingsDefinitions.ThermalGainMode gainMode = SettingsDefinitions.ThermalGainMode.values()[pushThermalCameraInfo.getGainMode().value()];
            int photoFormat = pushThermalCameraInfo.getImageFormat();
            boolean supportSpotThermometric = pushThermalCameraInfo.supportSpotThermometric();
            notifyValueChangeForKeyPath(ThermalMeasurementMode.find(pushThermalCameraInfo.getThermometricType().value()), CameraKeys.THERMAL_MEASUREMENT_MODE);
            notifyValueChangeForKeyPath(new TauVideoFormat(pushThermalCameraInfo.getVideoFormat(), pushThermalCameraInfo.getVideoFps()), CameraKeys.THERMAL_VIDEO_FORMAT);
            notifyValueChangeForKeyPath(DJICameraEnumMappingUtil.wrapResolutionAndFrameRate(pushThermalCameraInfo.getVideoFormat(), pushThermalCameraInfo.getVideoFps()), convertKeyToPath(CameraKeys.RESOLUTION_FRAME_RATE));
            notifyValueChangeForKeyPath(Boolean.valueOf(supportSpotThermometric), CameraKeys.SUPPORT_SPOT_THERMOMETRIC);
            notifyValueChangeForKeyPath(Integer.valueOf(photoFormat), CameraKeys.PHOTO_FORMAT);
            notifyValueChangeForKeyPath(SettingsDefinitions.TemperatureUnit.find(pushThermalCameraInfo.getTemperatureMode()), convertKeyToPath(CameraKeys.TEMPERATURE_UNIT));
            notifyValueChangeForKeyPath(photoFileFormat, convertKeyToPath(CameraKeys.PHOTO_FILE_FORMAT));
            notifyValueChangeForKeyPath(videoStandard, convertKeyToPath(CameraKeys.VIDEO_STANDARD));
            if (!(this instanceof DJICameraTauXT2Abstraction)) {
                notifyValueChangeForKeyPath(photoIntervalParam, convertKeyToPath(CameraKeys.PHOTO_TIME_INTERVAL_SETTINGS));
            }
            notifyValueChangeForKeyPath(roi, convertKeyToPath(CameraKeys.THERMAL_ROI));
            notifyValueChangeForKeyPath(thermalProfile, convertKeyToPath(CameraKeys.THERMAL_PROFILE));
            notifyValueChangeForKeyPath(checkDigitalZoomScale(thermalDigitalZoomScale), convertKeyToPath(CameraKeys.THERMAL_DIGITAL_ZOOM_FACTOR));
            notifyValueChangeForKeyPath(Integer.valueOf(thermalIsothermLowerValue), convertKeyToPath(CameraKeys.THERMAL_ISOTHERM_LOWER_VALUE));
            notifyValueChangeForKeyPath(Integer.valueOf(thermalIsothermMiddleValue), convertKeyToPath(CameraKeys.THERMAL_ISOTHERM_MIDDLE_VALUE));
            notifyValueChangeForKeyPath(Integer.valueOf(thermalIsothermUpperValue), convertKeyToPath(CameraKeys.THERMAL_ISOTHERM_UPPER_VALUE));
            notifyValueChangeForKeyPath(isothermUnit, convertKeyToPath(CameraKeys.THERMAL_ISOTHERM_UNIT));
            notifyValueChangeForKeyPath(Boolean.valueOf(isothermEnabled), convertKeyToPath(CameraKeys.THERMAL_ISOTHERM_ENABLED));
            notifyValueChangeForKeyPath(thermalScene, convertKeyToPath(CameraKeys.THERMAL_SCENE));
            notifyValueChangeForKeyPath(thermalPalette, convertKeyToPath(CameraKeys.THERMAL_PALETTE));
            notifyValueChangeForKeyPath(gainMode, convertKeyToPath(CameraKeys.THERMAL_GAIN_MODE));
            if (isThermalFFCModeSupported()) {
                notifyValueChangeForKeyPath(SettingsDefinitions.ThermalFFCMode.find(pushThermalCameraInfo.getFFCMode().value()), convertKeyToPath(CameraKeys.THERMAL_FFC_MODE));
            }
            if (isOverallTemperatureMeterSupported()) {
                notifyValueChangeForKeyPath(Boolean.valueOf(isOverallTemperatureMeterSupported()), convertKeyToPath(CameraKeys.THERMAL_IS_OVERALL_TEMPERATURE_METER_SUPPORTED));
                notifyValueChangeForKeyPath(new PointF(pushThermalCameraInfo.getThermometricXAxis(), pushThermalCameraInfo.getThermometricYAxis()), convertKeyToPath(CameraKeys.THERMAL_SPOT_METERING_TARGET_POINT));
                notifyValueChangeForKeyPath(new RectF(((float) pushThermalCameraInfo.getAreaThermometricLeft()) / AREA_MULTIPLIER, ((float) pushThermalCameraInfo.getAreaThermometricTop()) / AREA_MULTIPLIER, ((float) pushThermalCameraInfo.getAreaThermometricRight()) / AREA_MULTIPLIER, ((float) pushThermalCameraInfo.getAreaThermometricBottom()) / AREA_MULTIPLIER), convertKeyToPath(CameraKeys.THERMAL_METERING_AREA));
                float averageTemp = pushThermalCameraInfo.getAreaThermometricAverage();
                float minTemp = pushThermalCameraInfo.getAreaThermometricMin();
                int minTempXPos = pushThermalCameraInfo.getAreaThermometricMinX();
                int minTempYPos = pushThermalCameraInfo.getAreaThermometricMinY();
                notifyValueChangeForKeyPath(new ThermalAreaTemperatureAggregations(averageTemp, minTemp, new Point(minTempXPos, minTempYPos), pushThermalCameraInfo.getAreaThermometricMax(), new Point(pushThermalCameraInfo.getAreaThermometricMaxX(), pushThermalCameraInfo.getAreaThermometricMaxY())), convertKeyToPath(CameraKeys.THERMAL_AREA_TEMPERATURE_AGGREGATIONS));
                notifyValueChangeForKeyPath(SettingsDefinitions.ThermalCustomExternalSceneSettingsProfile.find(pushThermalCameraInfo.getExterParamType().value()), CameraKeys.THERMAL_CUSTOM_EXTERNAL_SCENE_SETTINGS_PROFILE);
                notifyValueChangeForKeyPath(Float.valueOf(((float) pushThermalCameraInfo.getAtmosphereTemperature()) / 100.0f), CameraKeys.THERMAL_ATMOSPHERIC_TEMPERATURE);
                notifyValueChangeForKeyPath(Float.valueOf(((float) pushThermalCameraInfo.getAtmosphereTransmission()) / 100.0f), CameraKeys.THERMAL_ATMOSPHERIC_TRANSMISSION_COEFFICIENT);
                notifyValueChangeForKeyPath(Float.valueOf(((float) pushThermalCameraInfo.getBackgroundTemperature()) / 100.0f), CameraKeys.THERMAL_BACKGROUND_TEMPERATURE);
                notifyValueChangeForKeyPath(Float.valueOf(((float) pushThermalCameraInfo.getTargetEmissivity()) / 100.0f), CameraKeys.THERMAL_SCENE_EMISSIVITY);
                notifyValueChangeForKeyPath(Float.valueOf(((float) pushThermalCameraInfo.getWindowReflection()) / 100.0f), CameraKeys.THERMAL_WINDOW_REFLECTION);
                notifyValueChangeForKeyPath(Float.valueOf(((float) pushThermalCameraInfo.getWindowReflectedTemperature()) / 100.0f), CameraKeys.THERMAL_WINDOW_REFLECTED_TEMPERATURE);
                notifyValueChangeForKeyPath(Float.valueOf(((float) pushThermalCameraInfo.getWindowTemperature()) / 100.0f), CameraKeys.THERMAL_WINDOW_TEMPERATURE);
                notifyValueChangeForKeyPath(Float.valueOf(((float) pushThermalCameraInfo.getWindowTransmission()) / 100.0f), CameraKeys.THERMAL_WINDOW_TRANSMISSION_COEFFICIENT);
            }
        }
    }

    public boolean isThermalImagingCamera() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isOverallTemperatureMeterSupported() {
        return DataCameraGetPushTauParam.getInstance().supportSpotThermometric(getExpectedSenderIdByIndex());
    }

    /* access modifiers changed from: protected */
    public boolean isThermalFFCModeSupported() {
        return DataCameraGetPushStateInfo.getInstance().getCameraProtocolType(getExpectedSenderIdByIndex()).value() >= 2 && (DataCameraGetPushStateInfo.getInstance().getCameraType(getExpectedSenderIdByIndex()) == DataCameraGetPushStateInfo.CameraType.DJICameraTypeTau336 || DataCameraGetPushStateInfo.getInstance().getCameraType(getExpectedSenderIdByIndex()) == DataCameraGetPushStateInfo.CameraType.DJICameraTypeTau640 || DataCameraGetPushStateInfo.getInstance().getCameraType(getExpectedSenderIdByIndex()) == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC1705);
    }

    private boolean isThermometricValid() {
        return DataCameraGetPushTauParam.getInstance().isThermometricValid(getExpectedSenderIdByIndex());
    }

    /* access modifiers changed from: protected */
    public boolean isMediaDownModeMapValue2() {
        return true;
    }

    @Setter("Mode")
    public void setCameraMode(SettingsDefinitions.CameraMode mode, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (mode == SettingsDefinitions.CameraMode.UNKNOWN || mode == SettingsDefinitions.CameraMode.BROADCAST) {
            if (callback != null) {
                callback.onFails(DJICameraError.COMMON_PARAM_ILLEGAL);
            }
        } else if (SettingsDefinitions.CameraMode.PLAYBACK != mode || isPlaybackSupported()) {
            if (SettingsDefinitions.CameraMode.MEDIA_DOWNLOAD == mode) {
                mode = SettingsDefinitions.CameraMode.PLAYBACK;
            }
            int value = mode.value();
            DataCameraSetMode setter = DataCameraSetMode.getInstance();
            setter.setReceiverId(getReceiverIdByIndex());
            setter.setMode(DataCameraGetMode.MODE.find(value));
            setter.start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraXTBaseAbstraction.AnonymousClass1 */

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
        } else if (callback != null) {
            callback.onFails(DJICameraError.COMMON_UNSUPPORTED);
        }
    }

    @Setter(CameraKeys.PHOTO_FILE_FORMAT)
    public void setPhotoFileFormat(SettingsDefinitions.PhotoFileFormat photoFormat, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (checkCapabilityForPhotoFileFormat(photoFormat)) {
            int imageFormat = convertPhotoFileFormatToProtocol(photoFormat);
            DataBaseCameraSetting setter = new DataBaseCameraSetting();
            setter.setCmdId("ImageFormat");
            setter.setValue(imageFormat);
            setter.setReceiverId(getReceiverIdByIndex());
            setter.start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraXTBaseAbstraction.AnonymousClass2 */

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
        } else if (callback != null) {
            callback.onFails(DJICameraError.INVALID_PARAMETERS);
        }
    }

    @Setter(CameraKeys.VIDEO_STANDARD)
    public void setVideoStandard(SettingsDefinitions.VideoStandard videoStandard, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        int ratio;
        int fps;
        if (!ServiceManager.getInstance().isConnected()) {
            if (callback != null) {
                callback.onFails(DJICameraError.NOT_CONNECTED);
            }
        } else if (videoStandard != SettingsDefinitions.VideoStandard.UNKNOWN) {
            if (SettingsDefinitions.VideoStandard.NTSC == videoStandard) {
                ratio = 0;
                fps = 3;
            } else if (SettingsDefinitions.VideoStandard.PAL == videoStandard) {
                ratio = 26;
                fps = 2;
            } else {
                callback.onFails(DJICameraError.COMMON_PARAM_ILLEGAL);
                return;
            }
            DataCameraSetVideoFormat setter = new DataCameraSetVideoFormat();
            setter.setReceiverId(getReceiverIdByIndex());
            setter.setAll();
            setter.setRatio(ratio);
            setter.setFps(fps);
            setter.setFov(0);
            setter.start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraXTBaseAbstraction.AnonymousClass3 */

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
        } else if (callback != null) {
            callback.onFails(DJICameraError.COMMON_PARAM_ILLEGAL);
        }
    }

    @Setter(CameraKeys.PHOTO_TIME_INTERVAL_SETTINGS)
    public void setPhotoTimeIntervalSettings(SettingsDefinitions.PhotoTimeIntervalSettings param, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (param == null || callback == null) {
            if (callback != null) {
                callback.onFails(DJICameraError.COMMON_PARAM_ILLEGAL);
            }
        } else if (param.getCaptureCount() != 255) {
            callback.onFails(DJICameraError.COMMON_PARAM_ILLEGAL);
        } else if (param.getTimeIntervalInSeconds() < 1 || param.getTimeIntervalInSeconds() > 60) {
            callback.onFails(DJICameraError.COMMON_PARAM_ILLEGAL);
        } else {
            ((DataCameraSetTimeParams) DataCameraSetTimeParams.getInstance().setNum(255).setPeriod(param.getTimeIntervalInSeconds()).setType(DataCameraSetTimeParams.TYPE.Single).setReceiverId(getReceiverIdByIndex(), DataCameraSetTimeParams.class)).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraXTBaseAbstraction.AnonymousClass4 */

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
    }

    @Action(CameraKeys.RESTORE_FACTORY_SETTINGS)
    public void loadFactorySettings(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataCameraLoadParams setter = DataCameraLoadParams.getInstance();
        setter.setMode(DataCameraSaveParams.USER.DEFAULT);
        setter.setReceiverId(getReceiverIdByIndex());
        setter.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraXTBaseAbstraction.AnonymousClass5 */

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

    @Setter(CameraKeys.THERMAL_CONTRAST)
    public void setThermalContrast(int value, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (DataCameraGetPushTauParam.getInstance().getAGC(getExpectedSenderIdByIndex()).value() != 5) {
            if (callback != null) {
                callback.onFails(DJICameraError.UNSUPPORTED_CMD_STATE);
            }
        } else if (value >= 0 && value <= 255) {
            DataBaseCameraSetting setter = new DataBaseCameraSetting();
            setter.setReceiverId(getReceiverIdByIndex());
            setter.setCmdId(CmdIdCamera.CmdIdType.SetContrast).setValue(value);
            setter.start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraXTBaseAbstraction.AnonymousClass6 */

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
        } else if (callback != null) {
            callback.onFails(DJICameraError.INVALID_PARAMETERS);
        }
    }

    @Getter(CameraKeys.THERMAL_CONTRAST)
    public void getThermalContrast(DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            if (DataCameraGetPushTauParam.getInstance().getAGC(getExpectedSenderIdByIndex()).value() < 5 || DataCameraGetPushTauParam.getInstance().getAGC(getExpectedSenderIdByIndex()).value() > 8) {
                callback.onFails(DJICameraError.UNSUPPORTED_CMD_STATE);
            } else {
                callback.onSuccess(Integer.valueOf(DataCameraGetPushTauParam.getInstance().getContrast(getExpectedSenderIdByIndex())));
            }
        }
    }

    @Setter(CameraKeys.THERMAL_ROI)
    public void setThermalROI(SettingsDefinitions.ThermalROI roi, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (roi == SettingsDefinitions.ThermalROI.UNKNOWN) {
            CallbackUtils.onFailure(callback, DJICameraError.INVALID_PARAMETERS);
            return;
        }
        DataCameraTauParamROI setter = new DataCameraTauParamROI();
        setter.setReceiverId(getReceiverIdByIndex());
        setter.setType(DataCameraTauParamROI.ROIType.find(roi.getInnerValue())).setOpt(false);
        setter.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraXTBaseAbstraction.AnonymousClass7 */

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

    @Setter(CameraKeys.THERMAL_PALETTE)
    public void setThermalPalette(SettingsDefinitions.ThermalPalette palette, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        SettingsDefinitions.ThermalPalette[] range = (SettingsDefinitions.ThermalPalette[]) CacheHelper.getCamera(this.index, CameraKeys.THERMAL_PALETTE_RANGE);
        boolean isValid = false;
        if (range != null) {
            int length = range.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                } else if (range[i] == palette) {
                    isValid = true;
                    break;
                } else {
                    i++;
                }
            }
        } else {
            isValid = true;
        }
        if (!isValid) {
            CallbackUtils.onFailure(callback, DJICameraError.INVALID_PARAMETERS);
            return;
        }
        DataBaseCameraSetting setter = new DataBaseCameraSetting();
        setter.setReceiverId(getReceiverIdByIndex());
        setter.setCmdId(CmdIdCamera.CmdIdType.SetDigitalFilter).setValue(palette.getInnerValue());
        setter.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraXTBaseAbstraction.AnonymousClass8 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, (Object) null);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    @Setter(CameraKeys.THERMAL_SCENE)
    public void setThermalScene(SettingsDefinitions.ThermalScene scene, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (scene == SettingsDefinitions.ThermalScene.UNKNOWN) {
            callback.onFails(DJICameraError.INVALID_PARAMETERS);
            return;
        }
        int sceneValue = scene.getInnerValue();
        DataCameraTauParamAGC setter = new DataCameraTauParamAGC();
        setter.setType(DataCameraTauParamAGC.AGCType.find(sceneValue)).setOpt(false);
        setter.setReceiverId(getReceiverIdByIndex());
        setter.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraXTBaseAbstraction.AnonymousClass9 */

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

    @Setter(CameraKeys.THERMAL_DDE)
    public void setThermalDDE(int value, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (DataCameraGetPushTauParam.getInstance().getAGC(getExpectedSenderIdByIndex()).value() < 5 || DataCameraGetPushTauParam.getInstance().getAGC(getExpectedSenderIdByIndex()).value() > 8) {
            callback.onFails(DJICameraError.UNSUPPORTED_CMD_STATE);
        } else if (value < -20 || value > 100) {
            callback.onFails(DJICameraError.INVALID_PARAMETERS);
        } else {
            DataCameraTauParamDigitalInc setter = new DataCameraTauParamDigitalInc();
            setter.setValue(value).setOpt(false);
            setter.setReceiverId(getReceiverIdByIndex());
            setter.start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraXTBaseAbstraction.AnonymousClass10 */

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
    }

    @Getter(CameraKeys.THERMAL_DDE)
    public void getThermalDDE(DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            if (DataCameraGetPushTauParam.getInstance().getAGC(getExpectedSenderIdByIndex()).value() < 5 || DataCameraGetPushTauParam.getInstance().getAGC(getExpectedSenderIdByIndex()).value() > 8) {
                callback.onFails(DJICameraError.UNSUPPORTED_CMD_STATE);
            } else {
                callback.onSuccess(Integer.valueOf(DataCameraGetPushTauParam.getInstance().getDDE()));
            }
        }
    }

    @Setter(CameraKeys.THERMAL_ACE)
    public void setThermalACE(int value, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (DataCameraGetPushTauParam.getInstance().getAGC(getExpectedSenderIdByIndex()).value() < 6 || DataCameraGetPushTauParam.getInstance().getAGC(getExpectedSenderIdByIndex()).value() > 8) {
            callback.onFails(DJICameraError.UNSUPPORTED_CMD_STATE);
        } else if (value < -8 || value > 8) {
            callback.onFails(DJICameraError.INVALID_PARAMETERS);
        } else {
            DataCameraTauParamConstrast setter = new DataCameraTauParamConstrast();
            setter.setValue(value).setOpt(false);
            setter.setReceiverId(getReceiverIdByIndex());
            setter.start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraXTBaseAbstraction.AnonymousClass11 */

                public void onSuccess(Object model) {
                    callback.onSuccess(null);
                }

                public void onFailure(Ccode ccode) {
                    callback.onFails(DJICameraError.getDJIError(ccode));
                }
            });
        }
    }

    @Getter(CameraKeys.THERMAL_ACE)
    public void getThermalACE(DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            if (DataCameraGetPushTauParam.getInstance().getAGC(getExpectedSenderIdByIndex()).value() < 5 || DataCameraGetPushTauParam.getInstance().getAGC(getExpectedSenderIdByIndex()).value() > 8) {
                callback.onFails(DJICameraError.UNSUPPORTED_CMD_STATE);
            } else if (callback != null) {
                callback.onSuccess(Integer.valueOf(DataCameraGetPushTauParam.getInstance().getACE(getExpectedSenderIdByIndex())));
            }
        }
    }

    @Setter(CameraKeys.THERMAL_SSO)
    public void setThermalSSO(int value, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (DataCameraGetPushTauParam.getInstance().getAGC(getExpectedSenderIdByIndex()).value() < 6 || DataCameraGetPushTauParam.getInstance().getAGC(getExpectedSenderIdByIndex()).value() > 8) {
            callback.onFails(DJICameraError.UNSUPPORTED_CMD_STATE);
        } else if (value < 0 || value > 100) {
            callback.onFails(DJICameraError.INVALID_PARAMETERS);
        } else {
            DataCameraTauParamOptizate setter = new DataCameraTauParamOptizate();
            setter.setValue(value).setOpt(false);
            setter.setReceiverId(getReceiverIdByIndex());
            setter.start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraXTBaseAbstraction.AnonymousClass12 */

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
    }

    @Getter(CameraKeys.THERMAL_SSO)
    public void getThermalSSO(DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            if (DataCameraGetPushTauParam.getInstance().getAGC(getExpectedSenderIdByIndex()).value() < 5 || DataCameraGetPushTauParam.getInstance().getAGC(getExpectedSenderIdByIndex()).value() > 8) {
                callback.onFails(DJICameraError.UNSUPPORTED_CMD_STATE);
            } else {
                callback.onSuccess(Integer.valueOf(DataCameraGetPushTauParam.getInstance().getSSO(getExpectedSenderIdByIndex())));
            }
        }
    }

    @Setter(CameraKeys.THERMAL_BRIGHTNESS)
    public void setThermalBrightness(int value, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (DataCameraGetPushTauParam.getInstance().getAGC(getExpectedSenderIdByIndex()).value() != 5) {
            callback.onFails(DJICameraError.UNSUPPORTED_CMD_STATE);
        } else if (value < 0 || value > 16383) {
            callback.onFails(DJICameraError.INVALID_PARAMETERS);
        } else {
            DataCameraTauParamBrightness setter = new DataCameraTauParamBrightness();
            setter.setValue(value).setOpt(false);
            setter.setReceiverId(getReceiverIdByIndex());
            setter.start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraXTBaseAbstraction.AnonymousClass13 */

                public void onSuccess(Object model) {
                    callback.onSuccess(null);
                }

                public void onFailure(Ccode ccode) {
                    callback.onFails(DJICameraError.getDJIError(ccode));
                }
            });
        }
    }

    @Getter(CameraKeys.THERMAL_BRIGHTNESS)
    public void getThermalBrightness(DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (DataCameraGetPushTauParam.getInstance().getAGC(getExpectedSenderIdByIndex()).value() < 5 || DataCameraGetPushTauParam.getInstance().getAGC(getExpectedSenderIdByIndex()).value() > 8) {
            callback.onFails(DJICameraError.UNSUPPORTED_CMD_STATE);
        } else if (callback != null) {
            callback.onSuccess(Integer.valueOf(DataCameraGetPushTauParam.getInstance().getBrightness(getExpectedSenderIdByIndex())));
        }
    }

    @Setter(CameraKeys.THERMAL_ISOTHERM_ENABLED)
    public void setThermalIsothermEnabled(boolean enabled, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataCameraTauParamIsothermEnable setter = new DataCameraTauParamIsothermEnable();
        setter.setEnable(enabled).setOpt(false);
        setter.setReceiverId(getReceiverIdByIndex());
        setter.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraXTBaseAbstraction.AnonymousClass14 */

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

    @Setter(CameraKeys.THERMAL_ISOTHERM_UNIT)
    public void setThermalIsothermUnit(SettingsDefinitions.ThermalIsothermUnit unit, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (unit == SettingsDefinitions.ThermalIsothermUnit.UNKNOWN) {
            callback.onFails(DJICameraError.INVALID_PARAMETERS);
            return;
        }
        DataCameraTauParamIsothermUnit setter = new DataCameraTauParamIsothermUnit();
        setter.setReceiverId(getReceiverIdByIndex());
        setter.setUnit(unit.value()).setOpt(false);
        setter.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraXTBaseAbstraction.AnonymousClass15 */

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

    @Setter(CameraKeys.THERMAL_ISOTHERM_UPPER_VALUE)
    public void setThermalIsothermUpperValue(int value, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (DataCameraGetPushTauParam.getInstance().getIsothermUnit(getExpectedSenderIdByIndex()) == 0 && (value < 0 || value > 100)) {
            callback.onFails(DJICameraError.INVALID_PARAMETERS);
        } else if (DataCameraGetPushTauParam.getInstance().getIsothermUnit(getExpectedSenderIdByIndex()) == 1 && (value < -40 || value > 1000)) {
            callback.onFails(DJICameraError.INVALID_PARAMETERS);
        } else if (value < DataCameraGetPushTauParam.getInstance().getIsothermMiddle(getExpectedSenderIdByIndex())) {
            callback.onFails(DJICameraError.INVALID_PARAMETERS);
        } else {
            DataCameraTauParamIsothermValue setter = new DataCameraTauParamIsothermValue();
            setter.setType(DataCameraTauParamer.ParamCmd.ISOTHERM_UPPER).setValue((short) value).setOpt(false);
            setter.setReceiverId(getReceiverIdByIndex());
            setter.start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraXTBaseAbstraction.AnonymousClass16 */

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
    }

    @Setter(CameraKeys.THERMAL_ISOTHERM_MIDDLE_VALUE)
    public void setThermalIsothermMiddleValue(int value, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (DataCameraGetPushTauParam.getInstance().getIsothermUnit(getExpectedSenderIdByIndex()) == 0 && (value < 0 || value > 100)) {
            callback.onFails(DJICameraError.INVALID_PARAMETERS);
        } else if (DataCameraGetPushTauParam.getInstance().getIsothermUnit(getExpectedSenderIdByIndex()) == 1 && (value < -40 || value > 1000)) {
            callback.onFails(DJICameraError.INVALID_PARAMETERS);
        } else if (value < DataCameraGetPushTauParam.getInstance().getIsothermLower(getExpectedSenderIdByIndex()) || value > DataCameraGetPushTauParam.getInstance().getIsothermUpper(getExpectedSenderIdByIndex())) {
            callback.onFails(DJICameraError.INVALID_PARAMETERS);
        } else {
            DataCameraTauParamIsothermValue setter = new DataCameraTauParamIsothermValue();
            setter.setType(DataCameraTauParamer.ParamCmd.ISOTHERM_MIDDLE).setValue((short) value).setOpt(false);
            setter.setReceiverId(getReceiverIdByIndex());
            setter.start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraXTBaseAbstraction.AnonymousClass17 */

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
    }

    @Setter(CameraKeys.THERMAL_ISOTHERM_LOWER_VALUE)
    public void setThermalIsothermLowerValue(int value, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (DataCameraGetPushTauParam.getInstance().getIsothermUnit(getExpectedSenderIdByIndex()) == 0 && (value < 0 || value > 100)) {
            callback.onFails(DJICameraError.INVALID_PARAMETERS);
        } else if (DataCameraGetPushTauParam.getInstance().getIsothermUnit(getExpectedSenderIdByIndex()) == 1 && (value < -40 || value > 1000)) {
            callback.onFails(DJICameraError.INVALID_PARAMETERS);
        } else if (value > DataCameraGetPushTauParam.getInstance().getIsothermMiddle(getExpectedSenderIdByIndex())) {
            callback.onFails(DJICameraError.INVALID_PARAMETERS);
        } else {
            DataCameraTauParamIsothermValue setter = new DataCameraTauParamIsothermValue();
            setter.setType(DataCameraTauParamer.ParamCmd.ISOTHERM_LOWER).setValue((short) value).setOpt(false);
            setter.setReceiverId(getReceiverIdByIndex());
            setter.start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraXTBaseAbstraction.AnonymousClass18 */

                public void onSuccess(Object model) {
                    callback.onSuccess(null);
                }

                public void onFailure(Ccode ccode) {
                    callback.onFails(DJICameraError.getDJIError(ccode));
                }
            });
        }
    }

    @Setter(CameraKeys.THERMAL_DIGITAL_ZOOM_FACTOR)
    public void setThermalDigitalZoomScale(SettingsDefinitions.ThermalDigitalZoomFactor scale, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (scale == SettingsDefinitions.ThermalDigitalZoomFactor.UNKNOWN) {
            if (callback != null) {
                callback.onFails(DJICameraError.INVALID_PARAMETERS);
            }
        } else if (!isTau336Camera() || scale != SettingsDefinitions.ThermalDigitalZoomFactor.X_8) {
            int scaleValue = -1;
            switch (scale) {
                case X_1:
                    scaleValue = 1;
                    break;
                case X_2:
                    scaleValue = 2;
                    break;
                case X_4:
                    scaleValue = 4;
                    break;
                case X_8:
                    scaleValue = 8;
                    break;
            }
            if (scaleValue != -1) {
                checkIfNeedWaitForPauseTheStabilization(callback, DataSingleSendAppStateForStabilization.CameraState.ZOOM_IN);
                DataCameraSetFocusParam setter = DataCameraSetFocusParam.getInstance();
                setter.setDigitalZoom(true).setDigitalMode(DataCameraSetFocusParam.ZoomMode.POSITION).setDigitalPosScale((float) scaleValue);
                setter.setReceiverId(getReceiverIdByIndex());
                setter.start(new DJIDataCallBack() {
                    /* class dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraXTBaseAbstraction.AnonymousClass19 */

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
                return;
            }
            callback.onFails(DJICameraError.INVALID_PARAMETERS);
        } else if (callback != null) {
            callback.onFails(DJICameraError.INVALID_PARAMETERS);
        }
    }

    @Setter(CameraKeys.THERMAL_GAIN_MODE)
    public void setThermalGainMode(SettingsDefinitions.ThermalGainMode gainMode, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (gainMode == SettingsDefinitions.ThermalGainMode.UNKNOWN) {
            callback.onFails(DJICameraError.INVALID_PARAMETERS);
            return;
        }
        DataCameraTauParamGainMode setter = new DataCameraTauParamGainMode();
        setter.setReceiverId(getReceiverIdByIndex());
        setter.setMode(DataCameraTauParamGainMode.GainMode.find(gainMode.value()));
        setter.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraXTBaseAbstraction.AnonymousClass20 */

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

    @Setter(CameraKeys.THERMAL_FFC_MODE)
    public void setThermalFFCMode(SettingsDefinitions.ThermalFFCMode ffcMode, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (isThermalFFCModeSupported()) {
            if (ffcMode != SettingsDefinitions.ThermalFFCMode.UNKNOWN) {
                DataCameraTauFFCMode setter = new DataCameraTauFFCMode();
                setter.setReceiverId(getReceiverIdByIndex());
                setter.setMode(DataCameraTauFFCMode.FFCMode.find(ffcMode.value()));
                setter.start(new DJIDataCallBack() {
                    /* class dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraXTBaseAbstraction.AnonymousClass21 */

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
            } else if (callback != null) {
                callback.onFails(DJICameraError.INVALID_PARAMETERS);
            }
        } else if (callback != null) {
            callback.onFails(DJIError.COMMON_UNSUPPORTED);
        }
    }

    @Action(CameraKeys.TRIGGER_THERMAL_FFC)
    public void triggerFFC(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (isThermalFFCModeSupported()) {
            DataCameraTauTriggerFFC action = new DataCameraTauTriggerFFC();
            action.setReceiverId(getReceiverIdByIndex());
            action.start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraXTBaseAbstraction.AnonymousClass22 */

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
        } else if (callback != null) {
            callback.onFails(DJIError.COMMON_UNSUPPORTED);
        }
    }

    @Setter(CameraKeys.THERMAL_SPOT_METERING_TARGET_POINT)
    public void setThermalSpotMeteringTargetPoint(PointF thermalSpotMeteringTargetPoint, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!isOverallTemperatureMeterSupported()) {
            callback.onFails(DJIError.COMMON_UNSUPPORTED);
        } else if (thermalSpotMeteringTargetPoint.x < 0.0f || thermalSpotMeteringTargetPoint.x > 1.0f || thermalSpotMeteringTargetPoint.y < 0.0f || thermalSpotMeteringTargetPoint.y > 1.0f) {
            callback.onFails(DJIError.COMMON_PARAM_INVALID);
        } else {
            DataCameraTauParamThermometric setter = new DataCameraTauParamThermometric();
            setter.setReceiverId(getReceiverIdByIndex());
            setter.setValue(thermalSpotMeteringTargetPoint.x, thermalSpotMeteringTargetPoint.y);
            setter.start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraXTBaseAbstraction.AnonymousClass23 */

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
    }

    @Setter(CameraKeys.THERMAL_MEASUREMENT_MODE)
    public void setThermalMeasurementMode(ThermalMeasurementMode mode, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (mode == null || mode == ThermalMeasurementMode.UNKNOWN) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_INVALID);
        } else {
            ((DataCameraTauParamThermometricEnable) new DataCameraTauParamThermometricEnable().setType(DataCameraTauParamThermometricEnable.ThermometricType.find(mode.value())).setReceiverId(getReceiverIdByIndex(), DataCameraTauParamThermometricEnable.class)).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraXTBaseAbstraction.AnonymousClass24 */

                public void onSuccess(Object model) {
                    CallbackUtils.onSuccess(callback, (Object) null);
                }

                public void onFailure(Ccode ccode) {
                    CallbackUtils.onFailure(callback, ccode);
                }
            });
        }
    }

    @Setter(CameraKeys.THERMAL_METERING_AREA)
    public void setThermalSpotMeteringArea(RectF thermalSpotMeteringArea, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (isOverallTemperatureMeterSupported()) {
            if (thermalSpotMeteringArea.left < 0.0f || thermalSpotMeteringArea.left > 1.0f || thermalSpotMeteringArea.top < 0.0f || thermalSpotMeteringArea.top > 1.0f || thermalSpotMeteringArea.right < 0.0f || thermalSpotMeteringArea.right > 1.0f || thermalSpotMeteringArea.bottom < 0.0f || thermalSpotMeteringArea.bottom > 1.0f) {
                callback.onFails(DJIError.COMMON_PARAM_INVALID);
                return;
            }
            DataCameraTauAreaAxis setter = new DataCameraTauAreaAxis();
            setter.setReceiverId(getReceiverIdByIndex());
            setter.setAxis((short) ((int) (thermalSpotMeteringArea.left * AREA_MULTIPLIER)), (short) ((int) (thermalSpotMeteringArea.top * AREA_MULTIPLIER)), (short) ((int) (thermalSpotMeteringArea.right * AREA_MULTIPLIER)), (short) ((int) (thermalSpotMeteringArea.bottom * AREA_MULTIPLIER)));
            setter.start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraXTBaseAbstraction.AnonymousClass25 */

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
        } else if (callback != null) {
            callback.onFails(DJIError.COMMON_UNSUPPORTED);
        }
    }

    @Setter(CameraKeys.THERMAL_CUSTOM_EXTERNAL_SCENE_SETTINGS_PROFILE)
    public void setThermalCustomExternalSceneSettingsProfile(SettingsDefinitions.ThermalCustomExternalSceneSettingsProfile profile, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (isOverallTemperatureMeterSupported()) {
            if (profile != SettingsDefinitions.ThermalCustomExternalSceneSettingsProfile.UNKNOWN) {
                DataCameraTauExterParamType setter = new DataCameraTauExterParamType();
                setter.setReceiverId(getReceiverIdByIndex());
                setter.setType(DataCameraTauExterParamType.ExterParamType.find(profile.value()));
                setter.start(new DJIDataCallBack() {
                    /* class dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraXTBaseAbstraction.AnonymousClass26 */

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
            } else if (callback != null) {
                callback.onFails(DJICameraError.INVALID_PARAMETERS);
            }
        } else if (callback != null) {
            callback.onFails(DJIError.COMMON_UNSUPPORTED);
        }
    }

    @Setter(CameraKeys.THERMAL_ATMOSPHERIC_TEMPERATURE)
    public void setThermalAtmosphericTemperature(float value, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (isOverallTemperatureMeterSupported()) {
            short protocolAtmosphericTemperature = (short) ((int) (((float) EXTERNAL_SCENE_PARAMETER_MULTIPLIER) * value));
            if (protocolAtmosphericTemperature >= -5000) {
                DataCameraTauExterParams setter = new DataCameraTauExterParams();
                setter.setReceiverId(getReceiverIdByIndex());
                setter.addParam(DataCameraTauExterParams.ExterParamId.ATMOSPHERE_TEMP, protocolAtmosphericTemperature);
                setter.start(new DJIDataCallBack() {
                    /* class dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraXTBaseAbstraction.AnonymousClass27 */

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
            } else if (callback != null) {
                callback.onFails(DJIError.COMMON_PARAM_INVALID);
            }
        } else if (callback != null) {
            callback.onFails(DJIError.COMMON_UNSUPPORTED);
        }
    }

    @Setter(CameraKeys.THERMAL_ATMOSPHERIC_TRANSMISSION_COEFFICIENT)
    public void setThermalAtmosphericTransmissionCoefficient(float value, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (isOverallTemperatureMeterSupported()) {
            short protocolValue = (short) ((int) (((float) EXTERNAL_SCENE_PARAMETER_MULTIPLIER) * value));
            if (protocolValue >= 5000 && protocolValue <= 10000) {
                DataCameraTauExterParams setter = new DataCameraTauExterParams();
                setter.setReceiverId(getReceiverIdByIndex());
                setter.addParam(DataCameraTauExterParams.ExterParamId.ATMOSPHERE_TRANSMISSION, protocolValue);
                setter.start(new DJIDataCallBack() {
                    /* class dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraXTBaseAbstraction.AnonymousClass28 */

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
            } else if (callback != null) {
                callback.onFails(DJIError.COMMON_PARAM_INVALID);
            }
        } else if (callback != null) {
            callback.onFails(DJIError.COMMON_UNSUPPORTED);
        }
    }

    @Setter(CameraKeys.THERMAL_BACKGROUND_TEMPERATURE)
    public void setThermalBackgroundTemperature(float value, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (isOverallTemperatureMeterSupported()) {
            short protocolValue = (short) ((int) (((float) EXTERNAL_SCENE_PARAMETER_MULTIPLIER) * value));
            if (protocolValue >= -5000) {
                DataCameraTauExterParams setter = new DataCameraTauExterParams();
                setter.setReceiverId(getReceiverIdByIndex());
                setter.addParam(DataCameraTauExterParams.ExterParamId.BACKGROUND_TEMP, protocolValue);
                setter.start(new DJIDataCallBack() {
                    /* class dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraXTBaseAbstraction.AnonymousClass29 */

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
            } else if (callback != null) {
                callback.onFails(DJIError.COMMON_PARAM_INVALID);
            }
        } else if (callback != null) {
            callback.onFails(DJIError.COMMON_UNSUPPORTED);
        }
    }

    @Setter(CameraKeys.THERMAL_SCENE_EMISSIVITY)
    public void setThermalSceneEmissivity(float value, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (isOverallTemperatureMeterSupported()) {
            short protocolValue = (short) ((int) (100.0f * value));
            if (protocolValue < 5000 || protocolValue > 10000) {
                callback.onFails(DJIError.COMMON_PARAM_INVALID);
                return;
            }
            DataCameraTauExterParams setter = new DataCameraTauExterParams();
            setter.setReceiverId(getReceiverIdByIndex());
            setter.addParam(DataCameraTauExterParams.ExterParamId.TARGET_EMISSIVITY, protocolValue);
            setter.start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraXTBaseAbstraction.AnonymousClass30 */

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
        } else if (callback != null) {
            callback.onFails(DJIError.COMMON_UNSUPPORTED);
        }
    }

    @Setter(CameraKeys.THERMAL_WINDOW_REFLECTION)
    public void setThermalWindowReflection(float value, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (isOverallTemperatureMeterSupported()) {
            short protocolValue = (short) ((int) (((float) EXTERNAL_SCENE_PARAMETER_MULTIPLIER) * value));
            if (protocolValue >= 0 && protocolValue <= DataCameraGetPushTauParam.getInstance().getWindowTransmission(getExpectedSenderIdByIndex())) {
                DataCameraTauExterParams setter = new DataCameraTauExterParams();
                setter.addParam(DataCameraTauExterParams.ExterParamId.WINDOW_REFLECTION, protocolValue);
                setter.setReceiverId(getReceiverIdByIndex());
                setter.start(new DJIDataCallBack() {
                    /* class dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraXTBaseAbstraction.AnonymousClass31 */

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
            } else if (callback != null) {
                callback.onFails(DJIError.COMMON_PARAM_INVALID);
            }
        } else if (callback != null) {
            callback.onFails(DJIError.COMMON_UNSUPPORTED);
        }
    }

    @Setter(CameraKeys.THERMAL_WINDOW_REFLECTED_TEMPERATURE)
    public void setThermalWindowReflectedTemperature(float value, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (isOverallTemperatureMeterSupported()) {
            short protocolValue = (short) ((int) (((float) EXTERNAL_SCENE_PARAMETER_MULTIPLIER) * value));
            if (protocolValue >= -5000) {
                DataCameraTauExterParams setter = new DataCameraTauExterParams();
                setter.setReceiverId(getReceiverIdByIndex());
                setter.addParam(DataCameraTauExterParams.ExterParamId.WINDOW_REFLECTED_TEMP, protocolValue);
                setter.start(new DJIDataCallBack() {
                    /* class dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraXTBaseAbstraction.AnonymousClass32 */

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
            } else if (callback != null) {
                callback.onFails(DJIError.COMMON_PARAM_INVALID);
            }
        } else if (callback != null) {
            callback.onFails(DJIError.COMMON_UNSUPPORTED);
        }
    }

    @Setter(CameraKeys.THERMAL_WINDOW_TEMPERATURE)
    public void setThermalWindowTemperature(float value, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (isOverallTemperatureMeterSupported()) {
            short protocolValue = (short) ((int) (((float) EXTERNAL_SCENE_PARAMETER_MULTIPLIER) * value));
            if (protocolValue >= -5000) {
                DataCameraTauExterParams setter = new DataCameraTauExterParams();
                setter.setReceiverId(getReceiverIdByIndex());
                setter.addParam(DataCameraTauExterParams.ExterParamId.WINDOW_TEMP, protocolValue);
                setter.start(new DJIDataCallBack() {
                    /* class dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraXTBaseAbstraction.AnonymousClass33 */

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
            } else if (callback != null) {
                callback.onFails(DJIError.COMMON_PARAM_INVALID);
            }
        } else if (callback != null) {
            callback.onFails(DJIError.COMMON_UNSUPPORTED);
        }
    }

    @Setter(CameraKeys.THERMAL_WINDOW_TRANSMISSION_COEFFICIENT)
    public void setThermalWindowTransmissionCoefficient(float value, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (isOverallTemperatureMeterSupported()) {
            short protocolValue = (short) ((int) (((float) EXTERNAL_SCENE_PARAMETER_MULTIPLIER) * value));
            if (protocolValue >= 5000 && protocolValue <= 10000 - DataCameraGetPushTauParam.getInstance().getWindowReflection(getExpectedSenderIdByIndex())) {
                DataCameraTauExterParams setter = new DataCameraTauExterParams();
                setter.setReceiverId(getReceiverIdByIndex());
                setter.addParam(DataCameraTauExterParams.ExterParamId.WINDOW_TRANSMISSION, protocolValue);
                setter.start(new DJIDataCallBack() {
                    /* class dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraXTBaseAbstraction.AnonymousClass34 */

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
            } else if (callback != null) {
                callback.onFails(DJIError.COMMON_PARAM_INVALID);
            }
        } else if (callback != null) {
            callback.onFails(DJIError.COMMON_UNSUPPORTED);
        }
    }

    /* access modifiers changed from: protected */
    public boolean checkPhotoShootMode(SettingsDefinitions.ShootPhotoMode mode) {
        return SettingsDefinitions.ShootPhotoMode.SINGLE == mode || SettingsDefinitions.ShootPhotoMode.INTERVAL == mode;
    }

    private DJIError checkPrerequisiteForStartShootPhoto() {
        if (!CameraUtils.isSDCardReady(this.index)) {
            return DJICameraError.SD_CARD_ERROR;
        }
        if (!CameraUtils.isInActionMode(this.index) || !CameraUtils.isPhotoActionExecutable(this.index)) {
            return DJICameraError.UNSUPPORTED_CMD_STATE;
        }
        if (DataCameraGetPushStateInfo.getInstance().getIsTimePhotoing(getExpectedSenderIdByIndex())) {
            this.lastShootPhotoState = DataCameraGetStateInfo.PhotoState.OTHER;
        } else if (DataCameraGetPushStateInfo.getInstance().getPhotoState(getExpectedSenderIdByIndex()) != DataCameraGetStateInfo.PhotoState.Single) {
            this.lastShootPhotoState = DataCameraGetPushStateInfo.getInstance().getPhotoState(getExpectedSenderIdByIndex());
        }
        return checkSDCardStatus();
    }

    private DJIError checkPrerequisiteForStopShootPhoto() {
        if (!CameraUtils.isInActionMode(this.index)) {
            return DJICameraError.UNSUPPORTED_CMD_STATE;
        }
        if (DataCameraGetPushStateInfo.getInstance().getIsTimePhotoing(getExpectedSenderIdByIndex())) {
            this.lastShootPhotoState = DataCameraGetStateInfo.PhotoState.OTHER;
        } else {
            this.lastShootPhotoState = DataCameraGetPushStateInfo.getInstance().getPhotoState(getExpectedSenderIdByIndex());
        }
        return checkSDCardStatus();
    }

    private DJIError checkSDCardStatus() {
        if (!DataCameraGetPushStateInfo.getInstance().getSDCardInsertState(getExpectedSenderIdByIndex())) {
            return DJICameraError.SD_CARD_NOT_INSERTED;
        }
        switch (DataCameraGetPushStateInfo.getInstance().getSDCardState(getExpectedSenderIdByIndex())) {
            case Normal:
                return null;
            case Full:
                return DJICameraError.SD_CARD_FULL;
            case None:
                return DJICameraError.SD_CARD_NOT_INSERTED;
            default:
                return DJICameraError.SD_CARD_ERROR;
        }
    }

    private DJIError checkPrerequisiteForStartRecordVideo() {
        if (DataCameraGetPushStateInfo.getInstance().getMode(getExpectedSenderIdByIndex()) != DataCameraGetMode.MODE.RECORD) {
            return DJICameraError.UNSUPPORTED_CMD_STATE;
        }
        if (!CameraUtils.isSDCardReady(this.index)) {
            return DJICameraError.SD_CARD_ERROR;
        }
        if (!CameraUtils.isRecordActionExecutable(this.index)) {
            return DJICameraError.UNSUPPORTED_CMD_STATE;
        }
        if (DataCameraGetPushStateInfo.getInstance().getRecordState(getExpectedSenderIdByIndex()) == DataCameraGetPushStateInfo.RecordType.START || DataCameraGetPushStateInfo.getInstance().getRecordState(getExpectedSenderIdByIndex()) == DataCameraGetPushStateInfo.RecordType.STARTING || DataCameraGetPushStateInfo.getInstance().getRecordState(getExpectedSenderIdByIndex()) == DataCameraGetPushStateInfo.RecordType.STOP) {
            return DJICameraError.UNSUPPORTED_CMD_STATE;
        }
        this.lastRecordVideoState = DataCameraGetPushStateInfo.getInstance().getRecordState(getExpectedSenderIdByIndex());
        return checkSDCardStatus();
    }

    private DJIError checkPrerequisiteForStopRecordVideo() {
        if (DataCameraGetPushStateInfo.getInstance().getMode(getExpectedSenderIdByIndex()) != DataCameraGetMode.MODE.RECORD) {
            return DJICameraError.UNSUPPORTED_CMD_STATE;
        }
        if (DataCameraGetPushStateInfo.getInstance().getRecordState(getExpectedSenderIdByIndex()) == DataCameraGetPushStateInfo.RecordType.NO || DataCameraGetPushStateInfo.getInstance().getRecordState(getExpectedSenderIdByIndex()) == DataCameraGetPushStateInfo.RecordType.STOP) {
            return DJICameraError.UNSUPPORTED_CMD_STATE;
        }
        this.lastRecordVideoState = DataCameraGetPushStateInfo.getInstance().getRecordState(getExpectedSenderIdByIndex());
        return checkSDCardStatus();
    }

    /* access modifiers changed from: protected */
    public void checkShootPhotoStatus() {
        if (this.shootPhotoFSM.state == InternalState.Idle && this.lastShootPhotoState == DataCameraGetStateInfo.PhotoState.NO) {
            if (DataCameraGetPushStateInfo.getInstance().getIsTimePhotoing(getExpectedSenderIdByIndex())) {
                if (CameraUtils.ShootPhotoTimeoutLock.getInstance(this.index).getThreadInitiatedState()) {
                    CameraUtils.ShootPhotoTimeoutLock.getInstance(this.index).setResult(true);
                } else {
                    this.shootPhotoFSM.setIdleToWorking();
                }
                this.lastShootPhotoState = DataCameraGetStateInfo.PhotoState.OTHER;
            } else if (DataCameraGetPushStateInfo.getInstance().getPhotoState(getExpectedSenderIdByIndex()) == DataCameraGetStateInfo.PhotoState.Single) {
                if (CameraUtils.ShootPhotoTimeoutLock.getInstance(this.index).getThreadInitiatedState()) {
                    CameraUtils.ShootPhotoTimeoutLock.getInstance(this.index).setResult(true);
                } else {
                    this.shootPhotoFSM.setIdleToWorking();
                }
                this.lastShootPhotoState = DataCameraGetStateInfo.PhotoState.Single;
            }
        }
        if (this.shootPhotoFSM.state == InternalState.Working && this.lastShootPhotoState != DataCameraGetStateInfo.PhotoState.NO) {
            if (this.lastShootPhotoState == DataCameraGetStateInfo.PhotoState.OTHER) {
                if (!DataCameraGetPushStateInfo.getInstance().getIsTimePhotoing(getExpectedSenderIdByIndex())) {
                    if (CameraUtils.ShootPhotoTimeoutLock.getInstance(this.index).getThreadInitiatedState()) {
                        CameraUtils.ShootPhotoTimeoutLock.getInstance(this.index).setResult(true);
                    }
                    this.shootPhotoFSM.setWorkingToIdle();
                }
            } else if (DataCameraGetPushStateInfo.getInstance().getPhotoState(getExpectedSenderIdByIndex()) != DataCameraGetStateInfo.PhotoState.Single) {
                if (CameraUtils.ShootPhotoTimeoutLock.getInstance(this.index).getThreadInitiatedState()) {
                    CameraUtils.ShootPhotoTimeoutLock.getInstance(this.index).setResult(true);
                }
                this.shootPhotoFSM.setWorkingToIdle();
            }
        }
        if (!DataCameraGetPushStateInfo.getInstance().getIsTimePhotoing(getExpectedSenderIdByIndex())) {
            this.lastShootPhotoState = DataCameraGetPushStateInfo.getInstance().getPhotoState(getExpectedSenderIdByIndex());
        } else {
            this.lastShootPhotoState = DataCameraGetStateInfo.PhotoState.OTHER;
        }
    }

    /* access modifiers changed from: protected */
    public void checkRecordVideoStatus() {
        DataCameraGetPushStateInfo pushStateInfo = DataCameraGetPushStateInfo.getInstance();
        if (this.recordVideoFSM.state == InternalState.Idle) {
            if (this.lastRecordVideoState != DataCameraGetPushStateInfo.RecordType.NO) {
                return;
            }
            if (pushStateInfo.getRecordState(getExpectedSenderIdByIndex()) == DataCameraGetPushStateInfo.RecordType.STARTING || pushStateInfo.getRecordState(getExpectedSenderIdByIndex()) == DataCameraGetPushStateInfo.RecordType.START || pushStateInfo.getRecordState(getExpectedSenderIdByIndex()) == DataCameraGetPushStateInfo.RecordType.STOP) {
                if (CameraUtils.RecordVideoTimeoutLock.getInstance(this.index).getThreadInitiatedState()) {
                    CameraUtils.RecordVideoTimeoutLock.getInstance(this.index).setResult(true);
                }
                this.recordVideoFSM.setIdleToWorking();
                this.lastRecordVideoState = pushStateInfo.getRecordState(getExpectedSenderIdByIndex());
            }
        } else if (pushStateInfo.getRecordState(getExpectedSenderIdByIndex()) != DataCameraGetPushStateInfo.RecordType.START && pushStateInfo.getRecordState(getExpectedSenderIdByIndex()) != DataCameraGetPushStateInfo.RecordType.STARTING && pushStateInfo.getRecordState(getExpectedSenderIdByIndex()) != DataCameraGetPushStateInfo.RecordType.STOP) {
            if (CameraUtils.RecordVideoTimeoutLock.getInstance(this.index).getThreadInitiatedState()) {
                CameraUtils.RecordVideoTimeoutLock.getInstance(this.index).setResult(true);
            }
            this.recordVideoFSM.setWorkingToIdle();
            this.lastRecordVideoState = DataCameraGetPushStateInfo.RecordType.NO;
        }
    }

    private SettingsDefinitions.VideoStandard checkVideoStandard(int resolution, int frameRate) {
        if (resolution == 0 && frameRate == 3) {
            return SettingsDefinitions.VideoStandard.NTSC;
        }
        if (resolution == 26 && frameRate == 2) {
            return SettingsDefinitions.VideoStandard.PAL;
        }
        return SettingsDefinitions.VideoStandard.UNKNOWN;
    }

    private SettingsDefinitions.PhotoTimeIntervalSettings wrapPhotoIntervalParam(int timeIntervalInSeconds) {
        return new SettingsDefinitions.PhotoTimeIntervalSettings(255, timeIntervalInSeconds);
    }

    private SettingsDefinitions.ThermalProfile wrapThermalProfile(SettingsDefinitions.ThermalProfile.Builder builder, int focalLength, int resolution, int fps) {
        if (focalLength != 255) {
            builder.focalLength(SettingsDefinitions.ThermalLensFocalLength.values()[focalLength]);
        } else {
            builder.focalLength(SettingsDefinitions.ThermalLensFocalLength.UNKNOWN);
        }
        builder.frameRateUpperBound(SettingsDefinitions.ThermalFrameRateUpperBound.find(fps));
        switch (resolution) {
            case 0:
                builder.resolution(SettingsDefinitions.ThermalResolution.RESOLUTION_640x512);
                break;
            case 1:
                builder.resolution(SettingsDefinitions.ThermalResolution.RESOLUTION_336x256);
                break;
            default:
                builder.resolution(SettingsDefinitions.ThermalResolution.UNKNOWN);
                break;
        }
        builder.version(isOverallTemperatureMeterSupported() ? ThermalVersion.XT_ADVANCED_RADIOMETRY_ENABLED : ThermalVersion.XT_STANDARD);
        return builder.build();
    }

    private SettingsDefinitions.CameraMode checkCameraMode(int value) {
        switch (value) {
            case 6:
                value = 2;
                break;
            case 7:
                value = 4;
                break;
        }
        return SettingsDefinitions.CameraMode.find(value);
    }

    /* access modifiers changed from: protected */
    public void startShootPhoto(final DJISDKCacheHWAbstraction.InnerCallback callback, SettingsDefinitions.ShootPhotoMode mode) {
        Object gimbalBehavior = CacheHelper.getValue(KeyHelper.getFlightControllerKey(FlightControllerKeys.CONTROL_GIMBAL_BEHAVIOR));
        if (gimbalBehavior == null || gimbalBehavior != ControlGimbalBehavior.BOTH_MOVE) {
            DJIError startPreError = checkPrerequisiteForStartShootPhoto();
            if (startPreError != null) {
                if (callback != null) {
                    callback.onFails(startPreError);
                }
            } else if (getReceiverIdByIndex() != 0 || CommonUtil.isPM420Platform()) {
                super.startShootPhoto(callback, mode);
            } else if (!checkPhotoShootMode(mode)) {
                callback.onFails(DJICameraError.INVALID_PARAMETERS);
            } else if (this.shootPhotoFSM.state == InternalState.Working) {
                callback.onFails(DJICameraError.UNSUPPORTED_CMD_STATE);
            } else {
                final SettingsDefinitions.ShootPhotoMode m = mode;
                new Thread("XTBaseAbs4") {
                    /* class dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraXTBaseAbstraction.AnonymousClass35 */

                    public void run() {
                        try {
                            CameraUtils.ShootPhotoTimeoutLock.getInstance(DJICameraXTBaseAbstraction.this.index).reset();
                            if (m == SettingsDefinitions.ShootPhotoMode.SINGLE) {
                                DataSpecialControl.getInstance().setPhotoType(DataCameraSetPhoto.TYPE.SINGLE).start(20);
                            } else if (m == SettingsDefinitions.ShootPhotoMode.INTERVAL) {
                                DataSpecialControl.getInstance().setPhotoType(DataCameraSetPhoto.TYPE.TIME, 255, DataCameraGetPushTauParam.getInstance().getPhotoInterval(DJICameraXTBaseAbstraction.this.getExpectedSenderIdByIndex())).start(20);
                            }
                            CameraUtils.ShootPhotoTimeoutLock.getInstance(DJICameraXTBaseAbstraction.this.index).waitResult();
                            if (CameraUtils.ShootPhotoTimeoutLock.getInstance(DJICameraXTBaseAbstraction.this.index).getResult()) {
                                DJICameraXTBaseAbstraction.this.shootPhotoFSM.setIdleToWorking();
                                if (callback != null) {
                                    callback.onSuccess(null);
                                }
                            } else if (callback != null) {
                                callback.onFails(DJICameraError.EXEC_TIMEOUT);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        } else {
            super.startShootPhoto(callback, mode);
        }
    }

    @Action(CameraKeys.STOP_SHOOT_PHOTO)
    public void stopShootPhoto(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        Object gimbalBehavior = CacheHelper.getValue(KeyHelper.getFlightControllerKey(FlightControllerKeys.CONTROL_GIMBAL_BEHAVIOR));
        if (gimbalBehavior == null || gimbalBehavior != ControlGimbalBehavior.BOTH_MOVE) {
            DJIError prerequisiteError = checkPrerequisiteForStopShootPhoto();
            if (prerequisiteError != null) {
                if (callback != null) {
                    callback.onFails(prerequisiteError);
                }
            } else if (getReceiverIdByIndex() != 0 || CommonUtil.isPM420Platform()) {
                super.stopShootPhoto(callback);
            } else if (this.shootPhotoFSM.state != InternalState.Idle) {
                new Thread("XTBaseAbs3") {
                    /* class dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraXTBaseAbstraction.AnonymousClass36 */

                    public void run() {
                        try {
                            CameraUtils.ShootPhotoTimeoutLock.getInstance(DJICameraXTBaseAbstraction.this.index).reset();
                            DataSpecialControl.getInstance().setPhotoType(DataCameraSetPhoto.TYPE.STOP).start(20);
                            CameraUtils.ShootPhotoTimeoutLock.getInstance(DJICameraXTBaseAbstraction.this.index).waitResult();
                            if (CameraUtils.ShootPhotoTimeoutLock.getInstance(DJICameraXTBaseAbstraction.this.index).getResult()) {
                                DJICameraXTBaseAbstraction.this.shootPhotoFSM.setWorkingToIdle();
                                if (callback != null) {
                                    callback.onSuccess(null);
                                }
                            } else if (callback != null) {
                                callback.onFails(DJICameraError.EXEC_TIMEOUT);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            } else if (callback != null) {
                callback.onFails(DJICameraError.UNSUPPORTED_CMD_STATE);
            }
        } else {
            super.stopShootPhoto(callback);
        }
    }

    @Action(CameraKeys.START_RECORD_VIDEO)
    public void startRecordVideo(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        Object gimbalBehavior = CacheHelper.getValue(KeyHelper.getFlightControllerKey(FlightControllerKeys.CONTROL_GIMBAL_BEHAVIOR));
        if (gimbalBehavior == null || gimbalBehavior != ControlGimbalBehavior.BOTH_MOVE) {
            DJIError startPreError = checkPrerequisiteForStartRecordVideo();
            if (startPreError != null) {
                if (callback != null) {
                    callback.onFails(startPreError);
                }
            } else if (getReceiverIdByIndex() != 0 || CommonUtil.isPM420Platform()) {
                super.startRecordVideo(callback);
            } else if (this.recordVideoFSM.state == InternalState.Idle) {
                if (DataCameraGetPushStateInfo.getInstance().getRecordState(getExpectedSenderIdByIndex()) != DataCameraGetPushStateInfo.RecordType.STARTING || DataCameraGetPushStateInfo.getInstance().getRecordState(getExpectedSenderIdByIndex()) != DataCameraGetPushStateInfo.RecordType.START) {
                    new Thread("XTBaseAbs2") {
                        /* class dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraXTBaseAbstraction.AnonymousClass37 */

                        public void run() {
                            try {
                                CameraUtils.RecordVideoTimeoutLock.getInstance(DJICameraXTBaseAbstraction.this.index).reset();
                                DataSpecialControl.getInstance().setRecordType(true).start(20);
                                CameraUtils.RecordVideoTimeoutLock.getInstance(DJICameraXTBaseAbstraction.this.index).waitResult();
                                if (CameraUtils.RecordVideoTimeoutLock.getInstance(DJICameraXTBaseAbstraction.this.index).getResult()) {
                                    DJICameraXTBaseAbstraction.this.recordVideoFSM.setIdleToWorking();
                                    if (callback != null) {
                                        callback.onSuccess(null);
                                    }
                                } else if (callback != null) {
                                    callback.onFails(DJICameraError.EXEC_TIMEOUT);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                }
            } else if (callback != null) {
                callback.onFails(DJICameraError.UNSUPPORTED_CMD_STATE);
            }
        } else {
            super.startRecordVideo(callback);
        }
    }

    @Action(CameraKeys.STOP_RECORD_VIDEO)
    public void stopRecordVideo(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        Object gimbalBehavior = CacheHelper.getValue(KeyHelper.getFlightControllerKey(FlightControllerKeys.CONTROL_GIMBAL_BEHAVIOR));
        if (gimbalBehavior == null || gimbalBehavior != ControlGimbalBehavior.BOTH_MOVE) {
            DJIError stopPreError = checkPrerequisiteForStopRecordVideo();
            if (stopPreError != null) {
                if (callback != null) {
                    callback.onFails(stopPreError);
                }
            } else if (getReceiverIdByIndex() != 0 || CommonUtil.isPM420Platform()) {
                super.stopRecordVideo(callback);
            } else if (this.recordVideoFSM.state == InternalState.Working) {
                if (DataCameraGetPushStateInfo.getInstance().getRecordState(getExpectedSenderIdByIndex()) == DataCameraGetPushStateInfo.RecordType.STARTING || DataCameraGetPushStateInfo.getInstance().getRecordState(getExpectedSenderIdByIndex()) == DataCameraGetPushStateInfo.RecordType.START) {
                    new Thread("XTBaseAbs1") {
                        /* class dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraXTBaseAbstraction.AnonymousClass38 */

                        public void run() {
                            try {
                                CameraUtils.RecordVideoTimeoutLock.getInstance(DJICameraXTBaseAbstraction.this.index).reset();
                                DataSpecialControl.getInstance().setRecordType(false).start(20);
                                CameraUtils.RecordVideoTimeoutLock.getInstance(DJICameraXTBaseAbstraction.this.index).waitResult();
                                if (CameraUtils.RecordVideoTimeoutLock.getInstance(DJICameraXTBaseAbstraction.this.index).getResult()) {
                                    if (callback != null) {
                                        callback.onSuccess(null);
                                    }
                                } else if (callback != null) {
                                    callback.onFails(DJICameraError.EXEC_TIMEOUT);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                }
            } else if (callback != null) {
                callback.onFails(DJICameraError.UNSUPPORTED_CMD_STATE);
            }
        } else {
            super.stopRecordVideo(callback);
        }
    }

    private boolean checkCapabilityForPhotoFileFormat(SettingsDefinitions.PhotoFileFormat format) {
        boolean isRadiometricJPEGSupported;
        boolean isRVersion;
        if (DataCameraGetPushStateInfo.getInstance().getVerstion(getExpectedSenderIdByIndex()) >= 3) {
            isRadiometricJPEGSupported = true;
        } else {
            isRadiometricJPEGSupported = false;
        }
        if (DataCameraGetPushStateInfo.getInstance().getVerstion(getExpectedSenderIdByIndex()) >= 4) {
            isRVersion = true;
        } else {
            isRVersion = false;
        }
        boolean isTIFFLowHightSupported = DataCameraGetPushTauParam.getInstance().supportSpotThermometric(getExpectedSenderIdByIndex());
        switch (format) {
            case JPEG:
            case TIFF_14_BIT:
                return true;
            case RADIOMETRIC_JPEG:
                return isRadiometricJPEGSupported;
            case TIFF_14_BIT_LINEAR_LOW_TEMP_RESOLUTION:
            case TIFF_14_BIT_LINEAR_HIGH_TEMP_RESOLUTION:
                if (isRVersion) {
                    return false;
                }
                return isTIFFLowHightSupported;
            default:
                return false;
        }
    }

    private SettingsDefinitions.PhotoFileFormat convertPhotoFileFormatToSDK(int format) {
        switch (format) {
            case 0:
            case 4:
                return SettingsDefinitions.PhotoFileFormat.TIFF_14_BIT;
            case 1:
                return SettingsDefinitions.PhotoFileFormat.JPEG;
            case 2:
            case 3:
            default:
                return SettingsDefinitions.PhotoFileFormat.UNKNOWN;
            case 5:
                return SettingsDefinitions.PhotoFileFormat.TIFF_14_BIT_LINEAR_LOW_TEMP_RESOLUTION;
            case 6:
                return SettingsDefinitions.PhotoFileFormat.TIFF_14_BIT_LINEAR_HIGH_TEMP_RESOLUTION;
            case 7:
            case 8:
            case 9:
                return SettingsDefinitions.PhotoFileFormat.RADIOMETRIC_JPEG;
        }
    }

    private int convertPhotoFileFormatToProtocol(SettingsDefinitions.PhotoFileFormat format) {
        switch (format) {
            case JPEG:
            default:
                return 1;
            case TIFF_14_BIT:
                return 4;
            case RADIOMETRIC_JPEG:
                return 7;
            case TIFF_14_BIT_LINEAR_LOW_TEMP_RESOLUTION:
                return 8;
            case TIFF_14_BIT_LINEAR_HIGH_TEMP_RESOLUTION:
                return 9;
        }
    }

    @Setter(CameraKeys.SHOOT_PHOTO_MODE)
    public void setShootPhotoMode(SettingsDefinitions.ShootPhotoMode photoMode, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (photoMode == null) {
            CallbackUtils.onFailure(callback, DJICameraError.INVALID_PARAMETERS);
        } else if (photoMode == SettingsDefinitions.ShootPhotoMode.SINGLE || photoMode == SettingsDefinitions.ShootPhotoMode.INTERVAL) {
            DataCameraSetPhotoMode setter = prepareShootPhotoMode(photoMode);
            setter.setReceiverId(getReceiverIdByIndex());
            setter.start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraXTBaseAbstraction.AnonymousClass39 */

                public void onSuccess(Object model) {
                    CallbackUtils.onSuccess(callback, model);
                }

                public void onFailure(Ccode ccode) {
                    CallbackUtils.onFailure(callback, DJICameraError.getDJIError(ccode));
                }
            });
        } else {
            CallbackUtils.onFailure(callback, DJICameraError.INVALID_PARAMETERS);
        }
    }

    @Setter(CameraKeys.TEMPERATURE_UNIT)
    public void setTemperatureUnit(SettingsDefinitions.TemperatureUnit unit, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (unit == null) {
            CallbackUtils.onFailure(callback, DJICameraError.INVALID_PARAMETERS);
        } else if (unit == SettingsDefinitions.TemperatureUnit.OTHER) {
            CallbackUtils.onFailure(callback, DJICameraError.INVALID_PARAMETERS);
        } else {
            DataCameraTauParamTempUnit setter = new DataCameraTauParamTempUnit();
            setter.setReceiverId(getReceiverIdByIndex());
            setter.setMode(DataCameraTauParamTempUnit.TempUnit.find(unit.value()));
            setter.start(CallbackUtils.defaultCB(callback));
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCameraGetPushShotParams pushShotParams) {
        SettingsDefinitions.ShootPhotoMode shootPhotoMode;
        if (pushShotParams.isGetted() && isValidSender(pushShotParams.getSenderId())) {
            if (pushShotParams.getPhotoType() != DataCameraSetPhoto.TYPE.TIME) {
                shootPhotoMode = SettingsDefinitions.ShootPhotoMode.find(pushShotParams.getPhotoType());
            } else if (pushShotParams.getTimeParamsType() == DataCameraSetTimeParams.TYPE.Timelapse.value()) {
                shootPhotoMode = SettingsDefinitions.ShootPhotoMode.TIME_LAPSE;
            } else {
                shootPhotoMode = SettingsDefinitions.ShootPhotoMode.INTERVAL;
            }
            notifyValueChangeForKeyPath(shootPhotoMode, convertKeyToPath(CameraKeys.SHOOT_PHOTO_MODE));
        }
    }
}
