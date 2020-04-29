package dji.sdksharedlib.extension;

import android.os.Handler;
import android.os.Message;
import dji.common.error.DJIError;
import dji.fieldAnnotation.EXClassNullAway;
import dji.sdksharedlib.DJISDKCache;
import dji.sdksharedlib.keycatalog.DJISDKCacheKey;
import dji.sdksharedlib.listener.DJIParamAccessListener;
import dji.sdksharedlib.listener.DJISetCallback;
import dji.sdksharedlib.store.DJISDKCacheParamValue;
import java.lang.ref.WeakReference;

@EXClassNullAway
public class TrackKeyModel<T> implements DJIParamAccessListener {
    public static final long DEFAULT_INTERVAL_SETVALUE = 200;
    public static final long MIN_INTERVAL_SETVALUE = 20;
    private static final int MSGID_SETVALUE = 4096;
    private final int STATUS_TRACK_FINISH = 2;
    private final int STATUS_TRACK_IDLE = 0;
    private final int STATUS_TRACK_NOW = 1;
    private volatile boolean isBinded = false;
    private final StaticHandler mHandler;
    private long mInterval = 200;
    private final DJISDKCacheKey mKeyForTracking;
    private int mSeed = 1;
    private int mTrackStatus = 0;
    private final DJIParamAccessListener mValueListener;

    public TrackKeyModel(DJISDKCacheKey key, DJIParamAccessListener listener) {
        this.mKeyForTracking = key;
        this.mValueListener = listener;
        this.mHandler = new StaticHandler();
    }

    public T getValue() {
        return null;
    }

    public TrackKeyModel setInterval(long interval) {
        if (interval < 20) {
            interval = 20;
        }
        this.mInterval = interval;
        return this;
    }

    public void bindData() {
        if (!this.isBinded) {
            this.isBinded = true;
            CacheHelper.addListener(this, this.mKeyForTracking);
        }
    }

    public void unbindData() {
        if (this.isBinded) {
            CacheHelper.removeListener(this);
            this.mHandler.removeCallbacksAndMessages(null);
            this.isBinded = false;
        }
    }

    public void startTracking() {
        this.mSeed++;
        this.mTrackStatus = 1;
        this.mHandler.sendEmptyMessageDelayed(4096, this.mInterval);
    }

    public void stopTracking(T value) {
        this.mHandler.removeCallbacksAndMessages(null);
        this.mTrackStatus = 2;
        setValueInner(value, this.mSeed);
    }

    public boolean beIdle() {
        return this.mTrackStatus == 0;
    }

    private void setValueInner(T value, final int seed) {
        DJISDKCache.getInstance().setValue(this.mKeyForTracking, value, new DJISetCallback() {
            /* class dji.sdksharedlib.extension.TrackKeyModel.AnonymousClass1 */

            public void onSuccess() {
                TrackKeyModel.this.handleCmdAck(true, seed);
            }

            public void onFails(DJIError error) {
                TrackKeyModel.this.handleCmdAck(false, seed);
            }
        });
    }

    /* access modifiers changed from: private */
    public void setValue() {
        T value = getValue();
        if (value != null && 1 == this.mTrackStatus) {
            setValueInner(value, Integer.MIN_VALUE);
            this.mHandler.sendEmptyMessageDelayed(4096, this.mInterval);
        }
    }

    /* access modifiers changed from: private */
    public void handleCmdAck(boolean success, final int seed) {
        if (Integer.MIN_VALUE != seed) {
            this.mHandler.post(new Runnable() {
                /* class dji.sdksharedlib.extension.TrackKeyModel.AnonymousClass2 */

                public void run() {
                    TrackKeyModel.this.finishTracking(seed);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void finishTracking(int seed) {
        if (this.mSeed == seed && 2 == this.mTrackStatus) {
            this.mTrackStatus = 0;
            DJISDKCacheParamValue value = DJISDKCache.getInstance().getAvailableValue(this.mKeyForTracking);
            onValueChange(this.mKeyForTracking, value, value);
        }
    }

    public void onValueChange(DJISDKCacheKey key, DJISDKCacheParamValue oldValue, DJISDKCacheParamValue newValue) {
        if (beIdle() && this.mKeyForTracking.equals(key)) {
            this.mValueListener.onValueChange(key, oldValue, newValue);
        }
    }

    private static final class StaticHandler extends Handler {
        private final WeakReference<TrackKeyModel> mOutCls;

        private StaticHandler(TrackKeyModel model) {
            this.mOutCls = new WeakReference<>(model);
        }

        public void handleMessage(Message msg) {
            TrackKeyModel model = this.mOutCls.get();
            if (model != null) {
                switch (msg.what) {
                    case 4096:
                        model.setValue();
                        return;
                    default:
                        return;
                }
            }
        }
    }
}
