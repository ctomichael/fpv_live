package dji.midware.util;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class BackgroundLooper {
    private static volatile BackgroundLooper looper;
    public Handler handler;
    public HandlerThread handlerThread = new HandlerThread("dji_background_thread");

    public static BackgroundLooper get() {
        if (looper == null) {
            synchronized (BackgroundLooper.class) {
                if (looper == null) {
                    looper = new BackgroundLooper();
                }
            }
        }
        return looper;
    }

    private BackgroundLooper() {
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
