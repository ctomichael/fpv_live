package dji.pilot.fpv.camera.util;

import android.content.Context;
import android.support.annotation.Nullable;
import dji.common.camera.CameraRecordingState;
import dji.common.camera.SettingsDefinitions;
import dji.common.product.Model;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILogHelper;
import dji.log.DJILogPaths;
import dji.logic.manager.DJIUSBWifiSwitchManager;
import dji.midware.component.DJIComponentManager;
import dji.midware.data.config.P3.CmdIdCamera;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.model.P3.DataBaseCameraSetting;
import dji.midware.data.model.P3.DataCameraGetImageSize;
import dji.midware.data.model.P3.DataCameraGetMode;
import dji.midware.data.model.P3.DataCameraGetPushRawParams;
import dji.midware.data.model.P3.DataCameraGetPushShotInfo;
import dji.midware.data.model.P3.DataCameraGetPushShotParams;
import dji.midware.data.model.P3.DataCameraGetPushStateInfo;
import dji.midware.data.model.P3.DataCameraGetPushStorageInfo;
import dji.midware.data.model.P3.DataCameraGetStateInfo;
import dji.midware.data.model.P3.DataCameraSetExposureMode;
import dji.midware.data.model.P3.DataEyeGetPushPseudoCameraParams;
import dji.midware.data.model.P3.DataEyeGetPushStabilizationState;
import dji.midware.data.model.P3.DataSingleSendAppStateForStabilization;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.util.AutoVideoSizeCalculator;
import dji.midware.util.DjiSharedPreferencesManager;
import dji.pilot.configs.ICommonConfig;
import dji.pilot.fpv.camera.ICameraDefine;
import dji.pilot.fpv.util.DJICommonUtil;
import dji.pilot.fpv.util.DJIVisionUtil;
import dji.pilot.publics.BuildConfig;
import dji.pilot.publics.R;
import dji.pilot.publics.util.DJICommonUtils;
import dji.pilot.publics.util.DJIPublicUtils;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.extension.KeyHelper;
import dji.sdksharedlib.keycatalog.CameraKeys;

@EXClassNullAway
public class DJICameraUtil implements ICameraDefine {
    public static double FPS_VALUE_FOR_TIME_LAPSE_NOT_FREE = 0.47619047619047616d;
    private static final boolean LOG_SYSTEM = false;
    private static final String TAG = "Camera";

    public static final void printLog(String tag, String msg) {
        String realTag;
        if (DJIPublicUtils.isEmpty(tag)) {
            realTag = "Camera";
        } else {
            realTag = tag;
        }
        if (BuildConfig.DEBUG) {
        }
        DJILogHelper.getInstance().LOGD(realTag, tag + "-" + msg, DJILogPaths.LOG_EXTRA_CAMERA);
    }

    public static boolean checkStorageCanWork(SettingsDefinitions.SDCardStateOperationState state) {
        return state == SettingsDefinitions.SDCardStateOperationState.NORMAL || state == SettingsDefinitions.SDCardStateOperationState.SLOW || state == SettingsDefinitions.SDCardStateOperationState.FORMAT_RECOMMENDED || state == SettingsDefinitions.SDCardStateOperationState.WRITING_SLOWLY;
    }

    public static boolean isOverHeated(DataCameraGetPushStateInfo.CameraTemperatureState temperatureState) {
        return temperatureState == DataCameraGetPushStateInfo.CameraTemperatureState.OVER_HEAT_LEVEL_2 || temperatureState == DataCameraGetPushStateInfo.CameraTemperatureState.OVER_HEAT_LEVEL_3;
    }

    public static boolean checkStorageValid(SettingsDefinitions.SDCardStateOperationState state) {
        return state == SettingsDefinitions.SDCardStateOperationState.NORMAL || state == SettingsDefinitions.SDCardStateOperationState.SLOW || state == SettingsDefinitions.SDCardStateOperationState.FORMAT_RECOMMENDED || state == SettingsDefinitions.SDCardStateOperationState.FULL || state == SettingsDefinitions.SDCardStateOperationState.NO_REMAIN_FILE_INDICES || state == SettingsDefinitions.SDCardStateOperationState.WRITING_SLOWLY;
    }

    public static boolean isSupportLocalCamera(Model model) {
        return true;
    }

    public static boolean checkSdError(SettingsDefinitions.SDCardStateOperationState sdCardState) {
        return SettingsDefinitions.SDCardStateOperationState.INVALID == sdCardState || SettingsDefinitions.SDCardStateOperationState.INVALID_FILE_SYSTEM == sdCardState || SettingsDefinitions.SDCardStateOperationState.UNKNOWN_ERROR == sdCardState;
    }

    public static boolean isCtrObjectOneV2(DataCameraGetPushStateInfo.CameraType type) {
        if (type == null) {
            type = DataCameraGetPushStateInfo.getInstance().getCameraType();
        }
        return DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6310 == type || type == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6510 || type == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6520 || type == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6540;
    }

    public static boolean isNotSupportCtrObject(DataCameraGetPushStateInfo.CameraType type) {
        if (DJIComponentManager.getInstance().isRcRM500()) {
            return false;
        }
        if (type == null) {
            type = DataCameraGetPushStateInfo.getInstance().getCameraType();
        }
        if (DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC240 == type || type == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC240_1) {
            return true;
        }
        return false;
    }

