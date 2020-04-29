package dji.sdksharedlib.keycatalog;

import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import dji.common.camera.CameraPeakThreshold;
import dji.common.camera.CameraRecordingState;
import dji.common.camera.CameraSSDVideoLicense;
import dji.common.camera.ColorWaveformSettings;
import dji.common.camera.ExposureSettings;
import dji.common.camera.FocusAssistantSettings;
import dji.common.camera.OriginalPhotoSettings;
import dji.common.camera.PhotoTimeLapseSettings;
import dji.common.camera.QuickPreviewSettings;
import dji.common.camera.ResolutionAndFrameRate;
import dji.common.camera.SSDCapacity;
import dji.common.camera.SSDOperationState;
import dji.common.camera.SaveOriginalPhotoState;
import dji.common.camera.SettingsDefinitions;
import dji.common.camera.StabilizationState;
import dji.common.camera.TauVideoFormat;
import dji.common.camera.ThermalAreaTemperatureAggregations;
import dji.common.camera.ThermalMeasurementMode;
import dji.common.camera.WatermarkDisplayContentSettings;
import dji.common.camera.WatermarkSettings;
import dji.common.camera.WhiteBalance;
import dji.common.flightcontroller.DJIMultiLEDControlMode;
import dji.common.flightcontroller.LEDsSettings;
import dji.common.util.DJIParamMinMaxCapability;
import dji.fieldAnnotation.EXClassNullAway;
import dji.internal.camera.SSDFileSystem;
import dji.internal.camera.SSDRawMode;
import dji.internal.mock.abstractions.MockCameraAbstraction;
import dji.midware.data.model.P3.DataCameraGetPushStateInfo;
import dji.midware.data.model.P3.DataCameraSetStorageInfo;
import dji.midware.data.model.P3.DataCameraSetVOutParams;
import dji.midware.data.model.P3.DataEyeSetPseudoCameraControl;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheUpdateType;
import dji.sdksharedlib.hardware.abstractions.camera.FoldingDrone.DJICameraFoldingDroneSAbstraction;
import dji.sdksharedlib.hardware.abstractions.camera.FoldingDrone.DJICameraFoldingDroneXAbstraction;
import dji.sdksharedlib.hardware.abstractions.camera.FoldingDrone.DJICameraSparkAbstraction;
import dji.sdksharedlib.hardware.abstractions.camera.FoldingDrone.DJICameraWM160Abstraction;
import dji.sdksharedlib.hardware.abstractions.camera.FoldingDrone.DJICameraWM230Abstraction;
import dji.sdksharedlib.hardware.abstractions.camera.FoldingDrone.DJICameraWM240HasselbladAbstraction;
import dji.sdksharedlib.hardware.abstractions.camera.FoldingDrone.DJICameraWM240ZoomAbstraction;
import dji.sdksharedlib.hardware.abstractions.camera.FoldingDrone.DJICameraWM245Abstraction;
import dji.sdksharedlib.hardware.abstractions.camera.GD600.DJICameraGD600Abstraction;
import dji.sdksharedlib.hardware.abstractions.camera.GD600.DJICameraPayloadAbstraction;
import dji.sdksharedlib.hardware.abstractions.camera.GD600.DJICameraWM245DualLightVLAbstraction;
import dji.sdksharedlib.hardware.abstractions.camera.GD600.DJICameraXT2Abstraction;
import dji.sdksharedlib.hardware.abstractions.camera.multistorage.DJICameraMultiStorageAbstraction;
import dji.sdksharedlib.hardware.abstractions.camera.phantom.DJICameraPhantom3AAbstraction;
import dji.sdksharedlib.hardware.abstractions.camera.phantom.DJICameraPhantom3PAbstraction;
import dji.sdksharedlib.hardware.abstractions.camera.phantom.DJICameraPhantom3SAbstraction;
import dji.sdksharedlib.hardware.abstractions.camera.phantom.DJICameraPhantom3WAbstraction;
import dji.sdksharedlib.hardware.abstractions.camera.phantom.DJICameraPhantom4AAbstraction;
import dji.sdksharedlib.hardware.abstractions.camera.phantom.DJICameraPhantom4Abstraction;
import dji.sdksharedlib.hardware.abstractions.camera.phantom.DJICameraPhantom4PAbstraction;
import dji.sdksharedlib.hardware.abstractions.camera.phantom.DJICameraPhantom4RTKAbstraction;
import dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraInspire2Abstraction;
import dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraInspire2RawAbstraction;
import dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraTau336Abstraction;
import dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraTau640Abstraction;
import dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraTauXT2Abstraction;
import dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraWM245DualLightIRAbstraction;
import dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraX3HandheldAbstraction;
import dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraX4SAbstraction;
import dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraX5Abstraction;
import dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraX5BaseAbstraction;
import dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraX5HandheldAbstraction;
import dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraX5RAbstraction;
import dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraX5RHandheldAbstraction;
import dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraX7Abstraction;
import dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraXTBaseAbstraction;
import dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraZ3Abstraction;
import dji.sdksharedlib.hardware.abstractions.camera.zenmuse.DJICameraZ3HandheldAbstraction;
import dji.sdksharedlib.keycatalog.extension.ComplexKey;
import dji.sdksharedlib.keycatalog.extension.IAbstractionGroup;
import dji.sdksharedlib.keycatalog.extension.InternalKey;
import dji.sdksharedlib.keycatalog.extension.Key;

