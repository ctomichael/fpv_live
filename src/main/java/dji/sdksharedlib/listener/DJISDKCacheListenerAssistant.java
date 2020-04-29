package dji.sdksharedlib.listener;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class DJISDKCacheListenerAssistant {
    private boolean isRunInMainThread = true;

    public DJISDKCacheListenerAssistant(boolean isRunInMainThread2) {
        this.isRunInMainThread = isRunInMainThread2;
    }

    public boolean getThread() {
        return this.isRunInMainThread;
    }
}
