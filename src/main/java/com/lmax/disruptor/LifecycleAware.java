package com.lmax.disruptor;

public interface LifecycleAware {
    void onShutdown();

    void onStart();
}
