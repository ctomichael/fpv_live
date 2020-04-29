package com.dji.lifecycle.core;

public class LifecycleLasting {
    private LifecycleEvent mEndEvent;
    private LifecycleEvent mStartEvent;

    public LifecycleLasting(LifecycleEvent start, LifecycleEvent end) {
        this.mStartEvent = start;
        this.mEndEvent = end;
    }

    public LifecycleEvent getStartEvent() {
        return this.mStartEvent;
    }

    public LifecycleEvent getEndEvent() {
        return this.mEndEvent;
    }
}
