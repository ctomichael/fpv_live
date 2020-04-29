package dji.midware.usb.P3;

import dji.log.DJILog;
import dji.log.DJILogHelper;
import java.util.Locale;

public class AoaLogUtil {
    private static final String TAG = "AoaLogUtil";
    private static long count = 0;
    private static long lastT = 0;
    private static float mDataRate = 0.0f;

    public static void printAndSave(String s) {
        DJILogHelper.getInstance().LOGD(TAG, s);
        DJILog.saveConnectDebug("AoaLogUtil " + s);
    }

    public static void printRate(int length) {
        count += (long) length;
        if (getTickCount() - lastT > 2000) {
            float rate = (((float) count) * 0.5f) / 1024.0f;
            if (rate > 1024.0f) {
                printAndSave(String.format(Locale.US, "rate %.2f MB\n", Float.valueOf(rate / 1024.0f)));
            } else {
                printAndSave(String.format(Locale.US, "rate %.2f KB\n", Float.valueOf(rate)));
            }
            mDataRate = rate;
            lastT = getTickCount();
            count = 0;
        }
    }

    public static float getDataRate() {
        return mDataRate;
    }

    private static long getTickCount() {
        return System.currentTimeMillis();
    }
}
