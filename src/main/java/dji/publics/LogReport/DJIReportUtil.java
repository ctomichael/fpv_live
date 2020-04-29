package dji.publics.LogReport;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Base64;
import com.dji.analytics.DJIA;
import com.dji.api.protocol.IAccountCenterHttpApi;
import com.dji.megatronking.stringfog.lib.annotation.DJIStringFog;
import dji.apppublic.reflect.AppPubInjectManager;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.model.P3.DataCommonGetVersion;
import dji.midware.data.model.P3.DataOsdGetPushCommon;
import dji.midware.data.model.P3.DataOsdGetPushHome;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.util.RepeatDataBase;
import dji.publics.LogReport.base.Fields;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

@EXClassNullAway
public class DJIReportUtil {
    public static boolean DEBUG_LOG_ENABLED = true;
    @DJIStringFog
    private static String aes = "iu3aBApVzP19xQNlPYWblIaSo2D9xbRc";
    /* access modifiers changed from: private */
    public static String batteryVer = "";
    private static Context context;
    private static String flightSession = "";
    private static byte[] iv = new byte[16];
    private static String mLastReportProductName = "DJI Device";
    private static ProductType mLastReportProductType = null;
    private static int mProductSubType = 0;
    private static String mProductSubTypeStr = "0";
    /* access modifiers changed from: private */
    public static String rcVer = "";
    private static String sn = "";

    public static void initDJIReport(Context context2, boolean isOpenDebug, boolean isEnableCellular, String flavor) {
        context = context2;
        if (isOpenDebug) {
            DJIA.enableDebug();
        }
        DJIA.init(context2, "437132", "NYSADRUHDSHAFSFC");
        DJIA.addExtraData("flavor", flavor);
        DJIA.setIsEnableCellular(isEnableCellular);
    }