    public static boolean isStreamNonLong(DataCameraGetPushStateInfo.CameraType cType) {
        return checkIsTauCamera(cType) || DataCameraGetPushStateInfo.CameraType.DJICameraTypeCV600 == cType;
    }

    public static boolean nonStreamBlackBoard(DataCameraGetPushStateInfo.CameraType cType) {
        return DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC260 == cType || DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC300S == cType || DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC300XW == cType || DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC220S == cType || isP4ProCamera(cType) || isStreamNonLong(cType);
    }

    public static boolean isCur4kVideo() {
        int videoFormat = DataCameraGetPushShotParams.getInstance().getVideoFormat(new int[0]);
        if ((videoFormat < 14 || videoFormat > 23) && videoFormat != 27 && videoFormat != 28 && videoFormat != 32 && videoFormat != 33) {
            return false;
        }
        if (DataCameraGetPushStateInfo.getInstance().getMode() == DataCameraGetMode.MODE.RECORD || DataCameraGetPushStateInfo.getInstance().getMode() == DataCameraGetMode.MODE.BROADCAST) {
            return true;
        }
        return false;
    }

    public static boolean isSupportSSD(DataCameraGetPushStateInfo.CameraType type) {
        if (type == null) {
            type = DataCameraGetPushStateInfo.getInstance().getCameraType();
        }
        return type == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC550Raw || (isInspire2RawCamera(type) && ProductType.Orange2 == DJIProductManager.getInstance().getType());
    }

    public static boolean isCameraOsdShowWb() {
        return DataCameraGetPushStateInfo.getInstance().getCameraType() != DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC1102;
    }

    public static boolean supportProfile(DataCameraGetPushStateInfo.CameraType type) {
        return isP4ProCamera(type);
    }

    public static boolean needShowRealFocusArea(DataCameraGetPushStateInfo.CameraType type) {
        return type == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC550 || type == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC550Raw || type == DataCameraGetPushStateInfo.CameraType.DJICameraTypeCV600;
    }

    public static DataCameraGetStateInfo.SDCardState getSDCardState() {
        if (isSupportMultiStorage()) {
            return DataCameraGetPushStorageInfo.getInstance().getSDCardState();
        }
        return DataCameraGetPushStateInfo.getInstance().getSDCardState(true);
    }

    public static int getSDCardStatusResId(DataCameraGetStateInfo.SDCardState state) {
        int resId = R.string.sdcardstatus_unknown_error;
        if (state == DataCameraGetStateInfo.SDCardState.Normal) {
            return R.string.sdcardstatus_normal;
        }
        if (state == DataCameraGetStateInfo.SDCardState.None) {
            return R.string.sdcardstatus_removal;
        }
        if (state == DataCameraGetStateInfo.SDCardState.Invalid) {
            return R.string.sdcardstatus_invalid;
        }
        if (state == DataCameraGetStateInfo.SDCardState.WriteProtection) {
            return R.string.sdcardstatus_write_protect;
        }
        if (state == DataCameraGetStateInfo.SDCardState.Unformat) {
            return R.string.sdcardstatus_not_formated;
        }
        if (state == DataCameraGetStateInfo.SDCardState.Formating) {
            return R.string.sdcardstatus_formating;
        }
        if (state == DataCameraGetStateInfo.SDCardState.Illegal) {
            return R.string.sdcardstatus_invalid_filesystem;
        }
        if (state == DataCameraGetStateInfo.SDCardState.Busy) {
            return R.string.sdcardstatus_busy;
        }
        if (state == DataCameraGetStateInfo.SDCardState.Full) {
            return R.string.sdcardstatus_full;
        }
        if (state == DataCameraGetStateInfo.SDCardState.Slow) {
            return R.string.sdcardstatus_slow;
        }
        if (state == DataCameraGetStateInfo.SDCardState.Unknow) {
            return R.string.sdcardstatus_unknown_error;
        }
        if (state == DataCameraGetStateInfo.SDCardState.IndexMax) {
            return R.string.sdcardstatus_full;
        }
        if (state == DataCameraGetStateInfo.SDCardState.Initialzing) {
            return R.string.sdcardstatus_initial;
        }
        if (state == DataCameraGetStateInfo.SDCardState.ToFormat) {
            return R.string.sdcardstatus_toformat;
        }
        if (state == DataCameraGetStateInfo.SDCardState.USBConnected) {
            return R.string.sdcardstatus_usb_connected;
        }
        if (state == DataCameraGetStateInfo.SDCardState.TryToRecoverFile) {
            return R.string.sdcardstatus_recover_file;
        }
        if (state == DataCameraGetStateInfo.SDCardState.BecomeSlow) {
            return R.string.sdcardstatus_write_slow;
        }
        return resId;
    }

