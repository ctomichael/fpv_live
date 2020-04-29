package dji.thirdparty.rx.internal.operators;

import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.Single;
import dji.thirdparty.rx.SingleSubscriber;
import dji.thirdparty.rx.Subscriber;
import java.util.NoSuchElementException;

public class OnSubscribeSingle<T> implements Single.OnSubscribe<T> {
    private final Observable<T> observable;

    public /* bridge */ /* synthetic */ void call(Object x0) {
        call((SingleSubscriber) ((SingleSubscriber) x0));
    }

    public OnSubscribeSingle(Observable<T> observable2) {
        this.observable = observable2;
    }

    public void call(final SingleSubscriber<? super T> child) {
        Subscriber<T> parent = new Subscriber<T>() {
            /* class dji.thirdparty.rx.internal.operators.OnSubscribeSingle.AnonymousClass1 */
            private T emission = null;
            private boolean emittedTooMany = false;
            private boolean itemEmitted = false;

            public void onStart() {
                request(2);
            }

            public void onCompleted() {
                if (!this.emittedTooMany) {
                    if (this.itemEmitted) {
                        child.onSuccess(this.emission);
                    } else {
                        child.onError(new NoSuchElementException("Observable emitted no items"));
                    }
                }
            }

            public void onError(Throwable e) {
                child.onError(e);
                unsubscribe();
            }

            public void onNext(T t) {
                if (this.itemEmitted) {
                    this.emittedTooMany = true;
                    child.onError(new IllegalArgumentException("Observable emitted too many elements"));
                    unsubscribe();
                    return;
                }
                this.itemEmitted = true;
                this.emission = t;
            }
        };
        child.add(parent);
        this.observable.unsafeSubscribe(parent);
    }

    public static <T> OnSubscribeSingle<T> create(Observable<T> observable2) {
        return new OnSubscribeSingle<>(observable2);
    }
}
