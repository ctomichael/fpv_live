package dji.thirdparty.rx;

import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.Scheduler;
import dji.thirdparty.rx.Single;
import dji.thirdparty.rx.annotations.Experimental;
import dji.thirdparty.rx.exceptions.CompositeException;
import dji.thirdparty.rx.exceptions.Exceptions;
import dji.thirdparty.rx.functions.Action0;
import dji.thirdparty.rx.functions.Action1;
import dji.thirdparty.rx.functions.Actions;
import dji.thirdparty.rx.functions.Func0;
import dji.thirdparty.rx.functions.Func1;
import dji.thirdparty.rx.functions.Func2;
import dji.thirdparty.rx.internal.operators.CompletableOnSubscribeConcat;
import dji.thirdparty.rx.internal.operators.CompletableOnSubscribeConcatArray;
import dji.thirdparty.rx.internal.operators.CompletableOnSubscribeConcatIterable;
import dji.thirdparty.rx.internal.operators.CompletableOnSubscribeMerge;
import dji.thirdparty.rx.internal.operators.CompletableOnSubscribeMergeArray;
import dji.thirdparty.rx.internal.operators.CompletableOnSubscribeMergeDelayErrorArray;
import dji.thirdparty.rx.internal.operators.CompletableOnSubscribeMergeDelayErrorIterable;
import dji.thirdparty.rx.internal.operators.CompletableOnSubscribeMergeIterable;
import dji.thirdparty.rx.internal.operators.CompletableOnSubscribeTimeout;
import dji.thirdparty.rx.internal.util.SubscriptionList;
import dji.thirdparty.rx.internal.util.UtilityFunctions;
import dji.thirdparty.rx.plugins.RxJavaErrorHandler;
import dji.thirdparty.rx.plugins.RxJavaPlugins;
import dji.thirdparty.rx.schedulers.Schedulers;
import dji.thirdparty.rx.subscriptions.BooleanSubscription;
import dji.thirdparty.rx.subscriptions.CompositeSubscription;
import dji.thirdparty.rx.subscriptions.MultipleAssignmentSubscription;
import dji.thirdparty.rx.subscriptions.SerialSubscription;
import dji.thirdparty.rx.subscriptions.Subscriptions;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

@Experimental
public class Completable {
    static final Completable COMPLETE = create(new CompletableOnSubscribe() {
        /* class dji.thirdparty.rx.Completable.AnonymousClass1 */

        public void call(CompletableSubscriber s) {
            s.onSubscribe(Subscriptions.unsubscribed());
            s.onCompleted();
        }
    });
    static final RxJavaErrorHandler ERROR_HANDLER = RxJavaPlugins.getInstance().getErrorHandler();
    static final Completable NEVER = create(new CompletableOnSubscribe() {
        /* class dji.thirdparty.rx.Completable.AnonymousClass2 */

        public void call(CompletableSubscriber s) {
            s.onSubscribe(Subscriptions.unsubscribed());
        }
    });
    private final CompletableOnSubscribe onSubscribe;

    public interface CompletableOnSubscribe extends Action1<CompletableSubscriber> {
    }

    public interface CompletableOperator extends Func1<CompletableSubscriber, CompletableSubscriber> {
    }

    public interface CompletableSubscriber {
        void onCompleted();

        void onError(Throwable th);

        void onSubscribe(Subscription subscription);
    }

    public interface CompletableTransformer extends Func1<Completable, Completable> {
    }

    public static Completable amb(final Completable... sources) {
        requireNonNull(sources);
        if (sources.length == 0) {
            return complete();
        }
        if (sources.length == 1) {
            return sources[0];
        }
        return create(new CompletableOnSubscribe() {
            /* class dji.thirdparty.rx.Completable.AnonymousClass3 */

            public void call(final CompletableSubscriber s) {
                final CompositeSubscription set = new CompositeSubscription();
                s.onSubscribe(set);
                final AtomicBoolean once = new AtomicBoolean();
                CompletableSubscriber inner = new CompletableSubscriber() {
                    /* class dji.thirdparty.rx.Completable.AnonymousClass3.AnonymousClass1 */

                    public void onCompleted() {
                        if (once.compareAndSet(false, true)) {
                            set.unsubscribe();
                            s.onCompleted();
                        }
                    }

                    public void onError(Throwable e) {
                        if (once.compareAndSet(false, true)) {
                            set.unsubscribe();
                            s.onError(e);
                            return;
                        }
                        Completable.ERROR_HANDLER.handleError(e);
                    }

                    public void onSubscribe(Subscription d) {
                        set.add(d);
                    }
                };
                Completable[] arr$ = sources;
                int len$ = arr$.length;
                int i$ = 0;
                while (i$ < len$) {
                    Completable c = arr$[i$];
                    if (!set.isUnsubscribed()) {
                        if (c == null) {
                            NullPointerException npe = new NullPointerException("One of the sources is null");
                            if (once.compareAndSet(false, true)) {
                                set.unsubscribe();
                                s.onError(npe);
                                return;
                            }
                            Completable.ERROR_HANDLER.handleError(npe);
                            return;
                        } else if (!once.get() && !set.isUnsubscribed()) {
                            c.subscribe(inner);
                            i$++;
                        } else {
                            return;
                        }
                    } else {
                        return;
                    }
                }
            }
        });
    }