    public static int getRomStatusResId(DataCameraGetStateInfo.SDCardState state) {
        int resId = R.string.romstatus_unknown_error;
        if (state == DataCameraGetStateInfo.SDCardState.Normal) {
            return R.string.romstatus_normal;
        }
        if (state == DataCameraGetStateInfo.SDCardState.None) {
            return R.string.romstatus_removal;
        }
        if (state == DataCameraGetStateInfo.SDCardState.Invalid) {
            return R.string.romstatus_invalid;
        }
        if (state == DataCameraGetStateInfo.SDCardState.WriteProtection) {
            return R.string.romstatus_write_protect;
        }
        if (state == DataCameraGetStateInfo.SDCardState.Unformat) {
            return R.string.romstatus_not_formated;
        }
        if (state == DataCameraGetStateInfo.SDCardState.Formating) {
            return R.string.romstatus_formating;
        }
        if (state == DataCameraGetStateInfo.SDCardState.Illegal) {
            return R.string.romstatus_invalid_filesystem;
        }
        if (state == DataCameraGetStateInfo.SDCardState.Busy) {
            return R.string.romstatus_busy;
        }
        if (state == DataCameraGetStateInfo.SDCardState.Full) {
            return R.string.romstatus_full;
        }
        if (state == DataCameraGetStateInfo.SDCardState.Slow) {
            return R.string.romstatus_slow;
        }
        if (state == DataCameraGetStateInfo.SDCardState.Unknow) {
            return R.string.romstatus_unknown_error;
        }
        if (state == DataCameraGetStateInfo.SDCardState.IndexMax) {
            return R.string.romstatus_full;
        }
        if (state == DataCameraGetStateInfo.SDCardState.Initialzing) {
            return R.string.romstatus_initial;
        }
        if (state == DataCameraGetStateInfo.SDCardState.ToFormat) {
            return R.string.romstatus_toformat;
        }
        if (state == DataCameraGetStateInfo.SDCardState.USBConnected) {
            return R.string.romstatus_usb_connected;
        }
        if (state == DataCameraGetStateInfo.SDCardState.TryToRecoverFile) {
            return R.string.romstatus_recover_file;
        }
        if (state == DataCameraGetStateInfo.SDCardState.BecomeSlow) {
            return R.string.romstatus_write_slow;
        }
        return resId;
    }

    public static int getChecklistStatusResId(DataCameraGetStateInfo.SDCardState state) {
        int resId = R.string.checklist_romstatus_unknown_error;
        if (state == DataCameraGetStateInfo.SDCardState.Normal) {
            return R.string.checklist_romstatus_normal;
        }
        if (state == DataCameraGetStateInfo.SDCardState.None) {
            return R.string.checklist_romstatus_removal;
        }
        if (state == DataCameraGetStateInfo.SDCardState.Invalid) {
            return R.string.checklist_romstatus_invalid;
        }
        if (state == DataCameraGetStateInfo.SDCardState.WriteProtection) {
            return R.string.checklist_romstatus_write_protect;
        }
        if (state == DataCameraGetStateInfo.SDCardState.Unformat) {
            return R.string.checklist_romstatus_not_formated;
        }
        if (state == DataCameraGetStateInfo.SDCardState.Formating) {
            return R.string.checklist_romstatus_formating;
        }
        if (state == DataCameraGetStateInfo.SDCardState.Illegal) {
            return R.string.checklist_romstatus_invalid_filesystem;
        }
        if (state == DataCameraGetStateInfo.SDCardState.Busy) {
            return R.string.checklist_romstatus_busy;
        }
        if (state == DataCameraGetStateInfo.SDCardState.Full) {
            return R.string.checklist_romstatus_full;
        }
        if (state == DataCameraGetStateInfo.SDCardState.Slow) {
            return R.string.checklist_romstatus_slow;
        }
        if (state == DataCameraGetStateInfo.SDCardState.Unknow) {
            return R.string.checklist_romstatus_unknown_error;
        }
        if (state == DataCameraGetStateInfo.SDCardState.IndexMax) {
            return R.string.checklist_romstatus_full;
        }
        if (state == DataCameraGetStateInfo.SDCardState.Initialzing) {
            return R.string.checklist_romstatus_initial;
        }
        if (state == DataCameraGetStateInfo.SDCardState.ToFormat) {
            return R.string.checklist_romstatus_toformat;
        }
        if (state == DataCameraGetStateInfo.SDCardState.USBConnected) {
            return R.string.romstatus_usb_connected;
        }
        if (state == DataCameraGetStateInfo.SDCardState.TryToRecoverFile) {
            return R.string.romstatus_recover_file;
        }
        if (state == DataCameraGetStateInfo.SDCardState.BecomeSlow) {
            return R.string.romstatus_write_slow;
        }
        return resId;
    }

    public static boolean checkSdCardValid(DataCameraGetStateInfo.SDCardState state) {
        if (DataCameraGetStateInfo.SDCardState.Normal == state || DataCameraGetStateInfo.SDCardState.Slow == state || DataCameraGetStateInfo.SDCardState.ToFormat == state) {
            return true;
        }
        return false;
    }

    public static boolean checkSdCardNormal(DataCameraGetStateInfo.SDCardState state) {
        if (DataCameraGetStateInfo.SDCardState.Normal == state || DataCameraGetStateInfo.SDCardState.Slow == state || DataCameraGetStateInfo.SDCardState.ToFormat == state || DataCameraGetStateInfo.SDCardState.Formating == state || DataCameraGetStateInfo.SDCardState.Initialzing == state) {
            return true;
        }
        return false;
    }

