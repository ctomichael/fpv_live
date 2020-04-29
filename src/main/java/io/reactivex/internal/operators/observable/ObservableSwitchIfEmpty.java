package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.SequentialDisposable;

public final class ObservableSwitchIfEmpty<T> extends AbstractObservableWithUpstream<T, T> {
    final ObservableSource<? extends T> other;

    public ObservableSwitchIfEmpty(ObservableSource<T> source, ObservableSource<? extends T> other2) {
        super(source);
        this.other = other2;
    }

    public void subscribeActual(Observer<? super T> t) {
        SwitchIfEmptyObserver<T> parent = new SwitchIfEmptyObserver<>(t, this.other);
        t.onSubscribe(parent.arbiter);
        this.source.subscribe(parent);
    }

    static final class SwitchIfEmptyObserver<T> implements Observer<T> {
        final SequentialDisposable arbiter = new SequentialDisposable();
        final Observer<? super T> downstream;
        boolean empty = true;
        final ObservableSource<? extends T> other;

        SwitchIfEmptyObserver(Observer<? super T> actual, ObservableSource<? extends T> other2) {
            this.downstream = actual;
            this.other = other2;
        }

        public void onSubscribe(Disposable d) {
            this.arbiter.update(d);
        }

        public void onNext(T t) {
            if (this.empty) {
                this.empty = false;
            }
            this.downstream.onNext(t);
        }

        public void onError(Throwable t) {
            this.downstream.onError(t);
        }

        public void onComplete() {
            if (this.empty) {
                this.empty = false;
                this.other.subscribe(this);
                return;
            }
            this.downstream.onComplete();
        }
    }
}
