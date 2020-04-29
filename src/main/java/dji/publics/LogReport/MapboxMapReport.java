package dji.publics.LogReport;

import dji.common.error.DJIError;
import dji.fieldAnnotation.EXClassNullAway;
import dji.publics.LogReport.base.Event;
import dji.publics.LogReport.base.Fields;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.keycatalog.DJISDKCacheKeys;
import dji.sdksharedlib.listener.DJIGetCallback;
import dji.sdksharedlib.store.DJISDKCacheParamValue;
import java.util.HashMap;

@EXClassNullAway
public class MapboxMapReport {
    private static final String CLOSE_MAPBOX_TRIAL_FROM_SETTINGS = "3";
    private static MapboxMapReport INSTANCE = null;
    private static final String LAUNCH_MAPBOX_MAP = "2";
    private static final String RECEIVE_MAPBOX_GEOCODING_RESPONSE_FROM_SHARE = "4";
    private static final String SHOW_NOTIFICATION_DIALOG = "1";

    private MapboxMapReport() {
    }

    public static MapboxMapReport getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MapboxMapReport();
        }
        return INSTANCE;
    }

    private void checkSnAndReport(final String action) {
        CacheHelper.getFlightController(DJISDKCacheKeys.SERIAL_NUMBER, new DJIGetCallback() {
            /* class dji.publics.LogReport.MapboxMapReport.AnonymousClass1 */

            public void onSuccess(DJISDKCacheParamValue value) {
                if (value != null && value.getData() != null) {
                    MapboxMapReport.this.report((String) value.getData(), action);
                }
            }

            public void onFails(DJIError error) {
                MapboxMapReport.this.report("sn_null_connect", action);
            }
        });
    }

    /* access modifiers changed from: private */
    public void report(String sn, String action) {
        DJIReportUtil.logEvent(Event.MapboxMapSNReport, getReportData(sn, action), true);
    }

    private HashMap<String, String> getReportData(String sn, String action) {
        HashMap<String, String> hashMap = DJIReportUtil.getNormalDeviceData();
        hashMap.put(Fields.ReportCommon.STR_SN_SIGN, DJIReportUtil.getSnEncrypt(sn));
        hashMap.put("action", action);
        return hashMap;
    }

    public void showNotificationDialog() {
        checkSnAndReport("1");
    }

    public void launchMapboxMap() {
        checkSnAndReport("2");
    }

    public void closeMapboxTrialFromSettings() {
        checkSnAndReport("3");
    }

    public void receiveMapboxGeocodingResponse() {
        checkSnAndReport("4");
    }
}
