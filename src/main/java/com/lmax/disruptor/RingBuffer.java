package com.lmax.disruptor;

import com.lmax.disruptor.dsl.ProducerType;

public final class RingBuffer<E> extends RingBufferFields<E> implements Cursored, EventSequencer<E>, EventSink<E> {
    public static final long INITIAL_CURSOR_VALUE = -1;
    protected long p1;
    protected long p2;
    protected long p3;
    protected long p4;
    protected long p5;
    protected long p6;
    protected long p7;

    RingBuffer(EventFactory<E> eventFactory, Sequencer sequencer) {
        super(eventFactory, sequencer);
    }

    public static <E> RingBuffer<E> createMultiProducer(EventFactory<E> factory, int bufferSize, WaitStrategy waitStrategy) {
        return new RingBuffer<>(factory, new MultiProducerSequencer(bufferSize, waitStrategy));
    }

    public static <E> RingBuffer<E> createMultiProducer(EventFactory<E> factory, int bufferSize) {
        return createMultiProducer(factory, bufferSize, new BlockingWaitStrategy());
    }

    public static <E> RingBuffer<E> createSingleProducer(EventFactory<E> factory, int bufferSize, WaitStrategy waitStrategy) {
        return new RingBuffer<>(factory, new SingleProducerSequencer(bufferSize, waitStrategy));
    }

    public static <E> RingBuffer<E> createSingleProducer(EventFactory<E> factory, int bufferSize) {
        return createSingleProducer(factory, bufferSize, new BlockingWaitStrategy());
    }

    public static <E> RingBuffer<E> create(ProducerType producerType, EventFactory<E> factory, int bufferSize, WaitStrategy waitStrategy) {
        switch (producerType) {
            case SINGLE:
                return createSingleProducer(factory, bufferSize, waitStrategy);
            case MULTI:
                return createMultiProducer(factory, bufferSize, waitStrategy);
            default:
                throw new IllegalStateException(producerType.toString());
        }
    }

    public E get(long sequence) {
        return elementAt(sequence);
    }

    public long next() {
        return this.sequencer.next();
    }

    public long next(int n) {
        return this.sequencer.next(n);
    }

    public long tryNext() throws InsufficientCapacityException {
        return this.sequencer.tryNext();
    }

    public long tryNext(int n) throws InsufficientCapacityException {
        return this.sequencer.tryNext(n);
    }

    @Deprecated
    public void resetTo(long sequence) {
        this.sequencer.claim(sequence);
        this.sequencer.publish(sequence);
    }

    public E claimAndGetPreallocated(long sequence) {
        this.sequencer.claim(sequence);
        return get(sequence);
    }

    @Deprecated
    public boolean isPublished(long sequence) {
        return this.sequencer.isAvailable(sequence);
    }

    public void addGatingSequences(Sequence... gatingSequences) {
        this.sequencer.addGatingSequences(gatingSequences);
    }

    public long getMinimumGatingSequence() {
        return this.sequencer.getMinimumSequence();
    }

    public boolean removeGatingSequence(Sequence sequence) {
        return this.sequencer.removeGatingSequence(sequence);
    }

    public SequenceBarrier newBarrier(Sequence... sequencesToTrack) {
        return this.sequencer.newBarrier(sequencesToTrack);
    }

    public EventPoller<E> newPoller(Sequence... gatingSequences) {
        return this.sequencer.newPoller(this, gatingSequences);
    }

    public long getCursor() {
        return this.sequencer.getCursor();
    }

    public int getBufferSize() {
        return this.bufferSize;
    }

    public boolean hasAvailableCapacity(int requiredCapacity) {
        return this.sequencer.hasAvailableCapacity(requiredCapacity);
    }

    public void publishEvent(EventTranslator<E> translator) {
        translateAndPublish(translator, this.sequencer.next());
    }

    public boolean tryPublishEvent(EventTranslator<E> translator) {
        try {
            translateAndPublish(translator, this.sequencer.tryNext());
            return true;
        } catch (InsufficientCapacityException e) {
            return false;
        }
    }

    public <A> void publishEvent(EventTranslatorOneArg<E, A> translator, A arg0) {
        translateAndPublish(translator, this.sequencer.next(), arg0);
    }

    public <A> boolean tryPublishEvent(EventTranslatorOneArg<E, A> translator, A arg0) {
        try {
            translateAndPublish(translator, this.sequencer.tryNext(), arg0);
            return true;
        } catch (InsufficientCapacityException e) {
            return false;
        }
    }

