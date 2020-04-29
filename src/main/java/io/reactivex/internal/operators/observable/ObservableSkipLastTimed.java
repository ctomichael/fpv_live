package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.queue.SpscLinkedArrayQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public final class ObservableSkipLastTimed<T> extends AbstractObservableWithUpstream<T, T> {
    final int bufferSize;
    final boolean delayError;
    final Scheduler scheduler;
    final long time;
    final TimeUnit unit;

    public ObservableSkipLastTimed(ObservableSource<T> source, long time2, TimeUnit unit2, Scheduler scheduler2, int bufferSize2, boolean delayError2) {
        super(source);
        this.time = time2;
        this.unit = unit2;
        this.scheduler = scheduler2;
        this.bufferSize = bufferSize2;
        this.delayError = delayError2;
    }

    public void subscribeActual(Observer<? super T> t) {
        this.source.subscribe(new SkipLastTimedObserver(t, this.time, this.unit, this.scheduler, this.bufferSize, this.delayError));
    }

    static final class SkipLastTimedObserver<T> extends AtomicInteger implements Observer<T>, Disposable {
        private static final long serialVersionUID = -5677354903406201275L;
        volatile boolean cancelled;
        final boolean delayError;
        volatile boolean done;
        final Observer<? super T> downstream;
        Throwable error;
        final SpscLinkedArrayQueue<Object> queue;
        final Scheduler scheduler;
        final long time;
        final TimeUnit unit;
        Disposable upstream;

        SkipLastTimedObserver(Observer<? super T> actual, long time2, TimeUnit unit2, Scheduler scheduler2, int bufferSize, boolean delayError2) {
            this.downstream = actual;
            this.time = time2;
            this.unit = unit2;
            this.scheduler = scheduler2;
            this.queue = new SpscLinkedArrayQueue<>(bufferSize);
            this.delayError = delayError2;
        }

        public void onSubscribe(Disposable d) {
            if (DisposableHelper.validate(this.upstream, d)) {
                this.upstream = d;
                this.downstream.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            this.queue.offer(Long.valueOf(this.scheduler.now(this.unit)), t);
            drain();
        }

        public void onError(Throwable t) {
            this.error = t;
            this.done = true;
            drain();
        }

        public void onComplete() {
            this.done = true;
            drain();
        }

        public void dispose() {
            if (!this.cancelled) {
                this.cancelled = true;
                this.upstream.dispose();
                if (getAndIncrement() == 0) {
                    this.queue.clear();
                }
            }
        }

        public boolean isDisposed() {
            return this.cancelled;
        }

        /* access modifiers changed from: package-private */
        public void drain() {
            if (getAndIncrement() == 0) {
                int missed = 1;
                Observer<? super T> a = this.downstream;
                SpscLinkedArrayQueue<Object> q = this.queue;
                boolean delayError2 = this.delayError;
                TimeUnit unit2 = this.unit;
                Scheduler scheduler2 = this.scheduler;
                long time2 = this.time;
                while (!this.cancelled) {
                    boolean d = this.done;
                    Long ts = (Long) q.peek();
                    boolean empty = ts == null;
                    long now = scheduler2.now(unit2);
                    if (!empty && ts.longValue() > now - time2) {
                        empty = true;
                    }
                    if (d) {
                        if (!delayError2) {
                            Throwable e = this.error;
                            if (e != null) {
                                this.queue.clear();
                                a.onError(e);
                                return;
                            } else if (empty) {
                                a.onComplete();
                                return;
                            }
                        } else if (empty) {
                            Throwable e2 = this.error;
                            if (e2 != null) {
                                a.onError(e2);
                                return;
                            } else {
                                a.onComplete();
                                return;
                            }
                        }
                    }
                    if (empty) {
                        missed = addAndGet(-missed);
                        if (missed == 0) {
                            return;
                        }
                    } else {
                        q.poll();
                        a.onNext(q.poll());
                    }
                }
                this.queue.clear();
            }
        }
    }
}
