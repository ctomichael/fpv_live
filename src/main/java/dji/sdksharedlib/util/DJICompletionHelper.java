package dji.sdksharedlib.util;

import android.os.Handler;
import android.os.Message;
import dji.common.error.DJIError;
import dji.common.util.CallbackUtils;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.P3.DataOsdGetPushCommon;
import dji.sdksharedlib.DJISDKCache;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.keycatalog.DJISDKCacheKey;
import dji.sdksharedlib.keycatalog.FlightControllerKeys;
import dji.sdksharedlib.listener.DJIParamAccessListener;
import dji.sdksharedlib.store.DJISDKCacheParamValue;

@EXClassNullAway
public class DJICompletionHelper {
    private static final DJISDKCacheKey ALTITUDE_KEY = new DJISDKCacheKey.Builder().component(FlightControllerKeys.COMPONENT_KEY).paramKey(FlightControllerKeys.ALTITUDE).build();
    private static final int REMOVE_LISTENER = 0;
    private static final String TAG = "DJICompletionHelper";
    private static DJICompletionHelper instance;
    private DefaultCompletionCallback cancelTakeOffCallback;
    /* access modifiers changed from: private */
    public DataOsdGetPushCommon.FLYC_STATE currentFlycState;
    /* access modifiers changed from: private */
    public Handler handler = new Handler(DJISDKCacheThreadManager.getSingletonBackgroundLooper()) {
        /* class dji.sdksharedlib.util.DJICompletionHelper.AnonymousClass1 */

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    DJISDKCache.getInstance().stopListening((DJIParamAccessListener) msg.obj);
                    return;
                default:
                    return;
            }
        }
    };
    /* access modifiers changed from: private */
    public DataOsdGetPushCommon.FLYC_STATE lastFlycState = DataOsdGetPushCommon.FLYC_STATE.GoHome;
    /* access modifiers changed from: private */
    public DefaultCompletionCallback takeOffCallback;
    final DJIParamAccessListener takeOffListener = new DJIParamAccessListener() {
        /* class dji.sdksharedlib.util.DJICompletionHelper.AnonymousClass2 */

        public void onValueChange(DJISDKCacheKey key, DJISDKCacheParamValue oldValue, DJISDKCacheParamValue newValue) {
            if (newValue != null && newValue.isValid()) {
                DataOsdGetPushCommon.FLYC_STATE unused = DJICompletionHelper.this.currentFlycState = DataOsdGetPushCommon.getInstance().getFlycState();
                if (DJICompletionHelper.this.lastFlycState.equals(DataOsdGetPushCommon.FLYC_STATE.AutoTakeoff) && DJICompletionHelper.this.currentFlycState.equals(DataOsdGetPushCommon.FLYC_STATE.GPS_Atti)) {
                    DJICompletionHelper.this.takeOffCallback.onSuccess(null);
                }
                DataOsdGetPushCommon.FLYC_STATE unused2 = DJICompletionHelper.this.lastFlycState = DJICompletionHelper.this.currentFlycState;
            }
        }
    };
    final Runnable takeOffTimeOut = new Runnable() {
        /* class dji.sdksharedlib.util.DJICompletionHelper.AnonymousClass3 */

        public void run() {
            DJICompletionHelper.this.takeOffCallback.onFails(DJIError.COMMON_TIMEOUT);
        }
    };

    public static synchronized DJICompletionHelper getInstance() {
        DJICompletionHelper dJICompletionHelper;
        synchronized (DJICompletionHelper.class) {
            if (instance == null) {
                instance = new DJICompletionHelper();
            }
            dJICompletionHelper = instance;
        }
        return dJICompletionHelper;
    }

    public synchronized void takeOffCompletionHelper(DJISDKCacheHWAbstraction.InnerCallback callback) {
        this.takeOffCallback = new DefaultCompletionCallback(callback, this.takeOffTimeOut, this.takeOffListener);
        this.handler.postDelayed(this.takeOffTimeOut, 8000);
        DJISDKCache.getInstance().startListeningForUpdates(ALTITUDE_KEY, this.takeOffListener, false);
    }

    public synchronized void cancelTakeOffCallbackCompletionHelper(DJISDKCacheHWAbstraction.InnerCallback callback) {
        CallbackUtils.onSuccess(callback, (Object) null);
        DJISDKCache.getInstance().stopListening(this.takeOffListener);
        this.handler.removeCallbacks(this.takeOffTimeOut);
    }

    private class DefaultCompletionCallback implements DJISDKCacheHWAbstraction.InnerCallback {
        private DJISDKCacheHWAbstraction.InnerCallback innerCallback;
        private DJIParamAccessListener listener;
        private Runnable runnable;

        public DefaultCompletionCallback(DJISDKCacheHWAbstraction.InnerCallback callback, Runnable runnable2, DJIParamAccessListener listener2) {
            this.innerCallback = callback;
            this.runnable = runnable2;
            this.listener = listener2;
        }

        public synchronized void onSuccess(Object o) {
            CallbackUtils.onSuccess(this.innerCallback, (Object) null);
            DJISDKCache.getInstance().stopListening(this.listener);
            DJICompletionHelper.this.handler.removeCallbacks(this.runnable);
        }

        public synchronized void onFails(DJIError error) {
            CallbackUtils.onFailure(this.innerCallback, DJIError.COMMON_TIMEOUT);
            DJISDKCache.getInstance().stopListening(this.listener);
        }
    }
}
