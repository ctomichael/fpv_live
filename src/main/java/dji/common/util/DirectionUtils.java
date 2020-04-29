package dji.common.util;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.manager.P3.ServiceManager;
import dji.midware.data.model.P3.DataCameraGetImageSize;
import dji.midware.data.model.P3.DataCameraGetMode;
import dji.midware.data.model.P3.DataCameraGetPushFovParam;
import dji.midware.data.model.P3.DataCameraGetPushShotParams;
import dji.midware.data.model.P3.DataCameraGetPushStateInfo;
import dji.midware.media.DJIVideoDecoder;

@EXClassNullAway
public class DirectionUtils {
    public static final float DEG2RAD = 0.017453292f;
    public static final float RAD2DEG = 57.29578f;
    private static float[] e = new float[3];
    private static float[] mCurrMovingDir = new float[2];
    private static float[] r = new float[9];

    public static void updateGimbalParam(float e1, float e2, float e3) {
        e[0] = e1;
        e[1] = e2;
        e[2] = e3;
        e2rGimbal(e, r);
    }

    public static void e2rGimbal(float[] pe, float[] pr) {
        float sr = (float) Math.sin((double) pe[2]);
        float cr = (float) Math.cos((double) pe[2]);
        float sp = (float) Math.sin((double) pe[1]);
        float cp = (float) Math.cos((double) pe[1]);
        float sy = (float) Math.sin((double) pe[0]);
        float cy = (float) Math.cos((double) pe[0]);
        pr[0] = (cp * cy) - ((sr * sp) * sy);
        pr[3] = (cp * sy) + (sr * sp * cy);
        pr[6] = (-sp) * cr;
        pr[1] = (-cr) * sy;
        pr[4] = cr * cy;
        pr[7] = sr;
        pr[2] = (sp * cy) + (sr * cp * sy);
        pr[5] = (sp * sy) - ((sr * cp) * cy);
        pr[8] = cr * cp;
    }

    public static float[] calculateCurrMovingDir(float[] droneVelocity) {
        DataCameraGetImageSize.RatioType ratio = getRatio();
        float camZ = (float) (((double) ((r[0] * droneVelocity[0]) + (r[3] * droneVelocity[1]) + (r[6] * droneVelocity[2]))) + 1.0E-8d);
        float camX = (r[1] * droneVelocity[0]) + (r[4] * droneVelocity[1]) + (r[7] * droneVelocity[2]);
        float camY = (r[2] * droneVelocity[0]) + (r[5] * droneVelocity[1]) + (r[8] * droneVelocity[2]);
        float fRatio = 0.5625f;
        if (ratio == DataCameraGetImageSize.RatioType.R_4_3) {
            fRatio = 0.75f;
        } else if (ratio == DataCameraGetImageSize.RatioType.R_3_2) {
            fRatio = 0.6666667f;
        }
        float horizontalFov = VisualUtils.cameraHorizontalFov();
        double vVov = Math.atan(Math.tan((double) (0.5f * horizontalFov * 0.017453292f)) * ((double) fRatio)) * 57.295780181884766d * 2.0d;
        mCurrMovingDir[0] = (float) ((1.0d + (((double) (camX / camZ)) / Math.tan((((double) horizontalFov) * 0.5d) * 0.01745329238474369d))) * 0.5d);
        mCurrMovingDir[1] = (float) ((1.0d + (((double) (camY / camZ)) / Math.tan((0.5d * vVov) * 0.01745329238474369d))) * 0.5d);
        return mCurrMovingDir;
    }

    public static void adjustXY(float[] after, float x, float y) {
        DataCameraGetImageSize.RatioType ratio = getRatio();
        float videoClipRatio = (float) (Math.tan((double) ((0.017453292f * VisualUtils.cameraHorizontalFov()) / 2.0f)) / Math.tan((double) ((0.017453292f * VisualUtils.cameraHorizontalStandardFov()) / 2.0f)));
        float fRatio = 0.75f;
        if (!DataCameraGetPushFovParam.getInstance().hasFovData()) {
            if (ratio == DataCameraGetImageSize.RatioType.R_4_3) {
                fRatio = 0.75f;
            } else if (ratio == DataCameraGetImageSize.RatioType.R_3_2) {
                fRatio = 0.6666667f;
            } else {
                fRatio = 0.5625f;
            }
        }
        after[0] = (float) (((((double) x) - 0.5d) * ((double) videoClipRatio)) + 0.5d);
        after[1] = (float) (((((double) y) - 0.5d) * ((double) (1.3333334f * fRatio)) * ((double) videoClipRatio)) + 0.5d);
        if (after[0] < 0.0f) {
            after[0] = 0.0f;
        } else if (after[0] > 1.0f) {
            after[0] = 1.0f;
        }
        if (after[1] < 0.0f) {
            after[1] = 0.0f;
        } else if (after[1] > 1.0f) {
            after[1] = 1.0f;
        }
    }

    private static DataCameraGetImageSize.RatioType getRatio() {
        DataCameraGetImageSize.RatioType ratioType = DataCameraGetImageSize.RatioType.R_16_9;
        DataCameraGetImageSize.RatioType tmpRatioType = DataCameraGetImageSize.RatioType.OTHER;
        DJIVideoDecoder decoder = ServiceManager.getInstance().getDecoder();
        int videoWidth = decoder == null ? 16 : decoder.width;
        int videoHeight = decoder == null ? 9 : decoder.height;
        if (tmpRatioType == DataCameraGetImageSize.RatioType.OTHER) {
            tmpRatioType = DataCameraGetPushShotParams.getInstance().isGetted() ? DataCameraGetPushShotParams.getInstance().getImageRatio() : DataCameraGetImageSize.RatioType.R_4_3;
        }
        ProductType productType = DJIProductManager.getInstance().getType();
        if (productType == ProductType.litchiC || productType == ProductType.litchiS || productType == ProductType.P34K || productType == ProductType.KumquatS || productType == ProductType.Pomato || productType == ProductType.PomatoSDR || productType == ProductType.PomatoRTK) {
            float videoratio = (((float) videoWidth) * 1.0f) / ((float) videoHeight);
            if (Math.abs(videoratio - 1.7777778f) < Math.abs(videoratio - 1.5f)) {
                return DataCameraGetImageSize.RatioType.R_16_9;
            }
            if (Math.abs(videoratio - 1.5f) < Math.abs(videoratio - 1.3333334f)) {
                return DataCameraGetImageSize.RatioType.R_3_2;
            }
            return DataCameraGetImageSize.RatioType.R_4_3;
        } else if (tmpRatioType == DataCameraGetImageSize.RatioType.R_4_3 && DataCameraGetPushStateInfo.getInstance().getMode() == DataCameraGetMode.MODE.TAKEPHOTO) {
            return DataCameraGetImageSize.RatioType.R_4_3;
        } else {
            if (tmpRatioType == DataCameraGetImageSize.RatioType.R_3_2 && DataCameraGetPushStateInfo.getInstance().getMode() == DataCameraGetMode.MODE.TAKEPHOTO) {
                return DataCameraGetImageSize.RatioType.R_3_2;
            }
            return DataCameraGetImageSize.RatioType.R_16_9;
        }
    }
}
