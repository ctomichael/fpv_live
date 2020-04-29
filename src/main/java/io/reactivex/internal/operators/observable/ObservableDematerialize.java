package io.reactivex.internal.operators.observable;

import io.reactivex.Notification;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.plugins.RxJavaPlugins;

public final class ObservableDematerialize<T, R> extends AbstractObservableWithUpstream<T, R> {
    final Function<? super T, ? extends Notification<R>> selector;

    public ObservableDematerialize(ObservableSource<T> source, Function<? super T, ? extends Notification<R>> selector2) {
        super(source);
        this.selector = selector2;
    }

    public void subscribeActual(Observer<? super R> observer) {
        this.source.subscribe(new DematerializeObserver(observer, this.selector));
    }

    static final class DematerializeObserver<T, R> implements Observer<T>, Disposable {
        boolean done;
        final Observer<? super R> downstream;
        final Function<? super T, ? extends Notification<R>> selector;
        Disposable upstream;

        DematerializeObserver(Observer<? super R> downstream2, Function<? super T, ? extends Notification<R>> selector2) {
            this.downstream = downstream2;
            this.selector = selector2;
        }

        public void onSubscribe(Disposable d) {
            if (DisposableHelper.validate(this.upstream, d)) {
                this.upstream = d;
                this.downstream.onSubscribe(this);
            }
        }

        public void dispose() {
            this.upstream.dispose();
        }

        public boolean isDisposed() {
            return this.upstream.isDisposed();
        }

        public void onNext(T item) {
            if (!this.done) {
                try {
                    Notification<R> notification = (Notification) ObjectHelper.requireNonNull(this.selector.apply(item), "The selector returned a null Notification");
                    if (notification.isOnError()) {
                        this.upstream.dispose();
                        onError(notification.getError());
                    } else if (notification.isOnComplete()) {
                        this.upstream.dispose();
                        onComplete();
                    } else {
                        this.downstream.onNext(notification.getValue());
                    }
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    this.upstream.dispose();
                    onError(ex);
                }
            } else if (item instanceof Notification) {
                Notification<?> notification2 = item;
                if (notification2.isOnError()) {
                    RxJavaPlugins.onError(notification2.getError());
                }
            }
        }

        public void onError(Throwable t) {
            if (this.done) {
                RxJavaPlugins.onError(t);
                return;
            }
            this.done = true;
            this.downstream.onError(t);
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                this.downstream.onComplete();
            }
        }
    }
}
