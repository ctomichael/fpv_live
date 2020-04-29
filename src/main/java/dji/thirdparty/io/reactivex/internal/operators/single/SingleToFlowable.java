package dji.thirdparty.io.reactivex.internal.operators.single;

import dji.thirdparty.io.reactivex.Flowable;
import dji.thirdparty.io.reactivex.SingleObserver;
import dji.thirdparty.io.reactivex.SingleSource;
import dji.thirdparty.io.reactivex.disposables.Disposable;
import dji.thirdparty.io.reactivex.internal.disposables.DisposableHelper;
import dji.thirdparty.io.reactivex.internal.subscriptions.DeferredScalarSubscription;
import org.reactivestreams.Subscriber;

public final class SingleToFlowable<T> extends Flowable<T> {
    final SingleSource<? extends T> source;

    public SingleToFlowable(SingleSource<? extends T> source2) {
        this.source = source2;
    }

    public void subscribeActual(Subscriber<? super T> s) {
        this.source.subscribe(new SingleToFlowableObserver(s));
    }

    static final class SingleToFlowableObserver<T> extends DeferredScalarSubscription<T> implements SingleObserver<T> {
        private static final long serialVersionUID = 187782011903685568L;
        Disposable d;

        SingleToFlowableObserver(Subscriber<? super T> actual) {
            super(actual);
        }

        public void onSubscribe(Disposable d2) {
            if (DisposableHelper.validate(this.d, d2)) {
                this.d = d2;
                this.actual.onSubscribe(this);
            }
        }

        public void onSuccess(T value) {
            complete(value);
        }

        public void onError(Throwable e) {
            this.actual.onError(e);
        }

        public void cancel() {
            super.cancel();
            this.d.dispose();
        }
    }
}
