package com.lmax.disruptor;

public final class AggregateEventHandler<T> implements EventHandler<T>, LifecycleAware {
    private final EventHandler<T>[] eventHandlers;

    public AggregateEventHandler(EventHandler<T>... eventHandlers2) {
        this.eventHandlers = eventHandlers2;
    }

    public void onEvent(T event, long sequence, boolean endOfBatch) throws Exception {
        for (EventHandler<T> eventHandler : this.eventHandlers) {
            eventHandler.onEvent(event, sequence, endOfBatch);
        }
    }

    public void onStart() {
        EventHandler<T>[] eventHandlerArr = this.eventHandlers;
        for (EventHandler<T> eventHandler : eventHandlerArr) {
            if (eventHandler instanceof LifecycleAware) {
                ((LifecycleAware) eventHandler).onStart();
            }
        }
    }

    public void onShutdown() {
        EventHandler<T>[] eventHandlerArr = this.eventHandlers;
        for (EventHandler<T> eventHandler : eventHandlerArr) {
            if (eventHandler instanceof LifecycleAware) {
                ((LifecycleAware) eventHandler).onShutdown();
            }
        }
    }
}
