package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.internal.queue.SpscLinkedArrayQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservableZip<T, R> extends Observable<R> {
    final int bufferSize;
    final boolean delayError;
    final ObservableSource<? extends T>[] sources;
    final Iterable<? extends ObservableSource<? extends T>> sourcesIterable;
    final Function<? super Object[], ? extends R> zipper;

    public ObservableZip(ObservableSource<? extends T>[] sources2, Iterable<? extends ObservableSource<? extends T>> sourcesIterable2, Function<? super Object[], ? extends R> zipper2, int bufferSize2, boolean delayError2) {
        this.sources = sources2;
        this.sourcesIterable = sourcesIterable2;
        this.zipper = zipper2;
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
        } else {
            new ZipCoordinator<>(observer, this.zipper, count, this.delayError).subscribe(sources2, this.bufferSize);
        }
    }

    static final class ZipCoordinator<T, R> extends AtomicInteger implements Disposable {
        private static final long serialVersionUID = 2983708048395377667L;
        volatile boolean cancelled;
        final boolean delayError;
        final Observer<? super R> downstream;
        final ZipObserver<T, R>[] observers;
        final T[] row;
        final Function<? super Object[], ? extends R> zipper;

        ZipCoordinator(Observer<? super R> actual, Function<? super Object[], ? extends R> zipper2, int count, boolean delayError2) {
            this.downstream = actual;
            this.zipper = zipper2;
            this.observers = new ZipObserver[count];
            this.row = (Object[]) new Object[count];
            this.delayError = delayError2;
        }

        public void subscribe(ObservableSource<? extends T>[] sources, int bufferSize) {
            ZipObserver<T, R>[] s = this.observers;
            int len = s.length;
            for (int i = 0; i < len; i++) {
                s[i] = new ZipObserver<>(this, bufferSize);
            }
            lazySet(0);
            this.downstream.onSubscribe(this);
            for (int i2 = 0; i2 < len && !this.cancelled; i2++) {
                sources[i2].subscribe(s[i2]);
            }
        }

        public void dispose() {
            if (!this.cancelled) {
                this.cancelled = true;
                cancelSources();
                if (getAndIncrement() == 0) {
                    clear();
                }
            }
        }

        public boolean isDisposed() {
            return this.cancelled;
        }

        /* access modifiers changed from: package-private */
        public void cancel() {
            clear();
            cancelSources();
        }

        /* access modifiers changed from: package-private */
        public void cancelSources() {
            for (ZipObserver<T, R> zipObserver : this.observers) {
                zipObserver.dispose();
            }
        }

        /* access modifiers changed from: package-private */
        public void clear() {
            for (ZipObserver<T, R> zipObserver : this.observers) {
                zipObserver.queue.clear();
            }
        }

        /* JADX WARNING: Code restructure failed: missing block: B:24:0x0061, code lost:
            if (r7 == 0) goto L_0x006d;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:25:0x0063, code lost:
            r10 = addAndGet(-r10);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:26:0x006a, code lost:
            if (r10 != 0) goto L_0x0018;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:30:0x007f, code lost:
            r4.onNext(io.reactivex.internal.functions.ObjectHelper.requireNonNull(r17.zipper.apply(r11.clone()), "The zipper returned a null value"));
            java.util.Arrays.fill(r11, (java.lang.Object) null);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:31:0x0088, code lost:
            r8 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:32:0x0089, code lost:
            io.reactivex.exceptions.Exceptions.throwIfFatal(r8);
            cancel();
            r4.onError(r8);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:47:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:48:?, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void drain() {
            /*
                r17 = this;
                int r1 = r17.getAndIncrement()
                if (r1 == 0) goto L_0x0007
            L_0x0006:
                return
            L_0x0007:
                r10 = 1
                r0 = r17
                io.reactivex.internal.operators.observable.ObservableZip$ZipObserver<T, R>[] r14 = r0.observers
                r0 = r17
                io.reactivex.Observer<? super R> r4 = r0.downstream
                r0 = r17
                T[] r11 = r0.row
                r0 = r17
                boolean r5 = r0.delayError
            L_0x0018:
                r9 = 0
                r7 = 0
                int r0 = r14.length
                r16 = r0
                r1 = 0
                r15 = r1
            L_0x001f:
                r0 = r16
                if (r15 >= r0) goto L_0x0061
                r6 = r14[r15]
                r1 = r11[r9]
                if (r1 != 0) goto L_0x004b
                boolean r2 = r6.done
                io.reactivex.internal.queue.SpscLinkedArrayQueue<T> r1 = r6.queue
                java.lang.Object r13 = r1.poll()
                if (r13 != 0) goto L_0x0046
                r3 = 1
            L_0x0034:
                r1 = r17
                boolean r1 = r1.checkTerminated(r2, r3, r4, r5, r6)
                if (r1 != 0) goto L_0x0006
                if (r3 != 0) goto L_0x0048
                r11[r9] = r13
            L_0x0040:
                int r9 = r9 + 1
                int r1 = r15 + 1
                r15 = r1
                goto L_0x001f
            L_0x0046:
                r3 = 0
                goto L_0x0034
            L_0x0048:
                int r7 = r7 + 1
                goto L_0x0040
            L_0x004b:
                boolean r1 = r6.done
                if (r1 == 0) goto L_0x0040
                if (r5 != 0) goto L_0x0040
                java.lang.Throwable r8 = r6.error
                if (r8 == 0) goto L_0x0040
                r1 = 1
                r0 = r17
                r0.cancelled = r1
                r17.cancel()
                r4.onError(r8)
                goto L_0x0006
            L_0x0061:
                if (r7 == 0) goto L_0x006d
                int r1 = -r10
                r0 = r17
                int r10 = r0.addAndGet(r1)
                if (r10 != 0) goto L_0x0018
                goto L_0x0006
            L_0x006d:
                r0 = r17
                io.reactivex.functions.Function<? super java.lang.Object[], ? extends R> r1 = r0.zipper     // Catch:{ Throwable -> 0x0088 }
                java.lang.Object r15 = r11.clone()     // Catch:{ Throwable -> 0x0088 }
                java.lang.Object r1 = r1.apply(r15)     // Catch:{ Throwable -> 0x0088 }
                java.lang.String r15 = "The zipper returned a null value"
                java.lang.Object r12 = io.reactivex.internal.functions.ObjectHelper.requireNonNull(r1, r15)     // Catch:{ Throwable -> 0x0088 }
                r4.onNext(r12)
                r1 = 0
                java.util.Arrays.fill(r11, r1)
                goto L_0x0018
            L_0x0088:
                r8 = move-exception
                io.reactivex.exceptions.Exceptions.throwIfFatal(r8)
                r17.cancel()
                r4.onError(r8)
                goto L_0x0006
            */
            throw new UnsupportedOperationException("Method not decompiled: io.reactivex.internal.operators.observable.ObservableZip.ZipCoordinator.drain():void");
        }

        /* access modifiers changed from: package-private */
        public boolean checkTerminated(boolean d, boolean empty, Observer<? super R> a, boolean delayError2, ZipObserver<?, ?> source) {
            if (this.cancelled) {
                cancel();
                return true;
            }
            if (d) {
                if (!delayError2) {
                    Throwable e = source.error;
                    if (e != null) {
                        this.cancelled = true;
                        cancel();
                        a.onError(e);
                        return true;
                    } else if (empty) {
                        this.cancelled = true;
                        cancel();
                        a.onComplete();
                        return true;
                    }
                } else if (empty) {
                    Throwable e2 = source.error;
                    this.cancelled = true;
                    cancel();
                    if (e2 != null) {
                        a.onError(e2);
                        return true;
                    }
                    a.onComplete();
                    return true;
                }
            }
            return false;
        }
    }

    static final class ZipObserver<T, R> implements Observer<T> {
        volatile boolean done;
        Throwable error;
        final ZipCoordinator<T, R> parent;
        final SpscLinkedArrayQueue<T> queue;
        final AtomicReference<Disposable> upstream = new AtomicReference<>();

        ZipObserver(ZipCoordinator<T, R> parent2, int bufferSize) {
            this.parent = parent2;
            this.queue = new SpscLinkedArrayQueue<>(bufferSize);
        }

        public void onSubscribe(Disposable d) {
            DisposableHelper.setOnce(this.upstream, d);
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

        public void dispose() {
            DisposableHelper.dispose(this.upstream);
        }
    }
}
