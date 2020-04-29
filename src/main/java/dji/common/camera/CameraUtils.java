package dji.common.camera;

import dji.common.camera.SettingsDefinitions;
import dji.fieldAnnotation.EXClassNullAway;
import dji.logic.manager.DJIUSBWifiSwitchManager;
import dji.logic.vision.IVisionResDefine;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.model.P3.DataCameraGetPushShotParams;
import dji.midware.data.model.P3.DataCameraGetPushStorageInfo;
import dji.midware.data.model.P3.DataCameraGetStateInfo;
import dji.midware.data.model.P3.DataCameraSetStorageInfo;
import dji.midware.data.model.P3.DataEyeGetPushException;
import dji.sdksharedlib.DJISDKCache;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.extension.KeyHelper;
import dji.sdksharedlib.keycatalog.CameraKeys;
import dji.sdksharedlib.store.DJISDKCacheParamValue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@EXClassNullAway
public class CameraUtils {
    public static boolean isInspire2Camera(SettingsDefinitions.CameraType cType) {
        return cType == SettingsDefinitions.CameraType.DJICameraTypeFC6510 || cType == SettingsDefinitions.CameraType.DJICameraTypeFC6520;
    }

    public static boolean isGDCamera(SettingsDefinitions.CameraType cType) {
        return cType == SettingsDefinitions.CameraType.DJICameraTypeGD600;
    }

    public static boolean isPayloadCamera(SettingsDefinitions.CameraType cType) {
        return cType == SettingsDefinitions.CameraType.DJIPayloadCamera;
    }

    public static boolean isSparkCamera(SettingsDefinitions.CameraType cType) {
        return cType == SettingsDefinitions.CameraType.DJICameraTypeFC1102;
    }

    public static boolean isMavicAirCamera(SettingsDefinitions.CameraType cType) {
        return cType == SettingsDefinitions.CameraType.DJICameraTypeFC230;
    }

    public static boolean isWM240Camera(SettingsDefinitions.CameraType cType) {
        return cType == SettingsDefinitions.CameraType.DJICameraTypeFC240 || cType == SettingsDefinitions.CameraType.DJICameraTypeFC240_1;
    }

    public static boolean isWM245ZoomCamera(SettingsDefinitions.CameraType cType) {
        return cType == SettingsDefinitions.CameraType.DJICameraTypeFC245_IMX477;
    }

    public static boolean isWM245DualCamera(SettingsDefinitions.CameraType cType) {
        return cType == SettingsDefinitions.CameraType.DJICameraTypeFC2403;
    }

    public static boolean isXTCamera(SettingsDefinitions.CameraType cType) {
        return cType == SettingsDefinitions.CameraType.DJICameraTypeTau336 || cType == SettingsDefinitions.CameraType.DJICameraTypeTau640;
    }

    public static boolean isXT2Camera(SettingsDefinitions.CameraType cType) {
        return cType == SettingsDefinitions.CameraType.DJICameraTypeFC1705;
    }

    public static boolean supportCameraManualFocus(SettingsDefinitions.CameraType type) {
        if (DJIUSBWifiSwitchManager.getInstance().isProductWifiConnected(null) || (type != SettingsDefinitions.CameraType.DJICameraTypeFC550 && type != SettingsDefinitions.CameraType.DJICameraTypeFC550Raw && type != SettingsDefinitions.CameraType.DJICameraTypeFC220 && type != SettingsDefinitions.CameraType.DJICameraTypeCV600 && type != SettingsDefinitions.CameraType.DJICameraTypeFC220S && type != SettingsDefinitions.CameraType.DJICameraTypeFC6310 && !isInspire2Camera(type) && !isGDCamera(type))) {
            return false;
        }
        return true;
    }

    public static SettingsDefinitions.ExposureCompensation getRealCameraExposureCompensation(int exposureCompensationValue) {
        if (DataCameraGetPushShotParams.getInstance().getExposureMode().value() == 1) {
            return SettingsDefinitions.ExposureCompensation.find(DataCameraGetPushShotParams.getInstance().getExposureCompensation());
        }
        return SettingsDefinitions.ExposureCompensation.find(exposureCompensationValue);
    }

