package dji.sdksharedlib.hardware.extension;

import android.os.Handler;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.midware.data.config.P3.Ccode;
import dji.midware.interfaces.DJIDataCallBack;
import dji.sdksharedlib.util.DJISDKCacheThreadManager;

@EXClassNullAway
public abstract class DJISDKCacheSubscription {
    private static final int SECOND = 1000;
    private static final String TAG = "DJISDKCacheSubscription";
    private static Handler handler;
    protected DJIDataCallBack callback = new DJIDataCallBack() {
        /* class dji.sdksharedlib.hardware.extension.DJISDKCacheSubscription.AnonymousClass1 */

        public void onSuccess(Object model) {
            DJISDKCacheSubscription.this.startCountingTimer();
        }

        public void onFailure(Ccode ccode) {
        }
    };
    protected boolean hasAlreadySubscribed;
    protected Runnable turnOffRunnable = new Runnable() {
        /* class dji.sdksharedlib.hardware.extension.DJISDKCacheSubscription.AnonymousClass3 */

        public synchronized void run() {
            DJISDKCacheSubscription.this.turnOffSubscription();
            DJISDKCacheSubscription.this.hasAlreadySubscribed = false;
        }
    };
    protected Runnable turnOnRunnable = new Runnable() {
        /* class dji.sdksharedlib.hardware.extension.DJISDKCacheSubscription.AnonymousClass2 */

        public synchronized void run() {
            DJISDKCacheSubscription.this.turnOnSubscription();
            DJISDKCacheSubscription.this.hasAlreadySubscribed = true;
        }
    };

    /* access modifiers changed from: protected */
    public abstract void turnOffSubscription();

    /* access modifiers changed from: protected */
    public abstract void turnOnSubscription();

    public DJISDKCacheSubscription() {
        handler = new Handler(DJISDKCacheThreadManager.getSingletonBackgroundLooper());
        DJILog.d("SDR", "ss", new Object[0]);
    }

    public void destroy() {
        handler = null;
        this.callback = null;
    }

    private void refreshTimer() {
        if (handler != null) {
            handler.removeCallbacks(this.turnOffRunnable);
            handler.postDelayed(this.turnOffRunnable, 10000);
        }
    }

    public synchronized void start() {
        if (this.hasAlreadySubscribed) {
            refreshTimer();
        } else if (handler != null) {
            handler.post(this.turnOnRunnable);
        }
    }

    public void stop() {
        if (handler != null) {
            handler.removeCallbacks(this.turnOffRunnable);
        }
        turnOffSubscription();
    }

    public synchronized boolean hasAlreadySubscribed() {
        return this.hasAlreadySubscribed;
    }

    /* access modifiers changed from: private */
    public synchronized void startCountingTimer() {
        if (handler != null) {
            handler.postDelayed(this.turnOffRunnable, 10000);
            this.hasAlreadySubscribed = true;
        }
    }
}