    public static Completable amb(final Iterable<? extends Completable> sources) {
        requireNonNull(sources);
        return create(new CompletableOnSubscribe() {
            /* class dji.thirdparty.rx.Completable.AnonymousClass4 */

            public void call(final CompletableSubscriber s) {
                final CompositeSubscription set = new CompositeSubscription();
                s.onSubscribe(set);
                final AtomicBoolean once = new AtomicBoolean();
                CompletableSubscriber inner = new CompletableSubscriber() {
                    /* class dji.thirdparty.rx.Completable.AnonymousClass4.AnonymousClass1 */

                    public void onCompleted() {
                        if (once.compareAndSet(false, true)) {
                            set.unsubscribe();
                            s.onCompleted();
                        }
                    }

                    public void onError(Throwable e) {
                        if (once.compareAndSet(false, true)) {
                            set.unsubscribe();
                            s.onError(e);
                            return;
                        }
                        Completable.ERROR_HANDLER.handleError(e);
                    }

                    public void onSubscribe(Subscription d) {
                        set.add(d);
                    }
                };
                try {
                    Iterator<? extends Completable> it2 = sources.iterator();
                    if (it2 == null) {
                        s.onError(new NullPointerException("The iterator returned is null"));
                        return;
                    }
                    boolean empty = true;
                    while (!once.get() && !set.isUnsubscribed()) {
                        try {
                            if (it2.hasNext()) {
                                empty = false;
                                if (!once.get() && !set.isUnsubscribed()) {
                                    try {
                                        Completable c = (Completable) it2.next();
                                        if (c == null) {
                                            NullPointerException npe = new NullPointerException("One of the sources is null");
                                            if (once.compareAndSet(false, true)) {
                                                set.unsubscribe();
                                                s.onError(npe);
                                                return;
                                            }
                                            Completable.ERROR_HANDLER.handleError(npe);
                                            return;
                                        } else if (!once.get() && !set.isUnsubscribed()) {
                                            c.subscribe(inner);
                                        } else {
                                            return;
                                        }
                                    } catch (Throwable e) {
                                        if (once.compareAndSet(false, true)) {
                                            set.unsubscribe();
                                            s.onError(e);
                                            return;
                                        }
                                        Completable.ERROR_HANDLER.handleError(e);
                                        return;
                                    }
                                } else {
                                    return;
                                }
                            } else if (empty) {
                                s.onCompleted();
                                return;
                            } else {
                                return;
                            }
                        } catch (Throwable e2) {
                            if (once.compareAndSet(false, true)) {
                                set.unsubscribe();
                                s.onError(e2);
                                return;
                            }
                            Completable.ERROR_HANDLER.handleError(e2);
                            return;
                        }
                    }
                } catch (Throwable e3) {
                    s.onError(e3);
                }
            }
        });
    }

    public static Completable complete() {
        return COMPLETE;
    }

    public static Completable concat(Completable... sources) {
        requireNonNull(sources);
        if (sources.length == 0) {
            return complete();
        }
        if (sources.length == 1) {
            return sources[0];
        }
        return create(new CompletableOnSubscribeConcatArray(sources));
    }

    public static Completable concat(Iterable<? extends Completable> sources) {
        requireNonNull(sources);
        return create(new CompletableOnSubscribeConcatIterable(sources));
    }

    public static Completable concat(Observable<? extends Completable> sources) {
        return concat(sources, 2);
    }

    public static Completable concat(Observable<? extends Completable> sources, int prefetch) {
        requireNonNull(sources);
        if (prefetch >= 1) {
            return create(new CompletableOnSubscribeConcat(sources, prefetch));
        }
        throw new IllegalArgumentException("prefetch > 0 required but it was " + prefetch);
    }

    public static Completable create(CompletableOnSubscribe onSubscribe2) {
        requireNonNull(onSubscribe2);
        try {
            return new Completable(onSubscribe2);
        } catch (NullPointerException ex) {
            throw ex;
        } catch (Throwable ex2) {
            ERROR_HANDLER.handleError(ex2);
            throw toNpe(ex2);
        }
    }

    public static Completable defer(final Func0<? extends Completable> completableFunc0) {
        requireNonNull(completableFunc0);
        return create(new CompletableOnSubscribe() {
            /* class dji.thirdparty.rx.Completable.AnonymousClass5 */

            public void call(CompletableSubscriber s) {
                try {
                    Completable c = (Completable) completableFunc0.call();
                    if (c == null) {
                        s.onSubscribe(Subscriptions.unsubscribed());
                        s.onError(new NullPointerException("The completable returned is null"));
                        return;
                    }
                    c.subscribe(s);
                } catch (Throwable e) {
                    s.onSubscribe(Subscriptions.unsubscribed());
                    s.onError(e);
                }
            }
        });
    }

    public static Completable error(final Func0<? extends Throwable> errorFunc0) {
        requireNonNull(errorFunc0);
        return create(new CompletableOnSubscribe() {
            /* class dji.thirdparty.rx.Completable.AnonymousClass6 */

            public void call(CompletableSubscriber s) {
                Throwable error;
                s.onSubscribe(Subscriptions.unsubscribed());
                try {
                    error = (Throwable) errorFunc0.call();
                } catch (Throwable e) {
                    error = e;
                }
                if (error == null) {
                    error = new NullPointerException("The error supplied is null");
                }
                s.onError(error);
            }
        });
    }

    public static Completable error(final Throwable error) {
        requireNonNull(error);
        return create(new CompletableOnSubscribe() {
            /* class dji.thirdparty.rx.Completable.AnonymousClass7 */

            public void call(CompletableSubscriber s) {
                s.onSubscribe(Subscriptions.unsubscribed());
                s.onError(error);
            }
        });
    }

