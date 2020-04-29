package dji.midware.media.external;

import dji.midware.media.record.ExternalSdRecordingHelper;

public class DJIExternalStorageActionDispatcher {
    public static void checkUnfinishedFile() {
        ExternalSdRecordingHelper.checkUnfinishedFile();
        DJIDownloadExternalStorageHelper.checkUnfinishedFile();
    }

    public static void unGrantExternalSd() {
        ExternalSdRecordingHelper.setVideoCacheExternalStorageEnable(false);
        DJIDownloadExternalStorageHelper.setDownloadExternalStorageEnable(false);
    }
}
