package com.dji.mapkit.lbs.helper.continuoustask;

class ContinuousTaskScheduler {
    private static final long NONE = Long.MIN_VALUE;
    private long initialTime = Long.MIN_VALUE;
    private boolean isSet = false;
    private long remainingTime = Long.MIN_VALUE;
    private long requiredDelay = Long.MIN_VALUE;
    private final ContinuousTask task;

    public ContinuousTaskScheduler(ContinuousTask task2) {
        this.task = task2;
    }

    /* access modifiers changed from: package-private */
    public boolean isSet() {
        return this.isSet;
    }

    /* access modifiers changed from: package-private */
    public void delayed(long delay) {
        this.requiredDelay = delay;
        this.remainingTime = this.requiredDelay;
        this.initialTime = this.task.getCurrentTime();
        set(delay);
    }

    /* access modifiers changed from: package-private */
    public void onPause() {
        if (this.requiredDelay != Long.MIN_VALUE) {
            release();
            this.remainingTime = this.requiredDelay - (this.task.getCurrentTime() - this.initialTime);
        }
    }

    /* access modifiers changed from: package-private */
    public void onResume() {
        if (this.remainingTime != Long.MIN_VALUE) {
            set(this.remainingTime);
        }
    }

    /* access modifiers changed from: package-private */
    public void onStop() {
        release();
        clean();
    }

    /* access modifiers changed from: package-private */
    public void set(long delay) {
        if (!this.isSet) {
            this.task.schedule(delay);
            this.isSet = true;
        }
    }

    /* access modifiers changed from: package-private */
    public void release() {
        this.task.unregister();
        this.isSet = false;
    }

    /* access modifiers changed from: package-private */
    public void clean() {
        this.requiredDelay = Long.MIN_VALUE;
        this.initialTime = Long.MIN_VALUE;
        this.remainingTime = Long.MIN_VALUE;
        this.isSet = false;
    }
}