    public static void setEnable(boolean enable) {
        try {
            DJIA.setIsEnableReport(enable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isCanReport() {
        return DJIA.getIsEnableReport();
    }

    public static String getUUID() {
        return DJIA.getUUID();
    }

    public static void onRemoteOK() {
        getRcVersion();
        getBatteryVersion();
    }

    public static void setSnOnConnect(String flycsn) {
        try {
            sn = encrypt(flycsn, aes);
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            calendar.setTime(new Date());
            flightSession = sn + "_" + String.valueOf(calendar.get(1)) + formatDay(calendar.get(2) + 1) + formatDay(calendar.get(5)) + "_" + String.valueOf(DataOsdGetPushHome.getInstance().getFlycLogIndex());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint({"DefaultLocale"})
    private static String formatDay(int day) {
        return String.format("%02d", Integer.valueOf(day));
    }

    private static void getRcVersion() {
        final DataCommonGetVersion getter = new DataCommonGetVersion();
        getter.setDeviceType(DeviceType.RC);
        new RepeatDataBase(getter, new DJIDataCallBack() {
            /* class dji.publics.LogReport.DJIReportUtil.AnonymousClass1 */

            public void onSuccess(Object model) {
                String unused = DJIReportUtil.rcVer = getter.getFirmVer(".");
            }

            public void onFailure(Ccode ccode) {
            }
        }).start();
    }

    private static void getBatteryVersion() {
        final DataCommonGetVersion getter = new DataCommonGetVersion();
        getter.setDeviceType(DeviceType.BATTERY);
        new RepeatDataBase(getter, new DJIDataCallBack() {
            /* class dji.publics.LogReport.DJIReportUtil.AnonymousClass2 */

            public void onSuccess(Object model) {
                String unused = DJIReportUtil.batteryVer = getter.getFirmVer(".");
            }

            public void onFailure(Ccode ccode) {
            }
        }).start();
    }

    public static void logEvent(String eventId) {
        DJIA.logEvent(eventId);
    }

    public static void logEventWithBaseInfo(String eventId) {
        DJIA.logEvent(eventId, getNormalDeviceData());
    }

    public static void logEvent(String eventId, HashMap<String, String> eventValue) {
        DJIA.logEvent(eventId, eventValue);
    }

    public static void logEvent(String eventId, HashMap<String, String> eventValue, boolean isEmergency) {
        DJIA.logEvent(eventId, eventValue, isEmergency);
    }

    public static HashMap<String, String> getNormalDeviceData() {
        HashMap<String, String> data = new HashMap<>();
        data.put(Fields.ReportCommon.STR_DEVICE_TYPE, getReportProductName(null));
        data.put(Fields.ReportCommon.STR_DEVICE_VER, getReportDeviceVersion());
        data.put(Fields.ReportCommon.STR_DEVICE_TYPE_SPEC, getReportDevcieTypeSpec());
        data.put(Fields.ReportCommon.battery_ver, batteryVer);
        if (DataOsdGetPushCommon.getInstance().isMotorUp()) {
            data.put(Fields.ReportCommon.flight_sessionid, flightSession);
        } else {
            data.put(Fields.ReportCommon.flight_sessionid, "");
        }
        data.put("rc_ver", getReportRcVersion());
        return data;
    }

    public static HashMap<String, String> getNormalDeviceDataWithMainSn() {
        HashMap<String, String> data = getNormalDeviceData();
        data.put(Fields.ReportCommon.STR_SN_SIGN, getReportFCSn());
        return data;
    }

    public static HashMap<String, String> getNormalDeviceDataWithRc() {
        return getNormalDeviceDataWithMainSn();
    }

    public static String getReportProductName(ProductType type) {
        if (type == null) {
            type = DJIProductManager.getInstance().getType();
        }
        if (type != mLastReportProductType) {
            mLastReportProductType = type;
            mLastReportProductName = type.value() + "";
        }
        return mLastReportProductName;
    }

    public static String getReportDevcieTypeSpec() {
        mProductSubType = DJIProductManager.getInstance().getSubType();
        mProductSubTypeStr = String.valueOf(mProductSubType);
        return mProductSubTypeStr;
    }

    public static String getReportAppVersion() {
        return AppPubInjectManager.getAppPubToP3Injectable().getAppVersion();
    }

    public static String getReportDeviceVersion() {
        return AppPubInjectManager.getAppPubToP3Injectable().getDeviceVersion();
    }

    public static String getReportFCSn() {
        return sn;
    }

    public static String getSnEncrypt(String sn2) {
        try {
            return encrypt(sn2, aes);
        } catch (Exception e) {
            e.printStackTrace();
            return sn2;
        }
    }

    public static String getReportRcVersion() {
        return rcVer;
    }

    public static void test() {
        DJILog.logWriteE("testreport", "getReportProductName=%s getReportAppVersion=%s getReportDeviceVersion=%s getReportFCSn=%s", "testreport", getReportProductName(null), getReportAppVersion(), getReportDeviceVersion(), getReportFCSn());
    }

    public static String decrypt(String encrypted, String key) throws Exception {
        byte[] bArr = new byte[32];
        SecretKeySpec skey = new SecretKeySpec(key.getBytes("utf-8"), "AES");
        Cipher dcipher = Cipher.getInstance(IAccountCenterHttpApi.ENCRYPT_TRANSFORMATION);
        dcipher.init(2, skey, new IvParameterSpec(iv));
        return new String(dcipher.doFinal(Base64.decode(encrypted, 0)));
    }

    public static String encrypt(String content, String key) throws Exception {
        byte[] input = content.getBytes("utf-8");
        byte[] bArr = new byte[32];
        SecretKeySpec skc = new SecretKeySpec(key.getBytes("utf-8"), "AES");
        Cipher cipher = Cipher.getInstance(IAccountCenterHttpApi.ENCRYPT_TRANSFORMATION);
        cipher.init(1, skc, new IvParameterSpec(iv));
        return Base64.encodeToString(cipher.doFinal(input), 0).replace("\n", "");
    }

    public static String getFlightSessionId() {
        return flightSession;
    }
}
