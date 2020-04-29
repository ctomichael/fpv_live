package com.dji.video.framing;

public class DJIVideoHEVCFomatManager {
    private static final String TAG = "DJIVideoHEVCFomatManager";
    private HevcChangeCallback mHevcChangeCallback;
    private boolean mIsInHevcMode = false;
    private boolean mIsPlaybackStatus = false;
    private boolean mIsSupportHevcMode = false;

    public interface HevcChangeCallback {
        void onHevcModeChanged(boolean z);

        void onHevcModeUnMatched(boolean z);
    }

    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static final DJIVideoHEVCFomatManager INSTANCE = new DJIVideoHEVCFomatManager();

        private SingletonHolder() {
        }
    }

    public static DJIVideoHEVCFomatManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public DJIVideoHEVCFomatManager() {
        checkCodecSupport();
    }

    private void checkCodecSupport() {
    }

    public synchronized boolean isInHevcMode() {
        return this.mIsInHevcMode && !this.mIsPlaybackStatus;
    }

    public boolean isSupportHevcMode() {
        return this.mIsSupportHevcMode;
    }

    public void setSupportHevcMode(boolean supportHevcMode) {
        this.mIsSupportHevcMode = supportHevcMode;
    }

    public void setHevcMode(boolean isHevcMode) {
        VideoLog.w(TAG, "setHevcMode() mIsSupportHevcMode:" + this.mIsSupportHevcMode + " mIsInHevcMode:" + this.mIsInHevcMode + " newHevcMode:" + isHevcMode, new Object[0]);
        boolean needNofity = false;
        synchronized (this) {
            if (this.mIsInHevcMode != isHevcMode) {
                this.mIsInHevcMode = isHevcMode;
                needNofity = true;
            }
        }
        if (needNofity && this.mHevcChangeCallback != null) {
            this.mHevcChangeCallback.onHevcModeChanged(this.mIsInHevcMode);
        }
    }

    public boolean checkVideoParserIsUnmatched(boolean isHevcFrame) {
        boolean result = false;
        synchronized (this) {
            if (isHevcFrame != this.mIsInHevcMode) {
                result = true;
            }
        }
        if (result && this.mHevcChangeCallback != null) {
            this.mHevcChangeCallback.onHevcModeUnMatched(isHevcFrame);
        }
        return result;
    }

    public void addHevcModeChangeListener(HevcChangeCallback callback) {
        this.mHevcChangeCallback = callback;
    }

    public void removeHevcModeChangeListener() {
        this.mHevcChangeCallback = null;
    }

    public synchronized boolean isPlaybackStatus() {
        return this.mIsPlaybackStatus;
    }

    public synchronized void setPlaybackStatus(boolean playbackStatus) {
        VideoLog.w(TAG, "setPlaybackStatus() playbackStatus:" + playbackStatus, new Object[0]);
        this.mIsPlaybackStatus = playbackStatus;
    }
}
