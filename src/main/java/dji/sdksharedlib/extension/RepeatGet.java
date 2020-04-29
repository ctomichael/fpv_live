package dji.sdksharedlib.extension;

import dji.common.error.DJIError;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.util.BackgroundLooper;
import dji.sdksharedlib.DJISDKCache;
import dji.sdksharedlib.keycatalog.DJISDKCacheKey;
import dji.sdksharedlib.listener.DJIGetCallback;
import dji.sdksharedlib.store.DJISDKCacheParamValue;
import dji.sdksharedlib.util.DJISDKCacheThreadManager;

@EXClassNullAway
public class RepeatGet implements DJIGetCallback, Runnable {
    public final String TAG = "RepeatGet";
    private DJIGetCallback callback;
    private int count;
    private DJISDKCacheKey key;
    private int repeatDelayTime;
    private int repeatInvoke = -1;
    private int repeatTime;
    private Runnable runnable = null;

    private void getFlycSnDemo() {
        DJISDKCache.getInstance().getValue(KeyHelper.getFlightControllerKey("InternalSerialNumber"), new DJIGetCallback() {
            /* class dji.sdksharedlib.extension.RepeatGet.AnonymousClass1 */

            public void onSuccess(DJISDKCacheParamValue value) {
            }

            public void onFails(DJIError error) {
            }
        });
    }

    private void getFlycSnRepeatDemo() {
        new RepeatGet(KeyHelper.getFlightControllerKey("InternalSerialNumber"), 3, new DJIGetCallback() {
            /* class dji.sdksharedlib.extension.RepeatGet.AnonymousClass2 */

            public void onSuccess(DJISDKCacheParamValue value) {
            }

            public void onFails(DJIError error) {
            }
        }).start();
    }

    public RepeatGet(DJISDKCacheKey key2, DJIGetCallback callback2) {
        this.key = key2;
        this.repeatTime = 3;
        this.repeatDelayTime = 1000;
        this.callback = callback2;
        this.count = 0;
    }

    public RepeatGet(DJISDKCacheKey key2, int repeatTime2, DJIGetCallback callback2) {
        this.key = key2;
        this.repeatTime = repeatTime2;
        this.repeatDelayTime = 1000;
        this.callback = callback2;
        this.count = 0;
    }

    public RepeatGet(DJISDKCacheKey key2, int repeatTime2, int repeatDelayTime2, DJIGetCallback callBack) {
        this.key = key2;
        this.repeatTime = repeatTime2;
        this.repeatDelayTime = repeatDelayTime2;
        this.callback = callBack;
        this.count = 0;
    }

    public void start() {
        DJISDKCache.getInstance().getValue(this.key, this);
    }

    public void run() {
        start();
    }

    public void onSuccess(DJISDKCacheParamValue value) {
        if (this.callback != null) {
            this.callback.onSuccess(value);
        }
    }

    public void onFails(DJIError error) {
        this.count++;
        if (this.count < this.repeatTime) {
            DJISDKCacheThreadManager.invokeDelay(this, (long) this.repeatDelayTime, true);
            if (this.repeatInvoke > 0 && this.repeatInvoke == this.count) {
                BackgroundLooper.post(this.runnable);
                return;
            }
            return;
        }
        this.callback.onFails(error);
    }
}
