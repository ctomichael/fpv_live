package com.lmax.disruptor;

import java.util.concurrent.atomic.AtomicBoolean;
import kotlin.jvm.internal.LongCompanionObject;

public final class WorkProcessor<T> implements EventProcessor {
    private final EventReleaser eventReleaser = new EventReleaser() {
        /* class com.lmax.disruptor.WorkProcessor.AnonymousClass1 */

        public void release() {
            WorkProcessor.this.sequence.set(LongCompanionObject.MAX_VALUE);
        }
    };
    private final ExceptionHandler<? super T> exceptionHandler;
    private final RingBuffer<T> ringBuffer;
    private final AtomicBoolean running = new AtomicBoolean(false);
    /* access modifiers changed from: private */
    public final Sequence sequence = new Sequence(-1);
    private final SequenceBarrier sequenceBarrier;
    private final TimeoutHandler timeoutHandler;
    private final WorkHandler<? super T> workHandler;
    private final Sequence workSequence;

    public WorkProcessor(RingBuffer<T> ringBuffer2, SequenceBarrier sequenceBarrier2, WorkHandler<? super T> workHandler2, ExceptionHandler<? super T> exceptionHandler2, Sequence workSequence2) {
        this.ringBuffer = ringBuffer2;
        this.sequenceBarrier = sequenceBarrier2;
        this.workHandler = workHandler2;
        this.exceptionHandler = exceptionHandler2;
        this.workSequence = workSequence2;
        if (this.workHandler instanceof EventReleaseAware) {
            ((EventReleaseAware) this.workHandler).setEventReleaser(this.eventReleaser);
        }
        this.timeoutHandler = workHandler2 instanceof TimeoutHandler ? (TimeoutHandler) workHandler2 : null;
    }

    public Sequence getSequence() {
        return this.sequence;
    }

    public void halt() {
        this.running.set(false);
        this.sequenceBarrier.alert();
    }

    public boolean isRunning() {
        return this.running.get();
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x0047 A[Catch:{ TimeoutException -> 0x005b, AlertException -> 0x0066, Throwable -> 0x0078 }] */
    /* JADX WARNING: Removed duplicated region for block: B:13:0x0054 A[Catch:{ TimeoutException -> 0x005b, AlertException -> 0x0066, Throwable -> 0x0078 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void run() {
        /*
            r15 = this;
            r14 = 0
            r12 = 1
            java.util.concurrent.atomic.AtomicBoolean r8 = r15.running
            r9 = 1
            boolean r8 = r8.compareAndSet(r14, r9)
            if (r8 != 0) goto L_0x0015
            java.lang.IllegalStateException r8 = new java.lang.IllegalStateException
            java.lang.String r9 = "Thread is already running"
            r8.<init>(r9)
            throw r8
        L_0x0015:
            com.lmax.disruptor.SequenceBarrier r8 = r15.sequenceBarrier
            r8.clearAlert()
            r15.notifyStart()
            r5 = 1
            r0 = -9223372036854775808
            com.lmax.disruptor.Sequence r8 = r15.sequence
            long r6 = r8.get()
            r3 = 0
        L_0x0027:
            if (r5 == 0) goto L_0x0043
            r5 = 0
        L_0x002a:
            com.lmax.disruptor.Sequence r8 = r15.workSequence     // Catch:{ TimeoutException -> 0x005b, AlertException -> 0x0066, Throwable -> 0x0078 }
            long r8 = r8.get()     // Catch:{ TimeoutException -> 0x005b, AlertException -> 0x0066, Throwable -> 0x0078 }
            long r6 = r8 + r12
            com.lmax.disruptor.Sequence r8 = r15.sequence     // Catch:{ TimeoutException -> 0x005b, AlertException -> 0x0066, Throwable -> 0x0078 }
            long r10 = r6 - r12
            r8.set(r10)     // Catch:{ TimeoutException -> 0x005b, AlertException -> 0x0066, Throwable -> 0x0078 }
            com.lmax.disruptor.Sequence r8 = r15.workSequence     // Catch:{ TimeoutException -> 0x005b, AlertException -> 0x0066, Throwable -> 0x0078 }
            long r10 = r6 - r12
            boolean r8 = r8.compareAndSet(r10, r6)     // Catch:{ TimeoutException -> 0x005b, AlertException -> 0x0066, Throwable -> 0x0078 }
            if (r8 == 0) goto L_0x002a
        L_0x0043:
            int r8 = (r0 > r6 ? 1 : (r0 == r6 ? 0 : -1))
            if (r8 < 0) goto L_0x0054
            com.lmax.disruptor.RingBuffer<T> r8 = r15.ringBuffer     // Catch:{ TimeoutException -> 0x005b, AlertException -> 0x0066, Throwable -> 0x0078 }
            java.lang.Object r3 = r8.get(r6)     // Catch:{ TimeoutException -> 0x005b, AlertException -> 0x0066, Throwable -> 0x0078 }
            com.lmax.disruptor.WorkHandler<? super T> r8 = r15.workHandler     // Catch:{ TimeoutException -> 0x005b, AlertException -> 0x0066, Throwable -> 0x0078 }
            r8.onEvent(r3)     // Catch:{ TimeoutException -> 0x005b, AlertException -> 0x0066, Throwable -> 0x0078 }
            r5 = 1
            goto L_0x0027
        L_0x0054:
            com.lmax.disruptor.SequenceBarrier r8 = r15.sequenceBarrier     // Catch:{ TimeoutException -> 0x005b, AlertException -> 0x0066, Throwable -> 0x0078 }
            long r0 = r8.waitFor(r6)     // Catch:{ TimeoutException -> 0x005b, AlertException -> 0x0066, Throwable -> 0x0078 }
            goto L_0x0027
        L_0x005b:
            r2 = move-exception
            com.lmax.disruptor.Sequence r8 = r15.sequence
            long r8 = r8.get()
            r15.notifyTimeout(r8)
            goto L_0x0027
        L_0x0066:
            r4 = move-exception
            java.util.concurrent.atomic.AtomicBoolean r8 = r15.running
            boolean r8 = r8.get()
            if (r8 != 0) goto L_0x0027
            r15.notifyShutdown()
            java.util.concurrent.atomic.AtomicBoolean r8 = r15.running
            r8.set(r14)
            return
        L_0x0078:
            r4 = move-exception
            com.lmax.disruptor.ExceptionHandler<? super T> r8 = r15.exceptionHandler
            r8.handleEventException(r4, r6, r3)
            r5 = 1
            goto L_0x0027
        */
        throw new UnsupportedOperationException("Method not decompiled: com.lmax.disruptor.WorkProcessor.run():void");
    }

    private void notifyTimeout(long availableSequence) {
        try {
            if (this.timeoutHandler != null) {
                this.timeoutHandler.onTimeout(availableSequence);
            }
        } catch (Throwable e) {
            this.exceptionHandler.handleEventException(e, availableSequence, null);
        }
    }

    private void notifyStart() {
        if (this.workHandler instanceof LifecycleAware) {
            try {
                ((LifecycleAware) this.workHandler).onStart();
            } catch (Throwable ex) {
                this.exceptionHandler.handleOnStartException(ex);
            }
        }
    }

    private void notifyShutdown() {
        if (this.workHandler instanceof LifecycleAware) {
            try {
                ((LifecycleAware) this.workHandler).onShutdown();
            } catch (Throwable ex) {
                this.exceptionHandler.handleOnShutdownException(ex);
            }
        }
    }
}
