package dji.internal.logics;

import dji.common.camera.SettingsDefinitions;
import dji.common.flightcontroller.FlightMode;
import dji.common.flightcontroller.VisionSensorPosition;
import dji.common.product.Model;
import dji.common.remotecontroller.RCMode;
import dji.component.areacode.DJIAreaCodeEvent;
import dji.component.areacode.IAreaCode;
import dji.component.areacode.IAreaCodeService;
import dji.fieldAnnotation.EXClassNullAway;
import dji.logic.manager.DJIUSBWifiSwitchManager;
import dji.logic.vision.IVisionResDefine;
import dji.midware.component.DJIComponentManager;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.Dpad.DpadProductManager;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.model.P3.DataEyeGetPushException;
import dji.midware.data.model.P3.DataOsdGetPushChannalStatus;
import dji.midware.data.model.P3.DataOsdGetPushCheckStatus;
import dji.midware.data.model.P3.DataOsdGetPushCommon;
import dji.midware.sdk.InternalDataProtectionGuard;
import dji.sdksharedlib.DJISDKCache;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.extension.KeyHelper;
import dji.sdksharedlib.keycatalog.CameraKeys;
import dji.sdksharedlib.keycatalog.FlightControllerKeys;
import dji.sdksharedlib.keycatalog.ProductKeys;
import dji.sdksharedlib.store.DJISDKCacheParamValue;
import dji.service.DJIAppServiceManager;
import java.util.Arrays;

@EXClassNullAway
public class CommonUtil {
    public static final int LF_RADAR_DISTANCE_MAX = 6;
    public static final int MAVIC2_RADAR_DISTANCE_MAX = 10;
    private static Model[] OCUSYNC_LINK_MODEL = {Model.MAVIC_2_PRO, Model.MAVIC_2_ZOOM, Model.MAVIC_2};
    public static final double ONE_METER_OFFSET = 8.99322E-6d;
    public static final int P4P_RADAR_DISTANCE_MAX = 20;
    public static final int P4_RADAR_DISTANCE_MAX = 10;
    public static final int TOF_RADAR_DISTANCE_MAX = 6;
    private static Model[] WIFI_LINK_MODEL = {Model.Spark, Model.MAVIC_AIR, Model.WM160};

    public static boolean isChannelPoor(DataOsdGetPushChannalStatus.CHANNEL_STATUS status) {
        return (DataOsdGetPushChannalStatus.CHANNEL_STATUS.Excellent == status || DataOsdGetPushChannalStatus.CHANNEL_STATUS.Good == status || DataOsdGetPushChannalStatus.CHANNEL_STATUS.Medium == status) ? false : true;
    }

    public static boolean useNewBattery() {
        ProductType type = DJIProductManager.getInstance().getType();
        return isProductM600Series(type) || isKumquatSeries(type);
    }

    public static boolean isProductM600Series(ProductType type) {
        if (type == null) {
            type = DJIProductManager.getInstance().getType();
        }
        return type == ProductType.PM820 || type == ProductType.PM820PRO;
    }

    public static boolean isA3Series() {
        ProductType type = DJIProductManager.getInstance().getType();
        return type == ProductType.A3 || type == ProductType.N3 || isProductM600Series(type);
    }

    public static boolean isKumquatSeries(ProductType type) {
        if (type == null) {
            type = DJIProductManager.getInstance().getType();
        }
        return type == ProductType.KumquatX || type == ProductType.KumquatS;
    }

    public static boolean checkGpsValid() {
        DataOsdGetPushCommon common = DataOsdGetPushCommon.getInstance();
        if (!common.isGetted()) {
            return false;
        }
        if (isOldMC() || common.getFlycVersion() < 6) {
            return checkGpsNumValid(common.getGpsNum());
        }
        return common.getGpsLevel() >= 4;
    }

