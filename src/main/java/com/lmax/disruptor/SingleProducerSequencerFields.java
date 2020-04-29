package com.lmax.disruptor;

/* compiled from: SingleProducerSequencer */
abstract class SingleProducerSequencerFields extends SingleProducerSequencerPad {
    long cachedValue = -1;
    long nextValue = -1;

    SingleProducerSequencerFields(int bufferSize, WaitStrategy waitStrategy) {
        super(bufferSize, waitStrategy);
    }
}