    public static Completable fromAction(final Action0 action) {
        requireNonNull(action);
        return create(new CompletableOnSubscribe() {
            /* class dji.thirdparty.rx.Completable.AnonymousClass8 */

            public void call(CompletableSubscriber s) {
                BooleanSubscription bs = new BooleanSubscription();
                s.onSubscribe(bs);
                try {
                    action.call();
                    if (!bs.isUnsubscribed()) {
                        s.onCompleted();
                    }
                } catch (Throwable e) {
                    if (!bs.isUnsubscribed()) {
                        s.onError(e);
                    }
                }
            }
        });
    }

    public static Completable fromCallable(final Callable<?> callable) {
        requireNonNull(callable);
        return create(new CompletableOnSubscribe() {
            /* class dji.thirdparty.rx.Completable.AnonymousClass9 */

            public void call(CompletableSubscriber s) {
                BooleanSubscription bs = new BooleanSubscription();
                s.onSubscribe(bs);
                try {
                    callable.call();
                    if (!bs.isUnsubscribed()) {
                        s.onCompleted();
                    }
                } catch (Throwable e) {
                    if (!bs.isUnsubscribed()) {
                        s.onError(e);
                    }
                }
            }
        });
    }

    public static Completable fromFuture(Future<?> future) {
        requireNonNull(future);
        return fromObservable(Observable.from(future));
    }

    public static Completable fromObservable(final Observable<?> flowable) {
        requireNonNull(flowable);
        return create(new CompletableOnSubscribe() {
            /* class dji.thirdparty.rx.Completable.AnonymousClass10 */

            public void call(final CompletableSubscriber cs) {
                Subscriber<Object> subscriber = new Subscriber<Object>() {
                    /* class dji.thirdparty.rx.Completable.AnonymousClass10.AnonymousClass1 */

                    public void onCompleted() {
                        cs.onCompleted();
                    }

                    public void onError(Throwable t) {
                        cs.onError(t);
                    }

                    public void onNext(Object t) {
                    }
                };
                cs.onSubscribe(subscriber);
                flowable.unsafeSubscribe(subscriber);
            }
        });
    }

    public static Completable fromSingle(final Single<?> single) {
        requireNonNull(single);
        return create(new CompletableOnSubscribe() {
            /* class dji.thirdparty.rx.Completable.AnonymousClass11 */

            public void call(final CompletableSubscriber s) {
                SingleSubscriber<Object> te = new SingleSubscriber<Object>() {
                    /* class dji.thirdparty.rx.Completable.AnonymousClass11.AnonymousClass1 */

                    public void onError(Throwable e) {
                        s.onError(e);
                    }

                    public void onSuccess(Object value) {
                        s.onCompleted();
                    }
                };
                s.onSubscribe(te);
                single.subscribe(te);
            }
        });
    }

    public static Completable merge(Completable... sources) {
        requireNonNull(sources);
        if (sources.length == 0) {
            return complete();
        }
        if (sources.length == 1) {
            return sources[0];
        }
        return create(new CompletableOnSubscribeMergeArray(sources));
    }

    public static Completable merge(Iterable<? extends Completable> sources) {
        requireNonNull(sources);
        return create(new CompletableOnSubscribeMergeIterable(sources));
    }

    public static Completable merge(Observable<? extends Completable> sources) {
        return merge0(sources, Integer.MAX_VALUE, false);
    }

    public static Completable merge(Observable<? extends Completable> sources, int maxConcurrency) {
        return merge0(sources, maxConcurrency, false);
    }

    protected static Completable merge0(Observable<? extends Completable> sources, int maxConcurrency, boolean delayErrors) {
        requireNonNull(sources);
        if (maxConcurrency >= 1) {
            return create(new CompletableOnSubscribeMerge(sources, maxConcurrency, delayErrors));
        }
        throw new IllegalArgumentException("maxConcurrency > 0 required but it was " + maxConcurrency);
    }

    public static Completable mergeDelayError(Completable... sources) {
        requireNonNull(sources);
        return create(new CompletableOnSubscribeMergeDelayErrorArray(sources));
    }

    public static Completable mergeDelayError(Iterable<? extends Completable> sources) {
        requireNonNull(sources);
        return create(new CompletableOnSubscribeMergeDelayErrorIterable(sources));
    }

    public static Completable mergeDelayError(Observable<? extends Completable> sources) {
        return merge0(sources, Integer.MAX_VALUE, true);
    }

    public static Completable mergeDelayError(Observable<? extends Completable> sources, int maxConcurrency) {
        return merge0(sources, maxConcurrency, true);
    }

    public static Completable never() {
        return NEVER;
    }

    static <T> T requireNonNull(T o) {
        if (o != null) {
            return o;
        }
        throw new NullPointerException();
    }

    public static Completable timer(long delay, TimeUnit unit) {
        return timer(delay, unit, Schedulers.computation());
    }

    public static Completable timer(final long delay, final TimeUnit unit, final Scheduler scheduler) {
        requireNonNull(unit);
        requireNonNull(scheduler);
        return create(new CompletableOnSubscribe() {
            /* class dji.thirdparty.rx.Completable.AnonymousClass12 */

            public void call(final CompletableSubscriber s) {
                MultipleAssignmentSubscription mad = new MultipleAssignmentSubscription();
                s.onSubscribe(mad);
                if (!mad.isUnsubscribed()) {
                    final Scheduler.Worker w = scheduler.createWorker();
                    mad.set(w);
                    w.schedule(new Action0() {
                        /* class dji.thirdparty.rx.Completable.AnonymousClass12.AnonymousClass1 */

                        public void call() {
                            try {
                                s.onCompleted();
                            } finally {
                                w.unsubscribe();
                            }
                        }
                    }, delay, unit);
                }
            }
        });
    }

