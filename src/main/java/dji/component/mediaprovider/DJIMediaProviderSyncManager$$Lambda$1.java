package dji.component.mediaprovider;

import android.media.MediaScannerConnection;
import android.net.Uri;

final /* synthetic */ class DJIMediaProviderSyncManager$$Lambda$1 implements MediaScannerConnection.OnScanCompletedListener {
    static final MediaScannerConnection.OnScanCompletedListener $instance = new DJIMediaProviderSyncManager$$Lambda$1();

    private DJIMediaProviderSyncManager$$Lambda$1() {
    }

    public void onScanCompleted(String str, Uri uri) {
        DJIMediaProviderUtils.saveLog(DJIMediaProviderSyncManager.TAG, "MediaScannerConnection.scanFile: " + str);
    }
}
