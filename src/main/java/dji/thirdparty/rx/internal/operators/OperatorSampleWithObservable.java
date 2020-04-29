package dji.thirdparty.rx.internal.operators;

import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.Subscriber;
import dji.thirdparty.rx.Subscription;
import dji.thirdparty.rx.observers.SerializedSubscriber;
import java.util.concurrent.atomic.AtomicReference;

public final class OperatorSampleWithObservable<T, U> implements Observable.Operator<T, T> {
    static final Object EMPTY_TOKEN = new Object();
    final Observable<U> sampler;

    public /* bridge */ /* synthetic */ Object call(Object x0) {
        return call((Subscriber) ((Subscriber) x0));
    }

    public OperatorSampleWithObservable(Observable<U> sampler2) {
        this.sampler = sampler2;
    }

    public Subscriber<? super T> call(Subscriber<? super T> child) {
        final SerializedSubscriber<T> s = new SerializedSubscriber<>(child);
        final AtomicReference<Object> value = new AtomicReference<>(EMPTY_TOKEN);
        final AtomicReference<Subscription> main = new AtomicReference<>();
        final Subscriber<U> samplerSub = new Subscriber<U>() {
            /* class dji.thirdparty.rx.internal.operators.OperatorSampleWithObservable.AnonymousClass1 */

            public void onNext(U u) {
                T localValue = value.getAndSet(OperatorSampleWithObservable.EMPTY_TOKEN);
                if (localValue != OperatorSampleWithObservable.EMPTY_TOKEN) {
                    s.onNext(localValue);
                }
            }

            public void onError(Throwable e) {
                s.onError(e);
                ((Subscription) main.get()).unsubscribe();
            }

            public void onCompleted() {
                onNext(null);
                s.onCompleted();
                ((Subscription) main.get()).unsubscribe();
            }
        };
        Subscriber<T> result = new Subscriber<T>() {
            /* class dji.thirdparty.rx.internal.operators.OperatorSampleWithObservable.AnonymousClass2 */

            public void onNext(T t) {
                value.set(t);
            }

            public void onError(Throwable e) {
                s.onError(e);
                samplerSub.unsubscribe();
            }

            public void onCompleted() {
                samplerSub.onNext(null);
                s.onCompleted();
                samplerSub.unsubscribe();
            }
        };
        main.lazySet(result);
        child.add(result);
        child.add(samplerSub);
        this.sampler.unsafeSubscribe(samplerSub);
        return result;
    }
}
