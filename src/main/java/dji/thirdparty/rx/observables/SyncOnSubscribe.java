package dji.thirdparty.rx.observables;

import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.Observer;
import dji.thirdparty.rx.Producer;
import dji.thirdparty.rx.Subscriber;
import dji.thirdparty.rx.Subscription;
import dji.thirdparty.rx.annotations.Beta;
import dji.thirdparty.rx.exceptions.Exceptions;
import dji.thirdparty.rx.functions.Action0;
import dji.thirdparty.rx.functions.Action1;
import dji.thirdparty.rx.functions.Action2;
import dji.thirdparty.rx.functions.Func0;
import dji.thirdparty.rx.functions.Func2;
import dji.thirdparty.rx.internal.operators.BackpressureUtils;
import dji.thirdparty.rx.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicLong;
import kotlin.jvm.internal.LongCompanionObject;

@Beta
public abstract class SyncOnSubscribe<S, T> implements Observable.OnSubscribe<T> {
    /* access modifiers changed from: protected */
    public abstract S generateState();

    /* access modifiers changed from: protected */
    public abstract S next(S s, Observer<? super T> observer);

    public /* bridge */ /* synthetic */ void call(Object x0) {
        call((Subscriber) ((Subscriber) x0));
    }

    public final void call(Subscriber<? super T> subscriber) {
        try {
            SubscriptionProducer<S, T> p = new SubscriptionProducer<>(subscriber, this, generateState());
            subscriber.add(p);
            subscriber.setProducer(p);
        } catch (Throwable e) {
            Exceptions.throwIfFatal(e);
            subscriber.onError(e);
        }
    }

    /* access modifiers changed from: protected */
    public void onUnsubscribe(S s) {
    }

    @Beta
    public static <S, T> SyncOnSubscribe<S, T> createSingleState(Func0<? extends S> generator, final Action2<? super S, ? super Observer<? super T>> next) {
        return new SyncOnSubscribeImpl(generator, new Func2<S, Observer<? super T>, S>() {
            /* class dji.thirdparty.rx.observables.SyncOnSubscribe.AnonymousClass1 */

            /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
             method: dji.thirdparty.rx.observables.SyncOnSubscribe.1.call(java.lang.Object, dji.thirdparty.rx.Observer):S
             arg types: [java.lang.Object, java.lang.Object]
             candidates:
              dji.thirdparty.rx.observables.SyncOnSubscribe.1.call(java.lang.Object, java.lang.Object):java.lang.Object
              dji.thirdparty.rx.functions.Func2.call(java.lang.Object, java.lang.Object):R
              dji.thirdparty.rx.observables.SyncOnSubscribe.1.call(java.lang.Object, dji.thirdparty.rx.Observer):S */
            public /* bridge */ /* synthetic */ Object call(Object x0, Object x1) {
                return call(x0, (Observer) ((Observer) x1));
            }

            public S call(S state, Observer<? super T> subscriber) {
                next.call(state, subscriber);
                return state;
            }
        });
    }

    @Beta
    public static <S, T> SyncOnSubscribe<S, T> createSingleState(Func0<? extends S> generator, final Action2<? super S, ? super Observer<? super T>> next, Action1<? super S> onUnsubscribe) {
        return new SyncOnSubscribeImpl(generator, new Func2<S, Observer<? super T>, S>() {
            /* class dji.thirdparty.rx.observables.SyncOnSubscribe.AnonymousClass2 */

            /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
             method: dji.thirdparty.rx.observables.SyncOnSubscribe.2.call(java.lang.Object, dji.thirdparty.rx.Observer):S
             arg types: [java.lang.Object, java.lang.Object]
             candidates:
              dji.thirdparty.rx.observables.SyncOnSubscribe.2.call(java.lang.Object, java.lang.Object):java.lang.Object
              dji.thirdparty.rx.functions.Func2.call(java.lang.Object, java.lang.Object):R
              dji.thirdparty.rx.observables.SyncOnSubscribe.2.call(java.lang.Object, dji.thirdparty.rx.Observer):S */
            public /* bridge */ /* synthetic */ Object call(Object x0, Object x1) {
                return call(x0, (Observer) ((Observer) x1));
            }

            public S call(S state, Observer<? super T> subscriber) {
                next.call(state, subscriber);
                return state;
            }
        }, onUnsubscribe);
    }

