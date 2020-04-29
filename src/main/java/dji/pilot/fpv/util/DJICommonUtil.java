package dji.pilot.fpv.util;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import dji.component.areacode.IAreaCode;
import dji.component.areacode.IAreaCodeService;
import dji.dbox.upgrade.p4.utils.DJIUpStatusHelper;
import dji.fieldAnnotation.EXClassNullAway;
import dji.logic.manager.DJIUSBWifiSwitchManager;
import dji.logic.utils.DJIProductSupportUtil;
import dji.logic.vision.IVisionResDefine;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.manager.P3.ServiceManager;
import dji.midware.data.model.P3.DataCameraGetPushStateInfo;
import dji.midware.data.model.P3.DataEyeGetPushException;
import dji.midware.data.model.P3.DataFlycGetPushLedStatus;
import dji.midware.data.model.P3.DataGimbalGetPushType;
import dji.midware.data.model.P3.DataOsdGetPushCommon;
import dji.midware.data.model.P3.DataOsdGetPushHome;
import dji.midware.link.DJILinkDaemonService;
import dji.midware.link.DJILinkType;
import dji.pilot.fpv.control.DJIGenSettingDataManager;
import dji.pilot.publics.R;
import dji.pilot.publics.util.DJICommonUtils;
import dji.pilot.publics.util.DJIUnitUtil;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.keycatalog.FlightControllerKeys;
import dji.service.DJIAppServiceManager;
import java.text.DecimalFormat;
import java.util.Locale;
import org.greenrobot.eventbus.EventBus;

@EXClassNullAway
@SuppressLint({"NewApi"})
public class DJICommonUtil {
    public static final int FLAG_SHOW_ALL = 7;
    public static final int FLAG_SHOW_ATTI = 1;
    public static final int FLAG_SHOW_NOVICE = 2;
    public static final int FLAG_SHOW_NOVICE_WHEN_REACH_LIMIT = 4;
    private static final int MAX_SHOW_MFFOCUS_TIP = 1;
    public static final int MULTIPLIER = 60;
    public static boolean isOpenWm240PackageVersionExcludeTimeLapse = true;
    private static boolean mBlackMode = false;
    private static boolean mCompassDisturb = false;
    private static boolean mIsEnableBGDownload = true;
    private static volatile boolean mShowFollowFocusTip = true;
    private static volatile int mShowMFFocusTip = 0;
    private static int mShowTipFlag = 7;
    private static int mTmpModeResId = 0;

    public enum COMMON_ERROR {
        COMPASS_DISTURB,
        COMPASS_NON_DISTURB,
        DEVICE_LOCK
    }

    public static boolean canShowMFFocusTip() {
        boolean ret = true;
        if (mShowMFFocusTip >= 1) {
            ret = false;
        }
        if (ret) {
            mShowMFFocusTip++;
        }
        return ret;
    }

    public static void setCompassDisturb(boolean value) {
        if (CacheHelper.toBool(CacheHelper.getFlightController(FlightControllerKeys.ARE_MOTOR_ON)) && mCompassDisturb != value) {
            mCompassDisturb = value;
            if (value) {
                EventBus.getDefault().post(COMMON_ERROR.COMPASS_DISTURB);
            } else {
                EventBus.getDefault().post(COMMON_ERROR.COMPASS_NON_DISTURB);
            }
        }
    }

    public static boolean isCompassDisturbed() {
        return mCompassDisturb;
    }

    public static void setBlackMode(boolean value) {
        if (mBlackMode != value) {
            mBlackMode = value;
        }
    }

    public static boolean isBlackMode() {
        return mBlackMode;
    }

    public static boolean canShowTip(int flag) {
        return (mShowTipFlag & flag) != 0;
    }

    public static void setShowTip(int flag, boolean can) {
        if (can) {
            mShowTipFlag |= flag;
        } else {
            mShowTipFlag &= flag ^ -1;
        }
    }

    public static void resetShowStatus() {
        mShowMFFocusTip = 0;
        mShowFollowFocusTip = true;
        mShowTipFlag = 7;
        mBlackMode = false;
        setTmpModeResId(0);
        if (mCompassDisturb) {
            mCompassDisturb = false;
            EventBus.getDefault().post(COMMON_ERROR.COMPASS_NON_DISTURB);
        }
    }

