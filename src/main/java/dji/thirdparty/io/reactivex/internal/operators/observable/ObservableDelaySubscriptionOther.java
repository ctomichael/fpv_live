package dji.thirdparty.io.reactivex.internal.operators.observable;

import dji.thirdparty.io.reactivex.Observable;
import dji.thirdparty.io.reactivex.ObservableSource;
import dji.thirdparty.io.reactivex.Observer;
import dji.thirdparty.io.reactivex.disposables.Disposable;
import dji.thirdparty.io.reactivex.internal.disposables.SequentialDisposable;
import dji.thirdparty.io.reactivex.plugins.RxJavaPlugins;

public final class ObservableDelaySubscriptionOther<T, U> extends Observable<T> {
    final ObservableSource<? extends T> main;
    final ObservableSource<U> other;

    public ObservableDelaySubscriptionOther(ObservableSource<? extends T> main2, ObservableSource<U> other2) {
        this.main = main2;
        this.other = other2;
    }

    public void subscribeActual(final Observer<? super T> child) {
        final SequentialDisposable serial = new SequentialDisposable();
        child.onSubscribe(serial);
        this.other.subscribe(new Observer<U>() {
            /* class dji.thirdparty.io.reactivex.internal.operators.observable.ObservableDelaySubscriptionOther.AnonymousClass1 */
            boolean done;

            public void onSubscribe(Disposable d) {
                serial.update(d);
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
                child.onError(e);
            }

            public void onComplete() {
                if (!this.done) {
                    this.done = true;
                    ObservableDelaySubscriptionOther.this.main.subscribe(new Observer<T>() {
                        /* class dji.thirdparty.io.reactivex.internal.operators.observable.ObservableDelaySubscriptionOther.AnonymousClass1.AnonymousClass1 */

                        public void onSubscribe(Disposable d) {
                            serial.update(d);
                        }

                        public void onNext(T value) {
                            child.onNext(value);
                        }

                        public void onError(Throwable e) {
                            child.onError(e);
                        }

                        public void onComplete() {
                            child.onComplete();
                        }
                    });
                }
            }
        });
    }
}
