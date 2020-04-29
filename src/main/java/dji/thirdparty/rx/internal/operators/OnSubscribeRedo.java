package dji.thirdparty.rx.internal.operators;

import dji.thirdparty.rx.Notification;
import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.Producer;
import dji.thirdparty.rx.Scheduler;
import dji.thirdparty.rx.Subscriber;
import dji.thirdparty.rx.functions.Action0;
import dji.thirdparty.rx.functions.Func1;
import dji.thirdparty.rx.functions.Func2;
import dji.thirdparty.rx.internal.producers.ProducerArbiter;
import dji.thirdparty.rx.observers.Subscribers;
import dji.thirdparty.rx.schedulers.Schedulers;
import dji.thirdparty.rx.subjects.BehaviorSubject;
import dji.thirdparty.rx.subscriptions.SerialSubscription;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import kotlin.jvm.internal.LongCompanionObject;

public final class OnSubscribeRedo<T> implements Observable.OnSubscribe<T> {
    static final Func1<Observable<? extends Notification<?>>, Observable<?>> REDO_INFINITE = new Func1<Observable<? extends Notification<?>>, Observable<?>>() {
        /* class dji.thirdparty.rx.internal.operators.OnSubscribeRedo.AnonymousClass1 */

        public /* bridge */ /* synthetic */ Object call(Object x0) {
            return call((Observable<? extends Notification<?>>) ((Observable) x0));
        }

        public Observable<?> call(Observable<? extends Notification<?>> ts) {
            return ts.map(new Func1<Notification<?>, Notification<?>>() {
                /* class dji.thirdparty.rx.internal.operators.OnSubscribeRedo.AnonymousClass1.AnonymousClass1 */

                public /* bridge */ /* synthetic */ Object call(Object x0) {
                    return call((Notification<?>) ((Notification) x0));
                }

                public Notification<?> call(Notification<?> notification) {
                    return Notification.createOnNext(null);
                }
            });
        }
    };
    private final Func1<? super Observable<? extends Notification<?>>, ? extends Observable<?>> controlHandlerFunction;
    private final Scheduler scheduler;
    final Observable<T> source;
    final boolean stopOnComplete;
    final boolean stopOnError;

    public /* bridge */ /* synthetic */ void call(Object x0) {
        call((Subscriber) ((Subscriber) x0));
    }

    public static final class RedoFinite implements Func1<Observable<? extends Notification<?>>, Observable<?>> {
        final long count;

        public /* bridge */ /* synthetic */ Object call(Object x0) {
            return call((Observable<? extends Notification<?>>) ((Observable) x0));
        }

        public RedoFinite(long count2) {
            this.count = count2;
        }

        public Observable<?> call(Observable<? extends Notification<?>> ts) {
            return ts.map(new Func1<Notification<?>, Notification<?>>() {
                /* class dji.thirdparty.rx.internal.operators.OnSubscribeRedo.RedoFinite.AnonymousClass1 */
                int num = 0;

                public /* bridge */ /* synthetic */ Object call(Object x0) {
                    return call((Notification<?>) ((Notification) x0));
                }

                public Notification<?> call(Notification<?> terminalNotification) {
                    if (RedoFinite.this.count == 0) {
                        return terminalNotification;
                    }
                    this.num++;
                    if (((long) this.num) <= RedoFinite.this.count) {
                        return Notification.createOnNext(Integer.valueOf(this.num));
                    }
                    return terminalNotification;
                }
            }).dematerialize();
        }
    }

    public static final class RetryWithPredicate implements Func1<Observable<? extends Notification<?>>, Observable<? extends Notification<?>>> {
        final Func2<Integer, Throwable, Boolean> predicate;

        public /* bridge */ /* synthetic */ Object call(Object x0) {
            return call((Observable<? extends Notification<?>>) ((Observable) x0));
        }

        public RetryWithPredicate(Func2<Integer, Throwable, Boolean> predicate2) {
            this.predicate = predicate2;
        }