    public <A, B> void publishEvent(EventTranslatorTwoArg<E, A, B> translator, A arg0, B arg1) {
        translateAndPublish(translator, this.sequencer.next(), arg0, arg1);
    }

    public <A, B> boolean tryPublishEvent(EventTranslatorTwoArg<E, A, B> translator, A arg0, B arg1) {
        try {
            translateAndPublish(translator, this.sequencer.tryNext(), arg0, arg1);
            return true;
        } catch (InsufficientCapacityException e) {
            return false;
        }
    }

    public <A, B, C> void publishEvent(EventTranslatorThreeArg<E, A, B, C> translator, A arg0, B arg1, C arg2) {
        translateAndPublish(translator, this.sequencer.next(), arg0, arg1, arg2);
    }

    public <A, B, C> boolean tryPublishEvent(EventTranslatorThreeArg<E, A, B, C> translator, A arg0, B arg1, C arg2) {
        try {
            translateAndPublish(translator, this.sequencer.tryNext(), arg0, arg1, arg2);
            return true;
        } catch (InsufficientCapacityException e) {
            return false;
        }
    }

    public void publishEvent(EventTranslatorVararg<E> translator, Object... args) {
        translateAndPublish(translator, this.sequencer.next(), args);
    }

    public boolean tryPublishEvent(EventTranslatorVararg<E> translator, Object... args) {
        try {
            translateAndPublish(translator, this.sequencer.tryNext(), args);
            return true;
        } catch (InsufficientCapacityException e) {
            return false;
        }
    }

