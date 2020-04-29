package dji.component.mediaprovider;

import android.os.Handler;
import android.os.Message;

final /* synthetic */ class DJIMediaProviderSyncManager$$Lambda$0 implements Handler.Callback {
    private final DJIMediaProviderSyncManager arg$1;

    DJIMediaProviderSyncManager$$Lambda$0(DJIMediaProviderSyncManager dJIMediaProviderSyncManager) {
        this.arg$1 = dJIMediaProviderSyncManager;
    }

    public boolean handleMessage(Message message) {
        return this.arg$1.lambda$new$2$DJIMediaProviderSyncManager(message);
    }
}
