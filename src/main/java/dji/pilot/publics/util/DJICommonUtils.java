package dji.pilot.publics.util;

import android.content.Context;
import android.content.res.Resources;
import android.location.Location;
import android.util.DisplayMetrics;
import com.drew.metadata.exif.makernotes.NikonType2MakernoteDirectory;
import dji.apppublic.reflect.AppPubInjectManager;
import dji.common.airlink.WiFiFrequencyBand;
import dji.common.airlink.WiFiSelectionMode;
import dji.common.product.Model;
import dji.common.remotecontroller.RCMode;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.logic.manager.DJIUSBWifiSwitchManager;
import dji.logic.utils.DJIProductSupportUtil;
import dji.midware.component.DJIComponentManager;
import dji.midware.component.rc.DJIRcDetectHelper;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.Dpad.DpadProductManager;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.manager.P3.ServiceManager;
import dji.midware.data.model.P3.DataCameraGetMode;
import dji.midware.data.model.P3.DataCameraGetPushStateInfo;
import dji.midware.data.model.P3.DataCameraGetShotInfo;
import dji.midware.data.model.P3.DataCommonGetVersion;
import dji.midware.data.model.P3.DataEyeGetPushFrontAvoidance;
import dji.midware.data.model.P3.DataGimbalGetPushParams;
import dji.midware.data.model.P3.DataGimbalGetPushType;
import dji.midware.data.model.P3.DataOsdGetPushCommon;
import dji.midware.data.model.P3.DataRcGetCustomFuction;
import dji.midware.data.model.P3.DataRcGetFDPushConnectStatus;
import dji.midware.data.model.P3.DataRcGetMaster;
import dji.midware.data.model.P3.DataRcGetPushGpsInfo;
import dji.midware.data.model.P3.DataRcGetPushParams;
import dji.midware.data.model.P3.DataRcGetRcRole;
import dji.midware.data.model.P3.DataRcSetCustomFuction;
import dji.midware.data.model.P3.DataRcSetMaster;
import dji.midware.data.model.P3.DataSmartBatteryGetPushDynamicData;
import dji.midware.util.ContextUtil;
import dji.pilot.configs.IProductConfigs;
import dji.pilot.fpv.camera.ICameraDefine;
import dji.pilot.fpv.camera.util.DJICameraUtil;
import dji.pilot.fpv.control.DJIGenSettingDataManager;
import dji.pilot.fpv.model.IVisionObjs;
import dji.pilot.fpv.util.DJICommonUtil;
import dji.pilot.fpv.util.DJIRcUtil;
import dji.pilot.fpv.util.DJIVisionUtil;
import dji.pilot.publics.objects.DjiSharedPreferencesManager;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.keycatalog.DJISDKCacheKeys;
import dji.sdksharedlib.keycatalog.IntelligentFlightAssistantKeys;
import dji.sdksharedlib.keycatalog.ProductKeys;
import dji.sdksharedlib.keycatalog.airlink.WifiLinkKeys;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

@EXClassNullAway
public class DJICommonUtils {
    public static final int AVOIDANCE_ALL = 0;
    public static final int AVOIDANCE_CLOSED = 3;
    public static final int AVOIDANCE_FRONT_END = 1;
    public static final int AVOIDANCE_NONE = 2;
    private static Context CONTEXT_INSTANCE = null;
    private static final String[] LENS_INFO = {"LUMIX G VARIO PZ 14-42/F3.5-5.6", "OLYMPUS M.12MM F2.0", "OLYMPUS M.17MM F1.8", "OLYMPUS M.25MM F1.8", "OLYMPUS M.45MM F1.8", "OLYMPUS M.9-18MM F4.0-5.6"};
    private static final float MAX_DISTANCE = 100000.0f;
    private static final String SUPPORT_CENDENCE_MAX_SPEED_VERSION_X4S = "01.01.01.27";
    private static final String SUPPORT_CENDENCE_MAX_SPEED_VERSION_X5S = "01.01.01.27";
    private static final String SUPPORT_CENDENCE_MAX_SPEED_VERSION_X7 = "00.02.05.17";
    private static final float[] distanceResult = new float[2];

    public static String generateProductSpKey(String key) {
        return generateProductKey(key, DJIProductManager.getInstance().getType());
    }

    public static boolean isSpotMeteringNotSupportedInRecordMode() {
        return DataCameraGetPushStateInfo.getInstance().getMode() == DataCameraGetMode.MODE.RECORD && (isMammoth() || isWM230() || isWM240());
    }

    public static String generateProductKey(String key, ProductType type) {
        String newKey = key;
        if (type == ProductType.Orange || type == ProductType.BigBanana || type == ProductType.OrangeRAW || type == ProductType.OrangeCV600 || type == ProductType.None || type == ProductType.OTHER) {
            return newKey;
        }
        if (type != null) {
            return type.toString() + key;
        }
        return ProductType.OTHER._name();
    }

    public static boolean isProductLitchi() {
        return isProductLitchi(DJIProductManager.getInstance().getType());
    }

    public static boolean isGimbalControlNeedToBeBlocked() {
        return CacheHelper.toBool(CacheHelper.getFlightAssistant(IntelligentFlightAssistantKeys.IS_TIME_LAPSE_EXECUTING));
    }

    public static boolean isC1C2ForwardDown() {
        ProductType type = DJIProductManager.getInstance().getType();
        return isProductLitchi(type) || type == ProductType.Mammoth || type == ProductType.WM240;
    }

    public static boolean isUsingWiFiToConnectMavic(ProductType type) {
        if (type == null) {
            type = DJIProductManager.getInstance().getType();
        }
        return DJIUSBWifiSwitchManager.getInstance().isProductWifiConnected(type);
    }

    public static boolean isPomatoSeries(ProductType type) {
        if (type == null) {
            type = DJIProductManager.getInstance().getType();
        }
        return ProductType.Pomato == type || ProductType.PomatoSDR == type || ProductType.PomatoRTK == type;
    }

    public static boolean isProductLitchi(ProductType type) {
        if (type == null) {
            type = DJIProductManager.getInstance().getType();
        }
        return type == ProductType.litchiC || type == ProductType.litchiS || type == ProductType.litchiX || type == ProductType.P34K || type == ProductType.Tomato || isKumquatSeries(type) || type == ProductType.Potato || type == ProductType.WM240 || isPomatoSeries(type);
    }

    public static boolean isVideoRecordSupportedByGesture() {
        return isWM230() || isMammoth();
    }

    public static boolean isP4Series(ProductType type) {
        if (type == null) {
            type = DJIProductManager.getInstance().getType();
        }
        return type == ProductType.Tomato || type == ProductType.Pomato || type == ProductType.Potato;
    }

    public static boolean useNewBattery() {
        ProductType type = DJIProductManager.getInstance().getType();
        return isProductM600Series(type) || isKumquatSeries(type) || isMammoth() || isWM230() || isWM240();
    }

    public static boolean isPhantomSeries(Model model) {
        if (model == null) {
            model = (Model) CacheHelper.getProduct(ProductKeys.MODEL_NAME);
        }
        if (model == null) {
            return false;
        }
        if (model == Model.Phantom_3_4K || model == Model.PHANTOM_3_ADVANCED || model == Model.PHANTOM_3_PROFESSIONAL || model == Model.PHANTOM_3_STANDARD || model == Model.PHANTOM_4 || model == Model.PHANTOM_4_PRO || model == Model.PHANTOM_4_ADVANCED || model == Model.PHANTOM_4_PRO_V2 || model == Model.PHANTOM_4_RTK) {
            return true;
        }
        return false;
    }

    public static boolean isRcChannelRequiredToWarning(DataOsdGetPushCommon.RcModeChannel rcChannel) {
        return rcChannel == DataOsdGetPushCommon.RcModeChannel.CHANNEL_A || rcChannel == DataOsdGetPushCommon.RcModeChannel.CHANNEL_S || rcChannel == DataOsdGetPushCommon.RcModeChannel.CHANNEL_T;
    }

    public static boolean isKumquatSeries(ProductType type) {
        if (type == null) {
            type = DJIProductManager.getInstance().getType();
        }
        return type == ProductType.KumquatX || type == ProductType.KumquatS;
    }

    public static boolean isP4SDR(ProductType type) {
        if (type == null) {
            type = DJIProductManager.getInstance().getType();
        }
        return type == ProductType.PomatoSDR;
    }

    public static boolean isMammoth() {
        return DJIProductManager.getInstance().getType() == ProductType.Mammoth;
    }

    public static boolean isWM230() {
        return DJIProductManager.getInstance().getType() == ProductType.WM230;
    }

    public static boolean isWM240() {
        return DJIProductManager.getInstance().getType() == ProductType.WM240;
    }

    public static boolean isWM160() {
        return DJIProductManager.getInstance().getType() == ProductType.WM160;
    }

    public static boolean isSideVisionCloseWhenPropellerCover() {
        return isWM240();
    }

    public static boolean isHasselbladCamera() {
        return DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC240_1.equals(DataCameraGetPushStateInfo.getInstance().getCameraType());
    }

    public static boolean isSupportHyperLapse() {
        return isWM240();
    }

    public static boolean isSupportWp2() {
        return isWM240();
    }

    public static boolean supportSwitchPanoToOtherMode() {
        ProductType pType = DJIProductManager.getInstance().getType();
        return isWM230() || isWM240() || isMammoth() || isP4SDR(pType) || pType == ProductType.Pomato;
    }

    public static boolean needMFSwitch(DataCameraGetPushStateInfo.CameraType cType) {
        return DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC220 == cType || DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC220S == cType || DJICameraUtil.isP4ProCamera(cType);
    }

    public static boolean isPanoFinishedByAircraft() {
        return isWM230();
    }

    public static boolean isSupportMultiSaveOriginal() {
        return isWM240();
    }