    public static boolean isDroneGpsValid() {
        boolean ret = false;
        DataOsdGetPushCommon common = DataOsdGetPushCommon.getInstance();
        if (common.isGetted()) {
            if (isOldMC() || common.getFlycVersion() < 6) {
                int gpsNum = common.getGpsNum();
                if (DJIProductManager.getInstance().getType() == ProductType.litchiC) {
                    if (gpsNum >= 2) {
                        return true;
                    }
                    return false;
                } else if (gpsNum < 4) {
                    return false;
                } else {
                    return true;
                }
            } else if (common.getGpsLevel() < 2 || !common.isGPSValid()) {
                ret = false;
            } else {
                ret = true;
            }
        }
        double latitude = common.getLatitude();
        double longitude = common.getLongitude();
        if (!ret || (Math.abs(latitude) <= 8.99322E-6d && Math.abs(longitude) <= 8.99322E-6d)) {
            return false;
        }
        return true;
    }

    public static boolean isOldMC() {
        return isOldMC(DataOsdGetPushCommon.getInstance().getDroneType());
    }

    public static boolean isOldMC(DataOsdGetPushCommon.DroneType type) {
        return type == DataOsdGetPushCommon.DroneType.A2 || type == DataOsdGetPushCommon.DroneType.WKM || type == DataOsdGetPushCommon.DroneType.NAZA;
    }

