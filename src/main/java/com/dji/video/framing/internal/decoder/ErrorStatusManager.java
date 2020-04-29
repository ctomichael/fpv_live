package com.dji.video.framing.internal.decoder;

public class ErrorStatusManager {
    private static final int SEND_ERR_STATUS_INTERVAL = 110;
    private static final String TAG = "ErrorStatusManager";
    private boolean mCurrentErrorStatus = false;
    private FrameCheckerCallback mFrameCheckerCallback;
    private long mLastSendErrorStatusTime = 0;

    public interface FrameCheckerCallback {
        boolean getErrorStatus();

        boolean isDemandI();

        boolean needCheckFrame();

        void onErrorStatusChange(boolean z);

        boolean supportHevcTransfer();
    }

    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static final ErrorStatusManager INSTANCE = new ErrorStatusManager();

        private SingletonHolder() {
        }
    }

    public static ErrorStatusManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void setFrameCheckerCallback(FrameCheckerCallback callback) {
        this.mFrameCheckerCallback = callback;
    }

    public void onErrorStatusChange(boolean newStatus) {
        if (this.mFrameCheckerCallback != null) {
            long time = System.currentTimeMillis();
            if (newStatus != this.mCurrentErrorStatus) {
                this.mFrameCheckerCallback.onErrorStatusChange(newStatus);
                this.mLastSendErrorStatusTime = time;
                this.mCurrentErrorStatus = newStatus;
            } else if (time - this.mLastSendErrorStatusTime > 110) {
                this.mFrameCheckerCallback.onErrorStatusChange(newStatus);
                this.mLastSendErrorStatusTime = time;
            }
        }
    }

    public boolean getErrorStatus() {
        return this.mCurrentErrorStatus;
    }

    public boolean isDemandI() {
        return this.mFrameCheckerCallback != null && this.mFrameCheckerCallback.isDemandI();
    }

    public boolean needCheckFrame() {
        return this.mFrameCheckerCallback != null && this.mFrameCheckerCallback.needCheckFrame();
    }
}
