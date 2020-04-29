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
import java.util.Arrays;
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
        } else {
            new ZipCoordinator<>(s, this.zipper, count, this.delayError).subscribe(sources2, this.bufferSize);
        }
    }

    static final class ZipCoordinator<T, R> extends AtomicInteger implements Disposable {
        private static final long serialVersionUID = 2983708048395377667L;
        final Observer<? super R> actual;
        volatile boolean cancelled;
        final boolean delayError;
        final ZipObserver<T, R>[] observers;
        final T[] row;
        final Function<? super Object[], ? extends R> zipper;

        ZipCoordinator(Observer<? super R> actual2, Function<? super Object[], ? extends R> zipper2, int count, boolean delayError2) {
            this.actual = actual2;
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
            this.actual.onSubscribe(this);
            for (int i2 = 0; i2 < len && !this.cancelled; i2++) {
                sources[i2].subscribe(s[i2]);
            }
        }

        public void dispose() {
            if (!this.cancelled) {
                this.cancelled = true;
                if (getAndIncrement() == 0) {
                    clear();
                }
            }
        }

        public boolean isDisposed() {
            return this.cancelled;
        }

        /* access modifiers changed from: package-private */
        public void clear() {
            ZipObserver[] arr$ = this.observers;
            for (ZipObserver zipObserver : arr$) {
                zipObserver.dispose();
                zipObserver.queue.clear();
            }
        }

        public void drain() {
            Throwable ex;
            if (getAndIncrement() == 0) {
                int missing = 1;
                ZipObserver<T, R>[] zs = this.observers;
                Observer<? super R> a = this.actual;
                T[] os = this.row;
                boolean delayError2 = this.delayError;
                while (true) {
                    int i = 0;
                    int emptyCount = 0;
                    ZipObserver<T, R>[] arr$ = zs;
                    for (ZipObserver<T, R> z : arr$) {
                        if (os[i] == null) {
                            boolean d = z.done;
                            T v = z.queue.poll();
                            boolean empty = v == null;
                            if (checkTerminated(d, empty, a, delayError2, z)) {
                                return;
                            }
                            if (!empty) {
                                os[i] = v;
                            } else {
                                emptyCount++;
                            }
                        } else if (z.done && !delayError2 && (ex = z.error) != null) {
                            clear();
                            a.onError(ex);
                            return;
                        }
                        i++;
                    }
                    if (emptyCount != 0) {
                        missing = addAndGet(-missing);
                        if (missing == 0) {
                            return;
                        }
                    } else {
                        try {
                            a.onNext(ObjectHelper.requireNonNull(this.zipper.apply(os.clone()), "The zipper returned a null value"));
                            Arrays.fill(os, (Object) null);
                        } catch (Throwable ex2) {
                            Exceptions.throwIfFatal(ex2);
                            clear();
                            a.onError(ex2);
                            return;
                        }
                    }
                }
            }
        }

        /* access modifiers changed from: package-private */
        public boolean checkTerminated(boolean d, boolean empty, Observer<? super R> a, boolean delayError2, ZipObserver<?, ?> source) {
            if (this.cancelled) {
                clear();
                return true;
            }
            if (d) {
                if (!delayError2) {
                    Throwable e = source.error;
                    if (e != null) {
                        clear();
                        a.onError(e);
                        return true;
                    } else if (empty) {
                        clear();
                        a.onComplete();
                        return true;
                    }
                } else if (empty) {
                    Throwable e2 = source.error;
                    clear();
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
        final AtomicReference<Disposable> s = new AtomicReference<>();

        ZipObserver(ZipCoordinator<T, R> parent2, int bufferSize) {
            this.parent = parent2;
            this.queue = new SpscLinkedArrayQueue<>(bufferSize);
        }

        public void onSubscribe(Disposable s2) {
            DisposableHelper.setOnce(this.s, s2);
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
            DisposableHelper.dispose(this.s);
        }
    }
}
