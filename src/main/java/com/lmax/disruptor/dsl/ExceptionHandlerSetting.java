package com.lmax.disruptor.dsl;

import com.lmax.disruptor.BatchEventProcessor;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.ExceptionHandler;

public class ExceptionHandlerSetting<T> {
    private final ConsumerRepository<T> consumerRepository;
    private final EventHandler<T> eventHandler;

    ExceptionHandlerSetting(EventHandler<T> eventHandler2, ConsumerRepository<T> consumerRepository2) {
        this.eventHandler = eventHandler2;
        this.consumerRepository = consumerRepository2;
    }

    public void with(ExceptionHandler<? super T> exceptionHandler) {
        ((BatchEventProcessor) this.consumerRepository.getEventProcessorFor(this.eventHandler)).setExceptionHandler(exceptionHandler);
        this.consumerRepository.getBarrierFor(this.eventHandler).alert();
    }
}
