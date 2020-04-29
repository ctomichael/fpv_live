package dji.common.camera;

import android.support.annotation.NonNull;
import dji.common.camera.SettingsDefinitions;
import dji.common.flightcontroller.FlightMode;
import dji.fieldAnnotation.EXClassNullAway;
import dji.internal.camera.SSDRawMode;
import dji.internal.logics.CommonUtil;
import dji.log.DJILog;
import dji.logic.utils.DJICameraSupportUtil;
import dji.midware.component.DJIComponentManager;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.model.P3.DataCameraGetPushStateInfo;
import dji.midware.data.model.P3.DataCameraGetPushTauParam;
import dji.midware.data.model.P3.DataOsdGetPushCommon;
import dji.midware.util.DoubleCameraSupportUtil;
import dji.sdksharedlib.DJISDKCache;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.extension.KeyHelper;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.keycatalog.CameraKeys;
import dji.sdksharedlib.keycatalog.DJISDKCacheKey;
import dji.sdksharedlib.keycatalog.DJISDKCacheKeys;
import dji.sdksharedlib.keycatalog.FlightControllerKeys;
import dji.sdksharedlib.listener.DJIParamAccessListener;
import dji.sdksharedlib.store.DJISDKCacheParamValue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@EXClassNullAway
public class CameraParamRangeManager {
    private static final String CAMERA_VER_SUPPORT_24OR48_FPS_WM160 = "02.43.01.30";
    private static final String TAG = "CameraParamRangeManager";
    private SettingsDefinitions.AntiFlickerFrequency[] antiFlickerRange = null;
    private SettingsDefinitions.Aperture[] cameraApertureRange = null;
    private SettingsDefinitions.CameraColor[] cameraFilterRange = null;
    private SettingsDefinitions.ISO[] cameraISORange = null;
    private SettingsDefinitions.CameraMode[] cameraModeRange = null;
    private SettingsDefinitions.SSDColor[] cameraSSDColorRange = null;
    private DJISDKCacheKey defaultKey;
    private SettingsDefinitions.DisplayMode[] displayModeRange = null;
    private SettingsDefinitions.ExposureCompensation[] exposureCompensationRange = null;
    private SettingsDefinitions.ExposureMode[] exposureModeRange = null;
    private SettingsDefinitions.PhotoFileFormat[] hyperlapseOriginalImagesFormatRange = null;
    private ArrayList<DJIParamAccessListener> listeners;
    private DJISDKCacheHWAbstraction.OnValueChangeListener onValueChangeListener;
    private SettingsDefinitions.Orientation[] orientationRange = null;
    private SettingsDefinitions.PhotoFileFormat[] panoOriginalImagesFormatRange = null;
    private SettingsDefinitions.PhotoAspectRatio[] photoAspectRatioRange = null;
    private SettingsDefinitions.PhotoFileFormat[] photoFileFormatRange = null;
    private ResolutionAndFrameRate[] resolutionAndFrameRateRange = null;
    private int[][] shootPhotoModeChildRange = null;
    private SettingsDefinitions.ShootPhotoMode[] shootPhotoModeRange = null;
    private SettingsDefinitions.ShutterSpeed[] shutterSpeedRange = null;
    private ResolutionAndFrameRate[] ssdResolutionAndFrameRateRange = null;
    private SettingsDefinitions.VideoResolution[] ssdVideoResolutionRange = null;
    private SettingsDefinitions.ThermalPalette[] thermalPaletteRange = null;
    private SettingsDefinitions.VideoFileCompressionStandard[] videoFileCompressionStandardsRange = null;
    private SettingsDefinitions.VideoFileFormat[] videoFileFormatRange = null;
    private SettingsDefinitions.VideoStandard[] videoStandardRange = null;
    private int[] whiteBalanceCustomColorTemperatureRange = null;
    private SettingsDefinitions.WhiteBalancePreset[] whiteBalancePresentRange = null;

    public CameraParamRangeManager(DJISDKCacheHWAbstraction.OnValueChangeListener valueChangeListener, DJISDKCacheKey defaultKey2) {
        this.onValueChangeListener = valueChangeListener;
        this.defaultKey = defaultKey2;
        if (this.onValueChangeListener != null) {
            triggerUpdateAll();
            this.listeners = new ArrayList<>();
            addListenersForISORange(defaultKey2.clone(CameraKeys.EXPOSURE_MODE), defaultKey2.clone("Mode"), defaultKey2.clone(CameraKeys.CAMERA_TYPE), defaultKey2.clone(CameraKeys.CAMERA_COLOR));
            addListenersForExposureCompensationRange(defaultKey2.clone(CameraKeys.CAMERA_TYPE), defaultKey2.clone(CameraKeys.EXPOSURE_MODE));
            addListenersForExposureModeRange(defaultKey2.clone(CameraKeys.CAMERA_TYPE), defaultKey2.clone(CameraKeys.EXPOSURE_SENSITIVITY_MODE));
            addListenersForCameraModeRange(defaultKey2.clone(CameraKeys.CAMERA_TYPE));
            addListenersForShootPhotoModeRange(defaultKey2.clone(CameraKeys.CAMERA_TYPE));
            addListenersForShootPhotoModeChildRange(defaultKey2.clone(CameraKeys.CAMERA_TYPE), defaultKey2.clone(CameraKeys.PHOTO_FILE_FORMAT));
            addListenersForPhotoFileFormatRange(defaultKey2.clone(CameraKeys.CAMERA_TYPE), defaultKey2.clone(CameraKeys.TRACKING_MODE));
            addListenersForWhiteBalancePresentRange(defaultKey2.clone(CameraKeys.CAMERA_TYPE));
            addListenersForWhiteBalanceCustomColorTemperatureRange(defaultKey2.clone(CameraKeys.CAMERA_TYPE));
            addListenersForPhotoAspectRatioRange(defaultKey2.clone(CameraKeys.CAMERA_TYPE), defaultKey2.clone(CameraKeys.TRACKING_MODE));
            addListenersForVideoFileFormatRange(defaultKey2.clone(CameraKeys.CAMERA_TYPE));
            addListenersForAntiFlickerRange(defaultKey2.clone(CameraKeys.CAMERA_TYPE));
            addListenersForVideoStandardRange(defaultKey2.clone(CameraKeys.CAMERA_TYPE));
            addListenersForVideoCompressionStandardRange(defaultKey2.clone(CameraKeys.CAMERA_TYPE));
            addListenersForOrientationRange(defaultKey2.clone(CameraKeys.CAMERA_TYPE), defaultKey2.clone(CameraKeys.TRACKING_MODE));
            addListenersForShutterSpeedRange(defaultKey2.clone(CameraKeys.EXPOSURE_MODE), defaultKey2.clone("Mode"), defaultKey2.clone(CameraKeys.SHOOT_PHOTO_MODE), defaultKey2.clone(CameraKeys.PHOTO_TIME_INTERVAL_SETTINGS), defaultKey2.clone(CameraKeys.RESOLUTION_FRAME_RATE));
            addListenersForApertureRange(defaultKey2.clone(CameraKeys.CAMERA_TYPE), defaultKey2.clone(CameraKeys.EXPOSURE_MODE));
            addListenersForSSDVideoLookRange(defaultKey2.clone(CameraKeys.CAMERA_TYPE), defaultKey2.clone(CameraKeys.EXPOSURE_SENSITIVITY_MODE), defaultKey2.clone(CameraKeys.ACTIVATE_SSD_VIDEO_LICENSE));
            addListenersForCameraFilterRange(defaultKey2.clone(CameraKeys.CAMERA_TYPE), defaultKey2.clone("Mode"), defaultKey2.clone(CameraKeys.SSD_VIDEO_RECORDING_ENABLED), defaultKey2.clone(CameraKeys.EXPOSURE_SENSITIVITY_MODE));
            addListenersForSSDVideoResolutionRange(defaultKey2.clone(CameraKeys.CAMERA_TYPE), defaultKey2.clone(CameraKeys.SSD_RAW_MODE), defaultKey2.clone(CameraKeys.RESOLUTION_FRAME_RATE));
            addListenersForSSDVideoResolutionAndFrameRateRange(defaultKey2.clone(CameraKeys.CAMERA_TYPE), defaultKey2.clone(CameraKeys.SSD_RAW_MODE), defaultKey2.clone("Mode"), defaultKey2.clone(CameraKeys.VIDEO_STANDARD), defaultKey2.clone(CameraKeys.VIDEO_FILE_COMPRESSION_STANDARD), defaultKey2.clone(DJISDKCacheKeys.FIRMWARE_VERSION));
            addListenersForPanoOriginalImagesFormatRange(defaultKey2.clone(CameraKeys.CAMERA_TYPE));
            addListenersForHyperlapseOriginalImagesFormatRange(defaultKey2.clone(CameraKeys.CAMERA_TYPE));
            addListenersForDualCameraDisplayModeRange(defaultKey2.clone(CameraKeys.CAMERA_TYPE));
            addListenersForThermalPaletteRange(defaultKey2.clone(CameraKeys.CAMERA_TYPE));
        }
    }

    private void addListener(DJIParamAccessListener listener, DJISDKCacheKey... keys) {
        for (DJISDKCacheKey key : keys) {
            addOneListener(key, listener);
        }
    }

    private void addListenersForSSDVideoResolutionRange(DJISDKCacheKey... keys) {
        addListener(new DJIParamAccessListener() {
            /* class dji.common.camera.CameraParamRangeManager.AnonymousClass1 */

            public void onValueChange(DJISDKCacheKey key, DJISDKCacheParamValue oldValue, DJISDKCacheParamValue newValue) {
                CameraParamRangeManager.this.updateSSDVideoResolutionRange();
            }
        }, keys);
    }

    private void addListenersForSSDVideoResolutionAndFrameRateRange(DJISDKCacheKey... keys) {
        addListener(new DJIParamAccessListener() {
            /* class dji.common.camera.CameraParamRangeManager.AnonymousClass2 */

            public void onValueChange(DJISDKCacheKey key, DJISDKCacheParamValue oldValue, DJISDKCacheParamValue newValue) {
                CameraParamRangeManager.this.updateSSDVideoResolutionAndFrameRateRange();
            }
        }, keys);
    }

    private void addListenersForISORange(DJISDKCacheKey... keys) {
        addListener(new DJIParamAccessListener() {
            /* class dji.common.camera.CameraParamRangeManager.AnonymousClass3 */

            public void onValueChange(DJISDKCacheKey key, DJISDKCacheParamValue oldValue, DJISDKCacheParamValue newValue) {
                CameraParamRangeManager.this.updateCameraISORange();
            }
        }, keys);
    }

    private void addListenersForExposureCompensationRange(DJISDKCacheKey... keys) {
        addListener(new DJIParamAccessListener() {
            /* class dji.common.camera.CameraParamRangeManager.AnonymousClass4 */

            public void onValueChange(DJISDKCacheKey key, DJISDKCacheParamValue oldValue, DJISDKCacheParamValue newValue) {
                CameraParamRangeManager.this.updateExposureCompensationRange();
            }
        }, keys);
    }

    private void addListenersForExposureModeRange(DJISDKCacheKey... keys) {
        addListener(new DJIParamAccessListener() {
            /* class dji.common.camera.CameraParamRangeManager.AnonymousClass5 */

            public void onValueChange(DJISDKCacheKey key, DJISDKCacheParamValue oldValue, DJISDKCacheParamValue newValue) {
                CameraParamRangeManager.this.updateExposureModeRange();
            }
        }, keys);
    }

    private void addListenersForCameraModeRange(DJISDKCacheKey... keys) {
        addListener(new DJIParamAccessListener() {
            /* class dji.common.camera.CameraParamRangeManager.AnonymousClass6 */

            public void onValueChange(DJISDKCacheKey key, DJISDKCacheParamValue oldValue, DJISDKCacheParamValue newValue) {
                CameraParamRangeManager.this.updateCameraModeRange();
            }
        }, keys);
    }

    private void addListenersForShootPhotoModeRange(DJISDKCacheKey... keys) {
        addListener(new DJIParamAccessListener() {
            /* class dji.common.camera.CameraParamRangeManager.AnonymousClass7 */

            public void onValueChange(DJISDKCacheKey key, DJISDKCacheParamValue oldValue, DJISDKCacheParamValue newValue) {
                CameraParamRangeManager.this.updateShootPhotoModeRange();
            }
        }, keys);
    }

    private void addListenersForShootPhotoModeChildRange(DJISDKCacheKey... keys) {
        addListener(new DJIParamAccessListener() {
            /* class dji.common.camera.CameraParamRangeManager.AnonymousClass8 */

            public void onValueChange(DJISDKCacheKey key, DJISDKCacheParamValue oldValue, DJISDKCacheParamValue newValue) {
                CameraParamRangeManager.this.updateShootPhotoModeChildRange();
            }
        }, keys);
    }

    private void addListenersForPhotoFileFormatRange(DJISDKCacheKey... keys) {
        addListener(new DJIParamAccessListener() {
            /* class dji.common.camera.CameraParamRangeManager.AnonymousClass9 */

            public void onValueChange(DJISDKCacheKey key, DJISDKCacheParamValue oldValue, DJISDKCacheParamValue newValue) {
                CameraParamRangeManager.this.updateCameraPhotoFileFormatRange();
            }
        }, keys);
    }

    private void addListenersForPanoOriginalImagesFormatRange(DJISDKCacheKey... keys) {
        addListener(new DJIParamAccessListener() {
            /* class dji.common.camera.CameraParamRangeManager.AnonymousClass10 */

            public void onValueChange(DJISDKCacheKey key, DJISDKCacheParamValue oldValue, DJISDKCacheParamValue newValue) {
                CameraParamRangeManager.this.updateCameraPanoOriginalImagesFormatRange();
            }
        }, keys);
    }

    private void addListenersForHyperlapseOriginalImagesFormatRange(DJISDKCacheKey... keys) {
        addListener(new DJIParamAccessListener() {
            /* class dji.common.camera.CameraParamRangeManager.AnonymousClass11 */

            public void onValueChange(DJISDKCacheKey key, DJISDKCacheParamValue oldValue, DJISDKCacheParamValue newValue) {
                CameraParamRangeManager.this.updateCameraHyperlapseOriginalImagesFormatRange();
            }
        }, keys);
    }

    private void addListenersForWhiteBalancePresentRange(DJISDKCacheKey... keys) {
        addListener(new DJIParamAccessListener() {
            /* class dji.common.camera.CameraParamRangeManager.AnonymousClass12 */

            public void onValueChange(DJISDKCacheKey key, DJISDKCacheParamValue oldValue, DJISDKCacheParamValue newValue) {
                CameraParamRangeManager.this.updateCameraWhiteBalancePresentRange();
            }
        }, keys);
    }

    private void addListenersForWhiteBalanceCustomColorTemperatureRange(DJISDKCacheKey... keys) {
        addListener(new DJIParamAccessListener() {
            /* class dji.common.camera.CameraParamRangeManager.AnonymousClass13 */

            public void onValueChange(DJISDKCacheKey key, DJISDKCacheParamValue oldValue, DJISDKCacheParamValue newValue) {
                CameraParamRangeManager.this.updateCameraWhiteBalanceCustomColorTemperatureRange();
            }
        }, keys);
    }

    private void addListenersForPhotoAspectRatioRange(DJISDKCacheKey... keys) {
        addListener(new DJIParamAccessListener() {
            /* class dji.common.camera.CameraParamRangeManager.AnonymousClass14 */

            public void onValueChange(DJISDKCacheKey key, DJISDKCacheParamValue oldValue, DJISDKCacheParamValue newValue) {
                CameraParamRangeManager.this.updateCameraPhotoAspectRatioRange();
            }
        }, keys);
    }

    private void addListenersForVideoFileFormatRange(DJISDKCacheKey... keys) {
        addListener(new DJIParamAccessListener() {
            /* class dji.common.camera.CameraParamRangeManager.AnonymousClass15 */

            public void onValueChange(DJISDKCacheKey key, DJISDKCacheParamValue oldValue, DJISDKCacheParamValue newValue) {
                CameraParamRangeManager.this.updateVideoFileFormatRange();
            }
        }, keys);
    }

    private void addListenersForAntiFlickerRange(DJISDKCacheKey... keys) {
        addListener(new DJIParamAccessListener() {
            /* class dji.common.camera.CameraParamRangeManager.AnonymousClass16 */

            public void onValueChange(DJISDKCacheKey key, DJISDKCacheParamValue oldValue, DJISDKCacheParamValue newValue) {
                CameraParamRangeManager.this.updateCameraAntiFlickerRange();
            }
        }, keys);
    }

    private void addListenersForVideoStandardRange(DJISDKCacheKey... keys) {
        addListener(new DJIParamAccessListener() {
            /* class dji.common.camera.CameraParamRangeManager.AnonymousClass17 */

            public void onValueChange(DJISDKCacheKey key, DJISDKCacheParamValue oldValue, DJISDKCacheParamValue newValue) {
                CameraParamRangeManager.this.updateCameraVideoStandardRange();
            }
        }, keys);
    }

    private void addListenersForVideoCompressionStandardRange(DJISDKCacheKey... keys) {
        addListener(new DJIParamAccessListener() {
            /* class dji.common.camera.CameraParamRangeManager.AnonymousClass18 */

            public void onValueChange(DJISDKCacheKey key, DJISDKCacheParamValue oldValue, DJISDKCacheParamValue newValue) {
                CameraParamRangeManager.this.updateCameraVideoCompressionStandardRange();
            }
        }, keys);
    }

    private void addListenersForOrientationRange(DJISDKCacheKey... keys) {
        addListener(new DJIParamAccessListener() {
            /* class dji.common.camera.CameraParamRangeManager.AnonymousClass19 */

            public void onValueChange(DJISDKCacheKey key, DJISDKCacheParamValue oldValue, DJISDKCacheParamValue newValue) {
                CameraParamRangeManager.this.updateCameraOrientationRange();
            }
        }, keys);
    }

    private void addListenersForShutterSpeedRange(DJISDKCacheKey... keys) {
        addListener(new DJIParamAccessListener() {
            /* class dji.common.camera.CameraParamRangeManager.AnonymousClass20 */

            public void onValueChange(DJISDKCacheKey key, DJISDKCacheParamValue oldValue, DJISDKCacheParamValue newValue) {
                CameraParamRangeManager.this.updateShutterSpeedRange();
            }
        }, keys);
    }

    private void addListenersForApertureRange(DJISDKCacheKey... keys) {
        addListener(new DJIParamAccessListener() {
            /* class dji.common.camera.CameraParamRangeManager.AnonymousClass21 */

            public void onValueChange(DJISDKCacheKey key, DJISDKCacheParamValue oldValue, DJISDKCacheParamValue newValue) {
                CameraParamRangeManager.this.updateApertureRange();
            }
        }, keys);
    }

    private void addListenersForCameraFilterRange(DJISDKCacheKey... keys) {
        addListener(new DJIParamAccessListener() {
            /* class dji.common.camera.CameraParamRangeManager.AnonymousClass22 */

            public void onValueChange(DJISDKCacheKey key, DJISDKCacheParamValue oldValue, DJISDKCacheParamValue newValue) {
                CameraParamRangeManager.this.updateCameraDigitalFilterRange();
            }
        }, keys);
    }

    private void addListenersForSSDVideoLookRange(DJISDKCacheKey... keys) {
        addListener(new DJIParamAccessListener() {
            /* class dji.common.camera.CameraParamRangeManager.AnonymousClass23 */

            public void onValueChange(DJISDKCacheKey key, DJISDKCacheParamValue oldValue, DJISDKCacheParamValue newValue) {
                CameraParamRangeManager.this.updateSSDVideoLookRange();
            }
        }, keys);
    }

    private void addListenersForDualCameraDisplayModeRange(DJISDKCacheKey... keys) {
        addListener(new DJIParamAccessListener() {
            /* class dji.common.camera.CameraParamRangeManager.AnonymousClass24 */

            public void onValueChange(DJISDKCacheKey key, DJISDKCacheParamValue oldValue, DJISDKCacheParamValue newValue) {
                CameraParamRangeManager.this.updateDualCameraDisplayModeRange();
            }
        }, keys);
    }

    private void addListenersForThermalPaletteRange(DJISDKCacheKey... keys) {
        addListener(new DJIParamAccessListener() {
            /* class dji.common.camera.CameraParamRangeManager.AnonymousClass25 */

            public void onValueChange(DJISDKCacheKey key, DJISDKCacheParamValue oldValue, DJISDKCacheParamValue newValue) {
                CameraParamRangeManager.this.updateThermalPaletteRange();
            }
        }, keys);
    }

    private void addOneListener(DJISDKCacheKey key, DJIParamAccessListener listener) {
        DJISDKCache.getInstance().startListeningForUpdates(key, listener, false);
        if (!this.listeners.contains(listener)) {
            this.listeners.add(listener);
        }
    }

    private static Object getKeyAvailableValue(DJISDKCacheKey keypath) {
        DJISDKCacheParamValue value = DJISDKCache.getInstance().getAvailableValue(keypath);
        if (value == null) {
            return null;
        }
        return value.getData();
    }

    private static SettingsDefinitions.ExposureMode getExposureMode(int componentIndex) {
        return (SettingsDefinitions.ExposureMode) getKeyAvailableValue(KeyHelper.getCameraKey(componentIndex, CameraKeys.EXPOSURE_MODE));
    }

    private static SettingsDefinitions.CameraMode getCameraMode(int componentIndex) {
        return (SettingsDefinitions.CameraMode) getKeyAvailableValue(KeyHelper.getCameraKey(componentIndex, "Mode"));
    }

