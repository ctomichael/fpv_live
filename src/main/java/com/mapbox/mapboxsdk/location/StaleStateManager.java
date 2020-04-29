package com.mapbox.mapboxsdk.location;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import java.lang.ref.WeakReference;

class StaleStateManager {
    private long delayTime;
    @NonNull
    private final StaleMessageHandler handler;
    private final OnLocationStaleListener innerOnLocationStaleListeners;
    private boolean isEnabled;
    private boolean isStale = true;
    private final int staleStateMessage = 1;

    StaleStateManager(OnLocationStaleListener innerListener, LocationComponentOptions options) {
        this.innerOnLocationStaleListeners = innerListener;
        this.handler = new StaleMessageHandler();
        this.isEnabled = options.enableStaleState();
        this.delayTime = options.staleStateTimeout();
    }

    /* access modifiers changed from: package-private */
    public void setEnabled(boolean enabled) {
        if (enabled) {
            setState(this.isStale);
        } else if (this.isEnabled) {
            onStop();
            this.innerOnLocationStaleListeners.onStaleStateChange(false);
        }
        this.isEnabled = enabled;
    }

    /* access modifiers changed from: package-private */
    public boolean isStale() {
        return this.isStale;
    }

    /* access modifiers changed from: package-private */
    public void updateLatestLocationTime() {
        setState(false);
        postTheCallback();
    }

    /* access modifiers changed from: package-private */
    public void setDelayTime(long delayTime2) {
        this.delayTime = delayTime2;
        if (this.handler.hasMessages(1)) {
            postTheCallback();
        }
    }

    /* access modifiers changed from: package-private */
    public void onStart() {
        if (!this.isStale) {
            postTheCallback();
        }
    }

    /* access modifiers changed from: package-private */
    public void onStop() {
        this.handler.removeCallbacksAndMessages(null);
    }

    private void postTheCallback() {
        this.handler.removeCallbacksAndMessages(null);
        this.handler.sendEmptyMessageDelayed(1, this.delayTime);
    }

    /* access modifiers changed from: private */
    public void setState(boolean stale) {
        if (stale != this.isStale) {
            this.isStale = stale;
            if (this.isEnabled) {
                this.innerOnLocationStaleListeners.onStaleStateChange(stale);
            }
        }
    }

    private static class StaleMessageHandler extends Handler {
        private final WeakReference<StaleStateManager> managerWeakReference;

        private StaleMessageHandler(StaleStateManager staleStateManager) {
            this.managerWeakReference = new WeakReference<>(staleStateManager);
        }

        public void handleMessage(Message msg) {
            StaleStateManager manager = this.managerWeakReference.get();
            if (manager != null) {
                manager.setState(true);
            }
        }
    }
}
