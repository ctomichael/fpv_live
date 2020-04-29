package dji.thirdparty.io.reactivex.internal.operators.observable;

import dji.thirdparty.io.reactivex.Observable;
import dji.thirdparty.io.reactivex.ObservableSource;
import dji.thirdparty.io.reactivex.Observer;
import dji.thirdparty.io.reactivex.disposables.Disposable;
import dji.thirdparty.io.reactivex.exceptions.Exceptions;
import dji.thirdparty.io.reactivex.functions.Function;
import dji.thirdparty.io.reactivex.internal.disposables.DisposableHelper;
import dji.thirdparty.io.reactivex.internal.disposables.EmptyDisposable;
import dji.thirdparty.io.reactivex.internal.functions.ObjectHelper;
import dji.thirdparty.io.reactivex.internal.queue.SpscLinkedArrayQueue;
import dji.thirdparty.io.reactivex.internal.util.AtomicThrowable;
import dji.thirdparty.io.reactivex.plugins.RxJavaPlugins;
import java.util.Arrays;
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

    public void subscribeActual(Observer<? super R> s) {
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
            EmptyDisposable.complete(s);
            return;
        }
        new LatestCoordinator<>(s, this.combiner, count, this.bufferSize, this.delayError).subscribe(sources2);
    }

    static final class LatestCoordinator<T, R> extends AtomicInteger implements Disposable {
        private static final long serialVersionUID = 8567835998786448817L;
        int active;
        final Observer<? super R> actual;
        volatile boolean cancelled;
        final Function<? super Object[], ? extends R> combiner;
        int complete;
        final boolean delayError;
        volatile boolean done;
        final AtomicThrowable errors = new AtomicThrowable();
        final T[] latest;
        final CombinerObserver<T, R>[] observers;
        final SpscLinkedArrayQueue<Object> queue;

        LatestCoordinator(Observer<? super R> actual2, Function<? super Object[], ? extends R> combiner2, int count, int bufferSize, boolean delayError2) {
            this.actual = actual2;
            this.combiner = combiner2;
            this.delayError = delayError2;
            this.latest = (Object[]) new Object[count];
            this.observers = new CombinerObserver[count];
            this.queue = new SpscLinkedArrayQueue<>(bufferSize);
        }

        public void subscribe(ObservableSource<? extends T>[] sources) {
            Observer<T>[] as = this.observers;
            int len = as.length;
            for (int i = 0; i < len; i++) {
                as[i] = new CombinerObserver<>(this, i);
            }
            lazySet(0);
            this.actual.onSubscribe(this);
            for (int i2 = 0; i2 < len && !this.cancelled; i2++) {
                sources[i2].subscribe(as[i2]);
            }
        }

        public void dispose() {
            if (!this.cancelled) {
                this.cancelled = true;
                if (getAndIncrement() == 0) {
                    cancel(this.queue);
                }
            }
        }

        public boolean isDisposed() {
            return this.cancelled;
        }

        /* access modifiers changed from: package-private */
        public void cancel(SpscLinkedArrayQueue<?> q) {
            clear(q);
            for (CombinerObserver<T, R> s : this.observers) {
                s.dispose();
            }
        }

        /* access modifiers changed from: package-private */
        public void clear(SpscLinkedArrayQueue<?> q) {
            synchronized (this) {
                Arrays.fill(this.latest, (Object) null);
            }
            q.clear();
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: Code restructure failed: missing block: B:23:0x0040, code lost:
            if (r4 != false) goto L_0x0044;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:24:0x0042, code lost:
            if (r10 != null) goto L_?;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:25:0x0044, code lost:
            drain();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:39:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:40:?, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void combine(T r10, int r11) {
            /*
                r9 = this;
                r3 = 0
                r7 = 1
                dji.thirdparty.io.reactivex.internal.operators.observable.ObservableCombineLatest$CombinerObserver<T, R>[] r8 = r9.observers
                r2 = r8[r11]
                monitor-enter(r9)
                boolean r8 = r9.cancelled     // Catch:{ all -> 0x004d }
                if (r8 == 0) goto L_0x000d
                monitor-exit(r9)     // Catch:{ all -> 0x004d }
            L_0x000c:
                return
            L_0x000d:
                T[] r8 = r9.latest     // Catch:{ all -> 0x004d }
                int r5 = r8.length     // Catch:{ all -> 0x004d }
                T[] r8 = r9.latest     // Catch:{ all -> 0x004d }
                r6 = r8[r11]     // Catch:{ all -> 0x004d }
                int r0 = r9.active     // Catch:{ all -> 0x004d }
                if (r6 != 0) goto L_0x001c
                int r0 = r0 + 1
                r9.active = r0     // Catch:{ all -> 0x004d }
            L_0x001c:
                int r1 = r9.complete     // Catch:{ all -> 0x004d }
                if (r10 != 0) goto L_0x0048
                int r1 = r1 + 1
                r9.complete = r1     // Catch:{ all -> 0x004d }
            L_0x0024:
                if (r0 != r5) goto L_0x0050
                r4 = r7
            L_0x0027:
                if (r1 == r5) goto L_0x002d
                if (r10 != 0) goto L_0x002e
                if (r6 != 0) goto L_0x002e
            L_0x002d:
                r3 = r7
            L_0x002e:
                if (r3 != 0) goto L_0x0060
                if (r10 == 0) goto L_0x0052
                if (r4 == 0) goto L_0x0052
                dji.thirdparty.io.reactivex.internal.queue.SpscLinkedArrayQueue<java.lang.Object> r7 = r9.queue     // Catch:{ all -> 0x004d }
                T[] r8 = r9.latest     // Catch:{ all -> 0x004d }
                java.lang.Object r8 = r8.clone()     // Catch:{ all -> 0x004d }
                r7.offer(r2, r8)     // Catch:{ all -> 0x004d }
            L_0x003f:
                monitor-exit(r9)     // Catch:{ all -> 0x004d }
                if (r4 != 0) goto L_0x0044
                if (r10 != 0) goto L_0x000c
            L_0x0044:
                r9.drain()
                goto L_0x000c
            L_0x0048:
                T[] r8 = r9.latest     // Catch:{ all -> 0x004d }
                r8[r11] = r10     // Catch:{ all -> 0x004d }
                goto L_0x0024
            L_0x004d:
                r7 = move-exception
                monitor-exit(r9)     // Catch:{ all -> 0x004d }
                throw r7
            L_0x0050:
                r4 = r3
                goto L_0x0027
            L_0x0052:
                if (r10 != 0) goto L_0x003f
                dji.thirdparty.io.reactivex.internal.util.AtomicThrowable r7 = r9.errors     // Catch:{ all -> 0x004d }
                java.lang.Object r7 = r7.get()     // Catch:{ all -> 0x004d }
                if (r7 == 0) goto L_0x003f
                r7 = 1
                r9.done = r7     // Catch:{ all -> 0x004d }
                goto L_0x003f
            L_0x0060:
                r7 = 1
                r9.done = r7     // Catch:{ all -> 0x004d }
                goto L_0x003f
            */
            throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.io.reactivex.internal.operators.observable.ObservableCombineLatest.LatestCoordinator.combine(java.lang.Object, int):void");
        }

        /* access modifiers changed from: package-private */
        public void drain() {
            if (getAndIncrement() == 0) {
                SpscLinkedArrayQueue<Object> q = this.queue;
                Observer<? super R> a = this.actual;
                boolean delayError2 = this.delayError;
                int missed = 1;
                do {
                    if (!checkTerminated(this.done, q.isEmpty(), a, q, delayError2)) {
                        while (true) {
                            boolean d = this.done;
                            boolean empty = ((CombinerObserver) q.poll()) == null;
                            if (checkTerminated(d, empty, a, q, delayError2)) {
                                return;
                            }
                            if (empty) {
                                missed = addAndGet(-missed);
                            } else {
                                try {
                                    a.onNext(ObjectHelper.requireNonNull(this.combiner.apply((Object[]) ((Object[]) q.poll())), "The combiner returned a null"));
                                } catch (Throwable ex) {
                                    Exceptions.throwIfFatal(ex);
                                    this.cancelled = true;
                                    cancel(q);
                                    a.onError(ex);
                                    return;
                                }
                            }
                        }
                    } else {
                        return;
                    }
                } while (missed != 0);
            }
        }

        /* access modifiers changed from: package-private */
        public boolean checkTerminated(boolean d, boolean empty, Observer<?> a, SpscLinkedArrayQueue<?> q, boolean delayError2) {
            if (this.cancelled) {
                cancel(q);
                return true;
            }
            if (d) {
                if (delayError2) {
                    if (empty) {
                        clear(this.queue);
                        Throwable e = this.errors.terminate();
                        if (e != null) {
                            a.onError(e);
                            return true;
                        }
                        a.onComplete();
                        return true;
                    }
                } else if (((Throwable) this.errors.get()) != null) {
                    cancel(q);
                    a.onError(this.errors.terminate());
                    return true;
                } else if (empty) {
                    clear(this.queue);
                    a.onComplete();
                    return true;
                }
            }
            return false;
        }

        /* access modifiers changed from: package-private */
        public void onError(Throwable e) {
            if (!this.errors.addThrowable(e)) {
                RxJavaPlugins.onError(e);
            }
        }
    }

    static final class CombinerObserver<T, R> implements Observer<T> {
        final int index;
        final LatestCoordinator<T, R> parent;
        final AtomicReference<Disposable> s = new AtomicReference<>();

        CombinerObserver(LatestCoordinator<T, R> parent2, int index2) {
            this.parent = parent2;
            this.index = index2;
        }

        public void onSubscribe(Disposable s2) {
            DisposableHelper.setOnce(this.s, s2);
        }

        public void onNext(T t) {
            this.parent.combine(t, this.index);
        }

        public void onError(Throwable t) {
            this.parent.onError(t);
            this.parent.combine(null, this.index);
        }

        public void onComplete() {
            this.parent.combine(null, this.index);
        }

        public void dispose() {
            DisposableHelper.dispose(this.s);
        }
    }
}
