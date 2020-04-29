package dji.logic.utils;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.model.P3.DataCameraGetPushShotInfo;
import dji.midware.data.model.P3.DataCameraGetPushStateInfo;

@EXClassNullAway
public class DJICameraSupportUtil {
    private static final String TAG = "DJICameraSupportUtil";

    public static boolean isSupportMF(DataCameraGetPushStateInfo.CameraType cameraType) {
        if (cameraType == null) {
            cameraType = DJIProductManager.getInstance().getCameraType();
        }
        if (cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC550 || cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC550Raw) {
            return true;
        }
        return false;
    }

    public static boolean isSupportElectronicStabilization(DataCameraGetPushStateInfo.CameraType type) {
        if (type == null) {
            type = DJIProductManager.getInstance().getCameraType();
        }
        return type == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC350;
    }

    public static boolean isSupportAudioWithInnerMic(DataCameraGetPushStateInfo.CameraType cameraType) {
        if (cameraType == null) {
            cameraType = DataCameraGetPushStateInfo.getInstance().getCameraType(0);
        }
        if (!isFC550SeriesCamera(cameraType)) {
            return true;
        }
        return false;
    }

    public static boolean supportCameraFocus(DataCameraGetPushStateInfo.CameraType type) {
        return type == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC550 || type == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC550Raw || type == DataCameraGetPushStateInfo.CameraType.DJICameraTypeCV600;
    }

    public static boolean supportDZoom(DataCameraGetPushStateInfo.CameraType type) {
        return type == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC350;
    }

    public static boolean supportAdjustAperture(DataCameraGetPushStateInfo.CameraType type) {
        return type == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC550 || type == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC550Raw;
    }

    public static boolean supportCameraFocusHandwheel(DataCameraGetPushStateInfo.CameraType type) {
        return type == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC550 || type == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC550Raw;
    }

    public static boolean isFC550SeriesCamera(DataCameraGetPushStateInfo.CameraType camera) {
        if (camera == null) {
            camera = DataCameraGetPushStateInfo.getInstance().getCameraType(0);
        }
        if (camera == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC550 || camera == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC550Raw) {
            return true;
        }
        return false;
    }

    public static boolean isSupportPortraitColor(DataCameraGetPushStateInfo.CameraType camera) {
        if (camera == null) {
            camera = DataCameraGetPushStateInfo.getInstance().getCameraType(0);
        }
        if (camera == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC350 || camera == DataCameraGetPushStateInfo.CameraType.DJICameraTypeCV600) {
            return true;
        }
        return false;
    }

    public static boolean isSupportPano(DataCameraGetPushStateInfo.CameraType camera) {
        if (camera == null) {
            camera = DataCameraGetPushStateInfo.getInstance().getCameraType(0);
        }
        if (camera == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC350 || camera == DataCameraGetPushStateInfo.CameraType.DJICameraTypeCV600) {
            return true;
        }
        return false;
    }

    public static int getCameraFOV(DataCameraGetPushStateInfo.CameraType camera) {
        if (camera == null) {
            camera = DataCameraGetPushStateInfo.getInstance().getCameraType(0);
        }
        if (camera == DataCameraGetPushStateInfo.CameraType.DJICameraTypeCV600) {
            return 74;
        }
        return 84;
    }

    public static boolean isSupportRefLineAndCenterPoint(DataCameraGetPushStateInfo.CameraType camera) {
        if (camera == null) {
            camera = DataCameraGetPushStateInfo.getInstance().getCameraType(0);
        }
        if (camera == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC550 || camera == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC550Raw) {
            return true;
        }
        return false;
    }

    public static boolean isSupportFocusDistance(DataCameraGetPushShotInfo info) {
        if (info.getShotFDType() == DataCameraGetPushShotInfo.ShotFDType.ZoomShotFD && info.getZoomFocusType() == DataCameraGetPushShotInfo.ZoomFocusType.AutoZoomFocus) {
            return true;
        }
        return false;
    }

