package dji.pilot.fpv.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v4.os.EnvironmentCompat;
import dji.component.accountcenter.IAccountCenterService;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILogHelper;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.pilot.fpv.util.DJIFlurryReport;
import dji.pilot.fpv.util.DJINewEditorStatisticConstants;
import dji.pilot.publics.util.DJICommonUtils;
import dji.publics.LogReport.DJIReportUtil;
import dji.sdksharedlib.keycatalog.ProductKeys;
import dji.service.DJIAppServiceManager;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

@EXClassNullAway
@SuppressLint({"NewApi"})
@Deprecated
public class DJIFlurryUtil implements DJIFlurryReport, DJIFlurryReport.FPV {
    public static final String FLURRY_SDK_KEY = "X529Q7SM6P224YP253M4";
    protected static boolean isInit = false;
    private static boolean mIsOpen = true;

    public static void initFlurry(Context context) {
    }

    public static void onStartSession(Context context) {
    }

    public static void onEndSession(Context context) {
    }

    private static boolean canReport() {
        return DJIReportUtil.isCanReport();
    }

    public static void logEvent(String eventId) {
        if (!isCloseReport()) {
            logEvent(eventId, true);
        }
    }

    public static void logWithoutType(String eventId) {
        if (!isCloseReport()) {
            DJIAliyunStatisticAgent.getInstance().logEvent(eventId);
        }
    }

    public static void logByType(String eventId) {
        if (!isCloseReport()) {
            Map<String, String> dataMap = new HashMap<>();
            dataMap.put(DJIFlurryReportPublic.V2_PRODUCT_TYPE_KEY, DJIProductManager.getInstance().getType().toString());
            DJIAliyunStatisticAgent.getInstance().logEvent(eventId, dataMap);
        }
    }

    public static void logByTypeNMap(String eventId, Map<String, String> dataMap) {
        if (!isCloseReport()) {
            dataMap.put(DJIFlurryReportPublic.V2_PRODUCT_TYPE_KEY, DJIProductManager.getInstance().getType().toString());
            DJIAliyunStatisticAgent.getInstance().logEvent(eventId, dataMap);
        }
    }

    public static void logEvent(String eventId, boolean needType) {
        String type;
        if (!isCloseReport()) {
            if (needType) {
                switch (DJIProductManager.getInstance().getType()) {
                    case litchiX:
                        type = "LitchiX";
                        break;
                    case litchiS:
                        type = "LitchiS";
                        break;
                    case litchiC:
                        type = "LitchiC";
                        break;
                    case P34K:
                        type = "P34K";
                        break;
                    case Orange:
                        type = "Banana";
                        break;
                    case N1:
                        type = "N1";
                        break;
                    case BigBanana:
                        type = "BigBanana";
                        break;
                    case Olives:
                        type = "Olives";
                        break;
                    case OrangeRAW:
                        type = "OrangeRAW";
                        break;
                    case Tomato:
                        type = "Phantom4";
                        break;
                    case OrangeCV600:
                        type = "Inspire CV600";
                        break;
                    case Pomato:
                        type = "Phantom4 Professional";
                        break;
                    default:
                        type = "None";
                        break;
                }
                Map<String, String> hashMap = new HashMap<>();
                hashMap.put(ProductKeys.COMPONENT_KEY, type);
                DJIAliyunStatisticAgent.getInstance().logEvent(eventId, hashMap);
                return;
            }
            DJIAliyunStatisticAgent.getInstance().logEvent(eventId);
        }
    }

    public static void logEventWithMap(String eventId, HashMap<String, String> datas) {
        if (!isCloseReport()) {
            DJIAliyunStatisticAgent.getInstance().logEvent(eventId, datas);
        }
    }

    public static void logEventWithTime(String eventId, boolean start) {
        if (!isCloseReport()) {
            if (start) {
                DJIAliyunStatisticAgent.getInstance().startEvent(eventId);
            } else {
                DJIAliyunStatisticAgent.getInstance().endEvent(eventId);
            }
        }
    }

    public static void FlightTimeRecord(long time) {
        String type;
        if (!isCloseReport()) {
            logFlightWithComboKey(DJIFlurryReport.FPV.V2_FLIGHT_DURATION, DJIFlurryReport.FPV.V2_FLIGHT_TIME_KEY, "" + time);
            switch (DJIProductManager.getInstance().getType()) {
                case litchiX:
                    type = "LitchiX";
                    break;
                case litchiS:
                    type = "LitchiS";
                    break;
                case litchiC:
                    type = "LitchiC";
                    break;
                case P34K:
                    type = "P34K";
                    break;
                case Orange:
                    type = "Banana";
                    break;
                case N1:
                    type = "N1";
                    break;
                case BigBanana:
                    type = "BigBanana";
                    break;
                case Olives:
                    type = "Zenmuse XT";
                    break;
                case OrangeRAW:
                    type = "OrangeRAW";
                    break;
                case Tomato:
                    type = "Phantom4";
                    break;
                case OrangeCV600:
                    type = "Inspire CV600";
                    break;
                case Pomato:
                    type = "Phantom4 Professional";
                    break;
                default:
                    type = "None";
                    break;
            }
            String email = "";
            if (DJICommonUtils.isEmpty(email)) {
                email = EnvironmentCompat.MEDIA_UNKNOWN;
            }
            long time2 = time / 1000;
            String str = email + ":" + type + ":" + time2 + "s";
            Map<String, String> hashMap = new HashMap<>();
            hashMap.put(DJIFlurryReport.FPV.V2_DURATION_COMBOKEY, str);
            hashMap.put(type, time2 + "");
            hashMap.put("FlightTimeStatistics", time2 + "");
            DJILogHelper.getInstance().LOGD("", "Email&ProductType&FlightTime " + str, false, true);
            DJIAliyunStatisticAgent.getInstance().logEvent("FlightTimeRecord", hashMap);
        }
    }