    public void publishEvents(EventTranslator<E>[] translators) {
        publishEvents(translators, 0, translators.length);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.lmax.disruptor.RingBuffer.checkBounds(com.lmax.disruptor.EventTranslator[], int, int):void
     arg types: [com.lmax.disruptor.EventTranslator<E>[], int, int]
     candidates:
      com.lmax.disruptor.RingBuffer.checkBounds(int, int, java.lang.Object[][]):void
      com.lmax.disruptor.RingBuffer.checkBounds(java.lang.Object[], int, int):void
      com.lmax.disruptor.RingBuffer.checkBounds(com.lmax.disruptor.EventTranslator[], int, int):void */
    public void publishEvents(EventTranslator<E>[] translators, int batchStartsAt, int batchSize) {
        checkBounds((EventTranslator[]) translators, batchStartsAt, batchSize);
        translateAndPublishBatch(translators, batchStartsAt, batchSize, this.sequencer.next(batchSize));
    }

    public boolean tryPublishEvents(EventTranslator<E>[] translators) {
        return tryPublishEvents(translators, 0, translators.length);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.lmax.disruptor.RingBuffer.checkBounds(com.lmax.disruptor.EventTranslator[], int, int):void
     arg types: [com.lmax.disruptor.EventTranslator<E>[], int, int]
     candidates:
      com.lmax.disruptor.RingBuffer.checkBounds(int, int, java.lang.Object[][]):void
      com.lmax.disruptor.RingBuffer.checkBounds(java.lang.Object[], int, int):void
      com.lmax.disruptor.RingBuffer.checkBounds(com.lmax.disruptor.EventTranslator[], int, int):void */
    public boolean tryPublishEvents(EventTranslator<E>[] translators, int batchStartsAt, int batchSize) {
        checkBounds((EventTranslator[]) translators, batchStartsAt, batchSize);
        try {
            translateAndPublishBatch(translators, batchStartsAt, batchSize, this.sequencer.tryNext(batchSize));
            return true;
        } catch (InsufficientCapacityException e) {
            return false;
        }
    }

    public <A> void publishEvents(EventTranslatorOneArg<E, A> translator, A[] arg0) {
        publishEvents(translator, 0, arg0.length, arg0);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.lmax.disruptor.RingBuffer.checkBounds(java.lang.Object[], int, int):void
     arg types: [A[], int, int]
     candidates:
      com.lmax.disruptor.RingBuffer.checkBounds(int, int, java.lang.Object[][]):void
      com.lmax.disruptor.RingBuffer.checkBounds(com.lmax.disruptor.EventTranslator[], int, int):void
      com.lmax.disruptor.RingBuffer.checkBounds(java.lang.Object[], int, int):void */
    public <A> void publishEvents(EventTranslatorOneArg<E, A> translator, int batchStartsAt, int batchSize, A[] arg0) {
        checkBounds((Object[]) arg0, batchStartsAt, batchSize);
        translateAndPublishBatch(translator, arg0, batchStartsAt, batchSize, this.sequencer.next(batchSize));
    }

    public <A> boolean tryPublishEvents(EventTranslatorOneArg<E, A> translator, A[] arg0) {
        return tryPublishEvents(translator, 0, arg0.length, arg0);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.lmax.disruptor.RingBuffer.checkBounds(java.lang.Object[], int, int):void
     arg types: [A[], int, int]
     candidates:
      com.lmax.disruptor.RingBuffer.checkBounds(int, int, java.lang.Object[][]):void
      com.lmax.disruptor.RingBuffer.checkBounds(com.lmax.disruptor.EventTranslator[], int, int):void
      com.lmax.disruptor.RingBuffer.checkBounds(java.lang.Object[], int, int):void */
    public <A> boolean tryPublishEvents(EventTranslatorOneArg<E, A> translator, int batchStartsAt, int batchSize, A[] arg0) {
        checkBounds((Object[]) arg0, batchStartsAt, batchSize);
        try {
            translateAndPublishBatch(translator, arg0, batchStartsAt, batchSize, this.sequencer.tryNext(batchSize));
            return true;
        } catch (InsufficientCapacityException e) {
            return false;
        }
    }

    public <A, B> void publishEvents(EventTranslatorTwoArg<E, A, B> translator, A[] arg0, B[] arg1) {
        publishEvents(translator, 0, arg0.length, arg0, arg1);
    }

    public <A, B> void publishEvents(EventTranslatorTwoArg<E, A, B> translator, int batchStartsAt, int batchSize, A[] arg0, B[] arg1) {
        checkBounds(arg0, arg1, batchStartsAt, batchSize);
        translateAndPublishBatch(translator, arg0, arg1, batchStartsAt, batchSize, this.sequencer.next(batchSize));
    }

    public <A, B> boolean tryPublishEvents(EventTranslatorTwoArg<E, A, B> translator, A[] arg0, B[] arg1) {
        return tryPublishEvents(translator, 0, arg0.length, arg0, arg1);
    }

    public <A, B> boolean tryPublishEvents(EventTranslatorTwoArg<E, A, B> translator, int batchStartsAt, int batchSize, A[] arg0, B[] arg1) {
        checkBounds(arg0, arg1, batchStartsAt, batchSize);
        try {
            translateAndPublishBatch(translator, arg0, arg1, batchStartsAt, batchSize, this.sequencer.tryNext(batchSize));
            return true;
        } catch (InsufficientCapacityException e) {
            return false;
        }
    }

    public <A, B, C> void publishEvents(EventTranslatorThreeArg<E, A, B, C> translator, A[] arg0, B[] arg1, C[] arg2) {
        publishEvents(translator, 0, arg0.length, arg0, arg1, arg2);
    }

    public <A, B, C> void publishEvents(EventTranslatorThreeArg<E, A, B, C> translator, int batchStartsAt, int batchSize, A[] arg0, B[] arg1, C[] arg2) {
        checkBounds(arg0, arg1, arg2, batchStartsAt, batchSize);
        translateAndPublishBatch(translator, arg0, arg1, arg2, batchStartsAt, batchSize, this.sequencer.next(batchSize));
    }

    public <A, B, C> boolean tryPublishEvents(EventTranslatorThreeArg<E, A, B, C> translator, A[] arg0, B[] arg1, C[] arg2) {
        return tryPublishEvents(translator, 0, arg0.length, arg0, arg1, arg2);
    }

    public <A, B, C> boolean tryPublishEvents(EventTranslatorThreeArg<E, A, B, C> translator, int batchStartsAt, int batchSize, A[] arg0, B[] arg1, C[] arg2) {
        checkBounds(arg0, arg1, arg2, batchStartsAt, batchSize);
        try {
            translateAndPublishBatch(translator, arg0, arg1, arg2, batchStartsAt, batchSize, this.sequencer.tryNext(batchSize));
            return true;
        } catch (InsufficientCapacityException e) {
            return false;
        }
    }

    public void publishEvents(EventTranslatorVararg<E> translator, Object[]... args) {
        publishEvents(translator, 0, args.length, args);
    }

    public void publishEvents(EventTranslatorVararg<E> translator, int batchStartsAt, int batchSize, Object[]... args) {
        checkBounds(batchStartsAt, batchSize, args);
        translateAndPublishBatch(translator, batchStartsAt, batchSize, this.sequencer.next(batchSize), args);
    }

    public boolean tryPublishEvents(EventTranslatorVararg<E> translator, Object[]... args) {
        return tryPublishEvents(translator, 0, args.length, args);
    }

    public boolean tryPublishEvents(EventTranslatorVararg<E> translator, int batchStartsAt, int batchSize, Object[]... args) {
        checkBounds(args, batchStartsAt, batchSize);
        try {
            translateAndPublishBatch(translator, batchStartsAt, batchSize, this.sequencer.tryNext(batchSize), args);
            return true;
        } catch (InsufficientCapacityException e) {
            return false;
        }
    }

    public void publish(long sequence) {
        this.sequencer.publish(sequence);
    }

    public void publish(long lo, long hi) {
        this.sequencer.publish(lo, hi);
    }

    public long remainingCapacity() {
        return this.sequencer.remainingCapacity();
    }

    private void checkBounds(EventTranslator<E>[] translators, int batchStartsAt, int batchSize) {
        checkBatchSizing(batchStartsAt, batchSize);
        batchOverRuns(translators, batchStartsAt, batchSize);
    }

    private void checkBatchSizing(int batchStartsAt, int batchSize) {
        if (batchStartsAt < 0 || batchSize < 0) {
            throw new IllegalArgumentException("Both batchStartsAt and batchSize must be positive but got: batchStartsAt " + batchStartsAt + " and batchSize " + batchSize);
        } else if (batchSize > this.bufferSize) {
            throw new IllegalArgumentException("The ring buffer cannot accommodate " + batchSize + " it only has space for " + this.bufferSize + " entities.");
        }
    }

    private <A> void checkBounds(A[] arg0, int batchStartsAt, int batchSize) {
        checkBatchSizing(batchStartsAt, batchSize);
        batchOverRuns(arg0, batchStartsAt, batchSize);
    }

    private <A, B> void checkBounds(A[] arg0, B[] arg1, int batchStartsAt, int batchSize) {
        checkBatchSizing(batchStartsAt, batchSize);
        batchOverRuns(arg0, batchStartsAt, batchSize);
        batchOverRuns(arg1, batchStartsAt, batchSize);
    }

    private <A, B, C> void checkBounds(A[] arg0, B[] arg1, C[] arg2, int batchStartsAt, int batchSize) {
        checkBatchSizing(batchStartsAt, batchSize);
        batchOverRuns(arg0, batchStartsAt, batchSize);
        batchOverRuns(arg1, batchStartsAt, batchSize);
        batchOverRuns(arg2, batchStartsAt, batchSize);
    }

    private void checkBounds(int batchStartsAt, int batchSize, Object[][] args) {
        checkBatchSizing(batchStartsAt, batchSize);
        batchOverRuns(args, batchStartsAt, batchSize);
    }

    private <A> void batchOverRuns(A[] arg0, int batchStartsAt, int batchSize) {
        if (batchStartsAt + batchSize > arg0.length) {
            throw new IllegalArgumentException("A batchSize of: " + batchSize + " with batchStatsAt of: " + batchStartsAt + " will overrun the available number of arguments: " + (arg0.length - batchStartsAt));
        }
    }

    private void translateAndPublish(EventTranslator<E> translator, long sequence) {
        try {
            translator.translateTo(get(sequence), sequence);
        } finally {
            this.sequencer.publish(sequence);
        }
    }

    private <A> void translateAndPublish(EventTranslatorOneArg<E, A> translator, long sequence, A arg0) {
        try {
            translator.translateTo(get(sequence), sequence, arg0);
        } finally {
            this.sequencer.publish(sequence);
        }
    }

    private <A, B> void translateAndPublish(EventTranslatorTwoArg<E, A, B> translator, long sequence, A arg0, B arg1) {
        try {
            translator.translateTo(get(sequence), sequence, arg0, arg1);
        } finally {
            this.sequencer.publish(sequence);
        }
    }

    private <A, B, C> void translateAndPublish(EventTranslatorThreeArg<E, A, B, C> translator, long sequence, A arg0, B arg1, C arg2) {
        try {
            translator.translateTo(get(sequence), sequence, arg0, arg1, arg2);
        } finally {
            this.sequencer.publish(sequence);
        }
    }

    private void translateAndPublish(EventTranslatorVararg<E> translator, long sequence, Object... args) {
        try {
            translator.translateTo(get(sequence), sequence, args);
        } finally {
            this.sequencer.publish(sequence);
        }
    }

    private void translateAndPublishBatch(EventTranslator<E>[] translators, int batchStartsAt, int batchSize, long finalSequence) {
        long initialSequence = finalSequence - ((long) (batchSize - 1));
        int batchEndsAt = batchStartsAt + batchSize;
        int i = batchStartsAt;
        long sequence = initialSequence;
        while (i < batchEndsAt) {
            try {
                long sequence2 = sequence + 1;
                try {
                    translators[i].translateTo(get(sequence), sequence);
                    i++;
                    sequence = sequence2;
                } catch (Throwable th) {
                    th = th;
                    this.sequencer.publish(initialSequence, finalSequence);
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                this.sequencer.publish(initialSequence, finalSequence);
                throw th;
            }
        }
        this.sequencer.publish(initialSequence, finalSequence);
    }

    private <A> void translateAndPublishBatch(EventTranslatorOneArg<E, A> translator, A[] arg0, int batchStartsAt, int batchSize, long finalSequence) {
        long initialSequence = finalSequence - ((long) (batchSize - 1));
        int batchEndsAt = batchStartsAt + batchSize;
        int i = batchStartsAt;
        long sequence = initialSequence;
        while (i < batchEndsAt) {
            try {
                long sequence2 = sequence + 1;
                try {
                    translator.translateTo(get(sequence), sequence, arg0[i]);
                    i++;
                    sequence = sequence2;
                } catch (Throwable th) {
                    th = th;
                    this.sequencer.publish(initialSequence, finalSequence);
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                this.sequencer.publish(initialSequence, finalSequence);
                throw th;
            }
        }
        this.sequencer.publish(initialSequence, finalSequence);
    }

    private <A, B> void translateAndPublishBatch(EventTranslatorTwoArg<E, A, B> translator, A[] arg0, B[] arg1, int batchStartsAt, int batchSize, long finalSequence) {
        long initialSequence = finalSequence - ((long) (batchSize - 1));
        long sequence = initialSequence;
        int batchEndsAt = batchStartsAt + batchSize;
        int i = batchStartsAt;
        while (i < batchEndsAt) {
            try {
                long sequence2 = sequence + 1;
                try {
                    translator.translateTo(get(sequence), sequence, arg0[i], arg1[i]);
                    i++;
                    sequence = sequence2;
                } catch (Throwable th) {
                    th = th;
                    this.sequencer.publish(initialSequence, finalSequence);
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                this.sequencer.publish(initialSequence, finalSequence);
                throw th;
            }
        }
        this.sequencer.publish(initialSequence, finalSequence);
    }

    private <A, B, C> void translateAndPublishBatch(EventTranslatorThreeArg<E, A, B, C> translator, A[] arg0, B[] arg1, C[] arg2, int batchStartsAt, int batchSize, long finalSequence) {
        long initialSequence = finalSequence - ((long) (batchSize - 1));
        long sequence = initialSequence;
        int batchEndsAt = batchStartsAt + batchSize;
        int i = batchStartsAt;
        while (i < batchEndsAt) {
            try {
                long sequence2 = sequence + 1;
                try {
                    translator.translateTo(get(sequence), sequence, arg0[i], arg1[i], arg2[i]);
                    i++;
                    sequence = sequence2;
                } catch (Throwable th) {
                    th = th;
                    this.sequencer.publish(initialSequence, finalSequence);
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                this.sequencer.publish(initialSequence, finalSequence);
                throw th;
            }
        }
        this.sequencer.publish(initialSequence, finalSequence);
    }

    private void translateAndPublishBatch(EventTranslatorVararg<E> translator, int batchStartsAt, int batchSize, long finalSequence, Object[][] args) {
        long initialSequence = finalSequence - ((long) (batchSize - 1));
        int batchEndsAt = batchStartsAt + batchSize;
        int i = batchStartsAt;
        long sequence = initialSequence;
        while (i < batchEndsAt) {
            try {
                long sequence2 = sequence + 1;
                try {
                    translator.translateTo(get(sequence), sequence, args[i]);
                    i++;
                    sequence = sequence2;
                } catch (Throwable th) {
                    th = th;
                    this.sequencer.publish(initialSequence, finalSequence);
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                this.sequencer.publish(initialSequence, finalSequence);
                throw th;
            }
        }
        this.sequencer.publish(initialSequence, finalSequence);
    }

    public String toString() {
        return "RingBuffer{bufferSize=" + this.bufferSize + ", sequencer=" + this.sequencer + "}";
    }
}
