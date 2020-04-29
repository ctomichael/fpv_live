package dji.thirdparty.io.reactivex.internal.operators.flowable;

import dji.thirdparty.io.reactivex.Flowable;
import dji.thirdparty.io.reactivex.Observable;
import dji.thirdparty.io.reactivex.Observer;
import dji.thirdparty.io.reactivex.disposables.Disposable;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableFromObservable<T> extends Flowable<T> {
    private final Observable<T> upstream;

    public FlowableFromObservable(Observable<T> upstream2) {
        this.upstream = upstream2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> s) {
        this.upstream.subscribe(new SubscriberObserver(s));
    }

    static class SubscriberObserver<T> implements Observer<T>, Subscription {
        private Disposable d;
        private final Subscriber<? super T> s;

        SubscriberObserver(Subscriber<? super T> s2) {
            this.s = s2;
        }

        public void onComplete() {
            this.s.onComplete();
        }

        public void onError(Throwable e) {
            this.s.onError(e);
        }

        public void onNext(T value) {
            this.s.onNext(value);
        }

        public void onSubscribe(Disposable d2) {
            this.d = d2;
            this.s.onSubscribe(this);
        }

        public void cancel() {
            this.d.dispose();
        }

        public void request(long n) {
        }
    }
}
