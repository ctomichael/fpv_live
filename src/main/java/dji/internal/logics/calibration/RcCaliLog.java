package dji.internal.logics.calibration;

import dji.component.accountcenter.IMemberProtocol;
import dji.log.DJILog;

public class RcCaliLog {
    private static final String LOG_PATH = "RcCaliLog";
    private static final String TAG = "RcCaliLog";

    static void i(String tag, String msg) {
        DJILog.i("RcCaliLog", IMemberProtocol.STRING_SEPERATOR_LEFT + tag + "] " + msg, new Object[0]);
    }

    static void i(String msg) {
        DJILog.i("RcCaliLog", msg, new Object[0]);
    }

    static void e(String tag, String msg) {
        DJILog.i("RcCaliLog", IMemberProtocol.STRING_SEPERATOR_LEFT + tag + "] " + msg, new Object[0]);
    }

    static void e(String msg) {
        DJILog.i("RcCaliLog", msg, new Object[0]);
    }

    static void logWriteI(String tag, String msg) {
        DJILog.logWriteI("RcCaliLog", IMemberProtocol.STRING_SEPERATOR_LEFT + tag + "] " + msg, "RcCaliLog", new Object[0]);
    }

    static void logWriteE(String tag, String msg) {
        DJILog.logWriteE("RcCaliLog", IMemberProtocol.STRING_SEPERATOR_LEFT + tag + "] " + msg, "RcCaliLog", new Object[0]);
    }
}
