package dji.publics.LogReport;

import android.content.Context;
import dji.pilot.publics.objects.DjiSharedPreferencesManager;
import dji.pilot.publics.util.DJIPublicUtils;
import dji.publics.LogReport.base.Event;
import java.util.HashMap;

public final class DurationDataReport {
    private static final int FLAG_APPLAUNCH = 2;
    private static final int FLAG_MAINPAGE_VISIBLE = 4;
    private static final int MAX_APPLAUNCH_TIME = 3;
    private static final String PREFIX_KEY_APPLAUNCH = "key_applaunch_report_";
    private static final String PREFIX_KEY_MAINPAGE_VISIBLE = "key_mainpage_report_";
    private static Context sAppCxt = null;
    private static int sAppLaunchReportTimes = 0;
    private static long sAppStartTime = 0;
    private static String sAppVersion = null;
    private static int sMainPageReportTimes = 0;
    private static int sReportedFlags = 0;

    public static void setParam(Context context, String appVersion) {
        sAppCxt = context;
        sAppVersion = appVersion;
        sAppLaunchReportTimes = DjiSharedPreferencesManager.getInt(context, PREFIX_KEY_APPLAUNCH + appVersion, sAppLaunchReportTimes);
        sMainPageReportTimes = DjiSharedPreferencesManager.getInt(context, PREFIX_KEY_MAINPAGE_VISIBLE + appVersion, sMainPageReportTimes);
    }

    public static void appStartTime() {
        sAppStartTime = System.currentTimeMillis();
    }

    public static void reportAppDuration() {
        if (sAppLaunchReportTimes < 3 && !hasReported(2) && !checkParamInvalid()) {
            long time = System.currentTimeMillis();
            HashMap<String, String> data = new HashMap<>(2);
            data.put("action", "1");
            data.put("time", String.valueOf(time - sAppStartTime));
            DJIReportUtil.logEvent(Event.Dgo_duration, data);
            sReportedFlags |= 2;
            sAppLaunchReportTimes++;
            DjiSharedPreferencesManager.putInt(sAppCxt, PREFIX_KEY_APPLAUNCH + sAppVersion, sAppLaunchReportTimes);
        }
    }

    public static void reportMainPageDuration() {
        if (sMainPageReportTimes < 3 && !hasReported(4) && !checkParamInvalid()) {
            long time = System.currentTimeMillis();
            HashMap<String, String> data = new HashMap<>(2);
            data.put("action", "2");
            data.put("time", String.valueOf(time - sAppStartTime));
            DJIReportUtil.logEvent(Event.Dgo_duration, data);
            sReportedFlags |= 4;
            sMainPageReportTimes++;
            DjiSharedPreferencesManager.putInt(sAppCxt, PREFIX_KEY_MAINPAGE_VISIBLE + sAppVersion, sMainPageReportTimes);
        }
    }

    private static boolean hasReported(int flag) {
        return (sReportedFlags & flag) != 0;
    }

    private static boolean checkParamInvalid() {
        return sAppCxt == null || DJIPublicUtils.isEmpty(sAppVersion) || 0 == sAppStartTime;
    }
}