@EXClassNullAway
public class CameraKeys extends DJISDKCacheKeys {
    @Key(accessType = 6, includedAbstractions = {DJICameraInspire2RawAbstraction.class}, type = CameraSSDVideoLicense.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String ACTIVATE_SSD_VIDEO_LICENSE = "ActivateSSDVideoLicense";
    @Key(accessType = 6, excludedAbstractions = {DJICameraTau336Abstraction.class, DJICameraTauXT2Abstraction.class, DJICameraXT2Abstraction.class, DJICameraTau640Abstraction.class}, type = Boolean.class)
    public static final String AE_LOCK = "AELock";
    @Key(accessType = 6, excludedAbstractions = {DJICameraTau336Abstraction.class, DJICameraTauXT2Abstraction.class, DJICameraTau640Abstraction.class}, type = SettingsDefinitions.AntiFlickerFrequency.class)
    public static final String ANTI_FLICKER_FREQUENCY = "AntiFlickerFrequency";
    @InternalKey
    @Key(accessType = 4, type = SettingsDefinitions.AntiFlickerFrequency[].class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String ANTI_FLICKER_RANGE = "CameraAntiFlickerRange";
    @Key(accessType = 6, excludedAbstractions = {DJICameraXT2Abstraction.class, DJICameraWM160Abstraction.class}, includedAbstractions = {DJICameraX5Abstraction.class, DJICameraX5HandheldAbstraction.class, DJICameraX5RAbstraction.class, DJICameraX5RHandheldAbstraction.class, DJICameraGD600Abstraction.class, DJICameraInspire2Abstraction.class, DJICameraPhantom4PAbstraction.class, DJICameraWM240HasselbladAbstraction.class, MockCameraAbstraction.class}, type = SettingsDefinitions.Aperture.class)
    public static final String APERTURE = "Aperture";
    @InternalKey
    @Key(accessType = 4, includedAbstractions = {DJICameraX5Abstraction.class, DJICameraX5HandheldAbstraction.class, DJICameraX5RAbstraction.class, DJICameraX5RHandheldAbstraction.class, DJICameraInspire2Abstraction.class, DJICameraGD600Abstraction.class, DJICameraPhantom4PAbstraction.class, DJICameraWM240HasselbladAbstraction.class, MockCameraAbstraction.class}, type = SettingsDefinitions.Aperture[].class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String APERTURE_RANGE = "ApertureRange";
    @Key(accessType = 3, includedAbstractions = {DJICameraX3HandheldAbstraction.class, DJICameraX5HandheldAbstraction.class, DJICameraX5RHandheldAbstraction.class, DJICameraZ3HandheldAbstraction.class}, type = Integer.class)
    public static final String AUDIO_GAIN = "AudioGain";
    @Key(accessType = 3, includedAbstractions = {DJICameraX5HandheldAbstraction.class, DJICameraX5RHandheldAbstraction.class, DJICameraX3HandheldAbstraction.class, DJICameraZ3HandheldAbstraction.class}, type = Boolean.class)
    public static final String AUDIO_RECORDING_ENABLED = "AudioRecordingEnabled";
    @Key(accessType = 6, includedAbstractions = {DJICameraPhantom4PAbstraction.class, DJICameraInspire2Abstraction.class}, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String AUTO_AE_UNLOCK_ENABLED = "AutoAEUnlockEnabled";
    @Key(accessType = 6, excludedAbstractions = {DJICameraWM160Abstraction.class}, includedAbstractions = {DJICameraPhantom4PAbstraction.class, DJICameraFoldingDroneXAbstraction.class, DJICameraSparkAbstraction.class, DJICameraWM245DualLightIRAbstraction.class, DJICameraWM245DualLightVLAbstraction.class}, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String AUTO_LOCK_GIMBAL_ENABLED = "AutoLockGimbalEnabled";
    @Key(accessType = 6, excludedAbstractions = {DJICameraTau336Abstraction.class, DJICameraTau640Abstraction.class, DJICameraTauXT2Abstraction.class}, type = SettingsDefinitions.CameraColor.class)
    public static final String CAMERA_COLOR = "CameraColor";
    @InternalKey
    @Key(accessType = 4, excludedAbstractions = {DJICameraTau336Abstraction.class, DJICameraTau640Abstraction.class}, type = SettingsDefinitions.CameraColor[].class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String CAMERA_COLOR_RANGE = "cameraColorRange";
    @Key(accessType = 4, type = CameraPeakThreshold.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String CAMERA_PEAK_THRESHOLD = "CameraPeakThreshold";
    @Key(accessType = 6, type = SettingsDefinitions.StorageLocation.class)
    public static final String CAMERA_STORAGE_LOCATION = "CameraStorageLocation";
    @InternalKey
    @Key(accessType = 4, includedAbstractions = {DJICameraWM160Abstraction.class}, type = DataCameraGetPushStateInfo.CameraTemperatureState.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String CAMERA_TEMPERATURE_STATE = "camera_temperature_state";
    @Key(accessType = 4, type = SettingsDefinitions.CameraType.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String CAMERA_TYPE = "CameraType";
    @Key(accessType = 3, includedAbstractions = {DJICameraInspire2Abstraction.class}, type = ColorWaveformSettings.ColorWaveformDisplayMode.class)
    public static final String COLOR_WAVEFORM_DISPLAY_MODE = "ColorWaveformDisplayMode";
    @Key(accessType = 6, includedAbstractions = {DJICameraInspire2Abstraction.class}, type = ColorWaveformSettings.ColorWaveformDisplayState.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String COLOR_WAVEFORM_DISPLAY_STATE = "ColorWaveformDisplayState";
    @Key(accessType = 6, includedAbstractions = {DJICameraInspire2Abstraction.class}, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String COLOR_WAVEFORM_ENABLED = "ColorWaveformEnabled";
    public static final String COMPONENT_KEY = "Camera";
    @Key(accessType = 6, excludedAbstractions = {DJICameraTau336Abstraction.class, DJICameraTauXT2Abstraction.class, DJICameraTau640Abstraction.class, DJICameraSparkAbstraction.class}, type = Integer.class)
    public static final String CONTRAST = "Contrast";
    @Key(accessType = 4, type = Integer.class)
    public static final String CURRENT_VIDEO_RECORDING_TIME_IN_SECONDS = "CurrentVideoRecordingTimeInSeconds";
    @Key(accessType = 3, excludedAbstractions = {DJICameraPhantom3AAbstraction.class, DJICameraPhantom3PAbstraction.class, DJICameraPhantom3SAbstraction.class, DJICameraPhantom3WAbstraction.class, DJICameraPhantom4Abstraction.class, DJICameraWM240HasselbladAbstraction.class}, type = String.class)
    public static final String CUSTOM_INFORMATION = "CUSTOM_INFORMATION";
    @Key(accessType = 3, excludedAbstractions = {DJICameraXT2Abstraction.class, DJICameraWM160Abstraction.class}, includedAbstractions = {DJICameraGD600Abstraction.class}, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String DEFOG_ENABLED = "DefogEnabled";
    @Key(accessType = 4, includedAbstractions = {DJICameraWM240HasselbladAbstraction.class, DJICameraInspire2Abstraction.class}, type = Integer.class)
    public static final String DEMARCATE_VALUE = "DemarcateValue";
    @Key(accessType = 3, includedAbstractions = {DJICameraInspire2Abstraction.class, DJICameraPhantom4RTKAbstraction.class}, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String DEWARPING_ENABLED = "DewarpingEnabled";
    @Key(accessType = 6, excludedAbstractions = {DJICameraTau336Abstraction.class, DJICameraTau640Abstraction.class, DJICameraTauXT2Abstraction.class, DJICameraInspire2Abstraction.class, DJICameraSparkAbstraction.class, DJICameraWM240HasselbladAbstraction.class, DJICameraWM240ZoomAbstraction.class}, type = Float.class)
    public static final String DIGITAL_ZOOM_FACTOR = "DigitalZoomFactor";
    @Key(accessType = 4, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String DISKCONNECTED = "DISKConnected";
    @Key(accessType = 6, includedAbstractions = {DJICameraTauXT2Abstraction.class}, type = SettingsDefinitions.DisplayMode.class)
    public static final String DISPLAY_MODE = "DisplayModeForXT2";
    @Key(accessType = 4, includedAbstractions = {DJICameraTauXT2Abstraction.class}, type = SettingsDefinitions.DisplayMode[].class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String DISPLAY_MODE_RANGE = "DisplayModeRange";
    @Key(accessType = 4, type = String.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String DISPLAY_NAME = "DisplayName";
    @Key(accessType = 6, includedAbstractions = {DJICameraTauXT2Abstraction.class}, type = Integer.class)
    public static final String DUAL_FEED_HORIZONTAL_ALIGNMENT_OFFSET = "DualFeedHorizontalAlignmentOffset";
    @Key(accessType = 4, includedAbstractions = {DJICameraTauXT2Abstraction.class}, type = DJIParamMinMaxCapability.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String DUAL_FEED_HORIZONTAL_ALIGNMENT_OFFSET_RANGE = "DualFeedHorizontalAlignmentOffsetRange";
    @Key(accessType = 6, includedAbstractions = {DJICameraTauXT2Abstraction.class}, type = Integer.class)
    public static final String DUAL_FEED_VERTICAL_ALIGNMENT_OFFSET = "DualFeedVerticalAlignmentOffset";
    @Key(accessType = 4, includedAbstractions = {DJICameraTauXT2Abstraction.class}, type = DJIParamMinMaxCapability.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String DUAL_FEED_VERTICAL_ALIGNMENT_OFFSET_RANGE = "DualFeedVerticalAlignmentOffsetRange";
    @Key(accessType = 6, includedAbstractions = {DJICameraInspire2Abstraction.class}, type = SettingsDefinitions.EIColor.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String EI_COLOR = "EIColor";
    @Key(accessType = 6, includedAbstractions = {DJICameraInspire2Abstraction.class}, type = Integer.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String EI_VALUE = "EIValue";
    @Key(accessType = 1, includedAbstractions = {DJICameraInspire2Abstraction.class}, type = int[].class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String EI_VALUE_RANGE = "EIRange";
    @Key(accessType = 8, includedAbstractions = {DJICameraX7Abstraction.class}, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String EXIT_SENSOR_CLEANING_MODE = "exitSensorDedustingMode";
    @Key(accessType = 6, excludedAbstractions = {DJICameraTau336Abstraction.class, DJICameraTauXT2Abstraction.class, DJICameraTau640Abstraction.class}, type = SettingsDefinitions.ExposureCompensation.class)
    public static final String EXPOSURE_COMPENSATION = "ExposureCompensation";
    @InternalKey
    @Key(accessType = 4, excludedAbstractions = {DJICameraTau336Abstraction.class, DJICameraTau640Abstraction.class}, type = SettingsDefinitions.ExposureCompensation[].class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String EXPOSURE_COMPENSATION_RANGE = "ExposureCompensationRange";
    @Key(accessType = 6, excludedAbstractions = {DJICameraTau336Abstraction.class, DJICameraTau640Abstraction.class, DJICameraTauXT2Abstraction.class}, type = SettingsDefinitions.ExposureMode.class)
    public static final String EXPOSURE_MODE = "ExposureMode";
    @InternalKey
    @Key(accessType = 4, excludedAbstractions = {DJICameraTau336Abstraction.class, DJICameraTau640Abstraction.class}, type = SettingsDefinitions.ExposureMode[].class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String EXPOSURE_MODE_RANGE = "ExposureModeRange";
    @Key(accessType = 6, includedAbstractions = {DJICameraInspire2Abstraction.class}, type = SettingsDefinitions.ExposureSensitivityMode.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String EXPOSURE_SENSITIVITY_MODE = "ExposureSensitivityMode";
    @Key(accessType = 4, type = ExposureSettings.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String EXPOSURE_SETTINGS = "ExposureSettings";
    @Key(accessType = 4, type = Integer.class)
    public static final String EXPOSURE_STATE = "ExposureState";
    @InternalKey
    @Key(accessType = 4, type = QuickPreviewSettings.class)
    public static final String FAST_PLAYBACK_SETTINGS = "FastPlayBackSettings";
    @Key(accessType = 7, type = SettingsDefinitions.FileIndexMode.class)
    public static final String FILE_INDEX_MODE = "FileIndexMode";
    @InternalKey
    @Key(accessType = 4, excludedAbstractions = {DJICameraTau336Abstraction.class, DJICameraTau640Abstraction.class}, type = Integer.class)
    public static final String FILTER = "Filter";
    @Key(accessType = 6, includedAbstractions = {DJICameraX5Abstraction.class, DJICameraX5HandheldAbstraction.class, DJICameraX5RAbstraction.class, DJICameraX5RHandheldAbstraction.class, DJICameraZ3Abstraction.class, DJICameraZ3HandheldAbstraction.class, DJICameraPhantom4PAbstraction.class, DJICameraInspire2Abstraction.class, DJICameraWM160Abstraction.class}, type = FocusAssistantSettings.class)
    public static final String FOCUS_ASSISTANT_SETTINGS = "FocusAssistantSettings";
    @Key(accessType = 6, includedAbstractions = {DJICameraX5Abstraction.class, DJICameraX5HandheldAbstraction.class, DJICameraX5RAbstraction.class, DJICameraX5RHandheldAbstraction.class, DJICameraZ3Abstraction.class, DJICameraZ3HandheldAbstraction.class, DJICameraInspire2Abstraction.class, DJICameraPhantom4PAbstraction.class, DJICameraFoldingDroneXAbstraction.class, MockCameraAbstraction.class}, type = SettingsDefinitions.FocusMode.class)
    public static final String FOCUS_MODE = "FocusMode";
    @Key(accessType = 6, includedAbstractions = {DJICameraZ3Abstraction.class, DJICameraZ3HandheldAbstraction.class, DJICameraX5Abstraction.class, DJICameraX5HandheldAbstraction.class, DJICameraX5RAbstraction.class, DJICameraX5RHandheldAbstraction.class, DJICameraInspire2Abstraction.class, DJICameraPhantom4PAbstraction.class, DJICameraFoldingDroneXAbstraction.class}, type = Integer.class)
    public static final String FOCUS_RING_VALUE = "FocusRingValue";
    @Key(accessType = 1, includedAbstractions = {DJICameraZ3Abstraction.class, DJICameraZ3HandheldAbstraction.class, DJICameraX5Abstraction.class, DJICameraX5HandheldAbstraction.class, DJICameraX5RAbstraction.class, DJICameraX5RHandheldAbstraction.class, DJICameraInspire2Abstraction.class, DJICameraPhantom4PAbstraction.class, DJICameraFoldingDroneXAbstraction.class}, type = Integer.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String FOCUS_RING_VALUE_UPPER_BOUND = "FocusRingValueUpperBound";
    @Key(accessType = 4, includedAbstractions = {DJICameraX5Abstraction.class, DJICameraX5HandheldAbstraction.class, DJICameraX5RAbstraction.class, DJICameraX5RHandheldAbstraction.class, DJICameraZ3Abstraction.class, DJICameraZ3HandheldAbstraction.class, DJICameraGD600Abstraction.class, DJICameraInspire2Abstraction.class, DJICameraWM230Abstraction.class}, type = SettingsDefinitions.FocusStatus.class)
    public static final String FOCUS_STATUS = "FocusStatus";
    @Key(accessType = 6, includedAbstractions = {DJICameraX5Abstraction.class, DJICameraX5HandheldAbstraction.class, DJICameraX5RAbstraction.class, DJICameraX5RHandheldAbstraction.class, DJICameraZ3Abstraction.class, DJICameraZ3HandheldAbstraction.class, DJICameraFoldingDroneSAbstraction.class, DJICameraFoldingDroneXAbstraction.class, DJICameraPhantom4PAbstraction.class, DJICameraInspire2Abstraction.class, DJICameraWM240HasselbladAbstraction.class}, type = PointF.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String FOCUS_TARGET = "FocusTarget";
    @Key(accessType = 8, excludedAbstractions = {DJICameraWM160Abstraction.class}, includedAbstractions = {DJICameraWM230Abstraction.class, DJICameraWM245DualLightVLAbstraction.class, DJICameraWM245DualLightIRAbstraction.class}, types = {SettingsDefinitions.StorageLocation.class}, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String FORMAT_INTERNAL_STORAGE = "FormatInternalStorage";
    @Key(accessType = 8)
    public static final String FORMAT_SD_CARD = "FormatSDCard";
    @Key(accessType = 8, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String FORMAT_SSD = "FormatSSD";
    @Key(accessType = 8, type = SSDFileSystem.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String FORMAT_SSD_WITH_TYPE = "FormatSSDWithType";
    @InternalKey
    @Key(accessType = 1, type = String.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String FULL_SERIAL_NUMBER_HASH = "FullSerialNumber";
    @Key(accessType = 4, type = Integer.class)
    public static final String GAMMA = "gamma";
    @Key(accessType = 1, includedAbstractions = {DJICameraXTBaseAbstraction.class}, type = String.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String HARDWARE_VERSION = "HardwareVersion";
    @Key(accessType = 4, type = Boolean.class)
    public static final String HAS_ERROR = "HasError";
    @Key(accessType = 3, includedAbstractions = {DJICameraInspire2Abstraction.class, DJICameraFoldingDroneXAbstraction.class, DJICameraPhantom4PAbstraction.class}, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String HD_LIVE_VIEW_ENABLED = "HDLiveViewEnabled";
    @Key(accessType = 3, includedAbstractions = {DJICameraPhantom4RTKAbstraction.class}, type = Boolean.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String HIGH_QUALITY_PREVIEW_ENABLED = "HighQualityPreviewEnabled";
    @ComplexKey({@Key(accessType = 6, includedAbstractions = {DJICameraXTBaseAbstraction.class, DJICameraGD600Abstraction.class, DJICameraWM160Abstraction.class}, type = Boolean.class), @Key(accessType = 3, type = Boolean.class)})
    public static final String HISTOGRAM_ENABLED = "HistogramEnabled";
    @Key(accessType = 4, type = Short[].class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String HISTOGRAM_LIGHT_VALUES = "HistogramLightValues";
    @Key(accessType = 3, excludedAbstractions = {DJICameraTau336Abstraction.class, DJICameraTauXT2Abstraction.class, DJICameraPayloadAbstraction.class, DJICameraGD600Abstraction.class, DJICameraTau640Abstraction.class}, type = Integer.class)
    public static final String HUE = "Hue";
    @InternalKey
    @Key(accessType = 4, type = SettingsDefinitions.PhotoFileFormat[].class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String HYPERLAPSE_ORIGINAL_IMAGES_FORMAT_RANGE = "HyperlapseOriginalImagesFormatRange";
    @InternalKey
    @Key(accessType = 3, excludedAbstractions = {DJICameraWM160Abstraction.class}, includedAbstractions = {DJICameraWM240HasselbladAbstraction.class}, type = OriginalPhotoSettings.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String HYPERLAPSE_ORIGINAL_PHOTO_SETTINGS = "HyperlapseOriginalPhotoSettings";
    @Key(accessType = 4, type = Float.class)
    public static final String HYPER_LAPSE_FRAME_X = "HyperLapseFrameX";
    @Key(accessType = 4, type = Float.class)
    public static final String HYPER_LAPSE_FRAME_Y = "HyperLapseFrameY";
    @Key(accessType = 8, includedAbstractions = {DJICameraX7Abstraction.class}, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String INIT_SENSOR_CLEANING_MODE = "enterSensorDedustingMode";
    @Key(accessType = 4, includedAbstractions = {DJICameraMultiStorageAbstraction.class, DJICameraWM245DualLightIRAbstraction.class, DJICameraWM245DualLightVLAbstraction.class}, type = Long.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String INNERSTORAGE_AVAILABLE_CAPTURE_COUNT = "InnerStorageAvailableCaptureCount";
    @Key(accessType = 4, includedAbstractions = {DJICameraMultiStorageAbstraction.class, DJICameraWM245DualLightIRAbstraction.class, DJICameraWM245DualLightVLAbstraction.class}, type = Integer.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String INNERSTORAGE_AVAILABLE_RECORDING_TIME_IN_SECONDS = "InnerStorageAvailableRecordingTimeInSeconds";
    @Key(accessType = 4, includedAbstractions = {DJICameraMultiStorageAbstraction.class, DJICameraWM245DualLightIRAbstraction.class, DJICameraWM245DualLightVLAbstraction.class}, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String INNERSTORAGE_HAS_ERROR = "InnerStorageHasError";
    @Key(accessType = 4, includedAbstractions = {DJICameraMultiStorageAbstraction.class, DJICameraWM245DualLightIRAbstraction.class, DJICameraWM245DualLightVLAbstraction.class}, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String INNERSTORAGE_IS_BUSY = "InnerStorageIsBusy";
    @Key(accessType = 4, includedAbstractions = {DJICameraMultiStorageAbstraction.class, DJICameraWM245DualLightIRAbstraction.class, DJICameraWM245DualLightVLAbstraction.class}, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String INNERSTORAGE_IS_FORMATTED = "InnerStorageIsFormatted";
    @Key(accessType = 4, includedAbstractions = {DJICameraMultiStorageAbstraction.class, DJICameraWM245DualLightIRAbstraction.class, DJICameraWM245DualLightVLAbstraction.class}, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String INNERSTORAGE_IS_FORMATTING = "InnerStorageIsFormatting";
    @Key(accessType = 4, includedAbstractions = {DJICameraMultiStorageAbstraction.class, DJICameraWM245DualLightIRAbstraction.class, DJICameraWM245DualLightVLAbstraction.class}, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String INNERSTORAGE_IS_FULL = "InnerStorageIsFull";
    @Key(accessType = 4, includedAbstractions = {DJICameraMultiStorageAbstraction.class, DJICameraWM245DualLightIRAbstraction.class, DJICameraWM245DualLightVLAbstraction.class}, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String INNERSTORAGE_IS_INITIALIZING = "InnerStorageIsInitializing";
    @Key(accessType = 4, includedAbstractions = {DJICameraMultiStorageAbstraction.class, DJICameraWM245DualLightIRAbstraction.class, DJICameraWM245DualLightVLAbstraction.class}, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String INNERSTORAGE_IS_INSERTED = "InnerStorageInsert";
    @Key(accessType = 4, includedAbstractions = {DJICameraMultiStorageAbstraction.class, DJICameraWM245DualLightIRAbstraction.class, DJICameraWM245DualLightVLAbstraction.class}, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String INNERSTORAGE_IS_INVALID_FORMAT = "InnerStorageIsInvalidFormat";
    @Key(accessType = 4, includedAbstractions = {DJICameraMultiStorageAbstraction.class, DJICameraWM245DualLightIRAbstraction.class, DJICameraWM245DualLightVLAbstraction.class}, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String INNERSTORAGE_IS_READ_ONLY = "InnerStorageIsReadOnly";
    @Key(accessType = 4, includedAbstractions = {DJICameraMultiStorageAbstraction.class, DJICameraWM245DualLightIRAbstraction.class, DJICameraWM245DualLightVLAbstraction.class}, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String INNERSTORAGE_IS_SLOW = "InnerStorageIsSlow";
    @Key(accessType = 4, includedAbstractions = {DJICameraMultiStorageAbstraction.class, DJICameraWM245DualLightIRAbstraction.class, DJICameraWM245DualLightVLAbstraction.class}, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String INNERSTORAGE_IS_VERIFIED = "InnerStorageIsVerified";
    @Key(accessType = 4, includedAbstractions = {DJICameraMultiStorageAbstraction.class, DJICameraWM245DualLightIRAbstraction.class, DJICameraWM245DualLightVLAbstraction.class}, type = Integer.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String INNERSTORAGE_REMAINING_SPACE_IN_MB = "InnerStorageRemainingSpaceInMB";
    @InternalKey
    @Key(accessType = 4, includedAbstractions = {DJICameraMultiStorageAbstraction.class, DJICameraWM245DualLightIRAbstraction.class, DJICameraWM245DualLightVLAbstraction.class}, type = SettingsDefinitions.SDCardStateOperationState.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String INNERSTORAGE_STATE = "InnerStorageState";
    @Key(accessType = 4, includedAbstractions = {DJICameraMultiStorageAbstraction.class, DJICameraWM245DualLightVLAbstraction.class, DJICameraWM245DualLightIRAbstraction.class}, type = Integer.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String INNERSTORAGE_TOTAL_SPACE_IN_MB = "InnerStorageTotalSpaceInMB";
    @InternalKey
    @Key(accessType = 4, excludedAbstractions = {DJICameraTau336Abstraction.class, DJICameraTauXT2Abstraction.class, DJICameraTau640Abstraction.class}, type = Integer.class)
    public static final String INTERVAL_SHOOT_COUNTDOWN = "IntervalShootCountdown";
    @Key(accessType = 3, includedAbstractions = {DJICameraGD600Abstraction.class}, type = Boolean.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String IRC_ENABLE = "IRCEnable";
    @Key(accessType = 6, excludedAbstractions = {DJICameraTau336Abstraction.class, DJICameraTauXT2Abstraction.class, DJICameraXT2Abstraction.class, DJICameraTau640Abstraction.class}, type = SettingsDefinitions.ISO.class)
    public static final String ISO = "ISO";
    @InternalKey
    @Key(accessType = 4, excludedAbstractions = {DJICameraTau336Abstraction.class, DJICameraTau640Abstraction.class}, type = SettingsDefinitions.ISO[].class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String ISO_RANGE = "ISORange";
    @Key(accessType = 4, type = Boolean.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String IS_ADJUSTABLE_APERTURE_SUPPORTED = "IsAdjustableApertureSupported";
    @Key(accessType = 4, type = Boolean.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String IS_ADJUSTABLE_FOCAL_POINT_SUPPORTED = "IsAdjustableFocalPointSupported";
    @Key(accessType = 4, type = Boolean.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String IS_AFC_SUPPORTED = "isContinuousAutoFocusSupported";
    @Key(accessType = 4, type = Boolean.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String IS_AUDIO_RECORDING_SUPPORTED = "IsAudioRecordingSupported";
    @Key(accessType = 4, type = Boolean.class)
    public static final String IS_CAMERA_IN_DEBUG_MODE = "inDebugMode";
    @Key(accessType = 4, type = Boolean.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String IS_COLOR_WAVEFORM_SUPPORTED = "IsColorWaveformSupported";
    @Key(accessType = 4, type = Boolean.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String IS_DEWARPING_SUPPORTED = "isDewarpingSupported";
    @Key(accessType = 4, type = Boolean.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String IS_DIGITAL_ZOOM_SUPPORTED = "IsDigitalZoomSupported";
    @InternalKey
    @Key(accessType = 4, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String IS_DOWNLOAD_BOKEH = "IsDownloadBokeh";
    @Key(accessType = 4, type = Boolean.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String IS_EI_MODE_SUPPORTED = "isEIModeSupported";
    @Key(accessType = 4, type = Boolean.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String IS_INTERCHANGEABLE_LENS_SUPPORTED = "IsInterchangeableLensSupported";
    @Key(accessType = 4, type = Boolean.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String IS_INTERNAL_STORAGE_SUPPORTED = "isInternalStorageSupported";
    @Key(accessType = 4, type = Boolean.class)
    public static final String IS_LOCK_GIMBAL_WHEN_SHOT = "isLockedGimbalWhenShot";
    @Key(accessType = 4, type = Boolean.class)
    public static final String IS_MCTF_ENABLE = "isMCTFEnable";
    @Key(accessType = 4, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String IS_MECHANICAL_SHUTTER_SUPPORTED = "isMechanicalShutterSupported";
    @Key(accessType = 4, type = Boolean.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String IS_MEDIA_DOWNLOAD_MODE_SUPPORTED = "IsMediaDownloadModeSupported";
    @InternalKey
    @Key(accessType = 4, type = Boolean.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String IS_MEDIA_DOWN_MODE_MAP_VALUE_2 = "isMediaDownModeMapValue2";
    @Key(accessType = 4, type = Boolean.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String IS_METERING_MODE_SUPPORTED = "IsMeteringModeSupported";
    @Key(accessType = 4, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String IS_ND_FILTER_MODE_SUPPORTED = "isNDFilterModeSupported";
    @Key(accessType = 4, type = Boolean.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String IS_OPTICAL_ZOOM_SUPPORTED = "IsOpticalZoomSupported";
    @Key(accessType = 4, type = Boolean.class)
    public static final String IS_OVERHEATING = "isOverheating";
    @Key(accessType = 4, type = Boolean.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String IS_PHOTO_QUICK_VIEW_SUPPORTED = "IsPhotoQuickViewSupported";
    @Key(accessType = 4, type = Boolean.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String IS_PLAYBACK_SUPPORTED = "isPlaybackSupported";
    @InternalKey
    @Key(accessType = 4, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String IS_PSEUDO_CAMERA_PROCESSING = "IsPseudoCameraProcessing";
    @InternalKey
    @Key(accessType = 4, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String IS_PSEUDO_CAMERA_SHOOTING = "IsPseudoCameraShooting";
    @Key(accessType = 4, type = Boolean.class)
    public static final String IS_RECORDING = "IsRecording";
    @Key(accessType = 4, type = Boolean.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String IS_SENSOR_CLEANING_SUPPORTED = "isSensorCleaningSupported";
    @Key(accessType = 4, type = Boolean.class)
    public static final String IS_SHOOTING_BURST_PHOTO = "IsShootingBurstPhoto";
    @Key(accessType = 4, type = Boolean.class)
    public static final String IS_SHOOTING_INTERVAL_PHOTO = "IsShootingIntervalPhoto";
    @Key(accessType = 4, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String IS_SHOOTING_PANORAMA_PHOTO = "IsShootingPanoramaPhoto";
    @InternalKey
    @Key(accessType = 4, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String IS_SHOOTING_PHOTO = "IsShootingPhoto";
    @Key(accessType = 4, type = Boolean.class)
    public static final String IS_SHOOTING_PHOTO_ENABLED = "IsShootPhotoEnabled";
    @InternalKey
    @Key(accessType = 4, type = Boolean.class)
    public static final String IS_SHOOTING_RAWBURST_PHOTO = "IsShootingRawBurstPhoto";
    @Key(accessType = 4, type = Boolean.class)
    public static final String IS_SHOOTING_RAW_BURST_PHOTO = "IsShootingRawBurstPhoto";
    @Key(accessType = 4, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String IS_SHOOTING_SHALLOW_FOCUS_PHOTO = "IsShootingShallowFocusPhoto";
    @Key(accessType = 4, type = Boolean.class)
    public static final String IS_SHOOTING_SINGLE_PHOTO = "IsShootingSinglePhoto";
    @Key(accessType = 4, type = Boolean.class)
    public static final String IS_SHOOTING_SINGLE_PHOTO_IN_RAW_FORMAT = "IsShootingSinglePhotoInRAWFormat";
    @InternalKey
    @Key(accessType = 4, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String IS_SHOOT_ENABLED = "IsShootEnabled";
    @Key(accessType = 4, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String IS_SSD_INITIALIZING = "isSSDInitializing";
    @Key(accessType = 4, type = Boolean.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String IS_SSD_SUPPORTED = "IsSSDSupported";
    @Key(accessType = 4, type = Boolean.class)
    public static final String IS_STORING_PHOTO = "IsStoringPhoto";
    @Key(accessType = 4, type = Boolean.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String IS_TAP_ZOOM_SUPPORTED = "isTapZoomSupported";
    @Key(accessType = 4, type = Boolean.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String IS_THERMAL_CAMERA = "IsThermalCamera";
    @Key(accessType = 4, type = Boolean.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String IS_TIME_LAPSE_SUPPORTED = "IsTimeLapseSupported";
    @Key(accessType = 4, type = Boolean.class)
    public static final String IS_VIDEO_CAPTION_ENABLE = "isVideoCaptionEnable";
    @Key(accessType = 4, type = Boolean.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String IS_VIDEO_PLAYBACK_SUPPORTED = "isVideoPlaybackSupported";
    @InternalKey
    @Key(accessType = 4, type = Long.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String LAST_FILE_CREATE_TIME = "LastFileCreateTime";
    @InternalKey
    @Key(accessType = 4, type = Integer.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String LAST_FILE_INDEX = "LastFileIndex";
    @InternalKey
    @Key(accessType = 4, type = Integer.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String LAST_FILE_SUBINDEX = "LastFileSubIndex";
    @InternalKey
    @Key(accessType = 4, type = Integer.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String LAST_FILE_TYPE = "LastFileType";
    @Key(accessType = 6, excludedAbstractions = {DJICameraWM160Abstraction.class}, type = LEDsSettings.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String LEDS_AUTO_TURN_OFF_ENABLED_SETTINGS = "LEDsAutoTurnOffEnabledSettings";
    @Key(accessType = 6, excludedAbstractions = {DJICameraWM160Abstraction.class}, includedAbstractions = {DJICameraFoldingDroneSAbstraction.class, DJICameraPhantom4PAbstraction.class, DJICameraInspire2Abstraction.class, DJICameraWM245DualLightVLAbstraction.class, DJICameraWM245DualLightIRAbstraction.class}, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String LED_AUTO_TURN_OFF_ENABLED = "LEDAutoTurnOffEnabled";
    @Key(accessType = 1, includedAbstractions = {DJICameraX5Abstraction.class, DJICameraX5HandheldAbstraction.class, DJICameraX5RAbstraction.class, DJICameraX5RHandheldAbstraction.class, DJICameraGD600Abstraction.class, DJICameraInspire2Abstraction.class}, type = String.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String LENS_INFORMATION = "LensInformation";
    @Key(accessType = 4, includedAbstractions = {DJICameraX5Abstraction.class, DJICameraX5HandheldAbstraction.class, DJICameraX5RAbstraction.class, DJICameraX5RHandheldAbstraction.class}, type = Boolean.class)
    public static final String LENS_IS_AF_SWITCH_ON = "LensIsAFSwitchOn";
    @Key(accessType = 4, includedAbstractions = {DJICameraX5Abstraction.class, DJICameraX5HandheldAbstraction.class, DJICameraX5RAbstraction.class, DJICameraX5RHandheldAbstraction.class, DJICameraZ3Abstraction.class, DJICameraZ3HandheldAbstraction.class, DJICameraInspire2Abstraction.class, DJICameraPhantom4PAbstraction.class, DJICameraWM160Abstraction.class}, type = Boolean.class)
    public static final String LENS_IS_FOCUS_ASSISTANT_WORKING = "LensIsFocusAssistantWorking";
    @Key(accessType = 4, includedAbstractions = {DJICameraX5BaseAbstraction.class}, type = Boolean.class)
    public static final String LENS_IS_INSTALLED = "LensIsInstalled";
    @Key(accessType = 8, types = {SettingsDefinitions.CustomSettingsProfile.class}, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String LOAD_SETTINGS_FROM_PROFILE = "LoadSettingsFromProfile";
    @Key(accessType = 6, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String MECHANICAL_SHUTTER_ENABLED = "MechanicalShutterEnabled";
    @Key(accessType = 6, excludedAbstractions = {DJICameraTau336Abstraction.class, DJICameraTauXT2Abstraction.class, DJICameraTau640Abstraction.class}, type = SettingsDefinitions.MeteringMode.class)
    public static final String METERING_MODE = "MeteringMode";
    @Key(accessType = 6, type = SettingsDefinitions.CameraMode.class)
    public static final String MODE = "Mode";
    @InternalKey
    @Key(accessType = 4, type = SettingsDefinitions.CameraMode[].class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String MODE_RANGE = "CameraModeRange";
    @Key(accessType = 6, excludedAbstractions = {DJICameraWM245DualLightIRAbstraction.class}, includedAbstractions = {DJICameraTauXT2Abstraction.class}, type = Integer.class)
    public static final String MSX_LEVEL = "BlendingLevel";
    @InternalKey
    @Key(accessType = 3, excludedAbstractions = {DJICameraWM160Abstraction.class}, type = DJIMultiLEDControlMode.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String MULTI_LEDS_AUTO_ENABLED = "MultiLEDsAutoEnabled";
    @Key(accessType = 6, type = SettingsDefinitions.NDFilterMode.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String NDFILTER_MODE = "NDFilterMode";
    @InternalKey
    @Key(accessType = 8, includedAbstractions = {DJICameraGD600Abstraction.class}, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String ONE_KEY_ZOOM = "OneKeyZoom";
    @Key(accessType = 6, includedAbstractions = {DJICameraX5Abstraction.class, DJICameraX5HandheldAbstraction.class, DJICameraX5RAbstraction.class, DJICameraX5RHandheldAbstraction.class, DJICameraZ3Abstraction.class, DJICameraZ3HandheldAbstraction.class, DJICameraGD600Abstraction.class, DJICameraInspire2Abstraction.class, DJICameraPhantom4PAbstraction.class, DJICameraWM240ZoomAbstraction.class}, type = Integer.class)
    public static final String OPTICAL_ZOOM_FOCAL_LENGTH = "OpticalZoomFocalLength";
    @Key(accessType = 4, includedAbstractions = {DJICameraGD600Abstraction.class, DJICameraWM240ZoomAbstraction.class}, type = Float.class)
    public static final String OPTICAL_ZOOM_SCALE = "OpticalZoomScale";
    @Key(accessType = 4, includedAbstractions = {DJICameraX5Abstraction.class, DJICameraX5HandheldAbstraction.class, DJICameraX5RAbstraction.class, DJICameraX5RHandheldAbstraction.class, DJICameraZ3Abstraction.class, DJICameraZ3HandheldAbstraction.class, DJICameraGD600Abstraction.class, DJICameraInspire2Abstraction.class, DJICameraPhantom4PAbstraction.class, DJICameraWM240ZoomAbstraction.class}, type = SettingsDefinitions.OpticalZoomSpec.class)
    public static final String OPTICAL_ZOOM_SPEC = "OpticalZoomSpec";
    @InternalKey
    @Key(accessType = 3, excludedAbstractions = {DJICameraWM240ZoomAbstraction.class, DJICameraWM240HasselbladAbstraction.class, DJICameraWM245Abstraction.class}, includedAbstractions = {DJICameraFoldingDroneSAbstraction.class}, type = SettingsDefinitions.Orientation.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String ORIENTATION = "Orientation";
    @InternalKey
    @Key(accessType = 4, type = SettingsDefinitions.Orientation[].class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String ORIENTATION_RANGE = "CameraOrientationRange";
    @Key(accessType = 4, type = SettingsDefinitions.PhotoFileFormat[].class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String PANO_ORIGINAL_IMAGES_FORMAT_RANGE = "PanoOriginalImagesFormatRange";
    @Key(accessType = 3, excludedAbstractions = {DJICameraWM160Abstraction.class}, includedAbstractions = {DJICameraWM230Abstraction.class}, type = OriginalPhotoSettings.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String PANO_ORIGINAL_PHOTO_SETTINGS = "PanoOriginalPhotoSettings";
    @Key(accessType = 6, excludedAbstractions = {DJICameraTau336Abstraction.class, DJICameraTau640Abstraction.class, DJICameraTauXT2Abstraction.class, DJICameraXT2Abstraction.class, DJICameraPayloadAbstraction.class, DJICameraGD600Abstraction.class, DJICameraPhantom4RTKAbstraction.class}, type = SettingsDefinitions.PhotoAEBCount.class)
    public static final String PHOTO_AEB_COUNT = "PhotoAEBCount";
    @Key(accessType = 6, excludedAbstractions = {DJICameraTau336Abstraction.class, DJICameraTau640Abstraction.class, DJICameraTauXT2Abstraction.class}, type = SettingsDefinitions.PhotoAspectRatio.class)
    public static final String PHOTO_ASPECT_RATIO = "PhotoAspectRatio";
    @InternalKey
    @Key(accessType = 4, type = SettingsDefinitions.PhotoAspectRatio[].class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String PHOTO_ASPECT_RATIO_RANGE = "PhotoAspectRatioRange";
    @Key(accessType = 6, excludedAbstractions = {DJICameraTau336Abstraction.class, DJICameraTau640Abstraction.class, DJICameraTauXT2Abstraction.class, DJICameraPhantom4RTKAbstraction.class}, type = SettingsDefinitions.PhotoBurstCount.class)
    public static final String PHOTO_BURST_COUNT = "PhotoBurstCount";
    @Key(accessType = 6, type = SettingsDefinitions.PhotoFileFormat.class)
    public static final String PHOTO_FILE_FORMAT = "PhotoFileFormat";
    @InternalKey
    @Key(accessType = 4, type = SettingsDefinitions.PhotoFileFormat[].class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String PHOTO_FILE_FORMAT_RANGE = "PhotoFileFormatRange";
    @InternalKey
    @Key(accessType = 4, type = Integer.class)
    public static final String PHOTO_FORMAT = "PhotoFormat";
    @Key(accessType = 6, excludedAbstractions = {DJICameraWM160Abstraction.class}, includedAbstractions = {DJICameraSparkAbstraction.class, DJICameraWM230Abstraction.class, DJICameraFoldingDroneXAbstraction.class, DJICameraPhantom4AAbstraction.class, DJICameraPhantom4PAbstraction.class, DJICameraWM240HasselbladAbstraction.class}, type = SettingsDefinitions.PhotoPanoramaMode.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String PHOTO_PANORAMA_MODE = "PhotoPanoramaMode";
    @Key(accessType = 3, excludedAbstractions = {DJICameraWM240HasselbladAbstraction.class, DJICameraWM240ZoomAbstraction.class, DJICameraWM245Abstraction.class, DJICameraWM245DualLightVLAbstraction.class, DJICameraWM160Abstraction.class}, type = Integer.class)
    public static final String PHOTO_QUICK_VIEW_DURATION = "PhotoQuickViewDuration";
    @Key(accessType = 6, includedAbstractions = {DJICameraInspire2Abstraction.class}, type = SettingsDefinitions.PhotoBurstCount.class)
    public static final String PHOTO_RAW_BURST_COUNT = "PhotoRAWBurstCount";
    @Key(accessType = 6, type = SettingsDefinitions.PhotoTimeIntervalSettings.class)
    public static final String PHOTO_TIME_INTERVAL_SETTINGS = "PhotoTimeIntervalSettings";
    @Key(accessType = 3, includedAbstractions = {DJICameraX5RHandheldAbstraction.class, DJICameraX3HandheldAbstraction.class, DJICameraZ3HandheldAbstraction.class, MockCameraAbstraction.class}, type = PhotoTimeLapseSettings.class)
    public static final String PHOTO_TIME_LAPSE_SETTINGS = "PhotoTimeLapseSettings";
    @Key(accessType = 6, type = SettingsDefinitions.PictureStylePreset.class)
    public static final String PICTURE_STYLE_PRESET = "PictureStylePreset";
    @Key(accessType = 6, excludedAbstractions = {DJICameraWM245DualLightIRAbstraction.class}, includedAbstractions = {DJICameraTauXT2Abstraction.class}, type = SettingsDefinitions.PIPPosition.class)
    public static final String PIP_POSITION = "PipModeForXT2";
    @InternalKey
    @Key(accessType = 4, type = Integer.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String PROTOCOL_VERSION = "ProtocolVersion";
    @InternalKey
    @Key(accessType = 4, type = Integer.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String PSEUDO_CAMERA_CAPTURE_PROGRESS = "PseudoCameraCaptureProgress";
    @InternalKey
    @Key(accessType = 4, type = DataEyeSetPseudoCameraControl.PseudoCameraCmdResult.class, updateType = DJISDKCacheUpdateType.EVENT)
    public static final String PSEUDO_CAMERA_CAPTURE_RESULT = "PseudoCameraCaptureResult";
    @InternalKey
    @Key(accessType = 4, type = Integer.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String PSEUDO_CAMERA_CAPTURE_TOTAL = "PseudoCameraCaptureTotal";
    @InternalKey
    @Key(accessType = 4, type = Integer.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String PSEUDO_CAMERA_PROCESS_PROGRESS = "PseudoCameraProcessProgress";
    @InternalKey
    @Key(accessType = 4, type = Integer.class)
    public static final String RAWBURST_SHOOTING_COUNT = "RawBurstShootingCount";
    @InternalKey
    @Key(accessType = 4, type = Integer.class)
    public static final String RAWBURST_SHOOT_NUMBER = "RawBurstShootNumber";
    @Key(accessType = 4, includedAbstractions = {DJICameraX5RAbstraction.class, DJICameraInspire2RawAbstraction.class, DJICameraX5RHandheldAbstraction.class}, type = Integer.class)
    public static final String RAW_PHOTO_BURST_COUNT = "RAWPhotoBurstCount";
    @InternalKey
    @Key(accessType = 6, excludedAbstractions = {DJICameraTau336Abstraction.class, DJICameraTauXT2Abstraction.class, DJICameraTau640Abstraction.class}, type = SettingsDefinitions.ShutterSpeed.class)
    public static final String REAL_SHUTTER_SPEED = "RealShutterSpeed";
    @Key(accessType = 1, includedAbstractions = {DJICameraInspire2Abstraction.class}, type = Integer.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String RECOMMENDED_EI_VALUE = "RecommendedEI";
    @Key(accessType = 4, type = CameraRecordingState.class)
    public static final String RECORDING_STATE = "RecordingState";
    @Key(accessType = 6, excludedAbstractions = {DJICameraPayloadAbstraction.class, DJICameraGD600Abstraction.class}, type = ResolutionAndFrameRate.class)
    public static final String RESOLUTION_FRAME_RATE = "ResolutionFrameRate";
    @Key(accessType = 8, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String RESTORE_FACTORY_SETTINGS = "restoreFactorySettings";
    @Key(accessType = 3, excludedAbstractions = {DJICameraTau336Abstraction.class, DJICameraPayloadAbstraction.class, DJICameraGD600Abstraction.class, DJICameraTauXT2Abstraction.class, DJICameraTau640Abstraction.class, DJICameraSparkAbstraction.class}, type = Integer.class)
    public static final String SATURATION = "Saturation";
    @InternalKey
    @Key(accessType = 3, type = SaveOriginalPhotoState.class)
    public static final String SAVE_ORIGINAL_PHOTO_ENABLED = "SaveOriginalPhotoEnabled";
    @Key(accessType = 8, types = {SettingsDefinitions.CustomSettingsProfile.class}, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String SAVE_SETTINGS_TO_PROFILE = "SaveSettingsToProfile";
    @Key(accessType = 4, type = Long.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String SDCARD_AVAILABLE_CAPTURE_COUNT = "SDCardAvailableCaptureCount";
    @Key(accessType = 4, type = Integer.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String SDCARD_AVAILABLE_RECORDING_TIME_IN_SECONDS = "SDCardAvailableRecordingTimeInSeconds";
    @Key(accessType = 4, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String SDCARD_HAS_ERROR = "SDCardHasError";
    @Key(accessType = 4, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String SDCARD_IS_BUSY = "SDCardIsBusy";
    @InternalKey
    @Key(accessType = 4, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String SDCARD_IS_CONNECTED_TO_PC = "SDCardIsConnectedToPC";
    @Key(accessType = 4, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String SDCARD_IS_FORMATTED = "SDCardIsFormatted";
    @Key(accessType = 4, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String SDCARD_IS_FORMATTING = "SDCardIsFormatting";
    @Key(accessType = 4, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String SDCARD_IS_FULL = "SDCardIsFull";
    @Key(accessType = 4, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String SDCARD_IS_INITIALIZING = "SDCardIsInitializing";
    @Key(accessType = 4, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String SDCARD_IS_INSERTED = "SDCardIsInserted";
    @Key(accessType = 4, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String SDCARD_IS_INVALID_FORMAT = "SDCardIsInvalidFormat";
    @Key(accessType = 4, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String SDCARD_IS_READ_ONLY = "SDCardIsReadOnly";
    @Key(accessType = 4, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String SDCARD_IS_REPAIRING_FILES = "SDCardIsRepairingFiles";
    @Key(accessType = 4, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String SDCARD_IS_SLOW = "SDCardIsSlow";
    @Key(accessType = 4, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String SDCARD_IS_VERIFIED = "SDCardIsVerified";
    @Key(accessType = 4, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String SDCARD_IS_WRITE_SLOW = "SDCardIsWriteSlow";
    @Key(accessType = 4, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String SDCARD_NEEDS_FORMATTING = "SDCardNeedsFormatting";
    @Key(accessType = 4, type = Integer.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String SDCARD_REMAINING_SPACE_IN_MB = "SDCardRemainingSpaceInMB";
    @Key(accessType = 4, type = SettingsDefinitions.SDCardStateOperationState.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String SDCARD_STATE = "SDCardState";
    @Key(accessType = 4, type = Integer.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String SDCARD_TOTAL_SPACE_IN_MB = "SDCardTotalSpaceInMB";
    @Key(accessType = 4, includedAbstractions = {DJICameraX7Abstraction.class}, type = SettingsDefinitions.SensorCleaningState.class)
    public static final String SENSOR_CLEANING_STATE = "SensorCleaningState";
    @Key(accessType = 6, excludedAbstractions = {DJICameraTau336Abstraction.class, DJICameraTauXT2Abstraction.class, DJICameraTau640Abstraction.class, DJICameraSparkAbstraction.class}, type = Integer.class)
    public static final String SHARPNESS = "Sharpness";
    @Key(accessType = 8, types = {SettingsDefinitions.ShootPhotoMode.class, String.class}, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String SHOOT_PHOTO_BY_CACHING = "ShootPhotoByCaching";
    @InternalKey
    @Key(accessType = 6, type = SettingsDefinitions.ShootPhotoMode.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String SHOOT_PHOTO_MODE = "ShootPhotoMode";
    @InternalKey
    @Key(accessType = 4, type = int[][].class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String SHOOT_PHOTO_MODE_CHILD_RANGE = "ShootPhotoChildRange";
    @InternalKey
    @Key(accessType = 4, type = SettingsDefinitions.ShootPhotoMode[].class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String SHOOT_PHOTO_MODE_RANGE = "ShootPhotoModeRange";
    @Key(accessType = 6, excludedAbstractions = {DJICameraTau336Abstraction.class, DJICameraTauXT2Abstraction.class, DJICameraTau640Abstraction.class}, type = SettingsDefinitions.ShutterSpeed.class)
    public static final String SHUTTER_SPEED = "ShutterSpeed";
    @InternalKey
    @Key(accessType = 4, excludedAbstractions = {DJICameraTau336Abstraction.class, DJICameraTau640Abstraction.class}, type = SettingsDefinitions.ShutterSpeed[].class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String SHUTTER_SPEED_RANGE = "ShutterSpeedRange";
    @Key(accessType = 3, excludedAbstractions = {DJICameraTau336Abstraction.class, DJICameraTau640Abstraction.class, DJICameraTauXT2Abstraction.class, DJICameraTau640Abstraction.class}, type = Point.class)
    public static final String SPOT_METERING_TARGET = "SpotMeteringTarget";
    @Key(accessType = 4, type = Integer.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String SSD_ACCUMULATE_DATA = "SSDAccumulateData";
    @Key(accessType = 4, includedAbstractions = {DJICameraX5RAbstraction.class, DJICameraX5RHandheldAbstraction.class, DJICameraInspire2Abstraction.class}, type = Integer.class)
    public static final String SSD_AVAILABLE_RECORDING_TIME_IN_SECONDS = "SSDAvailableRecordingTimeInSeconds";
    @Key(accessType = 6, includedAbstractions = {DJICameraX5BaseAbstraction.class}, type = SettingsDefinitions.SSDClipFileName.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String SSD_CLIP_FILE_NAME = "SSDClipFileName";
    @Key(accessType = 6, type = SettingsDefinitions.SSDColor.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String SSD_COLOR = "SSDColor";
    @Key(accessType = 4, type = SettingsDefinitions.SSDColor[].class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String SSD_COLOR_RANGE = "SSDVideoLookRange";
    @Key(accessType = 4, includedAbstractions = {DJICameraX5RAbstraction.class, DJICameraX5RHandheldAbstraction.class, DJICameraInspire2Abstraction.class}, type = Boolean.class)
    public static final String SSD_IS_CONNECTED = "SSDIsConnected";
    @Key(accessType = 6, includedAbstractions = {DJICameraInspire2Abstraction.class}, type = SettingsDefinitions.SSDLegacyColor.class)
    public static final String SSD_LEGACY_COLOR = "SSDLegacyColor";
    @Key(accessType = 4, includedAbstractions = {DJICameraX5RAbstraction.class, DJICameraX5RHandheldAbstraction.class, DJICameraInspire2Abstraction.class}, type = SSDOperationState.class)
    public static final String SSD_OPERATION_STATE = "SSDOperationState";
    @Key(accessType = 4, includedAbstractions = {DJICameraX5BaseAbstraction.class}, type = SSDRawMode.class)
    public static final String SSD_RAW_MODE = "ssdRawMode";
    @Key(accessType = 4, includedAbstractions = {DJICameraX5RAbstraction.class, DJICameraX5RHandheldAbstraction.class, DJICameraInspire2Abstraction.class}, type = Long.class)
    public static final String SSD_REMAINING_SPACE_IN_MB = "SSDRemainingSpaceInMB";
    @Key(accessType = 4, includedAbstractions = {DJICameraX5RAbstraction.class, DJICameraX5RHandheldAbstraction.class, DJICameraInspire2Abstraction.class}, type = SSDCapacity.class)
    public static final String SSD_TOTAL_SPACE = "SSDTotalSpace";
    @Key(accessType = 4, includedAbstractions = {DJICameraX5BaseAbstraction.class}, type = CameraSSDVideoLicense[].class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String SSD_VIDEO_LICENSES = "SSDVideoLicenses";
    @Key(accessType = 6, includedAbstractions = {DJICameraInspire2RawAbstraction.class}, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String SSD_VIDEO_RECORDING_ENABLED = "SSDVideoRecordingEnabled";
    @Key(accessType = 6, includedAbstractions = {DJICameraX5RAbstraction.class, DJICameraX5RHandheldAbstraction.class, DJICameraInspire2RawAbstraction.class}, type = ResolutionAndFrameRate.class)
    public static final String SSD_VIDEO_RESOLUTION_AND_FRAME_RATE = "SSDVideoResolutionAndFrameRate";
    @Key(accessType = 4, excludedAbstractions = {DJICameraTau336Abstraction.class, DJICameraTau640Abstraction.class}, type = ResolutionAndFrameRate[].class)
    public static final String SSD_VIDEO_RESOLUTION_FRAME_RATE_RANGE = "SSDVideoResolutionFrameRateRange";
    @InternalKey
    @Key(accessType = 4, type = SettingsDefinitions.VideoResolution[].class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String SSD_VIDEO_RESOLUTION_RANGE = "SSDVideoResolutionRange";
    @Key(accessType = 6, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String SSD_VIDEO_SYNC = "SSDVideoSync";
    @Key(accessType = 8, excludedAbstractions = {DJICameraXT2Abstraction.class, DJICameraWM160Abstraction.class}, includedAbstractions = {DJICameraX5Abstraction.class, DJICameraX5HandheldAbstraction.class, DJICameraX5RAbstraction.class, DJICameraX5RHandheldAbstraction.class, DJICameraZ3Abstraction.class, DJICameraZ3HandheldAbstraction.class, DJICameraGD600Abstraction.class, DJICameraInspire2Abstraction.class, DJICameraWM240ZoomAbstraction.class}, types = {SettingsDefinitions.ZoomDirection.class, SettingsDefinitions.ZoomSpeed.class}, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String START_CONTINUOUS_OPTICAL_ZOOM = "StartContinuousOpticalZoom";
    @InternalKey
    @Key(accessType = 8, types = {Boolean.class}, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String START_QUICK_SHOT_RECORD_VIDEO_IN_CACHE = "StartQuickShotRecordVideoInCache";
    @Key(accessType = 8, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String START_RECORD_VIDEO = "StartRecordVideo";
    @InternalKey
    @Key(accessType = 8, types = {Boolean.class}, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String START_RECORD_VIDEO_IN_CACHE = "StartRecordVideoInCache";
    @Key(accessType = 8, includedAbstractions = {DJICameraX7Abstraction.class}, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String START_SENSOR_CLEANING = "startSensorDedusting";
    @Key(accessType = 8, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String START_SHOOT_PHOTO = "StartShootPhoto";
    @Key(accessType = 8, excludedAbstractions = {DJICameraXT2Abstraction.class, DJICameraWM160Abstraction.class}, includedAbstractions = {DJICameraX5Abstraction.class, DJICameraX5HandheldAbstraction.class, DJICameraX5RAbstraction.class, DJICameraX5RHandheldAbstraction.class, DJICameraZ3Abstraction.class, DJICameraZ3HandheldAbstraction.class, DJICameraGD600Abstraction.class, DJICameraInspire2Abstraction.class, DJICameraWM240ZoomAbstraction.class}, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String STOP_CONTINUOUS_OPTICAL_ZOOM = "StopContinuousOpticalZoom";
    @Key(accessType = 8, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String STOP_RECORD_VIDEO = "StopRecordVideo";
    @InternalKey
    @Key(accessType = 8, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String STOP_RECORD_VIDEO_IN_CACHE = "StopRecordVideoInCache";
    @Key(accessType = 8, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String STOP_SHOOT_PHOTO = "StopShootPhoto";
    @InternalKey
    @Key(accessType = 6, type = DataCameraSetStorageInfo.Storage.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String STORAGE_LOCATION = "StorageLocation";
    @Deprecated
    @Key(accessType = 4, excludedAbstractions = {DJICameraWM230Abstraction.class, DJICameraWM240HasselbladAbstraction.class}, type = SettingsDefinitions.SDCardStateOperationState.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String STORAGE_STATE = "StorageState";
    @InternalKey
    @Key(accessType = 3, includedAbstractions = {DJICameraPhantom4PAbstraction.class, DJICameraX4SAbstraction.class, DJICameraFoldingDroneSAbstraction.class, DJICameraFoldingDroneXAbstraction.class, DJICameraInspire2RawAbstraction.class, DJICameraWM240HasselbladAbstraction.class}, type = DataCameraSetVOutParams.LCDFormat.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String STREAM_QUALITY = "StreamQuality";
    @Key(accessType = 4, type = Boolean.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String SUPPORT_SPOT_THERMOMETRIC = "supportSpotThermometric";
    @Key(accessType = 2, excludedAbstractions = {DJICameraXT2Abstraction.class, DJICameraPayloadAbstraction.class}, includedAbstractions = {DJICameraGD600Abstraction.class}, type = PointF.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String TAP_ZOOM_AT_TARGET = "TapZoomAtTarget";
    @Key(accessType = 3, excludedAbstractions = {DJICameraXT2Abstraction.class}, includedAbstractions = {DJICameraGD600Abstraction.class}, type = Boolean.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String TAP_ZOOM_ENABLED = "TapZoomEnabled";
    @Key(accessType = 6, excludedAbstractions = {DJICameraXT2Abstraction.class}, includedAbstractions = {DJICameraGD600Abstraction.class}, type = Integer.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String TAP_ZOOM_MULTIPLIER = "TapZoomMultiplier";
    @Key(accessType = 4, excludedAbstractions = {DJICameraWM245Abstraction.class, DJICameraPayloadAbstraction.class}, includedAbstractions = {DJICameraGD600Abstraction.class}, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String TAP_ZOOM_STATE = "TapZoomState";
    @InternalKey
    @Key(accessType = 4, includedAbstractions = {DJICameraGD600Abstraction.class}, type = Integer.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String TAP_ZOOM_WORKING = "TapZoomWorking";
    @Key(accessType = 6, includedAbstractions = {DJICameraXTBaseAbstraction.class}, type = SettingsDefinitions.TemperatureUnit.class)
    public static final String TEMPERATURE_UNIT = "temperatureUnitInSpot";
    @Key(accessType = 3, includedAbstractions = {DJICameraXTBaseAbstraction.class}, type = Integer.class)
    public static final String THERMAL_ACE = "ThermalACE";
    @Key(accessType = 4, type = ThermalAreaTemperatureAggregations.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String THERMAL_AREA_TEMPERATURE_AGGREGATIONS = "ThermalAreaTemperatureAggregations";
    @Key(accessType = 6, type = Float.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String THERMAL_ATMOSPHERIC_TEMPERATURE = "ThermalAtmosphericTemperature";
    @Key(accessType = 6, type = Float.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String THERMAL_ATMOSPHERIC_TRANSMISSION_COEFFICIENT = "ThermalAtmosphericTransmissionCoefficient";
    @Key(accessType = 6, type = Float.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String THERMAL_BACKGROUND_TEMPERATURE = "ThermalBackgroundTemperature";
    @Key(accessType = 3, includedAbstractions = {DJICameraXTBaseAbstraction.class}, type = Integer.class)
    public static final String THERMAL_BRIGHTNESS = "ThermalBrightness";
    @Key(accessType = 3, includedAbstractions = {DJICameraXTBaseAbstraction.class}, type = Integer.class)
    public static final String THERMAL_CONTRAST = "ThermalContrast";
    @Key(accessType = 6, type = SettingsDefinitions.ThermalCustomExternalSceneSettingsProfile.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String THERMAL_CUSTOM_EXTERNAL_SCENE_SETTINGS_PROFILE = "ThermalCustomExternalSceneSettingsProfile";
    @Key(accessType = 3, includedAbstractions = {DJICameraXTBaseAbstraction.class}, type = Integer.class)
    public static final String THERMAL_DDE = "ThermalDDE";
    @Key(accessType = 6, includedAbstractions = {DJICameraXTBaseAbstraction.class, DJICameraXT2Abstraction.class}, type = SettingsDefinitions.ThermalDigitalZoomFactor.class)
    public static final String THERMAL_DIGITAL_ZOOM_FACTOR = "ThermalDigitalZoomFactor";
    @Key(accessType = 6, type = SettingsDefinitions.ThermalFFCMode.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String THERMAL_FFC_MODE = "ThermalFFCMode";
    @Key(accessType = 6, includedAbstractions = {DJICameraXTBaseAbstraction.class}, type = SettingsDefinitions.ThermalGainMode.class)
    public static final String THERMAL_GAIN_MODE = "ThermalGainMode";
    @Key(accessType = 6, includedAbstractions = {DJICameraXTBaseAbstraction.class}, type = Boolean.class)
    public static final String THERMAL_ISOTHERM_ENABLED = "ThermalIsothermEnabled";
    @Key(accessType = 6, includedAbstractions = {DJICameraXTBaseAbstraction.class}, type = Integer.class)
    public static final String THERMAL_ISOTHERM_LOWER_VALUE = "ThermalIsothermLowerValue";
    @Key(accessType = 6, excludedAbstractions = {DJICameraWM245DualLightIRAbstraction.class}, includedAbstractions = {DJICameraXTBaseAbstraction.class}, type = Integer.class)
    public static final String THERMAL_ISOTHERM_MIDDLE_VALUE = "ThermalIsothermMiddleValue";
    @Key(accessType = 6, includedAbstractions = {DJICameraXTBaseAbstraction.class}, type = SettingsDefinitions.ThermalIsothermUnit.class)
    public static final String THERMAL_ISOTHERM_UNIT = "ThermalIsothermUnit";
    @Key(accessType = 6, includedAbstractions = {DJICameraXTBaseAbstraction.class}, type = Integer.class)
    public static final String THERMAL_ISOTHERM_UPPER_VALUE = "ThermalIsothermUpperValue";
    @InternalKey
    @Key(accessType = 4, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String THERMAL_IS_FFC_MODE_SUPPORTED = "ThermalIsFFCModeSupported";
    @InternalKey
    @Key(accessType = 4, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String THERMAL_IS_OVERALL_TEMPERATURE_METER_SUPPORTED = "ThermalIsOverallTemperatureMeterSupported";
    @Key(accessType = 6, type = ThermalMeasurementMode.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String THERMAL_MEASUREMENT_MODE = "ThermalMeasurementMode";
    @Key(accessType = 6, type = RectF.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String THERMAL_METERING_AREA = "ThermalMeteringArea";
    @Key(accessType = 6, includedAbstractions = {DJICameraXTBaseAbstraction.class}, type = SettingsDefinitions.ThermalPalette.class)
    public static final String THERMAL_PALETTE = "ThermalPalette";
    @Key(accessType = 4, includedAbstractions = {DJICameraXTBaseAbstraction.class}, type = SettingsDefinitions.ThermalPalette[].class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String THERMAL_PALETTE_RANGE = "ThermalPaletteRange";
    @Key(accessType = 4, includedAbstractions = {DJICameraXTBaseAbstraction.class}, type = SettingsDefinitions.ThermalProfile.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String THERMAL_PROFILE = "ThermalProfile";
    @Key(accessType = 6, includedAbstractions = {DJICameraXTBaseAbstraction.class}, type = SettingsDefinitions.ThermalROI.class)
    public static final String THERMAL_ROI = "ThermalROI";
    @Key(accessType = 6, includedAbstractions = {DJICameraXTBaseAbstraction.class}, type = SettingsDefinitions.ThermalScene.class)
    public static final String THERMAL_SCENE = "ThermalScene";
    @Key(accessType = 6, type = Float.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String THERMAL_SCENE_EMISSIVITY = "ThermalSceneEmissivity";
    @Key(accessType = 6, type = PointF.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String THERMAL_SPOT_METERING_TARGET_POINT = "ThermalSpotMeteringTargetPoint";
    @Key(accessType = 3, includedAbstractions = {DJICameraXTBaseAbstraction.class}, type = Integer.class)
    public static final String THERMAL_SSO = "ThermalSSO";
    @Key(accessType = 5, includedAbstractions = {DJICameraXTBaseAbstraction.class}, type = Float.class)
    public static final String THERMAL_TEMPERATURE_DATA = "ThermalTemperatureData";
    @InternalKey
    @Key(accessType = 4, includedAbstractions = {DJICameraXTBaseAbstraction.class}, type = TauVideoFormat.class)
    public static final String THERMAL_VIDEO_FORMAT = "ThermalVideoFormat";
    @Key(accessType = 6, type = Float.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String THERMAL_WINDOW_REFLECTED_TEMPERATURE = "ThermalWindowReflectedTemperature";
    @Key(accessType = 6, type = Float.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String THERMAL_WINDOW_REFLECTION = "ThermalWindowReflection";
    @Key(accessType = 6, type = Float.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String THERMAL_WINDOW_TEMPERATURE = "ThermalWindowTemperature";
    @Key(accessType = 6, type = Float.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String THERMAL_WINDOW_TRANSMISSION_COEFFICIENT = "ThermalWindowTransmissionCoefficient";
    @Key(accessType = 4, type = Boolean.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String TRACKING_MODE = "CameraTrackingMode";
    @Key(accessType = 8, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String TRIGGER_THERMAL_FFC = "TriggerThermalFFC";
    @Key(accessType = 3, includedAbstractions = {DJICameraX3HandheldAbstraction.class, DJICameraZ3HandheldAbstraction.class}, type = Boolean.class)
    public static final String TURN_OFF_FAN_WHEN_POSSIBLE = "TurnOffFanWhenPossible";
    @Key(accessType = 3, type = Boolean.class)
    public static final String VIDEO_CAPTION_ENABLED = "VideoCaptionEnabled";
    @InternalKey
    @Key(accessType = 4, type = SettingsDefinitions.VideoFileCompressionStandard[].class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String VIDEO_COMPRESSION_STANDARD_RANGE = "VideoCompressionStandardRange";
    @Key(accessType = 6, includedAbstractions = {DJICameraPhantom4PAbstraction.class, DJICameraWM240HasselbladAbstraction.class, DJICameraInspire2Abstraction.class}, type = SettingsDefinitions.VideoFileCompressionStandard.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String VIDEO_FILE_COMPRESSION_STANDARD = "VideoFileCompressionStandard";
    @Key(accessType = 6, type = SettingsDefinitions.VideoFileFormat.class)
    public static final String VIDEO_FILE_FORMAT = "VideoFileFormat";
    @InternalKey
    @Key(accessType = 4, type = SettingsDefinitions.VideoFileFormat[].class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String VIDEO_FILE_FORMAT_RANGE = "VideoFileFormatRange";
    @InternalKey
    @Key(accessType = 3, type = SettingsDefinitions.VideoFov.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String VIDEO_FOV_TYPE = "VideoFovType";
    @InternalKey
    @Key(accessType = 4, type = ResolutionAndFrameRate[].class)
    public static final String VIDEO_RESOLUTION_FRAME_RATE_RANGE = "VideoResolutionFrameRateRange";
    @Key(accessType = 6, type = SettingsDefinitions.VideoStandard.class)
    public static final String VIDEO_STANDARD = "VideoStandard";
    @InternalKey
    @Key(accessType = 4, type = SettingsDefinitions.VideoStandard[].class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String VIDEO_STANDARD_RANGE = "VideoStandardRange";
    @Key(accessType = 6, includedAbstractions = {DJICameraGD600Abstraction.class}, type = Boolean.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String VISION_STABILIZATION_ENABLED = "stabilization";
    @Key(accessType = 4, includedAbstractions = {DJICameraGD600Abstraction.class}, type = StabilizationState.class)
    public static final String VISION_STABILIZATION_STATE = "StabilizationState";
    @Key(accessType = 3, includedAbstractions = {DJICameraGD600Abstraction.class}, type = WatermarkDisplayContentSettings.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String WATERMARK_DISPLAY_CONTENT_SETTINGS = "WatermarkDisplayContentSettings";
    @Key(accessType = 3, includedAbstractions = {DJICameraWM245Abstraction.class, DJICameraGD600Abstraction.class}, type = WatermarkSettings.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String WATERMARK_SETTINGS = "WatermarkSettings";
    @Key(accessType = 3, includedAbstractions = {DJICameraGD600Abstraction.class}, type = String.class, updateType = DJISDKCacheUpdateType.DYNAMIC)
    public static final String WATERMARK_USER_CUSTOM_INFO = "WatermarkUserCustomInfo";
    @Key(accessType = 6, excludedAbstractions = {DJICameraTau336Abstraction.class, DJICameraTauXT2Abstraction.class, DJICameraTau640Abstraction.class}, type = WhiteBalance.class)
    public static final String WHITE_BALANCE = "WhiteBalance";
    @InternalKey
    @Key(accessType = 4, type = int[].class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String WHITE_BALANCE_CUSTOM_COLOR_TEMPERATURE_RANGE = "WhiteBalanceCustomColorTemperatureRange";
    @InternalKey
    @Key(accessType = 4, type = SettingsDefinitions.WhiteBalancePreset[].class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String WHITE_BALANCE_PRESENT_RANGE = "WhiteBalancePresentRange";

    private static class DSLRGroup implements IAbstractionGroup {
        private DSLRGroup() {
        }

        public Class[] getAbstractions() {
            return new Class[]{DJICameraX5Abstraction.class, DJICameraX5HandheldAbstraction.class, DJICameraX5RAbstraction.class, DJICameraX5RHandheldAbstraction.class};
        }
    }

    public CameraKeys(String name) {
        super(name);
    }

    /* access modifiers changed from: protected */
    public String getDefaultAbstractionName() {
        return COMPONENT_KEY;
    }
}
