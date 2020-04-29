package com.lmax.disruptor.dsl;

import com.lmax.disruptor.Sequence;
import com.lmax.disruptor.SequenceBarrier;
import java.util.concurrent.Executor;

interface ConsumerInfo {
    SequenceBarrier getBarrier();

    Sequence[] getSequences();

    void halt();

    boolean isEndOfChain();

    boolean isRunning();

    void markAsUsedInBarrier();

    void start(Executor executor);
}
