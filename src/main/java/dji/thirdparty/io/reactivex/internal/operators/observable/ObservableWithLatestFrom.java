package dji.thirdparty.io.reactivex.internal.operators.observable;

import dji.thirdparty.io.reactivex.ObservableSource;
import dji.thirdparty.io.reactivex.Observer;
import dji.thirdparty.io.reactivex.disposables.Disposable;
import dji.thirdparty.io.reactivex.exceptions.Exceptions;
import dji.thirdparty.io.reactivex.functions.BiFunction;
import dji.thirdparty.io.reactivex.internal.disposables.DisposableHelper;
import dji.thirdparty.io.reactivex.observers.SerializedObserver;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservableWithLatestFrom<T, U, R> extends AbstractObservableWithUpstream<T, R> {
    final BiFunction<? super T, ? super U, ? extends R> combiner;
    final ObservableSource<? extends U> other;

    public ObservableWithLatestFrom(ObservableSource<T> source, BiFunction<? super T, ? super U, ? extends R> combiner2, ObservableSource<? extends U> other2) {
        super(source);
        this.combiner = combiner2;
        this.other = other2;
    }

    public void subscribeActual(Observer<? super R> t) {
        final WithLatestFromObserver<T, U, R> wlf = new WithLatestFromObserver<>(new SerializedObserver<>(t), this.combiner);
        t.onSubscribe(wlf);
        this.other.subscribe(new Observer<U>() {
            /* class dji.thirdparty.io.reactivex.internal.operators.observable.ObservableWithLatestFrom.AnonymousClass1 */

            public void onSubscribe(Disposable s) {
                wlf.setOther(s);
            }

            public void onNext(U t) {
                wlf.lazySet(t);
            }

            public void onError(Throwable t) {
                wlf.otherError(t);
            }

            public void onComplete() {
            }
        });
        this.source.subscribe(wlf);
    }

    static final class WithLatestFromObserver<T, U, R> extends AtomicReference<U> implements Observer<T>, Disposable {
        private static final long serialVersionUID = -312246233408980075L;
        final Observer<? super R> actual;
        final BiFunction<? super T, ? super U, ? extends R> combiner;
        final AtomicReference<Disposable> other = new AtomicReference<>();
        final AtomicReference<Disposable> s = new AtomicReference<>();

        WithLatestFromObserver(Observer<? super R> actual2, BiFunction<? super T, ? super U, ? extends R> combiner2) {
            this.actual = actual2;
            this.combiner = combiner2;
        }

        public void onSubscribe(Disposable s2) {
            DisposableHelper.setOnce(this.s, s2);
        }

        public void onNext(T t) {
            U u = get();
            if (u != null) {
                try {
                    this.actual.onNext(this.combiner.apply(t, u));
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    dispose();
                    this.actual.onError(e);
                }
            }
        }

        public void onError(Throwable t) {
            DisposableHelper.dispose(this.other);
            this.actual.onError(t);
        }

        public void onComplete() {
            DisposableHelper.dispose(this.other);
            this.actual.onComplete();
        }

        public void dispose() {
            DisposableHelper.dispose(this.s);
            DisposableHelper.dispose(this.other);
        }

        public boolean isDisposed() {
            return DisposableHelper.isDisposed(this.s.get());
        }

        public boolean setOther(Disposable o) {
            return DisposableHelper.setOnce(this.other, o);
        }

        public void otherError(Throwable e) {
            DisposableHelper.dispose(this.s);
            this.actual.onError(e);
        }
    }
}
