package com.mapbox.android.telemetry;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.util.Log;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;

class EventsQueue {
    private static final String LOG_TAG = "EventsQueue";
    @VisibleForTesting
    static final int SIZE_LIMIT = 180;
    /* access modifiers changed from: private */
    public final FullQueueCallback callback;
    private final ExecutorService executorService;
    private final ConcurrentQueue<Event> queue;

    @VisibleForTesting
    EventsQueue(@NonNull ConcurrentQueue<Event> queue2, @NonNull FullQueueCallback callback2, @NonNull ExecutorService executorService2) {
        this.queue = queue2;
        this.callback = callback2;
        this.executorService = executorService2;
    }

    static synchronized EventsQueue create(@NonNull FullQueueCallback callback2, @NonNull ExecutorService executorService2) {
        EventsQueue eventsQueue;
        synchronized (EventsQueue.class) {
            if (callback2 == null || executorService2 == null) {
                throw new IllegalArgumentException("Callback or executor can't be null");
            }
            eventsQueue = new EventsQueue(new ConcurrentQueue(), callback2, executorService2);
        }
        return eventsQueue;
    }

    /* access modifiers changed from: package-private */
    public boolean isEmpty() {
        return this.queue.size() == 0;
    }

    /* access modifiers changed from: package-private */
    public int size() {
        return this.queue.size();
    }

    /* access modifiers changed from: package-private */
    public boolean push(Event event) {
        boolean add;
        synchronized (this) {
            if (this.queue.size() >= 180) {
                dispatchCallback(this.queue.flush());
            }
            add = this.queue.add(event);
        }
        return add;
    }

    /* access modifiers changed from: package-private */
    public List<Event> flush() {
        List<Event> flush;
        synchronized (this) {
            flush = this.queue.flush();
        }
        return flush;
    }

    private void dispatchCallback(final List<Event> events) {
        try {
            this.executorService.execute(new Runnable() {
                /* class com.mapbox.android.telemetry.EventsQueue.AnonymousClass1 */

                public void run() {
                    try {
                        EventsQueue.this.callback.onFullQueue(events);
                    } catch (Throwable throwable) {
                        Log.e(EventsQueue.LOG_TAG, throwable.toString());
                    }
                }
            });
        } catch (RejectedExecutionException rex) {
            Log.e(LOG_TAG, rex.toString());
        }
    }
}