    public static boolean checkInnerStorageValid(DataCameraGetStateInfo.SDCardState state) {
        if (DataCameraGetStateInfo.SDCardState.Normal == state || DataCameraGetStateInfo.SDCardState.Slow == state) {
            return true;
        }
        return false;
    }

    public static boolean isSupportMultiStorage() {
        SettingsDefinitions.CameraType cameraType = (SettingsDefinitions.CameraType) CacheHelper.getCamera(CameraKeys.CAMERA_TYPE);
        return SettingsDefinitions.CameraType.DJICameraTypeFC230 == cameraType || SettingsDefinitions.CameraType.DJICameraTypeFC240 == cameraType || SettingsDefinitions.CameraType.DJICameraTypeFC240_1 == cameraType;
    }

    public static boolean supportCameraModeS(DataCameraGetPushStateInfo.CameraType type) {
        return ICommonConfig.isForFactory || DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC350 == type || type == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC550 || type == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC550Raw || type == DataCameraGetPushStateInfo.CameraType.DJICameraTypeCV600 || isP4ProCamera(type) || isInspire2Camera(type) || type == DataCameraGetPushStateInfo.CameraType.DJICameraTypeGD600 || type == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC240_1;
    }

    public static boolean supportCameraModeC(DataCameraGetPushStateInfo.CameraType type) {
        return type == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC550 || type == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC550Raw;
    }

    public static boolean supportCameraFocus(DataCameraGetPushStateInfo.CameraType type) {
        boolean isProductWifiConnected = DJIUSBWifiSwitchManager.getInstance().isProductWifiConnected(null);
        return type == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC550 || type == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC550Raw || type == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC220 || type == DataCameraGetPushStateInfo.CameraType.DJICameraTypeCV600 || type == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC220S || isP4ProCamera(type) || type == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC240 || type == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC240_1 || isInspire2Camera(type) || type == DataCameraGetPushStateInfo.CameraType.DJICameraTypeGD600;
    }

    public static boolean needTipOverExposureOnlyCurrent(DataCameraGetPushStateInfo.CameraType type) {
        return type == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC550 || type == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC550Raw || type == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC220 || type == DataCameraGetPushStateInfo.CameraType.DJICameraTypeCV600 || type == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC220S || isP4ProCamera(type) || isInspire2Camera(type) || type == DataCameraGetPushStateInfo.CameraType.DJICameraTypeGD600;
    }

    public static boolean supportMF(DataCameraGetPushStateInfo.CameraType type) {
        return isInspire2Camera(type) || type == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC550 || type == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC550Raw || DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC220 == type || DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC220S == type || DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC240 == type || DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC240_1 == type || isP4ProCamera(type) || type == DataCameraGetPushStateInfo.CameraType.DJICameraTypeGD600;
    }

    public static boolean needMFDemarcate(DataCameraGetPushStateInfo.CameraType type) {
        return type == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC550 || type == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC550Raw || type == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6520 || type == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6540;
    }

    public static boolean supportISOAuto(DataCameraGetPushStateInfo.CameraType type) {
        if (type == null) {
            type = DataCameraGetPushStateInfo.getInstance().getCameraType();
        }
        return isInspire2Camera(type) || type == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC550 || type == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC550Raw || isP4ProCamera(type) || type == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC240_1;
    }

    public static boolean isSupportSaveOriginalPano() {
        return DJICommonUtils.isWM230() || DJICommonUtils.isWM240();
    }

    public static boolean supportFocusDistance(DataCameraGetPushStateInfo.CameraType cType, DataCameraGetPushShotInfo.ZoomFocusType fType) {
        return (cType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6520 || cType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6540 || cType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC240 || cType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC550 || cType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC550Raw) && fType == DataCameraGetPushShotInfo.ZoomFocusType.AutoZoomFocus;
    }

    public static boolean supportOneKeyInfinity(DataCameraGetPushStateInfo.CameraType cType) {
        if (cType == null) {
            cType = DataCameraGetPushStateInfo.getInstance().getCameraType();
        }
        return cType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC240 || cType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC240_1;
    }

    public static boolean supportElectricFocus(DataCameraGetPushShotInfo.ZoomFocusType type, DataCameraGetPushStateInfo.CameraType cType) {
        return type == DataCameraGetPushShotInfo.ZoomFocusType.AutoZoomFocus && (cType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeCV600 || cType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeGD600);
    }

    public static boolean supportAperture(DataCameraGetPushStateInfo.CameraType type) {
        return type == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC550 || type == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC550Raw || isP4ProCamera(type) || isInspire2Camera(type) || type == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC240_1 || type == DataCameraGetPushStateInfo.CameraType.DJICameraTypeGD600;
    }

    public static boolean hasLineRef(DataCameraGetPushStateInfo.CameraType type) {
        return DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC550 == type || DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC550Raw == type || DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6520 == type || type == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6540 || DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6510 == type;
    }

