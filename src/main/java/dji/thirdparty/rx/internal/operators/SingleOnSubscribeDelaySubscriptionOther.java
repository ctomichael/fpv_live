package dji.thirdparty.rx.internal.operators;

import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.Single;
import dji.thirdparty.rx.SingleSubscriber;
import dji.thirdparty.rx.Subscriber;
import dji.thirdparty.rx.plugins.RxJavaPlugins;
import dji.thirdparty.rx.subscriptions.SerialSubscription;

public final class SingleOnSubscribeDelaySubscriptionOther<T> implements Single.OnSubscribe<T> {
    final Single<? extends T> main;
    final Observable<?> other;

    public /* bridge */ /* synthetic */ void call(Object x0) {
        call((SingleSubscriber) ((SingleSubscriber) x0));
    }

    public SingleOnSubscribeDelaySubscriptionOther(Single<? extends T> main2, Observable<?> other2) {
        this.main = main2;
        this.other = other2;
    }

    public void call(final SingleSubscriber<? super T> subscriber) {
        final SingleSubscriber<T> child = new SingleSubscriber<T>() {
            /* class dji.thirdparty.rx.internal.operators.SingleOnSubscribeDelaySubscriptionOther.AnonymousClass1 */

            public void onSuccess(T value) {
                subscriber.onSuccess(value);
            }

            public void onError(Throwable error) {
                subscriber.onError(error);
            }
        };
        final SerialSubscription serial = new SerialSubscription();
        subscriber.add(serial);
        Subscriber<Object> otherSubscriber = new Subscriber<Object>() {
            /* class dji.thirdparty.rx.internal.operators.SingleOnSubscribeDelaySubscriptionOther.AnonymousClass2 */
            boolean done;

            public void onNext(Object t) {
                onCompleted();
            }

            public void onError(Throwable e) {
                if (this.done) {
                    RxJavaPlugins.getInstance().getErrorHandler().handleError(e);
                    return;
                }
                this.done = true;
                child.onError(e);
            }

            public void onCompleted() {
                if (!this.done) {
                    this.done = true;
                    serial.set(child);
                    SingleOnSubscribeDelaySubscriptionOther.this.main.subscribe(child);
                }
            }
        };
        serial.set(otherSubscriber);
        this.other.subscribe((Subscriber) otherSubscriber);
    }
}
