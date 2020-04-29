package dji.midware.transfer;

import dji.log.DJILog;

public class FileTransferLog {
    private static final boolean DEBUG_LOG = true;
    private static final String TAG = "MultiFileTransfer";

    public static void d(String str) {
        DJILog.logWriteD("MultiFileTransfer", str, "MultiFileTransfer", new Object[0]);
    }

    public static void e(String str) {
        DJILog.logWriteD("MultiFileTransfer", str, "MultiFileTransfer", new Object[0]);
    }
}