    public static SettingsDefinitions.ExposureCompensation getRealCameraExposureCompensation(int exposureCompensationValue, int index) {
        if (DataCameraGetPushShotParams.getInstance().getExposureMode(index).value() == 1) {
            return SettingsDefinitions.ExposureCompensation.find(DataCameraGetPushShotParams.getInstance().getExposureCompensation(index));
        }
        return SettingsDefinitions.ExposureCompensation.find(exposureCompensationValue);
    }

    public static SettingsDefinitions.ShutterSpeed getRealShutterSpeed(boolean isFraction, int integerValue, int fractionValue) {
        float value = Float.valueOf(integerValue + "." + fractionValue).floatValue();
        if (!isFraction) {
            return SettingsDefinitions.ShutterSpeed.find(value);
        }
        return SettingsDefinitions.ShutterSpeed.find(1.0f / value);
    }

    public static int[] getShutterSpeedCmdData(SettingsDefinitions.ShutterSpeed shutterSpeed) {
        if (shutterSpeed == null || shutterSpeed == SettingsDefinitions.ShutterSpeed.UNKNOWN) {
            return null;
        }
        int[] output = {-1, -1, -1};
        String[] sections = shutterSpeed.name().replace("SHUTTER_SPEED_", "").split("_");
        if (shutterSpeed.value() < 1.0f) {
            output[0] = 1;
            if (sections.length == 2) {
                output[1] = Integer.parseInt(sections[1]);
                output[2] = 0;
                return output;
            } else if (sections.length != 4 || !sections[2].equals("DOT")) {
                return output;
            } else {
                output[1] = Integer.parseInt(sections[1]);
                output[2] = Integer.parseInt(sections[3]);
                return output;
            }
        } else {
            output[0] = 0;
            if (sections.length == 1) {
                output[1] = Integer.parseInt(sections[0]);
                output[2] = 0;
                return output;
            } else if (sections.length != 3 || !sections[1].equals("DOT")) {
                return output;
            } else {
                output[1] = Integer.parseInt(sections[0]);
                output[2] = Integer.parseInt(sections[2]);
                return output;
            }
        }
    }

    public static int convertISOToInt(SettingsDefinitions.ISO iso) {
        if (iso == SettingsDefinitions.ISO.AUTO || iso == SettingsDefinitions.ISO.UNKNOWN) {
            return 0;
        }
        return Integer.parseInt(iso.toString().split("_")[1]);
    }

    public static SettingsDefinitions.ISO convertIntToISO(int isoValue) {
        if (isoValue > 0 && isoValue < 200) {
            return SettingsDefinitions.ISO.ISO_100;
        }
        if (isoValue >= 200 && isoValue < 400) {
            return SettingsDefinitions.ISO.ISO_200;
        }
        if (isoValue >= 400 && isoValue < 800) {
            return SettingsDefinitions.ISO.ISO_400;
        }
        if (isoValue >= 800 && isoValue < 1600) {
            return SettingsDefinitions.ISO.ISO_800;
        }
        if (isoValue >= 1600 && isoValue < 3200) {
            return SettingsDefinitions.ISO.ISO_1600;
        }
        if (isoValue >= 3200 && isoValue < 6400) {
            return SettingsDefinitions.ISO.ISO_3200;
        }
        if (isoValue >= 6400 && isoValue < 12800) {
            return SettingsDefinitions.ISO.ISO_6400;
        }
        if (isoValue >= 12800 && isoValue < 25600) {
            return SettingsDefinitions.ISO.ISO_12800;
        }
        if (isoValue >= 25600) {
            return SettingsDefinitions.ISO.ISO_25600;
        }
        return SettingsDefinitions.ISO.UNKNOWN;
    }

    public enum ShootPhotoTimeoutLock {
        TIMEOUT_LOCK_0(0),
        TIMEOUT_LOCK_1(1),
        TIMEOUT_LOCK_2(2);
        
