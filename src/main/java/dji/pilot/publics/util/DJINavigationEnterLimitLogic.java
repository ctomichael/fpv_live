package dji.pilot.publics.util;

import dji.common.camera.SettingsDefinitions;
import dji.log.DJILog;
import dji.midware.data.model.P3.DataOsdGetPushCommon;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.keycatalog.CameraKeys;
import dji.sdksharedlib.keycatalog.IntelligentFlightAssistantKeys;

public class DJINavigationEnterLimitLogic {
    private static final String TAG = "DJINavigationEnterLimitLogic";

    public static boolean isPhotoPanoramaMode() {
        SettingsDefinitions.PhotoPanoramaMode panoMode = (SettingsDefinitions.PhotoPanoramaMode) CacheHelper.getCamera(CameraKeys.PHOTO_PANORAMA_MODE);
        DJILog.logWriteD(TAG, " isPhotoPanoramaMode panoMode : " + panoMode, TAG, new Object[0]);
        if (panoMode == null || panoMode == SettingsDefinitions.PhotoPanoramaMode.UNKNOWN) {
            return false;
        }
        return true;
    }

    public static boolean isDownVpsEnabled() {
        return CacheHelper.toBool(CacheHelper.getFlightAssistant(IntelligentFlightAssistantKeys.VISION_ASSISTED_POSITIONING_ENABLED));
    }

    public static boolean isSensorAssistantEnabled() {
        return CacheHelper.toBool(CacheHelper.getFlightAssistant(IntelligentFlightAssistantKeys.INTELLIGENT_FLIGHT_ASSISTANT_IS_USERAVOID_ENABLE));
    }

    public static boolean isRecording() {
        return CacheHelper.toBool(CacheHelper.getCamera(CameraKeys.IS_RECORDING));
    }

    public static boolean isOrientationPortrait() {
        SettingsDefinitions.Orientation currentRotationMode = (SettingsDefinitions.Orientation) CacheHelper.getCamera(CameraKeys.ORIENTATION);
        DJILog.logWriteD(TAG, " isOrientationPortrait orientation : " + currentRotationMode, new Object[0]);
        if (currentRotationMode == null || !currentRotationMode.equals(SettingsDefinitions.Orientation.PORTRAIT)) {
            return false;
        }
        return true;
    }

    public static boolean isPhotoSingleMode() {
        SettingsDefinitions.ShootPhotoMode shootPhotoMode = (SettingsDefinitions.ShootPhotoMode) CacheHelper.getCamera(CameraKeys.SHOOT_PHOTO_MODE);
        DJILog.logWriteD(TAG, " ShootPhotoMode: " + shootPhotoMode, TAG, new Object[0]);
        if (shootPhotoMode == null || !SettingsDefinitions.ShootPhotoMode.SINGLE.equals(shootPhotoMode)) {
            return false;
        }
        return true;
    }

    public static boolean isNavigationSwitchCameraMode() {
        return DJICommonUtils.isWM240();
    }

    public static boolean isShootPhotoModeInPanorama() {
        SettingsDefinitions.ShootPhotoMode shootPhotoMode = (SettingsDefinitions.ShootPhotoMode) CacheHelper.getCamera(CameraKeys.SHOOT_PHOTO_MODE);
        if (shootPhotoMode == null || !SettingsDefinitions.ShootPhotoMode.PANORAMA.equals(shootPhotoMode)) {
            return false;
        }
        return true;
    }

    public static boolean isShootPhotoModeInTimeLapse() {
        SettingsDefinitions.ShootPhotoMode shootPhotoMode = (SettingsDefinitions.ShootPhotoMode) CacheHelper.getCamera(CameraKeys.SHOOT_PHOTO_MODE);
        if (shootPhotoMode == null || !SettingsDefinitions.ShootPhotoMode.HYPER_LAPSE.equals(shootPhotoMode)) {
            return false;
        }
        return true;
    }

    public static boolean isGoHomeNow() {
        DataOsdGetPushCommon common = DataOsdGetPushCommon.getInstance();
        return common.getFlycState() == DataOsdGetPushCommon.FLYC_STATE.GoHome || common.getFlycState() == DataOsdGetPushCommon.FLYC_STATE.AutoLanding;
    }

    public static boolean isSeriousLowBattery() {
        return DataOsdGetPushCommon.getInstance().getVoltageWarning() == 2;
    }

    public static boolean isInPoseMode() {
        return DataOsdGetPushCommon.getInstance().getFlycState() == DataOsdGetPushCommon.FLYC_STATE.Atti;
    }

    public static boolean isInNoviceMode() {
        return DataOsdGetPushCommon.getInstance().getFlycState() == DataOsdGetPushCommon.FLYC_STATE.NOVICE;
    }

    public static boolean isTakeoffNow() {
        DataOsdGetPushCommon common = DataOsdGetPushCommon.getInstance();
        return common.getFlycState() == DataOsdGetPushCommon.FLYC_STATE.AssitedTakeoff || common.getFlycState() == DataOsdGetPushCommon.FLYC_STATE.AutoTakeoff;
    }

    public static boolean isShootingPhoto() {
        return CacheHelper.toBool(CacheHelper.getCamera(CameraKeys.IS_SHOOTING_PHOTO));
    }
}
