package dji.thirdparty.io.reactivex.internal.operators.completable;

import dji.thirdparty.io.reactivex.Completable;
import dji.thirdparty.io.reactivex.CompletableObserver;
import dji.thirdparty.io.reactivex.SingleObserver;
import dji.thirdparty.io.reactivex.SingleSource;
import dji.thirdparty.io.reactivex.disposables.Disposable;

public final class CompletableFromSingle<T> extends Completable {
    final SingleSource<T> single;

    public CompletableFromSingle(SingleSource<T> single2) {
        this.single = single2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(CompletableObserver s) {
        this.single.subscribe(new CompletableFromSingleObserver(s));
    }

    static final class CompletableFromSingleObserver<T> implements SingleObserver<T> {
        final CompletableObserver co;

        CompletableFromSingleObserver(CompletableObserver co2) {
            this.co = co2;
        }

        public void onError(Throwable e) {
            this.co.onError(e);
        }

        public void onSubscribe(Disposable d) {
            this.co.onSubscribe(d);
        }

        public void onSuccess(T t) {
            this.co.onComplete();
        }
    }
}