    static NullPointerException toNpe(Throwable ex) {
        NullPointerException npe = new NullPointerException("Actually not, but can't pass out an exception otherwise...");
        npe.initCause(ex);
        return npe;
    }

    public static <R> Completable using(Func0<R> resourceFunc0, Func1<? super R, ? extends Completable> completableFunc1, Action1<? super R> disposer) {
        return using(resourceFunc0, completableFunc1, disposer, true);
    }

    public static <R> Completable using(final Func0<R> resourceFunc0, final Func1<? super R, ? extends Completable> completableFunc1, final Action1<? super R> disposer, final boolean eager) {
        requireNonNull(resourceFunc0);
        requireNonNull(completableFunc1);
        requireNonNull(disposer);
        return create(new CompletableOnSubscribe() {
            /* class dji.thirdparty.rx.Completable.AnonymousClass13 */

            public void call(final CompletableSubscriber s) {
                try {
                    final R resource = resourceFunc0.call();
                    try {
                        Completable cs = (Completable) completableFunc1.call(resource);
                        if (cs == null) {
                            try {
                                disposer.call(resource);
                                s.onSubscribe(Subscriptions.unsubscribed());
                                s.onError(new NullPointerException("The completable supplied is null"));
                            } catch (Throwable ex) {
                                Exceptions.throwIfFatal(ex);
                                s.onSubscribe(Subscriptions.unsubscribed());
                                s.onError(new CompositeException(Arrays.asList(new NullPointerException("The completable supplied is null"), ex)));
                            }
                        } else {
                            final AtomicBoolean once = new AtomicBoolean();
                            cs.subscribe(new CompletableSubscriber() {
                                /* class dji.thirdparty.rx.Completable.AnonymousClass13.AnonymousClass1 */
                                Subscription d;

                                /* access modifiers changed from: package-private */
                                public void dispose() {
                                    this.d.unsubscribe();
                                    if (once.compareAndSet(false, true)) {
                                        try {
                                            disposer.call(resource);
                                        } catch (Throwable ex) {
                                            Completable.ERROR_HANDLER.handleError(ex);
                                        }
                                    }
                                }

                                public void onCompleted() {
                                    if (eager && once.compareAndSet(false, true)) {
                                        try {
                                            disposer.call(resource);
                                        } catch (Throwable ex) {
                                            s.onError(ex);
                                            return;
                                        }
                                    }
                                    s.onCompleted();
                                    if (!eager) {
                                        dispose();
                                    }
                                }

                                public void onError(Throwable e) {
                                    if (eager && once.compareAndSet(false, true)) {
                                        try {
                                            disposer.call(resource);
                                        } catch (Throwable ex) {
                                            e = new CompositeException(Arrays.asList(e, ex));
                                        }
                                    }
                                    s.onError(e);
                                    if (!eager) {
                                        dispose();
                                    }
                                }

                                public void onSubscribe(Subscription d2) {
                                    this.d = d2;
                                    s.onSubscribe(Subscriptions.create(new Action0() {
                                        /* class dji.thirdparty.rx.Completable.AnonymousClass13.AnonymousClass1.AnonymousClass1 */

                                        public void call() {
                                            AnonymousClass1.this.dispose();
                                        }
                                    }));
                                }
                            });
                        }
                    } catch (Throwable ex2) {
                        Exceptions.throwIfFatal(e);
                        Exceptions.throwIfFatal(ex2);
                        s.onSubscribe(Subscriptions.unsubscribed());
                        s.onError(new CompositeException(Arrays.asList(e, ex2)));
                    }
                } catch (Throwable e) {
                    s.onSubscribe(Subscriptions.unsubscribed());
                    s.onError(e);
                }
            }
        });
    }

    protected Completable(CompletableOnSubscribe onSubscribe2) {
        this.onSubscribe = onSubscribe2;
    }

    public final Completable ambWith(Completable other) {
        requireNonNull(other);
        return amb(this, other);
    }

    public final void await() {
        final CountDownLatch cdl = new CountDownLatch(1);
        final Throwable[] err = new Throwable[1];
        subscribe(new CompletableSubscriber() {
            /* class dji.thirdparty.rx.Completable.AnonymousClass14 */

            public void onCompleted() {
                cdl.countDown();
            }

            public void onError(Throwable e) {
                err[0] = e;
                cdl.countDown();
            }

            public void onSubscribe(Subscription d) {
            }
        });
        if (cdl.getCount() != 0) {
            try {
                cdl.await();
                if (err[0] != null) {
                    Exceptions.propagate(err[0]);
                }
            } catch (InterruptedException ex) {
                throw Exceptions.propagate(ex);
            }
        } else if (err[0] != null) {
            Exceptions.propagate(err[0]);
        }
    }

    public final boolean await(long timeout, TimeUnit unit) {
        boolean b = true;
        requireNonNull(unit);
        final CountDownLatch cdl = new CountDownLatch(1);
        final Throwable[] err = new Throwable[1];
        subscribe(new CompletableSubscriber() {
            /* class dji.thirdparty.rx.Completable.AnonymousClass15 */

            public void onCompleted() {
                cdl.countDown();
            }

            public void onError(Throwable e) {
                err[0] = e;
                cdl.countDown();
            }

            public void onSubscribe(Subscription d) {
            }
        });
        if (cdl.getCount() != 0) {
            try {
                b = cdl.await(timeout, unit);
                if (b && err[0] != null) {
                    Exceptions.propagate(err[0]);
                }
            } catch (InterruptedException ex) {
                throw Exceptions.propagate(ex);
            }
        } else if (err[0] != null) {
            Exceptions.propagate(err[0]);
        }
        return b;
    }