    public static boolean checkIsTauCamera(DataCameraGetPushStateInfo.CameraType type) {
        return DJICommonUtils.checkIsTauCamera(type);
    }

    public static boolean checkIsGDCamera(DataCameraGetPushStateInfo.CameraType type) {
        if (type == null) {
            type = DataCameraGetPushStateInfo.getInstance().getCameraType();
        }
        return type == DataCameraGetPushStateInfo.CameraType.DJICameraTypeGD600;
    }

    public static boolean checkIsSparkCamera(DataCameraGetPushStateInfo.CameraType type) {
        if (type == null) {
            type = DataCameraGetPushStateInfo.getInstance().getCameraType();
        }
        return type == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC1102;
    }

    public static boolean checkIsWM230Camera(DataCameraGetPushStateInfo.CameraType type) {
        if (type == null) {
            type = DataCameraGetPushStateInfo.getInstance().getCameraType();
        }
        return type == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC230;
    }

    public static boolean checkIsWM240Camera(DataCameraGetPushStateInfo.CameraType type) {
        if (type == null) {
            type = DataCameraGetPushStateInfo.getInstance().getCameraType();
        }
        return type == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC240 || type == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC240_1;
    }

    public static boolean isSupportDigitalZoom() {
        DataCameraGetPushStateInfo.CameraType type = DataCameraGetPushStateInfo.getInstance().getCameraType();
        if (type == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC220S) {
            return true;
        }
        int version = DataCameraGetPushStateInfo.getInstance().getVerstion();
        if ((((type == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC350 || type == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC220 || type == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC330X) && version >= 4) || type == DataCameraGetPushStateInfo.CameraType.DJICameraTypeCV600) && !isCur4kVideo()) {
            return true;
        }
        return false;
    }

    public static boolean isCameraSupportDigitalZoom() {
        DataCameraGetPushStateInfo.CameraType cType = DataCameraGetPushStateInfo.getInstance().getCameraType();
        return cType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC350 || cType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeCV600 || cType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC220 || cType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC220S || cType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC330X;
    }

    public static boolean caseSSDException() {
        DataCameraGetPushRawParams rawPush = DataCameraGetPushRawParams.getInstance();
        if (!rawPush.isGetted() || rawPush.getDiskStatus() == DataCameraGetPushRawParams.DiskStatus.NOTCONNECTED || rawPush.getDiskStatus() == DataCameraGetPushRawParams.DiskStatus.WAITING || rawPush.getDiskStatus() == DataCameraGetPushRawParams.DiskStatus.STORING) {
            return false;
        }
        return true;
    }

    public static boolean isSSDCardInvalid(DataCameraGetPushRawParams.DiskStatus diskStatus) {
        if (diskStatus == null) {
            DataCameraGetPushRawParams rawParams = DataCameraGetPushRawParams.getInstance();
            if (!rawParams.isGetted()) {
                return true;
            }
            diskStatus = rawParams.getDiskStatus();
        }
        if (DataCameraGetPushRawParams.DiskStatus.NOTCONNECTED == diskStatus || DataCameraGetPushRawParams.DiskStatus.NA == diskStatus || DataCameraGetPushRawParams.DiskStatus.DEVICE_ERROR == diskStatus || DataCameraGetPushRawParams.DiskStatus.VERIFY_ERROR == diskStatus || DataCameraGetPushRawParams.DiskStatus.NotInitialized == diskStatus || DataCameraGetPushRawParams.DiskStatus.FormatNotSupport == diskStatus) {
            return true;
        }
        return false;
    }

    public static boolean isSSDCardNormal() {
        DataCameraGetPushRawParams rawParams = DataCameraGetPushRawParams.getInstance();
        if (!rawParams.isGetted()) {
            return false;
        }
        DataCameraGetPushRawParams.DiskStatus diskStatus = rawParams.getDiskStatus();
        if (DataCameraGetPushRawParams.DiskStatus.WAITING == diskStatus || DataCameraGetPushRawParams.DiskStatus.STORING == diskStatus || DataCameraGetPushRawParams.DiskStatus.SLOW_FORMATING == diskStatus || DataCameraGetPushRawParams.DiskStatus.FAST_FORMATING == diskStatus || DataCameraGetPushRawParams.DiskStatus.INITIALIZING == diskStatus || DataCameraGetPushRawParams.DiskStatus.ChangingMode == diskStatus) {
            return true;
        }
        return false;
    }

    public static boolean focusAreaAutoDismiss() {
        return DJICommonUtils.isProductOrange2() || DJICommonUtils.isM200Product(null) || DJICommonUtils.isWM240();
    }

    public static boolean canFocusByLongPress(Context context) {
        boolean can;
        DataCameraGetPushStateInfo.CameraType cType = DataCameraGetPushStateInfo.CameraType.OTHER;
        if (DataCameraGetPushStateInfo.getInstance().isGetted()) {
            cType = DataCameraGetPushStateInfo.getInstance().getCameraType();
        }
        if (DjiSharedPreferencesManager.getInt(context, ICameraDefine.KEY_FPV_LONG_PRESS_CTRL, 0) == 1) {
            can = true;
        } else {
            can = false;
        }
        if (!can || !supportLongPressSwitch() || !supportCameraFocus(cType)) {
            return false;
        }
        return true;
    }

