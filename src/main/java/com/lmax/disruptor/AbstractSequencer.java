package com.lmax.disruptor;

import com.lmax.disruptor.util.Util;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

public abstract class AbstractSequencer implements Sequencer {
    private static final AtomicReferenceFieldUpdater<AbstractSequencer, Sequence[]> SEQUENCE_UPDATER = AtomicReferenceFieldUpdater.newUpdater(AbstractSequencer.class, Sequence[].class, "gatingSequences");
    protected final int bufferSize;
    protected final Sequence cursor = new Sequence(-1);
    protected volatile Sequence[] gatingSequences = new Sequence[0];
    protected final WaitStrategy waitStrategy;

    public AbstractSequencer(int bufferSize2, WaitStrategy waitStrategy2) {
        if (bufferSize2 < 1) {
            throw new IllegalArgumentException("bufferSize must not be less than 1");
        } else if (Integer.bitCount(bufferSize2) != 1) {
            throw new IllegalArgumentException("bufferSize must be a power of 2");
        } else {
            this.bufferSize = bufferSize2;
            this.waitStrategy = waitStrategy2;
        }
    }

    public final long getCursor() {
        return this.cursor.get();
    }

    public final int getBufferSize() {
        return this.bufferSize;
    }

    public final void addGatingSequences(Sequence... gatingSequences2) {
        SequenceGroups.addSequences(this, SEQUENCE_UPDATER, this, gatingSequences2);
    }

    public boolean removeGatingSequence(Sequence sequence) {
        return SequenceGroups.removeSequence(this, SEQUENCE_UPDATER, sequence);
    }

    public long getMinimumSequence() {
        return Util.getMinimumSequence(this.gatingSequences, this.cursor.get());
    }

    public SequenceBarrier newBarrier(Sequence... sequencesToTrack) {
        return new ProcessingSequenceBarrier(this, this.waitStrategy, this.cursor, sequencesToTrack);
    }

    public <T> EventPoller<T> newPoller(DataProvider<T> dataProvider, Sequence... gatingSequences2) {
        return EventPoller.newInstance(dataProvider, this, new Sequence(), this.cursor, gatingSequences2);
    }

    public String toString() {
        return "AbstractSequencer{waitStrategy=" + this.waitStrategy + ", cursor=" + this.cursor + ", gatingSequences=" + Arrays.toString(this.gatingSequences) + '}';
    }
}
