package dji.midware.data.manager.packplugin;

import dji.log.DJILog;

public class PackPluginLog {
    private static final String TAG = "PackPluginLog";

    public static void logWriteE(String tag, String content) {
        DJILog.logWriteE(TAG + tag, content, TAG, new Object[0]);
    }

    public static void logWriteI(String tag, String content) {
        DJILog.logWriteI(TAG + tag, content, TAG, new Object[0]);
    }
}
