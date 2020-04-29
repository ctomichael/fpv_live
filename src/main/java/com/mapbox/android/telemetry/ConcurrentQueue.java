package com.mapbox.android.telemetry;

import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

class ConcurrentQueue<T> {
    private static final String TAG = "ConcurrentQueue";
    private final Queue<T> queue = new ConcurrentLinkedQueue();

    ConcurrentQueue() {
    }

    /* access modifiers changed from: package-private */
    public boolean add(T event) {
        try {
            return this.queue.add(event);
        } catch (Exception exc) {
            Log.e(TAG, exc.toString());
            return false;
        }
    }

    /* access modifiers changed from: package-private */
    @Nullable
    public T remove() {
        return this.queue.remove();
    }

    /* access modifiers changed from: package-private */
    public List<T> flush() {
        List<T> queuedEvents = new ArrayList<>(this.queue.size());
        try {
            queuedEvents.addAll(this.queue);
            this.queue.clear();
        } catch (Exception exc) {
            Log.e(TAG, exc.toString());
        }
        return queuedEvents;
    }

    /* access modifiers changed from: package-private */
    public int size() {
        return this.queue.size();
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public Queue<T> obtainQueue() {
        return this.queue;
    }
}
