package dji.dbox.upgrade.p4.utils;

import android.content.Context;
import com.dji.frame.util.V_JsonUtil;
import dji.dbox.upgrade.p4.constants.DJIUpConstants;
import dji.dbox.upgrade.p4.constants.DJIUpDeviceType;
import dji.dbox.upgrade.p4.model.DJIUpListElement;
import dji.dbox.upgrade.p4.model.DJIUpUrlModel;
import dji.dbox.upgrade.p4.server.DJIUpGetServerCfgManager;
import dji.dbox.upgrade.p4.server.DJIUpServerManager;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.util.DjiSharedPreferencesManager;
import dji.midware.util.WifiStateUtil;
import java.util.List;

@EXClassNullAway
public class DJIUpStatusOfflineHelper {
    private static final String PRODUCT_KEY_1 = "dji_up_device1_type_value";
    private static final String PRODUCT_KEY_2 = "dji_up_device2_type_value";
    private static final String PRODUCT_KEY_LAST_UP = "dji_up_device_last_up_type_value";
    private static final String TAG = "DJIUpStatusOfflineHelper";
    private static final String URL_MODEL_KEY = "dji_up_urlmodel_value";
    private static final String URL_SSID_KEY = "dji_up_ssid_value";
    private static DJIUpDeviceType device1;
    private static DJIUpDeviceType device2;
    private static Context mCtx;
    private static boolean offlineProductInLocal;
    private static boolean offlineServerInfo;

    public static void init(Context context) {
        mCtx = context.getApplicationContext();
        refreshDeviceInfo();
    }

    public static boolean isOfflineDevice() {
        return device1 != null && device1.isOffline();
    }

    public static boolean isNeedCompareOffline() {
        return DJIUpDeviceType.isSupportCompareOffline(device1);
    }

    public static boolean isOfflineEnableMode(Context context) {
        if (isNeedCompareOffline()) {
            return isOfflineServerInfo(context, device1.toString());
        }
        return false;
    }

    public static void setDeviceLastUp(DJIUpDeviceType upDeviceLast) {
        DJIUpConstants.LOGE_WIFI(TAG, "setUpDeviceLast upDeviceLast=" + upDeviceLast);
        DjiSharedPreferencesManager.putString(mCtx, PRODUCT_KEY_LAST_UP, upDeviceLast == null ? DJIUpDeviceType.unknow.toString() : upDeviceLast.toString());
    }

    public static DJIUpDeviceType getDeviceLastUp() {
        String value = DjiSharedPreferencesManager.getString(mCtx, PRODUCT_KEY_LAST_UP, DJIUpDeviceType.unknow.toString());
        DJIUpConstants.LOGE_WIFI(TAG, "getUpDeviceLast =" + value);
        return DJIUpDeviceType.find(value);
    }

    public static void setDevices(DJIUpDeviceType deviceType1, DJIUpDeviceType deviceType2) {
        DJIUpConstants.LOGE_WIFI(TAG, "isOfflineServerInfo deviceType1=" + deviceType1 + " deviceType2=" + deviceType2);
        DjiSharedPreferencesManager.putString(mCtx, PRODUCT_KEY_1, deviceType1 == null ? DJIUpDeviceType.unknow.toString() : deviceType1.toString());
        DjiSharedPreferencesManager.putString(mCtx, PRODUCT_KEY_2, deviceType2 == null ? DJIUpDeviceType.unknow.toString() : deviceType2.toString());
    }

    private static DJIUpDeviceType getDevice1(Context context) {
        String value = DjiSharedPreferencesManager.getString(context, PRODUCT_KEY_1, DJIUpDeviceType.unknow.toString());
        DJIUpConstants.LOGE_WIFI(TAG, "isOfflineServerInfo getDevice1=" + value);
        return DJIUpDeviceType.find(value);
    }

    private static DJIUpDeviceType getDevice2(Context context) {
        String value = DjiSharedPreferencesManager.getString(context, PRODUCT_KEY_2, DJIUpDeviceType.unknow.toString());
        DJIUpConstants.LOGE_WIFI(TAG, "isOfflineServerInfo getDevice2=" + value);
        return DJIUpDeviceType.find(value);
    }

    public static boolean isOfflineProductInLocal() {
        return device1 != null && device1.isOffline();
    }

    private static DJIUpDeviceType getOfflineProductInLocal(Context context) {
        return device1;
    }

    private static boolean isOfflineServerInfo(Context context, String product_id) {
        List<DJIUpListElement> list = DJIUpGetServerCfgManager.parseLocalList(context, DJIUpServerManager.getcfgTarget(context, product_id), product_id);
        DJIUpConstants.LOGE_WIFI(TAG, "isOfflineServerInfo list=" + list);
        return list != null && list.size() > 0;
    }

    public static DJIUpDeviceType getDevice1() {
        return device1;
    }

    public static DJIUpDeviceType getDevice2() {
        return device2;
    }

    public static boolean isLastDevice(String productId) {
        DJIUpDeviceType device = DJIUpDeviceType.find(productId);
        return device1 == device || device2 == device;
    }

    public static void saveUrlModel(DJIUpUrlModel model) {
        DjiSharedPreferencesManager.putString(mCtx, URL_MODEL_KEY, V_JsonUtil.toJson(model));
    }

    public static DJIUpUrlModel getUrlModel() {
        String s = DjiSharedPreferencesManager.getString(mCtx, URL_MODEL_KEY, "");
        if (!s.equals("")) {
            return (DJIUpUrlModel) V_JsonUtil.toBean(s, DJIUpUrlModel.class);
        }
        return new DJIUpUrlModel();
    }

    public static String getLastDeviceSsid() {
        String ssid = DjiSharedPreferencesManager.getString(mCtx, URL_SSID_KEY, "");
        DJIUpConstants.LOGE_WIFI(TAG, "getLastDeviceSsid ssid=" + ssid);
        return ssid;
    }

    public static void setLastDeviceSsid() {
        String ssid = WifiStateUtil.getWifiSSID(mCtx);
        DJIUpConstants.LOGE_WIFI(TAG, "setLastDeviceSsid ssid=" + ssid);
        if (!ssid.equals("")) {
            DjiSharedPreferencesManager.putString(mCtx, URL_SSID_KEY, ssid);
        }
    }

    public static void refreshDeviceInfo() {
        device1 = getDevice1(mCtx);
        device2 = getDevice2(mCtx);
    }
}
