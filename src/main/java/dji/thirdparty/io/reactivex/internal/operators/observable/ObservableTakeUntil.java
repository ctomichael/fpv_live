package dji.thirdparty.io.reactivex.internal.operators.observable;

import dji.thirdparty.io.reactivex.ObservableSource;
import dji.thirdparty.io.reactivex.Observer;
import dji.thirdparty.io.reactivex.disposables.Disposable;
import dji.thirdparty.io.reactivex.internal.disposables.ArrayCompositeDisposable;
import dji.thirdparty.io.reactivex.internal.disposables.DisposableHelper;
import dji.thirdparty.io.reactivex.observers.SerializedObserver;
import java.util.concurrent.atomic.AtomicBoolean;

public final class ObservableTakeUntil<T, U> extends AbstractObservableWithUpstream<T, T> {
    final ObservableSource<? extends U> other;

    public ObservableTakeUntil(ObservableSource<T> source, ObservableSource<? extends U> other2) {
        super(source);
        this.other = other2;
    }

    public void subscribeActual(Observer<? super T> child) {
        final SerializedObserver<T> serial = new SerializedObserver<>(child);
        final ArrayCompositeDisposable frc = new ArrayCompositeDisposable(2);
        TakeUntilObserver<T> tus = new TakeUntilObserver<>(serial, frc);
        child.onSubscribe(frc);
        this.other.subscribe(new Observer<U>() {
            /* class dji.thirdparty.io.reactivex.internal.operators.observable.ObservableTakeUntil.AnonymousClass1 */

            public void onSubscribe(Disposable s) {
                frc.setResource(1, s);
            }

            public void onNext(U u) {
                frc.dispose();
                serial.onComplete();
            }

            public void onError(Throwable t) {
                frc.dispose();
                serial.onError(t);
            }

            public void onComplete() {
                frc.dispose();
                serial.onComplete();
            }
        });
        this.source.subscribe(tus);
    }

    static final class TakeUntilObserver<T> extends AtomicBoolean implements Observer<T> {
        private static final long serialVersionUID = 3451719290311127173L;
        final Observer<? super T> actual;
        final ArrayCompositeDisposable frc;
        Disposable s;

        TakeUntilObserver(Observer<? super T> actual2, ArrayCompositeDisposable frc2) {
            this.actual = actual2;
            this.frc = frc2;
        }

        public void onSubscribe(Disposable s2) {
            if (DisposableHelper.validate(this.s, s2)) {
                this.s = s2;
                this.frc.setResource(0, s2);
            }
        }

        public void onNext(T t) {
            this.actual.onNext(t);
        }

        public void onError(Throwable t) {
            this.frc.dispose();
            this.actual.onError(t);
        }

        public void onComplete() {
            this.frc.dispose();
            this.actual.onComplete();
        }
    }
}