    public final Completable compose(CompletableTransformer transformer) {
        return (Completable) to(transformer);
    }

    public final <T> Observable<T> andThen(Observable<T> next) {
        requireNonNull(next);
        return next.delaySubscription(toObservable());
    }

    public final <T> Single<T> andThen(Single<T> next) {
        requireNonNull(next);
        return next.delaySubscription(toObservable());
    }

    public final Completable concatWith(Completable other) {
        requireNonNull(other);
        return concat(this, other);
    }

    public final Completable delay(long delay, TimeUnit unit) {
        return delay(delay, unit, Schedulers.computation(), false);
    }

    public final Completable delay(long delay, TimeUnit unit, Scheduler scheduler) {
        return delay(delay, unit, scheduler, false);
    }

    public final Completable delay(long delay, TimeUnit unit, Scheduler scheduler, boolean delayError) {
        requireNonNull(unit);
        requireNonNull(scheduler);
        final Scheduler scheduler2 = scheduler;
        final long j = delay;
        final TimeUnit timeUnit = unit;
        final boolean z = delayError;
        return create(new CompletableOnSubscribe() {
            /* class dji.thirdparty.rx.Completable.AnonymousClass16 */

            public void call(final CompletableSubscriber s) {
                final CompositeSubscription set = new CompositeSubscription();
                final Scheduler.Worker w = scheduler2.createWorker();
                set.add(w);
                Completable.this.subscribe(new CompletableSubscriber() {
                    /* class dji.thirdparty.rx.Completable.AnonymousClass16.AnonymousClass1 */

                    public void onCompleted() {
                        set.add(w.schedule(new Action0() {
                            /* class dji.thirdparty.rx.Completable.AnonymousClass16.AnonymousClass1.AnonymousClass1 */

                            public void call() {
                                try {
                                    s.onCompleted();
                                } finally {
                                    w.unsubscribe();
                                }
                            }
                        }, j, timeUnit));
                    }

                    public void onError(final Throwable e) {
                        if (z) {
                            set.add(w.schedule(new Action0() {
                                /* class dji.thirdparty.rx.Completable.AnonymousClass16.AnonymousClass1.AnonymousClass2 */

                                public void call() {
                                    try {
                                        s.onError(e);
                                    } finally {
                                        w.unsubscribe();
                                    }
                                }
                            }, j, timeUnit));
                        } else {
                            s.onError(e);
                        }
                    }

                    public void onSubscribe(Subscription d) {
                        set.add(d);
                        s.onSubscribe(set);
                    }
                });
            }
        });
    }

    @Deprecated
    public final Completable doOnComplete(Action0 onComplete) {
        return doOnCompleted(onComplete);
    }

    public final Completable doOnCompleted(Action0 onCompleted) {
        return doOnLifecycle(Actions.empty(), Actions.empty(), onCompleted, Actions.empty(), Actions.empty());
    }

    public final Completable doOnUnsubscribe(Action0 onUnsubscribe) {
        return doOnLifecycle(Actions.empty(), Actions.empty(), Actions.empty(), Actions.empty(), onUnsubscribe);
    }

    public final Completable doOnError(Action1<? super Throwable> onError) {
        return doOnLifecycle(Actions.empty(), onError, Actions.empty(), Actions.empty(), Actions.empty());
    }

    /* access modifiers changed from: protected */
    public final Completable doOnLifecycle(Action1<? super Subscription> onSubscribe2, Action1<? super Throwable> onError, Action0 onComplete, Action0 onAfterComplete, Action0 onUnsubscribe) {
        requireNonNull(onSubscribe2);
        requireNonNull(onError);
        requireNonNull(onComplete);
        requireNonNull(onAfterComplete);
        requireNonNull(onUnsubscribe);
        final Action0 action0 = onComplete;
        final Action0 action02 = onAfterComplete;
        final Action1<? super Throwable> action1 = onError;
        final Action1<? super Subscription> action12 = onSubscribe2;
        final Action0 action03 = onUnsubscribe;
        return create(new CompletableOnSubscribe() {
            /* class dji.thirdparty.rx.Completable.AnonymousClass17 */

            public void call(final CompletableSubscriber s) {
                Completable.this.subscribe(new CompletableSubscriber() {
                    /* class dji.thirdparty.rx.Completable.AnonymousClass17.AnonymousClass1 */

                    public void onCompleted() {
                        try {
                            action0.call();
                            s.onCompleted();
                            try {
                                action02.call();
                            } catch (Throwable e) {
                                Completable.ERROR_HANDLER.handleError(e);
                            }
                        } catch (Throwable e2) {
                            s.onError(e2);
                        }
                    }

                    public void onError(Throwable e) {
                        try {
                            action1.call(e);
                        } catch (Throwable ex) {
                            e = new CompositeException(Arrays.asList(e, ex));
                        }
                        s.onError(e);
                    }

                    public void onSubscribe(final Subscription d) {
                        try {
                            action12.call(d);
                            s.onSubscribe(Subscriptions.create(new Action0() {
                                /* class dji.thirdparty.rx.Completable.AnonymousClass17.AnonymousClass1.AnonymousClass1 */

                                public void call() {
                                    try {
                                        action03.call();
                                    } catch (Throwable e) {
                                        Completable.ERROR_HANDLER.handleError(e);
                                    }
                                    d.unsubscribe();
                                }
                            }));
                        } catch (Throwable ex) {
                            d.unsubscribe();
                            s.onSubscribe(Subscriptions.unsubscribed());
                            s.onError(ex);
                        }
                    }
                });
            }
        });
    }

