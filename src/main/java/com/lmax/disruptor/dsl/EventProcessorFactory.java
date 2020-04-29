package com.lmax.disruptor.dsl;

import com.lmax.disruptor.EventProcessor;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.Sequence;

public interface EventProcessorFactory<T> {
    EventProcessor createEventProcessor(RingBuffer<T> ringBuffer, Sequence[] sequenceArr);
}
