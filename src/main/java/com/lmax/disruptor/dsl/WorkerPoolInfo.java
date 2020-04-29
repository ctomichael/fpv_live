package com.lmax.disruptor.dsl;

import com.lmax.disruptor.Sequence;
import com.lmax.disruptor.SequenceBarrier;
import com.lmax.disruptor.WorkerPool;
import java.util.concurrent.Executor;

class WorkerPoolInfo<T> implements ConsumerInfo {
    private boolean endOfChain = true;
    private final SequenceBarrier sequenceBarrier;
    private final WorkerPool<T> workerPool;

    WorkerPoolInfo(WorkerPool<T> workerPool2, SequenceBarrier sequenceBarrier2) {
        this.workerPool = workerPool2;
        this.sequenceBarrier = sequenceBarrier2;
    }

    public Sequence[] getSequences() {
        return this.workerPool.getWorkerSequences();
    }

    public SequenceBarrier getBarrier() {
        return this.sequenceBarrier;
    }

    public boolean isEndOfChain() {
        return this.endOfChain;
    }

    public void start(Executor executor) {
        this.workerPool.start(executor);
    }

    public void halt() {
        this.workerPool.halt();
    }

    public void markAsUsedInBarrier() {
        this.endOfChain = false;
    }

    public boolean isRunning() {
        return this.workerPool.isRunning();
    }
}
