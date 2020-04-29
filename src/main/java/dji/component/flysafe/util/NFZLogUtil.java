package dji.component.flysafe.util;

import android.support.annotation.Keep;
import dji.component.appstate.IAppStateService;
import dji.fieldAnnotation.EXClassNullAway;
import dji.flysafe.LimitArea;
import dji.log.DJILog;
import dji.log.DJILogHelper;
import dji.log.DJILogUtils;
import dji.log.GlobalConfig;
import dji.service.DJIAppServiceManager;
import java.text.SimpleDateFormat;
import java.util.List;

@Keep
@EXClassNullAway
public class NFZLogUtil {
    private static final int CONVERT_WORD = 1098439584;
    private static boolean DEBUG = true;
    private static final String NFZ_SAVE_DIR = "NFZ";
    private static SimpleDateFormat df = new SimpleDateFormat(DJILogUtils.FORMAT_2);

    public static void LOGD(String content) {
        if (DEBUG) {
            DJILog.d("NFZ", content, new Object[0]);
        } else if (GlobalConfig.LOG_DEBUGGABLE) {
            DJILogHelper.getInstance().LOGD("nfz", content, true, true);
        }
    }

    public static synchronized void savedLOGD(String content) {
        synchronized (NFZLogUtil.class) {
            DJILog.logWriteD("NFZ", content, "NFZ", new Object[0]);
        }
    }

    public static synchronized void savedLOGE(String content) {
        synchronized (NFZLogUtil.class) {
            DJILog.logWriteE("NFZ", content, "NFZ", new Object[0]);
        }
    }

    public static void saveLogD(String tag, String content) {
        DJILog.logWriteD("NFZ-" + tag, content, "NFZ", new Object[0]);
    }

    public static double convertCoord4Log(double coordVal) {
        IAppStateService appStateService = (IAppStateService) DJIAppServiceManager.getInstance().getService(IAppStateService.NAME);
        return (appStateService == null || !appStateService.isInnerApp()) ? (double) (((int) (1000000.0d * coordVal)) ^ CONVERT_WORD) : coordVal;
    }

    public static void printIndex0AreaPos(List<LimitArea> areas) {
        if (areas != null && !areas.isEmpty()) {
            savedLOGD("area index 0, lat: " + convertCoord4Log(ProtobufHelper.toDouble(areas.get(0).latitude)) + ", lng: " + convertCoord4Log(ProtobufHelper.toDouble(areas.get(0).longitude)) + ", radius: " + areas.get(0).radius);
        }
    }
}
