package dji.pilot.fpv.util;

import android.app.Activity;
import android.content.Context;
import dji.fieldAnnotation.EXClassNullAway;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;

@EXClassNullAway
public class DJIAliyunStatisticAgent {
    private static boolean isOpen = false;
    private static final DJIAliyunStatisticAgent mSingleton = new DJIAliyunStatisticAgent();
    AtomicBoolean mIsInit = new AtomicBoolean(false);
    private Map<String, Queue<Long>> timeMap = new HashMap();

    public static void setIsOpen(boolean isOpen2) {
        isOpen = isOpen2;
    }

    public static DJIAliyunStatisticAgent getInstance() {
        return mSingleton;
    }

    public void init(Context context) {
    }

    public void startEvent(String event) {
    }

    private boolean isCloseReport() {
        return DJIFlurryUtil.isCloseReport() || !this.mIsInit.get();
    }

    public void endEvent(String event) {
    }

    public void logEvent(String event) {
        logEvent(event, new HashMap(0), null);
    }

    public void logEvent(String event, Map<String, String> params) {
        logEvent(event, params, null);
    }

    public void logEventWithTime(String event, Long duration) {
        logEvent(event, new HashMap(0), duration);
    }

    public void logEvent(String event, Map<String, String> map, Long duration) {
    }

    public void logActivityResume(Activity activity) {
    }

    public void logActivityPause(Activity activity) {
    }

    public void logRegister(String userName) {
    }

    public void logLogin(String userName, String uid) {
    }

    public void logLogout() {
    }
}