    @Deprecated
    public static int getRealFlycModeResId(DataOsdGetPushCommon.FLYC_STATE mode, boolean isVisualWork) {
        int resIds = R.string.fpv_default_str;
        if (DataOsdGetPushCommon.FLYC_STATE.Manula == mode) {
            resIds = R.string.ctrl_mode_manual;
        } else if (DataOsdGetPushCommon.FLYC_STATE.Atti == mode) {
            resIds = R.string.ctrl_mode_atti;
        } else if (DataOsdGetPushCommon.FLYC_STATE.Atti_CL == mode) {
            resIds = R.string.ctrl_mode_atti;
        } else if (DataOsdGetPushCommon.FLYC_STATE.Atti_Hover == mode) {
            resIds = R.string.ctrl_mode_atti;
        } else if (DataOsdGetPushCommon.FLYC_STATE.Atti_Limited == mode) {
            resIds = R.string.ctrl_mode_atti;
        } else if (DataOsdGetPushCommon.FLYC_STATE.AttiLangding == mode) {
            resIds = R.string.ctrl_mode_atti;
        } else if (DataOsdGetPushCommon.FLYC_STATE.AutoLanding == mode) {
            resIds = R.string.ctrl_mode_pgps;
        } else if (DataOsdGetPushCommon.FLYC_STATE.AssitedTakeoff == mode) {
            resIds = R.string.ctrl_mode_pgps;
        } else if (DataOsdGetPushCommon.FLYC_STATE.AutoTakeoff == mode) {
            resIds = R.string.ctrl_mode_pgps;
        } else if (DataOsdGetPushCommon.FLYC_STATE.GoHome == mode) {
            resIds = R.string.ctrl_mode_pgps;
        } else if (DataOsdGetPushCommon.FLYC_STATE.GPS_Atti == mode) {
            resIds = R.string.ctrl_mode_pgps;
        } else if (DataOsdGetPushCommon.FLYC_STATE.GPS_Blake == mode) {
            resIds = R.string.ctrl_mode_pgps;
        } else if (DataOsdGetPushCommon.FLYC_STATE.GPS_CL == mode) {
            resIds = R.string.ctrl_mode_pgps;
        } else if (DataOsdGetPushCommon.FLYC_STATE.GPS_HomeLock == mode) {
            resIds = R.string.ctrl_mode_pgps;
        } else if (DataOsdGetPushCommon.FLYC_STATE.GPS_HotPoint == mode) {
            resIds = R.string.ctrl_mode_pgps;
        } else if (DataOsdGetPushCommon.FLYC_STATE.Hover == mode) {
            resIds = R.string.ctrl_mode_pgps;
        } else if (DataOsdGetPushCommon.FLYC_STATE.Joystick == mode) {
            resIds = R.string.ctrl_mode_atti;
        } else if (DataOsdGetPushCommon.FLYC_STATE.NaviGo == mode) {
            resIds = R.string.ctrl_mode_pgps;
        } else if (DataOsdGetPushCommon.FLYC_STATE.ClickGo == mode) {
            resIds = R.string.ctrl_mode_pgps;
        } else if (DataOsdGetPushCommon.FLYC_STATE.NaviSubMode_Tracking == mode) {
            resIds = R.string.ctrl_mode_pgps;
        } else if (DataOsdGetPushCommon.FLYC_STATE.NaviSubMode_Pointing == mode) {
            resIds = R.string.ctrl_mode_pgps;
        } else if (DataOsdGetPushCommon.FLYC_STATE.SPORT == mode) {
            resIds = R.string.ctrl_mode_sport;
        } else if (DataOsdGetPushCommon.FLYC_STATE.FORCE_LANDING == mode) {
            resIds = R.string.ctrl_mode_landing;
        } else if (DataOsdGetPushCommon.FLYC_STATE.ENGINE_START == mode) {
            resIds = R.string.ctrl_mode_takeoff;
        } else if (DataOsdGetPushCommon.FLYC_STATE.NaviSubMode_Draw == mode) {
            resIds = R.string.ctrl_mode_pgps;
        }
        if (resIds == R.string.ctrl_mode_pgps) {
            if (isVisualWork) {
                return R.string.ctrl_mode_popti;
            }
            return resIds;
        } else if (resIds != R.string.ctrl_mode_atti) {
            return resIds;
        } else {
            DataOsdGetPushCommon.RcModeChannel modeChannel = DataOsdGetPushCommon.getInstance().getModeChannel();
            DataOsdGetPushHome home = DataOsdGetPushHome.getInstance();
            if (!DJIFlycUtil.isMultipleModeOpen(home.isBeginnerMode(), home.isMultipleModeOpen()) || modeChannel == DataOsdGetPushCommon.RcModeChannel.CHANNEL_F || modeChannel == DataOsdGetPushCommon.RcModeChannel.CHANNEL_P) {
                return R.string.ctrl_mode_patti;
            }
            return resIds;
        }
    }