        private CountDownLatch countDownLatch;
        private int index = 0;
        private boolean result = false;
        private boolean threadInitiatedState = false;

        private ShootPhotoTimeoutLock(int index2) {
            this.index = index2;
        }

        private boolean _equals(int b) {
            return this.index == b;
        }

        public static ShootPhotoTimeoutLock getInstance(int index2) {
            ShootPhotoTimeoutLock result2 = TIMEOUT_LOCK_0;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(index2)) {
                    return values()[i];
                }
            }
            return result2;
        }

        public void waitResult() {
            try {
                this.countDownLatch.await(2000, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void reset() {
            this.countDownLatch = new CountDownLatch(1);
            this.result = false;
            this.threadInitiatedState = true;
        }

        public void setResult(boolean ret) {
            this.countDownLatch.countDown();
            this.result = ret;
            this.threadInitiatedState = false;
        }

        public boolean getResult() {
            return this.result;
        }

        public boolean getThreadInitiatedState() {
            return this.threadInitiatedState;
        }
    }

    public enum RecordVideoTimeoutLock {
        TIMEOUT_LOCK_0(0),
        TIMEOUT_LOCK_1(1),
        TIMEOUT_LOCK_2(2);
        
        private CountDownLatch countDownLatch;
        private int index = 0;
        private boolean result = false;
        private boolean threadInitiatedState = false;

        private RecordVideoTimeoutLock(int index2) {
            this.index = index2;
        }

        private boolean _equals(int b) {
            return this.index == b;
        }

        public static RecordVideoTimeoutLock getInstance(int index2) {
            RecordVideoTimeoutLock result2 = TIMEOUT_LOCK_0;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(index2)) {
                    return values()[i];
                }
            }
            return result2;
        }

        public void waitResult() {
            try {
                this.countDownLatch.await(2000, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void reset() {
            this.countDownLatch = new CountDownLatch(1);
            this.result = false;
            this.threadInitiatedState = true;
        }

        public void setResult(boolean ret) {
            this.countDownLatch.countDown();
            this.result = ret;
            this.threadInitiatedState = false;
        }

        public boolean getResult() {
            return this.result;
        }

        public boolean getThreadInitiatedState() {
            return this.threadInitiatedState;
        }
    }

    public static boolean isSDCardReady(int index) {
        if (!CacheHelper.toBool(CacheHelper.getCamera(index, CameraKeys.SDCARD_IS_INSERTED)) || CacheHelper.toBool(CacheHelper.getCamera(index, CameraKeys.SDCARD_IS_INITIALIZING)) || CacheHelper.toBool(CacheHelper.getCamera(index, CameraKeys.SDCARD_IS_READ_ONLY)) || CacheHelper.toBool(CacheHelper.getCamera(index, CameraKeys.SDCARD_HAS_ERROR)) || CacheHelper.toBool(CacheHelper.getCamera(index, CameraKeys.SDCARD_IS_FULL)) || CacheHelper.toBool(CacheHelper.getCamera(index, CameraKeys.SDCARD_IS_BUSY)) || CacheHelper.toBool(CacheHelper.getCamera(index, CameraKeys.SDCARD_IS_FORMATTING)) || CacheHelper.toBool(CacheHelper.getCamera(index, CameraKeys.SDCARD_IS_INVALID_FORMAT)) || !CacheHelper.toBool(CacheHelper.getCamera(index, CameraKeys.SDCARD_IS_VERIFIED))) {
            return false;
        }
        return true;
    }

    public static boolean isStorageAvailable(int index) {
        DataCameraGetStateInfo.SDCardState storageState;
        boolean isInserted;
        if (!CacheHelper.toBool(CacheHelper.getCamera(index, CameraKeys.IS_INTERNAL_STORAGE_SUPPORTED)) || !DataCameraGetPushStorageInfo.getInstance().isGetted()) {
            return isSDCardReady(index);
        }
        DataCameraGetPushStorageInfo storageInfo = DataCameraGetPushStorageInfo.getInstance();
        if (storageInfo.getStorageLocation() == DataCameraSetStorageInfo.Storage.INNER_STORAGE) {
            storageState = storageInfo.getInnerStorageStatus();
            isInserted = storageInfo.getInnerStorageInsertStatus();
        } else {
            storageState = storageInfo.getSDCardState();
            isInserted = storageInfo.getSDCardInsertState();
        }
        return isInserted && !(storageState == DataCameraGetStateInfo.SDCardState.Initialzing) && !(storageState == DataCameraGetStateInfo.SDCardState.WriteProtection) && !(storageState == DataCameraGetStateInfo.SDCardState.Invalid || storageState == DataCameraGetStateInfo.SDCardState.Illegal || storageState == DataCameraGetStateInfo.SDCardState.Unknow) && !(storageState == DataCameraGetStateInfo.SDCardState.Full) && !(storageState == DataCameraGetStateInfo.SDCardState.Busy) && !(storageState == DataCameraGetStateInfo.SDCardState.Formating) && !(storageState == DataCameraGetStateInfo.SDCardState.Illegal) && (storageState != DataCameraGetStateInfo.SDCardState.Invalid);
    }

    public static boolean isProductSupportPanoSphere() {
        ProductType type = DJIProductManager.getInstance().getType();
        long version = DataEyeGetPushException.getInstance().getVisionVersion();
        switch (type) {
            case Mammoth:
                if (version < IVisionResDefine.SupportVersion.VERSION_VISION_SPARK_PANO_SPHERE) {
                    return false;
                }
                return true;
            case KumquatX:
                if (version < IVisionResDefine.SupportVersion.VERSION_VISION_KUMQUATX_PANO) {
                    return false;
                }
                return true;
            case Potato:
                if (version < IVisionResDefine.SupportVersion.VERSION_VISION_POTATO_PANO) {
                    return false;
                }
                return true;
            case Pomato:
                if (version < IVisionResDefine.SupportVersion.VERSION_VISION_POMATO_PANO) {
                    return false;
                }
                return true;
            case WM230:
            case WM240:
            case WM245:
            case PomatoSDR:
                return true;
            default:
                return false;
        }
    }

    public static boolean isInActionMode(int index) {
        return (CacheHelper.getCamera(index, "Mode") == SettingsDefinitions.CameraMode.MEDIA_DOWNLOAD || CacheHelper.getCamera(index, "Mode") == SettingsDefinitions.CameraMode.PLAYBACK) ? false : true;
    }

    public static boolean isPhotoActionExecutable(int index) {
        if (!CacheHelper.toBool(CacheHelper.getCamera(index, CameraKeys.IS_SHOOTING_PHOTO_ENABLED)) || CacheHelper.toBool(CacheHelper.getCamera(index, CameraKeys.IS_STORING_PHOTO)) || CacheHelper.toBool(CacheHelper.getCamera(index, CameraKeys.IS_SHOOTING_BURST_PHOTO)) || CacheHelper.toBool(CacheHelper.getCamera(index, CameraKeys.IS_SHOOTING_INTERVAL_PHOTO)) || CacheHelper.toBool(CacheHelper.getCamera(index, "IsShootingRawBurstPhoto")) || CacheHelper.toBool(CacheHelper.getCamera(index, CameraKeys.IS_SHOOTING_SINGLE_PHOTO)) || CacheHelper.toBool(CacheHelper.getCamera(index, CameraKeys.IS_SHOOTING_SINGLE_PHOTO_IN_RAW_FORMAT))) {
            return false;
        }
        return true;
    }

    public static boolean isRecordActionExecutable(int index) {
        return isPhotoActionExecutable(index) && !CacheHelper.toBool(CacheHelper.getCamera(index, CameraKeys.IS_RECORDING));
    }

    public static boolean isShootingPhoto(int index) {
        if (CacheHelper.toBool(CacheHelper.getCamera(index, CameraKeys.IS_SHOOTING_PHOTO)) || CacheHelper.toBool(CacheHelper.getCamera(index, CameraKeys.IS_SHOOTING_BURST_PHOTO)) || CacheHelper.toBool(CacheHelper.getCamera(index, CameraKeys.IS_SHOOTING_INTERVAL_PHOTO)) || CacheHelper.toBool(CacheHelper.getCamera(index, "IsShootingRawBurstPhoto")) || CacheHelper.toBool(CacheHelper.getCamera(index, CameraKeys.IS_SHOOTING_SINGLE_PHOTO)) || CacheHelper.toBool(CacheHelper.getCamera(index, CameraKeys.IS_SHOOTING_SINGLE_PHOTO_IN_RAW_FORMAT))) {
            return true;
        }
        return false;
    }

    public static SettingsDefinitions.CameraType getCameraType(int componentIndex) {
        DJISDKCacheParamValue paramValue = DJISDKCache.getInstance().getAvailableValue(KeyHelper.getCameraKey(componentIndex, CameraKeys.CAMERA_TYPE));
        if (paramValue != null) {
            return (SettingsDefinitions.CameraType) paramValue.getData();
        }
        return null;
    }

    public static SettingsDefinitions.ExposureSensitivityMode getExposureSensitivityMode(int componentIndex) {
        DJISDKCacheParamValue paramValue = DJISDKCache.getInstance().getAvailableValue(KeyHelper.getCameraKey(componentIndex, CameraKeys.EXPOSURE_SENSITIVITY_MODE));
        if (paramValue != null) {
            return (SettingsDefinitions.ExposureSensitivityMode) paramValue.getData();
        }
        return null;
    }

    public static boolean getSSDVideoRecordingEnabled(int componentIndex) {
        DJISDKCacheParamValue paramValue = DJISDKCache.getInstance().getAvailableValue(KeyHelper.getCameraKey(componentIndex, CameraKeys.SSD_VIDEO_RECORDING_ENABLED));
        if (paramValue != null) {
            return ((Boolean) paramValue.getData()).booleanValue();
        }
        return false;
    }

    public static CameraSSDVideoLicense getSSDVideoLicense(int componentIndex) {
        DJISDKCacheParamValue paramValue = DJISDKCache.getInstance().getAvailableValue(KeyHelper.getCameraKey(componentIndex, CameraKeys.ACTIVATE_SSD_VIDEO_LICENSE));
        if (paramValue != null) {
            return (CameraSSDVideoLicense) paramValue.getData();
        }
        return CameraSSDVideoLicense.Unknown;
    }

    public static boolean isSupportMultiStorage(SettingsDefinitions.CameraType cameraType) {
        return SettingsDefinitions.CameraType.DJICameraTypeFC230 == cameraType || SettingsDefinitions.CameraType.DJICameraTypeFC240 == cameraType || SettingsDefinitions.CameraType.DJICameraTypeFC2403 == cameraType || SettingsDefinitions.CameraType.DJICameraTypeFC240_1 == cameraType || SettingsDefinitions.CameraType.DJICameraTypeFC245_IMX477 == cameraType;
    }

    public static boolean isSupportWatermark(SettingsDefinitions.CameraType cameraType) {
        return SettingsDefinitions.CameraType.DJICameraTypeFC245_IMX477 == cameraType || SettingsDefinitions.CameraType.DJICameraTypeFC2403 == cameraType;
    }

    public static boolean isSupportHistogram(SettingsDefinitions.CameraType cameraType) {
        if (cameraType == SettingsDefinitions.CameraType.DJICameraTypeFC6510 || cameraType == SettingsDefinitions.CameraType.DJICameraTypeFC6520 || cameraType == SettingsDefinitions.CameraType.DJICameraTypeFC1705 || cameraType == SettingsDefinitions.CameraType.DJICameraTypeFC6540 || cameraType == SettingsDefinitions.CameraType.DJICameraTypeFC2403 || isWM240Camera(cameraType) || isWM245ZoomCamera(cameraType)) {
            return true;
        }
        return false;
    }
}
