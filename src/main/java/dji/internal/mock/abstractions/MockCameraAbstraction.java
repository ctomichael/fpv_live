package dji.internal.mock.abstractions;

import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import dji.common.camera.CameraUtils;
import dji.common.camera.ExposureSettings;
import dji.common.camera.PhotoTimeLapseSettings;
import dji.common.camera.ResolutionAndFrameRate;
import dji.common.camera.SettingsDefinitions;
import dji.common.camera.ThermalAreaTemperatureAggregations;
import dji.common.camera.ThermalExternalSceneSettings;
import dji.common.camera.WhiteBalance;
import dji.common.error.DJICameraError;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.util.RandomUtils;
import dji.sdksharedlib.hardware.abstractions.Action;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Setter;
import dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraXTBaseAbstraction;
import dji.sdksharedlib.keycatalog.CameraKeys;
import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.functions.Func1;
import dji.thirdparty.rx.schedulers.Schedulers;
import java.util.concurrent.TimeUnit;

@EXClassNullAway
public class MockCameraAbstraction extends DJICameraXTBaseAbstraction {
    /* access modifiers changed from: private */
    public SettingsDefinitions.CameraMode cameraMode = SettingsDefinitions.CameraMode.SHOOT_PHOTO;
    /* access modifiers changed from: private */
    public long captureCount = 1000;
    /* access modifiers changed from: private */
    public int counter = 0;
    /* access modifiers changed from: private */
    public Integer currentShootingTime = 0;
    /* access modifiers changed from: private */
    public SettingsDefinitions.ExposureMode exposureMode = SettingsDefinitions.ExposureMode.PROGRAM;
    /* access modifiers changed from: private */
    public ExposureSettings exposureSettings = new ExposureSettings(SettingsDefinitions.Aperture.F_2, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_8000, 100, SettingsDefinitions.ExposureCompensation.N_0_0);
    /* access modifiers changed from: private */
    public int interverForStoring = 3;
    private boolean isLocked = true;
    /* access modifiers changed from: private */
    public Boolean isRecording = false;
    private boolean isShootingIntervalPhoto = false;
    /* access modifiers changed from: private */
    public boolean isShootingPhoto = false;
    /* access modifiers changed from: private */
    public boolean isShootingPhotoEnabled = true;
    private SettingsDefinitions.PhotoBurstCount photoBurstCount = SettingsDefinitions.PhotoBurstCount.BURST_COUNT_3;
    private SettingsDefinitions.PhotoTimeIntervalSettings photoIntervalParam = new SettingsDefinitions.PhotoTimeIntervalSettings(255, 2);
    /* access modifiers changed from: private */
    public SettingsDefinitions.ShootPhotoMode shootPhotoMode = SettingsDefinitions.ShootPhotoMode.SINGLE;
    /* access modifiers changed from: private */
    public int startInterForStoring = 0;

    static /* synthetic */ int access$008(MockCameraAbstraction x0) {
        int i = x0.counter;
        x0.counter = i + 1;
        return i;
    }

    static /* synthetic */ int access$2308(MockCameraAbstraction x0) {
        int i = x0.startInterForStoring;
        x0.startInterForStoring = i + 1;
        return i;
    }

    public MockCameraAbstraction() {
        generateFakeCameraInfo();
    }