    public static boolean isProductM600Series(ProductType type) {
        if (type == null) {
            type = DJIProductManager.getInstance().getType();
        }
        return type == ProductType.PM820 || type == ProductType.PM820PRO;
    }

    public static boolean isInAtti(String tag) {
        DataOsdGetPushCommon.FLYC_STATE flycState = DataOsdGetPushCommon.getInstance().getFlycState();
        if (tag != null) {
            DJILog.logWriteD(tag, "FLYCSTATE " + flycState, tag, new Object[0]);
        }
        if (flycState == DataOsdGetPushCommon.FLYC_STATE.Atti || flycState == DataOsdGetPushCommon.FLYC_STATE.Atti_CL || flycState == DataOsdGetPushCommon.FLYC_STATE.Atti_Hover || flycState == DataOsdGetPushCommon.FLYC_STATE.Atti_Limited) {
            return true;
        }
        return false;
    }

    public static boolean isMultiBattery(ProductType type) {
        if (type == null) {
            type = DJIProductManager.getInstance().getType();
        }
        return type == ProductType.PM820 || type == ProductType.PM820PRO || type == ProductType.Orange2 || isM200Product(type);
    }

    public static boolean isSupportNewLandingProtocol() {
        return isWM240();
    }

    public static boolean isOnlySupportGimbalSwitchBetweenFpvAndFollow() {
        return DJICommonUtil.isProductLitchi() || isMammoth() || isWM230();
    }

    public static boolean isEmpty(String str) {
        if (str == null || str.length() == 0) {
            return true;
        }
        return false;
    }

    public static boolean isSdrProducts(ProductType type) {
        if (type == null) {
            type = DJIProductManager.getInstance().getType();
        }
        return (isKumquatSeries(type) && !DJIUSBWifiSwitchManager.getInstance().isProductWifiConnected(type)) || isWM240() || isP4SDR(null);
    }

    public static boolean checkWifiSsidValid(String ssid) {
        return Pattern.compile("[A-Z0-9a-z-_ ]{1,32}").matcher(ssid).matches();
    }

    public static boolean checkWifiPwdValid(String pwd) {
        return Pattern.compile("[A-Z0-9a-z]{8,63}").matcher(pwd).matches();
    }

    public static boolean isProductOrange(ProductType type) {
        if (type == null) {
            type = DJIProductManager.getInstance().getType();
        }
        return type == ProductType.Orange || type == ProductType.N1 || type == ProductType.BigBanana || type == ProductType.Olives || type == ProductType.OrangeRAW || type == ProductType.OrangeCV600 || type == ProductType.Orange2 || isM200Product(type);
    }

    public static boolean isProductOrange2(ProductType type) {
        if (type == null) {
            type = DJIProductManager.getInstance().getType();
        }
        return type == ProductType.Orange2;
    }

    public static boolean isProductOrange2() {
        return DJIProductManager.getInstance().getType() == ProductType.Orange2;
    }

    public static boolean isRcPro() {
        return DJIComponentManager.getInstance().getRcComponentType() == DJIComponentManager.RcComponentType.Cendence;
    }

    public static boolean isDoubleCamera() {
        return isProductOrange2() || isM200Product(null);
    }

    public static boolean isInspireSeries(Model model) {
        if (model == null) {
            model = (Model) CacheHelper.getProduct(ProductKeys.MODEL_NAME);
        }
        if (model == null) {
            return false;
        }
        if (model == Model.INSPIRE_1 || model == Model.INSPIRE_1_PRO || model == Model.INSPIRE_1_RAW || model == Model.MATRICE_100 || model == Model.ZENMUSE_Z3 || model == Model.INSPIRE_2 || model == Model.MATRICE_200 || model == Model.MATRICE_210 || model == Model.MATRICE_210_RTK) {
            return true;
        }
        return false;
    }

    public static boolean doesProductHaveFrontBackSensors() {
        ProductType type = DJIProductManager.getInstance().getType();
        return type == ProductType.WM240 || type == ProductType.WM230 || type == ProductType.Pomato || type == ProductType.Potato;
    }

    public static boolean isProductInspire(ProductType type) {
        if (type == null) {
            type = DJIProductManager.getInstance().getType();
        }
        return type == ProductType.Orange || type == ProductType.BigBanana || type == ProductType.OrangeRAW || type == ProductType.Olives || type == ProductType.OrangeCV600 || type == ProductType.Orange2;
    }

    public static boolean isProductLongan(ProductType type) {
        if (type == null) {
            type = DJIProductManager.getInstance().getType();
        }
        return DJIProductSupportUtil.isLonganSeries(type);
    }

    public static boolean isM200Product(ProductType type) {
        if (type == null) {
            type = DJIProductManager.getInstance().getType();
        }
        return ProductType.M200 == type || ProductType.M210 == type || ProductType.M210RTK == type;
    }

    public static boolean supportRTK(ProductType type) {
        if (type == null) {
            type = DJIProductManager.getInstance().getType();
        }
        return ProductType.M210RTK == type || isA3() || type == ProductType.PomatoRTK;
    }

    public static boolean isSupportRcProduct() {
        ProductType rcType = DJIProductManager.getInstance().getRcType();
        return (rcType == ProductType.Grape2 || rcType == ProductType.Orange || rcType == ProductType.litchiC || rcType == ProductType.P34K || rcType == ProductType.Longan || rcType == ProductType.litchiS) ? false : true;
    }

    public static boolean isSupportProduct(ProductType type) {
        if (type == null) {
            type = DJIProductManager.getInstance().getType();
        }
        boolean isNew = AppPubInjectManager.getAppPubToP3Injectable().isNewApp();
        boolean remoteOK = ServiceManager.getInstance().isRemoteOK();
        if (ServiceManager.getInstance().isConnected() && !remoteOK && isNew) {
            return isSupportRcProduct();
        }
        boolean newSeries = false;
        int i = 0;
        int length = IProductConfigs.APP_SUPPORT_LIST.length;
        while (true) {
            if (i >= length) {
                break;
            } else if (IProductConfigs.APP_SUPPORT_LIST[i] == type) {
                newSeries = true;
                break;
            } else {
                i++;
            }
        }
        boolean oldSeries = !newSeries || type == ProductType.Tomato || type == ProductType.KumquatX;
        if (!isNew) {
            return oldSeries;
        }
        return newSeries;
    }

    public static boolean isCompatibleProduct(ProductType type) {
        if (type == null) {
            type = DJIProductManager.getInstance().getType();
        }
        if (AppPubInjectManager.getAppPubToP3Injectable().isNewApp()) {
            return false;
        }
        if (type == ProductType.Tomato || type == ProductType.KumquatX) {
            return true;
        }
        return false;
    }

