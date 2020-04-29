package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.queue.MpscLinkedQueue;
import io.reactivex.internal.util.AtomicThrowable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.subjects.UnicastSubject;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservableWindowBoundarySupplier<T, B> extends AbstractObservableWithUpstream<T, Observable<T>> {
    final int capacityHint;
    final Callable<? extends ObservableSource<B>> other;

    public ObservableWindowBoundarySupplier(ObservableSource<T> source, Callable<? extends ObservableSource<B>> other2, int capacityHint2) {
        super(source);
        this.other = other2;
        this.capacityHint = capacityHint2;
    }

    public void subscribeActual(Observer<? super Observable<T>> observer) {
        this.source.subscribe(new WindowBoundaryMainObserver<>(observer, this.capacityHint, this.other));
    }

    static final class WindowBoundaryMainObserver<T, B> extends AtomicInteger implements Observer<T>, Disposable, Runnable {
        static final WindowBoundaryInnerObserver<Object, Object> BOUNDARY_DISPOSED = new WindowBoundaryInnerObserver<>(null);
        static final Object NEXT_WINDOW = new Object();
        private static final long serialVersionUID = 2233020065421370272L;
        final AtomicReference<WindowBoundaryInnerObserver<T, B>> boundaryObserver = new AtomicReference<>();
        final int capacityHint;
        volatile boolean done;
        final Observer<? super Observable<T>> downstream;
        final AtomicThrowable errors = new AtomicThrowable();
        final Callable<? extends ObservableSource<B>> other;
        final MpscLinkedQueue<Object> queue = new MpscLinkedQueue<>();
        final AtomicBoolean stopWindows = new AtomicBoolean();
        Disposable upstream;
        UnicastSubject<T> window;
        final AtomicInteger windows = new AtomicInteger(1);

        WindowBoundaryMainObserver(Observer<? super Observable<T>> downstream2, int capacityHint2, Callable<? extends ObservableSource<B>> other2) {
            this.downstream = downstream2;
            this.capacityHint = capacityHint2;
            this.other = other2;
        }

        public void onSubscribe(Disposable d) {
            if (DisposableHelper.validate(this.upstream, d)) {
                this.upstream = d;
                this.downstream.onSubscribe(this);
                this.queue.offer(NEXT_WINDOW);
                drain();
            }
        }

        public void onNext(T t) {
            this.queue.offer(t);
            drain();
        }

        public void onError(Throwable e) {
            disposeBoundary();
            if (this.errors.addThrowable(e)) {
                this.done = true;
                drain();
                return;
            }
            RxJavaPlugins.onError(e);
        }

        public void onComplete() {
            disposeBoundary();
            this.done = true;
            drain();
        }

        public void dispose() {
            if (this.stopWindows.compareAndSet(false, true)) {
                disposeBoundary();
                if (this.windows.decrementAndGet() == 0) {
                    this.upstream.dispose();
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void disposeBoundary() {
            Disposable d = this.boundaryObserver.getAndSet(BOUNDARY_DISPOSED);
            if (d != null && d != BOUNDARY_DISPOSED) {
                d.dispose();
            }
        }

        public boolean isDisposed() {
            return this.stopWindows.get();
        }

        public void run() {
            if (this.windows.decrementAndGet() == 0) {
                this.upstream.dispose();
            }
        }

        /* access modifiers changed from: package-private */
        public void innerNext(WindowBoundaryInnerObserver<T, B> sender) {
            this.boundaryObserver.compareAndSet(sender, null);
            this.queue.offer(NEXT_WINDOW);
            drain();
        }

        /* access modifiers changed from: package-private */
        public void innerError(Throwable e) {
            this.upstream.dispose();
            if (this.errors.addThrowable(e)) {
                this.done = true;
                drain();
                return;
            }
            RxJavaPlugins.onError(e);
        }

        /* access modifiers changed from: package-private */
        public void innerComplete() {
            this.upstream.dispose();
            this.done = true;
            drain();
        }

        /* access modifiers changed from: package-private */
        public void drain() {
            if (getAndIncrement() == 0) {
                int missed = 1;
                Observer<? super Observable<T>> downstream2 = this.downstream;
                MpscLinkedQueue<Object> queue2 = this.queue;
                AtomicThrowable errors2 = this.errors;
                while (this.windows.get() != 0) {
                    UnicastSubject<T> w = this.window;
                    boolean d = this.done;
                    if (!d || errors2.get() == null) {
                        Object v = queue2.poll();
                        boolean empty = v == null;
                        if (d && empty) {
                            Throwable ex = errors2.terminate();
                            if (ex == null) {
                                if (w != null) {
                                    this.window = null;
                                    w.onComplete();
                                }
                                downstream2.onComplete();
                                return;
                            }
                            if (w != null) {
                                this.window = null;
                                w.onError(ex);
                            }
                            downstream2.onError(ex);
                            return;
                        } else if (empty) {
                            missed = addAndGet(-missed);
                            if (missed == 0) {
                                return;
                            }
                        } else if (v != NEXT_WINDOW) {
                            w.onNext(v);
                        } else {
                            if (w != null) {
                                this.window = null;
                                w.onComplete();
                            }
                            if (!this.stopWindows.get()) {
                                UnicastSubject<T> w2 = UnicastSubject.create(this.capacityHint, this);
                                this.window = w2;
                                this.windows.getAndIncrement();
                                try {
                                    ObservableSource<B> otherSource = (ObservableSource) ObjectHelper.requireNonNull(this.other.call(), "The other Callable returned a null ObservableSource");
                                    WindowBoundaryInnerObserver<T, B> bo = new WindowBoundaryInnerObserver<>(this);
                                    if (this.boundaryObserver.compareAndSet(null, bo)) {
                                        otherSource.subscribe(bo);
                                        downstream2.onNext(w2);
                                    }
                                } catch (Throwable ex2) {
                                    Exceptions.throwIfFatal(ex2);
                                    errors2.addThrowable(ex2);
                                    this.done = true;
                                }
                            }
                        }
                    } else {
                        queue2.clear();
                        Throwable ex3 = errors2.terminate();
                        if (w != null) {
                            this.window = null;
                            w.onError(ex3);
                        }
                        downstream2.onError(ex3);
                        return;
                    }
                }
                queue2.clear();
                this.window = null;
            }
        }
    }

    static final class WindowBoundaryInnerObserver<T, B> extends DisposableObserver<B> {
        boolean done;
        final WindowBoundaryMainObserver<T, B> parent;

        WindowBoundaryInnerObserver(WindowBoundaryMainObserver<T, B> parent2) {
            this.parent = parent2;
        }

        public void onNext(B b) {
            if (!this.done) {
                this.done = true;
                dispose();
                this.parent.innerNext(this);
            }
        }

        public void onError(Throwable t) {
            if (this.done) {
                RxJavaPlugins.onError(t);
                return;
            }
            this.done = true;
            this.parent.innerError(t);
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                this.parent.innerComplete();
            }
        }
    }
}
