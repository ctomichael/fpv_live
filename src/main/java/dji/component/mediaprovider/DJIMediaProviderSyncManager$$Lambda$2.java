package dji.component.mediaprovider;

import android.media.MediaScannerConnection;
import android.net.Uri;

final /* synthetic */ class DJIMediaProviderSyncManager$$Lambda$2 implements MediaScannerConnection.OnScanCompletedListener {
    static final MediaScannerConnection.OnScanCompletedListener $instance = new DJIMediaProviderSyncManager$$Lambda$2();

    private DJIMediaProviderSyncManager$$Lambda$2() {
    }

    public void onScanCompleted(String str, Uri uri) {
        DJIMediaProviderUtils.saveLog(DJIMediaProviderSyncManager.TAG, "MediaScannerConnection.scanFile: " + str);
    }
}