    public static int getDataCodeResId(Ccode code) {
        int resId = R.string.code_unknown;
        if (Ccode.TIMEOUT == code || Ccode.TIMEOUT_REMOTE == code) {
            return R.string.code_timeout;
        }
        if (Ccode.SUCCEED == code) {
            return R.string.code_successed;
        }
        if (Ccode.INVALID_CMD == code) {
            return R.string.code_invalid_cmd;
        }
        if (Ccode.NOT_SUPPORT_CURRENT_STATE == code) {
            return R.string.code_notsupport_now;
        }
        if (Ccode.SDCARD_NOT_INSERTED == code) {
            return R.string.code_sd_removal;
        }
        if (Ccode.SDCARD_FULL == code) {
            return R.string.code_sd_full;
        }
        if (Ccode.SDCARD_ERR == code) {
            return R.string.code_sd_error;
        }
        if (Ccode.CAMERA_CRITICAL_ERR == code) {
            return R.string.code_camera_critical_error;
        }
        if (Ccode.NOCONNECT == code) {
            return R.string.code_disconnect;
        }
        return resId;
    }

    @Deprecated
    public static int getLedReasonResId(DataFlycGetPushLedStatus.LED_REASON reason) {
        int resId = R.string.fpv_led_normal;
        if (DataFlycGetPushLedStatus.LED_REASON.SET_HOME == reason) {
            return R.string.fpv_led_set_home;
        }
        if (DataFlycGetPushLedStatus.LED_REASON.SET_HOT_POINT == reason) {
            return R.string.fpv_led_set_hot_point;
        }
        if (DataFlycGetPushLedStatus.LED_REASON.SET_COURSE_LOCK == reason) {
            return R.string.fpv_led_set_course_lock;
        }
        if (DataFlycGetPushLedStatus.LED_REASON.TEST_LED == reason) {
            return R.string.fpv_led_test;
        }
        if (DataFlycGetPushLedStatus.LED_REASON.USB_IS_VALID == reason) {
            return R.string.fpv_led_usb_is_valid;
        }
        if (DataFlycGetPushLedStatus.LED_REASON.PACKING_FAIL == reason) {
            return R.string.fpv_led_packing_fail;
        }
        if (DataFlycGetPushLedStatus.LED_REASON.PACKING_NORMAL == reason) {
            return R.string.fpv_led_packing_normal;
        }
        if (DataFlycGetPushLedStatus.LED_REASON.NO_ATTI == reason) {
            return R.string.fpv_led_no_atti;
        }
        if (DataFlycGetPushLedStatus.LED_REASON.COMPASS_CALI_STEP0 == reason) {
            return R.string.fpv_led_compass_cali_step0;
        }
        if (DataFlycGetPushLedStatus.LED_REASON.COMPASS_CALI_STEP1 == reason) {
            return R.string.fpv_led_compass_cali_step1;
        }
        if (DataFlycGetPushLedStatus.LED_REASON.COMPASS_CALI_ERROR == reason) {
            return R.string.fpv_led_compass_cali_error;
        }
        if (DataFlycGetPushLedStatus.LED_REASON.SENSOR_TEMP_NOT_READY == reason) {
            return R.string.fpv_led_sensor_temp_not_ready;
        }
        if (DataFlycGetPushLedStatus.LED_REASON.IMU_OR_GYRO_LOST == reason) {
            return R.string.fpv_led_imu_gyro_lost;
        }
        if (DataFlycGetPushLedStatus.LED_REASON.IMU_BAD_ATTI == reason) {
            return R.string.fpv_led_imu_bad_atti;
        }
        if (DataFlycGetPushLedStatus.LED_REASON.SYSTEM_ERROR == reason) {
            return R.string.fpv_led_system_error;
        }
        if (DataFlycGetPushLedStatus.LED_REASON.IMU_ERROR == reason) {
            return R.string.fpv_led_imu_error;
        }
        if (DataFlycGetPushLedStatus.LED_REASON.IMU_NEED_CALI == reason) {
            return R.string.fpv_led_imu_need_cali;
        }
        if (DataFlycGetPushLedStatus.LED_REASON.COMPASS_OUT_RANGE == reason) {
            return R.string.fpv_led_compass_need_cali;
        }
        if (DataFlycGetPushLedStatus.LED_REASON.RC_COMPLETELY_LOST == reason) {
            return R.string.fpv_led_failsafe;
        }
        if (DataFlycGetPushLedStatus.LED_REASON.BATTERY_WARNING == reason) {
            int resId2 = R.string.fpv_led_battery_warning;
            if (DJIProductManager.getInstance().getType() == ProductType.A3 || DJIProductManager.getInstance().getType() == ProductType.N3) {
                return R.string.fpv_led_battery_voltage_warning;
            }
            return resId2;
        } else if (DataFlycGetPushLedStatus.LED_REASON.BATTERY_ERROR == reason) {
            int resId3 = R.string.fpv_led_battery_error;
            if (DJIProductManager.getInstance().getType() == ProductType.A3 || DJIProductManager.getInstance().getType() == ProductType.N3) {
                return R.string.fpv_led_battery_voltage_error;
            }
            return resId3;
        } else if (DataFlycGetPushLedStatus.LED_REASON.IMU_WARNING == reason) {
            return R.string.fpv_led_imu_warning;
        } else {
            if (DataFlycGetPushLedStatus.LED_REASON.SET_FLY_LIMIT == reason) {
                return R.string.fpv_led_set_fly_limit;
            }
            if (DataFlycGetPushLedStatus.LED_REASON.FDI_VIBRATE == reason) {
                return R.string.fpv_led_fdi_vibrate;
            }
            if (DataFlycGetPushLedStatus.LED_REASON.CODE_ERROR == reason) {
                return R.string.fpv_led_coder_error;
            }
            if (DataFlycGetPushLedStatus.LED_REASON.SYSTEM_RECONSTRUCTION == reason) {
                return R.string.fpv_led_system_reconstruction;
            }
            if (DataFlycGetPushLedStatus.LED_REASON.RECORDER_ERROR == reason) {
                return R.string.fpv_led_recorder_error;
            }
            return resId;
        }
    }

