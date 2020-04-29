package com.lmax.disruptor;

/* compiled from: SingleProducerSequencer */
abstract class SingleProducerSequencerPad extends AbstractSequencer {
    protected long p1;
    protected long p2;
    protected long p3;
    protected long p4;
    protected long p5;
    protected long p6;
    protected long p7;

    SingleProducerSequencerPad(int bufferSize, WaitStrategy waitStrategy) {
        super(bufferSize, waitStrategy);
    }
}