        public Observable<? extends Notification<?>> call(Observable<? extends Notification<?>> ts) {
            return ts.scan(Notification.createOnNext(0), new Func2<Notification<Integer>, Notification<?>, Notification<Integer>>() {
                /* class dji.thirdparty.rx.internal.operators.OnSubscribeRedo.RetryWithPredicate.AnonymousClass1 */

                public /* bridge */ /* synthetic */ Object call(Object x0, Object x1) {
                    return call((Notification<Integer>) ((Notification) x0), (Notification<?>) ((Notification) x1));
                }

                /* JADX WARN: Type inference failed for: r6v0, types: [dji.thirdparty.rx.Notification<?>, dji.thirdparty.rx.Notification, dji.thirdparty.rx.Notification<java.lang.Integer>], assign insn: null */
                /* JADX WARNING: Unknown variable types count: 1 */
                /* Code decompiled incorrectly, please refer to instructions dump. */
                public dji.thirdparty.rx.Notification<java.lang.Integer> call(dji.thirdparty.rx.Notification<java.lang.Integer> r5, dji.thirdparty.rx.Notification<?> r6) {
                    /*
                        r4 = this;
                        java.lang.Object r1 = r5.getValue()
                        java.lang.Integer r1 = (java.lang.Integer) r1
                        int r0 = r1.intValue()
                        dji.thirdparty.rx.internal.operators.OnSubscribeRedo$RetryWithPredicate r1 = dji.thirdparty.rx.internal.operators.OnSubscribeRedo.RetryWithPredicate.this
                        dji.thirdparty.rx.functions.Func2<java.lang.Integer, java.lang.Throwable, java.lang.Boolean> r1 = r1.predicate
                        java.lang.Integer r2 = java.lang.Integer.valueOf(r0)
                        java.lang.Throwable r3 = r6.getThrowable()
                        java.lang.Object r1 = r1.call(r2, r3)
                        java.lang.Boolean r1 = (java.lang.Boolean) r1
                        boolean r1 = r1.booleanValue()
                        if (r1 == 0) goto L_0x002c
                        int r1 = r0 + 1
                        java.lang.Integer r1 = java.lang.Integer.valueOf(r1)
                        dji.thirdparty.rx.Notification r6 = dji.thirdparty.rx.Notification.createOnNext(r1)
                    L_0x002c:
                        return r6
                    */
                    throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.rx.internal.operators.OnSubscribeRedo.RetryWithPredicate.AnonymousClass1.call(dji.thirdparty.rx.Notification, dji.thirdparty.rx.Notification):dji.thirdparty.rx.Notification");
                }
            });
        }
    }

    public static <T> Observable<T> retry(Observable<T> source2) {
        return retry(source2, REDO_INFINITE);
    }

    public static <T> Observable<T> retry(Observable observable, long count) {
        if (count >= 0) {
            return count == 0 ? observable : retry(observable, new RedoFinite(count));
        }
        throw new IllegalArgumentException("count >= 0 expected");
    }

    public static <T> Observable<T> retry(Observable observable, Func1<? super Observable<? extends Notification<?>>, ? extends Observable<?>> notificationHandler) {
        return Observable.create(new OnSubscribeRedo(observable, notificationHandler, true, false, Schedulers.trampoline()));
    }

    public static <T> Observable<T> retry(Observable<T> source2, Func1<? super Observable<? extends Notification<?>>, ? extends Observable<?>> notificationHandler, Scheduler scheduler2) {
        return Observable.create(new OnSubscribeRedo(source2, notificationHandler, true, false, scheduler2));
    }

    public static <T> Observable<T> repeat(Observable<T> source2) {
        return repeat(source2, Schedulers.trampoline());
    }

    public static <T> Observable<T> repeat(Observable observable, Scheduler scheduler2) {
        return repeat(observable, REDO_INFINITE, scheduler2);
    }

    public static <T> Observable<T> repeat(Observable observable, long count) {
        return repeat(observable, count, Schedulers.trampoline());
    }

    public static <T> Observable<T> repeat(Observable observable, long count, Scheduler scheduler2) {
        if (count == 0) {
            return Observable.empty();
        }
        if (count >= 0) {
            return repeat(observable, new RedoFinite(count - 1), scheduler2);
        }
        throw new IllegalArgumentException("count >= 0 expected");
    }

    public static <T> Observable<T> repeat(Observable observable, Func1<? super Observable<? extends Notification<?>>, ? extends Observable<?>> notificationHandler) {
        return Observable.create(new OnSubscribeRedo(observable, notificationHandler, false, true, Schedulers.trampoline()));
    }

    public static <T> Observable<T> repeat(Observable observable, Func1<? super Observable<? extends Notification<?>>, ? extends Observable<?>> notificationHandler, Scheduler scheduler2) {
        return Observable.create(new OnSubscribeRedo(observable, notificationHandler, false, true, scheduler2));
    }

    public static <T> Observable<T> redo(Observable<T> source2, Func1<? super Observable<? extends Notification<?>>, ? extends Observable<?>> notificationHandler, Scheduler scheduler2) {
        return Observable.create(new OnSubscribeRedo(source2, notificationHandler, false, false, scheduler2));
    }

    private OnSubscribeRedo(Observable<T> source2, Func1<? super Observable<? extends Notification<?>>, ? extends Observable<?>> f, boolean stopOnComplete2, boolean stopOnError2, Scheduler scheduler2) {
        this.source = source2;
        this.controlHandlerFunction = f;
        this.stopOnComplete = stopOnComplete2;
        this.stopOnError = stopOnError2;
        this.scheduler = scheduler2;
    }

