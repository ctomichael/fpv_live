package dji.common.util;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.model.P3.DataCameraGetImageSize;
import dji.midware.data.model.P3.DataCameraGetMode;
import dji.midware.data.model.P3.DataCameraGetPushFovParam;
import dji.midware.data.model.P3.DataCameraGetPushShotParams;
import dji.midware.data.model.P3.DataCameraGetPushStateInfo;

@EXClassNullAway
public class VisualUtils {
    static final int FPS_120 = 7;
    static final int VR_1920_1080P = 10;
    static final int VR_2704_1520P = 24;
    static final int VR_3840_1920P = 14;
    static final int VR_3840_2160P = 16;
    static final int VR_3840_2880P = 18;
    static final int VR_4096_2048P = 20;
    static final int VR_4096_2160P = 22;

    private static float cameraHorizontalFovOfP4(DataCameraGetMode.MODE mode) {
        if (mode == DataCameraGetMode.MODE.TAKEPHOTO) {
            return 82.0f;
        }
        if (mode != DataCameraGetMode.MODE.RECORD) {
            return cameraHorizontalStandardFov();
        }
        int videoFormat = DataCameraGetPushShotParams.getInstance().getVideoFormat(new int[0]);
        int fps = DataCameraGetPushShotParams.getInstance().getVideoFps();
        if (videoFormat == 20 || videoFormat == 22) {
            return cameraHorizontalStandardFov();
        }
        if (videoFormat == 24 || videoFormat == 14 || videoFormat == 16 || videoFormat == 18) {
            return 82.0f;
        }
        if (videoFormat == 10 && fps == 7) {
            return 41.0f;
        }
        return 80.0f;
    }

    private static float cameraHorizontalFovOfKumquat(DataCameraGetMode.MODE mode) {
        if (mode == DataCameraGetMode.MODE.TAKEPHOTO) {
            return 64.0f;
        }
        if (mode != DataCameraGetMode.MODE.RECORD) {
            return cameraHorizontalStandardFov();
        }
        int videoFormat = DataCameraGetPushShotParams.getInstance().getVideoFormat(new int[0]);
        if (videoFormat == 20 || videoFormat == 22) {
            return cameraHorizontalStandardFov();
        }
        return 62.0f;
    }

    private static float cameraHorizontalFovOfPomato(DataCameraGetMode.MODE mode) {
        if (mode == DataCameraGetMode.MODE.TAKEPHOTO) {
            DataCameraGetImageSize.RatioType ratio = DataCameraGetPushShotParams.getInstance().getImageRatio();
            if (DataCameraGetImageSize.RatioType.R_4_3 == ratio) {
                return 68.0f;
            }
            if (DataCameraGetImageSize.RatioType.R_16_9 == ratio) {
                return 72.5f;
            }
            return cameraHorizontalStandardFov();
        } else if (mode != DataCameraGetMode.MODE.RECORD) {
            return cameraHorizontalStandardFov();
        } else {
            int videoFormat = DataCameraGetPushShotParams.getInstance().getVideoFormat(new int[0]);
            int fps = DataCameraGetPushShotParams.getInstance().getVideoFps();
            if (videoFormat == 20 || videoFormat == 22) {
                return 68.0f;
            }
            if (videoFormat == 24 || videoFormat == 14 || videoFormat == 16 || videoFormat == 18) {
                return 72.5f;
            }
            if (videoFormat == 10 && fps == 7) {
                return 41.0f;
            }
            return 68.0f;
        }
    }

    public static float cameraHorizontalFov() {
        if (DataCameraGetPushFovParam.getInstance().hasFovData()) {
            return DataCameraGetPushFovParam.getInstance().getFovH();
        }
        DataCameraGetMode.MODE mode = DataCameraGetPushStateInfo.getInstance().getMode();
        ProductType pType = DJIProductManager.getInstance().getType();
        if (isKumquatSeries(pType)) {
            return cameraHorizontalFovOfKumquat(mode);
        }
        if (pType == ProductType.Pomato || pType == ProductType.PomatoSDR || pType == ProductType.PomatoRTK) {
            return cameraHorizontalFovOfPomato(mode);
        }
        if (pType == ProductType.Orange2) {
            return 75.7f;
        }
        if (pType == ProductType.WM230) {
            return DataCameraGetPushFovParam.getInstance().getFovH();
        }
        if (pType == ProductType.WM240 || pType == ProductType.WM245) {
            return DataCameraGetPushFovParam.getInstance().getFovH();
        }
        return cameraHorizontalFovOfP4(mode);
    }

    public static float cameraHorizontalStandardFov() {
        if (DataCameraGetPushFovParam.getInstance().hasFovData()) {
            return DataCameraGetPushFovParam.getInstance().getFovH();
        }
        ProductType pType = DJIProductManager.getInstance().getType();
        if (isKumquatSeries(pType)) {
            return 66.0f;
        }
        if (pType == ProductType.Pomato || pType == ProductType.PomatoSDR || pType == ProductType.PomatoRTK) {
            return 74.0f;
        }
        if (pType == ProductType.Orange2) {
            return 75.7f;
        }
        return 84.0f;
    }

    public static float cameraVerticalFov(DataCameraGetImageSize.RatioType ratio) {
        if (DataCameraGetPushFovParam.getInstance().hasFovData()) {
            return DataCameraGetPushFovParam.getInstance().getFovV();
        }
        ProductType pType = DJIProductManager.getInstance().getType();
        if (pType == ProductType.Orange2 || pType == ProductType.M200 || pType == ProductType.M210 || pType == ProductType.M210RTK) {
            return 60.6f;
        }
        if (pType == ProductType.WM230) {
            return DataCameraGetPushFovParam.getInstance().getFovV();
        }
        if (pType == ProductType.WM240 || pType == ProductType.WM245) {
            return DataCameraGetPushFovParam.getInstance().getFovV();
        }
        float horizontalFov = cameraHorizontalFov();
        if (ratio == DataCameraGetImageSize.RatioType.R_4_3) {
            return (horizontalFov * 3.0f) / 4.0f;
        }
        if (ratio == DataCameraGetImageSize.RatioType.R_3_2) {
            return (2.0f * horizontalFov) / 3.0f;
        }
        return (9.0f * horizontalFov) / 16.0f;
    }

    public static boolean isKumquatSeries(ProductType type) {
        if (type == null) {
            type = DJIProductManager.getInstance().getType();
        }
        return type == ProductType.KumquatX || type == ProductType.KumquatS;
    }
}
