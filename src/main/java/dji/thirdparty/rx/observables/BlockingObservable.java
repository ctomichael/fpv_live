package dji.thirdparty.rx.observables;

import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.Observer;
import dji.thirdparty.rx.Producer;
import dji.thirdparty.rx.Subscriber;
import dji.thirdparty.rx.Subscription;
import dji.thirdparty.rx.annotations.Experimental;
import dji.thirdparty.rx.exceptions.OnErrorNotImplementedException;
import dji.thirdparty.rx.functions.Action0;
import dji.thirdparty.rx.functions.Action1;
import dji.thirdparty.rx.functions.Actions;
import dji.thirdparty.rx.functions.Func1;
import dji.thirdparty.rx.internal.operators.BlockingOperatorLatest;
import dji.thirdparty.rx.internal.operators.BlockingOperatorMostRecent;
import dji.thirdparty.rx.internal.operators.BlockingOperatorNext;
import dji.thirdparty.rx.internal.operators.BlockingOperatorToFuture;
import dji.thirdparty.rx.internal.operators.BlockingOperatorToIterator;
import dji.thirdparty.rx.internal.operators.NotificationLite;
import dji.thirdparty.rx.internal.util.BlockingUtils;
import dji.thirdparty.rx.internal.util.UtilityFunctions;
import dji.thirdparty.rx.subscriptions.Subscriptions;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicReference;

public final class BlockingObservable<T> {
    static final Object ON_START = new Object();
    static final Object SET_PRODUCER = new Object();
    static final Object UNSUBSCRIBE = new Object();
    private final Observable<? extends T> o;

    private BlockingObservable(Observable<? extends T> o2) {
        this.o = o2;
    }

    public static <T> BlockingObservable<T> from(Observable<? extends T> o2) {
        return new BlockingObservable<>(o2);
    }

    public void forEach(final Action1<? super T> onNext) {
        final CountDownLatch latch = new CountDownLatch(1);
        final AtomicReference<Throwable> exceptionFromOnError = new AtomicReference<>();
        BlockingUtils.awaitForComplete(latch, this.o.subscribe((Subscriber) new Subscriber<T>() {
            /* class dji.thirdparty.rx.observables.BlockingObservable.AnonymousClass1 */

            public void onCompleted() {
                latch.countDown();
            }

            public void onError(Throwable e) {
                exceptionFromOnError.set(e);
                latch.countDown();
            }

            public void onNext(T args) {
                onNext.call(args);
            }
        }));
        if (exceptionFromOnError.get() == null) {
            return;
        }
        if (exceptionFromOnError.get() instanceof RuntimeException) {
            throw ((RuntimeException) exceptionFromOnError.get());
        }
        throw new RuntimeException((Throwable) exceptionFromOnError.get());
    }

    public Iterator<T> getIterator() {
        return BlockingOperatorToIterator.toIterator(this.o);
    }

    public T first() {
        return blockForSingle(this.o.first());
    }

    public T first(Func1<? super T, Boolean> predicate) {
        return blockForSingle(this.o.first(predicate));
    }

    public T firstOrDefault(T defaultValue) {
        return blockForSingle(this.o.map(UtilityFunctions.identity()).firstOrDefault(defaultValue));
    }

