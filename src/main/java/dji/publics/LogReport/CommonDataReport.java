package dji.publics.LogReport;

import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.publics.LogReport.base.Event;
import java.util.HashMap;

@EXClassNullAway
public class CommonDataReport {
    public static void reportEntrance(String action) {
        HashMap<String, String> data = new HashMap<>();
        data.put("action", action);
        DJIReportUtil.logEvent(Event.Dgo_homeclick, data);
    }

    public static void reportAction(String event, String action) {
        HashMap<String, String> data = new HashMap<>();
        data.put("action", action);
        DJIReportUtil.logEvent(event, data);
    }

    public static void log(String log, Object... args) {
        DJILog.e("reportTest", log, args);
    }
}
