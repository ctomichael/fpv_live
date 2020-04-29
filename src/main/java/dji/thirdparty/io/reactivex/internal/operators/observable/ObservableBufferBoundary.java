package dji.thirdparty.io.reactivex.internal.operators.observable;

import dji.thirdparty.io.reactivex.ObservableSource;
import dji.thirdparty.io.reactivex.Observer;
import dji.thirdparty.io.reactivex.disposables.CompositeDisposable;
import dji.thirdparty.io.reactivex.disposables.Disposable;
import dji.thirdparty.io.reactivex.exceptions.Exceptions;
import dji.thirdparty.io.reactivex.functions.Function;
import dji.thirdparty.io.reactivex.internal.disposables.DisposableHelper;
import dji.thirdparty.io.reactivex.internal.functions.ObjectHelper;
import dji.thirdparty.io.reactivex.internal.fuseable.SimpleQueue;
import dji.thirdparty.io.reactivex.internal.observers.QueueDrainObserver;
import dji.thirdparty.io.reactivex.internal.queue.MpscLinkedQueue;
import dji.thirdparty.io.reactivex.internal.util.QueueDrainHelper;
import dji.thirdparty.io.reactivex.observers.DisposableObserver;
import dji.thirdparty.io.reactivex.observers.SerializedObserver;
import dji.thirdparty.io.reactivex.plugins.RxJavaPlugins;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

public final class ObservableBufferBoundary<T, U extends Collection<? super T>, Open, Close> extends AbstractObservableWithUpstream<T, U> {
    final Function<? super Open, ? extends ObservableSource<? extends Close>> bufferClose;
    final ObservableSource<? extends Open> bufferOpen;
    final Callable<U> bufferSupplier;

    public ObservableBufferBoundary(ObservableSource<T> source, ObservableSource<? extends Open> bufferOpen2, Function<? super Open, ? extends ObservableSource<? extends Close>> bufferClose2, Callable<U> bufferSupplier2) {
        super(source);
        this.bufferOpen = bufferOpen2;
        this.bufferClose = bufferClose2;
        this.bufferSupplier = bufferSupplier2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super U> t) {
        this.source.subscribe(new BufferBoundaryObserver(new SerializedObserver(t), this.bufferOpen, this.bufferClose, this.bufferSupplier));
    }

    static final class BufferBoundaryObserver<T, U extends Collection<? super T>, Open, Close> extends QueueDrainObserver<T, U, U> implements Disposable {
        final Function<? super Open, ? extends ObservableSource<? extends Close>> bufferClose;
        final ObservableSource<? extends Open> bufferOpen;
        final Callable<U> bufferSupplier;
        final List<U> buffers;
        final CompositeDisposable resources;
        Disposable s;
        final AtomicInteger windows = new AtomicInteger();

        /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
         method: dji.thirdparty.io.reactivex.internal.operators.observable.ObservableBufferBoundary.BufferBoundaryObserver.accept(dji.thirdparty.io.reactivex.Observer, java.util.Collection):void
         arg types: [dji.thirdparty.io.reactivex.Observer, java.lang.Object]
         candidates:
          dji.thirdparty.io.reactivex.internal.operators.observable.ObservableBufferBoundary.BufferBoundaryObserver.accept(dji.thirdparty.io.reactivex.Observer, java.lang.Object):void
          dji.thirdparty.io.reactivex.internal.observers.QueueDrainObserver.accept(dji.thirdparty.io.reactivex.Observer, java.lang.Object):void
          dji.thirdparty.io.reactivex.internal.util.ObservableQueueDrain.accept(dji.thirdparty.io.reactivex.Observer, java.lang.Object):void
          dji.thirdparty.io.reactivex.internal.operators.observable.ObservableBufferBoundary.BufferBoundaryObserver.accept(dji.thirdparty.io.reactivex.Observer, java.util.Collection):void */
        public /* bridge */ /* synthetic */ void accept(Observer x0, Object x1) {
            accept(x0, (Collection) ((Collection) x1));
        }

        BufferBoundaryObserver(Observer<? super U> actual, ObservableSource<? extends Open> bufferOpen2, Function<? super Open, ? extends ObservableSource<? extends Close>> bufferClose2, Callable<U> bufferSupplier2) {
            super(actual, new MpscLinkedQueue());
            this.bufferOpen = bufferOpen2;
            this.bufferClose = bufferClose2;
            this.bufferSupplier = bufferSupplier2;
            this.buffers = new LinkedList();
            this.resources = new CompositeDisposable();
        }

