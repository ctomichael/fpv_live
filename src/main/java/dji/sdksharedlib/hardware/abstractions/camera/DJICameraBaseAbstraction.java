package dji.sdksharedlib.hardware.abstractions.camera;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.PointF;
import android.text.TextUtils;
import com.dji.frame.util.MD5;
import dji.common.camera.CameraParamRangeManager;
import dji.common.camera.CameraUtils;
import dji.common.camera.ExposureSettings;
import dji.common.camera.FocusAssistantSettings;
import dji.common.camera.PhotoTimeLapseSettings;
import dji.common.camera.ResolutionAndFrameRate;
import dji.common.camera.SettingsDefinitions;
import dji.common.camera.WatermarkDisplayContentSettings;
import dji.common.camera.WatermarkSettings;
import dji.common.camera.WhiteBalance;
import dji.common.error.DJICameraError;
import dji.common.error.DJIError;
import dji.common.error.DJIFlightControllerError;
import dji.common.flightcontroller.DJIMultiLEDControlMode;
import dji.common.flightcontroller.LEDsSettings;
import dji.common.util.CallbackUtils;
import dji.common.util.DJICameraEnumMappingUtil;
import dji.common.util.DJILensFeatureUtils;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.config.P3.CmdIdCamera;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.ServiceManager;
import dji.midware.data.model.P3.DataBaseCameraGetting;
import dji.midware.data.model.P3.DataBaseCameraSetting;
import dji.midware.data.model.P3.DataCameraFormatSSD;
import dji.midware.data.model.P3.DataCameraGetAEBParams;
import dji.midware.data.model.P3.DataCameraGetAudio;
import dji.midware.data.model.P3.DataCameraGetCustomInformation;
import dji.midware.data.model.P3.DataCameraGetForeArmLed;
import dji.midware.data.model.P3.DataCameraGetImageSize;
import dji.midware.data.model.P3.DataCameraGetIso;
import dji.midware.data.model.P3.DataCameraGetMode;
import dji.midware.data.model.P3.DataCameraGetPushChart;
import dji.midware.data.model.P3.DataCameraGetPushChartInfo;
import dji.midware.data.model.P3.DataCameraGetPushShotInfo;
import dji.midware.data.model.P3.DataCameraGetPushShotParams;
import dji.midware.data.model.P3.DataCameraGetPushStateInfo;
import dji.midware.data.model.P3.DataCameraGetQuickPlayBack;
import dji.midware.data.model.P3.DataCameraGetRecordFan;
import dji.midware.data.model.P3.DataCameraGetShotInfo;
import dji.midware.data.model.P3.DataCameraGetVOutParams;
import dji.midware.data.model.P3.DataCameraGetVideoCaption;
import dji.midware.data.model.P3.DataCameraGetVideoFormat;
import dji.midware.data.model.P3.DataCameraLoadParams;
import dji.midware.data.model.P3.DataCameraSaveParams;
import dji.midware.data.model.P3.DataCameraSetAEBParms;
import dji.midware.data.model.P3.DataCameraSetAELock;
import dji.midware.data.model.P3.DataCameraSetAperture;
import dji.midware.data.model.P3.DataCameraSetAudio;
import dji.midware.data.model.P3.DataCameraSetCustomInformation;
import dji.midware.data.model.P3.DataCameraSetExposureMode;
import dji.midware.data.model.P3.DataCameraSetFocusAid;
import dji.midware.data.model.P3.DataCameraSetFocusArea;
import dji.midware.data.model.P3.DataCameraSetFocusDistance;
import dji.midware.data.model.P3.DataCameraSetFocusParam;
import dji.midware.data.model.P3.DataCameraSetFocusStroke;
import dji.midware.data.model.P3.DataCameraSetForeArmLed;
import dji.midware.data.model.P3.DataCameraSetImageSize;
import dji.midware.data.model.P3.DataCameraSetIso;
import dji.midware.data.model.P3.DataCameraSetLockGimbalWhenShot;
import dji.midware.data.model.P3.DataCameraSetMechanicalShutter;
import dji.midware.data.model.P3.DataCameraSetMeteringArea;
import dji.midware.data.model.P3.DataCameraSetMode;
import dji.midware.data.model.P3.DataCameraSetOpticsZoomMode;
import dji.midware.data.model.P3.DataCameraSetPhoto;
import dji.midware.data.model.P3.DataCameraSetPhotoMode;
import dji.midware.data.model.P3.DataCameraSetPushChart;
import dji.midware.data.model.P3.DataCameraSetQuickPlayBack;
import dji.midware.data.model.P3.DataCameraSetRecordFan;
import dji.midware.data.model.P3.DataCameraSetSSDVideoFormat;
import dji.midware.data.model.P3.DataCameraSetShutterSpeed;
import dji.midware.data.model.P3.DataCameraSetTimeParams;
import dji.midware.data.model.P3.DataCameraSetVOutParams;
import dji.midware.data.model.P3.DataCameraSetVideoCaption;
import dji.midware.data.model.P3.DataCameraSetVideoEncode;
import dji.midware.data.model.P3.DataCameraSetVideoFormat;
import dji.midware.data.model.P3.DataCameraSetVideoRecordMode;
import dji.midware.data.model.P3.DataCameraSetWhiteBalance;
import dji.midware.data.model.P3.DataCameraWatermark;
import dji.midware.data.model.P3.DataCommonGetVersion;
import dji.midware.data.model.P3.DataCommonSetMultiParam;
import dji.midware.data.model.P3.DataEyeGetPushPseudoCameraParams;
import dji.midware.data.model.P3.DataEyeSetPseudoCameraControl;
import dji.midware.data.model.P3.DataOsdGetMicGain;
import dji.midware.data.model.P3.DataOsdSetMicGain;
import dji.midware.data.model.P3.DataSingleSendAppStateForStabilization;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.media.DJIVideoDecoder;
import dji.midware.util.BytesUtil;
import dji.sdksharedlib.DJISDKCache;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.hardware.abstractions.Action;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Getter;
import dji.sdksharedlib.hardware.abstractions.Setter;
import dji.sdksharedlib.keycatalog.CameraKeys;
import dji.sdksharedlib.keycatalog.DJISDKCacheKeys;
import dji.sdksharedlib.listener.DJISetCallback;
import dji.sdksharedlib.store.DJISDKCacheStoreLayer;
import java.io.File;
import java.util.Arrays;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class DJICameraBaseAbstraction extends DJICameraAbstraction {
    protected static final int PHOTO_COUNT_MAX = 255;
    private static final String TAG = "DJISDKCacheBaseCameraAbstraction";
    protected boolean isLensMounted;
    protected int maxAperture = -1;
    protected int maxFocalDistance = -1;
    protected int minAperture = -1;
    protected int minFocalDistance = -1;
    protected DataCameraSetOpticsZoomMode.ZoomSpeed speedCache = null;

    public void init(String component, int index, DJISDKCacheStoreLayer storeLayer, DJISDKCacheHWAbstraction.OnValueChangeListener onValueChangeListener) {
        super.init(component, index, storeLayer, onValueChangeListener);
    }

    public void syncPushDataFromMidware() {
        super.syncPushDataFromMidware();
        if (DataCameraGetPushShotParams.getInstance().isGetted()) {
            DataCameraGetPushShotParams.getInstance().swapValidData(getExpectedSenderIdByIndex());
            onEvent3BackgroundThread(DataCameraGetPushShotParams.getInstance());
        }
        if (DataCameraGetPushShotInfo.getInstance().isGetted()) {
            DataCameraGetPushShotInfo.getInstance().swapValidData(getExpectedSenderIdByIndex());
            onEvent3BackgroundThread(DataCameraGetPushShotInfo.getInstance());
        }
        if (DataCameraGetPushChartInfo.getInstance().isGetted()) {
            DataCameraGetPushChartInfo.getInstance().swapValidData(getExpectedSenderIdByIndex());
            onEvent3BackgroundThread(DataCameraGetPushChartInfo.getInstance());
        }
    }

    private SettingsDefinitions.ShootPhotoMode convertPhotoTypeToPhotoMode(DataCameraSetPhoto.TYPE type) {
        switch (type) {
            case SINGLE:
                return SettingsDefinitions.ShootPhotoMode.SINGLE;
            case HDR:
                return SettingsDefinitions.ShootPhotoMode.HDR;
            case BURST:
                return SettingsDefinitions.ShootPhotoMode.BURST;
            case AEB:
                return SettingsDefinitions.ShootPhotoMode.AEB;
            case TIME:
                return SettingsDefinitions.ShootPhotoMode.INTERVAL;
            case HDR_PLUS:
                return SettingsDefinitions.ShootPhotoMode.EHDR;
            case HYPER_NIGHT:
                return SettingsDefinitions.ShootPhotoMode.HYPER_LIGHT;
            case HYPER_LAPSE:
                return SettingsDefinitions.ShootPhotoMode.HYPER_LAPSE;
            default:
                return null;
        }
    }

    /* access modifiers changed from: protected */
    public SettingsDefinitions.ShootPhotoMode getShootPhotoModeFrom(DataCameraGetPushShotParams pushShotParams) {
        if (pushShotParams.getPhotoType(getExpectedSenderIdByIndex()) != DataCameraSetPhoto.TYPE.TIME) {
            return SettingsDefinitions.ShootPhotoMode.find(pushShotParams.getPhotoType(getExpectedSenderIdByIndex()));
        }
        if (pushShotParams.getTimeParamsType(getExpectedSenderIdByIndex()) == DataCameraSetTimeParams.TYPE.Timelapse.value()) {
            return SettingsDefinitions.ShootPhotoMode.TIME_LAPSE;
        }
        return SettingsDefinitions.ShootPhotoMode.INTERVAL;
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCameraGetPushShotParams pushShotParams) {
        WhiteBalance wb;
        if (pushShotParams != null && pushShotParams.isGetted() && isValidSender(pushShotParams.getSenderId())) {
            int realISO = pushShotParams.getRelISO();
            SettingsDefinitions.ExposureCompensation realExposureCompensation = CameraUtils.getRealCameraExposureCompensation(pushShotParams.getRelExposureCompensation(), getExpectedSenderIdByIndex());
            SettingsDefinitions.Aperture realAperture = SettingsDefinitions.Aperture.find(pushShotParams.getRealApertureSize());
            SettingsDefinitions.ShutterSpeed realShutterSpeed = CameraUtils.getRealShutterSpeed(pushShotParams.isRelReciprocal(), pushShotParams.getRelShutter(), pushShotParams.getRelShutterSpeedDecimal());
            ExposureSettings currentExposureValues = new ExposureSettings(realAperture, realShutterSpeed, realISO, realExposureCompensation);
            SettingsDefinitions.ISO iso = SettingsDefinitions.ISO.find(pushShotParams.getISO());
            SettingsDefinitions.ExposureCompensation exposureCompensation = CameraUtils.getRealCameraExposureCompensation(pushShotParams.getExposureCompensation(), getExpectedSenderIdByIndex());
            SettingsDefinitions.Aperture aperture = SettingsDefinitions.Aperture.find(pushShotParams.getApertureSize());
            SettingsDefinitions.ShutterSpeed shutterSpeed = CameraUtils.getRealShutterSpeed(pushShotParams.isReciprocal(), pushShotParams.getShutter(), pushShotParams.getShutterSpeedDecimal());
            SettingsDefinitions.VideoStandard videoStandard = SettingsDefinitions.VideoStandard.find(pushShotParams.getVideoStandard());
            boolean isAELock = pushShotParams.isAELock();
            SettingsDefinitions.PhotoFileFormat fileFormat = SettingsDefinitions.PhotoFileFormat.find(pushShotParams.getImageFormat());
            SettingsDefinitions.PhotoTimeIntervalSettings photoTimeIntervalSettings = new SettingsDefinitions.PhotoTimeIntervalSettings(pushShotParams.getTimeParamsNum(), pushShotParams.getTimeParamsPeriod());
            SettingsDefinitions.ExposureMode exposureMode = SettingsDefinitions.ExposureMode.find(pushShotParams.getExposureMode().value());
            SettingsDefinitions.ExposureState exposureState = SettingsDefinitions.ExposureState.find(pushShotParams.getExposureStatus());
            ResolutionAndFrameRate videoResolutionAndFrameRate = DJICameraEnumMappingUtil.wrapResolutionAndFrameRate(pushShotParams.getVideoFormat(new int[0]), pushShotParams.getVideoFps(), pushShotParams.getVideoFov());
            SettingsDefinitions.VideoFileFormat videoFileFormat = SettingsDefinitions.VideoFileFormat.find(pushShotParams.getVideoStoreFormat());
            SettingsDefinitions.MeteringMode exposureMeteringMode = SettingsDefinitions.MeteringMode.find(pushShotParams.getMetering());
            SettingsDefinitions.WhiteBalancePreset whiteBalancePreset = SettingsDefinitions.WhiteBalancePreset.find(pushShotParams.getWhiteBalance());
            if (DataCameraGetPushStateInfo.getInstance().getVerstion(getExpectedSenderIdByIndex()) != 0 || whiteBalancePreset == SettingsDefinitions.WhiteBalancePreset.CUSTOM) {
                wb = new WhiteBalance(whiteBalancePreset, pushShotParams.getColorTemp());
            } else {
                wb = new WhiteBalance(whiteBalancePreset);
            }
            notifyValueChangeForKeyPath(Integer.valueOf(pushShotParams.getTonal()), convertKeyToPath(CameraKeys.HUE));
            int aEBNumber = pushShotParams.getAEBNumber();
            SettingsDefinitions.CameraColor cameraColor = SettingsDefinitions.CameraColor.find(mapProtocolNumberToCorrespondingNumber(pushShotParams.getDigitalFilter()));
            SettingsDefinitions.AntiFlickerFrequency antiFlicker = SettingsDefinitions.AntiFlickerFrequency.find(pushShotParams.getAntiFlicker());
            SettingsDefinitions.ShootPhotoMode shootPhotoMode = getShootPhotoModeFrom(pushShotParams);
            SettingsDefinitions.PhotoAspectRatio aspectRatio = SettingsDefinitions.PhotoAspectRatio.find(pushShotParams.getImageRatio().value());
            byte b = BytesUtil.getByte(pushShotParams.getSharpe());
            int contrast = BytesUtil.getByte(pushShotParams.getContrast());
            byte b2 = BytesUtil.getByte(pushShotParams.getSaturation());
            SettingsDefinitions.PhotoBurstCount burstShotCount = SettingsDefinitions.PhotoBurstCount.find(pushShotParams.getContinuous());
            SettingsDefinitions.PhotoAEBCount photoAEBCount = SettingsDefinitions.PhotoAEBCount.find(pushShotParams.getAEBNumber());
            boolean autoUnlockAELock = pushShotParams.autoAEUnlock();
            SettingsDefinitions.VideoFileCompressionStandard standard = SettingsDefinitions.VideoFileCompressionStandard.values()[pushShotParams.getPrimaryVideoEncodeType().value()];
            SettingsDefinitions.PhotoBurstCount rawBurstCount = SettingsDefinitions.PhotoBurstCount.find(pushShotParams.getRawBurstCount());
            int photoFormat = pushShotParams.getImageFormat();
            notifyValueChangeForKeyPath(Integer.valueOf(pushShotParams.getConstrastEhance()), CameraKeys.GAMMA);
            notifyValueChangeForKeyPath(Boolean.valueOf(pushShotParams.isMCTFEnable()), CameraKeys.IS_MCTF_ENABLE);
            notifyValueChangeForKeyPath(Boolean.valueOf(pushShotParams.isVideoCaptionEnable()), CameraKeys.IS_VIDEO_CAPTION_ENABLE);
            boolean isLockedGimbalWhenShot = pushShotParams.isLockedGimbalWhenShot();
            notifyValueChangeForKeyPath(Boolean.valueOf(isLockedGimbalWhenShot), CameraKeys.IS_LOCK_GIMBAL_WHEN_SHOT);
            notifyValueChangeForKeyPath(Boolean.valueOf(isLockedGimbalWhenShot), CameraKeys.AUTO_LOCK_GIMBAL_ENABLED);
            notifyValueChangeForKeyPath(Integer.valueOf(pushShotParams.getDigitalFilter()), CameraKeys.FILTER);
            notifyValueChangeForKeyPath(Integer.valueOf(photoFormat), convertKeyToPath(CameraKeys.PHOTO_FORMAT));
            float opticalZoomScale = ((float) pushShotParams.getOpticsScale()) / 100.0f;
            notifyValueChangeForKeyPath(Float.valueOf((float) (((double) pushShotParams.getDigitalZoomScale(getExpectedSenderIdByIndex())) / 100.0d)), convertKeyToPath(CameraKeys.DIGITAL_ZOOM_FACTOR));
            notifyValueChangeForKeyPath(Float.valueOf(opticalZoomScale), convertKeyToPath(CameraKeys.OPTICAL_ZOOM_SCALE));
            notifyValueChangeForKeyPath(rawBurstCount, convertKeyToPath(CameraKeys.PHOTO_RAW_BURST_COUNT));
            notifyValueChangeForKeyPath(burstShotCount, convertKeyToPath(CameraKeys.PHOTO_BURST_COUNT));
            notifyValueChangeForKeyPath(photoAEBCount, convertKeyToPath(CameraKeys.PHOTO_AEB_COUNT));
            notifyValueChangeForKeyPath(Integer.valueOf(b2), convertKeyToPath(CameraKeys.SATURATION));
            notifyValueChangeForKeyPath(Integer.valueOf(b), convertKeyToPath(CameraKeys.SHARPNESS));
            notifyValueChangeForKeyPath(Integer.valueOf(contrast), convertKeyToPath(CameraKeys.CONTRAST));
            notifyValueChangeForKeyPath(aspectRatio, convertKeyToPath(CameraKeys.PHOTO_ASPECT_RATIO));
            notifyValueChangeForKeyPath(standard, convertKeyToPath(CameraKeys.VIDEO_FILE_COMPRESSION_STANDARD));
            notifyValueChangeForKeyPath(Boolean.valueOf(autoUnlockAELock), convertKeyToPath(CameraKeys.AUTO_AE_UNLOCK_ENABLED));
            notifyValueChangeForKeyPath(currentExposureValues, convertKeyToPath(CameraKeys.EXPOSURE_SETTINGS));
            notifyValueChangeForKeyPath(iso, convertKeyToPath(CameraKeys.ISO));
            notifyValueChangeForKeyPath(exposureCompensation, convertKeyToPath(CameraKeys.EXPOSURE_COMPENSATION));
            notifyValueChangeForKeyPath(aperture, convertKeyToPath(CameraKeys.APERTURE));
            notifyValueChangeForKeyPath(shutterSpeed, convertKeyToPath(CameraKeys.SHUTTER_SPEED));
            notifyValueChangeForKeyPath(videoStandard, convertKeyToPath(CameraKeys.VIDEO_STANDARD));
            notifyValueChangeForKeyPath(Boolean.valueOf(isAELock), convertKeyToPath(CameraKeys.AE_LOCK));
            notifyValueChangeForKeyPath(fileFormat, convertKeyToPath(CameraKeys.PHOTO_FILE_FORMAT));
            notifyValueChangeForKeyPath(photoTimeIntervalSettings, convertKeyToPath(CameraKeys.PHOTO_TIME_INTERVAL_SETTINGS));
            notifyValueChangeForKeyPath(exposureMode, convertKeyToPath(CameraKeys.EXPOSURE_MODE));
            notifyValueChangeForKeyPath(exposureState, convertKeyToPath(CameraKeys.EXPOSURE_STATE));
            notifyValueChangeForKeyPath(videoResolutionAndFrameRate, convertKeyToPath(CameraKeys.RESOLUTION_FRAME_RATE));
            notifyValueChangeForKeyPath(videoFileFormat, convertKeyToPath(CameraKeys.VIDEO_FILE_FORMAT));
            notifyValueChangeForKeyPath(exposureMeteringMode, convertKeyToPath(CameraKeys.METERING_MODE));
            notifyValueChangeForKeyPath(wb, convertKeyToPath(CameraKeys.WHITE_BALANCE));
            notifyValueChangeForKeyPath(cameraColor, convertKeyToPath(CameraKeys.CAMERA_COLOR));
            notifyValueChangeForKeyPath(antiFlicker, convertKeyToPath(CameraKeys.ANTI_FLICKER_FREQUENCY));
            notifyValueChangeForKeyPath(transformShootPhotoMode(shootPhotoMode, DataEyeGetPushPseudoCameraParams.getInstance()), convertKeyToPath(CameraKeys.SHOOT_PHOTO_MODE));
            notifyValueChangeForKeyPath(Integer.valueOf(pushShotParams.getTimeCountdown()), convertKeyToPath(CameraKeys.INTERVAL_SHOOT_COUNTDOWN));
            notifyValueChangeForKeyPath(Integer.valueOf(pushShotParams.getRawBurstNumber()), convertKeyToPath(CameraKeys.RAWBURST_SHOOT_NUMBER));
            notifyValueChangeForKeyPath(realShutterSpeed, convertKeyToPath(CameraKeys.REAL_SHUTTER_SPEED));
            notifyValueChangeForKeyPath(Boolean.valueOf(pushShotParams.autoTurnOffForeLed()), convertKeyToPath(CameraKeys.LED_AUTO_TURN_OFF_ENABLED));
            notifyValueChangeForKeyPath(LEDsSettings.generateLEDsEnabledSettings(pushShotParams.getLedArmControl()), convertKeyToPath(CameraKeys.LEDS_AUTO_TURN_OFF_ENABLED_SETTINGS));
            notifyValueChangeForKeyPath(new SettingsDefinitions.PictureStylePreset.Builder().sharpness(b).saturation(b2).contrast(contrast).build(), convertKeyToPath(CameraKeys.PICTURE_STYLE_PRESET));
            if (isMechanicalShutterSupported()) {
                notifyValueChangeForKeyPath(Boolean.valueOf(pushShotParams.isMechanicShutterEnable()), convertKeyToPath(CameraKeys.MECHANICAL_SHUTTER_ENABLED));
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataEyeGetPushPseudoCameraParams params) {
        super.onEvent3BackgroundThread(params);
        SettingsDefinitions.ShootPhotoMode mode = getShootPhotoModeFrom(DataCameraGetPushShotParams.getInstance());
        if (params.isGetted()) {
            SettingsDefinitions.ShootPhotoMode shootPhotoMode = transformShootPhotoMode(mode, params);
            if (shootPhotoMode == SettingsDefinitions.ShootPhotoMode.SHALLOW_FOCUS || shootPhotoMode == SettingsDefinitions.ShootPhotoMode.PANORAMA) {
                notifyValueChangeForKeyPath(shootPhotoMode, convertKeyToPath(CameraKeys.SHOOT_PHOTO_MODE));
            }
            notifyValueChangeForKeyPath(transformPhotoPanoMode(params), convertKeyToPath(CameraKeys.PHOTO_PANORAMA_MODE));
        }
    }

    /* access modifiers changed from: protected */
    public SettingsDefinitions.ShootPhotoMode transformShootPhotoMode(SettingsDefinitions.ShootPhotoMode mode, DataEyeGetPushPseudoCameraParams params) {
        if (!params.isGetted()) {
            return mode;
        }
        if (params.getCameraMode() == DataEyeGetPushPseudoCameraParams.PseudoCameraMode.PSEUDO_CAMERA_MODE_BOKEH) {
            return SettingsDefinitions.ShootPhotoMode.SHALLOW_FOCUS;
        }
        if (!isPanoramaMode(params)) {
            return mode;
        }
        notifyValueChangeForKeyPath(transformPhotoPanoMode(params), convertKeyToPath(CameraKeys.PHOTO_PANORAMA_MODE));
        return SettingsDefinitions.ShootPhotoMode.PANORAMA;
    }

    /* access modifiers changed from: protected */
    public SettingsDefinitions.PhotoPanoramaMode transformPhotoPanoMode(DataEyeGetPushPseudoCameraParams params) {
        if (params.isGetted()) {
            if (params.getCameraMode() == DataEyeGetPushPseudoCameraParams.PseudoCameraMode.PSEUDO_CAMERA_MODE_PANO_3x3) {
                return SettingsDefinitions.PhotoPanoramaMode.PANORAMA_MODE_3X3;
            }
            if (params.getCameraMode() == DataEyeGetPushPseudoCameraParams.PseudoCameraMode.PSEUDO_CAMERA_MODE_PANO_1x3) {
                return SettingsDefinitions.PhotoPanoramaMode.PANORAMA_MODE_3X1;
            }
            if (params.getCameraMode() == DataEyeGetPushPseudoCameraParams.PseudoCameraMode.PSEUDO_CAMERA_MODE_PANO_3x1) {
                return SettingsDefinitions.PhotoPanoramaMode.PANORAMA_MODE_1X3;
            }
            if (params.getCameraMode() == DataEyeGetPushPseudoCameraParams.PseudoCameraMode.PSEUDO_CAMERA_MODE_PANO_180) {
                return SettingsDefinitions.PhotoPanoramaMode.PANORAMA_MODE_180;
            }
            if (params.getCameraMode() == DataEyeGetPushPseudoCameraParams.PseudoCameraMode.PSEUDO_CAMERA_MODE_PANO_SPHERE) {
                return SettingsDefinitions.PhotoPanoramaMode.PANORAMA_MODE_SPHERE;
            }
            if (params.getCameraMode() == DataEyeGetPushPseudoCameraParams.PseudoCameraMode.PSEUDO_CAMERA_MODE_PANO_SUPER_RESOLUTION) {
                return SettingsDefinitions.PhotoPanoramaMode.PANORAMA_MODE_SUPER_RESOLUTION;
            }
        }
        return SettingsDefinitions.PhotoPanoramaMode.UNKNOWN;
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCameraGetPushShotInfo pushShotInfo) {
        boolean isAFSwitchOn;
        if (pushShotInfo.isGetted()) {
            if (isValidSender(pushShotInfo.getSenderId())) {
                DataCameraGetPushStateInfo.CameraType type = DataCameraGetPushStateInfo.getInstance().getCameraType(getExpectedSenderIdByIndex());
                if (isChangeableLensSupported()) {
                    if ((this.isLensMounted && !pushShotInfo.isShotConnected()) || (!this.isLensMounted && pushShotInfo.isShotConnected())) {
                        trigerLensChanged();
                    }
                    this.isLensMounted = pushShotInfo.isShotConnected();
                } else {
                    if (!this.isLensMounted) {
                        trigerLensChanged();
                    }
                    this.isLensMounted = true;
                }
                if (type == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC550 || type == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC550Raw || type == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6520) {
                    isAFSwitchOn = pushShotInfo.getShotFocusMode() == DataCameraGetPushShotInfo.ShotFocusMode.Auto;
                } else {
                    isAFSwitchOn = true;
                }
                boolean isFocusAssistantWorking = 1 == pushShotInfo.getMFFocusStatus();
                FocusAssistantSettings focusAssistantSettings = new FocusAssistantSettings(pushShotInfo.isDigitalFocusAEnable(), pushShotInfo.isDigitalFocusMEnable());
                SettingsDefinitions.FocusStatus focusStatus = SettingsDefinitions.FocusStatus.find(pushShotInfo.getFocusStatus());
                SettingsDefinitions.FocusMode focusMode = updateCameraLensFocusMode(pushShotInfo);
                int lensFocusRingValue = pushShotInfo.getShotFocusCurStroke();
                if (this.isMFInverted) {
                    if (this.maxFocalDistance == -1) {
                        this.maxFocalDistance = pushShotInfo.getShotFocusMaxStroke();
                    }
                    lensFocusRingValue = this.maxFocalDistance - lensFocusRingValue;
                }
                int demarcateValue = pushShotInfo.getDemarcateValue();
                PointF point = new PointF(pushShotInfo.getSpotAFAxisX(), pushShotInfo.getSpotAFAxisY());
                notifyValueChangeForKeyPath(Boolean.valueOf(this.isLensMounted), convertKeyToPath(CameraKeys.LENS_IS_INSTALLED));
                notifyValueChangeForKeyPath(Boolean.valueOf(isAFSwitchOn), convertKeyToPath(CameraKeys.LENS_IS_AF_SWITCH_ON));
                notifyValueChangeForKeyPath(focusStatus, convertKeyToPath(CameraKeys.FOCUS_STATUS));
                notifyValueChangeForKeyPath(focusMode, convertKeyToPath(CameraKeys.FOCUS_MODE));
                notifyValueChangeForKeyPath(Boolean.valueOf(isFocusAssistantWorking), convertKeyToPath(CameraKeys.LENS_IS_FOCUS_ASSISTANT_WORKING));
                notifyValueChangeForKeyPath(focusAssistantSettings, convertKeyToPath(CameraKeys.FOCUS_ASSISTANT_SETTINGS));
                notifyValueChangeForKeyPath(Integer.valueOf(lensFocusRingValue), convertKeyToPath(CameraKeys.FOCUS_RING_VALUE));
                notifyValueChangeForKeyPath(point, convertKeyToPath(CameraKeys.FOCUS_TARGET));
                notifyValueChangeForKeyPath(Integer.valueOf(demarcateValue), convertKeyToPath(CameraKeys.DEMARCATE_VALUE));
                if (isOpticalZoomSupported()) {
                    notifyValueChangeForKeyPath(Integer.valueOf(pushShotInfo.getCurFocusDistance()), convertKeyToPath(CameraKeys.OPTICAL_ZOOM_FOCAL_LENGTH));
                    notifyValueChangeForKeyPath(new SettingsDefinitions.OpticalZoomSpec(pushShotInfo.getMaxFocusDistance(getExpectedSenderIdByIndex()), pushShotInfo.getMinFocusDistance(getExpectedSenderIdByIndex()), pushShotInfo.getMinFocusDistanceStep(getExpectedSenderIdByIndex())), CameraKeys.OPTICAL_ZOOM_SPEC);
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCameraGetPushChartInfo chartInfo) {
        if (chartInfo.isGetted() && isValidSender(chartInfo.getSenderId())) {
            notifyValueChangeForKeyPath(chartInfo.getParams(), convertKeyToPath(CameraKeys.HISTOGRAM_LIGHT_VALUES));
        }
    }

    /* access modifiers changed from: protected */
    public void trigerLensChanged() {
    }

    @Setter("Mode")
    public void setCameraMode(SettingsDefinitions.CameraMode mode, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (mode == SettingsDefinitions.CameraMode.UNKNOWN) {
            if (callback != null) {
                callback.onFails(DJICameraError.INVALID_PARAMETERS);
            }
        } else if (checkCapabilityForWorkMode(mode)) {
            setCameraModeProtocol(mapCameraModeToProtocolValue(mode), callback);
        } else if (callback != null) {
            callback.onFails(DJICameraError.COMMON_UNSUPPORTED);
        }
    }

    /* access modifiers changed from: protected */
    public int mapCameraModeToProtocolValue(SettingsDefinitions.CameraMode mode) {
        if (SettingsDefinitions.CameraMode.MEDIA_DOWNLOAD == mode) {
            return 7;
        }
        if (SettingsDefinitions.CameraMode.BROADCAST == mode) {
            return -1;
        }
        return mode.value();
    }

    /* access modifiers changed from: protected */
    public void setCameraModeProtocol(int value, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataCameraSetMode setter = DataCameraSetMode.getInstance();
        setter.setReceiverId(getReceiverIdByIndex());
        setter.setMode(DataCameraGetMode.MODE.find(value));
        setter.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.AnonymousClass1 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, model);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJICameraError.getDJIError(ccode));
            }
        });
    }

    private boolean checkCapabilityForWorkMode(SettingsDefinitions.CameraMode mode) {
        if (!isBroadcastModeSupported() && mode == SettingsDefinitions.CameraMode.BROADCAST) {
            return false;
        }
        if (!isPlaybackSupported() && mode == SettingsDefinitions.CameraMode.PLAYBACK) {
            return false;
        }
        if (isMediaDownloadModeSupported() || mode != SettingsDefinitions.CameraMode.MEDIA_DOWNLOAD) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isShootPhotoModeSupported(SettingsDefinitions.ShootPhotoMode photoMode, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (photoMode == null) {
            CallbackUtils.onFailure(callback, DJICameraError.INVALID_PARAMETERS);
            return false;
        } else if (!isTimeLapseSupported() && photoMode == SettingsDefinitions.ShootPhotoMode.TIME_LAPSE) {
            CallbackUtils.onFailure(callback, DJICameraError.INVALID_PARAMETERS);
            return false;
        } else if (!isHDRPhotoSupported() && photoMode == SettingsDefinitions.ShootPhotoMode.HDR) {
            CallbackUtils.onFailure(callback, DJICameraError.INVALID_PARAMETERS);
            return false;
        } else if (!isShootPhotoRawBurstSupported() && photoMode == SettingsDefinitions.ShootPhotoMode.RAW_BURST) {
            CallbackUtils.onFailure(callback, DJICameraError.INVALID_PARAMETERS);
            return false;
        } else if (!isShootShallowFocusSupported() && photoMode == SettingsDefinitions.ShootPhotoMode.SHALLOW_FOCUS) {
            CallbackUtils.onFailure(callback, DJICameraError.INVALID_PARAMETERS);
            return false;
        } else if (isShootPanoramaSupported() || photoMode != SettingsDefinitions.ShootPhotoMode.PANORAMA) {
            return true;
        } else {
            CallbackUtils.onFailure(callback, DJICameraError.INVALID_PARAMETERS);
            return false;
        }
    }

    /* access modifiers changed from: protected */
    public void startSetShootPhotoModeCmd(SettingsDefinitions.ShootPhotoMode photoMode, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataCameraSetPhotoMode setter = prepareShootPhotoMode(photoMode);
        setter.setReceiverId(getReceiverIdByIndex());
        setter.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.AnonymousClass2 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, model);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJICameraError.getDJIError(ccode));
            }
        });
    }

    @Setter(CameraKeys.SHOOT_PHOTO_MODE)
    public void setShootPhotoMode(SettingsDefinitions.ShootPhotoMode photoMode, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (isShootPhotoModeSupported(photoMode, callback)) {
            startSetShootPhotoModeCmd(photoMode, callback);
        }
    }

    @Setter(CameraKeys.PHOTO_PANORAMA_MODE)
    public void setPhotoPanoramaMode(SettingsDefinitions.PhotoPanoramaMode panoramaMode, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!isSupportCurrentPanoramaMode(panoramaMode.value())) {
            CallbackUtils.onFailure(callback, DJICameraError.COMMAND_NOT_SUPPORTED_BY_FIRMWARE);
            return;
        }
        final DataEyeSetPseudoCameraControl setter = new DataEyeSetPseudoCameraControl();
        setter.setPseudoCameraCmd(DataEyeSetPseudoCameraControl.PseudoCameraCmd.find(panoramaMode.value())).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.AnonymousClass3 */

            public void onSuccess(Object model) {
                if (setter.getPseudoCameraCmdResult() == DataEyeSetPseudoCameraControl.PseudoCameraCmdResult.PSEUDO_CAMERA_ACK_SUCCESS) {
                    CallbackUtils.onSuccess(callback, (Object) null);
                } else {
                    CallbackUtils.onFailure(callback, DJICameraError.COMMON_EXECUTION_FAILED);
                }
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJICameraError.COMMON_EXECUTION_FAILED);
            }
        });
    }

    /* access modifiers changed from: protected */
    public boolean isSupportCurrentPanoramaMode(int panoramaModeValue) {
        int[] panoramaRange;
        int[][] childRange = (int[][]) CacheHelper.getCamera(this.index, CameraKeys.SHOOT_PHOTO_MODE_CHILD_RANGE);
        if (childRange == null || (panoramaRange = childRange[SettingsDefinitions.ShootPhotoMode.PANORAMA.value()]) == null || panoramaRange.length <= 0 || !checkValueInArray(panoramaRange, panoramaModeValue)) {
            return false;
        }
        return true;
    }

    @Setter(CameraKeys.FILE_INDEX_MODE)
    public void setFileIndexMode(SettingsDefinitions.FileIndexMode fileIndexMode, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (fileIndexMode != SettingsDefinitions.FileIndexMode.UNKNOWN) {
            DataBaseCameraSetting setter = new DataBaseCameraSetting();
            setter.setCmdId(CameraKeys.FILE_INDEX_MODE);
            setter.setValue(fileIndexMode.value());
            setter.setReceiverId(getReceiverIdByIndex());
            setter.start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.AnonymousClass4 */

                public void onSuccess(Object model) {
                    CallbackUtils.onSuccess(callback, model);
                }

                public void onFailure(Ccode ccode) {
                    CallbackUtils.onFailure(callback, DJICameraError.getDJIError(ccode));
                }
            });
        } else if (callback != null) {
            callback.onFails(DJICameraError.COMMON_PARAM_ILLEGAL);
        }
    }

    @Getter(CameraKeys.FILE_INDEX_MODE)
    public void getFileIndexMode(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataBaseCameraGetting getter = new DataBaseCameraGetting();
        getter.setCmdId(CameraKeys.FILE_INDEX_MODE);
        getter.setReceiverId(getReceiverIdByIndex());
        getter.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.AnonymousClass5 */

            public void onSuccess(Object model) {
                if (callback != null) {
                    callback.onSuccess(SettingsDefinitions.FileIndexMode.find(getter.getValue()));
                }
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJICameraError.getDJIError(ccode));
            }
        });
    }

    @Setter(CameraKeys.RESOLUTION_FRAME_RATE)
    public void setVideoResolutionAndFrameRate(ResolutionAndFrameRate resolutionAndFrameRate, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        SettingsDefinitions.VideoResolution videoResolution = resolutionAndFrameRate.getResolution();
        SettingsDefinitions.VideoFrameRate videoFrameRate = resolutionAndFrameRate.getFrameRate();
        SettingsDefinitions.VideoFov videFovType = resolutionAndFrameRate.getFov();
        if (checkIfSupported(videoResolution, videoFrameRate)) {
            int ratio = DJICameraEnumMappingUtil.getResolutionProtocolValue(videoResolution);
            int fps = DJICameraEnumMappingUtil.getFrameRateProtocolValue(videoFrameRate);
            int fov = 0;
            if (videFovType != null) {
                fov = videFovType.value();
            }
            if (ratio != -1 && fps != -1) {
                DataCameraSetVideoFormat setter = new DataCameraSetVideoFormat();
                setter.setReceiverId(getReceiverIdByIndex());
                setter.setAll();
                setter.setRatio(ratio);
                setter.setFps(fps);
                setter.setFov(fov);
                setter.start(new DJIDataCallBack() {
                    /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.AnonymousClass6 */

                    public void onSuccess(Object model) {
                        CallbackUtils.onSuccess(callback, model);
                    }

                    public void onFailure(Ccode ccode) {
                        CallbackUtils.onFailure(callback, DJICameraError.getDJIError(ccode));
                    }
                });
            } else if (callback != null) {
                callback.onFails(DJICameraError.COMMON_PARAM_ILLEGAL);
            }
        } else if (callback != null) {
            callback.onFails(DJICameraError.COMMON_UNSUPPORTED);
        }
    }

    /* access modifiers changed from: protected */
    public boolean checkIfSupported(SettingsDefinitions.VideoResolution resolution, SettingsDefinitions.VideoFrameRate videoFrameRate) {
        if ((videoFrameRate == SettingsDefinitions.VideoFrameRate.FRAME_RATE_120_FPS || videoFrameRate == SettingsDefinitions.VideoFrameRate.FRAME_RATE_96_FPS) && !isSlowMotionSupported() && !getDisplayName().equals(DJICameraAbstraction.DisplayNameX4S) && !getDisplayName().equals(DJICameraAbstraction.DisplayNameX5S) && !getDisplayName().equals(DJICameraAbstraction.DisplaynamePhantom4ProCamera)) {
            return false;
        }
        return true;
    }

    @Setter(CameraKeys.VIDEO_FOV_TYPE)
    public void setVideoFovType(SettingsDefinitions.VideoFov fovInput, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        int fov = fovInput.value();
        DataCameraSetVideoFormat setter = new DataCameraSetVideoFormat();
        setter.setReceiverId(getReceiverIdByIndex());
        setter.setAll();
        setter.setFov(fov);
        setter.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.AnonymousClass7 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, model);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJICameraError.getDJIError(ccode));
            }
        });
    }

    @Getter(CameraKeys.VIDEO_FOV_TYPE)
    public void getVideoFovType(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataCameraGetVideoFormat getter = new DataCameraGetVideoFormat();
        getter.setReceiverId(getReceiverIdByIndex());
        getter.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.AnonymousClass8 */

            public void onSuccess(Object model) {
                if (callback != null) {
                    callback.onSuccess(SettingsDefinitions.VideoFov.find(getter.getFov()));
                }
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJICameraError.getDJIError(ccode));
            }
        });
    }

    @Setter(CameraKeys.VIDEO_STANDARD)
    public void setVideoStandard(final SettingsDefinitions.VideoStandard videoStandard, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!ServiceManager.getInstance().isConnected()) {
            callback.onFails(DJICameraError.NOT_CONNECTED);
        } else if (videoStandard == SettingsDefinitions.VideoStandard.UNKNOWN) {
            callback.onFails(DJICameraError.COMMON_PARAM_ILLEGAL);
        } else {
            DataBaseCameraSetting setter = new DataBaseCameraSetting();
            setter.setCmdId("Standard");
            setter.setReceiverId(getReceiverIdByIndex());
            setter.setValue(videoStandard.value());
            setter.start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.AnonymousClass9 */

                public void onSuccess(Object model) {
                    callback.onSuccess(null);
                }

                public void onFailure(Ccode ccode) {
                    if (videoStandard.value() == DataCameraGetPushShotParams.getInstance().getVideoStandard(DJICameraBaseAbstraction.this.getExpectedSenderIdByIndex())) {
                        if (callback != null) {
                            callback.onSuccess(null);
                        }
                    } else if (callback != null) {
                        callback.onFails(DJICameraError.PARAMETERS_SET_FAILED);
                    }
                }
            });
        }
    }

    @Setter(CameraKeys.PHOTO_ASPECT_RATIO)
    public void setPhotoRatio(SettingsDefinitions.PhotoAspectRatio photoAspectRatio, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataCameraGetImageSize.SizeType sizeType = DataCameraGetImageSize.SizeType.DEFAULT;
        DataCameraGetImageSize.RatioType ratioType = DataCameraGetImageSize.RatioType.find(photoAspectRatio.value());
        if (ratioType != DataCameraGetImageSize.RatioType.OTHER) {
            DataCameraSetImageSize setter = DataCameraSetImageSize.getInstance();
            setter.setReceiverId(getReceiverIdByIndex());
            setter.setSize(sizeType).setRatio(ratioType).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.AnonymousClass10 */

                public void onSuccess(Object model) {
                    CallbackUtils.onSuccess(callback, model);
                }

                public void onFailure(Ccode ccode) {
                    CallbackUtils.onFailure(callback, DJICameraError.getDJIError(ccode));
                }
            });
        } else if (callback != null) {
            callback.onFails(DJICameraError.COMMON_PARAM_ILLEGAL);
        }
    }

    @Setter(CameraKeys.PHOTO_FILE_FORMAT)
    public void setPhotoFileFormat(final SettingsDefinitions.PhotoFileFormat photoFormat, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        SettingsDefinitions.PhotoTimeIntervalSettings intervalSettings;
        if (photoFormat != SettingsDefinitions.PhotoFileFormat.UNKNOWN) {
            SettingsDefinitions.PhotoFileFormat[] range = (SettingsDefinitions.PhotoFileFormat[]) CacheHelper.getCamera(this.index, CameraKeys.PHOTO_FILE_FORMAT_RANGE);
            boolean isValid = false;
            if (range != null) {
                int length = range.length;
                int i = 0;
                while (true) {
                    if (i >= length) {
                        break;
                    } else if (range[i] == photoFormat) {
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
            SettingsDefinitions.PhotoTimeIntervalSettings intervalSettings2 = (SettingsDefinitions.PhotoTimeIntervalSettings) CacheHelper.getCamera(this.index, CameraKeys.PHOTO_TIME_INTERVAL_SETTINGS);
            int[] timeRange = CameraParamRangeManager.getIntervalTimeRange(photoFormat, CameraUtils.getCameraType(this.index));
            if (!checkIntervalTime(intervalSettings2, timeRange)) {
                int interval = findBestIntervalTime(intervalSettings2, timeRange);
                if (intervalSettings2 == null) {
                    intervalSettings = new SettingsDefinitions.PhotoTimeIntervalSettings(1, interval);
                } else {
                    intervalSettings = new SettingsDefinitions.PhotoTimeIntervalSettings(intervalSettings2.getCaptureCount(), interval);
                }
                CacheHelper.setCamera(CameraKeys.PHOTO_TIME_INTERVAL_SETTINGS, intervalSettings, new DJISetCallback() {
                    /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.AnonymousClass11 */

                    public void onSuccess() {
                        DJICameraBaseAbstraction.this.setImageFormat(photoFormat, callback);
                    }

                    public void onFails(DJIError error) {
                        if (callback != null) {
                            callback.onFails(error);
                        }
                    }
                });
                return;
            }
            setImageFormat(photoFormat, callback);
        } else if (callback != null) {
            callback.onFails(DJICameraError.INVALID_PARAMETERS);
        }
    }

    /* access modifiers changed from: protected */
    public void setImageFormat(SettingsDefinitions.PhotoFileFormat photoFormat, DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataBaseCameraSetting setter = new DataBaseCameraSetting();
        setter.setReceiverId(getReceiverIdByIndex());
        setter.setCmdId("ImageFormat");
        setter.setValue(photoFormat.value());
        setter.start(CallbackUtils.defaultCB(callback));
    }

    private boolean checkIntervalTime(SettingsDefinitions.PhotoTimeIntervalSettings intervalSettings, int[] ranges) {
        if (intervalSettings == null) {
            return false;
        }
        int interval = intervalSettings.getTimeIntervalInSeconds();
        if (ranges == null || ranges.length == 0) {
            return true;
        }
        for (int value : ranges) {
            if (value == interval) {
                return true;
            }
        }
        return false;
    }

    private int findBestIntervalTime(SettingsDefinitions.PhotoTimeIntervalSettings intervalSettings, int[] ranges) {
        int interval = intervalSettings == null ? 0 : intervalSettings.getTimeIntervalInSeconds();
        if (ranges == null || ranges.length == 0) {
            return 5;
        }
        int i = ranges[0];
        Arrays.sort(ranges);
        for (int value : ranges) {
            if (value >= interval) {
                return value;
            }
        }
        return i;
    }

    @Setter(CameraKeys.PHOTO_BURST_COUNT)
    public void setPhotoBurstCount(SettingsDefinitions.PhotoBurstCount count, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!isBurstCountSupported(count)) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_ILLEGAL);
            return;
        }
        DataBaseCameraSetting setter = new DataBaseCameraSetting();
        setter.setCmdId("Continuous");
        setter.setValue(count.value());
        setter.setReceiverId(getReceiverIdByIndex());
        setter.setPackParam(0, 1);
        setter.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.AnonymousClass12 */

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
    public boolean isBurstCountSupported(SettingsDefinitions.PhotoBurstCount count) {
        return true;
    }

    @Setter(CameraKeys.PHOTO_TIME_INTERVAL_SETTINGS)
    public void setPhotoIntervalParam(SettingsDefinitions.PhotoTimeIntervalSettings param, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (param == null) {
            if (callback != null) {
                callback.onFails(DJICameraError.COMMON_PARAM_ILLEGAL);
            }
        } else if (isPhotoIntervalParamValid(param.getTimeIntervalInSeconds(), param.getCaptureCount())) {
            DataCameraSetTimeParams setter = DataCameraSetTimeParams.getInstance();
            setter.setReceiverId(getReceiverIdByIndex());
            setter.setNum(param.getCaptureCount()).setPeriod(param.getTimeIntervalInSeconds()).setType(DataCameraSetTimeParams.TYPE.Single).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.AnonymousClass13 */

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

    private boolean isOrangeFC550() {
        return this.cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC550;
    }

    private boolean cantShutterAdjust() {
        SettingsDefinitions.ExposureMode exposureMode = (SettingsDefinitions.ExposureMode) DJISDKCache.getInstance().getAvailableValue(this.defaultKeyPath.clone(CameraKeys.EXPOSURE_MODE)).getData();
        if (isOrangeFC550()) {
            if (exposureMode == SettingsDefinitions.ExposureMode.PROGRAM || exposureMode == SettingsDefinitions.ExposureMode.APERTURE_PRIORITY) {
                return true;
            }
            return false;
        } else if (exposureMode == SettingsDefinitions.ExposureMode.PROGRAM) {
            return true;
        } else {
            return false;
        }
    }

    @Setter(CameraKeys.EXPOSURE_MODE)
    public void setExposureMode(SettingsDefinitions.ExposureMode exposureMode, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (exposureMode != SettingsDefinitions.ExposureMode.UNKNOWN) {
            DataCameraSetExposureMode setter = DataCameraSetExposureMode.getInstance();
            setter.setReceiverId(getReceiverIdByIndex());
            setter.setExpMode(exposureMode.value()).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.AnonymousClass14 */

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

    @Setter(CameraKeys.ISO)
    public void setISO(SettingsDefinitions.ISO iso, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataCameraGetIso.TYPE type = DataCameraGetIso.TYPE.find(iso.value());
        if (type == DataCameraGetIso.TYPE.OTHER) {
            if (callback != null) {
                callback.onFails(DJICameraError.INVALID_PARAMETERS);
            }
        } else if (DataCameraGetPushShotParams.getInstance().getExposureMode(getExpectedSenderIdByIndex()) != DataCameraSetExposureMode.ExposureMode.M || type != DataCameraGetIso.TYPE.AUTO) {
            DataCameraSetIso setter = new DataCameraSetIso();
            setter.setType(true);
            setter.setReceiverId(getReceiverIdByIndex());
            setter.setAbsValue(type);
            setter.start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.AnonymousClass15 */

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
            callback.onFails(DJICameraError.CANNOT_SET_PARAMETERS_IN_THIS_STATE);
        }
    }

    @Setter(CameraKeys.SHUTTER_SPEED)
    public void setShutterSpeed(SettingsDefinitions.ShutterSpeed shutterSpeed, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        boolean z = true;
        if (shutterSpeed == null) {
            if (callback != null) {
                callback.onFails(DJICameraError.INVALID_PARAMETERS);
            }
        } else if (!cantShutterAdjust()) {
            int isReciprocal = -1;
            int integral = -1;
            int decimal = -1;
            int[] cmdData = CameraUtils.getShutterSpeedCmdData(shutterSpeed);
            if (cmdData != null && cmdData.length == 3) {
                isReciprocal = cmdData[0];
                integral = cmdData[1];
                decimal = cmdData[2];
            }
            if (isReciprocal != -1 && integral != -1 && decimal != -1) {
                DataCameraSetShutterSpeed setter = DataCameraSetShutterSpeed.getInstance();
                setter.setReceiverId(getReceiverIdByIndex());
                if (isReciprocal != 1) {
                    z = false;
                }
                setter.setAbsolute(z, integral, decimal).start(new DJIDataCallBack() {
                    /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.AnonymousClass16 */

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
            callback.onFails(DJICameraError.CANNOT_SET_PARAMETERS_IN_THIS_STATE);
        }
    }

    @Setter(CameraKeys.WHITE_BALANCE)
    public void setWhiteBalanceAndColorTemperature(WhiteBalance value, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        SettingsDefinitions.WhiteBalancePreset whiteBalancePreset = value.getWhiteBalancePreset();
        int colorTemperature = value.getColorTemperature();
        if (checkWhiteBaLanceValid(whiteBalancePreset, colorTemperature)) {
            DataCameraSetWhiteBalance setter = new DataCameraSetWhiteBalance();
            setter.setReceiverId(getReceiverIdByIndex());
            if (SettingsDefinitions.WhiteBalancePreset.CUSTOM == whiteBalancePreset) {
                setter.setAll();
                setter.setType(6);
                setter.setColorTemp(colorTemperature);
            } else {
                setter.setAll();
                setter.setType(whiteBalancePreset.value());
                setter.setColorTemp(0);
            }
            setter.start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.AnonymousClass17 */

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

    /* access modifiers changed from: protected */
    public boolean checkWhiteBaLanceValid(SettingsDefinitions.WhiteBalancePreset whiteBalancePreset, int colorTemperature) {
        if (whiteBalancePreset == SettingsDefinitions.WhiteBalancePreset.UNKNOWN) {
            return false;
        }
        if (SettingsDefinitions.WhiteBalancePreset.CUSTOM != whiteBalancePreset || (colorTemperature >= 20 && colorTemperature <= 100)) {
            return true;
        }
        return false;
    }

    @Setter(CameraKeys.METERING_MODE)
    public void setMeteringMode(SettingsDefinitions.MeteringMode meteringMode, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (meteringMode.value() != SettingsDefinitions.MeteringMode.UNKNOWN.value()) {
            DataBaseCameraSetting setter = new DataBaseCameraSetting();
            setter.setReceiverId(getReceiverIdByIndex());
            setter.setCmdId(CmdIdCamera.CmdIdType.SetMetering);
            setter.setValue(meteringMode.value());
            setter.start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.AnonymousClass18 */

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

    @Setter(CameraKeys.EXPOSURE_COMPENSATION)
    public void setExposureCompensation(SettingsDefinitions.ExposureCompensation exposureCompensation, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (exposureCompensation == SettingsDefinitions.ExposureCompensation.UNKNOWN) {
            CallbackUtils.onFailure(callback, DJICameraError.INVALID_PARAMETERS);
        } else if (CacheHelper.getCamera(this.index, CameraKeys.EXPOSURE_MODE) == SettingsDefinitions.ExposureMode.MANUAL) {
            CallbackUtils.onFailure(callback, DJICameraError.CANNOT_SET_PARAMETERS_IN_THIS_STATE);
        } else {
            DataBaseCameraSetting setter = new DataBaseCameraSetting();
            setter.setReceiverId(getReceiverIdByIndex());
            setter.setCmdId(CameraKeys.EXPOSURE_COMPENSATION);
            setter.setValue(exposureCompensation.value());
            setter.setPackParam(0, 1);
            setter.start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.AnonymousClass19 */

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

    @Setter(CameraKeys.ANTI_FLICKER_FREQUENCY)
    public void setAntiFlicker(SettingsDefinitions.AntiFlickerFrequency antiFlicker, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (DataCameraGetPushShotParams.getInstance().getExposureMode(getExpectedSenderIdByIndex()) != DataCameraSetExposureMode.ExposureMode.P) {
            if (callback != null) {
                callback.onFails(DJICameraError.CANNOT_SET_PARAMETERS_IN_THIS_STATE);
            }
        } else if (antiFlicker != SettingsDefinitions.AntiFlickerFrequency.UNKNOWN) {
            DataBaseCameraSetting setter = new DataBaseCameraSetting();
            setter.setReceiverId(getReceiverIdByIndex());
            setter.setCmdId("AntiFlicker");
            setter.setValue(antiFlicker.value());
            setter.setPackParam(0, 1);
            setter.start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.AnonymousClass20 */

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

    @Setter(CameraKeys.SHARPNESS)
    public void setSharpness(int sharpness, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (sharpness >= -3 && sharpness <= 3) {
            DataBaseCameraSetting setter = new DataBaseCameraSetting();
            setter.setReceiverId(getReceiverIdByIndex());
            setter.setCmdId("Sharpe");
            setter.setValue(sharpness);
            setter.setPackParam(0, 1);
            setter.start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.AnonymousClass21 */

                public void onSuccess(Object model) {
                    CallbackUtils.onSuccess(callback, (Object) null);
                }

                public void onFailure(Ccode ccode) {
                    CallbackUtils.onFailure(callback, ccode);
                }
            });
        } else if (callback != null) {
            callback.onFails(DJICameraError.INVALID_PARAMETERS);
        }
    }

    @Setter(CameraKeys.CONTRAST)
    public void setContrast(int contrast, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (contrast < -3 || contrast > 3) {
            CallbackUtils.onFailure(callback, DJICameraError.INVALID_PARAMETERS);
            return;
        }
        DataBaseCameraSetting setter = new DataBaseCameraSetting();
        setter.setReceiverId(getReceiverIdByIndex());
        setter.setCmdId(CameraKeys.CONTRAST);
        setter.setValue(contrast);
        setter.setPackParam(0, 1);
        setter.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.AnonymousClass22 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, (Object) null);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    @Setter(CameraKeys.SATURATION)
    public void setSaturation(int saturation, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (saturation < -3 || saturation > 3) {
            CallbackUtils.onFailure(callback, DJICameraError.INVALID_PARAMETERS);
            return;
        }
        DataBaseCameraSetting setter = new DataBaseCameraSetting();
        setter.setCmdId(CameraKeys.SATURATION);
        setter.setValue(saturation);
        setter.setReceiverId(getReceiverIdByIndex());
        setter.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.AnonymousClass23 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, (Object) null);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    @Setter(CameraKeys.PICTURE_STYLE_PRESET)
    public void setPictureStylePreset(SettingsDefinitions.PictureStylePreset style, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (style != null) {
            DataCommonSetMultiParam setter = new DataCommonSetMultiParam();
            byte[] mCmdBytes = new byte[10];
            mCmdBytes[0] = 3;
            mCmdBytes[1] = (byte) CmdIdCamera.CmdIdType.SetContrast.value();
            mCmdBytes[2] = 1;
            mCmdBytes[4] = (byte) CmdIdCamera.CmdIdType.SetSaturation.value();
            mCmdBytes[5] = 1;
            mCmdBytes[7] = (byte) CmdIdCamera.CmdIdType.SetSharpe.value();
            mCmdBytes[8] = 1;
            mCmdBytes[3] = (byte) style.getContrast();
            mCmdBytes[6] = (byte) style.getSaturation();
            mCmdBytes[9] = (byte) style.getSharpness();
            setter.setMultiParam(mCmdBytes);
            setter.setReceiverId(getReceiverIdByIndex());
            setter.start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.AnonymousClass24 */

                public void onSuccess(Object model) {
                    CallbackUtils.onSuccess(callback, (Object) null);
                }

                public void onFailure(Ccode ccode) {
                    CallbackUtils.onFailure(callback, ccode);
                }
            });
        } else if (callback != null) {
            callback.onFails(DJICameraError.INVALID_PARAMETERS);
        }
    }

    @Setter(CameraKeys.SPOT_METERING_TARGET)
    public void setSpotMeteringAreaRowIndexAndColIndex(Point area, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!DataCameraGetPushShotParams.getInstance().isAELock(getExpectedSenderIdByIndex())) {
            int colIndex = area.x;
            int rowIndex = area.y;
            if (rowIndex < 0 || rowIndex >= getSpotMeterRow()) {
                if (callback != null) {
                    callback.onFails(DJICameraError.COMMON_PARAM_ILLEGAL);
                }
            } else if (colIndex >= 0 && colIndex < getSpotMeterCol()) {
                int areaIndex = (getSpotMeterCol() * rowIndex) + colIndex;
                DataCameraSetMeteringArea setter = DataCameraSetMeteringArea.getInstance();
                setter.setReceiverId(getReceiverIdByIndex());
                setter.setIndex(areaIndex).start(new DJIDataCallBack() {
                    /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.AnonymousClass25 */

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
        } else if (callback != null) {
            callback.onFails(DJICameraError.CANNOT_SET_PARAMETERS_IN_THIS_STATE);
        }
    }

    /* access modifiers changed from: protected */
    public int getSpotMeterCol() {
        return 12;
    }

    /* access modifiers changed from: protected */
    public int getSpotMeterRow() {
        return 8;
    }

    @Getter(CameraKeys.SPOT_METERING_TARGET)
    public void getSpotMeteringAreaRowIndexAndColIndex(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            final DataBaseCameraGetting getter = new DataBaseCameraGetting();
            getter.setCmdId("MeteringArea");
            getter.setReceiverId(getReceiverIdByIndex());
            getter.start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.AnonymousClass26 */

                /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
                 method: dji.log.DJILog.d(java.lang.String, java.lang.String, boolean, boolean):void
                 arg types: [java.lang.String, java.lang.String, int, int]
                 candidates:
                  dji.log.DJILog.d(java.lang.String, java.lang.String, java.lang.Throwable, java.lang.Object[]):void
                  dji.log.DJILog.d(java.lang.String, java.lang.String, boolean, boolean):void */
                public void onSuccess(Object model) {
                    int colIndex;
                    int area = getter.getValue();
                    int colIndex2 = (area + 1) % DJICameraBaseAbstraction.this.getSpotMeterCol();
                    int rowIndex = (area + 1) / DJICameraBaseAbstraction.this.getSpotMeterCol();
                    if (colIndex2 > 0) {
                        colIndex = colIndex2 - 1;
                    } else {
                        colIndex = DJICameraBaseAbstraction.this.getSpotMeterCol() - 1;
                        rowIndex--;
                    }
                    DJILog.d("Version", "checkVersion", true, true);
                    callback.onSuccess(new Point(DJICameraBaseAbstraction.this.checkColIndex(colIndex), rowIndex));
                }

                public void onFailure(Ccode ccode) {
                    if (callback != null) {
                        callback.onFails(DJICameraError.getDJIError(ccode));
                    }
                }
            });
        }
    }

    /* access modifiers changed from: protected */
    public int checkColIndex(int colIndex) {
        return colIndex;
    }

    @Setter(CameraKeys.CAMERA_COLOR)
    public void setDigitalFilter(SettingsDefinitions.CameraColor cameraColor, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (cameraColor != SettingsDefinitions.CameraColor.TRUE_COLOR || checkTrueColorDigitalFilterSupported()) {
            if (cameraColor != SettingsDefinitions.CameraColor.PORTRAIT || checkPortraitDigitalFilterSupported()) {
                if (cameraColor == SettingsDefinitions.CameraColor.UNKNOWN) {
                    if (callback != null) {
                        callback.onFails(DJICameraError.INVALID_PARAMETERS);
                    }
                } else if (this.currentCameraColorRange != null) {
                    boolean isValid = false;
                    SettingsDefinitions.CameraColor[] cameraColorArr = this.currentCameraColorRange;
                    int length = cameraColorArr.length;
                    int i = 0;
                    while (true) {
                        if (i >= length) {
                            break;
                        } else if (cameraColor == cameraColorArr[i]) {
                            isValid = true;
                            break;
                        } else {
                            i++;
                        }
                    }
                    if (!isValid) {
                        CallbackUtils.onFailure(callback, DJICameraError.INVALID_PARAMETERS);
                        return;
                    }
                    int cmdVal = mapDigitalFilterToProtocol(cameraColor);
                    if (cmdVal == -2) {
                        CallbackUtils.onFailure(callback, DJICameraError.INVALID_PARAMETERS);
                        return;
                    }
                    DataBaseCameraSetting setter = new DataBaseCameraSetting();
                    setter.setCmdId("DigitalFilter");
                    if (cmdVal == -1) {
                        cmdVal = cameraColor.value();
                    }
                    setter.setValue(cmdVal);
                    setter.setReceiverId(getReceiverIdByIndex());
                    setter.start(new DJIDataCallBack() {
                        /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.AnonymousClass27 */

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
                } else {
                    CallbackUtils.onFailure(callback, DJICameraError.PARAMETERS_SET_FAILED);
                }
            } else if (callback != null) {
                callback.onFails(DJICameraError.INVALID_PARAMETERS);
            }
        } else if (callback != null) {
            callback.onFails(DJICameraError.INVALID_PARAMETERS);
        }
    }

    private int mapProtocolNumberToCorrespondingNumber(int protocolNumber) {
        if (protocolNumber == 52) {
            return 54;
        }
        if (protocolNumber == 43) {
            return 22;
        }
        if (protocolNumber > 22 || protocolNumber < 14 || !shouldMapCameraColorToProtocol(DataCameraGetPushStateInfo.getInstance().getCameraType(getExpectedSenderIdByIndex()), DataCameraGetPushStateInfo.getInstance().getVerstion(getExpectedSenderIdByIndex()))) {
            return protocolNumber;
        }
        switch (protocolNumber) {
            case 14:
                return 45;
            case 15:
                return 46;
            case 16:
                return 47;
            case 17:
                return 48;
            case 18:
                return 49;
            case 19:
                return 50;
            case 20:
                return 51;
            case 21:
                return 52;
            case 22:
                return 53;
            case 52:
                return 54;
            default:
                return protocolNumber;
        }
    }

    private int mapDigitalFilterToProtocol(SettingsDefinitions.CameraColor cameraColor) {
        DataCameraGetPushStateInfo.CameraType cameraType = DataCameraGetPushStateInfo.getInstance().getCameraType(getExpectedSenderIdByIndex());
        if (shouldMapCameraColorToProtocol(cameraType, DataCameraGetPushStateInfo.getInstance().getVerstion(getExpectedSenderIdByIndex()))) {
            switch (cameraColor) {
                case TRUE_COLOR:
                    if (cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC220) {
                        return -2;
                    }
                    return 43;
                case FILM_A:
                    return 14;
                case FILM_B:
                    return 15;
                case FILM_C:
                    return 16;
                case FILM_D:
                    return 17;
                case FILM_E:
                    return 18;
                case FILM_F:
                    return 19;
                case FILM_G:
                    return 20;
                case FILM_H:
                    return 21;
                case FILM_I:
                    return 22;
                case HLG:
                    return 52;
            }
        }
        return -1;
    }

    private boolean shouldMapCameraColorToProtocol(DataCameraGetPushStateInfo.CameraType cameraType, int version) {
        return cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6520 || cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6310 || cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6310S || (cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6510 && version >= 5) || ((cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC220 && version >= 9) || cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC240_1);
    }

    @Setter(CameraKeys.AE_LOCK)
    public void setAELock(boolean isLocked, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataCameraSetAELock setter = DataCameraSetAELock.getInstance();
        setter.setReceiverId(getReceiverIdByIndex());
        setter.setIsLock(isLocked).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.AnonymousClass28 */

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

    @Setter(CameraKeys.AUTO_AE_UNLOCK_ENABLED)
    public void setAutoUnlockAELockEnabled(boolean enabled, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataBaseCameraSetting setter = new DataBaseCameraSetting();
        setter.setReceiverId(getReceiverIdByIndex());
        setter.setCmdId(CmdIdCamera.CmdIdType.SetAEUnlockMode).setValue(enabled ? 0 : 1).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.AnonymousClass29 */

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

    @Setter(CameraKeys.PHOTO_AEB_COUNT)
    public void setPhotoAEBCount(SettingsDefinitions.PhotoAEBCount aebCount, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (aebCount == SettingsDefinitions.PhotoAEBCount.UNKNOWN || callback == null) {
            CallbackUtils.onFailure(callback, DJICameraError.COMMON_PARAM_ILLEGAL);
            return;
        }
        DataCameraSetAEBParms setter = DataCameraSetAEBParms.getInstance();
        setter.setReceiverId(getReceiverIdByIndex());
        setter.setExposureValue(3).setNumber(aebCount.value()).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.AnonymousClass30 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, (Object) null);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    @Getter(CameraKeys.PHOTO_AEB_COUNT)
    @Deprecated
    public void getPhotoAEBCount(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        ((DataCameraGetAEBParams) DataCameraGetAEBParams.getInstance().setReceiverId(getReceiverIdByIndex(), DataCameraGetAEBParams.class)).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.AnonymousClass31 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, SettingsDefinitions.PhotoAEBCount.find(DataCameraGetAEBParams.getInstance().getAEBNumber()));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    @Setter(CameraKeys.PHOTO_QUICK_VIEW_DURATION)
    public void setPhotoQuickViewDuration(int duration, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!isPhotoQuickViewSupported()) {
            if (callback != null) {
                callback.onFails(DJICameraError.COMMON_UNSUPPORTED);
            }
        } else if (duration >= 0 && duration <= 10) {
            DataCameraSetQuickPlayBack setter = DataCameraSetQuickPlayBack.getInstance();
            setter.setReceiverId(getReceiverIdByIndex());
            setter.setTime(BytesUtil.getByte(duration));
            if (duration == 0) {
                setter.setEnable(false);
            } else {
                setter.setEnable(true);
            }
            setter.start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.AnonymousClass32 */

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

    @Getter(CameraKeys.PHOTO_QUICK_VIEW_DURATION)
    public void getPhotoQuickViewDuration(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataCameraGetQuickPlayBack setter = DataCameraGetQuickPlayBack.getInstance();
        setter.setReceiverId(getReceiverIdByIndex());
        setter.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.AnonymousClass33 */

            public void onSuccess(Object model) {
                if (callback != null) {
                    callback.onSuccess(Integer.valueOf(DataCameraGetQuickPlayBack.getInstance().getTime()));
                }
            }

            public void onFailure(Ccode ccode) {
                if (callback != null) {
                    callback.onFails(DJICameraError.getDJIError(ccode));
                }
            }
        });
    }

    @Setter(CameraKeys.HISTOGRAM_ENABLED)
    public void setHistogramEnabled(boolean enabled, DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataCameraSetPushChart dataCameraSetPushChart = DataCameraSetPushChart.getInstance();
        dataCameraSetPushChart.setReceiverId(getReceiverIdByIndex());
        dataCameraSetPushChart.setEnable(enabled).start(CallbackUtils.defaultCB(callback, DJICameraError.class));
    }

    @Getter(CameraKeys.HISTOGRAM_ENABLED)
    public void getHistogramEnabled(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataCameraGetPushChart dataCameraGetPushChart = DataCameraGetPushChart.getInstance();
        dataCameraGetPushChart.setReceiverId(getReceiverIdByIndex());
        dataCameraGetPushChart.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.AnonymousClass34 */

            public void onSuccess(Object model) {
                if (callback != null) {
                    callback.onSuccess(Boolean.valueOf(dataCameraGetPushChart.isEnable()));
                }
            }

            public void onFailure(Ccode ccode) {
                if (callback != null) {
                    callback.onFails(DJICameraError.getDJIError(ccode));
                }
            }
        });
    }

    @Setter(CameraKeys.VIDEO_CAPTION_ENABLED)
    public void setVideoCaptionEnabled(boolean enabled, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataCameraSetVideoCaption videoCaptionSetter = DataCameraSetVideoCaption.getInstance();
        videoCaptionSetter.setReceiverId(getReceiverIdByIndex());
        videoCaptionSetter.reset().setGenerateVideoCaption(enabled).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.AnonymousClass35 */

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

    @Getter(CameraKeys.VIDEO_CAPTION_ENABLED)
    public void getVideoCaptionEnabled(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataCameraGetVideoCaption videoCaptionGetter = DataCameraGetVideoCaption.getInstance();
        videoCaptionGetter.setReceiverId(getReceiverIdByIndex());
        videoCaptionGetter.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.AnonymousClass36 */

            public void onSuccess(Object model) {
                if (callback != null) {
                    callback.onSuccess(Boolean.valueOf(videoCaptionGetter.isGenerateVideoCaptionEnable()));
                }
            }

            public void onFailure(Ccode ccode) {
                if (callback != null) {
                    callback.onFails(DJICameraError.getDJIError(ccode));
                }
            }
        });
    }

    @Getter(DJISDKCacheKeys.FIRMWARE_VERSION)
    public void getFirmwareVersion(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataCommonGetVersion dcgv = new DataCommonGetVersion();
        dcgv.setDeviceType(DeviceType.CAMERA);
        dcgv.setDeviceModel(getReceiverIdByIndex());
        dcgv.startForce(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.AnonymousClass37 */

            public void onSuccess(Object model) {
                String firmVersion = dcgv.getFirmVer(".");
                if (callback != null) {
                    callback.onSuccess(firmVersion);
                }
            }

            public void onFailure(Ccode ccode) {
                if (callback != null) {
                    callback.onFails(DJICameraError.getDJIError(ccode));
                }
            }
        });
    }

    @Setter(CameraKeys.AUDIO_RECORDING_ENABLED)
    public void setAudioRecordEnabled(boolean enabled, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (isAudioRecordSupported()) {
            DataCameraSetAudio setter = new DataCameraSetAudio();
            setter.setReceiverId(getReceiverIdByIndex());
            setter.setEnable(enabled).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.AnonymousClass38 */

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

    @Getter(CameraKeys.AUDIO_RECORDING_ENABLED)
    public void getAudioRecordEnabled(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (isAudioRecordSupported()) {
            final DataCameraGetAudio getter = new DataCameraGetAudio();
            getter.setReceiverId(getReceiverIdByIndex());
            getter.start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.AnonymousClass39 */

                public void onSuccess(Object model) {
                    if (callback != null) {
                        callback.onSuccess(Boolean.valueOf(getter.isEnable()));
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

    @Setter(CameraKeys.AUDIO_GAIN)
    public void setAudioGain(int gain, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!isAudioRecordSupported()) {
            if (callback != null) {
                callback.onFails(DJICameraError.COMMON_UNSUPPORTED);
            }
        } else if (gain >= 0 && gain <= 100) {
            DataOsdSetMicGain setter = DataOsdSetMicGain.getInstance();
            setter.setReceiverId(getReceiverIdByIndex());
            setter.setMicGain(gain).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.AnonymousClass40 */

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

    @Getter(CameraKeys.AUDIO_GAIN)
    public void getAudioGain(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (isAudioRecordSupported()) {
            final DataOsdGetMicGain getter = DataOsdGetMicGain.getInstance();
            getter.setReceiverId(getReceiverIdByIndex());
            getter.start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.AnonymousClass41 */

                public void onSuccess(Object model) {
                    if (callback != null) {
                        callback.onSuccess(Integer.valueOf(getter.getMicGain()));
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

    @Setter(CameraKeys.TURN_OFF_FAN_WHEN_POSSIBLE)
    public void setTurnOffFanWhenPossible(boolean turnOffWhenPossible, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!isTurnOffFanSupported()) {
            if (callback != null) {
                callback.onFails(DJICameraError.COMMON_UNSUPPORTED);
            }
        } else if (DataCameraGetPushStateInfo.getInstance().getVerstion(getExpectedSenderIdByIndex()) >= 4) {
            DataCameraSetRecordFan setter = new DataCameraSetRecordFan();
            setter.setReceiverId(getReceiverIdByIndex());
            setter.setIsForceTurnOffFan(turnOffWhenPossible).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.AnonymousClass42 */

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
            callback.onFails(DJICameraError.COMMAND_NOT_SUPPORTED_BY_FIRMWARE);
        }
    }

    @Getter(CameraKeys.TURN_OFF_FAN_WHEN_POSSIBLE)
    public void getTurnOffFanWhenPossible(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!isTurnOffFanSupported()) {
            if (callback != null) {
                callback.onFails(DJICameraError.COMMON_UNSUPPORTED);
            }
        } else if (DataCameraGetPushStateInfo.getInstance().getVerstion(getExpectedSenderIdByIndex()) >= 4) {
            final DataCameraGetRecordFan fanGetter = new DataCameraGetRecordFan();
            fanGetter.setReceiverId(getReceiverIdByIndex());
            fanGetter.start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.AnonymousClass43 */

                public void onSuccess(Object model) {
                    if (callback != null) {
                        callback.onSuccess(Boolean.valueOf(fanGetter.isForceTurnOffFan()));
                    }
                }

                public void onFailure(Ccode ccode) {
                    if (callback != null) {
                        callback.onFails(DJICameraError.getDJIError(ccode));
                    }
                }
            });
        } else if (callback != null) {
            callback.onFails(DJICameraError.COMMAND_NOT_SUPPORTED_BY_FIRMWARE);
        }
    }

    @Setter(CameraKeys.DIGITAL_ZOOM_FACTOR)
    public void setDigitalZoomScale(float scale, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (isDigitalZoomScaleSupported()) {
            DJIError preError = checkPrerequisiteForSetDigitalZoomScale();
            if (preError != null) {
                if (callback != null) {
                    callback.onFails(preError);
                }
            } else if (((double) scale) >= 1.0d && ((double) scale) <= 2.0d) {
                checkIfNeedWaitForPauseTheStabilization(callback, DataSingleSendAppStateForStabilization.CameraState.ZOOM_IN);
                DataCameraSetFocusParam setter = DataCameraSetFocusParam.getInstance();
                setter.setReceiverId(getReceiverIdByIndex());
                setter.setDigitalZoom(true).setDigitalMode(DataCameraSetFocusParam.ZoomMode.POSITION).setDigitalPosScale(scale);
                setter.start(new DJIDataCallBack() {
                    /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.AnonymousClass44 */

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
                callback.onFails(DJIError.COMMON_PARAM_ILLEGAL);
            }
        } else if (callback != null) {
            callback.onFails(DJICameraError.COMMON_UNSUPPORTED);
        }
    }

    @Setter(CameraKeys.PHOTO_TIME_LAPSE_SETTINGS)
    public void setPhotoTimeLapseSettings(PhotoTimeLapseSettings photoTimeLapseParam, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        SettingsDefinitions.PhotoTimeLapseFileFormat fileFormat = photoTimeLapseParam.getFileFormat();
        int interval = photoTimeLapseParam.getInterval();
        int duration = photoTimeLapseParam.getDuration();
        if (SettingsDefinitions.PhotoTimeLapseFileFormat.JPEG_AND_VIDEO == fileFormat) {
            if (interval < 20 || interval > 1000) {
                callback.onFails(DJICameraError.COMMON_PARAM_ILLEGAL);
                return;
            }
        } else if (interval < 10 || interval > 1000) {
            callback.onFails(DJICameraError.COMMON_PARAM_ILLEGAL);
            return;
        }
        if (duration < 0) {
            callback.onFails(DJICameraError.COMMON_PARAM_ILLEGAL);
        } else if (fileFormat == SettingsDefinitions.PhotoTimeLapseFileFormat.UNKNOWN) {
            callback.onFails(DJICameraError.COMMON_PARAM_ILLEGAL);
        } else {
            if (fileFormat == SettingsDefinitions.PhotoTimeLapseFileFormat.JPEG_AND_VIDEO && interval < 20) {
                callback.onFails(DJICameraError.COMMON_PARAM_ILLEGAL);
            }
            int fileFormatValue = fileFormat.value();
            if (fileFormatValue == 1) {
                fileFormatValue = 2;
            }
            DataCameraSetVideoRecordMode setter = new DataCameraSetVideoRecordMode();
            setter.setReceiverId(getReceiverIdByIndex());
            setter.setVideoRecordMode(1, interval, duration).setTimelapseControlMode(0).setTimelapseSaveType(fileFormatValue);
            setter.start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.AnonymousClass45 */

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

    @Getter(CameraKeys.PHOTO_TIME_LAPSE_SETTINGS)
    public void getPhotoTimeLapseSettings(DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            DataCameraGetPushShotParams getter = DataCameraGetPushShotParams.getInstance();
            int interval = getter.getVideoRecordIntervalTime(getExpectedSenderIdByIndex());
            int duration = getter.getTimelapseDuration(getExpectedSenderIdByIndex());
            int enumValue = getter.getTimelapseSaveType(getExpectedSenderIdByIndex());
            if (enumValue == 2) {
                enumValue = 1;
            }
            CallbackUtils.onSuccess(callback, new PhotoTimeLapseSettings(interval, duration, SettingsDefinitions.PhotoTimeLapseFileFormat.find(enumValue)));
        }
    }

    @Setter(CameraKeys.STREAM_QUALITY)
    public void setStreamQuality(DataCameraSetVOutParams.LCDFormat format, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        ((DataCameraSetVOutParams) new DataCameraSetVOutParams().setStream(DJICameraEnumMappingUtil.mapSecondOutputFormatToProtocol(format)).setReceiverId(getReceiverIdByIndex(), DataCameraSetVOutParams.class)).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.AnonymousClass46 */

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

    @Getter(CameraKeys.STREAM_QUALITY)
    public void getStreamQuality(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        ((DataCameraGetVOutParams) DataCameraGetVOutParams.getInstance().setReceiverId(getReceiverIdByIndex(), DataCameraGetVOutParams.class)).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.AnonymousClass47 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, DJICameraEnumMappingUtil.mapProtocolToSecondOutputFormat(DataCameraGetVOutParams.getInstance().getStream(DJICameraBaseAbstraction.this.getExpectedSenderIdByIndex())));
            }

            public void onFailure(Ccode ccode) {
                callback.onFails(DJICameraError.getDJIError(ccode));
            }
        });
    }

    @Setter(CameraKeys.HD_LIVE_VIEW_ENABLED)
    public void setLiveViewOutputMode(boolean enabled, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!isHDLiveViewAvailable()) {
            CallbackUtils.onFailure(callback, DJICameraError.COMMON_UNSUPPORTED);
            return;
        }
        DataCameraSetVOutParams.LCDFormat format = DataCameraSetVOutParams.LCDFormat.AUTO_NO_GLASS_CONNECTED;
        if (enabled) {
            format = DataCameraSetVOutParams.LCDFormat.HD_FORMAT;
        }
        DataCameraSetVOutParams setter = new DataCameraSetVOutParams();
        setter.setReceiverId(getReceiverIdByIndex());
        setter.setIsSettingLCD(true).setLCDFormat(format).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.AnonymousClass48 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, (Object) null);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    @Getter(CameraKeys.HD_LIVE_VIEW_ENABLED)
    public void getLiveViewHDEnable(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            if (!isHDLiveViewAvailable()) {
                callback.onFails(DJICameraError.COMMON_UNSUPPORTED);
                return;
            }
            final DataCameraGetVOutParams getter = new DataCameraGetVOutParams();
            getter.setReceiverId(getReceiverIdByIndex());
            getter.start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.AnonymousClass49 */

                public void onSuccess(Object model) {
                    if (getter.getLCDFormat(DJICameraBaseAbstraction.this.getExpectedSenderIdByIndex()) == DataCameraSetVOutParams.LCDFormat.R1920x1080_FPS30 || getter.getLCDFormat(DJICameraBaseAbstraction.this.getExpectedSenderIdByIndex()) == DataCameraSetVOutParams.LCDFormat.HD_FORMAT) {
                        callback.onSuccess(true);
                    } else {
                        callback.onSuccess(false);
                    }
                }

                public void onFailure(Ccode ccode) {
                    callback.onFails(DJICameraError.getDJIError(ccode));
                }
            });
        }
    }

    @Setter(CameraKeys.LED_AUTO_TURN_OFF_ENABLED)
    public void setTurnOffLEDDuringShootPhotoEnabled(boolean enabled, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataCameraSetForeArmLed setter = DataCameraSetForeArmLed.getInstance();
        setter.setReceiverId(getReceiverIdByIndex());
        setter.setEnable(enabled).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.AnonymousClass50 */

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

    @Setter(CameraKeys.VIDEO_FILE_COMPRESSION_STANDARD)
    public void setVideoFileCompressionStandard(SettingsDefinitions.VideoFileCompressionStandard standard, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataCameraSetVideoEncode setter = new DataCameraSetVideoEncode();
        setter.setReceiverId(getReceiverIdByIndex());
        setter.setPrimaryEncodeType(DataCameraSetVideoEncode.VideoEncodeType.find(standard.value())).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.AnonymousClass51 */

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
    public boolean isAudioRecordSupported() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isPhotoQuickViewSupported() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isPhotoIntervalParamValid(int interval, int count) {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isIntervalValueSupported(int interval) {
        int[] intervalRange;
        int[][] childRange = (int[][]) CacheHelper.getCamera(this.index, CameraKeys.SHOOT_PHOTO_MODE_CHILD_RANGE);
        if (childRange == null || (intervalRange = childRange[SettingsDefinitions.ShootPhotoMode.INTERVAL.value()]) == null) {
            return false;
        }
        return checkValueInArray(intervalRange, interval);
    }

    public DJIError checkPrerequisiteForSetDigitalZoomScale() {
        DataCameraGetMode.MODE mode = DataCameraGetPushStateInfo.getInstance().getMode(getExpectedSenderIdByIndex());
        if (mode == DataCameraGetMode.MODE.RECORD) {
            int resolution = DataCameraGetPushShotParams.getInstance().getVideoFormat(getExpectedSenderIdByIndex());
            int fps = DataCameraGetPushShotParams.getInstance().getVideoFps(getExpectedSenderIdByIndex());
            if (resolution > 10 && resolution != 24 && resolution != 25 && resolution != 31) {
                return DJICameraError.INVALID_PARAMETERS;
            }
            if (fps >= 7) {
                return DJICameraError.INVALID_PARAMETERS;
            }
        } else if (mode == DataCameraGetMode.MODE.TAKEPHOTO && DataCameraGetPushShotParams.getInstance().getPhotoType(getExpectedSenderIdByIndex()) == DataCameraSetPhoto.TYPE.APP_FULLVIEW) {
            return DJICameraError.INVALID_PARAMETERS;
        }
        return null;
    }

    @Setter(CameraKeys.LEDS_AUTO_TURN_OFF_ENABLED_SETTINGS)
    public void setLEDsAutoTurnOffEnabledSettings(LEDsSettings settings, DJISDKCacheHWAbstraction.InnerCallback callback) {
        ((DataCameraSetForeArmLed) DataCameraSetForeArmLed.getInstance().setEnable(settings.getByte().byteValue()).setReceiverId(getReceiverIdByIndex(), DataCameraSetForeArmLed.class)).start(CallbackUtils.defaultCB(callback));
    }

    @Setter(CameraKeys.MULTI_LEDS_AUTO_ENABLED)
    public void setMultiLEDsAutoEnabled(DJIMultiLEDControlMode mode, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        ((DataCameraSetForeArmLed) DataCameraSetForeArmLed.getInstance().setEnable(mode.getDate()).setReceiverId(getReceiverIdByIndex(), DataCameraSetForeArmLed.class)).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.AnonymousClass52 */

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

    @Getter(CameraKeys.MULTI_LEDS_AUTO_ENABLED)
    public void getMultiLEDsAutoEnabled(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataCameraGetForeArmLed getter = new DataCameraGetForeArmLed();
        getter.setReceiverId(getReceiverIdByIndex());
        getter.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.AnonymousClass53 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, new DJIMultiLEDControlMode(getter.getMultiMask()));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIFlightControllerError.getDJIError(ccode));
            }
        });
    }

    private void save(final Bitmap b, final DJISDKCacheHWAbstraction.InnerCallback callback, final String dir) {
        this.handler.post(new Runnable() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.AnonymousClass54 */

            public void run() {
                DJILog.startTime(4);
                File rst = DJICameraBaseAbstraction.this.saveBitmap(b, dir + "/screen_" + MD5.getMD5For16("0_" + System.currentTimeMillis() + "_" + b.getByteCount()) + "_" + System.currentTimeMillis() + ".jpg");
                b.recycle();
                DJILog.endTimes(4);
                if (callback == null) {
                    return;
                }
                if (rst != null) {
                    callback.onSuccess(rst);
                } else {
                    callback.onFails(DJIError.COMMON_EXECUTION_FAILED);
                }
            }
        });
    }

    /* JADX WARNING: Removed duplicated region for block: B:19:0x006a A[SYNTHETIC, Splitter:B:19:0x006a] */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x00af A[SYNTHETIC, Splitter:B:27:0x00af] */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x00d8 A[SYNTHETIC, Splitter:B:33:0x00d8] */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:16:0x004a=Splitter:B:16:0x004a, B:24:0x008f=Splitter:B:24:0x008f} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.io.File saveBitmap(android.graphics.Bitmap r10, java.lang.String r11) {
        /*
            r9 = this;
            java.io.File r1 = new java.io.File
            r1.<init>(r11)
            boolean r4 = r1.exists()
            if (r4 == 0) goto L_0x000e
            r1.delete()
        L_0x000e:
            r2 = 0
            r1.createNewFile()     // Catch:{ FileNotFoundException -> 0x0049, IOException -> 0x008e }
            java.io.FileOutputStream r3 = new java.io.FileOutputStream     // Catch:{ FileNotFoundException -> 0x0049, IOException -> 0x008e }
            r3.<init>(r1)     // Catch:{ FileNotFoundException -> 0x0049, IOException -> 0x008e }
            android.graphics.Bitmap$CompressFormat r4 = android.graphics.Bitmap.CompressFormat.JPEG     // Catch:{ FileNotFoundException -> 0x0102, IOException -> 0x00ff, all -> 0x00fc }
            r5 = 100
            r10.compress(r4, r5, r3)     // Catch:{ FileNotFoundException -> 0x0102, IOException -> 0x00ff, all -> 0x00fc }
            r3.flush()     // Catch:{ FileNotFoundException -> 0x0102, IOException -> 0x00ff, all -> 0x00fc }
            if (r3 == 0) goto L_0x0106
            r3.close()     // Catch:{ IOException -> 0x0028 }
            r2 = r3
        L_0x0027:
            return r1
        L_0x0028:
            r0 = move-exception
            dji.log.DJILogHelper r4 = dji.log.DJILogHelper.getInstance()
            java.lang.String r5 = "DJISDKCacheBaseCameraAbstraction"
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r7 = "saveBitmap: finally: "
            java.lang.StringBuilder r6 = r6.append(r7)
            java.lang.StringBuilder r6 = r6.append(r0)
            java.lang.String r6 = r6.toString()
            r4.LOGE(r5, r6)
            r2 = r3
            goto L_0x0027
        L_0x0049:
            r0 = move-exception
        L_0x004a:
            dji.log.DJILogHelper r4 = dji.log.DJILogHelper.getInstance()     // Catch:{ all -> 0x00d5 }
            java.lang.String r5 = "DJISDKCacheBaseCameraAbstraction"
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ all -> 0x00d5 }
            r6.<init>()     // Catch:{ all -> 0x00d5 }
            java.lang.String r7 = "saveBitmap: "
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ all -> 0x00d5 }
            java.lang.StringBuilder r6 = r6.append(r0)     // Catch:{ all -> 0x00d5 }
            java.lang.String r6 = r6.toString()     // Catch:{ all -> 0x00d5 }
            r4.LOGE(r5, r6)     // Catch:{ all -> 0x00d5 }
            if (r2 == 0) goto L_0x0027
            r2.close()     // Catch:{ IOException -> 0x006e }
            goto L_0x0027
        L_0x006e:
            r0 = move-exception
            dji.log.DJILogHelper r4 = dji.log.DJILogHelper.getInstance()
            java.lang.String r5 = "DJISDKCacheBaseCameraAbstraction"
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r7 = "saveBitmap: finally: "
            java.lang.StringBuilder r6 = r6.append(r7)
            java.lang.StringBuilder r6 = r6.append(r0)
            java.lang.String r6 = r6.toString()
            r4.LOGE(r5, r6)
            goto L_0x0027
        L_0x008e:
            r0 = move-exception
        L_0x008f:
            dji.log.DJILogHelper r4 = dji.log.DJILogHelper.getInstance()     // Catch:{ all -> 0x00d5 }
            java.lang.String r5 = "DJISDKCacheBaseCameraAbstraction"
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ all -> 0x00d5 }
            r6.<init>()     // Catch:{ all -> 0x00d5 }
            java.lang.String r7 = "saveBitmap: "
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ all -> 0x00d5 }
            java.lang.StringBuilder r6 = r6.append(r0)     // Catch:{ all -> 0x00d5 }
            java.lang.String r6 = r6.toString()     // Catch:{ all -> 0x00d5 }
            r4.LOGE(r5, r6)     // Catch:{ all -> 0x00d5 }
            if (r2 == 0) goto L_0x0027
            r2.close()     // Catch:{ IOException -> 0x00b4 }
            goto L_0x0027
        L_0x00b4:
            r0 = move-exception
            dji.log.DJILogHelper r4 = dji.log.DJILogHelper.getInstance()
            java.lang.String r5 = "DJISDKCacheBaseCameraAbstraction"
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r7 = "saveBitmap: finally: "
            java.lang.StringBuilder r6 = r6.append(r7)
            java.lang.StringBuilder r6 = r6.append(r0)
            java.lang.String r6 = r6.toString()
            r4.LOGE(r5, r6)
            goto L_0x0027
        L_0x00d5:
            r4 = move-exception
        L_0x00d6:
            if (r2 == 0) goto L_0x00db
            r2.close()     // Catch:{ IOException -> 0x00dc }
        L_0x00db:
            throw r4
        L_0x00dc:
            r0 = move-exception
            dji.log.DJILogHelper r5 = dji.log.DJILogHelper.getInstance()
            java.lang.String r6 = "DJISDKCacheBaseCameraAbstraction"
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            java.lang.String r8 = "saveBitmap: finally: "
            java.lang.StringBuilder r7 = r7.append(r8)
            java.lang.StringBuilder r7 = r7.append(r0)
            java.lang.String r7 = r7.toString()
            r5.LOGE(r6, r7)
            goto L_0x00db
        L_0x00fc:
            r4 = move-exception
            r2 = r3
            goto L_0x00d6
        L_0x00ff:
            r0 = move-exception
            r2 = r3
            goto L_0x008f
        L_0x0102:
            r0 = move-exception
            r2 = r3
            goto L_0x004a
        L_0x0106:
            r2 = r3
            goto L_0x0027
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.saveBitmap(android.graphics.Bitmap, java.lang.String):java.io.File");
    }

    @Action(CameraKeys.SHOOT_PHOTO_BY_CACHING)
    public void shootPhotoByCaching(final DJISDKCacheHWAbstraction.InnerCallback callback, SettingsDefinitions.ShootPhotoMode mode, final String dir) {
        final SettingsDefinitions.Orientation cameraOrientation = (SettingsDefinitions.Orientation) CacheHelper.getCamera(this.index, CameraKeys.ORIENTATION);
        this.handler.post(new Runnable() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.AnonymousClass55 */

            /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
             method: dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.access$300(dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction, java.lang.Object, dji.sdksharedlib.keycatalog.DJISDKCacheKey):void
             arg types: [dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction, boolean, dji.sdksharedlib.keycatalog.DJISDKCacheKey]
             candidates:
              dji.sdksharedlib.hardware.abstractions.camera.DJICameraAbstraction.access$300(dji.sdksharedlib.hardware.abstractions.camera.DJICameraAbstraction, java.lang.String, int):java.lang.String
              dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.access$300(dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction, java.lang.Object, dji.sdksharedlib.keycatalog.DJISDKCacheKey):void */
            public void run() {
                boolean z = true;
                DJICameraBaseAbstraction.this.notifyValueChangeForKeyPath(true, DJICameraBaseAbstraction.this.convertKeyToPath(CameraKeys.IS_SHOOTING_PHOTO));
                DJICameraBaseAbstraction.this.notifyValueChangeForKeyPath((Object) true, DJICameraBaseAbstraction.this.convertKeyToPath(CameraKeys.IS_STORING_PHOTO));
                DJIVideoDecoder decoder = ServiceManager.getInstance().getDecoder();
                if (cameraOrientation != SettingsDefinitions.Orientation.PORTRAIT) {
                    z = false;
                }
                Bitmap b = decoder.getBitmap(z);
                if (b != null) {
                    File rst = DJICameraBaseAbstraction.this.saveBitmap(b, dir + "/screen_" + MD5.getMD5For16("0_" + System.currentTimeMillis() + "_" + b.getByteCount()) + "_" + System.currentTimeMillis() + ".jpg");
                    b.recycle();
                    if (callback != null) {
                        if (rst != null) {
                            callback.onSuccess(rst);
                        } else {
                            callback.onFails(DJIError.COMMON_EXECUTION_FAILED);
                        }
                    }
                } else if (callback != null) {
                    callback.onFails(DJIError.COMMON_EXECUTION_FAILED);
                }
                DJICameraBaseAbstraction.this.notifyValueChangeForKeyPath(false, DJICameraBaseAbstraction.this.convertKeyToPath(CameraKeys.IS_STORING_PHOTO));
                DJICameraBaseAbstraction.this.notifyValueChangeForKeyPath(false, DJICameraBaseAbstraction.this.convertKeyToPath(CameraKeys.IS_SHOOTING_PHOTO));
            }
        });
    }

    @Action(CameraKeys.RESTORE_FACTORY_SETTINGS)
    public void loadFactorySettings(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataCameraLoadParams setter = DataCameraLoadParams.getInstance();
        setter.setMode(DataCameraSaveParams.USER.DEFAULT);
        setter.setReceiverId(getReceiverIdByIndex());
        setter.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.AnonymousClass56 */

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

    @Action(CameraKeys.SAVE_SETTINGS_TO_PROFILE)
    public void saveSettingsTo(final DJISDKCacheHWAbstraction.InnerCallback callback, SettingsDefinitions.CustomSettingsProfile user) {
        if (SettingsDefinitions.CustomSettingsProfile.UNKNOWN != user) {
            DataCameraSaveParams setter = DataCameraSaveParams.getInstance();
            setter.setReceiverId(getReceiverIdByIndex());
            setter.setMode(DataCameraSaveParams.USER.find(user.value()));
            setter.start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.AnonymousClass57 */

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

    @Action(CameraKeys.LOAD_SETTINGS_FROM_PROFILE)
    public void loadSettingsFrom(final DJISDKCacheHWAbstraction.InnerCallback callback, SettingsDefinitions.CustomSettingsProfile user) {
        DataCameraLoadParams setter = DataCameraLoadParams.getInstance();
        setter.setReceiverId(getReceiverIdByIndex());
        setter.setMode(DataCameraSaveParams.USER.find(user.value()));
        setter.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.AnonymousClass58 */

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

    @Setter(CameraKeys.FOCUS_MODE)
    public void setLensFocusMode(SettingsDefinitions.FocusMode focusMode, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (focusMode == SettingsDefinitions.FocusMode.AFC) {
            CallbackUtils.onFailure(callback, DJICameraError.COMMON_PARAM_ILLEGAL);
            return;
        }
        DataBaseCameraSetting setter = new DataBaseCameraSetting();
        setter.setReceiverId(getReceiverIdByIndex());
        setter.setCmdId(CmdIdCamera.CmdIdType.SetFocusMode).setValue(focusMode.value()).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.AnonymousClass59 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, (Object) null);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJICameraError.getDJIError(ccode));
            }
        });
    }

    @Setter(CameraKeys.FOCUS_TARGET)
    public void setLensFocusTarget(PointF point, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataCameraSetFocusArea setter = DataCameraSetFocusArea.getInstance();
        setter.setReceiverId(getReceiverIdByIndex());
        setter.setFocusAxisX(point.x).setFocusAxisY(point.y).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.AnonymousClass60 */

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

    @Setter(CameraKeys.FOCUS_ASSISTANT_SETTINGS)
    public void setLensFocusAssistantEnabled(FocusAssistantSettings assistant, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataCameraSetFocusAid setter = DataCameraSetFocusAid.getInstance();
        setter.setReceiverId(getReceiverIdByIndex());
        setter.setDigitalFocus(assistant.isEnabledAF(), assistant.isEnabledMF());
        setter.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.AnonymousClass61 */

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

    @Getter(CameraKeys.FOCUS_RING_VALUE_UPPER_BOUND)
    public void getLensFocusRingValueUpperBound(DJISDKCacheHWAbstraction.InnerCallback callback) {
        int upperBound = DataCameraGetPushShotInfo.getInstance().getShotFocusMaxStroke(getExpectedSenderIdByIndex());
        this.maxFocalDistance = upperBound;
        if (callback != null) {
            callback.onSuccess(Integer.valueOf(upperBound));
        }
    }

    @Setter(CameraKeys.FOCUS_RING_VALUE)
    public void setLensFocusRingValue(int distance, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (-1 == this.maxFocalDistance) {
            this.maxFocalDistance = DataCameraGetPushShotInfo.getInstance().getShotFocusMaxStroke(getExpectedSenderIdByIndex());
        }
        if (this.minFocalDistance <= distance && this.maxFocalDistance >= distance) {
            if (this.isMFInverted) {
                distance = this.maxFocalDistance - distance;
            }
            ((DataCameraSetFocusStroke) DataCameraSetFocusStroke.getInstance().setStroke(distance).setReceiverId(getReceiverIdByIndex(), DataCameraSetFocusStroke.class)).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.AnonymousClass62 */

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

    /* access modifiers changed from: protected */
    public boolean isOpticalZoomSupported() {
        return DataCameraGetPushShotInfo.getInstance().getZoomFocusType(getExpectedSenderIdByIndex()) == DataCameraGetPushShotInfo.ZoomFocusType.AutoZoomFocus;
    }

    @Setter(CameraKeys.OPTICAL_ZOOM_FOCAL_LENGTH)
    public void setOpticalZoomFocalLength(int length, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!isOpticalZoomSupported()) {
            if (callback != null) {
                callback.onFails(DJICameraError.COMMON_UNSUPPORTED);
            }
        } else if (checkOpticalFocalLengthValue(length)) {
            checkIfNeedWaitForPauseTheStabilization(callback, DataSingleSendAppStateForStabilization.CameraState.ZOOM_IN);
            doSetOpticalZoomFocalLength(length, callback);
        } else if (callback != null) {
            callback.onFails(DJIError.COMMON_PARAM_ILLEGAL);
        }
    }

    @Setter(CameraKeys.SSD_VIDEO_RESOLUTION_AND_FRAME_RATE)
    public void setSSDRawVideoResolutionAndFrameRate(ResolutionAndFrameRate resolutionAndFrameRate, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (isSSDSupported()) {
            SettingsDefinitions.VideoResolution videoResolution = resolutionAndFrameRate.getResolution();
            SettingsDefinitions.VideoFrameRate videoFrameRate = resolutionAndFrameRate.getFrameRate();
            int ratio = DJICameraEnumMappingUtil.getResolutionProtocolValue(videoResolution);
            int fps = DJICameraEnumMappingUtil.getFrameRateProtocolValue(videoFrameRate);
            if (ratio != -1 && fps != -1) {
                DataCameraSetSSDVideoFormat setterSSD = DataCameraSetSSDVideoFormat.getInstance();
                setterSSD.setReceiverId(getReceiverIdByIndex());
                setterSSD.setRatio(ratio).setFps(fps).start(new DJIDataCallBack() {
                    /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.AnonymousClass63 */

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
        } else if (callback != null) {
            callback.onFails(DJICameraError.COMMON_UNSUPPORTED);
        }
    }

    @Setter(CameraKeys.APERTURE)
    public void setAperture(SettingsDefinitions.Aperture aperture, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (checkApertureValid(aperture)) {
            short apertureValue = (short) aperture.value();
            DataCameraSetAperture setter = new DataCameraSetAperture();
            setter.setReceiverId(getReceiverIdByIndex());
            setter.setAperture(apertureValue).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.AnonymousClass64 */

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

    /* access modifiers changed from: protected */
    public boolean checkApertureValid(SettingsDefinitions.Aperture aperture) {
        if (-1 == this.minAperture || -1 == this.maxAperture) {
            this.minAperture = DataCameraGetPushShotInfo.getInstance().getMinAperture(getExpectedSenderIdByIndex());
            this.maxAperture = DataCameraGetPushShotInfo.getInstance().getMaxAperture(getExpectedSenderIdByIndex());
        }
        return aperture.value() >= this.minAperture && aperture.value() <= this.maxAperture;
    }

    @Getter(CameraKeys.LENS_INFORMATION)
    public void getLensInformation(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            final DataCameraGetShotInfo getter = DataCameraGetShotInfo.getInstance();
            getter.setReceiverId(getReceiverIdByIndex());
            getter.start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.AnonymousClass65 */

                public void onSuccess(Object model) {
                    String name = getter.getName();
                    if (name == null || name.trim().length() == 0) {
                        StringBuilder lensInfo = new StringBuilder();
                        lensInfo.append(DJILensFeatureUtils.getProductName(getter.getMemberId(), getter.getModelId(), getter.getHardVersion()));
                        if (lensInfo.length() != 0) {
                            callback.onSuccess(lensInfo.toString());
                        } else {
                            callback.onFails(DJICameraError.COMMON_UNKNOWN);
                        }
                    } else {
                        callback.onSuccess(name);
                    }
                }

                public void onFailure(Ccode ccode) {
                    callback.onFails(DJICameraError.COMMON_UNKNOWN);
                }
            });
        }
    }

    @Action(CameraKeys.START_CONTINUOUS_OPTICAL_ZOOM)
    public void startContinuousOpticalZoom(final DJISDKCacheHWAbstraction.InnerCallback callback, SettingsDefinitions.ZoomDirection zoomDirection, SettingsDefinitions.ZoomSpeed speed) {
        if (checkOpticalZoomSupported()) {
            this.speedCache = DataCameraSetOpticsZoomMode.ZoomSpeed.find(speed.value());
            if (zoomDirection.equals(SettingsDefinitions.ZoomDirection.ZOOM_IN)) {
                checkIfNeedWaitForPauseTheStabilization(callback, DataSingleSendAppStateForStabilization.CameraState.ZOOM_IN);
            } else {
                checkIfNeedWaitForPauseTheStabilization(callback, DataSingleSendAppStateForStabilization.CameraState.ZOOM_OUT);
            }
            DataCameraSetOpticsZoomMode setter = new DataCameraSetOpticsZoomMode();
            setter.setReceiverId(getReceiverIdByIndex());
            setter.setOpticsZoomMode(DataCameraSetOpticsZoomMode.OpticsZommMode.CONTINUOUS, DataCameraSetOpticsZoomMode.ZoomSpeed.find(speed.value()), zoomDirection.value(), 0).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.AnonymousClass66 */

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
        callback.onFails(DJICameraError.COMMON_UNSUPPORTED);
    }

    @Action(CameraKeys.STOP_CONTINUOUS_OPTICAL_ZOOM)
    public void stopContinuousOpticalZoom(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!checkOpticalZoomSupported()) {
            if (callback != null) {
                callback.onFails(DJICameraError.COMMON_UNSUPPORTED);
            }
        } else if (checkOpticalZoomCondition()) {
            DataCameraSetOpticsZoomMode setter = new DataCameraSetOpticsZoomMode();
            setter.setReceiverId(getReceiverIdByIndex());
            setter.setOpticsZoomMode(DataCameraSetOpticsZoomMode.OpticsZommMode.STOPZOOM, this.speedCache, 0, 0).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.AnonymousClass67 */

                public void onSuccess(Object model) {
                    callback.onSuccess(null);
                }

                public void onFailure(Ccode ccode) {
                    callback.onFails(DJICameraError.getDJIError(ccode));
                }
            });
        } else if (callback != null) {
            callback.onFails(DJICameraError.UNSUPPORTED_CMD_STATE);
        }
    }

    /* access modifiers changed from: protected */
    public boolean checkOpticalZoomSupported() {
        return isOpticalZoomScaleSupported() || isHandHeldProduct() || isOpticalZoomSupported();
    }

    /* access modifiers changed from: protected */
    public boolean checkOpticalZoomCondition() {
        return this.speedCache != null;
    }

    @Action(CameraKeys.FORMAT_SSD)
    public void formatSSD(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (isSSDSupported()) {
            DataCameraFormatSSD setter = DataCameraFormatSSD.getInstance();
            setter.setReceiverId(getReceiverIdByIndex());
            setter.setFormatSpeed(1).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.AnonymousClass68 */

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

    /* access modifiers changed from: protected */
    public void doSetOpticalZoomFocalLength(int length, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        ((DataCameraSetFocusDistance) DataCameraSetFocusDistance.getInstance().setStroke(length).setReceiverId(getReceiverIdByIndex(), DataCameraSetFocusDistance.class)).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.AnonymousClass69 */

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

    private boolean checkOpticalFocalLengthValue(int length) {
        int minDistance = DataCameraGetPushShotInfo.getInstance().getMinFocusDistance(getExpectedSenderIdByIndex());
        int maxDistance = DataCameraGetPushShotInfo.getInstance().getMaxFocusDistance(getExpectedSenderIdByIndex());
        int step = DataCameraGetPushShotInfo.getInstance().getMinFocusDistanceStep(getExpectedSenderIdByIndex());
        if (length < minDistance || length > maxDistance || length % step != 0) {
            return false;
        }
        return true;
    }

    private SettingsDefinitions.FocusMode updateCameraLensFocusMode(DataCameraGetPushShotInfo pushShotInfo) {
        if (isMFSupported() && !isAFSupported()) {
            return SettingsDefinitions.FocusMode.MANUAL;
        }
        if (!isAFSupported() || isMFSupported()) {
            return SettingsDefinitions.FocusMode.find(pushShotInfo.getFuselageFocusMode(getExpectedSenderIdByIndex()).value());
        }
        return SettingsDefinitions.FocusMode.AUTO;
    }

    /* access modifiers changed from: protected */
    public boolean checkPortraitDigitalFilterSupported() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean checkTrueColorDigitalFilterSupported() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isHDLiveViewAvailable() {
        DataCameraGetPushStateInfo.CameraType cameraType = DataCameraGetPushStateInfo.getInstance().getCameraType(getExpectedSenderIdByIndex());
        if (cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC240 || cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC240_1 || cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC245_IMX477) {
            return true;
        }
        ResolutionAndFrameRate videoResolutionAndFrameRate = DJICameraEnumMappingUtil.wrapResolutionAndFrameRate(DataCameraGetPushShotParams.getInstance().getVideoFormat(getExpectedSenderIdByIndex()), DataCameraGetPushShotParams.getInstance().getVideoFps(getExpectedSenderIdByIndex()));
        if (videoResolutionAndFrameRate.getResolution() == SettingsDefinitions.VideoResolution.RESOLUTION_2704x1520 || videoResolutionAndFrameRate.getResolution() == SettingsDefinitions.VideoResolution.RESOLUTION_2720x1530 || videoResolutionAndFrameRate.getResolution() == SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080) {
            return true;
        }
        return false;
    }

    private int ordinalValueTransformHelper(int value) {
        switch (value) {
            case -3:
                return 2;
            case 0:
                return 0;
            case 3:
                return 1;
            default:
                return 0;
        }
    }

    @Setter(CameraKeys.HUE)
    public void setHue(int hue, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (hue >= -3 && hue <= 3) {
            DataBaseCameraSetting setter = new DataBaseCameraSetting();
            setter.setCmdId("Tonal");
            setter.setValue(hue);
            setter.setReceiverId(getReceiverIdByIndex());
            setter.start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.AnonymousClass70 */

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

    @Getter(CameraKeys.HUE)
    public void getHue(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataBaseCameraGetting getter = new DataBaseCameraGetting();
        getter.setCmdId("Tonal");
        getter.setReceiverId(getReceiverIdByIndex());
        getter.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.AnonymousClass71 */

            public void onSuccess(Object model) {
                if (callback != null) {
                    callback.onSuccess(Integer.valueOf(BytesUtil.getByte(getter.getValue())));
                }
            }

            public void onFailure(Ccode ccode) {
                if (callback != null) {
                    callback.onFails(DJICameraError.getDJIError(ccode));
                }
            }
        });
    }

    @Setter(CameraKeys.CUSTOM_INFORMATION)
    public void setMediaFileCustomInformation(String information, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (TextUtils.isEmpty(information) || information.length() > 32) {
            CallbackUtils.onFailure(callback, DJICameraError.COMMON_PARAM_ILLEGAL);
            return;
        }
        DataCameraSetCustomInformation setter = DataCameraSetCustomInformation.getInstance();
        setter.setReceiverId(getReceiverIdByIndex());
        setter.setInformation(BytesUtil.getBytesUTF8(information)).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.AnonymousClass72 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, (Object) null);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJICameraError.getDJIError(ccode));
            }
        });
    }

    @Getter(CameraKeys.CUSTOM_INFORMATION)
    public void getMediaFileCustomInformation(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        ((DataCameraGetCustomInformation) DataCameraGetCustomInformation.getInstance().setReceiverId(getReceiverIdByIndex(), DataCameraGetCustomInformation.class)).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.AnonymousClass73 */

            public void onSuccess(Object model) {
                String information = DataCameraGetCustomInformation.getInstance().getCustomInformation();
                if (TextUtils.isEmpty(information)) {
                    CallbackUtils.onFailure(callback, DJICameraError.PARAMETERS_GET_FAILED);
                } else {
                    CallbackUtils.onSuccess(callback, information);
                }
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJICameraError.getDJIError(ccode));
            }
        });
    }

    @Setter(CameraKeys.MECHANICAL_SHUTTER_ENABLED)
    public void setMechanicalShutterEnabled(Boolean data, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!isMechanicalShutterSupported()) {
            CallbackUtils.onFailure(callback, DJIError.COMMAND_NOT_SUPPORTED_BY_FIRMWARE);
            return;
        }
        DataCameraSetMechanicalShutter setter = new DataCameraSetMechanicalShutter();
        setter.setReceiverId(getReceiverIdByIndex());
        setter.setEnable(data.booleanValue());
        setter.start(CallbackUtils.defaultCB(callback));
    }

    @Setter(CameraKeys.AUTO_LOCK_GIMBAL_ENABLED)
    public void setLockGimbalWhileShooting(boolean enabled, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        ((DataCameraSetLockGimbalWhenShot) DataCameraSetLockGimbalWhenShot.getInstance().setReceiverId(getReceiverIdByIndex(), DataCameraSetLockGimbalWhenShot.class)).setLockGimbalWhenShot(enabled).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.AnonymousClass74 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, (Object) null);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJICameraError.getDJIError(ccode));
            }
        });
    }

    /* access modifiers changed from: protected */
    public int getProtocolVersion() {
        return 1;
    }

    @Setter(CameraKeys.WATERMARK_SETTINGS)
    public void setWatermarkDisplayModeSettings(final WatermarkSettings settings, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        getCameraWatermarkSetter(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.AnonymousClass75 */

            public void onSuccess(Object model) {
                DataCameraWatermark setter = (DataCameraWatermark) model;
                setter.setReceiverId(DJICameraBaseAbstraction.this.getReceiverIdByIndex());
                setter.setGetMode(false).setProtocolVersion(DJICameraBaseAbstraction.this.getProtocolVersion()).setImageWatermark(settings.isEnabledForPhotos()).setVideoWatermark(settings.isEnabledForVideos()).setLiveViewEnabled(settings.isEnabledForLiveView()).start(CallbackUtils.defaultCB(callback, DJICameraError.class));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJICameraError.getDJIError(ccode));
            }
        });
    }

    @Getter(CameraKeys.WATERMARK_SETTINGS)
    public void getWatermarkDisplayModeSettings(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataCameraWatermark getter = new DataCameraWatermark();
        ((DataCameraWatermark) getter.setGetMode(true).setProtocolVersion(getProtocolVersion()).setReceiverId(getReceiverIdByIndex(), DataCameraWatermark.class)).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.AnonymousClass76 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, new WatermarkSettings(getter.isVideoWatermarkEnabled(), getter.isImageWatermarkEnabled(), getter.isLiveViewEnabled()));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJICameraError.getDJIError(ccode));
            }
        });
    }

    @Setter(CameraKeys.WATERMARK_DISPLAY_CONTENT_SETTINGS)
    public void setWatermarkDisplayContentSettings(final WatermarkDisplayContentSettings settings, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        getCameraWatermarkSetter(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.AnonymousClass77 */

            public void onSuccess(Object model) {
                DataCameraWatermark setter = (DataCameraWatermark) model;
                setter.setReceiverId(DJICameraBaseAbstraction.this.getReceiverIdByIndex());
                setter.setGetMode(false).setProtocolVersion(DJICameraBaseAbstraction.this.getProtocolVersion()).setCameraSnEnabled(settings.isCameraSnEnabled()).setCameraTypeEnabled(settings.isCameraTypeEnabled()).setDroneTypeEnabled(settings.isDroneTypeEnabled()).setDroneSnEnabled(settings.isDroneSnEnabled()).setDateTimeEnabled(settings.isDateTimeEnabled()).setGPSAltitudeEnabled(settings.isGpsAltitudeEnabled()).setGPSLonLatEnabled(settings.isGpsLonLatEnabled()).setTimeZoneEnabled(settings.isTimeZoneEnabled()).setUserCustomEnabled(settings.isUserCustomStringEnabled()).start(CallbackUtils.defaultCB(callback, DJICameraError.class));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJICameraError.getDJIError(ccode));
            }
        });
    }

    @Getter(CameraKeys.WATERMARK_DISPLAY_CONTENT_SETTINGS)
    public void getWatermarkDisplayContentSettings(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataCameraWatermark getter = new DataCameraWatermark();
        ((DataCameraWatermark) getter.setGetMode(true).setProtocolVersion(getProtocolVersion()).setReceiverId(getReceiverIdByIndex(), DataCameraWatermark.class)).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.AnonymousClass78 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, new WatermarkDisplayContentSettings.Builder().isCameraSnEnabled(getter.isCameraSnEnabled()).isCameraTypeEnabled(getter.isCameraTypeEnabled()).isDateTimeEnabled(getter.isDateTimeEnabled()).isDroneSnEnabled(getter.isDroneSnEnabled()).isDroneTypeEnabled(getter.isDronetypenabled()).isGPSAltitudeEnabled(getter.isGPSAltitudeEnabled()).isGPSLonLatEnabled(getter.isGPSLonLatEnabled()).isTimeZoneEnabled(getter.isTimeZoneEnabled()).isUserCustomStringEnabled(getter.isUserCustomInfoEnabled()).build());
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJICameraError.getDJIError(ccode));
            }
        });
    }

    @Setter(CameraKeys.WATERMARK_USER_CUSTOM_INFO)
    public void setWatermarkUserCustomInfo(final String infos, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        getCameraWatermarkSetter(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.AnonymousClass79 */

            public void onSuccess(Object model) {
                DataCameraWatermark setter = (DataCameraWatermark) model;
                setter.setReceiverId(DJICameraBaseAbstraction.this.getReceiverIdByIndex());
                setter.setGetMode(false).setProtocolVersion(DJICameraBaseAbstraction.this.getProtocolVersion()).setUserCustomString(infos).start(CallbackUtils.defaultCB(callback, DJICameraError.class));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJICameraError.getDJIError(ccode));
            }
        });
    }

    @Getter(CameraKeys.WATERMARK_USER_CUSTOM_INFO)
    public void getWatermarkUserCustomInfo(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataCameraWatermark getter = new DataCameraWatermark();
        ((DataCameraWatermark) getter.setGetMode(true).setProtocolVersion(getProtocolVersion()).setReceiverId(getReceiverIdByIndex(), DataCameraWatermark.class)).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.AnonymousClass80 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, getter.getUserCustomInfo());
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJICameraError.getDJIError(ccode));
            }
        });
    }

    private void getCameraWatermarkSetter(final DJIDataCallBack callback) {
        final DataCameraWatermark getter = new DataCameraWatermark();
        ((DataCameraWatermark) getter.setGetMode(true).setProtocolVersion(getProtocolVersion()).setReceiverId(getReceiverIdByIndex(), DataCameraWatermark.class)).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.camera.DJICameraBaseAbstraction.AnonymousClass81 */

            public void onSuccess(Object model) {
                if (callback != null) {
                    callback.onSuccess(getter.mapToSetterFromGetter());
                }
            }

            public void onFailure(Ccode ccode) {
                if (callback != null) {
                    callback.onFailure(ccode);
                }
            }
        });
    }
}
