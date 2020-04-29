package com.lmax.disruptor;

final class ProcessingSequenceBarrier implements SequenceBarrier {
    private volatile boolean alerted = false;
    private final Sequence cursorSequence;
    private final Sequence dependentSequence;
    private final Sequencer sequencer;
    private final WaitStrategy waitStrategy;

    ProcessingSequenceBarrier(Sequencer sequencer2, WaitStrategy waitStrategy2, Sequence cursorSequence2, Sequence[] dependentSequences) {
        this.sequencer = sequencer2;
        this.waitStrategy = waitStrategy2;
        this.cursorSequence = cursorSequence2;
        if (dependentSequences.length == 0) {
            this.dependentSequence = cursorSequence2;
        } else {
            this.dependentSequence = new FixedSequenceGroup(dependentSequences);
        }
    }

    public long waitFor(long sequence) throws AlertException, InterruptedException, TimeoutException {
        checkAlert();
        long availableSequence = this.waitStrategy.waitFor(sequence, this.cursorSequence, this.dependentSequence, this);
        return availableSequence < sequence ? availableSequence : this.sequencer.getHighestPublishedSequence(sequence, availableSequence);
    }

    public long getCursor() {
        return this.dependentSequence.get();
    }

    public boolean isAlerted() {
        return this.alerted;
    }

    public void alert() {
        this.alerted = true;
        this.waitStrategy.signalAllWhenBlocking();
    }

    public void clearAlert() {
        this.alerted = false;
    }

    public void checkAlert() throws AlertException {
        if (this.alerted) {
            throw AlertException.INSTANCE;
        }
    }
}