    public static boolean supportLongPressSwitch() {
        DataCameraGetPushStateInfo stateInfo = DataCameraGetPushStateInfo.getInstance();
        if (!stateInfo.isGetted()) {
            return false;
        }
        DataCameraGetPushStateInfo.CameraType cType = stateInfo.getCameraType();
        if (cType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC220 || cType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC220S || isP4ProCamera(cType)) {
            return true;
        }
        return false;
    }

    public static boolean isSupportStar(ProductType type) {
        if (type == null) {
            type = DJIProductManager.getInstance().getType();
        }
        return DJICommonUtils.isKumquatSeries(type) || DJICommonUtils.isMammoth() || DJICommonUtils.isWM230() || DJICommonUtils.isWM240();
    }

    public static boolean isInspire2Camera(DataCameraGetPushStateInfo.CameraType cType) {
        return cType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6510 || cType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6520 || cType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6540;
    }

    public static boolean isInspire2Camera() {
        return isInspire2Camera(DataCameraGetPushStateInfo.getInstance().getCameraType());
    }

    public static boolean isInspire2RawCamera(DataCameraGetPushStateInfo.CameraType cType) {
        return cType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6520 || cType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6540;
    }

    public static boolean isInspire2RawCamera() {
        return isInspire2RawCamera(DataCameraGetPushStateInfo.getInstance().getCameraType());
    }

    public static boolean isSupportH265() {
        ProductType type = DJIProductManager.getInstance().getType();
        return type == ProductType.Pomato || type == ProductType.PomatoSDR || DJICommonUtils.isWM240();
    }

    public static boolean isSupportEIMode() {
        return DataCameraGetPushStateInfo.getInstance().getCameraType() == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6540 || isCameraX5SSupportEI();
    }

    public static boolean isCameraX5SSupportEI() {
        DataCameraGetPushStateInfo info = DataCameraGetPushStateInfo.getInstance();
        return info.getWm620CameraProtocolVersion() >= 3 && info.getCameraType() == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6520;
    }

    public static boolean isSSDSupportEXFAT() {
        return isInspire2RawCamera() && DataCameraGetPushStateInfo.getInstance().getWm620CameraProtocolVersion() >= 2 && DataCameraGetPushRawParams.getInstance().getRawProtocolVersion() >= 3;
    }

    public static boolean isSupportTapFocus() {
        DataCameraGetPushStateInfo.CameraType cameraType = DataCameraGetPushStateInfo.getInstance().getCameraType();
        return supportCameraFocus(cameraType) && cameraType != DataCameraGetPushStateInfo.CameraType.DJICameraTypeGD600;
    }

    public static boolean isSupportTapZoom() {
        return DataCameraGetPushStateInfo.getInstance().getCameraType() == DataCameraGetPushStateInfo.CameraType.DJICameraTypeGD600;
    }

    public static boolean supportBoardcastMode(DataCameraGetPushStateInfo.CameraType cameraType) {
        if (cameraType == null) {
            cameraType = DataCameraGetPushStateInfo.getInstance().getCameraType();
        }
        return DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6510 == cameraType || DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6520 == cameraType || cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6540;
    }

    public static boolean supportNoVideoType(DataCameraGetPushStateInfo.CameraType cameraType) {
        if (cameraType == null) {
            cameraType = DataCameraGetPushStateInfo.getInstance().getCameraType();
        }
        return DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC230 == cameraType || DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC240 == cameraType || DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC240_1 == cameraType;
    }

    public static boolean isNotRelResolution(DataCameraGetPushStateInfo.CameraType cameraType) {
        if (cameraType == null) {
            cameraType = DataCameraGetPushStateInfo.getInstance().getCameraType();
        }
        return DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6510 == cameraType || DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6520 == cameraType || DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6540 == cameraType;
    }

    public static boolean isInPlaybackMode(SettingsDefinitions.CameraMode currentMode) {
        if (currentMode == null) {
            currentMode = (SettingsDefinitions.CameraMode) CacheHelper.getValue(KeyHelper.getCameraKey("Mode"), SettingsDefinitions.CameraMode.UNKNOWN);
        }
        return currentMode == SettingsDefinitions.CameraMode.PLAYBACK || currentMode == SettingsDefinitions.CameraMode.MEDIA_DOWNLOAD;
    }

    public static boolean isRecordMode(DataCameraGetMode.MODE mode) {
        return DataCameraGetMode.MODE.RECORD == mode || DataCameraGetMode.MODE.BROADCAST == mode;
    }

    public static void pauseCameraStabliztion() {
        if (DataEyeGetPushStabilizationState.getInstance().getStateIsTurnOn()) {
            new DataSingleSendAppStateForStabilization().setCameraState(DataSingleSendAppStateForStabilization.CameraState.ZOOM_OUT).start();
        }
    }

    public static boolean isLikeRecordMode(SettingsDefinitions.CameraMode mode) {
        return SettingsDefinitions.CameraMode.BROADCAST == mode || SettingsDefinitions.CameraMode.RECORD_VIDEO == mode;
    }