    static boolean isCloseReport() {
        return !mIsOpen;
    }

    private static IAccountCenterService getAccountService() {
        return (IAccountCenterService) DJIAppServiceManager.getInstance().getService(IAccountCenterService.NAME, IAccountCenterService.class);
    }

    public static void logFlightWithComboKey(String key, String subkey, String subval) {
        if (!isCloseReport()) {
            String type = DJIProductManager.getInstance().getType().toString();
            String str = getAccountService().getEmail() + ":" + type;
            if (!subval.equals("")) {
                str = str + ":" + subval;
            }
            HashMap<String, String> dataMap = new HashMap<>();
            String comboKey = DJIFlurryReport.FPV.V2_FLIGHT_COMPLETE_KEY;
            if (!subkey.equals("")) {
                comboKey = comboKey + "&" + subkey;
                dataMap.put(subkey, "" + subval);
            } else {
                dataMap.put(DJIFlurryReportPublic.V2_PRODUCT_TYPE_KEY, type);
            }
            dataMap.put(comboKey, str);
            DJIAliyunStatisticAgent.getInstance().logEvent(key, dataMap);
        }
    }

    public static void flightTimeRecordV2(long time) {
        if (!isCloseReport()) {
            HashMap<String, String> dataMap = new HashMap<>();
            dataMap.put(DJIFlurryReport.FPV.V2_FLIGHT_TIME_KEY, "" + time);
            dataMap.put(DJIFlurryReport.FPV.V2_DURATION_COMBOKEY, getAccountService().getEmail() + ":" + DJIProductManager.getInstance().getType().toString() + ":" + time + "s");
            DJIAliyunStatisticAgent.getInstance().logEvent(DJIFlurryReport.FPV.V2_FLIGHT_DURATION, dataMap);
        }
    }

    public static void logEventWithJsonString(String jsonString) {
        if (!isCloseReport()) {
            try {
                JSONObject jsonObj = new JSONObject(jsonString);
                Iterator<?> it2 = jsonObj.keys();
                while (it2.hasNext()) {
                    parseEvent(jsonObj, it2.next().toString());
                }
            } catch (JSONException e) {
                DJILogHelper.getInstance().LOGE("DJIFlurry", "v2 flurry error");
                e.printStackTrace();
            }
        }
    }

    private static void parseEvent(JSONObject jsonObject, String eventString) {
        JSONObject parseObject;
        if (!isCloseReport() && (parseObject = jsonObject.optJSONObject(eventString)) != null) {
            Iterator<?> it2 = parseObject.keys();
            String val = "";
            Map<String, String> hashMap = new HashMap<>();
            while (it2.hasNext()) {
                String key = it2.next().toString();
                if (key != null && !"".equals(key)) {
                    val = parseObject.optString(key);
                }
                hashMap.put(key, val);
                DJILogHelper.getInstance().LOGI("DJIFlurry", "key: " + key + "   value: " + val);
            }
            DJIAliyunStatisticAgent.getInstance().logEvent(eventString, hashMap);
        }
    }

    public static Resources getEnRes(Context context) {
        Configuration config = new Configuration(context.getResources().getConfiguration());
        config.setLocale(Locale.US);
        return context.createConfigurationContext(config).getResources();
    }

    public static void logEditor(String eventAction) {
        if (!isCloseReport()) {
            HashMap<String, String> dataMap = new HashMap<>();
            dataMap.put("action", eventAction);
            DJIReportUtil.logEvent(DJINewEditorStatisticConstants.EventType.Dgo_editor, dataMap);
        }
    }

    public static void logEditorWithMap(String eventId, HashMap<String, String> dataMap) {
        if (!isCloseReport()) {
            dataMap.put("action", eventId);
            DJIReportUtil.logEvent(DJINewEditorStatisticConstants.EventType.Dgo_editor, dataMap);
        }
    }

    public static void logInternet(String eventAction) {
        if (!isCloseReport()) {
            HashMap<String, String> dataMap = new HashMap<>();
            dataMap.put("action", eventAction);
            DJIReportUtil.logEvent(DJINewEditorStatisticConstants.EventType.Dgo_internet, dataMap);
        }
    }

    public static void logInternetWithMap(String eventId, HashMap<String, String> dataMap) {
        if (!isCloseReport()) {
            dataMap.put("action", eventId);
            DJIReportUtil.logEvent(DJINewEditorStatisticConstants.EventType.Dgo_internet, dataMap);
        }
    }

    public static void logInternetWithMapByType(String eventId) {
        HashMap<String, String> dataMap = new HashMap<>();
        dataMap.put(DJIFlurryReportPublic.V2_PRODUCT_TYPE_KEY, DJIProductManager.getInstance().getType().toString());
        logInternetWithMap(eventId, dataMap);
    }
}
