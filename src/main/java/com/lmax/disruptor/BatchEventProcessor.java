package com.lmax.disruptor;

import java.util.concurrent.atomic.AtomicBoolean;

public final class BatchEventProcessor<T> implements EventProcessor {
    private final BatchStartAware batchStartAware;
    private final DataProvider<T> dataProvider;
    private final EventHandler<? super T> eventHandler;
    private ExceptionHandler<? super T> exceptionHandler = new FatalExceptionHandler();
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final Sequence sequence = new Sequence(-1);
    private final SequenceBarrier sequenceBarrier;
    private final TimeoutHandler timeoutHandler;

    public BatchEventProcessor(DataProvider<T> dataProvider2, SequenceBarrier sequenceBarrier2, EventHandler<? super T> eventHandler2) {
        BatchStartAware batchStartAware2;
        TimeoutHandler timeoutHandler2;
        this.dataProvider = dataProvider2;
        this.sequenceBarrier = sequenceBarrier2;
        this.eventHandler = eventHandler2;
        if (eventHandler2 instanceof SequenceReportingEventHandler) {
            ((SequenceReportingEventHandler) eventHandler2).setSequenceCallback(this.sequence);
        }
        if (eventHandler2 instanceof BatchStartAware) {
            batchStartAware2 = (BatchStartAware) eventHandler2;
        } else {
            batchStartAware2 = null;
        }
        this.batchStartAware = batchStartAware2;
        if (eventHandler2 instanceof TimeoutHandler) {
            timeoutHandler2 = (TimeoutHandler) eventHandler2;
        } else {
            timeoutHandler2 = null;
        }
        this.timeoutHandler = timeoutHandler2;
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

    public void setExceptionHandler(ExceptionHandler<? super T> exceptionHandler2) {
        if (exceptionHandler2 == null) {
            throw new NullPointerException();
        }
        this.exceptionHandler = exceptionHandler2;
    }

    public void run() {
        boolean z;
        if (!this.running.compareAndSet(false, true)) {
            throw new IllegalStateException("Thread is already running");
        }
        this.sequenceBarrier.clearAlert();
        notifyStart();
        T event = null;
        long nextSequence = this.sequence.get() + 1;
        while (true) {
            try {
                long availableSequence = this.sequenceBarrier.waitFor(nextSequence);
                if (this.batchStartAware != null) {
                    this.batchStartAware.onBatchStart((availableSequence - nextSequence) + 1);
                }
                while (nextSequence <= availableSequence) {
                    event = this.dataProvider.get(nextSequence);
                    EventHandler<? super T> eventHandler2 = this.eventHandler;
                    if (nextSequence == availableSequence) {
                        z = true;
                    } else {
                        z = false;
                    }
                    eventHandler2.onEvent(event, nextSequence, z);
                    nextSequence++;
                }
                this.sequence.set(availableSequence);
            } catch (TimeoutException e) {
                notifyTimeout(this.sequence.get());
            } catch (AlertException e2) {
                if (!this.running.get()) {
                    notifyShutdown();
                    this.running.set(false);
                    return;
                }
            } catch (Throwable th) {
                notifyShutdown();
                this.running.set(false);
                throw th;
            }
        }
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
        if (this.eventHandler instanceof LifecycleAware) {
            try {
                ((LifecycleAware) this.eventHandler).onStart();
            } catch (Throwable ex) {
                this.exceptionHandler.handleOnStartException(ex);
            }
        }
    }

    private void notifyShutdown() {
        if (this.eventHandler instanceof LifecycleAware) {
            try {
                ((LifecycleAware) this.eventHandler).onShutdown();
            } catch (Throwable ex) {
                this.exceptionHandler.handleOnShutdownException(ex);
            }
        }
    }
}
