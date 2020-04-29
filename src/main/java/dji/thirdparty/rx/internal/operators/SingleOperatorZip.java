package dji.thirdparty.rx.internal.operators;

import dji.thirdparty.rx.Single;
import dji.thirdparty.rx.SingleSubscriber;
import dji.thirdparty.rx.exceptions.Exceptions;
import dji.thirdparty.rx.functions.FuncN;
import dji.thirdparty.rx.plugins.RxJavaPlugins;
import dji.thirdparty.rx.subscriptions.CompositeSubscription;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class SingleOperatorZip {
    public static <T, R> Single<R> zip(final Single<? extends T>[] singles, final FuncN<? extends R> zipper) {
        return Single.create(new Single.OnSubscribe<R>() {
            /* class dji.thirdparty.rx.internal.operators.SingleOperatorZip.AnonymousClass1 */

            public /* bridge */ /* synthetic */ void call(Object x0) {
                call((SingleSubscriber) ((SingleSubscriber) x0));
            }

            public void call(SingleSubscriber<? super R> subscriber) {
                if (singles.length == 0) {
                    subscriber.onError(new NoSuchElementException("Can't zip 0 Singles."));
                    return;
                }
                final AtomicInteger wip = new AtomicInteger(singles.length);
                final AtomicBoolean once = new AtomicBoolean();
                final Object[] values = new Object[singles.length];
                CompositeSubscription compositeSubscription = new CompositeSubscription();
                subscriber.add(compositeSubscription);
                int i = 0;
                while (i < singles.length && !compositeSubscription.isUnsubscribed() && !once.get()) {
                    final int j = i;
                    final SingleSubscriber<? super R> singleSubscriber = subscriber;
                    SingleSubscriber<T> singleSubscriber2 = new SingleSubscriber<T>() {
                        /* class dji.thirdparty.rx.internal.operators.SingleOperatorZip.AnonymousClass1.AnonymousClass1 */

                        public void onSuccess(T value) {
                            values[j] = value;
                            if (wip.decrementAndGet() == 0) {
                                try {
                                    singleSubscriber.onSuccess(zipper.call(values));
                                } catch (Throwable e) {
                                    Exceptions.throwIfFatal(e);
                                    onError(e);
                                }
                            }
                        }

                        public void onError(Throwable error) {
                            if (once.compareAndSet(false, true)) {
                                singleSubscriber.onError(error);
                            } else {
                                RxJavaPlugins.getInstance().getErrorHandler().handleError(error);
                            }
                        }
                    };
                    compositeSubscription.add(singleSubscriber2);
                    if (!compositeSubscription.isUnsubscribed() && !once.get()) {
                        singles[i].subscribe(singleSubscriber2);
                        i++;
                    } else {
                        return;
                    }
                }
            }
        });
    }
}
