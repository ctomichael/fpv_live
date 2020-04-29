package com.dji.scan.qr.camera;

import android.os.Handler;
import android.os.HandlerThread;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
class CameraThread {
    private static final String TAG = CameraThread.class.getSimpleName();
    private static CameraThread instance;
    private final Object LOCK = new Object();
    private Handler handler;
    private int openCount = 0;
    private HandlerThread thread;

    public static synchronized CameraThread getInstance() {
        CameraThread cameraThread;
        synchronized (CameraThread.class) {
            if (instance == null) {
                instance = new CameraThread();
            }
            cameraThread = instance;
        }
        return cameraThread;
    }

    private CameraThread() {
    }

    /* access modifiers changed from: protected */
    public void enqueue(Runnable runnable) {
        synchronized (this.LOCK) {
            checkRunning();
            this.handler.post(runnable);
        }
    }

    /* access modifiers changed from: protected */
    public void enqueueDelayed(Runnable runnable, long delayMillis) {
        synchronized (this.LOCK) {
            checkRunning();
            this.handler.postDelayed(runnable, delayMillis);
        }
    }

    private void checkRunning() {
        synchronized (this.LOCK) {
            if (this.handler == null) {
                if (this.openCount <= 0) {
                    throw new IllegalStateException("CameraThread is not open");
                }
                this.thread = new HandlerThread("CameraThread");
                this.thread.start();
                this.handler = new Handler(this.thread.getLooper());
            }
        }
    }

    private void quit() {
        synchronized (this.LOCK) {
            this.thread.quit();
            this.thread = null;
            this.handler = null;
        }
    }

    /* access modifiers changed from: protected */
    public void decrementInstances() {
        synchronized (this.LOCK) {
            this.openCount--;
            if (this.openCount == 0) {
                quit();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void incrementAndEnqueue(Runnable runner) {
        synchronized (this.LOCK) {
            this.openCount++;
            enqueue(runner);
        }
    }
}
