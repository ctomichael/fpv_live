package dji.pilot.fpv.util;

import android.text.TextUtils;
import dji.fieldAnnotation.EXClassNullAway;
import dji.logic.utils.DJIPublicUtils;
import dji.midware.component.DJIComponentManager;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.model.P3.DataCameraGetPushStateInfo;
import dji.midware.data.model.P3.DataCommonGetVersion;
import dji.midware.data.model.P3.DataEyeGetPushStabilizationState;
import dji.midware.data.model.P3.DataSingleSendAppStateForStabilization;
import dji.pilot.publics.util.DJICommonUtils;
import dji.pilot.publics.version.IVersionGimbal;

@EXClassNullAway
public class DJIGimbalUtil {
    public static boolean isSupportPitchFineTune() {
        return DJICommonUtils.isMammoth();
    }

    public static boolean isSupportRollFineTune() {
        DataCameraGetPushStateInfo.CameraType cameraType = DataCameraGetPushStateInfo.getInstance().getCameraType();
        if (cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6510 || cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeGD600 || cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6520 || !isLb2() || DJICommonUtils.isProductM600Series(null) || isSupportFPVGimbalFineTune() || DJICommonUtils.isWM240()) {
            return true;
        }
        return false;
    }

    public static boolean isSupportYawFineTune() {
        DataCameraGetPushStateInfo.CameraType cameraType = DataCameraGetPushStateInfo.getInstance().getCameraType();
        if (cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6510 || cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6520) {
            return true;
        }
        ProductType type = DJIProductManager.getInstance().getType();
        if (type == ProductType.Tomato && supportGimbalFirmware(IVersionGimbal.SUPPORT_GIMBAL_VERSION_TOMATO)) {
            return true;
        }
        if ((DJICommonUtils.isPomatoSeries(type) || type == ProductType.Potato) && supportGimbalFirmware(IVersionGimbal.SUPPORT_GIMBAL_VERSION_POMATO)) {
            return true;
        }
        if ((type != ProductType.Orange2 || !isInspire2SupportYaw()) && !DJICommonUtils.isWM230() && !DJICommonUtils.isWM240()) {
            return false;
        }
        return true;
    }

    public static boolean isHasYawAdjustAngle() {
        return DJICommonUtils.isWM230() || DJICommonUtils.isWM240();
    }

    public static boolean isInspire2SupportYaw() {
        DJIComponentManager.CameraComponentType type = DJIComponentManager.getInstance().getCameraComponentType();
        if (type == DJIComponentManager.CameraComponentType.X5S) {
            return supportGimbalFirmware(IVersionGimbal.SUPPORT_GIMBAL_VERSION_INSPIRE2_X5S);
        }
        if (type == DJIComponentManager.CameraComponentType.X4S) {
            return supportGimbalFirmware(IVersionGimbal.SUPPORT_GIMBAL_VERSION_INSPIRE2_X4S);
        }
        if (type == DJIComponentManager.CameraComponentType.X7) {
            return true;
        }
        return false;
    }

    public static boolean supportGimbalFirmware(String supportVersionStr) {
        if (TextUtils.isEmpty(supportVersionStr)) {
            return false;
        }
        DataCommonGetVersion getter = new DataCommonGetVersion();
        getter.setDeviceType(DeviceType.GIMBAL);
        if (DJIPublicUtils.formatVersion(getter.getFirmVer(".")) >= DJIPublicUtils.formatVersion(supportVersionStr)) {
            return true;
        }
        return false;
    }

    public static boolean supportFPVRollSpeed() {
        return DJICommonUtils.isProductOrange2();
    }

    public static boolean isLb2() {
        ProductType rcType = DJIProductManager.getInstance().getRcType();
        ProductType type = DJIProductManager.getInstance().getType();
        if (rcType == ProductType.Grape2 || type == ProductType.A2 || type == ProductType.A3 || type == ProductType.N3 || type == ProductType.Orange2 || DJICommonUtils.isM200Product(type)) {
            return true;
        }
        return false;
    }

    public static boolean isSupportFPVGimbalFineTune() {
        return DJIProductManager.getInstance().getType() == ProductType.Orange2;
    }

    public static boolean supportAdjustGimbalYaw(ProductType type) {
        if (type == null) {
            type = DJIProductManager.getInstance().getType();
        }
        return type == ProductType.Orange2 || type == ProductType.Orange || type == ProductType.N1 || type == ProductType.BigBanana || type == ProductType.OrangeRAW || type == ProductType.Olives || DJICommonUtils.isProductM600Series(type) || type == ProductType.OrangeCV600 || DJICommonUtils.isM200Product(type) || DJICommonUtils.isMammothWiFiConnected() || DJICommonUtils.isWM230WiFiConnected() || type == ProductType.WM240;
    }

    public static boolean supportMoreGimbals(ProductType type) {
        return DJICommonUtils.isProductM600Series(type);
    }

    public static boolean isEnableGimbal(ProductType type) {
        if (type == null) {
            type = DJIProductManager.getInstance().getType();
        }
        return type == ProductType.Orange || type == ProductType.N1 || type == ProductType.Grape2 || type == ProductType.A2 || type == ProductType.BigBanana || type == ProductType.OrangeRAW || DJICommonUtils.isProductM600Series(type) || type == ProductType.OrangeCV600 || type == ProductType.Orange2 || DJICommonUtils.isM200Product(type);
    }

    public static void pauseGimbalStabliztion() {
        if (DataEyeGetPushStabilizationState.getInstance().getStateIsTurnOn()) {
            new DataSingleSendAppStateForStabilization().setGimbalState(DataSingleSendAppStateForStabilization.GimbalState.START).start();
        }
    }

    public static boolean supportDoubleGimbals(ProductType productType) {
        if (productType == null) {
            productType = DJIProductManager.getInstance().getType();
        }
        return ProductType.M210 == productType || ProductType.M210RTK == productType;
    }

    private DJIGimbalUtil() {
    }
}
