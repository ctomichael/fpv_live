package com.lmax.disruptor;

public interface Sequencer extends Cursored, Sequenced {
    public static final long INITIAL_CURSOR_VALUE = -1;

    void addGatingSequences(Sequence... sequenceArr);

    void claim(long j);

    long getHighestPublishedSequence(long j, long j2);

    long getMinimumSequence();

    boolean isAvailable(long j);

    SequenceBarrier newBarrier(Sequence... sequenceArr);

    <T> EventPoller<T> newPoller(DataProvider<T> dataProvider, Sequence... sequenceArr);

    boolean removeGatingSequence(Sequence sequence);
}