    public final Completable doOnSubscribe(Action1<? super Subscription> onSubscribe2) {
        return doOnLifecycle(onSubscribe2, Actions.empty(), Actions.empty(), Actions.empty(), Actions.empty());
    }

    public final Completable doOnTerminate(final Action0 onTerminate) {
        return doOnLifecycle(Actions.empty(), new Action1<Throwable>() {
            /* class dji.thirdparty.rx.Completable.AnonymousClass18 */

            public void call(Throwable e) {
                onTerminate.call();
            }
        }, onTerminate, Actions.empty(), Actions.empty());
    }

    public final Completable endWith(Completable other) {
        return concatWith(other);
    }

    public final <T> Observable<T> endWith(Observable<T> next) {
        return next.startWith(toObservable());
    }

    public final Completable doAfterTerminate(Action0 onAfterComplete) {
        return doOnLifecycle(Actions.empty(), Actions.empty(), Actions.empty(), onAfterComplete, Actions.empty());
    }

    public final Throwable get() {
        final CountDownLatch cdl = new CountDownLatch(1);
        final Throwable[] err = new Throwable[1];
        subscribe(new CompletableSubscriber() {
            /* class dji.thirdparty.rx.Completable.AnonymousClass19 */

            public void onCompleted() {
                cdl.countDown();
            }

            public void onError(Throwable e) {
                err[0] = e;
                cdl.countDown();
            }

            public void onSubscribe(Subscription d) {
            }
        });
        if (cdl.getCount() == 0) {
            return err[0];
        }
        try {
            cdl.await();
            return err[0];
        } catch (InterruptedException ex) {
            throw Exceptions.propagate(ex);
        }
    }

    public final Throwable get(long timeout, TimeUnit unit) {
        requireNonNull(unit);
        final CountDownLatch cdl = new CountDownLatch(1);
        final Throwable[] err = new Throwable[1];
        subscribe(new CompletableSubscriber() {
            /* class dji.thirdparty.rx.Completable.AnonymousClass20 */

            public void onCompleted() {
                cdl.countDown();
            }

            public void onError(Throwable e) {
                err[0] = e;
                cdl.countDown();
            }

            public void onSubscribe(Subscription d) {
            }
        });
        if (cdl.getCount() == 0) {
            return err[0];
        }
        try {
            if (cdl.await(timeout, unit)) {
                return err[0];
            }
            Exceptions.propagate(new TimeoutException());
            return null;
        } catch (InterruptedException ex) {
            throw Exceptions.propagate(ex);
        }
    }

    public final Completable lift(final CompletableOperator onLift) {
        requireNonNull(onLift);
        return create(new CompletableOnSubscribe() {
            /* class dji.thirdparty.rx.Completable.AnonymousClass21 */

            public void call(CompletableSubscriber s) {
                try {
                    Completable.this.subscribe((CompletableSubscriber) onLift.call(s));
                } catch (NullPointerException ex) {
                    throw ex;
                } catch (Throwable ex2) {
                    throw Completable.toNpe(ex2);
                }
            }
        });
    }

    public final Completable mergeWith(Completable other) {
        requireNonNull(other);
        return merge(this, other);
    }

    public final Completable observeOn(final Scheduler scheduler) {
        requireNonNull(scheduler);
        return create(new CompletableOnSubscribe() {
            /* class dji.thirdparty.rx.Completable.AnonymousClass22 */

            public void call(final CompletableSubscriber s) {
                final SubscriptionList ad = new SubscriptionList();
                final Scheduler.Worker w = scheduler.createWorker();
                ad.add(w);
                s.onSubscribe(ad);
                Completable.this.subscribe(new CompletableSubscriber() {
                    /* class dji.thirdparty.rx.Completable.AnonymousClass22.AnonymousClass1 */

                    public void onCompleted() {
                        w.schedule(new Action0() {
                            /* class dji.thirdparty.rx.Completable.AnonymousClass22.AnonymousClass1.AnonymousClass1 */

                            public void call() {
                                try {
                                    s.onCompleted();
                                } finally {
                                    ad.unsubscribe();
                                }
                            }
                        });
                    }

                    public void onError(final Throwable e) {
                        w.schedule(new Action0() {
                            /* class dji.thirdparty.rx.Completable.AnonymousClass22.AnonymousClass1.AnonymousClass2 */

                            public void call() {
                                try {
                                    s.onError(e);
                                } finally {
                                    ad.unsubscribe();
                                }
                            }
                        });
                    }

                    public void onSubscribe(Subscription d) {
                        ad.add(d);
                    }
                });
            }
        });
    }

    public final Completable onErrorComplete() {
        return onErrorComplete(UtilityFunctions.alwaysTrue());
    }

    public final Completable onErrorComplete(final Func1<? super Throwable, Boolean> predicate) {
        requireNonNull(predicate);
        return create(new CompletableOnSubscribe() {
            /* class dji.thirdparty.rx.Completable.AnonymousClass23 */

            public void call(final CompletableSubscriber s) {
                Completable.this.subscribe(new CompletableSubscriber() {
                    /* class dji.thirdparty.rx.Completable.AnonymousClass23.AnonymousClass1 */

                    public void onCompleted() {
                        s.onCompleted();
                    }

                    public void onError(Throwable e) {
                        try {
                            if (((Boolean) predicate.call(e)).booleanValue()) {
                                s.onCompleted();
                            } else {
                                s.onError(e);
                            }
                        } catch (Throwable ex) {
                            new CompositeException(Arrays.asList(e, ex));
                        }
                    }

                    public void onSubscribe(Subscription d) {
                        s.onSubscribe(d);
                    }
                });
            }
        });
    }

