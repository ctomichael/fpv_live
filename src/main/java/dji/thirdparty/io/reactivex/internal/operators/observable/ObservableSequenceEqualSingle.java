package dji.thirdparty.io.reactivex.internal.operators.observable;

import dji.thirdparty.io.reactivex.Observable;
import dji.thirdparty.io.reactivex.ObservableSource;
import dji.thirdparty.io.reactivex.Observer;
import dji.thirdparty.io.reactivex.Single;
import dji.thirdparty.io.reactivex.SingleObserver;
import dji.thirdparty.io.reactivex.disposables.Disposable;
import dji.thirdparty.io.reactivex.exceptions.Exceptions;
import dji.thirdparty.io.reactivex.functions.BiPredicate;
import dji.thirdparty.io.reactivex.internal.disposables.ArrayCompositeDisposable;
import dji.thirdparty.io.reactivex.internal.fuseable.FuseToObservable;
import dji.thirdparty.io.reactivex.internal.queue.SpscLinkedArrayQueue;
import dji.thirdparty.io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicInteger;

public final class ObservableSequenceEqualSingle<T> extends Single<Boolean> implements FuseToObservable<Boolean> {
    final int bufferSize;
    final BiPredicate<? super T, ? super T> comparer;
    final ObservableSource<? extends T> first;
    final ObservableSource<? extends T> second;

    public ObservableSequenceEqualSingle(ObservableSource<? extends T> first2, ObservableSource<? extends T> second2, BiPredicate<? super T, ? super T> comparer2, int bufferSize2) {
        this.first = first2;
        this.second = second2;
        this.comparer = comparer2;
        this.bufferSize = bufferSize2;
    }

    public void subscribeActual(SingleObserver<? super Boolean> s) {
        EqualCoordinator<T> ec = new EqualCoordinator<>(s, this.bufferSize, this.first, this.second, this.comparer);
        s.onSubscribe(ec);
        ec.subscribe();
    }

    public Observable<Boolean> fuseToObservable() {
        return RxJavaPlugins.onAssembly(new ObservableSequenceEqual(this.first, this.second, this.comparer, this.bufferSize));
    }

    static final class EqualCoordinator<T> extends AtomicInteger implements Disposable {
        private static final long serialVersionUID = -6178010334400373240L;
        final SingleObserver<? super Boolean> actual;
        volatile boolean cancelled;
        final BiPredicate<? super T, ? super T> comparer;
        final ObservableSource<? extends T> first;
        final EqualObserver<T>[] observers;
        final ArrayCompositeDisposable resources = new ArrayCompositeDisposable(2);
        final ObservableSource<? extends T> second;
        T v1;
        T v2;

        EqualCoordinator(SingleObserver<? super Boolean> actual2, int bufferSize, ObservableSource<? extends T> first2, ObservableSource<? extends T> second2, BiPredicate<? super T, ? super T> comparer2) {
            this.actual = actual2;
            this.first = first2;
            this.second = second2;
            this.comparer = comparer2;
            EqualObserver<T>[] as = new EqualObserver[2];
            this.observers = as;
            as[0] = new EqualObserver<>(this, 0, bufferSize);
            as[1] = new EqualObserver<>(this, 1, bufferSize);
        }

        /* access modifiers changed from: package-private */
        public boolean setDisposable(Disposable s, int index) {
            return this.resources.setResource(index, s);
        }

        /* access modifiers changed from: package-private */
        public void subscribe() {
            EqualObserver<T>[] as = this.observers;
            this.first.subscribe(as[0]);
            this.second.subscribe(as[1]);
        }

        public void dispose() {
            if (!this.cancelled) {
                this.cancelled = true;
                this.resources.dispose();
                if (getAndIncrement() == 0) {
                    EqualObserver<T>[] as = this.observers;
                    as[0].queue.clear();
                    as[1].queue.clear();
                }
            }
        }

        public boolean isDisposed() {
            return this.cancelled;
        }

        /* access modifiers changed from: package-private */
        public void cancel(SpscLinkedArrayQueue<T> q1, SpscLinkedArrayQueue<T> q2) {
            this.cancelled = true;
            q1.clear();
            q2.clear();
        }

        /* access modifiers changed from: package-private */
        public void drain() {
            Throwable e;
            Throwable e2;
            if (getAndIncrement() == 0) {
                int missed = 1;
                EqualObserver<T>[] as = this.observers;
                EqualObserver<T> s1 = as[0];
                SpscLinkedArrayQueue<T> q1 = s1.queue;
                EqualObserver<T> s2 = as[1];
                SpscLinkedArrayQueue<T> q2 = s2.queue;
                while (!this.cancelled) {
                    boolean d1 = s1.done;
                    if (!d1 || (e2 = s1.error) == null) {
                        boolean d2 = s2.done;
                        if (!d2 || (e = s2.error) == null) {
                            if (this.v1 == null) {
                                this.v1 = q1.poll();
                            }
                            boolean e1 = this.v1 == null;
                            if (this.v2 == null) {
                                this.v2 = q2.poll();
                            }
                            boolean e22 = this.v2 == null;
                            if (d1 && d2 && e1 && e22) {
                                this.actual.onSuccess(true);
                                return;
                            } else if (!d1 || !d2 || e1 == e22) {
                                if (!e1 && !e22) {
                                    try {
                                        if (!this.comparer.test(this.v1, this.v2)) {
                                            cancel(q1, q2);
                                            this.actual.onSuccess(false);
                                            return;
                                        }
                                        this.v1 = null;
                                        this.v2 = null;
                                    } catch (Throwable ex) {
                                        Exceptions.throwIfFatal(ex);
                                        cancel(q1, q2);
                                        this.actual.onError(ex);
                                        return;
                                    }
                                }
                                if ((e1 || e22) && (missed = addAndGet(-missed)) == 0) {
                                    return;
                                }
                            } else {
                                cancel(q1, q2);
                                this.actual.onSuccess(false);
                                return;
                            }
                        } else {
                            cancel(q1, q2);
                            this.actual.onError(e);
                            return;
                        }
                    } else {
                        cancel(q1, q2);
                        this.actual.onError(e2);
                        return;
                    }
                }
                q1.clear();
                q2.clear();
            }
        }
    }

    static final class EqualObserver<T> implements Observer<T> {
        volatile boolean done;
        Throwable error;
        final int index;
        final EqualCoordinator<T> parent;
        final SpscLinkedArrayQueue<T> queue;

        EqualObserver(EqualCoordinator<T> parent2, int index2, int bufferSize) {
            this.parent = parent2;
            this.index = index2;
            this.queue = new SpscLinkedArrayQueue<>(bufferSize);
        }

        public void onSubscribe(Disposable s) {
            this.parent.setDisposable(s, this.index);
        }

        public void onNext(T t) {
            this.queue.offer(t);
            this.parent.drain();
        }

        public void onError(Throwable t) {
            this.error = t;
            this.done = true;
            this.parent.drain();
        }

        public void onComplete() {
            this.done = true;
            this.parent.drain();
        }
    }
}
