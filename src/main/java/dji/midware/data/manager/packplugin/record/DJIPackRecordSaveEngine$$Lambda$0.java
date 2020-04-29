package dji.midware.data.manager.packplugin.record;

import java.util.concurrent.ThreadFactory;

final /* synthetic */ class DJIPackRecordSaveEngine$$Lambda$0 implements ThreadFactory {
    static final ThreadFactory $instance = new DJIPackRecordSaveEngine$$Lambda$0();

    private DJIPackRecordSaveEngine$$Lambda$0() {
    }

    public Thread newThread(Runnable runnable) {
        return DJIPackRecordSaveEngine.lambda$new$0$DJIPackRecordSaveEngine(runnable);
    }
}
