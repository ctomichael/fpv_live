package com.lmax.disruptor;

public interface SequenceReportingEventHandler<T> extends EventHandler<T> {
    void setSequenceCallback(Sequence sequence);
}
