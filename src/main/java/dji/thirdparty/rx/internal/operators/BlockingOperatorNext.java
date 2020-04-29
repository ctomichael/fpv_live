package dji.thirdparty.rx.internal.operators;

import dji.thirdparty.rx.Notification;
import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.Subscriber;
import dji.thirdparty.rx.exceptions.Exceptions;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public final class BlockingOperatorNext {
    private BlockingOperatorNext() {
        throw new IllegalStateException("No instances!");
    }

    public static <T> Iterable<T> next(final Observable<? extends T> items) {
        return new Iterable<T>() {
            /* class dji.thirdparty.rx.internal.operators.BlockingOperatorNext.AnonymousClass1 */

            public Iterator<T> iterator() {
                return new NextIterator(items, new NextObserver<>());
            }
        };
    }

    static final class NextIterator<T> implements Iterator<T> {
        private Throwable error = null;
        private boolean hasNext = true;
        private boolean isNextConsumed = true;
        private final Observable<? extends T> items;
        private T next;
        private final NextObserver<T> observer;
        private boolean started = false;

        NextIterator(Observable<? extends T> items2, NextObserver<T> observer2) {
            this.items = items2;
            this.observer = observer2;
        }

        public boolean hasNext() {
            if (this.error != null) {
                throw Exceptions.propagate(this.error);
            } else if (!this.hasNext) {
                return false;
            } else {
                if (!this.isNextConsumed) {
                    return true;
                }
                return moveToNext();
            }
        }

        private boolean moveToNext() {
            try {
                if (!this.started) {
                    this.started = true;
                    this.observer.setWaiting(1);
                    this.items.materialize().subscribe((Subscriber) this.observer);
                }
                Notification<? extends T> nextNotification = this.observer.takeNext();
                if (nextNotification.isOnNext()) {
                    this.isNextConsumed = false;
                    this.next = nextNotification.getValue();
                    return true;
                }
                this.hasNext = false;
                if (nextNotification.isOnCompleted()) {
                    return false;
                }
                if (nextNotification.isOnError()) {
                    this.error = nextNotification.getThrowable();
                    throw Exceptions.propagate(this.error);
                }
                throw new IllegalStateException("Should not reach here");
            } catch (InterruptedException e) {
                this.observer.unsubscribe();
                Thread.currentThread().interrupt();
                this.error = e;
                throw Exceptions.propagate(this.error);
            }
        }

        public T next() {
            if (this.error != null) {
                throw Exceptions.propagate(this.error);
            } else if (hasNext()) {
                this.isNextConsumed = true;
                return this.next;
            } else {
                throw new NoSuchElementException("No more elements");
            }
        }

        public void remove() {
            throw new UnsupportedOperationException("Read only iterator");
        }
    }

    private static class NextObserver<T> extends Subscriber<Notification<? extends T>> {
        private final BlockingQueue<Notification<? extends T>> buf = new ArrayBlockingQueue(1);
        final AtomicInteger waiting = new AtomicInteger();

        public /* bridge */ /* synthetic */ void onNext(Object x0) {
            onNext((Notification) ((Notification) x0));
        }

        NextObserver() {
        }

        public void onCompleted() {
        }

        public void onError(Throwable e) {
        }

        public void onNext(Notification<? extends T> args) {
            if (this.waiting.getAndSet(0) == 1 || !args.isOnNext()) {
                Notification<? extends T> toOffer = args;
                while (!this.buf.offer(toOffer)) {
                    Notification<? extends T> concurrentItem = this.buf.poll();
                    if (concurrentItem != null && !concurrentItem.isOnNext()) {
                        toOffer = concurrentItem;
                    }
                }
            }
        }

        public Notification<? extends T> takeNext() throws InterruptedException {
            setWaiting(1);
            return this.buf.take();
        }

        /* access modifiers changed from: package-private */
        public void setWaiting(int value) {
            this.waiting.set(value);
        }
    }
}
