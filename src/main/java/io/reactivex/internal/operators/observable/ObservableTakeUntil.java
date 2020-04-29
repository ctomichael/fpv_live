package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.util.AtomicThrowable;
import io.reactivex.internal.util.HalfSerializer;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservableTakeUntil<T, U> extends AbstractObservableWithUpstream<T, T> {
    final ObservableSource<? extends U> other;

    public ObservableTakeUntil(ObservableSource<T> source, ObservableSource<? extends U> other2) {
        super(source);
        this.other = other2;
    }

    public void subscribeActual(Observer<? super T> child) {
        TakeUntilMainObserver<T, U> parent = new TakeUntilMainObserver<>(child);
        child.onSubscribe(parent);
        this.other.subscribe(parent.otherObserver);
        this.source.subscribe(parent);
    }

    static final class TakeUntilMainObserver<T, U> extends AtomicInteger implements Observer<T>, Disposable {
        private static final long serialVersionUID = 1418547743690811973L;
        final Observer<? super T> downstream;
        final AtomicThrowable error = new AtomicThrowable();
        final TakeUntilMainObserver<T, U>.OtherObserver otherObserver = new OtherObserver();
        final AtomicReference<Disposable> upstream = new AtomicReference<>();

        TakeUntilMainObserver(Observer<? super T> downstream2) {
            this.downstream = downstream2;
        }

        public void dispose() {
            DisposableHelper.dispose(this.upstream);
            DisposableHelper.dispose(this.otherObserver);
        }

        public boolean isDisposed() {
            return DisposableHelper.isDisposed(this.upstream.get());
        }

        public void onSubscribe(Disposable d) {
            DisposableHelper.setOnce(this.upstream, d);
        }

        public void onNext(T t) {
            HalfSerializer.onNext(this.downstream, t, this, this.error);
        }

        public void onError(Throwable e) {
            DisposableHelper.dispose(this.otherObserver);
            HalfSerializer.onError(this.downstream, e, this, this.error);
        }

        public void onComplete() {
            DisposableHelper.dispose(this.otherObserver);
            HalfSerializer.onComplete(this.downstream, this, this.error);
        }

        /* access modifiers changed from: package-private */
        public void otherError(Throwable e) {
            DisposableHelper.dispose(this.upstream);
            HalfSerializer.onError(this.downstream, e, this, this.error);
        }

        /* access modifiers changed from: package-private */
        public void otherComplete() {
            DisposableHelper.dispose(this.upstream);
            HalfSerializer.onComplete(this.downstream, this, this.error);
        }

        final class OtherObserver extends AtomicReference<Disposable> implements Observer<U> {
            private static final long serialVersionUID = -8693423678067375039L;

            OtherObserver() {
            }

            public void onSubscribe(Disposable d) {
                DisposableHelper.setOnce(this, d);
            }

            public void onNext(U u) {
                DisposableHelper.dispose(this);
                TakeUntilMainObserver.this.otherComplete();
            }

            public void onError(Throwable e) {
                TakeUntilMainObserver.this.otherError(e);
            }

            public void onComplete() {
                TakeUntilMainObserver.this.otherComplete();
            }
        }
    }
}
