package dji.sdksharedlib.util;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.publics.DJIExecutor;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;

@EXClassNullAway
public class DJISDKCacheThreadManager {
    private static final String TAG = "DJISDKCacheThreadManager";
    private static boolean isRunInIVTs = false;
    private Handler backgroundHandler;
    private HandlerThread singletonBackgroundLooperThread;
    /* access modifiers changed from: private */
    public Executor threadPool;
    private Handler uiHandler;

    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static DJISDKCacheThreadManager instance = new DJISDKCacheThreadManager();

        private SingletonHolder() {
        }
    }

    public static DJISDKCacheThreadManager getInstance() {
        return SingletonHolder.instance;
    }

    public static void setIsRunInIVTs(boolean flag) {
        isRunInIVTs = flag;
    }

    public static Looper getSingletonBackgroundLooper() {
        return getInstance().getBackgroundLooper();
    }

    private DJISDKCacheThreadManager() {
        this.uiHandler = new Handler(Looper.getMainLooper());
        this.threadPool = DJIExecutor.getExecutorFor(DJIExecutor.Purpose.URGENT);
        this.singletonBackgroundLooperThread = new HandlerThread("SingleBackgroundLooper");
        this.singletonBackgroundLooperThread.start();
        this.backgroundHandler = new Handler(this.singletonBackgroundLooperThread.getLooper());
    }

    public Looper getBackgroundLooper() {
        return this.singletonBackgroundLooperThread.getLooper();
    }

    public static void invoke(Runnable r, boolean isRunInUIThread) {
        getInstance().post(r, isRunInUIThread);
    }

    public static void invokeInSingletonThread(Runnable r, boolean isRunInUIThread) {
        getInstance().postInSingletonThread(r, isRunInUIThread);
    }

    public static void invokeDelay(Runnable r, long time, boolean isRunInUIThread) {
        getInstance().postDelay(r, time, isRunInUIThread);
    }

    public static void removePendingRunnableInUIThread(Runnable r) {
        getInstance().removeRunnable(r);
    }

    /* access modifiers changed from: private */
    public Runnable attachExecutionTime(final Runnable r) {
        return new Runnable() {
            /* class dji.sdksharedlib.util.DJISDKCacheThreadManager.AnonymousClass1 */

            public void run() {
                long currentTimeMillis = System.currentTimeMillis();
                r.run();
                long currentTimeMillis2 = System.currentTimeMillis();
            }
        };
    }

    private void postInSingletonThread(Runnable r, boolean isRunInUIThread) {
        if (isRunInIVTs) {
            new Thread(r, "CacheSingleton").start();
        } else if (isRunInUIThread) {
            this.uiHandler.post(r);
        } else {
            this.backgroundHandler.post(r);
        }
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.log.DJILog.d(java.lang.String, java.lang.String, boolean, boolean):void
     arg types: [java.lang.String, java.lang.String, int, int]
     candidates:
      dji.log.DJILog.d(java.lang.String, java.lang.String, java.lang.Throwable, java.lang.Object[]):void
      dji.log.DJILog.d(java.lang.String, java.lang.String, boolean, boolean):void */
    private void post(Runnable r, boolean isRunInUIThread) {
        if (isRunInIVTs) {
            new Thread(r, "CacheThreadNew").start();
        } else if (isRunInUIThread) {
            this.uiHandler.post(r);
        } else {
            try {
                this.threadPool.execute(attachExecutionTime(r));
            } catch (RejectedExecutionException e) {
                DJILog.d(TAG, "task has been rejected!", true, true);
            }
        }
    }

    private void postDelay(final Runnable r, long time, boolean isRunInUIThread) {
        if (isRunInUIThread) {
            this.uiHandler.postDelayed(r, time);
            return;
        }
        this.uiHandler.postDelayed(new Runnable() {
            /* class dji.sdksharedlib.util.DJISDKCacheThreadManager.AnonymousClass2 */

            /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
             method: dji.log.DJILog.d(java.lang.String, java.lang.String, boolean, boolean):void
             arg types: [java.lang.String, java.lang.String, int, int]
             candidates:
              dji.log.DJILog.d(java.lang.String, java.lang.String, java.lang.Throwable, java.lang.Object[]):void
              dji.log.DJILog.d(java.lang.String, java.lang.String, boolean, boolean):void */
            public void run() {
                try {
                    DJISDKCacheThreadManager.this.threadPool.execute(DJISDKCacheThreadManager.this.attachExecutionTime(r));
                } catch (RejectedExecutionException e) {
                    DJILog.d(DJISDKCacheThreadManager.TAG, "task has been rejected!", true, true);
                }
            }
        }, time);
    }

    private void removeRunnable(Runnable runnable) {
        this.uiHandler.removeCallbacks(runnable);
    }
}
