package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.SequentialDisposable;
import io.reactivex.plugins.RxJavaPlugins;

public final class ObservableDelaySubscriptionOther<T, U> extends Observable<T> {
    final ObservableSource<? extends T> main;
    final ObservableSource<U> other;

    public ObservableDelaySubscriptionOther(ObservableSource<? extends T> main2, ObservableSource<U> other2) {
        this.main = main2;
        this.other = other2;
    }

    public void subscribeActual(Observer<? super T> child) {
        SequentialDisposable serial = new SequentialDisposable();
        child.onSubscribe(serial);
        this.other.subscribe(new DelayObserver(serial, child));
    }

    final class DelayObserver implements Observer<U> {
        final Observer<? super T> child;
        boolean done;
        final SequentialDisposable serial;

        DelayObserver(SequentialDisposable serial2, Observer<? super T> child2) {
            this.serial = serial2;
            this.child = child2;
        }

        public void onSubscribe(Disposable d) {
            this.serial.update(d);
        }

        public void onNext(U u) {
            onComplete();
        }

        public void onError(Throwable e) {
            if (this.done) {
                RxJavaPlugins.onError(e);
                return;
            }
            this.done = true;
            this.child.onError(e);
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                ObservableDelaySubscriptionOther.this.main.subscribe(new OnComplete());
            }
        }

        final class OnComplete implements Observer<T> {
            OnComplete() {
            }

            public void onSubscribe(Disposable d) {
                DelayObserver.this.serial.update(d);
            }

            public void onNext(T value) {
                DelayObserver.this.child.onNext(value);
            }

            public void onError(Throwable e) {
                DelayObserver.this.child.onError(e);
            }

            public void onComplete() {
                DelayObserver.this.child.onComplete();
            }
        }
    }
}
