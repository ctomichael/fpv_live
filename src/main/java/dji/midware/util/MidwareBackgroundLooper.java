package dji.midware.util;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class MidwareBackgroundLooper {
    private static volatile MidwareBackgroundLooper looper;
    public Handler handler;
    public HandlerThread handlerThread = new HandlerThread("midware_background_thread");

    public static MidwareBackgroundLooper get() {
        if (looper == null) {
            synchronized (MidwareBackgroundLooper.class) {
                if (looper == null) {
                    looper = new MidwareBackgroundLooper();
                }
            }
        }
        return looper;
    }

    private MidwareBackgroundLooper() {
        this.handlerThread.start();
        this.handler = new Handler(this.handlerThread.getLooper());
    }

    public static Looper getLooper() {
        return get().handlerThread.getLooper();
    }

    public static void post(Runnable runnable) {
        get().handler.post(runnable);
    }

    public static void postDelayed(Runnable runnable, long delayTimes) {
        get().handler.postDelayed(runnable, delayTimes);
    }

    public static void remove(Runnable runnable) {
        get().handler.removeCallbacks(runnable);
    }
}