    public static boolean checkLatitude(double latitude) {
        return DJICommonUtils.checkLatitude(latitude);
    }

    public static boolean checkLongitude(double longitude) {
        return DJICommonUtils.checkLongitude(longitude);
    }

    public static float distanceBetween(double latitue1, double longtitue1, double latitue2, double longtitue2) {
        return DJICommonUtils.distanceBetween(latitue1, longtitue1, latitue2, longtitue2);
    }

    public static float distanceBetweenNoMax(double latitue1, double longtitue1, double latitue2, double longtitue2) {
        return DJICommonUtils.distanceBetweenNoMax(latitue1, longtitue1, latitue2, longtitue2);
    }

    public static float[] calculateLocation(double lan1, double long1, double lan2, double long2) {
        return DJICommonUtils.calculateLocation(lan1, long1, lan2, long2);
    }

    public static int[] formatSecondToHourMinute(int second) {
        int[] time = new int[2];
        int remainder = second % 60;
        int second2 = second / 60;
        time[0] = second2 % 60;
        if (remainder > 0) {
            time[0] = time[0] + 1;
        }
        time[1] = second2 / 60;
        if (time[0] == 60) {
            time[1] = time[1] + 1;
            time[0] = 0;
        }
        return time;
    }

    public static int[] formatSecondToMinuteAr(int second) {
        return new int[]{second % 60, second / 60};
    }

    public static int[] formatSecondToHourAr(int second) {
        int second2 = second / 60;
        return new int[]{second % 60, second2 % 60, second2 / 60};
    }

    public static String formatTemperature(Context context, float value, boolean need) {
        return formatTemperature(context, "%.1f", value, need);
    }

    public static String formatTemperature(Context context, String format, float value, boolean need) {
        int unit;
        if (need) {
            unit = DJIGenSettingDataManager.getInstance().getTemperatureUnit();
        } else {
            unit = 1;
        }
        if (unit == 0) {
            return context.getString(R.string.battery_temperature_h_unit, String.format(Locale.US, format, Float.valueOf(DJIUnitUtil.celsiusToFahrenheit(value))));
        } else if (2 == unit) {
            return context.getString(R.string.battery_temperature_k_unit, String.format(Locale.US, format, Float.valueOf(DJIUnitUtil.celsiusToKelvin(value))));
        } else {
            return context.getString(R.string.battery_temperature_unit, String.format(Locale.US, format, Float.valueOf(value)));
        }
    }

    public static float transformTemperature(float value) {
        int unit = DJIGenSettingDataManager.getInstance().getTemperatureUnit();
        if (unit == 0) {
            return DJIUnitUtil.celsiusToFahrenheit(value);
        }
        if (2 == unit) {
            return DJIUnitUtil.celsiusToKelvin(value);
        }
        return value;
    }

