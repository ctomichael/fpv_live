package com.dji.mapkit.lbs.helper.continuoustask;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

public class ContinuousTask extends Handler implements Runnable {
    private final ContinuousTaskRunner continuousRunner;
    private final ContinuousTaskScheduler continuousTaskScheduler = new ContinuousTaskScheduler(this);
    private final String taskId;

    public interface ContinuousTaskRunner {
        void runScheduledTask(@NonNull String str);
    }

    public ContinuousTask(@NonNull String taskId2, @NonNull ContinuousTaskRunner continuousTaskRunner) {
        super(Looper.getMainLooper());
        this.taskId = taskId2;
        this.continuousRunner = continuousTaskRunner;
    }

    public void delayed(long delayed) {
        this.continuousTaskScheduler.delayed(delayed);
    }

    public void pause() {
        this.continuousTaskScheduler.onPause();
    }

    public void resume() {
        this.continuousTaskScheduler.onResume();
    }

    public void stop() {
        this.continuousTaskScheduler.onStop();
    }

    public void run() {
        this.continuousRunner.runScheduledTask(this.taskId);
    }

    /* access modifiers changed from: package-private */
    public void schedule(long delay) {
        postDelayed(this, delay);
    }

    /* access modifiers changed from: package-private */
    public void unregister() {
        removeCallbacks(this);
    }

    /* access modifiers changed from: package-private */
    public long getCurrentTime() {
        return System.currentTimeMillis();
    }
}