    public void call(Subscriber<? super T> child) {
        final AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        final AtomicLong consumerCapacity = new AtomicLong();
        final Scheduler.Worker worker = this.scheduler.createWorker();
        child.add(worker);
        final SerialSubscription sourceSubscriptions = new SerialSubscription();
        child.add(sourceSubscriptions);
        final BehaviorSubject<Notification<?>> terminals = BehaviorSubject.create();
        terminals.subscribe((Subscriber) Subscribers.empty());
        final ProducerArbiter arbiter = new ProducerArbiter();
        final Subscriber<? super T> subscriber = child;
        Action0 subscribeToSource = new Action0() {
            /* class dji.thirdparty.rx.internal.operators.OnSubscribeRedo.AnonymousClass2 */

            public void call() {
                if (!subscriber.isUnsubscribed()) {
                    Subscriber<T> terminalDelegatingSubscriber = new Subscriber<T>() {
                        /* class dji.thirdparty.rx.internal.operators.OnSubscribeRedo.AnonymousClass2.AnonymousClass1 */
                        boolean done;

                        public void onCompleted() {
                            if (!this.done) {
                                this.done = true;
                                unsubscribe();
                                terminals.onNext(Notification.createOnCompleted());
                            }
                        }

                        public void onError(Throwable e) {
                            if (!this.done) {
                                this.done = true;
                                unsubscribe();
                                terminals.onNext(Notification.createOnError(e));
                            }
                        }

                        public void onNext(T v) {
                            if (!this.done) {
                                subscriber.onNext(v);
                                decrementConsumerCapacity();
                                arbiter.produced(1);
                            }
                        }

                        private void decrementConsumerCapacity() {
                            long cc;
                            do {
                                cc = consumerCapacity.get();
                                if (cc == LongCompanionObject.MAX_VALUE) {
                                    return;
                                }
                            } while (!consumerCapacity.compareAndSet(cc, cc - 1));
                        }

                        public void setProducer(Producer producer) {
                            arbiter.setProducer(producer);
                        }
                    };
                    sourceSubscriptions.set(terminalDelegatingSubscriber);
                    OnSubscribeRedo.this.source.unsafeSubscribe(terminalDelegatingSubscriber);
                }
            }
        };
        final Observable<?> restarts = (Observable) this.controlHandlerFunction.call(terminals.lift(new Observable.Operator<Notification<?>, Notification<?>>() {
            /* class dji.thirdparty.rx.internal.operators.OnSubscribeRedo.AnonymousClass3 */

            public /* bridge */ /* synthetic */ Object call(Object x0) {
                return call((Subscriber<? super Notification<?>>) ((Subscriber) x0));
            }

            public Subscriber<? super Notification<?>> call(final Subscriber<? super Notification<?>> filteredTerminals) {
                return new Subscriber<Notification<?>>(filteredTerminals) {
                    /* class dji.thirdparty.rx.internal.operators.OnSubscribeRedo.AnonymousClass3.AnonymousClass1 */

                    public /* bridge */ /* synthetic */ void onNext(Object x0) {
                        onNext((Notification<?>) ((Notification) x0));
                    }

                    public void onCompleted() {
                        filteredTerminals.onCompleted();
                    }

                    public void onError(Throwable e) {
                        filteredTerminals.onError(e);
                    }

                    public void onNext(Notification<?> t) {
                        if (t.isOnCompleted() && OnSubscribeRedo.this.stopOnComplete) {
                            filteredTerminals.onCompleted();
                        } else if (!t.isOnError() || !OnSubscribeRedo.this.stopOnError) {
                            filteredTerminals.onNext(t);
                        } else {
                            filteredTerminals.onError(t.getThrowable());
                        }
                    }

                    public void setProducer(Producer producer) {
                        producer.request(LongCompanionObject.MAX_VALUE);
                    }
                };
            }
        }));
        final Subscriber<? super T> subscriber2 = child;
        final AtomicLong atomicLong = consumerCapacity;
        final Action0 action0 = subscribeToSource;
        worker.schedule(new Action0() {
            /* class dji.thirdparty.rx.internal.operators.OnSubscribeRedo.AnonymousClass4 */

            public void call() {
                restarts.unsafeSubscribe(new Subscriber<Object>(subscriber2) {
                    /* class dji.thirdparty.rx.internal.operators.OnSubscribeRedo.AnonymousClass4.AnonymousClass1 */

                    public void onCompleted() {
                        subscriber2.onCompleted();
                    }

                    public void onError(Throwable e) {
                        subscriber2.onError(e);
                    }

                    public void onNext(Object t) {
                        if (subscriber2.isUnsubscribed()) {
                            return;
                        }
                        if (atomicLong.get() > 0) {
                            worker.schedule(action0);
                        } else {
                            atomicBoolean.compareAndSet(false, true);
                        }
                    }

                    public void setProducer(Producer producer) {
                        producer.request(LongCompanionObject.MAX_VALUE);
                    }
                });
            }
        });
        final AtomicLong atomicLong2 = consumerCapacity;
        final ProducerArbiter producerArbiter = arbiter;
        final AtomicBoolean atomicBoolean2 = atomicBoolean;
        final Scheduler.Worker worker2 = worker;
        final Action0 action02 = subscribeToSource;
        child.setProducer(new Producer() {
            /* class dji.thirdparty.rx.internal.operators.OnSubscribeRedo.AnonymousClass5 */

            public void request(long n) {
                if (n > 0) {
                    BackpressureUtils.getAndAddRequest(atomicLong2, n);
                    producerArbiter.request(n);
                    if (atomicBoolean2.compareAndSet(true, false)) {
                        worker2.schedule(action02);
                    }
                }
            }
        });
    }
}