    private void generateFakeCameraInfo() {
        Observable.timer(1, TimeUnit.SECONDS, Schedulers.newThread()).flatMap(new Func1<Long, Observable<Boolean>>() {
            /* class dji.internal.mock.abstractions.MockCameraAbstraction.AnonymousClass1 */

            public Observable<Boolean> call(Long aLong) {
                MockCameraAbstraction.access$008(MockCameraAbstraction.this);
                MockCameraAbstraction.this.notifyValueChangeForKeyPathFromSetter(SettingsDefinitions.Aperture.values(), CameraKeys.APERTURE_RANGE);
                MockCameraAbstraction.this.notifyValueChangeForKeyPathFromSetter(SettingsDefinitions.ISO.values(), CameraKeys.ISO_RANGE);
                MockCameraAbstraction.this.notifyValueChangeForKeyPathFromSetter(MockCameraAbstraction.this.exposureSettings, CameraKeys.EXPOSURE_SETTINGS);
                MockCameraAbstraction.this.notifyValueChangeForKeyPathFromSetter(Long.valueOf(MockCameraAbstraction.this.captureCount), CameraKeys.SDCARD_AVAILABLE_CAPTURE_COUNT);
                MockCameraAbstraction.this.notifyValueChangeForKeyPathFromSetter(Integer.valueOf(1000 - (MockCameraAbstraction.this.counter % 1000)), CameraKeys.SDCARD_REMAINING_SPACE_IN_MB);
                MockCameraAbstraction.this.notifyValueChangeForKeyPath(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.values()[MockCameraAbstraction.this.counter % 6], SettingsDefinitions.VideoFrameRate.values()[MockCameraAbstraction.this.counter % 8]), CameraKeys.RESOLUTION_FRAME_RATE);
                MockCameraAbstraction.this.notifyValueChangeForKeyPath(Integer.valueOf(1000 - (MockCameraAbstraction.this.counter % 1000)), CameraKeys.SDCARD_AVAILABLE_RECORDING_TIME_IN_SECONDS);
                MockCameraAbstraction.this.notifyValueChangeForKeyPath(true, CameraKeys.IS_THERMAL_CAMERA);
                MockCameraAbstraction.this.notifyValueChangeForKeyPath(MockCameraAbstraction.this.cameraMode, MockCameraAbstraction.this.convertKeyToPath("Mode"));
                MockCameraAbstraction.this.notifyValueChangeForKeyPath(Boolean.valueOf(MockCameraAbstraction.this.isShootingPhotoEnabled), MockCameraAbstraction.this.convertKeyToPath(CameraKeys.IS_SHOOTING_PHOTO_ENABLED));
                if (MockCameraAbstraction.this.cameraMode == SettingsDefinitions.CameraMode.SHOOT_PHOTO) {
                    MockCameraAbstraction.this.notifyValueChangeForKeyPathFromSetter(MockCameraAbstraction.this.shootPhotoMode, MockCameraAbstraction.this.convertKeyToPath(CameraKeys.SHOOT_PHOTO_MODE));
                    MockCameraAbstraction.this.notifyValueChangeForKeyPath(Boolean.valueOf(MockCameraAbstraction.this.isShootingPhoto), MockCameraAbstraction.this.convertKeyToPath(CameraKeys.IS_SHOOTING_PHOTO));
                    if (MockCameraAbstraction.this.isShootingPhoto) {
                        if (MockCameraAbstraction.this.startInterForStoring < MockCameraAbstraction.this.interverForStoring) {
                            MockCameraAbstraction.this.notifyValueChangeForKeyPath(true, CameraKeys.IS_STORING_PHOTO);
                        } else {
                            boolean unused = MockCameraAbstraction.this.isShootingPhoto = false;
                            MockCameraAbstraction.this.notifyValueChangeForKeyPath(false, CameraKeys.IS_STORING_PHOTO);
                        }
                        MockCameraAbstraction.access$2308(MockCameraAbstraction.this);
                    }
                } else if (MockCameraAbstraction.this.cameraMode == SettingsDefinitions.CameraMode.RECORD_VIDEO) {
                    MockCameraAbstraction.this.notifyValueChangeForKeyPath(MockCameraAbstraction.this.isRecording, MockCameraAbstraction.this.convertKeyToPath(CameraKeys.IS_RECORDING));
                    if (MockCameraAbstraction.this.isRecording.booleanValue()) {
                        Integer unused2 = MockCameraAbstraction.this.currentShootingTime;
                        Integer unused3 = MockCameraAbstraction.this.currentShootingTime = Integer.valueOf(MockCameraAbstraction.this.currentShootingTime.intValue() + 1);
                        MockCameraAbstraction.this.notifyValueChangeForKeyPath(MockCameraAbstraction.this.currentShootingTime, MockCameraAbstraction.this.convertKeyToPath(CameraKeys.CURRENT_VIDEO_RECORDING_TIME_IN_SECONDS));
                    } else {
                        Integer unused4 = MockCameraAbstraction.this.currentShootingTime = 0;
                    }
                }
                MockCameraAbstraction.this.notifyValueChangeForKeyPath(MockCameraAbstraction.this.exposureMode, MockCameraAbstraction.this.convertKeyToPath(CameraKeys.EXPOSURE_MODE));
                return Observable.just(true);
            }
        }).repeat().subscribe();
    }

    /* access modifiers changed from: protected */
    public boolean isPlaybackSupported() {
        return true;
    }

    @Setter("Mode")
    public void setCameraMode(SettingsDefinitions.CameraMode mode, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (mode == SettingsDefinitions.CameraMode.UNKNOWN) {
            if (callback != null) {
                callback.onFails(DJICameraError.INVALID_PARAMETERS);
            }
        } else if (SettingsDefinitions.CameraMode.PLAYBACK != mode || isPlaybackSupported()) {
            this.cameraMode = mode;
            if (callback != null) {
                callback.onSuccess(null);
            }
        } else if (callback != null) {
            callback.onFails(DJICameraError.INVALID_PARAMETERS);
        }
    }

    @Setter(CameraKeys.THERMAL_FFC_MODE)
    public void setThermalFFCMode(SettingsDefinitions.ThermalFFCMode ffcMode, DJISDKCacheHWAbstraction.InnerCallback callback) {
        notifyValueChangeForKeyPath(ffcMode, CameraKeys.THERMAL_FFC_MODE);
    }

    @Action(CameraKeys.TRIGGER_THERMAL_FFC)
    public void triggerFFC(DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            notifyValueChangeForKeyPath(Integer.valueOf(RandomUtils.createRandom().nextInt()), convertKeyToPath(CameraKeys.THERMAL_TEMPERATURE_DATA));
            notifyValueChangeForKeyPath(new ThermalAreaTemperatureAggregations((float) RandomUtils.createRandom().nextInt(), (float) RandomUtils.createRandom().nextInt(), new Point(RandomUtils.createRandom().nextInt(), RandomUtils.createRandom().nextInt()), (float) RandomUtils.createRandom().nextInt(), new Point(RandomUtils.createRandom().nextInt(), RandomUtils.createRandom().nextInt())), convertKeyToPath(CameraKeys.THERMAL_AREA_TEMPERATURE_AGGREGATIONS));
            callback.onSuccess(null);
        }
    }

    /* access modifiers changed from: protected */
    public void initializeComponentCharacteristics() {
        addCharacteristics(CameraKeys.class, getClass());
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, dji.sdksharedlib.keycatalog.DJISDKCacheKey):void
     arg types: [int, dji.sdksharedlib.keycatalog.DJISDKCacheKey]
     candidates:
      dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, java.lang.String):void
      dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, dji.sdksharedlib.keycatalog.DJISDKCacheKey):void */
    @Setter(CameraKeys.THERMAL_SPOT_METERING_TARGET_POINT)
    public void setThermalSpotMeteringTargetPoint(PointF thermalSpotMeteringTargetPoint, DJISDKCacheHWAbstraction.InnerCallback callback) {
        notifyValueChangeForKeyPath(thermalSpotMeteringTargetPoint, convertKeyToPath(CameraKeys.THERMAL_SPOT_METERING_TARGET_POINT));
        notifyValueChangeForKeyPath((Object) 0, convertKeyToPath(CameraKeys.THERMAL_TEMPERATURE_DATA));
    }

    @Setter(CameraKeys.THERMAL_METERING_AREA)
    public void setThermalSpotMeteringArea(RectF thermalSpotMeteringArea, DJISDKCacheHWAbstraction.InnerCallback callback) {
        notifyValueChangeForKeyPath(thermalSpotMeteringArea, convertKeyToPath(CameraKeys.THERMAL_METERING_AREA));
        notifyValueChangeForKeyPath(new ThermalAreaTemperatureAggregations(0.0f, 0.0f, new Point(0, 0), 0.0f, new Point(0, 0)), convertKeyToPath(CameraKeys.THERMAL_AREA_TEMPERATURE_AGGREGATIONS));
        if (callback != null) {
            callback.onSuccess(null);
        }
    }

    @Setter(CameraKeys.THERMAL_CUSTOM_EXTERNAL_SCENE_SETTINGS_PROFILE)
    public void setThermalCustomExternalSceneSettingsProfile(SettingsDefinitions.ThermalCustomExternalSceneSettingsProfile settings, DJISDKCacheHWAbstraction.InnerCallback callback) {
        notifyValueChangeForKeyPath(settings, CameraKeys.THERMAL_CUSTOM_EXTERNAL_SCENE_SETTINGS_PROFILE);
        if (callback != null) {
            callback.onSuccess(null);
        }
    }

    @Setter(CameraKeys.PICTURE_STYLE_PRESET)
    public void setPictureStylePreset(SettingsDefinitions.PictureStylePreset preset, DJISDKCacheHWAbstraction.InnerCallback callback) {
        notifyValueChangeForKeyPath(preset, CameraKeys.PICTURE_STYLE_PRESET);
        if (callback != null) {
            callback.onSuccess(null);
        }
    }

    @Setter(CameraKeys.CAMERA_COLOR)
    public void setDigitalFilter(SettingsDefinitions.CameraColor filter, DJISDKCacheHWAbstraction.InnerCallback callback) {
        notifyValueChangeForKeyPathFromSetter(filter, CameraKeys.CAMERA_COLOR);
        if (callback != null) {
            callback.onSuccess(null);
        }
    }

    @Setter(CameraKeys.THERMAL_CUSTOM_EXTERNAL_SCENE_SETTINGS_PROFILE)
    public void setThermalExternalSceneSettings(ThermalExternalSceneSettings settings, DJISDKCacheHWAbstraction.InnerCallback callback) {
        notifyValueChangeForKeyPath(settings, CameraKeys.THERMAL_CUSTOM_EXTERNAL_SCENE_SETTINGS_PROFILE);
        if (callback != null) {
            callback.onSuccess(null);
        }
    }

    @Setter(CameraKeys.THERMAL_ATMOSPHERIC_TEMPERATURE)
    public void setThermalAtmosphericTemperature(float value, DJISDKCacheHWAbstraction.InnerCallback callback) {
        notifyValueChangeForKeyPath(Float.valueOf(value), CameraKeys.THERMAL_ATMOSPHERIC_TEMPERATURE);
        if (callback != null) {
            callback.onSuccess(null);
        }
    }

    @Setter(CameraKeys.THERMAL_ATMOSPHERIC_TRANSMISSION_COEFFICIENT)
    public void setThermalAtmosphericTransmissionCoefficient(float value, DJISDKCacheHWAbstraction.InnerCallback callback) {
        notifyValueChangeForKeyPath(Float.valueOf(value), CameraKeys.THERMAL_ATMOSPHERIC_TRANSMISSION_COEFFICIENT);
        if (callback != null) {
            callback.onSuccess(null);
        }
    }

    @Setter(CameraKeys.THERMAL_BACKGROUND_TEMPERATURE)
    public void setThermalBackgroundTemperature(float value, DJISDKCacheHWAbstraction.InnerCallback callback) {
        notifyValueChangeForKeyPath(Float.valueOf(value), CameraKeys.THERMAL_BACKGROUND_TEMPERATURE);
        if (callback != null) {
            callback.onSuccess(null);
        }
    }

    @Setter(CameraKeys.THERMAL_SCENE_EMISSIVITY)
    public void setThermalSceneEmissivity(float value, DJISDKCacheHWAbstraction.InnerCallback callback) {
        notifyValueChangeForKeyPath(Float.valueOf(value), CameraKeys.THERMAL_SCENE_EMISSIVITY);
        if (callback != null) {
            callback.onSuccess(null);
        }
    }

    @Setter(CameraKeys.THERMAL_WINDOW_REFLECTION)
    public void setThermalWindowReflection(float value, DJISDKCacheHWAbstraction.InnerCallback callback) {
        notifyValueChangeForKeyPath(Float.valueOf(value), CameraKeys.THERMAL_WINDOW_REFLECTION);
        if (callback != null) {
            callback.onSuccess(null);
        }
    }

    @Setter(CameraKeys.THERMAL_WINDOW_REFLECTED_TEMPERATURE)
    public void setThermalWindowReflectedTemperature(float value, DJISDKCacheHWAbstraction.InnerCallback callback) {
        notifyValueChangeForKeyPath(Float.valueOf(value), CameraKeys.THERMAL_WINDOW_REFLECTED_TEMPERATURE);
        if (callback != null) {
            callback.onSuccess(null);
        }
    }

    @Setter(CameraKeys.THERMAL_WINDOW_TEMPERATURE)
    public void setThermalWindowTemperature(float value, DJISDKCacheHWAbstraction.InnerCallback callback) {
        notifyValueChangeForKeyPath(Float.valueOf(value), CameraKeys.THERMAL_WINDOW_TEMPERATURE);
        if (callback != null) {
            callback.onSuccess(null);
        }
    }

    @Setter(CameraKeys.THERMAL_WINDOW_TRANSMISSION_COEFFICIENT)
    public void setThermalWindowTransmissionCoefficient(float value, DJISDKCacheHWAbstraction.InnerCallback callback) {
        notifyValueChangeForKeyPath(Float.valueOf(value), CameraKeys.THERMAL_WINDOW_TRANSMISSION_COEFFICIENT);
        if (callback != null) {
            callback.onSuccess(null);
        }
    }

    @Setter(CameraKeys.SHUTTER_SPEED)
    public void setShutterSpeed(SettingsDefinitions.ShutterSpeed speed, DJISDKCacheHWAbstraction.InnerCallback callback) {
        this.exposureSettings = new ExposureSettings(this.exposureSettings.getAperture(), speed, this.exposureSettings.getISO(), this.exposureSettings.getExposureCompensation());
        notifyValueChangeForKeyPathFromSetter(this.exposureSettings, CameraKeys.EXPOSURE_SETTINGS);
        if (callback != null) {
            callback.onSuccess(null);
        }
    }

    @Setter(CameraKeys.WHITE_BALANCE)
    public void setWhiteBalance(WhiteBalance wb, DJISDKCacheHWAbstraction.InnerCallback callback) {
        notifyValueChangeForKeyPath(wb, CameraKeys.WHITE_BALANCE);
        if (callback != null) {
            callback.onSuccess(null);
        }
    }

    @Setter(CameraKeys.ISO)
    public void setISO(SettingsDefinitions.ISO iso, DJISDKCacheHWAbstraction.InnerCallback callback) {
        SettingsDefinitions.ISO iso2 = iso;
        if (iso.equals(SettingsDefinitions.ISO.AUTO)) {
            iso2 = SettingsDefinitions.ISO.values()[this.counter % SettingsDefinitions.ISO.values().length];
        }
        this.exposureSettings = new ExposureSettings(this.exposureSettings.getAperture(), this.exposureSettings.getShutterSpeed(), CameraUtils.convertISOToInt(iso2), this.exposureSettings.getExposureCompensation());
        notifyValueChangeForKeyPathFromSetter(this.exposureSettings, CameraKeys.EXPOSURE_SETTINGS);
        if (callback != null) {
            callback.onSuccess(null);
        }
    }

    @Setter(CameraKeys.APERTURE)
    public void setAperture(SettingsDefinitions.Aperture aperture, DJISDKCacheHWAbstraction.InnerCallback callback) {
        this.exposureSettings = new ExposureSettings(aperture, this.exposureSettings.getShutterSpeed(), this.exposureSettings.getISO(), this.exposureSettings.getExposureCompensation());
        notifyValueChangeForKeyPathFromSetter(this.exposureSettings, CameraKeys.EXPOSURE_SETTINGS);
        if (callback != null) {
            callback.onSuccess(null);
        }
    }

    @Setter(CameraKeys.EXPOSURE_COMPENSATION)
    public void setExposureCompensation(SettingsDefinitions.ExposureCompensation ec, DJISDKCacheHWAbstraction.InnerCallback callback) {
        this.exposureSettings = new ExposureSettings(this.exposureSettings.getAperture(), this.exposureSettings.getShutterSpeed(), this.exposureSettings.getISO(), ec);
        if (callback != null) {
            callback.onSuccess(null);
        }
    }

    @Setter(CameraKeys.AE_LOCK)
    public void setAELock(boolean isAELocked, DJISDKCacheHWAbstraction.InnerCallback callback) {
        notifyValueChangeForKeyPathFromSetter(Boolean.valueOf(isAELocked), CameraKeys.AE_LOCK);
        if (callback != null) {
            callback.onSuccess(null);
        }
    }

    @Setter(CameraKeys.EXPOSURE_MODE)
    public void setExposureMode(SettingsDefinitions.ExposureMode em, DJISDKCacheHWAbstraction.InnerCallback callback) {
        this.exposureMode = em;
        if (callback != null) {
            callback.onSuccess(null);
        }
    }

    @Setter(CameraKeys.FOCUS_MODE)
    public void setFocusMode(SettingsDefinitions.FocusMode focusMode, DJISDKCacheHWAbstraction.InnerCallback callback) {
        notifyValueChangeForKeyPath(focusMode, CameraKeys.FOCUS_MODE);
        if (callback != null) {
            callback.onSuccess(null);
        }
    }

    @Setter(CameraKeys.EXPOSURE_SETTINGS)
    public void setExposureSettings(ExposureSettings es, DJISDKCacheHWAbstraction.InnerCallback callback) {
        notifyValueChangeForKeyPath(this.exposureSettings, CameraKeys.EXPOSURE_SETTINGS);
        if (callback != null) {
            callback.onSuccess(null);
        }
    }

    @Setter(CameraKeys.PHOTO_FILE_FORMAT)
    public void setPhotoFileFormat(SettingsDefinitions.PhotoFileFormat ff, DJISDKCacheHWAbstraction.InnerCallback callback) {
        notifyValueChangeForKeyPath(ff, CameraKeys.PHOTO_FILE_FORMAT);
        if (callback != null) {
            callback.onSuccess(null);
        }
    }

    @Setter(CameraKeys.VIDEO_FILE_FORMAT)
    public void setVideoFileFormat(SettingsDefinitions.VideoFileFormat ff, DJISDKCacheHWAbstraction.InnerCallback callback) {
        notifyValueChangeForKeyPath(ff, CameraKeys.VIDEO_FILE_FORMAT);
        if (callback != null) {
            callback.onSuccess(null);
        }
    }

    @Setter(CameraKeys.SHOOT_PHOTO_MODE)
    public void setShootPhotoMode(SettingsDefinitions.ShootPhotoMode mode, DJISDKCacheHWAbstraction.InnerCallback callback) {
        this.shootPhotoMode = mode;
        notifyValueChangeForKeyPath(mode, CameraKeys.SHOOT_PHOTO_MODE);
        if (callback != null) {
            callback.onSuccess(null);
        }
    }

    @Setter(CameraKeys.PHOTO_TIME_LAPSE_SETTINGS)
    public void setPhotoTimeLapseSettings(PhotoTimeLapseSettings settings, DJISDKCacheHWAbstraction.InnerCallback callback) {
        notifyValueChangeForKeyPath(settings, CameraKeys.PHOTO_TIME_LAPSE_SETTINGS);
        if (callback != null) {
            callback.onSuccess(null);
        }
    }

    @Setter(CameraKeys.PHOTO_TIME_INTERVAL_SETTINGS)
    public void setPhotoTimeIntervalSettings(SettingsDefinitions.PhotoTimeIntervalSettings settings, DJISDKCacheHWAbstraction.InnerCallback callback) {
        notifyValueChangeForKeyPath(settings, convertKeyToPath(CameraKeys.PHOTO_TIME_INTERVAL_SETTINGS));
        notifyValueChangeForKeyPath(Boolean.valueOf(this.isShootingIntervalPhoto), convertKeyToPath(CameraKeys.IS_SHOOTING_INTERVAL_PHOTO));
        if (callback != null) {
            callback.onSuccess(null);
        }
    }

    @Setter(CameraKeys.PHOTO_BURST_COUNT)
    public void setPhotoBurstCount(SettingsDefinitions.PhotoBurstCount count, DJISDKCacheHWAbstraction.InnerCallback callback) {
        notifyValueChangeForKeyPath(count, convertKeyToPath(CameraKeys.PHOTO_BURST_COUNT));
        if (callback != null) {
            callback.onSuccess(null);
        }
    }

    @Setter(CameraKeys.PHOTO_AEB_COUNT)
    public void setPhotoAEBCount(SettingsDefinitions.PhotoAEBCount count, DJISDKCacheHWAbstraction.InnerCallback callback) {
        notifyValueChangeForKeyPath(count, CameraKeys.PHOTO_AEB_COUNT);
        if (callback != null) {
            callback.onSuccess(null);
        }
    }

    @Setter(CameraKeys.PHOTO_ASPECT_RATIO)
    public void setPhotoAspectRatio(SettingsDefinitions.PhotoAspectRatio ratio, DJISDKCacheHWAbstraction.InnerCallback callback) {
        notifyValueChangeForKeyPath(ratio, CameraKeys.PHOTO_ASPECT_RATIO);
        if (callback != null) {
            callback.onSuccess(null);
        }
    }

    /* access modifiers changed from: protected */
    public boolean isOverallTemperatureMeterSupported() {
        return true;
    }

    /* access modifiers changed from: protected */
    public void startShootPhoto(DJISDKCacheHWAbstraction.InnerCallback callback, SettingsDefinitions.ShootPhotoMode mode) {
        this.captureCount = this.captureCount < 0 ? 1000 : this.captureCount - 1;
        this.shootPhotoMode = mode;
        this.startInterForStoring = 0;
        this.isShootingIntervalPhoto = false;
        this.isShootingPhoto = true;
        if (this.shootPhotoMode == SettingsDefinitions.ShootPhotoMode.SINGLE) {
            this.interverForStoring = 2;
        } else if (this.shootPhotoMode == SettingsDefinitions.ShootPhotoMode.INTERVAL) {
            this.isShootingIntervalPhoto = true;
            this.interverForStoring = 1000;
        } else {
            this.interverForStoring = 3;
        }
        callback.onSuccess(null);
    }

    @Action(CameraKeys.STOP_SHOOT_PHOTO)
    public void stopShootPhoto(DJISDKCacheHWAbstraction.InnerCallback callback) {
        this.isShootingPhoto = false;
        this.isShootingIntervalPhoto = false;
        int temp = this.shootPhotoMode.value() + 1;
        if (temp > 7) {
            temp = 0;
        }
        this.shootPhotoMode = SettingsDefinitions.ShootPhotoMode.find(temp);
    }

    @Action(CameraKeys.START_RECORD_VIDEO)
    public void startRecordVideo(DJISDKCacheHWAbstraction.InnerCallback callback) {
        this.isRecording = true;
        if (callback != null) {
            callback.onSuccess(null);
        }
    }

    @Action(CameraKeys.STOP_RECORD_VIDEO)
    public void stopRecordVideo(DJISDKCacheHWAbstraction.InnerCallback callback) {
        this.isRecording = false;
        if (callback != null) {
            callback.onSuccess(null);
        }
    }
}
