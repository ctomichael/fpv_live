package com.lmax.disruptor.dsl;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.EventProcessor;
import com.lmax.disruptor.Sequence;
import com.lmax.disruptor.SequenceBarrier;
import java.util.concurrent.Executor;

class EventProcessorInfo<T> implements ConsumerInfo {
    private final SequenceBarrier barrier;
    private boolean endOfChain = true;
    private final EventProcessor eventprocessor;
    private final EventHandler<? super T> handler;

    EventProcessorInfo(EventProcessor eventprocessor2, EventHandler<? super T> handler2, SequenceBarrier barrier2) {
        this.eventprocessor = eventprocessor2;
        this.handler = handler2;
        this.barrier = barrier2;
    }

    public EventProcessor getEventProcessor() {
        return this.eventprocessor;
    }

    public Sequence[] getSequences() {
        return new Sequence[]{this.eventprocessor.getSequence()};
    }

    public EventHandler<? super T> getHandler() {
        return this.handler;
    }

    public SequenceBarrier getBarrier() {
        return this.barrier;
    }

    public boolean isEndOfChain() {
        return this.endOfChain;
    }

    public void start(Executor executor) {
        executor.execute(this.eventprocessor);
    }

    public void halt() {
        this.eventprocessor.halt();
    }

    public void markAsUsedInBarrier() {
        this.endOfChain = false;
    }

    public boolean isRunning() {
        return this.eventprocessor.isRunning();
    }
}