    private SettingsDefinitions.PhotoFileFormat getImageFormat(int componentIndex) {
        return (SettingsDefinitions.PhotoFileFormat) getKeyAvailableValue(KeyHelper.getCameraKey(componentIndex, CameraKeys.PHOTO_FILE_FORMAT));
    }

    private static SettingsDefinitions.VideoStandard getVideoStandard(int componentIndex) {
        return (SettingsDefinitions.VideoStandard) getKeyAvailableValue(KeyHelper.getCameraKey(componentIndex, CameraKeys.VIDEO_STANDARD));
    }

    private static SettingsDefinitions.CameraColor getCameraColor(int componentIndex) {
        return (SettingsDefinitions.CameraColor) getKeyAvailableValue(KeyHelper.getCameraKey(componentIndex, CameraKeys.CAMERA_COLOR));
    }

    private static boolean getTrackingMode(int componentIndex) {
        Object result = getKeyAvailableValue(KeyHelper.getCameraKey(componentIndex, CameraKeys.TRACKING_MODE));
        if (!(result instanceof Boolean)) {
            return false;
        }
        return ((Boolean) result).booleanValue();
    }

    private static ResolutionAndFrameRate getResolutionAndFrameRate(int componentIndex) {
        return (ResolutionAndFrameRate) getKeyAvailableValue(KeyHelper.getCameraKey(componentIndex, CameraKeys.RESOLUTION_FRAME_RATE));
    }

    private static SSDRawMode getSSDRawMode(int componentIndex) {
        return (SSDRawMode) getKeyAvailableValue(KeyHelper.getCameraKey(componentIndex, CameraKeys.SSD_RAW_MODE));
    }

    public static SettingsDefinitions.VideoResolution[] getSSDVideoResolutionRange(int componentIndex) {
        if (!DJIComponentManager.getInstance().isAircraftConnected()) {
            return null;
        }
        ArrayList<SettingsDefinitions.VideoResolution> resolutionRange = new ArrayList<>();
        ResolutionAndFrameRate[] resolutionAndFrameRates = getX7SSDVideoResolutionAndFrameRateRange(componentIndex);
        if (resolutionAndFrameRates != null) {
            for (ResolutionAndFrameRate rate : resolutionAndFrameRates) {
                resolutionRange.add(rate.getResolution());
            }
        }
        resolutionRange.add(SettingsDefinitions.VideoResolution.NO_SSD_VIDEO);
        return (SettingsDefinitions.VideoResolution[]) resolutionRange.toArray(new SettingsDefinitions.VideoResolution[resolutionRange.size()]);
    }

    /* access modifiers changed from: private */
    public void updateSSDVideoResolutionAndFrameRateRange() {
        ResolutionAndFrameRate[] sdList = getVideoResolutionAndFrameRateRange(this.defaultKey.getIndex());
        if (sdList != null) {
            if (this.resolutionAndFrameRateRange == null || !Arrays.deepEquals(this.resolutionAndFrameRateRange, sdList)) {
                this.resolutionAndFrameRateRange = sdList;
                if (this.onValueChangeListener != null) {
                    this.onValueChangeListener.onNewValue(sdList, this.defaultKey.clone(CameraKeys.VIDEO_RESOLUTION_FRAME_RATE_RANGE));
                }
            }
            ResolutionAndFrameRate[] ssdList = getSSDVideoResolutionAndFrameRateRange(this.defaultKey.getIndex());
            if (ssdList == null) {
                return;
            }
            if (this.ssdResolutionAndFrameRateRange == null || !Arrays.deepEquals(this.ssdResolutionAndFrameRateRange, ssdList)) {
                this.ssdResolutionAndFrameRateRange = ssdList;
                if (this.onValueChangeListener != null) {
                    this.onValueChangeListener.onNewValue(ssdList, this.defaultKey.clone(CameraKeys.SSD_VIDEO_RESOLUTION_FRAME_RATE_RANGE));
                }
            }
        }
    }

    public static ResolutionAndFrameRate[] getSSDVideoResolutionAndFrameRateRange(int componentIndex) {
        ResolutionAndFrameRate[] list = null;
        SettingsDefinitions.CameraType cameraType = CameraUtils.getCameraType(componentIndex);
        if (cameraType == null) {
            return null;
        }
        switch (cameraType) {
            case DJICameraTypeFC6540:
                list = getX7SSDVideoResolutionAndFrameRateRange(componentIndex);
                break;
            case DJICameraTypeFC6520:
                list = getX5sSSDVideoResolutionAndFrameRateRange(componentIndex);
                break;
        }
        return list;
    }