    public static float transformTmeperature2Celsius(float value) {
        int unit = DJIGenSettingDataManager.getInstance().getTemperatureUnit();
        if (unit == 0) {
            return DJIUnitUtil.fahrenheitToCelsius(value);
        }
        if (2 == unit) {
            return DJIUnitUtil.kelvinToCelsius(value);
        }
        return value;
    }

    public static String getTemperatureUnit(Context context) {
        int unit = DJIGenSettingDataManager.getInstance().getTemperatureUnit();
        if (unit == 0) {
            return context.getString(R.string.battery_temperature_h_unit, "");
        } else if (2 == unit) {
            return context.getString(R.string.battery_temperature_k_unit, "");
        } else {
            return context.getString(R.string.battery_temperature_unit, "");
        }
    }

    public static String getTemperatureStr(Context context) {
        int unit = DJIGenSettingDataManager.getInstance().getTemperatureUnit();
        if (unit == 0) {
            return context.getString(R.string.tau_temperature_fahrenheit);
        }
        if (2 == unit) {
            return context.getString(R.string.tau_temperature_kelvin);
        }
        return context.getString(R.string.tau_temperature_celsius);
    }

    public static boolean checkNetAvaiable(Context context) {
        NetworkInfo info = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (info == null || !info.isAvailable()) {
            return false;
        }
        if (!DJIProductManager.getInstance().getType().isFromWifi() || !ServiceManager.getInstance().isConnected()) {
            return true;
        }
        return false;
    }

    public static boolean checkAdbDebuging(ContentResolver resolver) {
        if (Build.VERSION.SDK_INT > 16) {
            if (Settings.Global.getInt(resolver, "adb_enabled", 0) > 0) {
                return true;
            }
            return false;
        } else if (Settings.Secure.getInt(resolver, "adb_enabled", 0) <= 0) {
            return false;
        } else {
            return true;
        }
    }

    public static String formatDecimal(long number) {
        return new DecimalFormat("#,###").format(number);
    }

    public static boolean isProductOrange(ProductType type) {
        return DJICommonUtils.isProductOrange(type);
    }

    public static boolean isSupportBgDownload(ProductType type) {
        if (type == null) {
            type = DJIProductManager.getInstance().getType();
        }
        DataCameraGetPushStateInfo.CameraType cType = DataCameraGetPushStateInfo.getInstance().getCameraType();
        if (type == ProductType.Tomato || DJICommonUtils.isPomatoSeries(type) || type == ProductType.KumquatS || type == ProductType.KumquatX || type == ProductType.Orange2 || type == ProductType.Potato || type == ProductType.Mammoth || type == ProductType.WM230 || DataCameraGetPushStateInfo.CameraType.DJICameraTypeGD600 == cType || DJICommonUtils.isM200Product(type) || type == ProductType.WM240) {
            return true;
        }
        return false;
    }

    public static boolean useNewLeftMenu(ProductType type) {
        if (type == null) {
            type = DJIProductManager.getInstance().getType();
        }
        if (ProductType.Tomato == type || DJICommonUtils.isPomatoSeries(type) || type == ProductType.Potato || DJICommonUtils.isKumquatSeries(null) || DJICommonUtils.isProductOrange2() || ProductType.Mammoth == type || ProductType.WM230 == type || ProductType.WM240 == type || DJICommonUtils.isM200Product(type)) {
            return true;
        }
        return false;
    }

    public static boolean isEnableBgDownload(ProductType type) {
        if (type == null) {
            ProductType type2 = DJIProductManager.getInstance().getType();
        }
        if (isSupportBgDownload(null)) {
            return DJIGenSettingDataManager.getInstance().getOpenBGDownload();
        }
        return false;
    }

    public static boolean isEnalbeBgHDDownload(ProductType type) {
        if (type == null) {
            type = DJIProductManager.getInstance().getType();
        }
        if (DJICommonUtils.isKumquatSeries(type) || DJICommonUtils.isWM240()) {
            return DJIGenSettingDataManager.getInstance().getOpenBGDownload();
        }
        return false;
    }

    public static boolean isProductInspire(ProductType type) {
        return DJICommonUtils.isProductInspire(type);
    }

