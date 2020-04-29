package dji.thirdparty.io.reactivex.internal.operators.flowable;

import dji.thirdparty.io.reactivex.Emitter;
import dji.thirdparty.io.reactivex.Flowable;
import dji.thirdparty.io.reactivex.exceptions.Exceptions;
import dji.thirdparty.io.reactivex.functions.BiFunction;
import dji.thirdparty.io.reactivex.functions.Consumer;
import dji.thirdparty.io.reactivex.internal.subscriptions.EmptySubscription;
import dji.thirdparty.io.reactivex.internal.subscriptions.SubscriptionHelper;
import dji.thirdparty.io.reactivex.internal.util.BackpressureHelper;
import dji.thirdparty.io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicLong;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableGenerate<T, S> extends Flowable<T> {
    final Consumer<? super S> disposeState;
    final BiFunction<S, Emitter<T>, S> generator;
    final Callable<S> stateSupplier;

    public FlowableGenerate(Callable<S> stateSupplier2, BiFunction<S, Emitter<T>, S> generator2, Consumer<? super S> disposeState2) {
        this.stateSupplier = stateSupplier2;
        this.generator = generator2;
        this.disposeState = disposeState2;
    }

    public void subscribeActual(Subscriber<? super T> s) {
        try {
            s.onSubscribe(new GeneratorSubscription(s, this.generator, this.disposeState, this.stateSupplier.call()));
        } catch (Throwable e) {
            Exceptions.throwIfFatal(e);
            EmptySubscription.error(e, s);
        }
    }

    static final class GeneratorSubscription<T, S> extends AtomicLong implements Emitter<T>, Subscription {
        private static final long serialVersionUID = 7565982551505011832L;
        final Subscriber<? super T> actual;
        volatile boolean cancelled;
        final Consumer<? super S> disposeState;
        final BiFunction<S, ? super Emitter<T>, S> generator;
        S state;
        boolean terminate;

        GeneratorSubscription(Subscriber<? super T> actual2, BiFunction<S, ? super Emitter<T>, S> generator2, Consumer<? super S> disposeState2, S initialState) {
            this.actual = actual2;
            this.generator = generator2;
            this.disposeState = disposeState2;
            this.state = initialState;
        }

        public void request(long n) {
            if (SubscriptionHelper.validate(n) && BackpressureHelper.add(this, n) == 0) {
                long e = 0;
                S s = this.state;
                BiFunction<S, ? super Emitter<T>, S> f = this.generator;
                while (true) {
                    if (e == n) {
                        n = get();
                        if (e == n) {
                            this.state = s;
                            n = addAndGet(-e);
                            if (n != 0) {
                                e = 0;
                            } else {
                                return;
                            }
                        } else {
                            continue;
                        }
                    } else if (this.cancelled) {
                        dispose(s);
                        return;
                    } else {
                        try {
                            s = f.apply(s, this);
                            if (this.terminate) {
                                this.cancelled = true;
                                dispose(s);
                                return;
                            }
                            e++;
                        } catch (Throwable ex) {
                            Exceptions.throwIfFatal(ex);
                            this.cancelled = true;
                            this.actual.onError(ex);
                            return;
                        }
                    }
                }
            }
        }

        private void dispose(S s) {
            try {
                this.disposeState.accept(s);
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                RxJavaPlugins.onError(ex);
            }
        }

        public void cancel() {
            if (!this.cancelled) {
                this.cancelled = true;
                if (BackpressureHelper.add(this, 1) == 0) {
                    dispose(this.state);
                }
            }
        }

        public void onNext(T t) {
            if (t == null) {
                onError(new NullPointerException("onNext called with null. Null values are generally not allowed in 2.x operators and sources."));
            } else {
                this.actual.onNext(t);
            }
        }

        public void onError(Throwable t) {
            if (t == null) {
                t = new NullPointerException("onError called with null. Null values are generally not allowed in 2.x operators and sources.");
            }
            this.terminate = true;
            this.actual.onError(t);
        }

        public void onComplete() {
            this.terminate = true;
            this.actual.onComplete();
        }
    }
}
