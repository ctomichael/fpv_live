package com.lmax.disruptor;

import com.lmax.disruptor.util.Util;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

public final class WorkerPool<T> {
    private final RingBuffer<T> ringBuffer;
    private final AtomicBoolean started = new AtomicBoolean(false);
    private final WorkProcessor<?>[] workProcessors;
    private final Sequence workSequence = new Sequence(-1);

    public WorkerPool(RingBuffer<T> ringBuffer2, SequenceBarrier sequenceBarrier, ExceptionHandler<? super T> exceptionHandler, WorkHandler<? super T>... workHandlers) {
        this.ringBuffer = ringBuffer2;
        int numWorkers = workHandlers.length;
        this.workProcessors = new WorkProcessor[numWorkers];
        for (int i = 0; i < numWorkers; i++) {
            this.workProcessors[i] = new WorkProcessor<>(ringBuffer2, sequenceBarrier, workHandlers[i], exceptionHandler, this.workSequence);
        }
    }

    /* JADX WARN: Type inference failed for: r10v0, types: [com.lmax.disruptor.EventFactory, com.lmax.disruptor.EventFactory<T>], assign insn: null */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public WorkerPool(com.lmax.disruptor.EventFactory<T> r10, com.lmax.disruptor.ExceptionHandler<? super T> r11, com.lmax.disruptor.WorkHandler<? super T>... r12) {
        /*
            r9 = this;
            r3 = 0
            r9.<init>()
            java.util.concurrent.atomic.AtomicBoolean r0 = new java.util.concurrent.atomic.AtomicBoolean
            r0.<init>(r3)
            r9.started = r0
            com.lmax.disruptor.Sequence r0 = new com.lmax.disruptor.Sequence
            r4 = -1
            r0.<init>(r4)
            r9.workSequence = r0
            r0 = 1024(0x400, float:1.435E-42)
            com.lmax.disruptor.BlockingWaitStrategy r1 = new com.lmax.disruptor.BlockingWaitStrategy
            r1.<init>()
            com.lmax.disruptor.RingBuffer r0 = com.lmax.disruptor.RingBuffer.createMultiProducer(r10, r0, r1)
            r9.ringBuffer = r0
            com.lmax.disruptor.RingBuffer<T> r0 = r9.ringBuffer
            com.lmax.disruptor.Sequence[] r1 = new com.lmax.disruptor.Sequence[r3]
            com.lmax.disruptor.SequenceBarrier r2 = r0.newBarrier(r1)
            int r7 = r12.length
            com.lmax.disruptor.WorkProcessor[] r0 = new com.lmax.disruptor.WorkProcessor[r7]
            r9.workProcessors = r0
            r6 = 0
        L_0x002f:
            if (r6 >= r7) goto L_0x0044
            com.lmax.disruptor.WorkProcessor<?>[] r8 = r9.workProcessors
            com.lmax.disruptor.WorkProcessor r0 = new com.lmax.disruptor.WorkProcessor
            com.lmax.disruptor.RingBuffer<T> r1 = r9.ringBuffer
            r3 = r12[r6]
            com.lmax.disruptor.Sequence r5 = r9.workSequence
            r4 = r11
            r0.<init>(r1, r2, r3, r4, r5)
            r8[r6] = r0
            int r6 = r6 + 1
            goto L_0x002f
        L_0x0044:
            com.lmax.disruptor.RingBuffer<T> r0 = r9.ringBuffer
            com.lmax.disruptor.Sequence[] r1 = r9.getWorkerSequences()
            r0.addGatingSequences(r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.lmax.disruptor.WorkerPool.<init>(com.lmax.disruptor.EventFactory, com.lmax.disruptor.ExceptionHandler, com.lmax.disruptor.WorkHandler[]):void");
    }

    public Sequence[] getWorkerSequences() {
        Sequence[] sequences = new Sequence[(this.workProcessors.length + 1)];
        int size = this.workProcessors.length;
        for (int i = 0; i < size; i++) {
            sequences[i] = this.workProcessors[i].getSequence();
        }
        sequences[sequences.length - 1] = this.workSequence;
        return sequences;
    }

    public RingBuffer<T> start(Executor executor) {
        if (!this.started.compareAndSet(false, true)) {
            throw new IllegalStateException("WorkerPool has already been started and cannot be restarted until halted.");
        }
        long cursor = this.ringBuffer.getCursor();
        this.workSequence.set(cursor);
        WorkProcessor<?>[] workProcessorArr = this.workProcessors;
        for (WorkProcessor<?> processor : workProcessorArr) {
            processor.getSequence().set(cursor);
            executor.execute(processor);
        }
        return this.ringBuffer;
    }

    public void drainAndHalt() {
        Sequence[] workerSequences = getWorkerSequences();
        while (this.ringBuffer.getCursor() > Util.getMinimumSequence(workerSequences)) {
            Thread.yield();
        }
        for (WorkProcessor<?> processor : this.workProcessors) {
            processor.halt();
        }
        this.started.set(false);
    }

    public void halt() {
        for (WorkProcessor<?> processor : this.workProcessors) {
            processor.halt();
        }
        this.started.set(false);
    }

    public boolean isRunning() {
        return this.started.get();
    }
}
