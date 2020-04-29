package io.reactivex.internal.operators.completable;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.FlowableSubscriber;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.AtomicThrowable;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;

public final class CompletableMerge extends Completable {
    final boolean delayErrors;
    final int maxConcurrency;
    final Publisher<? extends CompletableSource> source;

    public CompletableMerge(Publisher<? extends CompletableSource> source2, int maxConcurrency2, boolean delayErrors2) {
        this.source = source2;
        this.maxConcurrency = maxConcurrency2;
        this.delayErrors = delayErrors2;
    }

    public void subscribeActual(CompletableObserver observer) {
        this.source.subscribe(new CompletableMergeSubscriber(observer, this.maxConcurrency, this.delayErrors));
    }

    static final class CompletableMergeSubscriber extends AtomicInteger implements FlowableSubscriber<CompletableSource>, Disposable {
        private static final long serialVersionUID = -2108443387387077490L;
        final boolean delayErrors;
        final CompletableObserver downstream;
        final AtomicThrowable error = new AtomicThrowable();
        final int maxConcurrency;
        final CompositeDisposable set = new CompositeDisposable();
        Subscription upstream;

        CompletableMergeSubscriber(CompletableObserver actual, int maxConcurrency2, boolean delayErrors2) {
            this.downstream = actual;
            this.maxConcurrency = maxConcurrency2;
            this.delayErrors = delayErrors2;
            lazySet(1);
        }

        public void dispose() {
            this.upstream.cancel();
            this.set.dispose();
        }

        public boolean isDisposed() {
            return this.set.isDisposed();
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(this.upstream, s)) {
                this.upstream = s;
                this.downstream.onSubscribe(this);
                if (this.maxConcurrency == Integer.MAX_VALUE) {
                    s.request(LongCompanionObject.MAX_VALUE);
                } else {
                    s.request((long) this.maxConcurrency);
                }
            }
        }

        public void onNext(CompletableSource t) {
            getAndIncrement();
            MergeInnerObserver inner = new MergeInnerObserver();
            this.set.add(inner);
            t.subscribe(inner);
        }

        public void onError(Throwable t) {
            if (!this.delayErrors) {
                this.set.dispose();
                if (!this.error.addThrowable(t)) {
                    RxJavaPlugins.onError(t);
                } else if (getAndSet(0) > 0) {
                    this.downstream.onError(this.error.terminate());
                }
            } else if (!this.error.addThrowable(t)) {
                RxJavaPlugins.onError(t);
            } else if (decrementAndGet() == 0) {
                this.downstream.onError(this.error.terminate());
            }
        }

        public void onComplete() {
            if (decrementAndGet() != 0) {
                return;
            }
            if (((Throwable) this.error.get()) != null) {
                this.downstream.onError(this.error.terminate());
            } else {
                this.downstream.onComplete();
            }
        }

        /* access modifiers changed from: package-private */
        public void innerError(MergeInnerObserver inner, Throwable t) {
            this.set.delete(inner);
            if (!this.delayErrors) {
                this.upstream.cancel();
                this.set.dispose();
                if (!this.error.addThrowable(t)) {
                    RxJavaPlugins.onError(t);
                } else if (getAndSet(0) > 0) {
                    this.downstream.onError(this.error.terminate());
                }
            } else if (!this.error.addThrowable(t)) {
                RxJavaPlugins.onError(t);
            } else if (decrementAndGet() == 0) {
                this.downstream.onError(this.error.terminate());
            } else if (this.maxConcurrency != Integer.MAX_VALUE) {
                this.upstream.request(1);
            }
        }

        /* access modifiers changed from: package-private */
        public void innerComplete(MergeInnerObserver inner) {
            this.set.delete(inner);
            if (decrementAndGet() == 0) {
                Throwable ex = (Throwable) this.error.get();
                if (ex != null) {
                    this.downstream.onError(ex);
                } else {
                    this.downstream.onComplete();
                }
            } else if (this.maxConcurrency != Integer.MAX_VALUE) {
                this.upstream.request(1);
            }
        }

        final class MergeInnerObserver extends AtomicReference<Disposable> implements CompletableObserver, Disposable {
            private static final long serialVersionUID = 251330541679988317L;

            MergeInnerObserver() {
            }

            public void onSubscribe(Disposable d) {
                DisposableHelper.setOnce(this, d);
            }

            public void onError(Throwable e) {
                CompletableMergeSubscriber.this.innerError(this, e);
            }

            public void onComplete() {
                CompletableMergeSubscriber.this.innerComplete(this);
            }

            public boolean isDisposed() {
                return DisposableHelper.isDisposed((Disposable) get());
            }

            public void dispose() {
                DisposableHelper.dispose(this);
            }
        }
    }
}