    public static boolean hasCoachMode(ProductType type, long version) {
        if (type == null) {
            type = DJIProductManager.getInstance().getRcType();
        }
        if (!isEnableRcSlave(type)) {
            return false;
        }
        if (isProductOrange2() || isM200Product(null)) {
            return false;
        }
        if (ProductType.Grape2 == type) {
            if (67895552 > version) {
                return false;
            }
            return true;
        } else if (67764230 > version) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean isEnableRcSlave(ProductType type) {
        if (type == null) {
            type = DJIProductManager.getInstance().getType();
        }
        return type == ProductType.Orange || type == ProductType.N1 || type == ProductType.Grape2 || type == ProductType.A2 || type == ProductType.BigBanana || type == ProductType.OrangeRAW || type == ProductType.Olives || type == ProductType.OrangeCV600 || type == ProductType.A3 || isProductM600Series(type) || type == ProductType.N3 || type == ProductType.Orange2 || isM200Product(type);
    }

    public static boolean supportSomatoSensoryGimbal(ProductType type) {
        if (DpadProductManager.getInstance().isDpad()) {
            return false;
        }
        if (type == null) {
            type = DJIProductManager.getInstance().getType();
        }
        if ((type != ProductType.Mammoth || isConnectedToMammothRC()) && (type != ProductType.WM230 || isConnectedToWM230RC())) {
            return false;
        }
        return true;
    }

    public static boolean supportVisual(ProductType type) {
        if (type == null) {
            type = DJIProductManager.getInstance().getType();
        }
        return type == ProductType.Tomato || isPomatoSeries(type) || isKumquatSeries(type) || type == ProductType.Orange2 || type == ProductType.Potato || isM200Product(null) || type == ProductType.Mammoth || type == ProductType.WM230 || type == ProductType.WM240;
    }

    public static boolean isBackwardsInTrackingSupported() {
        return supportVisual(null) && !isWM240();
    }

    public static IVisionObjs.AvoidType getFrontVisionAvoidType(ProductType type) {
        if (type == null) {
            type = DJIProductManager.getInstance().getType();
        }
        if (ProductType.Tomato == type || isPomatoSeries(type) || type == ProductType.Orange2 || type == ProductType.Potato || isM200Product(type) || isKumquatSeries(type) || isWM230() || isWM240()) {
            return IVisionObjs.AvoidType.RADAR;
        }
        if (ProductType.Mammoth == type) {
            return IVisionObjs.AvoidType.TOF_MAMMOTH;
        }
        return IVisionObjs.AvoidType.NON;
    }

    public static IVisionObjs.AvoidType getBackVisionAvoidType(ProductType type) {
        if (type == null) {
            type = DJIProductManager.getInstance().getType();
        }
        if (isPomatoSeries(type) || type == ProductType.WM230 || type == ProductType.WM240) {
            return IVisionObjs.AvoidType.RADAR;
        }
        return IVisionObjs.AvoidType.NON;
    }

    public static IVisionObjs.AvoidType getLeftVisionAvoidType(ProductType type) {
        if (type == null) {
            type = DJIProductManager.getInstance().getType();
        }
        if (isPomatoSeries(type)) {
            return IVisionObjs.AvoidType.TOF;
        }
        if (supportNewVisionStatus()) {
            return IVisionObjs.AvoidType.RADAR;
        }
        return IVisionObjs.AvoidType.NON;
    }

    public static boolean isUseTofRadarImg(DataEyeGetPushFrontAvoidance.SensorType type) {
        return isWM240() && (type == DataEyeGetPushFrontAvoidance.SensorType.Left || type == DataEyeGetPushFrontAvoidance.SensorType.Right);
    }

    public static boolean isEagleProduct(ProductType type) {
        if (type == null) {
            type = DJIProductManager.getInstance().getType();
        }
        return type == ProductType.WM230 || type == ProductType.WM240 || type == ProductType.WM160;
    }

    public static IVisionObjs.AvoidType getRightVisionAvoidType(ProductType type) {
        if (type == null) {
            type = DJIProductManager.getInstance().getType();
        }
        if (isPomatoSeries(type)) {
            return IVisionObjs.AvoidType.TOF;
        }
        if (supportNewVisionStatus()) {
            return IVisionObjs.AvoidType.RADAR;
        }
        return IVisionObjs.AvoidType.NON;
    }

    public static IVisionObjs.AvoidType getBottomVisionAvoidType(ProductType type) {
        if (type == null) {
            type = DJIProductManager.getInstance().getType();
        }
        if (type == ProductType.litchiX || type == ProductType.litchiS || type == ProductType.P34K || type == ProductType.Orange || type == ProductType.OrangeRAW || type == ProductType.BigBanana || type == ProductType.Olives || type == ProductType.Tomato || isPomatoSeries(type) || type == ProductType.Orange2 || type == ProductType.OrangeCV600 || type == ProductType.Potato || type == ProductType.Mammoth || type == ProductType.WM230 || isM200Product(type)) {
            return IVisionObjs.AvoidType.RADAR;
        }
        return IVisionObjs.AvoidType.NON;
    }

    public static IVisionObjs.AvoidType getTopVisionAvoidType(ProductType type) {
        if (type == null) {
            type = DJIProductManager.getInstance().getType();
        }
        if (ProductType.Orange2 == type || isM200Product(type) || isWM240()) {
            return IVisionObjs.AvoidType.TOF;
        }
        return IVisionObjs.AvoidType.NON;
    }

    public static boolean hasGuidance(ProductType type) {
        if (type == null) {
            type = DJIProductManager.getInstance().getType();
        }
        return type == ProductType.N1;
    }

    public static boolean checkLatitude(double latitude) {
        double absLatitude = Math.abs(latitude);
        return 1.0E-6d < absLatitude && absLatitude <= 90.0d;
    }

    public static float[] calculateLocation(double lan1, double long1, double lan2, double long2) {
        float[] results = new float[2];
        float distance = distanceBetween(lan1, long1, lan2, long2);
        if (distance <= 0.0f) {
            results[0] = 0.0f;
        } else {
            double angle = Math.toDegrees(Math.asin((double) (distanceBetween(lan1, long2, lan2, long2) / distance)));
            if (lan2 > lan1) {
                if (long2 <= long1) {
                    angle = 180.0d - angle;
                }
            } else if (long2 > long1) {
                angle = 360.0d - angle;
            } else {
                angle += 180.0d;
            }
            if (Double.isNaN(angle)) {
                angle = 0.0d;
            }
            results[0] = (float) angle;
        }
        results[1] = distance;
        return results;
    }

    public static boolean checkGpsValid() {
        DataOsdGetPushCommon common = DataOsdGetPushCommon.getInstance();
        if (!common.isGetted()) {
            return false;
        }
        if (isOldMC() || common.getFlycVersion() < 6) {
            return checkGpsNumValid(common.getGpsNum());
        }
        return common.getGpsLevel() >= 3;
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

    public static boolean isOldMC() {
        return isOldMC(DataOsdGetPushCommon.getInstance().getDroneType());
    }

    public static boolean isOldMC(DataOsdGetPushCommon.DroneType type) {
        return type == DataOsdGetPushCommon.DroneType.A2 || type == DataOsdGetPushCommon.DroneType.WKM || type == DataOsdGetPushCommon.DroneType.NAZA;
    }

    public static float distanceBetween(double latitue1, double longtitue1, double latitue2, double longtitue2) {
        float[] res = new float[2];
        Location.distanceBetween(latitue1, longtitue1, latitue2, longtitue2, res);
        if (res[0] <= 0.0f || res[0] > MAX_DISTANCE) {
            res[0] = 0.0f;
        }
        return res[0];
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{java.util.Arrays.fill(float[], float):void}
     arg types: [float[], int]
     candidates:
      ClspMth{java.util.Arrays.fill(double[], double):void}
      ClspMth{java.util.Arrays.fill(byte[], byte):void}
      ClspMth{java.util.Arrays.fill(long[], long):void}
      ClspMth{java.util.Arrays.fill(boolean[], boolean):void}
      ClspMth{java.util.Arrays.fill(char[], char):void}
      ClspMth{java.util.Arrays.fill(short[], short):void}
      ClspMth{java.util.Arrays.fill(java.lang.Object[], java.lang.Object):void}
      ClspMth{java.util.Arrays.fill(int[], int):void}
      ClspMth{java.util.Arrays.fill(float[], float):void} */
    public static float distanceBetweenNoMax(double latitue1, double longtitue1, double latitue2, double longtitue2) {
        Arrays.fill(distanceResult, 0.0f);
        Location.distanceBetween(latitue1, longtitue1, latitue2, longtitue2, distanceResult);
        if (distanceResult[0] <= 0.0f) {
            distanceResult[0] = 0.0f;
        }
        return distanceResult[0];
    }

    public static boolean checkLongitude(double longitude) {
        double absLongitude = Math.abs(longitude);
        return 1.0E-6d < absLongitude && absLongitude <= 180.0d;
    }

    public static boolean isLocationValid(double latitude, double longitude) {
        return checkLatitude(latitude) && checkLongitude(longitude);
    }

    public static boolean checkUseNewModeChannel() {
        return checkUseNewModeChannel(DataOsdGetPushCommon.getInstance());
    }

    public static boolean checkUseNewModeChannel(DataOsdGetPushCommon common) {
        return common.getFlycVersion() >= 7;
    }

    public static boolean supportRedundancySenor() {
        ProductType type = DJIProductManager.getInstance().getType();
        return type == ProductType.Tomato || isPomatoSeries(type) || type == ProductType.Orange2 || type == ProductType.Potato || isA3() || isM200Product(null) || isKumquatSeries(type) || type == ProductType.Mammoth || type == ProductType.WM230 || type == ProductType.WM240;
    }

    public static boolean supportA3ImuSensor() {
        ProductType type = DJIProductManager.getInstance().getType();
        if (isProductM600Series(type) || type == ProductType.A3 || type == ProductType.Tomato || isPomatoSeries(type) || isKumquatSeries(type) || type == ProductType.N3 || isProductOrange2() || type == ProductType.Potato || isM200Product(type) || type == ProductType.Mammoth || type == ProductType.WM230 || type == ProductType.WM240) {
            return true;
        }
        return false;
    }

    public static boolean hasFPVCamera() {
        ProductType pType = DJIProductManager.getInstance().getType();
        return ProductType.Orange2 == pType || isM200Product(pType);
    }

    public static int getImuCount(ProductType type) {
        if (type == null) {
            type = DJIProductManager.getInstance().getType();
        }
        if (type == ProductType.Tomato || isPomatoSeries(type) || type == ProductType.Potato || isKumquatSeries(type) || isProductOrange2() || isM200Product(type) || isWM230() || isWM240()) {
            return 2;
        }
        if (isA3()) {
            return 3;
        }
        return 1;
    }

    public static int getCompassCount(ProductType type) {
        if (type == null) {
            type = DJIProductManager.getInstance().getType();
        }
        if (type == ProductType.Tomato || isPomatoSeries(type) || isKumquatSeries(type) || type == ProductType.N3 || type == ProductType.Potato || type == ProductType.M210 || type == ProductType.M210RTK) {
            return 2;
        }
        if (!isA3() || type == ProductType.N3) {
            return 1;
        }
        return 3;
    }

    public static boolean isA3() {
        ProductType type = DJIProductManager.getInstance().getType();
        return type == ProductType.A3 || isProductM600Series(type) || type == ProductType.N3;
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

    public static boolean isA3Flyc() {
        DataOsdGetPushCommon.DroneType dType = DataOsdGetPushCommon.getInstance().getDroneType();
        return DataOsdGetPushCommon.DroneType.A3 == dType || DataOsdGetPushCommon.DroneType.PM820 == dType || dType == DataOsdGetPushCommon.DroneType.N3;
    }

    public static boolean isM600() {
        return DJIProductManager.getInstance().getType() == ProductType.PM820 || DJIProductManager.getInstance().getType() == ProductType.PM820PRO;
    }

    public static boolean supportRCC1C2(ProductType type) {
        ProductType rctype = DJIProductManager.getInstance().getRcType();
        boolean fromWifi = DJIUSBWifiSwitchManager.getInstance().isProductWifiConnected(null);
        if ((type == ProductType.litchiC || type == ProductType.Mammoth || type == ProductType.WM230 || isProductLongan(type) || rctype == ProductType.Grape2 || type == ProductType.A2 || fromWifi || isRcPro()) && !isProductM600Series(type) && type != ProductType.A3 && type != ProductType.N3) {
            return false;
        }
        return true;
    }

    public static boolean supportFnC() {
        return isConnectedToMammothRC() || isConnectedToWM230RC();
    }

    public static boolean supportCtrlForearmLamp(ProductType type, int flycVersion) {
        return (isProductLitchi(null) && flycVersion >= 6) || isWM240() || (isProductOrange(null) && flycVersion >= 13);
    }

    public static int[] getDefaultCustomTypes() {
        int[] types = new int[2];
        ProductType pType = DJIProductManager.getInstance().getType();
        if (isPomatoSeries(pType)) {
            types[0] = DataRcSetCustomFuction.DJICustomType.CameraSetting.value();
            types[1] = DataRcSetCustomFuction.DJICustomType.CameraSetting.value();
        } else if (isKumquatSeries(null)) {
            types[0] = DataRcSetCustomFuction.DJICustomType.GimbalCenter.value();
            types[1] = DataRcSetCustomFuction.DJICustomType.CameraSetting.value();
        } else if (isProductLitchi(pType) || isProductOrange(pType)) {
            types[0] = DataRcSetCustomFuction.DJICustomType.GimbalCenter.value();
            types[1] = DataRcSetCustomFuction.DJICustomType.CameraSetting.value();
        } else if (pType == ProductType.Mammoth) {
            types[0] = DataRcSetCustomFuction.DJICustomType.Navigation.value();
            types[1] = DataRcSetCustomFuction.DJICustomType.CameraSetting.value();
        } else {
            types[0] = DataRcGetCustomFuction.getInstance().getC1();
            types[1] = DataRcGetCustomFuction.getInstance().getC2();
        }
        return types;
    }

    public static boolean supportFdDualController(ProductType rcType) {
        if (rcType == null) {
            rcType = DJIProductManager.getInstance().getRcType();
        }
        return isKumquatSeries(rcType);
    }

    public static DataRcSetCustomFuction.DJICustomType[] getC1C2Setting(Boolean isMaster, Context context) {
        DataRcSetCustomFuction.DJICustomType[] mTypes;
        DataRcSetCustomFuction.DJICustomType[] retTypes;
        int count;
        boolean getted = DataCameraGetPushStateInfo.getInstance().isGetted();
        DataCameraGetPushStateInfo.CameraType cType = getted ? DataCameraGetPushStateInfo.getInstance().getCameraType() : DataCameraGetPushStateInfo.CameraType.OTHER;
        int version = DataOsdGetPushCommon.getInstance().getFlycVersion();
        if (isConnectedToWM230RC()) {
            return new DataRcSetCustomFuction.DJICustomType[]{DataRcSetCustomFuction.DJICustomType.ForeArm, DataRcSetCustomFuction.DJICustomType.CenterMetering, DataRcSetCustomFuction.DJICustomType.AeLock, DataRcSetCustomFuction.DJICustomType.CameraSetting, DataRcSetCustomFuction.DJICustomType.GimbalCenter, DataRcSetCustomFuction.DJICustomType.SwitchGimbalMode, DataRcSetCustomFuction.DJICustomType.Battery, DataRcSetCustomFuction.DJICustomType.Navigation, DataRcSetCustomFuction.DJICustomType.PlayBack, DataRcSetCustomFuction.DJICustomType.MapSwitch, DataRcSetCustomFuction.DJICustomType.OTHER};
        } else if (isConnectedToMammothRC()) {
            return new DataRcSetCustomFuction.DJICustomType[]{DataRcSetCustomFuction.DJICustomType.Navigation, DataRcSetCustomFuction.DJICustomType.CameraSetting, DataRcSetCustomFuction.DJICustomType.PlayBack, DataRcSetCustomFuction.DJICustomType.Battery, DataRcSetCustomFuction.DJICustomType.AeLock, DataRcSetCustomFuction.DJICustomType.GimbalCenter, DataRcSetCustomFuction.DJICustomType.SwitchGimbalMode, DataRcSetCustomFuction.DJICustomType.MapSwitch, DataRcSetCustomFuction.DJICustomType.ClearRoute, DataRcSetCustomFuction.DJICustomType.OneKeyTakeOffLanding};
        } else {
            if (isMaster.booleanValue()) {
                ProductType type = DJIProductManager.getInstance().getType();
                if (DJICameraUtil.checkIsGDCamera(cType)) {
                    mTypes = new DataRcSetCustomFuction.DJICustomType[]{DataRcSetCustomFuction.DJICustomType.CameraSetting, DataRcSetCustomFuction.DJICustomType.GimbalCenter, DataRcSetCustomFuction.DJICustomType.SwitchGimbalMode, DataRcSetCustomFuction.DJICustomType.MapSwitch, DataRcSetCustomFuction.DJICustomType.ClearRoute, DataRcSetCustomFuction.DJICustomType.Battery, DataRcSetCustomFuction.DJICustomType.AeLock, DataRcSetCustomFuction.DJICustomType.OTHER};
                } else if (checkIsTauCamera(cType)) {
                    mTypes = supportCtrlForearmLamp(type, version) ? new DataRcSetCustomFuction.DJICustomType[]{DataRcSetCustomFuction.DJICustomType.GimbalCenter, DataRcSetCustomFuction.DJICustomType.SwitchGimbalMode, DataRcSetCustomFuction.DJICustomType.MapSwitch, DataRcSetCustomFuction.DJICustomType.ClearRoute, DataRcSetCustomFuction.DJICustomType.Battery, DataRcSetCustomFuction.DJICustomType.GimbalDirec, DataRcSetCustomFuction.DJICustomType.ForeArm, DataRcSetCustomFuction.DJICustomType.OTHER} : new DataRcSetCustomFuction.DJICustomType[]{DataRcSetCustomFuction.DJICustomType.GimbalCenter, DataRcSetCustomFuction.DJICustomType.SwitchGimbalMode, DataRcSetCustomFuction.DJICustomType.MapSwitch, DataRcSetCustomFuction.DJICustomType.ClearRoute, DataRcSetCustomFuction.DJICustomType.Battery, DataRcSetCustomFuction.DJICustomType.GimbalDirec, DataRcSetCustomFuction.DJICustomType.OTHER};
                } else if (type == ProductType.Tomato) {
                    mTypes = new DataRcSetCustomFuction.DJICustomType[]{DataRcSetCustomFuction.DJICustomType.CameraSetting, DataRcSetCustomFuction.DJICustomType.GimbalCenter, DataRcSetCustomFuction.DJICustomType.SwitchGimbalMode, DataRcSetCustomFuction.DJICustomType.MapSwitch, DataRcSetCustomFuction.DJICustomType.ClearRoute, DataRcSetCustomFuction.DJICustomType.Battery, DataRcSetCustomFuction.DJICustomType.CenterMetering, DataRcSetCustomFuction.DJICustomType.AeLock, DataRcSetCustomFuction.DJICustomType.ForeArm, DataRcSetCustomFuction.DJICustomType.OTHER};
                } else if (isPomatoSeries(type) || type == ProductType.Potato) {
                    mTypes = new DataRcSetCustomFuction.DJICustomType[]{DataRcSetCustomFuction.DJICustomType.CameraSetting, DataRcSetCustomFuction.DJICustomType.GimbalCenter, DataRcSetCustomFuction.DJICustomType.SwitchGimbalMode, DataRcSetCustomFuction.DJICustomType.MapSwitch, DataRcSetCustomFuction.DJICustomType.ClearRoute, DataRcSetCustomFuction.DJICustomType.Battery, DataRcSetCustomFuction.DJICustomType.CenterMetering, DataRcSetCustomFuction.DJICustomType.AeLock, DataRcSetCustomFuction.DJICustomType.ForeArm, DataRcSetCustomFuction.DJICustomType.Vision1, DataRcSetCustomFuction.DJICustomType.OTHER};
                } else if (type == ProductType.Mammoth) {
                    mTypes = new DataRcSetCustomFuction.DJICustomType[]{DataRcSetCustomFuction.DJICustomType.Navigation, DataRcSetCustomFuction.DJICustomType.CameraSetting, DataRcSetCustomFuction.DJICustomType.PlayBack, DataRcSetCustomFuction.DJICustomType.Battery, DataRcSetCustomFuction.DJICustomType.AeLock, DataRcSetCustomFuction.DJICustomType.GimbalCenter, DataRcSetCustomFuction.DJICustomType.SwitchGimbalMode, DataRcSetCustomFuction.DJICustomType.MapSwitch, DataRcSetCustomFuction.DJICustomType.ClearRoute, DataRcSetCustomFuction.DJICustomType.OTHER};
                } else if (isProductOrange(type)) {
                    if (type == ProductType.Orange2) {
                        ArrayList<DataRcSetCustomFuction.DJICustomType> list = new ArrayList<>();
                        list.addAll(Arrays.asList(DataRcSetCustomFuction.DJICustomType.CameraSetting, DataRcSetCustomFuction.DJICustomType.GimbalCenter, DataRcSetCustomFuction.DJICustomType.ColorOscilloScope, DataRcSetCustomFuction.DJICustomType.SwitchGimbalMode, DataRcSetCustomFuction.DJICustomType.AeLock, DataRcSetCustomFuction.DJICustomType.GimbalRecenter, DataRcSetCustomFuction.DJICustomType.LiveViewExpand, DataRcSetCustomFuction.DJICustomType.QuickCircle, DataRcSetCustomFuction.DJICustomType.Vision1, DataRcSetCustomFuction.DJICustomType.CenterFocus, DataRcSetCustomFuction.DJICustomType.OTHER));
                        if (isSupportGimbalPitchYaw()) {
                            list.add(list.indexOf(DataRcSetCustomFuction.DJICustomType.GimbalCenter) + 1, DataRcSetCustomFuction.DJICustomType.GimbalPitchYawCenter);
                        }
                        if (isSupportGimbalDownCenter()) {
                            list.add(list.indexOf(DataRcSetCustomFuction.DJICustomType.GimbalCenter) + 1, DataRcSetCustomFuction.DJICustomType.GimbalDownCenter);
                        }
                        if (DJIRcUtil.isNarrowBandWorkAsMaster()) {
                            list.add(list.indexOf(DataRcSetCustomFuction.DJICustomType.GimbalCenter) + 1, DataRcSetCustomFuction.DJICustomType.MasterSlaveGroup);
                        }
                        mTypes = (DataRcSetCustomFuction.DJICustomType[]) list.toArray(new DataRcSetCustomFuction.DJICustomType[0]);
                    } else {
                        mTypes = isM200Product(type) ? new DataRcSetCustomFuction.DJICustomType[]{DataRcSetCustomFuction.DJICustomType.CameraSetting, DataRcSetCustomFuction.DJICustomType.GimbalCenter, DataRcSetCustomFuction.DJICustomType.SwitchGimbalMode, DataRcSetCustomFuction.DJICustomType.AeLock, DataRcSetCustomFuction.DJICustomType.GimbalRecenter, DataRcSetCustomFuction.DJICustomType.LiveViewExpand, DataRcSetCustomFuction.DJICustomType.Vision1, DataRcSetCustomFuction.DJICustomType.CenterFocus, DataRcSetCustomFuction.DJICustomType.MapSwitch, DataRcSetCustomFuction.DJICustomType.OTHER} : supportCtrlForearmLamp(type, version) ? new DataRcSetCustomFuction.DJICustomType[]{DataRcSetCustomFuction.DJICustomType.CameraSetting, DataRcSetCustomFuction.DJICustomType.GimbalCenter, DataRcSetCustomFuction.DJICustomType.SwitchGimbalMode, DataRcSetCustomFuction.DJICustomType.MapSwitch, DataRcSetCustomFuction.DJICustomType.ClearRoute, DataRcSetCustomFuction.DJICustomType.Battery, DataRcSetCustomFuction.DJICustomType.CenterMetering, DataRcSetCustomFuction.DJICustomType.ForeArm, DataRcSetCustomFuction.DJICustomType.OTHER} : new DataRcSetCustomFuction.DJICustomType[]{DataRcSetCustomFuction.DJICustomType.CameraSetting, DataRcSetCustomFuction.DJICustomType.GimbalCenter, DataRcSetCustomFuction.DJICustomType.SwitchGimbalMode, DataRcSetCustomFuction.DJICustomType.MapSwitch, DataRcSetCustomFuction.DJICustomType.ClearRoute, DataRcSetCustomFuction.DJICustomType.Battery, DataRcSetCustomFuction.DJICustomType.CenterMetering, DataRcSetCustomFuction.DJICustomType.OTHER};
                    }
                } else if (isProductM600Series(type)) {
                    DataGimbalGetPushType.DJIGimbalType gimbalType = DataGimbalGetPushType.DJIGimbalType.OTHER;
                    if (DataGimbalGetPushType.getInstance().isGetted()) {
                        gimbalType = DataGimbalGetPushType.getInstance().getType();
                    }
                    mTypes = gimbalType == DataGimbalGetPushType.DJIGimbalType.Z15 ? new DataRcSetCustomFuction.DJICustomType[]{DataRcSetCustomFuction.DJICustomType.CameraSetting, DataRcSetCustomFuction.DJICustomType.SwitchGimbalMode, DataRcSetCustomFuction.DJICustomType.MapSwitch, DataRcSetCustomFuction.DJICustomType.ClearRoute, DataRcSetCustomFuction.DJICustomType.Battery, DataRcSetCustomFuction.DJICustomType.GimbalDirec, DataRcSetCustomFuction.DJICustomType.CenterMetering, DataRcSetCustomFuction.DJICustomType.OTHER} : new DataRcSetCustomFuction.DJICustomType[]{DataRcSetCustomFuction.DJICustomType.CameraSetting, DataRcSetCustomFuction.DJICustomType.GimbalCenter, DataRcSetCustomFuction.DJICustomType.SwitchGimbalMode, DataRcSetCustomFuction.DJICustomType.MapSwitch, DataRcSetCustomFuction.DJICustomType.ClearRoute, DataRcSetCustomFuction.DJICustomType.Battery, DataRcSetCustomFuction.DJICustomType.GimbalDirec, DataRcSetCustomFuction.DJICustomType.CenterMetering, DataRcSetCustomFuction.DJICustomType.OTHER};
                } else if (!supportCtrlForearmLamp(type, version)) {
                    mTypes = (type == ProductType.A3 || type == ProductType.N3) ? new DataRcSetCustomFuction.DJICustomType[]{DataRcSetCustomFuction.DJICustomType.MapSwitch, DataRcSetCustomFuction.DJICustomType.Battery, DataRcSetCustomFuction.DJICustomType.OTHER} : new DataRcSetCustomFuction.DJICustomType[]{DataRcSetCustomFuction.DJICustomType.CameraSetting, DataRcSetCustomFuction.DJICustomType.GimbalCenter, DataRcSetCustomFuction.DJICustomType.SwitchGimbalMode, DataRcSetCustomFuction.DJICustomType.MapSwitch, DataRcSetCustomFuction.DJICustomType.ClearRoute, DataRcSetCustomFuction.DJICustomType.Battery, DataRcSetCustomFuction.DJICustomType.CenterMetering, DataRcSetCustomFuction.DJICustomType.OTHER};
                } else if (isKumquatSeries(type)) {
                    DataCommonGetVersion verGetter = DJIRcDetectHelper.getInstance().getKumquat1860Getter();
                    mTypes = (!verGetter.isGetted() || verGetter.getFirmVer(".").compareTo(DJIRcDetectHelper.VERSION_KUMQUAT_SUPPORT_ISO_SHUTTER) < 0) ? new DataRcSetCustomFuction.DJICustomType[]{DataRcSetCustomFuction.DJICustomType.CameraSetting, DataRcSetCustomFuction.DJICustomType.GimbalCenter, DataRcSetCustomFuction.DJICustomType.SwitchGimbalMode, DataRcSetCustomFuction.DJICustomType.MapSwitch, DataRcSetCustomFuction.DJICustomType.ClearRoute, DataRcSetCustomFuction.DJICustomType.Battery, DataRcSetCustomFuction.DJICustomType.CenterMetering, DataRcSetCustomFuction.DJICustomType.ForeArm, DataRcSetCustomFuction.DJICustomType.Navigation, DataRcSetCustomFuction.DJICustomType.PlayBack, DataRcSetCustomFuction.DJICustomType.AeLock, DataRcSetCustomFuction.DJICustomType.CenterFocus, DataRcSetCustomFuction.DJICustomType.OTHER} : new DataRcSetCustomFuction.DJICustomType[]{DataRcSetCustomFuction.DJICustomType.CameraSetting, DataRcSetCustomFuction.DJICustomType.GimbalCenter, DataRcSetCustomFuction.DJICustomType.SwitchGimbalMode, DataRcSetCustomFuction.DJICustomType.MapSwitch, DataRcSetCustomFuction.DJICustomType.ClearRoute, DataRcSetCustomFuction.DJICustomType.Battery, DataRcSetCustomFuction.DJICustomType.CenterMetering, DataRcSetCustomFuction.DJICustomType.ForeArm, DataRcSetCustomFuction.DJICustomType.Navigation, DataRcSetCustomFuction.DJICustomType.PlayBack, DataRcSetCustomFuction.DJICustomType.AeLock, DataRcSetCustomFuction.DJICustomType.CenterFocus, DataRcSetCustomFuction.DJICustomType.IndexShutterISOSwitch, DataRcSetCustomFuction.DJICustomType.OTHER};
                } else {
                    mTypes = isWM240() ? new DataRcSetCustomFuction.DJICustomType[]{DataRcSetCustomFuction.DJICustomType.ForeArm, DataRcSetCustomFuction.DJICustomType.CenterMetering, DataRcSetCustomFuction.DJICustomType.AeLock, DataRcSetCustomFuction.DJICustomType.CameraSetting, DataRcSetCustomFuction.DJICustomType.GimbalCenter, DataRcSetCustomFuction.DJICustomType.SwitchGimbalMode, DataRcSetCustomFuction.DJICustomType.MapSwitch, DataRcSetCustomFuction.DJICustomType.Battery, DataRcSetCustomFuction.DJICustomType.Navigation, DataRcSetCustomFuction.DJICustomType.PlayBack, DataRcSetCustomFuction.DJICustomType.CenterFocus, DataRcSetCustomFuction.DJICustomType.ClearTipsForConsume, DataRcSetCustomFuction.DJICustomType.DownVisionLight, DataRcSetCustomFuction.DJICustomType.VisionSwitch, DataRcSetCustomFuction.DJICustomType.OTHER} : new DataRcSetCustomFuction.DJICustomType[]{DataRcSetCustomFuction.DJICustomType.CameraSetting, DataRcSetCustomFuction.DJICustomType.GimbalCenter, DataRcSetCustomFuction.DJICustomType.SwitchGimbalMode, DataRcSetCustomFuction.DJICustomType.MapSwitch, DataRcSetCustomFuction.DJICustomType.ClearRoute, DataRcSetCustomFuction.DJICustomType.Battery, DataRcSetCustomFuction.DJICustomType.CenterMetering, DataRcSetCustomFuction.DJICustomType.ForeArm, DataRcSetCustomFuction.DJICustomType.OTHER};
                }
            } else {
                ProductType type2 = DJIProductManager.getInstance().getType();
                if (checkIsTauCamera(cType)) {
                    mTypes = new DataRcSetCustomFuction.DJICustomType[]{DataRcSetCustomFuction.DJICustomType.GimbalCenter, DataRcSetCustomFuction.DJICustomType.SwitchGimbalMode, DataRcSetCustomFuction.DJICustomType.MapSwitch, DataRcSetCustomFuction.DJICustomType.ClearRoute, DataRcSetCustomFuction.DJICustomType.OTHER};
                } else if (DJIRcUtil.isNarrowBandWorkAsAssistant()) {
                    mTypes = new DataRcSetCustomFuction.DJICustomType[]{DataRcSetCustomFuction.DJICustomType.CameraSetting, DataRcSetCustomFuction.DJICustomType.ColorOscilloScope, DataRcSetCustomFuction.DJICustomType.MasterSlaveGroup, DataRcSetCustomFuction.DJICustomType.OTHER};
                } else if (type2 == ProductType.Orange2 || isM200Product(type2)) {
                    ArrayList<DataRcSetCustomFuction.DJICustomType> list2 = new ArrayList<>();
                    list2.addAll(Arrays.asList(DataRcSetCustomFuction.DJICustomType.CameraSetting, DataRcSetCustomFuction.DJICustomType.ColorOscilloScope, DataRcSetCustomFuction.DJICustomType.GimbalCenter, DataRcSetCustomFuction.DJICustomType.AeLock, DataRcSetCustomFuction.DJICustomType.CenterFocus, DataRcSetCustomFuction.DJICustomType.SwitchGimbalMode, DataRcSetCustomFuction.DJICustomType.OTHER));
                    if (isSupportGimbalPitchYaw()) {
                        list2.add(list2.indexOf(DataRcSetCustomFuction.DJICustomType.GimbalCenter) + 1, DataRcSetCustomFuction.DJICustomType.GimbalPitchYawCenter);
                    }
                    if (isSupportGimbalDownCenter()) {
                        list2.add(list2.indexOf(DataRcSetCustomFuction.DJICustomType.GimbalCenter) + 1, DataRcSetCustomFuction.DJICustomType.GimbalDownCenter);
                    }
                    mTypes = (DataRcSetCustomFuction.DJICustomType[]) list2.toArray(new DataRcSetCustomFuction.DJICustomType[0]);
                } else {
                    mTypes = new DataRcSetCustomFuction.DJICustomType[]{DataRcSetCustomFuction.DJICustomType.CameraSetting, DataRcSetCustomFuction.DJICustomType.GimbalCenter, DataRcSetCustomFuction.DJICustomType.SwitchGimbalMode, DataRcSetCustomFuction.DJICustomType.MapSwitch, DataRcSetCustomFuction.DJICustomType.ClearRoute, DataRcSetCustomFuction.DJICustomType.OTHER};
                }
            }
            if (getted || mTypes == null || mTypes.length <= 0) {
                retTypes = mTypes;
            } else {
                DataRcSetCustomFuction.DJICustomType[] delTypes = {DataRcSetCustomFuction.DJICustomType.CameraSetting, DataRcSetCustomFuction.DJICustomType.CenterMetering};
                int count2 = mTypes.length;
                int length = mTypes.length;
                for (int i = 0; i < length; i++) {
                    int j = 0;
                    int size = delTypes.length;
                    while (true) {
                        if (j >= size) {
                            break;
                        }
                        if (delTypes[j] == mTypes[i]) {
                            count2--;
                            break;
                        }
                        j++;
                    }
                }
                retTypes = new DataRcSetCustomFuction.DJICustomType[count2];
                int i2 = 0;
                int count3 = 0;
                while (i2 < length) {
                    boolean deled = false;
                    int j2 = 0;
                    int size2 = delTypes.length;
                    while (true) {
                        if (j2 >= size2) {
                            break;
                        }
                        if (delTypes[j2] == mTypes[i2]) {
                            deled = true;
                            break;
                        }
                        j2++;
                    }
                    if (!deled) {
                        count = count3 + 1;
                        retTypes[count3] = mTypes[i2];
                    } else {
                        count = count3;
                    }
                    i2++;
                    count3 = count;
                }
            }
            return retTypes;
        }
    }

    public static boolean checkIsTauCamera(DataCameraGetPushStateInfo.CameraType type) {
        return type == DataCameraGetPushStateInfo.CameraType.DJICameraTypeTau336 || type == DataCameraGetPushStateInfo.CameraType.DJICameraTypeTau640;
    }

    public static boolean isM600BatteryError() {
        return (17732923532771328L & DataSmartBatteryGetPushDynamicData.getInstance().getStatus()) != 0;
    }

    public static String convertTime(int totalSecs) {
        return String.format("%02d:%02d:%02d", Integer.valueOf(totalSecs / NikonType2MakernoteDirectory.TAG_NIKON_SCAN), Integer.valueOf((totalSecs % NikonType2MakernoteDirectory.TAG_NIKON_SCAN) / 60), Integer.valueOf(totalSecs % 60));
    }

    public static String convertTimeToMinAndSecond(int totalSecs) {
        return String.format("%02d:%02d", Integer.valueOf((totalSecs % NikonType2MakernoteDirectory.TAG_NIKON_SCAN) / 60), Integer.valueOf(totalSecs % 60));
    }

    public static boolean isLb2(ProductType type) {
        if (ServiceManager.getInstance().isRemoteOK()) {
            if (type == ProductType.Grape2 || type == ProductType.A2) {
                return true;
            }
            return false;
        } else if (DJIProductManager.getInstance().getRcType() == ProductType.Grape2 || type == ProductType.A2) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean rcHandleCustomKeySelf(ProductType rcType) {
        return ProductType.Pomato == rcType || ProductType.Orange2 == rcType || rcType == ProductType.Potato;
    }

    public static boolean hasSportMode(ProductType type) {
        if (type == null) {
            type = DJIProductManager.getInstance().getType();
        }
        return type == ProductType.Tomato || isPomatoSeries(type) || type == ProductType.Orange2 || type == ProductType.Potato || isKumquatSeries(type) || isM200Product(type) || (isNewA3Flyc() && (type == ProductType.N3 || type == ProductType.A3)) || isWM230() || isWM240() || isMammoth();
    }

    public static boolean isNewA3Flyc() {
        return DataOsdGetPushCommon.getInstance().getFlycVersion() >= 16;
    }

    public static long formatVersion(String firmware) {
        String[] firms;
        long result = 0;
        try {
            for (String str : firmware.split("\\.")) {
                result = (256 * result) + ((long) Integer.parseInt(str, 10));
            }
            return result;
        } catch (Exception e) {
            return 0;
        }
    }

    public static boolean isTablet() {
        return isTablet(ContextUtil.getContext());
    }

    public static boolean isTablet(Context context) {
        if (DpadProductManager.getInstance().isDpad()) {
            DisplayMetrics dm = context.getResources().getDisplayMetrics();
            float f = dm.density;
            int width3 = dm.widthPixels;
            int i = dm.heightPixels;
            if (width3 == 2048 || width3 == 1536) {
                return true;
            }
            return false;
        } else if ((context.getResources().getConfiguration().screenLayout & 15) < 3) {
            return false;
        } else {
            return true;
        }
    }

    public static String formatLength(String format, float length, boolean upperCase) {
        DJIGenSettingDataManager dm = DJIGenSettingDataManager.getInstance();
        int unit = dm.getParameterUnit();
        String lengthStr = String.format(Locale.US, format, Float.valueOf(dm.transformLength(length)));
        String unitStr = "m";
        if (unit == 0) {
            unitStr = "ft";
        }
        if (upperCase) {
            unitStr = unitStr.toUpperCase(Locale.US);
        }
        return lengthStr + unitStr;
    }

    public static String formatSpeed(String format, float speed, boolean upperCase) {
        DJIGenSettingDataManager dm = DJIGenSettingDataManager.getInstance();
        int unit = dm.getParameterUnit();
        String speedStr = String.format(Locale.US, format, Float.valueOf(dm.transformSpeed(speed)));
        String unitStr = "m/s";
        if (unit == 0) {
            unitStr = "mph";
        } else if (2 == unit) {
            unitStr = "kph";
        }
        if (upperCase) {
            unitStr = unitStr.toUpperCase(Locale.US);
        }
        return speedStr + unitStr;
    }

    public static float getMaxAssSpeedForView() {
        int max = 10;
        ProductType pType = DJIProductManager.getInstance().getType();
        if (isPomatoSeries(pType) || pType == ProductType.Potato) {
            max = 14;
        } else if (ProductType.Orange2 == pType || isM200Product(pType)) {
            max = 15;
        } else if (ProductType.Mammoth == pType) {
            max = 3;
        } else if (ProductType.WM230 == pType) {
            max = 8;
        }
        return (float) max;
    }

    public static float getMaxAssSpeed() {
        int max = 10;
        ProductType pType = DJIProductManager.getInstance().getType();
        if (isPomatoSeries(pType) || pType == ProductType.Potato) {
            max = 13;
        } else if (ProductType.Orange2 == pType || isM200Product(pType)) {
            max = 15;
        } else if (ProductType.Mammoth == pType) {
            max = 3;
        } else if (ProductType.WM230 == pType || ProductType.WM240 == pType) {
            max = 8;
        }
        return (float) max;
    }

    public static int getMaxTrackingAccessSpeed() {
        if (isKumquatSeries(DJIProductManager.getInstance().getType())) {
            return 15;
        }
        if (isWM230()) {
            return 12;
        }
        if (isWM240()) {
            return 20;
        }
        return 10;
    }

    public static synchronized Context getContext() {
        Context context;
        synchronized (DJICommonUtils.class) {
            if (CONTEXT_INSTANCE == null) {
                try {
                    Class<?> ActivityThread = Class.forName("android.app.ActivityThread");
                    Object currentActivityThread = ActivityThread.getMethod("currentActivityThread", new Class[0]).invoke(ActivityThread, new Object[0]);
                    CONTEXT_INSTANCE = (Context) currentActivityThread.getClass().getMethod("getApplication", new Class[0]).invoke(currentActivityThread, new Object[0]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            context = CONTEXT_INSTANCE;
        }
        return context;
    }

    public static boolean isRcSlaveMode() {
        RCMode modeParam = (RCMode) CacheHelper.getRemoteController("Mode");
        return modeParam != null && modeParam == RCMode.SLAVE;
    }

    public static boolean isAFCSupported() {
        if (!DjiSharedPreferencesManager.getBoolean(getContext(), ICameraDefine.AFC_SWITCH, true) || !supportAFC(DataCameraGetPushStateInfo.getInstance().getCameraType(), DataCameraGetPushStateInfo.getInstance().getVerstion())) {
            return false;
        }
        return true;
    }

    public static boolean supportAFC(DataCameraGetPushStateInfo.CameraType cameraType, int version) {
        return (version >= 9 && (cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC220 || DJICameraUtil.isP4ProCamera(cameraType))) || wm620SupportAFC() || DJICameraUtil.checkIsWM240Camera(null);
    }

    public static boolean wm620SupportAFC() {
        DataCameraGetPushStateInfo info = DataCameraGetPushStateInfo.getInstance();
        if (info.getCameraType() != DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6510 || info.getWm620CameraProtocolVersion() < 1) {
            return false;
        }
        return true;
    }

    public static int pxToDp(int px) {
        return (int) (((float) px) / Resources.getSystem().getDisplayMetrics().density);
    }

    public static int dpToPx(int dp) {
        return (int) (((float) dp) * Resources.getSystem().getDisplayMetrics().density);
    }

    public static boolean isMultiLedControl(String firmware) {
        if (firmware == null) {
            firmware = (String) CacheHelper.getFlightController(DJISDKCacheKeys.FIRMWARE_VERSION);
        }
        if (firmware == null) {
            return false;
        }
        long curVer = formatVersion(firmware);
        ProductType type = DJIProductManager.getInstance().getType();
        if ((type.equals(ProductType.Orange2) || isM200Product(type)) && curVer >= formatVersion("03.02.14.40")) {
            return true;
        }
        return false;
    }

    public static boolean isMultiLedControlOnlyFront() {
        return isWM240();
    }

    public static boolean isNeedTripodSetting(String firmware) {
        long curVer = formatVersion(firmware);
        ProductType type = DJIProductManager.getInstance().getType();
        if ((type.equals(ProductType.Orange2) || isM200Product(type)) && curVer >= formatVersion("03.02.14.34") && DataRcGetMaster.getInstance().getMode() == DataRcSetMaster.MODE.Master) {
            return true;
        }
        return isWM240();
    }

    public static boolean isHasTOF(ProductType productType) {
        if (productType == null) {
            productType = DJIProductManager.getInstance().getType();
        }
        return isPomatoSeries(productType);
    }

    public static boolean isNeedWbQuickSetting(ProductType productType) {
        if (productType == null) {
            productType = DJIProductManager.getInstance().getType();
        }
        return productType == ProductType.Orange2;
    }

    public static int transformLimitHeight(boolean beginnerMode, boolean nonRcWifiProduct, int height) {
        if (beginnerMode) {
            return 30;
        }
        if (!nonRcWifiProduct || height <= 50) {
            return height;
        }
        return 50;
    }

    public static boolean is100MDistanceLimited() {
        return isMammothWiFiConnected() || isWM230WiFiConnected();
    }

    public static boolean isHighAltitudeFanSupported() {
        ProductType productType = DJIProductManager.getInstance().getType();
        return productType == ProductType.Orange2 || productType == ProductType.PM820 || productType == ProductType.PM820PRO || productType == ProductType.Orange || isM200Product(productType);
    }

    public static boolean isNeedRightCtrlObjectTips(ProductType productType) {
        if (productType == null) {
            productType = DJIProductManager.getInstance().getType();
        }
        if (isRcPro()) {
            return false;
        }
        if (productType == ProductType.Orange2 || isPomatoSeries(productType) || isM200Product(productType)) {
            return true;
        }
        return false;
    }

    public static boolean isRunningMission(DataOsdGetPushCommon.FLYC_STATE state) {
        return state == DataOsdGetPushCommon.FLYC_STATE.TERRAIN_TRACKING || state == DataOsdGetPushCommon.FLYC_STATE.GPS_HomeLock || state == DataOsdGetPushCommon.FLYC_STATE.NaviGo || state == DataOsdGetPushCommon.FLYC_STATE.GPS_HotPoint || state == DataOsdGetPushCommon.FLYC_STATE.NaviMissionFollow || state == DataOsdGetPushCommon.FLYC_STATE.TRIPOD_GPS || state == DataOsdGetPushCommon.FLYC_STATE.Cinematic || state == DataOsdGetPushCommon.FLYC_STATE.GPS_CL;
    }

    public static boolean isPrecisionTakeOffSupported() {
        if (!CacheHelper.toBool(CacheHelper.getFlightAssistant(IntelligentFlightAssistantKeys.VISION_ASSISTED_POSITIONING_ENABLED)) || DataOsdGetPushCommon.getInstance().getFlycVersion() < 25) {
            return false;
        }
        ProductType type = DJIProductManager.getInstance().getType();
        if (isKumquatSeries(null) || isPomatoSeries(type) || ProductType.Potato == type || isM200Product(type) || isWM230()) {
            return checkGpsValid();
        }
        return false;
    }

    public static boolean isDpadProduct(ProductType type) {
        return isPomatoSeries(type) || type == ProductType.Potato;
    }

    public static boolean isUseSlaveMode() {
        DataRcGetFDPushConnectStatus rcConnectStatus = DataRcGetFDPushConnectStatus.getInstance();
        if (!rcConnectStatus.isGetted() || rcConnectStatus.isPushLosed()) {
            return RcModeHelper.isSlaveRc(null);
        }
        return (rcConnectStatus.getRole() == DataRcGetRcRole.RcRole.SLAVE_CONTROL || rcConnectStatus.getRole() == DataRcGetRcRole.RcRole.SLAVE_CONTROL_SUB) && rcConnectStatus.getMainControlStatus() == 2;
    }

    public static boolean isMotorUp() {
        if (ServiceManager.getInstance().isRemoteOK() && DataOsdGetPushCommon.getInstance().isGetted()) {
            return DataOsdGetPushCommon.getInstance().isMotorUp();
        }
        return false;
    }

    public static void saveCurrentFirmwareVersion(String version) {
        DjiSharedPreferencesManager.putString(getContext(), "cur_firmware_ver", version);
    }

    public static String readCurrentFirmwareVersion() {
        return DjiSharedPreferencesManager.getString(getContext(), "cur_firmware_ver", "00.00.0000");
    }

    public static boolean isSupportEsc() {
        ProductType type = DJIProductManager.getInstance().getType();
        if ((isPomatoSeries(type) || type == ProductType.Orange2 || ProductType.Tomato == type || type == ProductType.Potato || isM200Product(type)) && DataOsdGetPushCommon.getInstance().getFlycVersion() > 15) {
            return true;
        }
        return false;
    }

    public static boolean isSupportEscForFindMyDrone() {
        return DataOsdGetPushCommon.getInstance().getFlycVersion() > 15;
    }

    public static boolean isCanChangeWifiIdWhenActivate(ProductType type) {
        if (type == null) {
            type = DJIProductManager.getInstance().getType();
        }
        return type == ProductType.Mammoth;
    }

    public static boolean isYawControlledByLongClickSupported() {
        return isMammothWiFiConnected() || isWM230WiFiConnected();
    }

    public static boolean doesHDViewNeedSwitchingToWiFiView() {
        return DJICommonUtil.isWifiProduct(DJIProductManager.getInstance().getType()) || isConnectedToMammothRC() || isConnectedToWM230RC();
    }

    public static boolean isConnectedToMammothRC() {
        return isMammoth() && (DJIUSBWifiSwitchManager.getInstance().isRcWifiConnected(null) || DJIUSBWifiSwitchManager.getInstance().isUSBAoaConnected());
    }

    public static boolean isConnectedToWM230RC() {
        return isWM230() && DJIUSBWifiSwitchManager.getInstance().isUSBAoaConnected();
    }

    public static boolean isSupportSmartCapture() {
        return isWM230();
    }

    public static boolean isSupportApas() {
        return DJIVisionUtil.supportAPAS(DJIProductManager.getInstance().getType());
    }

    public static WiFiFrequencyBand getWiFiFrequencyBand() {
        if (!isMammoth() && !isWM230()) {
            return (WiFiFrequencyBand) CacheHelper.getWiFiAirLink("FrequencyBand");
        }
        if (CacheHelper.toInt(CacheHelper.getWiFiAirLink(WifiLinkKeys.CHANNEL_NUMBER)) > 100) {
            return WiFiFrequencyBand.FREQUENCY_BAND_5_GHZ;
        }
        return WiFiFrequencyBand.FREQUENCY_BAND_2_DOT_4_GHZ;
    }

    public static boolean isSupportWifiSettingWhenActivation(ProductType type) {
        if (type == null) {
            type = DJIProductManager.getInstance().getType();
        }
        return type.equals(ProductType.KumquatX) || type.equals(ProductType.KumquatS);
    }

    public static boolean isSupportMapTypePOI() {
        return DJIComponentManager.getInstance().getPlatformType() == DJIComponentManager.PlatformType.Inspire2;
    }

    public static boolean isVirtualStickSupported() {
        boolean result = (isKumquatSeries(null) && DJIUSBWifiSwitchManager.getInstance().isWifiConnected()) || (DJICommonUtil.isMammoth() && !isConnectedToMammothRC()) || (isWM230() && !isConnectedToWM230RC());
        ProductType type = DJIProductManager.getInstance().getType();
        ProductType rcType = DJIProductManager.getInstance().getRcType();
        DataCameraGetPushStateInfo.CameraType cameraType = DataCameraGetPushStateInfo.getInstance().getCameraType();
        boolean isWifi = DJIUSBWifiSwitchManager.getInstance().isWifiConnected();
        DJILog.saveLog("type=" + type + " rcType=" + rcType + " cameraType=" + cameraType + " isWifi=" + isWifi + " isAoa=" + DJIUSBWifiSwitchManager.getInstance().isUSBAoaConnected() + " isRcWifi=" + DJIUSBWifiSwitchManager.getInstance().isRcWifiConnected(type), "joyStickE");
        return result;
    }

    public static String getCurrentTimeInMillSecond() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
    }

    public static boolean isMammothWiFiConnected() {
        return isMammoth() && !isConnectedToMammothRC();
    }

    public static boolean isThirdLensNeedNofity() {
        String name = (DataCameraGetShotInfo.getInstance().getName() + "").toLowerCase();
        for (String len : LENS_INFO) {
            if (name.contains(len.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public static int getThirdLensNeedNofityFlagIndex() {
        String name = (DataCameraGetShotInfo.getInstance().getName() + "").toLowerCase();
        for (int i = 0; i < LENS_INFO.length; i++) {
            if (name.contains(LENS_INFO[i].toLowerCase())) {
                return i;
            }
        }
        return -1;
    }

    public static boolean isRcUsingWiFiModule() {
        return DJIUSBWifiSwitchManager.getInstance().isWifiConnected() || isWM230() || isMammoth() || isWM160();
    }

    public static boolean isSupportYawFirmware() {
        long supportVersion;
        if (DJIComponentManager.getInstance().getPlatformType() != DJIComponentManager.PlatformType.Inspire2) {
            return false;
        }
        DataCommonGetVersion getter = new DataCommonGetVersion();
        getter.setDeviceType(DeviceType.GIMBAL);
        long curVersion = 0;
        try {
            String[] split = getter.getFirmVer(".").split("\\.");
            curVersion = formatVersion(getter.getFirmVer("."));
        } catch (Exception e) {
        }
        if (DJIComponentManager.getInstance().getCameraComponentType() == DJIComponentManager.CameraComponentType.X5S) {
            supportVersion = formatVersion("1.0.0.76");
        } else {
            supportVersion = formatVersion("1.0.0.32");
        }
        if (curVersion >= supportVersion) {
            return true;
        }
        return false;
    }

    public static boolean doesWiFiSelectionModeSupport() {
        return ((WiFiSelectionMode) CacheHelper.getWiFiAirLink(WifiLinkKeys.SELECTION_MODE)) != null && (isWM230() || isMammoth() || isWM160());
    }

    public static boolean isSupportRcHome() {
        return isProductOrange2() && DataRcGetPushGpsInfo.getInstance().isGetted();
    }

    public static boolean isSupportFpvShortcutView() {
        return isKumquatSeries(null) || isProductOrange2();
    }

    public static boolean doesSeriousBatteryWarningNeedToBeBlocked() {
        return isMammoth() || isWM230() || isWM240();
    }

    public static boolean supportNewVisionStatus() {
        return DJIProductManager.getInstance().getType() == ProductType.WM240;
    }

    public static boolean isCendenceSupportGimbalMaxSpeed(String gimbalVersion) {
        if (DJIComponentManager.getInstance().getRcComponentType() != DJIComponentManager.RcComponentType.Cendence) {
            return false;
        }
        DataCommonGetVersion getter = new DataCommonGetVersion();
        getter.setDeviceType(DeviceType.OSD);
        if (formatVersion(getter.getFirmVer(".")) < formatVersion("5.36.19.0") || !isGimbalSupportCendenceGimbalMaxSpeed(gimbalVersion)) {
            return false;
        }
        return true;
    }

    private static boolean isGimbalSupportCendenceGimbalMaxSpeed(String gimbalVersion) {
        if (gimbalVersion == null || gimbalVersion.isEmpty()) {
            return false;
        }
        DataGimbalGetPushType.DJIGimbalType gimbalType = DataGimbalGetPushType.DJIGimbalType.OTHER;
        if (DataGimbalGetPushType.getInstance().isGetted()) {
            gimbalType = DataGimbalGetPushType.getInstance().getType();
        }
        if ((gimbalVersion.compareTo("01.01.01.27") < 0 || gimbalType != DataGimbalGetPushType.DJIGimbalType.WM620_OneInch) && ((gimbalVersion.compareTo("01.01.01.27") < 0 || gimbalType != DataGimbalGetPushType.DJIGimbalType.WM620_X5S) && (gimbalVersion.compareTo(SUPPORT_CENDENCE_MAX_SPEED_VERSION_X7) < 0 || gimbalType != DataGimbalGetPushType.DJIGimbalType.WM620_X7))) {
            return false;
        }
        return true;
    }

    public static boolean isSupportGimbalPitchYaw() {
        DJIComponentManager.RcComponentType rcType = DJIComponentManager.getInstance().getRcComponentType();
        if (rcType != DJIComponentManager.RcComponentType.Inspire2 && rcType != DJIComponentManager.RcComponentType.Cendence) {
            return false;
        }
        DataCommonGetVersion getter = new DataCommonGetVersion();
        getter.setDeviceType(DeviceType.OSD);
        long curVersion = 0;
        try {
            curVersion = formatVersion(getter.getFirmVer("."));
        } catch (Exception e) {
        }
        if (curVersion >= formatVersion("5.24.4.0")) {
            return true;
        }
        return false;
    }

    public static boolean isFpvGimbal() {
        DataGimbalGetPushParams gimbal = DataGimbalGetPushParams.getInstance();
        if (!supportFpvGimabl() || !gimbal.isFpvGimbal()) {
            return false;
        }
        return true;
    }

    public static boolean supportFpvGimabl() {
        ProductType type = DJIProductManager.getInstance().getType();
        if (type == ProductType.Orange2 || isM200Product(type)) {
            return true;
        }
        return false;
    }

    public static boolean isSupportGimbalDownCenter() {
        if (DJIComponentManager.getInstance().getPlatformType() != DJIComponentManager.PlatformType.Inspire2) {
            return false;
        }
        DataCommonGetVersion rcGetter = new DataCommonGetVersion().setDeviceType(DeviceType.OSD);
        DataCommonGetVersion flycGetter = new DataCommonGetVersion().setDeviceType(DeviceType.FLYC);
        DataCommonGetVersion gimbalGetter = new DataCommonGetVersion().setDeviceType(DeviceType.GIMBAL);
        long curRCVersion = formatVersion(rcGetter.getFirmVer("."));
        long curFlycVersion = formatVersion(flycGetter.getFirmVer("."));
        long curGimbalVersion = formatVersion(gimbalGetter.getFirmVer("."));
        long supportRCVersion = formatVersion("5.36.19.0");
        long supportFlycVersion = formatVersion("03.03.00.00");
        long supportGimbalVersionForX5SandX4 = formatVersion("01.01.01.28");
        long supportGimbalVersionForX7 = formatVersion("00.02.05.18");
        boolean isGimbalSupport = false;
        if (DataGimbalGetPushType.getInstance().getType() == DataGimbalGetPushType.DJIGimbalType.WM620_X7) {
            isGimbalSupport = curGimbalVersion >= supportGimbalVersionForX7;
        } else if (DataGimbalGetPushType.getInstance().getType() == DataGimbalGetPushType.DJIGimbalType.WM620_X5S) {
            isGimbalSupport = curGimbalVersion >= supportGimbalVersionForX5SandX4;
        } else if (DataGimbalGetPushType.getInstance().getType() == DataGimbalGetPushType.DJIGimbalType.WM620_OneInch) {
            isGimbalSupport = curGimbalVersion >= supportGimbalVersionForX5SandX4;
        }
        if (curRCVersion < supportRCVersion || curFlycVersion < supportFlycVersion || !isGimbalSupport) {
            return false;
        }
        return true;
    }

    public static boolean isMavicConnected() {
        return ProductType.KumquatX.equals(DJIProductManager.getInstance().getType());
    }

    public static boolean isWM230WiFiConnected() {
        return isWM230() && !isConnectedToWM230RC();
    }

    public static boolean isCinematicSupported() {
        return DataOsdGetPushCommon.getInstance().getFlycVersion() >= 21 && (isKumquatSeries(DJIProductManager.getInstance().getType()) || isWM230() || isWM240());
    }

    public static boolean isRcUsingSimpleAttitudeView() {
        return isConnectedToMammothRC();
    }

    public static boolean isLimitationNotSupportToChange() {
        return (isKumquatSeries(null) && DJIUSBWifiSwitchManager.getInstance().isWifiConnected()) || isMammothWiFiConnected() || isWM230WiFiConnected();
    }

    public static boolean isGimbalExtensionAvailable() {
        return isProductLitchi() || isMammoth() || isWM230() || isWM240() || isWM160();
    }

    public static boolean isPalmControlSupported() {
        return isMammoth() || isWM230();
    }

    public static boolean isGettedGimbalControl() {
        ProductType productType = DJIProductManager.getInstance().getType();
        return !(productType == ProductType.Orange2 || isM200Product(productType) || productType == ProductType.WM230 || productType == ProductType.Mammoth) || !DataRcGetPushParams.getInstance().isGetted() || DataRcGetPushParams.getInstance().isGettedGimbalControl();
    }

    public static boolean doesGestureSupportStartingFromGround() {
        return isWM230();
    }

    public static boolean isPitchMaxSpeedSupported() {
        ProductType productType = DJIProductManager.getInstance().getType();
        return productType.equals(ProductType.Orange2) || isM200Product(productType) || isWM240() || isWM230();
    }

    public static boolean isReturningHomeAtCurrentAltitudeSupported() {
        return isMammoth() || isWM240();
    }

    public static boolean isSupportDownLight() {
        return isWM240();
    }
}
