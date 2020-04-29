package dji.thirdparty.io.reactivex.internal.operators.completable;

import dji.thirdparty.io.reactivex.Completable;
import dji.thirdparty.io.reactivex.CompletableObserver;
import dji.thirdparty.io.reactivex.CompletableSource;
import dji.thirdparty.io.reactivex.Flowable;
import dji.thirdparty.io.reactivex.disposables.Disposable;
import dji.thirdparty.io.reactivex.exceptions.Exceptions;
import dji.thirdparty.io.reactivex.exceptions.MissingBackpressureException;
import dji.thirdparty.io.reactivex.internal.disposables.DisposableHelper;
import dji.thirdparty.io.reactivex.internal.fuseable.QueueSubscription;
import dji.thirdparty.io.reactivex.internal.fuseable.SimpleQueue;
import dji.thirdparty.io.reactivex.internal.queue.SpscArrayQueue;
import dji.thirdparty.io.reactivex.internal.queue.SpscLinkedArrayQueue;
import dji.thirdparty.io.reactivex.internal.subscriptions.SubscriptionHelper;
import dji.thirdparty.io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class CompletableConcat extends Completable {
    final int prefetch;
    final Publisher<? extends CompletableSource> sources;

    public CompletableConcat(Publisher<? extends CompletableSource> sources2, int prefetch2) {
        this.sources = sources2;
        this.prefetch = prefetch2;
    }

    public void subscribeActual(CompletableObserver s) {
        this.sources.subscribe(new CompletableConcatSubscriber(s, this.prefetch));
    }

    static final class CompletableConcatSubscriber extends AtomicInteger implements Subscriber<CompletableSource>, Disposable {
        private static final long serialVersionUID = 9032184911934499404L;
        volatile boolean active;
        final CompletableObserver actual;
        int consumed;
        volatile boolean done;
        final ConcatInnerObserver inner = new ConcatInnerObserver(this);
        final int limit;
        final AtomicBoolean once = new AtomicBoolean();
        final int prefetch;
        SimpleQueue<CompletableSource> queue;
        Subscription s;
        int sourceFused;

        CompletableConcatSubscriber(CompletableObserver actual2, int prefetch2) {
            this.actual = actual2;
            this.prefetch = prefetch2;
            this.limit = prefetch2 - (prefetch2 >> 2);
        }

        public void onSubscribe(Subscription s2) {
            long r;
            if (SubscriptionHelper.validate(this.s, s2)) {
                this.s = s2;
                if (this.prefetch == Integer.MAX_VALUE) {
                    r = LongCompanionObject.MAX_VALUE;
                } else {
                    r = (long) this.prefetch;
                }
                if (s2 instanceof QueueSubscription) {
                    QueueSubscription<CompletableSource> qs = (QueueSubscription) s2;
                    int m = qs.requestFusion(3);
                    if (m == 1) {
                        this.sourceFused = m;
                        this.queue = qs;
                        this.done = true;
                        this.actual.onSubscribe(this);
                        drain();
                        return;
                    } else if (m == 2) {
                        this.sourceFused = m;
                        this.queue = qs;
                        this.actual.onSubscribe(this);
                        s2.request(r);
                        return;
                    }
                }
                if (this.prefetch == Integer.MAX_VALUE) {
                    this.queue = new SpscLinkedArrayQueue(Flowable.bufferSize());
                } else {
                    this.queue = new SpscArrayQueue(this.prefetch);
                }
                this.actual.onSubscribe(this);
                s2.request(r);
            }
        }

        public void onNext(CompletableSource t) {
            if (this.sourceFused != 0 || this.queue.offer(t)) {
                drain();
            } else {
                onError(new MissingBackpressureException());
            }
        }

        public void onError(Throwable t) {
            if (this.once.compareAndSet(false, true)) {
                DisposableHelper.dispose(this.inner);
                this.actual.onError(t);
                return;
            }
            RxJavaPlugins.onError(t);
        }

        public void onComplete() {
            this.done = true;
            drain();
        }

        public void dispose() {
            this.s.cancel();
            DisposableHelper.dispose(this.inner);
        }

        public boolean isDisposed() {
            return DisposableHelper.isDisposed((Disposable) this.inner.get());
        }

        /* access modifiers changed from: package-private */
        public void drain() {
            boolean empty;
            if (getAndIncrement() == 0) {
                while (!isDisposed()) {
                    if (!this.active) {
                        boolean d = this.done;
                        try {
                            CompletableSource cs = this.queue.poll();
                            if (cs == null) {
                                empty = true;
                            } else {
                                empty = false;
                            }
                            if (!d || !empty) {
                                if (!empty) {
                                    this.active = true;
                                    cs.subscribe(this.inner);
                                    request();
                                }
                            } else if (this.once.compareAndSet(false, true)) {
                                this.actual.onComplete();
                                return;
                            } else {
                                return;
                            }
                        } catch (Throwable ex) {
                            Exceptions.throwIfFatal(ex);
                            innerError(ex);
                            return;
                        }
                    }
                    if (decrementAndGet() == 0) {
                        return;
                    }
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void request() {
            if (this.sourceFused != 1) {
                int p = this.consumed + 1;
                if (p == this.limit) {
                    this.consumed = 0;
                    this.s.request((long) p);
                    return;
                }
                this.consumed = p;
            }
        }

        /* access modifiers changed from: package-private */
        public void innerError(Throwable e) {
            if (this.once.compareAndSet(false, true)) {
                this.s.cancel();
                this.actual.onError(e);
                return;
            }
            RxJavaPlugins.onError(e);
        }

        /* access modifiers changed from: package-private */
        public void innerComplete() {
            this.active = false;
            drain();
        }

        static final class ConcatInnerObserver extends AtomicReference<Disposable> implements CompletableObserver {
            private static final long serialVersionUID = -5454794857847146511L;
            final CompletableConcatSubscriber parent;

            ConcatInnerObserver(CompletableConcatSubscriber parent2) {
                this.parent = parent2;
            }

            public void onSubscribe(Disposable d) {
                DisposableHelper.replace(this, d);
            }

            public void onError(Throwable e) {
                this.parent.innerError(e);
            }

            public void onComplete() {
                this.parent.innerComplete();
            }
        }
    }
}