    public static boolean isRecordMode() {
        SettingsDefinitions.CameraMode mode = (SettingsDefinitions.CameraMode) CacheHelper.getCamera("Mode");
        return SettingsDefinitions.CameraMode.BROADCAST == mode || SettingsDefinitions.CameraMode.RECORD_VIDEO == mode;
    }

    private DJICameraUtil() {
    }

    public static void setAFCModeEnabled(boolean enabled) {
        SettingsDefinitions.FocusMode focusMode = (SettingsDefinitions.FocusMode) CacheHelper.getCamera(CameraKeys.FOCUS_MODE);
        if (focusMode == null) {
            return;
        }
        if (!enabled) {
            if (focusMode.equals(SettingsDefinitions.FocusMode.AFC)) {
                new DataBaseCameraSetting().setCmdId(CmdIdCamera.CmdIdType.SetFocusMode).setValue(DataCameraGetPushShotInfo.FuselageFocusMode.OneAuto.value()).start((DJIDataCallBack) null);
            }
        } else if (focusMode.equals(SettingsDefinitions.FocusMode.AUTO)) {
            new DataBaseCameraSetting().setCmdId(CmdIdCamera.CmdIdType.SetFocusMode).setValue(DataCameraGetPushShotInfo.FuselageFocusMode.ContinuousAuto.value()).start((DJIDataCallBack) null);
            new DataBaseCameraSetting().setCmdId(CmdIdCamera.CmdIdType.SetMetering).setValue(3).start((DJIDataCallBack) null);
        }
    }

    public static boolean canExecutePlaybackAction(boolean isRecording, boolean isShootingPhoto, boolean isShootingIntervalPhoto, SettingsDefinitions.CameraMode mode, boolean isConnected, DataCameraGetStateInfo.SDCardState state, boolean isBokehDownloading, boolean isPseudoCameraShooting) {
        if (isRecording || isShootingPhoto || isShootingIntervalPhoto || !checkCanWork(isConnected, state, true) || mode == SettingsDefinitions.CameraMode.BROADCAST || isBokehDownloading || isPseudoCameraShooting) {
            return false;
        }
        return true;
    }

    public static boolean checkCanWork(boolean isConnected, DataCameraGetStateInfo.SDCardState state, boolean isPlayBack) {
        boolean beNormal;
        boolean beFull;
        if (DataCameraGetStateInfo.SDCardState.Normal == state || DataCameraGetStateInfo.SDCardState.ToFormat == state || DataCameraGetStateInfo.SDCardState.Slow == state) {
            beNormal = true;
        } else {
            beNormal = false;
        }
        if (DataCameraGetStateInfo.SDCardState.Full == state) {
            beFull = true;
        } else {
            beFull = false;
        }
        if (isConnected) {
            if (beNormal) {
                return true;
            }
            if (isPlayBack && beFull) {
                return true;
            }
        }
        return false;
    }

    public static boolean isRawBurstInfinite(int rawBurstNumber) {
        return 255 == rawBurstNumber;
    }

    public static boolean supportAverageMetering() {
        if (DJICommonUtil.isMammoth() || DJICommonUtils.isWM230() || DJICommonUtils.isWM240()) {
            return true;
        }
        return false;
    }

    public static boolean isCameraPushIso() {
        return DJICommonUtils.isWM240();
    }

    public static boolean isColorOscilloscopeSupported() {
        return isInspire2Camera();
    }

    public static boolean isAbleToFetchHighResolutionInBackground() {
        DataCameraGetPushStateInfo.CameraType type = DataCameraGetPushStateInfo.getInstance().getCameraType();
        return DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC220 == type || DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC240 == type || DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC240_1 == type;
    }

    public static boolean isMainCameraRecordingNow() {
        return isRecordingNow(0);
    }

    public static boolean isRecordingNow(int index) {
        return isRecordingState((CameraRecordingState) CacheHelper.getValue(KeyHelper.getCameraKey(index, CameraKeys.RECORDING_STATE)));
    }

    public static boolean isRecordingState(CameraRecordingState state) {
        if (state == null) {
            return false;
        }
        if (state == CameraRecordingState.PREPARING || state == CameraRecordingState.RECORDING || state == CameraRecordingState.RECORDING_TO_CACHE) {
            return true;
        }
        return false;
    }

    public static boolean supportDetachableLens(DataCameraGetPushStateInfo.CameraType cameraType) {
        if (cameraType == null) {
            return false;
        }
        if (DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC550 == cameraType || DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC550Raw == cameraType || cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6520 || cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6540) {
            return true;
        }
        return false;
    }

    public static boolean supportDetachableCamera(ProductType productType) {
        if (!ProductType.isValidType(productType)) {
            return false;
        }
        if (ProductType.Orange == productType || ProductType.Orange2 == productType || ProductType.BigBanana == productType || ProductType.PM820 == productType || ProductType.OrangeRAW == productType || ProductType.OrangeCV600 == productType || ProductType.PM820PRO == productType || DJICommonUtils.isM200Product(productType)) {
            return true;
        }
        return false;
    }

