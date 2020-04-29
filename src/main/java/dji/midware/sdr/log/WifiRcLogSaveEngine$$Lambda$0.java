package dji.midware.sdr.log;

import java.util.concurrent.ThreadFactory;

final /* synthetic */ class WifiRcLogSaveEngine$$Lambda$0 implements ThreadFactory {
    static final ThreadFactory $instance = new WifiRcLogSaveEngine$$Lambda$0();

    private WifiRcLogSaveEngine$$Lambda$0() {
    }

    public Thread newThread(Runnable runnable) {
        return WifiRcLogSaveEngine.lambda$new$0$WifiRcLogSaveEngine(runnable);
    }
}