    public final Completable onErrorResumeNext(final Func1<? super Throwable, ? extends Completable> errorMapper) {
        requireNonNull(errorMapper);
        return create(new CompletableOnSubscribe() {
            /* class dji.thirdparty.rx.Completable.AnonymousClass24 */

            public void call(final CompletableSubscriber s) {
                final SerialSubscription sd = new SerialSubscription();
                Completable.this.subscribe(new CompletableSubscriber() {
                    /* class dji.thirdparty.rx.Completable.AnonymousClass24.AnonymousClass1 */

                    public void onCompleted() {
                        s.onCompleted();
                    }

                    public void onError(Throwable e) {
                        try {
                            Completable c = (Completable) errorMapper.call(e);
                            if (c == null) {
                                s.onError(new CompositeException(Arrays.asList(e, new NullPointerException("The completable returned is null"))));
                                return;
                            }
                            c.subscribe(new CompletableSubscriber() {
                                /* class dji.thirdparty.rx.Completable.AnonymousClass24.AnonymousClass1.AnonymousClass1 */

                                public void onCompleted() {
                                    s.onCompleted();
                                }

                                public void onError(Throwable e) {
                                    s.onError(e);
                                }

                                public void onSubscribe(Subscription d) {
                                    sd.set(d);
                                }
                            });
                        } catch (Throwable ex) {
                            s.onError(new CompositeException(Arrays.asList(e, ex)));
                        }
                    }

                    public void onSubscribe(Subscription d) {
                        sd.set(d);
                    }
                });
            }
        });
    }

    public final Completable repeat() {
        return fromObservable(toObservable().repeat());
    }

    public final Completable repeat(long times) {
        return fromObservable(toObservable().repeat(times));
    }

    public final Completable repeatWhen(Func1<? super Observable<? extends Void>, ? extends Observable<?>> handler) {
        requireNonNull(handler);
        return fromObservable(toObservable().repeatWhen(handler));
    }

    public final Completable retry() {
        return fromObservable(toObservable().retry());
    }

    public final Completable retry(Func2<Integer, Throwable, Boolean> predicate) {
        return fromObservable(toObservable().retry(predicate));
    }

    public final Completable retry(long times) {
        return fromObservable(toObservable().retry(times));
    }

    public final Completable retryWhen(Func1<? super Observable<? extends Throwable>, ? extends Observable<?>> handler) {
        return fromObservable(toObservable().retryWhen(handler));
    }

    public final Completable startWith(Completable other) {
        requireNonNull(other);
        return concat(other, this);
    }

    public final <T> Observable<T> startWith(Observable<T> other) {
        requireNonNull(other);
        return toObservable().startWith((Observable) other);
    }

    public final Subscription subscribe() {
        final MultipleAssignmentSubscription mad = new MultipleAssignmentSubscription();
        subscribe(new CompletableSubscriber() {
            /* class dji.thirdparty.rx.Completable.AnonymousClass25 */

            public void onCompleted() {
                mad.unsubscribe();
            }

            public void onError(Throwable e) {
                Completable.ERROR_HANDLER.handleError(e);
                mad.unsubscribe();
                Completable.deliverUncaughtException(e);
            }

            public void onSubscribe(Subscription d) {
                mad.set(d);
            }
        });
        return mad;
    }

    public final Subscription subscribe(final Action0 onComplete) {
        requireNonNull(onComplete);
        final MultipleAssignmentSubscription mad = new MultipleAssignmentSubscription();
        subscribe(new CompletableSubscriber() {
            /* class dji.thirdparty.rx.Completable.AnonymousClass26 */

            public void onCompleted() {
                try {
                    onComplete.call();
                } catch (Throwable e) {
                    Completable.ERROR_HANDLER.handleError(e);
                    Completable.deliverUncaughtException(e);
                } finally {
                    mad.unsubscribe();
                }
            }

            public void onError(Throwable e) {
                Completable.ERROR_HANDLER.handleError(e);
                mad.unsubscribe();
                Completable.deliverUncaughtException(e);
            }

            public void onSubscribe(Subscription d) {
                mad.set(d);
            }
        });
        return mad;
    }

    public final Subscription subscribe(final Action1<? super Throwable> onError, final Action0 onComplete) {
        requireNonNull(onError);
        requireNonNull(onComplete);
        final MultipleAssignmentSubscription mad = new MultipleAssignmentSubscription();
        subscribe(new CompletableSubscriber() {
            /* class dji.thirdparty.rx.Completable.AnonymousClass27 */

            public void onCompleted() {
                try {
                    onComplete.call();
                    mad.unsubscribe();
                } catch (Throwable e) {
                    onError(e);
                }
            }

            public void onError(Throwable e) {
                try {
                    onError.call(e);
                    mad.unsubscribe();
                } catch (Throwable th) {
                    th = th;
                    mad.unsubscribe();
                    throw th;
                }
            }

            public void onSubscribe(Subscription d) {
                mad.set(d);
            }
        });
        return mad;
    }

    /* access modifiers changed from: private */
    public static void deliverUncaughtException(Throwable e) {
        Thread thread = Thread.currentThread();
        thread.getUncaughtExceptionHandler().uncaughtException(thread, e);
    }

    public final void subscribe(CompletableSubscriber s) {
        requireNonNull(s);
        try {
            this.onSubscribe.call(s);
        } catch (NullPointerException ex) {
            throw ex;
        } catch (Throwable ex2) {
            ERROR_HANDLER.handleError(ex2);
            throw toNpe(ex2);
        }
    }

