package com.lmax.disruptor;

public interface EventReleaseAware {
    void setEventReleaser(EventReleaser eventReleaser);
}