    @Beta
    public static <S, T> SyncOnSubscribe<S, T> createStateful(Func0<? extends S> generator, Func2<? super S, ? super Observer<? super T>, ? extends S> next, Action1<? super S> onUnsubscribe) {
        return new SyncOnSubscribeImpl(generator, next, onUnsubscribe);
    }

    @Beta
    public static <S, T> SyncOnSubscribe<S, T> createStateful(Func0<? extends S> generator, Func2<? super S, ? super Observer<? super T>, ? extends S> next) {
        return new SyncOnSubscribeImpl(generator, next);
    }

    @Beta
    public static <T> SyncOnSubscribe<Void, T> createStateless(final Action1<? super Observer<? super T>> next) {
        return new SyncOnSubscribeImpl(new Func2<Void, Observer<? super T>, Void>() {
            /* class dji.thirdparty.rx.observables.SyncOnSubscribe.AnonymousClass3 */

            /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
             method: dji.thirdparty.rx.observables.SyncOnSubscribe.3.call(java.lang.Void, dji.thirdparty.rx.Observer):java.lang.Void
             arg types: [java.lang.Object, java.lang.Object]
             candidates:
              dji.thirdparty.rx.observables.SyncOnSubscribe.3.call(java.lang.Object, java.lang.Object):java.lang.Object
              dji.thirdparty.rx.functions.Func2.call(java.lang.Object, java.lang.Object):R
              dji.thirdparty.rx.observables.SyncOnSubscribe.3.call(java.lang.Void, dji.thirdparty.rx.Observer):java.lang.Void */
            public /* bridge */ /* synthetic */ Object call(Object x0, Object x1) {
                return call((Void) ((Void) x0), (Observer) ((Observer) x1));
            }

            public Void call(Void state, Observer<? super T> subscriber) {
                next.call(subscriber);
                return state;
            }
        });
    }

    @Beta
    public static <T> SyncOnSubscribe<Void, T> createStateless(final Action1<? super Observer<? super T>> next, final Action0 onUnsubscribe) {
        return new SyncOnSubscribeImpl(new Func2<Void, Observer<? super T>, Void>() {
            /* class dji.thirdparty.rx.observables.SyncOnSubscribe.AnonymousClass4 */

            /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
             method: dji.thirdparty.rx.observables.SyncOnSubscribe.4.call(java.lang.Void, dji.thirdparty.rx.Observer):java.lang.Void
             arg types: [java.lang.Object, java.lang.Object]
             candidates:
              dji.thirdparty.rx.observables.SyncOnSubscribe.4.call(java.lang.Object, java.lang.Object):java.lang.Object
              dji.thirdparty.rx.functions.Func2.call(java.lang.Object, java.lang.Object):R
              dji.thirdparty.rx.observables.SyncOnSubscribe.4.call(java.lang.Void, dji.thirdparty.rx.Observer):java.lang.Void */
            public /* bridge */ /* synthetic */ Object call(Object x0, Object x1) {
                return call((Void) ((Void) x0), (Observer) ((Observer) x1));
            }

            public Void call(Void state, Observer<? super T> subscriber) {
                next.call(subscriber);
                return null;
            }
        }, new Action1<Void>() {
            /* class dji.thirdparty.rx.observables.SyncOnSubscribe.AnonymousClass5 */

            public void call(Void t) {
                onUnsubscribe.call();
            }
        });
    }

    private static final class SyncOnSubscribeImpl<S, T> extends SyncOnSubscribe<S, T> {
        private final Func0<? extends S> generator;
        private final Func2<? super S, ? super Observer<? super T>, ? extends S> next;
        private final Action1<? super S> onUnsubscribe;

        public /* bridge */ /* synthetic */ void call(Object x0) {
            SyncOnSubscribe.super.call((Subscriber) ((Subscriber) x0));
        }

        SyncOnSubscribeImpl(Func0<? extends S> generator2, Func2<? super S, ? super Observer<? super T>, ? extends S> next2, Action1<? super S> onUnsubscribe2) {
            this.generator = generator2;
            this.next = next2;
            this.onUnsubscribe = onUnsubscribe2;
        }

        public SyncOnSubscribeImpl(Func0<? extends S> generator2, Func2<? super S, ? super Observer<? super T>, ? extends S> next2) {
            this(generator2, next2, null);
        }

        public SyncOnSubscribeImpl(Func2<S, Observer<? super T>, S> next2, Action1<? super S> onUnsubscribe2) {
            this(null, next2, onUnsubscribe2);
        }

