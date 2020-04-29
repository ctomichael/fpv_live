package dji.thirdparty.io.reactivex.internal.operators.observable;

import dji.thirdparty.io.reactivex.ObservableSource;
import dji.thirdparty.io.reactivex.Observer;
import dji.thirdparty.io.reactivex.disposables.Disposable;
import dji.thirdparty.io.reactivex.exceptions.Exceptions;
import dji.thirdparty.io.reactivex.functions.Function;
import dji.thirdparty.io.reactivex.internal.disposables.DisposableHelper;
import dji.thirdparty.io.reactivex.internal.functions.ObjectHelper;
import dji.thirdparty.io.reactivex.internal.fuseable.QueueDisposable;
import dji.thirdparty.io.reactivex.internal.fuseable.SimplePlainQueue;
import dji.thirdparty.io.reactivex.internal.fuseable.SimpleQueue;
import dji.thirdparty.io.reactivex.internal.queue.SpscArrayQueue;
import dji.thirdparty.io.reactivex.internal.queue.SpscLinkedArrayQueue;
import dji.thirdparty.io.reactivex.internal.util.AtomicThrowable;
import dji.thirdparty.io.reactivex.internal.util.ExceptionHelper;
import dji.thirdparty.io.reactivex.plugins.RxJavaPlugins;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservableFlatMap<T, U> extends AbstractObservableWithUpstream<T, U> {
    final int bufferSize;
    final boolean delayErrors;
    final Function<? super T, ? extends ObservableSource<? extends U>> mapper;
    final int maxConcurrency;

    public ObservableFlatMap(ObservableSource<T> source, Function<? super T, ? extends ObservableSource<? extends U>> mapper2, boolean delayErrors2, int maxConcurrency2, int bufferSize2) {
        super(source);
        this.mapper = mapper2;
        this.delayErrors = delayErrors2;
        this.maxConcurrency = maxConcurrency2;
        this.bufferSize = bufferSize2;
    }

    public void subscribeActual(Observer<? super U> t) {
        if (!ObservableScalarXMap.tryScalarXMapSubscribe(this.source, t, this.mapper)) {
            this.source.subscribe(new MergeObserver(t, this.mapper, this.delayErrors, this.maxConcurrency, this.bufferSize));
        }
    }

    static final class MergeObserver<T, U> extends AtomicInteger implements Disposable, Observer<T> {
        static final InnerObserver<?, ?>[] CANCELLED = new InnerObserver[0];
        static final InnerObserver<?, ?>[] EMPTY = new InnerObserver[0];
        private static final long serialVersionUID = -2117620485640801370L;
        final Observer<? super U> actual;
        final int bufferSize;
        volatile boolean cancelled;
        final boolean delayErrors;
        volatile boolean done;
        final AtomicThrowable errors = new AtomicThrowable();
        long lastId;
        int lastIndex;
        final Function<? super T, ? extends ObservableSource<? extends U>> mapper;
        final int maxConcurrency;
        final AtomicReference<InnerObserver<?, ?>[]> observers;
        volatile SimplePlainQueue<U> queue;
        Disposable s;
        Queue<ObservableSource<? extends U>> sources;
        long uniqueId;
        int wip;

        MergeObserver(Observer<? super U> actual2, Function<? super T, ? extends ObservableSource<? extends U>> mapper2, boolean delayErrors2, int maxConcurrency2, int bufferSize2) {
            this.actual = actual2;
            this.mapper = mapper2;
            this.delayErrors = delayErrors2;
            this.maxConcurrency = maxConcurrency2;
            this.bufferSize = bufferSize2;
            if (maxConcurrency2 != Integer.MAX_VALUE) {
                this.sources = new ArrayDeque(maxConcurrency2);
            }
            this.observers = new AtomicReference<>(EMPTY);
        }

        public void onSubscribe(Disposable s2) {
            if (DisposableHelper.validate(this.s, s2)) {
                this.s = s2;
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                try {
                    ObservableSource<? extends U> p = (ObservableSource) ObjectHelper.requireNonNull(this.mapper.apply(t), "The mapper returned a null ObservableSource");
                    if (this.maxConcurrency != Integer.MAX_VALUE) {
                        synchronized (this) {
                            if (this.wip == this.maxConcurrency) {
                                this.sources.offer(p);
                                return;
                            }
                            this.wip++;
                        }
                    }
                    subscribeInner(p);
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    this.s.dispose();
                    onError(e);
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void subscribeInner(ObservableSource<? extends U> p) {
            while (p instanceof Callable) {
                tryEmitScalar((Callable) p);
                if (this.maxConcurrency != Integer.MAX_VALUE) {
                    synchronized (this) {
                        p = this.sources.poll();
                        if (p == null) {
                            this.wip--;
                            return;
                        }
                    }
                } else {
                    return;
                }
            }
            long j = this.uniqueId;
            this.uniqueId = 1 + j;
            InnerObserver<T, U> inner = new InnerObserver<>(this, j);
            addInner(inner);
            p.subscribe(inner);
        }

        /* access modifiers changed from: package-private */
        public void addInner(InnerObserver<T, U> inner) {
            InnerObserver<?, ?>[] a;
            InnerObserver[] innerObserverArr;
            do {
                a = (InnerObserver[]) this.observers.get();
                if (a == CANCELLED) {
                    inner.dispose();
                    return;
                }
                int n = a.length;
                innerObserverArr = new InnerObserver[(n + 1)];
                System.arraycopy(a, 0, innerObserverArr, 0, n);
                innerObserverArr[n] = inner;
            } while (!this.observers.compareAndSet(a, innerObserverArr));
        }

        /* access modifiers changed from: package-private */
        public void removeInner(InnerObserver<T, U> inner) {
            InnerObserver<?, ?>[] a;
            InnerObserver<?, ?>[] b;
            do {
                a = (InnerObserver[]) this.observers.get();
                int n = a.length;
                if (n != 0) {
                    int j = -1;
                    int i = 0;
                    while (true) {
                        if (i >= n) {
                            break;
                        } else if (a[i] == inner) {
                            j = i;
                            break;
                        } else {
                            i++;
                        }
                    }
                    if (j < 0) {
                        return;
                    }
                    if (n == 1) {
                        b = EMPTY;
                    } else {
                        b = new InnerObserver[(n - 1)];
                        System.arraycopy(a, 0, b, 0, j);
                        System.arraycopy(a, j + 1, b, j, (n - j) - 1);
                    }
                } else {
                    return;
                }
            } while (!this.observers.compareAndSet(a, b));
        }

        /* access modifiers changed from: package-private */
        public void tryEmitScalar(Callable<? extends U> value) {
            try {
                U u = value.call();
                if (u != null) {
                    if (get() != 0 || !compareAndSet(0, 1)) {
                        SimplePlainQueue<U> q = this.queue;
                        if (q == null) {
                            if (this.maxConcurrency == Integer.MAX_VALUE) {
                                q = new SpscLinkedArrayQueue<>(this.bufferSize);
                            } else {
                                q = new SpscArrayQueue<>(this.maxConcurrency);
                            }
                            this.queue = q;
                        }
                        if (!q.offer(u)) {
                            onError(new IllegalStateException("Scalar queue full?!"));
                            return;
                        } else if (getAndIncrement() != 0) {
                            return;
                        }
                    } else {
                        this.actual.onNext(u);
                        if (decrementAndGet() == 0) {
                            return;
                        }
                    }
                    drainLoop();
                }
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                this.errors.addThrowable(ex);
                drain();
            }
        }

        /* access modifiers changed from: package-private */
        public void tryEmit(U value, InnerObserver<T, U> inner) {
            if (get() != 0 || !compareAndSet(0, 1)) {
                SimpleQueue<U> q = inner.queue;
                if (q == null) {
                    q = new SpscLinkedArrayQueue<>(this.bufferSize);
                    inner.queue = q;
                }
                q.offer(value);
                if (getAndIncrement() != 0) {
                    return;
                }
            } else {
                this.actual.onNext(value);
                if (decrementAndGet() == 0) {
                    return;
                }
            }
            drainLoop();
        }

        public void onError(Throwable t) {
            if (this.done) {
                RxJavaPlugins.onError(t);
            } else if (this.errors.addThrowable(t)) {
                this.done = true;
                drain();
            } else {
                RxJavaPlugins.onError(t);
            }
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                drain();
            }
        }

        public void dispose() {
            Throwable ex;
            if (!this.cancelled) {
                this.cancelled = true;
                if (disposeAll() && (ex = this.errors.terminate()) != null && ex != ExceptionHelper.TERMINATED) {
                    RxJavaPlugins.onError(ex);
                }
            }
        }

        public boolean isDisposed() {
            return this.cancelled;
        }

        /* access modifiers changed from: package-private */
        public void drain() {
            if (getAndIncrement() == 0) {
                drainLoop();
            }
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: Code restructure failed: missing block: B:65:0x0106, code lost:
            if (r17 != null) goto L_0x00c6;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void drainLoop() {
            /*
                r26 = this;
                r0 = r26
                dji.thirdparty.io.reactivex.Observer<? super U> r4 = r0.actual
                r15 = 1
            L_0x0005:
                boolean r23 = r26.checkTerminate()
                if (r23 == 0) goto L_0x000c
            L_0x000b:
                return
            L_0x000c:
                r0 = r26
                dji.thirdparty.io.reactivex.internal.fuseable.SimplePlainQueue<U> r0 = r0.queue
                r22 = r0
                if (r22 == 0) goto L_0x0022
            L_0x0014:
                boolean r23 = r26.checkTerminate()
                if (r23 != 0) goto L_0x000b
                java.lang.Object r17 = r22.poll()
                if (r17 != 0) goto L_0x0059
                if (r17 != 0) goto L_0x0014
            L_0x0022:
                r0 = r26
                boolean r5 = r0.done
                r0 = r26
                dji.thirdparty.io.reactivex.internal.fuseable.SimplePlainQueue<U> r0 = r0.queue
                r22 = r0
                r0 = r26
                java.util.concurrent.atomic.AtomicReference<dji.thirdparty.io.reactivex.internal.operators.observable.ObservableFlatMap$InnerObserver<?, ?>[]> r0 = r0.observers
                r23 = r0
                java.lang.Object r9 = r23.get()
                dji.thirdparty.io.reactivex.internal.operators.observable.ObservableFlatMap$InnerObserver[] r9 = (dji.thirdparty.io.reactivex.internal.operators.observable.ObservableFlatMap.InnerObserver[]) r9
                int r0 = r9.length
                r16 = r0
                if (r5 == 0) goto L_0x006f
                if (r22 == 0) goto L_0x0045
                boolean r23 = r22.isEmpty()
                if (r23 == 0) goto L_0x006f
            L_0x0045:
                if (r16 != 0) goto L_0x006f
                r0 = r26
                dji.thirdparty.io.reactivex.internal.util.AtomicThrowable r0 = r0.errors
                r23 = r0
                java.lang.Object r6 = r23.get()
                java.lang.Throwable r6 = (java.lang.Throwable) r6
                if (r6 != 0) goto L_0x005f
                r4.onComplete()
                goto L_0x000b
            L_0x0059:
                r0 = r17
                r4.onNext(r0)
                goto L_0x0014
            L_0x005f:
                r0 = r26
                dji.thirdparty.io.reactivex.internal.util.AtomicThrowable r0 = r0.errors
                r23 = r0
                java.lang.Throwable r23 = r23.terminate()
                r0 = r23
                r4.onError(r0)
                goto L_0x000b
            L_0x006f:
                r10 = 0
                if (r16 == 0) goto L_0x0149
                r0 = r26
                long r0 = r0.lastId
                r20 = r0
                r0 = r26
                int r8 = r0.lastIndex
                r0 = r16
                if (r0 <= r8) goto L_0x008c
                r23 = r9[r8]
                r0 = r23
                long r0 = r0.id
                r24 = r0
                int r23 = (r24 > r20 ? 1 : (r24 == r20 ? 0 : -1))
                if (r23 == 0) goto L_0x00b6
            L_0x008c:
                r0 = r16
                if (r0 > r8) goto L_0x0091
                r8 = 0
            L_0x0091:
                r14 = r8
                r7 = 0
            L_0x0093:
                r0 = r16
                if (r7 >= r0) goto L_0x00a3
                r23 = r9[r14]
                r0 = r23
                long r0 = r0.id
                r24 = r0
                int r23 = (r24 > r20 ? 1 : (r24 == r20 ? 0 : -1))
                if (r23 != 0) goto L_0x00f6
            L_0x00a3:
                r8 = r14
                r0 = r26
                r0.lastIndex = r14
                r23 = r9[r14]
                r0 = r23
                long r0 = r0.id
                r24 = r0
                r0 = r24
                r2 = r26
                r2.lastId = r0
            L_0x00b6:
                r14 = r8
                r7 = 0
            L_0x00b8:
                r0 = r16
                if (r7 >= r0) goto L_0x0137
                boolean r23 = r26.checkTerminate()
                if (r23 != 0) goto L_0x000b
                r13 = r9[r14]
                r17 = 0
            L_0x00c6:
                boolean r23 = r26.checkTerminate()
                if (r23 != 0) goto L_0x000b
                dji.thirdparty.io.reactivex.internal.fuseable.SimpleQueue<U> r0 = r13.queue
                r19 = r0
                if (r19 != 0) goto L_0x0100
            L_0x00d2:
                boolean r11 = r13.done
                dji.thirdparty.io.reactivex.internal.fuseable.SimpleQueue<U> r12 = r13.queue
                if (r11 == 0) goto L_0x00ec
                if (r12 == 0) goto L_0x00e0
                boolean r23 = r12.isEmpty()
                if (r23 == 0) goto L_0x00ec
            L_0x00e0:
                r0 = r26
                r0.removeInner(r13)
                boolean r23 = r26.checkTerminate()
                if (r23 != 0) goto L_0x000b
                r10 = 1
            L_0x00ec:
                int r14 = r14 + 1
                r0 = r16
                if (r14 != r0) goto L_0x00f3
                r14 = 0
            L_0x00f3:
                int r7 = r7 + 1
                goto L_0x00b8
            L_0x00f6:
                int r14 = r14 + 1
                r0 = r16
                if (r14 != r0) goto L_0x00fd
                r14 = 0
            L_0x00fd:
                int r7 = r7 + 1
                goto L_0x0093
            L_0x0100:
                java.lang.Object r17 = r19.poll()     // Catch:{ Throwable -> 0x0109 }
                if (r17 != 0) goto L_0x012a
                if (r17 != 0) goto L_0x00c6
                goto L_0x00d2
            L_0x0109:
                r6 = move-exception
                dji.thirdparty.io.reactivex.exceptions.Exceptions.throwIfFatal(r6)
                r13.dispose()
                r0 = r26
                dji.thirdparty.io.reactivex.internal.util.AtomicThrowable r0 = r0.errors
                r23 = r0
                r0 = r23
                r0.addThrowable(r6)
                boolean r23 = r26.checkTerminate()
                if (r23 != 0) goto L_0x000b
                r0 = r26
                r0.removeInner(r13)
                r10 = 1
                int r7 = r7 + 1
                goto L_0x00f3
            L_0x012a:
                r0 = r17
                r4.onNext(r0)
                boolean r23 = r26.checkTerminate()
                if (r23 == 0) goto L_0x0100
                goto L_0x000b
            L_0x0137:
                r0 = r26
                r0.lastIndex = r14
                r23 = r9[r14]
                r0 = r23
                long r0 = r0.id
                r24 = r0
                r0 = r24
                r2 = r26
                r2.lastId = r0
            L_0x0149:
                if (r10 == 0) goto L_0x0187
                r0 = r26
                int r0 = r0.maxConcurrency
                r23 = r0
                r24 = 2147483647(0x7fffffff, float:NaN)
                r0 = r23
                r1 = r24
                if (r0 == r1) goto L_0x0005
                monitor-enter(r26)
                r0 = r26
                java.util.Queue<dji.thirdparty.io.reactivex.ObservableSource<? extends U>> r0 = r0.sources     // Catch:{ all -> 0x017a }
                r23 = r0
                java.lang.Object r18 = r23.poll()     // Catch:{ all -> 0x017a }
                dji.thirdparty.io.reactivex.ObservableSource r18 = (dji.thirdparty.io.reactivex.ObservableSource) r18     // Catch:{ all -> 0x017a }
                if (r18 != 0) goto L_0x017d
                r0 = r26
                int r0 = r0.wip     // Catch:{ all -> 0x017a }
                r23 = r0
                int r23 = r23 + -1
                r0 = r23
                r1 = r26
                r1.wip = r0     // Catch:{ all -> 0x017a }
                monitor-exit(r26)     // Catch:{ all -> 0x017a }
                goto L_0x0005
            L_0x017a:
                r23 = move-exception
                monitor-exit(r26)     // Catch:{ all -> 0x017a }
                throw r23
            L_0x017d:
                monitor-exit(r26)     // Catch:{ all -> 0x017a }
                r0 = r26
                r1 = r18
                r0.subscribeInner(r1)
                goto L_0x0005
            L_0x0187:
                int r0 = -r15
                r23 = r0
                r0 = r26
                r1 = r23
                int r15 = r0.addAndGet(r1)
                if (r15 != 0) goto L_0x0005
                goto L_0x000b
            */
            throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.io.reactivex.internal.operators.observable.ObservableFlatMap.MergeObserver.drainLoop():void");
        }

        /* access modifiers changed from: package-private */
        public boolean checkTerminate() {
            if (this.cancelled) {
                return true;
            }
            Throwable e = (Throwable) this.errors.get();
            if (this.delayErrors || e == null) {
                return false;
            }
            disposeAll();
            this.actual.onError(this.errors.terminate());
            return true;
        }

        /* access modifiers changed from: package-private */
        public boolean disposeAll() {
            InnerObserver<?, ?>[] a;
            this.s.dispose();
            if (((InnerObserver[]) this.observers.get()) == CANCELLED || (a = (InnerObserver[]) this.observers.getAndSet(CANCELLED)) == CANCELLED) {
                return false;
            }
            for (InnerObserver<?, ?> inner : a) {
                inner.dispose();
            }
            return true;
        }
    }

    static final class InnerObserver<T, U> extends AtomicReference<Disposable> implements Observer<U> {
        private static final long serialVersionUID = -4606175640614850599L;
        volatile boolean done;
        int fusionMode;
        final long id;
        final MergeObserver<T, U> parent;
        volatile SimpleQueue<U> queue;

        InnerObserver(MergeObserver<T, U> parent2, long id2) {
            this.id = id2;
            this.parent = parent2;
        }

        public void onSubscribe(Disposable s) {
            if (DisposableHelper.setOnce(this, s) && (s instanceof QueueDisposable)) {
                QueueDisposable<U> qd = (QueueDisposable) s;
                int m = qd.requestFusion(3);
                if (m == 1) {
                    this.fusionMode = m;
                    this.queue = qd;
                    this.done = true;
                    this.parent.drain();
                } else if (m == 2) {
                    this.fusionMode = m;
                    this.queue = qd;
                }
            }
        }

        public void onNext(U t) {
            if (this.fusionMode == 0) {
                this.parent.tryEmit(t, this);
            } else {
                this.parent.drain();
            }
        }

        public void onError(Throwable t) {
            if (this.parent.errors.addThrowable(t)) {
                if (!this.parent.delayErrors) {
                    this.parent.disposeAll();
                }
                this.done = true;
                this.parent.drain();
                return;
            }
            RxJavaPlugins.onError(t);
        }

        public void onComplete() {
            this.done = true;
            this.parent.drain();
        }

        public void dispose() {
            DisposableHelper.dispose(this);
        }
    }
}