    public static boolean isSupportTimelapse(DataCameraGetPushStateInfo.CameraType camera) {
        if (camera == null) {
            camera = DataCameraGetPushStateInfo.getInstance().getCameraType(0);
        }
        if (camera == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC350 || camera == DataCameraGetPushStateInfo.CameraType.DJICameraTypeCV600) {
            return true;
        }
        return false;
    }

    public static boolean isSupportFocus(DataCameraGetPushStateInfo.CameraType type) {
        if (type == null) {
            type = DJIProductManager.getInstance().getCameraType();
        }
        return type == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC550 || type == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC550Raw || type == DataCameraGetPushStateInfo.CameraType.DJICameraTypeCV600;
    }

    public static boolean isSupportOpticsZoom(DataCameraGetPushStateInfo.CameraType type) {
        if (type == null) {
            type = DJIProductManager.getInstance().getCameraType();
        }
        boolean isFisSupportFocusDistance = false;
        if (DataCameraGetPushShotInfo.getInstance().isGetted()) {
            isFisSupportFocusDistance = isSupportFocusDistance(DataCameraGetPushShotInfo.getInstance());
        }
        return type == DataCameraGetPushStateInfo.CameraType.DJICameraTypeCV600 || isFisSupportFocusDistance;
    }

    public static boolean supportRawBurst(DataCameraGetPushStateInfo.CameraType type) {
        if (type == null) {
            type = DataCameraGetPushStateInfo.getInstance().getCameraType(0);
        }
        ProductType pType = DJIProductManager.getInstance().getType();
        if ((type == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6520 || type == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6540) && ProductType.Orange2 == pType) {
            return true;
        }
        return false;
    }

    public static boolean supportSSD(DataCameraGetPushStateInfo.CameraType type) {
        if (type == null) {
            type = DataCameraGetPushStateInfo.getInstance().getCameraType(0);
        }
        ProductType pType = DJIProductManager.getInstance().getType();
        if ((type == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6520 || type == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6540) && ProductType.Orange2 == pType) {
            return true;
        }
        return false;
    }

    public static boolean supportClickCameraOsd(DataCameraGetPushStateInfo.CameraType type) {
        if (type == null) {
            type = DataCameraGetPushStateInfo.getInstance().getCameraType(0);
        }
        if (type == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6510 || type == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6520 || type == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6540) {
            return true;
        }
        return false;
    }

    public static boolean suport3t2PhotoSize(DataCameraGetPushStateInfo.CameraType type) {
        if (type == null) {
            type = DataCameraGetPushStateInfo.getInstance().getCameraType(0);
        }
        if (DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6310 == type || DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6510 == type || DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6310S == type) {
            return true;
        }
        return false;
    }

    public static boolean shouldShowDetailFormat(DataCameraGetPushStateInfo.CameraType type) {
        if (type == null) {
            type = DataCameraGetPushStateInfo.getInstance().getCameraType(0);
        }
        if (DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6510 == type || DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6520 == type || DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6540 == type) {
            return true;
        }
        return false;
    }

    public static boolean isInspire2Camera(DataCameraGetPushStateInfo.CameraType cType) {
        return cType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6510 || cType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6520 || cType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6540;
    }

    public static boolean supportMfNSwitch(DataCameraGetPushStateInfo.CameraType type) {
        return DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC220 == type || DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC220S == type || DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6310 == type || DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6310S == type;
    }

    public static boolean isSupportPeakingFocus(DataCameraGetPushStateInfo.CameraType cameraType) {
        if (isInspire2Camera(cameraType) || cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC550 || cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC550Raw || cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC240 || cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC240_1 || cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC245_IMX477 || supportMfNSwitch(cameraType)) {
            return true;
        }
        return false;
    }

    public static boolean unsupportRawPicture(DataCameraGetPushStateInfo.CameraType cameraType) {
        return DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC1102 == cameraType;
    }
}
