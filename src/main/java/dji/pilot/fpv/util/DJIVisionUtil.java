package dji.pilot.fpv.util;

import dji.fieldAnnotation.EXClassNullAway;
import dji.logic.vision.IVisionResDefine;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.model.P3.DataCameraGetPushStateInfo;
import dji.midware.data.model.P3.DataEyeGetPushException;
import dji.midware.data.model.P3.DataEyeGetPushPseudoCameraParams;
import dji.pilot.publics.util.DJICommonUtils;

@EXClassNullAway
public class DJIVisionUtil {
    public static boolean closeAvoidSoundWhenBeMovingObjDetect(ProductType type) {
        if (type == null) {
            ProductType type2 = DJIProductManager.getInstance().getType();
        }
        return DJICommonUtils.isMammoth() || DJICommonUtils.isWM230();
    }

    public static boolean supportFixWing(ProductType type, long visionVersion) {
        return DJICommonUtils.supportRCC1C2(type) && ProductType.KumquatX == type && visionVersion >= IVisionResDefine.SupportVersion.VERSION_VISION_KUMQUATX_FIXWING;
    }

    public static boolean supportFixWing() {
        return supportFixWing(DJIProductManager.getInstance().getType(), DataEyeGetPushException.getInstance().getVisionVersion());
    }

    public static boolean supportAPAS(ProductType type) {
        return type == ProductType.WM230 || type == ProductType.WM240;
    }

    public static boolean supportFixWingGimbalCtrl(ProductType type, long visionVersion) {
        return supportFixWing(type, visionVersion) && IVisionResDefine.SupportVersion.VERSION_VISION_KUMAUATX_FIXWING_GIMBALCTRL <= visionVersion;
    }

    public static boolean isInPseudoCameraMode() {
        DataEyeGetPushPseudoCameraParams pseudoCameraParams = DataEyeGetPushPseudoCameraParams.getInstance();
        return DataEyeGetPushPseudoCameraParams.PseudoCameraMissionState.PSEUDO_CAMERA_MISSION_STATE_IDLE != (pseudoCameraParams.isGetted() ? pseudoCameraParams.getMissionState() : DataEyeGetPushPseudoCameraParams.PseudoCameraMissionState.PSEUDO_CAMERA_MISSION_STATE_IDLE);
    }

    public static boolean useLeftMenuTrackView() {
        ProductType pType = DJIProductManager.getInstance().getType();
        return ProductType.Orange2 == pType || DJICommonUtils.isM200Product(pType);
    }

    public static boolean supportTrackAutoFocus() {
        return IVisionResDefine.SupportVersion.VERSION_VISION_IN2_TRACK_AUTOFOCUS <= DataEyeGetPushException.getInstance().getVisionVersion() && ProductType.Orange2 == DJIProductManager.getInstance().getType();
    }

    public static boolean supportPanoSphere() {
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
            case PomatoSDR:
                return true;
            default:
                return false;
        }
    }

    public static boolean supportSRPhoto() {
        switch (DataCameraGetPushStateInfo.getInstance().getCameraType()) {
            case DJICameraTypeFC240:
                if (!DJICommonUtil.isWm240FirstPackageVerion()) {
                    return true;
                }
                return false;
            default:
                return false;
        }
    }

    public static boolean supportPano() {
        ProductType type = DJIProductManager.getInstance().getType();
        long version = DataEyeGetPushException.getInstance().getVisionVersion();
        switch (type) {
            case Mammoth:
            case WM240:
            case PomatoSDR:
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
                return DJICommonUtil.isNotWM230UpgradeFirstVerion();
            default:
                return false;
        }
    }

    private DJIVisionUtil() {
    }

    public static boolean isMultiTargetsSupported() {
        return DJICommonUtils.isWM230() || DJICommonUtils.isWM240();
    }
}
