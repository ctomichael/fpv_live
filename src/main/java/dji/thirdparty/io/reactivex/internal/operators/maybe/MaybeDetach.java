package dji.thirdparty.io.reactivex.internal.operators.maybe;

import dji.thirdparty.io.reactivex.MaybeObserver;
import dji.thirdparty.io.reactivex.MaybeSource;
import dji.thirdparty.io.reactivex.disposables.Disposable;
import dji.thirdparty.io.reactivex.internal.disposables.DisposableHelper;

public final class MaybeDetach<T> extends AbstractMaybeWithUpstream<T, T> {
    public MaybeDetach(MaybeSource<T> source) {
        super(source);
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver<? super T> observer) {
        this.source.subscribe(new DetachMaybeObserver(observer));
    }

    static final class DetachMaybeObserver<T> implements MaybeObserver<T>, Disposable {
        MaybeObserver<? super T> actual;
        Disposable d;

        DetachMaybeObserver(MaybeObserver<? super T> actual2) {
            this.actual = actual2;
        }

        public void dispose() {
            this.actual = null;
            this.d.dispose();
            this.d = DisposableHelper.DISPOSED;
        }

        public boolean isDisposed() {
            return this.d.isDisposed();
        }

        public void onSubscribe(Disposable d2) {
            if (DisposableHelper.validate(this.d, d2)) {
                this.d = d2;
                this.actual.onSubscribe(this);
            }
        }

        public void onSuccess(T value) {
            this.d = DisposableHelper.DISPOSED;
            MaybeObserver<? super T> a = this.actual;
            if (a != null) {
                a.onSuccess(value);
            }
        }

        public void onError(Throwable e) {
            this.d = DisposableHelper.DISPOSED;
            MaybeObserver<? super T> a = this.actual;
            if (a != null) {
                a.onError(e);
            }
        }

        public void onComplete() {
            this.d = DisposableHelper.DISPOSED;
            MaybeObserver<? super T> a = this.actual;
            if (a != null) {
                a.onComplete();
            }
        }
    }
}