    public final <T> void subscribe(Subscriber<T> s) {
        requireNonNull(s);
        final Subscriber<T> subscriber = s;
        if (subscriber == null) {
            try {
                throw new NullPointerException("The RxJavaPlugins.onSubscribe returned a null Subscriber");
            } catch (NullPointerException ex) {
                throw ex;
            } catch (Throwable ex2) {
                ERROR_HANDLER.handleError(ex2);
                throw toNpe(ex2);
            }
        } else {
            subscribe(new CompletableSubscriber() {
                /* class dji.thirdparty.rx.Completable.AnonymousClass28 */

                public void onCompleted() {
                    subscriber.onCompleted();
                }

                public void onError(Throwable e) {
                    subscriber.onError(e);
                }

                public void onSubscribe(Subscription d) {
                    subscriber.add(d);
                }
            });
        }
    }

    public final Completable subscribeOn(final Scheduler scheduler) {
        requireNonNull(scheduler);
        return create(new CompletableOnSubscribe() {
            /* class dji.thirdparty.rx.Completable.AnonymousClass29 */

            public void call(final CompletableSubscriber s) {
                final Scheduler.Worker w = scheduler.createWorker();
                w.schedule(new Action0() {
                    /* class dji.thirdparty.rx.Completable.AnonymousClass29.AnonymousClass1 */

                    public void call() {
                        try {
                            Completable.this.subscribe(s);
                        } finally {
                            w.unsubscribe();
                        }
                    }
                });
            }
        });
    }

    public final Completable timeout(long timeout, TimeUnit unit) {
        return timeout0(timeout, unit, Schedulers.computation(), null);
    }

    public final Completable timeout(long timeout, TimeUnit unit, Completable other) {
        requireNonNull(other);
        return timeout0(timeout, unit, Schedulers.computation(), other);
    }

    public final Completable timeout(long timeout, TimeUnit unit, Scheduler scheduler) {
        return timeout0(timeout, unit, scheduler, null);
    }

    public final Completable timeout(long timeout, TimeUnit unit, Scheduler scheduler, Completable other) {
        requireNonNull(other);
        return timeout0(timeout, unit, scheduler, other);
    }

    public final Completable timeout0(long timeout, TimeUnit unit, Scheduler scheduler, Completable other) {
        requireNonNull(unit);
        requireNonNull(scheduler);
        return create(new CompletableOnSubscribeTimeout(this, timeout, unit, scheduler, other));
    }

    public final <U> U to(Func1<? super Completable, U> converter) {
        return converter.call(this);
    }

    public final <T> Observable<T> toObservable() {
        return Observable.create(new Observable.OnSubscribe<T>() {
            /* class dji.thirdparty.rx.Completable.AnonymousClass30 */

            public /* bridge */ /* synthetic */ void call(Object x0) {
                call((Subscriber) ((Subscriber) x0));
            }

            public void call(Subscriber<? super T> s) {
                Completable.this.subscribe(s);
            }
        });
    }

    public final <T> Single<T> toSingle(final Func0<? extends T> completionValueFunc0) {
        requireNonNull(completionValueFunc0);
        return Single.create(new Single.OnSubscribe<T>() {
            /* class dji.thirdparty.rx.Completable.AnonymousClass31 */

            public /* bridge */ /* synthetic */ void call(Object x0) {
                call((SingleSubscriber) ((SingleSubscriber) x0));
            }

            public void call(final SingleSubscriber<? super T> s) {
                Completable.this.subscribe(new CompletableSubscriber() {
                    /* class dji.thirdparty.rx.Completable.AnonymousClass31.AnonymousClass1 */

                    public void onCompleted() {
                        try {
                            T v = completionValueFunc0.call();
                            if (v == null) {
                                s.onError(new NullPointerException("The value supplied is null"));
                            } else {
                                s.onSuccess(v);
                            }
                        } catch (Throwable e) {
                            s.onError(e);
                        }
                    }

                    public void onError(Throwable e) {
                        s.onError(e);
                    }

                    public void onSubscribe(Subscription d) {
                        s.add(d);
                    }
                });
            }
        });
    }

    public final <T> Single<T> toSingleDefault(final T completionValue) {
        requireNonNull(completionValue);
        return toSingle(new Func0<T>() {
            /* class dji.thirdparty.rx.Completable.AnonymousClass32 */

            public T call() {
                return completionValue;
            }
        });
    }

    public final Completable unsubscribeOn(final Scheduler scheduler) {
        requireNonNull(scheduler);
        return create(new CompletableOnSubscribe() {
            /* class dji.thirdparty.rx.Completable.AnonymousClass33 */

            public void call(final CompletableSubscriber s) {
                Completable.this.subscribe(new CompletableSubscriber() {
                    /* class dji.thirdparty.rx.Completable.AnonymousClass33.AnonymousClass1 */

                    public void onCompleted() {
                        s.onCompleted();
                    }

                    public void onError(Throwable e) {
                        s.onError(e);
                    }

                    public void onSubscribe(final Subscription d) {
                        s.onSubscribe(Subscriptions.create(new Action0() {
                            /* class dji.thirdparty.rx.Completable.AnonymousClass33.AnonymousClass1.AnonymousClass1 */

                            public void call() {
                                final Scheduler.Worker w = scheduler.createWorker();
                                w.schedule(new Action0() {
                                    /* class dji.thirdparty.rx.Completable.AnonymousClass33.AnonymousClass1.AnonymousClass1.AnonymousClass1 */

                                    public void call() {
                                        try {
                                            d.unsubscribe();
                                        } finally {
                                            w.unsubscribe();
                                        }
                                    }
                                });
                            }
                        }));
                    }
                });
            }
        });
    }
}
