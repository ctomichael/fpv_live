package com.lmax.disruptor.dsl;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.EventProcessor;
import com.lmax.disruptor.Sequence;
import com.lmax.disruptor.SequenceBarrier;
import com.lmax.disruptor.WorkerPool;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

class ConsumerRepository<T> implements Iterable<ConsumerInfo> {
    private final Collection<ConsumerInfo> consumerInfos = new ArrayList();
    private final Map<EventHandler<?>, EventProcessorInfo<T>> eventProcessorInfoByEventHandler = new IdentityHashMap();
    private final Map<Sequence, ConsumerInfo> eventProcessorInfoBySequence = new IdentityHashMap();

    ConsumerRepository() {
    }

    public void add(EventProcessor eventprocessor, EventHandler<? super T> handler, SequenceBarrier barrier) {
        EventProcessorInfo<T> consumerInfo = new EventProcessorInfo<>(eventprocessor, handler, barrier);
        this.eventProcessorInfoByEventHandler.put(handler, consumerInfo);
        this.eventProcessorInfoBySequence.put(eventprocessor.getSequence(), consumerInfo);
        this.consumerInfos.add(consumerInfo);
    }

    public void add(EventProcessor processor) {
        EventProcessorInfo<T> consumerInfo = new EventProcessorInfo<>(processor, null, null);
        this.eventProcessorInfoBySequence.put(processor.getSequence(), consumerInfo);
        this.consumerInfos.add(consumerInfo);
    }

    public void add(WorkerPool<T> workerPool, SequenceBarrier sequenceBarrier) {
        WorkerPoolInfo<T> workerPoolInfo = new WorkerPoolInfo<>(workerPool, sequenceBarrier);
        this.consumerInfos.add(workerPoolInfo);
        for (Sequence sequence : workerPool.getWorkerSequences()) {
            this.eventProcessorInfoBySequence.put(sequence, workerPoolInfo);
        }
    }

    public Sequence[] getLastSequenceInChain(boolean includeStopped) {
        List<Sequence> lastSequence = new ArrayList<>();
        for (ConsumerInfo consumerInfo : this.consumerInfos) {
            if ((includeStopped || consumerInfo.isRunning()) && consumerInfo.isEndOfChain()) {
                Collections.addAll(lastSequence, consumerInfo.getSequences());
            }
        }
        return (Sequence[]) lastSequence.toArray(new Sequence[lastSequence.size()]);
    }

    public EventProcessor getEventProcessorFor(EventHandler<T> handler) {
        EventProcessorInfo<T> eventprocessorInfo = getEventProcessorInfo(handler);
        if (eventprocessorInfo != null) {
            return eventprocessorInfo.getEventProcessor();
        }
        throw new IllegalArgumentException("The event handler " + handler + " is not processing events.");
    }

    public Sequence getSequenceFor(EventHandler<T> handler) {
        return getEventProcessorFor(handler).getSequence();
    }

    public void unMarkEventProcessorsAsEndOfChain(Sequence... barrierEventProcessors) {
        for (Sequence barrierEventProcessor : barrierEventProcessors) {
            getEventProcessorInfo(barrierEventProcessor).markAsUsedInBarrier();
        }
    }

    public Iterator<ConsumerInfo> iterator() {
        return this.consumerInfos.iterator();
    }

    public SequenceBarrier getBarrierFor(EventHandler<T> handler) {
        ConsumerInfo consumerInfo = getEventProcessorInfo(handler);
        if (consumerInfo != null) {
            return consumerInfo.getBarrier();
        }
        return null;
    }

    private EventProcessorInfo<T> getEventProcessorInfo(EventHandler<T> handler) {
        return this.eventProcessorInfoByEventHandler.get(handler);
    }

    private ConsumerInfo getEventProcessorInfo(Sequence barrierEventProcessor) {
        return this.eventProcessorInfoBySequence.get(barrierEventProcessor);
    }
}