    public static boolean checkGpsNumValid(int gpsNum) {
        if (DJIProductManager.getInstance().getType() == ProductType.litchiC) {
            if (gpsNum < 6 || gpsNum >= 50) {
                return false;
            }
            return true;
        } else if (gpsNum < 8 || gpsNum >= 50) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean checkUseNewModeChannel(DataOsdGetPushCommon common) {
        return common.getFlycVersion() >= 7;
    }

    public static boolean supportRedundancySenor() {
        ProductType type = DJIProductManager.getInstance().getType();
        return type == ProductType.Tomato || type == ProductType.Pomato || type == ProductType.Orange2 || type == ProductType.Potato || type == ProductType.PomatoSDR || type == ProductType.PomatoRTK || isA3Series() || isM200Product(null) || isKumquatSeries(type) || isWM240Series(type) || isPM420Platform();
    }

    public static boolean isM200Product(ProductType type) {
        if (type == null) {
            type = DJIProductManager.getInstance().getType();
        }
        return ProductType.M200 == type || ProductType.M210 == type || ProductType.M210RTK == type || ProductType.PM420 == type || ProductType.PM420PRO == type || ProductType.PM420PRO_RTK == type;
    }

    public static boolean isPM420Series(ProductType type) {
        if (type == null) {
            type = DJIProductManager.getInstance().getType();
        }
        return ProductType.PM420 == type || ProductType.PM420PRO == type || ProductType.PM420PRO_RTK == type;
    }

    public static boolean checkIsAttiMode(DataOsdGetPushCommon.FLYC_STATE state) {
        if (state == DataOsdGetPushCommon.FLYC_STATE.Atti || state == DataOsdGetPushCommon.FLYC_STATE.Atti_CL || state == DataOsdGetPushCommon.FLYC_STATE.Atti_Hover || state == DataOsdGetPushCommon.FLYC_STATE.Atti_Limited || state == DataOsdGetPushCommon.FLYC_STATE.AttiLangding) {
            return true;
        }
        return false;
    }

    public static boolean checkIsAttiMode(FlightMode state) {
        if (state == FlightMode.ATTI || state == FlightMode.ATTI_COURSE_LOCK || state == FlightMode.ATTI_HOVER || state == FlightMode.ATTI_LIMITED || state == FlightMode.ATTI_LANDING) {
            return true;
        }
        return false;
    }

    public static boolean isMultipleModeOpen() {
        DJISDKCacheParamValue value = DJISDKCache.getInstance().getAvailableValue(KeyHelper.getFlightControllerKey(FlightControllerKeys.MULTI_MODE_OPEN));
        if (value != null) {
            return ((Boolean) value.getData()).booleanValue();
        }
        return false;
    }

    public static boolean checkIsPAttiMode(DataOsdGetPushCommon.FLYC_STATE mode) {
        if (!checkIsAttiMode(mode) || useModePToSmart(DJIProductManager.getInstance().getType())) {
            return false;
        }
        boolean open = isMultipleModeOpen();
        DataOsdGetPushCommon.RcModeChannel channel = DataOsdGetPushCommon.getInstance().getModeChannel();
        if (!open || channel != DataOsdGetPushCommon.RcModeChannel.CHANNEL_A) {
            return true;
        }
        return false;
    }

    public static boolean useModePToSmart(ProductType type) {
        if (type == null) {
            type = DJIProductManager.getInstance().getType();
        }
        if (type == ProductType.Tomato || type == ProductType.Pomato || type == ProductType.PomatoSDR || type == ProductType.PomatoRTK || isKumquatSeries(type) || isNewA3FlightMode()) {
            return true;
        }
        return false;
    }

    public static boolean isNewA3FlightMode() {
        if (DataOsdGetPushCommon.getInstance().getFlycVersion() < 16) {
            return false;
        }
        ProductType type = DJIProductManager.getInstance().getType();
        if (type == ProductType.A3 || type == ProductType.N3) {
            return true;
        }
        return false;
    }

    public static boolean isCompassDisturb(DataOsdGetPushCommon.NON_GPS_CAUSE cause) {
        return cause == DataOsdGetPushCommon.NON_GPS_CAUSE.COMPASS_ERROR_LARGE || cause == DataOsdGetPushCommon.NON_GPS_CAUSE.SPEED_ERROR_LARGE || cause == DataOsdGetPushCommon.NON_GPS_CAUSE.YAW_ERROR_LARGE;
    }

    public static boolean isWifiProduct(ProductType type) {
        if (type == null) {
            type = DJIProductManager.getInstance().getType();
        }
        return type.isFromWifi() || DJIUSBWifiSwitchManager.getInstance().isProductWifiConnected(type);
    }

    public static boolean isSdrProducts(ProductType type) {
        if (type == null) {
            type = DJIProductManager.getInstance().getType();
        }
        return (isKumquatSeries(type) || isWM240Series(type) || isPM420Platform() || type == ProductType.PomatoSDR || type == ProductType.PomatoRTK) && !DJIUSBWifiSwitchManager.getInstance().isProductWifiConnected(type);
    }

    public static boolean checkRcBatteryLow(int power) {
        DataOsdGetPushCheckStatus checkStatus = DataOsdGetPushCheckStatus.getInstance();
        if (checkStatus.isGetted()) {
            return checkStatus.getPowerStatus();
        }
        if (isKumquatSeries(null)) {
            if (power >= 15) {
                return false;
            }
            return true;
        } else if (power >= 30) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean isMultiStreamPlatform() {
        Model model = (Model) CacheHelper.getProduct(ProductKeys.MODEL_NAME);
        return model != null && (model == Model.INSPIRE_2 || model == Model.MATRICE_200 || model == Model.MATRICE_210 || model == Model.MATRICE_210_RTK || model == Model.MATRICE_PM420 || model == Model.MATRICE_PM420PRO || model == Model.MATRICE_PM420PRO_RTK || model == Model.MATRICE_600 || model == Model.MATRICE_600_PRO || model == Model.A3 || model == Model.N3);
    }

    public static boolean isNeedAntiDistortion() {
        Model model = (Model) CacheHelper.getProduct(ProductKeys.MODEL_NAME);
        return model == Model.MAVIC_2_ZOOM || model == Model.MAVIC_2_PRO || model == Model.MAVIC_2_ENTERPRISE;
    }

    public static boolean isNeedFetchKeyFrame() {
        return isMavic2SeriesProduct();
    }

    public static boolean isPlatformWithCameraAndFpv() {
        Model model = (Model) CacheHelper.getProduct(ProductKeys.MODEL_NAME);
        return model != null && (model == Model.INSPIRE_2 || model == Model.MATRICE_200 || model == Model.MATRICE_210 || model == Model.MATRICE_210_RTK);
    }

    public static boolean isPM420Platform() {
        Model model = (Model) CacheHelper.getProduct(ProductKeys.MODEL_NAME);
        return model != null && (model == Model.MATRICE_PM420 || model == Model.MATRICE_PM420PRO || model == Model.MATRICE_PM420PRO_RTK);
    }

    public static boolean isSlaveRc(RCMode mode) {
        if (mode == null) {
            RCMode modeParam = (RCMode) CacheHelper.getRemoteController("Mode");
            if (modeParam == null) {
                mode = RCMode.MASTER;
            } else {
                mode = modeParam;
            }
        }
        return mode == RCMode.SLAVE || mode == RCMode.SLAVE_SUB;
    }

    public static boolean isWM230(ProductType productType) {
        if (productType == null) {
            productType = DJIProductManager.getInstance().getType();
        }
        return productType == ProductType.WM230;
    }

    public static boolean isWM240(ProductType productType) {
        if (productType == null) {
            productType = DJIProductManager.getInstance().getType();
        }
        return productType == ProductType.WM240;
    }

    public static boolean isWM240Series(ProductType productType) {
        if (productType == null) {
            productType = DJIProductManager.getInstance().getType();
        }
        return productType == ProductType.WM240 || productType == ProductType.WM245;
    }

    public static boolean isMavic2SeriesProduct() {
        Model model = (Model) CacheHelper.getProduct(ProductKeys.MODEL_NAME);
        return model == Model.MAVIC_2_ZOOM || model == Model.MAVIC_2_PRO || model == Model.MAVIC_2_ENTERPRISE || model == Model.MAVIC_2_ENTERPRISE_DUAL;
    }

    public static boolean isLockGimbalWhenShootSupported() {
        Model model = (Model) CacheHelper.getProduct(ProductKeys.MODEL_NAME);
        return model == Model.MAVIC_2_ZOOM || model == Model.MAVIC_2_PRO || model == Model.MAVIC_2_ENTERPRISE || model == Model.MAVIC_PRO || model == Model.MAVIC_AIR;
    }

    public static boolean isWM160(ProductType productType) {
        if (productType == null) {
            productType = DJIProductManager.getInstance().getType();
        }
        return productType == ProductType.WM160;
    }

    public static boolean isRunningMission(FlightMode state) {
        return state == FlightMode.TERRAIN_FOLLOW || state == FlightMode.GPS_HOME_LOCK || state == FlightMode.GPS_WAYPOINT || state == FlightMode.GPS_HOT_POINT || state == FlightMode.GPS_FOLLOW_ME || state == FlightMode.GPS_COURSE_LOCK;
    }

    public static boolean isSNAccessAuthorized() {
        return InternalDataProtectionGuard.getInstance().isHardwareInfoAuthorized() || IAreaCode.STR_AREA_CODE_CHINA.equals(getAreaCodeFromCache());
    }

    public static boolean supportDualBand() {
        boolean disable;
        DJIAreaCodeEvent areaCode = getAreaCodeEvent();
        if (areaCode == null || IAreaCode.STR_AREA_CODE_JAPAN.equals(areaCode.areaCode) || "RU".equals(areaCode.areaCode)) {
            disable = true;
        } else {
            disable = false;
        }
        if (!disable) {
            return true;
        }
        return false;
    }

    public static DJIAreaCodeEvent getAreaCodeEvent() {
        return ((IAreaCodeService) DJIAppServiceManager.getInstance().getService(IAreaCodeService.COMPONENT_NAME)).getAreaCodeEvent();
    }

    public static String getAreaCodeFromCache() {
        return ((IAreaCodeService) DJIAppServiceManager.getInstance().getService(IAreaCodeService.COMPONENT_NAME)).getAreaCodeFromLocal();
    }

    public static boolean isPanoDownloadSupported(ProductType productType) {
        if (productType == null) {
            productType = DJIProductManager.getInstance().getType();
        }
        return productType == ProductType.WM230 || productType == ProductType.WM240 || productType == ProductType.WM245;
    }

    public static boolean isFileIndexModeSupported(SettingsDefinitions.CameraType cameraType) {
        return (cameraType == SettingsDefinitions.CameraType.DJICameraTypeFC1705 && cameraType == SettingsDefinitions.CameraType.DJICameraTypeGD600) ? false : true;
    }

    public static boolean isPhantom4Series(ProductType type) {
        if (type == null) {
            type = DJIProductManager.getInstance().getType();
        }
        return type == ProductType.Tomato || type == ProductType.Potato || type == ProductType.Pomato || type == ProductType.PomatoRTK || type == ProductType.PomatoSDR;
    }

    public static boolean isDM368Supported() {
        ProductType type = DJIProductManager.getInstance().getType();
        return isM200Product(type) || type == ProductType.Orange2 || isPhantom4Series(type);
    }

    public static boolean isProductInspire(ProductType type) {
        if (type == null) {
            type = DJIProductManager.getInstance().getType();
        }
        return type == ProductType.Orange || type == ProductType.BigBanana || type == ProductType.OrangeRAW || type == ProductType.Olives || type == ProductType.OrangeCV600 || type == ProductType.Orange2;
    }

    public static boolean isHardcodedPositionRequired() {
        return isKumquatSeries(null) || isWM240Series(null);
    }

    public static int getMaxRadarDistance(int direction, ProductType type) {
        if (type == null) {
            type = DJIProductManager.getInstance().getType();
        }
        if (direction == VisionSensorPosition.NOSE.value()) {
            if (isP4PSeries(type) || type == ProductType.Potato || type == ProductType.Orange2) {
                return 20;
            }
            return 10;
        } else if (direction == VisionSensorPosition.TAIL.value()) {
            if (isP4PSeries(type) || type == ProductType.Potato || type == ProductType.WM230) {
                return 20;
            }
            if (!isWM240Series(type)) {
                return 6;
            }
            return 10;
        } else if (isWM240Series(type)) {
            return 6;
        } else {
            return 6;
        }
    }

    public static boolean isP4PSeries(ProductType type) {
        if (type == null) {
            type = DJIProductManager.getInstance().getType();
        }
        return type == ProductType.Pomato || type == ProductType.PomatoRTK || type == ProductType.PomatoSDR;
    }

    public static boolean isDynamicHomeSupported(ProductType type) {
        if (type == null) {
            type = DJIProductManager.getInstance().getType();
        }
        long version = DataEyeGetPushException.getInstance().getVisionVersion();
        return (ProductType.Mammoth == type && IVisionResDefine.SupportVersion.VERSION_VISION_SPARK_DYNAMIC_HOME <= version) || (ProductType.KumquatX == type && IVisionResDefine.SupportVersion.VERSION_VISION_KUMAUATX_DYNAMIC_HOME <= version) || isMavic2SeriesProduct();
    }

    public static boolean isDualLight245Camera(int keyIndex) {
        DJISDKCacheParamValue value = DJISDKCache.getInstance().getAvailableValue(KeyHelper.getCameraKey(keyIndex, CameraKeys.CAMERA_TYPE));
        if (value == null || value.getData() != SettingsDefinitions.CameraType.DJICameraTypeFC2403) {
            return false;
        }
        return true;
    }

    public static boolean checkIsBusy(FlightMode mode) {
        return mode == FlightMode.AUTO_TAKEOFF || mode == FlightMode.ATTI_LANDING || mode == FlightMode.AUTO_LANDING || mode == FlightMode.ASSISTED_TAKEOFF || mode == FlightMode.GO_HOME;
    }

    public static boolean isRcSlaveWithIN2() {
        return isSlaveRc(null) && DJIComponentManager.getInstance().getPlatformType() == DJIComponentManager.PlatformType.Inspire2;
    }

    public static boolean isDpad() {
        return DpadProductManager.getInstance().isDpad();
    }

    public static boolean isWifiLinkModel(Model model) {
        return Arrays.asList(WIFI_LINK_MODEL).contains(model);
    }

    public static boolean isWifiLinkModel() {
        return Arrays.asList(WIFI_LINK_MODEL).contains((Model) CacheHelper.getProduct(ProductKeys.MODEL_NAME));
    }

    public static boolean isOcusyncLinkModel(Model model) {
        return Arrays.asList(OCUSYNC_LINK_MODEL).contains(model);
    }

    public static boolean isOcusyncLinkModel() {
        return Arrays.asList(OCUSYNC_LINK_MODEL).contains((Model) CacheHelper.getProduct(ProductKeys.MODEL_NAME));
    }
}
