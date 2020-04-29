package com.lmax.disruptor;

public class EventPoller<T> {
    private final DataProvider<T> dataProvider;
    private final Sequence gatingSequence;
    private final Sequence sequence;
    private final Sequencer sequencer;

    public interface Handler<T> {
        boolean onEvent(T t, long j, boolean z) throws Exception;
    }

    public enum PollState {
        PROCESSING,
        GATING,
        IDLE
    }

    public EventPoller(DataProvider<T> dataProvider2, Sequencer sequencer2, Sequence sequence2, Sequence gatingSequence2) {
        this.dataProvider = dataProvider2;
        this.sequencer = sequencer2;
        this.sequence = sequence2;
        this.gatingSequence = gatingSequence2;
    }

    /* JADX INFO: finally extract failed */
    public PollState poll(Handler<T> eventHandler) throws Exception {
        boolean processNextEvent;
        long currentSequence = this.sequence.get();
        long nextSequence = currentSequence + 1;
        long availableSequence = this.sequencer.getHighestPublishedSequence(nextSequence, this.gatingSequence.get());
        if (nextSequence <= availableSequence) {
            long processedSequence = currentSequence;
            do {
                try {
                    processNextEvent = eventHandler.onEvent(this.dataProvider.get(nextSequence), nextSequence, nextSequence == availableSequence);
                    processedSequence = nextSequence;
                    nextSequence++;
                } catch (Throwable th) {
                    this.sequence.set(processedSequence);
                    throw th;
                }
            } while ((nextSequence <= availableSequence) & processNextEvent);
            this.sequence.set(processedSequence);
            return PollState.PROCESSING;
        } else if (this.sequencer.getCursor() >= nextSequence) {
            return PollState.GATING;
        } else {
            return PollState.IDLE;
        }
    }

    public static <T> EventPoller<T> newInstance(DataProvider<T> dataProvider2, Sequencer sequencer2, Sequence sequence2, Sequence cursorSequence, Sequence... gatingSequences) {
        Sequence gatingSequence2;
        if (gatingSequences.length == 0) {
            gatingSequence2 = cursorSequence;
        } else if (gatingSequences.length == 1) {
            gatingSequence2 = gatingSequences[0];
        } else {
            gatingSequence2 = new FixedSequenceGroup(gatingSequences);
        }
        return new EventPoller<>(dataProvider2, sequencer2, sequence2, gatingSequence2);
    }

    public Sequence getSequence() {
        return this.sequence;
    }
}
