package dji.thirdparty.io.reactivex.internal.operators.observable;

import dji.thirdparty.io.reactivex.ObservableSource;
import dji.thirdparty.io.reactivex.Observer;
import dji.thirdparty.io.reactivex.disposables.Disposable;
import dji.thirdparty.io.reactivex.internal.disposables.DisposableHelper;

public final class ObservableTakeLastOne<T> extends AbstractObservableWithUpstream<T, T> {
    public ObservableTakeLastOne(ObservableSource<T> source) {
        super(source);
    }

    public void subscribeActual(Observer<? super T> s) {
        this.source.subscribe(new TakeLastOneObserver(s));
    }

    static final class TakeLastOneObserver<T> implements Observer<T>, Disposable {
        final Observer<? super T> actual;
        Disposable s;
        T value;

        TakeLastOneObserver(Observer<? super T> actual2) {
            this.actual = actual2;
        }

        public void onSubscribe(Disposable s2) {
            if (DisposableHelper.validate(this.s, s2)) {
                this.s = s2;
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            this.value = t;
        }

        public void onError(Throwable t) {
            this.value = null;
            this.actual.onError(t);
        }

        public void onComplete() {
            emit();
        }

        /* access modifiers changed from: package-private */
        public void emit() {
            T v = this.value;
            if (v != null) {
                this.value = null;
                this.actual.onNext(v);
            }
            this.actual.onComplete();
        }

        public void dispose() {
            this.value = null;
            this.s.dispose();
        }

        public boolean isDisposed() {
            return this.s.isDisposed();
        }
    }
}