    public static DataCameraGetImageSize.RatioType transformVideoType(AutoVideoSizeCalculator.Options.VideoType videoType) {
        DataCameraGetImageSize.RatioType ratioType = DataCameraGetImageSize.RatioType.R_16_9;
        if (videoType == AutoVideoSizeCalculator.Options.VideoType.Ratio3X2_IN16X9 || videoType == AutoVideoSizeCalculator.Options.VideoType.Ratio3X2) {
            return DataCameraGetImageSize.RatioType.R_3_2;
        }
        if (videoType == AutoVideoSizeCalculator.Options.VideoType.Ratio4X3_IN16X9 || videoType == AutoVideoSizeCalculator.Options.VideoType.Ratio4X3) {
            return DataCameraGetImageSize.RatioType.R_4_3;
        }
        return ratioType;
    }

    public static boolean isNewOriginalPhotoProto() {
        return checkIsWM240Camera(null);
    }

    public static int getMeterHorNum() {
        if (checkIsWM240Camera(null)) {
            return 15;
        }
        return 12;
    }

    public static int getMeterVerNum() {
        if (checkIsWM240Camera(null)) {
            return 15;
        }
        return 8;
    }

    public static int findBoundaryIndex(@Nullable int[] values, int value) {
        if (values == null || values.length <= 0) {
            return 0;
        }
        int length = values.length;
        for (int i = 0; i < length; i++) {
            if (value == values[i]) {
                return i;
            }
        }
        if (value > values[length - 1]) {
            return length - 1;
        }
        return 0;
    }

    public static boolean isCameraBusy() {
        return CacheHelper.toBool(CacheHelper.getCamera(CameraKeys.IS_SHOOTING_PHOTO)) || CacheHelper.toBool(CacheHelper.getCamera(CameraKeys.IS_SHOOTING_INTERVAL_PHOTO)) || CacheHelper.toBool(CacheHelper.getCamera(CameraKeys.IS_RECORDING)) || DataCameraGetPushStateInfo.getInstance().beInHyperLapseMode();
    }

    public static boolean isP4ProCamera(DataCameraGetPushStateInfo.CameraType type) {
        if (type == null) {
            type = DataCameraGetPushStateInfo.getInstance().getCameraType(0);
        }
        if (DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6310 == type || DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6310S == type) {
            return true;
        }
        return false;
    }

    public static boolean isWM160Camera(DataCameraGetPushStateInfo.CameraType type) {
        if (type == null) {
            type = DataCameraGetPushStateInfo.getInstance().getCameraType(0);
        }
        if (DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC160 == type) {
            return true;
        }
        return false;
    }

    public static boolean isP4SCamera(DataCameraGetPushStateInfo.CameraType type) {
        if (type == null) {
            type = DataCameraGetPushStateInfo.getInstance().getCameraType(0);
        }
        if (DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6310S == type) {
            return true;
        }
        return false;
    }

    public static boolean isInPano() {
        return DJIVisionUtil.supportPano() && !isRecordMode() && DataEyeGetPushPseudoCameraParams.getInstance().getCameraMode() != DataEyeGetPushPseudoCameraParams.PseudoCameraMode.PSEUDO_CAMERA_MODE_NONE;
    }

    public static boolean isInPanoCapture() {
        return DJIVisionUtil.supportPano() && DataEyeGetPushPseudoCameraParams.getInstance().isInPanoCapture();
    }

    public static boolean isSupportFileSystemWithNewFirmware() {
        return DataCameraGetPushRawParams.getInstance().getDiskStatus() == DataCameraGetPushRawParams.DiskStatus.FormatNotSupport;
    }

    public static boolean isSupportFileSystemWithOldFirmware() {
        DataCameraGetPushRawParams mCameraRawInstance = DataCameraGetPushRawParams.getInstance();
        if (mCameraRawInstance.getSsdTotalCapacity() != 0 || DJIComponentManager.getInstance().getCameraComponentType() == DJIComponentManager.CameraComponentType.None || mCameraRawInstance.getDiskStatus() == DataCameraGetPushRawParams.DiskStatus.NOTCONNECTED || mCameraRawInstance.getDiskStatus() == DataCameraGetPushRawParams.DiskStatus.INITIALIZING || mCameraRawInstance.getDiskStatus() == DataCameraGetPushRawParams.DiskStatus.ChangingMode || mCameraRawInstance.getDiskStatus() == DataCameraGetPushRawParams.DiskStatus.FAST_FORMATING || mCameraRawInstance.getDiskStatus() == DataCameraGetPushRawParams.DiskStatus.NotInitialized) {
            return false;
        }
        return true;
    }

    public static boolean cantApertureAdjust() {
        if (!supportAperture(DataCameraGetPushStateInfo.getInstance().getCameraType())) {
            return false;
        }
        DataCameraSetExposureMode.ExposureMode mode = DataCameraGetPushShotParams.getInstance().getExposureMode();
        if (mode == DataCameraSetExposureMode.ExposureMode.P || mode == DataCameraSetExposureMode.ExposureMode.S || mode == DataCameraSetExposureMode.ExposureMode.C) {
            return true;
        }
        return false;
    }

    public static int getCurrentAperture(DataCameraGetPushShotParams shot) {
        if (cantApertureAdjust()) {
            return shot.getRealApertureSize();
        }
        return shot.getApertureSize();
    }
}
