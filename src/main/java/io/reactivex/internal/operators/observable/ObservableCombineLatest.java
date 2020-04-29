package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.queue.SpscLinkedArrayQueue;
import io.reactivex.internal.util.AtomicThrowable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservableCombineLatest<T, R> extends Observable<R> {
    final int bufferSize;
    final Function<? super Object[], ? extends R> combiner;
    final boolean delayError;
    final ObservableSource<? extends T>[] sources;
    final Iterable<? extends ObservableSource<? extends T>> sourcesIterable;

    public ObservableCombineLatest(ObservableSource<? extends T>[] sources2, Iterable<? extends ObservableSource<? extends T>> sourcesIterable2, Function<? super Object[], ? extends R> combiner2, int bufferSize2, boolean delayError2) {
        this.sources = sources2;
        this.sourcesIterable = sourcesIterable2;
        this.combiner = combiner2;
        this.bufferSize = bufferSize2;
        this.delayError = delayError2;
    }

    public void subscribeActual(Observer<? super R> observer) {
        ObservableSource<? extends T>[] sources2 = this.sources;
        int count = 0;
        if (sources2 == null) {
            sources2 = new Observable[8];
            for (ObservableSource<? extends T> p : this.sourcesIterable) {
                if (count == sources2.length) {
                    ObservableSource<? extends T>[] b = new ObservableSource[((count >> 2) + count)];
                    System.arraycopy(sources2, 0, b, 0, count);
                    sources2 = b;
                }
                sources2[count] = p;
                count++;
            }
        } else {
            count = sources2.length;
        }
        if (count == 0) {
            EmptyDisposable.complete(observer);
            return;
        }
        new LatestCoordinator<>(observer, this.combiner, count, this.bufferSize, this.delayError).subscribe(sources2);
    }

    static final class LatestCoordinator<T, R> extends AtomicInteger implements Disposable {
        private static final long serialVersionUID = 8567835998786448817L;
        int active;
        volatile boolean cancelled;
        final Function<? super Object[], ? extends R> combiner;
        int complete;
        final boolean delayError;
        volatile boolean done;
        final Observer<? super R> downstream;
        final AtomicThrowable errors = new AtomicThrowable();
        Object[] latest;
        final CombinerObserver<T, R>[] observers;
        final SpscLinkedArrayQueue<Object[]> queue;

        LatestCoordinator(Observer<? super R> actual, Function<? super Object[], ? extends R> combiner2, int count, int bufferSize, boolean delayError2) {
            this.downstream = actual;
            this.combiner = combiner2;
            this.delayError = delayError2;
            this.latest = new Object[count];
            CombinerObserver<T, R>[] as = new CombinerObserver[count];
            for (int i = 0; i < count; i++) {
                as[i] = new CombinerObserver<>(this, i);
            }
            this.observers = as;
            this.queue = new SpscLinkedArrayQueue<>(bufferSize);
        }

        public void subscribe(ObservableSource<? extends T>[] sources) {
            Observer<T>[] as = this.observers;
            int len = as.length;
            this.downstream.onSubscribe(this);
            for (int i = 0; i < len && !this.done && !this.cancelled; i++) {
                sources[i].subscribe(as[i]);
            }
        }

        public void dispose() {
            if (!this.cancelled) {
                this.cancelled = true;
                cancelSources();
                if (getAndIncrement() == 0) {
                    clear(this.queue);
                }
            }
        }

        public boolean isDisposed() {
            return this.cancelled;
        }

        /* access modifiers changed from: package-private */
        public void cancelSources() {
            for (CombinerObserver<T, R> observer : this.observers) {
                observer.dispose();
            }
        }

        /* access modifiers changed from: package-private */
        public void clear(SpscLinkedArrayQueue<?> q) {
            synchronized (this) {
                this.latest = null;
            }
            q.clear();
        }

        /* access modifiers changed from: package-private */
        public void drain() {
            if (getAndIncrement() == 0) {
                SpscLinkedArrayQueue<Object[]> q = this.queue;
                Observer<? super R> a = this.downstream;
                boolean delayError2 = this.delayError;
                int missed = 1;
                while (!this.cancelled) {
                    if (delayError2 || this.errors.get() == null) {
                        boolean d = this.done;
                        Object[] s = q.poll();
                        boolean empty = s == null;
                        if (d && empty) {
                            clear(q);
                            Throwable ex = this.errors.terminate();
                            if (ex == null) {
                                a.onComplete();
                                return;
                            } else {
                                a.onError(ex);
                                return;
                            }
                        } else if (empty) {
                            missed = addAndGet(-missed);
                            if (missed == 0) {
                                return;
                            }
                        } else {
                            try {
                                a.onNext(ObjectHelper.requireNonNull(this.combiner.apply(s), "The combiner returned a null value"));
                            } catch (Throwable ex2) {
                                Exceptions.throwIfFatal(ex2);
                                this.errors.addThrowable(ex2);
                                cancelSources();
                                clear(q);
                                a.onError(this.errors.terminate());
                                return;
                            }
                        }
                    } else {
                        cancelSources();
                        clear(q);
                        a.onError(this.errors.terminate());
                        return;
                    }
                }
                clear(q);
            }
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: Code restructure failed: missing block: B:13:0x0022, code lost:
            if (r3 == false) goto L_?;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:14:0x0024, code lost:
            drain();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:23:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:24:?, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void innerNext(int r7, T r8) {
            /*
                r6 = this;
                r3 = 0
                monitor-enter(r6)
                java.lang.Object[] r1 = r6.latest     // Catch:{ all -> 0x0028 }
                if (r1 != 0) goto L_0x0008
                monitor-exit(r6)     // Catch:{ all -> 0x0028 }
            L_0x0007:
                return
            L_0x0008:
                r2 = r1[r7]     // Catch:{ all -> 0x0028 }
                int r0 = r6.active     // Catch:{ all -> 0x0028 }
                if (r2 != 0) goto L_0x0012
                int r0 = r0 + 1
                r6.active = r0     // Catch:{ all -> 0x0028 }
            L_0x0012:
                r1[r7] = r8     // Catch:{ all -> 0x0028 }
                int r4 = r1.length     // Catch:{ all -> 0x0028 }
                if (r0 != r4) goto L_0x0021
                io.reactivex.internal.queue.SpscLinkedArrayQueue<java.lang.Object[]> r4 = r6.queue     // Catch:{ all -> 0x0028 }
                java.lang.Object r5 = r1.clone()     // Catch:{ all -> 0x0028 }
                r4.offer(r5)     // Catch:{ all -> 0x0028 }
                r3 = 1
            L_0x0021:
                monitor-exit(r6)     // Catch:{ all -> 0x0028 }
                if (r3 == 0) goto L_0x0007
                r6.drain()
                goto L_0x0007
            L_0x0028:
                r4 = move-exception
                monitor-exit(r6)     // Catch:{ all -> 0x0028 }
                throw r4
            */
            throw new UnsupportedOperationException("Method not decompiled: io.reactivex.internal.operators.observable.ObservableCombineLatest.LatestCoordinator.innerNext(int, java.lang.Object):void");
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: Code restructure failed: missing block: B:14:0x0023, code lost:
            if (r2 == r1.length) goto L_0x0025;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void innerError(int r5, java.lang.Throwable r6) {
            /*
                r4 = this;
                r2 = 1
                io.reactivex.internal.util.AtomicThrowable r3 = r4.errors
                boolean r3 = r3.addThrowable(r6)
                if (r3 == 0) goto L_0x0037
                r0 = 1
                boolean r3 = r4.delayError
                if (r3 == 0) goto L_0x0029
                monitor-enter(r4)
                java.lang.Object[] r1 = r4.latest     // Catch:{ all -> 0x0034 }
                if (r1 != 0) goto L_0x0015
                monitor-exit(r4)     // Catch:{ all -> 0x0034 }
            L_0x0014:
                return
            L_0x0015:
                r3 = r1[r5]     // Catch:{ all -> 0x0034 }
                if (r3 != 0) goto L_0x0032
                r0 = r2
            L_0x001a:
                if (r0 != 0) goto L_0x0025
                int r2 = r4.complete     // Catch:{ all -> 0x0034 }
                int r2 = r2 + 1
                r4.complete = r2     // Catch:{ all -> 0x0034 }
                int r3 = r1.length     // Catch:{ all -> 0x0034 }
                if (r2 != r3) goto L_0x0028
            L_0x0025:
                r2 = 1
                r4.done = r2     // Catch:{ all -> 0x0034 }
            L_0x0028:
                monitor-exit(r4)     // Catch:{ all -> 0x0034 }
            L_0x0029:
                if (r0 == 0) goto L_0x002e
                r4.cancelSources()
            L_0x002e:
                r4.drain()
                goto L_0x0014
            L_0x0032:
                r0 = 0
                goto L_0x001a
            L_0x0034:
                r2 = move-exception
                monitor-exit(r4)     // Catch:{ all -> 0x0034 }
                throw r2
            L_0x0037:
                io.reactivex.plugins.RxJavaPlugins.onError(r6)
                goto L_0x0014
            */
            throw new UnsupportedOperationException("Method not decompiled: io.reactivex.internal.operators.observable.ObservableCombineLatest.LatestCoordinator.innerError(int, java.lang.Throwable):void");
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: Code restructure failed: missing block: B:11:0x0017, code lost:
            if (r2 == r1.length) goto L_0x0019;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:14:0x001d, code lost:
            if (r0 == false) goto L_0x0022;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:15:0x001f, code lost:
            cancelSources();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:16:0x0022, code lost:
            drain();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:26:?, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void innerComplete(int r5) {
            /*
                r4 = this;
                r2 = 1
                r0 = 0
                monitor-enter(r4)
                java.lang.Object[] r1 = r4.latest     // Catch:{ all -> 0x0028 }
                if (r1 != 0) goto L_0x0009
                monitor-exit(r4)     // Catch:{ all -> 0x0028 }
            L_0x0008:
                return
            L_0x0009:
                r3 = r1[r5]     // Catch:{ all -> 0x0028 }
                if (r3 != 0) goto L_0x0026
                r0 = r2
            L_0x000e:
                if (r0 != 0) goto L_0x0019
                int r2 = r4.complete     // Catch:{ all -> 0x0028 }
                int r2 = r2 + 1
                r4.complete = r2     // Catch:{ all -> 0x0028 }
                int r3 = r1.length     // Catch:{ all -> 0x0028 }
                if (r2 != r3) goto L_0x001c
            L_0x0019:
                r2 = 1
                r4.done = r2     // Catch:{ all -> 0x0028 }
            L_0x001c:
                monitor-exit(r4)     // Catch:{ all -> 0x0028 }
                if (r0 == 0) goto L_0x0022
                r4.cancelSources()
            L_0x0022:
                r4.drain()
                goto L_0x0008
            L_0x0026:
                r0 = 0
                goto L_0x000e
            L_0x0028:
                r2 = move-exception
                monitor-exit(r4)     // Catch:{ all -> 0x0028 }
                throw r2
            */
            throw new UnsupportedOperationException("Method not decompiled: io.reactivex.internal.operators.observable.ObservableCombineLatest.LatestCoordinator.innerComplete(int):void");
        }
    }

    static final class CombinerObserver<T, R> extends AtomicReference<Disposable> implements Observer<T> {
        private static final long serialVersionUID = -4823716997131257941L;
        final int index;
        final LatestCoordinator<T, R> parent;

        CombinerObserver(LatestCoordinator<T, R> parent2, int index2) {
            this.parent = parent2;
            this.index = index2;
        }

        public void onSubscribe(Disposable d) {
            DisposableHelper.setOnce(this, d);
        }

        public void onNext(T t) {
            this.parent.innerNext(this.index, t);
        }

        public void onError(Throwable t) {
            this.parent.innerError(this.index, t);
        }

        public void onComplete() {
            this.parent.innerComplete(this.index);
        }

        public void dispose() {
            DisposableHelper.dispose(this);
        }
    }
}
