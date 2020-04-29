package com.dji.lifecycle;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.dji.lifecycle.core.LifecycleEvent;
import dji.log.DJILog;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.extension.KeyHelper;
import dji.sdksharedlib.keycatalog.DJISDKCacheKey;
import dji.sdksharedlib.keycatalog.DJISDKCacheKeys;
import dji.sdksharedlib.listener.DJIParamAccessListener;
import dji.sdksharedlib.store.DJISDKCacheParamValue;
import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Lifecycle implements ILifecycle, DJIParamAccessListener {
    private static final String FPV_NAME = "FpvComponentActivity";
    static final String TAG = "Lifecycle";
    private static Lifecycle mInstance;
    /* access modifiers changed from: private */
    public AtomicInteger mActivityCount = new AtomicInteger(0);
    private DJISDKCacheKey mKeyDroneConnection = KeyHelper.getProductKey(DJISDKCacheKeys.CONNECTION);
    private DJISDKCacheKey mKeyRcConnection = KeyHelper.getRemoteControllerKey(DJISDKCacheKeys.CONNECTION);
    /* access modifiers changed from: private */
    public LifecycleLogicReceiverHolder mReceiverHolder;
    /* access modifiers changed from: private */
    public AtomicReference<WeakReference<Activity>> mTopActivityReference = new AtomicReference<>();
    /* access modifiers changed from: private */
    public WeakReference<Activity> mTopActivityWeakRef;

    public static synchronized void build(@NonNull Application application) {
        synchronized (Lifecycle.class) {
            if (mInstance == null) {
                mInstance = new Lifecycle(application);
            } else {
                throw new RuntimeException("Lifecycle is already built");
            }
        }
    }

    private Lifecycle(@NonNull Application application) {
        DJILog.logWriteD(TAG, "Lifecycle build", TAG, new Object[0]);
        this.mReceiverHolder = new LifecycleLogicReceiverHolder(application.getApplicationContext(), this);
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            /* class com.dji.lifecycle.Lifecycle.AnonymousClass1 */

            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            }

            public void onActivityStarted(Activity activity) {
                if (Lifecycle.this.mActivityCount.incrementAndGet() == 1) {
                    Lifecycle.this.mReceiverHolder.notifyLifecycleChange(LifecycleEvent.ON_APP_FOREGROUND);
                }
                if (activity.getClass().getSimpleName().contains(Lifecycle.FPV_NAME)) {
                    Lifecycle.this.mReceiverHolder.notifyLifecycleChange(LifecycleEvent.ON_FPV_START);
                }
            }

            public void onActivityResumed(Activity activity) {
                WeakReference unused = Lifecycle.this.mTopActivityWeakRef = new WeakReference(activity);
                Lifecycle.this.mTopActivityReference.set(Lifecycle.this.mTopActivityWeakRef);
                if (activity.getClass().getSimpleName().contains(Lifecycle.FPV_NAME)) {
                    Lifecycle.this.mReceiverHolder.notifyLifecycleChange(LifecycleEvent.ON_FPV_RESUME);
                }
            }

            public void onActivityPaused(Activity activity) {
                Lifecycle.this.mTopActivityReference.set(null);
                if (activity.getClass().getSimpleName().contains(Lifecycle.FPV_NAME)) {
                    Lifecycle.this.mReceiverHolder.notifyLifecycleChange(LifecycleEvent.ON_FPV_PAUSE);
                }
            }

            public void onActivityStopped(Activity activity) {
                if (Lifecycle.this.mActivityCount.decrementAndGet() == 0) {
                    Lifecycle.this.mReceiverHolder.notifyLifecycleChange(LifecycleEvent.ON_APP_BACKGROUND);
                }
                if (activity.getClass().getSimpleName().contains(Lifecycle.FPV_NAME)) {
                    Lifecycle.this.mReceiverHolder.notifyLifecycleChange(LifecycleEvent.ON_FPV_STOP);
                }
            }

            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }

            public void onActivityDestroyed(Activity activity) {
            }
        });
        this.mReceiverHolder.notifyLifecycleChange(LifecycleEvent.ON_APP_START);
        CacheHelper.addListener(this, this.mKeyDroneConnection, this.mKeyRcConnection);
        if (((Boolean) CacheHelper.getValue(this.mKeyRcConnection, false)).booleanValue()) {
            this.mReceiverHolder.notifyLifecycleChange(LifecycleEvent.ON_RC_CONNECT);
        }
        if (((Boolean) CacheHelper.getValue(this.mKeyDroneConnection, false)).booleanValue()) {
            this.mReceiverHolder.notifyLifecycleChange(LifecycleEvent.ON_DRONE_CONNECT);
        }
    }

    @Nullable
    public Activity getTopActivity() {
        WeakReference<Activity> weakReference = this.mTopActivityReference.get();
        if (weakReference != null) {
            return (Activity) weakReference.get();
        }
        return null;
    }

    @Nullable
    public Activity getApplicationTopActivity() {
        if (this.mTopActivityWeakRef == null) {
            return null;
        }
        return this.mTopActivityWeakRef.get();
    }

    public void onValueChange(DJISDKCacheKey key, DJISDKCacheParamValue oldValue, DJISDKCacheParamValue newValue) {
        if (key.equals(this.mKeyDroneConnection)) {
            this.mReceiverHolder.notifyLifecycleChange(((Boolean) CacheHelper.getValue(this.mKeyDroneConnection, false)).booleanValue() ? LifecycleEvent.ON_DRONE_CONNECT : LifecycleEvent.ON_DRONE_DISCONNECT);
        } else if (key.equals(this.mKeyRcConnection)) {
            this.mReceiverHolder.notifyLifecycleChange(((Boolean) CacheHelper.getValue(this.mKeyRcConnection, false)).booleanValue() ? LifecycleEvent.ON_RC_CONNECT : LifecycleEvent.ON_RC_DISCONNECT);
        }
    }
}