        public void onSubscribe(Disposable s2) {
            if (DisposableHelper.validate(this.s, s2)) {
                this.s = s2;
                BufferOpenObserver<T, U, Open, Close> bos = new BufferOpenObserver<>(this);
                this.resources.add(bos);
                this.actual.onSubscribe(this);
                this.windows.lazySet(1);
                this.bufferOpen.subscribe(bos);
            }
        }

        public void onNext(T t) {
            synchronized (this) {
                for (U b : this.buffers) {
                    b.add(t);
                }
            }
        }

        public void onError(Throwable t) {
            dispose();
            this.cancelled = true;
            synchronized (this) {
                this.buffers.clear();
            }
            this.actual.onError(t);
        }

        public void onComplete() {
            if (this.windows.decrementAndGet() == 0) {
                complete();
            }
        }

        /* access modifiers changed from: package-private */
        public void complete() {
            List<U> list;
            synchronized (this) {
                list = new ArrayList<>(this.buffers);
                this.buffers.clear();
            }
            SimpleQueue<U> q = this.queue;
            for (U u : list) {
                q.offer(u);
            }
            this.done = true;
            if (enter()) {
                QueueDrainHelper.drainLoop(q, this.actual, false, this, this);
            }
        }

        public void dispose() {
            if (!this.cancelled) {
                this.cancelled = true;
                this.resources.dispose();
            }
        }

        public boolean isDisposed() {
            return this.cancelled;
        }

        public void accept(Observer<? super U> a, U v) {
            a.onNext(v);
        }

        /* access modifiers changed from: package-private */
        public void open(Open window) {
            if (!this.cancelled) {
                try {
                    U b = (Collection) ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The buffer supplied is null");
                    try {
                        ObservableSource<? extends Close> p = (ObservableSource) ObjectHelper.requireNonNull(this.bufferClose.apply(window), "The buffer closing Observable is null");
                        if (!this.cancelled) {
                            synchronized (this) {
                                if (!this.cancelled) {
                                    this.buffers.add(b);
                                    BufferCloseObserver<T, U, Open, Close> bcs = new BufferCloseObserver<>(b, this);
                                    this.resources.add(bcs);
                                    this.windows.getAndIncrement();
                                    p.subscribe(bcs);
                                }
                            }
                        }
                    } catch (Throwable e) {
                        Exceptions.throwIfFatal(e);
                        onError(e);
                    }
                } catch (Throwable e2) {
                    Exceptions.throwIfFatal(e2);
                    onError(e2);
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void openFinished(Disposable d) {
            if (this.resources.remove(d) && this.windows.decrementAndGet() == 0) {
                complete();
            }
        }

        /* access modifiers changed from: package-private */
        public void close(U b, Disposable d) {
            boolean e;
            synchronized (this) {
                e = this.buffers.remove(b);
            }
            if (e) {
                fastPathOrderedEmit(b, false, this);
            }
            if (this.resources.remove(d) && this.windows.decrementAndGet() == 0) {
                complete();
            }
        }
    }

    static final class BufferOpenObserver<T, U extends Collection<? super T>, Open, Close> extends DisposableObserver<Open> {
        boolean done;
        final BufferBoundaryObserver<T, U, Open, Close> parent;

        BufferOpenObserver(BufferBoundaryObserver<T, U, Open, Close> parent2) {
            this.parent = parent2;
        }

        public void onNext(Open t) {
            if (!this.done) {
                this.parent.open(t);
            }
        }

        public void onError(Throwable t) {
            if (this.done) {
                RxJavaPlugins.onError(t);
                return;
            }
            this.done = true;
            this.parent.onError(t);
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                this.parent.openFinished(this);
            }
        }
    }

    static final class BufferCloseObserver<T, U extends Collection<? super T>, Open, Close> extends DisposableObserver<Close> {
        boolean done;
        final BufferBoundaryObserver<T, U, Open, Close> parent;
        final U value;

        BufferCloseObserver(U value2, BufferBoundaryObserver<T, U, Open, Close> parent2) {
            this.parent = parent2;
            this.value = value2;
        }

        public void onNext(Close close) {
            onComplete();
        }

        public void onError(Throwable t) {
            if (this.done) {
                RxJavaPlugins.onError(t);
            } else {
                this.parent.onError(t);
            }
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                this.parent.close(this.value, this);
            }
        }
    }
}
