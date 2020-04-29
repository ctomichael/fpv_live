package dji.thirdparty.io.reactivex.internal.operators.single;

import dji.thirdparty.io.reactivex.Observable;
import dji.thirdparty.io.reactivex.Observer;
import dji.thirdparty.io.reactivex.SingleObserver;
import dji.thirdparty.io.reactivex.SingleSource;
import dji.thirdparty.io.reactivex.disposables.Disposable;
import dji.thirdparty.io.reactivex.exceptions.Exceptions;
import dji.thirdparty.io.reactivex.functions.Function;
import dji.thirdparty.io.reactivex.internal.disposables.DisposableHelper;
import dji.thirdparty.io.reactivex.internal.functions.ObjectHelper;
import dji.thirdparty.io.reactivex.internal.observers.BasicIntQueueDisposable;
import java.util.Iterator;

public final class SingleFlatMapIterableObservable<T, R> extends Observable<R> {
    final Function<? super T, ? extends Iterable<? extends R>> mapper;
    final SingleSource<T> source;

    public SingleFlatMapIterableObservable(SingleSource<T> source2, Function<? super T, ? extends Iterable<? extends R>> mapper2) {
        this.source = source2;
        this.mapper = mapper2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super R> s) {
        this.source.subscribe(new FlatMapIterableObserver(s, this.mapper));
    }

    static final class FlatMapIterableObserver<T, R> extends BasicIntQueueDisposable<R> implements SingleObserver<T> {
        private static final long serialVersionUID = -8938804753851907758L;
        final Observer<? super R> actual;
        volatile boolean cancelled;
        Disposable d;

        /* renamed from: it  reason: collision with root package name */
        volatile Iterator<? extends R> f12it;
        final Function<? super T, ? extends Iterable<? extends R>> mapper;
        boolean outputFused;

        FlatMapIterableObserver(Observer<? super R> actual2, Function<? super T, ? extends Iterable<? extends R>> mapper2) {
            this.actual = actual2;
            this.mapper = mapper2;
        }

        public void onSubscribe(Disposable d2) {
            if (DisposableHelper.validate(this.d, d2)) {
                this.d = d2;
                this.actual.onSubscribe(this);
            }
        }

        public void onSuccess(T value) {
            Observer<? super R> a = this.actual;
            try {
                Iterator<? extends R> iter = ((Iterable) this.mapper.apply(value)).iterator();
                if (!iter.hasNext()) {
                    a.onComplete();
                } else if (this.outputFused) {
                    this.f12it = iter;
                    a.onNext(null);
                    a.onComplete();
                } else {
                    while (!this.cancelled) {
                        try {
                            a.onNext(iter.next());
                            if (!this.cancelled) {
                                try {
                                    if (!iter.hasNext()) {
                                        a.onComplete();
                                        return;
                                    }
                                } catch (Throwable ex) {
                                    Exceptions.throwIfFatal(ex);
                                    a.onError(ex);
                                    return;
                                }
                            } else {
                                return;
                            }
                        } catch (Throwable ex2) {
                            Exceptions.throwIfFatal(ex2);
                            a.onError(ex2);
                            return;
                        }
                    }
                }
            } catch (Throwable ex3) {
                Exceptions.throwIfFatal(ex3);
                this.actual.onError(ex3);
            }
        }

        public void onError(Throwable e) {
            this.d = DisposableHelper.DISPOSED;
            this.actual.onError(e);
        }

        public void dispose() {
            this.cancelled = true;
            this.d.dispose();
            this.d = DisposableHelper.DISPOSED;
        }

        public boolean isDisposed() {
            return this.cancelled;
        }

        public int requestFusion(int mode) {
            if ((mode & 2) == 0) {
                return 0;
            }
            this.outputFused = true;
            return 2;
        }

        public void clear() {
            this.f12it = null;
        }

        public boolean isEmpty() {
            return this.f12it == null;
        }

        public R poll() throws Exception {
            Iterator<? extends R> iter = this.f12it;
            if (iter == null) {
                return null;
            }
            R v = ObjectHelper.requireNonNull(iter.next(), "The iterator returned a null value");
            if (iter.hasNext()) {
                return v;
            }
            this.f12it = null;
            return v;
        }
    }
}
