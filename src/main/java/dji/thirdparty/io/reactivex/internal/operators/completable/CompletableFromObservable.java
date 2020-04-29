package dji.thirdparty.io.reactivex.internal.operators.completable;

import dji.thirdparty.io.reactivex.Completable;
import dji.thirdparty.io.reactivex.CompletableObserver;
import dji.thirdparty.io.reactivex.ObservableSource;
import dji.thirdparty.io.reactivex.Observer;
import dji.thirdparty.io.reactivex.disposables.Disposable;

public final class CompletableFromObservable<T> extends Completable {
    final ObservableSource<T> observable;

    public CompletableFromObservable(ObservableSource<T> observable2) {
        this.observable = observable2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(CompletableObserver s) {
        this.observable.subscribe(new CompletableFromObservableObserver(s));
    }

    static final class CompletableFromObservableObserver<T> implements Observer<T> {
        final CompletableObserver co;

        CompletableFromObservableObserver(CompletableObserver co2) {
            this.co = co2;
        }

        public void onSubscribe(Disposable d) {
            this.co.onSubscribe(d);
        }

        public void onNext(T t) {
        }

        public void onError(Throwable e) {
            this.co.onError(e);
        }

        public void onComplete() {
            this.co.onComplete();
        }
    }
}
