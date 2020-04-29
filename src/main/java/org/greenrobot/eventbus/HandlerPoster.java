package org.greenrobot.eventbus;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;

final class HandlerPoster extends Handler {
    private final EventBus eventBus;
    private boolean handlerActive;
    private final int maxMillisInsideHandleMessage;
    private final PendingPostQueue queue = new PendingPostQueue();

    HandlerPoster(EventBus eventBus2, Looper looper, int maxMillisInsideHandleMessage2) {
        super(looper);
        this.eventBus = eventBus2;
        this.maxMillisInsideHandleMessage = maxMillisInsideHandleMessage2;
    }

    /* access modifiers changed from: package-private */
    public void enqueue(Subscription subscription, Object event) {
        PendingPost pendingPost = PendingPost.obtainPendingPost(subscription, event);
        synchronized (this) {
            this.queue.enqueue(pendingPost);
            if (!this.handlerActive) {
                this.handlerActive = true;
                if (!sendMessage(obtainMessage())) {
                    throw new EventBusException("Could not send handler message");
                }
            }
        }
    }

    public void handleMessage(Message msg) {
        try {
            long started = SystemClock.uptimeMillis();
            do {
                PendingPost pendingPost = this.queue.poll();
                if (pendingPost == null) {
                    synchronized (this) {
                        pendingPost = this.queue.poll();
                        if (pendingPost == null) {
                            this.handlerActive = false;
                            this.handlerActive = false;
                            return;
                        }
                    }
                }
                this.eventBus.invokeSubscriber(pendingPost);
            } while (SystemClock.uptimeMillis() - started < ((long) this.maxMillisInsideHandleMessage));
            if (!sendMessage(obtainMessage())) {
                throw new EventBusException("Could not send handler message");
            }
            this.handlerActive = true;
        } catch (Throwable th) {
            this.handlerActive = false;
            throw th;
        }
    }
}
