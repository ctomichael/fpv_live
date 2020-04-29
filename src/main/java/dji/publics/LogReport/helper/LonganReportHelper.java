package dji.publics.LogReport.helper;

import android.util.Log;
import com.dji.frame.util.V_StringUtils;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.model.P3.DataGimbalActiveStatus;
import dji.midware.data.model.P3.DataOsdActiveStatus;
import dji.midware.data.model.common.DataAbstractGetPushActiveStatus;
import dji.midware.interfaces.DJIDataCallBack;
import dji.publics.LogReport.DJIReportUtil;
import dji.publics.LogReport.base.Event;
import dji.publics.LogReport.base.Fields;
import java.util.HashMap;

@EXClassNullAway
public class LonganReportHelper {
    public static final String OSMO_MOBILE = "OSMO MOBILE";
    private static final String TAG = "LonganReportHelper";
    private static LonganReportHelper mInstance = new LonganReportHelper();
    private boolean DEBUG = false;
    public HashMap<String, String> mBleMap = new HashMap<>();
    /* access modifiers changed from: private */
    public String mDeviceSn;
    private boolean mNeedSn = true;
    public HashMap<String, String> mSettingsMap = new HashMap<>();
    public HashMap<String, String> mTakePhotoMap = new HashMap<>();
    public HashMap<String, String> mTakeVideoMap = new HashMap<>();
    public HashMap<String, String> mTrackingMap = new HashMap<>();
    public HashMap<String, String> mUpdateMap = new HashMap<>();

    public static LonganReportHelper getInstance() {
        return mInstance;
    }

    public void report(String event) {
        DJIReportUtil.logEvent(event);
    }

    public void report(String event, HashMap<String, String> m) {
        DJIReportUtil.logEvent(event, m);
    }

    private void reportData(final String event, final HashMap<String, String> map, String info, String value, boolean isFinal) {
        map.put(info, value);
        if (isFinal) {
            if (!this.mNeedSn) {
                DJIReportUtil.logEvent(event, map);
            } else if (V_StringUtils.isEmpty(this.mDeviceSn)) {
                DataAbstractGetPushActiveStatus snGetter = getSnGetter();
                if (snGetter.isGetted()) {
                    String sn = snGetter.getSN();
                    this.mDeviceSn = sn;
                    logWithSn(sn, event, map);
                } else {
                    snGetter.setType(DataAbstractGetPushActiveStatus.TYPE.GET).start(new DJIDataCallBack() {
                        /* class dji.publics.LogReport.helper.LonganReportHelper.AnonymousClass1 */

                        public void onSuccess(Object model) {
                            String sn = DataGimbalActiveStatus.getInstance().getSN();
                            String unused = LonganReportHelper.this.mDeviceSn = sn;
                            LonganReportHelper.this.logWithSn(sn, event, map);
                        }

                        public void onFailure(Ccode ccode) {
                            LonganReportHelper.this.logWithSn("", event, map);
                        }
                    });
                }
            } else {
                logWithSn(this.mDeviceSn, event, map);
            }
            if (this.DEBUG) {
                Log.d(TAG, "DJIA reportData: " + map.toString());
            }
        }
    }

    private DataAbstractGetPushActiveStatus getSnGetter() {
        DataGimbalActiveStatus instance = DataGimbalActiveStatus.getInstance();
        if (DJIProductManager.getInstance().getType() == ProductType.LonganMobile) {
            return DataGimbalActiveStatus.getInstance();
        }
        return DataOsdActiveStatus.getInstance();
    }

    /* access modifiers changed from: private */
    public void logWithSn(String sn, String event, HashMap<String, String> map) {
        map.put(Fields.ReportCommon.STR_SN_SIGN, DJIReportUtil.getSnEncrypt(sn));
        DJIReportUtil.logEvent(event, map);
    }

    public LonganReportHelper rpBleConnect(String info, String value, boolean isFinal) {
        reportData(Event.Dgo_bleconnect, this.mBleMap, info, value, isFinal);
        return this;
    }

    public LonganReportHelper rpTakePhoto(String info, String value, boolean isFinal) {
        reportData(Event.Dgo_takephoto, this.mTakePhotoMap, info, value, isFinal);
        return this;
    }

    public LonganReportHelper rpTakeVideo(String info, String value, boolean isFinal) {
        reportData(Event.Dgo_takevideo, this.mTakeVideoMap, info, value, isFinal);
        return this;
    }

    public LonganReportHelper rpTracking(String info, String value, boolean isFinal) {
        reportData(Event.Dgo_tracking, this.mTrackingMap, info, value, isFinal);
        return this;
    }

    public LonganReportHelper rpSettings(String info, String value, boolean isFinal) {
        reportData(Event.Dgo_osmo_set, this.mSettingsMap, info, value, isFinal);
        return this;
    }

    public LonganReportHelper rpUpdate(String step, String deviceVer) {
        this.mUpdateMap.put(Fields.ReportCommon.STR_CREATE_TIME, System.currentTimeMillis() + "");
        this.mUpdateMap.put(Fields.Dgo_update.STEP, step);
        this.mUpdateMap.put(Fields.ReportCommon.STR_DEVICE_VER, deviceVer);
        reportData(Event.Dgo_update, this.mUpdateMap, Fields.ReportCommon.STR_DEVICE_TYPE, DJIReportUtil.getReportProductName(null), true);
        return this;
    }
}