        public SyncOnSubscribeImpl(Func2<S, Observer<? super T>, S> nextFunc) {
            this(null, nextFunc, null);
        }

        /* access modifiers changed from: protected */
        public S generateState() {
            if (this.generator == null) {
                return null;
            }
            return this.generator.call();
        }

        /* access modifiers changed from: protected */
        public S next(S state, Observer<? super T> observer) {
            return this.next.call(state, observer);
        }

        /* access modifiers changed from: protected */
        public void onUnsubscribe(S state) {
            if (this.onUnsubscribe != null) {
                this.onUnsubscribe.call(state);
            }
        }
    }

    private static class SubscriptionProducer<S, T> extends AtomicLong implements Producer, Subscription, Observer<T> {
        private static final long serialVersionUID = -3736864024352728072L;
        private final Subscriber<? super T> actualSubscriber;
        private boolean hasTerminated;
        private boolean onNextCalled;
        private final SyncOnSubscribe<S, T> parent;
        private S state;

        SubscriptionProducer(Subscriber<? super T> subscriber, SyncOnSubscribe<S, T> parent2, S state2) {
            this.actualSubscriber = subscriber;
            this.parent = parent2;
            this.state = state2;
        }

        public boolean isUnsubscribed() {
            return get() < 0;
        }

        public void unsubscribe() {
            long requestCount;
            do {
                requestCount = get();
                if (compareAndSet(0, -1)) {
                    doUnsubscribe();
                    return;
                }
            } while (!compareAndSet(requestCount, -2));
        }

        private boolean tryUnsubscribe() {
            if (!this.hasTerminated && get() >= -1) {
                return false;
            }
            set(-1);
            doUnsubscribe();
            return true;
        }

        private void doUnsubscribe() {
            try {
                this.parent.onUnsubscribe(this.state);
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                RxJavaPlugins.getInstance().getErrorHandler().handleError(e);
            }
        }

        public void request(long n) {
            if (n > 0 && BackpressureUtils.getAndAddRequest(this, n) == 0) {
                if (n == LongCompanionObject.MAX_VALUE) {
                    fastpath();
                } else {
                    slowPath(n);
                }
            }
        }

        private void fastpath() {
            SyncOnSubscribe<S, T> p = this.parent;
            Subscriber<? super T> a = this.actualSubscriber;
            do {
                try {
                    this.onNextCalled = false;
                    nextIteration(p);
                } catch (Throwable ex) {
                    handleThrownError(a, ex);
                    return;
                }
            } while (!tryUnsubscribe());
        }

        private void handleThrownError(Subscriber<? super T> a, Throwable ex) {
            if (this.hasTerminated) {
                RxJavaPlugins.getInstance().getErrorHandler().handleError(ex);
                return;
            }
            this.hasTerminated = true;
            a.onError(ex);
            unsubscribe();
        }

        private void slowPath(long n) {
            SyncOnSubscribe<S, T> p = this.parent;
            Subscriber<? super T> a = this.actualSubscriber;
            long numRequested = n;
            do {
                long numRemaining = numRequested;
                do {
                    try {
                        this.onNextCalled = false;
                        nextIteration(p);
                        if (!tryUnsubscribe()) {
                            if (this.onNextCalled) {
                                numRemaining--;
                            }
                        } else {
                            return;
                        }
                    } catch (Throwable ex) {
                        handleThrownError(a, ex);
                        return;
                    }
                } while (numRemaining != 0);
                numRequested = addAndGet(-numRequested);
            } while (numRequested > 0);
            tryUnsubscribe();
        }

        private void nextIteration(SyncOnSubscribe<S, T> parent2) {
            this.state = parent2.next(this.state, this);
        }

        public void onCompleted() {
            if (this.hasTerminated) {
                throw new IllegalStateException("Terminal event already emitted.");
            }
            this.hasTerminated = true;
            if (!this.actualSubscriber.isUnsubscribed()) {
                this.actualSubscriber.onCompleted();
            }
        }

        public void onError(Throwable e) {
            if (this.hasTerminated) {
                throw new IllegalStateException("Terminal event already emitted.");
            }
            this.hasTerminated = true;
            if (!this.actualSubscriber.isUnsubscribed()) {
                this.actualSubscriber.onError(e);
            }
        }

        public void onNext(T value) {
            if (this.onNextCalled) {
                throw new IllegalStateException("onNext called multiple times!");
            }
            this.onNextCalled = true;
            this.actualSubscriber.onNext(value);
        }
    }
}