    /* JADX WARN: Type inference failed for: r4v0, types: [dji.thirdparty.rx.functions.Func1, dji.thirdparty.rx.functions.Func1<? super T, java.lang.Boolean>], assign insn: null */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public T firstOrDefault(T r3, dji.thirdparty.rx.functions.Func1<? super T, java.lang.Boolean> r4) {
        /*
            r2 = this;
            dji.thirdparty.rx.Observable<? extends T> r0 = r2.o
            dji.thirdparty.rx.Observable r0 = r0.filter(r4)
            dji.thirdparty.rx.functions.Func1 r1 = dji.thirdparty.rx.internal.util.UtilityFunctions.identity()
            dji.thirdparty.rx.Observable r0 = r0.map(r1)
            dji.thirdparty.rx.Observable r0 = r0.firstOrDefault(r3)
            java.lang.Object r0 = r2.blockForSingle(r0)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.rx.observables.BlockingObservable.firstOrDefault(java.lang.Object, dji.thirdparty.rx.functions.Func1):java.lang.Object");
    }

    public T last() {
        return blockForSingle(this.o.last());
    }

    public T last(Func1<? super T, Boolean> predicate) {
        return blockForSingle(this.o.last(predicate));
    }

    public T lastOrDefault(T defaultValue) {
        return blockForSingle(this.o.map(UtilityFunctions.identity()).lastOrDefault(defaultValue));
    }

    /* JADX WARN: Type inference failed for: r4v0, types: [dji.thirdparty.rx.functions.Func1, dji.thirdparty.rx.functions.Func1<? super T, java.lang.Boolean>], assign insn: null */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public T lastOrDefault(T r3, dji.thirdparty.rx.functions.Func1<? super T, java.lang.Boolean> r4) {
        /*
            r2 = this;
            dji.thirdparty.rx.Observable<? extends T> r0 = r2.o
            dji.thirdparty.rx.Observable r0 = r0.filter(r4)
            dji.thirdparty.rx.functions.Func1 r1 = dji.thirdparty.rx.internal.util.UtilityFunctions.identity()
            dji.thirdparty.rx.Observable r0 = r0.map(r1)
            dji.thirdparty.rx.Observable r0 = r0.lastOrDefault(r3)
            java.lang.Object r0 = r2.blockForSingle(r0)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.rx.observables.BlockingObservable.lastOrDefault(java.lang.Object, dji.thirdparty.rx.functions.Func1):java.lang.Object");
    }

    public Iterable<T> mostRecent(T initialValue) {
        return BlockingOperatorMostRecent.mostRecent(this.o, initialValue);
    }

    public Iterable<T> next() {
        return BlockingOperatorNext.next(this.o);
    }

    public Iterable<T> latest() {
        return BlockingOperatorLatest.latest(this.o);
    }

    public T single() {
        return blockForSingle(this.o.single());
    }

    public T single(Func1<? super T, Boolean> predicate) {
        return blockForSingle(this.o.single(predicate));
    }

    public T singleOrDefault(T defaultValue) {
        return blockForSingle(this.o.map(UtilityFunctions.identity()).singleOrDefault(defaultValue));
    }

    /* JADX WARN: Type inference failed for: r4v0, types: [dji.thirdparty.rx.functions.Func1, dji.thirdparty.rx.functions.Func1<? super T, java.lang.Boolean>], assign insn: null */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public T singleOrDefault(T r3, dji.thirdparty.rx.functions.Func1<? super T, java.lang.Boolean> r4) {
        /*
            r2 = this;
            dji.thirdparty.rx.Observable<? extends T> r0 = r2.o
            dji.thirdparty.rx.Observable r0 = r0.filter(r4)
            dji.thirdparty.rx.functions.Func1 r1 = dji.thirdparty.rx.internal.util.UtilityFunctions.identity()
            dji.thirdparty.rx.Observable r0 = r0.map(r1)
            dji.thirdparty.rx.Observable r0 = r0.singleOrDefault(r3)
            java.lang.Object r0 = r2.blockForSingle(r0)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.rx.observables.BlockingObservable.singleOrDefault(java.lang.Object, dji.thirdparty.rx.functions.Func1):java.lang.Object");
    }

    public Future<T> toFuture() {
        return BlockingOperatorToFuture.toFuture(this.o);
    }

    public Iterable<T> toIterable() {
        return new Iterable<T>() {
            /* class dji.thirdparty.rx.observables.BlockingObservable.AnonymousClass2 */

            public Iterator<T> iterator() {
                return BlockingObservable.this.getIterator();
            }
        };
    }

    private T blockForSingle(Observable<? extends T> observable) {
        final AtomicReference<T> returnItem = new AtomicReference<>();
        final AtomicReference<Throwable> returnException = new AtomicReference<>();
        final CountDownLatch latch = new CountDownLatch(1);
        BlockingUtils.awaitForComplete(latch, observable.subscribe((Subscriber) new Subscriber<T>() {
            /* class dji.thirdparty.rx.observables.BlockingObservable.AnonymousClass3 */

            public void onCompleted() {
                latch.countDown();
            }

            public void onError(Throwable e) {
                returnException.set(e);
                latch.countDown();
            }

            public void onNext(T item) {
                returnItem.set(item);
            }
        }));
        if (returnException.get() == null) {
            return returnItem.get();
        }
        if (returnException.get() instanceof RuntimeException) {
            throw ((RuntimeException) returnException.get());
        }
        throw new RuntimeException((Throwable) returnException.get());
    }

    @Experimental
    public void subscribe() {
        final CountDownLatch cdl = new CountDownLatch(1);
        final Throwable[] error = {null};
        BlockingUtils.awaitForComplete(cdl, this.o.subscribe((Subscriber) new Subscriber<T>() {
            /* class dji.thirdparty.rx.observables.BlockingObservable.AnonymousClass4 */

            public void onNext(T t) {
            }

            public void onError(Throwable e) {
                error[0] = e;
                cdl.countDown();
            }

            public void onCompleted() {
                cdl.countDown();
            }
        }));
        Throwable e = error[0];
        if (e == null) {
            return;
        }
        if (e instanceof RuntimeException) {
            throw ((RuntimeException) e);
        }
        throw new RuntimeException(e);
    }

    @Experimental
    public void subscribe(Observer<? super T> observer) {
        Object o2;
        final NotificationLite<T> nl = NotificationLite.instance();
        final BlockingQueue<Object> queue = new LinkedBlockingQueue<>();
        Subscription s = this.o.subscribe((Subscriber) new Subscriber<T>() {
            /* class dji.thirdparty.rx.observables.BlockingObservable.AnonymousClass5 */

            public void onNext(T t) {
                queue.offer(nl.next(t));
            }

            public void onError(Throwable e) {
                queue.offer(nl.error(e));
            }

            public void onCompleted() {
                queue.offer(nl.completed());
            }
        });
        do {
            try {
                o2 = queue.poll();
                if (o2 == null) {
                    o2 = queue.take();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                observer.onError(e);
                return;
            } finally {
                s.unsubscribe();
            }
        } while (!nl.accept(observer, o2));
    }

    @Experimental
    public void subscribe(Subscriber<? super T> subscriber) {
        final NotificationLite<T> nl = NotificationLite.instance();
        final BlockingQueue<Object> queue = new LinkedBlockingQueue<>();
        final Producer[] theProducer = {null};
        Subscriber<T> s = new Subscriber<T>() {
            /* class dji.thirdparty.rx.observables.BlockingObservable.AnonymousClass6 */

            public void onNext(T t) {
                queue.offer(nl.next(t));
            }

            public void onError(Throwable e) {
                queue.offer(nl.error(e));
            }

            public void onCompleted() {
                queue.offer(nl.completed());
            }

            public void setProducer(Producer p) {
                theProducer[0] = p;
                queue.offer(BlockingObservable.SET_PRODUCER);
            }

            public void onStart() {
                queue.offer(BlockingObservable.ON_START);
            }
        };
        subscriber.add(s);
        subscriber.add(Subscriptions.create(new Action0() {
            /* class dji.thirdparty.rx.observables.BlockingObservable.AnonymousClass7 */

            public void call() {
                queue.offer(BlockingObservable.UNSUBSCRIBE);
            }
        }));
        this.o.subscribe((Subscriber) s);
        while (!subscriber.isUnsubscribed()) {
            try {
                Object o2 = queue.poll();
                if (o2 == null) {
                    o2 = queue.take();
                }
                if (subscriber.isUnsubscribed() || o2 == UNSUBSCRIBE) {
                    break;
                } else if (o2 == ON_START) {
                    subscriber.onStart();
                } else if (o2 == SET_PRODUCER) {
                    subscriber.setProducer(theProducer[0]);
                } else if (nl.accept(subscriber, o2)) {
                    return;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                subscriber.onError(e);
                return;
            } finally {
                s.unsubscribe();
            }
        }
        s.unsubscribe();
    }

    @Experimental
    public void subscribe(Action1<? super T> onNext) {
        subscribe(onNext, new Action1<Throwable>() {
            /* class dji.thirdparty.rx.observables.BlockingObservable.AnonymousClass8 */

            public void call(Throwable t) {
                throw new OnErrorNotImplementedException(t);
            }
        }, Actions.empty());
    }

    @Experimental
    public void subscribe(Action1<? super T> onNext, Action1<? super Throwable> onError) {
        subscribe(onNext, onError, Actions.empty());
    }

    @Experimental
    public void subscribe(final Action1<? super T> onNext, final Action1<? super Throwable> onError, final Action0 onCompleted) {
        subscribe(new Observer<T>() {
            /* class dji.thirdparty.rx.observables.BlockingObservable.AnonymousClass9 */

            public void onNext(T t) {
                onNext.call(t);
            }

            public void onError(Throwable e) {
                onError.call(e);
            }

            public void onCompleted() {
                onCompleted.call();
            }
        });
    }
}