    public static ResolutionAndFrameRate[] getX7SSDVideoResolutionAndFrameRateRange(int componentIndex) {
        List<ResolutionAndFrameRate> rangeList = new LinkedList<>();
        SSDRawMode rawMode = getSSDRawMode(componentIndex);
        SettingsDefinitions.VideoStandard videoType = getVideoStandard(componentIndex);
        if (rawMode != null) {
            switch (rawMode) {
                case JPEG_LOSSLESS:
                    rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_6016X3200, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS));
                    rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_5760X3240, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS));
                    rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_5280x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS));
                    rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_4096x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS));
                    rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS));
                    rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_6016X3200, SettingsDefinitions.VideoFrameRate.FRAME_RATE_24_FPS));
                    rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_5760X3240, SettingsDefinitions.VideoFrameRate.FRAME_RATE_24_FPS));
                    rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_5280x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_24_FPS));
                    rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_4096x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_24_FPS));
                    rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_24_FPS));
                    rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_6016X3200, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS));
                    rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_5760X3240, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS));
                    rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_5280x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS));
                    rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_4096x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS));
                    rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS));
                    if (videoType == SettingsDefinitions.VideoStandard.NTSC) {
                        rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_6016X3200, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS));
                        rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_5760X3240, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS));
                        rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_5280x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS));
                        rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_4096x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS));
                        rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS));
                        rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_6016X3200, SettingsDefinitions.VideoFrameRate.FRAME_RATE_30_FPS));
                        rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_5760X3240, SettingsDefinitions.VideoFrameRate.FRAME_RATE_30_FPS));
                        rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_5280x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_30_FPS));
                        rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_4096x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_30_FPS));
                        rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_30_FPS));
                    }
                    rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_5280x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS));
                    rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_4096x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS));
                    rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS));
                    rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_5280x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_48_FPS));
                    rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_4096x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_48_FPS));
                    rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_48_FPS));
                    rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3712x2088, SettingsDefinitions.VideoFrameRate.FRAME_RATE_50_FPS));
                    rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3944x2088, SettingsDefinitions.VideoFrameRate.FRAME_RATE_50_FPS));
                    if (videoType == SettingsDefinitions.VideoStandard.NTSC) {
                        rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3712x2088, SettingsDefinitions.VideoFrameRate.FRAME_RATE_59_DOT_940_FPS));
                        rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3944x2088, SettingsDefinitions.VideoFrameRate.FRAME_RATE_59_DOT_940_FPS));
                        break;
                    }
                    break;
                case PRORES_HQ444:
                    rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS));
                    rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_24_FPS));
                    rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS));
                    if (videoType == SettingsDefinitions.VideoStandard.NTSC) {
                        rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS));
                        rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_30_FPS));
                    }
                    rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2048x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS));
                    rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS));
                    rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2048x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_48_FPS));
                    rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_48_FPS));
                    rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2048x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_50_FPS));
                    rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_50_FPS));
                    if (videoType == SettingsDefinitions.VideoStandard.NTSC) {
                        rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2048x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_59_DOT_940_FPS));
                        rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_59_DOT_940_FPS));
                        break;
                    }
                    break;
                case PRORES_HQ422:
                    rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_5280x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS));
                    rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_4096x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS));
                    rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS));
                    rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_5280x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_24_FPS));
                    rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_4096x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_24_FPS));
                    rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_24_FPS));
                    rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_5280x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS));
                    rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_4096x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS));
                    rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS));
                    if (videoType == SettingsDefinitions.VideoStandard.NTSC) {
                        rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_5280x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS));
                        rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_4096x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS));
                        rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS));
                        rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_5280x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_30_FPS));
                        rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_4096x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_30_FPS));
                        rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_30_FPS));
                    }
                    rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2704x1520, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS));
                    rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2048x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS));
                    rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS));
                    rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2704x1520, SettingsDefinitions.VideoFrameRate.FRAME_RATE_48_FPS));
                    rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2048x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_48_FPS));
                    rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_48_FPS));
                    rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2704x1520, SettingsDefinitions.VideoFrameRate.FRAME_RATE_50_FPS));
                    rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2048x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_50_FPS));
                    rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_50_FPS));
                    if (videoType == SettingsDefinitions.VideoStandard.NTSC) {
                        rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2704x1520, SettingsDefinitions.VideoFrameRate.FRAME_RATE_59_DOT_940_FPS));
                        rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2048x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_59_DOT_940_FPS));
                        rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_59_DOT_940_FPS));
                        break;
                    }
                    break;
            }
        }
        return (ResolutionAndFrameRate[]) rangeList.toArray(new ResolutionAndFrameRate[rangeList.size()]);
    }

    public static ResolutionAndFrameRate[] getX5sSSDVideoResolutionAndFrameRateRange(int componentIndex) {
        SSDRawMode rawMode = getSSDRawMode(componentIndex);
        if (rawMode == null) {
            return null;
        }
        switch (rawMode) {
            case JPEG_LOSSLESS:
                return new ResolutionAndFrameRate[]{new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_5280x2972, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_4096x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_5280x2972, SettingsDefinitions.VideoFrameRate.FRAME_RATE_24_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_4096x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_24_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_24_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_5280x2972, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_4096x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_5280x2972, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_4096x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_4096x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_48_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_48_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_4096x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_50_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_50_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_4096x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_59_DOT_940_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_59_DOT_940_FPS)};
            case PRORES_HQ444:
                return new ResolutionAndFrameRate[]{new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2048x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_24_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_50_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2048x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_50_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_59_DOT_940_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2048x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_59_DOT_940_FPS)};
            case PRORES_HQ422:
                return new ResolutionAndFrameRate[]{new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_5280x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2704x1520, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2048x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_5280x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_24_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_24_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_5280x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2704x1520, SettingsDefinitions.VideoFrameRate.FRAME_RATE_50_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_50_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2048x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_50_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2704x1520, SettingsDefinitions.VideoFrameRate.FRAME_RATE_59_DOT_940_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_59_DOT_940_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2048x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_59_DOT_940_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_5280x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS)};
            default:
                return null;
        }
    }

    /* access modifiers changed from: private */
    public void updateSSDVideoResolutionRange() {
        SettingsDefinitions.VideoResolution[] list = getSSDVideoResolutionRange(this.defaultKey.getIndex());
        if (list != null) {
            if (this.ssdVideoResolutionRange == null || !Arrays.deepEquals(this.ssdVideoResolutionRange, list)) {
                this.ssdVideoResolutionRange = list;
                if (this.onValueChangeListener != null) {
                    this.onValueChangeListener.onNewValue(list, this.defaultKey.clone(CameraKeys.SSD_VIDEO_RESOLUTION_RANGE));
                }
            }
        }
    }

    public static SettingsDefinitions.ISO[] getISORange(int componentIndex) {
        SettingsDefinitions.ISO[] list = null;
        if (DJIComponentManager.getInstance().isAircraftConnected()) {
            SettingsDefinitions.CameraType type = CameraUtils.getCameraType(componentIndex);
            if (type == null) {
                return null;
            }
            SettingsDefinitions.CameraMode cameraMode = getCameraMode(componentIndex);
            if (cameraMode == null) {
                return null;
            }
            SettingsDefinitions.ExposureMode exposureMode = getExposureMode(componentIndex);
            if (exposureMode == null) {
                return null;
            }
            SettingsDefinitions.CameraColor cameraColor = getCameraColor(componentIndex);
            SettingsDefinitions.ExposureSensitivityMode exposureSensitivityMode = CameraUtils.getExposureSensitivityMode(componentIndex);
            if (type == SettingsDefinitions.CameraType.DJICameraTypeGD600 || type == SettingsDefinitions.CameraType.DJIPayloadCamera) {
                list = SettingsDefinitions.ExposureMode.MANUAL == exposureMode ? new SettingsDefinitions.ISO[]{SettingsDefinitions.ISO.ISO_100, SettingsDefinitions.ISO.ISO_200, SettingsDefinitions.ISO.ISO_400, SettingsDefinitions.ISO.ISO_800, SettingsDefinitions.ISO.ISO_1600} : new SettingsDefinitions.ISO[]{SettingsDefinitions.ISO.AUTO};
            } else if (type == SettingsDefinitions.CameraType.DJICameraTypeFC260 || type == SettingsDefinitions.CameraType.DJICameraTypeFC300S || type == SettingsDefinitions.CameraType.DJICameraTypeFC300X || type == SettingsDefinitions.CameraType.DJICameraTypeFC350 || type == SettingsDefinitions.CameraType.DJICameraTypeFC300XW || type == SettingsDefinitions.CameraType.DJICameraTypeFC330X || type == SettingsDefinitions.CameraType.DJICameraTypeCV600 || type == SettingsDefinitions.CameraType.DJICameraTypeFC220 || type == SettingsDefinitions.CameraType.DJICameraTypeFC220S || type == SettingsDefinitions.CameraType.DJICameraTypeFC1102) {
                list = cameraMode == SettingsDefinitions.CameraMode.RECORD_VIDEO ? SettingsDefinitions.ExposureMode.MANUAL == exposureMode ? new SettingsDefinitions.ISO[]{SettingsDefinitions.ISO.ISO_100, SettingsDefinitions.ISO.ISO_200, SettingsDefinitions.ISO.ISO_400, SettingsDefinitions.ISO.ISO_800, SettingsDefinitions.ISO.ISO_1600, SettingsDefinitions.ISO.ISO_3200} : new SettingsDefinitions.ISO[]{SettingsDefinitions.ISO.ISO_100, SettingsDefinitions.ISO.ISO_200, SettingsDefinitions.ISO.ISO_400, SettingsDefinitions.ISO.ISO_800, SettingsDefinitions.ISO.ISO_1600, SettingsDefinitions.ISO.ISO_3200, SettingsDefinitions.ISO.AUTO} : SettingsDefinitions.ExposureMode.MANUAL == exposureMode ? new SettingsDefinitions.ISO[]{SettingsDefinitions.ISO.ISO_100, SettingsDefinitions.ISO.ISO_200, SettingsDefinitions.ISO.ISO_400, SettingsDefinitions.ISO.ISO_800, SettingsDefinitions.ISO.ISO_1600} : new SettingsDefinitions.ISO[]{SettingsDefinitions.ISO.ISO_100, SettingsDefinitions.ISO.ISO_200, SettingsDefinitions.ISO.ISO_400, SettingsDefinitions.ISO.ISO_800, SettingsDefinitions.ISO.ISO_1600, SettingsDefinitions.ISO.AUTO};
            } else if (type == SettingsDefinitions.CameraType.DJICameraTypeFC550 || type == SettingsDefinitions.CameraType.DJICameraTypeFC550Raw || type == SettingsDefinitions.CameraType.DJICameraTypeFC6540 || type == SettingsDefinitions.CameraType.DJICameraTypeFC6520) {
                list = cameraMode == SettingsDefinitions.CameraMode.RECORD_VIDEO ? SettingsDefinitions.ExposureMode.MANUAL == exposureMode ? (type == SettingsDefinitions.CameraType.DJICameraTypeFC6540 && exposureSensitivityMode != null && exposureSensitivityMode == SettingsDefinitions.ExposureSensitivityMode.EI) ? new SettingsDefinitions.ISO[]{SettingsDefinitions.ISO.ISO_100, SettingsDefinitions.ISO.ISO_200, SettingsDefinitions.ISO.ISO_400, SettingsDefinitions.ISO.ISO_800, SettingsDefinitions.ISO.ISO_1600} : new SettingsDefinitions.ISO[]{SettingsDefinitions.ISO.ISO_100, SettingsDefinitions.ISO.ISO_200, SettingsDefinitions.ISO.ISO_400, SettingsDefinitions.ISO.ISO_800, SettingsDefinitions.ISO.ISO_1600, SettingsDefinitions.ISO.ISO_3200, SettingsDefinitions.ISO.ISO_6400} : (type == SettingsDefinitions.CameraType.DJICameraTypeFC6540 && exposureSensitivityMode != null && exposureSensitivityMode == SettingsDefinitions.ExposureSensitivityMode.EI) ? new SettingsDefinitions.ISO[]{SettingsDefinitions.ISO.ISO_100, SettingsDefinitions.ISO.ISO_200, SettingsDefinitions.ISO.ISO_400, SettingsDefinitions.ISO.ISO_800, SettingsDefinitions.ISO.ISO_1600, SettingsDefinitions.ISO.AUTO} : new SettingsDefinitions.ISO[]{SettingsDefinitions.ISO.ISO_100, SettingsDefinitions.ISO.ISO_200, SettingsDefinitions.ISO.ISO_400, SettingsDefinitions.ISO.ISO_800, SettingsDefinitions.ISO.ISO_1600, SettingsDefinitions.ISO.ISO_3200, SettingsDefinitions.ISO.ISO_6400, SettingsDefinitions.ISO.AUTO} : SettingsDefinitions.ExposureMode.MANUAL == exposureMode ? new SettingsDefinitions.ISO[]{SettingsDefinitions.ISO.ISO_100, SettingsDefinitions.ISO.ISO_200, SettingsDefinitions.ISO.ISO_400, SettingsDefinitions.ISO.ISO_800, SettingsDefinitions.ISO.ISO_1600, SettingsDefinitions.ISO.ISO_3200, SettingsDefinitions.ISO.ISO_6400, SettingsDefinitions.ISO.ISO_12800, SettingsDefinitions.ISO.ISO_25600} : new SettingsDefinitions.ISO[]{SettingsDefinitions.ISO.ISO_100, SettingsDefinitions.ISO.ISO_200, SettingsDefinitions.ISO.ISO_400, SettingsDefinitions.ISO.ISO_800, SettingsDefinitions.ISO.ISO_1600, SettingsDefinitions.ISO.ISO_3200, SettingsDefinitions.ISO.ISO_6400, SettingsDefinitions.ISO.ISO_12800, SettingsDefinitions.ISO.ISO_25600, SettingsDefinitions.ISO.AUTO};
            } else if (type == SettingsDefinitions.CameraType.DJICameraTypeFC6310 || type == SettingsDefinitions.CameraType.DJICameraTypeFC6310S) {
                list = cameraMode == SettingsDefinitions.CameraMode.RECORD_VIDEO ? SettingsDefinitions.ExposureMode.MANUAL == exposureMode ? new SettingsDefinitions.ISO[]{SettingsDefinitions.ISO.ISO_100, SettingsDefinitions.ISO.ISO_200, SettingsDefinitions.ISO.ISO_400, SettingsDefinitions.ISO.ISO_800, SettingsDefinitions.ISO.ISO_1600, SettingsDefinitions.ISO.ISO_3200, SettingsDefinitions.ISO.ISO_6400} : new SettingsDefinitions.ISO[]{SettingsDefinitions.ISO.AUTO, SettingsDefinitions.ISO.ISO_100, SettingsDefinitions.ISO.ISO_200, SettingsDefinitions.ISO.ISO_400, SettingsDefinitions.ISO.ISO_800, SettingsDefinitions.ISO.ISO_1600, SettingsDefinitions.ISO.ISO_3200} : SettingsDefinitions.ExposureMode.MANUAL == exposureMode ? new SettingsDefinitions.ISO[]{SettingsDefinitions.ISO.ISO_100, SettingsDefinitions.ISO.ISO_200, SettingsDefinitions.ISO.ISO_400, SettingsDefinitions.ISO.ISO_800, SettingsDefinitions.ISO.ISO_1600, SettingsDefinitions.ISO.ISO_3200, SettingsDefinitions.ISO.ISO_6400, SettingsDefinitions.ISO.ISO_12800} : new SettingsDefinitions.ISO[]{SettingsDefinitions.ISO.AUTO, SettingsDefinitions.ISO.ISO_100, SettingsDefinitions.ISO.ISO_200, SettingsDefinitions.ISO.ISO_400, SettingsDefinitions.ISO.ISO_800, SettingsDefinitions.ISO.ISO_1600, SettingsDefinitions.ISO.ISO_3200};
            } else if (type == SettingsDefinitions.CameraType.DJICameraTypeFC6510) {
                list = cameraMode == SettingsDefinitions.CameraMode.RECORD_VIDEO ? SettingsDefinitions.ExposureMode.MANUAL == exposureMode ? new SettingsDefinitions.ISO[]{SettingsDefinitions.ISO.ISO_100, SettingsDefinitions.ISO.ISO_200, SettingsDefinitions.ISO.ISO_400, SettingsDefinitions.ISO.ISO_800, SettingsDefinitions.ISO.ISO_1600, SettingsDefinitions.ISO.ISO_3200, SettingsDefinitions.ISO.ISO_6400} : new SettingsDefinitions.ISO[]{SettingsDefinitions.ISO.AUTO, SettingsDefinitions.ISO.ISO_100, SettingsDefinitions.ISO.ISO_200, SettingsDefinitions.ISO.ISO_400, SettingsDefinitions.ISO.ISO_800, SettingsDefinitions.ISO.ISO_1600, SettingsDefinitions.ISO.ISO_3200, SettingsDefinitions.ISO.ISO_6400} : SettingsDefinitions.ExposureMode.MANUAL == exposureMode ? new SettingsDefinitions.ISO[]{SettingsDefinitions.ISO.ISO_100, SettingsDefinitions.ISO.ISO_200, SettingsDefinitions.ISO.ISO_400, SettingsDefinitions.ISO.ISO_800, SettingsDefinitions.ISO.ISO_1600, SettingsDefinitions.ISO.ISO_3200, SettingsDefinitions.ISO.ISO_6400, SettingsDefinitions.ISO.ISO_12800} : new SettingsDefinitions.ISO[]{SettingsDefinitions.ISO.AUTO, SettingsDefinitions.ISO.ISO_100, SettingsDefinitions.ISO.ISO_200, SettingsDefinitions.ISO.ISO_400, SettingsDefinitions.ISO.ISO_800, SettingsDefinitions.ISO.ISO_1600, SettingsDefinitions.ISO.ISO_3200, SettingsDefinitions.ISO.ISO_6400, SettingsDefinitions.ISO.ISO_12800};
            } else if (type == SettingsDefinitions.CameraType.DJICameraTypeFC230) {
                list = SettingsDefinitions.ExposureMode.MANUAL == exposureMode ? new SettingsDefinitions.ISO[]{SettingsDefinitions.ISO.ISO_100, SettingsDefinitions.ISO.ISO_200, SettingsDefinitions.ISO.ISO_400, SettingsDefinitions.ISO.ISO_800, SettingsDefinitions.ISO.ISO_1600, SettingsDefinitions.ISO.ISO_3200} : SettingsDefinitions.ExposureMode.PROGRAM == exposureMode ? cameraMode == SettingsDefinitions.CameraMode.RECORD_VIDEO ? new SettingsDefinitions.ISO[]{SettingsDefinitions.ISO.AUTO, SettingsDefinitions.ISO.ISO_100, SettingsDefinitions.ISO.ISO_200, SettingsDefinitions.ISO.ISO_400, SettingsDefinitions.ISO.ISO_800, SettingsDefinitions.ISO.ISO_1600, SettingsDefinitions.ISO.ISO_3200} : new SettingsDefinitions.ISO[]{SettingsDefinitions.ISO.AUTO, SettingsDefinitions.ISO.ISO_100, SettingsDefinitions.ISO.ISO_200, SettingsDefinitions.ISO.ISO_400, SettingsDefinitions.ISO.ISO_800, SettingsDefinitions.ISO.ISO_1600, SettingsDefinitions.ISO.ISO_3200} : new SettingsDefinitions.ISO[]{SettingsDefinitions.ISO.AUTO, SettingsDefinitions.ISO.ISO_100, SettingsDefinitions.ISO.ISO_200, SettingsDefinitions.ISO.ISO_400, SettingsDefinitions.ISO.ISO_800, SettingsDefinitions.ISO.ISO_1600, SettingsDefinitions.ISO.ISO_3200};
            } else if (type == SettingsDefinitions.CameraType.DJICameraTypeFC240_1) {
                if (cameraMode == SettingsDefinitions.CameraMode.RECORD_VIDEO) {
                    if (cameraColor != null) {
                        switch (cameraColor) {
                            case NONE:
                            case D_CINELIKE:
                                if (SettingsDefinitions.ExposureMode.MANUAL != exposureMode) {
                                    list = new SettingsDefinitions.ISO[]{SettingsDefinitions.ISO.AUTO, SettingsDefinitions.ISO.ISO_100, SettingsDefinitions.ISO.ISO_200, SettingsDefinitions.ISO.ISO_400, SettingsDefinitions.ISO.ISO_800, SettingsDefinitions.ISO.ISO_1600, SettingsDefinitions.ISO.ISO_3200, SettingsDefinitions.ISO.ISO_6400};
                                    break;
                                } else {
                                    list = new SettingsDefinitions.ISO[]{SettingsDefinitions.ISO.ISO_100, SettingsDefinitions.ISO.ISO_200, SettingsDefinitions.ISO.ISO_400, SettingsDefinitions.ISO.ISO_800, SettingsDefinitions.ISO.ISO_1600, SettingsDefinitions.ISO.ISO_3200, SettingsDefinitions.ISO.ISO_6400};
                                    break;
                                }
                            case D_LOG:
                                if (SettingsDefinitions.ExposureMode.MANUAL != exposureMode) {
                                    list = new SettingsDefinitions.ISO[]{SettingsDefinitions.ISO.AUTO, SettingsDefinitions.ISO.ISO_100, SettingsDefinitions.ISO.ISO_200, SettingsDefinitions.ISO.ISO_400, SettingsDefinitions.ISO.ISO_800, SettingsDefinitions.ISO.ISO_1600};
                                    break;
                                } else {
                                    list = new SettingsDefinitions.ISO[]{SettingsDefinitions.ISO.ISO_100, SettingsDefinitions.ISO.ISO_200, SettingsDefinitions.ISO.ISO_400, SettingsDefinitions.ISO.ISO_800, SettingsDefinitions.ISO.ISO_1600};
                                    break;
                                }
                            case HLG:
                                if (SettingsDefinitions.ExposureMode.MANUAL != exposureMode) {
                                    list = new SettingsDefinitions.ISO[]{SettingsDefinitions.ISO.AUTO, SettingsDefinitions.ISO.ISO_100, SettingsDefinitions.ISO.ISO_200, SettingsDefinitions.ISO.ISO_400, SettingsDefinitions.ISO.ISO_800, SettingsDefinitions.ISO.ISO_1600, SettingsDefinitions.ISO.ISO_3200};
                                    break;
                                } else {
                                    list = new SettingsDefinitions.ISO[]{SettingsDefinitions.ISO.ISO_100, SettingsDefinitions.ISO.ISO_200, SettingsDefinitions.ISO.ISO_400, SettingsDefinitions.ISO.ISO_800, SettingsDefinitions.ISO.ISO_1600, SettingsDefinitions.ISO.ISO_3200};
                                    break;
                                }
                            default:
                                if (SettingsDefinitions.ExposureMode.MANUAL != exposureMode) {
                                    list = new SettingsDefinitions.ISO[]{SettingsDefinitions.ISO.AUTO, SettingsDefinitions.ISO.ISO_100, SettingsDefinitions.ISO.ISO_200, SettingsDefinitions.ISO.ISO_400, SettingsDefinitions.ISO.ISO_800, SettingsDefinitions.ISO.ISO_1600, SettingsDefinitions.ISO.ISO_3200};
                                    break;
                                } else {
                                    list = new SettingsDefinitions.ISO[]{SettingsDefinitions.ISO.ISO_100, SettingsDefinitions.ISO.ISO_200, SettingsDefinitions.ISO.ISO_400, SettingsDefinitions.ISO.ISO_800, SettingsDefinitions.ISO.ISO_1600, SettingsDefinitions.ISO.ISO_3200};
                                    break;
                                }
                        }
                    } else {
                        list = SettingsDefinitions.ExposureMode.MANUAL == exposureMode ? new SettingsDefinitions.ISO[]{SettingsDefinitions.ISO.ISO_100, SettingsDefinitions.ISO.ISO_200, SettingsDefinitions.ISO.ISO_400, SettingsDefinitions.ISO.ISO_800, SettingsDefinitions.ISO.ISO_1600, SettingsDefinitions.ISO.ISO_3200, SettingsDefinitions.ISO.ISO_6400} : new SettingsDefinitions.ISO[]{SettingsDefinitions.ISO.AUTO, SettingsDefinitions.ISO.ISO_100, SettingsDefinitions.ISO.ISO_200, SettingsDefinitions.ISO.ISO_400, SettingsDefinitions.ISO.ISO_800, SettingsDefinitions.ISO.ISO_1600, SettingsDefinitions.ISO.ISO_3200, SettingsDefinitions.ISO.ISO_6400};
                    }
                } else {
                    list = SettingsDefinitions.ExposureMode.MANUAL == exposureMode ? new SettingsDefinitions.ISO[]{SettingsDefinitions.ISO.ISO_100, SettingsDefinitions.ISO.ISO_200, SettingsDefinitions.ISO.ISO_400, SettingsDefinitions.ISO.ISO_800, SettingsDefinitions.ISO.ISO_1600, SettingsDefinitions.ISO.ISO_3200, SettingsDefinitions.ISO.ISO_6400, SettingsDefinitions.ISO.ISO_12800} : new SettingsDefinitions.ISO[]{SettingsDefinitions.ISO.AUTO, SettingsDefinitions.ISO.ISO_100, SettingsDefinitions.ISO.ISO_200, SettingsDefinitions.ISO.ISO_400, SettingsDefinitions.ISO.ISO_800, SettingsDefinitions.ISO.ISO_1600, SettingsDefinitions.ISO.ISO_3200};
                }
            } else if (type == SettingsDefinitions.CameraType.DJICameraTypeFC240) {
                list = cameraMode == SettingsDefinitions.CameraMode.RECORD_VIDEO ? SettingsDefinitions.ExposureMode.MANUAL == exposureMode ? new SettingsDefinitions.ISO[]{SettingsDefinitions.ISO.ISO_100, SettingsDefinitions.ISO.ISO_200, SettingsDefinitions.ISO.ISO_400, SettingsDefinitions.ISO.ISO_800, SettingsDefinitions.ISO.ISO_1600, SettingsDefinitions.ISO.ISO_3200} : new SettingsDefinitions.ISO[]{SettingsDefinitions.ISO.AUTO, SettingsDefinitions.ISO.ISO_100, SettingsDefinitions.ISO.ISO_200, SettingsDefinitions.ISO.ISO_400, SettingsDefinitions.ISO.ISO_800, SettingsDefinitions.ISO.ISO_1600, SettingsDefinitions.ISO.ISO_3200} : SettingsDefinitions.ExposureMode.MANUAL == exposureMode ? new SettingsDefinitions.ISO[]{SettingsDefinitions.ISO.ISO_100, SettingsDefinitions.ISO.ISO_200, SettingsDefinitions.ISO.ISO_400, SettingsDefinitions.ISO.ISO_800, SettingsDefinitions.ISO.ISO_1600, SettingsDefinitions.ISO.ISO_3200} : new SettingsDefinitions.ISO[]{SettingsDefinitions.ISO.AUTO, SettingsDefinitions.ISO.ISO_100, SettingsDefinitions.ISO.ISO_200, SettingsDefinitions.ISO.ISO_400, SettingsDefinitions.ISO.ISO_800, SettingsDefinitions.ISO.ISO_1600};
            } else if (type == SettingsDefinitions.CameraType.DJICameraTypeFC245_IMX477) {
                list = cameraMode == SettingsDefinitions.CameraMode.RECORD_VIDEO ? SettingsDefinitions.ExposureMode.MANUAL == exposureMode ? new SettingsDefinitions.ISO[]{SettingsDefinitions.ISO.ISO_100, SettingsDefinitions.ISO.ISO_200, SettingsDefinitions.ISO.ISO_400, SettingsDefinitions.ISO.ISO_800, SettingsDefinitions.ISO.ISO_1600, SettingsDefinitions.ISO.ISO_3200, SettingsDefinitions.ISO.ISO_6400, SettingsDefinitions.ISO.ISO_12800} : new SettingsDefinitions.ISO[]{SettingsDefinitions.ISO.AUTO, SettingsDefinitions.ISO.ISO_100, SettingsDefinitions.ISO.ISO_200, SettingsDefinitions.ISO.ISO_400, SettingsDefinitions.ISO.ISO_800, SettingsDefinitions.ISO.ISO_1600, SettingsDefinitions.ISO.ISO_3200} : SettingsDefinitions.ExposureMode.MANUAL == exposureMode ? new SettingsDefinitions.ISO[]{SettingsDefinitions.ISO.ISO_100, SettingsDefinitions.ISO.ISO_200, SettingsDefinitions.ISO.ISO_400, SettingsDefinitions.ISO.ISO_800, SettingsDefinitions.ISO.ISO_1600, SettingsDefinitions.ISO.ISO_3200, SettingsDefinitions.ISO.ISO_6400, SettingsDefinitions.ISO.ISO_12800} : new SettingsDefinitions.ISO[]{SettingsDefinitions.ISO.AUTO, SettingsDefinitions.ISO.ISO_100, SettingsDefinitions.ISO.ISO_200, SettingsDefinitions.ISO.ISO_400, SettingsDefinitions.ISO.ISO_800, SettingsDefinitions.ISO.ISO_1600};
            } else if (type != SettingsDefinitions.CameraType.DJICameraTypeFC160) {
                list = null;
            } else if (exposureMode == SettingsDefinitions.ExposureMode.PROGRAM) {
                list = new SettingsDefinitions.ISO[]{SettingsDefinitions.ISO.AUTO};
            } else if (exposureMode == SettingsDefinitions.ExposureMode.MANUAL) {
                list = new SettingsDefinitions.ISO[]{SettingsDefinitions.ISO.ISO_100, SettingsDefinitions.ISO.ISO_200, SettingsDefinitions.ISO.ISO_400, SettingsDefinitions.ISO.ISO_800, SettingsDefinitions.ISO.ISO_1600, SettingsDefinitions.ISO.ISO_3200};
            }
        }
        return list;
    }

    /* access modifiers changed from: private */
    public void updateCameraISORange() {
        SettingsDefinitions.ISO[] list = getISORange(this.defaultKey.getIndex());
        if (list != null) {
            if (this.cameraISORange == null || !Arrays.deepEquals(this.cameraISORange, list)) {
                this.cameraISORange = list;
                if (this.onValueChangeListener != null) {
                    this.onValueChangeListener.onNewValue(list, this.defaultKey.clone(CameraKeys.ISO_RANGE));
                }
            }
        }
    }

    private static SettingsDefinitions.ExposureCompensation[] defaultExposureCompensationList() {
        return new SettingsDefinitions.ExposureCompensation[]{SettingsDefinitions.ExposureCompensation.N_3_0, SettingsDefinitions.ExposureCompensation.N_2_7, SettingsDefinitions.ExposureCompensation.N_2_3, SettingsDefinitions.ExposureCompensation.N_2_0, SettingsDefinitions.ExposureCompensation.N_1_7, SettingsDefinitions.ExposureCompensation.N_1_3, SettingsDefinitions.ExposureCompensation.N_1_0, SettingsDefinitions.ExposureCompensation.N_0_7, SettingsDefinitions.ExposureCompensation.N_0_3, SettingsDefinitions.ExposureCompensation.N_0_0, SettingsDefinitions.ExposureCompensation.P_0_3, SettingsDefinitions.ExposureCompensation.P_0_7, SettingsDefinitions.ExposureCompensation.P_1_0, SettingsDefinitions.ExposureCompensation.P_1_3, SettingsDefinitions.ExposureCompensation.P_1_7, SettingsDefinitions.ExposureCompensation.P_2_0, SettingsDefinitions.ExposureCompensation.P_2_3, SettingsDefinitions.ExposureCompensation.P_2_7, SettingsDefinitions.ExposureCompensation.P_3_0};
    }

    public static SettingsDefinitions.ExposureCompensation[] getExposureCompensationRange(int componentIndex) {
        SettingsDefinitions.ExposureCompensation[] list = null;
        if (DJIComponentManager.getInstance().isAircraftConnected()) {
            DJIComponentManager.PlatformType type = DJIComponentManager.getInstance().getPlatformType();
            SettingsDefinitions.CameraType cameraType = CameraUtils.getCameraType(componentIndex);
            if (cameraType != null) {
                if (type == null) {
                    type = DJIComponentManager.PlatformType.Unknown;
                }
                if (cameraType != SettingsDefinitions.CameraType.DJICameraTypeGD600 && cameraType != SettingsDefinitions.CameraType.DJICameraTypeFC1705 && cameraType != SettingsDefinitions.CameraType.DJIPayloadCamera) {
                    switch (type) {
                        case FoldingDrone:
                            if (cameraType != SettingsDefinitions.CameraType.DJICameraTypeFC220S) {
                                list = defaultExposureCompensationList();
                                break;
                            } else {
                                list = new SettingsDefinitions.ExposureCompensation[]{SettingsDefinitions.ExposureCompensation.N_1_0, SettingsDefinitions.ExposureCompensation.N_0_7, SettingsDefinitions.ExposureCompensation.N_0_3, SettingsDefinitions.ExposureCompensation.N_0_0, SettingsDefinitions.ExposureCompensation.P_0_3, SettingsDefinitions.ExposureCompensation.P_0_7, SettingsDefinitions.ExposureCompensation.P_1_0};
                                break;
                            }
                        case WM230:
                            if (cameraType != SettingsDefinitions.CameraType.DJICameraTypeFC230 || getExposureMode(componentIndex) != SettingsDefinitions.ExposureMode.MANUAL) {
                                list = defaultExposureCompensationList();
                                break;
                            } else {
                                list = new SettingsDefinitions.ExposureCompensation[]{SettingsDefinitions.ExposureCompensation.N_5_0, SettingsDefinitions.ExposureCompensation.N_4_7, SettingsDefinitions.ExposureCompensation.N_4_3, SettingsDefinitions.ExposureCompensation.N_4_0, SettingsDefinitions.ExposureCompensation.N_3_7, SettingsDefinitions.ExposureCompensation.N_3_3, SettingsDefinitions.ExposureCompensation.N_3_0, SettingsDefinitions.ExposureCompensation.N_2_7, SettingsDefinitions.ExposureCompensation.N_2_3, SettingsDefinitions.ExposureCompensation.N_2_0, SettingsDefinitions.ExposureCompensation.N_1_7, SettingsDefinitions.ExposureCompensation.N_1_3, SettingsDefinitions.ExposureCompensation.N_1_0, SettingsDefinitions.ExposureCompensation.N_0_7, SettingsDefinitions.ExposureCompensation.N_0_3, SettingsDefinitions.ExposureCompensation.N_0_0, SettingsDefinitions.ExposureCompensation.P_0_3, SettingsDefinitions.ExposureCompensation.P_0_7, SettingsDefinitions.ExposureCompensation.P_1_0, SettingsDefinitions.ExposureCompensation.P_1_3, SettingsDefinitions.ExposureCompensation.P_1_7, SettingsDefinitions.ExposureCompensation.P_2_0, SettingsDefinitions.ExposureCompensation.P_2_3, SettingsDefinitions.ExposureCompensation.P_2_7, SettingsDefinitions.ExposureCompensation.P_3_0, SettingsDefinitions.ExposureCompensation.P_3_3, SettingsDefinitions.ExposureCompensation.P_3_7, SettingsDefinitions.ExposureCompensation.P_4_0, SettingsDefinitions.ExposureCompensation.P_4_3, SettingsDefinitions.ExposureCompensation.P_4_7, SettingsDefinitions.ExposureCompensation.P_5_0};
                                break;
                            }
                            break;
                        case Unknown:
                            break;
                        default:
                            list = defaultExposureCompensationList();
                            break;
                    }
                } else {
                    list = new SettingsDefinitions.ExposureCompensation[]{SettingsDefinitions.ExposureCompensation.N_2_3, SettingsDefinitions.ExposureCompensation.N_2_0, SettingsDefinitions.ExposureCompensation.N_1_7, SettingsDefinitions.ExposureCompensation.N_1_3, SettingsDefinitions.ExposureCompensation.N_1_0, SettingsDefinitions.ExposureCompensation.N_0_7, SettingsDefinitions.ExposureCompensation.N_0_3, SettingsDefinitions.ExposureCompensation.N_0_0, SettingsDefinitions.ExposureCompensation.P_0_3, SettingsDefinitions.ExposureCompensation.P_0_7, SettingsDefinitions.ExposureCompensation.P_1_0, SettingsDefinitions.ExposureCompensation.P_1_3, SettingsDefinitions.ExposureCompensation.P_1_7, SettingsDefinitions.ExposureCompensation.P_2_0, SettingsDefinitions.ExposureCompensation.P_2_3};
                }
            } else {
                return null;
            }
        }
        return list;
    }

    /* access modifiers changed from: private */
    public void updateExposureCompensationRange() {
        SettingsDefinitions.ExposureCompensation[] list = getExposureCompensationRange(this.defaultKey.getIndex());
        if (list != null) {
            if (this.exposureCompensationRange == null || !Arrays.deepEquals(this.exposureCompensationRange, list)) {
                this.exposureCompensationRange = list;
                if (this.onValueChangeListener != null) {
                    this.onValueChangeListener.onNewValue(list, this.defaultKey.clone(CameraKeys.EXPOSURE_COMPENSATION_RANGE));
                }
            }
        }
    }

    public static SettingsDefinitions.ExposureMode[] getExposureModeRange(int componentIndex) {
        SettingsDefinitions.ExposureMode[] list = null;
        if (DJIComponentManager.getInstance().isAircraftConnected()) {
            SettingsDefinitions.CameraType type = CameraUtils.getCameraType(componentIndex);
            if (type == null) {
                return null;
            }
            list = CameraUtils.getExposureSensitivityMode(componentIndex) == SettingsDefinitions.ExposureSensitivityMode.EI ? new SettingsDefinitions.ExposureMode[]{SettingsDefinitions.ExposureMode.MANUAL} : (type == SettingsDefinitions.CameraType.DJICameraTypeFC220 || type == SettingsDefinitions.CameraType.DJICameraTypeFC230 || type == SettingsDefinitions.CameraType.DJICameraTypeFC1102 || type == SettingsDefinitions.CameraType.DJICameraTypeFC330X || type == SettingsDefinitions.CameraType.DJICameraTypeFC260 || type == SettingsDefinitions.CameraType.DJICameraTypeFC300S || type == SettingsDefinitions.CameraType.DJICameraTypeFC300X) ? new SettingsDefinitions.ExposureMode[]{SettingsDefinitions.ExposureMode.MANUAL, SettingsDefinitions.ExposureMode.PROGRAM} : (type == SettingsDefinitions.CameraType.DJICameraTypeFC350 || type == SettingsDefinitions.CameraType.DJICameraTypeCV600) ? new SettingsDefinitions.ExposureMode[]{SettingsDefinitions.ExposureMode.MANUAL, SettingsDefinitions.ExposureMode.PROGRAM, SettingsDefinitions.ExposureMode.SHUTTER_PRIORITY} : (type == SettingsDefinitions.CameraType.DJICameraTypeFC550Raw || type == SettingsDefinitions.CameraType.DJICameraTypeFC550 || type == SettingsDefinitions.CameraType.DJICameraTypeGD600 || type == SettingsDefinitions.CameraType.DJIPayloadCamera || type == SettingsDefinitions.CameraType.DJICameraTypeFC6310 || type == SettingsDefinitions.CameraType.DJICameraTypeFC6510 || type == SettingsDefinitions.CameraType.DJICameraTypeFC6540 || type == SettingsDefinitions.CameraType.DJICameraTypeFC6520 || type == SettingsDefinitions.CameraType.DJICameraTypeFC6310S || type == SettingsDefinitions.CameraType.DJICameraTypeFC240_1) ? new SettingsDefinitions.ExposureMode[]{SettingsDefinitions.ExposureMode.MANUAL, SettingsDefinitions.ExposureMode.PROGRAM, SettingsDefinitions.ExposureMode.SHUTTER_PRIORITY, SettingsDefinitions.ExposureMode.APERTURE_PRIORITY} : (type == SettingsDefinitions.CameraType.DJICameraTypeFC240 || type == SettingsDefinitions.CameraType.DJICameraTypeFC245_IMX477) ? new SettingsDefinitions.ExposureMode[]{SettingsDefinitions.ExposureMode.MANUAL, SettingsDefinitions.ExposureMode.PROGRAM} : (type == SettingsDefinitions.CameraType.DJICameraTypeFC1705 || type == SettingsDefinitions.CameraType.DJICameraTypeFC2403 || type == SettingsDefinitions.CameraType.DJICameraTypeFC160) ? new SettingsDefinitions.ExposureMode[]{SettingsDefinitions.ExposureMode.PROGRAM} : new SettingsDefinitions.ExposureMode[]{SettingsDefinitions.ExposureMode.MANUAL, SettingsDefinitions.ExposureMode.PROGRAM, SettingsDefinitions.ExposureMode.SHUTTER_PRIORITY};
        }
        return list;
    }

    /* access modifiers changed from: private */
    public void updateExposureModeRange() {
        SettingsDefinitions.ExposureMode[] list = getExposureModeRange(this.defaultKey.getIndex());
        if (list != null) {
            if (this.exposureModeRange == null || !Arrays.deepEquals(this.exposureModeRange, list)) {
                this.exposureModeRange = list;
                if (this.onValueChangeListener != null) {
                    this.onValueChangeListener.onNewValue(list, this.defaultKey.clone(CameraKeys.EXPOSURE_MODE_RANGE));
                }
            }
        }
    }

    public static SettingsDefinitions.CameraMode[] getCameraModeRange(int componentIndex) {
        SettingsDefinitions.CameraMode[] list = null;
        DJIComponentManager.PlatformType type = DJIComponentManager.getInstance().getPlatformType();
        SettingsDefinitions.CameraType cameraType = CameraUtils.getCameraType(componentIndex);
        if (cameraType == null) {
            return null;
        }
        switch (cameraType) {
            case DJICameraTypeFC6540:
            case DJICameraTypeFC6520:
            case DJICameraTypeFC6510:
                list = new SettingsDefinitions.CameraMode[]{SettingsDefinitions.CameraMode.SHOOT_PHOTO, SettingsDefinitions.CameraMode.RECORD_VIDEO, SettingsDefinitions.CameraMode.MEDIA_DOWNLOAD, SettingsDefinitions.CameraMode.BROADCAST};
                break;
            case DJICameraTypeFC550:
                list = new SettingsDefinitions.CameraMode[]{SettingsDefinitions.CameraMode.SHOOT_PHOTO, SettingsDefinitions.CameraMode.RECORD_VIDEO, SettingsDefinitions.CameraMode.PLAYBACK};
                break;
            case DJICameraTypeFC300X:
            case DJICameraTypeFC330X:
            case DJICameraTypeFC300XW:
            case DJICameraTypeFC550Raw:
                list = new SettingsDefinitions.CameraMode[]{SettingsDefinitions.CameraMode.SHOOT_PHOTO, SettingsDefinitions.CameraMode.RECORD_VIDEO, SettingsDefinitions.CameraMode.PLAYBACK, SettingsDefinitions.CameraMode.MEDIA_DOWNLOAD};
                break;
            case DJICameraTypeFC350:
            case DJICameraTypeCV600:
                if (type != DJIComponentManager.PlatformType.OSMO) {
                    list = new SettingsDefinitions.CameraMode[]{SettingsDefinitions.CameraMode.SHOOT_PHOTO, SettingsDefinitions.CameraMode.RECORD_VIDEO, SettingsDefinitions.CameraMode.PLAYBACK, SettingsDefinitions.CameraMode.MEDIA_DOWNLOAD};
                    break;
                } else {
                    list = new SettingsDefinitions.CameraMode[]{SettingsDefinitions.CameraMode.SHOOT_PHOTO, SettingsDefinitions.CameraMode.RECORD_VIDEO, SettingsDefinitions.CameraMode.MEDIA_DOWNLOAD};
                    break;
                }
            case DJICameraTypeFC300S:
            case DJICameraTypeFC260:
            case DJICameraTypeTau640:
            case DJICameraTypeTau336:
            case DJICameraTypeFC220:
            case DJICameraTypeFC220S:
            case DJICameraTypeFC6310:
            case DJICameraTypeFC6310S:
            case DJICameraTypeGD600:
            case DJICameraTypeFC1102:
            case DJICameraTypeFC1705:
            case DJICameraTypeFC230:
            case DJICameraTypeFC240:
            case DJICameraTypeFC2403:
            case DJICameraTypeFC240_1:
            case DJICameraTypeFC245_IMX477:
            case DJICameraTypeFC160:
                list = new SettingsDefinitions.CameraMode[]{SettingsDefinitions.CameraMode.SHOOT_PHOTO, SettingsDefinitions.CameraMode.RECORD_VIDEO, SettingsDefinitions.CameraMode.MEDIA_DOWNLOAD};
                break;
            case DJIPayloadCamera:
                list = new SettingsDefinitions.CameraMode[]{SettingsDefinitions.CameraMode.SHOOT_PHOTO, SettingsDefinitions.CameraMode.RECORD_VIDEO};
                break;
        }
        return list;
    }

    /* access modifiers changed from: private */
    public void updateCameraModeRange() {
        SettingsDefinitions.CameraMode[] list = getCameraModeRange(this.defaultKey.getIndex());
        if (list != null) {
            if (this.cameraModeRange == null || !Arrays.deepEquals(this.cameraModeRange, list)) {
                this.cameraModeRange = list;
                if (this.onValueChangeListener != null) {
                    this.onValueChangeListener.onNewValue(list, this.defaultKey.clone(CameraKeys.MODE_RANGE));
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void updateDualCameraDisplayModeRange() {
        SettingsDefinitions.DisplayMode[] list = getDualCameraDisplayModeRange(this.defaultKey.getIndex());
        if (list != null) {
            if (this.displayModeRange == null || !Arrays.deepEquals(this.displayModeRange, list)) {
                this.displayModeRange = list;
                if (this.onValueChangeListener != null) {
                    this.onValueChangeListener.onNewValue(list, this.defaultKey.clone(CameraKeys.DISPLAY_MODE_RANGE));
                }
            }
        }
    }

    public static SettingsDefinitions.DisplayMode[] getDualCameraDisplayModeRange(int componentIndex) {
        SettingsDefinitions.DisplayMode[] list = null;
        SettingsDefinitions.CameraType cameraType = CameraUtils.getCameraType(componentIndex);
        if (cameraType == null) {
            return null;
        }
        switch (cameraType) {
            case DJICameraTypeFC1705:
                list = new SettingsDefinitions.DisplayMode[]{SettingsDefinitions.DisplayMode.VISUAL_ONLY, SettingsDefinitions.DisplayMode.THERMAL_ONLY, SettingsDefinitions.DisplayMode.MSX, SettingsDefinitions.DisplayMode.PIP};
                break;
            case DJICameraTypeFC2403:
                list = new SettingsDefinitions.DisplayMode[]{SettingsDefinitions.DisplayMode.VISUAL_ONLY, SettingsDefinitions.DisplayMode.THERMAL_ONLY, SettingsDefinitions.DisplayMode.MSX};
                break;
        }
        return list;
    }

    /* access modifiers changed from: private */
    public void updateThermalPaletteRange() {
        SettingsDefinitions.ThermalPalette[] list = getThermalPaletteRange(this.defaultKey.getIndex());
        if (list != null) {
            if (this.thermalPaletteRange == null || !Arrays.deepEquals(this.thermalPaletteRange, list)) {
                this.thermalPaletteRange = list;
                if (this.onValueChangeListener != null) {
                    this.onValueChangeListener.onNewValue(list, this.defaultKey.clone(CameraKeys.THERMAL_PALETTE_RANGE));
                }
            }
        }
    }

    public static SettingsDefinitions.ThermalPalette[] getThermalPaletteRange(int componentIndex) {
        SettingsDefinitions.ThermalPalette[] list = null;
        SettingsDefinitions.CameraType cameraType = CameraUtils.getCameraType(componentIndex);
        if (cameraType == null) {
            return null;
        }
        switch (cameraType) {
            case DJICameraTypeFC1705:
                list = new SettingsDefinitions.ThermalPalette[]{SettingsDefinitions.ThermalPalette.WHITE_HOT, SettingsDefinitions.ThermalPalette.BLACK_HOT, SettingsDefinitions.ThermalPalette.FUSION, SettingsDefinitions.ThermalPalette.RAINBOW, SettingsDefinitions.ThermalPalette.IRONBOW_1, SettingsDefinitions.ThermalPalette.ICE_FIRE, SettingsDefinitions.ThermalPalette.SEPIA, SettingsDefinitions.ThermalPalette.GLOWBOW, SettingsDefinitions.ThermalPalette.IRONBOW_2, SettingsDefinitions.ThermalPalette.COLOR_1, SettingsDefinitions.ThermalPalette.COLOR_2, SettingsDefinitions.ThermalPalette.RAIN, SettingsDefinitions.ThermalPalette.RED_HOT, SettingsDefinitions.ThermalPalette.GREEN_HOT, SettingsDefinitions.ThermalPalette.ARCTIC};
                break;
            case DJICameraTypeFC2403:
                list = new SettingsDefinitions.ThermalPalette[]{SettingsDefinitions.ThermalPalette.HOT_SPOT, SettingsDefinitions.ThermalPalette.RAINBOW2, SettingsDefinitions.ThermalPalette.GRAY, SettingsDefinitions.ThermalPalette.HOT_METAL, SettingsDefinitions.ThermalPalette.COLD_SPOT};
                break;
        }
        return list;
    }

    private static boolean isProductOrange(ProductType type) {
        if (type == null) {
            type = DJIProductManager.getInstance().getType();
        }
        if (type == ProductType.Orange || type == ProductType.N1 || type == ProductType.BigBanana || type == ProductType.Olives || type == ProductType.OrangeRAW || type == ProductType.OrangeCV600 || type == ProductType.Orange2 || CommonUtil.isM200Product(type)) {
            return true;
        }
        return false;
    }

    private boolean isTripodMode() {
        return FlightMode.TRIPOD == ((FlightMode) CacheHelper.getFlightController(FlightControllerKeys.FLIGHT_MODE));
    }

    private boolean isInTapFlyMode() {
        return FlightMode.TAP_FLY == ((FlightMode) CacheHelper.getFlightController(FlightControllerKeys.FLIGHT_MODE));
    }

    private boolean isInPoiMode() {
        return FlightMode.POI2 == ((FlightMode) CacheHelper.getFlightController(FlightControllerKeys.FLIGHT_MODE));
    }

    /* access modifiers changed from: private */
    public void updateShootPhotoModeRange() {
        SettingsDefinitions.ShootPhotoMode[] list;
        ProductType product = DJIProductManager.getInstance().getType();
        SettingsDefinitions.CameraType cameraType = CameraUtils.getCameraType(this.defaultKey.getIndex());
        if (cameraType != null) {
            if (CameraUtils.isGDCamera(cameraType) || cameraType == SettingsDefinitions.CameraType.DJIPayloadCamera) {
                list = new SettingsDefinitions.ShootPhotoMode[]{SettingsDefinitions.ShootPhotoMode.SINGLE, SettingsDefinitions.ShootPhotoMode.BURST, SettingsDefinitions.ShootPhotoMode.INTERVAL};
            } else if (CameraUtils.isXTCamera(cameraType) || cameraType == SettingsDefinitions.CameraType.DJICameraTypeFC160) {
                list = new SettingsDefinitions.ShootPhotoMode[]{SettingsDefinitions.ShootPhotoMode.SINGLE, SettingsDefinitions.ShootPhotoMode.INTERVAL};
            } else if (cameraType == SettingsDefinitions.CameraType.DJICameraTypeFC1705 || cameraType == SettingsDefinitions.CameraType.DJICameraTypeFC2403) {
                list = new SettingsDefinitions.ShootPhotoMode[]{SettingsDefinitions.ShootPhotoMode.SINGLE, SettingsDefinitions.ShootPhotoMode.BURST, SettingsDefinitions.ShootPhotoMode.INTERVAL};
            } else if (isProductOrange(product)) {
                list = ProductType.OrangeCV600 == product ? new SettingsDefinitions.ShootPhotoMode[]{SettingsDefinitions.ShootPhotoMode.SINGLE, SettingsDefinitions.ShootPhotoMode.HDR, SettingsDefinitions.ShootPhotoMode.BURST, SettingsDefinitions.ShootPhotoMode.AEB, SettingsDefinitions.ShootPhotoMode.INTERVAL} : DJICameraSupportUtil.supportRawBurst(DataCameraGetPushStateInfo.CameraType.find(cameraType.value())) ? new SettingsDefinitions.ShootPhotoMode[]{SettingsDefinitions.ShootPhotoMode.SINGLE, SettingsDefinitions.ShootPhotoMode.BURST, SettingsDefinitions.ShootPhotoMode.AEB, SettingsDefinitions.ShootPhotoMode.INTERVAL, SettingsDefinitions.ShootPhotoMode.RAW_BURST} : SettingsDefinitions.CameraType.DJICameraTypeFC6510 == cameraType ? new SettingsDefinitions.ShootPhotoMode[]{SettingsDefinitions.ShootPhotoMode.SINGLE, SettingsDefinitions.ShootPhotoMode.BURST, SettingsDefinitions.ShootPhotoMode.AEB, SettingsDefinitions.ShootPhotoMode.INTERVAL} : (CameraUtils.supportCameraManualFocus(cameraType) || cameraType == SettingsDefinitions.CameraType.DJICameraTypeFC6540 || DataCameraGetPushStateInfo.getInstance().getVerstion(getExpectedSenderIdByIndex()) < 1) ? new SettingsDefinitions.ShootPhotoMode[]{SettingsDefinitions.ShootPhotoMode.SINGLE, SettingsDefinitions.ShootPhotoMode.BURST, SettingsDefinitions.ShootPhotoMode.AEB, SettingsDefinitions.ShootPhotoMode.INTERVAL} : new SettingsDefinitions.ShootPhotoMode[]{SettingsDefinitions.ShootPhotoMode.SINGLE, SettingsDefinitions.ShootPhotoMode.HDR, SettingsDefinitions.ShootPhotoMode.BURST, SettingsDefinitions.ShootPhotoMode.AEB, SettingsDefinitions.ShootPhotoMode.INTERVAL};
            } else {
                ProductType pType = DJIProductManager.getInstance().getType();
                list = (CommonUtil.isProductM600Series(pType) || pType == ProductType.A3 || pType == ProductType.N3) ? new SettingsDefinitions.ShootPhotoMode[]{SettingsDefinitions.ShootPhotoMode.SINGLE, SettingsDefinitions.ShootPhotoMode.BURST, SettingsDefinitions.ShootPhotoMode.AEB, SettingsDefinitions.ShootPhotoMode.INTERVAL} : product == ProductType.KumquatX ? new SettingsDefinitions.ShootPhotoMode[]{SettingsDefinitions.ShootPhotoMode.SINGLE, SettingsDefinitions.ShootPhotoMode.HDR, SettingsDefinitions.ShootPhotoMode.BURST, SettingsDefinitions.ShootPhotoMode.AEB, SettingsDefinitions.ShootPhotoMode.INTERVAL} : (product == ProductType.Pomato || product == ProductType.KumquatS || product == ProductType.PomatoSDR || product == ProductType.PomatoRTK || product == ProductType.Potato) ? new SettingsDefinitions.ShootPhotoMode[]{SettingsDefinitions.ShootPhotoMode.SINGLE, SettingsDefinitions.ShootPhotoMode.BURST, SettingsDefinitions.ShootPhotoMode.AEB, SettingsDefinitions.ShootPhotoMode.INTERVAL} : (product == ProductType.Mammoth || product == ProductType.WM230) ? isTripodMode() ? new SettingsDefinitions.ShootPhotoMode[]{SettingsDefinitions.ShootPhotoMode.SINGLE, SettingsDefinitions.ShootPhotoMode.BURST, SettingsDefinitions.ShootPhotoMode.AEB, SettingsDefinitions.ShootPhotoMode.INTERVAL} : new SettingsDefinitions.ShootPhotoMode[]{SettingsDefinitions.ShootPhotoMode.SINGLE, SettingsDefinitions.ShootPhotoMode.BURST, SettingsDefinitions.ShootPhotoMode.AEB, SettingsDefinitions.ShootPhotoMode.INTERVAL, SettingsDefinitions.ShootPhotoMode.SHALLOW_FOCUS, SettingsDefinitions.ShootPhotoMode.PANORAMA} : (product == ProductType.WM240 || cameraType == SettingsDefinitions.CameraType.DJICameraTypeFC245_IMX477) ? (isTripodMode() || isInTapFlyMode() || isInPoiMode()) ? new SettingsDefinitions.ShootPhotoMode[]{SettingsDefinitions.ShootPhotoMode.SINGLE, SettingsDefinitions.ShootPhotoMode.BURST, SettingsDefinitions.ShootPhotoMode.AEB, SettingsDefinitions.ShootPhotoMode.INTERVAL, SettingsDefinitions.ShootPhotoMode.EHDR, SettingsDefinitions.ShootPhotoMode.HYPER_LIGHT} : getTrackingMode(this.defaultKey.getIndex()) ? new SettingsDefinitions.ShootPhotoMode[]{SettingsDefinitions.ShootPhotoMode.SINGLE} : new SettingsDefinitions.ShootPhotoMode[]{SettingsDefinitions.ShootPhotoMode.SINGLE, SettingsDefinitions.ShootPhotoMode.BURST, SettingsDefinitions.ShootPhotoMode.AEB, SettingsDefinitions.ShootPhotoMode.INTERVAL, SettingsDefinitions.ShootPhotoMode.EHDR, SettingsDefinitions.ShootPhotoMode.HYPER_LIGHT, SettingsDefinitions.ShootPhotoMode.PANORAMA} : new SettingsDefinitions.ShootPhotoMode[]{SettingsDefinitions.ShootPhotoMode.SINGLE, SettingsDefinitions.ShootPhotoMode.HDR, SettingsDefinitions.ShootPhotoMode.BURST, SettingsDefinitions.ShootPhotoMode.AEB, SettingsDefinitions.ShootPhotoMode.INTERVAL};
            }
            if (list == null) {
                return;
            }
            if (this.shootPhotoModeRange == null || !Arrays.deepEquals(this.shootPhotoModeRange, list)) {
                this.shootPhotoModeRange = list;
                if (this.onValueChangeListener != null) {
                    this.onValueChangeListener.onNewValue(list, this.defaultKey.clone(CameraKeys.SHOOT_PHOTO_MODE_RANGE));
                }
            }
        }
    }

    private boolean areDifferent2Degrees(int[][] a, int[][] b) {
        if (a == null && b == null) {
            return false;
        }
        if (a == null || b == null || a.length != b.length) {
            return true;
        }
        for (int i = 0; i < b.length; i++) {
            if (isDifferent(a[i], b[i])) {
                return true;
            }
        }
        return false;
    }

    private boolean isDifferent(int[] a, int[] b) {
        if (a == null && b == null) {
            return false;
        }
        if (a == null || b == null || a.length != b.length) {
            return true;
        }
        for (int i = 0; i < b.length; i++) {
            if (a[i] != b[i]) {
                return true;
            }
        }
        return false;
    }

    private boolean isH1CameraForType(SettingsDefinitions.CameraType cameraType) {
        return cameraType == SettingsDefinitions.CameraType.DJICameraTypeFC6310 || cameraType == SettingsDefinitions.CameraType.DJICameraTypeFC6510 || cameraType == SettingsDefinitions.CameraType.DJICameraTypeFC6520 || cameraType == SettingsDefinitions.CameraType.DJICameraTypeFC6540 || cameraType == SettingsDefinitions.CameraType.DJICameraTypeFC6310S;
    }

    private int[] getPanoramaModeRangeList(SettingsDefinitions.CameraType cameraType) {
        if (!CameraUtils.isProductSupportPanoSphere()) {
            return new int[]{SettingsDefinitions.PhotoPanoramaMode.PANORAMA_MODE_3X1.value(), SettingsDefinitions.PhotoPanoramaMode.PANORAMA_MODE_3X3.value(), SettingsDefinitions.PhotoPanoramaMode.PANORAMA_MODE_180.value()};
        } else if (isSupportSRPhoto(cameraType)) {
            return new int[]{SettingsDefinitions.PhotoPanoramaMode.PANORAMA_MODE_3X1.value(), SettingsDefinitions.PhotoPanoramaMode.PANORAMA_MODE_3X3.value(), SettingsDefinitions.PhotoPanoramaMode.PANORAMA_MODE_SPHERE.value(), SettingsDefinitions.PhotoPanoramaMode.PANORAMA_MODE_180.value(), SettingsDefinitions.PhotoPanoramaMode.PANORAMA_MODE_SUPER_RESOLUTION.value()};
        } else {
            return new int[]{SettingsDefinitions.PhotoPanoramaMode.PANORAMA_MODE_3X1.value(), SettingsDefinitions.PhotoPanoramaMode.PANORAMA_MODE_3X3.value(), SettingsDefinitions.PhotoPanoramaMode.PANORAMA_MODE_SPHERE.value(), SettingsDefinitions.PhotoPanoramaMode.PANORAMA_MODE_180.value()};
        }
    }

    private boolean isSupportSRPhoto(SettingsDefinitions.CameraType cameraType) {
        return cameraType == SettingsDefinitions.CameraType.DJICameraTypeFC240 || cameraType == SettingsDefinitions.CameraType.DJICameraTypeFC245_IMX477;
    }

    @NonNull
    private int[] getBurstList(SettingsDefinitions.CameraType cameraType) {
        if (cameraType == SettingsDefinitions.CameraType.DJICameraTypeFC220S || CameraUtils.isGDCamera(cameraType) || cameraType == SettingsDefinitions.CameraType.DJICameraTypeFC1705 || cameraType == SettingsDefinitions.CameraType.DJICameraTypeFC240_1) {
            return new int[]{3, 5};
        }
        if (cameraType == SettingsDefinitions.CameraType.DJIPayloadCamera) {
            return new int[]{2, 3, 5, 7, 10};
        }
        if (isH1CameraForType(cameraType)) {
            return new int[]{3, 5, 7, 10, 14};
        }
        if (cameraType != SettingsDefinitions.CameraType.DJICameraTypeFC1102) {
            return new int[]{3, 5, 7};
        }
        return new int[]{3};
    }

    /* access modifiers changed from: private */
    public void updateShootPhotoModeChildRange() {
        int[] aebList;
        int[][] newValue = new int[SettingsDefinitions.ShootPhotoMode.values().length][];
        SettingsDefinitions.CameraType cameraType = CameraUtils.getCameraType(this.defaultKey.getIndex());
        if (cameraType != null) {
            newValue[SettingsDefinitions.ShootPhotoMode.BURST.value()] = getBurstList(cameraType);
            if (cameraType == SettingsDefinitions.CameraType.DJICameraTypeFC220S || cameraType == SettingsDefinitions.CameraType.DJICameraTypeFC1102) {
                aebList = new int[]{3};
            } else if (SettingsDefinitions.CameraType.DJICameraTypeFC6310 == cameraType || SettingsDefinitions.CameraType.DJICameraTypeFC6310S == cameraType) {
                aebList = new int[]{3, 5};
            } else {
                aebList = new int[]{3, 5};
            }
            newValue[SettingsDefinitions.ShootPhotoMode.AEB.value()] = aebList;
            SettingsDefinitions.PhotoFileFormat imageFormat = getImageFormat(this.defaultKey.getIndex());
            if (imageFormat != null) {
                newValue[SettingsDefinitions.ShootPhotoMode.INTERVAL.value()] = getIntervalTimeRange(imageFormat, cameraType);
                newValue[SettingsDefinitions.ShootPhotoMode.PANORAMA.value()] = getPanoramaModeRangeList(cameraType);
                newValue[SettingsDefinitions.ShootPhotoMode.RAW_BURST.value()] = getBurstList(cameraType);
                if (areDifferent2Degrees(this.shootPhotoModeChildRange, newValue)) {
                    this.shootPhotoModeChildRange = newValue;
                    if (this.onValueChangeListener != null) {
                        this.onValueChangeListener.onNewValue(newValue, this.defaultKey.clone(CameraKeys.SHOOT_PHOTO_MODE_CHILD_RANGE));
                    }
                }
            }
        }
    }

    public static int[] getIntervalTimeRange(SettingsDefinitions.PhotoFileFormat imageFormat, SettingsDefinitions.CameraType cameraType) {
        if (imageFormat == SettingsDefinitions.PhotoFileFormat.JPEG) {
            if (cameraType == SettingsDefinitions.CameraType.DJICameraTypeFC220 || cameraType == SettingsDefinitions.CameraType.DJICameraTypeFC330X || cameraType == SettingsDefinitions.CameraType.DJICameraTypeFC230 || cameraType == SettingsDefinitions.CameraType.DJICameraTypeFC240 || cameraType == SettingsDefinitions.CameraType.DJICameraTypeFC240_1 || cameraType == SettingsDefinitions.CameraType.DJICameraTypeFC245_IMX477 || cameraType == SettingsDefinitions.CameraType.DJICameraTypeFC1102 || cameraType == SettingsDefinitions.CameraType.DJICameraTypeFC6510 || cameraType == SettingsDefinitions.CameraType.DJICameraTypeFC6540 || cameraType == SettingsDefinitions.CameraType.DJICameraTypeFC6520 || cameraType == SettingsDefinitions.CameraType.DJICameraTypeFC6310 || cameraType == SettingsDefinitions.CameraType.DJICameraTypeFC2403 || cameraType == SettingsDefinitions.CameraType.DJICameraTypeFC6310S || cameraType == SettingsDefinitions.CameraType.DJICameraTypeFC160) {
                return new int[]{2, 3, 5, 7, 10, 15, 20, 30, 60};
            }
            if (CameraUtils.isGDCamera(cameraType)) {
                return new int[]{2, 3, 4, 7, 10, 15, 20, 30};
            }
            if (cameraType == SettingsDefinitions.CameraType.DJIPayloadCamera) {
                return new int[]{1, 3, 5, 7, 10, 15, 20, 30};
            }
            if (cameraType == SettingsDefinitions.CameraType.DJICameraTypeFC1705 || cameraType == SettingsDefinitions.CameraType.DJICameraTypeFC2403 || cameraType == SettingsDefinitions.CameraType.DJICameraTypeTau640 || cameraType == SettingsDefinitions.CameraType.DJICameraTypeTau336) {
                return new int[]{2, 3, 5, 7, 10, 15, 20, 30};
            }
            if (DataOsdGetPushCommon.getInstance().getDroneType() == DataOsdGetPushCommon.DroneType.PomatoRTK) {
                return new int[]{3, 5, 7, 10, 15, 20, 30, 60};
            }
            return new int[]{2, 3, 5, 7, 10, 20, 30};
        } else if (cameraType == SettingsDefinitions.CameraType.DJICameraTypeFC220 || cameraType == SettingsDefinitions.CameraType.DJICameraTypeFC330X || cameraType == SettingsDefinitions.CameraType.DJICameraTypeFC230) {
            return new int[]{10, 15, 20, 30, 60};
        } else {
            if (SettingsDefinitions.CameraType.DJICameraTypeFC6310 == cameraType) {
                return new int[]{5, 7, 10, 15, 30, 60};
            }
            if (SettingsDefinitions.CameraType.DJICameraTypeFC6510 == cameraType || SettingsDefinitions.CameraType.DJICameraTypeFC6520 == cameraType || SettingsDefinitions.CameraType.DJICameraTypeFC6540 == cameraType || SettingsDefinitions.CameraType.DJICameraTypeFC240 == cameraType || SettingsDefinitions.CameraType.DJICameraTypeFC245_IMX477 == cameraType || SettingsDefinitions.CameraType.DJICameraTypeFC6310S == cameraType || SettingsDefinitions.CameraType.DJICameraTypeFC240_1 == cameraType) {
                return new int[]{5, 7, 10, 15, 20, 30, 60};
            }
            if (CameraUtils.isGDCamera(cameraType)) {
                return new int[]{2, 3, 4, 7, 10, 15, 20, 30};
            }
            if (cameraType == SettingsDefinitions.CameraType.DJIPayloadCamera) {
                return new int[]{1, 3, 5, 7, 10, 15, 20, 30};
            }
            if (cameraType == SettingsDefinitions.CameraType.DJICameraTypeFC1705 || cameraType == SettingsDefinitions.CameraType.DJICameraTypeTau640 || cameraType == SettingsDefinitions.CameraType.DJICameraTypeTau336) {
                return new int[]{2, 3, 5, 7, 10, 15, 20, 30};
            }
            return new int[]{10, 20, 30};
        }
    }

    /* access modifiers changed from: private */
    public void updateCameraPhotoFileFormatRange() {
        SettingsDefinitions.CameraType type;
        SettingsDefinitions.PhotoFileFormat[] list;
        boolean isRadiometricJPEGSupported;
        boolean isRVersion;
        if (DJIComponentManager.getInstance().isAircraftConnected() && (type = CameraUtils.getCameraType(this.defaultKey.getIndex())) != null) {
            Boolean isInTrackingMode = Boolean.valueOf(getTrackingMode(this.defaultKey.getIndex()));
            if (isInTrackingMode != null && isInTrackingMode.booleanValue()) {
                list = new SettingsDefinitions.PhotoFileFormat[]{SettingsDefinitions.PhotoFileFormat.JPEG};
            } else if (type == SettingsDefinitions.CameraType.DJICameraTypeFC220S || type == SettingsDefinitions.CameraType.DJICameraTypeGD600 || type == SettingsDefinitions.CameraType.DJICameraTypeFC1102 || type == SettingsDefinitions.CameraType.DJIPayloadCamera || type == SettingsDefinitions.CameraType.DJICameraTypeFC160) {
                list = new SettingsDefinitions.PhotoFileFormat[]{SettingsDefinitions.PhotoFileFormat.JPEG};
            } else if (type == SettingsDefinitions.CameraType.DJICameraTypeTau336 || type == SettingsDefinitions.CameraType.DJICameraTypeTau640) {
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
                boolean isTIFFLowHighSupported = DataCameraGetPushTauParam.getInstance().supportSpotThermometric(getExpectedSenderIdByIndex());
                list = isRadiometricJPEGSupported ? (isRVersion || !isTIFFLowHighSupported) ? new SettingsDefinitions.PhotoFileFormat[]{SettingsDefinitions.PhotoFileFormat.JPEG, SettingsDefinitions.PhotoFileFormat.TIFF_14_BIT, SettingsDefinitions.PhotoFileFormat.RADIOMETRIC_JPEG} : new SettingsDefinitions.PhotoFileFormat[]{SettingsDefinitions.PhotoFileFormat.JPEG, SettingsDefinitions.PhotoFileFormat.TIFF_14_BIT, SettingsDefinitions.PhotoFileFormat.TIFF_14_BIT_LINEAR_LOW_TEMP_RESOLUTION, SettingsDefinitions.PhotoFileFormat.TIFF_14_BIT_LINEAR_HIGH_TEMP_RESOLUTION, SettingsDefinitions.PhotoFileFormat.RADIOMETRIC_JPEG} : isTIFFLowHighSupported ? new SettingsDefinitions.PhotoFileFormat[]{SettingsDefinitions.PhotoFileFormat.JPEG, SettingsDefinitions.PhotoFileFormat.TIFF_14_BIT, SettingsDefinitions.PhotoFileFormat.TIFF_14_BIT_LINEAR_LOW_TEMP_RESOLUTION, SettingsDefinitions.PhotoFileFormat.TIFF_14_BIT_LINEAR_HIGH_TEMP_RESOLUTION} : new SettingsDefinitions.PhotoFileFormat[]{SettingsDefinitions.PhotoFileFormat.JPEG, SettingsDefinitions.PhotoFileFormat.TIFF_14_BIT};
            } else {
                list = type == SettingsDefinitions.CameraType.DJICameraTypeFC1705 ? this.defaultKey.getIndex() == 2 ? new SettingsDefinitions.PhotoFileFormat[]{SettingsDefinitions.PhotoFileFormat.JPEG, SettingsDefinitions.PhotoFileFormat.TIFF_14_BIT, SettingsDefinitions.PhotoFileFormat.RADIOMETRIC_JPEG} : new SettingsDefinitions.PhotoFileFormat[]{SettingsDefinitions.PhotoFileFormat.JPEG} : type == SettingsDefinitions.CameraType.DJICameraTypeFC2403 ? new SettingsDefinitions.PhotoFileFormat[]{SettingsDefinitions.PhotoFileFormat.JPEG} : new SettingsDefinitions.PhotoFileFormat[]{SettingsDefinitions.PhotoFileFormat.RAW, SettingsDefinitions.PhotoFileFormat.JPEG, SettingsDefinitions.PhotoFileFormat.RAW_AND_JPEG};
            }
            if (list == null) {
                return;
            }
            if (this.photoFileFormatRange == null || !Arrays.deepEquals(this.photoFileFormatRange, list)) {
                this.photoFileFormatRange = list;
                if (this.onValueChangeListener != null) {
                    this.onValueChangeListener.onNewValue(list, this.defaultKey.clone(CameraKeys.PHOTO_FILE_FORMAT_RANGE));
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void updateCameraPanoOriginalImagesFormatRange() {
        SettingsDefinitions.CameraType type;
        SettingsDefinitions.PhotoFileFormat[] list = null;
        if (DJIComponentManager.getInstance().isAircraftConnected() && (type = CameraUtils.getCameraType(this.defaultKey.getIndex())) != null) {
            switch (type) {
                case DJICameraTypeFC240:
                case DJICameraTypeFC240_1:
                case DJICameraTypeFC245_IMX477:
                    list = new SettingsDefinitions.PhotoFileFormat[]{SettingsDefinitions.PhotoFileFormat.RAW, SettingsDefinitions.PhotoFileFormat.JPEG};
                    break;
            }
            if (list == null) {
                return;
            }
            if (this.panoOriginalImagesFormatRange == null || !Arrays.deepEquals(this.panoOriginalImagesFormatRange, list)) {
                this.panoOriginalImagesFormatRange = list;
                if (this.onValueChangeListener != null) {
                    this.onValueChangeListener.onNewValue(list, this.defaultKey.clone(CameraKeys.PANO_ORIGINAL_IMAGES_FORMAT_RANGE));
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void updateCameraHyperlapseOriginalImagesFormatRange() {
        SettingsDefinitions.CameraType type;
        SettingsDefinitions.PhotoFileFormat[] list = null;
        if (DJIComponentManager.getInstance().isAircraftConnected() && (type = CameraUtils.getCameraType(this.defaultKey.getIndex())) != null) {
            switch (type) {
                case DJICameraTypeFC240:
                case DJICameraTypeFC240_1:
                case DJICameraTypeFC245_IMX477:
                    list = new SettingsDefinitions.PhotoFileFormat[]{SettingsDefinitions.PhotoFileFormat.RAW, SettingsDefinitions.PhotoFileFormat.JPEG};
                    break;
            }
            if (list == null) {
                return;
            }
            if (this.hyperlapseOriginalImagesFormatRange == null || !Arrays.deepEquals(this.hyperlapseOriginalImagesFormatRange, list)) {
                this.hyperlapseOriginalImagesFormatRange = list;
                if (this.onValueChangeListener != null) {
                    this.onValueChangeListener.onNewValue(list, this.defaultKey.clone(CameraKeys.HYPERLAPSE_ORIGINAL_IMAGES_FORMAT_RANGE));
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void updateCameraWhiteBalancePresentRange() {
        SettingsDefinitions.CameraType type;
        SettingsDefinitions.WhiteBalancePreset[] list;
        if (DJIComponentManager.getInstance().isAircraftConnected() && (type = CameraUtils.getCameraType(this.defaultKey.getIndex())) != null) {
            if (type == SettingsDefinitions.CameraType.DJICameraTypeFC220S || type == SettingsDefinitions.CameraType.DJICameraTypeFC1102) {
                list = new SettingsDefinitions.WhiteBalancePreset[]{SettingsDefinitions.WhiteBalancePreset.AUTO, SettingsDefinitions.WhiteBalancePreset.SUNNY, SettingsDefinitions.WhiteBalancePreset.CLOUDY, SettingsDefinitions.WhiteBalancePreset.INDOOR_INCANDESCENT, SettingsDefinitions.WhiteBalancePreset.INDOOR_FLUORESCENT, SettingsDefinitions.WhiteBalancePreset.CUSTOM};
            } else if (type == SettingsDefinitions.CameraType.DJICameraTypeTau336 || type == SettingsDefinitions.CameraType.DJICameraTypeTau640 || type == SettingsDefinitions.CameraType.DJICameraTypeFC1705 || type == SettingsDefinitions.CameraType.DJICameraTypeFC2403) {
                list = new SettingsDefinitions.WhiteBalancePreset[0];
            } else {
                list = (type == SettingsDefinitions.CameraType.DJICameraTypeGD600 || type == SettingsDefinitions.CameraType.DJIPayloadCamera) ? new SettingsDefinitions.WhiteBalancePreset[]{SettingsDefinitions.WhiteBalancePreset.AUTO, SettingsDefinitions.WhiteBalancePreset.SUNNY, SettingsDefinitions.WhiteBalancePreset.CLOUDY, SettingsDefinitions.WhiteBalancePreset.INDOOR_INCANDESCENT} : type == SettingsDefinitions.CameraType.DJICameraTypeFC6540 ? new SettingsDefinitions.WhiteBalancePreset[]{SettingsDefinitions.WhiteBalancePreset.PRESET_NEUTRAL, SettingsDefinitions.WhiteBalancePreset.AUTO, SettingsDefinitions.WhiteBalancePreset.SUNNY, SettingsDefinitions.WhiteBalancePreset.CLOUDY, SettingsDefinitions.WhiteBalancePreset.INDOOR_INCANDESCENT, SettingsDefinitions.WhiteBalancePreset.INDOOR_FLUORESCENT, SettingsDefinitions.WhiteBalancePreset.CUSTOM} : type == SettingsDefinitions.CameraType.DJICameraTypeFC160 ? new SettingsDefinitions.WhiteBalancePreset[]{SettingsDefinitions.WhiteBalancePreset.AUTO, SettingsDefinitions.WhiteBalancePreset.CUSTOM} : new SettingsDefinitions.WhiteBalancePreset[]{SettingsDefinitions.WhiteBalancePreset.AUTO, SettingsDefinitions.WhiteBalancePreset.SUNNY, SettingsDefinitions.WhiteBalancePreset.CLOUDY, SettingsDefinitions.WhiteBalancePreset.INDOOR_INCANDESCENT, SettingsDefinitions.WhiteBalancePreset.INDOOR_FLUORESCENT, SettingsDefinitions.WhiteBalancePreset.CUSTOM};
            }
            if (list == null) {
                return;
            }
            if (this.whiteBalancePresentRange == null || !Arrays.deepEquals(this.whiteBalancePresentRange, list)) {
                this.whiteBalancePresentRange = list;
                if (this.onValueChangeListener != null) {
                    this.onValueChangeListener.onNewValue(list, this.defaultKey.clone(CameraKeys.WHITE_BALANCE_PRESENT_RANGE));
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void updateCameraWhiteBalanceCustomColorTemperatureRange() {
        SettingsDefinitions.CameraType type;
        int[] list;
        if (DJIComponentManager.getInstance().isAircraftConnected() && (type = CameraUtils.getCameraType(this.defaultKey.getIndex())) != null) {
            if (type == SettingsDefinitions.CameraType.DJICameraTypeFC220S) {
                list = new int[]{28, 70};
            } else if (type == SettingsDefinitions.CameraType.DJICameraTypeFC1102) {
                list = new int[]{28, 100};
            } else {
                list = new int[]{20, 100};
            }
            if (list != null && isDifferent(this.whiteBalanceCustomColorTemperatureRange, list)) {
                this.whiteBalanceCustomColorTemperatureRange = list;
                if (this.onValueChangeListener != null) {
                    this.onValueChangeListener.onNewValue(list, this.defaultKey.clone(CameraKeys.WHITE_BALANCE_CUSTOM_COLOR_TEMPERATURE_RANGE));
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void updateCameraPhotoAspectRatioRange() {
        SettingsDefinitions.CameraType type;
        if (DJIComponentManager.getInstance().isAircraftConnected() && (type = CameraUtils.getCameraType(this.defaultKey.getIndex())) != null) {
            Boolean isTrackingMode = Boolean.valueOf(getTrackingMode(this.defaultKey.getIndex()));
            SettingsDefinitions.PhotoAspectRatio[] list = (isTrackingMode == null || !isTrackingMode.booleanValue()) ? (type == SettingsDefinitions.CameraType.DJICameraTypeFC6310 || type == SettingsDefinitions.CameraType.DJICameraTypeFC6310S || type == SettingsDefinitions.CameraType.DJICameraTypeFC6510) ? new SettingsDefinitions.PhotoAspectRatio[]{SettingsDefinitions.PhotoAspectRatio.RATIO_4_3, SettingsDefinitions.PhotoAspectRatio.RATIO_16_9, SettingsDefinitions.PhotoAspectRatio.RATIO_3_2} : (type == SettingsDefinitions.CameraType.DJICameraTypeGD600 || type == SettingsDefinitions.CameraType.DJIPayloadCamera) ? new SettingsDefinitions.PhotoAspectRatio[]{SettingsDefinitions.PhotoAspectRatio.RATIO_16_9} : (type == SettingsDefinitions.CameraType.DJICameraTypeFC1102 || type == SettingsDefinitions.CameraType.DJICameraTypeFC1705) ? new SettingsDefinitions.PhotoAspectRatio[]{SettingsDefinitions.PhotoAspectRatio.RATIO_4_3} : type == SettingsDefinitions.CameraType.DJICameraTypeFC2403 ? new SettingsDefinitions.PhotoAspectRatio[]{SettingsDefinitions.PhotoAspectRatio.RATIO_4_3, SettingsDefinitions.PhotoAspectRatio.RATIO_16_9} : (type == SettingsDefinitions.CameraType.DJICameraTypeFC240 || type == SettingsDefinitions.CameraType.DJICameraTypeFC245_IMX477) ? new SettingsDefinitions.PhotoAspectRatio[]{SettingsDefinitions.PhotoAspectRatio.RATIO_16_9, SettingsDefinitions.PhotoAspectRatio.RATIO_4_3} : type == SettingsDefinitions.CameraType.DJICameraTypeFC240_1 ? new SettingsDefinitions.PhotoAspectRatio[]{SettingsDefinitions.PhotoAspectRatio.RATIO_16_9, SettingsDefinitions.PhotoAspectRatio.RATIO_3_2} : new SettingsDefinitions.PhotoAspectRatio[]{SettingsDefinitions.PhotoAspectRatio.RATIO_4_3, SettingsDefinitions.PhotoAspectRatio.RATIO_16_9} : new SettingsDefinitions.PhotoAspectRatio[]{SettingsDefinitions.PhotoAspectRatio.RATIO_16_9};
            if (list == null) {
                return;
            }
            if (this.photoAspectRatioRange == null || !Arrays.deepEquals(this.photoAspectRatioRange, list)) {
                this.photoAspectRatioRange = list;
                if (this.onValueChangeListener != null) {
                    this.onValueChangeListener.onNewValue(list, this.defaultKey.clone(CameraKeys.PHOTO_ASPECT_RATIO_RANGE));
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void updateVideoFileFormatRange() {
        SettingsDefinitions.CameraType type;
        if (DJIComponentManager.getInstance().isAircraftConnected() && (type = CameraUtils.getCameraType(this.defaultKey.getIndex())) != null) {
            SettingsDefinitions.VideoFileFormat[] list = (type == SettingsDefinitions.CameraType.DJICameraTypeFC220S || type == SettingsDefinitions.CameraType.DJICameraTypeTau336 || type == SettingsDefinitions.CameraType.DJICameraTypeTau640 || type == SettingsDefinitions.CameraType.DJICameraTypeFC1102 || type == SettingsDefinitions.CameraType.DJICameraTypeFC160) ? new SettingsDefinitions.VideoFileFormat[]{SettingsDefinitions.VideoFileFormat.MP4} : type == SettingsDefinitions.CameraType.DJICameraTypeFC1705 ? this.defaultKey.getIndex() == 2 ? new SettingsDefinitions.VideoFileFormat[]{SettingsDefinitions.VideoFileFormat.MP4, SettingsDefinitions.VideoFileFormat.MOV, SettingsDefinitions.VideoFileFormat.TIFF_SEQ, SettingsDefinitions.VideoFileFormat.SEQ} : new SettingsDefinitions.VideoFileFormat[]{SettingsDefinitions.VideoFileFormat.MP4, SettingsDefinitions.VideoFileFormat.MOV} : type == SettingsDefinitions.CameraType.DJICameraTypeFC2403 ? new SettingsDefinitions.VideoFileFormat[]{SettingsDefinitions.VideoFileFormat.MP4, SettingsDefinitions.VideoFileFormat.MOV} : new SettingsDefinitions.VideoFileFormat[]{SettingsDefinitions.VideoFileFormat.MP4, SettingsDefinitions.VideoFileFormat.MOV};
            if (this.videoFileFormatRange == null || !Arrays.deepEquals(this.videoFileFormatRange, list)) {
                this.videoFileFormatRange = list;
                if (this.onValueChangeListener != null) {
                    this.onValueChangeListener.onNewValue(list, this.defaultKey.clone(CameraKeys.VIDEO_FILE_FORMAT_RANGE));
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void updateCameraAntiFlickerRange() {
        SettingsDefinitions.CameraType type;
        if (DJIComponentManager.getInstance().isAircraftConnected() && (type = CameraUtils.getCameraType(this.defaultKey.getIndex())) != null) {
            SettingsDefinitions.AntiFlickerFrequency[] list = (type == SettingsDefinitions.CameraType.DJICameraTypeFC220S || type == SettingsDefinitions.CameraType.DJICameraTypeGD600 || type == SettingsDefinitions.CameraType.DJIPayloadCamera) ? new SettingsDefinitions.AntiFlickerFrequency[]{SettingsDefinitions.AntiFlickerFrequency.MANUAL_50HZ, SettingsDefinitions.AntiFlickerFrequency.MANUAL_60HZ} : (CameraUtils.isSparkCamera(type) || CameraUtils.isXTCamera(type) || CameraUtils.isInspire2Camera(type)) ? new SettingsDefinitions.AntiFlickerFrequency[]{SettingsDefinitions.AntiFlickerFrequency.AUTO} : CameraUtils.isMavicAirCamera(type) ? new SettingsDefinitions.AntiFlickerFrequency[]{SettingsDefinitions.AntiFlickerFrequency.DISABLED, SettingsDefinitions.AntiFlickerFrequency.MANUAL_50HZ, SettingsDefinitions.AntiFlickerFrequency.MANUAL_60HZ} : (CameraUtils.isWM240Camera(type) || CameraUtils.isWM245ZoomCamera(type)) ? new SettingsDefinitions.AntiFlickerFrequency[]{SettingsDefinitions.AntiFlickerFrequency.DISABLED, SettingsDefinitions.AntiFlickerFrequency.AUTO, SettingsDefinitions.AntiFlickerFrequency.MANUAL_50HZ, SettingsDefinitions.AntiFlickerFrequency.MANUAL_60HZ} : new SettingsDefinitions.AntiFlickerFrequency[]{SettingsDefinitions.AntiFlickerFrequency.AUTO, SettingsDefinitions.AntiFlickerFrequency.MANUAL_50HZ, SettingsDefinitions.AntiFlickerFrequency.MANUAL_60HZ};
            if (this.antiFlickerRange == null || !Arrays.deepEquals(this.antiFlickerRange, list)) {
                this.antiFlickerRange = list;
                if (this.onValueChangeListener != null) {
                    this.onValueChangeListener.onNewValue(list, this.defaultKey.clone(CameraKeys.ANTI_FLICKER_RANGE));
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void updateCameraVideoStandardRange() {
        SettingsDefinitions.CameraType type;
        SettingsDefinitions.VideoStandard[] list;
        if (DJIComponentManager.getInstance().isAircraftConnected() && (type = CameraUtils.getCameraType(this.defaultKey.getIndex())) != null) {
            if (type == SettingsDefinitions.CameraType.DJICameraTypeFC1102 || type == SettingsDefinitions.CameraType.DJICameraTypeFC160) {
                list = new SettingsDefinitions.VideoStandard[]{SettingsDefinitions.VideoStandard.NTSC};
            } else {
                list = (type == SettingsDefinitions.CameraType.DJICameraTypeFC230 || type == SettingsDefinitions.CameraType.DJICameraTypeFC240_1 || type == SettingsDefinitions.CameraType.DJICameraTypeFC240 || type == SettingsDefinitions.CameraType.DJICameraTypeFC245_IMX477 || type == SettingsDefinitions.CameraType.DJICameraTypeFC2403) ? new SettingsDefinitions.VideoStandard[0] : new SettingsDefinitions.VideoStandard[]{SettingsDefinitions.VideoStandard.PAL, SettingsDefinitions.VideoStandard.NTSC};
            }
            if (this.videoStandardRange == null || !Arrays.deepEquals(this.videoStandardRange, list)) {
                this.videoStandardRange = list;
                if (this.onValueChangeListener != null) {
                    this.onValueChangeListener.onNewValue(list, this.defaultKey.clone(CameraKeys.VIDEO_STANDARD_RANGE));
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void updateCameraVideoCompressionStandardRange() {
        SettingsDefinitions.VideoFileCompressionStandard[] list = null;
        if (DJIComponentManager.getInstance().isAircraftConnected()) {
            SettingsDefinitions.CameraType type = CameraUtils.getCameraType(this.defaultKey.getIndex());
            if (type != null) {
                list = (type == SettingsDefinitions.CameraType.DJICameraTypeFC6310 || type == SettingsDefinitions.CameraType.DJICameraTypeFC6510 || type == SettingsDefinitions.CameraType.DJICameraTypeFC6520 || type == SettingsDefinitions.CameraType.DJICameraTypeFC6532 || type == SettingsDefinitions.CameraType.DJICameraTypeFC6540 || type == SettingsDefinitions.CameraType.DJICameraTypeFC6310S || type == SettingsDefinitions.CameraType.DJICameraTypeFC65XXUnknown || type == SettingsDefinitions.CameraType.DJICameraTypeFC240 || type == SettingsDefinitions.CameraType.DJICameraTypeFC245_IMX477 || type == SettingsDefinitions.CameraType.DJICameraTypeFC240_1) ? new SettingsDefinitions.VideoFileCompressionStandard[]{SettingsDefinitions.VideoFileCompressionStandard.H264, SettingsDefinitions.VideoFileCompressionStandard.H265} : new SettingsDefinitions.VideoFileCompressionStandard[]{SettingsDefinitions.VideoFileCompressionStandard.H264};
            } else {
                return;
            }
        }
        if (this.videoFileCompressionStandardsRange == null || !Arrays.deepEquals(this.videoFileCompressionStandardsRange, list)) {
            this.videoFileCompressionStandardsRange = list;
            if (this.onValueChangeListener != null) {
                this.onValueChangeListener.onNewValue(list, this.defaultKey.clone(CameraKeys.VIDEO_COMPRESSION_STANDARD_RANGE));
            }
        }
    }

    /* access modifiers changed from: private */
    public void updateCameraOrientationRange() {
        SettingsDefinitions.CameraType type;
        if (DJIComponentManager.getInstance().isAircraftConnected() && (type = CameraUtils.getCameraType(this.defaultKey.getIndex())) != null) {
            Boolean isTrackingMode = Boolean.valueOf(getTrackingMode(this.defaultKey.getIndex()));
            SettingsDefinitions.Orientation[] list = (isTrackingMode == null || !isTrackingMode.booleanValue()) ? (type == SettingsDefinitions.CameraType.DJICameraTypeFC220S || type == SettingsDefinitions.CameraType.DJICameraTypeFC220) ? new SettingsDefinitions.Orientation[]{SettingsDefinitions.Orientation.LANDSCAPE, SettingsDefinitions.Orientation.PORTRAIT} : new SettingsDefinitions.Orientation[]{SettingsDefinitions.Orientation.LANDSCAPE} : new SettingsDefinitions.Orientation[]{SettingsDefinitions.Orientation.LANDSCAPE};
            if (this.orientationRange == null || !Arrays.deepEquals(this.orientationRange, list)) {
                this.orientationRange = list;
                if (this.onValueChangeListener != null) {
                    this.onValueChangeListener.onNewValue(list, this.defaultKey.clone(CameraKeys.ORIENTATION_RANGE));
                }
            }
        }
    }

    @NonNull
    private static SettingsDefinitions.VideoFileCompressionStandard getVideoFileCompressionStandard(int compnentIndex) {
        SettingsDefinitions.VideoFileCompressionStandard videoFileCompressionStandard = SettingsDefinitions.VideoFileCompressionStandard.H264;
        DJISDKCacheParamValue value = DJISDKCache.getInstance().getAvailableValue(KeyHelper.getCameraKey(compnentIndex, CameraKeys.VIDEO_FILE_COMPRESSION_STANDARD));
        if (value != null) {
            return (SettingsDefinitions.VideoFileCompressionStandard) value.getData();
        }
        return videoFileCompressionStandard;
    }

    private static ResolutionAndFrameRate[] getFC260ResolutionAndFrameRateArray(SettingsDefinitions.VideoStandard videoType) {
        if (videoType == SettingsDefinitions.VideoStandard.NTSC) {
            return new ResolutionAndFrameRate[]{new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2704x1520, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2704x1520, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_59_DOT_940_FPS)};
        }
        return new ResolutionAndFrameRate[]{new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2704x1520, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2704x1520, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_50_FPS)};
    }

    private static ResolutionAndFrameRate[] getFC300SResolutionAndFrameRateArray(SettingsDefinitions.VideoStandard videoType) {
        if (videoType == SettingsDefinitions.VideoStandard.NTSC) {
            return new ResolutionAndFrameRate[]{new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2704x1520, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2704x1520, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_59_DOT_940_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_59_DOT_940_FPS)};
        }
        return new ResolutionAndFrameRate[]{new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2704x1520, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2704x1520, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_50_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_50_FPS)};
    }

    private static ResolutionAndFrameRate[] getCV600AndFC350ResolutionAndFrameRateArray(DJIComponentManager.PlatformType type, SettingsDefinitions.VideoStandard videoType) {
        ResolutionAndFrameRate[] list = null;
        if (type == null) {
            return null;
        }
        switch (type) {
            case OSMO:
                if (videoType != SettingsDefinitions.VideoStandard.NTSC) {
                    list = new ResolutionAndFrameRate[]{new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_4096x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2704x1520, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_4096x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2704x1520, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_50_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_50_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_120_FPS)};
                    break;
                } else {
                    list = new ResolutionAndFrameRate[]{new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_4096x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2704x1520, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2704x1520, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_59_DOT_940_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_59_DOT_940_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_120_FPS)};
                    break;
                }
            case M100:
            case Inspire:
            case M600:
            case M600Pro:
                if (videoType != SettingsDefinitions.VideoStandard.NTSC) {
                    list = new ResolutionAndFrameRate[]{new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_4096x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2704x1520, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_4096x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2704x1520, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_50_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_50_FPS)};
                    break;
                } else {
                    list = new ResolutionAndFrameRate[]{new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_4096x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2704x1520, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2704x1520, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_59_DOT_940_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_59_DOT_940_FPS)};
                    break;
                }
        }
        return list;
    }

    private static ResolutionAndFrameRate[] getFC330XResolutionAndFrameRateArray(SettingsDefinitions.VideoStandard videoType) {
        if (videoType == SettingsDefinitions.VideoStandard.NTSC) {
            return new ResolutionAndFrameRate[]{new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_4096x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2704x1520, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2704x1520, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_59_DOT_940_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_59_DOT_940_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_120_FPS)};
        }
        return new ResolutionAndFrameRate[]{new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_4096x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2704x1520, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_4096x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2704x1520, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_50_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_50_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_120_FPS)};
    }

    private static ResolutionAndFrameRate[] getFC300XAndFC300XWResolutionAndFrameRateArray(SettingsDefinitions.VideoStandard videoType) {
        if (videoType == SettingsDefinitions.VideoStandard.NTSC) {
            return new ResolutionAndFrameRate[]{new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_4096x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2704x1520, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2704x1520, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_59_DOT_940_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_59_DOT_940_FPS)};
        }
        return new ResolutionAndFrameRate[]{new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_4096x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2704x1520, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_4096x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2704x1520, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_50_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_50_FPS)};
    }

    private static ResolutionAndFrameRate[] getFC550AndFC550RawResolutionAndFrameRateArray(SettingsDefinitions.VideoStandard videoType) {
        if (videoType == SettingsDefinitions.VideoStandard.NTSC) {
            return new ResolutionAndFrameRate[]{new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_4096x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2704x1520, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2704x1520, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_59_DOT_940_FPS)};
        }
        return new ResolutionAndFrameRate[]{new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_4096x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2704x1520, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_4096x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2704x1520, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_50_FPS)};
    }

    private static ResolutionAndFrameRate[] getFC220ResolutionAndFrameRateArray(SettingsDefinitions.VideoStandard videoType) {
        if (videoType == SettingsDefinitions.VideoStandard.NTSC) {
            return new ResolutionAndFrameRate[]{new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_4096x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2720x1530, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2720x1530, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_59_DOT_940_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_59_DOT_940_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_96_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_120_FPS)};
        }
        return new ResolutionAndFrameRate[]{new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_4096x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2720x1530, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2720x1530, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_50_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_50_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_96_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_120_FPS)};
    }

    private static ResolutionAndFrameRate[] getFC6510ResolutionAndFrameRateArray(SettingsDefinitions.VideoStandard videoType, SettingsDefinitions.VideoFileCompressionStandard encodeType, SettingsDefinitions.CameraMode cameraMode) {
        ResolutionAndFrameRate[] resolutionAndFrameRateArray;
        ResolutionAndFrameRate[] list;
        if (cameraMode == null || cameraMode != SettingsDefinitions.CameraMode.BROADCAST) {
            if (encodeType != SettingsDefinitions.VideoFileCompressionStandard.H264) {
                resolutionAndFrameRateArray = videoType == SettingsDefinitions.VideoStandard.NTSC ? new ResolutionAndFrameRate[]{new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_4096x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2720x1530, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_4096x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2720x1530, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2720x1530, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2720x1530, SettingsDefinitions.VideoFrameRate.FRAME_RATE_59_DOT_940_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_59_DOT_940_FPS)} : new ResolutionAndFrameRate[]{new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_4096x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x1572, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2720x1530, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_4096x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x1572, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2720x1530, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2720x1530, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2720x1530, SettingsDefinitions.VideoFrameRate.FRAME_RATE_50_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_50_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_120_FPS)};
            } else if (videoType == SettingsDefinitions.VideoStandard.NTSC) {
                resolutionAndFrameRateArray = new ResolutionAndFrameRate[]{new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_4096x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2720x1530, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_4096x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2720x1530, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_4096x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2720x1530, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_4096x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_59_DOT_940_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_59_DOT_940_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2720x1530, SettingsDefinitions.VideoFrameRate.FRAME_RATE_59_DOT_940_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_59_DOT_940_FPS)};
                if (DataCameraGetPushStateInfo.getInstance().getVerstion() >= 4) {
                    LinkedList<ResolutionAndFrameRate> rangeList = new LinkedList<>(Arrays.asList(resolutionAndFrameRateArray));
                    rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_120_FPS));
                    ResolutionAndFrameRate[] list2 = (ResolutionAndFrameRate[]) rangeList.toArray(new ResolutionAndFrameRate[rangeList.size()]);
                }
            } else {
                resolutionAndFrameRateArray = new ResolutionAndFrameRate[]{new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_4096x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2720x1530, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_4096x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2720x1530, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_4096x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2720x1530, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_4096x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_50_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_50_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2720x1530, SettingsDefinitions.VideoFrameRate.FRAME_RATE_50_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_50_FPS)};
            }
            if (DataCameraGetPushStateInfo.getInstance().getVerstion() >= 4) {
                LinkedList<ResolutionAndFrameRate> rangeList2 = new LinkedList<>(Arrays.asList(resolutionAndFrameRateArray));
                rangeList2.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_120_FPS));
                list = (ResolutionAndFrameRate[]) rangeList2.toArray(new ResolutionAndFrameRate[rangeList2.size()]);
            } else {
                list = resolutionAndFrameRateArray;
            }
            return list;
        }
        return videoType == SettingsDefinitions.VideoStandard.NTSC ? new ResolutionAndFrameRate[]{new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_59_DOT_940_FPS)} : new ResolutionAndFrameRate[]{new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS)};
    }

    private static ResolutionAndFrameRate[] getFC6520ResolutionAndFrameRateArray(SettingsDefinitions.VideoStandard videoType, SettingsDefinitions.VideoFileCompressionStandard encodeType, SettingsDefinitions.CameraMode cameraMode) {
        if (cameraMode == null || cameraMode != SettingsDefinitions.CameraMode.BROADCAST) {
            return encodeType == SettingsDefinitions.VideoFileCompressionStandard.H264 ? videoType == SettingsDefinitions.VideoStandard.NTSC ? new ResolutionAndFrameRate[]{new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_4096x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x1572, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2720x1530, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_4096x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_24_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_24_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x1572, SettingsDefinitions.VideoFrameRate.FRAME_RATE_24_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2720x1530, SettingsDefinitions.VideoFrameRate.FRAME_RATE_24_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_24_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_4096x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x1572, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2720x1530, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_4096x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2720x1530, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_4096x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_59_DOT_940_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_59_DOT_940_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2720x1530, SettingsDefinitions.VideoFrameRate.FRAME_RATE_59_DOT_940_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_59_DOT_940_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_120_FPS)} : new ResolutionAndFrameRate[]{new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_4096x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x1572, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2720x1530, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_4096x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_24_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x1572, SettingsDefinitions.VideoFrameRate.FRAME_RATE_24_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_24_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2720x1530, SettingsDefinitions.VideoFrameRate.FRAME_RATE_24_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_24_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_4096x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x1572, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2720x1530, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_4096x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2720x1530, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_4096x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_50_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_50_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2720x1530, SettingsDefinitions.VideoFrameRate.FRAME_RATE_50_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_50_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_120_FPS)} : videoType == SettingsDefinitions.VideoStandard.NTSC ? new ResolutionAndFrameRate[]{new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_4096x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x1572, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2720x1530, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_4096x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_24_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_24_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x1572, SettingsDefinitions.VideoFrameRate.FRAME_RATE_24_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2720x1530, SettingsDefinitions.VideoFrameRate.FRAME_RATE_24_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_24_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_4096x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x1572, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2720x1530, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2720x1530, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2720x1530, SettingsDefinitions.VideoFrameRate.FRAME_RATE_59_DOT_940_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_59_DOT_940_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_120_FPS)} : new ResolutionAndFrameRate[]{new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_4096x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x1572, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2720x1530, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_4096x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_24_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_24_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x1572, SettingsDefinitions.VideoFrameRate.FRAME_RATE_24_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2720x1530, SettingsDefinitions.VideoFrameRate.FRAME_RATE_24_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_24_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_4096x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x1572, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2720x1530, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2720x1530, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2720x1530, SettingsDefinitions.VideoFrameRate.FRAME_RATE_50_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_50_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_120_FPS)};
        }
        return videoType == SettingsDefinitions.VideoStandard.NTSC ? new ResolutionAndFrameRate[]{new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_59_DOT_940_FPS)} : new ResolutionAndFrameRate[]{new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS)};
    }

    private static ResolutionAndFrameRate[] getFC6540ResolutionAndFrameRateArray(SettingsDefinitions.VideoStandard videoType, SettingsDefinitions.VideoFileCompressionStandard encodeType, SettingsDefinitions.CameraMode cameraMode) {
        ResolutionAndFrameRate[] list = getFC6520ResolutionAndFrameRateArray(videoType, encodeType, cameraMode);
        List<ResolutionAndFrameRate> rangeList = new LinkedList<>();
        if (cameraMode != null && cameraMode == SettingsDefinitions.CameraMode.BROADCAST) {
            return list;
        }
        for (ResolutionAndFrameRate frameRate : list) {
            if (!frameRate.getFrameRate().equals(SettingsDefinitions.VideoFrameRate.FRAME_RATE_120_FPS)) {
                rangeList.add(frameRate);
            }
        }
        if (videoType == SettingsDefinitions.VideoStandard.NTSC) {
            rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_4096x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_30_FPS));
            rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_30_FPS));
            rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x1572, SettingsDefinitions.VideoFrameRate.FRAME_RATE_30_FPS));
            rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2720x1530, SettingsDefinitions.VideoFrameRate.FRAME_RATE_30_FPS));
            rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_30_FPS));
        }
        if (encodeType == SettingsDefinitions.VideoFileCompressionStandard.H264) {
            rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x1572, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS));
            rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_4096x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_48_FPS));
            rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_48_FPS));
            rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x1572, SettingsDefinitions.VideoFrameRate.FRAME_RATE_48_FPS));
        }
        rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2720x1530, SettingsDefinitions.VideoFrameRate.FRAME_RATE_48_FPS));
        rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_48_FPS));
        return (ResolutionAndFrameRate[]) rangeList.toArray(new ResolutionAndFrameRate[rangeList.size()]);
    }

    private static ResolutionAndFrameRate[] getFC6310ResolutionAndFrameRateArray(SettingsDefinitions.VideoStandard videoType, SettingsDefinitions.VideoFileCompressionStandard encodeType, boolean isSupport120FPS) {
        ResolutionAndFrameRate[] resolutionAndFrameRateArray = encodeType == SettingsDefinitions.VideoFileCompressionStandard.H264 ? videoType == SettingsDefinitions.VideoStandard.NTSC ? new ResolutionAndFrameRate[]{new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_4096x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2720x1530, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_4096x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2720x1530, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_4096x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2720x1530, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_4096x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_59_DOT_940_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_59_DOT_940_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2720x1530, SettingsDefinitions.VideoFrameRate.FRAME_RATE_59_DOT_940_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_59_DOT_940_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_59_DOT_940_FPS)} : new ResolutionAndFrameRate[]{new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_4096x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2720x1530, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_4096x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2720x1530, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_4096x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2720x1530, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_4096x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_50_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_50_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2720x1530, SettingsDefinitions.VideoFrameRate.FRAME_RATE_50_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_50_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_50_FPS)} : videoType == SettingsDefinitions.VideoStandard.NTSC ? new ResolutionAndFrameRate[]{new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_4096x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2720x1530, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_4096x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2720x1530, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2720x1530, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2720x1530, SettingsDefinitions.VideoFrameRate.FRAME_RATE_59_DOT_940_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_59_DOT_940_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_59_DOT_940_FPS)} : new ResolutionAndFrameRate[]{new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_4096x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2720x1530, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_4096x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2720x1530, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2720x1530, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2720x1530, SettingsDefinitions.VideoFrameRate.FRAME_RATE_50_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_50_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_50_FPS)};
        if (DataCameraGetPushStateInfo.getInstance().getVerstion() < 4 || !isSupport120FPS) {
            return resolutionAndFrameRateArray;
        }
        LinkedList<ResolutionAndFrameRate> rangeList = new LinkedList<>(Arrays.asList(resolutionAndFrameRateArray));
        rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_120_FPS));
        rangeList.add(new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_120_FPS));
        return (ResolutionAndFrameRate[]) rangeList.toArray(new ResolutionAndFrameRate[rangeList.size()]);
    }

    private static ResolutionAndFrameRate[] getFC220SResolutionAndFrameRateArray(SettingsDefinitions.VideoStandard videoType) {
        if (videoType == SettingsDefinitions.VideoStandard.NTSC) {
            return new ResolutionAndFrameRate[]{new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS)};
        }
        return new ResolutionAndFrameRate[]{new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS)};
    }

    private static ResolutionAndFrameRate[] getFC230ResolutionAndFrameRateArray() {
        return new ResolutionAndFrameRate[]{new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2720x1530, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2720x1530, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2720x1530, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2720x1530, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2720x1530, SettingsDefinitions.VideoFrameRate.FRAME_RATE_50_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2720x1530, SettingsDefinitions.VideoFrameRate.FRAME_RATE_59_DOT_940_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_50_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_59_DOT_940_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_120_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_50_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_59_DOT_940_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1280x720, SettingsDefinitions.VideoFrameRate.FRAME_RATE_120_FPS)};
    }

    private static ResolutionAndFrameRate[] getCamera240ResolutionAndFrameRateArray(int index) {
        if (getTrackingMode(index)) {
            return new ResolutionAndFrameRate[]{new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2688x1512, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2688x1512, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2688x1512, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2688x1512, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2688x1512, SettingsDefinitions.VideoFrameRate.FRAME_RATE_50_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_50_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_59_DOT_940_FPS)};
        }
        return new ResolutionAndFrameRate[]{new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2688x1512, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2688x1512, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2688x1512, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2688x1512, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2688x1512, SettingsDefinitions.VideoFrameRate.FRAME_RATE_50_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2688x1512, SettingsDefinitions.VideoFrameRate.FRAME_RATE_59_DOT_940_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_50_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_59_DOT_940_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_120_FPS)};
    }

    private static ResolutionAndFrameRate[] getCamera240_1ResolutionAndFrameRateArray(int index) {
        if (getTrackingMode(index)) {
            return new ResolutionAndFrameRate[]{new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS, SettingsDefinitions.VideoFov.NARROW), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS, SettingsDefinitions.VideoFov.NARROW), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS, SettingsDefinitions.VideoFov.NARROW), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS, SettingsDefinitions.VideoFov.WIDE), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS, SettingsDefinitions.VideoFov.WIDE), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS, SettingsDefinitions.VideoFov.WIDE), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2688x1512, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2688x1512, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2688x1512, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2688x1512, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2688x1512, SettingsDefinitions.VideoFrameRate.FRAME_RATE_50_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_50_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_59_DOT_940_FPS)};
        }
        return new ResolutionAndFrameRate[]{new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS, SettingsDefinitions.VideoFov.NARROW), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS, SettingsDefinitions.VideoFov.NARROW), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS, SettingsDefinitions.VideoFov.NARROW), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS, SettingsDefinitions.VideoFov.WIDE), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS, SettingsDefinitions.VideoFov.WIDE), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS, SettingsDefinitions.VideoFov.WIDE), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2688x1512, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2688x1512, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2688x1512, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2688x1512, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2688x1512, SettingsDefinitions.VideoFrameRate.FRAME_RATE_50_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2688x1512, SettingsDefinitions.VideoFrameRate.FRAME_RATE_59_DOT_940_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_23_DOT_976_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_47_DOT_950_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_50_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_59_DOT_940_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_120_FPS)};
    }

    private static ResolutionAndFrameRate[] getGD600ResolutionAndFrameRateArray(SettingsDefinitions.VideoStandard videoType) {
        if (videoType == SettingsDefinitions.VideoStandard.NTSC) {
            return new ResolutionAndFrameRate[]{new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS)};
        }
        return new ResolutionAndFrameRate[]{new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS)};
    }

    private static ResolutionAndFrameRate[] getXT2ResolutionAndFrameRateArray(SettingsDefinitions.VideoStandard videoType, int componentIndex) {
        if (componentIndex != 2) {
            return new ResolutionAndFrameRate[]{new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS)};
        }
        return new ResolutionAndFrameRate[]{new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_640x512, SettingsDefinitions.VideoFrameRate.FRAME_RATE_7_DOT_5_FPS)};
    }

    private static ResolutionAndFrameRate[] getDual245ResolutionAndFrameRateArray(int componentIndex) {
        if (componentIndex != 2) {
            return new ResolutionAndFrameRate[]{new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2688x1512, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_3840x2160, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS)};
        }
        return new ResolutionAndFrameRate[]{new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_640x360, SettingsDefinitions.VideoFrameRate.FRAME_RATE_8_DOT_7_FPS)};
    }

    private static ResolutionAndFrameRate[] getWM160ResolutionAndFrameRateArray(SettingsDefinitions.VideoStandard videoType) {
        if (videoType != SettingsDefinitions.VideoStandard.NTSC) {
            return null;
        }
        return new ResolutionAndFrameRate[]{new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2720x1530, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_2720x1530, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_50_FPS), new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_59_DOT_940_FPS)};
    }

    public static ResolutionAndFrameRate[] getVideoResolutionAndFrameRateRange(int componentIndex) {
        ResolutionAndFrameRate[] list;
        SettingsDefinitions.CameraType cameraType = CameraUtils.getCameraType(componentIndex);
        if (cameraType == null) {
            return null;
        }
        SettingsDefinitions.VideoStandard videoType = getVideoStandard(componentIndex);
        if (videoType == null) {
            return null;
        }
        switch (cameraType) {
            case DJICameraTypeFC6540:
                list = getFC6540ResolutionAndFrameRateArray(videoType, getVideoFileCompressionStandard(componentIndex), getCameraMode(componentIndex));
                break;
            case DJICameraTypeFC6520:
                list = getFC6520ResolutionAndFrameRateArray(videoType, getVideoFileCompressionStandard(componentIndex), getCameraMode(componentIndex));
                break;
            case DJICameraTypeFC550:
            case DJICameraTypeFC550Raw:
                list = getFC550AndFC550RawResolutionAndFrameRateArray(videoType);
                break;
            case DJICameraTypeFC300X:
            case DJICameraTypeFC300XW:
                list = getFC300XAndFC300XWResolutionAndFrameRateArray(videoType);
                break;
            case DJICameraTypeFC330X:
                list = getFC330XResolutionAndFrameRateArray(videoType);
                break;
            case DJICameraTypeFC350:
            case DJICameraTypeCV600:
                list = getCV600AndFC350ResolutionAndFrameRateArray(DJIComponentManager.getInstance().getPlatformType(), videoType);
                break;
            case DJICameraTypeFC300S:
                list = getFC300SResolutionAndFrameRateArray(videoType);
                break;
            case DJICameraTypeFC260:
                list = getFC260ResolutionAndFrameRateArray(videoType);
                break;
            case DJICameraTypeTau640:
                if (videoType != SettingsDefinitions.VideoStandard.NTSC) {
                    list = new ResolutionAndFrameRate[]{new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_640x512, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS)};
                    break;
                } else {
                    list = new ResolutionAndFrameRate[]{new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_640x512, SettingsDefinitions.VideoFrameRate.FRAME_RATE_30_FPS)};
                    break;
                }
            case DJICameraTypeTau336:
                if (videoType != SettingsDefinitions.VideoStandard.NTSC) {
                    list = new ResolutionAndFrameRate[]{new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_336x256, SettingsDefinitions.VideoFrameRate.FRAME_RATE_25_FPS)};
                    break;
                } else {
                    list = new ResolutionAndFrameRate[]{new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_336x256, SettingsDefinitions.VideoFrameRate.FRAME_RATE_30_FPS)};
                    break;
                }
            case DJICameraTypeFC220:
                list = getFC220ResolutionAndFrameRateArray(videoType);
                break;
            case DJICameraTypeFC220S:
                list = getFC220SResolutionAndFrameRateArray(videoType);
                break;
            case DJICameraTypeFC6310:
                list = getFC6310ResolutionAndFrameRateArray(videoType, getVideoFileCompressionStandard(componentIndex), true);
                break;
            case DJICameraTypeFC6310S:
                list = getFC6310ResolutionAndFrameRateArray(videoType, getVideoFileCompressionStandard(componentIndex), false);
                break;
            case DJICameraTypeGD600:
            case DJIPayloadCamera:
                list = getGD600ResolutionAndFrameRateArray(videoType);
                break;
            case DJICameraTypeFC1102:
                list = new ResolutionAndFrameRate[]{new ResolutionAndFrameRate(SettingsDefinitions.VideoResolution.RESOLUTION_1920x1080, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS)};
                break;
            case DJICameraTypeFC1705:
                list = getXT2ResolutionAndFrameRateArray(videoType, componentIndex);
                break;
            case DJICameraTypeFC230:
                list = getFC230ResolutionAndFrameRateArray();
                break;
            case DJICameraTypeFC240:
            case DJICameraTypeFC245_IMX477:
                list = getCamera240ResolutionAndFrameRateArray(componentIndex);
                break;
            case DJICameraTypeFC2403:
                list = getDual245ResolutionAndFrameRateArray(componentIndex);
                break;
            case DJICameraTypeFC240_1:
                list = getCamera240_1ResolutionAndFrameRateArray(componentIndex);
                break;
            case DJICameraTypeFC160:
                list = getWM160ResolutionAndFrameRateArray(videoType);
                break;
            case DJICameraTypeFC6510:
                list = getFC6510ResolutionAndFrameRateArray(videoType, getVideoFileCompressionStandard(componentIndex), getCameraMode(componentIndex));
                break;
            default:
                list = null;
                break;
        }
        return list;
    }

    private static SettingsDefinitions.ShutterSpeed[] defaultAircraftRange() {
        return new SettingsDefinitions.ShutterSpeed[]{SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_8000, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_6400, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_5000, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_4000, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_3200, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_2500, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_2000, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_1600, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_1250, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_1000, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_800, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_640, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_500, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_400, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_320, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_240, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_200, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_160, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_120, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_100, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_80, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_60, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_50, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_40, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_30, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_25, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_20, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_15, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_12_DOT_5, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_10, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_8, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_6_DOT_25, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_5, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_4, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_3, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_2_DOT_5, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_2, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_1_DOT_67, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_1_DOT_25, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_DOT_3, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_DOT_6, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_2, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_2_DOT_5, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_3, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_3_DOT_2, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_4, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_5, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_6, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_7, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_8};
    }

    private static SettingsDefinitions.ShutterSpeed[] defaultHandheldRange() {
        return new SettingsDefinitions.ShutterSpeed[]{SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_8000, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_6400, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_5000, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_4000, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_3200, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_2500, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_2000, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_1600, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_1250, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_1000, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_800, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_640, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_500, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_400, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_320, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_240, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_200, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_160, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_120, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_100, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_80, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_60, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_50, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_40, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_30, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_25, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_20, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_15, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_12_DOT_5, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_10, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_8, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_6_DOT_25, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_5, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_4, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_3, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_2_DOT_5, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_2, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_1_DOT_67, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_1_DOT_25, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_DOT_3, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_DOT_6, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_2, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_2_DOT_5, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_3, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_3_DOT_2, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_4, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_5, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_6, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_7, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_8, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_9, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_10, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_13, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_15, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_20, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_25, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_30};
    }

    private static SettingsDefinitions.ShutterSpeed[] defaultWM160ShutterRange() {
        return new SettingsDefinitions.ShutterSpeed[]{SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_8000, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_6400, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_5000, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_4000, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_3200, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_2500, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_2000, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_1600, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_1250, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_1000, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_800, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_640, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_500, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_400, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_320, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_240, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_200, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_160, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_120, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_100, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_80, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_60, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_50, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_40, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_30, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_25, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_20, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_15, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_12_DOT_5, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_10, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_8, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_6_DOT_25, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_5, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_4, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_3, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_2_DOT_5, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_2, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_1_DOT_67, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_1_DOT_25, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_DOT_3, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_DOT_6, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_2, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_2_DOT_5, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_3, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_3_DOT_2, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_4};
    }

    private static SettingsDefinitions.ShutterSpeed[] defaultMavicShutterSpeedRange() {
        return new SettingsDefinitions.ShutterSpeed[]{SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_3200, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_2500, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_1600, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_1250, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_1000, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_800, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_640, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_500, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_400, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_320, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_240, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_200, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_160, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_120, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_100, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_80, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_60, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_50, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_40, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_30, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_25, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_20, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_15, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_12_DOT_5, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_10, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_8};
    }

    private static SettingsDefinitions.ShutterSpeed[] defaultSparkShutterSpeedRange() {
        return new SettingsDefinitions.ShutterSpeed[]{SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_8000, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_6400, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_5000, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_4000, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_3200, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_2500, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_2000, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_1600, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_1250, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_1000, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_800, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_640, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_500, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_400, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_320, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_240, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_200, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_160, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_120, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_100, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_80, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_60, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_50, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_40, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_30, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_25, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_20, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_15, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_12_DOT_5, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_10, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_8, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_6_DOT_25, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_5, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_4, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_3, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_2_DOT_5, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_2, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_1_DOT_67, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_1_DOT_25, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_DOT_3, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_DOT_6, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_2};
    }

    private static SettingsDefinitions.ShutterSpeed[] defaultGD600ShutterSpeedRange() {
        return new SettingsDefinitions.ShutterSpeed[]{SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_6000, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_4000, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_3000, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_2000, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_1500, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_1000, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_500, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_350, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_250, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_180, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_125, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_100, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_90, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_60, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_30, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_15, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_8, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_4, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_2, SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1};
    }

    public static SettingsDefinitions.ShutterSpeed[] pruneByTimeInterval(SettingsDefinitions.ShutterSpeed[] shutterSpeedArray, int timeIntervalInSeconds) {
        if (shutterSpeedArray == null) {
            return shutterSpeedArray;
        }
        LinkedList<SettingsDefinitions.ShutterSpeed> shutterSpeedList = new LinkedList<>(Arrays.asList(shutterSpeedArray));
        if (timeIntervalInSeconds == 1) {
            int index = shutterSpeedList.indexOf(SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1);
            if (index >= 0 && index < shutterSpeedList.size()) {
                while (shutterSpeedList.size() > index) {
                    shutterSpeedList.removeLast();
                }
            }
        } else {
            Iterator<SettingsDefinitions.ShutterSpeed> iter = shutterSpeedList.iterator();
            while (iter.hasNext()) {
                if (((float) (timeIntervalInSeconds - 1)) < ((SettingsDefinitions.ShutterSpeed) iter.next()).value()) {
                    iter.remove();
                }
            }
        }
        if (shutterSpeedList.size() <= 0) {
            return null;
        }
        return (SettingsDefinitions.ShutterSpeed[]) shutterSpeedList.toArray(new SettingsDefinitions.ShutterSpeed[shutterSpeedList.size()]);
    }

    private static LinkedList<SettingsDefinitions.ShutterSpeed> pruneByVideoFrameRate(SettingsDefinitions.ShutterSpeed[] shutterSpeedArray, @NonNull SettingsDefinitions.VideoFrameRate frameRateInput) {
        LinkedList<SettingsDefinitions.ShutterSpeed> shutterSpeedList = new LinkedList<>(Arrays.asList(shutterSpeedArray));
        int index = -1;
        switch (frameRateInput) {
            case FRAME_RATE_23_DOT_976_FPS:
            case FRAME_RATE_25_FPS:
                index = shutterSpeedList.indexOf(SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_25);
                break;
            case FRAME_RATE_29_DOT_970_FPS:
                index = shutterSpeedList.indexOf(SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_30);
                break;
            case FRAME_RATE_47_DOT_950_FPS:
            case FRAME_RATE_50_FPS:
                index = shutterSpeedList.indexOf(SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_50);
                break;
            case FRAME_RATE_59_DOT_940_FPS:
                index = shutterSpeedList.indexOf(SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_60);
                break;
            case FRAME_RATE_120_FPS:
                index = shutterSpeedList.indexOf(SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_120);
                break;
            case FRAME_RATE_96_FPS:
                index = shutterSpeedList.indexOf(SettingsDefinitions.ShutterSpeed.SHUTTER_SPEED_1_100);
                break;
        }
        if (index >= 0 && index < shutterSpeedList.size()) {
            while (shutterSpeedList.size() > index + 1) {
                shutterSpeedList.removeLast();
            }
        }
        return shutterSpeedList;
    }

    public static SettingsDefinitions.ShutterSpeed[] getShutterSpeedRange(int componentIndex) {
        ResolutionAndFrameRate resolutionAndFrameRate;
        SettingsDefinitions.ShutterSpeed[] list = null;
        SettingsDefinitions.ExposureMode exposureMode = getExposureMode(componentIndex);
        if (exposureMode == null) {
            return null;
        }
        SettingsDefinitions.CameraMode cameraMode = getCameraMode(componentIndex);
        if (cameraMode == null) {
            return null;
        }
        SettingsDefinitions.CameraType cameraType = CameraUtils.getCameraType(componentIndex);
        DJIComponentManager.PlatformType platformType = DJIComponentManager.getInstance().getPlatformType();
        if (platformType == null) {
            return null;
        }
        if (DJIComponentManager.getInstance().isAircraftConnected()) {
            if (exposureMode == SettingsDefinitions.ExposureMode.SHUTTER_PRIORITY || exposureMode == SettingsDefinitions.ExposureMode.MANUAL) {
                switch (platformType) {
                    case OSMO:
                    case OSMOMobile:
                        list = defaultHandheldRange();
                        break;
                    default:
                        if (cameraType != SettingsDefinitions.CameraType.DJICameraTypeFC220S) {
                            if (cameraType != SettingsDefinitions.CameraType.DJICameraTypeGD600 && cameraType != SettingsDefinitions.CameraType.DJICameraTypeFC1705 && cameraType != SettingsDefinitions.CameraType.DJICameraTypeFC2403 && cameraType != SettingsDefinitions.CameraType.DJIPayloadCamera) {
                                if (cameraType != SettingsDefinitions.CameraType.DJICameraTypeFC1102) {
                                    if (cameraType != SettingsDefinitions.CameraType.DJICameraTypeFC160) {
                                        list = defaultAircraftRange();
                                        break;
                                    } else {
                                        list = defaultWM160ShutterRange();
                                        break;
                                    }
                                } else {
                                    list = defaultSparkShutterSpeedRange();
                                    break;
                                }
                            } else {
                                list = defaultGD600ShutterSpeedRange();
                                break;
                            }
                        } else {
                            list = defaultMavicShutterSpeedRange();
                            break;
                        }
                        break;
                }
                if (cameraMode == SettingsDefinitions.CameraMode.RECORD_VIDEO && (resolutionAndFrameRate = getResolutionAndFrameRate(componentIndex)) != null) {
                    LinkedList<SettingsDefinitions.ShutterSpeed> rangeList = pruneByVideoFrameRate(list, resolutionAndFrameRate.getFrameRate());
                    list = (SettingsDefinitions.ShutterSpeed[]) rangeList.toArray(new SettingsDefinitions.ShutterSpeed[rangeList.size()]);
                }
                if (getTrackingMode(componentIndex)) {
                    LinkedList<SettingsDefinitions.ShutterSpeed> rangeList2 = pruneByVideoFrameRate(list, SettingsDefinitions.VideoFrameRate.FRAME_RATE_29_DOT_970_FPS);
                    list = (SettingsDefinitions.ShutterSpeed[]) rangeList2.toArray(new SettingsDefinitions.ShutterSpeed[rangeList2.size()]);
                }
            } else {
                list = null;
            }
        }
        return list;
    }

    /* access modifiers changed from: private */
    public void updateShutterSpeedRange() {
        SettingsDefinitions.ShutterSpeed[] list = getShutterSpeedRange(this.defaultKey.getIndex());
        SettingsDefinitions.ShootPhotoMode shootPhotoMode = (SettingsDefinitions.ShootPhotoMode) CacheHelper.getCamera(this.defaultKey.getIndex(), CameraKeys.SHOOT_PHOTO_MODE);
        SettingsDefinitions.PhotoTimeIntervalSettings intervalSettings = (SettingsDefinitions.PhotoTimeIntervalSettings) CacheHelper.getCamera(this.defaultKey.getIndex(), CameraKeys.PHOTO_TIME_INTERVAL_SETTINGS);
        if (shootPhotoMode == SettingsDefinitions.ShootPhotoMode.INTERVAL && intervalSettings != null) {
            list = pruneByTimeInterval(list, intervalSettings.getTimeIntervalInSeconds());
        }
        if (this.shutterSpeedRange == null || !Arrays.deepEquals(this.shutterSpeedRange, list)) {
            this.shutterSpeedRange = list;
            if (this.onValueChangeListener != null) {
                this.onValueChangeListener.onNewValue(list, this.defaultKey.clone(CameraKeys.SHUTTER_SPEED_RANGE));
            }
        }
    }

    /* access modifiers changed from: private */
    public void updateCameraDigitalFilterRange() {
        SettingsDefinitions.CameraColor[] list = null;
        if (DJIComponentManager.getInstance().isAircraftConnected()) {
            SettingsDefinitions.CameraType type = CameraUtils.getCameraType(this.defaultKey.getIndex());
            int version = DataCameraGetPushStateInfo.getInstance().getVerstion(getExpectedSenderIdByIndex());
            if (type != null) {
                boolean isSSDEnabled = CameraUtils.getSSDVideoRecordingEnabled(this.defaultKey.getIndex());
                SettingsDefinitions.CameraMode mode = (SettingsDefinitions.CameraMode) CacheHelper.getCamera(this.defaultKey.getIndex(), "Mode");
                SettingsDefinitions.ExposureSensitivityMode esMode = CameraUtils.getExposureSensitivityMode(this.defaultKey.getIndex());
                if (!isSSDEnabled || mode != SettingsDefinitions.CameraMode.RECORD_VIDEO) {
                    list = type == SettingsDefinitions.CameraType.DJICameraTypeFC6520 ? new SettingsDefinitions.CameraColor[]{SettingsDefinitions.CameraColor.D_CINELIKE, SettingsDefinitions.CameraColor.D_LOG, SettingsDefinitions.CameraColor.NONE, SettingsDefinitions.CameraColor.TRUE_COLOR, SettingsDefinitions.CameraColor.ART, SettingsDefinitions.CameraColor.FILM_A, SettingsDefinitions.CameraColor.FILM_B, SettingsDefinitions.CameraColor.FILM_C, SettingsDefinitions.CameraColor.FILM_D, SettingsDefinitions.CameraColor.FILM_E, SettingsDefinitions.CameraColor.FILM_F, SettingsDefinitions.CameraColor.FILM_G, SettingsDefinitions.CameraColor.FILM_H, SettingsDefinitions.CameraColor.FILM_I} : type == SettingsDefinitions.CameraType.DJICameraTypeFC6540 ? new SettingsDefinitions.CameraColor[]{SettingsDefinitions.CameraColor.NONE, SettingsDefinitions.CameraColor.D_CINELIKE} : (type == SettingsDefinitions.CameraType.DJICameraTypeFC6310 || (type == SettingsDefinitions.CameraType.DJICameraTypeFC6510 && version >= 5) || type == SettingsDefinitions.CameraType.DJICameraTypeFC6310S) ? new SettingsDefinitions.CameraColor[]{SettingsDefinitions.CameraColor.NONE, SettingsDefinitions.CameraColor.TRUE_COLOR, SettingsDefinitions.CameraColor.D_CINELIKE, SettingsDefinitions.CameraColor.D_LOG, SettingsDefinitions.CameraColor.FILM_A, SettingsDefinitions.CameraColor.FILM_B, SettingsDefinitions.CameraColor.FILM_C, SettingsDefinitions.CameraColor.FILM_D, SettingsDefinitions.CameraColor.FILM_E, SettingsDefinitions.CameraColor.FILM_F, SettingsDefinitions.CameraColor.FILM_G, SettingsDefinitions.CameraColor.FILM_H, SettingsDefinitions.CameraColor.FILM_I} : (type != SettingsDefinitions.CameraType.DJICameraTypeFC220 || version < 9) ? (type != SettingsDefinitions.CameraType.DJICameraTypeFC330X || version < 7) ? (type == SettingsDefinitions.CameraType.DJICameraTypeGD600 || type == SettingsDefinitions.CameraType.DJIPayloadCamera) ? new SettingsDefinitions.CameraColor[]{SettingsDefinitions.CameraColor.NONE, SettingsDefinitions.CameraColor.INVERSE, SettingsDefinitions.CameraColor.BLACK_AND_WHITE} : (type == SettingsDefinitions.CameraType.DJICameraTypeFC1102 || type == SettingsDefinitions.CameraType.DJICameraTypeFC1705 || type == SettingsDefinitions.CameraType.DJICameraTypeFC2403 || type == SettingsDefinitions.CameraType.DJICameraTypeFC160) ? new SettingsDefinitions.CameraColor[]{SettingsDefinitions.CameraColor.NONE} : type == SettingsDefinitions.CameraType.DJICameraTypeFC230 ? getCameraMode(this.defaultKey.getIndex()) == SettingsDefinitions.CameraMode.RECORD_VIDEO ? new SettingsDefinitions.CameraColor[]{SettingsDefinitions.CameraColor.NONE, SettingsDefinitions.CameraColor.D_CINELIKE} : new SettingsDefinitions.CameraColor[]{SettingsDefinitions.CameraColor.NONE} : (type == SettingsDefinitions.CameraType.DJICameraTypeFC240_1 || type == SettingsDefinitions.CameraType.DJICameraTypeFC240 || type == SettingsDefinitions.CameraType.DJICameraTypeFC245_IMX477) ? mode == SettingsDefinitions.CameraMode.SHOOT_PHOTO ? new SettingsDefinitions.CameraColor[]{SettingsDefinitions.CameraColor.NONE} : (type == SettingsDefinitions.CameraType.DJICameraTypeFC240 || type == SettingsDefinitions.CameraType.DJICameraTypeFC245_IMX477) ? new SettingsDefinitions.CameraColor[]{SettingsDefinitions.CameraColor.NONE, SettingsDefinitions.CameraColor.D_CINELIKE} : new SettingsDefinitions.CameraColor[]{SettingsDefinitions.CameraColor.NONE, SettingsDefinitions.CameraColor.D_LOG, SettingsDefinitions.CameraColor.HLG} : new SettingsDefinitions.CameraColor[]{SettingsDefinitions.CameraColor.D_LOG, SettingsDefinitions.CameraColor.D_CINELIKE, SettingsDefinitions.CameraColor.NONE, SettingsDefinitions.CameraColor.ART, SettingsDefinitions.CameraColor.BLACK_AND_WHITE, SettingsDefinitions.CameraColor.BRIGHT, SettingsDefinitions.CameraColor.M_31, SettingsDefinitions.CameraColor.K_DX, SettingsDefinitions.CameraColor.PRISMO, SettingsDefinitions.CameraColor.JUGO} : new SettingsDefinitions.CameraColor[]{SettingsDefinitions.CameraColor.D_LOG, SettingsDefinitions.CameraColor.D_CINELIKE, SettingsDefinitions.CameraColor.TRUE_COLOR, SettingsDefinitions.CameraColor.NONE, SettingsDefinitions.CameraColor.ART, SettingsDefinitions.CameraColor.BLACK_AND_WHITE, SettingsDefinitions.CameraColor.BRIGHT, SettingsDefinitions.CameraColor.M_31, SettingsDefinitions.CameraColor.K_DX, SettingsDefinitions.CameraColor.PRISMO, SettingsDefinitions.CameraColor.JUGO} : new SettingsDefinitions.CameraColor[]{SettingsDefinitions.CameraColor.NONE, SettingsDefinitions.CameraColor.BRIGHT, SettingsDefinitions.CameraColor.TRUE_COLOR_EXT, SettingsDefinitions.CameraColor.D_CINELIKE, SettingsDefinitions.CameraColor.D_LOG, SettingsDefinitions.CameraColor.FILM_A, SettingsDefinitions.CameraColor.FILM_B, SettingsDefinitions.CameraColor.FILM_C, SettingsDefinitions.CameraColor.FILM_D, SettingsDefinitions.CameraColor.FILM_E, SettingsDefinitions.CameraColor.FILM_F, SettingsDefinitions.CameraColor.FILM_G, SettingsDefinitions.CameraColor.FILM_H, SettingsDefinitions.CameraColor.FILM_I};
                } else if (esMode != SettingsDefinitions.ExposureSensitivityMode.EI) {
                    list = new SettingsDefinitions.CameraColor[]{SettingsDefinitions.CameraColor.NONE, SettingsDefinitions.CameraColor.D_CINELIKE};
                }
            } else {
                return;
            }
        }
        if (list == null) {
            return;
        }
        if (this.cameraFilterRange == null || !Arrays.deepEquals(this.cameraFilterRange, list)) {
            this.cameraFilterRange = list;
            if (this.onValueChangeListener != null) {
                this.onValueChangeListener.onNewValue(list, this.defaultKey.clone(CameraKeys.CAMERA_COLOR_RANGE));
            }
        }
    }

    /* access modifiers changed from: private */
    public void updateSSDVideoLookRange() {
        SettingsDefinitions.SSDColor[] list = null;
        if (DJIComponentManager.getInstance().isAircraftConnected()) {
            SettingsDefinitions.CameraType type = CameraUtils.getCameraType(this.defaultKey.getIndex());
            if (type != null) {
                SettingsDefinitions.ExposureSensitivityMode mode = CameraUtils.getExposureSensitivityMode(this.defaultKey.getIndex());
                CameraSSDVideoLicense license = CameraUtils.getSSDVideoLicense(this.defaultKey.getIndex());
                if (type == SettingsDefinitions.CameraType.DJICameraTypeFC6520 || type == SettingsDefinitions.CameraType.DJICameraTypeFC6540) {
                    if (license == CameraSSDVideoLicense.LicenseKeyTypeCinemaDNG) {
                        list = new SettingsDefinitions.SSDColor[]{SettingsDefinitions.SSDColor.RAW_COLOR};
                    } else if (license == CameraSSDVideoLicense.LicenseKeyTypeProRes422HQ || license == CameraSSDVideoLicense.LicenseKeyTypeProRes4444XQ) {
                        if (mode == SettingsDefinitions.ExposureSensitivityMode.EI) {
                            list = new SettingsDefinitions.SSDColor[]{SettingsDefinitions.SSDColor.DLOG, SettingsDefinitions.SSDColor.REC709};
                        } else if (mode == SettingsDefinitions.ExposureSensitivityMode.ISO) {
                            list = new SettingsDefinitions.SSDColor[]{SettingsDefinitions.SSDColor.STANDARD, SettingsDefinitions.SSDColor.CINE_LIKE};
                        }
                    }
                }
            } else {
                return;
            }
        }
        if (list == null) {
            return;
        }
        if (this.cameraSSDColorRange == null || !Arrays.deepEquals(this.cameraSSDColorRange, list)) {
            this.cameraSSDColorRange = list;
            if (this.onValueChangeListener != null) {
                this.onValueChangeListener.onNewValue(list, this.defaultKey.clone(CameraKeys.SSD_COLOR_RANGE));
            }
        }
    }

    public static SettingsDefinitions.Aperture[] getApertureRange(int componentIndex) {
        SettingsDefinitions.Aperture[] list = null;
        if (DJIComponentManager.getInstance().isAircraftConnected()) {
            SettingsDefinitions.CameraType type = CameraUtils.getCameraType(componentIndex);
            if (type == null) {
                return null;
            }
            SettingsDefinitions.ExposureMode exposureMode = getExposureMode(componentIndex);
            if (exposureMode == null) {
                return null;
            }
            if (SettingsDefinitions.ExposureMode.MANUAL == exposureMode || SettingsDefinitions.ExposureMode.APERTURE_PRIORITY == exposureMode) {
                list = (type == SettingsDefinitions.CameraType.DJICameraTypeFC550 || type == SettingsDefinitions.CameraType.DJICameraTypeFC550Raw || type == SettingsDefinitions.CameraType.DJICameraTypeFC6520) ? new SettingsDefinitions.Aperture[]{SettingsDefinitions.Aperture.F_1_DOT_7, SettingsDefinitions.Aperture.F_1_DOT_8, SettingsDefinitions.Aperture.F_2, SettingsDefinitions.Aperture.F_2_DOT_2, SettingsDefinitions.Aperture.F_2_DOT_5, SettingsDefinitions.Aperture.F_2_DOT_8, SettingsDefinitions.Aperture.F_3_DOT_2, SettingsDefinitions.Aperture.F_3_DOT_5, SettingsDefinitions.Aperture.F_4, SettingsDefinitions.Aperture.F_4_DOT_5, SettingsDefinitions.Aperture.F_5, SettingsDefinitions.Aperture.F_5_DOT_6, SettingsDefinitions.Aperture.F_6_DOT_3, SettingsDefinitions.Aperture.F_7_DOT_1, SettingsDefinitions.Aperture.F_8, SettingsDefinitions.Aperture.F_9, SettingsDefinitions.Aperture.F_10, SettingsDefinitions.Aperture.F_11, SettingsDefinitions.Aperture.F_13, SettingsDefinitions.Aperture.F_14, SettingsDefinitions.Aperture.F_16} : type == SettingsDefinitions.CameraType.DJICameraTypeFC6540 ? new SettingsDefinitions.Aperture[]{SettingsDefinitions.Aperture.F_2_DOT_8, SettingsDefinitions.Aperture.F_3_DOT_2, SettingsDefinitions.Aperture.F_3_DOT_5, SettingsDefinitions.Aperture.F_4, SettingsDefinitions.Aperture.F_4_DOT_5, SettingsDefinitions.Aperture.F_5, SettingsDefinitions.Aperture.F_5_DOT_6, SettingsDefinitions.Aperture.F_6_DOT_3, SettingsDefinitions.Aperture.F_7_DOT_1, SettingsDefinitions.Aperture.F_8, SettingsDefinitions.Aperture.F_9, SettingsDefinitions.Aperture.F_10, SettingsDefinitions.Aperture.F_11, SettingsDefinitions.Aperture.F_13, SettingsDefinitions.Aperture.F_14, SettingsDefinitions.Aperture.F_16} : (type == SettingsDefinitions.CameraType.DJICameraTypeGD600 || type == SettingsDefinitions.CameraType.DJIPayloadCamera) ? new SettingsDefinitions.Aperture[]{SettingsDefinitions.Aperture.F_1_DOT_6, SettingsDefinitions.Aperture.F_2, SettingsDefinitions.Aperture.F_2_DOT_4, SettingsDefinitions.Aperture.F_2_DOT_8, SettingsDefinitions.Aperture.F_3_DOT_4, SettingsDefinitions.Aperture.F_4, SettingsDefinitions.Aperture.F_4_DOT_8, SettingsDefinitions.Aperture.F_5_DOT_6, SettingsDefinitions.Aperture.F_6_DOT_8, SettingsDefinitions.Aperture.F_8, SettingsDefinitions.Aperture.F_9_DOT_6, SettingsDefinitions.Aperture.F_11, SettingsDefinitions.Aperture.F_14} : (type == SettingsDefinitions.CameraType.DJICameraTypeFC6310 || type == SettingsDefinitions.CameraType.DJICameraTypeFC240_1 || type == SettingsDefinitions.CameraType.DJICameraTypeFC6510 || type == SettingsDefinitions.CameraType.DJICameraTypeFC6310S) ? new SettingsDefinitions.Aperture[]{SettingsDefinitions.Aperture.F_2_DOT_8, SettingsDefinitions.Aperture.F_3_DOT_2, SettingsDefinitions.Aperture.F_3_DOT_5, SettingsDefinitions.Aperture.F_4, SettingsDefinitions.Aperture.F_4_DOT_5, SettingsDefinitions.Aperture.F_5, SettingsDefinitions.Aperture.F_5_DOT_6, SettingsDefinitions.Aperture.F_6_DOT_3, SettingsDefinitions.Aperture.F_7_DOT_1, SettingsDefinitions.Aperture.F_8, SettingsDefinitions.Aperture.F_9, SettingsDefinitions.Aperture.F_10, SettingsDefinitions.Aperture.F_11} : null;
            }
        }
        return list;
    }

    /* access modifiers changed from: private */
    public void updateApertureRange() {
        SettingsDefinitions.Aperture[] list = getApertureRange(this.defaultKey.getIndex());
        if (list != null) {
            if (this.cameraApertureRange == null || !Arrays.deepEquals(this.cameraApertureRange, list)) {
                this.cameraApertureRange = list;
                if (this.onValueChangeListener != null) {
                    this.onValueChangeListener.onNewValue(list, this.defaultKey.clone(CameraKeys.APERTURE_RANGE));
                }
            }
        }
    }

    private void triggerUpdateAll() {
        try {
            updateCameraISORange();
            updateExposureModeRange();
            updateExposureCompensationRange();
            updateCameraModeRange();
            updateShootPhotoModeRange();
            updateShootPhotoModeChildRange();
            updateCameraPhotoFileFormatRange();
            updateCameraWhiteBalancePresentRange();
            updateCameraWhiteBalanceCustomColorTemperatureRange();
            updateCameraPhotoAspectRatioRange();
            updateVideoFileFormatRange();
            updateCameraAntiFlickerRange();
            updateCameraOrientationRange();
            updateShutterSpeedRange();
            updateApertureRange();
            updateCameraDigitalFilterRange();
            updateSSDVideoResolutionRange();
            updateSSDVideoLookRange();
            updateSSDVideoResolutionAndFrameRateRange();
            updateDualCameraDisplayModeRange();
            updateThermalPaletteRange();
        } catch (Exception e) {
            DJILog.e(TAG, "init RangeManager fail in triggerUpdateAll method", new Object[0]);
            DJILog.e(TAG, DJILog.exceptionToString(e), new Object[0]);
        }
    }

    public void onDestory() {
        Iterator<DJIParamAccessListener> it2 = this.listeners.iterator();
        while (it2.hasNext()) {
            DJISDKCache.getInstance().stopListening(it2.next());
        }
        this.listeners = null;
        this.onValueChangeListener = null;
    }

    private int getExpectedSenderIdByIndex() {
        return DoubleCameraSupportUtil.getCameraIdInProtocol(this.defaultKey.getIndex());
    }
}