    public static boolean isProductLongan(ProductType type) {
        return DJIProductSupportUtil.isLonganSeries(type);
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

    public static boolean isOldMC() {
        return DJICommonUtils.isOldMC();
    }

    public static boolean canUpgradeViaApp() {
        ProductType type = DJIProductManager.getInstance().getType();
        return DJIProductSupportUtil.isLonganSeries(type) || type == ProductType.P34K || type == ProductType.litchiC || type == ProductType.Tomato || DJICommonUtils.isPomatoSeries(type) || type == ProductType.Potato || type == ProductType.Mammoth || type == ProductType.Orange2;
    }

    public static boolean isNewUpgradeSystem() {
        return DJIUpStatusHelper.isNewUpgradeSystem();
    }

    public static boolean hasGuidance(ProductType type) {
        return DJICommonUtils.hasGuidance(type);
    }

    public static boolean isProductLitchi() {
        return isProductLitchi(DJIProductManager.getInstance().getType());
    }

    public static boolean isProductLitchi(ProductType type) {
        return DJICommonUtils.isProductLitchi(type);
    }

    public static boolean isWifiProduct(ProductType type) {
        if (type == null) {
            type = DJIProductManager.getInstance().getType();
        }
        return type.isFromWifi() || DJIUSBWifiSwitchManager.getInstance().isProductWifiConnected(type);
    }

    public static boolean useFullScreenFMView(ProductType pType) {
        return DJICommonUtils.isKumquatSeries(pType) || DJICommonUtils.isPomatoSeries(pType) || pType == ProductType.Potato || pType == ProductType.WM240;
    }

    public static boolean supportVisual(ProductType type) {
        return DJICommonUtils.supportVisual(type);
    }

    private static Shape getDrawableShape() {
        return new RoundRectShape(new float[]{5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f}, null, null);
    }

    /* JADX WARN: Type inference failed for: r9v5, types: [android.graphics.drawable.ClipDrawable], assign insn: 0x006a: CONSTRUCTOR  (r9v5 ? I:android.graphics.drawable.ClipDrawable) = 
      (r7v0 'shapeDrawable' android.graphics.drawable.ShapeDrawable A[D('shapeDrawable' android.graphics.drawable.ShapeDrawable)])
      (80 int)
      (2 int)
     call: android.graphics.drawable.ClipDrawable.<init>(android.graphics.drawable.Drawable, int, int):void type: CONSTRUCTOR */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static android.graphics.drawable.Drawable tileify(android.graphics.drawable.Drawable r12, boolean r13) {
        /*
            boolean r9 = r12 instanceof android.graphics.drawable.LayerDrawable
            if (r9 == 0) goto L_0x0040
            r1 = r12
            android.graphics.drawable.LayerDrawable r1 = (android.graphics.drawable.LayerDrawable) r1
            int r0 = r1.getNumberOfLayers()
            android.graphics.drawable.Drawable[] r6 = new android.graphics.drawable.Drawable[r0]
            r3 = 0
        L_0x000e:
            if (r3 >= r0) goto L_0x002e
            int r4 = r1.getId(r3)
            android.graphics.drawable.Drawable r10 = r1.getDrawable(r3)
            r9 = 16908301(0x102000d, float:2.3877265E-38)
            if (r4 == r9) goto L_0x0022
            r9 = 16908303(0x102000f, float:2.387727E-38)
            if (r4 != r9) goto L_0x002c
        L_0x0022:
            r9 = 1
        L_0x0023:
            android.graphics.drawable.Drawable r9 = tileify(r10, r9)
            r6[r3] = r9
            int r3 = r3 + 1
            goto L_0x000e
        L_0x002c:
            r9 = 0
            goto L_0x0023
        L_0x002e:
            android.graphics.drawable.LayerDrawable r5 = new android.graphics.drawable.LayerDrawable
            r5.<init>(r6)
            r3 = 0
        L_0x0034:
            if (r3 >= r0) goto L_0x006f
            int r9 = r1.getId(r3)
            r5.setId(r3, r9)
            int r3 = r3 + 1
            goto L_0x0034
        L_0x0040:
            boolean r9 = r12 instanceof android.graphics.drawable.BitmapDrawable
            if (r9 == 0) goto L_0x0070
            android.graphics.drawable.BitmapDrawable r12 = (android.graphics.drawable.BitmapDrawable) r12
            android.graphics.Bitmap r8 = r12.getBitmap()
            android.graphics.drawable.ShapeDrawable r7 = new android.graphics.drawable.ShapeDrawable
            android.graphics.drawable.shapes.Shape r9 = getDrawableShape()
            r7.<init>(r9)
            android.graphics.BitmapShader r2 = new android.graphics.BitmapShader
            android.graphics.Shader$TileMode r9 = android.graphics.Shader.TileMode.REPEAT
            android.graphics.Shader$TileMode r10 = android.graphics.Shader.TileMode.CLAMP
            r2.<init>(r8, r9, r10)
            android.graphics.Paint r9 = r7.getPaint()
            r9.setShader(r2)
            if (r13 == 0) goto L_0x006e
            android.graphics.drawable.ClipDrawable r9 = new android.graphics.drawable.ClipDrawable
            r10 = 80
            r11 = 2
            r9.<init>(r7, r10, r11)
            r7 = r9
        L_0x006e:
            r5 = r7
        L_0x006f:
            return r5
        L_0x0070:
            r5 = r12
            goto L_0x006f
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.pilot.fpv.util.DJICommonUtil.tileify(android.graphics.drawable.Drawable, boolean):android.graphics.drawable.Drawable");
    }

    public static int getTmpModeResId() {
        return mTmpModeResId;
    }

    public static void setTmpModeResId(int mTmpModeResId2) {
        mTmpModeResId = mTmpModeResId2;
    }

    public static boolean isProductM600Series(ProductType type) {
        return DJICommonUtils.isProductM600Series(type);
    }

    public static boolean isRonin() {
        return DataGimbalGetPushType.getInstance().getType() == DataGimbalGetPushType.DJIGimbalType.Ronin && isProductM600Series(null);
    }

    public static boolean supportRedundancySenor() {
        return DJICommonUtils.supportRedundancySenor();
    }

    public static boolean isA3Only() {
        ProductType type = DJIProductManager.getInstance().getType();
        return type == ProductType.A3 || type == ProductType.N3;
    }

    public static boolean isA3() {
        return DJICommonUtils.isA3();
    }

    public static boolean useNewBattery() {
        ProductType type = DJIProductManager.getInstance().getType();
        return DJICommonUtils.isProductM600Series(type) || DJICommonUtils.isKumquatSeries(type) || type == ProductType.Mammoth || DJICommonUtils.isWM240();
    }

    public static boolean isP4Series() {
        ProductType productType = DJIProductManager.getInstance().getType();
        return productType == ProductType.Tomato || DJICommonUtils.isPomatoSeries(productType) || DJICommonUtils.isKumquatSeries(productType) || productType == ProductType.Potato;
    }

    public static boolean isMammoth() {
        return DJIProductManager.getInstance().getType() == ProductType.Mammoth;
    }

    public static boolean isMammothWithOtg() {
        return isMammoth() && DJILinkDaemonService.getInstance().getLinkType() == DJILinkType.AOA;
    }

    public static boolean isNotWM230UpgradeFirstVerion() {
        return DataEyeGetPushException.getInstance().getWM230Version() != IVisionResDefine.SupportVersion.VERSION_VISION_WM230_TRACK;
    }

    public static boolean isWm240FirstPackageVerion() {
        long version = DataEyeGetPushException.getInstance().getVisionVersion();
        return isOpenWm240PackageVersionExcludeTimeLapse && version > IVisionResDefine.SupportVersion.VERSION_VISION_WM240_MIN_PACKGE_VERSION && version < IVisionResDefine.SupportVersion.VERSION_VISION_WM240_MAX_PACKGE_VERSION;
    }

    public static boolean isHyperLapseCanAddSecVersion() {
        long version = DataEyeGetPushException.getInstance().getVisionVersion();
        return version >= IVisionResDefine.SupportVersion.VERSION_VISION_WM240_HYPER_LAPSE_ADD_SEC || version < IVisionResDefine.SupportVersion.VERSION_VISION_DEBUG_BRANCH_MAX;
    }

    public static boolean isHyperLapseTaskLibraryVersion() {
        return isSupportShotProductInfoVersion();
    }

    public static boolean isSupportShotProductInfoVersion() {
        long version = DataEyeGetPushException.getInstance().getVisionVersion();
        return version >= IVisionResDefine.SupportVersion.VERSION_VISION_WM240_SUPPORT_PRODUCT_INFO || version < IVisionResDefine.SupportVersion.VERSION_VISION_DEBUG_BRANCH_MAX;
    }

    public static boolean canUpgradeAircraftByApp(ProductType type) {
        if (type == null) {
            type = DJIProductManager.getInstance().getType();
        }
        return type == ProductType.litchiC || type == ProductType.P34K || type == ProductType.Tomato || DJICommonUtils.isPomatoSeries(type) || type == ProductType.Potato || DJICommonUtils.isKumquatSeries(type);
    }

    public static int dip2px(Context context, float dipValue) {
        return (int) ((dipValue * context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        return (int) ((pxValue / context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static int getMCC(Context context) {
        if (context != null) {
            return context.getResources().getConfiguration().mcc;
        }
        return -1;
    }

    public static boolean getIsJapan(Context context) {
        String coCode = ((IAreaCodeService) DJIAppServiceManager.getInstance().getService(IAreaCodeService.COMPONENT_NAME)).getAreaCodeEvent().areaCode;
        if (!TextUtils.isEmpty(coCode)) {
            return coCode.equals(IAreaCode.STR_AREA_CODE_JAPAN);
        }
        return Locale.JAPAN.getCountry().equals(Locale.getDefault().getCountry());
    }

    public static boolean getIsChina(Context context) {
        String coCode = ((IAreaCodeService) DJIAppServiceManager.getInstance().getService(IAreaCodeService.COMPONENT_NAME)).getAreaCodeEvent().areaCode;
        if (TextUtils.isEmpty(coCode)) {
            String lan = Locale.getDefault().getCountry();
            if (Locale.CHINA.getCountry().equals(lan) || Locale.TAIWAN.getCountry().equals(lan)) {
                return true;
            }
            return false;
        } else if (coCode.equals(IAreaCode.STR_AREA_CODE_CHINA) || coCode.equals(IAreaCode.STR_AREA_CODE_HONGKONG) || coCode.equals("TW") || coCode.equals(IAreaCode.STR_AREA_CODE_MACAU)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean getIsNeedVerPhone(ProductType productType) {
        if (productType == null) {
            productType = DJIProductManager.getInstance().getType();
        }
        return productType == ProductType.KumquatS || productType == ProductType.KumquatX || productType == ProductType.Orange2 || DJICommonUtils.isM200Product(productType) || DJICommonUtils.isPomatoSeries(productType) || productType == ProductType.Potato || productType == ProductType.Tomato || productType == ProductType.PomatoSDR || productType == ProductType.Mammoth || productType == ProductType.WM230 || productType == ProductType.WM160 || productType == ProductType.WM240;
    }

    public static boolean getIsNeedCAACRealName(ProductType productType) {
        if (productType != null) {
            return false;
        }
        ProductType productType2 = DJIProductManager.getInstance().getType();
        return false;
    }

    public static String formatSpeed(String format, float value) {
        DJIGenSettingDataManager dm = DJIGenSettingDataManager.getInstance();
        int type = dm.getParameterUnit();
        String speedStr = String.format(Locale.US, format, Float.valueOf(dm.transformSpeed(value)));
        if (type == 0) {
            return speedStr + "mph";
        }
        if (2 == type) {
            return speedStr + "km/h";
        }
        return speedStr + "m/s";
    }

    public static String getUnitStr(float value, boolean isSpeed, boolean isValid) {
        return getUnitStr(value, isSpeed, isValid, true);
    }

    public static String getUnitStr(float value, boolean isSpeed, boolean isValid, boolean isFloat) {
        String str = "";
        int unitType = DJIGenSettingDataManager.getInstance().getParameterUnit();
        if (!isValid) {
            if (unitType == 0) {
                return "-- ft";
            }
            if (unitType == 1) {
                return "-- m";
            }
            if (unitType == 2) {
                return "-- m";
            }
        }
        if (isSpeed) {
            if (unitType == 0) {
                float valueFloat = value * 2.24f;
                str = isFloat ? to2Float(valueFloat) + "mph" : ((int) valueFloat) + "mph";
            } else if (unitType == 1) {
                str = isFloat ? to2Float(value) + "m/s" : ((int) value) + "m/s";
            } else if (unitType == 2) {
                float valueFloat2 = value * 3.6f;
                str = isFloat ? to2Float(valueFloat2) + "km/h" : ((int) valueFloat2) + "km/h";
            }
        } else if (unitType == 0) {
            float valueFloat3 = value * 3.2f;
            str = isFloat ? to2Float(valueFloat3) + "ft" : ((int) valueFloat3) + "ft";
        } else if (unitType == 1) {
            str = isFloat ? to2Float(value) + "m" : ((int) value) + "m";
        } else if (unitType == 2) {
            str = isFloat ? to2Float(value) + "m" : ((int) value) + "m";
        }
        return str;
    }

    public static float to2Float(float a) {
        return ((float) Math.round(a * 10.0f)) / 10.0f;
    }
}
